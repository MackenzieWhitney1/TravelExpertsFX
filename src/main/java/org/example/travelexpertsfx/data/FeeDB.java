package org.example.travelexpertsfx.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.travelexpertsfx.DatabaseHelper;
import org.example.travelexpertsfx.models.Fee;

import java.sql.*;
import java.util.ArrayList;

public class FeeDB {
    public static ObservableList<Fee> getFees() throws SQLException {
        ObservableList<Fee> fees = FXCollections.observableArrayList();

        Fee fee; // for processing data
        Connection conn = DatabaseHelper.getConnection();
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
        Connection conn = DatabaseHelper.getConnection();
        String sql = "INSERT INTO fees (FeeId, FeeName, FeeAmt, FeeDesc) " +
                "VALUES(?,?,?,?)";
        numRows = DatabaseHelper.createPreparedStatementExecute(conn, sql,
                fee.getFeeId(),
                fee.getFeeName(),
                fee.getFeeAmt(),
                fee.getFeeDesc());

        conn.close();
        return numRows;
    }

    public static int updateFee(String feeId, Fee fee) throws SQLException {
        int numRows = 0; // number of rows affected
        Connection conn = DatabaseHelper.getConnection();
        String sql = "UPDATE fees SET " +
                "FeeName = ?, " +
                "FeeAmt = ?, " +
                "FeeDesc = ? " +
                "WHERE FeeId = ?";

        numRows = DatabaseHelper.createPreparedStatementExecute(conn, sql,
                fee.getFeeName(),
                fee.getFeeAmt(),
                fee.getFeeDesc(),
                feeId);

        conn.close();

        return numRows;
    }

    public static int deleteFee(String feeId) throws SQLException {
        int numRows = 0; // number of rows affected
        Connection conn = DatabaseHelper.getConnection();
        String sql = "DELETE from fees WHERE FeeId=?";

        numRows = DatabaseHelper.createPreparedStatementExecute(conn, sql, feeId);

        conn.close();

        return numRows;
    }

    public static ArrayList<String> getExistingFeeIds() throws SQLException {
        Connection conn = DatabaseHelper.getConnection();
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

