// Fichier PretService.java
// Auteur : Team PayDay
// Date de création : 2016-05-18

package ca.qc.collegeahuntsic.bibliotheque.service;

import java.sql.Timestamp;
import java.util.List;
import ca.qc.collegeahuntsic.bibliotheque.dao.LivreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.MembreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.ReservationDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.implementations.PretDAO;
import ca.qc.collegeahuntsic.bibliotheque.dto.LivreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.MembreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.PretDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.ReservationDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.DAOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.ServiceException;
import ca.qc.collegeahuntsic.bibliotheque.service.interfaces.IPretService;

/**
 * Service de la table <code>pret</code>.
 *
 * @author Team PayDay
 */
public class PretService extends Service implements IPretService {
    private static final long serialVersionUID = 1L;

    private PretDAO pretDAO;

    private MembreDAO membreDAO;

    private LivreDAO livreDAO;

    private ReservationDAO reservationDAO;

    /**
     * Crée le service de la table <code>pret</code>.
     *
     * @param pretDAO Le DAO de la table <code>pret</code>
     * @param membreDAO Le DAO de la table <code>membre</code>
     * @param livreDAO Le DAO de la table <code>livre</code>
     * @param reservationDAO Le DAO de la table <code>reservation</code>
     */
    public PretService(PretDAO pretDAO,
        MembreDAO membreDAO,
        LivreDAO livreDAO,
        ReservationDAO reservationDAO) {
        super();
        setPretDAO(pretDAO);
        setMembreDAO(membreDAO);
        setLivreDAO(livreDAO);
        setReservationDAO(reservationDAO);
    }

    // Region Getters and Setters
    /**
     * Getter de la variable d'instance <code>this.pretDAO</code>.
     *
     * @return La variable d'instance <code>this.pretDAO</code>
     */
    private PretDAO getPretDAO() {
        return this.pretDAO;
    }

    /**
     * Setter de la variable d'instance <code>this.pretDAO</code>.
     *
     * @param pretDAO La valeur à utiliser pour la variable d'instance <code>this.pretDAO</code>
     */
    private void setPretDAO(PretDAO pretDAO) {
        this.pretDAO = pretDAO;
    }

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
     * Getter de la variable d'instance <code>this.reservationDAO</code>.
     *
     * @return La variable d'instance <code>this.reservationDAO</code>
     */
    private ReservationDAO getReservationDAO() {
        return this.reservationDAO;
    }

    /**
     * Setter de la variable d'instance <code>this.reservationDAO</code>.
     *
     * @param reservationDAO La valeur à utiliser pour la variable d'instance <code>this.reservationDAO</code>
     */
    private void setReservationDAO(ReservationDAO reservationDAO) {
        this.reservationDAO = reservationDAO;
    }
    // EndRegion Getters and Setters

