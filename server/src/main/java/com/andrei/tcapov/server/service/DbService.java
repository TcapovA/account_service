package com.andrei.tcapov.server.service;

import com.andrei.tcapov.server.Server;
import com.andrei.tcapov.server.api.Account;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.h2.jdbc.JdbcSQLException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DbService {

    private DbService() {
    }

    private static HikariConfig config = new HikariConfig();
    private static HikariDataSource dataSource;

    private static String getAccountByIdSql;
    private static String addAmountSql;
    private static String createAmountWithIdSql;

    public static void init() {
        initJdbcConnection();
        initDataSource();
        initSqlStatements();
        createTable();
    }

    private static void createTable() {
        try {
            dataSource.getConnection().createStatement().executeUpdate("DROP TABLE IF EXISTS ACCOUNTS");
            dataSource.getConnection().createStatement().executeUpdate("CREATE TABLE ACCOUNTS(ID INT PRIMARY KEY, AMOUNT BIGINT NOT NULL)");
        } catch (SQLException ex) {
            throw new RuntimeException("Error while trying to create table", ex);
        }
        System.out.println("test");
    }

    public static long getAmount(int id) throws SQLException {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = dataSource.getConnection();
            connection.setReadOnly(true);
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(getAccountSql(id));
            if (!resultSet.next()) {
                return 0L;
            }
            return resultSet.getInt("AMOUNT");
        } finally {
            if (connection != null) {
                connection.setReadOnly(false);
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    public static String createAccount(int id, long amount) throws SQLException {
        Connection connection = null;
        Statement createStatement = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            connection.createStatement().executeUpdate(createAccountSql(id, amount));
            // Not atomic in absence of synchronization
            connection.commit();
            Server.getCache().put(new Account(id, amount));
            return "Account with id = " + id + " has been successfully updated";
        } catch (JdbcSQLException jdbcEx) {
            // account has already been created

            return updateAmount(id, amount);
        } catch (SQLException ex) {
            if (connection != null) {
                connection.rollback();
            }
            String errMsg = "Error while trying to handle request! with params: " + id + " " + amount;
            Logger.sendMessage(errMsg);
            Logger.sendMessage(ex);
            return errMsg;
        } finally {
            if (createStatement != null) {
                createStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    public static String updateAmount(int id, long amount) throws SQLException {
        Connection connection = null;
        Statement getStatement = null;
        Statement createStatement = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            getStatement = connection.createStatement();
            ResultSet resultSet = getStatement.executeQuery(getAccountSql(id));
            long prevAmount = 0;
            if (resultSet.next()) {
                amount = resultSet.getLong("AMOUNT");
            }
            createStatement = connection.createStatement();
            createStatement.executeUpdate(updateAmountSql(id, prevAmount + amount));
            connection.commit();
            Server.getCache().put(new Account(id, amount));
            return "Account with id = " + id + " has been successfully updated";
        } catch (SQLException ex) {
            if (connection != null) {
                connection.rollback();
            }
            throw ex;
        } finally {
            if (connection != null) {
                connection.setAutoCommit(true);
            }
            if (getStatement != null) {
                getStatement.close();
            }
            if (createStatement != null) {
                createStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    public static void shutdown() {
        dataSource.close();
    }

    private static void initJdbcConnection() {
        try {
            Class.forName(ConfigService.getDbDriver());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Can't establish jdbc connection using driver: " +
                    ConfigService.getDbDriver());
        }
    }

    private static void initDataSource() {
        config.setDriverClassName(ConfigService.getDbDriver());
        config.setJdbcUrl(ConfigService.getDbUrl());
        config.setUsername(ConfigService.getDbUser());
        config.setPassword(ConfigService.getDbPass());
        // default values
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.setAutoCommit(true);
        dataSource = new HikariDataSource(config);
    }

    private static void initSqlStatements() {
        getAccountByIdSql =
                "SELECT * FROM ACCOUNTS " +
                        "WHERE ID = %s";
        addAmountSql =
                "UPDATE ACCOUNTS " +
                        "SET AMOUNT = %s" +
                        "WHERE ID = %s";
        createAmountWithIdSql =
                "INSERT INTO ACCOUNTS(ID, AMOUNT) " +
                        "VALUES (%s, %s)";
    }

    private static String getAccountSql(int id) {
        return String.format(getAccountByIdSql, id);
    }

    private static String createAccountSql(int id, long amount) {
        return String.format(createAmountWithIdSql, id, amount);
    }

    private static String updateAmountSql(int id, long amount) {
        return String.format(addAmountSql, amount, id);
    }
}
