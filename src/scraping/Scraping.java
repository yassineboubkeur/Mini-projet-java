package scraping;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;

public class Scraping {
    public static void main(String[] args) {
        try {
       
            Document doc = Jsoup.connect("https://www.rekrute.com/offres-emploi-maroc.html").get();

           
            Elements jobTitles = doc.select("a.titreJob"); 
            Elements descriptions = doc.select("div.info > span"); // 
            Elements dates = doc.select("em.date > span"); // 
            Elements contractTypes = doc.select("li:contains(Type de contrat proposé :) a"); 

             
            FileWriter csvWriter = new FileWriter("jobs.csv");
            csvWriter.append("Titre,Description,Date Début,Date Fin,Type de Contrat\n");

            for (int i = 0; i < jobTitles.size(); i++) {
                String jobTitle = jobTitles.get(i).text(); // استخراج العنوان
                String description = (descriptions.size() > i) ? descriptions.get(i).text() : "Non spécifié"; 
                String dateStart = (dates.size() > i) ? dates.get(i).text() : "Non spécifié"; 
                String dateEnd = (dates.size() > i + 1) ? dates.get(i + 1).text() : "Non spécifié"; 
                String contractType = (contractTypes.size() > i) ? contractTypes.get(i).text() : "Non spécifié";  

                
                csvWriter.append(jobTitle).append(",")
                        .append(description).append(",")
                        .append(dateStart).append(",")
                        .append(dateEnd).append(",")
                        .append(contractType).append("\n");
            }

            csvWriter.close();
            System.out.println("Données enregistrées dans jobs.csv");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
