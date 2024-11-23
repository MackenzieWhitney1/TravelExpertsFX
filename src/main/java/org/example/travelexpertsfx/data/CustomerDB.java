package org.example.travelexpertsfx.data;

import org.example.travelexpertsfx.DatabaseHelper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class CustomerDB {
    public static Map<Integer, String> getCustomerComboBoxMap() throws SQLException {
        Map<Integer, String> customers = new HashMap<>();
        int nextId;
        String nextName;
        Connection conn = DatabaseHelper.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("select CustomerId, CustFirstName, CustLastName from customers order by CustFirstName");
        while(rs.next()){
            nextId = rs.getInt(1);
            nextName = rs.getString(2)+" "+rs.getString(3);
            customers.put(nextId, nextName);
        }
        conn.close();
        return customers;
    }
}
