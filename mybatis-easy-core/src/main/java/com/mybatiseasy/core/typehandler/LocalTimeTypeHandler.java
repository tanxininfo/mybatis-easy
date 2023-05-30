package com.mybatiseasy.core.typehandler;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class LocalTimeTypeHandler extends org.apache.ibatis.type.LocalTimeTypeHandler {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, LocalTime parameter, JdbcType jdbcType) throws SQLException {
        ps.setObject(i, parameter);
    }

    @Override
    public LocalTime getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String time = rs.getString(columnName);
        if(time == null) return null;
        return LocalTime.parse(time, getTimeFormatter(time));
    }

    @Override
    public LocalTime getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String time = rs.getString(columnIndex);
        if(time == null) return null;
        return LocalTime.parse(time, getTimeFormatter(time));
    }

    @Override
    public LocalTime getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String time = cs.getString(columnIndex);
        if(time == null) return null;
        return LocalTime.parse(time, getTimeFormatter(time));
    }

    private DateTimeFormatter getTimeFormatter(String time) {
        if (time.length() > 8) return DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
        else return DateTimeFormatter.ofPattern("HH:mm:ss");
    }
}
