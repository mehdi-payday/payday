// Fichier MembreService.java
// Auteur : Adam Cherti
// Date de création : 2016-09-15

package ca.qc.collegeahuntsic.bibliotheque.service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.MembreDTO;

/**
 * Permet d'effectuer les acces a la table membre. Cette classe gere tous
 * les acces a la table membre.
 */

public class MembreService extends Service {

    private PreparedStatement stmtExiste;

    private PreparedStatement stmtInsert;

    private PreparedStatement stmtUpdateIncrNbPret;

    private PreparedStatement stmtUpdateDecNbPret;

    private PreparedStatement stmtDelete;

    private Connexion cx;

    /**
     * Creation d'une instance. Precompilation d'enonces SQL.
     */
    public MembreService(Connexion cx) throws SQLException {
        this.cx = cx;
        this.stmtExiste = cx.getConnection().prepareStatement("select idMembre, nom, telephone, limitePret, nbpret from membre where idmembre = ?");
        this.stmtInsert = cx.getConnection().prepareStatement("insert into membre (idmembre, nom, telephone, limitepret, nbpret) "
            + "values (?,?,?,?,0)");
        this.stmtUpdateIncrNbPret = cx.getConnection().prepareStatement("update membre set nbpret = nbPret + 1 where idMembre = ?");
        this.stmtUpdateDecNbPret = cx.getConnection().prepareStatement("update membre set nbpret = nbPret - 1 where idMembre = ?");
        this.stmtDelete = cx.getConnection().prepareStatement("delete from membre where idmembre = ?");
    }

    /**
     * Retourner la connexion associee.
     */
    public Connexion getConnexion() {
        return this.cx;
    }

    /**
     * Verifie si un membre existe.
     */
    public boolean existe(int idMembre) throws SQLException {
        this.stmtExiste.setInt(1,
            idMembre);
        ResultSet rset = this.stmtExiste.executeQuery();
        boolean membreExiste = rset.next();
        rset.close();
        return membreExiste;
    }

    /**
     * Lecture d'un membre.
     */
    public MembreDTO getMembre(int idMembre) throws SQLException {
        this.stmtExiste.setInt(1,
            idMembre);
        ResultSet rset = this.stmtExiste.executeQuery();
        if(rset.next()) {
            MembreDTO tupleMembre = new MembreDTO();
            tupleMembre.idMembre = idMembre;
            tupleMembre.nom = rset.getString(2);
            tupleMembre.telephone = rset.getLong(3);
            tupleMembre.limitePret = rset.getInt(4);
            tupleMembre.nbPret = rset.getInt(5);
            return tupleMembre;
        } else {
            return null;
        }
    }

    /**
     * Ajout d'un nouveau membre.
     */
    public void inscrire(int idMembre,
        String nom,
        long telephone,
        int limitePret) throws SQLException {
        this.stmtInsert.setInt(1,
            idMembre);
        this.stmtInsert.setString(2,
            nom);
        this.stmtInsert.setLong(3,
            telephone);
        this.stmtInsert.setInt(4,
            limitePret);
        this.stmtInsert.executeUpdate();
    }

    /**
     * Incrementer le nb de pret d'un membre.
     */
    public int preter(int idMembre) throws SQLException {
        this.stmtUpdateIncrNbPret.setInt(1,
            idMembre);
        return this.stmtUpdateIncrNbPret.executeUpdate();
    }

    /**
     * Decrementer le nb de pret d'un membre.
     */
    public int retourner(int idMembre) throws SQLException {
        this.stmtUpdateDecNbPret.setInt(1,
            idMembre);
        return this.stmtUpdateDecNbPret.executeUpdate();
    }

    /**
     * Suppression d'un membre.
     */
    public int desinscrire(int idMembre) throws SQLException {
        this.stmtDelete.setInt(1,
            idMembre);
        return this.stmtDelete.executeUpdate();
    }
}
