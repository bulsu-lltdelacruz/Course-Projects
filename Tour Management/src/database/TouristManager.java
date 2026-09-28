package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import tourist.Tourist;
import java.util.ArrayList;
import java.util.Random;

public class TouristManager {

    public boolean addTourist(Tourist tourist) {
        String sql = "INSERT INTO tourists (touristID, firstName, middleName, lastName, birthday, gender, phoneNumber, email, username, password) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = UtilDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tourist.getTouristID());
            stmt.setString(2, tourist.getFirstName());
            stmt.setString(3, tourist.getMiddleName());
            stmt.setString(4, tourist.getLastName());
            stmt.setDate(5, java.sql.Date.valueOf(tourist.getBirthday()));
            stmt.setString(6, tourist.getGender());
            stmt.setString(7, tourist.getPhoneNumber());
            stmt.setString(8, tourist.getEmail());
            stmt.setString(9, tourist.getUsername());
            stmt.setString(10, tourist.getPassword());

            return stmt.executeUpdate() > 0;

        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Tourist getTouristByID(String tourID) {
        String sql = "SELECT * FROM tourists WHERE touristID = ?";
        try (Connection conn = UtilDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tourID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Tourist(
                    rs.getString("touristID"),
                    rs.getString("firstName"),
                    rs.getString("middleName"),
                    rs.getString("lastName"),
                    rs.getDate("birthday").toLocalDate(),
                    rs.getString("gender"),
                    rs.getString("phoneNumber"),
                    rs.getString("email"),
                    rs.getString("username"),
                    rs.getString("password")
                );
            }

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Tourist> getAllTourists() {
        ArrayList<Tourist> list = new ArrayList<>();
        String sql = "SELECT * FROM tourists";
        System.out.println("PUTANINGA");
        try (Connection conn = UtilDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Tourist tourist = new Tourist(
                    rs.getString("touristID"),
                    rs.getString("firstName"),
                    rs.getString("middleName"),
                    rs.getString("lastName"),
                    rs.getDate("birthday").toLocalDate(),
                    rs.getString("gender"),
                    rs.getString("phoneNumber"),
                    rs.getString("email"),
                    rs.getString("username"),
                    rs.getString("password")
                );
                list.add(tourist);
            }

        } 
        catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean updateTourist(Tourist tourist, String touristID) {
        String sql = "UPDATE tourists SET firstName=?, middleName=?, lastName=?, birthday=?, gender=?, phoneNumber=?, email=?, username=?, password=? WHERE touristID=?";

        try (Connection conn = UtilDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tourist.getFirstName());
            stmt.setString(2, tourist.getMiddleName());
            stmt.setString(3, tourist.getLastName());
            stmt.setDate(4, java.sql.Date.valueOf(tourist.getBirthday()));
            stmt.setString(5, tourist.getGender());
            stmt.setString(6, tourist.getPhoneNumber());
            stmt.setString(7, tourist.getEmail());
            stmt.setString(8, tourist.getUsername());
            stmt.setString(9, tourist.getPassword());
            stmt.setString(10, touristID);

            return stmt.executeUpdate() > 0;

        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteTourist(String tourID) {
        String sql = "DELETE FROM tourists WHERE touristID = ?";

        try (Connection conn = UtilDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tourID);
            return stmt.executeUpdate() > 0;

        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public Tourist findTouristByDetails(String username, String email, String phone) {
        String sql = "SELECT * FROM tourists WHERE username = ? AND email = ? AND phoneNumber = ?";

        try (Connection conn = UtilDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, email);
            stmt.setString(3, phone);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Tourist(
                    rs.getString("touristID"),
                    rs.getString("firstName"),
                    rs.getString("middleName"),
                    rs.getString("lastName"),
                    rs.getDate("birthday").toLocalDate(),
                    rs.getString("gender"),
                    rs.getString("phoneNumber"),
                    rs.getString("email"),
                    rs.getString("username"),
                    rs.getString("password")
                );
            }

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean updatePassword(String touristID, String newPassword) {
        String sql = "UPDATE tourists SET password = ? WHERE touristID = ?";

        try (Connection conn = UtilDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newPassword);
            stmt.setString(2, touristID);

            return stmt.executeUpdate() > 0;

        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean isValidLogin(String username, String password) {
      
        for (Tourist t : getAllTourists()) {
            if (t.getUsername().equals(username) && t.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }


    
    public String generateTouristID() {
        String touristID;
        Random random = new Random();

        do {
            int number = 1000 + random.nextInt(9000);
            touristID = "T" + number;
        } while (getTouristByID(touristID) != null);

        return touristID;
    }
}
