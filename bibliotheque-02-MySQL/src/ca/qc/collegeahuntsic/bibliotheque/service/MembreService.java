// Fichier MembreService.java
// Auteur : Adam Cherti
// Date de création : 2016-09-15

package ca.qc.collegeahuntsic.bibliotheque.service;

import java.util.List;
import ca.qc.collegeahuntsic.bibliotheque.dao.LivreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.MembreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.ReservationDAO;
import ca.qc.collegeahuntsic.bibliotheque.dto.LivreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.MembreDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.DAOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.ServiceException;

/**
 *
 * Service de la table Membre.
 *
 * @author Adam Cherti
 */

public class MembreService extends Service {
    private static final long serialVersionUID = 1L;

    private LivreDAO livreDAO;

    private MembreDAO membreDAO;

    private ReservationDAO reservationDAO;

    /**
     * Crée le service de la table membre.
     *
     * @param livreDAO Le DAO de la table livre.
     * @param membreDAO Le DAO de la table membre.
     * @param reservationDAO Le DAO de la table reservation
     */
    public MembreService(final LivreDAO livreDAO,
        final MembreDAO membreDAO,
        ReservationDAO reservationDAO) {
        setLivreDAO(livreDAO);
        setMembreDAO(membreDAO);
        setReservationDAO(reservationDAO);
    }

