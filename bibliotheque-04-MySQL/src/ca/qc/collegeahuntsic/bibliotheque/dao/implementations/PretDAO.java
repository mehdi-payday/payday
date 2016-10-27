// Fichier PretDAO.java
// Auteur : Team PayDay
// Date de création : 2016-05-18

package ca.qc.collegeahuntsic.bibliotheque.dao.implementations;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import ca.qc.collegeahuntsic.bibliotheque.dao.DAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.interfaces.IPretDAO;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.DTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.LivreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.MembreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.PretDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.DAOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidHibernateSessionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidPrimaryKeyException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dto.InvalidDTOClassException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dto.InvalidDTOException;

/**
 * DAO pour effectuer des CRUDs avec la table <code>pret</code>.
 *
 * @author Team PayDay
 */
public class PretDAO extends DAO implements IPretDAO {
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
     * {@inheritDoc}
     */
    @Override
    public void add(Connexion connexion,
        DTO dto) throws InvalidHibernateSessionException,
        InvalidDTOException,
        InvalidDTOClassException,
        DAOException {

        if(connexion == null) {
            throw new InvalidHibernateSessionException("La connexion ne peut être null");
        }

        if(dto == null) {
            throw new InvalidDTOException("Le DTO ne peut être null");
        }

        if(!dto.getClass().equals(dto.getClass())) {
            throw new InvalidDTOClassException("Le DTO doit être un "
                + dto.getClass().getName());
        }

        try(
            PreparedStatement addPreparedStatement = connexion.getConnection().prepareStatement(PretDAO.ADD_REQUEST)) {
            final PretDTO pretDTO = (PretDTO) dto;

            addPreparedStatement.setString(1,
                pretDTO.getMembreDTO().getIdMembre());
            addPreparedStatement.setString(2,
                pretDTO.getLivreDTO().getIdLivre());
            addPreparedStatement.setTimestamp(3,
                pretDTO.getDatePret());
            addPreparedStatement.executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DTO get(Connexion connexion,
        Serializable primaryKey) throws InvalidHibernateSessionException,
        InvalidPrimaryKeyException,
        DAOException {

        if(primaryKey == null) {
            throw new InvalidPrimaryKeyException("La clef primaire ne peut être null");
        }

        return read(connexion,
            (String) primaryKey);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PretDTO read(Connexion connexion,
        final String idPret) throws InvalidHibernateSessionException,
        DAOException {

        if(connexion == null) {
            throw new InvalidHibernateSessionException("La connexion ne peut être null");
        }

        PretDTO pretDTO = null;
        try(
            PreparedStatement readPreparedStatement = connexion.getConnection().prepareStatement(PretDAO.READ_REQUEST)) {
            readPreparedStatement.setString(1,
                idPret);
            try(
                ResultSet resultSet = readPreparedStatement.executeQuery()) {
                if(resultSet.next()) {
                    pretDTO = new PretDTO();
                    pretDTO.setIdPret(resultSet.getString(1));
                    final MembreDTO membreDTO = new MembreDTO();
                    membreDTO.setIdMembre(resultSet.getString(2));
                    pretDTO.setMembreDTO(membreDTO);
                    final LivreDTO livreDTO = new LivreDTO();
                    livreDTO.setIdLivre(resultSet.getString(3));
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
     * {@inheritDoc}
     */
    @Override
    public void update(Connexion connexion,
        final DTO dto) throws InvalidHibernateSessionException,
        InvalidDTOException,
        InvalidDTOClassException,
        DAOException {

        if(connexion == null) {
            throw new InvalidHibernateSessionException("La connexion ne peut être null");
        }

        if(dto == null) {
            throw new InvalidDTOException("Le DTO ne peut être null");
        }

        if(!dto.getClass().equals(dto.getClass())) {
            throw new InvalidDTOClassException("Le DTO doit être un "
                + dto.getClass().getName());
        }

        try(
            PreparedStatement updatePreparedStatement = connexion.getConnection().prepareStatement(PretDAO.UPDATE_REQUEST)) {
            final PretDTO pretDTO = (PretDTO) dto;

            updatePreparedStatement.setString(1,
                pretDTO.getMembreDTO().getIdMembre());
            updatePreparedStatement.setString(2,
                pretDTO.getLivreDTO().getIdLivre());
            updatePreparedStatement.setTimestamp(3,
                pretDTO.getDatePret());
            updatePreparedStatement.setTimestamp(4,
                pretDTO.getDateRetour());
            updatePreparedStatement.setString(5,
                pretDTO.getIdPret());
            updatePreparedStatement.executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Connexion connexion,
        final DTO dto) throws InvalidHibernateSessionException,
        InvalidDTOException,
        InvalidDTOClassException,
        DAOException {

        if(connexion == null) {
            throw new InvalidHibernateSessionException("La connexion ne peut être null");
        }

        if(dto == null) {
            throw new InvalidDTOException("Le DTO ne peut être null");
        }

        if(!dto.getClass().equals(dto.getClass())) {
            throw new InvalidDTOClassException("Le DTO doit être un "
                + dto.getClass().getName());
        }

        try(
            PreparedStatement deletePreparedStatement = connexion.getConnection().prepareStatement(PretDAO.DELETE_REQUEST)) {
            final PretDTO pretDTO = (PretDTO) dto;

            deletePreparedStatement.setString(1,
                pretDTO.getIdPret());
            deletePreparedStatement.executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PretDTO> getAll(Connexion connexion,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        DAOException {

        if(connexion == null) {
            throw new InvalidHibernateSessionException("La connexion ne peut être null");
        }

        List<PretDTO> prets = Collections.emptyList();
        try(
            PreparedStatement getAllPreparedStatement = connexion.getConnection().prepareStatement(PretDAO.GET_ALL_REQUEST)) {
            try(
                ResultSet resultSet = getAllPreparedStatement.executeQuery()) {
                PretDTO pretDTO = null;
                if(resultSet.next()) {
                    prets = new ArrayList<>();
                    do {
                        pretDTO = new PretDTO();
                        pretDTO.setIdPret(resultSet.getString(1));
                        final MembreDTO membreDTO = new MembreDTO();
                        membreDTO.setIdMembre(resultSet.getString(2));
                        pretDTO.setMembreDTO(membreDTO);
                        final LivreDTO livreDTO = new LivreDTO();
                        livreDTO.setIdLivre(resultSet.getString(3));
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
     * {@inheritDoc}
     */
    @Override
    public List<PretDTO> findByMembre(Connexion connexion,
        final String idMembre) throws InvalidHibernateSessionException,
        DAOException {

        if(connexion == null) {
            throw new InvalidHibernateSessionException("La connexion ne peut être null");
        }

        List<PretDTO> prets = Collections.emptyList();
        try(
            PreparedStatement findByMembrePreparedStatement = connexion.getConnection().prepareStatement(PretDAO.FIND_BY_MEMBRE)) {
            findByMembrePreparedStatement.setString(1,
                idMembre);
            try(
                ResultSet resultSet = findByMembrePreparedStatement.executeQuery()) {
                PretDTO pretDTO = null;
                if(resultSet.next()) {
                    prets = new ArrayList<>();
                    do {
                        pretDTO = new PretDTO();
                        pretDTO.setIdPret(resultSet.getString(1));
                        final MembreDTO membreDTO = new MembreDTO();
                        membreDTO.setIdMembre(resultSet.getString(2));
                        pretDTO.setMembreDTO(membreDTO);
                        final LivreDTO livreDTO = new LivreDTO();
                        livreDTO.setIdLivre(resultSet.getString(3));
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
     * {@inheritDoc}
     */
    @Override
    public List<PretDTO> findByLivre(Connexion connexion,
        final String idLivre) throws InvalidHibernateSessionException,
        DAOException {

        if(connexion == null) {
            throw new InvalidHibernateSessionException("La connexion ne peut être null");
        }

        List<PretDTO> prets = Collections.emptyList();
        try(
            PreparedStatement findByLivrePreparedStatement = connexion.getConnection().prepareStatement(PretDAO.FIND_BY_LIVRE)) {
            findByLivrePreparedStatement.setString(1,
                idLivre);
            try(
                ResultSet resultSet = findByLivrePreparedStatement.executeQuery()) {
                PretDTO pretDTO = null;
                if(resultSet.next()) {
                    prets = new ArrayList<>();
                    do {
                        pretDTO = new PretDTO();
                        pretDTO.setIdPret(resultSet.getString(1));
                        final MembreDTO membreDTO = new MembreDTO();
                        membreDTO.setIdMembre(resultSet.getString(2));
                        pretDTO.setMembreDTO(membreDTO);
                        final LivreDTO livreDTO = new LivreDTO();
                        livreDTO.setIdLivre(resultSet.getString(3));
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
     * {@inheritDoc}
     */
    @Override
    public List<PretDTO> findByDatePret(Connexion connexion,
        final Timestamp datePret) throws InvalidHibernateSessionException,
        DAOException {

        if(connexion == null) {
            throw new InvalidHibernateSessionException("La connexion ne peut être null");
        }

        List<PretDTO> prets = Collections.emptyList();
        try(
            PreparedStatement findByDatePretPreparedStatement = connexion.getConnection().prepareStatement(PretDAO.FIND_BY_DATE_PRET)) {
            findByDatePretPreparedStatement.setTimestamp(1,
                datePret);
            try(
                ResultSet resultSet = findByDatePretPreparedStatement.executeQuery()) {
                PretDTO pretDTO = null;
                if(resultSet.next()) {
                    prets = new ArrayList<>();
                    do {
                        pretDTO = new PretDTO();
                        pretDTO.setIdPret(resultSet.getString(1));
                        final MembreDTO membreDTO = new MembreDTO();
                        membreDTO.setIdMembre(resultSet.getString(2));
                        pretDTO.setMembreDTO(membreDTO);
                        final LivreDTO livreDTO = new LivreDTO();
                        livreDTO.setIdLivre(resultSet.getString(3));
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
     * {@inheritDoc}
     */
    @Override
    public List<PretDTO> findByDateRetour(Connexion connexion,
        final Timestamp dateRetour) throws InvalidHibernateSessionException,
        DAOException {

        if(connexion == null) {
            throw new InvalidHibernateSessionException("La connexion ne peut être null");
        }

        List<PretDTO> prets = Collections.emptyList();
        try(
            PreparedStatement findByDateRetourPreparedStatement = connexion.getConnection().prepareStatement(PretDAO.FIND_BY_DATE_RETOUR)) {
            findByDateRetourPreparedStatement.setTimestamp(1,
                dateRetour);
            try(
                ResultSet resultSet = findByDateRetourPreparedStatement.executeQuery()) {
                PretDTO pretDTO = null;
                if(resultSet.next()) {
                    prets = new ArrayList<>();
                    do {
                        pretDTO = new PretDTO();
                        pretDTO.setIdPret(resultSet.getString(1));
                        final MembreDTO membreDTO = new MembreDTO();
                        membreDTO.setIdMembre(resultSet.getString(2));
                        pretDTO.setMembreDTO(membreDTO);
                        final LivreDTO livreDTO = new LivreDTO();
                        livreDTO.setIdLivre(resultSet.getString(3));
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
