package weka;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToNominal;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.Classifier;
import gui.ClassificationResult;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class WekaClassifier {

    // Database connection constants
    private static final String DB_URL = "jdbc:mysql://localhost:3306/jobs_db";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "root";

    public static List<ClassificationResult> classifyJobs() {
        List<ClassificationResult> results = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            // Fetch data from the database
            String query = "SELECT job_title, contract_type, experience_required FROM job_offers";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            // Create Weka dataset
            ArrayList<Attribute> attributes = new ArrayList<>();
            attributes.add(new Attribute("job_title", (ArrayList<String>) null)); // String attribute
            attributes.add(new Attribute("contract_type", (ArrayList<String>) null)); // String attribute
            attributes.add(new Attribute("experience_required", (ArrayList<String>) null)); // String attribute

            Instances dataset = new Instances("JobOffers", attributes, 0);
            dataset.setClassIndex(1); // Set the class index to the "contract_type" column

            // Populate the dataset with data from the database
            while (rs.next()) {
                String jobTitle = rs.getString("job_title");
                String contractType = rs.getString("contract_type");
                String experienceRequired = rs.getString("experience_required");

                double[] instanceValues = new double[dataset.numAttributes()];
                instanceValues[0] = dataset.attribute(0).addStringValue(jobTitle);
                instanceValues[1] = dataset.attribute(1).addStringValue(contractType);
                instanceValues[2] = dataset.attribute(2).addStringValue(experienceRequired);

                dataset.add(new DenseInstance(1.0, instanceValues));
            }

            // Debug: Print the dataset before filtering
            System.out.println("Dataset before filtering:");
            System.out.println(dataset);

            // Convert string attributes to nominal
            StringToNominal stringToNominal = new StringToNominal();
            stringToNominal.setInputFormat(dataset);

            // Configure the filter to convert all attributes
            String[] options = new String[] { "-R", "first-last" }; // Convert all attributes
            stringToNominal.setOptions(options);

            // Apply the filter
            dataset = Filter.useFilter(dataset, stringToNominal);

            // Debug: Print the dataset after filtering
            System.out.println("Dataset after filtering:");
            System.out.println(dataset);

            // Train the classifier
            Classifier classifier = new NaiveBayes();
            classifier.buildClassifier(dataset);

            // Classify each instance
            for (int i = 0; i < dataset.numInstances(); i++) {
                double actualClassValue = dataset.instance(i).classValue();
                double predictedClassValue = classifier.classifyInstance(dataset.instance(i));

                String actualClass = dataset.classAttribute().value((int) actualClassValue);
                String predictedClass = dataset.classAttribute().value((int) predictedClassValue);

                String title = dataset.instance(i).stringValue(0); // Title column
                String experienceRequired = dataset.instance(i).stringValue(2); // Experience column

                results.add(new ClassificationResult(title, actualClass, predictedClass, experienceRequired));
            }

        } catch (Exception e) {
            System.err.println("Error during classification: " + e.getMessage());
            e.printStackTrace();
        }

        return results;
    }
}

