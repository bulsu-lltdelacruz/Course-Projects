package Project_TMS;

import database.TouristManager;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import static javafx.scene.layout.VBox.setMargin;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import tourist.Tourist;

public class Accounts extends ScrollPane{
    
    //Feedbacks
    //ScrollPane scroll = new ScrollPane();
    List<AccountDisplay> accountDisplay;
    List<Tourist> allAccounts = new ArrayList<Tourist>();
     AddTourist signup;
     Button addAccount;
   
    VBox accountPane = new VBox(20);
    
    public Accounts(){
        accountDisplay = new ArrayList<AccountDisplay>();
        
        signup = new AddTourist(this);
        addAccount = Util.createButton("Add Account", 200, Util.setButtonStyle("black", "16px", "green", "20px"));
        accountPane.setStyle("-fx-background-color: #393232");
        setStyle("-fx-background-color: #393232");
        
        setFitToWidth(true);
        setFitToHeight(true);
        accountPane.setFillWidth(false);
        accountPane.setAlignment(Pos.CENTER);
        accountPane.getChildren().add(addAccount);
        accountPane.setMargin(addAccount, new Insets(20,0,-40,700));
        
        refresh();
        
        
        
        addAccount.setOnAction(e->{
            setContent(signup);
        });
        setContent(accountPane);
    }
    
    void back(){
        setContent(accountPane);
    }
    void refresh(){
        
        for(int i = 0; i<allAccounts.size(); i++)
        {
            accountPane.getChildren().remove(accountDisplay.get(i));
        }
        accountDisplay.clear();
        TouristManager touristManager = new TouristManager();
        allAccounts = new ArrayList<Tourist>();
        
        allAccounts = touristManager.getAllTourists();
        
        for(int i = 0; i<allAccounts.size(); i++)
        {
            System.out.println("TANGINAMO"+allAccounts.get(i).getFullName()
                    + "");
            String name = allAccounts.get(i).getFullName();
            String userName = allAccounts.get(i).getUsername();
            String password = allAccounts.get(i).getPassword();
            String number = allAccounts.get(i).getPhoneNumber();
            String gender = allAccounts.get(i).getGender();
            LocalDate birthday = allAccounts.get(i).getBirthday();
            
            accountDisplay.add(new AccountDisplay(name, userName, password, number,allAccounts.get(i).getTouristID(),gender, birthday, this));
            accountPane.getChildren().add(accountDisplay.get(i));
        }     
        accountPane.setMargin(accountDisplay.get(0), new Insets(50,0,0,0));
    }
    
    
}

class AccountDisplay extends VBox{

