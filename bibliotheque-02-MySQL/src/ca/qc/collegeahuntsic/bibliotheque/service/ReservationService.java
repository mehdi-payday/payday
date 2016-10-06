// Fichier ReservationService.java
// Auteur : Adam Cherti
// Date de création : 2016-09-15

package ca.qc.collegeahuntsic.bibliotheque.service;

import java.util.Collections;
import java.util.List;
import ca.qc.collegeahuntsic.bibliotheque.dao.LivreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.MembreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.ReservationDAO;
import ca.qc.collegeahuntsic.bibliotheque.dto.LivreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.MembreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.ReservationDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.DAOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.ServiceException;

/**
 * Service de la table reservation.
 *
 * @author Adam Cherti
 */
public class ReservationService extends Service {
    private static final long serialVersionUID = 1L;

    private ReservationDAO reservationDAO;

    private MembreDAO membreDAO;

    private LivreDAO livreDAO;

    /**
     * Crée le service de la table reservation.
     *
     * @param reservationDAO - Le DAO de la table reservation
     * @param membreDAO - Le DAO de la table membre
     * @param livreDAO - Le DAO de la table livre
     */
    public ReservationService(ReservationDAO reservationDAO,
        MembreDAO membreDAO,
        LivreDAO livreDAO) {
        super();
        setReservationDAO(reservationDAO);
        setMembreDAO(membreDAO);
        setLivreDAO(livreDAO);
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
     * Getter de la variable d'instance <code>this.livreDAO</code>.
     *
     * @return La variable d'instance <code>this.livreDAO</code>
     */
    private LivreDAO getLivreDAO() {
        return this.livreDAO;
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

    /**
     * Setter de la variable d'instance <code>this.membreDAO</code>.
     *
     * @param membreDAO La valeur à utiliser pour la variable d'instance <code>this.membreDAO</code>
     */
    private void setMembreDAO(MembreDAO membreDAO) {
        this.membreDAO = membreDAO;
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
     * Ajoute une nouvelle réservation.
     *
     * @param reservationDTO - La réservation à ajouter
     * @throws ServiceException - S'il y a une erreur avec la base de données
     */
    public void add(ReservationDTO reservationDTO) throws ServiceException {
        try {
            getReservationDAO().add(reservationDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * Lit une réservation. Si aucune réservation n'est trouvée, null est retourné.
     *
     * @param idReservation - L'ID de la réservation à lire
     * @return La réservation lue ; null sinon
     * @throws ServiceException - S'il y a une erreur avec la base de données
     */
    public ReservationDTO read(int idReservation) throws ServiceException {
        ReservationDTO reservationDTO = null;
        try {
            reservationDTO = getReservationDAO().read(idReservation);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
        return reservationDTO;
    }

    /**
     * Met à jour une réservation.
     *
     * @param reservationDTO - La réservation à mettre à jour
     * @throws ServiceException - S'il y a une erreur avec la base de données
     */
    public void update(ReservationDTO reservationDTO) throws ServiceException {
        try {
            getReservationDAO().update(reservationDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * Supprime une réservation.
     *
     * @param reservationDTO - La réservation à supprimer
     * @throws ServiceException - S'il y a une erreur avec la base de données
     */
    public void delete(ReservationDTO reservationDTO) throws ServiceException {
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
     * @throws ServiceException - S'il y a une erreur avec la base de données
     */
    public List<ReservationDTO> getAll() throws ServiceException {
        List<ReservationDTO> listReservationDTO = Collections.emptyList();
        try {
            listReservationDTO = getReservationDAO().getAll();
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
        return listReservationDTO;
    }

    /**
     * Trouve les réservations à partir d'un livre.
     *
     * @param livreDTO - Le livre à utiliser
     * @return La liste des réservations correspondantes, triée par date de réservation croissante ; une liste vide sinon
     * @throws ServiceException - S'il y a une erreur avec la base de données
     */
    public List<ReservationDTO> findByLivre(LivreDTO livreDTO) throws ServiceException {
        List<ReservationDTO> listReservationDTO = Collections.emptyList();
        try {
            listReservationDTO = getReservationDAO().findByLivre(livreDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
        return listReservationDTO;
    }

    /**
     * Trouve les réservations à partir d'un membre.
     *
     * @param membreDTO - Le membre à utiliser
     * @return La liste des réservations correspondantes ; une liste vide sinon
     * @throws ServiceException - S'il y a une erreur avec la base de données
     */
    public List<ReservationDTO> findByMembre(MembreDTO membreDTO) throws ServiceException {
        List<ReservationDTO> listReservationDTO = Collections.emptyList();
        try {
            listReservationDTO = getReservationDAO().findByMembre(membreDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
        return listReservationDTO;
    }

    /**
     * Réserve un livre.
     *
     * @param reservationDTO - La réservation à créer
     * @param membreDTO - Le membre qui réserve
     * @param livreDTO - Le livre à réserver
     * @throws ServiceException - Si la réservation existe déjà, si le membre n'existe pas, si le livre n'existe pas, si le livre n'a pas encore été prêté, si le livre est déjà prêté au membre, si le membre a déjà réservé ce livre ou s'il y a une erreur avec la base de données
     */
    public void reserver(ReservationDTO reservationDTO,
        MembreDTO membreDTO,
        LivreDTO livreDTO) throws ServiceException {
        try {
            if(getReservationDAO().read(reservationDTO.getIdReservation()) != null) {
                throw new ServiceException("La réservation "
                    + reservationDTO.getIdReservation()
                    + " existe déjà");
            }

            if(getMembreDAO().read(membreDTO.getIdMembre()) == null) {
                throw new ServiceException("Le membre "
                    + membreDTO.getIdMembre()
                    + " n'existe pas");
            }

            if(getLivreDAO().read(livreDTO.getIdLivre()) == null) {
                throw new ServiceException("Le livre "
                    + livreDTO.getIdLivre()
                    + " n'existe pas");
            }

            //livre n'a pas encore été prêté?

            if(!getReservationDAO().findByLivre(livreDTO).isEmpty()) {
                throw new ServiceException("Le livre "
                    + livreDTO.getIdLivre()
                    + " est déjà réservé");
            }

            getReservationDAO().add(reservationDTO);

        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * Utilise une réservation.
     *
     * @param reservationDTO - La réservation à utiliser
     * @param membreDTO - Le membre qui utilise
     * @param livreDTO - Le livre à utilise
     * @throws ServiceException - Si la réservation n'existe pas, si le membre n'existe pas, si le livre n'existe pas, si la réservation n'est pas la première de la liste, si le livre est déjà prété, si le membre a atteint sa limite de prêt ou s'il y a une erreur avec la base de données
     */
    public void utiliser(ReservationDTO reservationDTO,
        MembreDTO membreDTO,
        LivreDTO livreDTO) throws ServiceException {
        try {
            if(getReservationDAO().read(reservationDTO.getIdReservation()) != null) {
                throw new ServiceException("La réservation "
                    + reservationDTO.getIdReservation()
                    + " existe déjà");
            }

            if(getMembreDAO().read(membreDTO.getIdMembre()) == null) {
                throw new ServiceException("Le membre "
                    + membreDTO.getIdMembre()
                    + " n'existe pas");
            }

            if(getLivreDAO().read(livreDTO.getIdLivre()) == null) {
                throw new ServiceException("Le livre "
                    + livreDTO.getIdLivre()
                    + " n'existe pas");
            }

            //livre n'a pas encore été prêté?

            final List<ReservationDTO> listReservationDTO = getReservationDAO().findByLivre(livreDTO);
            if(listReservationDTO.isEmpty()) {
                throw new ServiceException("Le livre "
                    + livreDTO.getIdLivre()
                    + " n'est pas réservé");
            }

            if(listReservationDTO.get(0) != reservationDTO) {
                throw new ServiceException("La réservation "
                    + reservationDTO.getIdReservation()
                    + " n'est pas première dansa la liste d'attente");
            }

            getReservationDAO().update(reservationDTO);

        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * Annule une réservation.
     *
     * @param reservationDTO - Le reservation à annuler
     * @throws ServiceException - Si la réservation n'existe pas ou s'il y a une erreur avec la base de données
     */
    public void annuler(ReservationDTO reservationDTO) throws ServiceException {
        try {
            if(getReservationDAO().read(reservationDTO.getIdReservation()) == null) {
                throw new ServiceException("La réservation "
                    + reservationDTO.getIdReservation()
                    + " n'existe pas");
            }

            getReservationDAO().delete(reservationDTO);

        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }
}
