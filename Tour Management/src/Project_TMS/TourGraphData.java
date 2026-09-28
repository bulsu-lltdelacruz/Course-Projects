/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Project_TMS;

/**
 *
 * @author user
 */
public class TourGraphData {
    private int numberOfBooks;
    private String tourName;

    public TourGraphData(String tourName, int numberOfBooks) {
        this.tourName = tourName;
        this.numberOfBooks = numberOfBooks;
    }
    
    
    public int getNumberOfBooks(){
        return numberOfBooks;
    }
    public String getTourName(){
        return tourName;
    }
}
