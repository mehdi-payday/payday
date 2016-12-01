// Fichier MembreFacade.java
// Auteur : Mehdi Hamidi
// Date de création : 2016-10-26

package ca.qc.collegeahuntsic.bibliothequeBackEnd.facade.implementations;

import ca.qc.collegeahuntsic.bibliothequeBackEnd.dto.MembreDTO;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dao.InvalidHibernateSessionException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dto.InvalidDTOException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.facade.FacadeException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.facade.InvalidServiceException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.service.ExistingLoanException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.service.ExistingReservationException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.service.ServiceException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.facade.interfaces.IMembreFacade;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.service.interfaces.IMembreService;
import org.hibernate.Session;

/**
 * Facade pour interagir avec le service de membres.
 *
 * @author Mehdi Hamidi
 */
public class MembreFacade extends Facade implements IMembreFacade {
    /**
     * Crée la façade de la table membre.
     *
     * @param membreService Le service de la table membre
     * @throws InvalidServiceException Si le service de membres est null
     */
    public MembreFacade(IMembreService membreService) throws InvalidServiceException {
        super(membreService);
    }

    /**
     *  {@inheritDoc}
     */
    @Override
    public void desinscrire(final Session session,
        final MembreDTO membreDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        ExistingLoanException,
        ExistingReservationException,
        FacadeException {
        try {
            ((IMembreService) getService()).desinscrire(session,
                membreDTO);
        } catch(ServiceException serviceException) {
            throw new FacadeException(serviceException);
        }

    }

    /**
     *  {@inheritDoc}
     */
    @Override
    public void inscrire(final Session session,
        final MembreDTO membreDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        FacadeException {
        try {
            ((IMembreService) getService()).inscrire(session,
                membreDTO);
        } catch(ServiceException serviceException) {
            throw new FacadeException(serviceException);
        }

    }

}
