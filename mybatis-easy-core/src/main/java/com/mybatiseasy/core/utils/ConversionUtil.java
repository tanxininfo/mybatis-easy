package com.mybatiseasy.core.utils;


import com.mybatiseasy.core.session.EntityFieldMap;
import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;


public class ConversionUtil {
    public static Object convertValue(Object value, Class<?> toType){
        Class<?> fromType = value.getClass();
        //log.info("field={}, fromType={}, toType={}", field.getName(), fromType, toType);
        if(fromType == Integer.class){
            return convertInteger((Integer) value, toType);
        }
        else if(fromType == BigInteger.class){
            return convertBigInteger((BigInteger) value, toType);
        }
        else if(fromType == LocalDateTime.class){
            return convertLocalDateTime((LocalDateTime) value, toType);
        }
        else if(fromType == LocalDate.class){
            return convertLocalDate((LocalDate) value, toType);
        }
        else if(fromType == LocalTime.class){
            return convertLocalTime((LocalTime) value, toType);
        }
        return value;
    }
    private static Object convertInteger(Integer value, Class<?> toType) {
        if (toType.equals(Long.class)) return Long.parseLong(value.toString());
        else if (toType.equals(Short.class)) return Short.parseShort(value.toString());
        return value;
    }

    private static Object convertBigInteger(BigInteger value, Class<?> toType) {
        if (toType.equals(Long.class)) return value.longValue();
        else if (toType.equals(Integer.class)) return value.intValue();
        return value;
    }

    private static Object convertLocalDateTime(LocalDateTime value, Class<?> toType) {
        if (toType.equals(LocalDate.class)) return value.toLocalDate();
        else if (toType.equals(LocalTime.class)) return value.toLocalTime();
        else if (toType.equals(Date.class)) return Date.from(value.atZone(ZoneId.systemDefault()).toInstant());
        else if (toType.equals(String.class)) {
             return value.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        return value;
    }

    private static Object convertLocalDate(LocalDate value, Class<?> toType){
        if(toType.equals(LocalDateTime.class)) return value.atStartOfDay();
        else if(toType.equals(Date.class)) return Date.from(value.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        return value;
    }

    private static Object convertLocalTime(LocalTime value, Class<?> toType){
        LocalDate now = LocalDate.now();
        if(toType.equals(Date.class)) return Date.from(value.atDate(LocalDate.of(now.getYear(), now.getMonth(), now.getDayOfMonth())).atZone(ZoneId.systemDefault()).toInstant());
        return value;
    }
}
