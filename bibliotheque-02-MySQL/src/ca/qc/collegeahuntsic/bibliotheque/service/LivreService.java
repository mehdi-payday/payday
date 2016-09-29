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
 * @author Adam Cherti
 */

public class LivreService extends Service {

    private static final long serialVersionUID = 1L;

    private static final String QUERY_GET = "select idlivre, titre, auteur, dateAcquisition, idMembre, datePret from livre where idlivre = ?";
    private static final String QUERY_INSERT = "insert into livre (idLivre, titre, auteur, dateAcquisition, idMembre, datePret) " + "values (?,?,?,?,null,null)";
    private static final String QUERY_UPDATE = "update livre set idMembre = ?, datePret = ? " + "where idLivre = ?";
    private static final String QUERY_DELETE = "delete from livre where idlivre = ?";

    private Connexion connexion;

    /**
     *
     * Cree le service de la table livre.
     *
     * @param connexion : La connexion à la base de données
     * @throws ServiceException s'il y a une erreur avec la base de donnees
     */
    public LivreService(final Connexion connexion) throws ServiceException {
        this.connexion = connexion;
    }

    /**
     *
     * Retourner la connexion associée.
     *
     * @return la {@link java.sql.Connection} connexion a la base de données
     */
    public Connexion getConnexion() {
        return this.connexion;
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
        ResultSet rset = null;
        try (final PreparedStatement statementExiste = this.connexion.getConnection().prepareStatement(LivreService.QUERY_GET)) {
            statementExiste.setInt(1,
                idLivre);
            rset = statementExiste.executeQuery();
            livreExiste = rset.next();
        } catch(SQLException sqlException) {
            throw new ServiceException(sqlException);
        } finally {
            try {
                rset.close();
            } catch(SQLException sqlException2) {
                throw new ServiceException(sqlException2);
            }
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
        LivreDTO livreDTO = null;
        ResultSet resultatLivre = null;
        try (final PreparedStatement statementGet = this.connexion.getConnection().prepareStatement(LivreService.QUERY_GET);) {
            statementGet.setInt(1,
                idLivre);
            resultatLivre = statementGet.executeQuery();
            if(resultatLivre.next()) {
                livreDTO = new LivreDTO();

                livreDTO.setIdLivre(idLivre);
                livreDTO.setTitre(resultatLivre.getString(2));
                livreDTO.setAuteur(resultatLivre.getString(3));
                livreDTO.setDateAcquisition(resultatLivre.getDate(4));
                livreDTO.setIdMembre(resultatLivre.getInt(5));
                livreDTO.setDatePret(resultatLivre.getDate(6));
            }
        } catch(SQLException sqlException) {
            throw new ServiceException(sqlException);
        } finally {
            try {
                resultatLivre.close();
            } catch(SQLException sqlException2) {
                throw new ServiceException(sqlException2);
            }
        }

        return livreDTO;
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
        try (final PreparedStatement statementInsert = this.connexion.getConnection().prepareStatement(LivreService.QUERY_INSERT);) {
            final LivreDTO livreExistant = this.getLivre(idLivre);
            if(livreExistant != null) {
                throw new ServiceException("L'id du livre " + idLivre + " à ajouter existe déjà : " + livreExistant.toString());
            }
            
            statementInsert.setInt(1,
                idLivre);
            statementInsert.setString(2,
                titre);
            statementInsert.setString(3,
                auteur);
            statementInsert.setDate(4,
                Date.valueOf(dateAcquisition));
            statementInsert.executeUpdate();
        } catch(SQLException sqlException) {
            throw new ServiceException(sqlException);
        }
    }

    /**
     *
     * Enregistrement de l'emprunt d'un livre.
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

        try (final PreparedStatement statementUpdate = this.connexion.getConnection().prepareStatement(LivreService.QUERY_UPDATE);) {
            statementUpdate.setInt(1,
                idMembre);
            statementUpdate.setDate(2,
                Date.valueOf(datePret));
            statementUpdate.setInt(3,
                idLivre);
            return statementUpdate.executeUpdate();
        } catch(SQLException sqlException) {
            throw new ServiceException(sqlException);
        }
    }

    /**
     *
     * Retourne un livre.
     *
     * @param idLivre l'id du livre à retourner
     * @return 1 si le livre a bien été retourné. 0 si le livre n'existe pas.
     * 
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    public int retourner(final int idLivre) throws ServiceException {
        try (final PreparedStatement statementUpdate = this.connexion.getConnection().prepareStatement(LivreService.QUERY_UPDATE);) {
            statementUpdate.setNull(1,
                Types.INTEGER);
            statementUpdate.setNull(2,
                Types.DATE);
            statementUpdate.setInt(3,
                idLivre);
            return statementUpdate.executeUpdate();
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
        try (final PreparedStatement statementDelete = this.connexion.getConnection().prepareStatement(LivreService.QUERY_DELETE);) {
            statementDelete.setInt(1,
                idLivre);
            return statementDelete.executeUpdate();
        } catch(SQLException sqlException) {
            throw new ServiceException(sqlException);
        }
    }
}
