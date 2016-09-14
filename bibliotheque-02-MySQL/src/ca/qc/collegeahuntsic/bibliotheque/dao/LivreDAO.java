
package ca.qc.collegeahuntsic.bibliotheque.dao;

import java.sql.SQLException;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.LivreDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.BiblioException;
import ca.qc.collegeahuntsic.bibliotheque.service.LivreService;
import ca.qc.collegeahuntsic.bibliotheque.service.ReservationService;

/**
 * Gestion des transactions reliées à la création et suppresion de
 * livres dans une bibliothèque.
 *
 * Ce programme permet de gérer les transactions reliées à la création et
 * suppresion de livres.
 *
 * Pré-condition la base de données de la bibliothèque doit exister
 *
 * Post-condition le programme effectue les mises à jour associées à chaque
 * transaction
 */
public class LivreDAO {

    private LivreService livre;

    private ReservationService reservation;

    private Connexion cx;

    /**
     * Creation d'une instance
     */
    public LivreDAO(LivreService livre,
        ReservationService reservation) {
        this.cx = livre.getConnexion();
        this.livre = livre;
        this.reservation = reservation;
    }

    /**
     * Ajout d'un nouveau livre dans la base de données. S'il existe déjà, une
     * exception est levée
     */
    public void acquerir(int idLivre,
        String titre,
        String auteur,
        String dateAcquisition) throws SQLException,
        BiblioException,
        Exception {
        try {
            if(this.livre.existe(idLivre)) {
                throw new BiblioException("Livre existe deja: "
                    + idLivre);
            }

            this.livre.acquerir(idLivre,
                titre,
                auteur,
                dateAcquisition);
            this.cx.commit();
        } catch(Exception e) {
            // System.out.println(e);
            this.cx.rollback();
            throw e;
        }
    }

    /**
     * Vente d'un livre.
     */
    public void vendre(int idLivre) throws SQLException,
        BiblioException,
        Exception {
        try {
            LivreDTO tupleLivre = this.livre.getLivre(idLivre);
            if(tupleLivre == null) {
                throw new BiblioException("Livre inexistant: "
                    + idLivre);
            }
            if(tupleLivre.idMembre != 0) {
                throw new BiblioException("Livre "
                    + idLivre
                    + " prete a "
                    + tupleLivre.idMembre);
            }
            if(this.reservation.getReservationLivre(idLivre) != null) {
                throw new BiblioException("Livre "
                    + idLivre
                    + " r�serv� ");
            }

            int nb = this.livre.vendre(idLivre);
            if(nb == 0) {
                throw new BiblioException("Livre "
                    + idLivre
                    + " inexistant");
            }
            this.cx.commit();
        } catch(Exception e) {
            this.cx.rollback();
            throw e;
        }
    }
}
