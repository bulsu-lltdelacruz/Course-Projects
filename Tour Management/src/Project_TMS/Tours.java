package Project_TMS;

import database.TourManager;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.ScaleTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import tour.Tour;

public class Tours {
    
    private ScrollPane toursScrollPane = new ScrollPane();
    private GridPane toursFlowPane;
    ImageView view;
    
    public ScrollPane getToursNode(BorderPane borderpane) {
        
        List<Project_TMS.TourPreviews> tourPreviews = new ArrayList<>();
        TourManager tourManager = new TourManager();
        List<Tour> allTours;
        
        toursFlowPane = new GridPane(120, 60);
        toursFlowPane.setAlignment(Pos.CENTER);
        
        allTours = tourManager.getAllTours();
        //DAGDAG DITO
        toursFlowPane.getChildren().clear();
        tourPreviews.clear();
        for(int i = 0; i< tourPreviews.size(); i++){
            toursFlowPane.getChildren().remove(tourPreviews.get(i));
        }
        //DAGDAG DITO
        int actualSize = allTours.size()/3;
        actualSize=(allTours.size()%3>0)?actualSize+1:actualSize;
        int realLength = 3;
        if(toursScrollPane.getWidth()>1300)
            realLength = 4;
        int actualIndex = -1;
        
        for(int j = 0; j< actualSize; j++){
            for(int i=0; i < realLength; i++){
                actualIndex++;
                
                if(actualIndex < allTours.size())
                {
                    //System.out.println("asd" + actualIndex); 
                    String tourName = allTours.get(actualIndex).getName();
                    String tourDescription = allTours.get(actualIndex).getDescription();
                    double tourPrice = allTours.get(actualIndex).getBasePrice();
                    String tourID = allTours.get(actualIndex).getId();
                    String tourImage = allTours.get(actualIndex).getImagePath();

                    tourPreviews.add(new TourPreviews(tourName, tourDescription, tourPrice, tourID, tourImage, tourManager, this));
                    toursFlowPane.add(tourPreviews.get(actualIndex),i,j);
                }
                
            }
        }
        toursFlowPane.setStyle("-fx-background-color: #393232");
        toursScrollPane.setStyle("-fx-background-color: #393232");
        toursScrollPane.setFitToWidth(true);
        Button addTours = addBTN(borderpane);
        HBox spacerTop = new HBox(addTours);
        spacerTop.setMargin(addTours, new Insets(20,40,30,0));
        
        spacerTop.setAlignment(Pos.CENTER_RIGHT);
        Rectangle spacerBottom = new Rectangle();
            //spacerTop.setPrefHeight(70);
            spacerBottom.setHeight(50);
        
        VBox parentVBox = new VBox();
        parentVBox.setStyle("-fx-background-color: #393232");     
        parentVBox.getChildren().addAll(spacerTop,toursFlowPane,spacerBottom);
        toursScrollPane.setContent(parentVBox);
        toursScrollPane.setFitToWidth(true);
        toursScrollPane.setFitToHeight(true);
        parentVBox.setFillWidth(true);
        toursFlowPane.setStyle("-fx-background-color: #393232;");
        parentVBox.setMargin(toursFlowPane, new Insets(0,20,0,20));
        
        toursScrollPane.widthProperty().addListener(new ChangeListener(){
            @Override
            public void changed(ObservableValue ov, Object t, Object t1) {
                
                boolean changed = false;
                if((Double)ov.getValue()>1300){
                    if(!changed)
                    {
                        changed = true;
                        for(int i = 0; i< tourPreviews.size(); i++){
                            toursFlowPane.getChildren().remove(tourPreviews.get(i));
                        }
                        int actualSize = allTours.size()/3;
                        actualSize=(allTours.size()%3>0)?actualSize+1:actualSize;

                        int actualIndex = -1;
                        for(int j = 0; j< actualSize; j++){
                            for(int i=0; i < 4; i++){
                                actualIndex++;

                                if(actualIndex < allTours.size())
                                {
                                    toursFlowPane.add(tourPreviews.get(actualIndex),i,j);
                                }

                            }
                        }
                    }
                    
                }else{
                    
                    if(true)
                    {
                        System.out.println("PUTANGINAMO");
                        //changed = false;
                        for(int i = 0; i< tourPreviews.size(); i++){
                            toursFlowPane.getChildren().remove(tourPreviews.get(i));
                        }
                        int actualSize = allTours.size()/3;
                        actualSize=(allTours.size()%3>0)?actualSize+1:actualSize;

                        int actualIndex = -1;
                        for(int j = 0; j< actualSize; j++){
                            for(int i=0; i < 3; i++){
                                actualIndex++;

                                if(actualIndex < allTours.size())
                                {
                                    toursFlowPane.add(tourPreviews.get(actualIndex),i,j);
                                }

                            }
                        }
                    }
                    
                }
                //System.out.println("dhahhahah : "+(Double)ov.getValue());
            }
        
        });
        
        return toursScrollPane;
    }
    
    
        private Button addBTN(BorderPane borderPane){
            Button addTourButton = Util.createButton("Add Tour", 200, Util.setButtonStyle("black", "16px", "green", "20px"));
            //addTourButton.setMinWidth(120);
            //addTourButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 8 16 8 16; -fx-background-radius: 8px;");

            addTourButton.setOnAction(e -> {
                new AddTour().getAddTourDialog(this);
            });

            return addTourButton;
    }

}

