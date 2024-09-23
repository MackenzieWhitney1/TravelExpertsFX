package org.example.travelexpertsfx.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.travelexpertsfx.models.Agent;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

public class AgentDB {
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

    public static ObservableList<Agent> getAgents() throws SQLException {
        ObservableList<Agent> agents = FXCollections.observableArrayList();

        Agent agent; // for processing data
        Connection conn = getConnection();
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
        Connection conn = getConnection();
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
        Connection conn = getConnection();
        String sql = "INSERT INTO agents (AgtFirstName, AgtMiddleInitial, AgtLastName, AgtBusPhone, " +
                "AgtEmail, AgtPosition, agencyId) " +
                "VALUES(?,?,?,?,?,?,?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, agent.getAgtFirstName());
        stmt.setString(2, agent.getAgtMiddleInitial());
        stmt.setString(3, agent.getAgtLastName());
        stmt.setString(4, agent.getAgtBusPhone());
        stmt.setString(5, agent.getAgtEmail());
        stmt.setString(6, agent.getAgtPosition());
        stmt.setInt(7, agent.getAgencyId());
        numRows = stmt.executeUpdate();
        conn.close();
        return numRows;
    }

    public static int updateAgent(int agentId, Agent agent) throws SQLException {
        int numRows = 0; // number of rows affected
        Connection conn = getConnection();
        String sql = "UPDATE agents SET " +
                "AgtFirstName = ?, " +
                "AgtMiddleInitial = ?, " +
                "AgtLastName = ?," +
                "AgtBusPhone = ?, " +
                "AgtEmail = ?, " +
                "AgtPosition = ?, " +
                "AgencyId = ? " +
                "WHERE AgentId = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, agent.getAgtFirstName());
        stmt.setString(2, agent.getAgtMiddleInitial());
        stmt.setString(3, agent.getAgtLastName());
        stmt.setString(4, agent.getAgtBusPhone());
        stmt.setString(5, agent.getAgtEmail());
        stmt.setString(6, agent.getAgtPosition());
        stmt.setInt(7, agent.getAgencyId());
        stmt.setInt(8, agentId);
        numRows = stmt.executeUpdate();
        conn.close();
        return numRows;
    }

    public static int deleteAgent(int agentId) throws SQLException {
        int numRows = 0; // number of rows affected
        Connection conn = getConnection();
        String sql = "DELETE from agents WHERE agentid=?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, agentId);
        numRows = stmt.executeUpdate();
        conn.close();
        return numRows;
    }
} // end class
