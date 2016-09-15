// Fichier ReservationDTO.java
// Auteur : Alexandre Barone
// Date de création : 2016-09-15

package ca.qc.collegeahuntsic.bibliotheque.dto;

import java.sql.Date;

/**
 * Permet de représenter un tuple de la table membre.
 *
 */

public class ReservationDTO extends DTO {

    public int idReservation;

    public int idLivre;

    public int idMembre;

    public Date dateReservation;
}
