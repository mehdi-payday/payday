// Fichier MembreDTO.java
// Auteur : Alexandre Barone
// Date de création : 2016-09-15

package ca.qc.collegeahuntsic.bibliotheque.dto;

/**
 * Permet de representer un tuple de la table membre.
 */

public class MembreDTO extends DTO {
    public int idMembre;

    public String nom;

    public long telephone;

    public int limitePret;

    public int nbPret;
}
