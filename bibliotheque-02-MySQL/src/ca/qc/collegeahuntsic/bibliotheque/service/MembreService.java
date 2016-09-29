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

    private static final String QUERY_GET = "select idMembre, nom, telephone, limitePret, nbpret from membre where idmembre = ?";
    private static final String QUERY_INSERT = "insert into membre (idmembre, nom, telephone, limitepret, nbpret) " + "values (?,?,?,?,0)";
    private static final String QUERY_UPDATE_INCR_NB_PRET = "update membre set nbpret = nbPret + 1 where idMembre = ?";
    private static final String QUERY_UPDATE_DCR_NB_PRET = "update membre set nbpret = nbPret - 1 where idMembre = ?";
    private static final String QUERY_DELETE = "delete from membre where idmembre = ?";

    private Connexion connexion;

    /**
     *
     * Crée le service de la table membre.
     *
     * @param connexion connexion à la base de données
     * @throws ServiceException erreur de la base de données
     */

    public MembreService(Connexion connexion) throws ServiceException {
        this.setConnexion(connexion);
    }

    /**
     *
     * Retourne la connexion associee.
     *
     * @return la {@link java.sql.Connection} connexion à la base de données
     */
    public Connexion getConnexion() {
        return this.connexion;
    }
    
    
    /**
     * 
     * Changer la connexion à la base de données.
     *
     * @param connexion la connexion à la base de données
     */
    public void setConnexion(Connexion connexion)  {
        this.connexion = connexion;
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
        
        try(final PreparedStatement statementExiste = this.connexion.getConnection().prepareStatement(MembreService.QUERY_GET)) {
            statementExiste.setInt(1,
                idMembre);
            try(final ResultSet resultatLivreExiste = statementExiste.executeQuery();) {
                membreExiste = resultatLivreExiste.next();
            }
        } catch(SQLException sqlException) {
            throw new ServiceException(sqlException);
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
        
        try(final PreparedStatement statementExiste = this.connexion.getConnection().prepareStatement(MembreService.QUERY_GET)) {
            statementExiste.setInt(1,
                idMembre);
            try(ResultSet resultatMembreGet = statementExiste.executeQuery();) {
                if(resultatMembreGet.next()) {
                    final MembreDTO tupleMembre = new MembreDTO();
                    tupleMembre.setIdMembre(idMembre);
                    tupleMembre.setNom(resultatMembreGet.getString(2));
                    tupleMembre.setTelephone(resultatMembreGet.getLong(3));
                    tupleMembre.setLimitePret(resultatMembreGet.getInt(4));
                    tupleMembre.setNbPret(resultatMembreGet.getInt(5));
                    return tupleMembre;
                }
            }
        } catch(SQLException sqlException) {
            throw new ServiceException(sqlException);
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
        try(final PreparedStatement statementInsert = this.connexion.getConnection().prepareStatement(MembreService.QUERY_INSERT)) {
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
        try(final PreparedStatement statementUpdateIncrNbPret = this.getConnexion().getConnection().prepareStatement(MembreService.QUERY_UPDATE_INCR_NB_PRET)) {
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
        try(final PreparedStatement statementUpdateDecrNbPret = this.getConnexion().getConnection().prepareStatement(MembreService.QUERY_UPDATE_DCR_NB_PRET)) {
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
        try(final PreparedStatement statementDelete = this.getConnexion().getConnection().prepareStatement(MembreService.QUERY_DELETE)) {
            statementDelete.setInt(1,
                idMembre);
            return statementDelete.executeUpdate();
        } catch(SQLException sqlException) {
            throw new ServiceException(sqlException);
        }
    }
}
