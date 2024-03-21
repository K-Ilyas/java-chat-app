

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class Register extends Application {


    UserDAO user_orm = new UserDAO(MySQLConnectSingleton.getInstance());
    UserInformation userInformation = null;
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("User Registration");
    
        // Create a grid pane for the registration form
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setVgap(15);
        gridPane.setHgap(27);

        // Add name label and text field
        Label nameLabel = new Label("Name:");
        GridPane.setConstraints(nameLabel, 0, 0);
        TextField nameInput = new TextField();
        GridPane.setConstraints(nameInput, 1, 0);

        // Add email label and text field
        Label emailLabel = new Label("Email:");
        GridPane.setConstraints(emailLabel, 0, 1);
        TextField emailInput = new TextField();
        GridPane.setConstraints(emailInput, 1, 1);

        // Add bio label and text area
        Label passwordLabel = new Label("Password:");
        GridPane.setConstraints(passwordLabel, 0, 2);
        PasswordField passwordInput = new PasswordField();
        // passwordInput.setEditable(true);
        GridPane.setConstraints(passwordInput, 1, 2);

        // Add preferred language label and choice box
        Label languageLabel = new Label("Preferred Language:");
        GridPane.setConstraints(languageLabel, 0, 3);
        ChoiceBox<String> languageChoiceBox = new ChoiceBox<>();
        languageChoiceBox.getItems().addAll("English", "French", "arabic");
        languageChoiceBox.setValue("English");
        GridPane.setConstraints(languageChoiceBox, 1, 3);

        // Add register button
        Button register = new Button("Register");
        GridPane.setConstraints(register, 1, 4);

        Button LoginButton = new Button("Login");
        // place register button on the right to login button
        GridPane.setConstraints(LoginButton, 0, 4);

        LoginButton.setOnAction(e -> {
            Login login = new Login();
            login.start(primaryStage);
        });
        

        register.setOnAction(e -> {
            String uuid = "undefined";
            String name = nameInput.getText();
            String email = emailInput.getText();
            String password = passwordInput.getText();
            String profile_picture = "https://robohash.org/" + name + ".png";
            

            userInformation = new UserInformation(uuid,name,password,email,profile_picture,false);

            user_orm.create(userInformation);

            showAlert(Alert.AlertType.INFORMATION, "Registration Successful",
                    "User registration successful. Name: " + name);
            Session.setLoggedInUser(userInformation);
            Home homePage = new Home();
            homePage.start(primaryStage);

        });

        gridPane.getChildren().addAll(nameLabel, nameInput, emailLabel, emailInput,
        passwordLabel, passwordInput,languageLabel, languageChoiceBox, register);

         VBox vbox = new VBox(gridPane);
 
         Scene scene = new Scene(vbox, 700, 350);
         primaryStage.setScene(scene);
         primaryStage.show();
    }


    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
