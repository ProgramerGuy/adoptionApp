package com.expeditors.adoptionapp.dao;

import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BaseDAO {

    protected Connection getConnection() throws SQLException {
        String url = "jdbc:postgresql://localhost:5433/adoptapp";
        String user = "larku";
        String pw = "larku";

        return DriverManager.getConnection(url, user, pw);
    }

    protected DriverManagerDataSource getDataSource() {
        String url = "jdbc:postgresql://localhost:5433/adoptapp";
        String user = "larku";
        String pw = "larku";

        DriverManagerDataSource dataSource = new DriverManagerDataSource(url, user, pw);

        return dataSource;
    }
}
