// Fichier : BiblioException.java
// Auteur : Mehdi Hamidi
// Date de creation : 2016-09-15

package ca.qc.collegeahuntsic.bibliotheque.exception;

/**
 * L'exception BiblioException est levée lorsqu'une transaction est inadéquate.
 * Par exemple -- livre inexistant
 */

public final class BiblioException extends Exception {

    private static final long serialVersionUID = 1L;

    public BiblioException(final String message) {
        super(message);
    }
}