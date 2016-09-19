// Fichier FormatDate.java
// Auteur : Vincent Laferrière
// Date de création : 15 sept. 2016

package ca.qc.collegeahuntsic.bibliotheque.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Utilitaire de création d'un @see java.sql.Timestamp dans un format défini.
 *
 * @author Jeremi Cyr
 */
public class FormatteurDate {
    private static SimpleDateFormat formatAMJ;
    static {
        formatAMJ = new SimpleDateFormat("yyyy-MM-dd");
        formatAMJ.setLenient(false);
    }

    /**
     *
     * constructeur privé pour empêcher toute instantiation
     *
     */
    private FormatteurDate() {

    }

    /**
     *
     * Convertit une chaine de caractères en Timestamp selon le format YYYY-MM-DD .
     *
     * @param dateString - la chaine de caractètres
     * @return le timestamp issu de la conversion
     * @throws ParseException - si la chaine de caractère n'est pas formatée correctement.
     */
    public static Timestamp convertirDate(String dateString) throws ParseException {
        return (Timestamp) formatAMJ.parse(dateString);
    }

    /**
     *
     * Convertit Timestamp en une chaine de caractères selon le format YYYY-MM-DD .
     *
     * @param timestamp - le timestamp
     * @return la chaine de caracteres issu de la conversion
     */
    public static String toString(Timestamp timestamp) {
        return formatAMJ.format(timestamp);
    }
}
