
package gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DataView {

    public static TableView<Job> getDataTableView() {
        TableView<Job> table = new TableView<>();

        // Columns
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

        // Load data from the database
        ObservableList<Job> data = getJobDataFromDatabase();
        table.setItems(data);

        return table;
    }
//    methode pour la suppression des donnees sur la table mysql 
    
    public static void deleteAllJobsFromDatabase() {
        String url = "jdbc:mysql://localhost:3306/jobs_db"; // Remplacez par votre URL de base de données
        String username = "root"; // Remplacez par votre nom d'utilisateur
        String password = ""; // Remplacez par votre mot de passe

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String query = "DELETE FROM job_offers";
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//

    static ObservableList<Job> getJobDataFromDatabase() {
        ObservableList<Job> jobList = FXCollections.observableArrayList();

        String url = "jdbc:mysql://localhost:3306/jobs_db"; // Replace with your database URL
        String username = "root"; // Replace with your username
        String password = ""; // Replace with your password

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

