// Fichier ReservationService.java
// Auteur : Adam Cherti
// Date de création : 2016-09-15

package ca.qc.collegeahuntsic.bibliotheque.service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.ReservationDTO;

/**
 *
 * Service de la table reservation
 *
 */

public class ReservationService extends Service {

    private static final long serialVersionUID = 1L;

    private PreparedStatement stmtExiste;

    private PreparedStatement stmtExisteLivre;

    private PreparedStatement stmtExisteMembre;

    private PreparedStatement stmtInsert;

    private PreparedStatement stmtDelete;

    private Connexion cx;

    /**
     *
     * Crée le service de la table reservation.
     *
     * @param cx
     * @throws SQLException
     */
    public ReservationService(final Connexion cx) throws SQLException {

        this.cx = cx;
        this.stmtExiste = cx.getConnection().prepareStatement("select idReservation, idLivre, idMembre, dateReservation "
            + "from reservation where idReservation = ?");
        this.stmtExisteLivre = cx.getConnection().prepareStatement("select idReservation, idLivre, idMembre, dateReservation "
            + "from reservation where idLivre = ? "
            + "order by dateReservation");
        this.stmtExisteMembre = cx.getConnection().prepareStatement("select idReservation, idLivre, idMembre, dateReservation "
            + "from reservation where idMembre = ? ");
        this.stmtInsert = cx.getConnection().prepareStatement("insert into reservation (idReservation, idlivre, idMembre, dateReservation) "
            + "values (?,?,?,STR_TO_DATE(?,'%Y-%m-%D'))");
        this.stmtDelete = cx.getConnection().prepareStatement("delete from reservation where idReservation = ?");
    }

    /**
     *
     * Retourne la connexion associée.
     *
     * @return la connexion à la base de données
     */
    public Connexion getConnexion() {

        return this.cx;
    }

    /**
     *
     * Vérifie si une réservation existe.
     *
     * @param idReservation l'id de la réservation
     * @return
     * @throws SQLException
     */
    public boolean existe(final int idReservation) throws SQLException {
        boolean reservationExiste;
        this.stmtExiste.setInt(1,
            idReservation);
        try(
            ResultSet rset = this.stmtExiste.executeQuery()) {
            reservationExiste = rset.next();
        }

        return reservationExiste;
    }

    /**
     *
     * Lecture d'une réservation.
     *
     * @param idReservation l'id de la réservation
     * @return
     * @throws SQLException
     */
    public ReservationDTO getReservation(final int idReservation) throws SQLException {

        this.stmtExiste.setInt(1,
            idReservation);
        try(
            ResultSet rset = this.stmtExiste.executeQuery()) {
            if(rset.next()) {
                ReservationDTO tupleReservation = new ReservationDTO();
                tupleReservation.setIdReservation(rset.getInt(1));
                tupleReservation.setIdLivre(rset.getInt(2));

                tupleReservation.setIdMembre(rset.getInt(3));
                tupleReservation.setDateReservation(rset.getDate(4));
                return tupleReservation;
            }
        }
        return null;
    }

    /**
     *
     * Lecture de la première reservation d'un livre.
     *
     * @param idLivre
     * @return
     * @throws SQLException
     */
    public ReservationDTO getReservationLivre(final int idLivre) throws SQLException {

        this.stmtExisteLivre.setInt(1,
            idLivre);
        try(
            ResultSet rset = this.stmtExisteLivre.executeQuery()) {
            if(rset.next()) {
                ReservationDTO tupleReservation = new ReservationDTO();
                tupleReservation.setIdReservation(rset.getInt(1));
                tupleReservation.setIdLivre(rset.getInt(2));

                tupleReservation.setIdMembre(rset.getInt(3));
                tupleReservation.setDateReservation(rset.getDate(4));
                return tupleReservation;
            }
        }
        return null;
    }

    /**
     *
     * Lecture de la première reservation d'un livre.
     *
     * @param idMembre l'id du membre
     * @return
     * @throws SQLException
     */
    public ReservationDTO getReservationMembre(final int idMembre) throws SQLException {

        this.stmtExisteMembre.setInt(1,
            idMembre);
        try(
            ResultSet rset = this.stmtExisteMembre.executeQuery()) {
            if(rset.next()) {
                ReservationDTO tupleReservation = new ReservationDTO();
                tupleReservation.setIdReservation(rset.getInt(1));
                tupleReservation.setIdLivre(rset.getInt(2));

                tupleReservation.setIdMembre(rset.getInt(3));
                tupleReservation.setDateReservation(rset.getDate(4));
                return tupleReservation;
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
     * @throws SQLException
     */
    public void reserver(final int idReservation,
        final int idLivre,
        final int idMembre,
        final String dateReservation) throws SQLException {
        this.stmtInsert.setInt(1,
            idReservation);
        this.stmtInsert.setInt(2,
            idLivre);
        this.stmtInsert.setInt(3,
            idMembre);
        this.stmtInsert.setString(4,
            dateReservation);
        this.stmtInsert.executeUpdate();
    }

    /**
     *
     * Suppression d'une réservation.
     *
     * @param idReservation l'id de la réservation à annuler
     * @return
     * @throws SQLException
     */
    public int annulerRes(final int idReservation) throws SQLException {
        this.stmtDelete.setInt(1,
            idReservation);
        return this.stmtDelete.executeUpdate();
    }
}
