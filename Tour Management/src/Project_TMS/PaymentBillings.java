package Project_TMS;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class PaymentBillings {

    public VBox getPaymentBillingsNode(BorderPane borderPane) {
        
            String labelStyle = "-fx-font-size: 12px; "
                + "-fx-text-fill: black; "
                + "-fx-font-weight: bold; "
                + "-fx-font-family: Arial; ";

        // Main VBox Container
        VBox parentVBox = new VBox();
        parentVBox.setPadding(new Insets(35, 0, 40, 0));
        parentVBox.setSpacing(20);
        parentVBox.setStyle("-fx-background-color: white;");
        parentVBox.setPrefWidth(1000);

        // Header
        Label header = new Label("Payment and Billings");
        header.setFont(Font.font("Arial", FontWeight.BOLD, 40));
        header.setTextFill(Color.BLACK);
        header.setPadding(new Insets(0, 0, 0, 30));

        // Main Black Container
        HBox mainContainer = new HBox(20);
        mainContainer.setPadding(new Insets(20));
        mainContainer.setStyle("-fx-background-color: #1a1a1a;");
        mainContainer.setPrefHeight(450);
        mainContainer.setAlignment(Pos.CENTER);

        // Left - Payment Overview
        HBox leftBox = new HBox(0);
        leftBox.setPadding(new Insets(20));
        leftBox.setPrefWidth(500);
        leftBox.setStyle("-fx-background-color: #f4dede; -fx-background-radius: 12px;");
        
        VBox categoriesContainer = new VBox();
        categoriesContainer.setMinWidth(300);
        categoriesContainer.setSpacing(15);
        String [] categories = {
            "Payment Overview","Tour Package/Itineraries",
            "Transportation","Accommodation",
            "Environmental Fee","Local Taxes"
        };
        
        for(int i = 0; i < categories.length; i++){            
            Label label = new Label(categories[i]);
            if(i==0){
              label.setStyle(labelStyle+"-fx-font-size: 23px;");
              label.setPadding(new Insets(15,0,20,0));
            }
            else{
              label.setStyle(labelStyle+"-fx-font-size: 18px;");
            }
            
            categoriesContainer.getChildren().add(label);
        }
        
        VBox valuesContainer = new VBox();
        valuesContainer.setMinWidth(200);
        valuesContainer.setSpacing(15);     
        valuesContainer.setAlignment(Pos.TOP_RIGHT);
        String [] values = {
            "   ", Util.formatCurrency(10000),
            Util.formatCurrency(3000),Util.formatCurrency(1500),
            Util.formatCurrency(200),Util.formatCurrency(450)
        };
        
        for(int i = 0; i < values.length; i++){            
            Label label = new Label(values[i]);
            if(i==0){
              label.setStyle(labelStyle+"-fx-font-size: 23px;");
              label.setPadding(new Insets(15,0,20,0));
            }
            else{
              label.setStyle(labelStyle+"-fx-font-size: 18px;");
            }
            
            valuesContainer.getChildren().add(label);
        }

        leftBox.getChildren().addAll(categoriesContainer,valuesContainer);
        
        // Right - Total Bill
        VBox rightBox = new VBox(20);
        rightBox.setPadding(new Insets(20));
        rightBox.setPrefWidth(350);
        rightBox.setStyle("-fx-background-color: black; -fx-background-radius: 12;");
        Label rightTitle = new Label("Total Bill");
        rightTitle.setTextFill(Color.WHITE);
        rightTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        rightBox.getChildren().add(rightTitle);

        // Duration & Persons
        GridPane infoGrid = new GridPane();
        infoGrid.setVgap(15);
        infoGrid.setHgap(10);

        Label durationLabel = new Label("Duration:");
        durationLabel.setStyle(labelStyle+"-fx-font-weight: normal; -fx-text-fill: white;");
        Label durationValue = new Label("3 Days");
        durationValue.setStyle(labelStyle+"-fx-text-fill: white;");

        Label personsLabel = new Label("No. of Persons:");
        personsLabel.setStyle(labelStyle+"-fx-font-weight: normal; -fx-text-fill: white;");
        Label personsValue = new Label("5");
        personsValue.setStyle(labelStyle+"-fx-text-fill: white;");

        infoGrid.add(durationLabel, 0, 0);
        infoGrid.add(durationValue, 1, 0);
        infoGrid.add(personsLabel, 0, 1);
        infoGrid.add(personsValue, 1, 1);

        // Total Amount Due Box
        VBox amountBox = new VBox();
        amountBox.setPadding(new Insets(20));
        amountBox.setStyle("-fx-background-color: #4b4b4b; -fx-background-radius: 10;");
        Label totalLabel = new Label("Total Amount Due:");
        totalLabel.setTextFill(Color.WHITE);
        totalLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));

        Label totalAmount = new Label("₱24,800.00");
        totalAmount.setTextFill(Color.WHITE);
        totalAmount.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        amountBox.getChildren().addAll(totalLabel, totalAmount);
        rightBox.getChildren().addAll(infoGrid, amountBox);
        
        //Payment method
        VBox paymentMethodBox = getPaymentSection(labelStyle);

        // Buttons (Cancel / Process)
        HBox buttonBox = new HBox(20);
        buttonBox.setPadding(new Insets(10));
        buttonBox.setAlignment(Pos.CENTER_RIGHT);

        Button cancelBtn = new Button("Cancel");
        cancelBtn.setStyle("-fx-background-color: #777; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8;");
        cancelBtn.setPrefWidth(100);

        Button processBtn = new Button("Process Payment");
        processBtn.setStyle("-fx-background-color: green; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8;");
        processBtn.setPrefWidth(150);

        buttonBox.getChildren().addAll(cancelBtn, processBtn);
        rightBox.getChildren().addAll(paymentMethodBox,buttonBox);

        mainContainer.getChildren().addAll(leftBox, rightBox);
        parentVBox.getChildren().addAll(header, mainContainer);

        return parentVBox;
    }
    
    
    public VBox getPaymentSection(String labelStyle) {
        HBox paymentMethodBox = new HBox(30);
        paymentMethodBox.setPadding(new Insets(10));
        paymentMethodBox.setAlignment(Pos.CENTER_LEFT);
        
        Label paymentMethodLabel = new Label("Mode of Payment:");
        paymentMethodLabel.setStyle(labelStyle + "-fx-text-fill: white;");

        String[] payMethodChoices = {"Online Payment", "Credit Card", "Cash"};
        ObservableList<String> options = FXCollections.observableArrayList(payMethodChoices);

        ComboBox<String> paymentMethodCB = new ComboBox<>();
        paymentMethodCB.setItems(options);
        paymentMethodCB.setPrefWidth(300);
        paymentMethodCB.setPromptText("Select Payment Method");        
        
        VBox grouper = new VBox(5,paymentMethodLabel,paymentMethodCB);
        

        TextField accountNumberField = new TextField();
        accountNumberField.setPromptText("Enter Account Number");
        accountNumberField.setVisible(false);

        paymentMethodCB.setOnAction(e -> {
            String selected = paymentMethodCB.getValue();
            if (selected.equals("Online Payment") || selected.equals("Credit Card")) {
                accountNumberField.setVisible(true);
            } else {
                accountNumberField.setVisible(false);
                accountNumberField.clear();
            }
        });

        paymentMethodBox.getChildren().addAll(grouper);

        VBox container = new VBox(10, paymentMethodBox, accountNumberField);
        container.setAlignment(Pos.CENTER_LEFT);
        container.setPadding(new Insets(10,20,20,20));
        container.setStyle("-fx-background-color: #333333; -fx-border-radius: 15px;");

        return container;
    }
}
