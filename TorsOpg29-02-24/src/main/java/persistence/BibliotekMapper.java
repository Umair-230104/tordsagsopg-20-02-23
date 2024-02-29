package persistence;

import entities.Bog;
import entities.Laaner;
import entities.Udlaan;
import exceptions.DatabaseException;
import exceptions.IllegalInputException;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BibliotekMapper {
    private Database database;

    public BibliotekMapper(Database database) {
        this.database = database;
    }

    public List<Laaner> getAllLaaners() throws DatabaseException {
        List<Laaner> lanerList = new ArrayList<>();
        String sql = """
                select laaner_id, navn, adresse, p.postnr, p.by from laaner l inner join postnummer p on p.postnr = l.postnr
                """;
        try (Connection connection = database.connect()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    int laaner_id = rs.getInt("laaner_id");
                    String navn = rs.getString("navn");
                    String adresse = rs.getString("adresse");
                    int postnr = rs.getInt("postnr");
                    lanerList.add(new Laaner(laaner_id, navn, adresse, postnr));
                }
            } catch (SQLException throwables) {
                throw new DatabaseException("Could not get all laaners from database");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DatabaseException("Could not establish connection to database");
        }
        return lanerList;
    }


    public Laaner getLaanerById(int laaner_id) throws DatabaseException {
        Laaner laaner = null;
        String sql = """
                SELECT * FROM laaner WHERE laaner_id = ?;
                """;
        try (Connection connection = database.connect()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, laaner_id);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    laaner_id = rs.getInt("laaner_id");
                    String navn = rs.getString("navn");
                    String adresse = rs.getString("adresse");
                    int postnr = rs.getInt("postnr");
                    laaner = new Laaner(laaner_id, navn, adresse, postnr);
                } else {
                    throw new DatabaseException("Låner with id = " + laaner_id + " is not found");
                }
            } catch (SQLException ex) {
                throw new DatabaseException("Could not find member with id = " + laaner_id + " in database");
            }
        } catch (SQLException ex) {
            throw new DatabaseException("Could not establish connection to database");
        }
        int a = 1;
        return laaner;
    }


    public List<Bog> getAllBooksAndTheirAuthor() throws DatabaseException {
        List<Bog> bogList = new ArrayList<>();
        String sql = """

                SELECT b.bog_id, b.titel, b.udgivelsesaar, b.forfatter_id, f.navn AS forfatternavn
                         FROM bog b
                         INNER JOIN forfatter f ON b.forfatter_id = f.forfatter_id;
                                         """;
        try (Connection connection = database.connect()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    int bog_id = rs.getInt("bog_id");
                    String titel = rs.getString("titel");
                    int udgivelsesaar = rs.getInt("udgivelsesaar");
                    int forfatter_id = rs.getInt("forfatter_id");
                    String forfatternavn = rs.getString("forfatternavn");
                    bogList.add(new Bog(bog_id, titel, udgivelsesaar, forfatter_id, forfatternavn));
                }
            } catch (SQLException throwables) {
                throw new DatabaseException("Could not get all books and their authors from database");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DatabaseException("Could not establish connection to database");
        }
        return bogList;
    }


    public List<Laaner> getAllLaanersAndTheirBooks() throws DatabaseException {
        Map<Integer, Laaner> laanerMap = new HashMap<>();
        String sql = """
                SELECT l.laaner_id, l.navn AS laaner_navn, l.adresse, l.postnr, b.bog_id, b.titel, b.udgivelsesaar, f.forfatter_id, f.navn AS forfatternavn
                FROM laaner l
                INNER JOIN udlaan u ON l.laaner_id = u.laaner_id
                INNER JOIN bog b ON u.bog_id = b.bog_id
                INNER JOIN forfatter f ON b.forfatter_id = f.forfatter_id;
                """;
        try (Connection connection = database.connect()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    int laanerId = rs.getInt("laaner_id");
                    Laaner laaner = laanerMap.getOrDefault(laanerId, new Laaner(laanerId, rs.getString("laaner_navn"), rs.getString("adresse"), rs.getInt("postnr")));
                    Bog bog = new Bog(rs.getInt("bog_id"), rs.getString("titel"), rs.getInt("udgivelsesaar"), rs.getInt("forfatter_id"), rs.getString("forfatternavn"));
                    laaner.addBog(bog); // Antager at der er en metode addBog() i Laaner-klassen for at tilføje en bog til laanerens liste af bøger.
                    laanerMap.putIfAbsent(laanerId, laaner);
                }
            } catch (SQLException e) {
                throw new DatabaseException("Could not get all borrowers and their books from database");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Could not establish connection to database");
        }
        return new ArrayList<>(laanerMap.values());
    }


    public Laaner insertlaaner(Laaner laaner) throws DatabaseException, IllegalInputException {
        boolean result = false;
        int newId = 0;
        String sql = """
                INSERT INTO laaner (navn, adresse, postnr) VALUES (?,?,?)
                """;
        try (Connection connection = database.connect()) {
            try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, laaner.getNavn());
                ps.setString(2, laaner.getAdresse());
                ps.setInt(3, laaner.getPostnr());

                int rowsAffected = ps.executeUpdate();
                if (rowsAffected == 1) {
                    result = true;
                } else {
                    throw new DatabaseException("Laaner with name = " + laaner.getNavn() + " could not be inserted into database");
                }

                ResultSet idResultset = ps.getGeneratedKeys();
                if (idResultset.next()) {
                    newId = idResultset.getInt(1);
                    laaner.setLaaner_id(newId);
                } else {
                    laaner = null;
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace(); // Tilføj denne linje for at se stack trace
                throw new DatabaseException("Could not insert laaner in database: " + throwables.getMessage());
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DatabaseException("Could not establish connection to database");
        }
        return laaner;
    }


    public Udlaan insertUdlån(Udlaan udlaan) throws DatabaseException, IllegalInputException {
        boolean result = false;
        int newId = 0;
        String sql = """
                INSERT INTO udlaan (bog_id, laaner_id, dato) VALUES (?, ?, ?)
                                """;
        try (Connection connection = database.connect()) {
            try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, udlaan.getBog_id());
                ps.setInt(2, udlaan.getLaaner_id());
                ps.setDate(3, udlaan.getUdlaansdato());
                int rowsAffected = ps.executeUpdate();
                if (rowsAffected == 1) {
                    result = true;
                } else {
                    throw new DatabaseException("Unable to insert the loan into the database.");
                }

            } catch (SQLException throwables) {
                throwables.printStackTrace(); // Tilføj denne linje for at se stack trace
                throw new DatabaseException("Could not insert udlaan in database: " + throwables.getMessage());
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DatabaseException("Could not establish connection to database");
        }
        return udlaan;
    }

    public Udlaan deleteUdlaan(Udlaan udlaan) throws DatabaseException, IllegalInputException {
        String sql = "DELETE FROM public.udlaan WHERE bog_id = ? AND laaner_id = ?";
        try (Connection connection = database.connect()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, udlaan.getBog_id()); // Antager at getBogId() henter bogens ID
                ps.setInt(2, udlaan.getLaaner_id()); // Antager at getLaanerId() henter lånerens ID
                int rowsAffected = ps.executeUpdate();
                if (rowsAffected != 1){
                    throw new DatabaseException("Unable to delete the loan from the database.");
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                throw new DatabaseException("Could not delete udlaan from database: " + throwables.getMessage());
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DatabaseException("Could not establish connection to database");
        }
        return udlaan;
    }


    public void updateBookTitle(int bog_id, String newTitle) throws DatabaseException {
        String sql = "UPDATE bog SET titel = ? WHERE bog_id = ?";
        try (Connection connection = database.connect();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, newTitle);
            ps.setInt(2, bog_id);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected != 1) {
                throw new DatabaseException("Unable to update book title.");
            }
        } catch (SQLException ex) {
            throw new DatabaseException("Could not update book title: " + ex.getMessage());
        }
    }

}
