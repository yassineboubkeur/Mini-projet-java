package gui;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainApp extends Application {
    private TableView<Job> jobTableView;
    private ObservableList<Job> allJobs;

    // Variables to store the initial position of the window when dragging
    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Application ML & NLP");

        // Remove default window decorations and make the stage transparent
        primaryStage.initStyle(StageStyle.TRANSPARENT);

        // Title label
        Label titleLabel = new Label("Welcome to Web Scraper Tool");
        titleLabel.setFont(Font.font("Arial", 24));
        titleLabel.setTextFill(Color.WHITE);
        titleLabel.setStyle("-fx-padding: 10; -fx-background-color: linear-gradient(to right, #6a11cb, #2575fc); "
                + "-fx-border-radius: 8px; -fx-background-radius: 8px; "
                + "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.8), 10, 0, 0, 0); -fx-cursor: hand;");

        // Add hover effect to titleLabel
        titleLabel.setOnMouseEntered(e -> titleLabel.setStyle("-fx-padding: 10; -fx-background-color: linear-gradient(to right, #2575fc, #6a11cb); "
                + "-fx-border-radius: 8px; -fx-background-radius: 8px; "
                + "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.8), 10, 0, 0, 0); -fx-cursor: hand;"));

        titleLabel.setOnMouseExited(e -> titleLabel.setStyle("-fx-padding: 10; -fx-background-color: linear-gradient(to right, #6a11cb, #2575fc); "
                + "-fx-border-radius: 8px; -fx-background-radius: 8px; "
                + "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.8), 10, 0, 0, 0); -fx-cursor: hand;"));

        // Minimize, Maximize, and Close buttons
        Button minimizeButton = createWindowControlButton("—");
        Button maximizeButton = createWindowControlButton("□");
        Button closeButton = createWindowControlButton("X");

        // Set actions for the buttons
        minimizeButton.setOnAction(event -> primaryStage.setIconified(true)); // Minimize the window
        maximizeButton.setOnAction(event -> primaryStage.setMaximized(!primaryStage.isMaximized())); // Toggle maximize
        closeButton.setOnAction(event -> primaryStage.close()); // Close the window

        // Create an HBox for the window control buttons
        HBox windowControlBox = new HBox(5, minimizeButton, maximizeButton, closeButton);
        windowControlBox.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);
        windowControlBox.setStyle("-fx-padding: 5;");

        // Create a flexible spacer to push the titleLabel and windowControlBox apart
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Create a new HBox to hold the titleLabel, spacer, and windowControlBox
        HBox titleBox = new HBox(titleLabel, spacer, windowControlBox);
        titleBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        titleBox.setStyle("-fx-padding: 20;");

        // Add drag-and-drop functionality to the titleBox
        titleBox.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        titleBox.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() - xOffset);
            primaryStage.setY(event.getScreenY() - yOffset);
        });

        // Buttons
        Button scrapeButton = createStyledButton("Scraper les annonces");
        Button viewDataButton = createStyledButton("Afficher les données");
        Button classifyButton = createStyledButton("Appliquer la classification");
        Button deleteButton = createStyledButton("Supprimer les données");

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

        // Filter components
        Label filterLabel = new Label("Filtrer Par Titre :");
        filterLabel.setFont(Font.font("Arial", 14));
        filterLabel.setTextFill(Color.WHITE);

        TextField filterTextField = new TextField();
        filterTextField.setPromptText("Titre de l'annonce");
        filterTextField.setStyle("-fx-padding: 5; -fx-font-size: 14; -fx-background-radius: 5;");

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
        VBox leftBox = new VBox(15);
        // Add filter components first
        leftBox.getChildren().addAll(filterLabel, filterTextField, scrapeButton, viewDataButton, classifyButton, deleteButton);
        leftBox.setStyle("-fx-padding: 20; -fx-background-color: rgba(255, 255, 255, 0.1); -fx-border-radius: 10px; -fx-background-radius: 10px;");

        jobTableView = DataView.getDataTableView();
        VBox rightBox = new VBox(10);
        rightBox.getChildren().addAll(jobTableView);
        rightBox.setStyle("-fx-padding: 20; -fx-background-color: rgba(255, 255, 255, 0.1); -fx-border-radius: 10px; -fx-background-radius: 10px;");

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

        // Footer layout with flexible space
        Label footerLeft = new Label("Réalisé par: Yassine Boubkeur et Abdessamad El Maaroufi");
        footerLeft.setFont(Font.font("Arial", 12));
        footerLeft.setTextFill(Color.WHITE);

        Label footerRight = new Label("Prof : Mr. Aniss Moumen");
        footerRight.setFont(Font.font("Arial", 12));
        footerRight.setTextFill(Color.WHITE);

        // Create a flexible space (Region) to push the labels apart
        Region flexibleSpace = new Region();
        HBox.setHgrow(flexibleSpace, Priority.ALWAYS);

        // Create the HBox and add the labels and flexible space
        HBox footerBox = new HBox(10, footerLeft, flexibleSpace, footerRight);  // Using 10px space between the labels
        footerBox.setAlignment(javafx.geometry.Pos.BOTTOM_LEFT);
        footerBox.setStyle("-fx-padding: 10; -fx-background-color: rgba(0, 0, 0, 0.3);");

        // Main layout with a gradient background
        VBox mainVBox = new VBox(20, titleBox, gridPane, footerBox);
        mainVBox.setStyle("-fx-padding: 20; -fx-background-color: linear-gradient(to bottom, #1e3c72, #2a5298); "
                + "-fx-border-radius: 20px; -fx-background-radius: 20px;");

        // Create the scene with a transparent fill
        Scene scene = new Scene(mainVBox, 1200, 600);
        scene.setFill(Color.TRANSPARENT); // Make the scene background transparent

        // Apply the border radius to the root container
        mainVBox.setStyle(mainVBox.getStyle() + "-fx-border-radius: 20px; -fx-background-radius: 20px;");

        primaryStage.setScene(scene);
        primaryStage.setTitle("Web Scraper Tool");
        primaryStage.show();
    }

    // Helper method to create styled buttons
    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setFont(Font.font("Arial", 14));
        button.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 10 20; "
                + "-fx-border-radius: 5px; -fx-background-radius: 5px; -fx-cursor: hand; "
                + "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.3), 5, 0, 0, 0);");
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #45a049; -fx-text-fill: white; -fx-padding: 10 20; "
                + "-fx-border-radius: 5px; -fx-background-radius: 5px; -fx-cursor: hand; "
                + "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.3), 5, 0, 0, 0);"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 10 20; "
                + "-fx-border-radius: 5px; -fx-background-radius: 5px; -fx-cursor: hand; "
                + "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.3), 5, 0, 0, 0);"));
        return button;
    }

    // Helper method to create window control buttons (Minimize, Maximize, Close)
    private Button createWindowControlButton(String text) {
        Button button = new Button(text);
        button.setFont(Font.font("Arial", 12));
        button.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-padding: 5 10; "
                + "-fx-border-radius: 5px; -fx-background-radius: 5px; -fx-cursor: hand;");
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: rgba(255, 255, 255, 0.2); -fx-text-fill: white; -fx-padding: 5 10; "
                + "-fx-border-radius: 5px; -fx-background-radius: 5px; -fx-cursor: hand;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-padding: 5 10; "
                + "-fx-border-radius: 5px; -fx-background-radius: 5px; -fx-cursor: hand;"));
        return button;
    }

    public static void main(String[] args) {
        launch(args);
    }
}


