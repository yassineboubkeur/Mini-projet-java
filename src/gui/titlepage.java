package gui;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
public class titlepage extends Application {

	@Override
    public void start(Stage primaryStage) {
        // Create a label
        Label h1Label = new Label("Welcome to Web Scrapper App");
        
        // Apply H1 styling using CSS
        h1Label.setStyle("-fx-font-size: 32px; -fx-font-weight: bold;");

        // Create a layout and add the label
        StackPane root = new StackPane(h1Label);
        Scene scene = new Scene(root, 400, 200);

        // Set the stage
        primaryStage.setTitle("JavaFX H1 Example");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
