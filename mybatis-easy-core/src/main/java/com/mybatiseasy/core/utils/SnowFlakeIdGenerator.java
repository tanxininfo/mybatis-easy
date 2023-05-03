/*
 * Copyright (c) 2023-2033, 杭州坦信科技有限公司 (https://www.mybatis-easy.com).
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.mybatiseasy.core.utils;

/**
 * 雪花算法
 *
 * 雪花算法原理就是生成一个的64位比特位的 long 类型的唯一 id。
 *
 * 最高1位固定值0，因为生成的 id 是正整数，如果是1就是负数了。
 * 接下来41位存储毫秒级时间戳，2^41/(1000606024365)=69，大概可以使用69年。
 * 再接下10位存储机器码，包括5位 datacenterId 和5位 workerId。最多可以部署2^10=1024台机器。
 * 最后12位存储序列号。同一毫秒时间戳时，通过这个递增的序列号来区分。即对于同一台机器而言，同一毫秒时间戳下，可以生成2^12=4096个不重复 id。
 *
 * 可以将雪花算法作为一个单独的服务进行部署，然后需要全局唯一 id 的系统，请求雪花算法服务获取 id 即可。
 * 对于每一个雪花算法服务，需要先指定10位的机器码，这个根据自身业务进行设定即可。例如机房号+机器号，机器号+服务号，或者是其他可区别标识的10位比特位的整数值都行。
 *
 * 作者：陈皮的JavaLib
 * 链接：<a href="https://juejin.cn/post/7082669476658806792">https://juejin.cn/post/7082669476658806792</a>
 * 来源：稀土掘金
 * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
 */
public class SnowFlakeIdGenerator {

    // 初始时间戳(纪年)，可用雪花算法服务上线时间戳的值
    // 1662290025163 2022-09-04 19:13:45
    private static final long INIT_EPOCH = 1662290025163L;

    // 记录最后使用的毫秒时间戳，主要用于判断是否同一毫秒，以及用于服务器时钟回拨判断
    private long lastTimeMillis = -1L;

    // dataCenterId占用的位数
    private static final long DATA_CENTER_ID_BITS = 5L;
    // dataCenterId占用5个比特位，最大值31
    // 0000000000000000000000000000000000000000000000000000000000011111
    private static final long MAX_DATA_CENTER_ID = ~(-1L << DATA_CENTER_ID_BITS);
    // datacenterId
    private final long datacenterId;

    // workId占用的位数
    private static final long WORKER_ID_BITS = 5L;
    // workId占用5个比特位，最大值31
    // 0000000000000000000000000000000000000000000000000000000000011111
    private static final long MAX_WORKER_ID = ~(-1L << WORKER_ID_BITS);
    // workId
    private final long workerId;

    // 最后11位，代表每毫秒内可产生最大序列号，即 2^12 - 1 = 4095
    private static final long SEQUENCE_BITS = 12L;
    // 掩码（最低12位为1，高位都为0），主要用于与自增后的序列号进行位与，如果值为0，则代表自增后的序列号超过了4095
    // 0000000000000000000000000000000000000000000000000000111111111111
    private static final long SEQUENCE_MASK = ~(-1L << SEQUENCE_BITS);
    // 同一毫秒内的最新序号，最大值可为 2^12 - 1 = 4095
    private long sequence;

    // workId位需要左移的位数 12
    private static final long WORK_ID_SHIFT = SEQUENCE_BITS;
    // dataCenterId位需要左移的位数 12+5
    private static final long DATA_CENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;
    // 时间戳需要左移的位数 12+5+5
    private static final long TIMESTAMP_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATA_CENTER_ID_BITS;


    public SnowFlakeIdGenerator() {
        this(1, 1);
    }

    public SnowFlakeIdGenerator(long datacenterId, long workerId) {

        // 检查datacenterId的合法值
        if (datacenterId < 0 || datacenterId > MAX_DATA_CENTER_ID) {
            throw new IllegalArgumentException(
                    String.format("datacenterId值必须大于0并且小于%d", MAX_DATA_CENTER_ID));
        }

        // 检查workId的合法值
        if (workerId < 0 || workerId > MAX_WORKER_ID) {
            throw new IllegalArgumentException(String.format("workId值必须大于0并且小于%d", MAX_WORKER_ID));
        }

        this.workerId = workerId;
        this.datacenterId = datacenterId;
    }

    /**
     * 通过雪花算法生成下一个id，注意这里使用synchronized同步
     *
     * @return 唯一id
     */
    public synchronized long nextId() {

        long currentTimeMillis = System.currentTimeMillis();

        // 当前时间小于上一次生成id使用的时间，可能出现服务器时钟回拨问题
        if (currentTimeMillis < lastTimeMillis) {
            throw new RuntimeException(
                    String.format("可能出现服务器时钟回拨问题，请检查服务器时间。当前服务器时间戳：%d，上一次使用时间戳：%d", currentTimeMillis,
                            lastTimeMillis));
        }

        if (currentTimeMillis == lastTimeMillis) { // 还是在同一毫秒内，则将序列号递增1，序列号最大值为4095

            // 序列号的最大值是4095，使用掩码（最低12位为1，高位都为0）进行位与运行后如果值为0，则自增后的序列号超过了4095
            // 那么就使用新的时间戳
            sequence = (sequence + 1) & SEQUENCE_MASK;
            if (sequence == 0) {
                currentTimeMillis = tilNextMillis(lastTimeMillis);
            }

        } else { // 不在同一毫秒内，则序列号重新从0开始，序列号最大值为4095
            sequence = 0;
        }

        // 记录最后一次使用的毫秒时间戳
        lastTimeMillis = currentTimeMillis;

        // 核心算法，将不同部分的数值移动到指定的位置，然后进行或运行
        return ((currentTimeMillis - INIT_EPOCH) << TIMESTAMP_SHIFT) | (datacenterId
                << DATA_CENTER_ID_SHIFT) | (workerId << WORK_ID_SHIFT) | sequence;
    }

    /**
     * 获取指定时间戳的接下来的时间戳，也可以说是下一毫秒
     *
     * @param lastTimeMillis 指定毫秒时间戳
     * @return 时间戳
     */
    private long tilNextMillis(long lastTimeMillis) {
        long currentTimeMillis = System.currentTimeMillis();
        while (currentTimeMillis <= lastTimeMillis) {
            currentTimeMillis = System.currentTimeMillis();
        }
        return currentTimeMillis;
    }
}