//
//package gui;
//
//import javafx.application.Application;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.scene.Scene;
//import javafx.scene.control.*;
//import javafx.scene.layout.*;
//import javafx.scene.paint.Color;
//import javafx.scene.text.Font;
//import javafx.stage.Stage;
//import javafx.stage.StageStyle;
//
//public class MainApp extends Application {
//    private TableView<Job> jobTableView;
//    private ObservableList<Job> allJobs;
//
//    // Variables to store the initial position of the window when dragging
//    private double xOffset = 0;
//    private double yOffset = 0;
//
//    @Override
//    public void start(Stage primaryStage) {
//        primaryStage.setTitle("Application ML & NLP");
//
//        // Remove default window decorations and make the stage transparent
//        primaryStage.initStyle(StageStyle.TRANSPARENT);
//
//        // Title label
//        Label titleLabel = new Label("Welcome to Web Scraper Tool");
//        titleLabel.setFont(Font.font("Arial", 24));
//        titleLabel.setTextFill(Color.WHITE);
//        titleLabel.setStyle("-fx-padding: 10; -fx-background-color: linear-gradient(to right, #6a11cb, #2575fc); "
//                + "-fx-border-radius: 8px; -fx-background-radius: 8px; "
//                + "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.8), 10, 0, 0, 0); -fx-cursor: hand;");
//
//        // Add hover effect to titleLabel
//        titleLabel.setOnMouseEntered(e -> titleLabel.setStyle("-fx-padding: 10; -fx-background-color: linear-gradient(to right, #2575fc, #6a11cb); "
//                + "-fx-border-radius: 8px; -fx-background-radius: 8px; "
//                + "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.8), 10, 0, 0, 0); -fx-cursor: hand;"));
//
//        titleLabel.setOnMouseExited(e -> titleLabel.setStyle("-fx-padding: 10; -fx-background-color: linear-gradient(to right, #6a11cb, #2575fc); "
//                + "-fx-border-radius: 8px; -fx-background-radius: 8px; "
//                + "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.8), 10, 0, 0, 0); -fx-cursor: hand;"));
//
//        // Minimize, Maximize, and Close buttons
//        Button minimizeButton = createWindowControlButton("—");
//        Button maximizeButton = createWindowControlButton("□");
//        Button closeButton = createWindowControlButton("X");
//
//        // Set actions for the buttons
//        minimizeButton.setOnAction(event -> primaryStage.setIconified(true)); // Minimize the window
//        maximizeButton.setOnAction(event -> primaryStage.setMaximized(!primaryStage.isMaximized())); // Toggle maximize
//        closeButton.setOnAction(event -> primaryStage.close()); // Close the window
//
//        // Create an HBox for the window control buttons
//        HBox windowControlBox = new HBox(5, minimizeButton, maximizeButton, closeButton);
//        windowControlBox.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);
//        windowControlBox.setStyle("-fx-padding: 5;");
//
//        // Create a flexible spacer to push the titleLabel and windowControlBox apart
//        Region spacer = new Region();
//        HBox.setHgrow(spacer, Priority.ALWAYS);
//
//        // Create a new HBox to hold the titleLabel, spacer, and windowControlBox
//        HBox titleBox = new HBox(titleLabel, spacer, windowControlBox);
//        titleBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
//        titleBox.setStyle("-fx-padding: 20;");
//
//        // Add drag-and-drop functionality to the titleBox
//        titleBox.setOnMousePressed(event -> {
//            xOffset = event.getSceneX();
//            yOffset = event.getSceneY();
//        });
//
//        titleBox.setOnMouseDragged(event -> {
//            primaryStage.setX(event.getScreenX() - xOffset);
//            primaryStage.setY(event.getScreenY() - yOffset);
//        });
//
//        // Buttons
//        Button scrapeButton = createStyledButton("Scraper les annonces");
//        Button viewDataButton = createStyledButton("Afficher les données");
//        Button classifyButton = createStyledButton("Appliquer la classification");
//        Button deleteButton = createStyledButton("Supprimer les données");
//
//        // Add actions to buttons
//        scrapeButton.setOnAction(event -> ScrapingView.showScrapingView());
//
//        viewDataButton.setOnAction(event -> {
//            allJobs = DataView.getJobDataFromDatabase();
//            jobTableView.setItems(allJobs);
//        });
//
//        classifyButton.setOnAction(event -> ClassificationView.showClassificationView());
//
//        deleteButton.setOnAction(event -> {
//            jobTableView.getItems().clear(); // Clear the table
//            DataView.deleteAllJobsFromDatabase(); // Delete all jobs from the database
//        });
//
//        // Filter components
//        Label filterLabel = new Label("Filtrer Par Titre :");
//        filterLabel.setFont(Font.font("Arial", 14));
//        filterLabel.setTextFill(Color.WHITE);
//
//        TextField filterTextField = new TextField();
//        filterTextField.setPromptText("Titre de l'annonce");
//        filterTextField.setStyle("-fx-padding: 5; -fx-font-size: 14; -fx-background-radius: 5;");
//
//        filterTextField.textProperty().addListener((observable, oldValue, newValue) -> {
//            if (allJobs != null) {
//                ObservableList<Job> filteredJobs = FXCollections.observableArrayList();
//                for (Job job : allJobs) {
//                    if (job.getTitle().toLowerCase().contains(newValue.toLowerCase())) {
//                        filteredJobs.add(job);
//                    }
//                }
//                jobTableView.setItems(filteredJobs);
//            }
//        });
//
//        // Layouts
//        VBox leftBox = new VBox(15);
//        leftBox.getChildren().addAll(scrapeButton, viewDataButton, classifyButton, deleteButton, filterLabel, filterTextField);
//        leftBox.setStyle("-fx-padding: 20; -fx-background-color: rgba(255, 255, 255, 0.1); -fx-border-radius: 10px; -fx-background-radius: 10px;");
//
//        jobTableView = DataView.getDataTableView();
//        VBox rightBox = new VBox(10);
//        rightBox.getChildren().addAll(jobTableView);
//        rightBox.setStyle("-fx-padding: 20; -fx-background-color: rgba(255, 255, 255, 0.1); -fx-border-radius: 10px; -fx-background-radius: 10px;");
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
//        // Footer layout with flexible space
//        Label footerLeft = new Label("Realiser par: Yassine Beubkeur et Abdessamad El Maaroufi");
//        footerLeft.setFont(Font.font("Arial", 12));
//        footerLeft.setTextFill(Color.WHITE);
//
//        Label footerRight = new Label("Prof : Mr. Aniss");
//        footerRight.setFont(Font.font("Arial", 12));
//        footerRight.setTextFill(Color.WHITE);
//
//        // Create a flexible space (Region) to push the labels apart
//        Region flexibleSpace = new Region();
//        HBox.setHgrow(flexibleSpace, Priority.ALWAYS);
//
//        // Create the HBox and add the labels and flexible space
//        HBox footerBox = new HBox(10, footerLeft, flexibleSpace, footerRight);  // Using 10px space between the labels
//        footerBox.setAlignment(javafx.geometry.Pos.BOTTOM_LEFT);
//        footerBox.setStyle("-fx-padding: 10; -fx-background-color: rgba(0, 0, 0, 0.3);");
//
//        // Main layout with a gradient background
//        VBox mainVBox = new VBox(20, titleBox, gridPane, footerBox);
//        mainVBox.setStyle("-fx-padding: 20; -fx-background-color: linear-gradient(to bottom, #1e3c72, #2a5298); "
//                + "-fx-border-radius: 20px; -fx-background-radius: 20px;");
//
//        // Create the scene with a transparent fill
//        Scene scene = new Scene(mainVBox, 1200, 600);
//        scene.setFill(Color.TRANSPARENT); // Make the scene background transparent
//
//        // Apply the border radius to the root container
//        mainVBox.setStyle(mainVBox.getStyle() + "-fx-border-radius: 20px; -fx-background-radius: 20px;");
//
//        primaryStage.setScene(scene);
//        primaryStage.setTitle("Web Scraper Tool");
//        primaryStage.show();
//    }
//
//    // Helper method to create styled buttons
//    private Button createStyledButton(String text) {
//        Button button = new Button(text);
//        button.setFont(Font.font("Arial", 14));
//        button.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 10 20; "
//                + "-fx-border-radius: 5px; -fx-background-radius: 5px; -fx-cursor: hand; "
//                + "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.3), 5, 0, 0, 0);");
//        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #45a049; -fx-text-fill: white; -fx-padding: 10 20; "
//                + "-fx-border-radius: 5px; -fx-background-radius: 5px; -fx-cursor: hand; "
//                + "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.3), 5, 0, 0, 0);"));
//        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 10 20; "
//                + "-fx-border-radius: 5px; -fx-background-radius: 5px; -fx-cursor: hand; "
//                + "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.3), 5, 0, 0, 0);"));
//        return button;
//    }
//
//    // Helper method to create window control buttons (Minimize, Maximize, Close)
//    private Button createWindowControlButton(String text) {
//        Button button = new Button(text);
//        button.setFont(Font.font("Arial", 12));
//        button.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-padding: 5 10; "
//                + "-fx-border-radius: 5px; -fx-background-radius: 5px; -fx-cursor: hand;");
//        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: rgba(255, 255, 255, 0.2); -fx-text-fill: white; -fx-padding: 5 10; "
//                + "-fx-border-radius: 5px; -fx-background-radius: 5px; -fx-cursor: hand;"));
//        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-padding: 5 10; "
//                + "-fx-border-radius: 5px; -fx-background-radius: 5px; -fx-cursor: hand;"));
//        return button;
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}

