// Fichier IReservationService.java
// Auteur : Mehdi Hamidi
// Date de création : 2016-10-26

package ca.qc.collegeahuntsic.bibliotheque.service.interfaces;

import ca.qc.collegeahuntsic.bibliotheque.dto.ReservationDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidCriterionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidCriterionValueException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidHibernateSessionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidPrimaryKeyException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidSortByPropertyException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dto.InvalidDTOClassException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dto.InvalidDTOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dto.MissingDTOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.ExistingLoanException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.ExistingReservationException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.InvalidLoanLimitException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.MissingLoanException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.ServiceException;
import org.hibernate.Session;

/**
 * Interface de service pour manipuler les réservations dans la base de données.
 *
 * @author Mehdi Hamidi
 */
public interface IReservationService extends IService {

    /**
     *
     * Place une réservation.
     *
     * @param session La connexion à utiliser
     * @param reservationDTO La réservation à placer
     * @throws InvalidHibernateSessionException - Si la connexion est null
     * @throws InvalidDTOException - Si la réservation est null
     * @throws InvalidPrimaryKeyException - Si la clef primaire du membre est null ou si la clef primaire du livre est null
     * @throws MissingDTOException - Si le membre n'existe pas ou si le livre n'existe pas
     * @throws InvalidCriterionException - Si l'ID du livre est null
     * @throws InvalidSortByPropertyException - Si la propriété à utiliser pour classer est null
     * @throws MissingLoanException - Si le livre n'a pas encore été prêté
     * @throws ExistingLoanException - Si le livre est déjà prêté au membre
     * @throws ExistingReservationException - Si le membre a déjà réservé ce livre
     * @throws InvalidDTOClassException - Si la classe de la réservation n'est pas celle que prend en charge le DAO
     * @throws ServiceException - S'il y a une erreur avec la base de données
     * @throws InvalidCriterionValueException Si la valeur à trouver est null
     */
    void placer(Session session,
        ReservationDTO reservationDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        InvalidPrimaryKeyException,
        MissingDTOException,
        InvalidCriterionException,
        InvalidSortByPropertyException,
        MissingLoanException,
        ExistingLoanException,
        ExistingReservationException,
        InvalidDTOClassException,
        InvalidCriterionValueException,
        ServiceException;

    /**
    *
    * Utilise une réservation.
    *
    * @param session La connexion à utiliser
    * @param reservationDTO La réservation à utiliser
    * @throws InvalidHibernateSessionException - Si la connexion est null
    * @throws InvalidDTOException - Si la réservation est null
    * @throws InvalidPrimaryKeyException - Si la clef primaire du membre est null ou si la clef primaire du livre est null
    * @throws MissingDTOException - Si le membre n'existe pas ou si le livre n'existe pas
    * @throws InvalidCriterionException - Si l'ID du livre est null
    * @throws InvalidSortByPropertyException - Si la propriété à utiliser pour classer est null
    * @throws ExistingLoanException - Si le livre est déjà prêté au membre
    * @throws InvalidLoanLimitException InvalidLoanLimitException - Si le membre a atteint sa limite de prêt
    * @throws ExistingReservationException - Si le membre a déjà réservé ce livre
    * @throws InvalidDTOClassException - Si la classe de la réservation n'est pas celle que prend en charge le DAO
    * @throws ServiceException - S'il y a une erreur avec la base de données
    * @throws InvalidCriterionValueException Si la valeur à trouver est null
    */
    void utiliser(Session session,
        ReservationDTO reservationDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        InvalidPrimaryKeyException,
        MissingDTOException,
        InvalidCriterionException,
        InvalidSortByPropertyException,
        ExistingReservationException,
        ExistingLoanException,
        InvalidLoanLimitException,
        InvalidDTOClassException,
        InvalidCriterionValueException,
        ServiceException;

    /**
     *
     * Annule une réservation.
     *
     * @param session La connexion à utiliser
     * @param reservationDTO Le reservation à annuler
     * @throws InvalidHibernateSessionException - Si la connexion est null
     * @throws InvalidDTOException - Si la réservation est null
     * @throws InvalidPrimaryKeyException - Si la clef primaire de la réservation est null
     * @throws MissingDTOException - Si la réservation n'existe pas, si le membre n'existe pas ou si le livre n'existe pas
     * @throws InvalidDTOClassException - Si la classe de la réservation n'est pas celle que prend en charge le DAO
     * @throws ServiceException - S'il y a une erreur avec la base de données
     * @throws InvalidCriterionValueException Si la valeur à trouver est null
     */
    void annuler(Session session,
        ReservationDTO reservationDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        InvalidPrimaryKeyException,
        MissingDTOException,
        InvalidDTOClassException,
        InvalidCriterionValueException,
        ServiceException;
}
