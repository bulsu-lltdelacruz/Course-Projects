package Project_TMS;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import static javafx.scene.layout.HBox.setMargin;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class TourTracking extends ScrollPane{
    
    //Feedbacks
    //ScrollPane scroll = new ScrollPane();
    TTD[] feedbackDisplay = new TTD[10];
    Button[] filterButton = new Button[4];
    
    //Button addTour;
    
    VBox feedbackPane = new VBox(20);
    HBox filterButtonContainer = new HBox(30);
    Label label = new Label("Add Tour/Package");
    public TourTracking(){
        
        label.setStyle("-fx-text-fill: white;"
                + "-fx-font-size: 24px;"
                + "-fx-font-weight: 900;");
        
        feedbackPane.setStyle("-fx-background-color: #393232");
        setStyle("-fx-background-color: #393232");
        
        setFitToWidth(true);
        
        feedbackPane.setFillWidth(false);
        feedbackPane.setAlignment(Pos.CENTER);
        
        filterButton[0] = Util.createButton("All", 190, Util.setButtonStyle("black", "15px", "white", "100px", "none"));
        filterButton[1] = Util.createButton("Paid/Advance", 190, Util.setButtonStyle("black", "15px", "transparent", "100px", "none"));
        filterButton[2] = Util.createButton("Pending Payment", 190, Util.setButtonStyle("black", "15px", "transparent", "100px", "none"));
        filterButton[3] = Util.createButton("Waiting for Cancellation", 210, Util.setButtonStyle("black", "15px", "transparent", "100px", "none"));
        
        //addTour = Util.createButton("Add Tour", 120, Util.setButtonStyle("black", "13px", "#1DF700", "13px", "none"));
        
        for(int i = 0; i < filterButton.length; i++){
            
            int counter = i;
            filterButton[i].setOnAction(e->{
                /*switchTabs(borderPane, counter);*/
                changeButtonStyle(counter);
            filterButton[counter].setStyle(filterButton[1].getStyle()+"-fx-background-color: white;");
            });
        }
        
        filterButtonContainer.getChildren().addAll(filterButton[0],
                filterButton[1],
                filterButton[2],
                filterButton[3]);
        
        feedbackPane.getChildren().add(filterButtonContainer);
        //feedbackPane.getChildren().add(addTour);
        feedbackPane.setMargin(filterButtonContainer, new Insets(25,0,0,0));
        //feedbackPane.setMargin(addTour, new Insets(0,0,-10,760));
        
        for(int i = 0; i<feedbackDisplay.length; i++)
        {
            feedbackDisplay[i] = new TTD("Tour Name", "Tour Description", "Pending", "2023-04-04");
            feedbackPane.getChildren().add(feedbackDisplay[i]);
        }
        feedbackPane.setMargin(feedbackDisplay[0], new Insets(20,0,0,0));
        
        //initAddTourForm();
        
        setContent(feedbackPane);
        
        
        /*addTour.setOnAction(e->{
            setFitToHeight(true);
            
            feedbackPane.getChildren().removeAll(filterButtonContainer, addTour);
            for(int i = 0; i < feedbackDisplay.length; i++)
            {
                feedbackPane.getChildren().remove(feedbackDisplay[i]);
            }
            feedbackPane.getChildren().add(label);
            feedbackPane.getChildren().add(addTourPane);
            feedbackPane.setMargin(label, new Insets(30,0,20,0));
        });*/
        
        
    }
    
    /*VBox addTourPane = new VBox(10);
    
    private void initAddTourForm(){
        
        addTourPane.setFillWidth(false);
        
        String textFieldStyle = "-fx-border-radius: 15px;"
                + "-fx-background-radius: 15px";
        
        TextField tourNameF = new TextField();
        TextField tourDestinationF = new TextField();
        TextField tourPriceF = new TextField();
        TextField tourActF = new TextField();
        TextField tourDetailsF = new TextField();
        
        tourNameF.setStyle(textFieldStyle);
        tourDestinationF.setStyle(textFieldStyle);
        tourPriceF.setStyle(textFieldStyle);
        tourActF.setStyle(textFieldStyle);
        tourDetailsF.setStyle(textFieldStyle);
        
        tourNameF.setPrefSize(260, 30);
        tourDestinationF.setPrefSize(260, 30);
        tourPriceF.setPrefSize(260, 30);
        tourActF.setPrefSize(260, 80);
        tourDetailsF.setPrefSize(260, 140);
        
        Label[] label = new Label[5];
        HBox[] formContainer = new HBox[5];
        
        for(int i = 0; i < label.length; i++)
        {
            label[i] = new Label();
            label[i].setPrefSize(195, 30);
            label[i].setStyle("-fx-text-fill: black;"
                    + "-fx-font-size: 18px;"
                    + "-fx-font-weight: 700;");
            formContainer[i] = new HBox(20);
            
            formContainer[i].setFillHeight(false);
        }
        
        Button confirmButton = Util.createButton("Confirm", 200, Util.setButtonStyle("black", "16px", "#00A30B", "40px", "none"));
        Button cancelButton = Util.createButton("Cancel", 200, Util.setButtonStyle("black", "16px", "#F54242", "40px", "none"));
        
        formContainer[0].getChildren().addAll(label[0], tourNameF);
        formContainer[1].getChildren().addAll(label[1], tourDestinationF);
        formContainer[2].getChildren().addAll(label[2], tourPriceF);
        formContainer[3].getChildren().addAll(label[3], tourActF);
        formContainer[4].getChildren().addAll(label[4], tourDetailsF);
        
        label[0].setText("Tour Name:");
        label[1].setText("Tour Destination:");
        label[2].setText("Tour Price");
        label[3].setText("Tour Activity");
        label[4].setText("Tour Details");
        
        addTourPane.setPrefSize(510, 450);
        addTourPane.setAlignment(Pos.CENTER);
        addTourPane.setStyle(textFieldStyle);
        addTourPane.setStyle("-fx-background-color: #BFBFBC;"
                + textFieldStyle);
        HBox buttonContainer = new HBox(40);
        buttonContainer.setFillHeight(false);
        buttonContainer.getChildren().addAll(confirmButton, cancelButton);
        buttonContainer.setPrefWidth(500);
        addTourPane.getChildren().addAll(formContainer[0], formContainer[1], formContainer[2], formContainer[3], formContainer[4], buttonContainer);
        addTourPane.setMargin(buttonContainer, new Insets(0,0,0,35));
        for(int i = 0; i < label.length; i++){
            addTourPane.setMargin(formContainer[i], new Insets(0,0,0,5));
        }
        
        cancelButton.setOnAction(e->{
            setFitToHeight(false);
            feedbackPane.getChildren().removeAll(this.label, addTourPane);
            feedbackPane.getChildren().add(filterButtonContainer);
            feedbackPane.getChildren().add(addTour);
            for(int i = 0; i<feedbackDisplay.length; i++)
            {
                feedbackPane.getChildren().add(feedbackDisplay[i]);
            }

        });
    }*/ 
    
        private void changeButtonStyle(int index){
        for(int i=0; i<filterButton.length; i++){
            if(i == index)
                filterButton[i].setStyle(Util.setButtonStyle("black", "15px", "white", "100px", "none"));
            else
                filterButton[i].setStyle(Util.setButtonStyle("black", "15px", "transparent", "100px", "none"));
        }
    }
    
       
}



