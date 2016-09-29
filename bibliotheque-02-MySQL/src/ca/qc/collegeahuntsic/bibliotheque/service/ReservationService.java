// Fichier ReservationService.java
// Auteur : Adam Cherti
// Date de création : 2016-09-15

package ca.qc.collegeahuntsic.bibliotheque.service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.ReservationDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.ServiceException;

/**
 *
 * Service de la table reservation.
 *
 * @author Adam Cherti
 */
public class ReservationService extends Service {
    private static final long serialVersionUID = 1L;

    private final String queryGet = "select idReservation, idLivre, idMembre, dateReservation "
        + "from reservation where idReservation = ?";
    private final String queryExisteLivre = "select idReservation, idLivre, idMembre, dateReservation "
        + "from reservation where idLivre = ? "
        + "order by dateReservation";
    private final String queryExisteMembre = "select idReservation, idLivre, idMembre, dateReservation " + "from reservation where idMembre = ? ";
    private final String queryInsert = "insert into reservation (idReservation, idlivre, idMembre, dateReservation) " + "values (?,?,?,STR_TO_DATE(?,'%Y-%m-%D'))";
    private final String queryDelete = "delete from reservation where idReservation = ?";

    private Connexion connexion;

    /**
     *
     * Crée le service de la table reservation.
     *
     * @param connexion la connexion a la base données
     * @throws ServiceException s'il y a une erreur avec la base de données
     */
    public ReservationService(final Connexion connexion) throws ServiceException {
        this.connexion = connexion;
    }

    /**
     *
     * Retourne la connexion associée.
     *
     * @return la connexion à la base de données
     */
    public Connexion getConnexion() {

        return this.connexion;
    }

    /**
     *
     * Vérifie si une réservation existe.
     *
     * @param idReservation l'id de la réservation
     * @return true si la réservation existe, false sinon.
     * @throws ServiceException s'il y a une erreur avec la base de données
     */
    public boolean existe(final int idReservation) throws ServiceException {
        try (final PreparedStatement statementExiste = this.connexion.getConnection().prepareStatement(this.queryGet);) {
            boolean reservationExiste = false;

            statementExiste.setInt(1,
                idReservation);
            try(
                ResultSet rset = statementExiste.executeQuery()) {
                reservationExiste = rset.next();
            }
            return reservationExiste;
        } catch(SQLException e) {
            throw new ServiceException(e);
        }
    }

    /**
     *
     * Lecture d'une réservation par id.
     *
     * @param idReservation l'id de la réservation
     * @return l'objet représentant la réservation, null si cela n'existe pas.
     * @throws ServiceException s'il y a une erreur avec la base de données
     */
    public ReservationDTO getReservation(final int idReservation) throws ServiceException {
        ResultSet resultatReservationListe = null;
        try (final PreparedStatement statementExiste = this.connexion.getConnection().prepareStatement(this.queryGet);) {
            statementExiste.setInt(1,
                idReservation);
            resultatReservationListe = statementExiste.executeQuery();
            if(resultatReservationListe.next()) {
                final ReservationDTO tupleReservation = new ReservationDTO();
                tupleReservation.setIdReservation(resultatReservationListe.getInt(1));
                tupleReservation.setIdLivre(resultatReservationListe.getInt(2));

                tupleReservation.setIdMembre(resultatReservationListe.getInt(3));
                tupleReservation.setDateReservation(resultatReservationListe.getDate(4));
                return tupleReservation;
            }
        } catch(SQLException e) {
            throw new ServiceException(e);
        } finally {
            try {
                resultatReservationListe.close();
            } catch(SQLException SqlException) {
                throw new ServiceException(SqlException);
            }
        }
        return null;
    }

