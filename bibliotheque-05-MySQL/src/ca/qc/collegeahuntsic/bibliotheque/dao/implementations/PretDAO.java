// Fichier PretDAO.java
// Auteur : Team PayDay
// Date de création : 2016-05-18

package ca.qc.collegeahuntsic.bibliotheque.dao.implementations;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import ca.qc.collegeahuntsic.bibliotheque.dao.interfaces.IPretDAO;
import ca.qc.collegeahuntsic.bibliotheque.dto.PretDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.DAOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidCriterionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidCriterionValueException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidHibernateSessionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidSortByPropertyException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dto.InvalidDTOClassException;
import org.hibernate.Session;

/**
 * DAO pour effectuer des CRUDs avec la table <code>pret</code>.
 *
 * @author Team PayDay
 */
public class PretDAO extends DAO implements IPretDAO {
    /**
     * Crée un DAO à partir d'une connexion à la base de données.
     *
     * @param pretDTOClass La classe de membre DTO à utiliser
     * @throws InvalidDTOClassException Si la classe de DTO est null
     */
    public PretDAO(Class<PretDTO> pretDTOClass) throws InvalidDTOClassException {
        super(pretDTOClass);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<PretDTO> findByMembre(Session session,
        String idMembre,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidCriterionValueException,
        InvalidSortByPropertyException,
        DAOException {

        if(session == null) {
            throw new InvalidHibernateSessionException("La connexion ne peut être null");
        }
        if(idMembre == null) {
            throw new InvalidCriterionException("Le id de membre ne peut être null");
        }
        if(sortByPropertyName == null) {
            throw new InvalidSortByPropertyException("La propriété utilisée pour classer ne peut être null");
        }

        List<PretDTO> prets = Collections.emptyList();
        try {
            prets = (List<PretDTO>) find(session,
                PretDTO.ID_MEMBRE_COLUMN_NAME,
                idMembre,
                sortByPropertyName);
        } catch(InvalidCriterionValueException invalidCriterionValueException) {
            throw new DAOException("La valeur de l'id de membre ne peut être null",
                invalidCriterionValueException);
        }
        return prets;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<PretDTO> findByLivre(Session session,
        String idLivre,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidCriterionValueException,
        InvalidSortByPropertyException,
        DAOException {

        if(session == null) {
            throw new InvalidHibernateSessionException("La connexion ne peut être null");
        }
        if(idLivre == null) {
            throw new InvalidCriterionException("Le id de livre ne peut être null");
        }
        if(sortByPropertyName == null) {
            throw new InvalidSortByPropertyException("La propriété utilisée pour classer ne peut être null");
        }

        List<PretDTO> prets = Collections.emptyList();

        try {
            prets = (List<PretDTO>) find(session,
                PretDTO.ID_LIVRE_COLUMN_NAME,
                idLivre,
                sortByPropertyName);
        } catch(InvalidCriterionValueException invalidCriterionValueException) {
            throw new DAOException("La valeur de l'id du livre a rechercher ne peut être null",
                invalidCriterionValueException);
        }
        return prets;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<PretDTO> findByDatePret(Session session,
        Timestamp datePret,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidCriterionValueException,
        InvalidSortByPropertyException,
        DAOException {

        if(session == null) {
            throw new InvalidHibernateSessionException("La connexion ne peut être null");
        }
        if(datePret == null) {
            throw new InvalidCriterionException("La date de prêt ne peut être null");
        }
        if(sortByPropertyName == null) {
            throw new InvalidSortByPropertyException("La propriété utilisée pour classer ne peut être null");
        }

        List<PretDTO> prets = Collections.emptyList();

        try {
            prets = (List<PretDTO>) find(session,
                PretDTO.DATE_PRET_COLUMN_NAME,
                datePret,
                sortByPropertyName);
        } catch(InvalidCriterionValueException invalidCriterionValueException) {
            throw new DAOException("La valeur de la date de prêt a rechercher ne peut être null",
                invalidCriterionValueException);
        }

        return prets;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<PretDTO> findByDateRetour(Session session,
        Timestamp dateRetour,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidCriterionValueException,
        InvalidSortByPropertyException,
        DAOException {

        if(session == null) {
            throw new InvalidHibernateSessionException("La connexion ne peut être null");
        }
        if(dateRetour == null) {
            throw new InvalidCriterionException("La date de retour ne peut être null");
        }
        if(sortByPropertyName == null) {
            throw new InvalidSortByPropertyException("La propriété utilisée pour classer ne peut être null");
        }

        List<PretDTO> prets = Collections.emptyList();

        try {
            prets = (List<PretDTO>) find(session,
                PretDTO.DATE_RETOUR_COLUMN_NAME,
                dateRetour,
                sortByPropertyName);
        } catch(InvalidCriterionValueException invalidCriterionValueException) {
            throw new DAOException("La valeur de la date de retour a rechercher ne peut être null",
                invalidCriterionValueException);
        }

        return prets;
    }
}
