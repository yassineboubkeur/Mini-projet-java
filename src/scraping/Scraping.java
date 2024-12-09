package scraping;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.io.FileWriter;

public class Scraping {
    public static void main(String[] args) {
        try {
            Document doc = Jsoup.connect("https://www.rekrute.com").get();
            Elements jobTitles = doc.select("titreJob");

            // فتح ملف CSV
            FileWriter csvWriter = new FileWriter("jobs.csv");
            csvWriter.append("Titre\n");

            for (int i = 0; i < jobTitles.size(); i++) {
                String jobTitle = jobTitles.get(i).text();
                csvWriter.append(jobTitle).append("\n");
            }

            csvWriter.close();
            System.out.println("Données enregistrées dans jobs.csv");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

