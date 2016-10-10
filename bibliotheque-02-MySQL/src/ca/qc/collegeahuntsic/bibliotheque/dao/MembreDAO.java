// Fichier MembreDAO.java
// Auteur : Jeremi Cyr
// Date de création : 2016-09-15

package ca.qc.collegeahuntsic.bibliotheque.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.MembreDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.DAOException;

/**
 * DAO pour effectuer des CRUDs avec la table membre.
 *
 * @author Mehdi Hamidi
 */

public class MembreDAO extends DAO {

    private static final long serialVersionUID = 1L;

    private static final String ADD_REQUEST = "INSERT INTO membre (idMembre, nom, telephone, limitePret, nbPret) VALUES(?, ?, ?, ?, ?)";

    private static final String DELETE_REQUEST = "DELETE FROM membre WHERE idMembre = ?";

    private static final String GET_ALL_REQUEST = "SELECT idMembre, nom, telephone, limitePret, nbpret FROM membre";

    private static final String READ_REQUEST = "SELECT idMembre, nom, telephone, limitePret, nbpret FROM membre WHERE idMembre= ?";

    private static final String UPDATE_REQUEST = "UPDATE membre SET nom = ?, telephone = ?, limitePret = ?, nbPret = ? WHERE idMembre = ?";

    /**
     * Crée un DAO à partir d'une connexion à la base de données.
     *
     * @param connexion {@link java.sql.connexion} object.
     */
    public MembreDAO(final Connexion connexion) {
        super(connexion);

    }

    /*
     * Ajout d'un nouveau membre dans la base de donnees. S'il existe deja, une
     *      exception est levee.
     *
     * @param idMembre id du membre.
     * @param nom nom du membre.
     * @param telephone telephone du membre.
     * @param limitePret la limite de pret du membre.
     * @throws DAOException Exception specifique au package contenant les differentes exceptions levees.
     */
    /*public void inscrire(int idMembre,
        String nom,
        long telephone,
        int limitePret) throws DAOException {
        try {
            if(this.membre.existe(idMembre)) {
                throw new DAOException("Membre existe deja: "
                    + idMembre);
            }
            this.membre.inscrire(idMembre,
                nom,
                telephone,
                limitePret);
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
     * Suppression d'un membre de la base de donnees.
     *
     * @param idMembre id du membre a identifier.
     * @throws DAOException Exception specifique au package contenant les differentes exceptions levees.
     */
    /*public void desinscrire(int idMembre) throws DAOException {
        try {
            final MembreDTO tupleMembre = this.membre.getMembre(idMembre);
            if(tupleMembre == null) {
                throw new DAOException("Membre inexistant: "
                    + idMembre);
            }
            if(tupleMembre.getNbPret() > 0) {
                throw new DAOException("Le membre "
                    + idMembre
                    + " a encore des prets.");
            }
            if(this.reservation.getReservationMembre(idMembre) != null) {
                throw new DAOException("Membre "
                    + idMembre
                    + " a des réservations");
            }
    
            final int nb = this.membre.desinscrire(idMembre);
            if(nb == 0) {
                throw new DAOException("Membre "
                    + idMembre
                    + " inexistant");
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

    /**
     * Ajoute un nouveau membre.
     *
     * @param membreDTO - Le membre à ajouter
     * @throws DAOException - S'il y a une erreur avec la base de données
     */
    public void add(final MembreDTO membreDTO) throws DAOException {
        try(
            PreparedStatement preparedStatement = getConnection().prepareStatement(MembreDAO.ADD_REQUEST)) {
            preparedStatement.setInt(1,
                membreDTO.getIdMembre());
            preparedStatement.setString(2,
                membreDTO.getNom());
            preparedStatement.setLong(3,
                membreDTO.getTelephone());
            preparedStatement.setInt(4,
                membreDTO.getLimitePret());
            preparedStatement.setInt(5,
                membreDTO.getNbPret());
            preparedStatement.execute();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     * Lit un membre. Si aucun membre n'est trouvé, null est retourné.
     *
     * @param idMembre - L'ID du membre à lire
     * @throws DAOException - S'il y a une erreur avec la base de données
     * @return Le membrelu ; null sinon
     */
    public MembreDTO read(final int idMembre) throws DAOException {
        MembreDTO membreDTO = null;
        try(
            PreparedStatement preparedStatement = getConnection().prepareStatement(MembreDAO.READ_REQUEST)) {
            preparedStatement.setInt(1,
                idMembre);
            try(
                ResultSet resultSet = preparedStatement.executeQuery()) {
                if(resultSet.next()) {
                    membreDTO = new MembreDTO();
                    membreDTO.setIdMembre(resultSet.getInt(1));
                    membreDTO.setNom(resultSet.getString(2));
                    membreDTO.setTelephone(resultSet.getLong(3));
                    membreDTO.setLimitePret(resultSet.getInt(4));
                    membreDTO.setNbPret(resultSet.getInt(5));
                }
            }
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
        return membreDTO;
    }

    /**
     * Met à jour un membre.
     *
     * @param membreDTO - Le membre à mettre à jour
     * @throws DAOException - S'il y a une erreur avec la base de données
     */
    public void update(final MembreDTO membreDTO) throws DAOException {
        try(
            PreparedStatement preparedStatement = getConnection().prepareStatement(MembreDAO.UPDATE_REQUEST)) {
            preparedStatement.setString(1,
                membreDTO.getNom());
            preparedStatement.setLong(2,
                membreDTO.getTelephone());
            preparedStatement.setInt(3,
                membreDTO.getLimitePret());
            preparedStatement.setInt(4,
                membreDTO.getNbPret());
            preparedStatement.setInt(5,
                membreDTO.getIdMembre());

            preparedStatement.execute();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     * Supprime un membre.
     *
     * @param membreDTO - Le membre à supprimer
     * @throws DAOException - S'il y a une erreur avec la base de données
     */
    public void delete(final MembreDTO membreDTO) throws DAOException {
        try(
            PreparedStatement preparedStatement = getConnection().prepareStatement(MembreDAO.DELETE_REQUEST)) {
            preparedStatement.setInt(1,
                membreDTO.getIdMembre());
            preparedStatement.execute();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     * Trouve tous les membres.
     *
     * @throws DAOException - S'il y a une erreur avec la base de données
     * @return La liste des membres; une liste vide sinon
     */
    public List<MembreDTO> getAll() throws DAOException {
        final List<MembreDTO> membres = new ArrayList<>();
        try(
            PreparedStatement preparedStatement = getConnection().prepareStatement(MembreDAO.GET_ALL_REQUEST)) {
            try(
                ResultSet resultSet = preparedStatement.executeQuery()) {
                while(resultSet.next()) {
                    final MembreDTO membreDTO = new MembreDTO();
                    membreDTO.setIdMembre(resultSet.getInt(1));
                    membreDTO.setNom(resultSet.getString(2));
                    membreDTO.setTelephone(resultSet.getLong(3));
                    membreDTO.setLimitePret(resultSet.getInt(4));
                    membreDTO.setNbPret(resultSet.getInt(5));

                    membres.add(membreDTO);
                }
            }
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
        return membres;
    }

}