    public AccountDisplay(String name, String username, String password, String number, String touristID, String gender,
            LocalDate birthday, Accounts account) {
        Label nameLabel = new Label(name);
        Label usernameLabel = new Label("Username: "+username);
        Label passwordLabel = new Label("Password: "+password);
        Label numberLabel = new Label("Number: "+number);
        Label genderLabel = new Label("Gender: "+gender);
        Label birthdayLabel = new Label("Birthday: "+birthday);
        
        usernameLabel.setPrefWidth(900);
        passwordLabel.setPrefWidth(860);
        numberLabel.setPrefWidth(860);
        nameLabel.setPrefWidth(860);
        genderLabel.setPrefWidth(860);
        birthdayLabel.setPrefWidth(860);
         
        
        VBox commentPane = new VBox();
        Button deleteButton = new Button("Delete");
        Button updateButton = new Button("Update");
        
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
        updateButton.setStyle("-fx-background-color: #5B8328;"
                + "-fx-background-radius: 10px;"
                + "/*-fx-background-radius: 10px;"
                + "-fx-border-style: solid;"
                + "-fx-border-color: #A9A7A7;"
                + "-fx-border-size: 10px;*/");
        deleteButton.setPrefSize(120, 20);
        deleteButton.setCursor(Cursor.CLOSED_HAND);
        updateButton.setPrefSize(120, 20);
        updateButton.setCursor(Cursor.CLOSED_HAND);
        commentPane.setSpacing(10);
        commentPane.getChildren().addAll(usernameLabel, passwordLabel, numberLabel, genderLabel, birthdayLabel);
        HBox buttonContainer = new HBox(updateButton, deleteButton);
        buttonContainer.setSpacing(40);
        buttonContainer.setAlignment(Pos.CENTER_RIGHT);
        //commentPane.getChildren().addAll(buttonContainer);
        //t r b l
        setMargin(deleteButton, new Insets(60,0,0,740));
        commentPane.setMargin(deleteButton, new Insets(40,0,0,740));
        
        setMargin(nameLabel, new Insets(0,0,0,5));
        setMargin(usernameLabel, new Insets(10,0,0,10));
        setMargin(passwordLabel, new Insets(0,0,0,10));
        setMargin(numberLabel, new Insets(0,0,0,10));
        setMargin(genderLabel, new Insets(0,0,0,10));
        setMargin(birthdayLabel, new Insets(0,0,10,10));
        
        nameLabel.setStyle("-fx-font-size: 20px;"
                + "-fx-font-weight: 800");
        String descStyle = "-fx-font-size: 15px;"
                + "-fx-font-weight: 700";
        usernameLabel.setStyle(descStyle);
        passwordLabel.setStyle(descStyle);
        numberLabel.setStyle(descStyle);
        genderLabel.setStyle(descStyle);
        birthdayLabel.setStyle(descStyle);
        
        getChildren().add(nameLabel);
        getChildren().add(commentPane);
        getChildren().add(buttonContainer);
        setMargin(buttonContainer, new Insets(5,0,5,570));
        
        deleteButton.setOnAction(e->{
            
            TouristManager touristManager = new TouristManager();
            Alert alert = new Alert(Alert.AlertType.WARNING, "Are you sure you want to delete tour "+touristID, ButtonType.YES, ButtonType.NO);
                    Alert alertConfirm = new Alert(Alert.AlertType.CONFIRMATION, "Tour "+touristID+" Deleted", ButtonType.OK);
                    alert.showAndWait();
                    if(alert.getResult() == ButtonType.YES)
                        if(touristManager.deleteTourist(touristID))
                        {
                            alertConfirm.show();
                            account.refresh();
                        }
                        else
                        {
                            alertConfirm.setAlertType(Alert.AlertType.WARNING);
                            alertConfirm.setContentText("There was an error during deletion.");
                            alertConfirm.show();
                        }
                            
        });
        
        updateButton.setOnAction(e->{
            account.setContent(new UpdateAccount(account, touristID));
        });
    }
    
}

class AddTourist extends VBox{
    
    String titleStyle = "-fx-text-fill: white;"
            + "-fx-font-weight: bold;"
            + "-fx-font-size: 35px;"
            + "-fx-font-family: Arial;";
    