class TTD extends HBox{

    public TTD(String tourName, String tourDescription, String status, String bookDate) {
        
        Pane imagePane = new Pane();
        VBox infoPane = new VBox(0);
        VBox detailsPane = new VBox(); 
        VBox descriptionPane = new VBox();
        
        Label tourLabel = new Label();
        Label descriptionLabel = new Label();
        Label statusLabel = new Label();
        Label bookDateLabel = new Label();
        
        tourLabel.setText(tourName);
        descriptionLabel.setText(tourDescription);
        statusLabel.setText("Status: "+status);
        bookDateLabel.setText("Book Date: "+bookDate);
        
        Button refundButton = Util.createButton("Refund", 120, Util.setButtonStyle("black", "10px", "#1DF700", "10px", "none"));
        Button cancelButton = Util.createButton("Cancel", 120, Util.setButtonStyle("black", "10px", "#EE2F32", "10px", "none"));
        
        setFillHeight(false);
        setAlignment(Pos.CENTER_LEFT);
        setSpacing(20);
        
        detailsPane.setFillWidth(false);
        detailsPane.setAlignment(Pos.CENTER);
        
        setPrefSize(900, 140);
        
        refundButton.setPrefSize(100, 20);
        cancelButton.setPrefSize(100, 20);
        infoPane.setPrefSize(500, 120);
        detailsPane.setPrefSize(450, 110);
        descriptionPane.setPrefSize(466, 70);
        imagePane.setPrefSize(400, 115);
        
        tourLabel.setPrefWidth(230);
        bookDateLabel.setPrefWidth(230);
        bookDateLabel.setAlignment(Pos.CENTER_RIGHT);
        statusLabel.setPrefWidth(270);
        
        String labelStyle = "-fx-text-fill: black;"
                 + "-fx-font-size: 12px;"
                + "-fx-font-weight: 800;";
        
        tourLabel.setStyle(labelStyle); 
        descriptionLabel.setStyle("-fx-text-fill: white;"
                 + "-fx-font-size: 10px;"
                + "-fx-font-weight: 800");
        statusLabel.setStyle(labelStyle);
        bookDateLabel.setStyle(labelStyle);
        
        descriptionPane.setStyle("-fx-background-color: black;"
                + "-fx-background-radius: 10px;"); 
        
        detailsPane.setStyle("-fx-background-color: rgba(29,247,0, 0.42);"
                + "-fx-background-radius: 10px;");
        imagePane.setStyle(""
                        + "-fx-background-color: black;"
                        + "-fx-background-image: url('/images/homebg.jpg');"
                        + "-fx-background-radius: 20px;"
                        + "-fx-border-radius: 20px;"
                        + "-fx-background-size: cover;");
        
        setStyle("-fx-background-color: #D9D9D9;"
                + "-fx-background-radius: 10px");
        
        
        descriptionPane.getChildren().add(descriptionLabel);
        descriptionPane.setMargin(descriptionLabel, new Insets(5,0,0,5));
        
        HBox bottom = new HBox();
        HBox top = new HBox();
        bottom.setAlignment(Pos.CENTER_RIGHT);
        
        top.getChildren().addAll(tourLabel, bookDateLabel);
        
        bottom.getChildren().addAll(statusLabel, refundButton, cancelButton);
        
        detailsPane.getChildren().addAll(top,descriptionPane);
        
        infoPane.getChildren().addAll(detailsPane, bottom);
        
        getChildren().add(imagePane);
        getChildren().add(infoPane);
        
        setMargin(imagePane, new Insets(0,0,0,10));
        setMargin(infoPane, new Insets(0,10,0,0));
    }
    
    
}
