package Project_TMS;


import java.util.Optional;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class BookingForm {
    
    public GridPane getBookingFormNode(BorderPane borderPane,BookTours bookTours){
        
        String labelStyle = "-fx-font-size: 13px;"
                + "-fx-text-fill: white;"
                + "-fx-font-weight: bold;"
                + "-fx-font-family: Arial;";
        
        String fieldStyle = "-fx-font-size: 13px;"
                + "-fx-text-fill: black;"
                + "-fx-font-family: Arial;";
        
        
         ColumnConstraints c1 = new ColumnConstraints();
            c1.setPercentWidth(50);
         ColumnConstraints c2 = new ColumnConstraints();
            c2.setPercentWidth(50);
         RowConstraints r = new RowConstraints();
            r.setPercentHeight(10);
         RowConstraints r1 = new RowConstraints();
            r1.setPercentHeight(100);
         RowConstraints r2 = new RowConstraints();
            r2.setPercentHeight(10);
         GridPane gridCenter = new GridPane(10, 0);
            gridCenter.getColumnConstraints().addAll(c1,c2);
            gridCenter.getRowConstraints().addAll(r,r1,r2);  
            gridCenter.setStyle("-fx-background-color: gray;");
            gridCenter.setPadding(new Insets(10,20,80,20));
            gridCenter.setMaxWidth(1200);
            
            
        Label tourNameLabel = new Label("Boracay Island");
        tourNameLabel.setStyle(labelStyle+"-fx-font-size: 40px;");
        HBox headerContainer = new HBox(tourNameLabel);
            
        //LEFT SIDE HOLDER OF ALL FIELDS CONTAINER
        VBox leftContainer = new VBox();
            leftContainer.setStyle("-fx-background-color: transparent;");
            leftContainer.setSpacing(10);
            leftContainer.setPadding(new Insets(10,10,10,10));
            
            
            //TOP LEFT SIDE
            VBox topBoxLeft = new VBox();
                topBoxLeft.setStyle("-fx-background-color: black;  -fx-background-radius: 12px; -fx-border-style: none;");
                topBoxLeft.setPrefHeight(300);
                
                Label descriptionLabel = new Label("Tour Description:");
                descriptionLabel.setStyle(labelStyle);
                TextArea descriptionArea = new TextArea();
                descriptionArea.setStyle(fieldStyle);
                descriptionArea.setWrapText(true);
                descriptionArea.setPrefRowCount(5);

                topBoxLeft.setPadding(new Insets(15));
                topBoxLeft.setSpacing(10);
                topBoxLeft.getChildren().addAll(descriptionLabel, descriptionArea);

                
            //CENTER LEFT SIDE    
            VBox centerBoxLeft = new VBox();
                centerBoxLeft.setStyle("-fx-background-color: black;  -fx-background-radius: 12px; -fx-border-style: none;");
                centerBoxLeft.setPrefHeight(400);
                
                Label numTouristLabel = new Label("Number of Tourists:");
                numTouristLabel.setStyle(labelStyle);
                Spinner<Integer> numTouristSpinner = new Spinner<>(1, 100, 1);
                numTouristSpinner.setStyle(fieldStyle); 
                numTouristSpinner.setPrefWidth(Double.MAX_VALUE);

                Label dateLabel = new Label("Reservation Date:");
                dateLabel.setStyle(labelStyle);
                DatePicker datePicker = new DatePicker();
                datePicker.setStyle(fieldStyle);
                datePicker.setPrefWidth(Double.MAX_VALUE);

                Label transportLabel = new Label("Transportation:");
                transportLabel.setStyle(labelStyle);
                ComboBox<String> transportCombo = new ComboBox<>();
                transportCombo.getItems().addAll("Van", "Bus");
                transportCombo.setStyle(fieldStyle);
                transportCombo.setPrefWidth(Double.MAX_VALUE);

                Label accommodationLabel = new Label("Accommodation:");
                accommodationLabel.setStyle(labelStyle);
                ComboBox<String> accommodationCombo = new ComboBox<>();
                accommodationCombo.getItems().addAll("Hotel", "Inn", "SOGO hahahah");
                accommodationCombo.setStyle(fieldStyle);
                accommodationCombo.setPrefWidth(Double.MAX_VALUE);
                
                Label tourDurationLabel = new Label("Tour Duration:");
                tourDurationLabel.setStyle(labelStyle);
                TextField tourDurationField = new TextField();
                tourDurationField.setStyle(fieldStyle);
            
            
            

                centerBoxLeft.setPadding(new Insets(15));
                centerBoxLeft.getChildren().addAll(
                    numTouristLabel, numTouristSpinner,
                    dateLabel, datePicker,
                    transportLabel, transportCombo,
                    accommodationLabel, accommodationCombo, 
                    tourDurationLabel, tourDurationField
                );

                    
                
            //BOTTOM LEFT SIDE
            HBox bottomBoxLeft = new HBox();
            bottomBoxLeft.setPrefHeight(250);
            bottomBoxLeft.setStyle("-fx-background-color: black; -fx-background-radius: 12px; -fx-border-style: none;");

            // Create left and right spacers (expandable Regions)
            Region leftSpacer = new Region();
            Region rightSpacer = new Region();
            HBox.setHgrow(leftSpacer, Priority.ALWAYS);
            HBox.setHgrow(rightSpacer, Priority.ALWAYS);

            VBox addOnsContainer = new VBox();
            addOnsContainer.setPadding(new Insets(15));
            addOnsContainer.setSpacing(10);
            addOnsContainer.setAlignment(Pos.CENTER);

            Label addOnsLabel = new Label("Add-Ons");
            addOnsLabel.setStyle(labelStyle + "-fx-font-size: 18px;");

            CheckBox tourGuideCheck = new CheckBox("Tour Guide");
            tourGuideCheck.setStyle(labelStyle);

            CheckBox mealsCheck = new CheckBox("Meals");
            mealsCheck.setStyle(labelStyle);

            HBox cbContainer = new HBox(tourGuideCheck, mealsCheck);
            cbContainer.setSpacing(10);  // Optional: Add spacing between checkboxes
            cbContainer.setAlignment(Pos.CENTER);

            addOnsContainer.getChildren().addAll(addOnsLabel, cbContainer);

            // Add spacers and content to HBox
            bottomBoxLeft.getChildren().addAll(leftSpacer, addOnsContainer, rightSpacer);


            leftContainer.getChildren().addAll(topBoxLeft, centerBoxLeft, bottomBoxLeft);
            
             
            
        //RIGHT SIDE HOLDER OF ALL FIELDS CONTAINER
        VBox rightContainer = new VBox();
            rightContainer.setStyle("-fx-background-color: transparent; ");
            rightContainer.setSpacing(10);
            rightContainer.setPadding(new Insets(10,10,10,10));
            
            //TOP RIGHT
            VBox topBoxRight = new VBox();
                topBoxRight.setStyle("-fx-background-color: black;  -fx-background-radius: 12px; -fx-border-style: none;");
                topBoxRight.setPrefHeight(250);

                Label activitiesLabel = new Label("Activities:");
                activitiesLabel.setStyle(labelStyle);
                TextArea activitiesArea = new TextArea();
                activitiesArea.setStyle(fieldStyle);
                activitiesArea.setWrapText(true);
                activitiesArea.setPrefRowCount(5);
                
                topBoxRight.setPadding(new Insets(15));
                topBoxRight.setSpacing(10);
                topBoxRight.getChildren().addAll(activitiesLabel, activitiesArea);
                
            //CENTER RIGHT
            VBox centerBoxRight = new VBox();
                centerBoxRight.setStyle("-fx-background-color: black;  -fx-background-radius: 12px; -fx-border-style: none;");
                centerBoxRight.setPrefHeight(350);
                
            Label contactPersonHeading = new Label("Contact Person Details");
            contactPersonHeading.setStyle(labelStyle+"-fx-font-size: 25px;");
            contactPersonHeading.setAlignment(Pos.CENTER_RIGHT);         
            
            Label firstNameLabel = new Label("First Name:");
            firstNameLabel.setStyle(labelStyle);
            TextField firstNameField = new TextField();
            firstNameField.setStyle(fieldStyle);
            
            Label lastNameLabel = new Label("Last Name:");
            lastNameLabel.setStyle(labelStyle);
            TextField lastNameField = new TextField();
            lastNameField.setStyle(fieldStyle);
            
            Label contactNumberLabel = new Label("Contact Number:");
            contactNumberLabel.setStyle(labelStyle);
            TextField contactNumberField = new TextField();
            contactNumberField.setStyle(fieldStyle);
            
            Label emailAddressLabel = new Label("Email Address:");
            emailAddressLabel.setStyle(labelStyle);

            TextField emailAddressField = new TextField();
            emailAddressField.setStyle(fieldStyle);

            String emailRegex = "^[\\w.-]+@[\\w.-]+\\.\\w{2,}$";

            emailAddressField.focusedProperty().addListener((obs, oldVal, newVal) -> {
                if (!newVal) {
                    String input = emailAddressField.getText();
                    if (!input.matches(emailRegex)) {
                        emailAddressField.setStyle(fieldStyle + "-fx-border-color: red; -fx-text-fill: red;");
                    } 
                    else {
                        emailAddressField.setStyle(fieldStyle);
                    }
                }
            });

            
            centerBoxRight.setPadding(new Insets(15));
            centerBoxRight.getChildren().addAll(contactPersonHeading,
                    firstNameLabel, firstNameField, 
                    lastNameLabel, lastNameField, 
                    contactNumberLabel, contactNumberField, 
                    emailAddressLabel, emailAddressField);
                
            //BOTTOM RIGHT
            VBox bottomBoxRight = new VBox();
                bottomBoxRight.setPrefHeight(300);
                bottomBoxRight.setStyle("-fx-background-color: black;  -fx-background-radius: 12px; -fx-border-style: none;");
            
                bottomBoxRight.setPadding(new Insets(15));
                
                Label additionalRequestLabel = new Label("Additional Request");
                additionalRequestLabel.setStyle(labelStyle);

                TextArea additionalRequestArea = new TextArea();
                additionalRequestArea.setStyle(fieldStyle);
                additionalRequestArea.setWrapText(true);
                additionalRequestArea.setPrefRowCount(5);

            bottomBoxRight.getChildren().addAll(additionalRequestLabel, additionalRequestArea);


            rightContainer.getChildren().addAll(topBoxRight, centerBoxRight, bottomBoxRight);
            



             
            HBox buttonContainer = new HBox();
                buttonContainer.setAlignment(Pos.CENTER_RIGHT);
                buttonContainer.setSpacing(15);
                buttonContainer.setScaleX(1.038);
                buttonContainer.setMaxWidth(685);
                buttonContainer.setPadding(new Insets(10,20,10,10));
                
                
            Button confirmButton = Util.createButton("Confirm", 120, Util.setButtonStyle("black", "13px", "rgba(25, 198, 36, 1)", "5px"));
                 
            Button cancelButton = Util.createButton("Cancel", 75, Util.setButtonStyle("white", "13px", "rgba(255, 255, 255, 0.3)", "5px"));

//for testing lang
TextField tourTypeField = new TextField("Adventure");
TextField destinationField = new TextField("Boracay Island");
TextField fn = new TextField("Limuel");
TextField ls = new TextField("Nigger");
TextField contactField = new TextField("09239239223");
TextField emailField = new TextField("adsadad@gmail.com");
TextField durationField = new TextField("5");
TextField touristCountField = new TextField("10");
TextField dateField = new TextField("2023-08-04");

ComboBox<String> t = new ComboBox<>();
t.getItems().addAll("Helicopter", "Van", "Bus");
t.setValue("Helicopter");

ComboBox<String> a = new ComboBox<>();
a.getItems().addAll("Kubo", "Hotel", "Inn");
a.setValue("Kubo");

CheckBox tt = new CheckBox();
tt.setSelected(true);

CheckBox m = new CheckBox();
m.setSelected(false);

TextArea requestArea = new TextArea("wala naman");

confirmButton.setOnAction(e -> showUserFilledDetails(borderPane,
    tourTypeField, destinationField,
    fn, ls, contactField, emailField,
    durationField, touristCountField, dateField,
    t, a,
    tt, m,
    requestArea
));

    cancelButton.setOnAction(e -> 
    {Home.borderPane.setCenter(bookTours.getBookToursNode(Home.borderPane));}
    );
            
            confirmButton.setOnMouseEntered(e -> confirmButton.setStyle(Util.setButtonStyle("black", "13px", "rgba(119, 231, 126, 1)", "5px")));
            confirmButton.setOnMouseExited(e -> confirmButton.setStyle(Util.setButtonStyle("black", "13px", "rgba(25, 198, 36, 1)", "5px")));
            
            //cancelButton.setOnAction();
            cancelButton.setOnMouseEntered(e -> cancelButton.setStyle(Util.setButtonStyle("black", "13px", "rgba(255, 255, 255, 0.7)", "5px")));
            cancelButton.setOnMouseExited(e -> cancelButton.setStyle(Util.setButtonStyle("white", "13px", "rgba(255, 255, 255, 0.3)", "5px")));
            
            
            buttonContainer.getChildren().addAll(cancelButton, confirmButton);
            
            
            gridCenter.add(headerContainer, 0, 0);
            gridCenter.add(leftContainer, 0, 1);
            gridCenter.add(rightContainer, 1, 1);
            gridCenter.add(buttonContainer,1,2);
        
        return gridCenter;
    }
    

    

    private void showUserFilledDetails(BorderPane borderPane,
        TextField tourTypeField, TextField destinationField,
        TextField firstNameField, TextField lastNameField, TextField contactField, TextField emailField,
        TextField durationField, TextField touristCountField, TextField dateField,
        ComboBox<String> transportCombo, ComboBox<String> accommodationCombo,
        CheckBox tourGuideCheck, CheckBox mealsCheck,
        TextArea requestArea
    ) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Confirm Tour Booking");
        dialog.setHeaderText("Please review the entered details below:");

        VBox mainVBox = new VBox(15);
        mainVBox.setStyle("-fx-background-color: gray;");
        mainVBox.setPadding(new Insets(20));
        
        // === Section: Tour Details ===
        Label tourDetailsLabel = new Label("Tour Details");
        tourDetailsLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        HBox tourTypeBox = createFieldRow("Tour Type:", tourTypeField.getText());
        HBox tourDestBox = createFieldRow("Tour Destination:", destinationField.getText());

        // === Section: Contact Person Details ===
        Label contactLabel = new Label("Contact Person Details");
        contactLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        HBox fnameBox = createFieldRow("First Name:", firstNameField.getText());
        HBox lnameBox = createFieldRow("Last Name:", lastNameField.getText());
        HBox contactBox = createFieldRow("Contact No:", contactField.getText());
        HBox emailBox = createFieldRow("Email Address:", emailField.getText());

        // === Section: Tour Info ===
        HBox durationBox = createFieldRow("Tour Duration:", durationField.getText());
        HBox touristsBox = createFieldRow("Number of Tourist:", touristCountField.getText());
        HBox dateBox = createFieldRow("Reservation Date:", dateField.getText());
        HBox transportBox = createFieldRow("Transportation:", transportCombo.getValue());
        HBox accomBox = createFieldRow("Accommodation:", accommodationCombo.getValue());

        // === Section: Add-Ons ===
        Label addOnsLabel = new Label("Add-Ons");
        addOnsLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        HBox addOnBox = new HBox(20);
        addOnBox.getChildren().addAll(
            new Label("Tour Guide: " + (tourGuideCheck.isSelected() ? "Yes" : "No")),
            new Label("Meals: " + (mealsCheck.isSelected() ? "Yes" : "No"))
        );

        // === Section: Additional Request ===
        Label reqLabel = new Label("Additional Request:");
        TextArea reqArea = new TextArea(requestArea.getText());
        reqArea.setEditable(false);
        reqArea.setWrapText(true);
        reqArea.setPrefHeight(80);
        reqArea.setStyle("-fx-control-inner-background: #f0f0f0;");

        // Add everything to main layout
        mainVBox.getChildren().addAll(
            tourDetailsLabel, tourTypeBox, tourDestBox,
            contactLabel, fnameBox, lnameBox, contactBox, emailBox,
            durationBox, touristsBox, dateBox, transportBox, accomBox,
            addOnsLabel, addOnBox,
            reqLabel, reqArea
        );

        dialog.getDialogPane().setContent(mainVBox);

        // Add buttons
        ButtonType confirmButton = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButton, cancelButton);

        dialog.showAndWait().ifPresent(response -> {
            if (response == confirmButton) {
                System.out.println("Booking confirmed.");
                proceedToPayment(borderPane);
            }
            else {
                System.out.println("Booking cancelled.");
            }
        });
    }

    private HBox createFieldRow(String labelText, String valueText) {
        Label label = new Label(labelText);
        label.setPrefWidth(150);
        label.setStyle("-fx-font-weight: bold; -fx-text-fill: white;");
        Label value = new Label(valueText);
        value.setStyle("-fx-font-weight: bold; -fx-text-fill: white;");
        HBox row = new HBox(10, label, value);
        
        return row;
    }
    
    private void proceedToPayment(BorderPane borderPane) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Payment Option");
        alert.setHeaderText("Choose Payment Option");
        alert.setContentText("Would you like to pay now or pay later?");

        ButtonType payNowButton = new ButtonType("Pay Now", ButtonBar.ButtonData.YES);
        ButtonType payLaterButton = new ButtonType("Pay Later", ButtonBar.ButtonData.NO);

        alert.getButtonTypes().setAll(payNowButton, payLaterButton);

        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.setOnCloseRequest(event -> event.consume());

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                event.consume();
            }
        });

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent()) {
            if (result.get() == payNowButton) {
                PaymentBillings pay = new PaymentBillings();     
                borderPane.setCenter(pay.getPaymentBillingsNode(borderPane));
                    
            }
            else if (result.get() == payLaterButton) {
                System.out.println("User chose to pay later.");

            }
        }
    }



}
