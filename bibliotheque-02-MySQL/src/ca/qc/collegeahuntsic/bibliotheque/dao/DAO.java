// Fichier DAO.java
// Auteur : Jeremi Cyr
// Date de création : 2016-09-15

package ca.qc.collegeahuntsic.bibliotheque.dao;

import java.io.Serializable;
import java.sql.Connection;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;

/**
 * Classe de base pour tous les DAOs.
 *
 * @author Mehdi Hamidi
 */
public class DAO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Connexion connexion;

    /**
     * Crée un DAO à partir d'une connexion à la base de données.
     *
     * @param connexion Reçoit une connexion en param
     */
    public DAO(final Connexion connexion) {
        setConnexion(connexion);
    }

    /**
     * Getter de la variable d'instance <code>this.connexion</code>.
     *
     * @return La variable d'instance <code>this.connexion</code>
     */
    public Connexion getConnexion() {
        return this.connexion;
    }

    /**
     * Retourne la {@link java.sql.Connection} JDBC.
     *
     * @return La {@link java.sql.Connection} JDBC.
     */
    protected Connection getConnection() {
        return getConnexion().getConnection();
    }

    /**
     * Setter de la variable d'instance <code>this.connexion</code>.
     *
     * @param connexion La valeur à utiliser pour la variable d'instance <code>this.connexion</code>
     */
    public void setConnexion(final Connexion connexion) {
        this.connexion = connexion;
    }

}
