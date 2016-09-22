// Fichier GestionBibliotheque.java
// Auteur : Adam Cherti
// Date de création : 2016-09-15

package ca.qc.collegeahuntsic.bibliotheque.service;

import ca.qc.collegeahuntsic.bibliotheque.dao.InterrogationDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.LivreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.MembreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.PretDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.ReservationDAO;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.exception.BiblioException;
import ca.qc.collegeahuntsic.bibliotheque.exception.ConnexionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.DAOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.ServiceException;

/**
 * Système de gestion d'une bibliothèque
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
 * @author Adam Cherti
 *
 */
public class GestionBibliotheque extends Service {

    private static final long serialVersionUID = 1L;

    private Connexion cx;

    private LivreService livre;

    private MembreService membre;

    private ReservationService reservation;

    private LivreDAO gestionLivre;

    private MembreDAO gestionMembre;

    private PretDAO gestionPret;

    private ReservationDAO gestionReservation;

    private InterrogationDAO gestionInterrogation;

    /**
      * Ouvre une connexion avec la BD relationnelle et
      * alloue les gestionnaires de transactions et de tables.
      *
      * @param serveur SQL
      * @param bd nom de la bade de données
      * @param user user id pour établir une connexion avec le serveur SQL
      * @param password mot de passe pour le user id
      * @throws BiblioException erreur d'initialisation de la
      * bibliotheque
      * @throws ServiceException erreur SQL
      */
    public GestionBibliotheque(final String serveur,
        final String bd,
        final String user,
        final String password) throws ServiceException {

        this.cx = new Connexion(serveur,
            bd,
            user,
            password);
        try {
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
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        } catch(ConnexionException connException) {
            throw new ServiceException(connException);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * 
     * Fermer la connection a la base de données.
     *
     * @throws ConnexionException erreur SQL
     */
    public void fermer() throws ConnexionException {
        this.cx.fermer();
    }

    // Region Getters and Setters
    /**
     *
     * Retourne la connexion.
     *
     * @return la connexion à la base de données
     */
    public Connexion getCx() {
        return this.cx;
    }

    /**
     *
     * Retourne le service de la table livre.
     *
     * @return le service de la table livre
     */
    public LivreService getLivre() {
        return this.livre;
    }

    /**
     *
     * Retourne le service de la table membre.
     *
     * @return le service de la table membre
     */
    public MembreService getMembre() {
        return this.membre;
    }

    /**
     *
     * Retourne le service de la table réservation.
     *
     * @return le service de la table reservation
     */
    public ReservationService getReservation() {
        return this.reservation;
    }

    /**
     *
     * Retourne le DAO de la table livre.
     *
     * @return le DAO de livre
     */
    public LivreDAO getGestionLivre() {
        return this.gestionLivre;
    }

    /**
     *
     * Retourne le DAO de la table membre.
     *
     * @return le DAO de membre
     */
    public MembreDAO getGestionMembre() {
        return this.gestionMembre;
    }

    /**
     *
     * Retourne le DAO de la table pret.
     *
     * @return le DAO de pret
     */
    public PretDAO getGestionPret() {
        return this.gestionPret;
    }

    /**
     *
     * Retourne le DAO de la table reservation.
     *
     * @return le DAO de reservation
     */
    public ReservationDAO getGestionReservation() {
        return this.gestionReservation;
    }

    /**
     *
     * Retourne le DAO de la table integorration.
     *
     * @return le DAO de la table interrogation
     */
    public InterrogationDAO getGestionInterrogation() {
        return this.gestionInterrogation;
    }
    // EndRegion
}