    /**
     * {@inheritDoc}
     */
    @Override
    public void add(PretDTO pretDTO) throws ServiceException {
        try {
            getPretDAO().add(pretDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PretDTO read(String idPret) throws ServiceException {
        try {
            return getPretDAO().read(idPret);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(PretDTO pretDTO) throws ServiceException {
        try {
            getPretDAO().update(pretDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(PretDTO pretDTO) throws ServiceException {
        try {
            getPretDAO().delete(pretDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PretDTO> getAll() throws ServiceException {
        try {
            return getPretDAO().getAll();
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PretDTO> findByMembre(String idMembre) throws ServiceException {
        try {
            return getPretDAO().findByMembre(idMembre);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PretDTO> findByLivre(String idLivre) throws ServiceException {
        try {
            return getPretDAO().findByLivre(idLivre);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PretDTO> findByDatePret(Timestamp datePret) throws ServiceException {
        try {
            return getPretDAO().findByDatePret(datePret);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PretDTO> findByDateRetour(Timestamp dateRetour) throws ServiceException {
        try {
            return getPretDAO().findByDateRetour(dateRetour);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void commencer(PretDTO pretDTO) throws ServiceException {
        try {
            final MembreDTO unMembreDTO = getMembreDAO().read(pretDTO.getMembreDTO().getIdMembre());
            if(unMembreDTO == null) {
                throw new ServiceException("Le membre "
                    + pretDTO.getMembreDTO().getIdMembre()
                    + " n'existe pas");
            }
            final LivreDTO unLivreDTO = getLivreDAO().read(pretDTO.getLivreDTO().getIdLivre());
            if(unLivreDTO == null) {
                throw new ServiceException("Le livre "
                    + pretDTO.getLivreDTO().getIdLivre()
                    + " n'existe pas");
            }
            List<PretDTO> prets = findByLivre(unLivreDTO.getIdLivre());
            if(!prets.isEmpty()) {
                final PretDTO unPretDTO = prets.get(0);
                final MembreDTO emprunteur = getMembreDAO().read(unPretDTO.getMembreDTO().getIdMembre());
                throw new ServiceException("Le livre "
                    + unLivreDTO.getTitre()
                    + " (ID de livre : "
                    + unLivreDTO.getIdLivre()
                    + ") a été prêté à "
                    + emprunteur.getNom()
                    + " (ID de membre : "
                    + emprunteur.getIdMembre()
                    + ")");
            }
            prets = findByMembre(unMembreDTO.getIdMembre());
            if(unMembreDTO.getLimitePret().equals(prets.size()
                + "")) {
                throw new ServiceException("Le membre "
                    + unMembreDTO.getNom()
                    + " (ID de membre : "
                    + unMembreDTO.getIdMembre()
                    + ") a atteint sa limite de prêt ("
                    + unMembreDTO.getLimitePret()
                    + " emprunt(s) maximum)");
            }
            final List<ReservationDTO> reservations = getReservationDAO().findByLivre(unLivreDTO.getIdLivre());
            if(!reservations.isEmpty()) {
                final ReservationDTO uneReservationDTO = reservations.get(0);
                final MembreDTO booker = getMembreDAO().read(uneReservationDTO.getMembreDTO().getIdMembre());
                throw new ServiceException("Le livre "
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
            add(pretDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void renouveler(PretDTO pretDTO) throws ServiceException {
        try {
            final PretDTO unPretDTO = read(pretDTO.getIdPret());
            if(unPretDTO == null) {
                throw new ServiceException("Le prêt "
                    + pretDTO.getIdPret()
                    + " n'existe pas");
            }
            final MembreDTO unMembreDTO = getMembreDAO().read(unPretDTO.getMembreDTO().getIdMembre());
            final LivreDTO unLivreDTO = getLivreDAO().read(unPretDTO.getLivreDTO().getIdLivre());
            final List<PretDTO> prets = findByMembre(unMembreDTO.getIdMembre());
            if(prets.isEmpty()) {
                throw new ServiceException("Le livre "
                    + unLivreDTO.getTitre()
                    + " (ID de livre : "
                    + unLivreDTO.getIdLivre()
                    + ") n'est pas encore prêté");
            }
            boolean aEteEmprunteParMembre = false;
            for(PretDTO unAutrePretDTO : prets) {
                aEteEmprunteParMembre = unMembreDTO.equals(unAutrePretDTO.getMembreDTO())
                    && unLivreDTO.equals(unAutrePretDTO.getLivreDTO());
            }
            if(!aEteEmprunteParMembre) {
                throw new ServiceException("Le livre "
                    + unLivreDTO.getTitre()
                    + " (ID de livre : "
                    + unLivreDTO.getIdLivre()
                    + ") n'a pas été prêté à "
                    + unMembreDTO.getNom()
                    + " (ID de membre : "
                    + unMembreDTO.getIdMembre()
                    + ")");
            }
            final List<ReservationDTO> reservations = getReservationDAO().findByLivre(unLivreDTO.getIdLivre());
            if(!reservations.isEmpty()) {
                final ReservationDTO uneReservationDTO = reservations.get(0);
                final MembreDTO booker = getMembreDAO().read(uneReservationDTO.getMembreDTO().getIdMembre());
                throw new ServiceException("Le livre "
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
            update(unPretDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void retourner(PretDTO pretDTO) throws ServiceException {
        try {
            final PretDTO unPretDTO = read(pretDTO.getIdPret());
            if(unPretDTO == null) {
                throw new ServiceException("Le prêt "
                    + pretDTO.getIdPret()
                    + " n'existe pas");
            }
            final MembreDTO unMembreDTO = getMembreDAO().read(unPretDTO.getMembreDTO().getIdMembre());
            final LivreDTO unLivreDTO = getLivreDAO().read(unPretDTO.getLivreDTO().getIdLivre());
            final List<PretDTO> prets = findByMembre(unMembreDTO.getIdMembre());
            if(prets.isEmpty()) {
                throw new ServiceException("Le livre "
                    + unLivreDTO.getTitre()
                    + " (ID de livre : "
                    + unLivreDTO.getIdLivre()
                    + ") n'est pas encore prêté");
            }
            boolean aEteEmprunteParMembre = false;
            for(PretDTO unAutrePretDTO : prets) {
                aEteEmprunteParMembre = unMembreDTO.equals(unAutrePretDTO.getMembreDTO())
                    && unLivreDTO.equals(unAutrePretDTO.getLivreDTO());
            }
            if(!aEteEmprunteParMembre) {
                throw new ServiceException("Le livre "
                    + unLivreDTO.getTitre()
                    + " (ID de livre : "
                    + unLivreDTO.getIdLivre()
                    + ") n'a pas été prêté à "
                    + unMembreDTO.getNom()
                    + " (ID de membre : "
                    + unMembreDTO.getIdMembre()
                    + ")");
            }
            unPretDTO.setDateRetour(new Timestamp(System.currentTimeMillis()));
            update(unPretDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }
}
