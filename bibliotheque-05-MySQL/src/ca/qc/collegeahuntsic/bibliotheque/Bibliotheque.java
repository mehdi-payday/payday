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
import ca.qc.collegeahuntsic.bibliotheque.dto.LivreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.MembreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.PretDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.ReservationDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.BibliothequeException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidCriterionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidHibernateSessionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidPrimaryKeyException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidSortByPropertyException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dto.InvalidDTOClassException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dto.InvalidDTOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dto.MissingDTOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.facade.FacadeException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.ExistingLoanException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.ExistingReservationException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.InvalidLoanLimitException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.MissingLoanException;
import ca.qc.collegeahuntsic.bibliotheque.util.BibliothequeCreateur;
import ca.qc.collegeahuntsic.bibliotheque.util.FormatteurDate;

/**
 * Interface du système de gestion d'une bibliothèque.<br /><br />
 *
 * Ce programme permet d'appeler les transactions de base d'une bibliothèque.  Il gère des livres, des membres et des réservations. Les données
 * sont conservées dans une base de données relationnelles accédée avec JDBC. Pour une liste des transactions traitées, voir la méthode
 * {@link Bibliotheque#afficherAide() afficherAide()}.<br /><br />
 *
 * Paramètres :<br />
 * 0- site du serveur SQL ("local", "distant" ou "postgres")<br />
 * 1- nom de la BD<br />
 * 2- user id pour établir une connexion avec le serveur SQL<br />
 * 3- mot de passe pour le user id<br />
 * 4- fichier de transaction<br /><br />
 *
 * Pré-condition :<br />
 *   La base de données de la bibliothèque doit exister<br /><br />
 *
 * Post-condition :<br />
 *   Le programme effectue les maj associées à chaque transaction
 *
 * @author Team PayDay
 */
