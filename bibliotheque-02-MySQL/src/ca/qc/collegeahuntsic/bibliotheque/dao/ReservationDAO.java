// Fichier ReservationDAO.java
// Auteur : Jeremi Cyr
// Date de création : 2016-09-15

package ca.qc.collegeahuntsic.bibliotheque.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.LivreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.MembreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.ReservationDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.DAOException;

/**
 * DAO pour effectuer des CRUDs avec la table reservation.
 *
 * @author Mehdi Hamidi
 */

public class ReservationDAO extends DAO {

    private static final long serialVersionUID = 1L;

    private static final String ADD_REQUEST = "INSERT INTO Reservation (idReservation, idLivre, idMembre, dateReservation) VALUES(?, ?, ?, ?)";

    private static final String DELETE_REQUEST = "DELETE FROM Reservation WHERE idReservation = ?";

    private static final String FIND_BY_LIVRE_REQUEST = "SELECT idReservation, idLivre, idMembre, dateReservation FROM Reservation WHERE idLivre = ?";

    private static final String FIND_BY_MEMBRE_REQUEST = "SELECT idReservation, idLivre, idMembre, dateReservation FROM Reservation WHERE idMembre = ?";

    private static final String GET_ALL_REQUEST = "SELECT idReservation, idLivre, idMembre, dateReservation FROM Reservation";

    private static final String READ_REQUEST = "SELECT idReservation, idLivre, idMembre, dateReservation FROM Reservation WHERE idReservation = ?";

    private static final String UPDATE_REQUEST = "UPDATE Reservation SET idLivre = ?, idMembre = ?, dateReservation = ? WHERE idReservation = ?";

    /**
     * Crée un DAO à partir d'une connexion à la base de données.
     *
     * @param connexion - La connexion à utiliser
     */
    public ReservationDAO(Connexion connexion) {
        super(connexion);
    }

