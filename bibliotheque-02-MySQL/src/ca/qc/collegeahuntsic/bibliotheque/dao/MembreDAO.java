// Fichier MembreDAO.java
// Auteur : Jeremi Cyr
// Date de création : 2016-09-15

package ca.qc.collegeahuntsic.bibliotheque.dao;

import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.MembreDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.ConnexionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.DAOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.ServiceException;
import ca.qc.collegeahuntsic.bibliotheque.service.MembreService;
import ca.qc.collegeahuntsic.bibliotheque.service.ReservationService;

/**
 * DAO pour effectuer des CRUDs avec la table membre.
 *
 * @author Mehdi Hamidi
 */

public class MembreDAO extends DAO {

    private static final long serialVersionUID = 1L;

    private Connexion cx;

    private MembreService membre;

    private ReservationService reservation;

    /**
     * Crée un DAO à partir d'une connexion à la base de données.
     *
     * @param membre .
     * @param reservation .
     */
    public MembreDAO(MembreService membre,
        ReservationService reservation) {
        super(membre.getConnexion());
        this.cx = membre.getConnexion();
        this.membre = membre;
        this.reservation = reservation;
    }

    /**
     * Ajout d'un nouveau membre dans la base de donnees. S'il existe deja, une
     *      exception est levee.
     *
     * @param idMembre .
     * @param nom .
     * @param telephone .
     * @param limitePret .
     * @throws DAOException .
     */
    public void inscrire(int idMembre,
        String nom,
        long telephone,
        int limitePret) throws DAOException {
        try {
            if(this.membre.existe(idMembre)) {
                throw new DAOException("Membre existe deja: "
                    + idMembre);
            }
            this.membre.inscrire(idMembre,
                nom,
                telephone,
                limitePret);
            this.cx.commit();
        } catch(ConnexionException connexionException) {
            try {
                this.cx.rollback();
            } catch(ConnexionException connexionException2) {
                throw new DAOException(connexionException2);
            }
            throw new DAOException(connexionException);
        } catch(ServiceException serviceException) {
            throw new DAOException(serviceException);
        }
    }

    /**
     * Suppression d'un membre de la base de donnees.
     *
     * @param idMembre .
     * @throws DAOException .
     */
    public void desinscrire(int idMembre) throws DAOException {
        try {
            final MembreDTO tupleMembre = this.membre.getMembre(idMembre);
            if(tupleMembre == null) {
                throw new DAOException("Membre inexistant: "
                    + idMembre);
            }
            if(tupleMembre.getNbPret() > 0) {
                throw new DAOException("Le membre "
                    + idMembre
                    + " a encore des prets.");
            }
            if(this.reservation.getReservationMembre(idMembre) != null) {
                throw new DAOException("Membre "
                    + idMembre
                    + " a des r�servations");
            }

            final int nb = this.membre.desinscrire(idMembre);
            if(nb == 0) {
                throw new DAOException("Membre "
                    + idMembre
                    + " inexistant");
            }
            this.cx.commit();
        } catch(ConnexionException connexionException) {
            try {
                this.cx.rollback();
            } catch(ConnexionException connexionException2) {
                throw new DAOException(connexionException2);
            }
            throw new DAOException(connexionException);
        } catch(ServiceException serviceException) {
            throw new DAOException(serviceException);
        }
    }
}
