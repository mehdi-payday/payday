// Fichier GestionBibliotheque.java
// Auteur : Adam Cherti
// Date de création : 2016-09-15

package ca.qc.collegeahuntsic.bibliotheque.service;

import java.sql.SQLException;
import ca.qc.collegeahuntsic.bibliotheque.dao.InterrogationDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.LivreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.MembreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.PretDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.ReservationDAO;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.exception.BiblioException;

/**
 * Système de gestion d'une bibliothèque
 *
 *
 * Ce programme permet de gérer les transaction de base d'une
 * bibliothèque.  Il gère des livres, des membres et des
 * réservations. Les données sont conservées dans une base de
 * données relationnelles accédées avec JDBC.
 *
 * Pré-condition
 *   la base de données de la bibliothèque doit exister
 *
 * Post-condition
 *   le programme effectue les maj associées à chaque
 *   transaction
 *
 */
public class GestionBibliotheque extends Service {
    public Connexion cx;

    public LivreService livre;

    public MembreService membre;

    public ReservationService reservation;

    public LivreDAO gestionLivre;

    public MembreDAO gestionMembre;

    public PretDAO gestionPret;

    public ReservationDAO gestionReservation;

    public InterrogationDAO gestionInterrogation;

    /**
      * Ouvre une connexion avec la BD relationnelle et
      * alloue les gestionnaires de transactions et de tables.
      *
      * @param serveur SQL
      * @param bd nom de la bade de données
      * @param user user id pour établir une connexion avec le serveur SQL
      * @param password mot de passe pour le user id
      */
    public GestionBibliotheque(final String serveur,
        final String bd,
        final String user,
        final String password) throws BiblioException,
        SQLException {

        this.cx = new Connexion(serveur,
            bd,
            user,
            password);
        this.livre = new LivreService(this.cx);
        this.membre = new MembreService(this.cx);
        this.reservation = new ReservationService(this.cx);
        this.gestionLivre = new LivreDAO(this.livre,
            this.reservation);
        this.gestionMembre = new MembreDAO(this.membre,
            this.reservation);
        this.gestionPret = new PretDAO(this.livre,
            this.membre,
            this.reservation);
        this.gestionReservation = new ReservationDAO(this.livre,
            this.membre,
            this.reservation);
        this.gestionInterrogation = new InterrogationDAO(this.cx);
    }

    public void fermer() throws SQLException {
        this.cx.fermer();
    }
}
