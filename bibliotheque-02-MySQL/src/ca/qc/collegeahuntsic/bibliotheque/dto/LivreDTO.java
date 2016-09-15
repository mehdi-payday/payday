
package ca.qc.collegeahuntsic.bibliotheque.dto;

import java.sql.Date;

/**
 * Permet de représenter une entrée de la table livre.
 *
 */

public class LivreDTO extends DTO {

    public int idLivre;

    public String titre;

    public String auteur;

    public Date dateAcquisition;

    public int idMembre;

    public Date datePret;
}
