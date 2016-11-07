// Fichier Service.java
// Auteur : Team PayDay
// Date de création : 2016-05-18

package ca.qc.collegeahuntsic.bibliotheque.service.implementations;

import java.io.Serializable;
import java.util.List;
import ca.qc.collegeahuntsic.bibliotheque.dao.interfaces.IDAO;
import ca.qc.collegeahuntsic.bibliotheque.dto.DTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.DAOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidHibernateSessionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidPrimaryKeyException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidSortByPropertyException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dto.InvalidDTOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.ServiceException;
import ca.qc.collegeahuntsic.bibliotheque.service.interfaces.IService;
import org.hibernate.Session;

/**
 * Classe de base pour tous les services.
 *
 * @author Team PayDay
 */
public class Service implements IService {
    private IDAO dao;

    /**
     * Crée un service.
     *
     * @param daoInstance L'instance du DAO associée au service
     */
    protected Service(IDAO daoInstance) {
        setDAO(daoInstance);
    }

    /**
     *
     * Crée un service.
     *
     */
    protected Service() {

    }

    /**
     *
     * Getter du DAO.
     *
     * @return L'instance de DAO associée au service
     */
    public IDAO getDAO() {
        return this.dao;
    }

    /**
     *
     * Setter du DAO.
     *
     * @param instanceDao L'instance du DAO associée au service
     */
    protected void setDAO(IDAO instanceDao) {
        this.dao = instanceDao;
    }

    /*
     * {@inheritDoc}
     */
    @Override
    public void add(Session session,
        DTO dto) throws InvalidHibernateSessionException,
        InvalidDTOException,
        ServiceException {
        try {
            getDAO().add(session,
                dto);
        } catch(DAOException e) {
            throw new ServiceException(e);
        }
    }

    /*
     * {@inheritDoc}
     */
    @Override
    public DTO get(Session session,
        Serializable primaryKey) throws InvalidHibernateSessionException,
        InvalidPrimaryKeyException,
        ServiceException {
        try {
            return getDAO().get(session,
                primaryKey);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /*
     * {@inheritDoc}
     */
    @Override
    public void update(Session session,
        DTO dto) throws InvalidHibernateSessionException,
        InvalidDTOException,
        ServiceException {
        try {
            getDAO().update(session,
                dto);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /*
     * {@inheritDoc}
     */
    @Override
    public void save(Session session,
        DTO dto) throws InvalidHibernateSessionException,
        InvalidDTOException,
        ServiceException {
        try {
            getDAO().save(session,
                dto);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /* (non-Javadoc)
     * @see ca.qc.collegeahuntsic.bibliotheque.service.interfaces.IService#delete(org.hibernate.Session, ca.qc.collegeahuntsic.bibliotheque.dto.DTO)
     */
    @Override
    public void delete(Session session,
        DTO dto) throws InvalidHibernateSessionException,
        InvalidDTOException,
        ServiceException {
        try {
            getDAO().delete(session,
                dto);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /* (non-Javadoc)
     * @see ca.qc.collegeahuntsic.bibliotheque.service.interfaces.IService#getAll(org.hibernate.Session, java.lang.String)
     */
    @Override
    public List<? extends DTO> getAll(Session session,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidSortByPropertyException,
        ServiceException {
        // TODO Auto-generated method stub
        return null;
    }

}
