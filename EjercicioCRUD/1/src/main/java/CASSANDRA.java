import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;


import java.net.InetSocketAddress;
import java.util.Arrays;

public class CASSANDRA {

	private static final String CONTACT_POINT = "127.0.0.1";
    private static final int PORT = 9042;
    private static final String KEYSPACE = "cassandraPeliculas";
    private static final String LOCAL_DATACENTER = "datacenter1";

   

    // Creación de keyspace
    private static void crearKeyspace(CqlSession session) {
        session.execute("CREATE KEYSPACE IF NOT EXISTS " + KEYSPACE +
                " WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1}");
        session.execute("USE " + KEYSPACE);
    }
    
    private static CqlSession connect() {
        try {
            return CqlSession.builder()
                    .addContactPoint(new InetSocketAddress(CONTACT_POINT, PORT))
                    .withLocalDatacenter(LOCAL_DATACENTER)
                    .build();
        } catch (Exception e) {
            System.out.println("Error al conectar con Cassandra: " + e.getMessage());
            return null;
        }
    }

    // Creación de tabla
    private static void crearTabla(CqlSession session) {
        session.execute("CREATE TABLE IF NOT EXISTS cassandraPeliculas.movies (" +
                "title TEXT PRIMARY KEY," +
                "writer TEXT," +
                "year INT," +
                "actors LIST<TEXT>," +
                "franchise TEXT," +
                "synopsis TEXT)");
    }

    // A. Insertar documentos
    private static void insertarPeliculas(CqlSession session) {
        insertarPelicula(session, "Fight Club", "Chuck Palahniuk", 1999, new String[]{"Brad Pitt", "Edward Norton"}, null, null);
        insertarPelicula(session, "Pulp Fiction", "Quentin Tarantino", 1994, new String[]{"John Travolta", "Uma Thurman"}, null, null);
        insertarPelicula(session, "Inglorious Basterds", "Quentin Tarantino", 2009, new String[]{"Brad Pitt", "Diane Kruger", "Eli Roth"}, null, null);
        insertarPelicula(session, "The Hobbit: An Unexpected Journey", "J.R.R. Tolkein", 2012, null, "The Hobbit", null);
        insertarPelicula(session, "The Hobbit: The Desolation of Smaug", "J.R.R. Tolkein", 2013, null, "The Hobbit", null);
        insertarPelicula(session, "The Hobbit: The Battle of the Five Armies", "J.R.R. Tolkein", 2012, null, "The Hobbit", "Bilbo and Company are forced to engage in a war against an array of combatants and keep the Lonely Mountain from falling into the hands of a rising darkness.");
        insertarPelicula(session, "Pee Wee Herman's Big Adventure", null, 0, null, null, null);
        insertarPelicula(session, "Avatar", null, 0, null, null, null);
    }

    // Función para insertar una película
    private static void insertarPelicula(CqlSession session, String title, String writer, int year, String[] actors, String franchise, String synopsis) {
        session.execute("INSERT INTO movies (title, writer, year, actors, franchise, synopsis) VALUES (?, ?, ?, ?, ?, ?)",
                title, writer, year, Arrays.asList(actors), franchise, synopsis);
        System.out.println("Película insertada correctamente: " + title);
    }

    // B. Actualizar documentos
    private static void actualizarSinopsis(CqlSession session) {
        actualizarSinopsisPelicula(session, "The Hobbit: An Unexpected Journey",
                "A reluctant hobbit, Bilbo Baggins, sets out to the Lonely Mountain with a spirited group of dwarves to reclaim their mountain home - and the gold within it - from the dragon Smaug.");
        actualizarSinopsisPelicula(session, "The Hobbit: The Desolation of Smaug",
                "The dwarves, along with Bilbo Baggins and Gandalf the Grey, continue their quest to reclaim Erebor, their homeland, from Smaug. Bilbo Baggins is in possession of a mysterious and magical ring.");
    }

    // Función para actualizar la sinopsis de una película
    private static void actualizarSinopsisPelicula(CqlSession session, String title, String nuevaSinopsis) {
        session.execute("UPDATE movies SET synopsis = ? WHERE title = ?", nuevaSinopsis, title);
        System.out.println("Sinopsis de la película '" + title + "' actualizada correctamente");
    }
    
 // 3. Agregar un actor llamado "Samuel L. Jackson" a la película "Pulp Fiction"
    private static void agregarActorACinta(CqlSession session, String pelicula, String actor) {
        session.execute("UPDATE movies SET actors = actors + ? WHERE title = ?", actor, pelicula);
        System.out.println("Actor '" + actor + "' agregado a la película '" + pelicula + "'");
    }

    // 4. Agregar los actores Sam Worthington, Zoe Saldaña, Stephen Lang, Sigourney Weaver y Michelle Rodríguez
    private static void agregarActoresACinta(CqlSession session, String pelicula, String... actores) {
        session.execute("UPDATE movies SET actors = actors + ? WHERE title = ?", Arrays.asList(actores), pelicula);
        System.out.println("Actores agregados a la película '" + pelicula + "'");
    }

