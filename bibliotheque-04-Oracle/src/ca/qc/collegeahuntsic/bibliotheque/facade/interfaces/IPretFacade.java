// Fichier IPretFacade.java
// Auteur : Gilles Benichou
// Date de création : 2016-10-27

package ca.qc.collegeahuntsic.bibliotheque.facade.interfaces;

import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.PretDTO;
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
 * Interface de façade pour manipuler les prêts dans la base de données.
 *
 * @author Adam Cherti
 */

public interface IPretFacade extends IFacade {

    /**
     *
     * Commence un prêt.
     *
     * @param connexion la connexion à utiliser
     * @param pretDTO Le prêt à commencer
     * @throws InvalidHibernateSessionException Si la connexion est null
     * @throws InvalidDTOException Si le prêt est null
     * @throws InvalidPrimaryKeyException Si la clef primaire du membre est null ou si la clef primaire du livre est null
     * @throws MissingDTOException Si le membre n'existe pas ou si le livre n'existe pas
     * @throws InvalidCriterionException Si l'ID du livre est null
     * @throws InvalidSortByPropertyException Si la propriété à utiliser pour classer est null
     * @throws ExistingLoanException Si le livre a été prêté
     * @throws InvalidLoanLimitException Si le membre a atteint sa limite de prêt
     * @throws ExistingReservationException Si le livre a été réservé
     * @throws InvalidDTOClassException Si la classe du membre n'est pas celle que prend en charge le DAO
     * @throws FacadeException S'il y a une erreur avec la base de données
     */
    void commencer(Connexion connexion,
        PretDTO pretDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        InvalidPrimaryKeyException,
        MissingDTOException,
        InvalidCriterionException,
        InvalidSortByPropertyException,
        ExistingLoanException,
        InvalidLoanLimitException,
        ExistingReservationException,
        InvalidDTOClassException,
        FacadeException;

    /**
     *
     * Renouvelle le prêt d'un livre.
     *
     * @param connexion la connexion à utiliser
     * @param pretDTO le prêt à renouveler
     * @throws InvalidHibernateSessionException Si la connexion est null
     * @throws InvalidDTOException Si le prêt est null
     * @throws InvalidPrimaryKeyException Si la clef primaire du prêt est null, si la clef primaire du membre est null ou si la clef primaire du livre est null
     * @throws MissingDTOException Si le prêt n'existe pas, si le membre n'existe pas ou si le livre n'existe pas
     * @throws InvalidCriterionException Si l'ID du membre est null ou si l'ID du livre est null
     * @throws InvalidSortByPropertyException Si la propriété à utiliser pour classer est null
     * @throws MissingLoanException Si le livre n'a pas encore été prêté
     * @throws ExistingLoanException Si le livre a été prêté à quelqu'un d'autre
     * @throws ExistingReservationException Si le livre a été réservé
     * @throws InvalidDTOClassException Si la classe du prêt n'est pas celle que prend en charge le DAO
     * @throws FacadeException S'il y a une erreur avec la base de données
     */
    void renouveler(Connexion connexion,
        PretDTO pretDTO) throws InvalidHibernateSessionException,
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

    /**+
     *
     * Termine un prêt.
     *
     * @param connexion La connexion à utiliser
     * @param pretDTO Le prêt à terminer
     * @throws InvalidHibernateSessionException Si la connexion est null
     * @throws InvalidDTOException Si le prêt est null
     * @throws InvalidPrimaryKeyException Si la clef primaire du prêt est null, si la clef primaire du membre est null ou si la clef primaire du livre est null
     * @throws MissingDTOException Si le prêt n'existe pas, si le membre n'existe pas ou si le livre n'existe pas
     * @throws InvalidCriterionException Si l'ID du membre est null ou si l'ID du livre est null
     * @throws InvalidSortByPropertyException Si la propriété à utiliser pour classer est null
     * @throws MissingLoanException Si le livre n'a pas encore été prêté
     * @throws ExistingLoanException Si le livre a été prêté à quelqu'un d'autre
     * @throws InvalidDTOClassException Si la classe du membre n'est pas celle que prend en charge le DAO ou si la classe du prêt n'est pas celle que prend en charge le DAO
     * @throws FacadeException S'il y a une erreur avec la base de données
     */
    void terminer(Connexion connexion,
        PretDTO pretDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        InvalidPrimaryKeyException,
        MissingDTOException,
        InvalidCriterionException,
        InvalidSortByPropertyException,
        MissingLoanException,
        ExistingLoanException,
        InvalidDTOClassException,
        FacadeException;
}
