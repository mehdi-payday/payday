// Fichier ReservationService.java
// Auteur : Team PayDay
// Date de création : 2016-05-18

package ca.qc.collegeahuntsic.bibliotheque.service.implementations;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import ca.qc.collegeahuntsic.bibliotheque.dao.interfaces.ILivreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.interfaces.IMembreDAO;
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

    private IMembreDAO membreDAO;

    private IPretDAO pretDAO;

    /**
     * Crée le service de la table <code>reservation</code>.
     *
     * @param reservationDAO Le DAO de la table reservation
     * @param membreDAO Le DAO de la table membre
     * @param livreDAO Le DAO de la table livre
     * @param pretDAO  Le DAO de la table pret
     * @throws InvalidDAOException Si le DAO de réservation est null, si le DAO de membre est null, si le DAO de livre est null ou si le DAO de prêt est null
     */
    public ReservationService(final IReservationDAO reservationDAO,
        final IMembreDAO membreDAO,
        final ILivreDAO livreDAO,
        final IPretDAO pretDAO) throws InvalidDAOException {
        super(reservationDAO);
        if(livreDAO == null) {
            throw new InvalidDAOException("Le DAO de livre ne peut être null");
        }
        if(membreDAO == null) {
            throw new InvalidDAOException("Le DAO de membre ne peut être null");
        }
        if(pretDAO == null) {
            throw new InvalidDAOException("Le DAO de prêt ne peut être null");
        }
        if(reservationDAO == null) {
            throw new InvalidDAOException("Le DAO de réservation ne peut être null");
        }
        setMembreDAO(membreDAO);
        setPretDAO(pretDAO);
    }

    //Region Getters and Setters
    /**
     * Getter de la variable d'instance <code>this.membreDAO</code>.
     *
     * @return La variable d'instance <code>this.membreDAO</code>
     */
    private IMembreDAO getMembreDAO() {
        return this.membreDAO;
    }

    /**
     * Setter de la variable d'instance <code>this.membreDAO</code>.
     *
     * @param membreDAO La valeur à utiliser pour la variable d'instance <code>this.membreDAO</code>
     */
    private void setMembreDAO(final IMembreDAO membreDAO) {
        this.membreDAO = membreDAO;
    }

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
    private void setPretDAO(final IPretDAO pretDAO) {
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
        try {
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
            final MembreDTO emprunteur = (MembreDTO) getMembreDAO().get(session,
                pret.get(0).getMembreDTO().getIdMembre());
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
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        } catch(InvalidPrimaryKeyException exception) {
            throw new ServiceException(exception);
        }

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
        try {
            final ReservationDTO uneReservationDTO = (ReservationDTO) get(session,
                reservationDTO.getIdReservation());
            if(uneReservationDTO == null) {
                throw new ServiceException("La réservation "
                    + reservationDTO.getIdReservation()
                    + " n'existe pas");
            }
            final LivreDTO unLivreDTO = uneReservationDTO.getLivreDTO();
            final MembreDTO unMembreDTO = uneReservationDTO.getMembreDTO();
            final List<ReservationDTO> reservationsTrouvees = new ArrayList<>(unLivreDTO.getReservations());
            if(!reservationsTrouvees.isEmpty()) {
                final ReservationDTO premiereReservationDTO = reservationsTrouvees.get(0);
                if(!premiereReservationDTO.equals(uneReservationDTO)) {
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

            annuler(session,
                uneReservationDTO);

            final PretDTO newPretDTO = new PretDTO();
            newPretDTO.setMembreDTO(unMembreDTO);
            newPretDTO.setLivreDTO(unLivreDTO);
            getPretDAO().add(session,
                newPretDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        } catch(InvalidPrimaryKeyException exception) {
            throw new ServiceException(exception);
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
