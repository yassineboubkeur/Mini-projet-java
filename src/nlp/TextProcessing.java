package nlp;

import java.util.*;
import java.util.regex.Pattern;

public class TextProcessing {

    private static final List<String> STOPWORDS = Arrays.asList(
            "le", "la", "les", "de", "des", "un", "une", "et", "pour", "dans", "par", "sur", "en", "avec"
    );

    // Fonction pour normaliser le texte (nettoyage de base)
    public static String normalizeText(String text) {
        if (text == null) return "";

        // Convertir en minuscule, supprimer les espaces inutiles
        text = text.toLowerCase(Locale.ROOT).trim();

        // Supprimer les caractères spéciaux mais garder les chiffres et caractères accentués comme "à"
        text = text.replaceAll("[^a-zA-Z0-9\\sàâäéèêëîïôöùûüç]", "");

        return text;
    }

    // Fonction pour supprimer les mots vides (stopwords)
    public static String removeStopwords(String text) {
        String[] words = text.split("\\s+");
        StringBuilder cleanedText = new StringBuilder();

        for (String word : words) {
            if (!STOPWORDS.contains(word)) {
                cleanedText.append(word).append(" ");
            }
        }

        return cleanedText.toString().trim();
    }

    // Fonction pour extraire les mots-clés
    public static List<String> extractKeywords(String text, List<String> keywords) {
        List<String> foundKeywords = new ArrayList<>();
        for (String keyword : keywords) {
            if (text.contains(keyword.toLowerCase(Locale.ROOT))) {
                foundKeywords.add(keyword);
            }
        }
        return foundKeywords;
    }

    // Fonction pour effectuer un "stemming" simple
    public static String stemWord(String word) {
        // Implémentation simplifiée (vous pouvez intégrer un outil comme Snowball Stemmer)
        if (word.endsWith("ation")) {
            return word.replace("ation", "er");
        } else if (word.endsWith("ment")) {
            return word.replace("ment", "");
        }
        return word;
    }

    // Exemple de vectorisation simple avec Bag of Words
    public static Map<String, Integer> bagOfWords(String text) {
        Map<String, Integer> wordCounts = new HashMap<>();
        String[] words = text.split("\\s+");
        for (String word : words) {
            wordCounts.put(word, wordCounts.getOrDefault(word, 0) + 1);
        }
        return wordCounts;
    }
}
