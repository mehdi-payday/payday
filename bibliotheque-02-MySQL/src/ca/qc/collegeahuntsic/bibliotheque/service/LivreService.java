// Fichier LivreSerivce.java
// Auteur : Adam Cherti
// Date de création : 2016-09-15

package ca.qc.collegeahuntsic.bibliotheque.service;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.LivreDTO;

/**
 * Permet d'effectuer les accès à la table livre.
 */

public class LivreService extends Service {

    private static final long serialVersionUID = 1L;

    private PreparedStatement stmtExiste;

    private PreparedStatement stmtInsert;

    private PreparedStatement stmtUpdate;

    private PreparedStatement stmtDelete;

    private Connexion cx;

    /**
     * Creation d'une instance. Des énoncés SQL pour chaque requête sont
     * précompilés
     */
    public LivreService(Connexion cx) throws SQLException {
        this.cx = cx;
        this.stmtExiste = cx.getConnection()
            .prepareStatement("select idlivre, titre, auteur, dateAcquisition, idMembre, datePret from livre where idlivre = ?");
        this.stmtInsert = cx.getConnection().prepareStatement("insert into livre (idLivre, titre, auteur, dateAcquisition, idMembre, datePret) "
            + "values (?,?,?,?,null,null)");
        this.stmtUpdate = cx.getConnection().prepareStatement("update livre set idMembre = ?, datePret = ? "
            + "where idLivre = ?");
        this.stmtDelete = cx.getConnection().prepareStatement("delete from livre where idlivre = ?");
    }

    /**
     * Retourner la connexion associée
     */
    public Connexion getConnexion() {
        return this.cx;
    }

    /**
     * Verifie si un livre existe
     */
    public boolean existe(int idLivre) throws SQLException {
        this.stmtExiste.setInt(1,
            idLivre);
        ResultSet rset = this.stmtExiste.executeQuery();
        boolean livreExiste = rset.next();
        rset.close();
        return livreExiste;
    }

    /**
     * Lecture d'un livre
     */
    public LivreDTO getLivre(int idLivre) throws SQLException {
        this.stmtExiste.setInt(1,
            idLivre);
        ResultSet rset = this.stmtExiste.executeQuery();
        if(rset.next()) {
            LivreDTO tupleLivre = new LivreDTO();
            tupleLivre.setIdLivre( idLivre );
            tupleLivre.setTitre( rset.getString(2) );
            tupleLivre.setAuteur( rset.getString(3) );
            tupleLivre.setDateAcquisition( rset.getDate(4) );
            tupleLivre.setIdMembre( rset.getInt(5) );
            tupleLivre.setDatePret( rset.getDate(6) );
            return tupleLivre;
        } else {
            return null;
        }
    }

    /**
     * Ajout d'un nouveau livre dans la base de données
     */
    public void acquerir(int idLivre,
        String titre,
        String auteur,
        String dateAcquisition) throws SQLException {
        this.stmtInsert.setInt(1,
            idLivre);
        this.stmtInsert.setString(2,
            titre);
        this.stmtInsert.setString(3,
            auteur);
        this.stmtInsert.setDate(4,
            Date.valueOf(dateAcquisition));
        this.stmtInsert.executeUpdate();
    }

    /**
     * Enregistrement de l'emprunteur d'un livre
     */
    public int preter(int idLivre,
        int idMembre,
        String datePret) throws SQLException {
        this.stmtUpdate.setInt(1,
            idMembre);
        this.stmtUpdate.setDate(2,
            Date.valueOf(datePret));
        this.stmtUpdate.setInt(3,
            idLivre);
        return this.stmtUpdate.executeUpdate();
    }

    /**
     * Rendre le livre disponible (non-prêté)
     */
    public int retourner(int idLivre) throws SQLException {
        this.stmtUpdate.setNull(1,
            Types.INTEGER);
        this.stmtUpdate.setNull(2,
            Types.DATE);
        this.stmtUpdate.setInt(3,
            idLivre);
        return this.stmtUpdate.executeUpdate();
    }

    /**
     * Suppression d'un livre
     */
    public int vendre(int idLivre) throws SQLException {
        this.stmtDelete.setInt(1,
            idLivre);
        return this.stmtDelete.executeUpdate();
    }
}
