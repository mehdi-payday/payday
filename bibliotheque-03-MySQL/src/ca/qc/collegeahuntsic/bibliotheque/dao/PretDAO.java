// Fichier PretDAO.java
// Auteur : Team PayDay
// Date de création : 2016-05-18

package ca.qc.collegeahuntsic.bibliotheque.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.LivreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.MembreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.PretDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.DAOException;

/**
 * DAO pour effectuer des CRUDs avec la table <code>pret</code>.
 *
 * @author Team PayDay
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
        + "                                                      NULL)";

    private static final String READ_REQUEST = "SELECT idPret, "
        + "                                            idMembre, "
        + "                                            idLivre, "
        + "                                            datePret, "
        + "                                            dateRetour "
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
        + "                                               dateRetour "
        + "                                        FROM   pret";

    private static final String FIND_BY_MEMBRE = "SELECT   idPret, "
        + "                                                idMembre, "
        + "                                                idLivre, "
        + "                                                datePret, "
        + "                                                dateRetour "
        + "                                       FROM     pret "
        + "                                       WHERE    idMembre = ? "
        + "                                       AND      dateRetour IS NULL "
        + "                                       ORDER BY datePret ASC";

    private static final String FIND_BY_LIVRE = "SELECT   idPret, "
        + "                                               idMembre, "
        + "                                               idLivre, "
        + "                                               datePret, "
        + "                                               dateRetour "
        + "                                      FROM     pret "
        + "                                      WHERE    idLivre = ? "
        + "                                      AND      dateRetour IS NULL "
        + "                                      ORDER BY datePret ASC";

    private static final String FIND_BY_DATE_PRET = "SELECT idPret, "
        + "                                                 idMembre, "
        + "                                                 idLivre, "
        + "                                                 datePret, "
        + "                                                 dateRetour "
        + "                                          FROM   pret "
        + "                                          WHERE  datePret = ?";

    private static final String FIND_BY_DATE_RETOUR = "SELECT idPret, "
        + "                                                   idMembre, "
        + "                                                   idLivre, "
        + "                                                   datePret, "
        + "                                                   dateRetour "
        + "                                            FROM   pret "
        + "                                            WHERE  dateRetour = ?";

    /**
     * Crée un DAO à partir d'une connexion à la base de données.
     *
     * @param connexion La connexion à utiliser
     */
    public PretDAO(final Connexion connexion) {
        super(connexion);
    }

    /**
     * Ajoute un nouveau prêt.
     *
     * @param pretDTO Le prêt à ajouter
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    public void add(final PretDTO pretDTO) throws DAOException {
        try(
            PreparedStatement addPreparedStatement = getConnection().prepareStatement(PretDAO.ADD_REQUEST)) {
            addPreparedStatement.setInt(1,
                pretDTO.getMembreDTO().getIdMembre());
            addPreparedStatement.setInt(2,
                pretDTO.getLivreDTO().getIdLivre());
            addPreparedStatement.setTimestamp(3,
                pretDTO.getDatePret());
            addPreparedStatement.executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     * Lit un prêt. Si aucun prêt n'est trouvé, <code>null</code> est retourné.
     *
     * @param idPret L'ID du prêt à lire
     * @return Le prêt lu ; <code>null</code> sinon
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    public PretDTO read(final int idPret) throws DAOException {
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
                    final MembreDTO membreDTO = new MembreDTO();
                    membreDTO.setIdMembre(resultSet.getInt(2));
                    pretDTO.setMembreDTO(membreDTO);
                    final LivreDTO livreDTO = new LivreDTO();
                    livreDTO.setIdLivre(resultSet.getInt(3));
                    pretDTO.setLivreDTO(livreDTO);
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
     * Met à jour un prêt.
     *
     * @param pretDTO Le prêt à mettre à jour
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    public void update(final PretDTO pretDTO) throws DAOException {
        try(
            PreparedStatement updatePreparedStatement = getConnection().prepareStatement(PretDAO.UPDATE_REQUEST)) {
            updatePreparedStatement.setInt(1,
                pretDTO.getMembreDTO().getIdMembre());
            updatePreparedStatement.setInt(2,
                pretDTO.getLivreDTO().getIdLivre());
            updatePreparedStatement.setTimestamp(3,
                pretDTO.getDatePret());
            updatePreparedStatement.setTimestamp(4,
                pretDTO.getDateRetour());
            updatePreparedStatement.setInt(5,
                pretDTO.getIdPret());
            updatePreparedStatement.executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     * Supprime un prêt.
     *
     * @param pretDTO Le prêt à supprimer
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    public void delete(final PretDTO pretDTO) throws DAOException {
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
     * Trouve tous les prêts.
     *
     * @return La liste des prêts ; une liste vide sinon
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
                        final MembreDTO membreDTO = new MembreDTO();
                        membreDTO.setIdMembre(resultSet.getInt(2));
                        pretDTO.setMembreDTO(membreDTO);
                        final LivreDTO livreDTO = new LivreDTO();
                        livreDTO.setIdLivre(resultSet.getInt(3));
                        pretDTO.setLivreDTO(livreDTO);
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
     * Trouve les prêts non terminés d'un membre.
     *
     * @param idMembre L'ID du membre à trouver
     * @return La liste des prêts correspondants ; une liste vide sinon
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    public List<PretDTO> findByMembre(final int idMembre) throws DAOException {
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
                        final MembreDTO membreDTO = new MembreDTO();
                        membreDTO.setIdMembre(resultSet.getInt(2));
                        pretDTO.setMembreDTO(membreDTO);
                        final LivreDTO livreDTO = new LivreDTO();
                        livreDTO.setIdLivre(resultSet.getInt(3));
                        pretDTO.setLivreDTO(livreDTO);
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
     * Trouve les livres en cours d'emprunt.
     *
     * @param idLivre L'ID du livre à trouver
     * @return La liste des prêts correspondants ; une liste vide sinon
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    public List<PretDTO> findByLivre(final int idLivre) throws DAOException {
        List<PretDTO> prets = Collections.emptyList();
        try(
            PreparedStatement findByLivrePreparedStatement = getConnection().prepareStatement(PretDAO.FIND_BY_LIVRE)) {
            findByLivrePreparedStatement.setInt(1,
                idLivre);
            try(
                ResultSet resultSet = findByLivrePreparedStatement.executeQuery()) {
                PretDTO pretDTO = null;
                if(resultSet.next()) {
                    prets = new ArrayList<>();
                    do {
                        pretDTO = new PretDTO();
                        pretDTO.setIdPret(resultSet.getInt(1));
                        final MembreDTO membreDTO = new MembreDTO();
                        membreDTO.setIdMembre(resultSet.getInt(2));
                        pretDTO.setMembreDTO(membreDTO);
                        final LivreDTO livreDTO = new LivreDTO();
                        livreDTO.setIdLivre(resultSet.getInt(3));
                        pretDTO.setLivreDTO(livreDTO);
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
     * Trouve les prêts à partir d'une date de prêt.
     *
     * @param datePret La date de prêt à trouver
     * @return La liste des prêts correspondants ; une liste vide sinon
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    public List<PretDTO> findByDatePret(final Timestamp datePret) throws DAOException {
        List<PretDTO> prets = Collections.emptyList();
        try(
            PreparedStatement findByDatePretPreparedStatement = getConnection().prepareStatement(PretDAO.FIND_BY_DATE_PRET)) {
            findByDatePretPreparedStatement.setTimestamp(1,
                datePret);
            try(
                ResultSet resultSet = findByDatePretPreparedStatement.executeQuery()) {
                PretDTO pretDTO = null;
                if(resultSet.next()) {
                    prets = new ArrayList<>();
                    do {
                        pretDTO = new PretDTO();
                        pretDTO.setIdPret(resultSet.getInt(1));
                        final MembreDTO membreDTO = new MembreDTO();
                        membreDTO.setIdMembre(resultSet.getInt(2));
                        pretDTO.setMembreDTO(membreDTO);
                        final LivreDTO livreDTO = new LivreDTO();
                        livreDTO.setIdLivre(resultSet.getInt(3));
                        pretDTO.setLivreDTO(livreDTO);
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
     * Trouve les prêts à partir d'une date de retour.
     *
     * @param dateRetour La date de retour à trouver
     * @return La liste des prêts correspondants ; une liste vide sinon
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    public List<PretDTO> findByDateRetour(final Timestamp dateRetour) throws DAOException {
        List<PretDTO> prets = Collections.emptyList();
        try(
            PreparedStatement findByDateRetourPreparedStatement = getConnection().prepareStatement(PretDAO.FIND_BY_DATE_RETOUR)) {
            findByDateRetourPreparedStatement.setTimestamp(1,
                dateRetour);
            try(
                ResultSet resultSet = findByDateRetourPreparedStatement.executeQuery()) {
                PretDTO pretDTO = null;
                if(resultSet.next()) {
                    prets = new ArrayList<>();
                    do {
                        pretDTO = new PretDTO();
                        pretDTO.setIdPret(resultSet.getInt(1));
                        final MembreDTO membreDTO = new MembreDTO();
                        membreDTO.setIdMembre(resultSet.getInt(2));
                        pretDTO.setMembreDTO(membreDTO);
                        final LivreDTO livreDTO = new LivreDTO();
                        livreDTO.setIdLivre(resultSet.getInt(3));
                        pretDTO.setLivreDTO(livreDTO);
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