//
//package gui;
//
//import javafx.application.Application;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.scene.Scene;
//import javafx.scene.control.*;
//import javafx.scene.layout.*;
//import javafx.scene.paint.Color;
//import javafx.scene.text.Font;
//import javafx.stage.Stage;
//import javafx.stage.StageStyle;
//
//public class MainApp extends Application {
//    private TableView<Job> jobTableView;
//    private ObservableList<Job> allJobs;
//
//    // Variables to store the initial position of the window when dragging
//    private double xOffset = 0;
//    private double yOffset = 0;
//
//    @Override
//    public void start(Stage primaryStage) {
//        primaryStage.setTitle("Application ML & NLP");
//
//        // Remove default window decorations
//        primaryStage.initStyle(StageStyle.UNDECORATED);
//
//        // Title label
//        Label titleLabel = new Label("Welcome to Web Scraper Tool");
//        titleLabel.setFont(Font.font("Arial", 24));
//        titleLabel.setTextFill(Color.WHITE);
//        titleLabel.setStyle("-fx-padding: 10; -fx-background-color: linear-gradient(to right, #6a11cb, #2575fc); "
//                + "-fx-border-radius: 8px; -fx-background-radius: 8px; "
//                + "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.8), 10, 0, 0, 0); -fx-cursor: hand;");
//
//        // Add hover effect to titleLabel
//        titleLabel.setOnMouseEntered(e -> titleLabel.setStyle("-fx-padding: 10; -fx-background-color: linear-gradient(to right, #2575fc, #6a11cb); "
//                + "-fx-border-radius: 8px; -fx-background-radius: 8px; "
//                + "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.8), 10, 0, 0, 0); -fx-cursor: hand;"));
//
//        titleLabel.setOnMouseExited(e -> titleLabel.setStyle("-fx-padding: 10; -fx-background-color: linear-gradient(to right, #6a11cb, #2575fc); "
//                + "-fx-border-radius: 8px; -fx-background-radius: 8px; "
//                + "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.8), 10, 0, 0, 0); -fx-cursor: hand;"));
//
//        // Minimize, Maximize, and Close buttons
//        Button minimizeButton = createWindowControlButton("—");
//        Button maximizeButton = createWindowControlButton("□");
//        Button closeButton = createWindowControlButton("X");
//
//        // Set actions for the buttons
//        minimizeButton.setOnAction(event -> primaryStage.setIconified(true)); // Minimize the window
//        maximizeButton.setOnAction(event -> primaryStage.setMaximized(!primaryStage.isMaximized())); // Toggle maximize
//        closeButton.setOnAction(event -> primaryStage.close()); // Close the window
//
//        // Create an HBox for the window control buttons
//        HBox windowControlBox = new HBox(5, minimizeButton, maximizeButton, closeButton);
//        windowControlBox.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);
//        windowControlBox.setStyle("-fx-padding: 5;");
//
//        // Create a flexible spacer to push the titleLabel and windowControlBox apart
//        Region spacer = new Region();
//        HBox.setHgrow(spacer, Priority.ALWAYS);
//
//        // Create a new HBox to hold the titleLabel, spacer, and windowControlBox
//        HBox titleBox = new HBox(titleLabel, spacer, windowControlBox);
//        titleBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
//        titleBox.setStyle("-fx-padding: 20;");
//
//        // Add drag-and-drop functionality to the titleBox
//        titleBox.setOnMousePressed(event -> {
//            xOffset = event.getSceneX();
//            yOffset = event.getSceneY();
//        });
//
//        titleBox.setOnMouseDragged(event -> {
//            primaryStage.setX(event.getScreenX() - xOffset);
//            primaryStage.setY(event.getScreenY() - yOffset);
//        });
//
//        // Buttons
//        Button scrapeButton = createStyledButton("Scraper les annonces");
//        Button viewDataButton = createStyledButton("Afficher les données");
//        Button classifyButton = createStyledButton("Appliquer la classification");
//        Button deleteButton = createStyledButton("Supprimer les données");
//
//        // Add actions to buttons
//        scrapeButton.setOnAction(event -> ScrapingView.showScrapingView());
//
//        viewDataButton.setOnAction(event -> {
//            allJobs = DataView.getJobDataFromDatabase();
//            jobTableView.setItems(allJobs);
//        });
//
//        classifyButton.setOnAction(event -> ClassificationView.showClassificationView());
//
//        deleteButton.setOnAction(event -> {
//            jobTableView.getItems().clear(); // Clear the table
//            DataView.deleteAllJobsFromDatabase(); // Delete all jobs from the database
//        });
//
//        // Filter components
//        Label filterLabel = new Label("Filtrer Par Titre :");
//        filterLabel.setFont(Font.font("Arial", 14));
//        filterLabel.setTextFill(Color.WHITE);
//
//        TextField filterTextField = new TextField();
//        filterTextField.setPromptText("Titre de l'annonce");
//        filterTextField.setStyle("-fx-padding: 5; -fx-font-size: 14; -fx-background-radius: 5;");
//
//        filterTextField.textProperty().addListener((observable, oldValue, newValue) -> {
//            if (allJobs != null) {
//                ObservableList<Job> filteredJobs = FXCollections.observableArrayList();
//                for (Job job : allJobs) {
//                    if (job.getTitle().toLowerCase().contains(newValue.toLowerCase())) {
//                        filteredJobs.add(job);
//                    }
//                }
//                jobTableView.setItems(filteredJobs);
//            }
//        });
//
//        // Layouts
//        VBox leftBox = new VBox(15);
//        leftBox.getChildren().addAll(scrapeButton, viewDataButton, classifyButton, deleteButton, filterLabel, filterTextField);
//        leftBox.setStyle("-fx-padding: 20; -fx-background-color: rgba(255, 255, 255, 0.1); -fx-border-radius: 10px; -fx-background-radius: 10px;");
//
//        jobTableView = DataView.getDataTableView();
//        VBox rightBox = new VBox(10);
//        rightBox.getChildren().addAll(jobTableView);
//        rightBox.setStyle("-fx-padding: 20; -fx-background-color: rgba(255, 255, 255, 0.1); -fx-border-radius: 10px; -fx-background-radius: 10px;");
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
//        // Footer layout with flexible space
//        Label footerLeft = new Label("Realiser par: Yassine Beubkeur et Abdessamad El Maaroufi");
//        footerLeft.setFont(Font.font("Arial", 12));
//        footerLeft.setTextFill(Color.WHITE);
//
//        Label footerRight = new Label("Prof : Mr. Aniss");
//        footerRight.setFont(Font.font("Arial", 12));
//        footerRight.setTextFill(Color.WHITE);
//
//        // Create a flexible space (Region) to push the labels apart
//        Region flexibleSpace = new Region();
//        HBox.setHgrow(flexibleSpace, Priority.ALWAYS);
//
//        // Create the HBox and add the labels and flexible space
//        HBox footerBox = new HBox(10, footerLeft, flexibleSpace, footerRight);  // Using 10px space between the labels
//        footerBox.setAlignment(javafx.geometry.Pos.BOTTOM_LEFT);
//        footerBox.setStyle("-fx-padding: 10; -fx-background-color: rgba(0, 0, 0, 0.3);");
//
//        // Main layout with a gradient background
//        VBox mainVBox = new VBox(20, titleBox, gridPane, footerBox);
//        mainVBox.setStyle("-fx-padding: 20; -fx-background-color: linear-gradient(to bottom, #1e3c72, #2a5298);");
//        mainVBox.setAlignment(javafx.geometry.Pos.TOP_CENTER);
//
//        Scene scene = new Scene(mainVBox, 1200, 600);
//        primaryStage.setScene(scene);
//        primaryStage.setTitle("Web Scraper Tool");
//        primaryStage.show();
//    }
//
//    // Helper method to create styled buttons
//    private Button createStyledButton(String text) {
//        Button button = new Button(text);
//        button.setFont(Font.font("Arial", 14));
//        button.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 10 20; "
//                + "-fx-border-radius: 5px; -fx-background-radius: 5px; -fx-cursor: hand; "
//                + "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.3), 5, 0, 0, 0);");
//        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #45a049; -fx-text-fill: white; -fx-padding: 10 20; "
//                + "-fx-border-radius: 5px; -fx-background-radius: 5px; -fx-cursor: hand; "
//                + "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.3), 5, 0, 0, 0);"));
//        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 10 20; "
//                + "-fx-border-radius: 5px; -fx-background-radius: 5px; -fx-cursor: hand; "
//                + "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.3), 5, 0, 0, 0);"));
//        return button;
//    }
//
//    // Helper method to create window control buttons (Minimize, Maximize, Close)
//    private Button createWindowControlButton(String text) {
//        Button button = new Button(text);
//        button.setFont(Font.font("Arial", 12));
//        button.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-padding: 5 10; "
//                + "-fx-border-radius: 5px; -fx-background-radius: 5px; -fx-cursor: hand;");
//        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: rgba(255, 255, 255, 0.2); -fx-text-fill: white; -fx-padding: 5 10; "
//                + "-fx-border-radius: 5px; -fx-background-radius: 5px; -fx-cursor: hand;"));
//        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-padding: 5 10; "
//                + "-fx-border-radius: 5px; -fx-background-radius: 5px; -fx-cursor: hand;"));
//        return button;
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}

