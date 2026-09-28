package tour;

import java.util.List;

public class Tour {
    private String id;
    private String name;
    private String description;
    private List<String> activities;
    private List<String> transportation;
    private List<String> accommodations;
    private List<Double> transportPrices;
    private List<Double> accommodationPrices;
    private int maxDuration;
    private double basePrice;
    private double totalPrice;
    private double discountPercentage;
    private String imagePath;

    public Tour(String id, String name, String description, List<String> activities,
                List<String> transportation, List<String> accommodations,
                List<Double> transportPrices,
                List<Double> accommodationPrices,
                int maxDuration,
                double basePrice, double totalPrice, double discountPercentage, String imagePath) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.activities = activities;
        this.transportation = transportation;
        this.accommodations = accommodations;
        this.transportPrices = transportPrices;
        this.accommodationPrices = accommodationPrices;
        this.maxDuration = maxDuration;
        this.basePrice = basePrice;
        this.totalPrice = totalPrice;
        this.discountPercentage = discountPercentage;
        this.imagePath = imagePath;
    }

    public Tour(String id, String name, String description, List<String> activities,
                List<String> transportation, List<String> accommodations, List<Double> transportPrices,
                List<Double> accommodationPrices,
                int maxDuration, int selectedDuration,
                double basePrice, double totalPrice, String imagePath) {
        this(id, name, description, activities, transportation, accommodations, transportPrices, accommodationPrices,
             maxDuration, basePrice, totalPrice, 0.0, imagePath);
    }
    
    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public double calculateDiscountedPrice() {
        return totalPrice - (totalPrice * discountPercentage / 100.0);
    }

    public String getId() {
        return id; 
    }
    public void setId(String id) {
        this.id = id; 
    }

    public String getName() {
        return name; 
    }
    public void setName(String name) {
        this.name = name; 
    }

    public String getDescription() {
        return description; 
    }
    
    public void setDescription(String description) {
        this.description = description; 
    }

    public List<String> getActivities() {
        return activities; 
    }
    
    public void setActivities(List<String> activities) {
        this.activities = activities; 
    }

    public List<String> getTransportation() { 
        return transportation; 
    }
    
    public void setTransportation(List<String> transportation) {
        this.transportation = transportation; 
    }
    
    public List<Double> getTransportPrices() {
        return transportPrices;
    }

    public void setTransportPrices(List<Double> transportPrices) {
        this.transportPrices = transportPrices;
    }

    public List<Double> getAccommodationPrices() {
        return accommodationPrices;
    }

    public void setAccommodationPrices(List<Double> accommodationPrices) {
        this.accommodationPrices = accommodationPrices;
    }

    public List<String> getAccommodations() {
        return accommodations; 
    }
    public void setAccommodations(List<String> accommodations) { 
        this.accommodations = accommodations; 
    }

    public int getMaxDuration() {
        return maxDuration;
    }
    
    public void setMaxDuration(int maxDuration) { 
        this.maxDuration = maxDuration; 
    }

    public double getBasePrice() { 
        return basePrice;
    }
    
    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public double getTotalPrice() {
        return totalPrice;
    }
    
    public void setTotalPrice(double totalPrice) { 
        this.totalPrice = totalPrice; 
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }
    
    public void setDiscountPercentage(double discountPercentage) { 
        this.discountPercentage = discountPercentage;
    }
   
}
