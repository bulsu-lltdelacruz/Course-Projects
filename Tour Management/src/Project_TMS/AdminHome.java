package Project_TMS;

import database.TourManager;
import java.util.List;
import java.util.Optional;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;


public class AdminHome {
    
    //Leo
    String adminBorderpaneStyle = "-fx-background-color: #393232;";
    
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
    
    public static BorderPane borderPane;
    private Button[] navBarButtons = new Button[5];
    private GridPane dashboardPane;
    FadeTransition ft = new FadeTransition(Duration.millis(100), dashboardPane);
        //Leo
    Feedbacks feedbackPane = new Feedbacks();
    TourTracking tourTrackingPane = new TourTracking();
    Accounts accountsPane = new Accounts();
    
    public Scene getAdminScene(Stage primaryStage){
        
        borderPane = new BorderPane();
        
        borderPane.setStyle(adminBorderpaneStyle);
        
        dashboardPane = new GridPane(30, 10);
        dashboardPane.setAlignment(Pos.CENTER);
        
        VBox accSummary = new VBox();
        VBox earningsPane = new VBox();//yung pie chart
        VBox bookingsPane = new VBox();//yung bargraph
        
        accSummary.setStyle("-fx-background-color: #D5CBA1;");
        earningsPane.setStyle("-fx-background-color: #4D805E;");
        bookingsPane.setStyle("-fx-background-color: #398DC9;");
        accSummary.setPrefSize(300, 190);
        earningsPane.setPrefSize(300, 190);
        bookingsPane.setPrefSize(900, 240);
        
        TourManager tourManager = new TourManager();
        List<Integer> data = tourManager.getTourGraphDataByPrice();
        
        //PIECHART//
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                new PieChart.Data("0-100", data.get(0)),
                new PieChart.Data("101-500", data.get(1)),
                new PieChart.Data("501-1000", data.get(2)),
                new PieChart.Data(">1000", data.get(3)));
        
        final PieChart chart = new PieChart(pieChartData);
        chart.setTitle("Trends by Price");
        System.out.println("FDSSSSSSSSSSSSSSS"+data.get(0)+'\n'+
                data.get(1)+'\n'+
                data.get(2)+'\n'+
                data.get(3)+'\n');
                
        earningsPane.getChildren().add(chart);
        
        dashboardPane.add(accSummary, 0, 0);
        dashboardPane.add(earningsPane, 1, 0);
        dashboardPane.add(bookingsPane, 0, 2, 2, 1);
 
            
        HBox navContainer = new HBox();
            navContainer.setPadding(new Insets(10, 10, 10, 50));
            navContainer.setSpacing(40);
            navContainer.setStyle("-fx-background-color: #FFF0F040;");
            
        HBox settingsContainer = new HBox();
            settingsContainer.setStyle("-fx-background-color: #FFF0F040;");
            settingsContainer.setAlignment(Pos.CENTER_RIGHT);
           
        ColumnConstraints col1 = new ColumnConstraints();
            col1.setPercentWidth(85);
        ColumnConstraints col2 = new ColumnConstraints();
            col2.setPercentWidth(15);
        RowConstraints row1 = new RowConstraints();
            row1.setPercentHeight(100);
            
        GridPane grid = new GridPane();
            grid.setStyle(gridStyle);
            grid.setPrefHeight(50);       
            grid.getColumnConstraints().addAll(col1, col2);
            grid.getRowConstraints().add(row1);
            grid.add(navContainer,0,0);
            grid.add(settingsContainer,1,0);
        
        
            navBarButtons[0] = Util.createButton("Dashboard", 120, Util.setButtonStyle("black", "16px", "white", "10px", "none"));
            navBarButtons[1] = Util.createButton("Tours", 120, Util.setButtonStyle("black", "16px", "transparent", "10px", "none"));
            navBarButtons[2] = Util.createButton("Feedbacks", 120, Util.setButtonStyle("black", "16px", "transparent", "10px", "none"));
            navBarButtons[3] = Util.createButton("Tour Tracking", 145, Util.setButtonStyle("black", "16px", "transparent", "10px", "none"));
            navBarButtons[4] = Util.createButton("Accounts", 145, Util.setButtonStyle("black", "16px", "transparent", "10px", "none"));
            
        /*Button dashboardButton = Util.createButton("Dashboard", 120, Util.setButtonStyle("black", "16px", "white", "10px"));

        Button toursButton = Util.createButton("Tours", 120, Util.setButtonStyle("black", "16px", "rgba(255, 255, 255, 0.35)", "10px"));

        Button historyButton = Util.createButton("History", 120, Util.setButtonStyle("black", "16px", "rgba(255, 255, 255, 0.35)", "10px"));
        
        Button feedbackButton = Util.createButton("Feedbacks", 120, Util.setButtonStyle("black", "16px", "rgba(255, 255, 255, 0.35)", "10px"));*/

        navContainer.getChildren().addAll(navBarButtons[0], navBarButtons[1], navBarButtons[2], navBarButtons[3], navBarButtons[4]);

        //EVENTS////EVENTS////EVENTS////EVENTS////EVENTS////EVENTS////EVENTS////EVENTS////EVENTS//
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
        menuBar.setPrefWidth(100);
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
        borderPane.setCenter(dashboardPane);
             
        Scene scene = new Scene(borderPane,1000,700);
        
        return scene;
    }
    
    
    private void switchTabs(BorderPane borderPane, int index){
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.setCycleCount(1);
        
        if(index == 0){
            //dashboard
            ft.setNode(dashboardPane);
            ft.play();
            borderPane.setCenter(dashboardPane);
           
        }else if(index == 1){
            //tours
            Tours tours = new Tours();
            
            ft.setNode(tours.getToursNode(borderPane));
            ft.play();
            borderPane.setCenter(tours.getToursNode(borderPane));
            
        }else if(index == 2){
            //history
            ft.setNode(feedbackPane);
            ft.play();
            borderPane.setCenter(feedbackPane);
            
        }else if(index == 3){
            //feedback
            ft.setNode(tourTrackingPane);
            ft.play();
            borderPane.setCenter(tourTrackingPane);
        }
        else if(index == 4){
            //feedback
            ft.setNode(accountsPane);
            ft.play();
            accountsPane.refresh();
            borderPane.setCenter(accountsPane);
        }
        changeButtonStyle(index);
    }
    
    //para mawala yung selected ng iba
    private void changeButtonStyle(int index){
        for(int i=0; i<navBarButtons.length; i++){
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
                 TravelMate_Admin login = new TravelMate_Admin();
                 Util.addScene(primaryStage, login.getAdminLoginScene(primaryStage));
             }
             else{  

             }         

    }   
}
