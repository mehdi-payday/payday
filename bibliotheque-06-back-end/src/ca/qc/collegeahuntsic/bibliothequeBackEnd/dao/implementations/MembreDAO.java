// Fichier MembreDAO.java
// Auteur : Alexandre Barone
// Date de création : 2016-10-26

package ca.qc.collegeahuntsic.bibliothequeBackEnd.dao.implementations;

import java.util.Collections;
import java.util.List;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.dao.interfaces.IMembreDAO;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.dto.MembreDTO;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dao.DAOException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dao.InvalidCriterionException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dao.InvalidCriterionValueException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dao.InvalidHibernateSessionException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dao.InvalidSortByPropertyException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dto.InvalidDTOClassException;
import org.hibernate.Session;

/**
 * DAO pour effectuer des CRUDs avec la table <code>membre</code>.
 *
 * @author Team PayDay
 */
public class MembreDAO extends DAO implements IMembreDAO {
    /**
     * Crée le DAO de la table membre.
     *
     * @param membreDTOClass La classe de membre DTO à utiliser
     * @throws InvalidDTOClassException Si la classe de DTO est null
     */
    MembreDAO(Class<MembreDTO> membreDTOClass) throws InvalidDTOClassException {
        super(membreDTOClass);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<MembreDTO> findByNom(Session session,
        String nom,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidCriterionValueException,
        InvalidSortByPropertyException,
        DAOException {
        if(session == null) {
            throw new InvalidHibernateSessionException("La connexion ne peut être null");
        }
        if(nom == null) {
            throw new InvalidCriterionException("Le nom ne peut être null");
        }
        if(sortByPropertyName == null) {
            throw new InvalidSortByPropertyException("La propriété utilisée pour classer ne peut être null");
        }
        List<MembreDTO> membres = Collections.emptyList();
        try {
            membres = (List<MembreDTO>) find(session,
                MembreDTO.NOM_COLUMN_NAME,
                nom,
                sortByPropertyName);
        } catch(InvalidCriterionValueException InvalidCriterionValueException) {
            throw new InvalidCriterionValueException(InvalidCriterionValueException);
        }
        return membres;
    }
}
