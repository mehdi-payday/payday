// Fichier PretService.java
// Auteur : Team PayDay
// Date de création : 2016-05-18

package ca.qc.collegeahuntsic.bibliotheque.service.implementations;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import ca.qc.collegeahuntsic.bibliotheque.dao.implementations.PretDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.interfaces.IPretDAO;
import ca.qc.collegeahuntsic.bibliotheque.dto.LivreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.MembreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.PretDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.ReservationDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.DAOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidCriterionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidCriterionValueException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidHibernateSessionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidSortByPropertyException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dto.InvalidDTOException;
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
    /**
     * Crée le service de la table <code>pret</code>.
     *
     * @param pretDAO Le DAO de la table <code>pret</code>
     * @throws InvalidDAOException Si le DAO de prêt est null, si le DAO de membre est null, si le DAO de livre est null ou si le DAO de réservation est null
     */
    PretService(IPretDAO pretDAO) throws InvalidDAOException {
        super(pretDAO);
    }

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
        if(session == null) {
            throw new InvalidHibernateSessionException("La connexion ne peut être null");
        }
        if(datePret == null) {
            throw new InvalidCriterionException("La date de prêt ne peut être null");
        }
        if(sortByPropertyName == null) {
            throw new InvalidSortByPropertyException("La propriété utilisée pour classer ne peut être null");
        }
        try {
            return ((PretDAO) getDao()).findByDatePret(session,
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

        if(session == null) {
            throw new InvalidHibernateSessionException("La connexion ne peut être null");
        }
        if(dateRetour == null) {
            throw new InvalidCriterionException("La date de retour ne peut être null");
        }
        if(sortByPropertyName == null) {
            throw new InvalidSortByPropertyException("La propriété utilisée pour classer ne peut être null");
        }

        try {
            return ((PretDAO) getDao()).findByDateRetour(session,
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

        if(session == null) {
            throw new InvalidHibernateSessionException("la session hibernate ne peut pas etre null");
        }
        if(pretDTO == null) {
            throw new InvalidDTOException("le pret ne peut etre null");
        }
        List<PretDTO> prets = new ArrayList<>(pretDTO.getLivreDTO().getPrets());

        if(!prets.isEmpty()) {
            final PretDTO unPretDTO = prets.get(0);
            final MembreDTO emprunteur = unPretDTO.getMembreDTO();
            throw new ExistingLoanException("Le livre "
                + pretDTO.getLivreDTO().getTitre()
                + " (ID de livre : "
                + pretDTO.getLivreDTO().getIdLivre()
                + ") a déjà été prêté à "
                + emprunteur.getNom()
                + " (ID de membre : "
                + emprunteur.getIdMembre()
                + ")");

        }
        prets = new ArrayList<>(pretDTO.getMembreDTO().getPrets());

        if(Integer.parseInt(pretDTO.getMembreDTO().getLimitePret()) <= prets.size()) {
            throw new InvalidLoanLimitException("Le membre "
                + pretDTO.getMembreDTO().getNom()
                + " (ID de membre : "
                + pretDTO.getMembreDTO().getIdMembre()
                + ") a atteint sa limite de prêt ("
                + pretDTO.getMembreDTO().getLimitePret()
                + " emprunt(s) maximum)");
        }
        final List<ReservationDTO> reservations = new ArrayList<>(pretDTO.getLivreDTO().getReservations());
        if(!reservations.isEmpty()) {
            final ReservationDTO uneReservationDTO = reservations.get(0);
            final MembreDTO booker = uneReservationDTO.getMembreDTO();
            throw new ExistingReservationException("Le livre "
                + pretDTO.getLivreDTO().getTitre()
                + " (ID de livre : "
                + pretDTO.getLivreDTO().getIdLivre()
                + ") est réservé pour "
                + booker.getNom()
                + " (ID de membre : "
                + booker.getIdMembre()
                + ")");
        }
        pretDTO.setDatePret(new Timestamp(System.currentTimeMillis()));
        pretDTO.setDateRetour(null);
        add(session,
            pretDTO);

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
        if(session == null) {
            throw new InvalidHibernateSessionException("la session hibernate ne peut pas etre null");
        }
        if(pretDTO == null) {
            throw new InvalidDTOException("le pret ne peut etre null");
        }
        final MembreDTO unMembreDTO = pretDTO.getMembreDTO();
        final LivreDTO unLivreDTO = pretDTO.getLivreDTO();
        final List<PretDTO> prets = new ArrayList<>(unMembreDTO.getPrets());

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
            if(unLivreDTO.equals(pretDTODuMembre.getLivreDTO())) {
                aEteEmprunteParMembre = true;
                break;
            }

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
        if(!reservations.isEmpty()) {
            final ReservationDTO uneReservationDTO = reservations.get(0);
            final MembreDTO booker = uneReservationDTO.getMembreDTO();
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
        pretDTO.setDateRetour(null);
        update(session,
            pretDTO);

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

        if(pretDTO == null) {
            throw new InvalidDTOException("le pret ne peut etre null");
        }
        if(session == null) {
            throw new InvalidHibernateSessionException("la session hibernate ne peut pas etre null");
        }
        /*
         * TODO : décider de faire cette vérification ou pas
         * if(pretDTO.getDateRetour() != null) {
            throw new InvalidLoanException("");
        }*/
        final MembreDTO unMembreDTO = pretDTO.getMembreDTO();
        final LivreDTO unLivreDTO = pretDTO.getLivreDTO();
        final List<PretDTO> prets = new ArrayList<>(unMembreDTO.getPrets());

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
            if(unLivreDTO.equals(pretDTODuMembre.getLivreDTO())) {
                aEteEmprunteParMembre = true;
                break;
            }
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
        pretDTO.setDateRetour(new Timestamp(System.currentTimeMillis()));
        update(session,
            pretDTO);

    }
}
