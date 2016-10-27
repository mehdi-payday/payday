// Fichier MembreDAO.java
// Auteur : Team PayDay
// Date de création : 2016-05-18

package ca.qc.collegeahuntsic.bibliotheque.dao;

/**
 * DAO pour effectuer des CRUDs avec la table <code>membre</code>.
 *
 * @author Team PayDay
 */
public final class MembreDAO {
    /**
     *
     * TODO Auto-generated constructor javadoc.
     *
     */
    private MembreDAO() {
    }
    /*
    private static final String ADD_REQUEST = "INSERT INTO membre (nom, "
        + "                                                        telephone, "
        + "                                                        limitePret) "
        + "                                    VALUES             (?, "
        + "                                                        ?, "
        + "                                                        ?)";

    private static final String READ_REQUEST = "SELECT idMembre, "
        + "                                            nom, "
        + "                                            telephone, "
        + "                                            limitePret "
        + "                                     FROM   membre "
        + "                                     WHERE  idMembre = ?";

    private static final String UPDATE_REQUEST = "UPDATE membre "
        + "                                       SET    nom = ?, "
        + "                                              telephone = ?, "
        + "                                              limitePret = ?, "
        + "                                              nbPret = ? "
        + "                                       WHERE  idMembre = ?";

    private static final String DELETE_REQUEST = "DELETE FROM membre "
        + "                                       WHERE       idMembre = ?";

    private static final String GET_ALL_REQUEST = "SELECT idMembre, "
        + "                                               nom, "
        + "                                               telephone, "
        + "                                               limitePret, "
        + "                                               nbPret "
        + "                                        FROM   membre";

    /**
     * Crée un DAO à partir d'une connexion à la base de données.
     *
     * @param connexion La connexion à utiliser
    
    public MembreDAO(final Connexion connexion) {
        super(connexion);
    }

    /**
     * Ajoute un nouveau membre.
     *
     * @param membreDTO Le membre à ajouter
     * @throws DAOException S'il y a une erreur avec la base de données
    
    public void add(final MembreDTO membreDTO) throws DAOException {
        try(
            PreparedStatement addPreparedStatement = getConnection()
                .prepareStatement(MembreDAO.ADD_REQUEST)) {
            addPreparedStatement.setString(1,
                membreDTO.getNom());
            addPreparedStatement.setLong(2,
                membreDTO.getTelephone());
            addPreparedStatement.setInt(3,
                membreDTO.getLimitePret());
            addPreparedStatement.executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     * Lit un membre. Si aucun membre n'est trouvé, <code>null</code> est retourné.
     *
     * @param idMembre Le membre à lire
     * @return Le membre lu ; <code>null</code> sinon
     * @throws DAOException S'il y a une erreur avec la base de données
    
    public MembreDTO read(final int idMembre) throws DAOException {
        MembreDTO membreDTO = null;
        try(
            PreparedStatement readPreparedStatement = getConnection().prepareStatement(MembreDAO.READ_REQUEST)) {
            readPreparedStatement.setInt(1,
                idMembre);
            try(
                ResultSet resultSet = readPreparedStatement.executeQuery()) {
                if(resultSet.next()) {
                    membreDTO = new MembreDTO();
                    membreDTO.setIdMembre(resultSet.getInt(1));
                    membreDTO.setNom(resultSet.getString(2));
                    membreDTO.setTelephone(resultSet.getLong(3));
                    membreDTO.setLimitePret(resultSet.getInt(4));
                    //membreDTO.setNbPret(resultSet.getInt(5));
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
     * @param membreDTO Le membre à mettre à jour
     * @throws DAOException S'il y a une erreur avec la base de données
    
    public void update(final MembreDTO membreDTO) throws DAOException {
        try(
            PreparedStatement updatePreparedStatement = getConnection().prepareStatement(MembreDAO.UPDATE_REQUEST)) {
            updatePreparedStatement.setString(1,
                membreDTO.getNom());
            updatePreparedStatement.setLong(2,
                membreDTO.getTelephone());
            updatePreparedStatement.setInt(3,
                membreDTO.getLimitePret());
            /*updatePreparedStatement.setInt(4,
                membreDTO.getNbPret());
            updatePreparedStatement.setInt(/*54,
                membreDTO.getIdMembre());
            updatePreparedStatement.executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     * Supprime un membre.
     *
     * @param membreDTO Le membre à supprimer
     * @throws DAOException S'il y a une erreur avec la base de données
    
    public void delete(final MembreDTO membreDTO) throws DAOException {
        try(
            PreparedStatement deletePreparedStatement = getConnection().prepareStatement(MembreDAO.DELETE_REQUEST)) {
            deletePreparedStatement.setInt(1,
                membreDTO.getIdMembre());
            deletePreparedStatement.executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     * Trouve tous les membres.
     *
     * @return La liste des membres ; une liste vide sinon
     * @throws DAOException S'il y a une erreur avec la base de données
    
    public List<MembreDTO> getAll() throws DAOException {
        List<MembreDTO> membres = Collections.emptyList();
        try(
            PreparedStatement getAll = getConnection().prepareStatement(MembreDAO.GET_ALL_REQUEST)) {
            try(
                ResultSet resultSet = getAll.executeQuery()) {
                MembreDTO membreDTO = null;
                if(resultSet.next()) {
                    membres = new ArrayList<>();
                    do {
                        membreDTO = new MembreDTO();
                        membreDTO.setIdMembre(resultSet.getInt(1));
                        membreDTO.setNom(resultSet.getString(2));
                        membreDTO.setTelephone(resultSet.getLong(3));
                        membreDTO.setLimitePret(resultSet.getInt(4));
                        //membreDTO.setNbPret(resultSet.getInt(5));
                        membres.add(membreDTO);
                    } while(resultSet.next());
                }
            }
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
        return membres;
    }

    /**
     * Emprunte un livre.
     *
     * @param membreDTO Le membre à mettre à jour
     * @throws DAOException S'il y a une erreur avec la base de données
    
    public void emprunter(final MembreDTO membreDTO) throws DAOException {
        /*membreDTO.setNbPret(membreDTO.getNbPret()
            + 1);
        update(membreDTO);
    }

    /**
     * Retourne un livre.
     *
     * @param membreDTO Le membre à mettre à jour
     * @throws DAOException S'il y a une erreur avec la base de données
    
    public void retourner(final MembreDTO membreDTO) throws DAOException {
        /*membreDTO.setNbPret(membreDTO.getNbPret()
            - 1);
        update(membreDTO);
    }
    */
}
