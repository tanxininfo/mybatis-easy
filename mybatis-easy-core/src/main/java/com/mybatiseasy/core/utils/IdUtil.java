package com.mybatiseasy.core.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * 用于parameter下标，避免重复。
 */
public class IdUtil {
    private static volatile int seq = 0;
    private static volatile long currentTimeMillis;

    public record UniqueId(long currentTimeMillis, String idStr, String compensate) {

        /**
         * 生成该id时候的时间戳
         */
        @Override
        public long currentTimeMillis() {
            return currentTimeMillis;
        }

        /**
         * 唯一ID
         */
        @Override
        public String idStr() {
            return idStr;
        }

        public Long id() {
            return Long.parseLong(idStr);
        }


        /**
         * 补全的位，该时间戳的第几个
         * @return
         */
        @Override
        public String compensate() {
            return compensate;
        }
    }

    /**
     * 生成一个唯一Id,长度为 mmss + compensateI
     *
     * compensateI 4 代表长度加4位 ，支持每毫秒4位的并发
     * @return
     */
    public static synchronized UniqueId uniqueId() {
        int compensateI = 4;
        final long ctm = System.currentTimeMillis();
        if (currentTimeMillis != ctm) {
            seq = new Random().nextInt(500);
            currentTimeMillis = ctm;
        }
        StringBuilder compensate = new StringBuilder();// 补零
        String sqlStr = String.valueOf(++seq);
        int length = sqlStr.length();
        compensate.append("0".repeat(Math.max(0, compensateI - length)));
        String currentTimeStr = (new SimpleDateFormat("ss")).format(new Date(ctm));
        String idStr = currentTimeStr + compensate + sqlStr;
        return new UniqueId(ctm, idStr, compensate.toString());
    }

}