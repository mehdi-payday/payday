// Fichier IPretService.java
// Auteur : Gilles Benichou
// Date de création : 2016-10-26

package ca.qc.collegeahuntsic.bibliotheque.service.interfaces;

import java.sql.Timestamp;
import java.util.List;
import ca.qc.collegeahuntsic.bibliotheque.dto.PretDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.ServiceException;

/**
 * Interface DAO pour manipuler les prets dans la base de données.
 *
 * @author Team PayDay
 */
public interface IPretService extends IService {
    /**
     * Ajoute un nouveau prêt.
     *
     * @param pretDTO Le prêt à ajouter
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    void add(PretDTO pretDTO) throws ServiceException;

    /**
     * Lit un prêt. Si aucun prêt n'est trouvé, <code>null</code> est retourné.
     *
     * @param idPret L'ID du prêt à lire
     * @return Le prêt lu ; <code>null</code> sinon
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    PretDTO read(int idPret) throws ServiceException;

    /**
     * Met à jour un prêt.
     *
     * @param pretDTO Le prêt à mettre à jour
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    void update(PretDTO pretDTO) throws ServiceException;

    /**
     * Supprime un prêt.
     *
     * @param pretDTO Le prêt à supprimer
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    void delete(PretDTO pretDTO) throws ServiceException;

    /**
     * Trouve tous les prêts.
     *
     * @return La liste des prêts ; une liste vide sinon
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    List<PretDTO> getAll() throws ServiceException;

    /**
     * Trouve les prêts non terminés d'un membre.
     *
     * @param idMembre L'ID du membre à trouver
     * @return La liste des prêts correspondants ; une liste vide sinon
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    List<PretDTO> findByMembre(int idMembre) throws ServiceException;

    /**
     * Trouve les livres en cours d'emprunt.
     *
     * @param idLivre L'ID du livre à trouver
     * @return La liste des prêts correspondants ; une liste vide sinon
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    List<PretDTO> findByLivre(int idLivre) throws ServiceException;

    /**
     * Trouve les prêts à partir d'une date de prêt.
     *
     * @param datePret La date de prêt à trouver
     * @return La liste des prêts correspondants ; une liste vide sinon
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    List<PretDTO> findByDatePret(Timestamp datePret) throws ServiceException;

    /**
     * Trouve les prêts à partir d'une date de retour.
     *
     * @param dateRetour La date de retour à trouver
     * @return La liste des prêts correspondants ; une liste vide sinon
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    List<PretDTO> findByDateRetour(Timestamp dateRetour) throws ServiceException;

    /**
     * Commence un prêt.
     *
     * @param pretDTO Le prêt à commencer
     * @throws ServiceException Si le membre n'existe pas, si le livre n'existe pas, si le livre a été prêté, si le livre a été réservé, si le
     *         membre a atteint sa limite de prêt ou s'il y a une erreur avec la base de données
     */
    void commencer(PretDTO pretDTO) throws ServiceException;

    /**
     * Renouvelle le prêt d'un livre.
     *
     * @param pretDTO Le prêt à renouveler
     * @throws ServiceException Si le prêt n'existe pas, si le livre n'a pas encore été prêté, si le livre a été prêté à quelqu'un d'autre, si
     *         le livre a été réservé ou s'il y a une erreur avec la base de données
     */
    void renouveler(PretDTO pretDTO) throws ServiceException;

    /**
     * Retourne un livre.
     *
     * @param pretDTO Le prêt à terminer
     * @throws ServiceException Si le prêt n'existe pas, si le livre n'a pas encore été prêté, si le livre a été prêté à quelqu'un d'autre ou
     *         s'il y a une erreur avec la base de données
     */
    void retourner(PretDTO pretDTO) throws ServiceException;
}
