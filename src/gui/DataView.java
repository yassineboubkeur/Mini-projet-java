package gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DataView {
    public static void showDataView() {
        Stage stage = new Stage();
        stage.setTitle("Données Scraper");

        TableView<Job> table = new TableView<>();

        // Colonnes
        TableColumn<Job, String> titleCol = new TableColumn<>("Titre");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Job, String> contractCol = new TableColumn<>("Type de Contrat");
        contractCol.setCellValueFactory(new PropertyValueFactory<>("contract"));

        TableColumn<Job, String> expCol = new TableColumn<>("Expérience demandée");
        expCol.setCellValueFactory(new PropertyValueFactory<>("experience"));

        table.getColumns().addAll(titleCol, contractCol, expCol);

        // Charger les données dans le tableau
        ObservableList<Job> data = FXCollections.observableArrayList(
                new Job("Développeur Java", "CDI", "2 ans"),
                new Job("Chef de projet", "CDD", "5 ans")
        );
        table.setItems(data);

        VBox vbox = new VBox(table);
        stage.setScene(new Scene(vbox, 600, 400));
        stage.show();
    }
}
