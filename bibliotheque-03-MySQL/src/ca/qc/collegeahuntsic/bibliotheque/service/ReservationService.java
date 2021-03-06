// Fichier ReservationService.java
// Auteur : Team PayDay
// Date de création : 2016-05-18

package ca.qc.collegeahuntsic.bibliotheque.service;

import java.sql.Timestamp;
import java.util.List;
import ca.qc.collegeahuntsic.bibliotheque.dao.LivreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.MembreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.PretDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.ReservationDAO;
import ca.qc.collegeahuntsic.bibliotheque.dto.LivreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.MembreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.PretDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.ReservationDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.DAOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.ServiceException;

/**
 * Service de la table <code>reservation</code>.
 *
 * @author Team PayDay
 */
public class ReservationService extends Service {
    private static final long serialVersionUID = 1L;

    private ReservationDAO reservationDAO;

    private LivreDAO livreDAO;

    private MembreDAO membreDAO;

    private PretDAO pretDAO;

    /**
     * Crée le service de la table <code>reservation</code>.
     *
     * @param reservationDAO Le DAO de la table <code>reservation</code>
     * @param membreDAO Le DAO de la table <code>membre</code>
     * @param livreDAO Le DAO de la table <code>livre</code>
     * @param pretDAO Le DAO de la table <code>pret</code>
     */
    public ReservationService(final ReservationDAO reservationDAO,
        final LivreDAO livreDAO,
        final MembreDAO membreDAO,
        final PretDAO pretDAO) {
        super();
        setReservationDAO(reservationDAO);
        setMembreDAO(membreDAO);
        setLivreDAO(livreDAO);
        setPretDAO(pretDAO);
    }

