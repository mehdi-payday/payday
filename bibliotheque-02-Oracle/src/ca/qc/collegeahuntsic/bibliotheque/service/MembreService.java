// Fichier MembreService.java
// Auteur : Team PayDay
// Date de création : 2016-05-18

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
 * Service de la table <code>membre</code>.
 *
 * @author Team PayDay
 */
public class MembreService extends Service {
    private static final long serialVersionUID = 1L;

    private MembreDAO membreDAO;

    private LivreDAO livreDAO;

    private ReservationDAO reservationDAO;

    /**
     * Crée le service de la table <code>membre</code>.
     *
     * @param membreDAO Le DAO de la table <code>membre</code>
     * @param livreDAO Le DAO de la table <code>livre</code>
     * @param reservationDAO Le DAO de la table <code>reservation</code>
     */
    public MembreService(MembreDAO membreDAO,
        LivreDAO livreDAO,
        ReservationDAO reservationDAO) {
        super();
        setMembreDAO(membreDAO);
        setLivreDAO(livreDAO);
        setReservationDAO(reservationDAO);
    }

    // Region Getters and Setters
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
     * Ajoute un nouveau membre.
     *
     * @param membreDTO Le membre à ajouter
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    public void add(MembreDTO membreDTO) throws ServiceException {
        try {
            getMembreDAO().add(membreDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * Lit un membre. Si aucun membre n'est trouvé, <code>null</code> est retourné.
     *
     * @param idMembre L'ID du membre à lire
     * @return Le membre lu ; <code>null</code> sinon
     * @throws ServiceException S'il y a une erreur avec la base de données
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
     * @param membreDTO Le membre à mettre à jour
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    public void update(MembreDTO membreDTO) throws ServiceException {
        try {
            getMembreDAO().update(membreDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * Supprime un membre.
     *
     * @param membreDTO Le membre à supprimer
     * @throws ServiceException S'il y a une erreur avec la base de données
     */
    public void delete(MembreDTO membreDTO) throws ServiceException {
        try {
            getMembreDAO().delete(membreDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * Trouve tous les membres.
     *
     * @return La liste des membres ; une liste vide sinon
     * @throws ServiceException S'il y a une erreur avec la base de données
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
     * @param membreDTO Le membre à ajouter
     * @throws ServiceException Si le membre existe déjà ou s'il y a une erreur avec la base de données
     */
    public void inscrire(MembreDTO membreDTO) throws ServiceException {
        if(read(membreDTO.getIdMembre()) != null) {
            throw new ServiceException("Le membre "
                + membreDTO.getIdMembre()
                + " existe déjà");
        }
        add(membreDTO);
    }

    /**
     * Emprunte un livre.
     *
     * @param membreDTO Le membre qui emprunte
     * @param livreDTO Le livre à emprunter
     * @throws ServiceException Si le membre n'existe pas, si le livre n'existe pas, si le livre a été prêté, si le livre a été réservé, si le
     *         membre a atteint sa limite de prêt ou s'il y a une erreur avec la base de données
     */
    public void emprunter(MembreDTO membreDTO,
        LivreDTO livreDTO) throws ServiceException {
        try {
            final MembreDTO unMembreDTO = read(membreDTO.getIdMembre());
            if(unMembreDTO == null) {
                throw new ServiceException("Le membre "
                    + membreDTO.getIdMembre()
                    + " n'existe pas");
            }
            final LivreDTO unLivreDTO = getLivreDAO().read(livreDTO.getIdLivre());
            if(unLivreDTO == null) {
                throw new ServiceException("Le livre "
                    + livreDTO.getIdLivre()
                    + " n'existe pas");
            }
            final MembreDTO emprunteur = read(unLivreDTO.getIdMembre());
            if(emprunteur != null) {
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
            if(unMembreDTO.getNbPret() == unMembreDTO.getLimitePret()) {
                throw new ServiceException("Le membre "
                    + unMembreDTO.getNom()
                    + " (ID de membre : "
                    + unMembreDTO.getIdMembre()
                    + ") a atteint sa limite de prêt ("
                    + unMembreDTO.getLimitePret()
                    + " emprunt(s) maximum)");
            }
            if(!getReservationDAO().findByLivre(unLivreDTO.getIdLivre()).isEmpty()) {
                throw new ServiceException("Le livre "
                    + unLivreDTO.getTitre()
                    + " (ID de livre : "
                    + unLivreDTO.getIdLivre()
                    + ") a des réservations");
            }
            // Problème de la date de prêt
            // On voit également le manque de la table prêt simulée en ce moment par les deux tables
            unLivreDTO.setIdMembre(unMembreDTO.getIdMembre());
            getLivreDAO().emprunter(unLivreDTO);
            getMembreDAO().emprunter(unMembreDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * Renouvelle le prêt d'un livre.
     *
     * @param membreDTO Le membre qui renouvelle
     * @param livreDTO Le livre à renouveler
     * @throws ServiceException Si le membre n'existe pas, si le livre n'existe pas, si le livre n'a pas encore été prêté, si le livre a été
     *         prêté à quelqu'un d'autre, si le livre a été réservé ou s'il y a une erreur avec la base de données
     */
    public void renouveler(MembreDTO membreDTO,
        LivreDTO livreDTO) throws ServiceException {
        try {
            final MembreDTO unMembreDTO = read(membreDTO.getIdMembre());
            if(unMembreDTO == null) {
                throw new ServiceException("Le membre "
                    + membreDTO.getIdMembre()
                    + " n'existe pas");
            }
            final LivreDTO unLivreDTO = getLivreDAO().read(livreDTO.getIdLivre());
            if(unLivreDTO == null) {
                throw new ServiceException("Le livre "
                    + livreDTO.getIdLivre()
                    + " n'existe pas");
            }
            final MembreDTO emprunteur = read(unLivreDTO.getIdMembre());
            if(emprunteur == null) {
                throw new ServiceException("Le livre "
                    + unLivreDTO.getTitre()
                    + " (ID de livre : "
                    + unLivreDTO.getIdLivre()
                    + ") n'est pas encore prêté");
            }
            if(unMembreDTO.getIdMembre() != emprunteur.getIdMembre()) {
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
            if(!getReservationDAO().findByLivre(unLivreDTO.getIdLivre()).isEmpty()) {
                throw new ServiceException("Le livre "
                    + unLivreDTO.getTitre()
                    + " (ID de livre : "
                    + unLivreDTO.getIdLivre()
                    + ") a des réservations");
            }

            // Cas éliminé en utilisant la date de prêt comme étant la date système de la base de données

            /* Verifier si date renouvellement >= datePret */
            // if(Date.valueOf(datePret).before(tupleLivre.getDatePret())) {
            //     throw new BibliothequeException("Date de renouvellement inférieure à la date de prêt");
            // }

            getLivreDAO().emprunter(unLivreDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * Retourne un livre.
     *
     * @param membreDTO Le membre qui retourne
     * @param livreDTO Le livre à retourner
     * @throws ServiceException Si le membre n'existe pas, si le livre n'existe pas, si le livre n'a pas encore été prêté, si le livre a été
     *         prêté à quelqu'un d'autre ou s'il y a une erreur avec la base de données
     */
    public void retourner(MembreDTO membreDTO,
        LivreDTO livreDTO) throws ServiceException {
        try {
            final MembreDTO unMembreDTO = read(membreDTO.getIdMembre());
            if(unMembreDTO == null) {
                throw new ServiceException("Le membre "
                    + membreDTO.getIdMembre()
                    + " n'existe pas");
            }
            final LivreDTO unLivreDTO = getLivreDAO().read(livreDTO.getIdLivre());
            if(unLivreDTO == null) {
                throw new ServiceException("Le livre "
                    + livreDTO.getIdLivre()
                    + " n'existe pas");
            }
            final MembreDTO emprunteur = read(unLivreDTO.getIdMembre());
            if(emprunteur == null) {
                throw new ServiceException("Le livre "
                    + unLivreDTO.getTitre()
                    + " (ID de livre : "
                    + unLivreDTO.getIdLivre()
                    + ") n'est pas encore prêté");
            }
            if(unMembreDTO.getIdMembre() != emprunteur.getIdMembre()) {
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

            // Cas éliminé en utilisant la date de prêt comme étant la date système de la base de données

            /* Verifier si date retour >= datePret */
            // if(Date.valueOf(dateRetour).before(tupleLivre.getDatePret())) {
            //     throw new BibliothequeException("Date de retour inférieure à la date de prêt");
            // }

            // On voit le manque de la table prêt simulée en ce moment par les deux tables
            getLivreDAO().retourner(unLivreDTO);
            getMembreDAO().retourner(unMembreDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * Désincrit un membre.
     *
     * @param membreDTO Le membre à désinscrire
     * @throws ServiceException Si le membre n'existe pas, si le membre a encore des prêts, s'il a des réservations ou s'il y a une erreur avec
     *         la base de données
     */
    public void desinscrire(MembreDTO membreDTO) throws ServiceException {
        try {
            final MembreDTO unMembreDTO = read(membreDTO.getIdMembre());
            if(unMembreDTO == null) {
                throw new ServiceException("Le membre "
                    + membreDTO.getIdMembre()
                    + " n'existe pas");
            }
            if(unMembreDTO.getNbPret() > 0) {
                throw new ServiceException("Le membre "
                    + unMembreDTO.getNom()
                    + " (ID de membre : "
                    + unMembreDTO.getIdMembre()
                    + ") a encore des prêts");
            }
            if(!getReservationDAO().findByMembre(unMembreDTO.getIdMembre()).isEmpty()) {
                throw new ServiceException("Le membre "
                    + unMembreDTO.getNom()
                    + " (ID de membre : "
                    + unMembreDTO.getIdMembre()
                    + ") a des réservations");
            }
            delete(unMembreDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }
}
