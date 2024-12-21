package gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Application ML & NLP");

        // Menu principal
        Button scrapeButton = new Button("Scraper les annonces");
        Button viewDataButton = new Button("Afficher les données");
        Button classifyButton = new Button("Appliquer la classification");

        scrapeButton.setOnAction(event -> ScrapingView.showScrapingView());
        viewDataButton.setOnAction(event -> DataView.showDataView());
        classifyButton.setOnAction(event -> ClassificationView.showClassificationView());

        VBox vbox = new VBox(10, scrapeButton, viewDataButton, classifyButton);
        vbox.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Scene scene = new Scene(vbox, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
