// Fichier : Connexion.java
// Auteur : Mehdi Hamidi
// Date de creation : 2016-09-15

package ca.qc.collegeahuntsic.bibliotheque.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import ca.qc.collegeahuntsic.bibliotheque.exception.ConnexionException;

/**
 *
 * Cette classe encapsule une connexion JDBC en fonction d'un type et d'une instance de base de données.
 *
 * La méthode getServeursSupportes() indique les type de serveur supportés.
 *
 * Pré-condition : Le driver JDBC approprié doit être accessible.
 * Post-condition : La connexion est créée en mode autocommit false.
 *
 * @author Vincent Laferrière
 */
public class Connexion implements AutoCloseable {

    private Connection conn;

    /**
     *
     * Crée une connexion en mode autocommit false.
     *
     * @param typeServeur - Type de serveur SQL de la BD
     * @param schema - Nom du schéma de la base de données
     * @param nomUtilisateur - Nom d'utilisateur sur le serveur SQL
     * @param motPasse - Mot de passe sur le serveur SQL
     * @throws ConnexionException - S'il y a une erreur avec la base de données
     */
    public Connexion(final String typeServeur,
        final String schema,
        final String nomUtilisateur,
        final String motPasse) throws ConnexionException {
        final Driver d;
        try {
            if("local".equals(typeServeur)) {
                d = (Driver) Class.forName("com.mysql.jdbc.Driver").newInstance();
                DriverManager.registerDriver(d);
                this.conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/"
                    + schema,
                    nomUtilisateur,
                    motPasse);
            } else if("distant".equals(typeServeur)) {
                d = (Driver) Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
                DriverManager.registerDriver(d);
                this.conn = DriverManager.getConnection("jdbc:oracle:thin:@collegeahuntsic.info:1521:"
                    + schema,
                    nomUtilisateur,
                    motPasse);
            } /* else if(serveur.equals("postgres")) {
                 d = (Driver) Class.forName("org.postgresql.Driver").newInstance();
                 DriverManager.registerDriver(d);
                 this.conn = DriverManager.getConnection("jdbc:postgresql:"
                     + bd,
                     user,
                     pass);
              }
               else // access
               {
               d = (Driver) Class.forName("org.postgresql.Driver").newInstance();
               DriverManager.registerDriver(new sun.jdbc.odbc.JdbcOdbcDriver());
               conn = DriverManager.getConnection(
               "jdbc:odbc:" + bd,
               "", "");
               }
               */
            this.conn.setAutoCommit(false);

            final DatabaseMetaData dbmd = this.conn.getMetaData();
            if(dbmd.supportsTransactionIsolationLevel(Connection.TRANSACTION_SERIALIZABLE)) {
                this.conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                System.out.println("Ouverture de la connexion en mode s�rialisable :\n"
                    + "Estampille "
                    + System.currentTimeMillis()
                    + " "
                    + this.conn);
            } else {
                System.out.println("Ouverture de la connexion en mode read committed (default) :\n"
                    + "Heure "
                    + System.currentTimeMillis()
                    + " "
                    + this.conn);
            }
        } catch(SQLException e) {
            throw new ConnexionException(e);
        } catch(Exception e) {
            throw new ConnexionException("JDBC Driver non instanci�");
        }
    }

    /**
     * fermeture d'une connexion.
     * @throws ConnexionException Exception lors de fermeture de connection
     */
    public void fermer() throws ConnexionException {
        try {
            this.conn.rollback();
            this.conn.close();
            System.out.println("Connexion fermée"
                + " "
                + this.conn);
        } catch(SQLException e) {
            throw new ConnexionException(e);
        }
    }

    /**
     *
     * Effectue un commit sur la Connection JDBC.
     *
     * @throws ConnexionException - S'il y a une erreur avec la base de données
     */
    public void commit() throws ConnexionException {
        try {
            this.conn.commit();
        } catch(SQLException e) {
            throw new ConnexionException(e);
        }
    }

    /**
     *
     * Effectue un rollback sur la Connection JDBC.
     *
     * @throws ConnexionException - S'il y a une erreur avec la base de données
     */
    public void rollback() throws ConnexionException {
        try {
            this.conn.rollback();
        } catch(SQLException e) {
            throw new ConnexionException(e);
        }
    }

    /**
     *
     * Getter de la variable d'instance this.connection.
     *
     * @return La variable d'instance this.connection
     */
    public Connection getConnection() {
        return this.conn;
    }

    /**
     *
     * Setter de la variable d'instance this.connection.
     *
     * @param connection - La valeur à utiliser pour la variable d'instance this.connection
     */
    public void setConnection(Connection connection) {
        this.conn = connection;
    }

    /**
     *
     * Retourne la liste des serveurs supportés par ce gestionnaire de connexion.
     * local : MySQL installé localement
     * distant : Oracle installé au Département d'Informatique du Collège Ahuntsic
     * postgres : Postgres installé localement
     * access : Microsoft Access installé localement et inscrit dans ODBC
     *
     * @return La liste des serveurs supportés par ce gestionnaire de connexion
     */
    public static String serveursSupportes() {
        return "local : MySQL installé localement\n"
            + "distant : Oracle installé au Département d'Informatique du Collège Ahuntsic\n"
            + "postgres : Postgres installé localement\n"
            + "access : Microsoft Access installé localement et inscrit dans ODBC";
    }

    /* (non-Javadoc)
     * @see java.lang.AutoCloseable#close()
     */
    @Override
    public void close() throws ConnexionException {
        try {
            this.conn.close();
        } catch(SQLException e) {
            throw new ConnexionException(e);
        }
        // TODO Auto-generated method stub
    }
}
