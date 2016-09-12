
package ca.qc.collegeahuntsic.bibliotheque.service;

import java.sql.SQLException;
import ca.qc.collegeahuntsic.bibliotheque.dao.InterrogationDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.LivreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.MembreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.PretDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.ReservationDAO;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.exception.BiblioException;

/**
 * Syst�me de gestion d'une biblioth�que
 *
 *<pre>
 * Ce programme permet de g�rer les transaction de base d'une
 * biblioth�que.  Il g�re des livres, des membres et des
 * r�servations. Les donn�es sont conserv�es dans une base de
 * donn�es relationnelles acc�d�e avec JDBC.
 *
 * Pr�-condition
 *   la base de donn�es de la biblioth�que doit exister
 *
 * Post-condition
 *   le programme effectue les maj associ�es � chaque
 *   transaction
 * </pre>
 */
public class GestionBibliotheque {
    public Connexion cx;

    public Livre livre;

    public Membre membre;

    public Reservation reservation;

    public LivreDAO gestionLivre;

    public MembreDAO gestionMembre;

    public PretDAO gestionPret;

    public ReservationDAO gestionReservation;

    public InterrogationDAO gestionInterrogation;

    /**
      * Ouvre une connexion avec la BD relationnelle et
      * alloue les gestionnaires de transactions et de tables.
      * <pre>
      *
      * @param serveur SQL
      * @param bd nom de la bade de donn�es
      * @param user user id pour �tablir une connexion avec le serveur SQL
      * @param password mot de passe pour le user id
      *</pre>
      */
    public GestionBibliotheque(final String serveur,
        final String bd,
        final String user,
        final String password) throws BiblioException,
        SQLException {
        // allocation des objets pour le traitement des transactions
        this.cx = new Connexion(serveur,
            bd,
            user,
            password);
        this.livre = new Livre(this.cx);
        this.membre = new Membre(this.cx);
        this.reservation = new Reservation(this.cx);
        this.gestionLivre = new LivreDAO(this.livre,
            this.reservation);
        this.gestionMembre = new MembreDAO(this.membre,
            this.reservation);
        this.gestionPret = new PretDAO(this.livre,
            this.membre,
            this.reservation);
        this.gestionReservation = new ReservationDAO(this.livre,
            this.membre,
            this.reservation);
        this.gestionInterrogation = new InterrogationDAO(this.cx);
    }

    public void fermer() throws SQLException {
        // fermeture de la connexion
        this.cx.fermer();
    }
}