//package gui;
//
//import javafx.application.Application;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.scene.Scene;
//import javafx.scene.control.*;
//import javafx.scene.layout.*;
//import javafx.scene.paint.Color;
//import javafx.scene.text.Font;
//import javafx.stage.Stage;
//import javafx.stage.StageStyle;
//
//public class MainApp extends Application {
//    private TableView<Job> jobTableView;
//    private ObservableList<Job> allJobs;
//
//    // Variables to store the initial position of the window when dragging
//    private double xOffset = 0;
//    private double yOffset = 0;
//
//    @Override
//    public void start(Stage primaryStage) {
//        primaryStage.setTitle("Application ML & NLP");
//
//        // Remove default window decorations
//        primaryStage.initStyle(StageStyle.UNDECORATED);
//
//        // Title label
//        Label titleLabel = new Label("Welcome to Web Scraper Tool");
//        titleLabel.setFont(Font.font("Arial", 24));
//        titleLabel.setTextFill(Color.WHITE);
//        titleLabel.setStyle("-fx-padding: 10; -fx-background-color: linear-gradient(to right, #6a11cb, #2575fc); "
//                + "-fx-border-radius: 8px; -fx-background-radius: 8px; "
//                + "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.8), 10, 0, 0, 0); -fx-cursor: hand;");
//
//        // Add hover effect to titleLabel
//        titleLabel.setOnMouseEntered(e -> titleLabel.setStyle("-fx-padding: 10; -fx-background-color: linear-gradient(to right, #2575fc, #6a11cb); "
//                + "-fx-border-radius: 8px; -fx-background-radius: 8px; "
//                + "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.8), 10, 0, 0, 0); -fx-cursor: hand;"));
//
//        titleLabel.setOnMouseExited(e -> titleLabel.setStyle("-fx-padding: 10; -fx-background-color: linear-gradient(to right, #6a11cb, #2575fc); "
//                + "-fx-border-radius: 8px; -fx-background-radius: 8px; "
//                + "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.8), 10, 0, 0, 0); -fx-cursor: hand;"));
//
//        // Minimize, Maximize, and Close buttons
//        Button minimizeButton = createWindowControlButton("—");
//        Button maximizeButton = createWindowControlButton("□");
//        Button closeButton = createWindowControlButton("X");
//
//        // Set actions for the buttons
//        minimizeButton.setOnAction(event -> primaryStage.setIconified(true)); // Minimize the window
//        maximizeButton.setOnAction(event -> primaryStage.setMaximized(!primaryStage.isMaximized())); // Toggle maximize
//        closeButton.setOnAction(event -> primaryStage.close()); // Close the window
//
//        // Create an HBox for the window control buttons
//        HBox windowControlBox = new HBox(5, minimizeButton, maximizeButton, closeButton);
//        windowControlBox.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);
//        windowControlBox.setStyle("-fx-padding: 5;");
//
//        // Create a new HBox to hold the titleLabel and windowControlBox
//        HBox titleBox = new HBox(titleLabel, windowControlBox);
//        titleBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
//        titleBox.setStyle("-fx-padding: 20;");
//        HBox.setHgrow(titleLabel, Priority.ALWAYS); // Make the titleLabel take up remaining space
//
//        // Add drag-and-drop functionality to the titleBox
//        titleBox.setOnMousePressed(event -> {
//            xOffset = event.getSceneX();
//            yOffset = event.getSceneY();
//        });
//
//        titleBox.setOnMouseDragged(event -> {
//            primaryStage.setX(event.getScreenX() - xOffset);
//            primaryStage.setY(event.getScreenY() - yOffset);
//        });
//
//        // Buttons
//        Button scrapeButton = createStyledButton("Scraper les annonces");
//        Button viewDataButton = createStyledButton("Afficher les données");
//        Button classifyButton = createStyledButton("Appliquer la classification");
//        Button deleteButton = createStyledButton("Supprimer les données");
//
//        // Add actions to buttons
//        scrapeButton.setOnAction(event -> ScrapingView.showScrapingView());
//
//        viewDataButton.setOnAction(event -> {
//            allJobs = DataView.getJobDataFromDatabase();
//            jobTableView.setItems(allJobs);
//        });
//
//        classifyButton.setOnAction(event -> ClassificationView.showClassificationView());
//
//        deleteButton.setOnAction(event -> {
//            jobTableView.getItems().clear(); // Clear the table
//            DataView.deleteAllJobsFromDatabase(); // Delete all jobs from the database
//        });
//
//        // Filter components
//        Label filterLabel = new Label("Filtrer Par Titre :");
//        filterLabel.setFont(Font.font("Arial", 14));
//        filterLabel.setTextFill(Color.WHITE);
//
//        TextField filterTextField = new TextField();
//        filterTextField.setPromptText("Titre de l'annonce");
//        filterTextField.setStyle("-fx-padding: 5; -fx-font-size: 14; -fx-background-radius: 5;");
//
//        filterTextField.textProperty().addListener((observable, oldValue, newValue) -> {
//            if (allJobs != null) {
//                ObservableList<Job> filteredJobs = FXCollections.observableArrayList();
//                for (Job job : allJobs) {
//                    if (job.getTitle().toLowerCase().contains(newValue.toLowerCase())) {
//                        filteredJobs.add(job);
//                    }
//                }
//                jobTableView.setItems(filteredJobs);
//            }
//        });
//
//        // Layouts
//        VBox leftBox = new VBox(15);
//        leftBox.getChildren().addAll(scrapeButton, viewDataButton, classifyButton, deleteButton, filterLabel, filterTextField);
//        leftBox.setStyle("-fx-padding: 20; -fx-background-color: rgba(255, 255, 255, 0.1); -fx-border-radius: 10px; -fx-background-radius: 10px;");
//
//        jobTableView = DataView.getDataTableView();
//        VBox rightBox = new VBox(10);
//        rightBox.getChildren().addAll(jobTableView);
//        rightBox.setStyle("-fx-padding: 20; -fx-background-color: rgba(255, 255, 255, 0.1); -fx-border-radius: 10px; -fx-background-radius: 10px;");
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
//        // Footer layout with flexible space
//        Label footerLeft = new Label("Realiser par: Yassine Beubkeur et Abdessamad El Maaroufi");
//        footerLeft.setFont(Font.font("Arial", 12));
//        footerLeft.setTextFill(Color.WHITE);
//
//        Label footerRight = new Label("Prof : Mr. Aniss");
//        footerRight.setFont(Font.font("Arial", 12));
//        footerRight.setTextFill(Color.WHITE);
//
//        // Create a flexible space (Region) to push the labels apart
//        Region flexibleSpace = new Region();
//        HBox.setHgrow(flexibleSpace, Priority.ALWAYS);
//
//        // Create the HBox and add the labels and flexible space
//        HBox footerBox = new HBox(10, footerLeft, flexibleSpace, footerRight);  // Using 10px space between the labels
//        footerBox.setAlignment(javafx.geometry.Pos.BOTTOM_LEFT);
//        footerBox.setStyle("-fx-padding: 10; -fx-background-color: rgba(0, 0, 0, 0.3);");
//
//        // Main layout with a gradient background
//        VBox mainVBox = new VBox(20, titleBox, gridPane, footerBox);
//        mainVBox.setStyle("-fx-padding: 20; -fx-background-color: linear-gradient(to bottom, #1e3c72, #2a5298);");
//        mainVBox.setAlignment(javafx.geometry.Pos.TOP_CENTER);
//
//        Scene scene = new Scene(mainVBox, 1200, 600);
//        primaryStage.setScene(scene);
//        primaryStage.setTitle("Web Scraper Tool");
//        primaryStage.show();
//    }
//
//    // Helper method to create styled buttons
//    private Button createStyledButton(String text) {
//        Button button = new Button(text);
//        button.setFont(Font.font("Arial", 14));
//        button.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 10 20; "
//                + "-fx-border-radius: 5px; -fx-background-radius: 5px; -fx-cursor: hand; "
//                + "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.3), 5, 0, 0, 0);");
//        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #45a049; -fx-text-fill: white; -fx-padding: 10 20; "
//                + "-fx-border-radius: 5px; -fx-background-radius: 5px; -fx-cursor: hand; "
//                + "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.3), 5, 0, 0, 0);"));
//        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 10 20; "
//                + "-fx-border-radius: 5px; -fx-background-radius: 5px; -fx-cursor: hand; "
//                + "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.3), 5, 0, 0, 0);"));
//        return button;
//    }
//
//    // Helper method to create window control buttons (Minimize, Maximize, Close)
//    private Button createWindowControlButton(String text) {
//        Button button = new Button(text);
//        button.setFont(Font.font("Arial", 12));
//        button.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-padding: 5 10; "
//                + "-fx-border-radius: 5px; -fx-background-radius: 5px; -fx-cursor: hand;");
//        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: rgba(255, 255, 255, 0.2); -fx-text-fill: white; -fx-padding: 5 10; "
//                + "-fx-border-radius: 5px; -fx-background-radius: 5px; -fx-cursor: hand;"));
//        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-padding: 5 10; "
//                + "-fx-border-radius: 5px; -fx-background-radius: 5px; -fx-cursor: hand;"));
//        return button;
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}


