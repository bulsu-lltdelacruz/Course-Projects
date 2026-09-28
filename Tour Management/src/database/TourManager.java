package database;

import Project_TMS.TourGraphData;
import java.sql.*;
import java.util.*;
import tour.Tour;

public class TourManager {

    public boolean addTour(Tour tour) {
        String insertTourSql = "INSERT INTO tours (id, name, description, maxDuration, basePrice, totalPrice, discountPercentage, imagePath) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String insertActivitySql = "INSERT INTO tour_activities (tour_id, activity) VALUES (?, ?)";
        String insertTransportSql = "INSERT INTO tour_transportation (tour_id, transport_type, price) VALUES (?, ?, ?)";
        String insertAccommodationSql = "INSERT INTO tour_accommodations (tour_id, accommodation_type, price) VALUES (?, ?, ?)";

        try (Connection conn = UtilDB.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement stmt = conn.prepareStatement(insertTourSql)) {
                stmt.setString(1, tour.getId());
                stmt.setString(2, tour.getName());
                stmt.setString(3, tour.getDescription());
                stmt.setInt(4, tour.getMaxDuration());
                stmt.setDouble(5, tour.getBasePrice());
                stmt.setDouble(6, tour.getTotalPrice());
                stmt.setDouble(7, tour.getDiscountPercentage());
                stmt.setString(8, tour.getImagePath());
                stmt.executeUpdate();
            }

            try (PreparedStatement stmt = conn.prepareStatement(insertActivitySql)) {
                for (String activity : tour.getActivities()) {
                    stmt.setString(1, tour.getId());
                    stmt.setString(2, activity);
                    stmt.addBatch();
                }
                stmt.executeBatch();
            }

            try (PreparedStatement stmt = conn.prepareStatement(insertTransportSql)) {
                for (int i = 0; i < tour.getTransportation().size(); i++) {
                    stmt.setString(1, tour.getId());
                    stmt.setString(2, tour.getTransportation().get(i));
                    stmt.setDouble(3, tour.getTransportPrices().get(i));
                    stmt.addBatch();
                }
                stmt.executeBatch();
            }

            try (PreparedStatement stmt = conn.prepareStatement(insertAccommodationSql)) {
                for (int i = 0; i < tour.getAccommodations().size(); i++) {
                    stmt.setString(1, tour.getId());
                    stmt.setString(2, tour.getAccommodations().get(i));
                    stmt.setDouble(3, tour.getAccommodationPrices().get(i));
                    stmt.addBatch();
                }
                stmt.executeBatch();
            }

            conn.commit();
            return true;
        } 
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Tour getTourById(String tourId) {
        String tourSql = "SELECT * FROM tours WHERE id = ?";
        String activitiesSql = "SELECT activity FROM tour_activities WHERE tour_id = ?";
        String transportSql = "SELECT transport_type, price FROM tour_transportation WHERE tour_id = ?";
        String accommodationSql = "SELECT accommodation_type, price FROM tour_accommodations WHERE tour_id = ?";

        try (Connection conn = UtilDB.getConnection();
             PreparedStatement tourStmt = conn.prepareStatement(tourSql)) {

            tourStmt.setString(1, tourId);
            ResultSet rs = tourStmt.executeQuery();

            if (!rs.next()) return null;

            Tour tour = new Tour(
                rs.getString("id"),
                rs.getString("name"),
                rs.getString("description"),
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
                new ArrayList<>(), new ArrayList<>(),
                rs.getInt("maxDuration"),
                rs.getDouble("basePrice"),
                rs.getDouble("totalPrice"),
                rs.getDouble("discountPercentage"),
                rs.getString("imagePath")
            );

            try (PreparedStatement stmt = conn.prepareStatement(activitiesSql)) {
                stmt.setString(1, tourId);
                ResultSet ars = stmt.executeQuery();
                while (ars.next()) tour.getActivities().add(ars.getString("activity"));
            }

            try (PreparedStatement stmt = conn.prepareStatement(transportSql)) {
                stmt.setString(1, tourId);
                ResultSet trs = stmt.executeQuery();
                while (trs.next()) {
                    tour.getTransportation().add(trs.getString("transport_type"));
                    tour.getTransportPrices().add(trs.getDouble("price"));
                }
            }

            try (PreparedStatement stmt = conn.prepareStatement(accommodationSql)) {
                stmt.setString(1, tourId);
                ResultSet acrs = stmt.executeQuery();
                while (acrs.next()) {
                    tour.getAccommodations().add(acrs.getString("accommodation_type"));
                    tour.getAccommodationPrices().add(acrs.getDouble("price"));
                }
            }

            return tour;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Tour> getAllTours() {
        List<Tour> tours = new ArrayList<>();
        String sql = "SELECT id FROM tours";

        try (Connection conn = UtilDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Tour t = getTourById(rs.getString("id"));
                if (t != null) tours.add(t);
            }

        } 
        catch (SQLException e) {
            e.printStackTrace();
        }

        return tours;
    }
    public boolean updateTour(Tour tour, String tourID) {
        System.out.println("DELETEIGINNGGNN: " + tourID);
        if (!deleteTour(tourID)) return false;
        return addTour(tour);
    }
    /*public boolean updateTour(String tourID, Tour tour) {
        if (!deleteTour(tour.getId())) return false;
        //return addTour(tour);
        
        String updateActivities = "UPDATE FROM tour_activities "
                + "SET name=?,"
                + " description=?, "
                + "maxDuration=?, "
                + "basePrice=?,"
                + "totalPrice=?, "
                + "discountPercentage=?, "
                + "imagePath=? "
                + "WHERE tour_id = ?";
        String updateTransport = "UPDATE FROM tour_transportation "
                + "SET transport_type=?, "
                + "price=?,";
        String updateAccommodation = "UPDATE FROM tour_accommodations "
                + "SET"
                + "WHERE tour_id = ?";
        String updateTour = "UPDATE FROM tours WHERE id = ?";

        try (Connection conn = UtilDB.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement stmt = conn.prepareStatement(updateActivities)) {
                stmt.setString(1, tourID);
                stmt.executeUpdate();
            }
            try (PreparedStatement stmt = conn.prepareStatement(updateTransport)) {
                stmt.setString(1, tourID);
                stmt.executeUpdate();
            }
            try (PreparedStatement stmt = conn.prepareStatement(updateAccommodation)) {
                stmt.setString(1, tourID);
                stmt.executeUpdate();
            }
            try (PreparedStatement stmt = conn.prepareStatement(updateTour)) {
                stmt.setString(1, tourID);
                stmt.executeUpdate();
            }

            conn.commit();
            return true;
        } 
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        
    }*/

    public boolean deleteTour(String tourId) {
        String deleteActivities = "DELETE FROM tour_activities WHERE tour_id = ?";
        String deleteTransport = "DELETE FROM tour_transportation WHERE tour_id = ?";
        String deleteAccommodation = "DELETE FROM tour_accommodations WHERE tour_id = ?";
        String deleteTour = "DELETE FROM tours WHERE id = ?";

        try (Connection conn = UtilDB.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement stmt = conn.prepareStatement(deleteActivities)) {
                stmt.setString(1, tourId);
                stmt.executeUpdate();
            }
            try (PreparedStatement stmt = conn.prepareStatement(deleteTransport)) {
                stmt.setString(1, tourId);
                stmt.executeUpdate();
            }
            try (PreparedStatement stmt = conn.prepareStatement(deleteAccommodation)) {
                stmt.setString(1, tourId);
                stmt.executeUpdate();
            }
            try (PreparedStatement stmt = conn.prepareStatement(deleteTour)) {
                stmt.setString(1, tourId);
                stmt.executeUpdate();
            }

            conn.commit();
            return true;
        } 
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String generateTourId() {
        String tourId;
        Random random = new Random();

        do {
            int n = 1000 + random.nextInt(9000);
            tourId = String.valueOf(n);
        } while (getTourById(tourId) != null);

        return tourId;
    }

    public Tour findTourByDetails(String name, String description) {
        String sql = "SELECT id FROM tours WHERE name = ? AND description = ?";

        try (Connection conn = UtilDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.setString(2, description);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return getTourById(rs.getString("id"));
            }

        } 
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
      public List<Integer> getTourGraphDataByPrice() {
        String[] queries = new String[4];  
         queries[0] = "SELECT * FROM tours WHERE basePrice BETWEEN 0 AND 100 ";
         queries[1] = "SELECT * FROM tours WHERE basePrice BETWEEN 101 AND 500 ";
         queries[2] = "SELECT * FROM tours WHERE basePrice BETWEEN 501 AND 1000 ";
         queries[3] = "SELECT * FROM tours WHERE basePrice > 1000 ";
        
        String sql = "SELECT id FROM tours WHERE name = ? AND description = ?";
        
        List<Integer> data = new ArrayList<Integer>();

        try (Connection conn = UtilDB.getConnection();
             Statement stmt = conn.createStatement()) {
             ResultSet[] rs = new ResultSet[4];
             
            for(int i =0; i<4;i++){
                
                rs[i]= stmt.executeQuery(queries[i]);
                int count = 0;
                while (rs[i].next()) {
                    count++;
                }
                data.add(count);
            }
            return data;

        } 
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
}
