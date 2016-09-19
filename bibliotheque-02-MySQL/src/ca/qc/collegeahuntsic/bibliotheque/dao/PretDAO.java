// Fichier PretDAO.java
// Auteur : Jeremi Cyr
// Date de création : 2016-09-15

package ca.qc.collegeahuntsic.bibliotheque.dao;

import java.sql.Date;
import java.sql.SQLException;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.LivreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.MembreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.ReservationDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.BiblioException;
import ca.qc.collegeahuntsic.bibliotheque.service.LivreService;
import ca.qc.collegeahuntsic.bibliotheque.service.MembreService;
import ca.qc.collegeahuntsic.bibliotheque.service.ReservationService;

/**
 * Gestion des transactions de reliees aux prets de livres aux membres dans
 * une bibliotheque.
 *
 * Ce programme permet de  gerer les transactions preter, renouveler et
 * retourner.
 *
 * Pre-condition la base de donnees de la bibliotheque doit exister
 *
 * Post-condition le programme effectue les maj associees  a chaque
 * transaction
 * 
 */

public class PretDAO extends DAO {

    private static final long serialVersionUID = 1L;

    private LivreService livre;

    private MembreService membre;

    private ReservationService reservation;

    private Connexion cx;

    /**
     * 
     * Creation d'une instance. La connection de l'instance de livre et de
     * membre doit etre la meme que cx, afin d'assurer l'integrite des
     * transactions.     * 
     *
     * @param livre
     * @param membre
     * @param reservation
     * @throws BiblioException
     */
    public PretDAO(LivreService livre,
        MembreService membre,
        ReservationService reservation) throws BiblioException {
        super(livre.getConnexion());
        if(livre.getConnexion() != membre.getConnexion()
            || reservation.getConnexion() != membre.getConnexion()) {
            throw new BiblioException("Les instances de livre, de membre et de reservation n'utilisent pas la meme connexion au serveur");
        }

        this.cx = livre.getConnexion();
        this.livre = livre;
        this.membre = membre;
        this.reservation = reservation;
    }


    /**
     * 
     * Pret d'un livre  un membre. Le livre ne doit pas etre prete. Le
     * membre ne doit pas avoir depasse sa limite de pret.
     * 
     *
     * @param idLivre
     * @param idMembre
     * @param datePret
     * @throws SQLException
     * @throws BiblioException
     * @throws Exception
     */
    public void preter(int idLivre,
        int idMembre,
        String datePret) throws SQLException,
        BiblioException,
        Exception {
        try {
            /* Verfier si le livre est disponible */
            LivreDTO tupleLivre = this.livre.getLivre(idLivre);
            if(tupleLivre == null) {
                throw new BiblioException("Livre inexistant: "
                    + idLivre);
            }
            if(tupleLivre.getIdMembre() != 0) {
                throw new BiblioException("Livre "
                    + idLivre
                    + " deja prete a "
                    + tupleLivre.getIdMembre());
            }

            /* Verifie si le membre existe et sa limite de pret */
            MembreDTO tupleMembre = this.membre.getMembre(idMembre);
            if(tupleMembre == null) {
                throw new BiblioException("Membre inexistant: "
                    + idMembre);
            }
            if(tupleMembre.getNbPret() >= tupleMembre.getLimitePret()) {
                throw new BiblioException("Limite de pret du membre "
                    + idMembre
                    + " atteinte");
            }

            /* Verifie s'il existe une reservation pour le livre */
            ReservationDTO tupleReservation = this.reservation.getReservationLivre(idLivre);
            if(tupleReservation != null) {
                throw new BiblioException("Livre reserve par : "
                    + tupleReservation.getIdMembre()
                    + " idReservation : "
                    + tupleReservation.getIdReservation());
            }

            /* Enregistrement du pret. */
            int nb1 = this.livre.preter(idLivre,
                idMembre,
                datePret);
            if(nb1 == 0) {
                throw new BiblioException("Livre supprime par une autre transaction");
            }
            int nb2 = this.membre.preter(idMembre);
            if(nb2 == 0) {
                throw new BiblioException("Membre supprime par une autre transaction");
            }
            this.cx.commit();
        } catch(Exception e) {
            this.cx.rollback();
            throw e;
        }
    }
    
    /**
     * 
     * Renouvellement d'un pret. Le livre doit etre prete. Le livre ne
     * doit pas etre reserve.
     *
     * @param idLivre
     * @param datePret
     * @throws SQLException
     * @throws BiblioException
     * @throws Exception
     */
    public void renouveler(int idLivre,
        String datePret) throws SQLException,
        BiblioException,
        Exception {
        try {
            /* Verifier si le livre est prete */
            LivreDTO tupleLivre = this.livre.getLivre(idLivre);
            if(tupleLivre == null) {
                throw new BiblioException("Livre inexistant: "
                    + idLivre);
            }
            if(tupleLivre.getIdMembre() == 0) {
                throw new BiblioException("Livre "
                    + idLivre
                    + " n'est pas prete");
            }

            /* Verifier si date renouvellement >= datePret */
            if(Date.valueOf(datePret).before(tupleLivre.getDatePret())) {
                throw new BiblioException("Date de renouvellement inferieure a la date de pret");
            }

            /* Verifie s'il existe une reservation pour le livre */
            ReservationDTO tupleReservation = this.reservation.getReservationLivre(idLivre);
            if(tupleReservation != null) {
                throw new BiblioException("Livre reserve par : "
                    + tupleReservation.getIdMembre()
                    + " idReservation : "
                    + tupleReservation.getIdReservation());
            }

            /* Enregistrement du pret. */
            int nb1 = this.livre.preter(idLivre,
                tupleLivre.getIdMembre(),
                datePret);
            if(nb1 == 0) {
                throw new BiblioException("Livre supprime par une autre transaction");
            }
            this.cx.commit();
        } catch(Exception e) {
            this.cx.rollback();
            throw e;
        }
    }
    
    /**
     * 
     * Retourner un livre prete Le livre doit etre prete.
     *
     * @param idLivre
     * @param dateRetour
     * @throws SQLException
     * @throws BiblioException
     * @throws Exception
     */
    public void retourner(int idLivre,
        String dateRetour) throws SQLException,
        BiblioException,
        Exception {
        try {
            /* Verifier si le livre est prete*/
            LivreDTO tupleLivre = this.livre.getLivre(idLivre);
            if(tupleLivre == null) {
                throw new BiblioException("Livre inexistant: "
                    + idLivre);
            }
            if(tupleLivre.getIdMembre() == 0) {
                throw new BiblioException("Livre "
                    + idLivre
                    + " n'est pas prete ");
            }

            /* Verifier si date retour >= datePret */
            if(Date.valueOf(dateRetour).before(tupleLivre.getDatePret())) {
                throw new BiblioException("Date de retour inferieure a la date de pret");
            }

            /* Retour du pret. */
            int nb1 = this.livre.retourner(idLivre);
            if(nb1 == 0) {
                throw new BiblioException("Livre supprime par une autre transaction");
            }

            int nb2 = this.membre.retourner(tupleLivre.getIdMembre());
            if(nb2 == 0) {
                throw new BiblioException("Livre supprime par une autre transaction");
            }
            this.cx.commit();
        } catch(Exception e) {
            this.cx.rollback();
            throw e;
        }
    }
}
