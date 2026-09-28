package Project_TMS;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainStage extends Application {

    @Override
    public void start(Stage primaryStage) {

        Login login = new Login();
        Util.addScene(primaryStage, login.getLoginScene(primaryStage));
        primaryStage.setMinWidth(600);
        primaryStage.setMinHeight(600);
        primaryStage.setTitle("TravelMate");
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

}
