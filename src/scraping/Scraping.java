package scraping;

import nlp.TextProcessing;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Scraping {
    public static void main(String[] args) {
        // Connexion à la base de données MySQL
        String url = "jdbc:mysql://localhost:3306/jobs_db"; // Remplacez par votre URL de connexion
        String username = "root"; // Remplacez par votre nom d'utilisateur
        String password = ""; // Remplacez par votre mot de passe

        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            Document doc = Jsoup.connect("https://www.rekrute.com/offres-emploi-maroc.html").get();

            // Sélection des données
            Elements jobTitles = doc.select("h2 > a.titreJob");
            Elements experienceDemande = doc.select("li:contains(Expérience requise :) > a");
            Elements contractTypes = doc.select("li:contains(Type de contrat proposé :) > a");
            Elements dateElements = doc.select("em.date");

            // Préparer la requête d'insertion
            String sql = "INSERT INTO job_offers (job_title, contract_type, experience_required, start_date, end_date, job_posts) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
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

                    // Convertir la date au format MySQL (YYYY-MM-DD)
                    startDate = formatDate(startDate);
                    endDate = formatDate(endDate);

                    // Traitement des données avec TextProcessing
                    jobTitle = TextProcessing.normalizeText(jobTitle);
                    experienceDemandee = TextProcessing.removeStopwords(experienceDemandee);
                    contractType = TextProcessing.normalizeText(contractType);

                    // Remplir les paramètres de la requête préparée
                    stmt.setString(1, jobTitle);
                    stmt.setString(2, contractType);
                    stmt.setString(3, experienceDemandee);
                    stmt.setString(4, startDate.equals("Non spécifié") ? null : startDate);
                    stmt.setString(5, endDate.equals("Non spécifié") ? null : endDate);
                    stmt.setInt(6, jobPosts.equals("Non spécifié") ? 0 : Integer.parseInt(jobPosts));

                    // Exécuter l'insertion
                    stmt.executeUpdate();
                }

                System.out.println("Données enregistrées dans la base de données");

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Méthode pour formater la date au format YYYY-MM-DD
    private static String formatDate(String date) {
        try {
            if (date.equals("Non spécifié") || date.isEmpty()) {
                return "Non spécifié";
            }
            SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date parsedDate = inputFormat.parse(date);
            return outputFormat.format(parsedDate);
        } catch (Exception e) {
            return "Non spécifié"; // En cas d'erreur, renvoyer "Non spécifié"
        }
    }
}




