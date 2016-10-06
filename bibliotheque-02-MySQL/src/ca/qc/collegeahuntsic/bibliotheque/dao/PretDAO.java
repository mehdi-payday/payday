// Fichier PretDAO.java
// Auteur : Jeremi Cyr
// Date de création : 2016-09-15

package ca.qc.collegeahuntsic.bibliotheque.dao;

import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;

/**
 * Gestion des transactions de reliées aux prêts de livres aux membres dans
 *      une bibliotheque.
 *
 * Ce programme permet de gérer les transactions : prêter, renouveler et
 *      retourner.
 *
 * Pré-condition la base de données de la bibliotheque doit exister
 *
 * Post-condition le programme effectue les mises à jour associées à chaque
 * transaction
 *
 *  @author Mehdi Hamidi
 */

public class PretDAO extends DAO {

    private static final long serialVersionUID = 1L;

    /**
     * Initialise PretDAO avec la connexion.
     *
     * @param connexion {@link java.sql.connexion} la connexion à la base de données
     */
    public PretDAO(Connexion connexion) {
        super(connexion);
    }

    /**
     * Creation d'une instance. La connexion de l'instance de livre et de
     *      membre doit être la même que connexion, afin d'assurer l'intégrité des
     *      transactions.
     *
     * @param livre le service de la table livre
     * @param membre le service de la table membre
     * @param reservation le service de la table reservation
     * @throws DAOException si les service de livre, membre et réservation n'utilisent pas la même connexion au serveur
     *
    public PretDAO(LivreService livre,
        MembreService membre,
        ReservationService reservation) throws DAOException {
        super(livre.getConnexion());
        if(livre.getConnexion() != membre.getConnexion()
            || reservation.getConnexion() != membre.getConnexion()) {
            throw new DAOException("Les instances de livre, de membre et de reservation n'utilisent pas la meme connexion au serveur");
        }
    
        this.connexion = livre.getConnexion();
        this.livre = livre;
        this.membre = membre;
        this.reservation = reservation;
    }*/

    /**
     * Prêt d'un livre à un membre.
     *
     * Pré-conditions :
     * - Le livre doit être existant.
     * - Le livre ne doit pas être déjà prêté à un membre.
     * - Le membre ne doit pas avoir dépassé sa limite de prêts.
     * - Le livre ne doit pas être réservé par un autre membre que le membre qui veut le prêter.
     *
     * Post-conditions :
     * Le livre est marqué comme étant prêté au membre en la date de prêt datePret
     *
     * @param idLivre l'id du livre à prêter
     * @param idMembre l'id du membre à qui prêter le livre
     * @param datePret la date de prêt du livre au membre
     * @throws DAOException si le livre est inexistant, si le livre a déjà été prêté,
     * si la limite de prêts du membre a été atteinte, si le livre est déjà réservé par quelqu'un d'autre que le membre qui essaie de le prêter,
     * ou si le membre/le livre ont été supprimés en cours de transaction
     *
    public void preter(int idLivre,
        int idMembre,
        String datePret) throws DAOException {
        try {
            final LivreDTO livreDTO = this.livre.getLivre(idLivre);
            if(livreDTO == null) {
                throw new DAOException("Livre inexistant: "
                    + idLivre);
            }
            if(livreDTO.getIdMembre() != 0) {
                throw new DAOException("Livre "
                    + idLivre
                    + " deja prete a "
                    + livreDTO.getIdMembre());
            }
            final MembreDTO tupleMembre = this.membre.getMembre(idMembre);
            if(tupleMembre == null) {
                throw new DAOException("Membre inexistant: "
                    + idMembre);
            }
            if(tupleMembre.getNbPret() >= tupleMembre.getLimitePret()) {
                throw new DAOException("Limite de prêts du membre "
                    + idMembre
                    + " atteinte");
            }
            final ReservationDTO tupleReservation = this.reservation.getReservationLivre(idLivre);

            if(tupleReservation != null
                && tupleReservation.getIdMembre() != idMembre) {
                throw new DAOException("Livre reserve par : "
                    + tupleReservation.getIdMembre()
                    + " idReservation : "
                    + tupleReservation.getIdReservation());
            }
            final int livreCountUpdates = this.livre.preter(idLivre,
                idMembre,
                datePret);
            if(livreCountUpdates == 0) {
                throw new DAOException("Livre supprimé par une autre transaction");
            }
            final int membreCountUpdates = this.membre.preter(idMembre);
            if(membreCountUpdates == 0) {
                throw new DAOException("Membre supprimé par une autre transaction");
            }
            this.connexion.commit();
        } catch(ConnexionException connexionException) {
            try {
                this.connexion.rollback();
            } catch(ConnexionException connexionException2) {
                throw new DAOException(connexionException2);
            }
            throw new DAOException(connexionException);
        } catch(ServiceException serviceException) {
            throw new DAOException(serviceException);
        }
    }*/

