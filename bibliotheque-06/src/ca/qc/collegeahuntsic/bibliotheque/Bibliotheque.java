// Fichier Bibliotheque.java
// Auteur : Team PayDay
// Date de création : 2016-05-18

package ca.qc.collegeahuntsic.bibliotheque;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.StringTokenizer;
import ca.qc.collegeahuntsic.bibliotheque.exception.BibliothequeException;
import ca.qc.collegeahuntsic.bibliotheque.util.BibliothequeCreateur;
import ca.qc.collegeahuntsic.bibliotheque.util.FormatteurDate;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.dto.LivreDTO;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.dto.MembreDTO;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.dto.PretDTO;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.dto.ReservationDTO;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dao.InvalidHibernateSessionException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dao.InvalidPrimaryKeyException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dto.InvalidDTOException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dto.MissingDTOException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.facade.FacadeException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.service.ExistingLoanException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.service.ExistingReservationException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.service.InvalidLoanLimitException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.service.MissingLoanException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Interface du système de gestion d'une bibliothèque.
 *
 * Ce programme permet d'appeler les transactions de base d'une bibliothèque.  Il gère des livres, des membres et des réservations. Les données
 * sont conservées dans une base de données relationnelles accédée avec JDBC. Pour une liste des transactions traitées, voir la méthode
 * {@link Bibliotheque#afficherAide() afficherAide()}.
 *
 * Paramètres :
 * 0 - fichier de transaction
 *
 * Pré-condition :
 *   La base de données de la bibliothèque doit exister
 *
 * Post-condition :
 *   Le programme effectue les maj associées à chaque transaction
 *
 * @author Team PayDay
 */
public final class Bibliotheque {
    private static BibliothequeCreateur gestionnaireBibliotheque;

    private static final Log LOGGER = LogFactory.getLog(Bibliotheque.class);

    /**
     * Constructeur privé pour empêcher toute instanciation.
     */
    private Bibliotheque() {
        super();
    }

    /**
     * Crée une connexion sur la base de données, traite toutes les transactions et détruit la connexion.
     *
     * @param arguments Les arguments du main
     * @throws Exception Si une erreur survient
     */
    public static void main(final String[] arguments) throws Exception {
        // Validation du nombre de paramètres
        if(arguments.length < 1) {
            Bibliotheque.LOGGER.info("Usage: java Bibliotheque  <fichier-transactions>");
            return;
        }

        try {
            // Ouverture du fichier de transactions
            final InputStream sourceTransaction = Bibliotheque.class.getResourceAsStream("/"
                + arguments[0]);
            try(
                BufferedReader reader = new BufferedReader(new InputStreamReader(sourceTransaction))) {

                Bibliotheque.gestionnaireBibliotheque = new BibliothequeCreateur();
                Bibliotheque.traiterTransactions(reader);
            }
        } catch(IOException ioException) {
            Bibliotheque.LOGGER.error(" *** "
                + ioException.getMessage());
        } catch(BibliothequeException bibliothequeException) {
            Bibliotheque.gestionnaireBibliotheque.rollbackTransaction();
            Bibliotheque.LOGGER.error(" *** "
                + bibliothequeException.getMessage());
        }
    }

    /**
     * Traite le fichier de transactions.
     *
     * @param reader Le flux d'entrée à lire
     * @throws Exception Si une erreur survient
     */
    private static void traiterTransactions(final BufferedReader reader) throws Exception {
        Bibliotheque.afficherAide();
        Bibliotheque.LOGGER.info("\n\n\n");
        String transaction = Bibliotheque.lireTransaction(reader);
        while(!Bibliotheque.finTransaction(transaction)) {
            final StringTokenizer tokenizer = new StringTokenizer(transaction,
                " ");
            if(tokenizer.hasMoreTokens()) {
                Bibliotheque.executerTransaction(tokenizer);
            }
            transaction = Bibliotheque.lireTransaction(reader);
        }
    }

    /**
     * Lit une transaction.
     *
     * @param reader Le flux d'entrée à lire
     * @return La transaction lue
     * @throws IOException Si une erreur de lecture survient
     */
    private static String lireTransaction(final BufferedReader reader) throws IOException {
        final String transaction = reader.readLine();
        if(transaction != null) {
            Bibliotheque.LOGGER.info("> "
                + transaction);
        }
        return transaction;
    }

    /**
     * Décode et traite une transaction.
     *
     * @param tokenizer L'entrée à décoder
     * @throws BibliothequeException Si une erreur survient
     */
    private static void executerTransaction(final StringTokenizer tokenizer) throws BibliothequeException {
        final String command = tokenizer.nextToken();
        switch(command) {
            case "aide":
                Bibliotheque.afficherAide();
                break;
            case "inscrire":
                Bibliotheque.inscrireMembre(tokenizer);
                break;
            case "desinscrire":
                Bibliotheque.desinscrireMembre(tokenizer);
                break;
            case "acquerir":
                Bibliotheque.acquerirLivre(tokenizer);
                break;
            case "vendre":
                Bibliotheque.vendreLivre(tokenizer);
                break;
            case "preter":
                Bibliotheque.commencerPret(tokenizer);
                break;
            case "renouveler":
                Bibliotheque.renouvelerPret(tokenizer);
                break;
            case "retourner":
                Bibliotheque.terminerPret(tokenizer);
                break;
            case "reserver":
                Bibliotheque.placerReservation(tokenizer);
                break;
            case "utiliser":
                Bibliotheque.utiliserReservation(tokenizer);
                break;
            case "annuler":
                Bibliotheque.annulerReservation(tokenizer);
                break;
            case "--":
                break;
            default:
                Bibliotheque.LOGGER.error("  Transactions non reconnue.  Essayer \"aide\"");
        }
    }

    /**
     * Affiche le menu des transactions acceptées par le système.
     */
    private static void afficherAide() {

        Bibliotheque.LOGGER.info("\n");
        Bibliotheque.LOGGER.info("Chaque transaction comporte un nom et une liste d'arguments");
        Bibliotheque.LOGGER.info("séparés par des espaces. La liste peut être vide.");
        Bibliotheque.LOGGER.info(" Les dates sont en format yyyy-mm-dd.\n");
        Bibliotheque.LOGGER.info("Les transactions sont :");
        Bibliotheque.LOGGER.info("  aide");
        Bibliotheque.LOGGER.info("  inscrire <nom> <telephone> <limitePret>");
        Bibliotheque.LOGGER.info("  desinscrire <idMembre>");
        Bibliotheque.LOGGER.info("  acquerir <titre> <auteur> <dateAcquisition>");
        Bibliotheque.LOGGER.info("  vendre <idLivre>");
        Bibliotheque.LOGGER.info("  preter <idMembre> <idLivre>");
        Bibliotheque.LOGGER.info("  renouveler <idPret>");
        Bibliotheque.LOGGER.info("  retourner <idPret>");
        Bibliotheque.LOGGER.info("  reserver <idMembre> <idLivre>");
        Bibliotheque.LOGGER.info("  utiliser <idReservation>");
        Bibliotheque.LOGGER.info("  annuler <idReservation>");
    }

    /**
     * Inscrit un membre.
     *
     * @param tokenizer Le tokenizer à utiliser
     * @throws BibliothequeException S'il y a une erreur
     */
    private static void inscrireMembre(StringTokenizer tokenizer) throws BibliothequeException {
        try {
            Bibliotheque.gestionnaireBibliotheque.beginTransaction();
            final MembreDTO membreDTO = new MembreDTO();
            membreDTO.setNom(Bibliotheque.readString(tokenizer));
            membreDTO.setTelephone(Bibliotheque.readString(tokenizer));
            membreDTO.setLimitePret(Bibliotheque.readString(tokenizer));
            Bibliotheque.gestionnaireBibliotheque.getMembreFacade().inscrire(Bibliotheque.gestionnaireBibliotheque.getSession(),
                membreDTO);
            Bibliotheque.gestionnaireBibliotheque.commitTransaction();
        } catch(
            InvalidHibernateSessionException
            | InvalidDTOException
            | FacadeException exception) {
            Bibliotheque.LOGGER.error("**** "
                + exception.getMessage());
            Bibliotheque.gestionnaireBibliotheque.rollbackTransaction();
        }
    }

    /**
     * Désinscrire un membre.
     *
     * @param tokenizer Le tokenizer à utiliser
     * @throws BibliothequeException S'il y a une erreur
     */
    private static void desinscrireMembre(final StringTokenizer tokenizer) throws BibliothequeException {
        try {
            Bibliotheque.gestionnaireBibliotheque.beginTransaction();
            final String idMembre = Bibliotheque.readString(tokenizer);
            final MembreDTO membreDTO = (MembreDTO) Bibliotheque.gestionnaireBibliotheque.getMembreFacade().get(
                Bibliotheque.gestionnaireBibliotheque.getSession(),
                idMembre);
            if(membreDTO == null) {
                throw new MissingDTOException("Le membre "
                    + idMembre
                    + " n'existe pas");
            }

            Bibliotheque.gestionnaireBibliotheque.getMembreFacade().desinscrire(Bibliotheque.gestionnaireBibliotheque.getSession(),
                membreDTO);
            Bibliotheque.gestionnaireBibliotheque.commitTransaction();
        } catch(
            InvalidHibernateSessionException
            | InvalidPrimaryKeyException
            | InvalidDTOException
            | FacadeException
            | ExistingLoanException
            | ExistingReservationException
            | MissingDTOException exception) {
            Bibliotheque.LOGGER.error(" *** "
                + exception.getMessage());
            Bibliotheque.gestionnaireBibliotheque.rollbackTransaction();
        }
    }

    /**
     *
     * Permet d'acquérir un livre.
     *
     * @param tokenizer Le tokenizer à utiliser
     * @throws BibliothequeException Si la connexion avec la base de données ne peut être faite ou que la transaction Hibernate ne peut être créée
     */
    private static void acquerirLivre(final StringTokenizer tokenizer) throws BibliothequeException {
        try {
            Bibliotheque.gestionnaireBibliotheque.beginTransaction();
            final LivreDTO livreDTO = new LivreDTO();

            final String titre = Bibliotheque.readString(tokenizer);
            final String auteur = Bibliotheque.readString(tokenizer);
            final Timestamp dateAcquisition = Bibliotheque.readDate(tokenizer);
            livreDTO.setTitre(titre);
            livreDTO.setAuteur(auteur);
            livreDTO.setDateAcquisition(dateAcquisition);

            Bibliotheque.gestionnaireBibliotheque.getLivreFacade().acquerir(Bibliotheque.gestionnaireBibliotheque.getSession(),
                livreDTO);
            Bibliotheque.gestionnaireBibliotheque.commitTransaction();
        } catch(
            InvalidHibernateSessionException
            | InvalidDTOException
            | FacadeException exception) {
            Bibliotheque.LOGGER.info(" *** "
                + exception.getMessage());
            Bibliotheque.gestionnaireBibliotheque.rollbackTransaction();
        }

    }

    /**
    *
    * Permet de vendre un livre.
    *
    * @param tokenizer Le tokenizer à utiliser
    * @throws BibliothequeException Si la connexion avec la base de données ne peut être faite ou que la transaction Hibernate ne peut être créée
     */
    private static void vendreLivre(final StringTokenizer tokenizer) throws BibliothequeException {
        try {
            Bibliotheque.gestionnaireBibliotheque.beginTransaction();
            final String idLivre = Bibliotheque.readString(tokenizer);
            final LivreDTO livreDTO = (LivreDTO) Bibliotheque.gestionnaireBibliotheque.getLivreFacade().get(Bibliotheque.gestionnaireBibliotheque.getSession(),
                idLivre);
            if(livreDTO == null) {
                throw new MissingDTOException("Le livre "
                    + idLivre
                    + " n'existe pas");
            }
            Bibliotheque.gestionnaireBibliotheque.getLivreFacade().vendre(Bibliotheque.gestionnaireBibliotheque.getSession(),
                livreDTO);
            Bibliotheque.gestionnaireBibliotheque.commitTransaction();
        } catch(
            InvalidHibernateSessionException
            | InvalidDTOException
            | ExistingLoanException
            | ExistingReservationException
            | FacadeException
            | MissingDTOException
            | InvalidPrimaryKeyException exception) {
            Bibliotheque.LOGGER.error(" *** "
                + exception.getMessage());
            Bibliotheque.gestionnaireBibliotheque.rollbackTransaction();
        }
    }

    /**
     * Preter un livre.
     *
     * @param tokenizer Le tokenizer à utiliser
     * @throws BibliothequeException S'il y a une erreur
     */
    private static void commencerPret(final StringTokenizer tokenizer) throws BibliothequeException {
        try {
            Bibliotheque.gestionnaireBibliotheque.beginTransaction();
            final String idMembre = Bibliotheque.readString(tokenizer);
            final MembreDTO membreDTO = (MembreDTO) Bibliotheque.gestionnaireBibliotheque.getMembreFacade().get(
                Bibliotheque.gestionnaireBibliotheque.getSession(),
                idMembre);
            if(membreDTO == null) {
                throw new MissingDTOException("Le membre "
                    + idMembre
                    + " n'existe pas");
            }
            final String idLivre = readString(tokenizer);
            final LivreDTO livreDTO = (LivreDTO) Bibliotheque.gestionnaireBibliotheque.getLivreFacade().get(Bibliotheque.gestionnaireBibliotheque.getSession(),
                idLivre);
            if(livreDTO == null) {
                throw new MissingDTOException("Le livre "
                    + idMembre
                    + " n'existe pas");
            }

            final PretDTO pretDTO = new PretDTO();
            pretDTO.setLivreDTO(livreDTO);
            pretDTO.setMembreDTO(membreDTO);

            Bibliotheque.gestionnaireBibliotheque.getPretFacade().commencer(Bibliotheque.gestionnaireBibliotheque.getSession(),
                pretDTO);
            Bibliotheque.gestionnaireBibliotheque.commitTransaction();
        } catch(
            InvalidHibernateSessionException
            | MissingDTOException
            | InvalidPrimaryKeyException
            | FacadeException
            | InvalidDTOException
            | ExistingLoanException
            | InvalidLoanLimitException
            | ExistingReservationException exception) {
            Bibliotheque.LOGGER.error(" *** "
                + exception.getMessage());
            Bibliotheque.gestionnaireBibliotheque.rollbackTransaction();
        }
    }

    /**
     * Renouveler un prêt.
     *
     * @param tokenizer Le tokenizer a utiliser
     * @throws BibliothequeException S'il y a une erreur
     */
    private static void renouvelerPret(final StringTokenizer tokenizer) throws BibliothequeException {
        try {
            Bibliotheque.gestionnaireBibliotheque.beginTransaction();
            final String idPret = Bibliotheque.readString(tokenizer);
            final PretDTO pretDTO = (PretDTO) Bibliotheque.gestionnaireBibliotheque.getPretFacade().get(Bibliotheque.gestionnaireBibliotheque.getSession(),
                idPret);
            if(pretDTO == null) {
                throw new MissingDTOException("Le prêt "
                    + idPret
                    + " n'existe pas");
            }
            Bibliotheque.gestionnaireBibliotheque.getPretFacade().renouveler(Bibliotheque.gestionnaireBibliotheque.getSession(),
                pretDTO);
            Bibliotheque.gestionnaireBibliotheque.commitTransaction();
        } catch(
            InvalidHibernateSessionException
            | InvalidDTOException
            | FacadeException
            | MissingLoanException
            | ExistingReservationException
            | InvalidPrimaryKeyException
            | MissingDTOException exception) {
            Bibliotheque.LOGGER.error(" *** "
                + exception.getMessage());
            Bibliotheque.gestionnaireBibliotheque.rollbackTransaction();
        }
    }

    /**
     * Terminer un prêt.
     *
     * @param tokenizer Le tokenizer à utiliser
     * @throws BibliothequeException S'il y a une erreur
     */
    private static void terminerPret(final StringTokenizer tokenizer) throws BibliothequeException {
        try {
            Bibliotheque.gestionnaireBibliotheque.beginTransaction();
            final String idPret = Bibliotheque.readString(tokenizer);
            final PretDTO pretDTO = (PretDTO) Bibliotheque.gestionnaireBibliotheque.getPretFacade().get(Bibliotheque.gestionnaireBibliotheque.getSession(),
                idPret);
            if(pretDTO == null) {
                throw new MissingDTOException("Le prêt "
                    + idPret
                    + " n'existe pas");
            }
            Bibliotheque.gestionnaireBibliotheque.getPretFacade().terminer(Bibliotheque.gestionnaireBibliotheque.getSession(),
                pretDTO);
            Bibliotheque.gestionnaireBibliotheque.commitTransaction();
        } catch(
            InvalidHibernateSessionException
            | InvalidDTOException
            | FacadeException
            | MissingLoanException
            | InvalidPrimaryKeyException
            | MissingDTOException exception) {
            Bibliotheque.LOGGER.error(" *** "
                + exception.getMessage());
            Bibliotheque.gestionnaireBibliotheque.rollbackTransaction();
        }
    }

    /**
     * Place une réservation.
     *
     * @param tokenizer Le tokenizer à utiliser
     * @throws BibliothequeException S'il y a une erreur
     */
    private static void placerReservation(final StringTokenizer tokenizer) throws BibliothequeException {
        try {
            Bibliotheque.gestionnaireBibliotheque.beginTransaction();
            final String idMembre = Bibliotheque.readString(tokenizer);
            final MembreDTO membreDTO = (MembreDTO) Bibliotheque.gestionnaireBibliotheque.getMembreFacade().get(
                Bibliotheque.gestionnaireBibliotheque.getSession(),
                idMembre);
            if(membreDTO == null) {
                throw new MissingDTOException("Le membre "
                    + idMembre
                    + " n'existe pas");
            }
            final String idLivre = Bibliotheque.readString(tokenizer);
            final LivreDTO livreDTO = (LivreDTO) Bibliotheque.gestionnaireBibliotheque.getLivreFacade().get(Bibliotheque.gestionnaireBibliotheque.getSession(),
                idLivre);
            if(livreDTO == null) {
                throw new MissingDTOException("Le livre "
                    + idLivre
                    + " n'existe pas");
            }
            final ReservationDTO reservationDTO = new ReservationDTO();
            reservationDTO.setMembreDTO(membreDTO);
            reservationDTO.setLivreDTO(livreDTO);

            Bibliotheque.gestionnaireBibliotheque.getReservationFacade().placer(Bibliotheque.gestionnaireBibliotheque.getSession(),
                reservationDTO);
            Bibliotheque.gestionnaireBibliotheque.commitTransaction();
        } catch(
            InvalidHibernateSessionException
            | InvalidDTOException
            | FacadeException
            | InvalidPrimaryKeyException
            | MissingDTOException
            | ExistingReservationException
            | ExistingLoanException
            | MissingLoanException exception) {
            Bibliotheque.LOGGER.error(" *** "
                + exception.getMessage());
            Bibliotheque.gestionnaireBibliotheque.rollbackTransaction();
        }
    }

    /**
     * Utilise une réservation.
     *
     * @param tokenizer Le tokenizer à utiliser
     * @throws BibliothequeException S'il y a une erreur
     */
    private static void utiliserReservation(final StringTokenizer tokenizer) throws BibliothequeException {
        try {
            Bibliotheque.gestionnaireBibliotheque.beginTransaction();
            final String idReservation = Bibliotheque.readString(tokenizer);
            final ReservationDTO reservationDTO = (ReservationDTO) Bibliotheque.gestionnaireBibliotheque.getReservationFacade().get(
                Bibliotheque.gestionnaireBibliotheque.getSession(),
                idReservation);

            if(reservationDTO == null) {
                throw new MissingDTOException("La réservation "
                    + idReservation
                    + " n'existe pas");
            }
            Bibliotheque.gestionnaireBibliotheque.getReservationFacade().utiliser(Bibliotheque.gestionnaireBibliotheque.getSession(),
                reservationDTO);
            Bibliotheque.gestionnaireBibliotheque.commitTransaction();
        } catch(
            InvalidHibernateSessionException
            | InvalidDTOException
            | FacadeException
            | InvalidPrimaryKeyException
            | MissingDTOException
            | ExistingReservationException
            | ExistingLoanException
            | InvalidLoanLimitException exception) {
            Bibliotheque.LOGGER.error(" *** "
                + exception.getMessage());
            Bibliotheque.gestionnaireBibliotheque.rollbackTransaction();
        }
    }

    /**
     * Annule une réservation.
     *
     * @param tokenizer Le tokenizer à utiliser
     * @throws BibliothequeException S'il y a une erreur
     */
    public static void annulerReservation(final StringTokenizer tokenizer) throws BibliothequeException {
        try {
            Bibliotheque.gestionnaireBibliotheque.beginTransaction();
            final String idReservation = Bibliotheque.readString(tokenizer);
            final ReservationDTO reservationDTO = (ReservationDTO) Bibliotheque.gestionnaireBibliotheque.getReservationFacade().get(
                Bibliotheque.gestionnaireBibliotheque.getSession(),
                idReservation);
            if(reservationDTO == null) {
                throw new MissingDTOException("La réservation "
                    + idReservation
                    + " n'existe pas");
            }
            Bibliotheque.gestionnaireBibliotheque.getReservationFacade().annuler(Bibliotheque.gestionnaireBibliotheque.getSession(),
                reservationDTO);
            Bibliotheque.gestionnaireBibliotheque.commitTransaction();
        } catch(
            InvalidHibernateSessionException
            | InvalidPrimaryKeyException
            | InvalidDTOException
            | FacadeException
            | MissingDTOException exception) {
            Bibliotheque.LOGGER.error(" *** "
                + exception.getMessage());
            Bibliotheque.gestionnaireBibliotheque.rollbackTransaction();
        }
    }

    /**
     * Vérifie si la fin du traitement des transactions est atteinte.
     *
     * @param transaction La transaction à traiter
     * @return <code>true</code> Si la fin du fichier est atteinte, <code>false</code> sinon
     */
    private static boolean finTransaction(final String transaction) {
        boolean finDeFichier = transaction == null;
        if(!finDeFichier) {
            final StringTokenizer tokenizer = new StringTokenizer(transaction,
                " ");
            finDeFichier = !tokenizer.hasMoreTokens();
            if(!finDeFichier) {
                final String commande = tokenizer.nextToken();
                finDeFichier = "exit".equals(commande);
            }
        }
        return finDeFichier;
    }

    /**
     * Lit une chaîne de caractères de la transaction.
     *
     * @param tokenizer La transaction à décoder
     * @return La chaîne de caractères lue
     * @throws BibliothequeException Si l'élément lu est manquant
     */
    private static String readString(final StringTokenizer tokenizer) throws BibliothequeException {
        if(tokenizer.hasMoreElements()) {
            return tokenizer.nextToken();
        }
        throw new BibliothequeException("Autre paramètre attendu");
    }

    /**
     * Lit une date au format YYYY-MM-DD de la transaction.
     *
     * @param tokenizer La transaction à décoder
     * @return La date lue
     * @throws BibliothequeException Si l'élément lu est manquant ou n'est pas une date correctement formatée
     */
    private static Timestamp readDate(final StringTokenizer tokenizer) throws BibliothequeException {
        if(tokenizer.hasMoreElements()) {
            final String token = tokenizer.nextToken();
            try {
                return FormatteurDate.timestampValue(token);
            } catch(ParseException parseException) {
                throw new BibliothequeException("Date en format YYYY-MM-DD attendue à la place  de \""
                    + token
                    + "\"",
                    parseException);
            }
        }
        throw new BibliothequeException("Autre paramètre attendu");
    }
}
