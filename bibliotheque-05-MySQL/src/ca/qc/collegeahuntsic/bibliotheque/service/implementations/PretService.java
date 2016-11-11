// Fichier PretService.java
// Auteur : Team PayDay
// Date de création : 2016-05-18

package ca.qc.collegeahuntsic.bibliotheque.service.implementations;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import ca.qc.collegeahuntsic.bibliotheque.dao.implementations.LivreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.implementations.MembreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.implementations.PretDAO;
import ca.qc.collegeahuntsic.bibliotheque.dto.LivreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.MembreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.PretDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.ReservationDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.DAOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidCriterionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidCriterionValueException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidHibernateSessionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidPrimaryKeyException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidSortByPropertyException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dto.InvalidDTOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dto.MissingDTOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.ExistingLoanException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.ExistingReservationException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.InvalidDAOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.InvalidLoanLimitException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.MissingLoanException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.ServiceException;
import ca.qc.collegeahuntsic.bibliotheque.service.interfaces.IPretService;
import org.hibernate.Session;

/**
 * Service de la table <code>pret</code>.
 *
 * @author Team PayDay
 * @SuppressWarnings({"checkstyle:ClassFanOutComplexity"})
 */
public class PretService extends Service implements IPretService {
    private MembreDAO membreDAO;

    private LivreDAO livreDAO;

    /**
     * Crée le service de la table <code>pret</code>.
     *
     * @param pretDAO Le DAO de la table <code>pret</code>
     * @param membreDAO Le DAO de la table <code>membre</code>
     * @param livreDAO Le DAO de la table <code>livre</code>
     * @param reservationDAO Le DAO de la table <code>reservation</code>
     * @throws InvalidDAOException Si le DAO de prêt est null, si le DAO de membre est null, si le DAO de livre est null ou si le DAO de réservation est null
     */
    public PretService(PretDAO pretDAO,
        MembreDAO membreDAO,
        LivreDAO livreDAO) throws InvalidDAOException {
        super(pretDAO);
        if(pretDAO == null) {
            throw new InvalidDAOException("Le DAO de prêt ne doit pas être null");
        }
        if(membreDAO == null) {
            throw new InvalidDAOException("Le DAO de membre ne peut être null");
        }
        if(livreDAO == null) {
            throw new InvalidDAOException("Le DAO de prêt ne peut être null");
        }
        if(reservationDAO == null) {
            throw new InvalidDAOException("Le DAO de réservation ne peut être null");
        }
        setMembreDAO(membreDAO);
        setLivreDAO(livreDAO);
        //setReservationDAO(reservationDAO);
    }

    // Region Getters and Setters
    /**
     * Getter de la variable d'instance <code>this.pretDAO</code>.
     *
     * @return La variable d'instance <code>this.pretDAO</code>
     */
    private PretDAO getPretDAO() {
        return (PretDAO) getDao();
    }

    /**
     * Setter de la variable d'instance <code>this.pretDAO</code>.
     *
     * @param pretDAO La valeur à utiliser pour la variable d'instance <code>this.pretDAO</code>
     *
    private void setPretDAO(PretDAO pretDAO) {

        //this.pretDAO = pretDAO;
    }*/

    /**
     * Getter de la variable d'instance <code>this.membreDAO</code>.
     *
     * @return La variable d'instance <code>this.membreDAO</code>
     */
    private MembreDAO getMembreDAO() {
        return this.membreDAO;
    }

    /**
     * Setter de la variable d'instance <code>this.membreDAO</code>.
     *
     * @param membreDAO La valeur à utiliser pour la variable d'instance <code>this.membreDAO</code>
     */
    private void setMembreDAO(MembreDAO membreDAO) {
        this.membreDAO = membreDAO;
    }

    /**
     * Getter de la variable d'instance <code>this.livreDAO</code>.
     *
     * @return La variable d'instance <code>this.livreDAO</code>
     */
    private LivreDAO getLivreDAO() {
        return this.livreDAO;
    }

    /**
     * Setter de la variable d'instance <code>this.livreDAO</code>.
     *
     * @param livreDAO La valeur à utiliser pour la variable d'instance <code>this.livreDAO</code>
     */
    private void setLivreDAO(LivreDAO livreDAO) {
        this.livreDAO = livreDAO;
    }

