import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class NewContact{

    public static void display() {
        Stage Dialog = new Stage();
        Dialog.initModality(Modality.APPLICATION_MODAL);
        Dialog.setTitle("Add Friend");


        Label pseudoLabel = new Label("Pseudo: ");
        TextField pseudoTextField = new TextField();


        Button AddButton = new Button("Add");
        AddButton.setOnAction(e->{
            // DataBase Manupilation
            
            Dialog.close();
        });

        GridPane gridPane = new GridPane();
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.setPadding(new Insets(10));
        
        gridPane.add(pseudoLabel, 0, 0);
        gridPane.add(pseudoTextField, 1, 0);
        
        gridPane.add(AddButton, 1, 1);
        
        gridPane.setAlignment(Pos.CENTER);
        Scene scene = new Scene(gridPane);
        Dialog.setScene(scene);
        Dialog.showAndWait();
    }
}
