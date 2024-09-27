package org.example.travelexpertsfx;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseHelper {
    public static Connection getConnection() {
        String url;
        String user;
        String password;
        Connection conn;

        //retrieve connection information from properties file
        try  {
            FileInputStream fis = new FileInputStream("c:\\connection.properties");
            Properties prop = new Properties();
            prop.load(fis);

            // Read the properties
            url = prop.get("url").toString();
            user = prop.getProperty("user");
            password = prop.getProperty("password");
        }
        catch (IOException e) {
            throw new RuntimeException("Problem with reading connection information: " + e.getMessage());
        }

        try {
            conn = DriverManager.getConnection(url, user, password);
        }
        catch (SQLException e) {
            throw new RuntimeException("Problem with database connection: " + e.getMessage());
        }

        return conn;
    } // end getConnection method

    public static int createPreparedStatementExecute(Connection conn, String sql, Object... params) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(sql);

        for (int i = 0; i < params.length; i++) {
            stmt.setObject(i + 1, params[i]);
        }

        return stmt.executeUpdate();

    }
}
