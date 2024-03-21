import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class Setting extends Application{

public void start(Stage primaryStage){
        primaryStage.setTitle("Chat Application");


        Label helloWorldLabel = new Label("Setting Page!");
        helloWorldLabel.setAlignment(Pos.CENTER);
        Scene primaryScene = new Scene(helloWorldLabel);
        primaryStage.setScene(primaryScene);

        primaryStage.show();
    
    }
}
