// Fichier IPretDAO.java
// Auteur : Team PayDay
// Date de création : 2016-10-24

package ca.qc.collegeahuntsic.bibliothequeBackEnd.dao.interfaces;

import java.sql.Timestamp;
import java.util.List;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.dto.PretDTO;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dao.DAOException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dao.InvalidCriterionException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dao.InvalidCriterionValueException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dao.InvalidHibernateSessionException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dao.InvalidSortByPropertyException;
import org.hibernate.Session;

/**
 * Interface DAO pour manipuler les prêts dans la base de données.
 *
 * @author Team PayDay
 */
public interface IPretDAO extends IDAO {
    /**
     * Trouve les prêts non terminés d'un membre.
     *
     * @param session La session Hibernate à utiliser
     * @param idMembre L'ID du membre à trouver
     * @param sortByPropertyName  Le nom de la propriété à utiliser pour classer
     * @return La liste des prêts correspondants ; une liste vide sinon
     * @throws InvalidHibernateSessionException Si la connexion est <code>null</code>
     * @throws InvalidCriterionException Si la propriété à utiliser est <code>null</code>
     * @throws InvalidCriterionValueException Si la valeur à trouver est <code>null</code>
     * @throws InvalidSortByPropertyException Si la propriété à utiliser pour classer est <code>null</code>
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    List<PretDTO> findByMembre(Session session,
        String idMembre,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidCriterionValueException,
        InvalidSortByPropertyException,
        DAOException;

    /**
     * Trouve les prêts non retournés d'un livre. La liste est classée par ordre croissant sur sortByPropertyName. Si aucun prêt n'est trouvé, une List vide est retournée.
     *
     * @param session La session Hibernate à utiliser
     * @param idLivre L'ID du livre à trouver
     * @param sortByPropertyName  Le nom de la propriété à utiliser pour classer
     * @return La liste des prêts correspondants ; une liste vide sinon
     * @throws InvalidHibernateSessionException Si la connexion est <code>null</code>
     * @throws InvalidCriterionException Si la propriété à utiliser est <code>null</code>
     * @throws InvalidCriterionValueException Si la valeur à trouver est <code>null</code>
     * @throws InvalidSortByPropertyException Si la propriété à utiliser pour classer est <code>null</code>
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    List<PretDTO> findByLivre(Session session,
        String idLivre,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidCriterionValueException,
        InvalidSortByPropertyException,
        DAOException;

    /**
     * Trouve les prêts à partir d'une date de prêt. La liste est classée par ordre croissant sur sortByPropertyName. Si aucun prêt n'est trouvé, une List vide est retournée
     *
     * @param session La session Hibernate à utiliser
     * @param datePret La date de prêt à trouver
     * @param sortByPropertyName  Le nom de la propriété à utiliser pour classer
     * @return La liste des prêts correspondants ; une liste vide sinon
     * @throws InvalidHibernateSessionException Si la connexion est <code>null</code>
     * @throws InvalidCriterionException Si la propriété à utiliser est <code>null</code>
     * @throws InvalidCriterionValueException Si la valeur à trouver est <code>null</code>
     * @throws InvalidSortByPropertyException Si la propriété à utiliser pour classer est <code>null</code>
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    List<PretDTO> findByDatePret(Session session,
        Timestamp datePret,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidCriterionValueException,
        InvalidSortByPropertyException,
        DAOException;

    /**
     * Trouve les prêts à partir d'une date de retour. La liste est classée par ordre croissant sur sortByPropertyName. Si aucun prêt n'est trouvé, une List vide est retournée.
     *
     * @param session La session Hibernate à utiliser
     * @param dateRetour La date de retour à trouver
     * @param sortByPropertyName  Le nom de la propriété à utiliser pour classer
     * @return La liste des prêts correspondants ; une liste vide sinon
     * @throws InvalidHibernateSessionException Si la connexion est <code>null</code>
     * @throws InvalidCriterionException Si la propriété à utiliser est <code>null</code>
     * @throws InvalidCriterionValueException Si la valeur à trouver est <code>null</code>
     * @throws InvalidSortByPropertyException Si la propriété à utiliser pour classer est <code>null</code>
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    List<PretDTO> findByDateRetour(Session session,
        Timestamp dateRetour,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidCriterionValueException,
        InvalidSortByPropertyException,
        DAOException;
}
