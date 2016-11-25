// Fichier ReservationService.java
// Auteur : Team PayDay
// Date de création : 2016-05-18

package ca.qc.collegeahuntsic.bibliotheque.service.implementations;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import ca.qc.collegeahuntsic.bibliotheque.dao.interfaces.IPretDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.interfaces.IReservationDAO;
import ca.qc.collegeahuntsic.bibliotheque.dto.LivreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.MembreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.PretDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.ReservationDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.DAOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidHibernateSessionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidPrimaryKeyException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dto.InvalidDTOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.ExistingLoanException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.ExistingReservationException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.InvalidDAOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.InvalidLoanLimitException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.MissingLoanException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.ServiceException;
import ca.qc.collegeahuntsic.bibliotheque.service.interfaces.IReservationService;
import org.hibernate.Session;

/**
 * Service de la table <code>reservation</code>.
 *
 * @author Team PayDay
 */
public class ReservationService extends Service implements IReservationService {
    private IPretDAO pretDAO;

    /**
     * Crée le service de la table <code>reservation</code>.
     *
     * @param reservationDAO Le DAO de la table reservation
     * @param pretDAO Le DAO de la table Pret
     * @throws InvalidDAOException Si le DAO de réservation est null, si le DAO de membre est null, si le DAO de livre est null ou si le DAO de prêt est null
     */
    public ReservationService(IReservationDAO reservationDAO,
        IPretDAO pretDAO) throws InvalidDAOException {
        super(reservationDAO);
        if(pretDAO == null) {
            throw new InvalidDAOException("Le DAO de pret ne peut être null");
        }
        setPretDAO(pretDAO);
    }

    // Region Getters and Setters
    /**
     * Getter de la variable d'instance <code>this.pretDAO</code>.
     *
     * @return La variable d'instance <code>this.pretDAO</code>
     */
    private IPretDAO getPretDAO() {
        return this.pretDAO;
    }

    /**
     * Setter de la variable d'instance <code>this.pretDAO</code>.
     *
     * @param pretDAO La valeur à utiliser pour la variable d'instance <code>this.pretDAO</code>
     */
    private void setPretDAO(IPretDAO pretDAO) {
        this.pretDAO = pretDAO;
    }
    // EndRegion Getters and Setters

