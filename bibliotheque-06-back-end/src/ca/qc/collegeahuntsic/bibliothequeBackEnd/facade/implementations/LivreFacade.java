// Fichier LivreFacade.java
// Auteur : Team PayDay
// Date de création : 2016-05-18

package ca.qc.collegeahuntsic.bibliothequeBackEnd.facade.implementations;

import ca.qc.collegeahuntsic.bibliothequeBackEnd.dto.LivreDTO;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dao.InvalidHibernateSessionException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dto.InvalidDTOException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.facade.FacadeException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.facade.InvalidServiceException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.service.ExistingLoanException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.service.ExistingReservationException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.service.ServiceException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.facade.interfaces.ILivreFacade;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.service.interfaces.ILivreService;
import org.hibernate.Session;

/**
 * Facade pour interagir avec le service de livres.
 *
 * @author Team PayDay
 */
public class LivreFacade extends Facade implements ILivreFacade {

    /**
     * Crée la façade de la table <code>livre</code>.
     *
     * @param livreService Le service de la table <code>livre</code>
     * @throws InvalidServiceException Si le service de livres est <code>null</code>
     */
    LivreFacade(ILivreService livreService) throws InvalidServiceException {
        super(livreService);

    }

    // Region Getters and Setters

    // EndRegion Getters and Setters

    /**
     * {@inheritDoc}
     */
    @Override
    public void acquerir(Session session,
        LivreDTO livreDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        FacadeException {
        try {
            ((ILivreService) getService()).acquerir(session,
                livreDTO);
        } catch(ServiceException serviceException) {
            throw new FacadeException(serviceException);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void vendre(Session session,
        LivreDTO livreDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        ExistingLoanException,
        ExistingReservationException,
        FacadeException {
        try {
            ((ILivreService) getService()).vendre(session,
                livreDTO);
        } catch(ServiceException serviceException) {
            throw new FacadeException(serviceException);
        }
    }
}
