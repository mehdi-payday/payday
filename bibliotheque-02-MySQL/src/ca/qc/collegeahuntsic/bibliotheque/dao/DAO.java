
package ca.qc.collegeahuntsic.bibliotheque.dao;

import java.io.Serializable;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;

public class DAO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Connexion connexion;

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
     * Setter de la variable d'instance <code>this.connexion</code>.
     *
     * @param connexion La valeur à utiliser pour la variable d'instance <code>this.connexion</code>
     */
    public void setConnexion(final Connexion connexion) {
        this.connexion = connexion;
    }

}
