
package gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import weka.WekaClassifier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassificationView {
    public static void showClassificationView() {
        Stage stage = new Stage();
        stage.setTitle("Job offers Classification");

        // Main layout
        VBox mainLayout = new VBox(20);
        mainLayout.setStyle("-fx-padding: 20; -fx-background-color: #f4f4f4;");

        // Title
        Text title = new Text("Job offers Classification");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setStyle("-fx-fill: #2a5298;");

        // Classify button
        Button classifyButton = new Button("Show classification");
        classifyButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14; "
                + "-fx-padding: 10 20; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-cursor: hand;");
        classifyButton.setOnMouseEntered(e -> classifyButton.setStyle("-fx-background-color: #45a049; -fx-text-fill: white; "
                + "-fx-padding: 10 20; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-cursor: hand;"));
        classifyButton.setOnMouseExited(e -> classifyButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; "
                + "-fx-padding: 10 20; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-cursor: hand;"));

        // Table to display classification results
        TableView<ClassificationResult> table = new TableView<>();
        table.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-radius: 5px;");

        TableColumn<ClassificationResult, String> titleCol = new TableColumn<>("Titre");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<ClassificationResult, String> actualCol = new TableColumn<>("Type de Contrat (Réel)");
        actualCol.setCellValueFactory(new PropertyValueFactory<>("actualContract"));

        TableColumn<ClassificationResult, String> predictedCol = new TableColumn<>("Type de Contrat (Prédit)");
        predictedCol.setCellValueFactory(new PropertyValueFactory<>("predictedContract"));

        TableColumn<ClassificationResult, String> experienceCol = new TableColumn<>("Expérience Requise");
        experienceCol.setCellValueFactory(new PropertyValueFactory<>("experienceRequired"));

        table.getColumns().addAll(titleCol, actualCol, predictedCol, experienceCol);

        // Charts
        PieChart pieChart = new PieChart();
        pieChart.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-radius: 5px;");

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-radius: 5px;");
        xAxis.setLabel("Required experience");
        yAxis.setLabel("Number of job postings");
        barChart.setTitle("Jobs Distribution by level of experience");

        // Data visualization title
        Text dataVisualizationTitle = new Text("Data Visualisation");
        dataVisualizationTitle.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-fill: #2a5298;");

        // Action for classify button
        classifyButton.setOnAction(event -> {
            try {
                List<ClassificationResult> results = WekaClassifier.classifyJobs();

                ObservableList<ClassificationResult> data = FXCollections.observableArrayList(results);
                table.setItems(data);

                updatePieChart(pieChart, results);
                updateBarChart(barChart, results);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succès");
                alert.setHeaderText(null);
                alert.setContentText(" Classification done Successfully !");
                alert.showAndWait();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Error  classification : " + e.getMessage());
                alert.showAndWait();
            }
        });

        // Layout for charts
        HBox chartsLayout = new HBox(20);
        chartsLayout.getChildren().addAll(pieChart, barChart);
        chartsLayout.setStyle("-fx-alignment: center;");

        // Add components to main layout
        mainLayout.getChildren().addAll(title, classifyButton, table, dataVisualizationTitle, chartsLayout);
        mainLayout.setAlignment(javafx.geometry.Pos.TOP_CENTER);

        // Scene
        Scene scene = new Scene(mainLayout, 1000, 800);
        stage.setScene(scene);
        stage.show();
    }

    private static void updatePieChart(PieChart pieChart, List<ClassificationResult> results) {
        long countCDI = results.stream().filter(r -> "CDI".equalsIgnoreCase(r.getPredictedContract())).count();
        long countCDD = results.stream().filter(r -> "CDD".equalsIgnoreCase(r.getPredictedContract())).count();

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("CDI", countCDI),
                new PieChart.Data("CDD", countCDD)
        );

        pieChart.setData(pieChartData);
        pieChart.setTitle("Jobs Distribution by contract type (CDD/CDI)");
    }

    private static void updateBarChart(BarChart<String, Number> barChart, List<ClassificationResult> results) {
        Map<String, Long> experienceCount = new HashMap<>();
        results.forEach(r -> {
            String experience = r.getExperienceRequired();
            experienceCount.put(experience, experienceCount.getOrDefault(experience, 0L) + 1);
        });

        barChart.getData().clear();
        BarChart.Series<String, Number> series = new BarChart.Series<>();
        series.setName("Expérience");

        experienceCount.forEach((experience, count) ->
                series.getData().add(new BarChart.Data<>(experience, count))
        );

        barChart.getData().add(series);
    }
}

