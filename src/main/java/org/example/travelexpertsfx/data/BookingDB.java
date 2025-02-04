package org.example.travelexpertsfx.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.travelexpertsfx.DatabaseHelper;
import org.example.travelexpertsfx.models.Booking;
import org.example.travelexpertsfx.models.BookingAudit;

import java.sql.*;

public class BookingDB {
    public static ObservableList<Booking> getBookings() throws SQLException{
        ObservableList<Booking> bookings = FXCollections.observableArrayList();
        Booking booking;
        Connection conn = DatabaseHelper.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("select * from bookings");
        while (rs.next()){
            booking = new Booking(rs.getInt(1),
                    rs.getDate(2),
                    rs.getString(3),
                    rs.getDouble(4),
                    rs.getInt(5),
                    rs.getString(6),
                    rs.getInt(7));
            bookings.add(booking);
        }
        conn.close();
        return bookings;
    }

    public static int insertBooking(Booking booking) throws SQLException{
        int numRows=0;
        Connection conn = DatabaseHelper.getConnection();
        String sql = "INSERT INTO bookings (BookingId,BookingDate,BookingNo,TravelerCount,CustomerId,TripTypeId,PackageId) ";
        sql+="VALUES(?,?,?,?,?,?,?)";
        numRows = DatabaseHelper.createPreparedStatementExecute(conn,sql,
                booking.getBookingId(),
                booking.getBookingDate(),
                booking.getBookingNo(),
                booking.getTravelerCount(),
                booking.getCustomerId(),
                booking.getTripTypeId(),
                booking.getPackageId());

        conn.close();

        return numRows;
    }

    public static int updateBooking(int bookingId,Booking booking) throws SQLException{
        int numRows=0;
        Connection conn = DatabaseHelper.getConnection();
        String sql = "UPDATE Bookings SET "+
                "BookingId = ?, "+
                "BookingDate = ?, "+
                "BookingNo = ?, "+
                "TravelerCount = ?, "+
                "CustomerId = ?, "+
                "TripTypeId = ?, "+
                "PackageId = ? "+
                "WHERE BookingId = ?";
        numRows = DatabaseHelper.createPreparedStatementExecute(conn,sql,
                booking.getBookingId(),
                booking.getBookingDate(),
                booking.getBookingNo(),
                booking.getTravelerCount(),
                booking.getCustomerId(),
                booking.getTripTypeId(),
                booking.getPackageId(),
                bookingId);

        conn.close();

        return numRows;
    }

    public static int deleteBooking(int bookingId) throws SQLException{
        int numrows = 0;
        Connection conn = DatabaseHelper.getConnection();
        String sql = "DELETE FROM Bookings WHERE BookingId = ?";
        numrows = DatabaseHelper.createPreparedStatementExecute(conn, sql, bookingId);
        conn.close();
        return numrows;
    }

    public static ObservableList<BookingAudit> getBookingAudits() throws SQLException{
        ObservableList<BookingAudit> bookingAudits = FXCollections.observableArrayList();
        BookingAudit bookingAudit;
        Connection conn = DatabaseHelper.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("select * from bookings_audits");
        while (rs.next()){
            bookingAudit = new BookingAudit(rs.getInt(1),
                    rs.getDate(2),
                    rs.getString(3),
                    rs.getDouble(4),
                    rs.getInt(5),
                    rs.getString(6),
                    rs.getInt(7),
                    rs.getDate(8),
                    rs.getString(9));
            bookingAudits.add(bookingAudit);
        }
        conn.close();
        return bookingAudits;
    }

}
