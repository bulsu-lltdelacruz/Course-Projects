package Project_TMS;

import database.TourManager;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;
import tour.Tour;

public class BookTours {
    
    private ScrollPane toursScrollPane = new ScrollPane();
    private FlowPane toursFlowPane;
    ImageView view;
    String labelStyle = "-fx-font-size: 12px; "
                + "-fx-text-fill: black; "
                + "-fx-font-weight: bold; "
                + "-fx-font-family: Arial; ";  
    
    public ScrollPane getBookToursNode(BorderPane borderPane) {
    
        toursFlowPane = new FlowPane(Orientation.HORIZONTAL,120, 60);
        toursFlowPane.setAlignment(Pos.CENTER);
        TourManager tourManager = new TourManager();
        
        List<TourPreviewUser> tourPreviews = new ArrayList<TourPreviewUser>();
        List<Tour> allTours = tourManager.getAllTours();
        
        Insets btnInsets = new Insets(5,7,5,7);
        Button  tempButtonDel = Util.createButton("", 120, Util.setButtonStyle("black", "16px", "#C30F0F", "10px", "none"));
        Button  tempButtonUpdate = Util.createButton("", 120, Util.setButtonStyle("black", "16px", "#5B8328", "10px", "none"));
        String deleteBtnStyle = tempButtonDel.getStyle()+"-fx-background-image: url('/images/deleteIcon.png');"
            + "-fx-background-size: 15 15;"
            + "-fx-background-repeat: no-repeat;"
                + "-fx-background-position: center;";
        String updateBtnStyle = tempButtonUpdate.getStyle()+"-fx-background-image: url('/images/updateIcon.png');"
            + "-fx-background-size: 15 15;"
            + "-fx-background-repeat: no-repeat;"
            + "-fx-background-position: center;";
        
        for(int i=0; i < allTours.size(); i++){
            String tourName = allTours.get(i).getName();
            String tourDescription = allTours.get(i).getDescription();
            double tourPrice = allTours.get(i).getBasePrice();
            String tourID = allTours.get(i).getId();
            String tourImage = allTours.get(i).getImagePath();
            
            tourPreviews.add(new TourPreviewUser(tourName, tourDescription, tourPrice, tourImage, tourID, borderPane, this));
            toursFlowPane.getChildren().add(tourPreviews.get(i));
        }
        toursFlowPane.setStyle("-fx-background-color: transparent");
        toursScrollPane.setStyle("-fx-background-color: transparent");
        
        Rectangle spacerTop = new Rectangle();
        Rectangle spacerBottom = new Rectangle();
            spacerTop.setHeight(50);
            spacerBottom.setHeight(50);
        
        VBox parentVBox = new VBox();
        parentVBox.setStyle("-fx-background-color: rgba(82, 89, 68, 1);");     
        parentVBox.getChildren().addAll(spacerTop,toursFlowPane,spacerBottom);
        toursScrollPane.setContent(parentVBox);
        toursScrollPane.setFitToWidth(true);
        toursScrollPane.setFitToHeight(true);
        
        return toursScrollPane;
    }
    

    
    

}

class TourPreviewUser extends VBox{

    String labelStyle = "-fx-font-size: 12px; "
                + "-fx-text-fill: black; "
                + "-fx-font-weight: bold; "
                + "-fx-font-family: Arial; ";  
    BookTours bookTours;
    
    public TourPreviewUser(String tourName, String tourDescription, double tourPrice, String tourImage, String tourID, BorderPane borderPane, BookTours bookTours) {
        this.bookTours = bookTours;
        VBox bottom = new VBox(5);
                VBox picture = new VBox(0);//palitan siguro image pag meron na
                HBox bottomPart = new HBox();
                HBox buttonContainer = new HBox();
                
                Label priceLabel = Util.createLabel("₱"+Double.toString(tourPrice), 13.5, "white","bold");
                
                Button bookButton = Util.createButton("Book", 100, Util.setButtonStyle("black", "16px", "rgba(25, 198, 36, 1)", "10px", "none"));
                
                
                buttonContainer.setPrefHeight(20);
                buttonContainer.getChildren().addAll(bookButton);
                
                buttonContainer.setStyle("-fx-background-color:black; -fx-background-radius: 10px");
                
                
                bottomPart.getChildren().addAll(buttonContainer, priceLabel);
                bottomPart.setMargin(buttonContainer, new Insets(7,0,0,10));
                bottomPart.setMargin(priceLabel, new Insets(20,0,0,20));
                
                Label header = Util.createLabel(tourName, 13.5, "white","bold");
                Label description = Util.createLabel(tourDescription, 11.5, "white","normal");
                
                
                bottom.getChildren().addAll(header, description, bottomPart);
                bottom.setStyle("-fx-background-color: black;"
                        + "-fx-background-radius: 10px;");
                picture.setStyle("-fx-background-color: black;"
                        + "-fx-background-radius: 20px;"
                        + "-fx-background-image: url('"+tourImage+"');"
                        + "-fx-background-size: cover;");
                
                bottom.setMargin(header, new Insets(10,0,0,13));
                bottom.setMargin(description, new Insets(3,0,0,13));
                
                bottom.setPrefSize(210, 110);
                picture.setPrefSize(210, 170);

                //actual container
                //tourPreviews[i][j] = new VBox();
                setCursor(Cursor.HAND);
                setFillWidth(false);
                setSpacing(7);
                setAlignment(Pos.CENTER);
                setMinSize(220, 300);
                setStyle("-fx-background-color: #333333; -fx-background-radius: 10px; -fx-border-color: transparent;");
                getChildren().addAll(picture, bottom);

                //int a=i,b=j;
                setOnMouseEntered(e -> highlightOnHover(header, description, priceLabel, this, true));
                setOnMouseExited(e -> highlightOnHover(header, description, priceLabel, this, false));
                
                bookButton.setOnAction(e -> proceedBooking(borderPane));
                bookButton.setOnMouseEntered(e -> bookButton.setStyle(bookButton.getStyle()+"-fx-background-color: rgba(119, 231, 126, 1);"));
                bookButton.setOnMouseExited(e -> bookButton.setStyle(bookButton.getStyle()+"-fx-background-color: rgba(25, 198, 36, 1);"));
                
                setOnMouseClicked(e -> {
                    Dialog<Void> dialog = showTourDetails(tourName, tourDescription, "₱"+Double.toString(tourPrice), borderPane);
                    dialog.showAndWait();
                });
    }
    