//
//package gui;
//
//import javafx.application.Application;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.scene.Scene;
//import javafx.scene.control.*;
//import javafx.scene.layout.*;
//import javafx.scene.paint.Color;
//import javafx.scene.text.Font;
//import javafx.stage.Stage;
//import javafx.stage.StageStyle;
//
//public class MainApp extends Application {
//    private TableView<Job> jobTableView;
//    private ObservableList<Job> allJobs;
//
//    @Override
//    public void start(Stage primaryStage) {
//        primaryStage.setTitle("Application ML & NLP");
//
//        // Remove default window decorations
//        primaryStage.initStyle(StageStyle.UNDECORATED);
//
//        // Title label
//        Label titleLabel = new Label("Welcome to Web Scraper Tool");
//        titleLabel.setFont(Font.font("Arial", 24));
//        titleLabel.setTextFill(Color.WHITE);
//        titleLabel.setStyle("-fx-padding: 10; -fx-background-color: linear-gradient(to right, #6a11cb, #2575fc); "
//                + "-fx-border-radius: 8px; -fx-background-radius: 8px; "
//                + "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.8), 10, 0, 0, 0); -fx-cursor: hand;");
//
//        // Add hover effect to titleLabel
//        titleLabel.setOnMouseEntered(e -> titleLabel.setStyle("-fx-padding: 10; -fx-background-color: linear-gradient(to right, #2575fc, #6a11cb); "
//                + "-fx-border-radius: 8px; -fx-background-radius: 8px; "
//                + "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.8), 10, 0, 0, 0); -fx-cursor: hand;"));
//
//        titleLabel.setOnMouseExited(e -> titleLabel.setStyle("-fx-padding: 10; -fx-background-color: linear-gradient(to right, #6a11cb, #2575fc); "
//                + "-fx-border-radius: 8px; -fx-background-radius: 8px; "
//                + "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.8), 10, 0, 0, 0); -fx-cursor: hand;"));
//
//        // Minimize, Maximize, and Close buttons
//        Button minimizeButton = createWindowControlButton("—");
//        Button maximizeButton = createWindowControlButton("□");
//        Button closeButton = createWindowControlButton("X");
//
//        // Set actions for the buttons
//        minimizeButton.setOnAction(event -> primaryStage.setIconified(true)); // Minimize the window
//        maximizeButton.setOnAction(event -> primaryStage.setMaximized(!primaryStage.isMaximized())); // Toggle maximize
//        closeButton.setOnAction(event -> primaryStage.close()); // Close the window
//
//        // Create an HBox for the window control buttons
//        HBox windowControlBox = new HBox(5, minimizeButton, maximizeButton, closeButton);
//        windowControlBox.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);
//        windowControlBox.setStyle("-fx-padding: 5;");
//
//        // Create a new HBox to hold the titleLabel and windowControlBox
//        HBox titleBox = new HBox(titleLabel, windowControlBox);
//        titleBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
//        titleBox.setStyle("-fx-padding: 20;");
//        HBox.setHgrow(titleLabel, Priority.ALWAYS); // Make the titleLabel take up remaining space
//
//        // Buttons
//        Button scrapeButton = createStyledButton("Scraper les annonces");
//        Button viewDataButton = createStyledButton("Afficher les données");
//        Button classifyButton = createStyledButton("Appliquer la classification");
//        Button deleteButton = createStyledButton("Supprimer les données");
//
//        // Add actions to buttons
//        scrapeButton.setOnAction(event -> ScrapingView.showScrapingView());
//
//        viewDataButton.setOnAction(event -> {
//            allJobs = DataView.getJobDataFromDatabase();
//            jobTableView.setItems(allJobs);
//        });
//
//        classifyButton.setOnAction(event -> ClassificationView.showClassificationView());
//
//        deleteButton.setOnAction(event -> {
//            jobTableView.getItems().clear(); // Clear the table
//            DataView.deleteAllJobsFromDatabase(); // Delete all jobs from the database
//        });
//
//        // Filter components
//        Label filterLabel = new Label("Filtrer Par Titre :");
//        filterLabel.setFont(Font.font("Arial", 14));
//        filterLabel.setTextFill(Color.WHITE);
//
//        TextField filterTextField = new TextField();
//        filterTextField.setPromptText("Titre de l'annonce");
//        filterTextField.setStyle("-fx-padding: 5; -fx-font-size: 14; -fx-background-radius: 5;");
//
//        filterTextField.textProperty().addListener((observable, oldValue, newValue) -> {
//            if (allJobs != null) {
//                ObservableList<Job> filteredJobs = FXCollections.observableArrayList();
//                for (Job job : allJobs) {
//                    if (job.getTitle().toLowerCase().contains(newValue.toLowerCase())) {
//                        filteredJobs.add(job);
//                    }
//                }
//                jobTableView.setItems(filteredJobs);
//            }
//        });
//
//        // Layouts
//        VBox leftBox = new VBox(15);
//        leftBox.getChildren().addAll(scrapeButton, viewDataButton, classifyButton, deleteButton, filterLabel, filterTextField);
//        leftBox.setStyle("-fx-padding: 20; -fx-background-color: rgba(255, 255, 255, 0.1); -fx-border-radius: 10px; -fx-background-radius: 10px;");
//
//        jobTableView = DataView.getDataTableView();
//        VBox rightBox = new VBox(10);
//        rightBox.getChildren().addAll(jobTableView);
//        rightBox.setStyle("-fx-padding: 20; -fx-background-color: rgba(255, 255, 255, 0.1); -fx-border-radius: 10px; -fx-background-radius: 10px;");
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
//        // Footer layout with flexible space
//        Label footerLeft = new Label("Realiser par: Yassine Beubkeur et Abdessamad El Maaroufi");
//        footerLeft.setFont(Font.font("Arial", 12));
//        footerLeft.setTextFill(Color.WHITE);
//
//        Label footerRight = new Label("Prof : Mr. Aniss");
//        footerRight.setFont(Font.font("Arial", 12));
//        footerRight.setTextFill(Color.WHITE);
//
//        // Create a flexible space (Region) to push the labels apart
//        Region flexibleSpace = new Region();
//        HBox.setHgrow(flexibleSpace, Priority.ALWAYS);
//
//        // Create the HBox and add the labels and flexible space
//        HBox footerBox = new HBox(10, footerLeft, flexibleSpace, footerRight);  // Using 10px space between the labels
//        footerBox.setAlignment(javafx.geometry.Pos.BOTTOM_LEFT);
//        footerBox.setStyle("-fx-padding: 10; -fx-background-color: rgba(0, 0, 0, 0.3);");
//
//        // Main layout with a gradient background
//        VBox mainVBox = new VBox(20, titleBox, gridPane, footerBox);
//        mainVBox.setStyle("-fx-padding: 20; -fx-background-color: linear-gradient(to bottom, #1e3c72, #2a5298);");
//        mainVBox.setAlignment(javafx.geometry.Pos.TOP_CENTER);
//
//        Scene scene = new Scene(mainVBox, 1200, 600);
//        primaryStage.setScene(scene);
//        primaryStage.setTitle("Web Scraper Tool");
//        primaryStage.show();
//    }
//
//    // Helper method to create styled buttons
//    private Button createStyledButton(String text) {
//        Button button = new Button(text);
//        button.setFont(Font.font("Arial", 14));
//        button.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 10 20; "
//                + "-fx-border-radius: 5px; -fx-background-radius: 5px; -fx-cursor: hand; "
//                + "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.3), 5, 0, 0, 0);");
//        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #45a049; -fx-text-fill: white; -fx-padding: 10 20; "
//                + "-fx-border-radius: 5px; -fx-background-radius: 5px; -fx-cursor: hand; "
//                + "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.3), 5, 0, 0, 0);"));
//        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 10 20; "
//                + "-fx-border-radius: 5px; -fx-background-radius: 5px; -fx-cursor: hand; "
//                + "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.3), 5, 0, 0, 0);"));
//        return button;
//    }
//
//    // Helper method to create window control buttons (Minimize, Maximize, Close)
//    private Button createWindowControlButton(String text) {
//        Button button = new Button(text);
//        button.setFont(Font.font("Arial", 12));
//        button.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-padding: 5 10; "
//                + "-fx-border-radius: 5px; -fx-background-radius: 5px; -fx-cursor: hand;");
//        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: rgba(255, 255, 255, 0.2); -fx-text-fill: white; -fx-padding: 5 10; "
//                + "-fx-border-radius: 5px; -fx-background-radius: 5px; -fx-cursor: hand;"));
//        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-padding: 5 10; "
//                + "-fx-border-radius: 5px; -fx-background-radius: 5px; -fx-cursor: hand;"));
//        return button;
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}


