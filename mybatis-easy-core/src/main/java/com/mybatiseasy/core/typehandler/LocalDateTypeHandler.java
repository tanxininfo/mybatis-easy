package com.mybatiseasy.core.typehandler;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class LocalDateTypeHandler extends org.apache.ibatis.type.LocalDateTypeHandler {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, LocalDate parameter, JdbcType jdbcType) throws SQLException {
        ps.setObject(i, parameter);
    }

    @Override
    public LocalDate getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String date = rs.getString(columnName);
        if(date == null) return null;
        return LocalDate.parse(date, getDateFormatter());
    }

    @Override
    public LocalDate getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String date = rs.getString(columnIndex);
        if(date == null) return null;
        return LocalDate.parse(date, getDateFormatter());
    }

    @Override
    public LocalDate getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String date = cs.getString(columnIndex);
        if(date == null) return null;
        return LocalDate.parse(date, getDateFormatter());
    }

    private DateTimeFormatter getDateFormatter() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd");
    }
}