    /**
    * {@inheritDoc}
    */
    @Override
    public void placer(final Session session,
        final ReservationDTO reservationDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        MissingLoanException,
        ExistingLoanException,
        ExistingReservationException,
        ServiceException {

        if(session == null) {
            throw new InvalidHibernateSessionException("la session hibernate ne peut pas etre null");
        }
        if(reservationDTO == null) {
            throw new InvalidDTOException("la réservation ne peut etre null");
        }

        final MembreDTO unMembreDTO = reservationDTO.getMembreDTO();
        if(unMembreDTO == null) {
            throw new InvalidDTOException("Le membre "
                + reservationDTO.getMembreDTO().getIdMembre()
                + " n'existe pas");
        }
        final LivreDTO unLivreDTO = reservationDTO.getLivreDTO();
        if(unLivreDTO == null) {
            throw new InvalidDTOException("Le livre "
                + reservationDTO.getLivreDTO().getIdLivre()
                + " n'existe pas");
        }
        final List<PretDTO> pret = new ArrayList<>(unLivreDTO.getPrets());
        if(pret.isEmpty()) {
            throw new MissingLoanException("Le livre "
                + unLivreDTO.getTitre()
                + " (ID de livre : "
                + unLivreDTO.getIdLivre()
                + ") n'est pas encore prêté");
        }
        final MembreDTO emprunteur = pret.get(0).getMembreDTO();
        if(unMembreDTO.getIdMembre() == emprunteur.getIdMembre()) {
            throw new ExistingLoanException("Le livre "
                + unLivreDTO.getTitre()
                + " (ID de livre : "
                + unLivreDTO.getIdLivre()
                + ") est déjà prêté à "
                + emprunteur.getNom()
                + " (ID de membre : "
                + emprunteur.getIdMembre()
                + ")");
        }
        final List<ReservationDTO> reservations = new ArrayList<>(unMembreDTO.getReservations());
        for(ReservationDTO uneAutreReservationDTO : reservations) {
            if(uneAutreReservationDTO.getLivreDTO().getIdLivre() == unLivreDTO.getIdLivre()) {
                throw new ExistingReservationException("Le livre "
                    + unLivreDTO.getTitre()
                    + " (ID de livre : "
                    + unLivreDTO.getIdLivre()
                    + ") est déjà réservé à "
                    + emprunteur.getNom()
                    + " (ID de membre : "
                    + emprunteur.getIdMembre()
                    + ")");
            }
        }
        reservationDTO.setDateReservation(new Timestamp(System.currentTimeMillis()));
        add(session,
            reservationDTO);

    }

    /**
    * {@inheritDoc}
    */
    @Override
    public void utiliser(final Session session,
        final ReservationDTO reservationDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        ExistingReservationException,
        ExistingLoanException,
        InvalidLoanLimitException,
        ServiceException {

        if(session == null) {
            throw new InvalidHibernateSessionException("la session hibernate ne peut pas etre null");
        }
        if(reservationDTO == null) {
            throw new InvalidDTOException("la réservation ne peut etre null");
        }
        try {
            final LivreDTO unLivreDTO = reservationDTO.getLivreDTO();
            final MembreDTO unMembreDTO = reservationDTO.getMembreDTO();
            final List<ReservationDTO> reservationsTrouvees = new ArrayList<>(unLivreDTO.getReservations());
            if(!reservationsTrouvees.isEmpty()) {
                final ReservationDTO premiereReservationDTO = reservationsTrouvees.get(0);
                if(!premiereReservationDTO.equals(reservationDTO)) {
                    final MembreDTO booker = premiereReservationDTO.getMembreDTO();
                    throw new ExistingReservationException("Le livre "
                        + unLivreDTO.getTitre()
                        + "(ID de livre : "
                        + unLivreDTO.getIdLivre()
                        + ") est réservé pour "
                        + booker.getNom()
                        + " (ID de membre : "
                        + booker.getIdMembre()
                        + ")");
                }
            }
            if(!unLivreDTO.getPrets().isEmpty()) {
                throw new ExistingReservationException("ID de livre : "
                    + unLivreDTO.getIdLivre()
                    + " est deja prêté");
            }
            if(unMembreDTO.getPrets().size() == Integer.parseInt(unMembreDTO.getLimitePret())) {
                throw new InvalidLoanLimitException("Le membre "
                    + unMembreDTO.getNom()
                    + " (ID de membre : "
                    + unMembreDTO.getIdMembre()
                    + ") a atteint sa limite de prêt ("
                    + unMembreDTO.getLimitePret()
                    + " emprunt(s) maximum)");
            }

            final PretDTO newPretDTO = new PretDTO();
            newPretDTO.setMembreDTO(unMembreDTO);
            newPretDTO.setLivreDTO(unLivreDTO);
            newPretDTO.setDatePret(new Timestamp(System.currentTimeMillis()));
            newPretDTO.setDateRetour(null);
            getPretDAO().add(session,
                newPretDTO);
            annuler(session,
                reservationDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void annuler(final Session session,
        final ReservationDTO reservationDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        ServiceException {

        if(session == null) {
            throw new InvalidHibernateSessionException("la session hibernate ne peut pas etre null");
        }
        if(reservationDTO == null) {
            throw new InvalidDTOException("la réservation ne peut etre null");
        }

        final ReservationDTO uneReservationDTO;
        try {
            uneReservationDTO = (ReservationDTO) get(session,
                reservationDTO.getIdReservation());
            if(uneReservationDTO == null) {
                throw new ServiceException("La réservation "
                    + reservationDTO.getIdReservation()
                    + " n'existe pas");
            }
            delete(session,
                uneReservationDTO);
        } catch(InvalidPrimaryKeyException exception) {
            throw new ServiceException(exception);
        }

    }
}