//
//package gui;
//
//import javafx.application.Application;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.scene.Scene;
//import javafx.scene.control.*;
//import javafx.scene.layout.*;
//import javafx.scene.paint.Color;
//import javafx.scene.text.Font;
//import javafx.stage.Stage;
//
//public class MainApp extends Application {
//    private TableView<Job> jobTableView;
//    private ObservableList<Job> allJobs;
//
//    @Override
//    public void start(Stage primaryStage) {
//        primaryStage.setTitle("Application ML & NLP");
//
//        // Title label
//        Label titleLabel = new Label("Welcome to Web Scraper Tool");
//        titleLabel.setFont(Font.font("Arial", 24));
//        titleLabel.setTextFill(Color.WHITE);
//        titleLabel.setStyle("-fx-padding: 10; -fx-background-color: linear-gradient(to right, #6a11cb, #2575fc); "
//                + "-fx-border-radius: 8px; -fx-background-radius: 8px; "
//                + "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.8), 10, 0, 0, 0); -fx-cursor: hand;");
//
//        // Add hover effect to titleLabel
//        titleLabel.setOnMouseEntered(e -> titleLabel.setStyle("-fx-padding: 10; -fx-background-color: linear-gradient(to right, #2575fc, #6a11cb); "
//                + "-fx-border-radius: 8px; -fx-background-radius: 8px; "
//                + "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.8), 10, 0, 0, 0); -fx-cursor: hand;"));
//
//        titleLabel.setOnMouseExited(e -> titleLabel.setStyle("-fx-padding: 10; -fx-background-color: linear-gradient(to right, #6a11cb, #2575fc); "
//                + "-fx-border-radius: 8px; -fx-background-radius: 8px; "
//                + "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.8), 10, 0, 0, 0); -fx-cursor: hand;"));
//
//        // Close button
//        Button closeButton = new Button("X");
//        closeButton.setStyle("-fx-background-color: #ff4444; -fx-text-fill: white; -fx-font-weight: bold; "
//                + "-fx-padding: 5 10; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-cursor: hand;");
//        closeButton.setOnAction(event -> primaryStage.close());
//
//        // Buttons
//        Button scrapeButton = createStyledButton("Scraper les annonces");
//        Button viewDataButton = createStyledButton("Afficher les données");
//        Button classifyButton = createStyledButton("Appliquer la classification");
//        Button deleteButton = createStyledButton("Supprimer les données");
//
//        // Add actions to buttons
//        scrapeButton.setOnAction(event -> ScrapingView.showScrapingView());
//
//        viewDataButton.setOnAction(event -> {
//            allJobs = DataView.getJobDataFromDatabase();
//            jobTableView.setItems(allJobs);
//        });
//
//        classifyButton.setOnAction(event -> ClassificationView.showClassificationView());
//
//        deleteButton.setOnAction(event -> {
//            jobTableView.getItems().clear(); // Clear the table
//            DataView.deleteAllJobsFromDatabase(); // Delete all jobs from the database
//        });
//
//        // Filter components
//        Label filterLabel = new Label("Filtrer Par Titre :");
//        filterLabel.setFont(Font.font("Arial", 14));
//        filterLabel.setTextFill(Color.WHITE);
//
//        TextField filterTextField = new TextField();
//        filterTextField.setPromptText("Titre de l'annonce");
//        filterTextField.setStyle("-fx-padding: 5; -fx-font-size: 14; -fx-background-radius: 5;");
//
//        filterTextField.textProperty().addListener((observable, oldValue, newValue) -> {
//            if (allJobs != null) {
//                ObservableList<Job> filteredJobs = FXCollections.observableArrayList();
//                for (Job job : allJobs) {
//                    if (job.getTitle().toLowerCase().contains(newValue.toLowerCase())) {
//                        filteredJobs.add(job);
//                    }
//                }
//                jobTableView.setItems(filteredJobs);
//            }
//        });
//
//        // Layouts
//        VBox leftBox = new VBox(15);
//        leftBox.getChildren().addAll(scrapeButton, viewDataButton, classifyButton, deleteButton, filterLabel, filterTextField);
//        leftBox.setStyle("-fx-padding: 20; -fx-background-color: rgba(255, 255, 255, 0.1); -fx-border-radius: 10px; -fx-background-radius: 10px;");
//
//        jobTableView = DataView.getDataTableView();
//        VBox rightBox = new VBox(10);
//        rightBox.getChildren().addAll(jobTableView);
//        rightBox.setStyle("-fx-padding: 20; -fx-background-color: rgba(255, 255, 255, 0.1); -fx-border-radius: 10px; -fx-background-radius: 10px;");
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
//        // Create a new HBox to hold the titleLabel and closeButton
//        HBox titleBox = new HBox(titleLabel, closeButton);
//        titleBox.setAlignment(javafx.geometry.Pos.TOP_LEFT);
//        titleBox.setStyle("-fx-padding: 20;");
//        HBox.setHgrow(titleLabel, Priority.ALWAYS); // Make the titleLabel take up remaining space
//        HBox.setMargin(closeButton, new javafx.geometry.Insets(0, 0, 0, 10)); // Add margin to closeButton
//        
//     // Footer layout with flexible space
//        Label footerLeft = new Label("Realiser par: Yassine Beubkeur et Abdessamad El Maaroufi");
//        footerLeft.setFont(Font.font("Arial", 12));
//        footerLeft.setTextFill(Color.WHITE);
//
//        Label footerRight = new Label("Prof : Mr. Aniss");
//        footerRight.setFont(Font.font("Arial", 12));
//        footerRight.setTextFill(Color.WHITE);
//
//        // Create a flexible space (Region) to push the labels apart
//        Region flexibleSpace = new Region();
//        HBox.setHgrow(flexibleSpace, Priority.ALWAYS);
//
//        // Create the HBox and add the labels and flexible space
//        HBox footerBox = new HBox(10, footerLeft, flexibleSpace, footerRight);  // Using 10px space between the labels
//        footerBox.setAlignment(javafx.geometry.Pos.BOTTOM_LEFT);
//        footerBox.setStyle("-fx-padding: 10; -fx-background-color: rgba(0, 0, 0, 0.3);");
//
//        
//        // Main layout with a gradient background
//        VBox mainVBox = new VBox(20, titleBox, gridPane, footerBox);
//        mainVBox.setStyle("-fx-padding: 20; -fx-background-color: linear-gradient(to bottom, #1e3c72, #2a5298);");
//        mainVBox.setAlignment(javafx.geometry.Pos.TOP_CENTER);
//
//        Scene scene = new Scene(mainVBox, 1200, 600);
//        primaryStage.setScene(scene);
//        primaryStage.setTitle("Web Scraper Tool");
//        primaryStage.show();
//    }
//
//    // Helper method to create styled buttons
//    private Button createStyledButton(String text) {
//        Button button = new Button(text);
//        button.setFont(Font.font("Arial", 14));
//        button.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 10 20; "
//                + "-fx-border-radius: 5px; -fx-background-radius: 5px; -fx-cursor: hand; "
//                + "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.3), 5, 0, 0, 0);");
//        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #45a049; -fx-text-fill: white; -fx-padding: 10 20; "
//                + "-fx-border-radius: 5px; -fx-background-radius: 5px; -fx-cursor: hand; "
//                + "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.3), 5, 0, 0, 0);"));
//      button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 10 20; "
//      + "-fx-border-radius: 5px; -fx-background-radius: 5px; -fx-cursor: hand; "
//      + "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.3), 5, 0, 0, 0);"));
//return button;
//}
//
//public static void main(String[] args) {
//launch(args);
//}
//}

