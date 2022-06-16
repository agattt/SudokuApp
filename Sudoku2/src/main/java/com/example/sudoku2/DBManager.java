package com.example.sudoku2;
import java.sql.*;
import java.util.Objects;

import org.junit.jupiter.api.Assertions;

public class DBManager {

    private Connection connection;

    public Connection getConnection() {
        try {
            if (connection.isClosed() || !connection.isValid(1000)) {
                this.connection = DriverManager.getConnection("jdbc:hsqldb:file:users", "SA", "");
            }
            return this.connection;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void prepareDb() {

        try (Connection c = DriverManager.getConnection("jdbc:hsqldb:file:users", "SA", "")) {

            this.connection = c;

            try (Statement st = c.createStatement()) {

                st.execute("CREATE TABLE IF NOT EXISTS USERS (ID INT IDENTITY, NAME VARCHAR(20), EASY BOOLEAN, " +
                        "MEDIUM BOOLEAN, HARD BOOLEAN)");

            }

        } catch (Exception e) {

            Assertions.fail(e);

        }
    }

    public boolean verifyIfUserExists(String username) {

        try (Connection c = DriverManager.getConnection("jdbc:hsqldb:file:users", "SA", "")) {

            try (PreparedStatement st = c.prepareStatement("SELECT NAME FROM users")) {

                try (ResultSet rs = st.executeQuery()) {

                    if (!rs.next()) {

                        addUsernameToDB(username);

                    } else {

                        while (rs.next()) {

                            if (Objects.equals(rs.getString("NAME"), username)) {

                                return true;

                            }

                        }

                        addUsernameToDB(username);

                        return false;


                    }

                }

            }

        } catch (SQLException exception) {

            exception.printStackTrace();

        }

        return false;

    }

    public boolean verifyIfUserCompletedLevel(String level, String username){

        try (Connection c = DriverManager.getConnection("jdbc:hsqldb:file:users", "SA", "")) {

            try (PreparedStatement st = c.prepareStatement("SELECT " + level + " FROM users WHERE NAME = ?")) {

                st.setString(1, username);

                System.out.println(st);

                try (ResultSet rs = st.executeQuery()) {

                    rs.next();

                    return rs.getBoolean(level);

                }

            }

        } catch (SQLException exception) {

            exception.printStackTrace();

        }

        return false;

    }

    private void addUsernameToDB(String username) {

        try (Connection c = DriverManager.getConnection("jdbc:hsqldb:file:users", "SA", "")) {

            try (PreparedStatement st = c.prepareStatement("INSERT INTO users (NAME, EASY, MEDIUM, HARD) VALUES (?, ?, ?, ?)")) {

                st.setString(1, username);
                st.setBoolean(2, false);
                st.setBoolean(3, false);
                st.setBoolean(4, false);

                st.execute();

            }

        } catch (SQLException throwables) {

            throwables.printStackTrace();

        }

    }

    public void setLevelCompleted(String level, String username) throws SQLException {

        try (Connection c = DriverManager.getConnection("jdbc:hsqldb:file:users", "SA", "")) {

            try (PreparedStatement st = c.prepareStatement("UPDATE users SET " + level + " = ? WHERE NAME = ?")) {

                st.setBoolean(1, true);

                st.setString(2, username);

                st.execute();

            }

        } catch (SQLException throwables) {

            throwables.printStackTrace();

        }

    }

}