//
//
//package gui;
//
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.scene.Scene;
//import javafx.scene.chart.BarChart;
//import javafx.scene.chart.CategoryAxis;
//import javafx.scene.chart.NumberAxis;
//import javafx.scene.chart.PieChart;
//import javafx.scene.control.*;
//import javafx.scene.control.cell.PropertyValueFactory;
//import javafx.scene.layout.*;
//import javafx.scene.text.Font;
//import javafx.scene.text.FontWeight;
//import javafx.scene.text.Text;
//import javafx.stage.Stage;
//import weka.WekaClassifier;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class ClassificationView {
//    public static void showClassificationView() {
//        Stage stage = new Stage();
//        stage.setTitle("Classification des Annonces");
//
//        // Main layout
//        VBox mainLayout = new VBox(20);
//        mainLayout.setStyle("-fx-padding: 20; -fx-background-color: #f4f4f4;");
//
//        // Title
//        Text title = new Text("Classification des Annonces");
//        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
//        title.setStyle("-fx-fill: #2a5298;");
//
//        // Classify button
//        Button classifyButton = new Button("Lancer la classification");
//        classifyButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14; "
//                + "-fx-padding: 10 20; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-cursor: hand;");
//        classifyButton.setOnMouseEntered(e -> classifyButton.setStyle("-fx-background-color: #45a049; -fx-text-fill: white; "
//                + "-fx-padding: 10 20; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-cursor: hand;"));
//        classifyButton.setOnMouseExited(e -> classifyButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; "
//                + "-fx-padding: 10 20; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-cursor: hand;"));
//
//        // Table to display classification results
//        TableView<ClassificationResult> table = new TableView<>();
//        table.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-radius: 5px;");
//
//        TableColumn<ClassificationResult, String> titleCol = new TableColumn<>("Titre");
//        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
//
//        TableColumn<ClassificationResult, String> actualCol = new TableColumn<>("Type de Contrat (Réel)");
//        actualCol.setCellValueFactory(new PropertyValueFactory<>("actualContract"));
//
//        TableColumn<ClassificationResult, String> predictedCol = new TableColumn<>("Type de Contrat (Prédit)");
//        predictedCol.setCellValueFactory(new PropertyValueFactory<>("predictedContract"));
//
//        TableColumn<ClassificationResult, String> experienceCol = new TableColumn<>("Expérience Requise");
//        experienceCol.setCellValueFactory(new PropertyValueFactory<>("experienceRequired"));
//
//        table.getColumns().addAll(titleCol, actualCol, predictedCol, experienceCol);
//
//        // Charts
//        PieChart pieChart = new PieChart();
//        pieChart.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-radius: 5px;");
//
//        CategoryAxis xAxis = new CategoryAxis();
//        NumberAxis yAxis = new NumberAxis();
//        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
//        barChart.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-radius: 5px;");
//        xAxis.setLabel("Expérience demandée");
//        yAxis.setLabel("Nombre d'annonces");
//        barChart.setTitle("Répartition des postes par niveau d'expérience");
//
//        // Data visualization title
//        Text dataVisualizationTitle = new Text("Visualisation des Données");
//        dataVisualizationTitle.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-fill: #2a5298;");
//
//        // Action for classify button
//        classifyButton.setOnAction(event -> {
//            try {
//                List<ClassificationResult> results = WekaClassifier.classifyJobs("jobs.csv");
//
//                ObservableList<ClassificationResult> data = FXCollections.observableArrayList(results);
//                table.setItems(data);
//
//                updatePieChart(pieChart, results);
//                updateBarChart(barChart, results);
//
//                Alert alert = new Alert(Alert.AlertType.INFORMATION);
//                alert.setTitle("Succès");
//                alert.setHeaderText(null);
//                alert.setContentText("Classification terminée avec succès !");
//                alert.showAndWait();
//            } catch (Exception e) {
//                Alert alert = new Alert(Alert.AlertType.ERROR);
//                alert.setTitle("Erreur");
//                alert.setHeaderText(null);
//                alert.setContentText("Erreur lors de la classification : " + e.getMessage());
//                alert.showAndWait();
//            }
//        });
//
//        // Layout for charts
//        HBox chartsLayout = new HBox(20);
//        chartsLayout.getChildren().addAll(pieChart, barChart);
//        chartsLayout.setStyle("-fx-alignment: center;");
//
//        // Add components to main layout
//        mainLayout.getChildren().addAll(title, classifyButton, table, dataVisualizationTitle, chartsLayout);
//        mainLayout.setAlignment(javafx.geometry.Pos.TOP_CENTER);
//
//        // Scene
//        Scene scene = new Scene(mainLayout, 1000, 800);
//        stage.setScene(scene);
//        stage.show();
//    }
//
//    private static void updatePieChart(PieChart pieChart, List<ClassificationResult> results) {
//        long countCDI = results.stream().filter(r -> "CDI".equalsIgnoreCase(r.getPredictedContract())).count();
//        long countCDD = results.stream().filter(r -> "CDD".equalsIgnoreCase(r.getPredictedContract())).count();
//
//        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
//                new PieChart.Data("CDI", countCDI),
//                new PieChart.Data("CDD", countCDD)
//        );
//
//        pieChart.setData(pieChartData);
//        pieChart.setTitle("Répartition des postes par types de contrat (CDI/CDD)");
//    }
//
//    private static void updateBarChart(BarChart<String, Number> barChart, List<ClassificationResult> results) {
//        Map<String, Long> experienceCount = new HashMap<>();
//        results.forEach(r -> {
//            String experience = r.getExperienceRequired();
//            experienceCount.put(experience, experienceCount.getOrDefault(experience, 0L) + 1);
//        });
//
//        barChart.getData().clear();
//        BarChart.Series<String, Number> series = new BarChart.Series<>();
//        series.setName("Expérience");
//
//        experienceCount.forEach((experience, count) ->
//                series.getData().add(new BarChart.Data<>(experience, count))
//        );
//
//        barChart.getData().add(series);
//    }
//}
//
//
//
