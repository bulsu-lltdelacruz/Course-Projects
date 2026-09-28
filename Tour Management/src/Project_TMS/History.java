package Project_TMS;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;

public class History extends ScrollPane{
    
    TourTrackingDisplay[] feedbackDisplay = new TourTrackingDisplay[10];
    Button[] filterButton = new Button[4];
    
    VBox feedbackPane = new VBox(20);
    HBox filterButtonContainer = new HBox(30);
    
    
    public History(){
        
        feedbackPane.setStyle("-fx-background-color: rgba(82, 89, 68, 1)");
        setStyle("-fx-background-color: rgba(82, 89, 68, 1)");
        
        
        setFitToWidth(true);
        feedbackPane.setFillWidth(false);
        feedbackPane.setAlignment(Pos.CENTER);
        
        filterButton[0] = Util.createButton("All", 190, Util.setButtonStyle("black", "15px", "white", "100px", "none"));
        filterButton[1] = Util.createButton("Cancelled", 190, Util.setButtonStyle("black", "15px", "transparent", "100px", "none"));
        filterButton[2] = Util.createButton("Unpaid", 190, Util.setButtonStyle("black", "15px", "transparent", "100px", "none"));
        filterButton[3] = Util.createButton("Paid", 190, Util.setButtonStyle("black", "15px", "transparent", "100px", "none"));
        
        for(int i = 0; i < filterButton.length; i++){
            
            int counter = i;
            filterButton[i].setOnAction(e->{
                changeButtonStyle(counter);
            filterButton[counter].setStyle(filterButton[1].getStyle()+"-fx-background-color: white;");
            });
        }
        
        filterButtonContainer.getChildren().addAll(filterButton[0],
                filterButton[1],
                filterButton[2],
                filterButton[3]);
        
        feedbackPane.getChildren().add(filterButtonContainer);
        feedbackPane.setMargin(filterButtonContainer, new Insets(25,0,0,0));
        
        for(int i = 0; i<feedbackDisplay.length; i++){
            feedbackDisplay[i] = new TourTrackingDisplay("Tour Name", "Tour Description", "Pending", "2023-04-04", Util.formatCurrency(9999));
            feedbackPane.getChildren().add(feedbackDisplay[i]);
        }for(int i = 0; i<feedbackDisplay.length; i++){
            feedbackDisplay[i] = new TourTrackingDisplay("Tour Name", "Tour Description", "Pending", "2023-04-04", Util.formatCurrency(9999));
            feedbackPane.getChildren().add(feedbackDisplay[i]);
        }
        feedbackPane.setMargin(feedbackDisplay[0], new Insets(20,0,0,0));
        
        setContent(feedbackPane);
        
    }
    
        private void changeButtonStyle(int index){
            for(int i=0; i<filterButton.length; i++){
                if(i == index)
                    filterButton[i].setStyle(Util.setButtonStyle("black", "15px", "white", "100px", "none"));
                else
                    filterButton[i].setStyle(Util.setButtonStyle("black", "15px", "transparent", "100px", "none"));
            }
        
        }
    
}

class TourTrackingDisplay extends HBox{

