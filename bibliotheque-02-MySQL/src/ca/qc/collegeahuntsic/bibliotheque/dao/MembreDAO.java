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
import ca.qc.collegeahuntsic.bibliotheque.exception.ConnexionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.DAOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.ServiceException;
import ca.qc.collegeahuntsic.bibliotheque.service.MembreService;
import ca.qc.collegeahuntsic.bibliotheque.service.ReservationService;

/**
 * DAO pour effectuer des CRUDs avec la table membre.
 *
 * @author Mehdi Hamidi
 */

public class MembreDAO extends DAO {

    private static final long serialVersionUID = 1L;

    private static final String ADD_REQUEST = "INSERT INTO membre (idMembre, nom, telephone, limitePret, nbPret) VALUES(?, ?, ?, ?, ?)";

    private static final String DELETE_REQUEST = "DELETE FROM membre WHERE idMembre = ?";

    private static final String GET_ALL_REQUEST = "SELECT * FROM membre";

    private static final String READ_REQUEST = "SELECT * FROM membre WHERE idMembre= ?";

    private static final String UPDATE_REQUEST = "UPDATE membre SET idMembre = ?, nom = ?, telephone = ?, limitePret = ?, nbPret = ? WHERE idMembre = ?";

    private Connexion connexion;

    private MembreService membre;

    private ReservationService reservation;

    /**
     * Crée un DAO à partir d'une connexion à la base de données.
     *
     * @param membre {@link ca.qc.collegeahuntsic.bibliotheque.service.MembreService} object.
     * @param reservation {@link ca.qc.collegeahuntsic.bibliotheque.service.ReservationService} object.
     */
    public MembreDAO(MembreService membre,
        ReservationService reservation) {
        super(membre.getConnexion());
        this.connexion = membre.getConnexion();
        this.membre = membre;
        this.reservation = reservation;
    }

    /**
     * Ajout d'un nouveau membre dans la base de donnees. S'il existe deja, une
     *      exception est levee.
     *
     * @param idMembre id du membre.
     * @param nom nom du membre.
     * @param telephone telephone du membre.
     * @param limitePret la limite de pret du membre.
     * @throws DAOException Exception specifique au package contenant les differentes exceptions levees.
     */
    public void inscrire(int idMembre,
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
    }

    /**
     * Suppression d'un membre de la base de donnees.
     *
     * @param idMembre id du membre a identifier.
     * @throws DAOException Exception specifique au package contenant les differentes exceptions levees.
     */
    public void desinscrire(int idMembre) throws DAOException {
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
    }

    /**
     * Ajoute un nouveau membre.
     *
     * @param membreDTO - Le membre à ajouter
     * @throws DAOException - S'il y a une erreur avec la base de données
     */
    public void add(MembreDTO membreDTO) throws DAOException {
        try(
            PreparedStatement preparedStatement = this.connexion.getConnection().prepareStatement(ADD_REQUEST)) {
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
        } catch(SQLException e) {
            throw new DAOException(e);
        }
    }

    /**
     * Lit un membre. Si aucun membre n'est trouvé, null est retourné.
     *
     * @param idMembre - L'ID du membre à lire
     * @throws DAOException - S'il y a une erreur avec la base de données
     * @return Le membrelu ; null sinon
     */
    public MembreDTO read(int idMembre) throws DAOException {
        try(
            PreparedStatement preparedStatement = this.connexion.getConnection().prepareStatement(READ_REQUEST)) {
            preparedStatement.setInt(1,
                idMembre);

            try(
                ResultSet resultSet = preparedStatement.executeQuery()) {
                if(resultSet.next()) {
                    final MembreDTO membreDTO = new MembreDTO();
                    membreDTO.setIdMembre(resultSet.getInt(1));
                    membreDTO.setNom(resultSet.getString(2));
                    membreDTO.setTelephone(resultSet.getLong(3));
                    membreDTO.setLimitePret(resultSet.getInt(4));
                    membreDTO.setNbPret(resultSet.getInt(5));

                    return membreDTO;
                }

                throw new DAOException("Le membre avec l'id "
                    + idMembre
                    + " n'existe pas.");
            }
        } catch(SQLException e) {
            throw new DAOException(e);
        }
    }

    /**
     * Met à jour un membre.
     *
     * @param membreDTO - Le membre à mettre à jour
     * @throws DAOException - S'il y a une erreur avec la base de données
     */
    public void update(MembreDTO membreDTO) throws DAOException {
        try(
            PreparedStatement preparedStatement = this.connexion.getConnection().prepareStatement(UPDATE_REQUEST)) {
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
            preparedStatement.setInt(6,
                membreDTO.getIdMembre());

            preparedStatement.execute();
        } catch(SQLException e) {
            throw new DAOException(e);
        }
    }

    /**
     * Supprime un membre.
     *
     * @param membreDTO - Le membre à supprimer
     * @throws DAOException - S'il y a une erreur avec la base de données
     */
    public void delete(MembreDTO membreDTO) throws DAOException {
        try(
            PreparedStatement preparedStatement = this.connexion.getConnection().prepareStatement(DELETE_REQUEST)) {
            preparedStatement.setInt(1,
                membreDTO.getIdMembre());
            preparedStatement.execute();
        } catch(SQLException e) {
            throw new DAOException(e);
        }
    }

    /**
     * Trouve tous les membres.
     *
     * @throws DAOException - S'il y a une erreur avec la base de données
     * @return La liste des membres; une liste vide sinon
     */
    public List<MembreDTO> getAll() throws DAOException {
        try(
            PreparedStatement preparedStatement = this.connexion.getConnection().prepareStatement(GET_ALL_REQUEST)) {
            try(
                ResultSet resultSet = preparedStatement.executeQuery()) {
                final List<MembreDTO> membres = new ArrayList<>();
                while(resultSet.next()) {
                    final MembreDTO membreDTO = new MembreDTO();
                    membreDTO.setIdMembre(resultSet.getInt(1));
                    membreDTO.setNom(resultSet.getString(2));
                    membreDTO.setTelephone(resultSet.getLong(3));
                    membreDTO.setLimitePret(resultSet.getInt(4));
                    membreDTO.setNbPret(resultSet.getInt(5));

                    membres.add(membreDTO);
                }
                return membres;
            }
        } catch(SQLException e) {
            throw new DAOException(e);
        }
    }

}