    /**
     * Ajoute une nouvelle réservation.
     *
     * @param reservationDTO - La réservation à ajouter
     * @throws DAOException - S'il y a une erreur avec la base de données
     */
    public void add(ReservationDTO reservationDTO) throws DAOException {
        try(
            PreparedStatement preparedStatement = getConnection().prepareStatement(ReservationDAO.ADD_REQUEST)) {
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
        ReservationDTO reservationDTO = null;
        try(
            PreparedStatement preparedStatement = getConnection().prepareStatement(ReservationDAO.READ_REQUEST)) {
            preparedStatement.setInt(1,
                idReservation);
            try(
                ResultSet resultSet = preparedStatement.executeQuery()) {
                if(resultSet.next()) {
                    reservationDTO = new ReservationDTO();
                    reservationDTO.setIdReservation(resultSet.getInt(1));
                    reservationDTO.setIdMembre(resultSet.getInt(2));
                    reservationDTO.setIdLivre(resultSet.getInt(3));
                    reservationDTO.setDateReservation(resultSet.getDate(4));
                }
            }
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
        return reservationDTO;
    }

    /**
     * Met à jour une réservation.
     *
     * @param reservationDTO - La réservation à mettre à jour
     * @throws DAOException - S'il y a une erreur avec la base de données
     */
    public void update(ReservationDTO reservationDTO) throws DAOException {
        try(
            PreparedStatement preparedStatement = getConnection().prepareStatement(ReservationDAO.UPDATE_REQUEST)) {
            preparedStatement.setInt(1,
                reservationDTO.getIdLivre());
            preparedStatement.setInt(2,
                reservationDTO.getIdMembre());
            preparedStatement.setDate(3,
                reservationDTO.getDateReservation());
            preparedStatement.setInt(4,
                reservationDTO.getIdReservation());
            preparedStatement.execute();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
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
            PreparedStatement preparedStatement = getConnection().prepareStatement(ReservationDAO.DELETE_REQUEST)) {
            preparedStatement.setInt(1,
                reservationDTO.getIdReservation());
            preparedStatement.execute();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     * Trouve toutes les réservations.
     *
     * @throws DAOException - S'il y a une erreur avec la base de données
     * @return La liste des réservations ; une liste vide sinon
     */
    public List<ReservationDTO> getAll() throws DAOException {
        final List<ReservationDTO> reservations = new ArrayList<>();
        try(
            PreparedStatement preparedStatement = getConnection().prepareStatement(ReservationDAO.GET_ALL_REQUEST)) {
            try(
                ResultSet resultSet = preparedStatement.executeQuery()) {

                while(resultSet.next()) {
                    final ReservationDTO reservationDTO = new ReservationDTO();
                    reservationDTO.setIdReservation(resultSet.getInt(1));
                    reservationDTO.setIdLivre(resultSet.getInt(2));
                    reservationDTO.setIdLivre(resultSet.getInt(2));
                    reservationDTO.setDateReservation(resultSet.getDate(4));

                    reservations.add(reservationDTO);
                }
            }
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
        return reservations;
    }

    /**
     * Trouve les réservations à partir d'un livre.
     *
     * @param livreDTO - Le livre à utiliser
     * @return La liste des réservations correspondantes, triée par date de réservation croissante ; une liste vide sinon
     * @throws DAOException - S'il y a une erreur avec la base de données
     */
    public List<ReservationDTO> findByLivre(LivreDTO livreDTO) throws DAOException {
        final List<ReservationDTO> reservations = new ArrayList<>();
        try(
            PreparedStatement preparedStatement = getConnection().prepareStatement(ReservationDAO.FIND_BY_LIVRE_REQUEST)) {
            preparedStatement.setInt(1,
                livreDTO.getIdLivre());
            try(
                ResultSet resultSet = preparedStatement.executeQuery()) {
                while(resultSet.next()) {
                    final ReservationDTO reservationDTO = new ReservationDTO();
                    reservationDTO.setIdReservation(resultSet.getInt(1));
                    reservationDTO.setIdLivre(resultSet.getInt(2));
                    reservationDTO.setIdLivre(resultSet.getInt(2));
                    reservationDTO.setDateReservation(resultSet.getDate(4));

                    reservations.add(reservationDTO);
                }
            }
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
        return reservations;
    }

    /**
     * Trouve les réservations à partir d'un livre.
     *
     * @param membreDTO - Le membre à utiliser
     * @return La liste des réservations correspondantes, triée par date de réservation croissante ; une liste vide sinon
     * @throws DAOException - S'il y a une erreur avec la base de données
     */
    public List<ReservationDTO> findByMembre(MembreDTO membreDTO) throws DAOException {
        final List<ReservationDTO> reservations = new ArrayList<>();
        try(
            PreparedStatement preparedStatement = getConnection().prepareStatement(ReservationDAO.FIND_BY_MEMBRE_REQUEST)) {
            preparedStatement.setInt(1,
                membreDTO.getIdMembre());
            try(
                ResultSet resultSet = preparedStatement.executeQuery()) {
                while(resultSet.next()) {
                    final ReservationDTO reservationDTO = new ReservationDTO();
                    reservationDTO.setIdReservation(resultSet.getInt(1));
                    reservationDTO.setIdLivre(resultSet.getInt(2));
                    reservationDTO.setIdLivre(resultSet.getInt(2));
                    reservationDTO.setDateReservation(resultSet.getDate(4));

                    reservations.add(reservationDTO);
                }

            }
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
        return reservations;
    }

    /*
     * Réservation d'un livre par un membre. Le livre doit être prêté.
     *
     * @param idReservation .
     * @param idLivre .
     * @param idMembre .
     * @param dateReservation .
     * @throws DAOException .
     */
    /*public void reserver(int idReservation,
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
    }*/

    /*
     * Prise d'une réservation. Le livre ne doit pas être prêté. Le
     *      membre ne doit pas avoir dépassé sa limite de pret. La réservation
     *      doit la être la première en liste.
     *
     * @param idReservation .
     * @param datePret .
     * @throws DAOException .
     */
    /*public void prendreRes(int idReservation,
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
    }*/

    /*
     * Annulation d'une réservation. La réservation doit exister.
     *
     * @param idReservation .
     * @throws DAOException .
     */
    /*public void annulerRes(int idReservation) throws DAOException {
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
    }*/
}
