package org.example.travelexpertsfx.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.travelexpertsfx.DatabaseHelper;
import org.example.travelexpertsfx.models.Agency;

import java.sql.*;
import java.util.ArrayList;

public class AgencyDB {
    public static ObservableList<Agency> getAgencies() throws SQLException {
        ObservableList<Agency> agencies = FXCollections.observableArrayList();

        Agency agency; // for processing data
        Connection conn = DatabaseHelper.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("select * from agencies");
        while(rs.next()){
            agency = new Agency(
                    rs.getInt(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4),
                    rs.getString(5),
                    rs.getString(6),
                    rs.getString(7),
                    rs.getString(8)
            );
            agencies.add(agency);
        }
        conn.close();
        return agencies;
    } // end getAgencies

    public static int insertAgency(Agency agency) throws SQLException {
        int numRows = 0; // number of rows affected
        Connection conn = DatabaseHelper.getConnection();
        String sql = "INSERT INTO agencies (AgncyAddress, AgncyCity, AgncyProv, AgncyPostal, " +
                "AgncyCountry, AgncyPhone, AgncyFax) " +
                "VALUES(?,?,?,?,?,?,?)";

        numRows = DatabaseHelper.createPreparedStatementExecute(conn, sql,
                agency.getAgncyAddress(),
        agency.getAgncyCity(),
        agency.getAgncyProv(),
        agency.getAgncyPostal(),
        agency.getAgncyCountry(),
        agency.getAgncyPhone(),
        agency.getAgncyFax());

        conn.close();

        return numRows;
    }

    public static int updateAgency(int agencyId, Agency agency) throws SQLException {
        int numRows = 0; // number of rows affected
        Connection conn = DatabaseHelper.getConnection();
        String sql = "UPDATE agencies SET " +
                "AgncyAddress = ?, " +
                "AgncyCity = ?, " +
                "AgncyProv = ?," +
                "AgncyPostal = ?, " +
                "AgncyCountry = ?, " +
                "AgncyPhone = ?, " +
                "AgncyFax = ? " +
                "WHERE AgencyId = ?";
        numRows = DatabaseHelper.createPreparedStatementExecute(conn, sql,
                agency.getAgncyAddress(),
                agency.getAgncyCity(),
                agency.getAgncyProv(),
                agency.getAgncyPostal(),
                agency.getAgncyCountry(),
                agency.getAgncyPhone(),
                agency.getAgncyFax(),
                agencyId);

        conn.close();

        return numRows;
    }

    public static int deleteAgency(int agencyId) throws SQLException {
        int numRows = 0; // number of rows affected
        Connection conn = DatabaseHelper.getConnection();
        String sql = "DELETE from agencies WHERE AgencyId=?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, agencyId);
        numRows = stmt.executeUpdate();
        conn.close();
        return numRows;
    }
} // end class