    String gridStyle = "-fx-background-color: rgba(255, 255, 255, 0.5);"
            + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 15, 0.5, -3, 3);"
            + "-fx-border-radius: 15px;"
            + "-fx-background-radius: 15px;";
    
    String imageStyle = "-fx-background-color: rgba(82, 89, 68, 1);"
            + "-fx-border-radius: 15px;"
            + "-fx-background-radius: 15px;";
    
    String labelStyle = "-fx-font-size: 12px; "
                + "-fx-text-fill: black; "
                + "-fx-font-weight: bold; "
                + "-fx-font-family: Arial; ";  
    
    String tagStyle = "-fx-font-size: 25px; "
                + "-fx-text-fill: white; "
                + "-fx-font-weight: bold; "
                + "-fx-font-family: Serif; ";
    Accounts scrollPane;
            
    private TouristManager touristManager = new TouristManager();
    
    
    TextField fnameField, lnameField, midnameField, phoneField, emailField, usernameField;
    PasswordField passwordField, confpasswordField;
    RadioButton maleButton, femaleButton;
    DatePicker bdayPicker;
    
    public AddTourist(Accounts scrollPane){
        this.scrollPane = scrollPane;
        //parent layout
        //VBox vbox = new VBox();
            setStyle("-fx-background-color: rgba(82, 89, 68, 1);");
            setPadding(new Insets(50,0,50,0));
            setSpacing(0);
            setAlignment(Pos.CENTER);
        
            
        //title
        Label title = new Label("Sign Up");     
             title.setStyle(titleStyle);
             title.setScaleX(1.1);
        
        HBox titleContainer = new HBox(title);
             titleContainer.setPrefSize(100, 70);
             titleContainer.setAlignment(Pos.BOTTOM_LEFT);
             titleContainer.setMaxWidth(700);
        
             
        //signup form container
        ColumnConstraints col1 = new ColumnConstraints();
            col1.setPercentWidth(100);
        //ColumnConstraints col2 = new ColumnConstraints();
           // col2.setPercentWidth(40);
        RowConstraints row = new RowConstraints();
            row.setPercentHeight(100);
        GridPane grid = new GridPane();
             grid.setStyle(gridStyle);
             grid.setPrefSize(250, 450);
             grid.getColumnConstraints().addAll(col1);
             grid.getRowConstraints().add(row);  
             
        Image image = new Image("/images/signupImage2.jpg");
        ImageView imageView = new ImageView(image);
            imageView.setFitWidth(285);
            imageView.setFitHeight(457);
            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);
            
        Rectangle clip = new Rectangle(285, 457); 
            clip.setArcWidth(30); 
            clip.setArcHeight(30);
            imageView.setClip(clip);
            
        Label tagLabel = new Label("Your journey starts here!");
            tagLabel.setPadding(new Insets(30,0,0,0));
            tagLabel.setStyle(tagStyle+ "-fx-text-fill: yellow;");
            
        Label tagLabel2 = new Label("Add Account.");
            tagLabel2.setPadding(new Insets(55,0,0,0));
            tagLabel2.setStyle(labelStyle+ "-fx-text-fill: white;");
            
        StackPane imageWithLabel = new StackPane(imageView, tagLabel, tagLabel2);
            imageWithLabel.setAlignment(Pos.TOP_CENTER);
       
        //VBox imageContainer = new VBox(imageWithLabel);
             //imageContainer.setStyle(imageStyle);
             
             
        //signup form fields and labels                     
        VBox namesContainer = new VBox();
            namesContainer.setSpacing(5);
            namesContainer.setPadding(new Insets(10,20,5,20));
            
            Label fnameLabel = new Label("First Name:");
                fnameLabel.setStyle(labelStyle);
            fnameField = new TextField();
                fnameField.setPromptText("Enter your first name");
            Label midnameLabel = new Label("Middle Name:");
                midnameLabel.setStyle(labelStyle);
            midnameField = new TextField();
                midnameField.setPromptText("Enter your middle name");
            Label lnameLabel = new Label("Last Name:");
                lnameLabel.setStyle(labelStyle);
            lnameField = new TextField();
                lnameField.setPromptText("Enter your last name");
            
        namesContainer.getChildren().addAll(fnameLabel, fnameField, midnameLabel, midnameField, lnameLabel, lnameField);
        
        HBox bdaygenderContainer = new HBox();
            bdaygenderContainer.setAlignment(Pos.CENTER_LEFT);
            bdaygenderContainer.setSpacing(10);
            bdaygenderContainer.setPrefHeight(70);
            bdaygenderContainer.setPadding(new Insets(0,15,5,20));
            
            Label bdayLabel = new Label("Birthday:");
                bdayLabel.setStyle(labelStyle);
            bdayPicker = new DatePicker();
                bdayPicker.setPrefWidth(110);
                bdayPicker.setPromptText("mm/dd/yyyy");
            Label genderLabel = new Label("Gender:");
                genderLabel.setStyle(labelStyle);
                genderLabel.setPadding(new Insets(0,0,0,20));
            ToggleGroup genderGroup = new ToggleGroup();
            maleButton = new RadioButton("Male");
                maleButton.setStyle(labelStyle);
                maleButton.setToggleGroup(genderGroup);
            femaleButton = new RadioButton("Female");
                femaleButton.setStyle(labelStyle);
                femaleButton.setToggleGroup(genderGroup);

        bdaygenderContainer.getChildren().addAll(bdayLabel, bdayPicker,genderLabel, maleButton, femaleButton);
             
        VBox accContainer = new VBox();
            accContainer.setPrefHeight(260);
            accContainer.setPadding(new Insets(0,20,15,20));
            accContainer.setSpacing(5);
            
            Label phoneLabel = new Label("Phone Number:");
                phoneLabel.setStyle(labelStyle);
            phoneField = new TextField();          
                phoneField.setPromptText("Enter your phone number");
              
            Label emailLabel = new Label("Email:");
                emailLabel.setStyle(labelStyle);
            emailField = new TextField();
                emailField.setPromptText("Enter your email");
                
            Label usernameLabel = new Label("Username:");
                usernameLabel.setStyle(labelStyle);
            usernameField = new TextField();
                usernameField.setPromptText("Enter your username");
                
            Label passwordLabel = new Label("Password:");
                passwordLabel.setStyle(labelStyle);
            passwordField = new PasswordField();
                passwordField.setPromptText("Enter your password");
                
            Label confpasswordLabel = new Label("Confirm Password:");
                confpasswordLabel.setStyle(labelStyle);
            confpasswordField = new PasswordField();
                confpasswordField.setPromptText("Re-type your password");
         
        accContainer.getChildren().addAll(phoneLabel,phoneField, emailLabel,emailField, usernameLabel, usernameField, passwordLabel, passwordField, confpasswordLabel, confpasswordField);
        
        VBox formContainer = new VBox(namesContainer, bdaygenderContainer, accContainer);
        
        //buttons
            HBox buttonContainer = new HBox();
                buttonContainer.setAlignment(Pos.CENTER_RIGHT);
                buttonContainer.setSpacing(15);
                buttonContainer.setScaleX(1.038);
                buttonContainer.setMaxWidth(685);
                
            Button signupButton = Util.createButton("Sign Up", 120, Util.setButtonStyle("black", "13px", "rgba(25, 198, 36, 1)", "5px"));
                 
            Button cancelButton = Util.createButton("Cancel", 75, Util.setButtonStyle("white", "13px", "rgba(255, 255, 255, 0.3)", "5px"));
        
            buttonContainer.getChildren().addAll(cancelButton, signupButton);
        
        //events
            signupButton.setOnAction(e -> signUp());
            signupButton.setOnMouseEntered(e -> signupButton.setStyle(Util.setButtonStyle("black", "13px", "rgba(119, 231, 126, 1)", "5px")));
            signupButton.setOnMouseExited(e -> signupButton.setStyle(Util.setButtonStyle("black", "13px", "rgba(25, 198, 36, 1)", "5px")));
            
            cancelButton.setOnAction(e -> cancel());
            cancelButton.setOnMouseEntered(e -> cancelButton.setStyle(Util.setButtonStyle("black", "13px", "rgba(255, 255, 255, 0.7)", "5px")));
            cancelButton.setOnMouseExited(e -> cancelButton.setStyle(Util.setButtonStyle("white", "13px", "rgba(255, 255, 255, 0.3)", "5px")));

             

        grid.add(formContainer, 0, 0);
        //grid.add(imageContainer, 1, 0);
        grid.setMaxSize(685, 450);
        
        getChildren().addAll(titleContainer, grid, buttonContainer);
    
        
            
        //Scene scene = new Scene(vbox,1000,700);
        //return scene;
    }
    
    private void cancel(){
        //Login login = new Login();
        //Util.addScene(primaryStage, login.getLoginScene(primaryStage));
        scrollPane.back();
    }
    
    private void signUp(){
        if(addNewAccount()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Account created successfully!");
            alert.showAndWait();
            scrollPane.back();
            scrollPane.refresh();
        }
        
    }
    
    private Tourist getAllInputs() {
        String firstName = fnameField.getText().trim();
        String middleName = midnameField.getText().trim();
        String lastName = lnameField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String confirmPassword = confpasswordField.getText().trim();
        String gender = maleButton.isSelected() ? "Male" : (femaleButton.isSelected() ? "Female" : "");
        LocalDate birthday = bdayPicker.getValue();
        

        if (firstName.isEmpty() || lastName.isEmpty() || phone.isEmpty() ||
            email.isEmpty() || username.isEmpty() || password.isEmpty() ||
            confirmPassword.isEmpty() || gender.isEmpty() || birthday == null) {
            showAlert("Missing Field", "Please fill out all required fields.");
            return null;
        }

        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$")) {
            showAlert("Invalid Email", "Please enter a valid email address.");
            return null;
        }

        if (!phone.matches("^\\d{10,11}$")) {
            showAlert("Invalid Phone", "Phone number must be 10 to 11 digits.");
            return null;
        }

        if (!username.matches("^[a-zA-Z0-9]{5,15}$")) {
            showAlert("Invalid Username", "Username must be alphanumeric and 5–15 characters.");
            return null;
        }

        if (!password.matches("^(?=.*[A-Za-z])(?=.*\\d).{6,}$")) {
            showAlert("Weak Password", "Password must be at least 6 characters, include letters and numbers.");
            return null;
        }

        if (!password.equals(confirmPassword)) {
            showAlert("Password Mismatch", "Passwords do not match.");
            return null;
        }

        return new Tourist(
                touristManager.generateTouristID() , firstName, 
                middleName, lastName,
                birthday, gender, phone,
                email, username, password
        );
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    
    
    private boolean addNewAccount(){
        Tourist tourist = getAllInputs();
        if(tourist != null){
            touristManager.addTourist(tourist);
            return true;
        }
        else{
            System.out.println("burat samar philippines ka");
            return false;
        }
    }
    
}