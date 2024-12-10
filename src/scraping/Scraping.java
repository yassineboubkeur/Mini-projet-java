package scraping;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.nio.charset.StandardCharsets;

public class Scraping {
    public static void main(String[] args) {
        try {
            Document doc = Jsoup.connect("https://www.rekrute.com/offres-emploi-maroc.html").get();

            Elements jobTitles = doc.select("h2 > a.titreJob");
            Elements experienceDemande = doc.select("li:contains(Type de contrat proposé :) > a");
            Elements contractTypes = doc.select("ul > li:contains(Expérience requise :) > a");
            Elements dateElements = doc.select("em.date");

            FileWriter csvWriter = new FileWriter("jobs.csv", StandardCharsets.UTF_8);
            csvWriter.append("Titre,Type de Contrat,Experience demandée,Date de début,Date de fin,Nombre de postes\n");

            for (int i = 0; i < jobTitles.size(); i++) {
                String jobTitle = jobTitles.get(i).text();
                String experienceDemandee = experienceDemande.size() > i ? experienceDemande.get(i).text() : "Non spécifié";
                String contractType = contractTypes.size() > i ? contractTypes.get(i).text() : "Non spécifié";

                String startDate = "Non spécifié";
                String endDate = "Non spécifié";
                String jobPosts = "Non spécifié";

                if (dateElements.size() > i) {
                    Element dateElement = dateElements.get(i);
                    Elements spans = dateElement.select("span");

                    if (spans.size() == 3) {
                        startDate = spans.get(0).text();
                        endDate = spans.get(1).text();
                        jobPosts = spans.get(2).text();
                    }
                }

                csvWriter.append(jobTitle).append(",")
                        .append(contractType).append(",")
                        .append(experienceDemandee).append(",")
                        .append(startDate).append(",")
                        .append(endDate).append(",")
                        .append(jobPosts).append("\n");
            }

            csvWriter.close();
            System.out.println("Données enregistrées dans jobs.csv");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}







////*******************************************
//
//package scraping;
//
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//
//import java.io.FileWriter;
//import java.nio.charset.StandardCharsets;
//
//public class Scraping {
//    public static void main(String[] args) {
//        try {
//       
//            Document doc = Jsoup.connect("https://www.rekrute.com/offres-emploi-maroc.html").get();
//
//
//            Elements jobTitles = doc.select("h2 > a.titreJob"); 
//          Elements experienceDemande = doc.select("li:contains(Type de contrat proposé :) > a"); // 
//
////            Elements descriptions = doc.select("div.info > span"); // 
////            Elements dates = doc.select("em.date > span"); // 
//            Elements contractTypes = doc.select("ul > li:contains(Expérience requise :) > a"); 
//            		                                         
//
//             
//            FileWriter csvWriter = new FileWriter("jobs.csv",StandardCharsets.UTF_8);
//            csvWriter.append("Titre,Type de Contrat,Experience demande,Date de debut\n");
////            csvWriter.append("Titre,Description,Date Début,Date Fin,Type de Contrat\n");
//
//            for (int i = 0; i < jobTitles.size(); i++) {
//                String jobTitle = jobTitles.get(i).text(); // استخراج العنوان
//                String experienceDemandee = experienceDemande.get(i).text(); // استخراج العنوان
//
//
//                
//                //                String description = (descriptions.size() > i) ? descriptions.get(i).text() : "Non spécifié"; 
////                String dateStart = (dates.size() > i) ? dates.get(i).text() : "Non spécifié"; 
////                String dateEnd = (dates.size() > i + 1) ? dates.get(i + 1).text() : "Non spécifié"; 
//                String contractType = (contractTypes.size() > i) ? contractTypes.get(i).text() : "Non spécifié";  
//
//                csvWriter.append(jobTitle).append(",")
//                        .append(experienceDemandee).append(",")
//
////                        .append(dateStart).append(",")
////                        .append(dateEnd).append(",")
//                        .append(contractType).append("\n");
//            }
//
//            csvWriter.close();
//            System.out.println("Données enregistrées dans jobs.csv");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