    /**
     * Setter de la variable d'instance <code>this.reservationDAO</code>.
     *
     * @param reservationDAO La valeur à utiliser pour la variable d'instance <code>this.reservationDAO</code>
     *
    private void setReservationDAO(ReservationDAO reservationDAO) {

    }*/
    // EndRegion Getters and Setters

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PretDTO> findByDatePret(Session session,
        Timestamp datePret,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidSortByPropertyException,
        InvalidCriterionValueException,
        ServiceException {
        try {
            return getPretDAO().findByDatePret(session,
                datePret,
                sortByPropertyName);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PretDTO> findByDateRetour(Session session,
        Timestamp dateRetour,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidSortByPropertyException,
        InvalidCriterionValueException,
        ServiceException {
        try {
            return getPretDAO().findByDateRetour(session,
                dateRetour,
                sortByPropertyName);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void commencer(Session session,
        PretDTO pretDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        ExistingLoanException,
        InvalidLoanLimitException,
        ExistingReservationException,
        ServiceException {
        try {
            final MembreDTO unMembreDTO = (MembreDTO) getMembreDAO().get(session,
                pretDTO.getMembreDTO().getIdMembre());

            if(unMembreDTO == null) {
                throw new MissingDTOException("Le membre "
                    + pretDTO.getMembreDTO().getIdMembre()
                    + " n'existe pas");
            }
            final LivreDTO unLivreDTO = (LivreDTO) getLivreDAO().get(session,
                pretDTO.getLivreDTO().getIdLivre());
            if(unLivreDTO == null) {
                throw new InvalidSortByPropertyException("Le livre "
                    + pretDTO.getLivreDTO().getIdLivre()
                    + " n'existe pas");
            }
            List<PretDTO> prets = new ArrayList<>(unLivreDTO.getPrets());

            /*List<PretDTO> prets = findByLivre(session,
                unLivreDTO.getIdLivre(),
                PretDTO.DATE_PRET_COLUMN_NAME);*/

            if(!prets.isEmpty()) {
                final PretDTO unPretDTO = prets.get(0);
                if(unPretDTO.getDateRetour() != null) {
                    final MembreDTO emprunteur = (MembreDTO) getMembreDAO().get(session,
                        unPretDTO.getMembreDTO().getIdMembre());
                    throw new ExistingLoanException("Le livre "
                        + unLivreDTO.getTitre()
                        + " (ID de livre : "
                        + unLivreDTO.getIdLivre()
                        + ") a déjà été prêté à "
                        + emprunteur.getNom()
                        + " (ID de membre : "
                        + emprunteur.getIdMembre()
                        + ")");
                }
            }
            prets = new ArrayList<>(unMembreDTO.getPrets());
            /*
            prets = findByMembre(session,
                unMembreDTO.getIdMembre(),
                MembreDTO.ID_MEMBRE_COLUMN_NAME);*/
            if(unMembreDTO.getLimitePret().equals(prets.size()
                + "")) {
                throw new InvalidLoanLimitException("Le membre "
                    + unMembreDTO.getNom()
                    + " (ID de membre : "
                    + unMembreDTO.getIdMembre()
                    + ") a atteint sa limite de prêt ("
                    + unMembreDTO.getLimitePret()
                    + " emprunt(s) maximum)");
            }
            final List<ReservationDTO> reservations = new ArrayList<>(unLivreDTO.getReservations());
            /*final List<ReservationDTO> reservations = getReservationDAO().findByLivre(session,
                unLivreDTO.getIdLivre(),
                ReservationDTO.DATE_RESERVATION_COLUMN_NAME);*/
            if(!reservations.isEmpty()) {
                final ReservationDTO uneReservationDTO = reservations.get(0);
                final MembreDTO booker = (MembreDTO) getMembreDAO().get(session,
                    uneReservationDTO.getMembreDTO().getIdMembre());
                throw new ExistingReservationException("Le livre "
                    + unLivreDTO.getTitre()
                    + " (ID de livre : "
                    + unLivreDTO.getIdLivre()
                    + ") est réservé pour "
                    + booker.getNom()
                    + " (ID de membre : "
                    + booker.getIdMembre()
                    + ")");
            }
            pretDTO.setDatePret(new Timestamp(System.currentTimeMillis()));

            add(session,
                pretDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        } catch(
            InvalidPrimaryKeyException
            | MissingDTOException
            | InvalidSortByPropertyException exception) {
            // TODO : Find out how to Remove this catch
            throw new ServiceException(exception);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void renouveler(Session session,
        PretDTO pretDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        MissingLoanException,
        ExistingReservationException,
        ServiceException {
        try {
            final PretDTO unPretDTO = (PretDTO) get(session,
                pretDTO.getIdPret());
            if(unPretDTO == null) {
                throw new MissingLoanException("Le prêt "
                    + pretDTO.getIdPret()
                    + " n'existe pas");
            }
            final MembreDTO unMembreDTO = (MembreDTO) getMembreDAO().get(session,
                unPretDTO.getMembreDTO().getIdMembre());
            final LivreDTO unLivreDTO = (LivreDTO) getLivreDAO().get(session,
                unPretDTO.getLivreDTO().getIdLivre());
            final List<PretDTO> prets = new ArrayList<>(unMembreDTO.getPrets());

            /*final List<PretDTO> prets = findByMembre(session,
                unMembreDTO.getIdMembre(),
                PretDTO.DATE_PRET_COLUMN_NAME);*/
            if(prets.isEmpty()) {
                throw new MissingLoanException("Le livre "
                    + unLivreDTO.getTitre()
                    + " (ID de livre : "
                    + unLivreDTO.getIdLivre()
                    + ") n'est pas encore prêté."
                    + "De plus, le membre #"
                    + unMembreDTO.getIdMembre()
                    + " n'a aucun prêts");
            }
            boolean aEteEmprunteParMembre = false;
            for(PretDTO pretDTODuMembre : prets) {
                if(unLivreDTO.equals(pretDTODuMembre.getLivreDTO())
                    && pretDTODuMembre.getDateRetour() == null) {
                    aEteEmprunteParMembre = true;
                    break;
                }
                /*aEteEmprunteParMembre = unMembreDTO.equals(unAutrePretDTO.getMembreDTO())
                    && unLivreDTO.equals(unAutrePretDTO.getLivreDTO());*/
            }
            if(!aEteEmprunteParMembre) {
                throw new MissingLoanException("Le livre "
                    + unLivreDTO.getTitre()
                    + " (ID de livre : "
                    + unLivreDTO.getIdLivre()
                    + ") n'est pas actuellement prêté à "
                    + unMembreDTO.getNom()
                    + " (ID de membre : "
                    + unMembreDTO.getIdMembre()
                    + ")");
            }
            final List<ReservationDTO> reservations = new ArrayList<>(unLivreDTO.getReservations());
            /*final List<ReservationDTO> reservations = getReservationDAO().findByLivre(session,
                unLivreDTO.getIdLivre(),
                ReservationDTO.DATE_RESERVATION_COLUMN_NAME);*/
            if(!reservations.isEmpty()) {
                final ReservationDTO uneReservationDTO = reservations.get(0);
                final MembreDTO booker = (MembreDTO) getMembreDAO().get(session,
                    uneReservationDTO.getMembreDTO().getIdMembre());
                throw new ExistingReservationException("Le livre "
                    + unLivreDTO.getTitre()
                    + " (ID de livre : "
                    + unLivreDTO.getIdLivre()
                    + ") est réservé pour "
                    + booker.getNom()
                    + " (ID de membre : "
                    + booker.getIdMembre()
                    + ")");
            }
            unPretDTO.setDatePret(new Timestamp(System.currentTimeMillis()));
            update(session,
                unPretDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        } catch(InvalidPrimaryKeyException exception) {
            // TODO : Remove out how to remove this catch
            throw new ServiceException(exception);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void terminer(Session session,
        PretDTO pretDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        MissingLoanException,
        ServiceException {
        try {
            final PretDTO unPretDTO = (PretDTO) get(session,
                pretDTO.getIdPret());
            if(unPretDTO == null) {
                throw new MissingLoanException("Le prêt "
                    + pretDTO.getIdPret()
                    + " n'existe pas");
            }
            final MembreDTO unMembreDTO = (MembreDTO) getMembreDAO().get(session,
                unPretDTO.getMembreDTO().getIdMembre());
            final LivreDTO unLivreDTO = (LivreDTO) getLivreDAO().get(session,
                unPretDTO.getLivreDTO().getIdLivre());
            final List<PretDTO> prets = new ArrayList<>(unMembreDTO.getPrets());

            /*final List<PretDTO> prets = findByMembre(session,
                unMembreDTO.getIdMembre(),
                PretDTO.DATE_PRET_COLUMN_NAME);*/
            if(prets.isEmpty()) {
                throw new MissingLoanException("Le livre "
                    + unLivreDTO.getTitre()
                    + " (ID de livre : "
                    + unLivreDTO.getIdLivre()
                    + ") n'est pas prêté par le membre."
                    + " De plus, le membre n'a aucun prêt.");
            }
            boolean aEteEmprunteParMembre = false;
            for(PretDTO pretDTODuMembre : prets) {
                if(unLivreDTO.equals(pretDTODuMembre.getLivreDTO())
                    && pretDTODuMembre.getDateRetour() == null) {
                    aEteEmprunteParMembre = true;
                    break;
                }
                /*aEteEmprunteParMembre = unMembreDTO.equals(pretDTODuMembre.getMembreDTO())
                    && unLivreDTO.equals(pretDTODuMembre.getLivreDTO());*/
            }

            if(!aEteEmprunteParMembre) {
                throw new MissingLoanException("Le livre "
                    + unLivreDTO.getTitre()
                    + " (ID de livre : "
                    + unLivreDTO.getIdLivre()
                    + ") n'est pas actuellement prêté à "
                    + unMembreDTO.getNom()
                    + " (ID de membre : "
                    + unMembreDTO.getIdMembre()
                    + ")");
            }
            unPretDTO.setDateRetour(new Timestamp(System.currentTimeMillis()));
            update(session,
                unPretDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        } catch(InvalidPrimaryKeyException exception) {
            // TODO : Remove  the try/catch when MembreDAO, PretDAO and PretService are ready
            throw new ServiceException(exception);
        }
    }
}
