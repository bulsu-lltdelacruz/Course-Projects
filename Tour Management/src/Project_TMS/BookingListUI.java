
package Project_TMS;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class BookingListUI {

    private ScrollPane scrollPane = new ScrollPane();
    private FlowPane flowPane;

    public ScrollPane getBookingListUI(BorderPane borderPane) {
        flowPane = new FlowPane(Orientation.HORIZONTAL, 120, 30);
        flowPane.setAlignment(Pos.CENTER);
        flowPane.setPadding(new Insets(20));

        String[][] bookings = {
            {"Beach somewhere", "Details keme keme asdasd asdasdasdasdasda", "5/15/2005", "₱9999.00"},
            {"Beach somewhere", "Details keme keme asdasd asdasdasdasdasda", "5/15/2005", "₱9999.00"},
            {"Beach somewhere", "Details keme keme asdasd asdasdasdasdasda", "5/15/2005", "₱9999.00"},
            {"Beach somewhere", "Details keme keme asdasd asdasdasdasdasda", "5/15/2005", "₱9999.00"},
            {"Beach somewhere", "Details keme keme asdasd asdasdasdasdasda", "5/15/2005", "₱9999.00"},
            {"Beach somewhere", "Details keme keme asdasd asdasdasdasdasda", "5/15/2005", "₱9999.00"},
            {"Beach somewhere", "Details keme keme asdasd asdasdasdasdasda", "5/15/2005", "₱9999.00"},
            {"Beach somewhere", "Details keme keme asdasd asdasdasdasdasda", "5/15/2005", "₱9999.00"},
            {"Beach somewhere", "Details keme keme asdasd asdasdasdasdasda", "5/15/2005", "₱9999.00"}
        };

        for (int i = 0; i < bookings.length; i++) {
            VBox card = new VBox();
            card.setSpacing(10);
            card.setPadding(new Insets(10));
            card.setStyle("-fx-background-color: white; -fx-background-radius: 10px; -fx-border-color: transparent;");
            card.setPrefSize(900, 150);

            HBox imageHolder = new HBox();
            imageHolder.setPrefSize(300, 200);
            imageHolder.setPadding(new Insets(5));

            imageHolder.setStyle(
                "-fx-background-image: url('images/homebg.jpg');" +
                "-fx-background-size: cover;" +
                "-fx-background-repeat: no-repeat;" +
                "-fx-background-position: center center;" +
                "-fx-background-radius: 20px;"
            );
            
            VBox infoBox = new VBox(3);
            infoBox.setStyle("-fx-background-color: blue;");
            infoBox.setPrefWidth(700);
                    
            Label title = new Label(bookings[i][0]);
            title.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: green;");
            Label desc = new Label(bookings[i][1]);
            desc.setStyle("-fx-font-size: 11px;");
            
            HBox feedbackAndDate = new HBox(10);
            feedbackAndDate.setStyle("-fx-background-color: yellow;");
            Button feedbackBtn = new Button("Leave a feedback");
            feedbackBtn.setStyle("-fx-background-color: #5BFF5C; -fx-font-weight: bold;");
            Label date = new Label("Booking Date: " + bookings[i][2]);
            date.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
            feedbackAndDate.getChildren().addAll(feedbackBtn, date);

            Label status = new Label("Status: Pending Payment");
            status.setStyle("-fx-font-weight: bold;");

            HBox buttons = new HBox(10);
            buttons.setStyle("-fx-background-color: pink;");
            Button cancelBtn = new Button("Cancel");
            cancelBtn.setStyle("-fx-background-color: red; -fx-text-fill: white;");
            Button payNowBtn = new Button("Pay Now");
            payNowBtn.setStyle("-fx-background-color: limegreen; -fx-text-fill: black; -fx-font-weight: bold;");
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);
            Label price = new Label(bookings[i][3]);
            price.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
            buttons.getChildren().addAll(cancelBtn, payNowBtn, spacer, price);
            buttons.setAlignment(Pos.CENTER_LEFT);

            infoBox.getChildren().addAll(title, desc, feedbackAndDate, status, buttons);

            HBox container = new HBox(10);
            container.getChildren().addAll(imageHolder, infoBox);
            card.getChildren().add(container);

            flowPane.getChildren().add(card);
        }

        flowPane.setStyle("-fx-background-color: transparent;");
        scrollPane.setContent(flowPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-background: rgba(82, 89, 68, 1);");

        return scrollPane;
    }
}
