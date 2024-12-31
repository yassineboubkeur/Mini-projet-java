

package weka;

import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToNominal;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.Classifier;
import weka.core.converters.CSVLoader;
import weka.core.Instances;

import gui.ClassificationResult;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class WekaClassifier {
    public static List<ClassificationResult> classifyJobs(String csvFilePath) {
        List<ClassificationResult> results = new ArrayList<>();

        try {
            CSVLoader loader = new CSVLoader();
            loader.setSource(new File(csvFilePath));
            Instances dataset = loader.getDataSet();

            StringToNominal filter = new StringToNominal();
            filter.setInputFormat(dataset);
            dataset = Filter.useFilter(dataset, filter);

            int classIndex = 1; // "Type de Contrat"
            dataset.setClassIndex(classIndex);

            Classifier classifier = new NaiveBayes();
            classifier.buildClassifier(dataset);

            for (int i = 0; i < dataset.numInstances(); i++) {
                double actualClassValue = dataset.instance(i).classValue();
                double predictedClassValue = classifier.classifyInstance(dataset.instance(i));

                String actualClass = dataset.classAttribute().value((int) actualClassValue);
                String predictedClass = dataset.classAttribute().value((int) predictedClassValue);

                String title = dataset.instance(i).stringValue(0); // Titre
                String experienceRequired = dataset.instance(i).stringValue(2); // Expérience Requise

                results.add(new ClassificationResult(title, actualClass, predictedClass, experienceRequired));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return results;
    }
}




//
//package weka;
//
//import weka.filters.Filter;
//import weka.filters.unsupervised.attribute.StringToNominal;
//import weka.classifiers.bayes.NaiveBayes;
//import weka.classifiers.Classifier;
//import weka.core.converters.CSVLoader;
//import weka.core.Instances;
//import java.io.File;
//import java.util.ArrayList;
//import java.util.List;
//
//import gui.ClassificationResult;
//
//public class WekaClassifier {
//    public static List<ClassificationResult> classifyJobs(String csvFilePath) {
//        List<ClassificationResult> results = new ArrayList<>();
//
//        try {
//            // Charger le fichier CSV
//            CSVLoader loader = new CSVLoader();
//            loader.setSource(new File(csvFilePath));
//            Instances dataset = loader.getDataSet();
//
//            // Convertir les attributs String en nominal
//            StringToNominal filter = new StringToNominal();
//            filter.setInputFormat(dataset);
//            dataset = Filter.useFilter(dataset, filter);
//
//            // Définir la colonne "Type de Contrat" comme classe cible
//            int classIndex = 1; // Index de la colonne "Type de Contrat" (adapté selon votre CSV)
//            dataset.setClassIndex(classIndex);
//
//            // Appliquer NaiveBayes
//            Classifier classifier = new NaiveBayes();
//            classifier.buildClassifier(dataset);
//
//            // Classifier chaque instance
//            for (int i = 0; i < dataset.numInstances(); i++) {
//                double actualClassValue = dataset.instance(i).classValue();
//                double predictedClassValue = classifier.classifyInstance(dataset.instance(i));
//
//                String actualClass = dataset.classAttribute().value((int) actualClassValue);
//                String predictedClass = dataset.classAttribute().value((int) predictedClassValue);
//
//                String title = dataset.instance(i).toString(0); // Supposons que le titre soit dans la colonne 0
//                String experienceRequired = dataset.instance(i).toString(2); // Supposons que l'expérience soit dans la colonne 2
//
//                // Vérifier et ajuster les valeurs d'expérience
//                if ("5-10 ans".equalsIgnoreCase(experienceRequired)) {
//                    experienceRequired = "De 5 à 10 ans";
//                } else if ("3-5 ans".equalsIgnoreCase(experienceRequired)) {
//                    experienceRequired = "De 3 à 5 ans";
//                }
//
//                results.add(new ClassificationResult(title, actualClass, predictedClass, experienceRequired));
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return results;
//    }
//}



//package weka;
//
//import weka.filters.Filter;
//import weka.filters.unsupervised.attribute.StringToNominal;
//import weka.classifiers.bayes.NaiveBayes;
//import weka.classifiers.Classifier;
//import weka.core.converters.CSVLoader;
//import weka.core.Instances;
//import java.io.File;
//import java.util.ArrayList;
//import java.util.List;
//
//import gui.ClassificationResult;
//
//public class WekaClassifier {
//    public static List<ClassificationResult> classifyJobs(String csvFilePath) {
//        List<ClassificationResult> results = new ArrayList<>();
//
//        try {
//            // Charger le fichier CSV
//            CSVLoader loader = new CSVLoader();
//            loader.setSource(new File(csvFilePath));
//            Instances dataset = loader.getDataSet();
//
//            // Convertir les attributs String en nominal
//            StringToNominal filter = new StringToNominal();
//            filter.setInputFormat(dataset);
//            dataset = Filter.useFilter(dataset, filter);
//
//            // Définir la colonne "Type de Contrat" comme classe cible
//            int classIndex = 1; // Index de la colonne "Type de Contrat" (adapté selon votre CSV)
//            dataset.setClassIndex(classIndex);
//
//            // Appliquer NaiveBayes
//            Classifier classifier = new NaiveBayes();
//            classifier.buildClassifier(dataset);
//
//            // Classifier chaque instance
//            for (int i = 0; i < dataset.numInstances(); i++) {
//                double actualClassValue = dataset.instance(i).classValue();
//                double predictedClassValue = classifier.classifyInstance(dataset.instance(i));
//
//                String actualClass = dataset.classAttribute().value((int) actualClassValue);
//                String predictedClass = dataset.classAttribute().value((int) predictedClassValue);
//
//                String title = dataset.instance(i).toString(0); // Supposons que le titre soit dans la colonne 0
//                String experienceRequired = dataset.instance(i).toString(2); // Supposons que l'expérience soit dans la colonne 2
//
//                results.add(new ClassificationResult(title, actualClass, predictedClass, experienceRequired));
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return results;
//    }
//}


//************************************************************************************
//import weka.classifiers.bayes.NaiveBayes;
//import weka.classifiers.Classifier;
//import weka.classifiers.Evaluation;
//import weka.core.converters.CSVLoader;
//import weka.core.Instances;
//
//import java.io.File;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import gui.ClassificationResult;
//
//public class WekaClassifier {
//    public static List<ClassificationResult> classifyJobs(String csvFilePath) {
//        List<ClassificationResult> results = new ArrayList<>();
//        try {
//            // Charger le fichier CSV
//            CSVLoader loader = new CSVLoader();
//            loader.setSource(new File(csvFilePath));
//            Instances dataset = loader.getDataSet();
//
//            // Définir la colonne "Type de Contrat" comme classe cible
//            int classIndex = 1; // Index de la colonne "Type de Contrat"
//            dataset.setClassIndex(classIndex);
//
//            // Appliquer NaiveBayes
//            Classifier classifier = new NaiveBayes();
//            classifier.buildClassifier(dataset);
//
//            // Classifier chaque instance
//            for (int i = 0; i < dataset.numInstances(); i++) {
//                double actualClassValue = dataset.instance(i).classValue();
//                double predictedClassValue = classifier.classifyInstance(dataset.instance(i));
//
//                String actualClass = dataset.classAttribute().value((int) actualClassValue);
//                String predictedClass = dataset.classAttribute().value((int) predictedClassValue);
//
//                String title = dataset.instance(i).toString(0); // Supposons que le titre soit dans la colonne 0
//
//                results.add(new ClassificationResult(title, actualClass, predictedClass));
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return results;
//    }
//}