//
//
//package weka;
//
//import weka.core.Attribute;
//import weka.core.DenseInstance;
//import weka.core.Instances;
//import weka.filters.Filter;
//import weka.filters.unsupervised.attribute.StringToNominal;
//import weka.classifiers.bayes.NaiveBayes;
//import weka.classifiers.Classifier;
//import gui.ClassificationResult;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.ResultSet;
//import java.sql.Statement;
//import java.util.ArrayList;
//import java.util.List;
//
//public class WekaClassifier {
//
//    // Database connection constants
//    private static final String DB_URL = "jdbc:mysql://localhost:3306/jobs_db";
//    private static final String DB_USERNAME = "root";
//    private static final String DB_PASSWORD = "";
//
//    public static List<ClassificationResult> classifyJobs() {
//        List<ClassificationResult> results = new ArrayList<>();
//
//        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
//            // Fetch data from the database
//            String query = "SELECT job_title, contract_type, experience_required FROM job_offers";
//            Statement stmt = connection.createStatement();
//            ResultSet rs = stmt.executeQuery(query);
//
//            // Create Weka dataset
//            ArrayList<Attribute> attributes = new ArrayList<>();
//            attributes.add(new Attribute("job_title", (ArrayList<String>) null)); // String attribute
//            attributes.add(new Attribute("contract_type", (ArrayList<String>) null)); // String attribute
//            attributes.add(new Attribute("experience_required", (ArrayList<String>) null)); // String attribute
//
//            Instances dataset = new Instances("JobOffers", attributes, 0);
//            dataset.setClassIndex(1); // Set the class index to the "contract_type" column
//
//            // Populate the dataset with data from the database
//            while (rs.next()) {
//                String jobTitle = rs.getString("job_title");
//                String contractType = rs.getString("contract_type");
//                String experienceRequired = rs.getString("experience_required");
//
//                double[] instanceValues = new double[dataset.numAttributes()];
//                instanceValues[0] = dataset.attribute(0).addStringValue(jobTitle);
//                instanceValues[1] = dataset.attribute(1).addStringValue(contractType);
//                instanceValues[2] = dataset.attribute(2).addStringValue(experienceRequired);
//
//                dataset.add(new DenseInstance(1.0, instanceValues));
//            }
//
//            // Convert string attributes to nominal
//            StringToNominal stringToNominal = new StringToNominal();
//            stringToNominal.setInputFormat(dataset);
//            stringToNominal.setAttributeIndices("first-last"); // Convert all string attributes
//            dataset = Filter.useFilter(dataset, stringToNominal);
//
//            // Train the classifier
//            Classifier classifier = new NaiveBayes();
//            classifier.buildClassifier(dataset);
//
//            // Classify each instance
//            for (int i = 0; i < dataset.numInstances(); i++) {
//                double actualClassValue = dataset.instance(i).classValue();
//                double predictedClassValue = classifier.classifyInstance(dataset.instance(i));
//
//                String actualClass = dataset.classAttribute().value((int) actualClassValue);
//                String predictedClass = dataset.classAttribute().value((int) predictedClassValue);
//
//                String title = dataset.instance(i).stringValue(0); // Title column
//                String experienceRequired = dataset.instance(i).stringValue(2); // Experience column
//
//                results.add(new ClassificationResult(title, actualClass, predictedClass, experienceRequired));
//            }
//
//        } catch (Exception e) {
//            System.err.println("Error during classification: " + e.getMessage());
//            e.printStackTrace();
//        }
//
//        return results;
//    }
//}


