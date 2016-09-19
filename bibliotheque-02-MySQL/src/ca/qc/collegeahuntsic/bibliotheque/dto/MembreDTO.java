// Fichier MembreDTO.java
// Auteur : Alexandre Barone
// Date de création : 2016-09-15

package ca.qc.collegeahuntsic.bibliotheque.dto;

/**
 * DTO de la table membre.
 *
 * @author Alexandre Barone
 */

public class MembreDTO extends DTO {

    private static final long serialVersionUID = 1L;

    private int idMembre;

    private int limitePret;

    private int nbPret;

    private String nom;

    private long telephone;

    /**
     *
     * Constructeur par défaut.
     *
     */
    public MembreDTO() {
    }

    // Region Getters and Setters
    /**
     * Setter de la variable d'instance <code>this.idMembre</code>.
     *
     * @param idMembre La valeur à utiliser pour la variable d'instance <code>this.idMembre</code>
     */
    public void setIdMembre(int idMembre) {
        this.idMembre = idMembre;
    }

    /**
     * Getter de la variable d'instance <code>this.idMembre</code>.
     *
     * @return La variable d'instance <code>this.idMembre</code>
     */
    public int getIdMembre() {
        return this.idMembre;
    }

    /**
     * Setter de la variable d'instance <code>this.nom</code>.
     *
     * @param nom La valeur à utiliser pour la variable d'instance <code>this.nom</code>
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Getter de la variable d'instance <code>this.nom</code>.
     *
     * @return La variable d'instance <code>this.nom</code>
     */
    public String getNom() {
        return this.nom;
    }

    /**
     * Setter de la variable d'instance <code>this.telephone</code>.
     *
     * @param telephone La valeur à utiliser pour la variable d'instance <code>this.telephone</code>
     */
    public void setTelephone(long telephone) {
        this.telephone = telephone;
    }

    /**
     * Getter de la variable d'instance <code>this.telephone</code>.
     *
     * @return La variable d'instance <code>this.telephone</code>
     */
    public long getTelephone() {
        return this.telephone;
    }

    /**
     * Getter de la variable d'instance <code>this.limitePret</code>.
     *
     * @return La variable d'instance <code>this.limitePret</code>
     */
    public int getLimitePret() {
        return this.limitePret;
    }

    /**
     * Setter de la variable d'instance <code>this.limitePret</code>.
     *
     * @param limitePret La valeur à utiliser pour la variable d'instance <code>this.limitePret</code>
     */
    public void setLimitePret(int limitePret) {
        this.limitePret = limitePret;
    }

    /**
     * Getter de la variable d'instance <code>this.nbPret</code>.
     *
     * @return La variable d'instance <code>this.nbPret</code>
     */
    public int getNbPret() {
        return this.nbPret;
    }

    /**
     * Setter de la variable d'instance <code>this.nbPret</code>.
     *
     * @param nbPret La valeur à utiliser pour la variable d'instance <code>this.nbPret</code>
     */
    public void setNbPret(int nbPret) {
        this.nbPret = nbPret;
    }

}
