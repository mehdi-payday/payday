// Fichier ReservationDAO.java
// Auteur : Jeremi Cyr
// Date de création : 2016-09-15

package ca.qc.collegeahuntsic.bibliotheque.dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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

    private static final String ADD_REQUEST = "INSERT INTO Reservation (idReservation, idLivre, idMembre, dateReservation) VALUES(?, ?, ?, ?)";

    private static final String DELETE_REQUEST = "DELETE FROM Reservation WHERE idReservation = ?";

    private static final String FIND_BY_LIVRE_REQUEST = "SELECT * FROM Reservation WHERE idLivre = ?";

    private static final String FIND_BY_MEMBRE_REQUEST = "SELECT * FROM Reservation WHERE idMembre = ?";

    private static final String GET_ALL_REQUEST = "SELECT * FROM Reservation";

    private static final String READ_REQUEST = "SELECT * FROM Reservation WHERE idReservation = ?";

    private static final String UPDATE_REQUEST = "UPDATE Reservation SET idReservation = ?, idLivre = ?, idMembre = ?, dateReservation = ? WHERE idReservation = ?";

    private LivreService livre;

    private MembreService membre;

    private ReservationService reservation;

    private Connexion connexion;

    /**
     * Crée un DAO à partir d'une connexion à la base de données.
     *
     * @param connexion - La connexion à utiliser
     */
    public ReservationDAO(Connexion connexion) {
        super(connexion);
        this.connexion = connexion;
    }

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
        this.connexion = livre.getConnexion();
        this.livre = livre;
        this.membre = membre;
        this.reservation = reservation;
    }

    /**
     * Ajoute une nouvelle réservation.
     *
     * @param reservationDTO - La réservation à ajouter
     * @throws DAOException - S'il y a une erreur avec la base de données
     */
    public void add(ReservationDTO reservationDTO) throws DAOException {
        try(
            PreparedStatement preparedStatement = this.connexion.getConnection().prepareStatement(ReservationDAO.ADD_REQUEST)) {
            preparedStatement.setInt(1,
                reservationDTO.getIdReservation());
            preparedStatement.setInt(2,
                reservationDTO.getIdLivre());
            preparedStatement.setInt(3,
                reservationDTO.getIdMembre());
            preparedStatement.setDate(4,
                reservationDTO.getDateReservation());
            preparedStatement.execute();
        } catch(SQLException e) {
            throw new DAOException(e);
        }
    }

    /**
     * Lit une réservation.
     *
     * @param idReservation - La réservation à lire
     * @throws DAOException - S'il y a une erreur avec la base de données
     * @return La réservation
     */
    public ReservationDTO read(int idReservation) throws DAOException {
        try(
            PreparedStatement preparedStatement = this.connexion.getConnection().prepareStatement(ReservationDAO.READ_REQUEST)) {
            preparedStatement.setInt(1,
                idReservation);

            try(
                ResultSet resultSet = preparedStatement.executeQuery()) {
                if(resultSet.next()) {
                    final ReservationDTO reservationDTO = new ReservationDTO();
                    reservationDTO.setIdReservation(resultSet.getInt(1));
                    reservationDTO.setIdMembre(resultSet.getInt(2));
                    reservationDTO.setIdLivre(resultSet.getInt(3));
                    reservationDTO.setDateReservation(resultSet.getDate(4));
                    return reservationDTO;
                }

                throw new DAOException("La réservation avec l'id "
                    + idReservation
                    + " n'existe pas.");
            }
        } catch(SQLException e) {
            throw new DAOException(e);
        }
    }

    /**
     * Met à jour une réservation.
     *
     * @param reservationDTO - La réservation à mettre à jour
     * @throws DAOException - S'il y a une erreur avec la base de données
     */
    public void update(ReservationDTO reservationDTO) throws DAOException {
        try(
            PreparedStatement preparedStatement = this.connexion.getConnection().prepareStatement(ReservationDAO.UPDATE_REQUEST)) {
            preparedStatement.setInt(1,
                reservationDTO.getIdReservation());
            preparedStatement.setInt(2,
                reservationDTO.getIdLivre());
            preparedStatement.setInt(3,
                reservationDTO.getIdMembre());
            preparedStatement.setDate(4,
                reservationDTO.getDateReservation());
            preparedStatement.setInt(5,
                reservationDTO.getIdReservation());
            preparedStatement.execute();
        } catch(SQLException e) {
            throw new DAOException(e);
        }
    }

    /**
     * Supprime une réservation.
     *
     * @param reservationDTO - La réservation à supprimer
     * @throws DAOException - S'il y a une erreur avec la base de données
     */
    public void delete(ReservationDTO reservationDTO) throws DAOException {
        try(
            PreparedStatement preparedStatement = this.connexion.getConnection().prepareStatement(ReservationDAO.DELETE_REQUEST)) {
            preparedStatement.setInt(1,
                reservationDTO.getIdReservation());
            preparedStatement.execute();
        } catch(SQLException e) {
            throw new DAOException(e);
        }
    }

    /**
     * Trouve toutes les réservations.
     *
     * @throws DAOException - S'il y a une erreur avec la base de données
     * @return La liste des réservations ; une liste vide sinon
     */
    public List<ReservationDTO> getAll() throws DAOException {
        try(
            PreparedStatement preparedStatement = this.connexion.getConnection().prepareStatement(ReservationDAO.GET_ALL_REQUEST)) {
            try(
                ResultSet resultSet = preparedStatement.executeQuery()) {
                final List<ReservationDTO> reservations = new ArrayList<>();
                while(resultSet.next()) {
                    final ReservationDTO reservationDTO = new ReservationDTO();
                    reservationDTO.setIdReservation(resultSet.getInt(1));
                    reservationDTO.setIdLivre(resultSet.getInt(2));
                    reservationDTO.setIdLivre(resultSet.getInt(2));
                    reservationDTO.setDateReservation(resultSet.getDate(4));

                    reservations.add(reservationDTO);
                }
                return reservations;
            }
        } catch(SQLException e) {
            throw new DAOException(e);
        }
    }

    /**
     * Trouve les réservations à partir d'un livre.
     *
     * @param livreDTO - Le livre à utiliser
     * @return La liste des réservations correspondantes, triée par date de réservation croissante ; une liste vide sinon
     * @throws DAOException - S'il y a une erreur avec la base de données
     */
    public List<ReservationDTO> findByLivre(LivreDTO livreDTO) throws DAOException {
        try(
            PreparedStatement preparedStatement = this.connexion.getConnection().prepareStatement(ReservationDAO.FIND_BY_LIVRE_REQUEST)) {
            preparedStatement.setInt(1,
                livreDTO.getIdLivre());
            try(
                ResultSet resultSet = preparedStatement.executeQuery()) {
                final List<ReservationDTO> reservations = new ArrayList<>();
                while(resultSet.next()) {
                    final ReservationDTO reservationDTO = new ReservationDTO();
                    reservationDTO.setIdReservation(resultSet.getInt(1));
                    reservationDTO.setIdLivre(resultSet.getInt(2));
                    reservationDTO.setIdLivre(resultSet.getInt(2));
                    reservationDTO.setDateReservation(resultSet.getDate(4));

                    reservations.add(reservationDTO);
                }
                return reservations;
            }
        } catch(SQLException e) {
            throw new DAOException(e);
        }
    }

    /**
     * Trouve les réservations à partir d'un livre.
     *
     * @param membreDTO - Le membre à utiliser
     * @return La liste des réservations correspondantes, triée par date de réservation croissante ; une liste vide sinon
     * @throws DAOException - S'il y a une erreur avec la base de données
     */
    public List<ReservationDTO> finbByMembre(MembreDTO membreDTO) throws DAOException {
        try(
            PreparedStatement preparedStatement = this.connexion.getConnection().prepareStatement(ReservationDAO.FIND_BY_MEMBRE_REQUEST)) {
            preparedStatement.setInt(1,
                membreDTO.getIdMembre());
            try(
                ResultSet resultSet = preparedStatement.executeQuery()) {
                final List<ReservationDTO> reservations = new ArrayList<>();
                while(resultSet.next()) {
                    final ReservationDTO reservationDTO = new ReservationDTO();
                    reservationDTO.setIdReservation(resultSet.getInt(1));
                    reservationDTO.setIdLivre(resultSet.getInt(2));
                    reservationDTO.setIdLivre(resultSet.getInt(2));
                    reservationDTO.setDateReservation(resultSet.getDate(4));

                    reservations.add(reservationDTO);
                }
                return reservations;
            }
        } catch(SQLException e) {
            throw new DAOException(e);
        }
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
            this.connexion.commit();
        } catch(ConnexionException connexionException) {
            try {
                this.connexion.rollback();
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
            this.connexion.commit();
        } catch(ConnexionException connexionException) {
            try {
                this.connexion.rollback();
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

            this.connexion.commit();
        } catch(ConnexionException connexionException) {
            try {
                this.connexion.rollback();
            } catch(ConnexionException connexionException2) {
                throw new DAOException(connexionException2);
            }
            throw new DAOException(connexionException);
        } catch(ServiceException serviceException) {
            throw new DAOException(serviceException);
        }
    }
}
