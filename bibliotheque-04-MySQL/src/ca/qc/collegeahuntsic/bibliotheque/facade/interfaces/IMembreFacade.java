// Fichier IMembreFacade.java
// Auteur : Mehdi Hamidi
// Date de création : 2016-10-26

package ca.qc.collegeahuntsic.bibliotheque.facade.interfaces;

import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.MembreDTO;
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

/**
 * Interface de façade pour manipuler les membres dans la base de données.
 *
 * @author Mehdi Hamidi
 */
public interface IMembreFacade extends IFacade {

    /**
     * Désincrit un membre.
     *
     * @param connexion La connexion à utiliser
     * @param membreDTO Le membre à inscrire
     * @throws InvalidHibernateSessionException - Si la connexion est null
     * @throws InvalidDTOException - Si le livre est null
     * @throws InvalidDTOClassException - Si la classe du membre n'est pas celle que prend en charge le DAO
     * @throws InvalidPrimaryKeyException - Si la clef primaire du membre est null
     * @throws MissingDTOException - Si le membre n'existe pas
     * @throws ExistingLoanException - Si le membre a encore des prêts
     * @throws InvalidCriterionException - Si l'ID du membre est null
     * @throws InvalidSortByPropertyException - Si la propriété à utiliser pour classer est null
     * @throws ExistingReservationException - Si le membre a des réservations
     * @throws FacadeException - S'il y a une erreur avec la base de données
     */
    void desinscrire(final Connexion connexion,
        final MembreDTO membreDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        InvalidDTOClassException,
        InvalidPrimaryKeyException,
        MissingDTOException,
        ExistingLoanException,
        InvalidCriterionException,
        InvalidSortByPropertyException,
        ExistingReservationException,
        FacadeException;

    /**
     * Inscrit un membre.
     *
     * @param connexion La connexion à utiliser
     * @param membreDTO Le membre à inscrire
     * @throws InvalidHibernateSessionException - Si la connexion est null
     * @throws InvalidDTOException - Si le membre est null
     * @throws InvalidDTOClassException - Si la classe du membre n'est pas celle que prend en charge le DAO
     * @throws FacadeException - S'il y a une erreur avec la base de données
     */
    void inscrire(final Connexion connexion,
        final MembreDTO membreDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        InvalidDTOClassException,
        FacadeException;

}