    public TourTrackingDisplay(String tourName, String tourDescription, String status, String bookDate, String price) {
        setFillHeight(false);
        setAlignment(Pos.CENTER_LEFT);
        setSpacing(20);
        setPrefSize(900, 155);
        
        
        Pane imagePane = new Pane();
        VBox infoPane = new VBox(0);
        VBox detailsPane = new VBox(); 
        VBox descriptionPane = new VBox();
        HBox pricePane = new HBox();
        pricePane.setPadding(new Insets(5,10,10,0));
        pricePane.setAlignment(Pos.CENTER_RIGHT);
        BorderPane descriptionNpricePane = new BorderPane();
        descriptionNpricePane.setStyle("-fx-background-color: black; -fx-background-radius: 10px;");
        descriptionNpricePane.setTop(descriptionPane);
        descriptionNpricePane.setBottom(pricePane);
        
        Label tourLabel = new Label(tourName);
        Label descriptionLabel = new Label(tourDescription);
        Label priceLabel = new Label(price);
        Label statusLabel = new Label("Status: "+status);
        Label bookDateLabel = new Label("Book Date: "+bookDate);
       
        //buttons
        Button feedbackButton = Util.createButton("Leave a Feedback", 130, Util.setButtonStyle("white", "10px", "rgba(1, 17, 6, 0.77)", "10px", "none"));
        Button payButton = Util.createButton("Pay", 120, Util.setButtonStyle("black", "14px", "#1DF700", "10px", "none"));
        Button cancelButton = Util.createButton("Cancel", 120, Util.setButtonStyle("black", "14px", "#EE2F32", "10px", "none"));
        payButton.setPrefHeight(30);
        cancelButton.setPrefHeight(30);
        feedbackButton.setPrefHeight(15);
        
        feedbackButton.setOnAction(e ->{ 
            Dialog <Void> comsec = showTourDetails();
            comsec.showAndWait();
                });
        
        HBox buttonContainer = new HBox(cancelButton,payButton);
        buttonContainer.setPrefHeight(50);
        buttonContainer.setSpacing(7);   
        
        detailsPane.setFillWidth(false);
        detailsPane.setAlignment(Pos.CENTER);
        
        infoPane.setPrefSize(500, 120);
        detailsPane.setPrefSize(450, 110);
        descriptionPane.setPrefSize(466, 40);
        imagePane.setPrefSize(400, 115);
        
        tourLabel.setPrefWidth(230);
        bookDateLabel.setPrefWidth(150);
        statusLabel.setPrefWidth(270);
        
        String labelStyle = "-fx-text-fill: black;"
                 + "-fx-font-size: 12px;"
                + "-fx-font-weight: 800;"
                + "-fx-font-family: Arial;";
        
        tourLabel.setStyle(labelStyle); 
        descriptionLabel.setStyle("-fx-text-fill: white;"
                 + "-fx-font-size: 10px;"
                + "-fx-font-weight: 800");
        statusLabel.setStyle(labelStyle);
        bookDateLabel.setStyle(labelStyle);
        priceLabel.setStyle(labelStyle+"-fx-font-weight: bold; -fx-text-fill: green; -fx-font-size: 16px;");
        
        
        detailsPane.setStyle("-fx-background-color: rgba(29,247,0, 0.42);"
                + "-fx-background-radius: 10px;");
        imagePane.setStyle("-fx-background-color: black;"
                        + "-fx-background-image: url('/images/homebg.jpg');"
                        + "-fx-background-radius: 20px;"
                        + "-fx-border-radius: 20px;"
                        + "-fx-background-size: cover;");
        
        setStyle("-fx-background-color: #D9D9D9;"
                + "-fx-background-radius: 10px");
        
        
        descriptionPane.getChildren().add(descriptionLabel);
        pricePane.getChildren().add(priceLabel);
        descriptionPane.setMargin(descriptionLabel, new Insets(5,0,0,5));
        
        HBox bottom = new HBox();
        HBox feedbackNdateContainer = new HBox(feedbackButton,bookDateLabel);
        feedbackNdateContainer.setSpacing(7);
        
        HBox top = new HBox();
        top.setPadding(new Insets(2,5,2,5));
        bottom.setPadding(new Insets(10,0,0,0));
        bottom.setAlignment(Pos.CENTER_RIGHT);
        
        top.getChildren().addAll(tourLabel, feedbackNdateContainer);
        
        bottom.getChildren().addAll(statusLabel, buttonContainer);
        
        detailsPane.getChildren().addAll(top,descriptionNpricePane);
        
        infoPane.getChildren().addAll(detailsPane, bottom);
        
        getChildren().add(imagePane);
        getChildren().add(infoPane);
        
        setMargin(imagePane, new Insets(0,0,0,10));
        setMargin(infoPane, new Insets(0,10,0,0));
    }
    
     private Dialog<Void> showTourDetails() {
            
            String labelStyle = "-fx-font-size: 12px; "
                + "-fx-text-fill: black; "
                + "-fx-font-weight: bold; "
                + "-fx-font-family: Arial; "; 
            
            Dialog<Void> dialog = new Dialog<>();
            dialog.setTitle("Feedback");
            dialog.getDialogPane().setStyle("-fx-background-color: gray;");

            ButtonType closeType = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);
            ButtonType sendType = new ButtonType("Send", ButtonBar.ButtonData.OK_DONE);

            dialog.getDialogPane().getButtonTypes().addAll( closeType,sendType);

            Button sendButton = (Button) dialog.getDialogPane().lookupButton(sendType);
            sendButton.setDefaultButton(true);
            sendButton.setCursor(Cursor.HAND);
            sendButton.setStyle(Util.setButtonStyle("black", "14px", "rgba(25, 198, 36, 1)", "5px", "none"));
            sendButton.setOnMouseEntered(e -> sendButton.setStyle(sendButton.getStyle()+"-fx-background-color: rgba(119, 231, 126, 1);"));
            sendButton.setOnMouseExited(e -> sendButton.setStyle(sendButton.getStyle()+"-fx-background-color: rgba(25, 198, 36, 1);"));

            Button closeButton = (Button) dialog.getDialogPane().lookupButton(closeType);
            closeButton.setDefaultButton(false);
            closeButton.setStyle(Util.setButtonStyle("white", "13px", "rgba(255, 255, 255, 0.3)", "5px"));
            closeButton.setOnMouseEntered(e -> closeButton.setStyle(closeButton.getStyle()+"-fx-text-fill: black; -fx-background-color: rgba(255, 255, 255, 0.7);"));
            closeButton.setOnMouseExited(e -> closeButton.setStyle(closeButton.getStyle()+"-fx-text-fill: white; -fx-background-color: rgba(255, 255, 255, 0.3);"));

            VBox content = new VBox(10);
            content.setStyle("-fx-background-color: gray;");
            content.setPadding(new Insets(20));
            content.setAlignment(Pos.CENTER_LEFT);

            Label title = new Label("Title: Siargo Island");
            title.setStyle(labelStyle);

            Label descpLabel = new Label("Feedback:");
            descpLabel.setStyle(labelStyle);

            TextArea commentArea = new TextArea();
            commentArea.setStyle("-fx-font-size: 14px;");
            commentArea.setPrefHeight(200);
            commentArea.setWrapText(true);

            VBox descriptionContainer = new VBox(10, descpLabel, commentArea);

            dialog.setResultConverter(button -> {
                if (button == sendType) {
                 
                }
                return null;
            });

            content.getChildren().addAll(title, descriptionContainer);

            dialog.getDialogPane().setContent(content);
            dialog.setResizable(false);
            dialog.getDialogPane().setMinWidth(600);

            return dialog;
        }
    
}