//
//package gui;
//
//import javafx.application.Application;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.scene.Scene;
//import javafx.scene.control.*;
//import javafx.scene.layout.*;
//import javafx.scene.paint.Color;
//import javafx.scene.text.Font;
//import javafx.stage.Stage;
//
//public class MainApp extends Application {
//    private TableView<Job> jobTableView;
//    private ObservableList<Job> allJobs;
//
//    @Override
//    public void start(Stage primaryStage) {
//        primaryStage.setTitle("Application ML & NLP");
//
//        // Title label
//        Label titleLabel = new Label("Welcome to Web Scraper Tool");
//        titleLabel.setFont(Font.font("Arial", 24));
//        titleLabel.setTextFill(Color.WHITE);
//        titleLabel.setStyle("-fx-padding: 10; -fx-background-color: linear-gradient(to right, #6a11cb, #2575fc); "
//                + "-fx-border-radius: 8px; -fx-background-radius: 8px; "
//                + "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.8), 10, 0, 0, 0); -fx-cursor: hand;");
//
//        // Add hover effect to titleLabel
//        titleLabel.setOnMouseEntered(e -> titleLabel.setStyle("-fx-padding: 10; -fx-background-color: linear-gradient(to right, #2575fc, #6a11cb); "
//                + "-fx-border-radius: 8px; -fx-background-radius: 8px; "
//                + "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.8), 10, 0, 0, 0); -fx-cursor: hand;"));
//
//        titleLabel.setOnMouseExited(e -> titleLabel.setStyle("-fx-padding: 10; -fx-background-color: linear-gradient(to right, #6a11cb, #2575fc); "
//                + "-fx-border-radius: 8px; -fx-background-radius: 8px; "
//                + "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.8), 10, 0, 0, 0); -fx-cursor: hand;"));
//
//        // Close button
//        Button closeButton = new Button("X");
//        closeButton.setStyle("-fx-background-color: #ff4444; -fx-text-fill: white; -fx-font-weight: bold; "
//                + "-fx-padding: 5 10; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-cursor: hand;");
//        closeButton.setOnAction(event -> primaryStage.close());
//
//        // Buttons
//        Button scrapeButton = createStyledButton("Scraper les annonces");
//        Button viewDataButton = createStyledButton("Afficher les données");
//        Button classifyButton = createStyledButton("Appliquer la classification");
//        Button deleteButton = createStyledButton("Supprimer les données");
//
//        // Add actions to buttons
//        scrapeButton.setOnAction(event -> ScrapingView.showScrapingView());
//
//        viewDataButton.setOnAction(event -> {
//            allJobs = DataView.getJobDataFromDatabase();
//            jobTableView.setItems(allJobs);
//        });
//
//        classifyButton.setOnAction(event -> ClassificationView.showClassificationView());
//
//        deleteButton.setOnAction(event -> {
//            jobTableView.getItems().clear(); // Clear the table
//            DataView.deleteAllJobsFromDatabase(); // Delete all jobs from the database
//        });
//
//        // Filter components
//        Label filterLabel = new Label("Filtrer Par Titre :");
//        filterLabel.setFont(Font.font("Arial", 14));
//        filterLabel.setTextFill(Color.WHITE);
//
//        TextField filterTextField = new TextField();
//        filterTextField.setPromptText("Titre de l'annonce");
//        filterTextField.setStyle("-fx-padding: 5; -fx-font-size: 14; -fx-background-radius: 5;");
//
//        filterTextField.textProperty().addListener((observable, oldValue, newValue) -> {
//            if (allJobs != null) {
//                ObservableList<Job> filteredJobs = FXCollections.observableArrayList();
//                for (Job job : allJobs) {
//                    if (job.getTitle().toLowerCase().contains(newValue.toLowerCase())) {
//                        filteredJobs.add(job);
//                    }
//                }
//                jobTableView.setItems(filteredJobs);
//            }
//        });
//
//        // Layouts
//        VBox leftBox = new VBox(15);
//        leftBox.getChildren().addAll(scrapeButton, viewDataButton, classifyButton, deleteButton, filterLabel, filterTextField);
//        leftBox.setStyle("-fx-padding: 20; -fx-background-color: rgba(255, 255, 255, 0.1); -fx-border-radius: 10px; -fx-background-radius: 10px;");
//
//        jobTableView = DataView.getDataTableView();
//        VBox rightBox = new VBox(10);
//        rightBox.getChildren().addAll(jobTableView);
//        rightBox.setStyle("-fx-padding: 20; -fx-background-color: rgba(255, 255, 255, 0.1); -fx-border-radius: 10px; -fx-background-radius: 10px;");
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
//        // Create a new HBox to hold the titleLabel and closeButton
//        HBox titleBox = new HBox(titleLabel, closeButton);
//        titleBox.setAlignment(javafx.geometry.Pos.TOP_LEFT);
//        titleBox.setStyle("-fx-padding: 20;");
//        HBox.setHgrow(titleLabel, Priority.ALWAYS); // Make the titleLabel take up remaining space
//        HBox.setMargin(closeButton, new javafx.geometry.Insets(0, 0, 0, 10)); // Add margin to closeButton
//
//        // Main layout with a gradient background
//        VBox mainVBox = new VBox(20, titleBox, gridPane);
//        mainVBox.setStyle("-fx-padding: 20; -fx-background-color: linear-gradient(to bottom, #1e3c72, #2a5298);");
//        mainVBox.setAlignment(javafx.geometry.Pos.TOP_CENTER);
//
//        Scene scene = new Scene(mainVBox, 1200, 600);
//        primaryStage.setScene(scene);
//        primaryStage.setTitle("Web Scraper Tool");
//        primaryStage.show();
//    }
//
//    // Helper method to create styled buttons
//    private Button createStyledButton(String text) {
//        Button button = new Button(text);
//        button.setFont(Font.font("Arial", 14));
//        button.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 10 20; "
//                + "-fx-border-radius: 5px; -fx-background-radius: 5px; -fx-cursor: hand; "
//                + "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.3), 5, 0, 0, 0);");
//        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #45a049; -fx-text-fill: white; -fx-padding: 10 20; "
//                + "-fx-border-radius: 5px; -fx-background-radius: 5px; -fx-cursor: hand; "
//                + "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.3), 5, 0, 0, 0);"));
//        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 10 20; "
//                + "-fx-border-radius: 5px; -fx-background-radius: 5px; -fx-cursor: hand; "
//                + "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.3), 5, 0, 0, 0);"));
//        return button;
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}


