import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Session;
import org.neo4j.driver.Transaction;
import org.neo4j.driver.TransactionWork;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import java.util.List;
import static org.neo4j.driver.Values.parameters;

public class NEO4J {
    private final Driver driver;

    public NEO4J(String uri, String user, String password) {
        this.driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
    }

    public void close() {
        this.driver.close();
    }

    // A. Insertar documentos en una colección llamada películas
    public void insertMovies() {
        try (Session session = driver.session()) {
            session.writeTransaction(new TransactionWork<Void>() {
                @Override
                public Void execute(Transaction tx) {
                    tx.run("CREATE (m:Movie {title: 'Fight Club', writer: 'Chuck Palahniuk', year: 1999, actors: ['Brad Pitt', 'Edward Norton']})");
                    tx.run("CREATE (m:Movie {title: 'Pulp Fiction', writer: 'Quentin Tarantino', year: 1994, actors: ['John Travolta', 'Uma Thurman']})");
                    tx.run("CREATE (m:Movie {title: 'Inglorious Basterds', writer: 'Quentin Tarantino', year: 2009, actors: ['Brad Pitt', 'Diane Kruger', 'Eli Roth']})");
                    tx.run("CREATE (m:Movie {title: 'The Hobbit: An Unexpected Journey', writer: 'J.R.R. Tolkien', year: 2012, franchise: 'The Hobbit'})");
                    tx.run("CREATE (m:Movie {title: 'The Hobbit: The Desolation of Smaug', writer: 'J.R.R. Tolkien', year: 2013, franchise: 'The Hobbit'})");
                    tx.run("CREATE (m:Movie {title: 'The Hobbit: The Battle of the Five Armies', writer: 'J.R.R. Tolkien', year: 2012, franchise: 'The Hobbit', synopsis: 'Bilbo and Company are forced to engage in a war against an array of combatants and keep the Lonely Mountain from falling into the hands of a rising darkness.'})");
                    tx.run("CREATE (m:Movie {title: 'Pee Wee Hermans Big Adventure'})");
                    tx.run("CREATE (m:Movie {title: 'Avatar'})");
                    return null;
                }
            });
        }
    }

    // B. Actualizar documentos
    public void updateMovies() {
        try (Session session = driver.session()) {
            session.writeTransaction(new TransactionWork<Void>() {
                @Override
                public Void execute(Transaction tx) {
                    // 1. Agregar sinopsis a "The Hobbit: An Unexpected Journey"
                    tx.run("MATCH (m:Movie {title: 'The Hobbit: An Unexpected Journey'}) SET m.synopsis = 'A reluctant hobbit, Bilbo Baggins, sets out to the Lonely Mountain with a spirited group of dwarves to reclaim their mountain home - and the gold within it - from the dragon Smaug.'");
                    // 2. Agregar sinopsis a "The Hobbit: The Desolation of Smaug"
                    tx.run("MATCH (m:Movie {title: 'The Hobbit: The Desolation of Smaug'}) SET m.synopsis = 'The dwarves, along with Bilbo Baggins and Gandalf the Grey, continue their quest to reclaim Erebor, their homeland, from Smaug. Bilbo Baggins is in possession of a mysterious and magical ring.'");
                    // 3. Agregar actor "Samuel L. Jackson" a "Pulp Fiction"
                    tx.run("MATCH (m:Movie {title: 'Pulp Fiction'}) SET m.actors = m.actors + 'Samuel L. Jackson'");
                    // 4. Agregar actores
                    tx.run("MATCH (m:Movie {title: 'Avatar'}) SET m.actors = ['Sam Worthington', 'Zoe Saldana', 'Stephen Lang', 'Sigourney Weaver', 'Michelle Rodriguez']");
                    // 5. Agregar escritores a "Pee Wee Herman's Big Adventure"
                    tx.run("MATCH (m:Movie {title: 'Pee Wee Hermans Big Adventure'}) SET m.writers = ['Tom McCarthy', 'Alex Ross Perry', 'Allison Schroeder']");
                    return null;
                }
            });
        }
    }

