import entities.Bog;
import entities.Laaner;
import entities.Udlaan;
import exceptions.DatabaseException;
import exceptions.IllegalInputException;
import persistence.BibliotekMapper;
import persistence.Database;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class Main {
    private final static String USER = "postgres";
    private final static String PASSWORD = "postgres";
    private final static String URL = "jdbc:postgresql://localhost:5432/Bib_Tors_opg";
    private static BibliotekMapper bibliotekMapper;

    public static void main(String[] args) throws ClassNotFoundException, DatabaseException, IllegalInputException {

        Database db = new Database(USER, PASSWORD, URL);
        bibliotekMapper = new BibliotekMapper(db);

//        List<Laaner> laaners = bibliotekMapper.getAllLaaners();
//        showLaaners(laaners);
//        showLaanerById(bibliotekMapper, 1);
//
//        List<Bog> books = bibliotekMapper.getAllBooksAndTheirAuthor();
//        showBooks(books);


//        List<Laaner> laanersAndBooks = bibliotekMapper.getAllLaanersAndTheirBooks();
//        showLaanersAndTheirBooks(laanersAndBooks);


        // Opretter en ny låner
        Laaner nyLaaner = new Laaner(0, "Nyt Navn", "Ny Adresse", 7490); // laaner_id sættes til 0, da den genereres af databasen
        // Indsætter den nye låner i databasen
//        Laaner indsatLaaner = bibliotekMapper.insertlaaner(nyLaaner);

        Udlaan udlaan1 = new Udlaan(1, 3, java.sql.Date.valueOf("2005-12-01"));
        Udlaan udlaan = new Udlaan(1, 3, Date.valueOf(LocalDate.of(2004, 1, 1)));
//        bibliotekMapper.insertUdlån(udlaan);

        Udlaan udDelete = new Udlaan(1, 3);
//        bibliotekMapper.deleteUdlaan(udDelete);

        bibliotekMapper.updateBookTitle(1, "HEJ");


    }

    private static void showLaanerById(BibliotekMapper bibliotekMapper, int laaner_id) throws DatabaseException {
        System.out.println("\n" + "***** Viser låner med id: " + laaner_id + " *******");
        System.out.println(bibliotekMapper.getLaanerById(laaner_id).toString());
    }

    private static void showLaaners(List<Laaner> laaners) {
        System.out.println("\n" + "***** Viser alle lånere *******");
        for (Laaner laaner : laaners) {
            System.out.println(laaner.toString());
        }
    }

    private static void showBooks(List<Bog> books) {
        System.out.println("\n" + "***** Viser alle bøger og deres forfattere *******");
        for (Bog bog : books) {
            System.out.println(bog.toString());
        }
    }


    private static void showLaanersAndTheirBooks(List<Laaner> laaners) {
        System.out.println("\n***** Viser alle lånere og de bøger de har lånt *******");
        for (Laaner laaner : laaners) {
            System.out.println("Låner ID: " + laaner.getLaaner_id() + ", Navn: " + laaner.getNavn() + ", Adresse: " + laaner.getAdresse() + ", Postnr: " + laaner.getPostnr());
            System.out.println("Har lånt følgende bøger:");
            for (Bog bog : laaner.getBoger()) { // Antager at Laaner klassen har en getBoger metode.
                System.out.println("\tBog ID: " + bog.getBog_id() + ", Titel: " + bog.getTitel() + ", Udgivelsesår: " + bog.getUdgivelsesaar() + ", Forfatter: " + bog.getForfatternavn());
            }
        }
    }


}