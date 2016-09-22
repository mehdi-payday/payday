// Fichier LivreDAO.java
// Auteur : Jeremi Cyr
// Date de création : 2016-09-15

package ca.qc.collegeahuntsic.bibliotheque.dao;

import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.LivreDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.ConnexionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.DAOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.ServiceException;
import ca.qc.collegeahuntsic.bibliotheque.service.LivreService;
import ca.qc.collegeahuntsic.bibliotheque.service.ReservationService;

/**
 * DAO pour effectuer des CRUDs avec la table livre.
 *
 * @author Mehdi Hamidi
 */
public class LivreDAO extends DAO {

    private static final long serialVersionUID = 1L;

    private LivreService livre;

    private ReservationService reservation;

    private Connexion cx;

    /**
     * Crée un DAO à partir d'une connexion à la base de données.
     *
     * @param livre reçoit un livre en parametre
     * @param reservation reçoit un reservation en parametre
     */
    public LivreDAO(LivreService livre,
        ReservationService reservation) {
        super(livre.getConnexion());
        this.cx = livre.getConnexion();
        this.livre = livre;
        this.reservation = reservation;
    }

    /**
     * Ajout d'un nouveau livre dans la base de données. S'il existe déjà, une
     *      exception est levée.
     *
     * @param idLivre .
     * @param titre .
     * @param auteur .
     * @param dateAcquisition .
     * @throws DAOException .
     */
    public void acquerir(int idLivre,
        String titre,
        String auteur,
        String dateAcquisition) throws DAOException {
        try {
            if(this.livre.existe(idLivre)) {
                throw new DAOException("Livre existe deja: "
                    + idLivre);
            }

            this.livre.acquerir(idLivre,
                titre,
                auteur,
                dateAcquisition);
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
     * Vente d'un livre.
     *
     * @param idLivre .
     * @throws DAOException .
     */
    public void vendre(int idLivre) throws DAOException {
        try {
            final LivreDTO tupleLivre = this.livre.getLivre(idLivre);
            if(tupleLivre == null) {
                throw new DAOException("Livre inexistant: "
                    + idLivre);
            }
            if(tupleLivre.getIdMembre() != 0) {
                throw new DAOException("Livre "
                    + idLivre
                    + " prete a "
                    + tupleLivre.getIdMembre());
            }
            if(this.reservation.getReservationLivre(idLivre) != null) {
                throw new DAOException("Livre "
                    + idLivre
                    + " reserve ");
            }

            final int nb = this.livre.vendre(idLivre);
            if(nb == 0) {
                throw new DAOException("Livre "
                    + idLivre
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
