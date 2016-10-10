// Fichier LivreDAO.java
// Auteur : Jeremi Cyr
// Date de création : 2016-09-15

package ca.qc.collegeahuntsic.bibliotheque.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
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

    private static final String FIND_BY_TITRE = "SELECT idLivre, titre, auteur, dateAcquisition, idMembre, datePret FROM livre WHERE LOWER(titre) like LOWER(?)";

    private static final String FIND_BY_MEMBRE = "SELECT idLivre, titre, auteur, dateAcquisition, idMembre, datePret FROM livre WHERE idMembre = ?";

    private static final String GET_ALL_REQUEST = "SELECT idLivre, titre, auteur, dateAcquisition, idMembre, datePret FROM livre";

    private static final String READ_REQUEST = "SELECT idLivre, titre, auteur, dateAcquisition, idMembre, datePret FROM livre WHERE idLivre = ?";

    private static final String UPDATE_REQUEST = "UPDATE livre SET titre = ?, auteur = ?, dateAcquisition= ?, idMembre = ?, datePret = ? WHERE idLivre = ?";

    private static final String EMPRUNT_REQUEST = "UPDATE livre SET titre = ?, auteur = ?, dateAcquisition= ?, idMembre = ?, datePret = CURRENT_TIMESTAMP WHERE idLivre = ?";

    private static final String RETOUR_REQUEST = "UPDATE livre SET titre = ?, auteur = ?, dateAcquisition= ?, idMembre = NULL, datePret = NULL WHERE idLivre = ?";

    /**
     * Crée un DAO à partir d'une connexion à la base de données.
     *
     * @param connexion reçoit une connexion
     */
    public LivreDAO(final Connexion connexion) {
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
     * Emprunte un livre.
     *
     * @param livreDTO Le livre à emprunter
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    public void emprunter(final LivreDTO livreDTO) throws DAOException {
        final LivreDTO realLivreDTO = this.read(livreDTO.getIdLivre());
        try(
            PreparedStatement preparedStatement = getConnection().prepareStatement(LivreDAO.EMPRUNT_REQUEST)) {
            preparedStatement.setString(1,
                realLivreDTO.getTitre());
            preparedStatement.setString(2,
                realLivreDTO.getAuteur());
            preparedStatement.setTimestamp(3,
                realLivreDTO.getDateAcquisition());
            preparedStatement.setInt(4,
                realLivreDTO.getIdMembre());
            preparedStatement.setInt(5,
                realLivreDTO.getIdLivre());
            preparedStatement.execute();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     * Retourne un livre.
     *
     * @param livreDTO  Le livre à retourner
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    public void retourner(final LivreDTO livreDTO) throws DAOException {
        final LivreDTO realLivreDTO = this.read(livreDTO.getIdLivre());
        try(
            PreparedStatement preparedStatement = getConnection().prepareStatement(LivreDAO.RETOUR_REQUEST)) {
            preparedStatement.setString(1,
                realLivreDTO.getTitre());
            preparedStatement.setString(2,
                realLivreDTO.getAuteur());
            preparedStatement.setTimestamp(3,
                realLivreDTO.getDateAcquisition());
            preparedStatement.setInt(4,
                realLivreDTO.getIdLivre());
            preparedStatement.execute();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     * Ajoute un nouveau livre.
     *
     * @param livreDTO - Le livre à ajouter
     * @throws DAOException - S'il y a une erreur avec la base de données
     */
    public void add(final LivreDTO livreDTO) throws DAOException {
        try(
            PreparedStatement preparedStatement = getConnection().prepareStatement(LivreDAO.ADD_REQUEST)) {
            preparedStatement.setInt(1,
                livreDTO.getIdLivre());
            preparedStatement.setString(2,
                livreDTO.getTitre());
            preparedStatement.setString(3,
                livreDTO.getAuteur());
            preparedStatement.setTimestamp(4,
                livreDTO.getDateAcquisition());
            if(livreDTO.getIdMembre() == 0) {
                preparedStatement.setNull(5,
                    java.sql.Types.INTEGER);
            } else {
                preparedStatement.setInt(5,
                    livreDTO.getIdMembre());
            }

            preparedStatement.setTimestamp(6,
                livreDTO.getDatePret());
            preparedStatement.execute();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     * Lit un livre.
     *
     * @param idLivre - L'ID du livre à lire
     * @throws DAOException - S'il y a une erreur avec la base de données
     * @return Le livre lu ; null sinon
     */
    public LivreDTO read(final int idLivre) throws DAOException {
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
                    livreDTO.setDateAcquisition(resultSet.getTimestamp(4));
                    livreDTO.setIdMembre(resultSet.getInt(5));
                    livreDTO.setDatePret(resultSet.getTimestamp(6));

                }

            }
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
        return livreDTO;
    }

    /**
     * Met à jour un livre.
     *
     * @param livreDTO - Le livre à mettre à jour
     * @throws DAOException - S'il y a une erreur avec la base de données
     */
    public void update(final LivreDTO livreDTO) throws DAOException {
        try(
            PreparedStatement preparedStatement = getConnection().prepareStatement(LivreDAO.UPDATE_REQUEST)) {
            preparedStatement.setString(1,
                livreDTO.getTitre());
            preparedStatement.setString(2,
                livreDTO.getAuteur());
            preparedStatement.setTimestamp(3,
                livreDTO.getDateAcquisition());
            preparedStatement.setInt(4,
                livreDTO.getIdMembre());
            preparedStatement.setTimestamp(5,
                livreDTO.getDatePret());
            preparedStatement.setInt(6,
                livreDTO.getIdLivre());
            preparedStatement.execute();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     * Supprime un livre.
     *
     * @param livreDTO - Le livre à supprimer
     * @throws DAOException - S'il y a une erreur avec la base de données
     */
    public void delete(final LivreDTO livreDTO) throws DAOException {
        try(
            PreparedStatement preparedStatement = getConnection().prepareStatement(LivreDAO.DELETE_REQUEST)) {
            preparedStatement.setInt(1,
                livreDTO.getIdLivre());
            preparedStatement.execute();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
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
                LivreDTO livreDTO = null;
                while(resultSet.next()) {
                    livreDTO = new LivreDTO();
                    livreDTO.setIdLivre(resultSet.getInt(1));
                    livreDTO.setTitre(resultSet.getString(2));
                    livreDTO.setAuteur(resultSet.getString(3));
                    livreDTO.setDateAcquisition(resultSet.getTimestamp(4));
                    livreDTO.setIdMembre(resultSet.getInt(5));
                    livreDTO.setDatePret(resultSet.getTimestamp(6));

                    livres.add(livreDTO);
                }

            }
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
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
    public List<LivreDTO> findByTitre(final String titre) throws DAOException {
        final List<LivreDTO> livres = Collections.emptyList();
        try(
            PreparedStatement preparedStatement = getConnection().prepareStatement(LivreDAO.FIND_BY_TITRE)) {
            preparedStatement.setString(1,
                "%"
                    + titre
                    + "%");
            try(
                ResultSet resultSet = preparedStatement.executeQuery()) {
                LivreDTO livreDTO = null;
                while(resultSet.next()) {
                    livreDTO = new LivreDTO();
                    livreDTO.setIdLivre(resultSet.getInt(1));
                    livreDTO.setTitre(resultSet.getString(2));
                    livreDTO.setAuteur(resultSet.getString(3));
                    livreDTO.setDateAcquisition(resultSet.getTimestamp(4));
                    livreDTO.setIdMembre(resultSet.getInt(5));
                    livreDTO.setDatePret(resultSet.getTimestamp(6));

                    livres.add(livreDTO);
                }

            }
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
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
    public List<LivreDTO> findByMembre(final MembreDTO membreDTO) throws DAOException {
        final List<LivreDTO> livres = new ArrayList<>();
        try(
            PreparedStatement preparedStatement = getConnection().prepareStatement(LivreDAO.FIND_BY_MEMBRE)) {
            preparedStatement.setInt(1,
                membreDTO.getIdMembre());
            try(
                ResultSet resultSet = preparedStatement.executeQuery()) {
                LivreDTO livreDTO = null;
                while(resultSet.next()) {
                    livreDTO = new LivreDTO();
                    livreDTO.setIdLivre(resultSet.getInt(1));
                    livreDTO.setTitre(resultSet.getString(2));
                    livreDTO.setAuteur(resultSet.getString(3));
                    livreDTO.setDateAcquisition(resultSet.getTimestamp(4));
                    livreDTO.setIdMembre(resultSet.getInt(5));
                    livreDTO.setDatePret(resultSet.getTimestamp(6));

                    livres.add(livreDTO);
                }

            }
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
        return livres;
    }

}
