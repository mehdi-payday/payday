// Fichier ReservationFacade.java
// Auteur : Team PayDay
// Date de création : Oct 26, 2016

package ca.qc.collegeahuntsic.bibliothequeBackEnd.facade.implementations;

import ca.qc.collegeahuntsic.bibliothequeBackEnd.dto.ReservationDTO;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dao.InvalidHibernateSessionException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dto.InvalidDTOException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.facade.FacadeException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.facade.InvalidServiceException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.service.ExistingLoanException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.service.ExistingReservationException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.service.InvalidLoanLimitException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.service.MissingLoanException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.service.ServiceException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.facade.interfaces.IReservationFacade;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.service.interfaces.IReservationService;
import org.hibernate.Session;

/**
 * Facade pour interagir avec le service de réservations.
 *
 * @author Team PayDay
 */
public class ReservationFacade extends Facade implements IReservationFacade {
    /**
     * Crée la façade de la table <code>reservation</code>.
     *
     * @param reservationService Le service de la table <code>reservation</code>
     * @throws InvalidServiceException Si le service de réservations est <code>null</code>
     */
    ReservationFacade(IReservationService reservationService) throws InvalidServiceException {
        super(reservationService);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void placer(final Session session,
        final ReservationDTO reservationDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        MissingLoanException,
        ExistingLoanException,
        ExistingReservationException,
        FacadeException {
        try {
            ((IReservationService) getService()).placer(session,
                reservationDTO);
        } catch(ServiceException serviceException) {
            throw new FacadeException(serviceException);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void utiliser(final Session session,
        final ReservationDTO reservationDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        ExistingReservationException,
        ExistingLoanException,
        InvalidLoanLimitException,
        FacadeException {
        try {
            ((IReservationService) getService()).utiliser(session,
                reservationDTO);
        } catch(ServiceException serviceException) {
            throw new FacadeException(serviceException);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void annuler(final Session session,
        final ReservationDTO reservationDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        FacadeException {
        try {
            ((IReservationService) getService()).annuler(session,
                reservationDTO);
        } catch(ServiceException serviceException) {
            throw new FacadeException(serviceException);
        }

    }

}
