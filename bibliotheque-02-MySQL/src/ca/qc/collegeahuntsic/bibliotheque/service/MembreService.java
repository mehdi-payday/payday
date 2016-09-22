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
 * @author Gilles Bénichou
 */

public class MembreService extends Service {

    private static final long serialVersionUID = 1L;

    private PreparedStatement stmtExiste;

    private PreparedStatement stmtInsert;

    private PreparedStatement stmtUpdateIncrNbPret;

    private PreparedStatement stmtUpdateDecNbPret;

    private PreparedStatement stmtDelete;

    private Connexion cx;

    /**
     *
     * Crée le service de la table membre.
     *
     * @param cx connexion à la base de données
     * @throws ServiceException erreur de la base de données
     */

    public MembreService(Connexion cx) throws ServiceException {
        try {
            this.cx = cx;
            this.stmtExiste = cx.getConnection().prepareStatement("select idMembre, nom, telephone, limitePret, nbpret from membre where idmembre = ?");
            this.stmtInsert = cx.getConnection().prepareStatement("insert into membre (idmembre, nom, telephone, limitepret, nbpret) "
                + "values (?,?,?,?,0)");
            this.stmtUpdateIncrNbPret = cx.getConnection().prepareStatement("update membre set nbpret = nbPret + 1 where idMembre = ?");
            this.stmtUpdateDecNbPret = cx.getConnection().prepareStatement("update membre set nbpret = nbPret - 1 where idMembre = ?");
            this.stmtDelete = cx.getConnection().prepareStatement("delete from membre where idmembre = ?");    
        } catch(SQLException e) {
            throw new ServiceException(e);
        }
    }

    /**
     *
     * Retourne la connexion associee.
     *
     * @return la connexion à la base de données
     */
    public Connexion getConnexion() {
        return this.cx;
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
        
        try {
            this.stmtExiste.setInt(1,
                idMembre);
            final ResultSet rset = this.stmtExiste.executeQuery();
            membreExiste = rset.next();
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
        try {
            this.stmtExiste.setInt(1,
                idMembre);
            final ResultSet rset = this.stmtExiste.executeQuery();
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
        try {
            this.stmtInsert.setInt(1,
                idMembre);
            this.stmtInsert.setString(2,
                nom);
            this.stmtInsert.setLong(3,
                telephone);
            this.stmtInsert.setInt(4,
                limitePret);
            this.stmtInsert.executeUpdate();
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
        try {
            this.stmtUpdateIncrNbPret.setInt(1,
                idMembre);
            return this.stmtUpdateIncrNbPret.executeUpdate();
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
        try {
            this.stmtUpdateDecNbPret.setInt(1,
                idMembre);
            return this.stmtUpdateDecNbPret.executeUpdate();
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
        try {
            this.stmtDelete.setInt(1,
                idMembre);
            return this.stmtDelete.executeUpdate();
        } catch(SQLException sqlException) {
            throw new ServiceException(sqlException);
        }
    }
}
