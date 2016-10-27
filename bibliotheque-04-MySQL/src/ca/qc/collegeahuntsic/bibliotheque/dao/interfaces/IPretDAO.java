// Fichier IPretDAO.java
// Auteur : Team PayDay
// Date de création : 2016-10-24

package ca.qc.collegeahuntsic.bibliotheque.dao.interfaces;

import java.sql.Timestamp;
import java.util.List;
import ca.qc.collegeahuntsic.bibliotheque.dto.PretDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.DAOException;

/**
 * TODO Auto-generated class javadoc.
 *
 * @author Team PayDay
 */
public interface IPretDAO extends IDAO {
    /**
     * Ajoute un nouveau prêt.
     *
     * @param pretDTO Le prêt à ajouter
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    void add(final PretDTO pretDTO) throws DAOException;

    /**
     * Lit un prêt. Si aucun prêt n'est trouvé, <code>null</code> est retourné.
     *
     * @param idPret L'ID du prêt à lire
     * @return Le prêt lu ; <code>null</code> sinon
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    PretDTO read(final int idPret) throws DAOException;

    /**
     * Met à jour un prêt.
     *
     * @param pretDTO Le prêt à mettre à jour
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    void update(final PretDTO pretDTO) throws DAOException;

    /**
     * Supprime un prêt.
     *
     * @param pretDTO Le prêt à supprimer
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    void delete(final PretDTO pretDTO) throws DAOException;

    /**
     * Trouve tous les prêts.
     *
     * @return La liste des prêts ; une liste vide sinon
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    List<PretDTO> getAll() throws DAOException;

    /**
     * Trouve les prêts non terminés d'un membre.
     *
     * @param idMembre L'ID du membre à trouver
     * @return La liste des prêts correspondants ; une liste vide sinon
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    List<PretDTO> findByMembre(final int idMembre) throws DAOException;

    /**
     * Trouve les livres en cours d'emprunt.
     *
     * @param idLivre L'ID du livre à trouver
     * @return La liste des prêts correspondants ; une liste vide sinon
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    List<PretDTO> findByLivre(final int idLivre) throws DAOException;

    /**
     * Trouve les prêts à partir d'une date de prêt.
     *
     * @param datePret La date de prêt à trouver
     * @return La liste des prêts correspondants ; une liste vide sinon
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    List<PretDTO> findByDatePret(final Timestamp datePret) throws DAOException;

    /**
     * Trouve les prêts à partir d'une date de retour.
     *
     * @param dateRetour La date de retour à trouver
     * @return La liste des prêts correspondants ; une liste vide sinon
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    List<PretDTO> findByDateRetour(final Timestamp dateRetour) throws DAOException;
}