    // C. Realizar consultas en la colección movies
    public void executeQueries() {
        try (Session session = driver.session()) {
            // 1. Obtener todos los documentos
            executeQuery(session, "MATCH (m:Movie) RETURN m");
            // 2. Obtener documentos con writer igual a "Quentin Tarantino"
            executeQuery(session, "MATCH (m:Movie) WHERE m.writer = 'Quentin Tarantino' RETURN m");
            // 3. Obtener documentos con actors que incluyan a "Brad Pitt"
            executeQuery(session, "MATCH (m:Movie) WHERE 'Brad Pitt' IN m.actors RETURN m");
            // 4. Obtener documentos con franchise igual a "The Hobbit"
            executeQuery(session, "MATCH (m:Movie) WHERE m.franchise = 'The Hobbit' RETURN m");
            // 5. Obtener todas las películas de los 90s
            executeQuery(session, "MATCH (m:Movie) WHERE m.year >= 1990 AND m.year < 2000 RETURN m");
            // 6. Obtener las películas estrenadas entre el año 2000 y 2010
            executeQuery(session, "MATCH (m:Movie) WHERE m.year >= 2000 AND m.year <= 2010 RETURN m");
        }
    }
    
 // D. Búsqueda por Texto (Text Search)
    public void textSearch() {
        try (Session session = driver.session()) {
            // 1. Encontrar las películas que en la sinopsis contengan la palabra "Bilbo"
            executeQuery(session, "MATCH (m:Movie) WHERE m.synopsis CONTAINS 'Bilbo' RETURN m");
            // 2. Encontrar las películas que en la sinopsis contengan la palabra "Gandalf"
            executeQuery(session, "MATCH (m:Movie) WHERE m.synopsis CONTAINS 'Gandalf' RETURN m");
            // 3. Encontrar las películas que en la sinopsis contengan la palabra "Bilbo" y no la palabra "Gandalf"
            executeQuery(session, "MATCH (m:Movie) WHERE m.synopsis CONTAINS 'Bilbo' AND NOT m.synopsis CONTAINS 'Gandalf' RETURN m");
            // 4. Encontrar las películas que en la sinopsis contengan la palabra "dwarves" o "hobbit"
            executeQuery(session, "MATCH (m:Movie) WHERE m.synopsis CONTAINS 'dwarves' OR m.synopsis CONTAINS 'hobbit' RETURN m");
            // 5. Encontrar las películas que en la sinopsis contengan la palabra "gold" y "dragon"
            executeQuery(session, "MATCH (m:Movie) WHERE m.synopsis CONTAINS 'gold' AND m.synopsis CONTAINS 'dragon' RETURN m");
        }
    }
    
    public void deleteMovie(String title) {
        try (Session session = driver.session()) {
            session.writeTransaction(tx -> tx.run("MATCH (m:Movie {title: $title}) DETACH DELETE m", parameters("title", title)));
        }
    }

    public void deleteMoviesWithActor(String actorName) {
        try (Session session = driver.session()) {
            session.writeTransaction(tx -> tx.run(
                    "MATCH (a:Actor)-[:ACTS_IN]->(m:Movie) " +
                            "WHERE a.name = $actorName " +
                            "DETACH DELETE m",
                    parameters("actorName", actorName)));
        }
    }

    // Método para ejecutar una consulta y mostrar el resultado
    private static void executeQuery(Session session, String query) {
        Result result = session.run(query);
        System.out.println("Query: " + query);
        while (result.hasNext()) {
            Record record = result.next();
            System.out.println(record.get("m").asMap());
        }
        System.out.println("-----------------------------------------");
    }

    public static void main(String[] args) {
        NEO4J mainNeo4j = new NEO4J("bolt://localhost:7687", "neo4j", "password");
        
        mainNeo4j.insertMovies();
        mainNeo4j.updateMovies();
        
     // Eliminar la película "Pee Wee Herman's Big Adventure"
        mainNeo4j.deleteMovie("Pee Wee Herman's Big Adventure");

     // Eliminar las películas que tengan como actor a Brad Pit
        mainNeo4j.deleteMoviesWithActor("Brad Pitt");
        mainNeo4j.executeQueries();
        mainNeo4j.close();
    }
}