package gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import weka.WekaClassifier;

import java.util.List;

public class ClassificationView {
    public static void showClassificationView() {
        Stage stage = new Stage();
        stage.setTitle("Classification des Annonces");

        // Bouton pour exécuter la classification
        Button classifyButton = new Button("Lancer la classification");
        TableView<ClassificationResult> table = new TableView<>();

        classifyButton.setOnAction(event -> {
            try {
                // Appel de la méthode de classification
                List<ClassificationResult> results = WekaClassifier.classifyJobs("jobs.csv");

                // Charger les résultats dans la table
                ObservableList<ClassificationResult> data = FXCollections.observableArrayList(results);
                table.setItems(data);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Classification terminée avec succès !");
                alert.showAndWait();
            } catch (Exception e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Erreur lors de la classification : " + e.getMessage());
                alert.showAndWait();
            }
        });

        // Colonnes pour afficher les résultats
        TableColumn<ClassificationResult, String> titleCol = new TableColumn<>("Titre");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<ClassificationResult, String> actualCol = new TableColumn<>("Type de Contrat (Réel)");
        actualCol.setCellValueFactory(new PropertyValueFactory<>("actualContract"));

        TableColumn<ClassificationResult, String> predictedCol = new TableColumn<>("Type de Contrat (Prédit)");
        predictedCol.setCellValueFactory(new PropertyValueFactory<>("predictedContract"));

        table.getColumns().addAll(titleCol, actualCol, predictedCol);

        VBox vbox = new VBox(10, classifyButton, table);
        vbox.setStyle("-fx-padding: 20; -fx-alignment: center;");
        stage.setScene(new Scene(vbox, 600, 400));
        stage.show();
    }
}
