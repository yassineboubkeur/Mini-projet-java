
package gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

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

        TableColumn<Job, String> startDateCol = new TableColumn<>("Date de début");
        startDateCol.setCellValueFactory(new PropertyValueFactory<>("startDate"));

        TableColumn<Job, String> endDateCol = new TableColumn<>("Date de fin");
        endDateCol.setCellValueFactory(new PropertyValueFactory<>("endDate"));

        TableColumn<Job, Integer> jobPostsCol = new TableColumn<>("Nombre de postes");
        jobPostsCol.setCellValueFactory(new PropertyValueFactory<>("jobPosts"));

        table.getColumns().addAll(titleCol, contractCol, expCol, startDateCol, endDateCol, jobPostsCol);

        // Charger les données depuis la base de données MySQL
        ObservableList<Job> data = getJobDataFromDatabase();
        table.setItems(data);

        VBox vbox = new VBox(table);
        stage.setScene(new Scene(vbox, 800, 600));
        stage.show();
    }

    private static ObservableList<Job> getJobDataFromDatabase() {
        ObservableList<Job> jobList = FXCollections.observableArrayList();

        String url = "jdbc:mysql://localhost:3306/jobs_db"; // Remplacez par votre URL de base de données
        String username = "root"; // Remplacez par votre nom d'utilisateur
        String password = "root"; // Remplacez par votre mot de passe

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String query = "SELECT job_title, contract_type, experience_required, start_date, end_date, job_posts FROM job_offers";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                String jobTitle = rs.getString("job_title");
                String contractType = rs.getString("contract_type");
                String experienceRequired = rs.getString("experience_required");
                String startDate = rs.getString("start_date");
                String endDate = rs.getString("end_date");
                int jobPosts = rs.getInt("job_posts");

                Job job = new Job(jobTitle, contractType, experienceRequired, startDate, endDate, jobPosts);
                jobList.add(job);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return jobList;
    }
}










//package gui;
//
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.scene.Scene;
//import javafx.scene.control.TableColumn;
//import javafx.scene.control.TableView;
//import javafx.scene.control.cell.PropertyValueFactory;
//import javafx.scene.layout.VBox;
//import javafx.stage.Stage;
//
//public class DataView {
//    public static void showDataView() {
//        Stage stage = new Stage();
//        stage.setTitle("Données Scraper");
//
//        TableView<Job> table = new TableView<>();
//
//        // Colonnes
//        TableColumn<Job, String> titleCol = new TableColumn<>("Titre");
//        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
//
//        TableColumn<Job, String> contractCol = new TableColumn<>("Type de Contrat");
//        contractCol.setCellValueFactory(new PropertyValueFactory<>("contract"));
//
//        TableColumn<Job, String> expCol = new TableColumn<>("Expérience demandée");
//        expCol.setCellValueFactory(new PropertyValueFactory<>("experience"));
//
//        table.getColumns().addAll(titleCol, contractCol, expCol);
//
//        // Charger les données dans le tableau
//        ObservableList<Job> data = FXCollections.observableArrayList(
//                new Job("Développeur Java", "CDI", "2 ans"),
//                new Job("Chef de projet", "CDD", "5 ans")
//        );
//        table.setItems(data);
//
//        VBox vbox = new VBox(table);
//        stage.setScene(new Scene(vbox, 600, 400));
//        stage.show();
//    }
//}
