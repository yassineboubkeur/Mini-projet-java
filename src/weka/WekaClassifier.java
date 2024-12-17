package weka;

import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.converters.CSVLoader;
import weka.core.Instances;

import java.io.File;

public class WekaClassifier {
    public static void main(String[] args) {
        try {
            // Étape 1 : Charger le fichier CSV
            CSVLoader loader = new CSVLoader();
            loader.setSource(new File("jobs.csv"));
            Instances dataset = loader.getDataSet();

            // Étape 2 : Définir la colonne "Type de Contrat" comme classe cible
            int classIndex = 1; // "Type de Contrat" est la 2ème colonne (index 1)
            dataset.setClassIndex(classIndex);

            // Étape 3 : Appliquer l'algorithme NaiveBayes
            Classifier classifier = new NaiveBayes();
            classifier.buildClassifier(dataset);

            // Étape 4 : Évaluer le modèle avec une validation croisée (10 folds)
            Evaluation eval = new Evaluation(dataset);
            eval.crossValidateModel(classifier, dataset, 10, new java.util.Random(1));

            // Résultats de l'évaluation
            System.out.println("=== Résumé de l'évaluation ===");
            System.out.println(eval.toSummaryString());

            System.out.println("\n=== Matrice de confusion ===");
            System.out.println(eval.toMatrixString());

            System.out.println("\n=== Précision par classe ===");
            System.out.println(eval.toClassDetailsString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
