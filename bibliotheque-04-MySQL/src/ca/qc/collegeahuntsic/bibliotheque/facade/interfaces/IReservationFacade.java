// Fichier IReservationFacade.java
// Auteur : Team PayDay
// Date de création : Oct 26, 2016

package ca.qc.collegeahuntsic.bibliotheque.facade.interfaces;

import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.ReservationDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidCriterionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidHibernateSessionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidPrimaryKeyException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidSortByPropertyException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dto.InvalidDTOClassException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dto.InvalidDTOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dto.MissingDTOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.facade.FacadeException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.ExistingLoanException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.ExistingReservationException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.InvalidLoanLimitException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.MissingLoanException;

/**
 * Interface de façade pour manipuler les réservations dans la base de données.
 *
 * @author Team PayDay
 */
public interface IReservationFacade extends IFacade {

    /**
     *
     * Place une réservation.
     *
     * @param connexion La connexion à utiliser
     * @param reservationDTO La réservation à utiliser
     * @throws InvalidHibernateSessionException Si la connexion est null
     * @throws InvalidDTOException Si la réservation est null
     * @throws InvalidPrimaryKeyException Si la clef primaire du membre est null ou si la clef primaire du livre est null
     * @throws MissingDTOException Si le membre n'existe pas ou si le livre n'existe pas
     * @throws InvalidCriterionException Si l'ID du livre est null
     * @throws InvalidSortByPropertyException Si la propriété à utiliser pour classer est null
     * @throws MissingLoanException Si le livre n'a pas encore été prêté
     * @throws ExistingLoanException Si le livre est déja prêté au membre
     * @throws ExistingReservationException Si le membre a déja réservé ce livre
     * @throws InvalidDTOClassException Si la classe de la réservation n'est pas celle que prends en charge le DAO
     * @throws FacadeException Si il y a une erreur avec la base de données
     */
    void placer(Connexion connexion,
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
        FacadeException;

    /**
     *
     * Utilise une réservation.
     *
     * @param connexion La connexion à utiliser
     * @param reservationDTO La réservation à utiliser
     * @throws InvalidHibernateSessionException - Si la connexion est null
     * @throws InvalidDTOException - Si la réservation est null
     * @throws InvalidPrimaryKeyException - Si la clef primaire de la réservation est null, si la clef primaire du membre est null ou si la clef primaire du livre est null
     * @throws MissingDTOException - Si la réservation n'existe pas, si le membre n'existe pas ou si le livre n'existe pas
     * @throws InvalidCriterionException - Si l'ID du livre est null
     * @throws InvalidSortByPropertyException - Si la propriété à utiliser pour classer est null
     * @throws ExistingReservationException - Si la réservation n'est pas la première de la liste
     * @throws ExistingLoanException - Si le livre est déjà prêté au membre
     * @throws InvalidLoanLimitException - Si le membre a atteint sa limite de prêt
     * @throws InvalidDTOClassException - Si la classe du membre n'est pas celle que prend en charge le DAO ou si la classe du n'est pas celle que prend en charge le DAO
     * @throws FacadeException - S'il y a une erreur avec la base de données
     */
    void utiliser(Connexion connexion,
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
        FacadeException;

    /**
     * Place une réservation.
     *
     * @param connexion La connexion à utiliser
     * @param reservationDTO La réservation à utiliser
     * @throws InvalidHibernateSessionException - Si la connexion est null
     * @throws InvalidDTOException - Si la réservation est null
     * @throws InvalidPrimaryKeyException - Si la clef primaire de la réservation est null
     * @throws MissingDTOException - Si la réservation n'existe pas, si le membre n'existe pas ou si le livre n'existe pas
     * @throws InvalidDTOClassException - Si la classe de la réservation n'est pas celle que prend en charge le DAO
     * @throws FacadeException - Si la connexion est null, si la réservation est null, si la réservation n'existe pas ou s'il y a une erreur avec la base de données
     */
    void annuler(Connexion connexion,
        ReservationDTO reservationDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        InvalidPrimaryKeyException,
        MissingDTOException,
        InvalidDTOClassException,
        FacadeException;
}
