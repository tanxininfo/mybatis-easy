package com.mybatiseasy.core.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * 一个轻量的订单号生成，主要特点是看得到时间。
 * 来源于网上，作者不详。
 */
public class IdMakerUtil {
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
     * 生成一个唯一的订单序列,长度为 yyMMddHHmmss + compensateI
     *
     * @param compensateI 4 代表长度加4位 ，支持每毫秒4位的并发
     * @return
     */
    public static synchronized UniqueId uniqueId(int compensateI) {
        final long ctm = System.currentTimeMillis();
        if (currentTimeMillis != ctm) {
            seq = new Random().nextInt(500);
            currentTimeMillis = ctm;
        }
        StringBuilder compensate = new StringBuilder();// 补零
        String sqlStr = String.valueOf(++seq);
        int length = sqlStr.length();
        for (int i = 0; i < compensateI - length; i++) {
            compensate.append("0");
        }
        String currentTimeStr = (new SimpleDateFormat("yyMMddHHmmss")).format(new Date(ctm));
        String idStr = currentTimeStr + compensate + sqlStr;
        return new UniqueId(ctm, idStr, compensate.toString());
    }


    private static volatile long currentTimeMillis1;

    /**
     * 生成一个唯一序列yyyyMMddHHmmss，延迟一毫秒生成，多线程调用那么串行
     */
    public static String yyMMddHHmmss() {
        long ctm;
        synchronized (IdMakerUtil.class) {
            while (true) {
                ctm = System.currentTimeMillis();
                if (currentTimeMillis1 != ctm) {
                    currentTimeMillis1 = ctm;
                    break;
                }
            }
        }
        return (new SimpleDateFormat("yyMMddHHmmss")).format(new Date(ctm));
    }

}