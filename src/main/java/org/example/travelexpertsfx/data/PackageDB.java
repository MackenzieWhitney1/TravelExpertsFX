package org.example.travelexpertsfx.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.travelexpertsfx.DatabaseHelper;
import org.example.travelexpertsfx.models.MyPackage;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class PackageDB {
    public static ObservableList<MyPackage> getPackages() throws SQLException {
        ObservableList<MyPackage> packages = FXCollections.observableArrayList();

        MyPackage myPackage; // for processing data
        Connection conn = DatabaseHelper.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("select * from packages");
        while(rs.next()){
            myPackage = new MyPackage(
                    rs.getInt(1),
                    rs.getString(2),
                    rs.getDate(3),
                    rs.getDate(4),
                    rs.getString(5),
                    rs.getDouble(6),
                    rs.getDouble(7)
            );
            packages.add(myPackage);
        }
        conn.close();
        return packages;
    }

    public static int insertPackage(MyPackage myPackage) throws SQLException {
        int numRows = 0; // number of rows affected
        Connection conn = DatabaseHelper.getConnection();
        String sql = "INSERT INTO packages (PkgName, PkgStartDate, PkgEndDate, PkgDesc, " +
                "PkgBasePrice, PkgAgencyCommission) " +
                "VALUES(?,?,?,?,?,?)";

        numRows = DatabaseHelper.createPreparedStatementExecute(conn, sql,
                myPackage.getPkgName(),
                myPackage.getPkgStartDate(),
                myPackage.getPkgEndDate(),
                myPackage.getPkgDesc(),
                myPackage.getPkgBasePrice(),
                myPackage.getPkgAgencyCommission());

        conn.close();

        return numRows;
    }

    public static int updatePackage(int packageId, MyPackage myPackage) throws SQLException {
        int numRows = 0; // number of rows affected
        Connection conn = DatabaseHelper.getConnection();
        String sql = "UPDATE packages SET " +
                "PkgName = ?, " +
                "PkgStartDate = ?, " +
                "PkgEndDate = ?," +
                "PkgDesc = ?, " +
                "PkgBasePrice = ?, " +
                "PkgAgencyCommission = ? " +
                "WHERE PackageId = ?";
        numRows = DatabaseHelper.createPreparedStatementExecute(conn, sql,
                myPackage.getPkgName(),
                myPackage.getPkgStartDate(),
                myPackage.getPkgEndDate(),
                myPackage.getPkgDesc(),
                myPackage.getPkgBasePrice(),
                myPackage.getPkgAgencyCommission(),
                packageId);

        conn.close();

        return numRows;
    }

    public static int deletePackage(int packageId) throws SQLException {
        int numRows = 0; // number of rows affected
        Connection conn = DatabaseHelper.getConnection();
        String sql = "DELETE from packages WHERE packageid=?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, packageId);
        numRows = stmt.executeUpdate();
        conn.close();
        return numRows;
    }

    public static Map<Integer, String> getPackageComboBoxMap() throws SQLException {
        Map<Integer, String> packages = new HashMap<>();
        int nextId;
        String nextName;
        Connection conn = DatabaseHelper.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("select PackageId, PkgName from packages order by PackageId");
        while(rs.next()){
            nextId = rs.getInt(1);
            nextName = rs.getString(2);
            packages.put(nextId, nextName);
        }
        conn.close();
        return packages;
    }
} // end class
