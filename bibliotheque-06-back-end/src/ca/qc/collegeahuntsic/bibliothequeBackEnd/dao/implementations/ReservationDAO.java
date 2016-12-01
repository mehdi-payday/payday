// Fichier ReservationDAO.java
// Auteur : Team PayDay
// Date de création : 2016-05-18

package ca.qc.collegeahuntsic.bibliothequeBackEnd.dao.implementations;

import java.util.Collections;
import java.util.List;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.dao.interfaces.IReservationDAO;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.dto.ReservationDTO;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dao.DAOException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dao.InvalidCriterionException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dao.InvalidCriterionValueException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dao.InvalidHibernateSessionException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dao.InvalidSortByPropertyException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dto.InvalidDTOClassException;
import org.hibernate.Session;

/**
 * DAO pour effectuer des CRUDs avec la table <code>reservation</code>.
 *
 * @author Team PayDay
 */
public class ReservationDAO extends DAO implements IReservationDAO {

    /**
     * Crée le DAO de la table reservation.
     *
     * @param reservationDTOClass La classe de réservation DTO à utiliser
     * @throws InvalidDTOClassException Si la classe de DTO est null
     */
    ReservationDAO(Class<ReservationDTO> reservationDTOClass) throws InvalidDTOClassException {
        super(reservationDTOClass);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<ReservationDTO> findByLivre(Session session,
        String idLivre,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidSortByPropertyException,
        DAOException,
        InvalidCriterionValueException {
        if(session == null) {
            throw new InvalidHibernateSessionException("La session ne peut être null");
        }
        if(idLivre == null) {
            throw new InvalidCriterionException("Le id du livre ne peut être null");
        }
        if(sortByPropertyName == null) {
            throw new InvalidSortByPropertyException("La propriété utilisée pour classer ne peut être null");
        }
        List<ReservationDTO> reservations = Collections.emptyList();

        reservations = (List<ReservationDTO>) find(session,
            ReservationDTO.ID_LIVRE_COLUMN_NAME,
            idLivre,
            sortByPropertyName);

        return reservations;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<ReservationDTO> findByMembre(Session session,
        String idMembre,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidSortByPropertyException,
        DAOException,
        InvalidCriterionValueException {
        if(session == null) {
            throw new InvalidHibernateSessionException("La session ne peut être null");
        }
        if(idMembre == null) {
            throw new InvalidCriterionException("Le id du membre ne peut être null");
        }
        if(sortByPropertyName == null) {
            throw new InvalidSortByPropertyException("La propriété utilisée pour classer ne peut être null");
        }
        List<ReservationDTO> reservations = Collections.emptyList();

        reservations = (List<ReservationDTO>) find(session,
            ReservationDTO.ID_MEMBRE_COLUMN_NAME,
            idMembre,
            sortByPropertyName);

        return reservations;
    }
}