//********************************************
//
//package weka;
//
//import weka.core.Attribute;
//import weka.core.DenseInstance;
//import weka.core.Instances;
//import weka.core.converters.ConverterUtils.DataSource;
//import weka.filters.Filter;
//import weka.filters.unsupervised.attribute.StringToNominal;
//import weka.classifiers.bayes.NaiveBayes;
//import weka.classifiers.Classifier;
//import gui.ClassificationResult;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.ResultSet;
//import java.sql.Statement;
//import java.util.ArrayList;
//import java.util.List;
//
//public class WekaClassifier {
//
//    // Database connection constants
//    private static final String DB_URL = "jdbc:mysql://localhost:3306/jobs_db";
//    private static final String DB_USERNAME = "root";
//    private static final String DB_PASSWORD = "";
//
//    public static List<ClassificationResult> classifyJobs() {
//        List<ClassificationResult> results = new ArrayList<>();
//
//        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
//            // Fetch data from the database
//            String query = "SELECT job_title, contract_type, experience_required FROM job_offers";
//            Statement stmt = connection.createStatement();
//            ResultSet rs = stmt.executeQuery(query);
//
//            // Create Weka dataset
//            ArrayList<Attribute> attributes = new ArrayList<>();
//            attributes.add(new Attribute("job_title", (ArrayList<String>) null)); // String attribute
//            attributes.add(new Attribute("contract_type", (ArrayList<String>) null)); // String attribute
//            attributes.add(new Attribute("experience_required", (ArrayList<String>) null)); // String attribute
//
//            Instances dataset = new Instances("JobOffers", attributes, 0);
//            dataset.setClassIndex(1); // Set the class index to the "contract_type" column
//
//            // Populate the dataset with data from the database
//            while (rs.next()) {
//                String jobTitle = rs.getString("job_title");
//                String contractType = rs.getString("contract_type");
//                String experienceRequired = rs.getString("experience_required");
//
//                double[] instanceValues = new double[dataset.numAttributes()];
//                instanceValues[0] = dataset.attribute(0).addStringValue(jobTitle);
//                instanceValues[1] = dataset.attribute(1).addStringValue(contractType);
//                instanceValues[2] = dataset.attribute(2).addStringValue(experienceRequired);
//
//                dataset.add(new DenseInstance(1.0, instanceValues));
//            }
//
//            // Convert string attributes to nominal (if needed)
//            StringToNominal filter = new StringToNominal();
//            filter.setInputFormat(dataset);
//            dataset = Filter.useFilter(dataset, filter);
//
//            // Train the classifier
//            Classifier classifier = new NaiveBayes();
//            classifier.buildClassifier(dataset);
//
//            // Classify each instance
//            for (int i = 0; i < dataset.numInstances(); i++) {
//                double actualClassValue = dataset.instance(i).classValue();
//                double predictedClassValue = classifier.classifyInstance(dataset.instance(i));
//
//                String actualClass = dataset.classAttribute().value((int) actualClassValue);
//                String predictedClass = dataset.classAttribute().value((int) predictedClassValue);
//
//                String title = dataset.instance(i).stringValue(0); // Title column
//                String experienceRequired = dataset.instance(i).stringValue(2); // Experience column
//
//                results.add(new ClassificationResult(title, actualClass, predictedClass, experienceRequired));
//            }
//
//        } catch (Exception e) {
//            System.err.println("Error during classification: " + e.getMessage());
//            e.printStackTrace();
//        }
//
//        return results;
//    }
//}



//
//
//package weka;
//
//import weka.filters.Filter;
//import weka.filters.unsupervised.attribute.StringToNominal;
//import weka.classifiers.bayes.NaiveBayes;
//import weka.classifiers.Classifier;
//import weka.core.converters.CSVLoader;
//import weka.core.Instances;
//
//import gui.ClassificationResult;
//import java.io.File;
//import java.util.ArrayList;
//import java.util.List;
//
//public class WekaClassifier {
//    public static List<ClassificationResult> classifyJobs(String csvFilePath) {
//        List<ClassificationResult> results = new ArrayList<>();
//
//        try {
//            CSVLoader loader = new CSVLoader();
//            loader.setSource(new File(csvFilePath));
//            Instances dataset = loader.getDataSet();
//
//            StringToNominal filter = new StringToNominal();
//            filter.setInputFormat(dataset);
//            dataset = Filter.useFilter(dataset, filter);
//
//            int classIndex = 1; // "Type de Contrat"
//            dataset.setClassIndex(classIndex);
//
//            Classifier classifier = new NaiveBayes();
//            classifier.buildClassifier(dataset);
//
//            for (int i = 0; i < dataset.numInstances(); i++) {
//                double actualClassValue = dataset.instance(i).classValue();
//                double predictedClassValue = classifier.classifyInstance(dataset.instance(i));
//
//                String actualClass = dataset.classAttribute().value((int) actualClassValue);
//                String predictedClass = dataset.classAttribute().value((int) predictedClassValue);
//
//                String title = dataset.instance(i).stringValue(0); // Titre
//                String experienceRequired = dataset.instance(i).stringValue(2); // Expérience Requise
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
//
//
//
//
