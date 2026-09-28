package Project_TMS;

import database.TourManager;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.*;
import javafx.scene.control.ScrollPane;
import javafx.collections.*;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import tour.Tour;

public class AddTour {

    private TourManager tourManager = new TourManager();
    
public void getAddTourDialog(Tours tours) {
    Stage dialogStage = new Stage();
    dialogStage.setTitle("Create Travel Package");

    dialogStage.initModality(Modality.APPLICATION_MODAL);
    dialogStage.setResizable(false);

    ScrollPane scrollPane = new ScrollPane();

    VBox root = new VBox(15);
    root.setPadding(new Insets(20));
    root.setStyle("-fx-background-color: #f9f9f9;");

    TextField tourNameField = new TextField();
    tourNameField.setPromptText("Enter Tour Name");

    TextField tourDescriptionField = new TextField();
    tourDescriptionField.setPromptText("Enter Tour Description");

    TextField activity1Field = new TextField();
    activity1Field.setPromptText("Activity 1");

    TextField activity2Field = new TextField();
    activity2Field.setPromptText("Activity 2");
    
    Label imageLabel = new Label("No image selected");
    Button uploadButton = new Button("Upload Destination Image");
    final File[] selectedImage = new File[1];

    uploadButton.setOnAction(e -> {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Image");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        File file = fileChooser.showOpenDialog(dialogStage);
        if (file != null) {
            selectedImage[0] = file;
            imageLabel.setText("Selected: " + file.getName());
        }
    });

    Label transportLabel = new Label("Available Transportations (Check to Include):");
    CheckBox busCheckBox = new CheckBox("Bus");
    TextField busPriceField = new TextField("500");
    busPriceField.setPromptText("Bus Price");
    busPriceField.setVisible(false);

    CheckBox planeCheckBox = new CheckBox("Airplane");
    TextField planePriceField = new TextField("2000");
    planePriceField.setPromptText("Airplane Price");
    planePriceField.setVisible(false);

    HBox transportBox = new HBox(10, busCheckBox, busPriceField, planeCheckBox, planePriceField);
    transportBox.setAlignment(Pos.CENTER_LEFT);

    Label accommodationLabel = new Label("Available Accommodations (Check to Include):");
    CheckBox standardRoom = new CheckBox("Standard Room");
    TextField standardPriceField = new TextField("1000");
    standardPriceField.setPromptText("Standard Room Price");
    standardPriceField.setVisible(false);

    CheckBox luxuryRoom = new CheckBox("Luxury Room");
    TextField luxuryPriceField = new TextField("2000");
    luxuryPriceField.setPromptText("Luxury Room Price");
    luxuryPriceField.setVisible(false);

    CheckBox suite = new CheckBox("Suite");
    TextField suitePriceField = new TextField("3000");
    suitePriceField.setPromptText("Suite Price");
    suitePriceField.setVisible(false);

    CheckBox deluxe = new CheckBox("Deluxe");
    TextField deluxePriceField = new TextField("4000");
    deluxePriceField.setPromptText("Deluxe Price");
    deluxePriceField.setVisible(false);

    VBox accommodationBox = new VBox(5,
        new HBox(10, standardRoom, standardPriceField),
        new HBox(10, luxuryRoom, luxuryPriceField),
        new HBox(10, suite, suitePriceField),
        new HBox(10, deluxe, deluxePriceField)
    );

    //m duration
    Label maxDurationLabel = new Label("Set Max Tour Duration (Days):");
    Spinner<Integer> maxDurationSpinner = new Spinner<>(1, 30, 7);

    //base price
    Label basePriceLabel = new Label("Base Price for Package (₱):");
    TextField basePriceField = new TextField();
    basePriceField.setPromptText("e.g. 5000");

    //discount
    Label discountLabel = new Label("Discount Percentage (%):");
    TextField discountField = new TextField();
    discountField.setPromptText("e.g. 10");

    busCheckBox.setOnAction(e -> busPriceField.setVisible(busCheckBox.isSelected()));
    planeCheckBox.setOnAction(e -> planePriceField.setVisible(planeCheckBox.isSelected()));
    standardRoom.setOnAction(e -> standardPriceField.setVisible(standardRoom.isSelected()));
    luxuryRoom.setOnAction(e -> luxuryPriceField.setVisible(luxuryRoom.isSelected()));
    suite.setOnAction(e -> suitePriceField.setVisible(suite.isSelected()));
    deluxe.setOnAction(e -> deluxePriceField.setVisible(deluxe.isSelected()));

    Button submitButton = new Button("Create Travel Package");
    submitButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 8 16;");
    VBox.setMargin(submitButton, new Insets(10, 0, 0, 0));

    submitButton.setOnAction(e -> {
    try {
        if (tourNameField.getText().isEmpty() || basePriceField.getText().isEmpty() || discountField.getText().isEmpty()) {
            throw new IllegalArgumentException("Please fill in all required fields.");
        }

        String tourName = tourNameField.getText();
        String description = tourDescriptionField.getText();
        List<String> activities = new ArrayList<>();
        if (!activity1Field.getText().isEmpty()) activities.add(activity1Field.getText());
        if (!activity2Field.getText().isEmpty()) activities.add(activity2Field.getText());

        List<String> transportations = new ArrayList<>();
        if (busCheckBox.isSelected()) {
            transportations.add("Bus");
        }
        if (planeCheckBox.isSelected()) {
            transportations.add("Airplane");
        }

        List<String> accommodations = new ArrayList<>();
        if (standardRoom.isSelected()) {
            accommodations.add("Standard Room");
        }
        if (luxuryRoom.isSelected()) {
            accommodations.add("Luxury Room");
        }
        if (suite.isSelected()) {
            accommodations.add("Suite");
        }
        if (deluxe.isSelected()) {
            accommodations.add("Deluxe");
        }

        int maxDuration = maxDurationSpinner.getValue();
        double basePrice = Double.parseDouble(basePriceField.getText());
        double discount = Double.parseDouble(discountField.getText());

        List<Double> transportPrices = new ArrayList<>();
        if (busCheckBox.isSelected()) {
            transportPrices.add(Double.parseDouble(busPriceField.getText()));
        }
        if (planeCheckBox.isSelected()) {
            transportPrices.add(Double.parseDouble(planePriceField.getText()));
        }

        List<Double> accommodationPrices = new ArrayList<>();
        if (standardRoom.isSelected()) {
            accommodationPrices.add(Double.parseDouble(standardPriceField.getText()));
        }
        if (luxuryRoom.isSelected()) {
            accommodationPrices.add(Double.parseDouble(luxuryPriceField.getText()));
        }
        if (suite.isSelected()) {
            accommodationPrices.add(Double.parseDouble(suitePriceField.getText()));
        }
        if (deluxe.isSelected()) {
            accommodationPrices.add(Double.parseDouble(deluxePriceField.getText()));
        }

            String imagePath = null;
            if (selectedImage[0] != null) {
                File destDir = new File("src/tourIMG");
                if (!destDir.exists()) destDir.mkdirs();
                String destPath = "src/tourIMG/" + selectedImage[0].getName();
                Files.copy(selectedImage[0].toPath(), new File(destPath).toPath(), StandardCopyOption.REPLACE_EXISTING);
                imagePath = "tourIMG/" + selectedImage[0].getName();
                System.out.println("destPath" + destPath);
            }
            
       tourManager.addTour(new Tour(tourManager.generateTourId(),tourName, description, activities,
                    transportations, accommodations,transportPrices,accommodationPrices,
                                maxDuration, basePrice, 0.0, discount, imagePath));
        
       
        dialogStage.close();
        tours.getToursNode(AdminHome.borderPane);

    } 
    catch (Exception ex) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Input Error");
        alert.setContentText("Please ensure all required fields have valid numbers.\n" + ex.getMessage());
        alert.showAndWait();
    }
});

    root.getChildren().addAll(
        new Label("Tour Name:"), tourNameField,
        new Label("Tour Description:"), tourDescriptionField,
        new Label("Activities:"), activity1Field, activity2Field,
        new Label("Upload Image:"), uploadButton, imageLabel,
        transportLabel, transportBox,
        accommodationLabel, accommodationBox,
        maxDurationLabel, maxDurationSpinner,
        basePriceLabel, basePriceField,
        discountLabel, discountField,
        submitButton
    );

    scrollPane.setContent(root);

    Scene scene = new Scene(scrollPane, 500, 750);
    dialogStage.setScene(scene);
    dialogStage.showAndWait();
}


}
