package org.example.travelexpertsfx.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.travelexpertsfx.DatabaseHelper;
import org.example.travelexpertsfx.models.Agent;

import java.sql.*;
import java.util.ArrayList;

public class AgentDB {
    public static ObservableList<Agent> getAgents() throws SQLException {
        ObservableList<Agent> agents = FXCollections.observableArrayList();

        Agent agent; // for processing data
        Connection conn = DatabaseHelper.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("select * from agents");
        while(rs.next()){
            agent = new Agent(
                    rs.getInt(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4),
                    rs.getString(5),
                    rs.getString(6),
                    rs.getString(7),
                    rs.getInt(8)
            );
            agents.add(agent);
        }
        conn.close();
        return agents;
    } // end getAgents

    public static ArrayList<Integer> getAgencyIds() throws SQLException {
        ArrayList<Integer> agencyIds = new ArrayList<>();
        int nextId;
        Connection conn = DatabaseHelper.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("select AgencyId from agencies order by AgencyId");
        while(rs.next()){
            nextId = rs.getInt(1);
            agencyIds.add(nextId);
        }
        conn.close();
        return agencyIds;
    }

    public static int insertAgent(Agent agent) throws SQLException {
        int numRows = 0; // number of rows affected
        Connection conn = DatabaseHelper.getConnection();
        String sql = "INSERT INTO agents (AgtFirstName, AgtMiddleInitial, AgtLastName, AgtBusPhone, " +
                "AgtEmail, AgtPosition, agencyId) " +
                "VALUES(?,?,?,?,?,?,?)";

        numRows = DatabaseHelper.createPreparedStatementExecute(conn, sql,
                agent.getAgtFirstName(),
                agent.getAgtMiddleInitial(),
                agent.getAgtLastName(),
                agent.getAgtBusPhone(),
                agent.getAgtEmail(),
                agent.getAgtPosition(),
                agent.getAgencyId());

        conn.close();

        return numRows;
    }

    public static int updateAgent(int agentId, Agent agent) throws SQLException {
        int numRows = 0; // number of rows affected
        Connection conn = DatabaseHelper.getConnection();
        String sql = "UPDATE agents SET " +
                "AgtFirstName = ?, " +
                "AgtMiddleInitial = ?, " +
                "AgtLastName = ?," +
                "AgtBusPhone = ?, " +
                "AgtEmail = ?, " +
                "AgtPosition = ?, " +
                "AgencyId = ? " +
                "WHERE AgentId = ?";
        numRows = DatabaseHelper.createPreparedStatementExecute(conn, sql,
                agent.getAgtFirstName(),
                agent.getAgtMiddleInitial(),
                agent.getAgtLastName(),
                agent.getAgtBusPhone(),
                agent.getAgtEmail(),
                agent.getAgtPosition(),
                agent.getAgencyId(),
                agentId);

        conn.close();

        return numRows;
    }

    public static int deleteAgent(int agentId) throws SQLException {
        int numRows = 0; // number of rows affected
        Connection conn = DatabaseHelper.getConnection();
        String sql = "DELETE from agents WHERE agentid=?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, agentId);
        numRows = stmt.executeUpdate();
        conn.close();
        return numRows;
    }
} // end class
