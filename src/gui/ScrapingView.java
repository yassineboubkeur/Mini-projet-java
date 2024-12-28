package gui;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import scraping.Scraping;

public class ScrapingView {
    public static void showScrapingView() {
        Stage stage = new Stage();
        stage.setTitle("Scraper les annonces");

        Button scrapeButton = new Button("Commencer le scraping");
        {
            try {
                Scraping.main(null); // Appel du scraping
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Scraping terminé avec succès !");
                alert.showAndWait();
            } catch (Exception e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Erreur lors du scraping : " + e.getMessage());
                alert.showAndWait();
            }
        };
//
//        VBox vbox = new VBox(10, scrapeButton);
//        vbox.setStyle("-fx-padding: 20; -fx-alignment: center;");
//        stage.setScene(new Scene(vbox, 300, 200));
//        stage.show();
    }
}
