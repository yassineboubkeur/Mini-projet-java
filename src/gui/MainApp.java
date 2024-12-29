package gui;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class MainApp extends Application {
    private TableView<Job> jobTableView;
    private ObservableList<Job> allJobs;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Application ML & NLP");

        // Title label
        Label titleLabel = new Label("Welcome to Web Scrapper Tool");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-padding: 10; -fx-background-color: darksalmon; "
                + "-fx-text-fill: rgb(255,255,255); -fx-border-radius: 8px; -fx-background-radius: 8px; "
                + "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.8), 6, 0, 0, 0); -fx-cursor: hand;");

        // Buttons
        Button scrapeButton = new Button("Scraper les annonces");
        Button viewDataButton = new Button("Afficher les données");
        Button classifyButton = new Button("Appliquer la classification");
        Button deleteButton = new Button("Supprimer les données");

        // Add actions to buttons
        scrapeButton.setOnAction(event -> ScrapingView.showScrapingView());

        viewDataButton.setOnAction(event -> {
            allJobs = DataView.getJobDataFromDatabase();
            jobTableView.setItems(allJobs);
        });

        classifyButton.setOnAction(event -> ClassificationView.showClassificationView());

        deleteButton.setOnAction(event -> {
            jobTableView.getItems().clear(); // Clear the table
            DataView.deleteAllJobsFromDatabase(); // Delete all jobs from the database
        });
//****************************
        // Filter components
        Label filterLabel = new Label("Filtrer Par Titre :");
        TextField filterTextField = new TextField();
        filterTextField.setPromptText("Titre de l'annonce");

        filterTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (allJobs != null) {
                ObservableList<Job> filteredJobs = FXCollections.observableArrayList();
                for (Job job : allJobs) {
                    if (job.getTitle().toLowerCase().contains(newValue.toLowerCase())) {
                        filteredJobs.add(job);
                    }
                }
                jobTableView.setItems(filteredJobs);
            }
        });

        // Layouts
        VBox leftBox = new VBox(10);
        leftBox.getChildren().addAll(scrapeButton, viewDataButton, classifyButton, deleteButton, filterLabel, filterTextField);
        leftBox.setStyle("-fx-padding: 20; -fx-border-color: lightgray; -fx-border-width: 1; -fx-border-radius: 8px;");

        jobTableView = DataView.getDataTableView();
        VBox rightBox = new VBox(10);
        rightBox.getChildren().addAll(jobTableView);
        rightBox.setStyle("-fx-padding: 20; -fx-border-color: lightgray; -fx-border-width: 1; -fx-border-radius: 8px;");

        GridPane gridPane = new GridPane();
        gridPane.setHgap(20);
        gridPane.setVgap(20);

        ColumnConstraints leftColumn = new ColumnConstraints();
        leftColumn.setPercentWidth(25);

        ColumnConstraints rightColumn = new ColumnConstraints();
        rightColumn.setPercentWidth(75);

        gridPane.getColumnConstraints().addAll(leftColumn, rightColumn);
        gridPane.add(leftBox, 0, 0);
        gridPane.add(rightBox, 1, 0);

        gridPane.setStyle("-fx-padding: 20;");
        gridPane.setAlignment(javafx.geometry.Pos.CENTER);

        VBox mainVBox = new VBox(20, titleLabel, gridPane);
        mainVBox.setStyle("-fx-padding: 10;");
        mainVBox.setAlignment(javafx.geometry.Pos.TOP_CENTER);

        Scene scene = new Scene(mainVBox, 1200, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Web Scrapper Tool");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}









//
//
//
//package gui;
//
//import javafx.application.Application;
//import javafx.scene.Scene;
//import javafx.scene.control.*;
//import javafx.scene.layout.*;
//import javafx.stage.Stage;
//
//public class MainApp extends Application {
//    private TableView<Job> jobTableView;
//
//    @Override
//    public void start(Stage primaryStage) {
//        primaryStage.setTitle("Application ML & NLP");
//
//        // Title label
//        Label titleLabel = new Label("Welcome to Web Scrapper Tool");
//        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-padding: 10; -fx-background-color: darksalmon; "
//                + "-fx-text-fill: rgb(255,255,255); -fx-border-radius: 8px; -fx-background-radius: 8px; "
//                + "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.8), 6, 0, 0, 0); -fx-cursor: hand;");
//
//        // Buttons
//        Button scrapeButton = new Button("Scraper les annonces");
//        Button viewDataButton = new Button("Afficher les données");
//        Button classifyButton = new Button("Appliquer la classification");
//        Button deleteButton = new Button("Supprimer les données");
//
//        // Add actions to buttons
//        scrapeButton.setOnAction(event -> ScrapingView.showScrapingView());
//
//        viewDataButton.setOnAction(event -> {
//            jobTableView.setItems(DataView.getJobDataFromDatabase());
//        });
//
//        classifyButton.setOnAction(event -> {
//            ClassificationView.showClassificationView();
//        });
//
//        deleteButton.setOnAction(event -> {
//            jobTableView.getItems().clear(); // Clear the table
//            DataView.deleteAllJobsFromDatabase(); // Delete all jobs from the database
//        });
//
//        // Layouts
//        VBox leftBox = new VBox(10);
//        leftBox.getChildren().addAll(scrapeButton, viewDataButton, classifyButton, deleteButton);
//        leftBox.setStyle("-fx-padding: 20; -fx-border-color: lightgray; -fx-border-width: 1; -fx-border-radius: 8px;");
//
//        jobTableView = DataView.getDataTableView();
//        VBox rightBox = new VBox(10);
//        rightBox.getChildren().addAll(jobTableView);
//        rightBox.setStyle("-fx-padding: 20; -fx-border-color: lightgray; -fx-border-width: 1; -fx-border-radius: 8px;");
//
//        GridPane gridPane = new GridPane();
//        gridPane.setHgap(20);
//        gridPane.setVgap(20);
//
//        ColumnConstraints leftColumn = new ColumnConstraints();
//        leftColumn.setPercentWidth(25);
//
//        ColumnConstraints rightColumn = new ColumnConstraints();
//        rightColumn.setPercentWidth(75);
//
//        gridPane.getColumnConstraints().addAll(leftColumn, rightColumn);
//        gridPane.add(leftBox, 0, 0);
//        gridPane.add(rightBox, 1, 0);
//
//        gridPane.setStyle("-fx-padding: 20;");
//        gridPane.setAlignment(javafx.geometry.Pos.CENTER);
//
//        VBox mainVBox = new VBox(20, titleLabel, gridPane);
//        mainVBox.setStyle("-fx-padding: 10;");
//        mainVBox.setAlignment(javafx.geometry.Pos.TOP_CENTER);
//
//        Scene scene = new Scene(mainVBox, 1200, 600);
//        primaryStage.setScene(scene);
//        primaryStage.setTitle("Web Scrapper Tool");
//        primaryStage.show();
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}

