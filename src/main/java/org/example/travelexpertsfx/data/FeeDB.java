package org.example.travelexpertsfx.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.travelexpertsfx.models.Fee;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

public class FeeDB {
    public static Connection getConnection(){
        String url = "";
        String user = "";
        String password = "";
        // retrieve connection information from properties file
        try{
            FileInputStream fileInputStream = new FileInputStream("c:\\connection.properties");
            Properties prop = new Properties();
            prop.load(fileInputStream);
            url = (String) prop.get("url"); // more general way - in case you need a double for example
            user = prop.getProperty("user");
            password = prop.getProperty("password");
        } catch(IOException e) {
            throw new RuntimeException("Problem with reading connection info: " + e.getMessage());
        }
        // build the connection
        Connection conn = null;
        try{
            conn = DriverManager.getConnection(url, user, password);
        } catch(SQLException e){
            throw new RuntimeException("Problem with database connection: " + e.getMessage());
        }
        return conn;
    } // end getConnection

    public static ObservableList<Fee> getFees() throws SQLException {
        ObservableList<Fee> fees = FXCollections.observableArrayList();

        Fee fee; // for processing data
        Connection conn = getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("select * from fees");
        while(rs.next()){
            fee = new Fee(
                    rs.getString(1),
                    rs.getString(2),
                    rs.getDouble(3),
                    rs.getString(4));
            fees.add(fee);
        }
        conn.close();
        return fees;
    } // end getFees

    public static int insertFee(Fee fee) throws SQLException {
        int numRows = 0; // number of rows affected
        Connection conn = getConnection();
        String sql = "INSERT INTO fees (FeeId, FeeName, FeeAmt, FeeDesc) " +
                "VALUES(?,?,?,?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, fee.getFeeId());
        stmt.setString(2, fee.getFeeName());
        stmt.setDouble(3, fee.getFeeAmt());
        stmt.setString(4, fee.getFeeDesc());
        numRows = stmt.executeUpdate();
        conn.close();
        return numRows;
    }

    public static int updateFee(String feeId, Fee fee) throws SQLException {
        int numRows = 0; // number of rows affected
        Connection conn = getConnection();
        String sql = "UPDATE fees SET " +
                "FeeName = ?, " +
                "FeeAmt = ?, " +
                "FeeDesc = ? " +
                "WHERE FeeId = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, fee.getFeeName());
        stmt.setString(2, fee.getFeeAmt()+"");
        stmt.setString(3, fee.getFeeDesc());
        stmt.setString(4, feeId);
        numRows = stmt.executeUpdate();
        conn.close();
        return numRows;
    }

    public static int deleteFee(String feeId) throws SQLException {
        int numRows = 0; // number of rows affected
        Connection conn = getConnection();
        String sql = "DELETE from fees WHERE FeeId=?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, feeId);
        numRows = stmt.executeUpdate();
        conn.close();
        return numRows;
    }

    public static ArrayList<String> getExistingFeeIds() throws SQLException {
        Connection conn = getConnection();
        ArrayList<String> arrayList = new ArrayList<>();
        String sql = "select FeeId from fees";
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()){
            arrayList.add(rs.getString(1));
        }
        conn.close();
        return arrayList;
    }
} // end class

