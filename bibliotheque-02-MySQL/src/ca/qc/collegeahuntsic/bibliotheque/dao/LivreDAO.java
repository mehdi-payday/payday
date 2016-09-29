// Fichier LivreDAO.java
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
import ca.qc.collegeahuntsic.bibliotheque.exception.DAOException;

/**
 * DAO pour effectuer des CRUDs avec la table livre.
 *
 * @author Mehdi Hamidi
 */
public class LivreDAO extends DAO {

    private static final long serialVersionUID = 1L;

    private static final String ADD_REQUEST = "INSERT INTO livre (idLivre, titre, auteur, dateAcquisition, idMembre, datePret) VALUES(?, ?, ?, ?, ?, ?)";

    private static final String DELETE_REQUEST = "DELETE FROM livre WHERE idLivre = ?";

    private static final String EMPRUNT_REQUEST = "UPDATE livre SET titre = ?, auteur = ?, dateAcquisition= ?, idMembre = ?, datePret = CURRENT_TIMESTAMP WHERE idLivre = ?";
    
    private static final String RETOUR_REQUEST = "UPDATE livre SET titre = ?, auteur = ?, dateAcquisition= ?, idMembre = NULL, datePret = NULL WHERE idLivre = ?";

    private static final String FIND_BY_TITRE = "SELECT * FROM livre WHERE LOWER(titre) like LOWER(?)";

    private static final String FIND_BY_MEMBRE = "SELECT * FROM livre WHERE idMembre = ?";

    private static final String GET_ALL_REQUEST = "SELECT * FROM livre";

    private static final String READ_REQUEST = "SELECT * FROM livre WHERE idLivre = ?";

    private static final String UPDATE_REQUEST = "UPDATE livre SET titre = ?, auteur = ?, dateAcquisition= ?, idMembre = ?, datePret = ? WHERE idLivre = ?";

    /**
     * Crée un DAO à partir d'une connexion à la base de données.
     *
     * @param connexion reçoit une connexion
     */
    public LivreDAO(Connexion connexion) {
        super(connexion);

    }

    /*
     * Ajout d'un nouveau livre dans la base de données. S'il existe déjà, une
     *      exception est levée.
     *
     * @param idLivre id du nouveau livre
     * @param titre titre du nouveau livre
     * @param auteur auteur du nouveau libre
     * @param dateAcquisition date d'acquisition du nouveau libre
     * @throws DAOException Exception DAO levée s'il y un problème avec l'acquisition
     */
    /*
    public void acquerir(int idLivre,
        String titre,
        String auteur,
        String dateAcquisition) throws DAOException {
        try {
            if(this.livre.existe(idLivre)) {
                throw new DAOException("Livre existe deja: "
                    + idLivre);
            }

            this.livre.acquerir(idLivre,
                titre,
                auteur,
                dateAcquisition);
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
    */

    /*
     * Vente d'un livre.
     *
     * @param idLivre id du livre à vendre
     * @throws DAOException Exception DAO levée s'il y un problème avec la vente
     */
    /*
    public void vendre(int idLivre) throws DAOException {
        try {
            final LivreDTO tupleLivre = this.livre.getLivre(idLivre);
            if(tupleLivre == null) {
                throw new DAOException("Livre inexistant: "
                    + idLivre);
            }
            if(tupleLivre.getIdMembre() != 0) {
                throw new DAOException("Livre "
                    + idLivre
                    + " prete a "
                    + tupleLivre.getIdMembre());
            }
            if(this.reservation.getReservationLivre(idLivre) != null) {
                throw new DAOException("Livre "
                    + idLivre
                    + " reserve ");
            }

            final int nb = this.livre.vendre(idLivre);
            if(nb == 0) {
                throw new DAOException("Livre "
                    + idLivre
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
    */

    /**
     * Ajoute un nouveau livre.
     *
     * @param livreDTO - Le livre à ajouter
     * @throws DAOException - S'il y a une erreur avec la base de données
     */
    public void add(LivreDTO livreDTO) throws DAOException {
        try(
            PreparedStatement preparedStatement = getConnection().prepareStatement(LivreDAO.ADD_REQUEST)) {
            preparedStatement.setInt(1,
                livreDTO.getIdLivre());
            preparedStatement.setString(2,
                livreDTO.getTitre());
            preparedStatement.setString(3,
                livreDTO.getAuteur());
            preparedStatement.setDate(4,
                livreDTO.getDateAcquisition());
            preparedStatement.setInt(5,
                livreDTO.getIdMembre());
            preparedStatement.setDate(6,
                livreDTO.getDatePret());
            preparedStatement.execute();
        } catch(SQLException e) {
            throw new DAOException(e);
        }
    }

    /**
     * Lit un livre.
     *
     * @param idLivre - L'ID du livre à lire
     * @throws DAOException - S'il y a une erreur avec la base de données
     * @return Le livre lu ; null sinon
     */
    public LivreDTO read(int idLivre) throws DAOException {
        LivreDTO livreDTO = null;
        try(
            PreparedStatement preparedStatement = getConnection().prepareStatement(LivreDAO.READ_REQUEST)) {
            preparedStatement.setInt(1,
                idLivre);
            try(
                ResultSet resultSet = preparedStatement.executeQuery()) {
                if(resultSet.next()) {
                    livreDTO = new LivreDTO();
                    livreDTO.setIdLivre(resultSet.getInt(1));
                    livreDTO.setTitre(resultSet.getString(2));
                    livreDTO.setAuteur(resultSet.getString(3));
                    livreDTO.setDateAcquisition(resultSet.getDate(4));
                    livreDTO.setIdMembre(resultSet.getInt(5));
                    livreDTO.setDatePret(resultSet.getDate(6));

                }

            }
        } catch(SQLException e) {
            throw new DAOException(e);
        }
        return livreDTO;
    }