    // 5. Agregar escritores a la película “Pee Wee Herman’s Big Adventure”: Tom McCarthy, Alex Ross Perry, Allison Schroeder
    private static void agregarEscritoresACinta(CqlSession session, String pelicula, String... escritores) {
        session.execute("UPDATE movies SET writer = ? WHERE title = ?", escritores[0], pelicula);
        System.out.println("Escritores agregados a la película '" + pelicula + "'");
    }


    // C. Realizar consultas
    private static void consultarPeliculas(CqlSession session) {
        consultarPeliculasPorFiltro(session, "SELECT * FROM movies","Todas las películas");
        consultarPeliculasPorFiltro(session, "SELECT * FROM movies WHERE writer = 'Quentin Tarantino' ALLOW FILTERING", "Películas escritas por Quentin Tarantino");
        consultarPeliculasPorFiltro(session, "SELECT * FROM movies WHERE actors CONTAINS 'Brad Pitt' ALLOW FILTERING",  "Películas con Brad Pitt como actor");
        consultarPeliculasPorFiltro(session, "SELECT * FROM movies WHERE franchise = 'The Hobbit' ALLOW FILTERING",     "Películas de The Hobbit");
        consultarPeliculasPorFiltro(session, "SELECT * FROM movies WHERE year >= 1990 AND year < 2000 ALLOW FILTERING", "Películas de los 90s");
        consultarPeliculasPorFiltro(session, "SELECT * FROM movies WHERE year >= 2000 AND year < 2010 ALLOW FILTERING", "Películas entre 2000 y 2010");
    }

    // Función para consultar películas por un filtro específico
    private static void consultarPeliculasPorFiltro(CqlSession session, String query, String descripcion) {
        ResultSet result = session.execute(query);
        System.out.println(descripcion + ":");
        result.forEach(row -> System.out.println(row));
        System.out.println();
    }

    // D. Búsqueda por Texto (Text Search)
    private static void buscarPorTexto(CqlSession session) {
        buscarPeliculasPorTexto(session, "Bilbo");
        buscarPeliculasPorTexto(session, "Gandalf");
        buscarPeliculasPorTexto(session, "Bilbo", "Gandalf");
        buscarPeliculasPorTexto(session, "dwarves", "hobbit");
        buscarPeliculasPorTexto(session, "gold", "dragon");
    }

    // Función para buscar películas por texto en la sinopsis
    private static void buscarPeliculasPorTexto(CqlSession session, String... textos) {
        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM movies WHERE synopsis CONTAINS ");
        for (int i = 0; i < textos.length; i++) {
            queryBuilder.append("'" + textos[i] + "'");
            if (i < textos.length - 1) {
                queryBuilder.append(" AND synopsis CONTAINS ");
            }
        }
        ResultSet result = session.execute(queryBuilder.toString());
        System.out.println("Búsqueda por texto (" + Arrays.toString(textos) + "):");
        result.forEach(row -> System.out.println(row));
        System.out.println();
    }

    // E. Eliminar Documentos
    private static void eliminarPelicula(CqlSession session) {
        eliminarPeliculaPorTitulo(session, "Pee Wee Herman's Big Adventure");
        eliminarPeliculasPorActor(session, "Brad Pitt");
    }

    // Función para eliminar una película por título
    private static void eliminarPeliculaPorTitulo(CqlSession session, String title) {
        session.execute("DELETE FROM movies WHERE title = ?", title);
        System.out.println("Película eliminada correctamente: " + title);
    }

    // Función para eliminar películas por actor
    private static void eliminarPeliculasPorActor(CqlSession session, String actor) {
        session.execute("DELETE FROM movies WHERE actors CONTAINS ?", actor);
        System.out.println("Películas con el actor '" + actor + "' eliminadas correctamente");
    }
    
    
    //MAIN
    public static void main(String[] args) {
        	
			CqlSession session = connect();
			
			//crearKeyspace(session);
			//crearTabla(session);
			
			//Operaciones CRUD
			insertarPeliculas(session);
			actualizarSinopsis(session);
			
			agregarActorACinta(session, "Pulp Fiction", "Samuel L. Jackson");
			agregarActoresACinta(session, "Avatar", "Sam Worthington", "Zoe Saldaña", "Stephen Lang", "Sigourney Weaver", "Michelle Rodríguez");
			agregarEscritoresACinta(session, "Pee Wee Herman's Big Adventure", "Tom McCarthy", "Alex Ross Perry", "Allison Schroeder");

			consultarPeliculas(session);
			buscarPorTexto(session);
			eliminarPelicula(session);
			  
			session.close(); // Cierra la sesión cuando ya no la necesites							       				   
    }
    
    
}