//
//package gui;
//
//import javafx.application.Application;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.scene.Scene;
//import javafx.scene.control.*;
//import javafx.scene.layout.*;
//import javafx.scene.paint.Color;
//import javafx.scene.text.Font;
//import javafx.stage.Stage;
//
//public class MainApp extends Application {
//    private TableView<Job> jobTableView;
//    private ObservableList<Job> allJobs;
//
//    @Override
//    public void start(Stage primaryStage) {
//        primaryStage.setTitle("Application ML & NLP");
//
//        // Title label
//        Label titleLabel = new Label("Welcome to Web Scraper Tool");
//        titleLabel.setFont(Font.font("Arial", 24));
//        titleLabel.setTextFill(Color.WHITE);
//        titleLabel.setStyle("-fx-padding: 10; -fx-background-color: linear-gradient(to right, #6a11cb, #2575fc); "
//                + "-fx-border-radius: 8px; -fx-background-radius: 8px; "
//                + "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.8), 10, 0, 0, 0); -fx-cursor: hand;");
//
//        // Add hover effect to titleLabel
//        titleLabel.setOnMouseEntered(e -> titleLabel.setStyle("-fx-padding: 10; -fx-background-color: linear-gradient(to right, #2575fc, #6a11cb); "
//                + "-fx-border-radius: 8px; -fx-background-radius: 8px; "
//                + "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.8), 10, 0, 0, 0); -fx-cursor: hand;"));
//
//        titleLabel.setOnMouseExited(e -> titleLabel.setStyle("-fx-padding: 10; -fx-background-color: linear-gradient(to right, #6a11cb, #2575fc); "
//                + "-fx-border-radius: 8px; -fx-background-radius: 8px; "
//                + "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.8), 10, 0, 0, 0); -fx-cursor: hand;"));
//
//        // Buttons
//        Button scrapeButton = createStyledButton("Scraper les annonces");
//        Button viewDataButton = createStyledButton("Afficher les données");
//        Button classifyButton = createStyledButton("Appliquer la classification");
//        Button deleteButton = createStyledButton("Supprimer les données");
//
//        // Add actions to buttons
//        scrapeButton.setOnAction(event -> ScrapingView.showScrapingView());
//
//        viewDataButton.setOnAction(event -> {
//            allJobs = DataView.getJobDataFromDatabase();
//            jobTableView.setItems(allJobs);
//        });
//
//        classifyButton.setOnAction(event -> ClassificationView.showClassificationView());
//
//        deleteButton.setOnAction(event -> {
//            jobTableView.getItems().clear(); // Clear the table
//            DataView.deleteAllJobsFromDatabase(); // Delete all jobs from the database
//        });
//
//        // Filter components
//        Label filterLabel = new Label("Filtrer Par Titre :");
//        filterLabel.setFont(Font.font("Arial", 14));
//        filterLabel.setTextFill(Color.WHITE);
//
//        TextField filterTextField = new TextField();
//        filterTextField.setPromptText("Titre de l'annonce");
//        filterTextField.setStyle("-fx-padding: 5; -fx-font-size: 14; -fx-background-radius: 5;");
//
//        filterTextField.textProperty().addListener((observable, oldValue, newValue) -> {
//            if (allJobs != null) {
//                ObservableList<Job> filteredJobs = FXCollections.observableArrayList();
//                for (Job job : allJobs) {
//                    if (job.getTitle().toLowerCase().contains(newValue.toLowerCase())) {
//                        filteredJobs.add(job);
//                    }
//                }
//                jobTableView.setItems(filteredJobs);
//            }
//        });
//
//        // Layouts
//        VBox leftBox = new VBox(15);
//        leftBox.getChildren().addAll(scrapeButton, viewDataButton, classifyButton, deleteButton, filterLabel, filterTextField);
//        leftBox.setStyle("-fx-padding: 20; -fx-background-color: rgba(255, 255, 255, 0.1); -fx-border-radius: 10px; -fx-background-radius: 10px;");
//
//        jobTableView = DataView.getDataTableView();
//        VBox rightBox = new VBox(10);
//        rightBox.getChildren().addAll(jobTableView);
//        rightBox.setStyle("-fx-padding: 20; -fx-background-color: rgba(255, 255, 255, 0.1); -fx-border-radius: 10px; -fx-background-radius: 10px;");
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
//        // Create a new HBox to hold the titleLabel and align it to the left
//        HBox titleBox = new HBox(titleLabel);
//        titleBox.setAlignment(javafx.geometry.Pos.TOP_LEFT);
//        titleBox.setStyle("-fx-padding: 20;");
//
//        // Main layout with a gradient background
//        VBox mainVBox = new VBox(20, titleBox, gridPane);
//        mainVBox.setStyle("-fx-padding: 20; -fx-background-color: linear-gradient(to bottom, #1e3c72, #2a5298);");
//        mainVBox.setAlignment(javafx.geometry.Pos.TOP_CENTER);
//
//        Scene scene = new Scene(mainVBox, 1200, 600);
//        primaryStage.setScene(scene);
//        primaryStage.setTitle("Web Scraper Tool");
//        primaryStage.show();
//    }
//
//    // Helper method to create styled buttons
//    private Button createStyledButton(String text) {
//        Button button = new Button(text);
//        button.setFont(Font.font("Arial", 14));
//        button.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 10 20; "
//                + "-fx-border-radius: 5px; -fx-background-radius: 5px; -fx-cursor: hand; "
//                + "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.3), 5, 0, 0, 0);");
//        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #45a049; -fx-text-fill: white; -fx-padding: 10 20; "
//                + "-fx-border-radius: 5px; -fx-background-radius: 5px; -fx-cursor: hand; "
//                + "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.3), 5, 0, 0, 0);"));
//        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 10 20; "
//                + "-fx-border-radius: 5px; -fx-background-radius: 5px; -fx-cursor: hand; "
//                + "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.3), 5, 0, 0, 0);"));
//        return button;
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}

//2
//
//package gui;
//
//import javafx.application.Application;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.scene.Scene;
//import javafx.scene.control.*;
//import javafx.scene.layout.*;
//import javafx.stage.Stage;
//
//public class MainApp extends Application {
//    private TableView<Job> jobTableView;
//    private ObservableList<Job> allJobs;
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
//        // Add hover effect to titleLabel
//        titleLabel.setOnMouseEntered(e -> titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-padding: 10; -fx-background-color: indigo; "
//                + "-fx-text-fill: rgb(255,255,255); -fx-border-radius: 8px; -fx-background-radius: 8px; "
//                + "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.8), 6, 0, 0, 0); -fx-cursor: hand;"));
//
//        titleLabel.setOnMouseExited(e -> titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-padding: 10; -fx-background-color: darksalmon; "
//                + "-fx-text-fill: rgb(255,255,255); -fx-border-radius: 8px; -fx-background-radius: 8px; "
//                + "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.8), 6, 0, 0, 0); -fx-cursor: hand;"));
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
//            allJobs = DataView.getJobDataFromDatabase();
//            jobTableView.setItems(allJobs);
//        });
//
//        classifyButton.setOnAction(event -> ClassificationView.showClassificationView());
//
//        deleteButton.setOnAction(event -> {
//            jobTableView.getItems().clear(); // Clear the table
//            DataView.deleteAllJobsFromDatabase(); // Delete all jobs from the database
//        });
//
//        // Filter components
//        Label filterLabel = new Label("Filtrer Par Titre :");
//        TextField filterTextField = new TextField();
//        filterTextField.setPromptText("Titre de l'annonce");
//
//        filterTextField.textProperty().addListener((observable, oldValue, newValue) -> {
//            if (allJobs != null) {
//                ObservableList<Job> filteredJobs = FXCollections.observableArrayList();
//                for (Job job : allJobs) {
//                    if (job.getTitle().toLowerCase().contains(newValue.toLowerCase())) {
//                        filteredJobs.add(job);
//                    }
//                }
//                jobTableView.setItems(filteredJobs);
//            }
//        });
//
//        // Layouts
//        VBox leftBox = new VBox(10);
//        leftBox.getChildren().addAll(scrapeButton, viewDataButton, classifyButton, deleteButton, filterLabel, filterTextField);
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
//        // Create a new HBox to hold the titleLabel and align it to the left
//        HBox titleBox = new HBox(titleLabel);
//        titleBox.setAlignment(javafx.geometry.Pos.TOP_LEFT);
//        titleBox.setStyle("-fx-padding: 10;");
//
//        VBox mainVBox = new VBox(10, titleBox, gridPane);
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

//1)
//package gui;
//
//import javafx.application.Application;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.scene.Scene;
//import javafx.scene.control.*;
//import javafx.scene.layout.*;
//import javafx.stage.Stage;
//
//public class MainApp extends Application {
//    private TableView<Job> jobTableView;
//    private ObservableList<Job> allJobs;
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
//            allJobs = DataView.getJobDataFromDatabase();
//            jobTableView.setItems(allJobs);
//        });
//
//        classifyButton.setOnAction(event -> ClassificationView.showClassificationView());
//
//        deleteButton.setOnAction(event -> {
//            jobTableView.getItems().clear(); // Clear the table
//            DataView.deleteAllJobsFromDatabase(); // Delete all jobs from the database
//        });
////****************************
//        // Filter components
//        Label filterLabel = new Label("Filtrer Par Titre :");
//        TextField filterTextField = new TextField();
//        filterTextField.setPromptText("Titre de l'annonce");
//
//        filterTextField.textProperty().addListener((observable, oldValue, newValue) -> {
//            if (allJobs != null) {
//                ObservableList<Job> filteredJobs = FXCollections.observableArrayList();
//                for (Job job : allJobs) {
//                    if (job.getTitle().toLowerCase().contains(newValue.toLowerCase())) {
//                        filteredJobs.add(job);
//                    }
//                }
//                jobTableView.setItems(filteredJobs);
//            }
//        });
//
//        // Layouts
//        VBox leftBox = new VBox(10);
//        leftBox.getChildren().addAll(scrapeButton, viewDataButton, classifyButton, deleteButton, filterLabel, filterTextField);
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

