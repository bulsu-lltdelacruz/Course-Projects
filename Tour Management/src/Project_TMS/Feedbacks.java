package Project_TMS;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import static javafx.scene.layout.VBox.setMargin;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class Feedbacks extends ScrollPane{
    
    //Feedbacks
    //ScrollPane scroll = new ScrollPane();
    FeedbackDisplay[] feedbackDisplay = new FeedbackDisplay[10];
    VBox feedbackPane = new VBox(20);
    
    public Feedbacks(){
        feedbackPane.setStyle("-fx-background-color: #393232");
        setStyle("-fx-background-color: #393232");
        
        setFitToWidth(true);
        feedbackPane.setFillWidth(false);
        feedbackPane.setAlignment(Pos.CENTER);
        
        for(int i = 0; i<feedbackDisplay.length; i++)
        {
            feedbackDisplay[i] = new FeedbackDisplay("Commentator's Name", "Comment goes here");
            feedbackPane.getChildren().add(feedbackDisplay[i]);
        }
        feedbackPane.setMargin(feedbackDisplay[0], new Insets(50,0,0,0));
        
        setContent(feedbackPane);
        
    }
    
}

class FeedbackDisplay extends VBox{

    public FeedbackDisplay(String name, String comment) {
        Label nameLabel = new Label(name);
        Label commentLabel = new Label(comment);
        VBox commentPane = new VBox();
        Button deleteButton = new Button("Delete");
        
        setFillWidth(false);
        setAlignment(Pos.CENTER);
        
        setStyle("-fx-background-color: #D9D9D9;"
                + "-fx-background-radius: 10px");
        setPrefSize(900, 135);
        commentPane.setPrefSize(860, 80);
        commentPane.setStyle("-fx-background-color: #A9A7A7;"
                + "-fx-background-radius: 10px");
        
        deleteButton.setStyle("-fx-background-color: rgba(247, 0, 4, 0.5);"
                + "-fx-background-radius: 10px;"
                + "/*-fx-background-radius: 10px;"
                + "-fx-border-style: solid;"
                + "-fx-border-color: #A9A7A7;"
                + "-fx-border-size: 10px;*/");
        deleteButton.setPrefSize(120, 20);
        deleteButton.setCursor(Cursor.CLOSED_HAND);
        
        commentPane.getChildren().add(commentLabel);
        commentPane.getChildren().add(deleteButton);
        //t r b l
        setMargin(deleteButton, new Insets(60,0,0,740));
        commentPane.setMargin(deleteButton, new Insets(40,0,0,740));
        
        setMargin(nameLabel, new Insets(0,690,0,0));
        setMargin(commentLabel, new Insets(10,0,0,10));
        
        nameLabel.setStyle("-fx-font-size: 15px;"
                + "-fx-font-weight: 800");
        commentLabel.setStyle("-fx-font-size: 13px;"
                + "-fx-font-weight: 800");
        
        getChildren().add(nameLabel);
        getChildren().add(commentPane);
        
        
        
    }
    
    
}