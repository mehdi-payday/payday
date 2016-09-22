// Fichier LivreService.java
// Auteur : Adam Cherti
// Date de création : 2016-09-15

package ca.qc.collegeahuntsic.bibliotheque.service;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.LivreDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.ServiceException;

/**
 * 
 * Service de la table Livre.
 *
 * @author Gilles Bénichou
 */

public class LivreService extends Service {

    private static final long serialVersionUID = 1L;

    private PreparedStatement stmtExiste;

    private PreparedStatement stmtInsert;

    private PreparedStatement stmtUpdate;

    private PreparedStatement stmtDelete;

    private Connexion cx;

    /**
     *
     * Cree le service de lq table livre.
     *
     * @param cx : La connexion à la base de données
     * @throws ServiceException s'il y a une erreur avec la base de donnees
     */
    public LivreService(final Connexion cx) throws ServiceException {
        try {
            this.cx = cx;
            this.stmtExiste = cx.getConnection()
                .prepareStatement("select idlivre, titre, auteur, dateAcquisition, idMembre, datePret from livre where idlivre = ?");
            this.stmtInsert = cx.getConnection().prepareStatement("insert into livre (idLivre, titre, auteur, dateAcquisition, idMembre, datePret) "
                + "values (?,?,?,?,null,null)");
            this.stmtUpdate = cx.getConnection().prepareStatement("update livre set idMembre = ?, datePret = ? "
                + "where idLivre = ?");
            this.stmtDelete = cx.getConnection().prepareStatement("delete from livre where idlivre = ?");    
        } catch(SQLException sqlException) {
            throw new ServiceException(sqlException);
        }
    }
    /**
     *
     * Retourner la connexion associée.
     *
     * @return la connexion a la base de données
     */
    public Connexion getConnexion() {
        return this.cx;
    }

    /**
     *
     * Verifie si un livre existe.
     *
     * @param idLivre l'id du livre
     * @return true si le livre est existe, false sinon.
     * @throws ServiceException  S'il y a une erreur avec la base de données
     * 
     */
    public boolean existe(final int idLivre) throws ServiceException {
        boolean livreExiste = false;
        
        try {
            this.stmtExiste.setInt(1,
                    idLivre);
            final ResultSet rset = this.stmtExiste.executeQuery();
            livreExiste = rset.next();
        } catch (SQLException sqlException) {
            throw new ServiceException(sqlException);
        }

        return livreExiste;
    }

    /**
     *
     * Lecture d'un livre.
     *
     * @param idLivre id du livre a lire
     * @return un objet représentant livre, null s'il n'existe pas.
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    public LivreDTO getLivre(final int idLivre) throws ServiceException {
        LivreDTO tupleLivre = null;

        try {
            this.stmtExiste.setInt(1,
                idLivre);
            final ResultSet rset = this.stmtExiste.executeQuery();

            if(rset.next()) {
                tupleLivre = new LivreDTO();

                tupleLivre.setIdLivre(idLivre);
                tupleLivre.setTitre(rset.getString(2));
                tupleLivre.setAuteur(rset.getString(3));
                tupleLivre.setDateAcquisition(rset.getDate(4));
                tupleLivre.setIdMembre(rset.getInt(5));
                tupleLivre.setDatePret(rset.getDate(6));
            }
        } catch(SQLException sqlException) {
            throw new ServiceException(sqlException);
        }

        return tupleLivre;
    }

    /**
     *
     * Acquiert un livre.
     *
     * @param idLivre id du livre a acquerir
     * @param titre titre du livre a acquerir
     * @param auteur nom de l'auteur du livre
     * @param dateAcquisition date d'acquisition du livre
     * @throws ServiceException Si le livre existe déjà ou s'il y a une erreur avec la base de données
     */
    public void acquerir(final int idLivre,
        final String titre,
        final String auteur,
        final String dateAcquisition) throws ServiceException {
        try {
            this.stmtInsert.setInt(1,
                idLivre);
            this.stmtInsert.setString(2,
                titre);
            this.stmtInsert.setString(3,
                auteur);
            this.stmtInsert.setDate(4,
                Date.valueOf(dateAcquisition));
            this.stmtInsert.executeUpdate();
        } catch(SQLException sqlException) {
            throw new ServiceException(sqlException);
        }
    }

    /**
     *
     * Enregistrement de l'emprunteur d'un livre.
     *
     * @param idLivre le livre
     * @param idMembre le membre à qui sera prêté le livre
     * @param datePret la date du prêt
     * @return 1 si le livre a été prêté. 0 si le livre n'existe pas.
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    public int preter(final int idLivre,
        final int idMembre,
        final String datePret) throws ServiceException {
        
        try {
            this.stmtUpdate.setInt(1,
                idMembre);
            this.stmtUpdate.setDate(2,
                Date.valueOf(datePret));
            this.stmtUpdate.setInt(3,
                idLivre);
            return this.stmtUpdate.executeUpdate();
        } catch(SQLException sqlException) {
            throw new ServiceException(sqlException);
        }
    }

    /**
     *
     * Retourne un livre.
     *
     * @param idLivre l'id du livre a supprimer
     * @return 1 si le livre a bien été retourné. 0 si le livre n'existe pas.
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    public int retourner(final int idLivre) throws ServiceException {
        try {
            this.stmtUpdate.setNull(1,
                Types.INTEGER);
            this.stmtUpdate.setNull(2,
                Types.DATE);
            this.stmtUpdate.setInt(3,
                idLivre);
            return this.stmtUpdate.executeUpdate();
        } catch(SQLException sqlException) {
            throw new ServiceException(sqlException);
        }
    }

    /**
     *
     * Suppression d'un livre.
     * TODO : soulever une exception si le livre n'existe pas, si le livre a été prêté ou si le livre a été réservé
     *
     * @param idLivre l'id du livre a supprimer
     * @return 1 si le livre a bien été vendu. 0 si le livre n'existe pas.
     * @throws ServiceException  Si le livre n'existe pas, si le livre a été prêté, si le livre a été réservé ou s'il y a une erreur avec la base de données
     */
    public int vendre(final int idLivre) throws ServiceException {
        try {
            this.stmtDelete.setInt(1,
                idLivre);
            return this.stmtDelete.executeUpdate();
        } catch(SQLException sqlException) {
            throw new ServiceException(sqlException);
        }
    }
}
