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
        stage.setTitle("Scrape Job offers");

        Button scrapeButton = new Button("Start scraping");
        {
            try {
                Scraping.main(null); // Appel du scraping
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Scraping Successfull !Please Click Display Data button to Show results");
                alert.showAndWait();
            } catch (Exception e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Error scraping : " + e.getMessage());
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
