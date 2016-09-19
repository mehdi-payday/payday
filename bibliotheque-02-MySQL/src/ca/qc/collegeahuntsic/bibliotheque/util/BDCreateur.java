// Fichier BDCreateur.java
// Auteur : Vincent Laferrière
// Date de création : 15 sept. 2016

package ca.qc.collegeahuntsic.bibliotheque.util;

import java.sql.SQLException;
import java.sql.Statement;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;

/**
 *
 * Utilitaire de création de la base de données
 *
 * @author Vincent Laferrière
 */
class BDCreateur {
    public static void main(final String args[]) throws Exception,
        SQLException {

        if(args.length < 3) {
            System.out.println("Usage: java CreerBD <serveur> <bd> <user> <password>");
            return;
        }

        Connexion cx = new Connexion(args[0],
            args[1],
            args[2],
            args[3]);

        try(
            Statement stmt = cx.getConnection().createStatement()) {
            stmt.executeUpdate("DROP TABLE  IF EXISTS reservation");

            stmt.executeUpdate("DROP TABLE IF EXISTS livre");

            stmt.executeUpdate("DROP TABLE IF EXISTS membre");

            stmt.executeUpdate("CREATE TABLE membre ( "
                + "idMembre        int(3) check(idMembre > 0), "
                + "nom             varchar(10) NOT NULL, "
                + "telephone       bigint(10) , "
                + "limitePret      int(2) check(limitePret > 0 and limitePret <= 10) , "
                + "nbpret          int(2) default 0 check(nbpret >= 0) , "
                + "CONSTRAINT cleMembre PRIMARY KEY (idMembre), "
                + "CONSTRAINT limiteNbPret check(nbpret <= limitePret) "
                + ")");

            stmt.executeUpdate("CREATE TABLE livre ( "
                + "idLivre         int(3) check(idLivre > 0) , "
                + "titre           varchar(10) NOT NULL, "
                + "auteur          varchar(10) NOT NULL, "
                + "dateAcquisition date not null, "
                + "idMembre        int(3) , "
                + "datePret        date , "
                + "CONSTRAINT cleLivre PRIMARY KEY (idLivre), "
                + "CONSTRAINT refPretMembre FOREIGN KEY (idMembre) REFERENCES membre(idMembre)"
                + ")");

            stmt.executeUpdate("CREATE TABLE reservation ( "
                + "idReservation   int(3) , "
                + "idMembre        int(3) , "
                + "idLivre         int(3) , "
                + "dateReservation date , "
                + "CONSTRAINT cleReservation PRIMARY KEY (idReservation) , "
                + "CONSTRAINT cleCandidateReservation UNIQUE (idMembre,idLivre) , "
                + "CONSTRAINT refReservationMembre FOREIGN KEY (idMembre) REFERENCES membre(idMembre)"
                + "  ON DELETE CASCADE , "
                + "CONSTRAINT refReservationLivre FOREIGN KEY (idLivre) REFERENCES livre(idLivre) "
                + "  ON DELETE CASCADE "
                + ")");

        }
        cx.fermer();
    }
}