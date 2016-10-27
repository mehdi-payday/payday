// Fichier IPretDAO.java
// Auteur : Team PayDay
// Date de création : 2016-10-24

package ca.qc.collegeahuntsic.bibliotheque.dao.interfaces;

import java.sql.Timestamp;
import java.util.List;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.PretDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.DAOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidHibernateSessionException;

/**
 * TODO Auto-generated class javadoc.
 *
 * @author Team PayDay
 */
public interface IPretDAO extends IDAO {
    /**
     * Lit un prêt. Si aucun prêt n'est trouvé, <code>null</code> est retourné.
     *
     * @param connexion La connexion à utiliser
     * @param idPret L'ID du prêt à lire
     * @return Le prêt lu ; <code>null</code> sinon
     * @throws InvalidHibernateSessionException Si la connexion est <code>null</code>
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    PretDTO read(Connexion connexion,
        final String idPret) throws InvalidHibernateSessionException,
        DAOException;

    /**
     * Trouve les prêts non terminés d'un membre.
     *
     * @param connexion La connexion à utiliser
     * @param idMembre L'ID du membre à trouver
     * @return La liste des prêts correspondants ; une liste vide sinon
     * @throws InvalidHibernateSessionException Si la connexion est <code>null</code>
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    List<PretDTO> findByMembre(Connexion connexion,
        final String idMembre) throws InvalidHibernateSessionException,
        DAOException;

    /**
     * Trouve les livres en cours d'emprunt.
     *
     * @param connexion La connexion à utiliser
     * @param idLivre L'ID du livre à trouver
     * @return La liste des prêts correspondants ; une liste vide sinon
     * @throws InvalidHibernateSessionException Si la connexion est <code>null</code>
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    List<PretDTO> findByLivre(Connexion connexion,
        final String idLivre) throws InvalidHibernateSessionException,
        DAOException;

    /**
     * Trouve les prêts à partir d'une date de prêt.
     *
     * @param connexion La connexion à utiliser
     * @param datePret La date de prêt à trouver
     * @return La liste des prêts correspondants ; une liste vide sinon
     * @throws InvalidHibernateSessionException Si la connexion est <code>null</code>
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    List<PretDTO> findByDatePret(Connexion connexion,
        final Timestamp datePret) throws InvalidHibernateSessionException,
        DAOException;

    /**
     * Trouve les prêts à partir d'une date de retour.
     *
     * @param connexion La connexion à utiliser
     * @param dateRetour La date de retour à trouver
     * @return La liste des prêts correspondants ; une liste vide sinon
     * @throws InvalidHibernateSessionException Si la connexion est <code>null</code>
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    List<PretDTO> findByDateRetour(Connexion connexion,
        final Timestamp dateRetour) throws InvalidHibernateSessionException,
        DAOException;
}
