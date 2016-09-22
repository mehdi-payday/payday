// Fichier ReservationDAO.java
// Auteur : Jeremi Cyr
// Date de création : 2016-09-15

package ca.qc.collegeahuntsic.bibliotheque.dao;

import java.sql.Date;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.LivreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.MembreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.ReservationDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.ConnexionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.DAOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.ServiceException;
import ca.qc.collegeahuntsic.bibliotheque.service.LivreService;
import ca.qc.collegeahuntsic.bibliotheque.service.MembreService;
import ca.qc.collegeahuntsic.bibliotheque.service.ReservationService;

/**
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
     * Crée un DAO à partir d'une connexion à la base de données.
     *
     * @param livre .
     * @param membre .
     * @param reservation .
     * @throws DAOException .
     */
    public ReservationDAO(LivreService livre,
        MembreService membre,
        ReservationService reservation) throws DAOException {
        super(livre.getConnexion());
        if(livre.getConnexion() != membre.getConnexion()
            || reservation.getConnexion() != membre.getConnexion()) {
            throw new DAOException("Les instances de livre, de membre et de reservation n'utilisent pas la même connexion au serveur");
        }
        this.cx = livre.getConnexion();
        this.livre = livre;
        this.membre = membre;
        this.reservation = reservation;
    }

    /**
     * Réservation d'un livre par un membre. Le livre doit être prêté.
     *
     * @param idReservation .
     * @param idLivre .
     * @param idMembre .
     * @param dateReservation .
     * @throws DAOException .
     */
    public void reserver(int idReservation,
        int idLivre,
        int idMembre,
        String dateReservation) throws DAOException {
        try {
            final LivreDTO tupleLivre = this.livre.getLivre(idLivre);
            if(tupleLivre == null) {
                throw new DAOException("Livre inexistant: "
                    + idLivre);
            }
            if(tupleLivre.getIdMembre() == 0) {
                throw new DAOException("Livre "
                    + idLivre
                    + " n'est pas prete");
            }
            if(tupleLivre.getIdLivre() == idMembre) {
                throw new DAOException("Livre "
                    + idLivre
                    + " deja prete a ce membre");
            }

            final MembreDTO tupleMembre = this.membre.getMembre(idMembre);
            if(tupleMembre == null) {
                throw new DAOException("Membre inexistant: "
                    + idMembre);
            }

            if(Date.valueOf(dateReservation).before(tupleLivre.getDatePret())) {
                throw new DAOException("Date de reservation inferieure à la date de pret");
            }

            if(this.reservation.existe(idReservation)) {
                throw new DAOException("Réservation "
                    + idReservation
                    + " existe deja");
            }

            this.reservation.reserver(idReservation,
                idLivre,
                idMembre,
                dateReservation);
            this.cx.commit();
        } catch(ConnexionException connexionException) {
            try {
                this.cx.rollback();
            } catch(ConnexionException connexionException2) {
                throw new DAOException(connexionException2);
            }
            throw new DAOException(connexionException);
        } catch(ServiceException serviceException) {
            throw new DAOException(serviceException);
        }
    }

    /**
     * Prise d'une réservation. Le livre ne doit pas être prêté. Le
     *      membre ne doit pas avoir dépassé sa limite de pret. La réservation
     *      doit la être la première en liste.
     *
     * @param idReservation .
     * @param datePret .
     * @throws DAOException .
     */
    public void prendreRes(int idReservation,
        String datePret) throws DAOException {
        try {
            final ReservationDTO tupleReservation = this.reservation.getReservation(idReservation);
            if(tupleReservation == null) {
                throw new DAOException("Réservation inexistante : "
                    + idReservation);
            }

            final ReservationDTO tupleReservationPremiere = this.reservation.getReservationLivre(tupleReservation.getIdLivre());
            if(tupleReservation.getIdReservation() != tupleReservationPremiere.getIdReservation()) {
                throw new DAOException("La réservation n'est pas la première de la liste "
                    + "pour ce livre; la premiere est "
                    + tupleReservationPremiere.getIdReservation());
            }

            final LivreDTO tupleLivre = this.livre.getLivre(tupleReservation.getIdLivre());
            if(tupleLivre == null) {
                throw new DAOException("Livre inexistant: "
                    + tupleReservation.getIdLivre());
            }
            if(tupleLivre.getIdMembre() != 0) {
                throw new DAOException("Livre "
                    + tupleLivre.getIdLivre()
                    + " deja prêté ? "
                    + tupleLivre.getIdMembre());
            }

            final MembreDTO tupleMembre = this.membre.getMembre(tupleReservation.getIdMembre());
            if(tupleMembre == null) {
                throw new DAOException("Membre inexistant: "
                    + tupleReservation.getIdMembre());
            }
            if(tupleMembre.getNbPret() >= tupleMembre.getLimitePret()) {
                throw new DAOException("Limite de prêt du membre "
                    + tupleReservation.getIdMembre()
                    + " atteinte");
            }

            if(Date.valueOf(datePret).before(tupleReservation.getDateReservation())) {
                throw new DAOException("Date de prêt inférieure à la date de réservation");
            }

            if(this.livre.preter(tupleReservation.getIdLivre(),
                tupleReservation.getIdMembre(),
                datePret) == 0) {
                throw new DAOException("Livre supprimé par une autre transaction");
            }
            if(this.membre.preter(tupleReservation.getIdLivre()) == 0) {
                throw new DAOException("Membre supprimé par une autre transaction");
            }

            this.reservation.annulerRes(idReservation);
            this.cx.commit();
        } catch(ConnexionException connexionException) {
            try {
                this.cx.rollback();
            } catch(ConnexionException connexionException2) {
                throw new DAOException(connexionException2);
            }
            throw new DAOException(connexionException);
        } catch(ServiceException serviceException) {
            throw new DAOException(serviceException);
        }
    }

    /**
     * Annulation d'une réservation. La réservation doit exister.
     *
     * @param idReservation .
     * @throws DAOException .
     */
    public void annulerRes(int idReservation) throws DAOException {
        try {
            if(this.reservation.annulerRes(idReservation) == 0) {
                throw new DAOException("Réservation "
                    + idReservation
                    + " n'existe pas");
            }

            this.cx.commit();
        } catch(ConnexionException connexionException) {
            try {
                this.cx.rollback();
            } catch(ConnexionException connexionException2) {
                throw new DAOException(connexionException2);
            }
            throw new DAOException(connexionException);
        } catch(ServiceException serviceException) {
            throw new DAOException(serviceException);
        }
    }
}
