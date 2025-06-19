package ru.netology.data;

import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLHelper {
    private static final QueryRunner QUERY_RUNNER = new QueryRunner();

    private SQLHelper() {
    }

    private static Connection getConn() throws SQLException {
        return DriverManager.getConnection(System.getProperty("db.url"), "app", "pass");
    }

    @SneakyThrows
    public static DataHelper.VerificationCode getVerificationCode() {
        var codeSQL = "SELECT code FROM auth_codes ORDER BY created DESC LIMIT 1";
        try (var conn = getConn()) {
            return QUERY_RUNNER.query(conn, codeSQL, new BeanHandler<>(DataHelper.VerificationCode.class));
     }
    }
    @SneakyThrows
    public static void cleanDatabase() {
        var connection = getConn();
        QUERY_RUNNER.execute(connection, "DELETE FROM auth_codes");
        QUERY_RUNNER.execute(connection, "DELETE FROM card_transactions");
        QUERY_RUNNER.execute(connection, "DELETE FROM cards");
        QUERY_RUNNER.execute(connection, "DELETE FROM users");
    }

    @SneakyThrows
    public static void cleanAuthCodes() {
        try (var conn = getConn()){
            QUERY_RUNNER.execute(conn, "DELETE FROM codes");
        }
    }
}