        private void highlightOnHover(Label header, Label description, Label priceLabel, VBox tourPreview, boolean highlight) {
        ScaleTransition scale = new ScaleTransition(Duration.millis(100), tourPreview);
        scale.setFromX(1);
        scale.setToX(highlight ? 1.1 : 1);
        scale.setFromY(1);
        scale.setToY(highlight ? 1.1 : 1);
        scale.play();
        
        if(highlight){
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
        }
    }
    
    private void proceedBooking(BorderPane borderPane){
        BookingForm booking = new BookingForm();
            borderPane.setCenter(booking.getBookingFormNode(borderPane, bookTours));
    }
    
    private Dialog<Void> showTourDetails(String titleText, String descriptionText, String priceText, BorderPane borderPane) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Tour Details");
        dialog.getDialogPane().setStyle("-fx-background-color: #333333;");
        
        ButtonType closeType = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType bookNowType = new ButtonType("Book Now", ButtonBar.ButtonData.OK_DONE);

        dialog.getDialogPane().getButtonTypes().addAll( closeType,bookNowType);

        Button bookNowButton = (Button) dialog.getDialogPane().lookupButton(bookNowType);
        bookNowButton.setDefaultButton(true);
        bookNowButton.setCursor(Cursor.HAND);
        bookNowButton.setStyle(Util.setButtonStyle("black", "14px", "rgba(25, 198, 36, 1)", "5px", "none"));
        bookNowButton.setOnMouseEntered(e -> bookNowButton.setStyle(bookNowButton.getStyle()+"-fx-background-color: rgba(119, 231, 126, 1);"));
        bookNowButton.setOnMouseExited(e -> bookNowButton.setStyle(bookNowButton.getStyle()+"-fx-background-color: rgba(25, 198, 36, 1);"));
        
        Button closeButton = (Button) dialog.getDialogPane().lookupButton(closeType);
        closeButton.setDefaultButton(false);
        closeButton.setStyle(Util.setButtonStyle("white", "13px", "rgba(255, 255, 255, 0.3)", "5px"));
        closeButton.setOnMouseEntered(e -> closeButton.setStyle(closeButton.getStyle()+"-fx-text-fill: black; -fx-background-color: rgba(255, 255, 255, 0.7);"));
        closeButton.setOnMouseExited(e -> closeButton.setStyle(closeButton.getStyle()+"-fx-text-fill: white; -fx-background-color: rgba(255, 255, 255, 0.3);"));

        VBox content = new VBox(50);
        content.setStyle("-fx-background-color: #333333;");
        content.setPadding(new Insets(20));
        content.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label("Title: " + titleText);
        title.setStyle(labelStyle);
        
        Label descpLabel = new Label("Description:");
        descpLabel.setStyle(labelStyle);

        Text descriptionTextNode = new Text(descriptionText);
        descriptionTextNode.setStyle("-fx-font-size: 14px;");

        TextFlow descriptionFlow = new TextFlow(descriptionTextNode);
        descriptionFlow.setTextAlignment(TextAlignment.JUSTIFY);
        descriptionFlow.setPrefWidth(500);
        descriptionFlow.setStyle("-fx-background-color: transparent;");

        VBox descriptionContainer = new VBox(10, descpLabel, descriptionFlow);

        Label price = new Label("Price: " + priceText);
        price.setStyle(labelStyle+"-fx-font-size: 18px;");
        
        HBox priceContainer = new HBox(price);
        priceContainer.setAlignment(Pos.CENTER_RIGHT);
        
        dialog.setResultConverter(button -> {
            if (button == bookNowType) {
                proceedBooking(borderPane);
            }
            return null;
        });

        content.getChildren().addAll(title, descriptionContainer, priceContainer);

        dialog.getDialogPane().setContent(content);
        dialog.setResizable(false);
        dialog.getDialogPane().setMinWidth(600);

        return dialog;
    }
    
}