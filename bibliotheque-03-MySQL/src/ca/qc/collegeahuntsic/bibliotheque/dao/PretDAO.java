// Fichier PretDAO.java
// Auteur : Alexandre Barone
// Date de création : 2016-05-18

package ca.qc.collegeahuntsic.bibliotheque.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.PretDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.DAOException;

/**
 * DAO pour effectuer des CRUDs avec la table <code>pret</code>.
 *
 * @author Alexandre Barone
 */
public class PretDAO extends DAO {
    private static final long serialVersionUID = 1L;

    private static final String ADD_REQUEST = "INSERT INTO pret (idMembre, "
        + "                                                      idLivre, "
        + "                                                      datePret, "
        + "                                                      dateRetour) "
        + "                                    VALUES           (?, "
        + "                                                      ?, "
        + "                                                      ?, "
        + "                                                      ?)";

    private static final String READ_REQUEST = "SELECT idMembre, "
        + "                                            idLivre, "
        + "                                            datePret, "
        + "                                            dateRetour) "
        + "                                     FROM   pret "
        + "                                     WHERE  idPret = ?";

    private static final String UPDATE_REQUEST = "UPDATE pret "
        + "                                       SET    idMembre = ?, "
        + "                                              idLivre = ?, "
        + "                                              datePret = ?, "
        + "                                              dateRetour = ? "
        + "                                       WHERE  idPret = ?";

    private static final String DELETE_REQUEST = "DELETE FROM pret "
        + "                                       WHERE       idPret = ?";

    private static final String GET_ALL_REQUEST = "SELECT idPret, "
        + "                                               idMembre, "
        + "                                               idLivre, "
        + "                                               datePret, "
        + "                                               dateRetour) "
        + "                                        FROM   pret";

    private static final String FIND_BY_MEMBRE = "SELECT idPret, "
        + "                                              idMembre, "
        + "                                              idLivre, "
        + "                                              datePret, "
        + "                                              dateRetour) "
        + "                                       FROM   pret "
        + "                                       WHERE  idMembre = ?";

    /**
     * Crée un DAO à partir d'une connexion à la base de données.
     *
     * @param connexion La connexion à utiliser
     */
    public PretDAO(Connexion connexion) {
        super(connexion);
    }

    /**
     * Ajoute un nouveau pret.
     *
     * @param pretDTO Le pret à ajouter
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    public void add(PretDTO pretDTO) throws DAOException {
        try(
            PreparedStatement addPreparedStatement = getConnection().prepareStatement(PretDAO.ADD_REQUEST)) {
            addPreparedStatement.setInt(1,
                pretDTO.getIdMembre());
            addPreparedStatement.setInt(2,
                pretDTO.getIdLivre());
            addPreparedStatement.setTimestamp(3,
                pretDTO.getDatePret());
            addPreparedStatement.setTimestamp(4,
                pretDTO.getDateRetour());
            addPreparedStatement.executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     * Lit un pret. Si aucun pret n'est trouvé, <code>null</code> est retourné.
     *
     * @param idPret L'ID du pret à lire
     * @return Le pret lu ; <code>null</code> sinon
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    public PretDTO read(int idPret) throws DAOException {
        PretDTO pretDTO = null;
        try(
            PreparedStatement readPreparedStatement = getConnection().prepareStatement(PretDAO.READ_REQUEST)) {
            readPreparedStatement.setInt(1,
                idPret);
            try(
                ResultSet resultSet = readPreparedStatement.executeQuery()) {
                if(resultSet.next()) {
                    pretDTO = new PretDTO();
                    pretDTO.setIdPret(resultSet.getInt(1));
                    pretDTO.setIdMembre(resultSet.getInt(2));
                    pretDTO.setIdLivre(resultSet.getInt(3));
                    pretDTO.setDatePret(resultSet.getTimestamp(4));
                    pretDTO.setDateRetour(resultSet.getTimestamp(5));
                }
            }
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
        return pretDTO;
    }

    /**
     * Met à jour un pret.
     *
     * @param pretDTO Le pret à mettre à jour
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    public void update(PretDTO pretDTO) throws DAOException {
        try(
            PreparedStatement updatePreparedStatement = getConnection().prepareStatement(PretDAO.UPDATE_REQUEST)) {
            updatePreparedStatement.setInt(1,
                pretDTO.getIdMembre());
            updatePreparedStatement.setInt(2,
                pretDTO.getIdLivre());
            updatePreparedStatement.setTimestamp(3,
                pretDTO.getDatePret());
            updatePreparedStatement.setTimestamp(4,
                pretDTO.getDateRetour());
            updatePreparedStatement.executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     * Supprime un pret.
     *
     * @param pretDTO Le pret à supprimer
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    public void delete(PretDTO pretDTO) throws DAOException {
        try(
            PreparedStatement deletePreparedStatement = getConnection().prepareStatement(PretDAO.DELETE_REQUEST)) {
            deletePreparedStatement.setInt(1,
                pretDTO.getIdPret());
            deletePreparedStatement.executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     * Trouve tous les prets.
     *
     * @return La liste des prets ; une liste vide sinon
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    public List<PretDTO> getAll() throws DAOException {
        List<PretDTO> prets = Collections.emptyList();
        try(
            PreparedStatement getAllPreparedStatement = getConnection().prepareStatement(PretDAO.GET_ALL_REQUEST)) {
            try(
                ResultSet resultSet = getAllPreparedStatement.executeQuery()) {
                PretDTO pretDTO = null;
                if(resultSet.next()) {
                    prets = new ArrayList<>();
                    do {
                        pretDTO = new PretDTO();
                        pretDTO.setIdPret(resultSet.getInt(1));
                        pretDTO.setIdMembre(resultSet.getInt(2));
                        pretDTO.setIdLivre(resultSet.getInt(3));
                        pretDTO.setDatePret(resultSet.getTimestamp(4));
                        pretDTO.setDateRetour(resultSet.getTimestamp(5));
                        prets.add(pretDTO);
                    } while(resultSet.next());
                }
            }
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
        return prets;
    }

    /**
     * Trouve les prets à partir d'un membre.
     *
     * @param idMembre L'ID du membre à utiliser
     * @return La liste des prets correspondants ; une liste vide sinon
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    public List<PretDTO> findByMembre(int idMembre) throws DAOException {
        List<PretDTO> prets = Collections.emptyList();
        try(
            PreparedStatement findByMembrePreparedStatement = getConnection().prepareStatement(PretDAO.FIND_BY_MEMBRE)) {
            findByMembrePreparedStatement.setInt(1,
                idMembre);
            try(
                ResultSet resultSet = findByMembrePreparedStatement.executeQuery()) {
                PretDTO pretDTO = null;
                if(resultSet.next()) {
                    prets = new ArrayList<>();
                    do {
                        pretDTO = new PretDTO();
                        pretDTO.setIdPret(resultSet.getInt(1));
                        pretDTO.setIdMembre(resultSet.getInt(2));
                        pretDTO.setIdLivre(resultSet.getInt(3));
                        pretDTO.setDatePret(resultSet.getTimestamp(4));
                        pretDTO.setDateRetour(resultSet.getTimestamp(5));
                        prets.add(pretDTO);
                    } while(resultSet.next());
                }
            }
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
        return prets;
    }
}
