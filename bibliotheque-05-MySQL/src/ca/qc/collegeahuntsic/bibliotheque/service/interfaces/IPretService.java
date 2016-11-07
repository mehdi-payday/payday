// Fichier IPretService.java
// Auteur : Gilles Benichou
// Date de création : 2016-10-26

package ca.qc.collegeahuntsic.bibliotheque.service.interfaces;

import java.sql.Timestamp;
import java.util.List;
import ca.qc.collegeahuntsic.bibliotheque.dto.PretDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidCriterionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidCriterionValueException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidHibernateSessionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidPrimaryKeyException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidSortByPropertyException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dto.InvalidDTOClassException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dto.InvalidDTOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.ExistingLoanException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.ExistingReservationException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.InvalidLoanLimitException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.MissingLoanException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.ServiceException;
import org.hibernate.Session;

/**
 * Interface DAO pour manipuler les prets dans la base de données.
 *
 * @author Team PayDay
 */
public interface IPretService extends IService {
    /**
     * Ajoute un nouveau prêt dans la base de données.
     *
     * @param session La session Hibernate à utiliser
     * @param pretDTO Le prêt à ajouter
     *
     * @throws InvalidHibernateSessionException Si la connexion est null
     * @throws ServiceException S'il y a une erreur avec la base de données
     * @throws InvalidDTOException Si le prêt est null
     * @throws InvalidDTOClassException Si la classe du prêt n'est pas celle que prend en charge le DAO
     */
    void add(Session session,
        PretDTO pretDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        InvalidDTOClassException,
        ServiceException;

    /**
     * Lit un prêt. Si aucun prêt n'est trouvé, <code>null</code> est retourné.
     *
     * @param session La session Hibernate à utiliser
     * @param idPret L'ID du prêt à lire
     * @return Le prêt lu ; <code>null</code> sinon
     *
     * @throws InvalidHibernateSessionException Si la connexion est null
     * @throws InvalidPrimaryKeyException Si la clef primaire du prêt est null
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    PretDTO get(Session session,
        String idPret) throws InvalidHibernateSessionException,
        InvalidPrimaryKeyException,
        ServiceException;

    /**
     * Met à jour un prêt dans la base de données.
     *
     * @param session La session Hibernate à utiliser
     * @param pretDTO Le prêt à mettre à jour
     * @throws InvalidHibernateSessionException Si la connexion est null
     * @throws InvalidDTOException Si le prêt est null
     * @throws InvalidDTOClassException Si la classe du prêt n'est pas celle que prend en charge le DAO
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    void update(Session session,
        PretDTO pretDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        InvalidDTOClassException,
        ServiceException;

    /**
     *
     * Supprime un prêt de la base de données.
     *
     * @param session La session Hibernate à utiliser
     * @param pretDTO Le prêt à supprimer
     * @throws InvalidHibernateSessionException Si la connexion est null
     * @throws InvalidDTOException Si le prêt est null
     * @throws InvalidDTOClassException Si la classe du prêt n'est pas celle que prend en charge le DAO
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    void delete(Session session,
        PretDTO pretDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        InvalidDTOClassException,
        ServiceException;

    /**
     *
     * Trouve les prêts à partir d'un membre. La liste est classée par ordre croissant sur sortByPropertyName. Si aucun prêt n'est trouvé, une List vide est retournée.
     *
     * @param session - La session Hibernate à utiliser
     * @param idMembre L'ID du membre à trouver
     * @param sortByPropertyName Le nom de la propriété à utiliser pour classer
     * @return La liste des prêts correspondants ; une liste vide sinon
     * @throws InvalidHibernateSessionException Si la connexion est null
     * @throws InvalidCriterionException Si l'ID du membre est null
     * @throws InvalidSortByPropertyException Si la propriété à utiliser pour classer est null
     * @throws InvalidCriterionValueException Si la valeur à trouver est null
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    List<PretDTO> findByMembre(Session session,
        String idMembre,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidCriterionValueException,
        InvalidSortByPropertyException,
        ServiceException;

    /**
     *
     * Trouve les prêts à partir d'un livre.
     * La liste est classée par ordre croissant sur sortByPropertyName.
     * Si aucun prêt n'est trouvé, une List vide est retournée.
     *
     * @param session La session Hibernate à utiliser
     * @param idLivre L'ID du livre à trouver
     * @param sortByPropertyName Le nom de la propriété à utiliser pour classer
     * @return La liste des prêts correspondants ; une liste vide sinon
     * @throws InvalidHibernateSessionException Si la connexion est null
     * @throws InvalidCriterionException Si l'ID du livre est null
     * @throws InvalidCriterionValueException Si la valeur à trouver est null
     * @throws InvalidSortByPropertyException Si la propriété à utiliser pour classer est null
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    List<PretDTO> findByLivre(Session session,
        String idLivre,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidSortByPropertyException,
        InvalidCriterionValueException,
        ServiceException;

    /**
     *
     * Trouve les prêts à partir d'une date de prêt. La liste est classée par ordre croissant sur sortByPropertyName. Si aucun prêt n'est trouvé, une List vide est retournée.
     *
     * @param session La session Hibernate à utiliser
     * @param datePret La date de prêt à trouver
     * @param sortByPropertyName Le nom de la propriété à utiliser pour classer
     * @return La liste des prêts correspondants ; une liste vide sinon
     * @throws InvalidHibernateSessionException Si la connexion est null
     * @throws InvalidCriterionException Si la propriété à utiliser est null
     * @throws InvalidCriterionValueException Si la valeur à trouver est null
     * @throws InvalidSortByPropertyException Si la propriété à utiliser pour classer est null
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    List<PretDTO> findByDatePret(Session session,
        Timestamp datePret,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidCriterionValueException,
        InvalidSortByPropertyException,
        ServiceException;

    /**
     *
     * Trouve les prêts à partir d'une date de retour. La liste est classée par ordre croissant sur sortByPropertyName. Si aucun prêt n'est trouvé, une List vide est retournée.
     *
     * @param session La session hibernate a utiliser
     * @param dateRetour La date de retour à trouver
     * @param sortByPropertyName Le nom de la propriété à utiliser pour classer
     * @return La liste des prêts correspondants ; une liste vide sinon
     * @throws InvalidHibernateSessionException Si la connexion est null
     * @throws InvalidCriterionException Si la propriété a utiliser de retour est null
     * @throws InvalidCriterionValueException Si la valeur a trouver est null
     * @throws InvalidSortByPropertyException Si la propriété à utiliser pour classer est null
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    List<PretDTO> findByDateRetour(Session session,
        Timestamp dateRetour,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidCriterionValueException,
        InvalidSortByPropertyException,
        ServiceException;

    /**
     *
     * Commence un prêt.
     *
     * @param session La session hibernate a utiliser
     * @param pretDTO Le prêt à commencer
     * @throws InvalidHibernateSessionException Si la connexion est null
     * @throws InvalidDTOException Si le prêt est null
     * @throws ExistingLoanException Si le livre a été prêté
     * @throws InvalidLoanLimitException Si le membre a atteint sa limite de prêt
     * @throws ExistingReservationException Si le livre a été réservé
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    void commencer(Session session,
        PretDTO pretDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        ExistingLoanException,
        InvalidLoanLimitException,
        ExistingReservationException,
        ServiceException;

    /**
     *
     * Renouvelle le prêt d'un livre.
     *
     * @param session La session hibernate a utiliser
     * @param pretDTO Le prêt à renouveler
     * @throws InvalidHibernateSessionException Si la session Hibernate est null
     * @throws InvalidDTOException Si le prêt est null
     * @throws MissingLoanException Si la propriété à utiliser pour classer est null
     * @throws ExistingReservationException Si le livre a été réservé
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    void renouveler(Session session,
        PretDTO pretDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        MissingLoanException,
        ExistingReservationException,
        ServiceException;

    /**
     *
     * Termine un prêt.
     *
     * @param session La session hibernate a utiliser
     * @param pretDTO Le prêt à terminer
     * @throws InvalidHibernateSessionException Si la session Hibernate est null
     * @throws InvalidDTOException Si le prêt est null
     * @throws MissingLoanException Si le livre n'a pas encore été prêté
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    void terminer(Session session,
        PretDTO pretDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        MissingLoanException,
        ServiceException;
}
