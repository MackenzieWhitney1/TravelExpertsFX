package org.example.travelexpertsfx;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.*;
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

    public static <T> void setupTableColumns(TableView<T> tableView, String tableName, Class<T> clazz) {
        // Clear existing columns
        tableView.getColumns().clear();

        try {
            // Establish database connection
            Connection conn = DatabaseHelper.getConnection();
            Statement stmt = conn.createStatement();

            // Execute a query to retrieve the structure of the given table
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName + " LIMIT 1");
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Loop through the columns dynamically based on the table structure
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnName(i);

                // Create a new TableColumn using the column name from the database
                TableColumn<T, String> column = getTableColumn(clazz, columnName);

                // Add the created column to the TableView
                tableView.getColumns().add(column);
            }

        } catch (SQLException e) {
            // Handle any SQL exceptions
            throw new RuntimeException(e);
        }
    }

    private static <T> TableColumn<T, String> getTableColumn(Class<T> clazz, String columnName) {
        TableColumn<T, String> column = new TableColumn<>(columnName);

        // Set up the cellValueFactory to extract the corresponding property from the data object
        column.setCellValueFactory(cellData -> {
            T data = cellData.getValue();
            String value;

            try {
                // Use reflection to get the getter method based on the field name
                String methodName = "get" + Character.toUpperCase(columnName.charAt(0)) + columnName.substring(1);
                Method method = clazz.getMethod(methodName);
                Object fieldValue = method.invoke(data);

                // Convert the field value to a String
                value = fieldValue != null ? fieldValue.toString() : "N/A";
            } catch (Exception e) {
                e.printStackTrace();
                return new SimpleStringProperty("N/A");
            }

            return new SimpleStringProperty(value);
        });
        return column;
    }
}