    /**
     * Ajoute un nouveau membre.
     *
     * @param membreDTO Le membre à ajouter.
     * @throws ServiceException  S'il y a une erreur avec la base de données.
     */
    public void add(MembreDTO membreDTO) throws ServiceException {
        try {
            getMembreDAO().add(membreDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * Supprime un membre.
     *
     * @param membreDTO Le membre à supprimer.
     * @throws ServiceException Si le membre a encore des prêts, s'il a des
     *      réservations ou s'il y a une erreur avec la base de données.
     */
    public void delete(MembreDTO membreDTO) throws ServiceException {
        try {
            getMembreDAO().delete(membreDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * Lit un membre. Si aucun membre n'est trouvé, null est retourné.
     *
     * @param idMembre L'ID du membre à lire.
     * @return Le membre lu ; null sinon.
     * @throws ServiceException S'il y a une erreur avec la base de données.
     */
    public MembreDTO read(int idMembre) throws ServiceException {
        try {
            return getMembreDAO().read(idMembre);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * Met à jour un membre.
     *
     * @param membreDTO Le membre à mettre à jour.
     * @throws ServiceException  S'il y a une erreur avec la base de données.
     */
    public void update(MembreDTO membreDTO) throws ServiceException {
        try {
            getMembreDAO().update(membreDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * Trouve tous les membres.
     *
     * @return La liste des membres ; une liste vide sinon.
     * @throws ServiceException S'il y a une erreur avec la base de données.
     */
    public List<MembreDTO> getAll() throws ServiceException {
        try {
            return getMembreDAO().getAll();
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * Inscrit un membre.
     *
     * @param membreDTO Le membre à ajouter.
     * @throws ServiceException Si le membre existe déjà ou s'il y a une erreur avec la base de données.
     */
    public void inscrire(MembreDTO membreDTO) throws ServiceException {
        if(read(membreDTO.getIdMembre()) == null) {
            throw new ServiceException("Le membre avec le id: "
                + membreDTO.getIdMembre()
                + " n'existe pas.");
        }
        add(membreDTO);
    }

    /**
     * Retourne un livre.
     *
     * @param membreDTO Le membre qui retourne.
     * @return
     * @throws ServiceException Si le membre n'existe pas, si le livre n'existe pas, si le livre n'a pas encore été prêté,
     *      si le livre a été prêté à quelqu'un d'autre ou s'il y a une erreur avec la base de données.
     */
    public void desinscrire(MembreDTO membreDTO) throws ServiceException {
        try {
            if(read(membreDTO.getIdMembre()) == null) {
                throw new ServiceException("Le membre avec le id: "
                    + membreDTO.getIdMembre()
                    + " n'existe pas.");
            }
            if(read(membreDTO.getIdMembre()).getNbPret() > 0) {
                throw new ServiceException("Le membre avec le id: "
                    + membreDTO.getIdMembre()
                    + " a toujours "
                    + membreDTO.getNbPret()
                    + " en sa possession.");
            }
            if(!getReservationDAO().findByMembre(membreDTO).isEmpty()) {
                throw new ServiceException("Le membre avec le id: "
                    + membreDTO.getIdMembre()
                    + " a toujours des reservations.");
            }
            delete(membreDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * Emprunte un livre.
     *
     * @param membreDTO Le membre qui emprunte.
     * @param livreDTO Le livre à emprunter.
     *
     * @throws ServiceException Si le membre n'existe pas,
     *      si le livre n'existe pas, si le livre a été prêté, si le livre a été réservé, si le membre a atteint sa limite de prêt ou s'il y a une erreur avec la base de données
     */
    public void emprunter(MembreDTO membreDTO,
        LivreDTO livreDTO) throws ServiceException {
        try {
            if(read(membreDTO.getIdMembre()) == null) {
                throw new ServiceException("Le membre avec le id: "
                    + membreDTO.getIdMembre()
                    + " n'existe pas.");
            }
            if(getLivreDAO().read(livreDTO.getIdLivre()) == null) {
                throw new ServiceException("Le livre avec le id: "
                    + livreDTO.getIdLivre()
                    + " n'existe pas.");
            }
            final MembreDTO emprunteur = read(livreDTO.getIdMembre());
            if(emprunteur != null) {
                throw new ServiceException("Le livre avec le id: "
                    + livreDTO.getIdLivre()
                    + " est deja prete a "
                    + emprunteur.getNom());
            }
            if(!getReservationDAO().findByLivre(livreDTO).isEmpty()) {
                throw new ServiceException("Le livre avec le id: "
                    + livreDTO.getIdLivre()
                    + " est reserve.");
            }
            if(read(membreDTO.getIdMembre()).getNbPret() == membreDTO.getLimitePret()) {
                throw new ServiceException("Le membre avec le id: "
                    + membreDTO.getIdMembre()
                    + " a atteint sa limite de pret.");
            }
            membreDTO.setNbPret(membreDTO.getNbPret()
                + 1);
            update(membreDTO);
            livreDTO.setIdMembre(membreDTO.getIdMembre());
            getLivreDAO().emprunter(livreDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * Renouvelle le prêt d'un livre.
     *
     * @param membreDTO Le membre qui renouvelle.
     * @param livreDTO Le livre à renouveler.
     *
     * @throws ServiceException Si le membre n'existe pas, si le livre n'existe pas, si le livre n'a pas encore été prêté,
     *      si le livre a été prêté à quelqu'un d'autre, si le livre a été réservé ou s'il y a une erreur avec la base de données
     */
    public void renouveler(MembreDTO membreDTO,
        LivreDTO livreDTO) throws ServiceException {
        try {
            if(read(membreDTO.getIdMembre()) == null) {
                throw new ServiceException("Le membre avec le id: "
                    + membreDTO.getIdMembre()
                    + " n'existe pas.");
            }
            if(getLivreDAO().read(livreDTO.getIdLivre()) == null) {
                throw new ServiceException("Le livre avec le id: "
                    + livreDTO.getIdLivre()
                    + " n'existe pas.");
            }
            final MembreDTO emprunteur = read(livreDTO.getIdMembre());
            if(emprunteur == null) {
                throw new ServiceException("Le livre avec le id: "
                    + livreDTO.getIdLivre()
                    + " n'est pas presentement prete.");
            }
            if(emprunteur.getIdMembre() != membreDTO.getIdMembre()) {
                throw new ServiceException("Le livre avec le id: "
                    + livreDTO.getIdLivre()
                    + " a ete prete a quelqu'un d'autre.");
            }
            if(getReservationDAO().findByLivre(livreDTO).isEmpty()) {
                throw new ServiceException("Le livre avec le id: "
                    + livreDTO.getIdLivre()
                    + " est reserve.");
            }
            getLivreDAO().emprunter(livreDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * Retourne un livre.
     *
     * @param membreDTO Le membre qui retourne.
     * @param livreDTO  Le livre à retourner.
     *
     * @throws ServiceException Si le membre n'existe pas, si le livre n'existe pas,
     *      si le livre n'a pas encore été prêté, si le livre a été prêté à quelqu'un d'autre ou s'il y a une erreur avec la base de données
     */
    public void retourner(MembreDTO membreDTO,
        LivreDTO livreDTO) throws ServiceException {
        try {
            if(read(membreDTO.getIdMembre()) == null) {
                throw new ServiceException("Le membre avec le id: "
                    + membreDTO.getIdMembre()
                    + " n'existe pas.");
            }
            if(getLivreDAO().read(livreDTO.getIdLivre()) == null) {
                throw new ServiceException("Le livre avec le id: "
                    + livreDTO.getIdLivre()
                    + " n'existe pas.");
            }
            if(getLivreDAO().read(livreDTO.getIdMembre()) == null) {
                throw new ServiceException("Le livre avec le id: "
                    + livreDTO.getIdLivre()
                    + " n'est pas presentement prete.");
            }
            if(read(livreDTO.getIdMembre()).getIdMembre() != membreDTO.getIdMembre()) {
                throw new ServiceException("Le livre avec le id: "
                    + livreDTO.getIdLivre()
                    + " a ete prete a quelqu'un d'autre.");
            }
            membreDTO.setNbPret(membreDTO.getNbPret()
                - 1);
            update(membreDTO);
            livreDTO.setIdMembre(0);
            getLivreDAO().retourner(livreDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    // Region Getters and Setters

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

    // EndRegion
}
