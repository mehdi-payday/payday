// Fichier MembreService.java
// Auteur : Adam Cherti
// Date de création : 2016-09-15

package ca.qc.collegeahuntsic.bibliotheque.service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.MembreDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.ServiceException;

/**
 *
 * Service de la table Membre.
 *
 * @author Adam Cherti
 */

public class MembreService extends Service {

    private static final long serialVersionUID = 1L;

    private final String queryGet = "select idMembre, nom, telephone, limitePret, nbpret from membre where idmembre = ?";
    private final String queryInsert = "insert into membre (idmembre, nom, telephone, limitepret, nbpret) " + "values (?,?,?,?,0)";
    private final String queryUpdateIncrNbPret = "update membre set nbpret = nbPret + 1 where idMembre = ?";
    private final String queryUpdateDecNbPret = "update membre set nbpret = nbPret - 1 where idMembre = ?";
    private final String queryDelete = "delete from membre where idmembre = ?";

    private Connexion connexion;

    /**
     *
     * Crée le service de la table membre.
     *
     * @param connexion connexion à la base de données
     * @throws ServiceException erreur de la base de données
     */

    public MembreService(Connexion connexion) throws ServiceException {
        this.connexion = connexion;
    }

    /**
     *
     * Retourne la connexion associee.
     *
     * @return la connexion à la base de données
     */
    public Connexion getConnexion() {
        return this.connexion;
    }

    /**
     *
     * Verifie si un membre existe.
     *
     * @param idMembre l'id du membre
     * @return true si le membre existe, false sinon
     * @throws ServiceException s'il y a une erreur de la base de données
     */
    public boolean existe(int idMembre) throws ServiceException {
        boolean membreExiste = false;
        ResultSet rset = null;
        try(final PreparedStatement statementExiste = this.connexion.getConnection().prepareStatement(this.queryGet)) {
            statementExiste.setInt(1,
                idMembre);
            rset = statementExiste.executeQuery();
            membreExiste = rset.next();
        } catch(SQLException sqlException) {
            throw new ServiceException(sqlException);
        } finally {
            try {
                rset.close();
            } catch(SQLException sqlException2) {
                throw new ServiceException(sqlException2);
            }
        }

        return membreExiste;
    }

    /**
     *
     * Lecture d'un membre.
     *
     * @param idMembre l'id du membre
     * @return l'objet représentant le membre, null s'il n'existe pas.
     * @throws ServiceException s'il y a une erreur avec la base de données
     */
    public MembreDTO getMembre(int idMembre) throws ServiceException {
        ResultSet rset = null;
        try(final PreparedStatement statementExiste = this.connexion.getConnection().prepareStatement(this.queryGet)) {
            statementExiste.setInt(1,
                idMembre);
            rset = statementExiste.executeQuery();
            if(rset.next()) {

                final MembreDTO tupleMembre = new MembreDTO();
                tupleMembre.setIdMembre(idMembre);
                tupleMembre.setNom(rset.getString(2));
                tupleMembre.setTelephone(rset.getLong(3));
                tupleMembre.setLimitePret(rset.getInt(4));
                tupleMembre.setNbPret(rset.getInt(5));
                return tupleMembre;
            }
        } catch(SQLException sqlException) {
            throw new ServiceException(sqlException);
        } finally {
            try {
                rset.close();
            } catch(SQLException sqlException2) {
                throw new ServiceException(sqlException2);
            }
        }
        return null;
    }

    /**
     *
     * Ajout d'un nouveau membre.
     *
     * @param idMembre l'id du nouveau membre
     * @param nom le nom du membre
     * @param telephone le numéro de téléphone
     * @param limitePret le nombre de prêts maximums auquel le membre a droit
     * @throws ServiceException s'il y a une erreur avec la base de données
     */
    public void inscrire(int idMembre,
        String nom,
        long telephone,
        int limitePret) throws ServiceException {
        try(final PreparedStatement statementInsert = this.connexion.getConnection().prepareStatement(this.queryInsert)) {
            statementInsert.setInt(1,
                idMembre);
            statementInsert.setString(2,
                nom);
            statementInsert.setLong(3,
                telephone);
            statementInsert.setInt(4,
                limitePret);
            statementInsert.executeUpdate();
        } catch(SQLException sqlException) {
            throw new ServiceException(sqlException);
        }
    }

    /**
     *
     * Emprunte un livre.
     *
     * @param idMembre l'id du membre qui emprunte
     * @return 1 si le nombre de prêts du membre a été incrémenté, 0 si le membre n'existe pas
     * @throws ServiceException s'il y a une erreur dans la base de données
     */
    public int preter(int idMembre) throws ServiceException {
        try(final PreparedStatement statementUpdateIncrNbPret = this.connexion.getConnection().prepareStatement(this.queryUpdateIncrNbPret)) {
            statementUpdateIncrNbPret.setInt(1,
                idMembre);
            return statementUpdateIncrNbPret.executeUpdate();
        } catch(SQLException sqlException) {
            throw new ServiceException(sqlException);
        }
    }

    /**
     *
     * Désinscrire un membre.
     *
     * @param idMembre l'id du membre qui emprunte
     * @return 1 si le nombre de prêts du membre a été décrémenté, 0 si le membre n'existe pas
     * @throws ServiceException s'il y a une erreur avec la base de données
     */
    public int retourner(int idMembre) throws ServiceException {
        try(final PreparedStatement statementUpdateDecrNbPret = this.connexion.getConnection().prepareStatement(this.queryUpdateDecNbPret)) {
            statementUpdateDecrNbPret.setInt(1,
                idMembre);
            return statementUpdateDecrNbPret.executeUpdate();
        } catch(SQLException sqlException) {
            throw new ServiceException(sqlException);
        }
    }

    /**
     *
     * Suppression d'un membre.
     * TODO : soulever une exception si le membre a encore de prêts / réservations
     *
     * @param idMembre l'id du membre qui emprunte
     * @return 1 si le membre a bien été supprimé. 0 si il n'existe pas.
     * @throws ServiceException Si le membre a encore des prêts, s'il a des réservations ou s'il y a une erreur avec la base de données
     */
    public int desinscrire(int idMembre) throws ServiceException {
        try(final PreparedStatement statementDelete = this.connexion.getConnection().prepareStatement(this.queryDelete)) {
            statementDelete.setInt(1,
                idMembre);
            return statementDelete.executeUpdate();
        } catch(SQLException sqlException) {
            throw new ServiceException(sqlException);
        }
    }
}