public final class Bibliotheque {
    private static BibliothequeCreateur gestionnaireBibliotheque;

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
            System.out.println("Usage: java Bibliotheque  <fichier-transactions>");
            return;
        }

        try {
            // Ouverture du fichier de transactions
            final InputStream sourceTransaction = Bibliotheque.class.getResourceAsStream("/"
                + arguments[4]);
            try(
                BufferedReader reader = new BufferedReader(new InputStreamReader(sourceTransaction))) {

                Bibliotheque.gestionnaireBibliotheque = new BibliothequeCreateur();
                Bibliotheque.traiterTransactions(reader);
            }
        } catch(Exception exception) {
            Bibliotheque.gestionnaireBibliotheque.rollbackTransaction();
            exception.printStackTrace(System.out);
        } finally {
            Bibliotheque.gestionnaireBibliotheque.close();
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
        System.out.println("\n\n\n");
        String transaction = Bibliotheque.lireTransaction(reader);
        while(!Bibliotheque.finTransaction(transaction)) {
            // Découpage de la transaction en mots
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
            System.out.println("> "
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
        try {
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
                    Bibliotheque.preterLivre(tokenizer);
                    break;
                case "renouveller":
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
                    System.out.println("  Transactions non reconnue.  Essayer \"aide\"");
            }

        } catch(InterruptedException interruptedException) {
            System.out.println("** "
                + interruptedException.toString());
            Bibliotheque.gestionnaireBibliotheque.rollback();
        } catch(
            FacadeException
            | InvalidDTOClassException
            | BibliothequeException
            | InvalidHibernateSessionException
            | InvalidDTOException
            | InvalidPrimaryKeyException
            | MissingDTOException
            | InvalidCriterionException
            | InvalidSortByPropertyException
            | ExistingReservationException
            | ExistingLoanException
            | InvalidLoanLimitException
            | MissingLoanException exception) {
            System.out.println("** "
                + exception.getMessage());
            Bibliotheque.gestionnaireBibliotheque.rollbackTransaction();
        }
    }

    /**
     * Affiche le menu des transactions acceptées par le système.
     */
    private static void afficherAide() {

        System.out.println();
        System.out.println("Chaque transaction comporte un nom et une liste d'arguments");
        System.out.println("séparés par des espaces. La liste peut être vide.");
        System.out.println(" Les dates sont en format yyyy-mm-dd.\n");
        System.out.println("Les transactions sont :");
        System.out.println("  aide");
        System.out.println("  inscrire <nom> <telephone> <limitePret>");
        System.out.println("  desinscrire <idMembre>");
        System.out.println("  acquerir <titre> <auteur> <dateAcquisition>");
        System.out.println("  vendre <idLivre>");
        System.out.println("  preter <idMembre> <idLivre>");
        System.out.println("  renouveler <idPret>");
        System.out.println("  retourner <idPret>");
        System.out.println("  reserver <idMembre> <idLivre>");
        System.out.println("  utiliser <idReservation>");
        System.out.println("  annuler <idReservation>");
        // System.out.println("  listerLivresRetard <dateCourante>");
        // System.out.println("  listerLivresTitre <mot>");
        // System.out.println("  listerLivres");
    }

    /**
     *
     * TODO Auto-generated method javadoc.
     *
     * @param tokenizer le tokenizer de la commande
     * @throws BibliothequeException TODO
     */
    private static void inscrireMembre(final StringTokenizer tokenizer) throws BibliothequeException {
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
            // TODO Auto-generated catch block
            throw new BibliothequeException(exception);
        }

    }

    private static void desinscrireMembre(final StringTokenizer tokenizer) throws BibliothequeException {
        try {
            final MembreDTO membreDTO = new MembreDTO();
            membreDTO.setIdMembre(Bibliotheque.readString(tokenizer));
            Bibliotheque.gestionnaireBibliotheque.getMembreFacade().desinscrire(Bibliotheque.gestionnaireBibliotheque.getSession()
                membreDTO);
            Bibliotheque.gestionnaireBibliotheque.commitTransaction();
        }catch(){

        }
    }

    private static void acquerirLivre(final StringTokenizer tokenizer) throws BibliothequeException {
        final LivreDTO livreDTO = new LivreDTO();
        livreDTO.setTitre(Bibliotheque.readString(tokenizer));
        livreDTO.setAuteur(Bibliotheque.readString(tokenizer));
        livreDTO.setDateAcquisition(Bibliotheque.readDate(tokenizer));
        Bibliotheque.gestionnaireBibliotheque.getLivreFacade().acquerir(Bibliotheque.gestionnaireBibliotheque.getConnexion(),
            livreDTO);
        Bibliotheque.gestionnaireBibliotheque.commitTransaction();
    }

    private static void vendreLivre(final StringTokenizer tokenizer) throws BibliothequeException {
        final LivreDTO livreDTO = new LivreDTO();
        livreDTO.setIdLivre(Bibliotheque.readString(tokenizer));
        Bibliotheque.gestionnaireBibliotheque.getLivreFacade().vendre(Bibliotheque.gestionnaireBibliotheque.getConnexion(),
            livreDTO);
        Bibliotheque.gestionnaireBibliotheque.commitTransaction();
    }

    private static void preterLivre(final StringTokenizer tokenizer) throws BibliothequeException {
        final MembreDTO membreDTO = new MembreDTO();
        membreDTO.setIdMembre(Bibliotheque.readString(tokenizer));
        final LivreDTO livreDTO = new LivreDTO();
        livreDTO.setIdLivre(Bibliotheque.readString(tokenizer));
        final PretDTO pretDTO = new PretDTO();
        pretDTO.setMembreDTO(membreDTO);
        pretDTO.setLivreDTO(livreDTO);
        Bibliotheque.gestionnaireBibliotheque.getPretFacade().commencer(Bibliotheque.gestionnaireBibliotheque.getConnexion(),
            pretDTO);
        Bibliotheque.gestionnaireBibliotheque.commitTransaction();
    }

    public static void renouvelerPret(final StringTokenizer tokenizer) throws BibliothequeException {
        final PretDTO pretDTO = new PretDTO();
        pretDTO.setIdPret(Bibliotheque.readString(tokenizer));

        Bibliotheque.gestionnaireBibliotheque.getPretFacade().renouveler(Bibliotheque.gestionnaireBibliotheque.getSession(),
            pretDTO);
        Bibliotheque.gestionnaireBibliotheque.commitTransaction();
    }

    private static void terminerPret(final StringTokenizer tokenizer) throws BibliothequeException {
        final PretDTO pretDTO = new PretDTO();
        pretDTO.setIdPret(Bibliotheque.readString(tokenizer));

        Bibliotheque.gestionnaireBibliotheque.getPretFacade().terminer(Bibliotheque.gestionnaireBibliotheque.getSession(),
            pretDTO);
        Bibliotheque.gestionnaireBibliotheque.commitTransaction();
    }

    private static void placerReservation(final StringTokenizer tokenizer) throws BibliothequeException {
        // Juste pour éviter deux timestamps de réservation strictement identiques
        Thread.sleep(1);
        final ReservationDTO reservationDTO = new ReservationDTO();
        if(reservationDTO.getMembreDTO() == null) {
            reservationDTO.setMembreDTO(new MembreDTO());
        }
        if(reservationDTO.getLivreDTO() == null) {
            reservationDTO.setLivreDTO(new LivreDTO());
        }
        reservationDTO.getMembreDTO().setIdMembre(Bibliotheque.readString(tokenizer));
        reservationDTO.getLivreDTO().setIdLivre(Bibliotheque.readString(tokenizer));
        final MembreDTO membreDTO = new MembreDTO();
        membreDTO.setIdMembre(reservationDTO.getMembreDTO().getIdMembre());
        final LivreDTO livreDTO = new LivreDTO();
        livreDTO.setIdLivre(reservationDTO.getLivreDTO().getIdLivre());
        Bibliotheque.gestionnaireBibliotheque.getReservationFacade().placer(Bibliotheque.gestionnaireBibliotheque.getConnexion(),
            reservationDTO);
        Bibliotheque.gestionnaireBibliotheque.commit();
    }

    private static void utiliserReservation(final StringTokenizer tokenizer) {
        final ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setIdReservation(Bibliotheque.readString(tokenizer));
        Bibliotheque.gestionnaireBibliotheque.getReservationFacade().utiliser(Bibliotheque.gestionnaireBibliotheque.getConnexion(),
            reservationDTO);
        Bibliotheque.gestionnaireBibliotheque.commit();
    }

    public static void annulerReservation(final StringTokenizer tokenizer) {
        final ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setIdReservation(Bibliotheque.readString(tokenizer));
        Bibliotheque.gestionnaireBibliotheque.getReservationFacade().annuler(Bibliotheque.gestionnaireBibliotheque.getConnexion(),
            reservationDTO);
        Bibliotheque.gestionnaireBibliotheque.commit();
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
