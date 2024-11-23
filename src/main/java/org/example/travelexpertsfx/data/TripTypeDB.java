package org.example.travelexpertsfx.data;

import org.example.travelexpertsfx.DatabaseHelper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class TripTypeDB {
    public static Map<String, String> getTripTypeComboBoxMap() throws SQLException {
        Map<String, String> tripTypes = new HashMap<>();
        String nextId;
        String nextName;
        Connection conn = DatabaseHelper.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("select * from triptypes");
        while(rs.next()){
            nextId = rs.getString(1);
            nextName = rs.getString(2);
            tripTypes.put(nextId, nextName);
        }
        conn.close();
        return tripTypes;
    }
}