    /**
     * Met à jour un livre.
     *
     * @param livreDTO - Le livre à mettre à jour
     * @throws DAOException - S'il y a une erreur avec la base de données
     */
    public void update(LivreDTO livreDTO) throws DAOException {
        try(
            PreparedStatement preparedStatement = getConnection().prepareStatement(LivreDAO.UPDATE_REQUEST)) {
            preparedStatement.setString(1,
                livreDTO.getTitre());
            preparedStatement.setString(2,
                livreDTO.getAuteur());
            preparedStatement.setDate(3,
                livreDTO.getDateAcquisition());
            preparedStatement.setInt(4,
                livreDTO.getIdMembre());
            preparedStatement.setDate(5,
                livreDTO.getDatePret());
            preparedStatement.setInt(6,
                livreDTO.getIdLivre());
            preparedStatement.execute();
        } catch(SQLException e) {
            throw new DAOException(e);
        }
    }

    /**
     * Supprime un livre.
     *
     * @param livreDTO - Le livre à supprimer
     * @throws DAOException - S'il y a une erreur avec la base de données
     */
    public void delete(LivreDTO livreDTO) throws DAOException {
        try(
            PreparedStatement preparedStatement = getConnection().prepareStatement(LivreDAO.DELETE_REQUEST)) {
            preparedStatement.setInt(1,
                livreDTO.getIdLivre());
            preparedStatement.execute();
        } catch(SQLException e) {
            throw new DAOException(e);
        }
    }

    /**
     * Trouve tous les livres.
     *
     * @throws DAOException - S'il y a une erreur avec la base de données
     * @return La liste des livres; une liste vide sinon
     */
    public List<LivreDTO> getAll() throws DAOException {
        final List<LivreDTO> livres = new ArrayList<>();
        try(
            PreparedStatement preparedStatement = getConnection().prepareStatement(LivreDAO.GET_ALL_REQUEST)) {
            try(
                ResultSet resultSet = preparedStatement.executeQuery()) {
                while(resultSet.next()) {
                    final LivreDTO livreDTO = new LivreDTO();
                    livreDTO.setIdLivre(resultSet.getInt(1));
                    livreDTO.setTitre(resultSet.getString(2));
                    livreDTO.setAuteur(resultSet.getString(3));
                    livreDTO.setDateAcquisition(resultSet.getDate(4));
                    livreDTO.setIdMembre(resultSet.getInt(5));
                    livreDTO.setDatePret(resultSet.getDate(6));

                    livres.add(livreDTO);
                }

            }
        } catch(SQLException e) {
            throw new DAOException(e);
        }
        return livres;
    }

    /**
     * Trouve les Livres à partir d'un titre.
     *
     * @param titre - Le titre à utiliser
     * @return La liste des livres correspondants; une liste vide sinon
     * @throws DAOException - S'il y a une erreur avec la base de données
     */
    public List<LivreDTO> findByTitre(String titre) throws DAOException {
        final List<LivreDTO> livres = new ArrayList<>();
        try(
            PreparedStatement preparedStatement = getConnection().prepareStatement(LivreDAO.FIND_BY_TITRE)) {
            preparedStatement.setString(1,
                titre);
            try(
                ResultSet resultSet = preparedStatement.executeQuery()) {
                while(resultSet.next()) {
                    final LivreDTO livreDTO = new LivreDTO();
                    livreDTO.setIdLivre(resultSet.getInt(1));
                    livreDTO.setTitre(resultSet.getString(2));
                    livreDTO.setAuteur(resultSet.getString(3));
                    livreDTO.setDateAcquisition(resultSet.getDate(4));
                    livreDTO.setIdMembre(resultSet.getInt(5));
                    livreDTO.setDatePret(resultSet.getDate(6));

                    livres.add(livreDTO);
                }

            }
        } catch(SQLException e) {
            throw new DAOException(e);
        }
        return livres;
    }

    /**
     * Trouve les livres à partir d'un membre.
     *
     * @param membreDTO - Le membre à utiliser
     * @return La liste des livres correspondants; une liste vide sinon
     * @throws DAOException - S'il y a une erreur avec la base de données
     */
    public List<LivreDTO> finbByMembre(MembreDTO membreDTO) throws DAOException {
        final List<LivreDTO> livres = new ArrayList<>();
        try(
            PreparedStatement preparedStatement = getConnection().prepareStatement(LivreDAO.FIND_BY_MEMBRE)) {
            preparedStatement.setInt(1,
                membreDTO.getIdMembre());
            try(
                ResultSet resultSet = preparedStatement.executeQuery()) {
                while(resultSet.next()) {
                    final LivreDTO livreDTO = new LivreDTO();
                    livreDTO.setIdLivre(resultSet.getInt(1));
                    livreDTO.setTitre(resultSet.getString(2));
                    livreDTO.setAuteur(resultSet.getString(3));
                    livreDTO.setDateAcquisition(resultSet.getDate(4));
                    livreDTO.setIdMembre(resultSet.getInt(5));
                    livreDTO.setDatePret(resultSet.getDate(6));

                    livres.add(livreDTO);
                }

            }
        } catch(SQLException e) {
            throw new DAOException(e);
        }
        return livres;
    }

}