    // Region Getters and Setters
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
    private void setReservationDAO(final ReservationDAO reservationDAO) {
        this.reservationDAO = reservationDAO;
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
    private void setLivreDAO(final LivreDAO livreDAO) {
        this.livreDAO = livreDAO;
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
    private void setMembreDAO(final MembreDAO membreDAO) {
        this.membreDAO = membreDAO;
    }

    /**
     * Setter de la variable d'instance <code>this.pretDAO</code>.
     *
     * @param pretDAO La valeur à utiliser pour la variable d'instance <code>this.pretDAO</code>
     */
    private void setPretDAO(final PretDAO pretDAO) {
        this.pretDAO = pretDAO;
    }

    /**
     * Getter de la variable d'instance <code>this.pretDAO</code>.
     *
     * @return La variable d'instance <code>this.pretDAO</code>
     */
    public PretDAO getPretDAO() {
        return this.pretDAO;
    }

    // EndRegion Getters and Setters

    /**
     * Ajoute une nouvelle réservation.
     *
     * @param reservationDTO La réservation à ajouter
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    public void add(final ReservationDTO reservationDTO) throws ServiceException {
        try {
            getReservationDAO().add(reservationDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * Lit une réservation. Si aucune réservation n'est trouvée, <code>null</code> est retourné.
     *
     * @param idReservation L'ID de la réservation à lire
     * @return La réservation lue ; <code>null</code> sinon
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    public ReservationDTO read(final int idReservation) throws ServiceException {
        try {
            return getReservationDAO().read(idReservation);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * Met à jour une réservation.
     *
     * @param reservationDTO La réservation à mettre à jour
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    public void update(final ReservationDTO reservationDTO) throws ServiceException {
        try {
            getReservationDAO().update(reservationDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * Supprime une réservation.
     *
     * @param reservationDTO La réservation à supprimer
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    public void delete(final ReservationDTO reservationDTO) throws ServiceException {
        try {
            getReservationDAO().delete(reservationDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * Trouve toutes les réservations.
     *
     * @return La liste des réservations ; une liste vide sinon
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    public List<ReservationDTO> getAll() throws ServiceException {
        try {
            return getReservationDAO().getAll();
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * Trouve les réservations à partir d'un livre.
     *
     * @param idLivre L'ID du livre à utiliser
     * @return La liste des réservations correspondantes, triée par date de réservation croissante ; une liste vide sinon
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    public List<ReservationDTO> findByLivre(final int idLivre) throws ServiceException {
        try {
            return getReservationDAO().findByLivre(idLivre);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * Trouve les réservations à partir d'un membre.
     *
     * @param idMembre L'ID du membre à utiliser
     * @return La liste des réservations correspondantes ; une liste vide sinon
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    public List<ReservationDTO> findByMembre(final int idMembre) throws ServiceException {
        try {
            return getReservationDAO().findByMembre(idMembre);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * Réserve un livre.
     *
     * @param reservationDTO La réservation à créer
     * @throws ServiceException Si la réservation existe déjà, si le membre n'existe pas, si le livre n'existe pas, si le livre n'a pas encore
     *         été prêté, si le livre est déjà prêté au membre, si le membre a déjà réservé ce livre ou s'il y a une erreur avec la base de
     *         données
     */
    public void reserver(final ReservationDTO reservationDTO) throws ServiceException {
        try {
            final ReservationDTO uneReservationDTO = read(reservationDTO.getIdReservation());
            if(uneReservationDTO != null) {
                throw new ServiceException("La réservation "
                    + reservationDTO.getIdReservation()
                    + " existe déjà");
            }
            final MembreDTO unMembreDTO = getMembreDAO().read(reservationDTO.getMembreDTO().getIdMembre());
            if(unMembreDTO == null) {
                throw new ServiceException("Le membre "
                    + reservationDTO.getMembreDTO().getIdMembre()
                    + " n'existe pas");
            }
            final LivreDTO unLivreDTO = getLivreDAO().read(reservationDTO.getLivreDTO().getIdLivre());
            if(unLivreDTO == null) {
                throw new ServiceException("Le livre "
                    + reservationDTO.getLivreDTO().getIdLivre()
                    + " n'existe pas");
            }
            final List<PretDTO> pret = getPretDAO().findByLivre(unLivreDTO.getIdLivre());
            if(pret.isEmpty()) {
                throw new ServiceException("Le livre "
                    + unLivreDTO.getTitre()
                    + " (ID de livre : "
                    + unLivreDTO.getIdLivre()
                    + ") n'est pas encore prêté");
            }
            final MembreDTO emprunteur = getMembreDAO().read(pret.get(0).getMembreDTO().getIdMembre());
            if(unMembreDTO.getIdMembre() == emprunteur.getIdMembre()) {
                throw new ServiceException("Le livre "
                    + unLivreDTO.getTitre()
                    + " (ID de livre : "
                    + unLivreDTO.getIdLivre()
                    + ") est déjà prêté à "
                    + emprunteur.getNom()
                    + " (ID de membre : "
                    + emprunteur.getIdMembre()
                    + ")");
            }
            final List<ReservationDTO> reservations = getReservationDAO().findByMembre(unMembreDTO.getIdMembre());
            for(ReservationDTO uneAutreReservationDTO : reservations) {
                if(uneAutreReservationDTO.getLivreDTO().getIdLivre() == unLivreDTO.getIdLivre()) {
                    throw new ServiceException("Le livre "
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
            add(reservationDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * Utilise une réservation.
     *
     * @param reservationDTO La réservation à utiliser
     *
     * @throws ServiceException  Si la réservation n'existe pas, si la réservation n'est pas la première de la liste,
     *      si le livre est déjà prêté, si le membre a atteint sa limite de prêt ou s'il y a une erreur avec la base de données
     */
    public void utiliser(final ReservationDTO reservationDTO) throws ServiceException {
        try {
            final ReservationDTO uneReservationDTO = read(reservationDTO.getIdReservation());
            if(uneReservationDTO == null) {
                throw new ServiceException("La réservation "
                    + reservationDTO.getIdReservation()
                    + " n'existe pas");
            }
            final LivreDTO unLivreDTO = getLivreDAO().read(uneReservationDTO.getLivreDTO().getIdLivre());
            final MembreDTO unMembreDTO = getMembreDAO().read(uneReservationDTO.getMembreDTO().getIdMembre());
            if(!findByLivre(unLivreDTO.getIdLivre()).get(0).equals(uneReservationDTO)) {
                throw new ServiceException("Le livre "
                    + unLivreDTO.getTitre()
                    + "(ID de livre : "
                    + unLivreDTO.getIdLivre()
                    + ") est réservé pour "
                    + unMembreDTO.getNom()
                    + " (ID de membre : "
                    + unMembreDTO.getIdMembre()
                    + ")");
            }
            if(!getPretDAO().findByLivre(unLivreDTO.getIdLivre()).isEmpty()) {
                throw new ServiceException("ID de livre : "
                    + unLivreDTO.getIdLivre()
                    + " est deja prêté");
            }
            if(getPretDAO().findByMembre(unMembreDTO.getIdMembre()).size() == unMembreDTO.getLimitePret()) {
                throw new ServiceException("Le membre "
                    + unMembreDTO.getNom()
                    + " (ID de membre : "
                    + unMembreDTO.getIdMembre()
                    + ") a atteint sa limite de prêt ("
                    + unMembreDTO.getLimitePret()
                    + " emprunt(s) maximum)");
            }

            annuler(uneReservationDTO);

            final PretDTO newPretDTO = new PretDTO();
            newPretDTO.setMembreDTO(unMembreDTO);
            newPretDTO.setLivreDTO(unLivreDTO);
            getPretDAO().add(newPretDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * Annule une réservation.
     *
     * @param reservationDTO Le reservation à annuler
     * @throws ServiceException Si la réservation n'existe pas ou s'il y a une erreur avec la base de données
     */
    public void annuler(final ReservationDTO reservationDTO) throws ServiceException {
        final ReservationDTO uneReservationDTO = read(reservationDTO.getIdReservation());
        if(uneReservationDTO == null) {
            throw new ServiceException("La réservation "
                + reservationDTO.getIdReservation()
                + " n'existe pas");
        }
        delete(uneReservationDTO);
    }
}
