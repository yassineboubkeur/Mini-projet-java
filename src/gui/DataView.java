package gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DataView {

    // Database connection constants
    private static final String DB_URL = "jdbc:mysql://localhost:3306/jobs_db";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "root";

    public static TableView<Job> getDataTableView() {
        TableView<Job> table = new TableView<>();

        // Style the table
        table.setStyle("-fx-font-size: 14px; -fx-font-family: 'Arial';");
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); // Responsive columns

        // Columns
        TableColumn<Job, String> titleCol = new TableColumn<>("Titre");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleCol.setStyle("-fx-alignment: CENTER-LEFT;");

        TableColumn<Job, String> contractCol = new TableColumn<>("Type de Contrat");
        contractCol.setCellValueFactory(new PropertyValueFactory<>("contract"));
        contractCol.setStyle("-fx-alignment: CENTER;");

        TableColumn<Job, String> expCol = new TableColumn<>("Expérience demandée");
        expCol.setCellValueFactory(new PropertyValueFactory<>("experience"));
        expCol.setStyle("-fx-alignment: CENTER;");

        TableColumn<Job, String> startDateCol = new TableColumn<>("Date de début");
        startDateCol.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        startDateCol.setStyle("-fx-alignment: CENTER;");

        TableColumn<Job, String> endDateCol = new TableColumn<>("Date de fin");
        endDateCol.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        endDateCol.setStyle("-fx-alignment: CENTER;");

        TableColumn<Job, Integer> jobPostsCol = new TableColumn<>("Nombre de postes");
        jobPostsCol.setCellValueFactory(new PropertyValueFactory<>("jobPosts"));
        jobPostsCol.setStyle("-fx-alignment: CENTER;");

        // Add columns to the table
        table.getColumns().addAll(titleCol, contractCol, expCol, startDateCol, endDateCol, jobPostsCol);

        // Load data from the database
        ObservableList<Job> data = getJobDataFromDatabase();
        table.setItems(data);

        // Style the table rows
        table.setRowFactory(tv -> {
            javafx.scene.control.TableRow<Job> row = new javafx.scene.control.TableRow<>() {
                @Override
                protected void updateItem(Job item, boolean empty) {
                    super.updateItem(item, empty);
                    if (isEmpty() || item == null) {
                        setStyle("");
                    } else {
                        // Default row style
                        setStyle("-fx-background-color: #f9f9f9;");
                    }
                }
            };

            // Add hover effect
            row.hoverProperty().addListener((obs, wasHovered, isNowHovered) -> {
                if (isNowHovered) {
                    row.setStyle("-fx-background-color: #e0f7fa;");
                } else {
                    row.setStyle("-fx-background-color: #f9f9f9;");
                }
            });

            // Add focus effect
            row.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                if (isNowFocused) {
                    row.setStyle("-fx-text-fill: black; -fx-font-weight: bold; -fx-background-color: #e0f7fa;");
                } else {
                    row.setStyle("-fx-text-fill: black; -fx-font-weight: normal; -fx-background-color: #f9f9f9;");
                }
            });

            // Override default selection styling
            row.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
                if (isNowSelected) {
                    row.setStyle("-fx-text-fill: black; -fx-font-weight: bold; -fx-background-color: #e0f7fa;");
                } else {
                    row.setStyle("-fx-text-fill: black; -fx-font-weight: normal; -fx-background-color: #f9f9f9;");
                }
            });

            return row;
        });

        // Enable vertical growth of the table
        VBox.setVgrow(table, Priority.ALWAYS);

        return table;
    }

    // Method to delete all jobs from the database
    public static void deleteAllJobsFromDatabase() {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String query = "DELETE FROM job_offers";
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(query);
            System.out.println("All jobs deleted successfully.");
        } catch (Exception e) {
            System.err.println("Error deleting jobs: " + e.getMessage());
        }
    }

    // Method to fetch job data from the database
    public static ObservableList<Job> getJobDataFromDatabase() {
        ObservableList<Job> jobList = FXCollections.observableArrayList();

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
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
            System.err.println("Error fetching jobs: " + e.getMessage());
        }

        return jobList;
    }
}



