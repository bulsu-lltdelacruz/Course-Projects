package Project_TMS;

import java.util.Optional;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.*;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Home {
    
    String borderpaneStyle = "-fx-background-color: rgba(82, 89, 68, 1);";
    
    String centerStyle = "-fx-background-image: url('/images/homebg.jpg');"
            + "-fx-background-size: cover;"
            + "-fx-background-repeat: no-repeat;"
            + "-fx-background-position: center;";
    
    String adminBorderpaneStyle = ""
            + "-fx-background-color: #393232;";
    
    String gridStyle = "-fx-background-color: rgba(255, 240, 240, 0.25);";
    
    String settingsStyle = "-fx-background-image: url('/images/gear.png');"
            + "-fx-background-size: contain;"
            + "-fx-background-repeat: no-repeat;"
            + "-fx-background-position: center;";
    
    String logoutStyle = "-fx-font-size: 13px;"
                + "-fx-text-fill: white;"
                + "-fx-background-color: firebrick;"
                + "-fx-font-weight: bold;"
                + "-fx-font-family: Arial;"
                + "-fx-border-radius: 5px;"
                + "-fx-background-radius: 5px;"
                + "-fx-padding: 5 0 5 0;";
    
    String labelStyle = "-fx-font-size: 35px; "
                + "-fx-text-fill: white; "
                + "-fx-font-weight: bold; "
                + "-fx-font-family: Serif;";
    
    private Button[] navBarButtons = new Button[3];
    public static BorderPane borderPane;
    private BorderPane centerPane;
    FadeTransition ft = new FadeTransition(Duration.millis(100), centerPane);
    
    public Scene getHomeScene(Stage primaryStage){
        
        borderPane = new BorderPane();
            borderPane.setStyle(borderpaneStyle);
            
        VBox left = new VBox();
            left.setStyle("-fx-background-color: transparent;");
            left.setPrefWidth(50);
        VBox right = new VBox();
            right.setStyle("-fx-background-color: transparent;");
            right.setPrefWidth(50);
        VBox bottom = new VBox();
            bottom.setStyle("-fx-background-color: transparent;");
            bottom.setPrefHeight(200);
        VBox innerContainer = new VBox();
                innerContainer.setStyle("-fx-background-color: transparent;");
                innerContainer.setPadding(new Insets(10, 10, 0, 10));
                innerContainer.setSpacing(10);
                innerContainer.setAlignment(Pos.CENTER);  
            
        centerPane = new BorderPane();
            centerPane.setStyle(centerStyle);
            centerPane.setPadding(new Insets(10, 10, 0, 10));
  
            centerPane.setLeft(left);
            centerPane.setRight(right);
            centerPane.setBottom(bottom);
            centerPane.setCenter(innerContainer);
            
        Label label = new Label("Start your journey with TravelMate");
            label.setStyle(labelStyle);
            
        Button startnowButton = Util.createButton("Start now", 130, Util.setButtonStyle("black", "18px", "rgba(239, 247, 0, 1)", "8px"));          
                startnowButton.setOnAction(e -> switchTabs(borderPane, 1));
            innerContainer.getChildren().addAll(label, startnowButton);
 
            
        HBox navContainer = new HBox();
            navContainer.setPadding(new Insets(10, 10, 10, 50));
            navContainer.setSpacing(40);
            navContainer.setStyle("-fx-background-color: transparent;");
            
        HBox settingsContainer = new HBox();
            settingsContainer.setStyle("-fx-background-color: transparent;");
            settingsContainer.setAlignment(Pos.CENTER_RIGHT);
           
        ColumnConstraints col1 = new ColumnConstraints();
            col1.setPercentWidth(80);
        ColumnConstraints col2 = new ColumnConstraints();
            col2.setPercentWidth(30);
        RowConstraints row1 = new RowConstraints();
            row1.setPercentHeight(100);
            
        GridPane grid = new GridPane();
            grid.setStyle(gridStyle);
            grid.setPrefHeight(50);       
            grid.getColumnConstraints().addAll(col1, col2);
            grid.getRowConstraints().add(row1);
            grid.add(navContainer,0,0);
            grid.add(settingsContainer,1,0);
        
     
            navBarButtons[0] = Util.createButton("Home", 120, Util.setButtonStyle("black", "16px", "white", "10px", "none"));
            navBarButtons[1] = Util.createButton("Book Tour", 120, Util.setButtonStyle("black", "16px", "transparent", "10px", "none"));
            navBarButtons[2] = Util.createButton("Bookings", 120, Util.setButtonStyle("black", "16px", "transparent", "10px", "none"));
            
        navContainer.getChildren().addAll(navBarButtons[0], navBarButtons[1], navBarButtons[2]);

        //EVENTS
        for(int i = 0; i < navBarButtons.length; i++){
            
            int counter = i;       
            navBarButtons[i].setOnAction(e->{
            
                switchTabs(borderPane, counter);

                navBarButtons[counter].setStyle(navBarButtons[counter].getStyle()+"-fx-background-color: white;");
                ScaleTransition scale = new ScaleTransition(Duration.millis(100), navBarButtons[counter]);
                    scale.setFromX(1);
                    scale.setFromY(1);
                    scale.setToX(1.1);
                    scale.setToY(1.1);
                    scale.setCycleCount(1);
                    scale.setAutoReverse(true);
                    scale.play();           
            });
        }


        MenuBar menuBar = new MenuBar();
        menuBar.setStyle("-fx-background-color: transparent;");
        Menu settings = new Menu("      ");
             settings.setStyle(settingsStyle);
             MenuItem about = new MenuItem("About");
             MenuItem account = new MenuItem("myAccount");
             MenuItem help = new MenuItem("Help");
             MenuItem logout = new MenuItem("    Logout   ");
                logout.setStyle(logoutStyle);
                logout.setOnAction(e -> logOut(primaryStage));

        settings.getItems().addAll(about, account, help, new SeparatorMenuItem(), logout);

        menuBar.getMenus().addAll(settings);

        settingsContainer.getChildren().add(menuBar);

        borderPane.setTop(grid);
        borderPane.setCenter(centerPane);
            
        Scene scene = new Scene(borderPane,1000,700);
        
        return scene;
    }
    
    private void switchTabs(BorderPane borderPane, int index){
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.setCycleCount(1);
        
        if(index == 0){
            //home
            ft.setNode(centerPane);
            ft.play();
            borderPane.setLeft(null);
            borderPane.setRight(null);
            borderPane.setBottom(null);
            borderPane.setCenter(centerPane);
        }
        else if(index == 1){
            //tours
            BookTours tours = new BookTours();
            
            ft.setNode(tours.getBookToursNode(borderPane));
            ft.play();
            borderPane.setLeft(null);
            borderPane.setRight(null);
            borderPane.setBottom(null);
            borderPane.setCenter(tours.getBookToursNode(borderPane));        
        }
        else if(index == 2){
            //history
          
            ft.setNode( new History());
            ft.play();
            borderPane.setLeft(null);
            borderPane.setRight(null);
            borderPane.setBottom(null);
            borderPane.setCenter( new History());       
            
        }
        changeButtonStyle(index);
    }
    
    //para mawala yung selected ng iba
    private void changeButtonStyle(int index){
        for(int i = 0; i<navBarButtons.length; i++){
            if(i == index)
                navBarButtons[i].setStyle(Util.setButtonStyle("black", "16px", "white", "10px", "none"));
            else
                navBarButtons[i].setStyle(Util.setButtonStyle("black", "16px", "transparent", "10px", "none"));
        }
    }
    
    private void logOut(Stage primaryStage){

                 Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                     confirm.setHeaderText("Are you sure you want to log out?");
                     confirm.setContentText("Press OK to confirm.");
                     confirm.setTitle("Logout Confirmation");
                     confirm.initOwner(primaryStage);
                     
                 Optional<ButtonType> choice = confirm.showAndWait();
                     if(choice.isPresent() && choice.get() == ButtonType.OK){
                         Login login = new Login();
                         Util.addScene(primaryStage, login.getLoginScene(primaryStage));
                     }
                     else{  

                     }         

//Dialog<ButtonType> dialog = new Dialog<>();
//dialog.setTitle("Logout Confirmation");
//
//// Custom header and content using VBox
//VBox content = new VBox(10);
//content.setPadding(new Insets(15));
//content.setAlignment(Pos.CENTER);
//
//// Header text
//Label header = new Label("Are you sure you want to log out?");
//header.setFont(Font.font("System", FontWeight.BOLD, 14));
//
//// Content text
//Label message = new Label("Press OK to confirm.");
//message.setWrapText(true);
//
//// Add content to dialog
//content.getChildren().addAll(header, message);
//
//// Add content to dialog pane
//dialog.getDialogPane().setContent(content);
//
//// Add buttons to dialog
//ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
//ButtonType confirmButton = new ButtonType("Log Out", ButtonBar.ButtonData.OK_DONE);
//dialog.getDialogPane().getButtonTypes().setAll(cancelButton, confirmButton);
//
//// Show the dialog and wait for the result
//Optional<ButtonType> result = dialog.showAndWait();
//
//if (result.isPresent() && result.get() == confirmButton) {
//    System.out.println("User confirmed logout");
//} else {
//    System.out.println("User canceled logout");
//}


//Dialog<ButtonType> dialog = new Dialog<>();
//dialog.setTitle("Custom Dialog with Custom Icon");
//dialog.setHeaderText("Are you sure?");
//dialog.getDialogPane().setContent(new Label("Press OK to confirm."));
//
//// Load custom image (ensure it's available in the project path)
//Image customIcon = new Image(getClass().getResourceAsStream("/path/to/your/icon.png"));
//ImageView icon = new ImageView(customIcon);
//dialog.getDialogPane().setGraphic(icon); // Set your custom icon
//
//ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
//ButtonType confirmButton = new ButtonType("Log Out", ButtonBar.ButtonData.OK_DONE);
//dialog.getDialogPane().getButtonTypes().setAll(cancelButton, confirmButton);
//
//// Show the dialog and wait for the result
//Optional<ButtonType> result = dialog.showAndWait();
//if (result.isPresent() && result.get() == confirmButton) {
//    System.out.println("User confirmed logout");
//} else {
//    System.out.println("User canceled logout");
//}



        }   
}
