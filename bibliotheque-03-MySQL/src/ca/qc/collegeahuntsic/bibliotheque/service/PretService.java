// Fichier PretService.java
// Auteur : Team PayDay
// Date de création : 2016-05-18

package ca.qc.collegeahuntsic.bibliotheque.service;

import ca.qc.collegeahuntsic.bibliotheque.dao.LivreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dto.LivreDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.DAOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.ServiceException;

/**
 * Service de la table <code>pret</code>.
 *
 * @author Team PayDay
 */
public class PretService extends Service {
    private static final long serialVersionUID = 1L;

    private LivreDAO livreDAO;

    /**
     * Crée le service de la table <code>pret</code>.
     */
    public PretService() {
        super();
    }

    /**
     * Getter de la variable d'instance <code>this.livreDAO</code>.
     *
     * @return La variable d'instance <code>this.livreDAO</code>
     */
    public LivreDAO getLivreDAO() {
        return this.livreDAO;
    }

    /**
     * Setter de la variable d'instance <code>this.livreDAO</code>.
     *
     * @param livreDAO La valeur à utiliser pour la variable d'instance <code>this.livreDAO</code>
     */
    public void setLivreDAO(LivreDAO livreDAO) {
        this.livreDAO = livreDAO;
    }

    /**
     * Acquiert un livre.
     *
     * @param livreDTO Le livre à ajouter
     * @throws ServiceException Si le livre existe déjà ou s'il y a une erreur avec la base de données
     */
    public void acquerir(LivreDTO livreDTO) throws ServiceException {
        try {
            if(getLivreDAO().read(livreDTO.getIdLivre()) != null) {
                throw new ServiceException("Le livre "
                    + livreDTO.getIdLivre()
                    + " existe déjà");
            }
            getLivreDAO().add(livreDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }
}