    /**
     *
     * Lecture de la première reservation d'un livre.
     *
     * @param idLivre l'id du livre
     * @return l'objet de la premiere reservation du livre, null si cela n'existe pas.
     * @throws ServiceException s'il y a une erreur avec la base de données
     */
    public ReservationDTO getReservationLivre(final int idLivre) throws ServiceException {
        ResultSet resultatReservationGet = null;
        try (final PreparedStatement stmtExisteLivre = this.connexion.getConnection().prepareStatement(this.queryExisteLivre);) {
            stmtExisteLivre.setInt(1,
                idLivre);

            resultatReservationGet = stmtExisteLivre.executeQuery();
            if(resultatReservationGet.next()) {
                final ReservationDTO tupleReservation = new ReservationDTO();
                tupleReservation.setIdReservation(resultatReservationGet.getInt(1));
                tupleReservation.setIdLivre(resultatReservationGet.getInt(2));

                tupleReservation.setIdMembre(resultatReservationGet.getInt(3));
                tupleReservation.setDateReservation(resultatReservationGet.getDate(4));
                return tupleReservation;
            }
        } catch(SQLException sqlException) {
            throw new ServiceException(sqlException);
        } finally {
            try {
                resultatReservationGet.close();
            } catch(SQLException sqlException2) {
                throw new ServiceException(sqlException2);
            }
        }
        return null;
    }

    /**
     *
     * Lecture de la première reservation d'un livre.
     *
     * @param idMembre l'id du membre
     * @return la premiere reservation du membrem, null si cela n'existe pas.
     * @throws ServiceException s'il y a une erreur avec la base de données
     */
    public ReservationDTO getReservationMembre(final int idMembre) throws ServiceException {
        ResultSet resultatReservationGet = null;
        try (final PreparedStatement statementExisteMembre = this.connexion.getConnection().prepareStatement(this.queryExisteMembre);) {
            statementExisteMembre.setInt(1,
                idMembre);

            resultatReservationGet = statementExisteMembre.executeQuery();
            if(resultatReservationGet.next()) {
                final ReservationDTO tupleReservation = new ReservationDTO();
                tupleReservation.setIdReservation(resultatReservationGet.getInt(1));
                tupleReservation.setIdLivre(resultatReservationGet.getInt(2));

                tupleReservation.setIdMembre(resultatReservationGet.getInt(3));
                tupleReservation.setDateReservation(resultatReservationGet.getDate(4));
                return tupleReservation;
            }
        } catch(SQLException sqlException) {
            throw new ServiceException(sqlException);
        } finally {
            try {
                resultatReservationGet.close();
            } catch(SQLException sqlException2) {
                throw new ServiceException(sqlException2);
            }
        }

        return null;
    }

    /**
     *
     * Réservation d'un livre.
     *
     * @param idReservation l'id de la réservation à créer
     * @param idLivre l'id du livre à réserver
     * @param idMembre l'id du membre à qui sera réservé le livre
     * @param dateReservation date de la réservation
     * @throws ServiceException Si la réservation existe déjà, si le membre n'existe pas, si le livre n'existe pas, si le livre n'a pas encore été prêté, si le livre est déjà prêté au membre, si le membre a déjà réservé ce livre ou s'il y a une erreur avec la base de données
     */
    public void reserver(final int idReservation,
        final int idLivre,
        final int idMembre,
        final String dateReservation) throws ServiceException {
        try (final PreparedStatement statementInsert = this.connexion.getConnection().prepareStatement(this.queryInsert);) {
            statementInsert.setInt(1,
                idReservation);
            statementInsert.setInt(2,
                idLivre);
            statementInsert.setInt(3,
                idMembre);
            statementInsert.setString(4,
                dateReservation);
            statementInsert.executeUpdate();
        } catch(SQLException e) {
            throw new ServiceException(e);
        }
    }

    /**
     *
     * Suppression d'une réservation.
     *
     * @param idReservation l'id de la réservation à annuler
     * @return nombre d'entrées supprimés. Si 0, alors rien n'a été supprimé (car cela n'existe pas).
     * Si plus que 0, cela veut dire que la reservation a bien été annulée.
     * @throws ServiceException Si la réservation n'existe pas ou s'il y a une erreur avec la base de données
     */
    public int annulerRes(final int idReservation) throws ServiceException {
        try (final PreparedStatement statementDelete = this.connexion.getConnection().prepareStatement(this.queryDelete);) {
            statementDelete.setInt(1,
                idReservation);
            return statementDelete.executeUpdate();
        } catch(SQLException e) {
            throw new ServiceException(e);
        }
    }
}
