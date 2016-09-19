// Fichier MembreDAO.java
// Auteur : Jeremi Cyr
// Date de création : 2016-09-15

package ca.qc.collegeahuntsic.bibliotheque.dao;

import java.sql.SQLException;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.MembreDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.BiblioException;
import ca.qc.collegeahuntsic.bibliotheque.service.MembreService;
import ca.qc.collegeahuntsic.bibliotheque.service.ReservationService;

/**
 * 
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
   * 
   * Crée un DAO à partir d'une connexion à la base de données.
   *
   * @param membre
   * @param reservation
   */
    public MembreDAO(MembreService membre,
        ReservationService reservation) {
        super(membre.getConnexion());
        this.cx = membre.getConnexion();
        this.membre = membre;
        this.reservation = reservation;
    }

    /**
     * 
     * Ajout d'un nouveau membre dans la base de donnees. S'il existe deja, une
     * exception est levee.
     *
     * @param idMembre
     * @param nom
     * @param telephone
     * @param limitePret
     * @throws SQLException
     * @throws BiblioException
     * @throws Exception
     */
    public void inscrire(int idMembre,
        String nom,
        long telephone,
        int limitePret) throws SQLException,
        BiblioException,
        Exception {
        try {
            if(this.membre.existe(idMembre)) {
                throw new BiblioException("Membre existe deja: "
                    + idMembre);
            }
            this.membre.inscrire(idMembre,
                nom,
                telephone,
                limitePret);
            this.cx.commit();
        } catch(Exception e) {
            this.cx.rollback();
            throw e;
        }
    }
    
    /**
     * 
     * Suppression d'un membre de la base de donnees.
     *
     * @param idMembre
     * @throws SQLException
     * @throws BiblioException
     * @throws Exception
     */
    public void desinscrire(int idMembre) throws SQLException,
        BiblioException,
        Exception {
        try {
            MembreDTO tupleMembre = this.membre.getMembre(idMembre);
            if(tupleMembre == null) {
                throw new BiblioException("Membre inexistant: "
                    + idMembre);
            }
            if(tupleMembre.getNbPret() > 0) {
                throw new BiblioException("Le membre "
                    + idMembre
                    + " a encore des prets.");
            }
            if(this.reservation.getReservationMembre(idMembre) != null) {
                throw new BiblioException("Membre "
                    + idMembre
                    + " a des r�servations");
            }

            int nb = this.membre.desinscrire(idMembre);
            if(nb == 0) {
                throw new BiblioException("Membre "
                    + idMembre
                    + " inexistant");
            }
            this.cx.commit();
        } catch(Exception e) {
            this.cx.rollback();
            throw e;
        }
    }
}
