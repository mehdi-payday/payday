// Fichier IPretDAO.java
// Auteur : Team PayDay
// Date de création : 2016-10-24

package ca.qc.collegeahuntsic.bibliotheque.dao.interfaces;

import java.sql.Timestamp;
import java.util.List;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.PretDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.DAOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidCriterionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidHibernateSessionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidSortByPropertyException;

/**
 * TODO Auto-generated class javadoc.
 *
 * @author Team PayDay
 */
public interface IPretDAO extends IDAO {
    /**
     * Trouve les prêts non terminés d'un membre.
     *
     * @param connexion La connexion à utiliser
     * @param idMembre L'ID du membre à trouver
     * @param sortByPropertyName  Le nom de la propriété à utiliser pour classer
     * @return La liste des prêts correspondants ; une liste vide sinon
     * @throws InvalidHibernateSessionException Si la connexion est <code>null</code>
     * @throws InvalidCriterionException Si l'ID du membre est null
     * @throws InvalidSortByPropertyException Si la propriété à utiliser pour classer est null
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    List<PretDTO> findByMembre(Connexion connexion,
        final String idMembre,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidSortByPropertyException,
        DAOException;

    /**
     * Trouve les livres en cours d'emprunt.
     *
     * @param connexion La connexion à utiliser
     * @param idLivre L'ID du livre à trouver
     * @param sortByPropertyName  Le nom de la propriété à utiliser pour classer
     * @return La liste des prêts correspondants ; une liste vide sinon
     * @throws InvalidHibernateSessionException Si la connexion est <code>null</code>
     * @throws InvalidCriterionException Si l'ID du livre est null
     * @throws InvalidSortByPropertyException Si la propriété à utiliser pour classer est null
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    List<PretDTO> findByLivre(Connexion connexion,
        final String idLivre,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidSortByPropertyException,
        DAOException;

    /**
     * Trouve les prêts à partir d'une date de prêt.
     *
     * @param connexion La connexion à utiliser
     * @param datePret La date de prêt à trouver
     * @param sortByPropertyName  Le nom de la propriété à utiliser pour classer
     * @return La liste des prêts correspondants ; une liste vide sinon
     * @throws InvalidHibernateSessionException Si la connexion est <code>null</code>
     * @throws InvalidCriterionException Si la date de prêt est null
     * @throws InvalidSortByPropertyException Si la propriété à utiliser pour classer est null
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    List<PretDTO> findByDatePret(Connexion connexion,
        final Timestamp datePret,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidSortByPropertyException,
        DAOException;

    /**
     * Trouve les prêts à partir d'une date de retour.
     *
     * @param connexion La connexion à utiliser
     * @param dateRetour La date de retour à trouver
     * @param sortByPropertyName  Le nom de la propriété à utiliser pour classer
     * @return La liste des prêts correspondants ; une liste vide sinon
     * @throws InvalidHibernateSessionException Si la connexion est <code>null</code>
     * @throws InvalidCriterionException Si la date de retour est null
     * @throws InvalidSortByPropertyException Si la propriété à utiliser pour classer est null
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    List<PretDTO> findByDateRetour(Connexion connexion,
        final Timestamp dateRetour,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidSortByPropertyException,
        DAOException;
}