    /**
     * Renouvellement d'un prêt.
     *
     * Pré-conditions :
     * - Le livre est déjà prêté à un membre.
     * - Le livre ne doit pas être déjà réservé par un membre.
     * Post-conditions :
     * La datePret du livre est réaffectée à la date de renouvellement
     *
     * @param idLivre l'id du livre à renouveler
     * @param dateRenouvellement la date de renouvellement
     * @throws DAOException si le livre est inexistant ou n'est pas prêté, si sa date de renouvellement est inférieure à la date de prêt ultérieure ou si le livre a déjà été réservé
     *
    public void renouveler(int idLivre,
        String dateRenouvellement) throws DAOException {
        try {
            final LivreDTO livreDTO = this.livre.getLivre(idLivre);
            if(livreDTO == null) {
                throw new DAOException("Livre inexistant: "
                    + idLivre);
            }
            if(livreDTO.getIdMembre() == 0) {
                throw new DAOException("Livre "
                    + idLivre
                    + " n'est pas prete");
            }
            if(Date.valueOf(dateRenouvellement).before(livreDTO.getDatePret())) {
                throw new DAOException("Date de renouvellement inférieure à la date de prêt.");
            }
            final ReservationDTO tupleReservation = this.reservation.getReservationLivre(idLivre);
            if(tupleReservation != null) {
                throw new DAOException("Livre "
                    + livreDTO.toString()
                    + " déjà réservé par : "
                    + tupleReservation.getIdMembre()
                    + " idReservation : "
                    + tupleReservation.getIdReservation());
            }
            final int countUpdates = this.livre.preter(idLivre,
                livreDTO.getIdMembre(),
                dateRenouvellement);
            if(countUpdates == 0) {
                throw new DAOException("Livre supprime par une autre transaction");
            }
            this.connexion.commit();
        } catch(ConnexionException connexionException) {
            try {
                this.connexion.rollback();
            } catch(ConnexionException connexionException2) {
                throw new DAOException(connexionException2);
            }
            throw new DAOException(connexionException);
        } catch(ServiceException serviceException) {
            throw new DAOException(serviceException);
        }
    }*/

    /**
     *
     * Retourne un livre prêté.
     *
     * Pré-conditions :
     * Le livre existe et est être déjà prêté à quelqu'un.
     *
     * Post-conditions :
     * Le membre est marqué comme n'ayant pas de livre prêté
     * Le livre est marqué comme n'étant plus prêté au membre
     *
     * @param idLivre l'id du livre
     * @param dateRetour la date de retour du livre. Doit être supérieur a sa date de prêt.
     * @throws DAOException Si le livre est inexistant, le livre n'est pas prêté, la date de retour est inférieur a la date de prêt ou le livre a été supprimé par une autre transaction
     *
     *
    public void retourner(int idLivre,
        String dateRetour) throws DAOException {
        try {
            final LivreDTO livreDTO = this.livre.getLivre(idLivre);
            if(livreDTO == null) {
                throw new DAOException("Livre inexistant: "
                    + idLivre);
            }
            if(livreDTO.getIdMembre() == 0) {
                throw new DAOException("Livre "
                    + idLivre
                    + " n'est pas prete ");
            }
            if(Date.valueOf(dateRetour).before(livreDTO.getDatePret())) {
                throw new DAOException("Date de retour inferieure a la date de pret");
            }
            final int livreRetourCountUpdates = this.livre.retourner(idLivre);
            if(livreRetourCountUpdates == 0) {
                throw new DAOException("Livre supprime par une autre transaction");
            }

            final int membreRetourCountUpdates = this.membre.retourner(livreDTO.getIdMembre());
            if(membreRetourCountUpdates == 0) {
                throw new DAOException("Livre supprime par une autre transaction");
            }
            this.connexion.commit();
        } catch(ConnexionException connexionException) {
            try {
                this.connexion.rollback();
            } catch(ConnexionException rollbackConnexionException) {
                throw new DAOException(rollbackConnexionException);
            }
            throw new DAOException(connexionException);
        } catch(ServiceException serviceException) {
            throw new DAOException(serviceException);
        }
    }*/
}
