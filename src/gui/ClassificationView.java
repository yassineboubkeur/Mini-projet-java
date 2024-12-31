

package gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import weka.WekaClassifier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassificationView {
    public static void showClassificationView() {
        Stage stage = new Stage();
        stage.setTitle("Classification des Annonces");

        Button classifyButton = new Button("Lancer la classification");
        TableView<ClassificationResult> table = new TableView<>();
        PieChart pieChart = new PieChart();

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        xAxis.setLabel("Expérience demandée");
        yAxis.setLabel("Nombre d'annonces");
        barChart.setTitle("Répartition des postes par niveau d'expérience");

        Text dataVisualizationTitle = new Text("Data Visualization (Visualisation des données)");
        dataVisualizationTitle.setStyle("-fx-font-size: 25; -fx-font-weight: bold;");

        classifyButton.setOnAction(event -> {
            try {
                List<ClassificationResult> results = WekaClassifier.classifyJobs("jobs.csv");

                ObservableList<ClassificationResult> data = FXCollections.observableArrayList(results);
                table.setItems(data);

                updatePieChart(pieChart, results);
                updateBarChart(barChart, results);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Classification terminée avec succès !");
                alert.showAndWait();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Erreur lors de la classification : " + e.getMessage());
                alert.showAndWait();
            }
        });

        TableColumn<ClassificationResult, String> titleCol = new TableColumn<>("Titre");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<ClassificationResult, String> actualCol = new TableColumn<>("Type de Contrat (Réel)");
        actualCol.setCellValueFactory(new PropertyValueFactory<>("actualContract"));

        TableColumn<ClassificationResult, String> predictedCol = new TableColumn<>("Type de Contrat (Prédit)");
        predictedCol.setCellValueFactory(new PropertyValueFactory<>("predictedContract"));

        TableColumn<ClassificationResult, String> experienceCol = new TableColumn<>("Expérience Requise");
        experienceCol.setCellValueFactory(new PropertyValueFactory<>("experienceRequired"));

        table.getColumns().addAll(titleCol, actualCol, predictedCol, experienceCol);

        VBox vbox = new VBox(10, classifyButton, table, dataVisualizationTitle, pieChart, barChart);
        vbox.setStyle("-fx-padding: 20; -fx-alignment: center;");
        stage.setScene(new Scene(vbox, 800, 800));
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
        pieChart.setTitle("Répartition des types de contrat (CDI/CDD)");
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
//import javafx.scene.control.Alert;
//import javafx.scene.control.Button;
//import javafx.scene.control.TableColumn;
//import javafx.scene.control.TableView;
//import javafx.scene.control.cell.PropertyValueFactory;
//import javafx.scene.layout.VBox;
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
//        Button classifyButton = new Button("Lancer la classification");
//        TableView<ClassificationResult> table = new TableView<>();
//        PieChart pieChart = new PieChart();
//
//        CategoryAxis xAxis = new CategoryAxis();
//        NumberAxis yAxis = new NumberAxis();
//        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
//        xAxis.setLabel("Expérience demandée");
//        yAxis.setLabel("Nombre d'annonces");
//        barChart.setTitle("Répartition des postes par niveau d'expérience");
//
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
//                alert.setContentText("Classification terminée avec succès !");
//                alert.showAndWait();
//            } catch (Exception e) {
//                Alert alert = new Alert(Alert.AlertType.ERROR);
//                alert.setContentText("Erreur lors de la classification : " + e.getMessage());
//                alert.showAndWait();
//            }
//        });
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
//        VBox vbox = new VBox(10, classifyButton, table, pieChart, barChart);
//        vbox.setStyle("-fx-padding: 20; -fx-alignment: center;");
//        stage.setScene(new Scene(vbox, 800, 800));
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
//        pieChart.setTitle("Répartition des types de contrat (CDI/CDD)");
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
//            series.getData().add(new BarChart.Data<>(experience, count))
//        );
//
//        barChart.getData().add(series);
//    }
//}
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
//import javafx.scene.control.Alert;
//import javafx.scene.control.Button;
//import javafx.scene.control.TableColumn;
//import javafx.scene.control.TableView;
//import javafx.scene.control.cell.PropertyValueFactory;
//import javafx.scene.layout.VBox;
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
//        // Bouton pour exécuter la classification
//        Button classifyButton = new Button("Lancer la classification");
//        TableView<ClassificationResult> table = new TableView<>();
//        PieChart pieChart = new PieChart();
//
//        // BarChart pour visualisation des expériences demandées
//        CategoryAxis xAxis = new CategoryAxis();
//        NumberAxis yAxis = new NumberAxis();
//        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
//        xAxis.setLabel("Expérience demandée");
//        yAxis.setLabel("Nombre d'annonces");
//        barChart.setTitle("Répartition des postes par niveau d'expérience");
//
//        classifyButton.setOnAction(event -> {
//            try {
//                // Appel de la méthode de classification
//                List<ClassificationResult> results = WekaClassifier.classifyJobs("jobs.csv");
//
//                // Charger les résultats dans la table
//                ObservableList<ClassificationResult> data = FXCollections.observableArrayList(results);
//                table.setItems(data);
//
//                // Mettre à jour le graphique circulaire
//                updatePieChart(pieChart, results);
//
//                // Mettre à jour le BarChart
//                updateBarChart(barChart, results);
//
//                Alert alert = new Alert(Alert.AlertType.INFORMATION);
//                alert.setContentText("Classification terminée avec succès !");
//                alert.showAndWait();
//            } catch (Exception e) {
//                e.printStackTrace();
//                Alert alert = new Alert(Alert.AlertType.ERROR);
//                alert.setContentText("Erreur lors de la classification : " + e.getMessage());
//                alert.showAndWait();
//            }
//        });
//
//        // Colonnes pour afficher les résultats
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
//        VBox vbox = new VBox(10, classifyButton, table, pieChart, barChart);
//        vbox.setStyle("-fx-padding: 20; -fx-alignment: center;");
//        stage.setScene(new Scene(vbox, 800, 800));
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
//        pieChart.setTitle("Répartition des types de contrat (CDI/CDD)");
//    }
//
//    private static void updateBarChart(BarChart<String, Number> barChart, List<ClassificationResult> results) {
//        // Compter les postes par niveau d'expérience
//        Map<String, Long> experienceCount = new HashMap<>();
//        for (ClassificationResult result : results) {
//            String experience = result.getExperienceRequired();
//            experienceCount.put(experience, experienceCount.getOrDefault(experience, 0L) + 1);
//        }
//
//        // Ajouter les données au BarChart
//        barChart.getData().clear();
//        BarChart.Series<String, Number> series = new BarChart.Series<>();
//        series.setName("Expérience");
//
//        experienceCount.forEach((experience, count) -> 
//            series.getData().add(new BarChart.Data<>(experience, count))
//        );
//
//        barChart.getData().add(series);
//    }
//}


//package gui;
//
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.scene.Scene;
//import javafx.scene.chart.BarChart;
//import javafx.scene.chart.CategoryAxis;
//import javafx.scene.chart.NumberAxis;
//import javafx.scene.chart.PieChart;
//import javafx.scene.control.Alert;
//import javafx.scene.control.Button;
//import javafx.scene.control.TableColumn;
//import javafx.scene.control.TableView;
//import javafx.scene.control.cell.PropertyValueFactory;
//import javafx.scene.layout.VBox;
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
//        // Bouton pour exécuter la classification
//        Button classifyButton = new Button("Lancer la classification");
//        TableView<ClassificationResult> table = new TableView<>();
//        PieChart pieChart = new PieChart();
//
//        // BarChart pour visualisation des expériences demandées
//        CategoryAxis xAxis = new CategoryAxis();
//        NumberAxis yAxis = new NumberAxis();
//        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
//        xAxis.setLabel("Expérience demandée");
//        yAxis.setLabel("Nombre d'annonces");
//        barChart.setTitle("Répartition des postes par niveau d'expérience");
//
//        classifyButton.setOnAction(event -> {
//            try {
//                // Appel de la méthode de classification
//                List<ClassificationResult> results = WekaClassifier.classifyJobs("jobs.csv");
//
//                // Charger les résultats dans la table
//                ObservableList<ClassificationResult> data = FXCollections.observableArrayList(results);
//                table.setItems(data);
//
//                // Mettre à jour le graphique circulaire
//                updatePieChart(pieChart, results);
//
//                // Mettre à jour le BarChart
//                updateBarChart(barChart, results);
//
//                Alert alert = new Alert(Alert.AlertType.INFORMATION);
//                alert.setContentText("Classification terminée avec succès !");
//                alert.showAndWait();
//            } catch (Exception e) {
//                e.printStackTrace();
//                Alert alert = new Alert(Alert.AlertType.ERROR);
//                alert.setContentText("Erreur lors de la classification : " + e.getMessage());
//                alert.showAndWait();
//            }
//        });
//
//        // Colonnes pour afficher les résultats
//        TableColumn<ClassificationResult, String> titleCol = new TableColumn<>("Titre");
//        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
//
//        TableColumn<ClassificationResult, String> actualCol = new TableColumn<>("Type de Contrat (Réel)");
//        actualCol.setCellValueFactory(new PropertyValueFactory<>("actualContract"));
//
//        TableColumn<ClassificationResult, String> predictedCol = new TableColumn<>("Type de Contrat (Prédit)");
//        predictedCol.setCellValueFactory(new PropertyValueFactory<>("predictedContract"));
//
//        table.getColumns().addAll(titleCol, actualCol, predictedCol);
//
//        VBox vbox = new VBox(10, classifyButton, table, pieChart, barChart);
//        vbox.setStyle("-fx-padding: 20; -fx-alignment: center;");
//        stage.setScene(new Scene(vbox, 800, 800));
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
//        pieChart.setTitle("Répartition des types de contrat (CDI/CDD)");
//    }
//
//    private static void updateBarChart(BarChart<String, Number> barChart, List<ClassificationResult> results) {
//        // Compter les postes par niveau d'expérience
//        Map<String, Long> experienceCount = new HashMap<>();
//        for (ClassificationResult result : results) {
//            String experience = result.getExperienceRequired();
//            experienceCount.put(experience, experienceCount.getOrDefault(experience, 0L) + 1);
//        }
//
//        // Ajouter les données au BarChart
//        barChart.getData().clear();
//        BarChart.Series<String, Number> series = new BarChart.Series<>();
//        series.setName("Expérience");
//
//        experienceCount.forEach((experience, count) -> 
//            series.getData().add(new BarChart.Data<>(experience, count))
//        );
//
//        barChart.getData().add(series);
//    }
//}




//package gui;
//
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.scene.Scene;
//import javafx.scene.control.Alert;
//import javafx.scene.control.Button;
//import javafx.scene.control.TableColumn;
//import javafx.scene.control.TableView;
//import javafx.scene.control.cell.PropertyValueFactory;
//import javafx.scene.layout.VBox;
//import javafx.stage.Stage;
//import weka.WekaClassifier;
//
//import java.util.List;
//
//public class ClassificationView {
//    public static void showClassificationView() {
//        Stage stage = new Stage();
//        stage.setTitle("Classification des Annonces");
//
//        // Bouton pour exécuter la classification
//        Button classifyButton = new Button("Lancer la classification");
//        TableView<ClassificationResult> table = new TableView<>();
//
//        classifyButton.setOnAction(event -> {
//            try {
//                // Appel de la méthode de classification
//                List<ClassificationResult> results = WekaClassifier.classifyJobs("jobs.csv");
//
//                // Charger les résultats dans la table
//                ObservableList<ClassificationResult> data = FXCollections.observableArrayList(results);
//                table.setItems(data);
//
//                Alert alert = new Alert(Alert.AlertType.INFORMATION);
//                alert.setContentText("Classification terminée avec succès !");
//                alert.showAndWait();
//            } catch (Exception e) {
//                e.printStackTrace();
//                Alert alert = new Alert(Alert.AlertType.ERROR);
//                alert.setContentText("Erreur lors de la classification : " + e.getMessage());
//                alert.showAndWait();
//            }
//        });
//
//        // Colonnes pour afficher les résultats
//        TableColumn<ClassificationResult, String> titleCol = new TableColumn<>("Titre");
//        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
//
//        TableColumn<ClassificationResult, String> actualCol = new TableColumn<>("Type de Contrat (Réel)");
//        actualCol.setCellValueFactory(new PropertyValueFactory<>("actualContract"));
//
//        TableColumn<ClassificationResult, String> predictedCol = new TableColumn<>("Type de Contrat (Prédit)");
//        predictedCol.setCellValueFactory(new PropertyValueFactory<>("predictedContract"));
//
//        table.getColumns().addAll(titleCol, actualCol, predictedCol);
//
//        VBox vbox = new VBox(10, classifyButton, table);
//        vbox.setStyle("-fx-padding: 20; -fx-alignment: center;");
//        stage.setScene(new Scene(vbox, 600, 400));
//        stage.show();
//    }
//}