class TourPreviews extends VBox{
    
    TourPreviews(String name, String description, double price, String tourID, String tourImage, TourManager tourManager, Tours tours){
        
        Insets btnInsets = new Insets(5,7,5,7);
        Button  tempButtonDel = Util.createButton("", 120, Util.setButtonStyle("black", "16px", "#C30F0F", "10px", "none"));
        Button  tempButtonUpdate = Util.createButton("", 120, Util.setButtonStyle("black", "16px", "#5B8328", "10px", "none"));
        ImageView view;
        
        String deleteBtnStyle = tempButtonDel.getStyle()+"-fx-background-image: url('/images/deleteIcon.png');"
            + "-fx-background-size: 15 15;"
            + "-fx-background-repeat: no-repeat;"
                + "-fx-background-position: center;";
        String updateBtnStyle = tempButtonUpdate.getStyle()+"-fx-background-image: url('/images/updateIcon.png');"
            + "-fx-background-size: 15 15;"
            + "-fx-background-repeat: no-repeat;"
            + "-fx-background-position: center;";
        
         VBox bottom = new VBox(5);
                VBox picture = new VBox(0);//palitan siguro image pag meron na
                HBox bottomPart = new HBox();
                HBox buttonContainer = new HBox();
                String priceString = Double.toString(price);
                Label priceLabel = Util.createLabel("₱"+priceString, 13.5, "black","bold");
                
                Button deleteButton = Util.createButton("", 120, Util.setButtonStyle("black", "16px", "#C30F0F", "10px", "none"));
                Button updateButton = Util.createButton("", 120, Util.setButtonStyle("black", "16px", "#5B8328", "10px", "none"));
                deleteButton.setPrefSize(35, 1);
                updateButton.setPrefSize(35, 1);
                deleteButton.setStyle(deleteBtnStyle);
                updateButton.setStyle(updateBtnStyle);
                //deleteButton.setGraphic(view);
                
                buttonContainer.setPrefHeight(20);
                buttonContainer.getChildren().addAll(deleteButton, updateButton);
                
                buttonContainer.setMargin(deleteButton, btnInsets);
                buttonContainer.setMargin(updateButton, btnInsets);
                buttonContainer.setStyle("-fx-background-color:black; -fx-background-radius: 10px");
                
                
                bottomPart.getChildren().addAll(buttonContainer, priceLabel);
                bottomPart.setMargin(buttonContainer, new Insets(7,0,0,10));
                bottomPart.setMargin(priceLabel, new Insets(20,0,0,20));
                
                Label header = Util.createLabel(name, 13.5, "black","bold");
                Label descriptionLabel = Util.createLabel(description, 11.5, "black","normal");
                
                
                bottom.getChildren().addAll(header, descriptionLabel, bottomPart);
                bottom.setStyle("-fx-background-color: #EFF70096;"
                        + "-fx-background-radius: 10px;");
                picture.setStyle("-fx-background-color: black;"
                        + "-fx-background-radius: 20px;"
                        //+ "-fx-background-image: url('tourIMG/1903340_360.jpg');"
                        + "-fx-background-image: url('"+tourImage+"');"
                        + "-fx-background-size: cover;");
                //+ "-fx-background-image: url('images/tourIMG/'"+tourImage+"');"
                
                bottom.setMargin(header, new Insets(10,0,0,13));
                bottom.setMargin(descriptionLabel, new Insets(3,0,0,13));
                
                bottom.setPrefSize(210, 110);
                picture.setPrefSize(210, 170);

                setSpacing(2);
                setFillWidth(false);
                setSpacing(7);
                setAlignment(Pos.CENTER);
                setMinSize(220, 300);
                setStyle("-fx-background-color: white; -fx-background-radius: 10px; -fx-border-color: transparent;");
                getChildren().addAll(picture, bottom);
                
                setOnMouseEntered(e -> {highlightOnHover(header, descriptionLabel, priceLabel, this, true); System.out.println("PATH? :"+tourImage);});
                setOnMouseExited(e -> highlightOnHover(header, descriptionLabel, priceLabel, this, false));
                
                deleteButton.setOnAction(e->{
                    Alert alert = new Alert(Alert.AlertType.WARNING, "Are you sure you want to delete tour "+tourID, ButtonType.YES, ButtonType.NO);
                    Alert alertConfirm = new Alert(Alert.AlertType.CONFIRMATION, "Tour "+tourID+" Deleted", ButtonType.OK);
                    alert.showAndWait();
                    if(alert.getResult() == ButtonType.YES)
                        if(tourManager.deleteTour(tourID))
                        {
                            alertConfirm.show();
                            tours.getToursNode(AdminHome.borderPane);
                        }
                        else
                        {
                            alertConfirm.setAlertType(Alert.AlertType.WARNING);
                            alertConfirm.setContentText("There was an error during deletion.");
                            alertConfirm.show();
                        }
                            
                       //if(tourManager.deleteTourById(tourID))
                           
                });
                updateButton.setOnAction(e->{
                    new UpdateTour().getUpdateTourDialog(tours, tourID);
                });
        
    }
    
    private void highlightOnHover(Label header, Label description, Label priceLabel, VBox tourPreview, boolean highlight) {
        ScaleTransition scale = new ScaleTransition(Duration.millis(100), tourPreview);
        scale.setFromX(1);
        scale.setToX(highlight ? 1.1 : 1);
        scale.setFromY(1);
        scale.setToY(highlight ? 1.1 : 1);
        scale.play();
        
        
        
        /*if(highlight){
            header.setStyle(header.getStyle()+"-fx-text-fill: yellow;");
            description.setStyle(description.getStyle()+"-fx-text-fill: lightgray;");
            priceLabel.setStyle(priceLabel.getStyle()+"-fx-text-fill: gold;");
            tourPreview.setStyle(tourPreview.getStyle()+"-fx-background-color: white;");          
        }
        else{
            header.setStyle(header.getStyle()+"-fx-text-fill: white;");
            description.setStyle(description.getStyle()+"-fx-text-fill: white;");
            priceLabel.setStyle(priceLabel.getStyle()+"-fx-text-fill: white;");
            tourPreview.setStyle(tourPreview.getStyle()+"-fx-background-color: #333333;");
        }*/
    }
    
}