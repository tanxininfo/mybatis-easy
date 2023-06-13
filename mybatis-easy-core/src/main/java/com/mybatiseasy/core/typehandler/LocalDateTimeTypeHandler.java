package com.mybatiseasy.core.typehandler;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.JdbcType;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeTypeHandler extends org.apache.ibatis.type.LocalDateTimeTypeHandler {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, LocalDateTime parameter, JdbcType jdbcType) throws SQLException {
        ps.setObject(i, parameter);
    }

    @Override
    public LocalDateTime getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String dateTime = rs.getString(columnName);
        if(dateTime == null) return null;
        return LocalDateTime.parse(dateTime, getDateTimeFormatter(dateTime));
    }

    @Override
    public LocalDateTime getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String dateTime = rs.getString(columnIndex);
        if(dateTime == null) return null;
        return LocalDateTime.parse(dateTime, getDateTimeFormatter(dateTime));
    }

    @Override
    public LocalDateTime getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String dateTime = cs.getString(columnIndex);
        if(dateTime == null) return null;
        return LocalDateTime.parse(dateTime, getDateTimeFormatter(dateTime));
    }

    private DateTimeFormatter getDateTimeFormatter(String dateTime) {
        if (dateTime.length() > 19) return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        else return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    }

}
