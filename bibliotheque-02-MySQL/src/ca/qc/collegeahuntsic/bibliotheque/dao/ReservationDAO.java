// Fichier ReservationDAO.java
// Auteur : Jeremi Cyr
// Date de création : 2016-09-15

package ca.qc.collegeahuntsic.bibliotheque.dao;

import java.sql.Date;
import java.sql.SQLException;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.LivreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.MembreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.ReservationDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.BiblioException;
import ca.qc.collegeahuntsic.bibliotheque.service.LivreService;
import ca.qc.collegeahuntsic.bibliotheque.service.MembreService;
import ca.qc.collegeahuntsic.bibliotheque.service.ReservationService;

/**
 * 
 * DAO pour effectuer des CRUDs avec la table reservation.
 *
 * @author Mehdi Hamidi
 */

public class ReservationDAO extends DAO {

    private static final long serialVersionUID = 1L;

    private LivreService livre;

    private MembreService membre;

    private ReservationService reservation;

    private Connexion cx;

    /**
     * 
     * Crée un DAO à partir d'une connexion à la base de données.
     *
     * @param livre .
     * @param membre .
     * @param reservation .
     * @throws BiblioException .
     */
    public ReservationDAO(LivreService livre,
        MembreService membre,
        ReservationService reservation) throws BiblioException {
        super(livre.getConnexion());
        if(livre.getConnexion() != membre.getConnexion()
            || reservation.getConnexion() != membre.getConnexion()) {
            throw new BiblioException("Les instances de livre, de membre et de reservation n'utilisent pas la même connexion au serveur");
        }
        this.cx = livre.getConnexion();
        this.livre = livre;
        this.membre = membre;
        this.reservation = reservation;
    }

    /**
     * 
     * Réservation d'un livre par un membre. Le livre doit être prêté.
     *
     * @param idReservation .
     * @param idLivre .
     * @param idMembre .
     * @param dateReservation .
     * @throws SQLException .
     * @throws BiblioException .
     * @throws Exception .
     */
    public void reserver(int idReservation,
        int idLivre,
        int idMembre,
        String dateReservation) throws SQLException,
        BiblioException,
        Exception {
        try {
            /* Verifier que le livre est prêté */
            final LivreDTO tupleLivre = this.livre.getLivre(idLivre);
            if(tupleLivre == null) {
                throw new BiblioException("Livre inexistant: "
                    + idLivre);
            }
            if(tupleLivre.getIdMembre() == 0) {
                throw new BiblioException("Livre "
                    + idLivre
                    + " n'est pas prete");
            }
            if(tupleLivre.getIdLivre() == idMembre) {
                throw new BiblioException("Livre "
                    + idLivre
                    + " deja prete a ce membre");
            }

            /* Vérifier que le membre existe */
            final MembreDTO tupleMembre = this.membre.getMembre(idMembre);
            if(tupleMembre == null) {
                throw new BiblioException("Membre inexistant: "
                    + idMembre);
            }

            /* Verifier si date reservation >= datePret */
            if(Date.valueOf(dateReservation).before(tupleLivre.getDatePret())) {
                throw new BiblioException("Date de reservation inferieure à la date de pret");
            }

            /* Vérifier que la réservation n'existe pas */
            if(this.reservation.existe(idReservation)) {
                throw new BiblioException("Réservation "
                    + idReservation
                    + " existe deja");
            }

            /* Creation de la reservation */
            this.reservation.reserver(idReservation,
                idLivre,
                idMembre,
                dateReservation);
            this.cx.commit();
        } catch(Exception e) {
            this.cx.rollback();
            throw e;
        }
    }

    /**
     * 
     * Prise d'une réservation. Le livre ne doit pas être prêté. Le
     * membre ne doit pas avoir dépassé sa limite de pret. La réservation
     * doit la être la première en liste.
     *
     * @param idReservation .
     * @param datePret .
     * @throws SQLException .
     * @throws BiblioException .
     * @throws Exception .
     */
    public void prendreRes(int idReservation,
        String datePret) throws SQLException,
        BiblioException,
        Exception {
        try {
            /* Vérifie s'il existe une réservation pour le livre */
            final ReservationDTO tupleReservation = this.reservation.getReservation(idReservation);
            if(tupleReservation == null) {
                throw new BiblioException("Réservation inexistante : "
                    + idReservation);
            }

            /* Vérifie que c'est la première réservation pour le livre */
            final ReservationDTO tupleReservationPremiere = this.reservation.getReservationLivre(tupleReservation.getIdLivre());
            if(tupleReservation.getIdReservation() != tupleReservationPremiere.getIdReservation()) {
                throw new BiblioException("La réservation n'est pas la première de la liste "
                    + "pour ce livre; la premiere est "
                    + tupleReservationPremiere.getIdReservation());
            }

            /* Verifier si le livre est disponible */
            final LivreDTO tupleLivre = this.livre.getLivre(tupleReservation.getIdLivre());
            if(tupleLivre == null) {
                throw new BiblioException("Livre inexistant: "
                    + tupleReservation.getIdLivre());
            }
            if(tupleLivre.getIdMembre() != 0) {
                throw new BiblioException("Livre "
                    + tupleLivre.getIdLivre()
                    + " deja prêté ? "
                    + tupleLivre.getIdMembre());
            }

            /* Vérifie si le membre existe et sa limite de prêt */
            final MembreDTO tupleMembre = this.membre.getMembre(tupleReservation.getIdMembre());
            if(tupleMembre == null) {
                throw new BiblioException("Membre inexistant: "
                    + tupleReservation.getIdMembre());
            }
            if(tupleMembre.getNbPret() >= tupleMembre.getLimitePret()) {
                throw new BiblioException("Limite de prêt du membre "
                    + tupleReservation.getIdMembre()
                    + " atteinte");
            }

            /* Verifier si datePret >= tupleReservation.dateReservation */
            if(Date.valueOf(datePret).before(tupleReservation.getDateReservation())) {
                throw new BiblioException("Date de prêt inférieure à la date de réservation");
            }

            /* Enregistrement du prêt. */
            if(this.livre.preter(tupleReservation.getIdLivre(),
                tupleReservation.getIdMembre(),
                datePret) == 0) {
                throw new BiblioException("Livre supprimé par une autre transaction");
            }
            if(this.membre.preter(tupleReservation.getIdLivre()) == 0) {
                throw new BiblioException("Membre supprimé par une autre transaction");
            }
            /* Eliminer la réservation */
            this.reservation.annulerRes(idReservation);
            this.cx.commit();
        } catch(Exception e) {
            this.cx.rollback();
            throw e;
        }
    }
    
    /**
     * 
     * Annulation d'une réservation. La réservation doit exister.
     *
     * @param idReservation .
     * @throws SQLException .
     * @throws BiblioException .
     * @throws Exception .
     */
    public void annulerRes(int idReservation) throws SQLException,
        BiblioException,
        Exception {
        try {

            /* Vérifier que la réservation existe */
            if(this.reservation.annulerRes(idReservation) == 0) {
                throw new BiblioException("Réservation "
                    + idReservation
                    + " n'existe pas");
            }

            this.cx.commit();
        } catch(Exception e) {
            this.cx.rollback();
            throw e;
        }
    }
}
