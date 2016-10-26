// Fichier PretDTO.java
// Auteur : Team PayDay
// Date de création : 2016-05-18

package ca.qc.collegeahuntsic.bibliotheque.dto;

import java.sql.Timestamp;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * DTO de la table <code>pret</code>.
 *
 * @author Team PayDay
 */
public class PretDTO extends DTO {
    public static final String DATE_PRET_COLUMN_NAME = "datePret";

    public static final String DATE_RETOUR_COLUMN_NAME = "dateRetour";

    public static final String ID_LIVRE_COLUMN_NAME = "idLivre";

    public static final String ID_MEMBRE_COLUMN_NAME = "idMembre";

    public static final String ID_PRET_COLUMN_NAME = "idPret";

    private static final long serialVersionUID = 1L;

    private String idPret;

    private MembreDTO membreDTO;

    private LivreDTO livreDTO;

    private Timestamp datePret;

    private Timestamp dateRetour;

    /**
     * Constructeur par défaut.
     */
    public PretDTO() {
        super();
    }

    // Region Getters and Setters
    /**
     * Getter de la variable d'instance <code>this.idPret</code>.
     *
     * @return La variable d'instance <code>this.idPret</code>
     */
    public String getIdPret() {
        return this.idPret;
    }

    /**
     * Setter de la variable d'instance <code>this.idPret</code>.
     *
     * @param idPret La valeur à utiliser pour la variable d'instance <code>this.idPret</code>
     */
    public void setIdPret(final String idPret) {
        this.idPret = idPret;
    }

    /**
     * Getter de la variable d'instance <code>this.membreDTO</code>.
     *
     * @return La variable d'instance <code>this.membreDTO</code>
     */
    public MembreDTO getMembreDTO() {
        return this.membreDTO;
    }

    /**
     * Setter de la variable d'instance <code>this.membreDTO</code>.
     *
     * @param membreDTO La valeur à utiliser pour la variable d'instance <code>this.membreDTO</code>
     */
    public void setMembreDTO(final MembreDTO membreDTO) {
        this.membreDTO = membreDTO;
    }

    /**
     * Getter de la variable d'instance <code>this.livreDTO</code>.
     *
     * @return La variable d'instance <code>this.livreDTO</code>
     */
    public LivreDTO getLivreDTO() {
        return this.livreDTO;
    }

    /**
     * Setter de la variable d'instance <code>this.livreDTO</code>.
     *
     * @param livreDTO La valeur à utiliser pour la variable d'instance <code>this.livreDTO</code>
     */
    public void setLivreDTO(final LivreDTO livreDTO) {
        this.livreDTO = livreDTO;
    }

    /**
     * Getter de la variable d'instance <code>this.datePret</code>.
     *
     * @return La variable d'instance <code>this.datePret</code>
     */
    public Timestamp getDatePret() {
        return this.datePret;
    }

    /**
     * Setter de la variable d'instance <code>this.datePret</code>.
     *
     * @param datePret La valeur à utiliser pour la variable d'instance <code>this.datePret</code>
     */
    public void setDatePret(final Timestamp datePret) {
        this.datePret = datePret;
    }

    /**
     * Getter de la variable d'instance <code>this.dateRetour</code>.
     *
     * @return La variable d'instance <code>this.dateRetour</code>
     */
    public Timestamp getDateRetour() {
        return this.dateRetour;
    }

    /**
     * Setter de la variable d'instance <code>this.dateRetour</code>.
     *
     * @param dateRetour La valeur à utiliser pour la variable d'instance <code>this.dateRetour</code>
     */
    public void setDateRetour(final Timestamp dateRetour) {
        this.dateRetour = dateRetour;
    }
    // EndRegion Getters and Setters

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object obj) {
        boolean equals = this == obj;
        if(!equals) {
            equals = obj != null
                && obj instanceof PretDTO;
            if(equals) {
                final PretDTO pretDTO = (PretDTO) obj;
                final EqualsBuilder equalsBuilder = new EqualsBuilder();
                equalsBuilder.appendSuper(super.equals(pretDTO));
                equalsBuilder.append(getIdPret(),
                    pretDTO.getIdPret());
                equals = equalsBuilder.isEquals();
            }
        }
        return equals;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new HashCodeBuilder(23,
            13);
        hashCodeBuilder.appendSuper(super.hashCode());
        hashCodeBuilder.append(getIdPret());
        return hashCodeBuilder.toHashCode();
    }
}
