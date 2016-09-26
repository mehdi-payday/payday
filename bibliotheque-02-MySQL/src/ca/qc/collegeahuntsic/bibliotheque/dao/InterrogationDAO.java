// Fichier InterrogationDAO.java
// Auteur : Jeremi Cyr
// Date de création : 2016-09-15

package ca.qc.collegeahuntsic.bibliotheque.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.exception.ConnexionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.DAOException;

/**
 * Gestion des transactions d'interrogation dans une bibliothèque.
 *
 *   Ce programme permet de faire diverses interrogations
 *   sur l'état de la bibliothèque.
 *
 *   Pré-condition
 *     la base de données de la bibliothèque doit exister
 *
 *   Post-condition
 *     le programme effectue les maj associées à chaque
 *     transaction
 *  @author Mehdi Hamidi
 */

public class InterrogationDAO extends DAO {

    private static final long serialVersionUID = 1L;

    private Connexion connexion;

    private final String qUERYLIVRESTITRESMOTS = "SELECT t1.idLivre, t1.titre, t1.auteur, t1.idmembre, (t1.datePret + 14) FROM livre t1 WHERE lower(titre) LIKE ?";

    private final String qUERYLISTETOUSLIVRES = "SELECT idLivre, titre, auteur, idmembre, datePret FROM livre";

    /**
     * Creation d'une instance.
     *
     * @param connexion Reçoit une connexion en parametre.
     * @throws DAOException Si erreur de SQL throw une SQLException.
     */
    public InterrogationDAO(Connexion connexion) throws DAOException {
        super(connexion);
        this.connexion = connexion;
    }

    /**
     * Affiche les livres contenu un mot dans le titre.
     *
     * @param mot Reçoit un mot en parametre.
     * @throws DAOException si erreur de SQL throw une SQLException.
     */
    public void listerLivresTitre(String mot) throws DAOException {
        PreparedStatement stmtLivresTitreMot = null;
        ResultSet rsetLivresTitreMot = null;
        try {
            stmtLivresTitreMot = this.connexion.getConnection().prepareStatement(this.qUERYLIVRESTITRESMOTS);
            stmtLivresTitreMot.setString(1,
                "%"
                    + mot
                    + "%");
            rsetLivresTitreMot = stmtLivresTitreMot.executeQuery();
            int idMembre;
            System.out.println("idLivre titre auteur idMembre dateRetour");
            while(rsetLivresTitreMot.next()) {
                System.out.print(rsetLivresTitreMot.getInt(1)
                    + " "
                    + rsetLivresTitreMot.getString(2)
                    + " "
                    + rsetLivresTitreMot.getString(3));
                idMembre = rsetLivresTitreMot.getInt(4);
                if(!rsetLivresTitreMot.wasNull()) {
                    System.out.print(" "
                        + idMembre
                        + " "
                        + rsetLivresTitreMot.getDate(5));
                }
                System.out.println();
            }
            rsetLivresTitreMot.close();
            this.connexion.commit();
        } catch(ConnexionException connexionException) {
            try {
                this.connexion.rollback();
            } catch(ConnexionException connexionException2) {
                throw new DAOException(connexionException2);
            }
            throw new DAOException(connexionException);
        } catch(SQLException sqlException) {
            try {
                rsetLivresTitreMot.close();
            } catch(SQLException sqlException2) {
                sqlException2.printStackTrace();
            }
            throw new DAOException(sqlException);
        } finally {
            try {
                if(stmtLivresTitreMot != null) {
                    stmtLivresTitreMot.close();
                }
                if(rsetLivresTitreMot != null) {
                    rsetLivresTitreMot.close();
                }
            } catch(SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }
    }

    /**
     * Affiche tous les livres de la BD.
     *
     * @throws DAOException si erreur de SQL throw une DAOException
     */
    public void listerLivres() throws DAOException {
        PreparedStatement stmtListeTousLivres = null;
        ResultSet rsetListeTousLivres = null;
        try {
            stmtListeTousLivres = this.connexion.getConnection().prepareStatement(this.qUERYLISTETOUSLIVRES);
            rsetListeTousLivres = stmtListeTousLivres.executeQuery();

            System.out.println("idLivre titre auteur idMembre datePret");
            int idMembre;
            while(rsetListeTousLivres.next()) {
                System.out.print(rsetListeTousLivres.getInt("idLivre")
                    + " "
                    + rsetListeTousLivres.getString("titre")
                    + " "
                    + rsetListeTousLivres.getString("auteur"));
                idMembre = rsetListeTousLivres.getInt("idMembre");
                if(!rsetListeTousLivres.wasNull()) {
                    System.out.print(" "
                        + idMembre
                        + " "
                        + rsetListeTousLivres.getDate("datePret"));
                }
                System.out.println();
            }
            this.connexion.commit();
        } catch(ConnexionException connexionException) {
            try {
                this.connexion.rollback();
            } catch(ConnexionException connexionException2) {
                throw new DAOException(connexionException2);
            }
            throw new DAOException(connexionException);
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        } finally {
            try {
                if(stmtListeTousLivres != null) {
                    stmtListeTousLivres.close();
                }
                if(rsetListeTousLivres != null) {
                    rsetListeTousLivres.close();
                }
            } catch(SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }
    }
}
