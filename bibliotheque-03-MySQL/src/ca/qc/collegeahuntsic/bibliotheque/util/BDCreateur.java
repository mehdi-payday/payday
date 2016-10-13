// Fichier BDCreateur.java
// Auteur : Gilles Bénichou
// Date de création : 2016-05-18

package ca.qc.collegeahuntsic.bibliotheque.util;

import java.sql.SQLException;
import java.sql.Statement;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.exception.BDCreateurException;
import ca.qc.collegeahuntsic.bibliotheque.exception.ConnexionException;

/**
 * Utilitaire de création de la base de données.
 *
 * @author Gilles Bénichou
 */
public final class BDCreateur {
    /**
     * Constructeur privé pour empêcher toute instanciation.
     */
    private BDCreateur() {
        super();
    }

    /**
     * Crée la base de données nécessaire à l'application bibliothèque.<br /><br />
     *
     * Paramètres :<br />
     *              0 - Type de serveur SQL de la BD<br />
     *              1 - Nom du schéma de la base de données<br />
     *              2 - Nom d'utilisateur sur le serveur SQL<br />
     *              3 - Mot de passe sur le serveur SQL
     *
     * @param arguments Les arguments du main
     * @throws BDCreateurException S'il y a une erreur avec la connexion ou s'il y a une erreur avec la base de données
     */
    public static void main(String[] arguments) throws BDCreateurException {
        if(arguments.length < 4) {
            System.out.println("Usage : java BDCreateur <type_serveur> <nom_schema> <nom_utilisateur> <mot_passe>");
        } else {
            try(
                Connexion connexion = new Connexion(arguments[0],
                    arguments[1],
                    arguments[2],
                    arguments[3])) {

                try(
                    Statement statement = connexion.getConnection().createStatement()) {

                    statement.executeUpdate("DROP TABLE IF EXISTS reservation CASCADE");
                    statement.executeUpdate("DROP TABLE IF EXISTS livre       CASCADE");
                    statement.executeUpdate("DROP TABLE IF EXISTS membre      CASCADE");

                    statement.executeUpdate("CREATE TABLE membre ("
                        + "                               idMembre   INTEGER(3)   CHECK (idMembre > 0), "
                        + "                               nom        VARCHAR(10)  NOT NULL, "
                        + "                               telephone  BIGINT(10), "
                        + "                               limitePret INTEGER(2)   CHECK (limitePret > 0 AND limitePret <= 10), "
                        + "                               nbPret     INTEGER(2)   DEFAULT 0 CHECK (nbpret >= 0), "
                        + "                               CONSTRAINT cleMembre    PRIMARY KEY (idMembre), "
                        + "                               CONSTRAINT limiteNbPret CHECK (nbPret <= limitePret))");

                    statement.executeUpdate("CREATE TABLE livre ("
                        + "                               idLivre         INTEGER(3)    CHECK (idLivre > 0), "
                        + "                               titre           VARCHAR(10)   NOT NULL, "
                        + "                               auteur          VARCHAR(10)   NOT NULL, "
                        + "                               dateAcquisition TIMESTAMP(3)  NOT NULL, "
                        + "                               idMembre        INTEGER(3), "
                        + "                               datePret        TIMESTAMP(3), "
                        + "                               CONSTRAINT      cleLivre      PRIMARY KEY (idLivre), "
                        + "                               CONSTRAINT      refPretMembre FOREIGN KEY (idMembre) REFERENCES membre (idMembre))");

                    statement.executeUpdate("CREATE TABLE reservation ("
                        + "                               idReservation   INTEGER(3), "
                        + "                               idMembre        INTEGER(3), "
                        + "                               idLivre         INTEGER(3), "
                        + "                               dateReservation TIMESTAMP(3), "
                        + "                               CONSTRAINT      clePrimaireReservation  PRIMARY KEY (idReservation), "
                        + "                               CONSTRAINT      cleEtrangereReservation UNIQUE (idMembre, idLivre), "
                        + "                               CONSTRAINT      refReservationMembre    FOREIGN KEY (idMembre) REFERENCES membre (idMembre) "
                        + "                                                                       ON DELETE CASCADE, "
                        + "                               CONSTRAINT      refReservationLivre     FOREIGN KEY (idLivre) REFERENCES livre (idLivre) "
                        + "                                                                       ON DELETE CASCADE)");

                    connexion.commit();

                }
            } catch(ConnexionException connexionException) {
                throw new BDCreateurException(connexionException);
            } catch(SQLException sqlException) {
                throw new BDCreateurException(sqlException);
            } catch(Exception exception) {
                throw new BDCreateurException(exception);
            }
        }
    }
}
