package scraping;

import nlp.TextProcessing;
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

            // Sélection des données
            Elements jobTitles = doc.select("h2 > a.titreJob");
            Elements experienceDemande = doc.select("li:contains(Expérience requise :) > a");
            Elements contractTypes = doc.select("li:contains(Type de contrat proposé :) > a");
            Elements dateElements = doc.select("em.date");

            // Écriture dans le fichier CSV
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

                // Traitement des données avec TextProcessing
                jobTitle = TextProcessing.normalizeText(jobTitle);
                experienceDemandee = TextProcessing.removeStopwords(experienceDemandee);
                contractType = TextProcessing.normalizeText(contractType);

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
