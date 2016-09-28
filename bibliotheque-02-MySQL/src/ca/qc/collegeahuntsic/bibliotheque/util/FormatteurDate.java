// Fichier FormatDate.java
// Auteur : Vincent Laferrière
// Date de création : 15 sept. 2016

package ca.qc.collegeahuntsic.bibliotheque.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utilitaire de création d'une {@link java.util.Date} dans un format défini.
 *
 * @author Jeremi Cyr
 */
public final class FormatteurDate {
    private static SimpleDateFormat formatAMJ;
    static {
        formatAMJ = new SimpleDateFormat("yyyy-MM-dd");
        formatAMJ.setLenient(false);
    }

    /**
     * Constructeur privé pour empêcher toute instantiation.
     *
     */
    private FormatteurDate() {

    }

    /**
     * Convertit une chaine de caractères en Date selon le format YYYY-MM-DD.
     *
     * @param dateString - la chaine de caractètres
     * @return la date issu de la conversion
     * @throws ParseException - si la chaine de caractère n'est pas formatée correctement.
     */
    public static Date convertirDate(final String dateString) throws ParseException {
        return formatAMJ.parse(dateString);
    }

    /**
     * Convertit date en une chaine de caractères selon le format YYYY-MM-DD.
     *
     * @param date - la date
     * @return la chaine de caracteres issu de la conversion
     */
    public static String toString(final Date date) {
        return formatAMJ.format(date);
    }
}
