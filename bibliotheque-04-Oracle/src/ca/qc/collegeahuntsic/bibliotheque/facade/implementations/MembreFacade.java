// Fichier MembreFacade.java
// Auteur : Mehdi Hamidi
// Date de création : 2016-10-26

package ca.qc.collegeahuntsic.bibliotheque.facade.implementations;

import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.MembreDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidCriterionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidHibernateSessionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidPrimaryKeyException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidSortByPropertyException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dto.InvalidDTOClassException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dto.InvalidDTOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dto.MissingDTOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.facade.FacadeException;
import ca.qc.collegeahuntsic.bibliotheque.exception.facade.InvalidServiceException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.ExistingLoanException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.ExistingReservationException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.ServiceException;
import ca.qc.collegeahuntsic.bibliotheque.facade.interfaces.IMembreFacade;
import ca.qc.collegeahuntsic.bibliotheque.service.interfaces.IMembreService;

/**
 * Facade pour interagir avec le service de membres.
 *
 * @author Mehdi Hamidi
 */
public class MembreFacade extends Facade implements IMembreFacade {
    private IMembreService membreService;

    /**
     * Crée la façade de la table membre.
     *
     * @param membreService Le service de la table membre
     * @throws InvalidServiceException Si le service de membres est null
     */
    public MembreFacade(final IMembreService membreService) throws InvalidServiceException {
        super();
        if(membreService == null) {
            throw new InvalidServiceException("Le service de membres ne peut être null");
        }
        setMembreService(membreService);
    }

    /**
     *  {@inheritDoc}
     */
    @Override
    public void desinscrire(final Connexion connexion,
        final MembreDTO membreDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        InvalidDTOClassException,
        InvalidPrimaryKeyException,
        MissingDTOException,
        ExistingLoanException,
        InvalidCriterionException,
        InvalidSortByPropertyException,
        ExistingReservationException,
        FacadeException {
        try {
            getMembreService().desinscrire(connexion,
                membreDTO);
        } catch(ServiceException serviceException) {
            throw new FacadeException(serviceException);
        }

    }

    /**
     *  {@inheritDoc}
     */
    @Override
    public void inscrire(final Connexion connexion,
        final MembreDTO membreDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        InvalidDTOClassException,
        FacadeException {
        try {
            getMembreService().inscrire(connexion,
                membreDTO);
        } catch(ServiceException serviceException) {
            throw new FacadeException(serviceException);
        }

    }

    // Region Getters and Setters
    /**
     * Getter de la variable d'instance <code>this.membreService</code>.
     *
     * @return La variable d'instance <code>this.membreService</code>
     */
    private IMembreService getMembreService() {
        return this.membreService;
    }

    /**
     * Setter de la variable d'instance <code>this.membreService</code>.
     *
     * @param membreService La valeur à utiliser pour la variable d'instance <code>this.membreService</code>
     */
    private void setMembreService(IMembreService membreService) {
        this.membreService = membreService;
    }
    // EndRegion Getters and Setters
}
