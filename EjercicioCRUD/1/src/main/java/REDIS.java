import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;
import java.util.Map;
import java.util.Set;

public class REDIS {
    private static Jedis jedis;

    public static void main(String[] args) {
        try {
            // Establecer conexión a Redis
            jedis = new Jedis("localhost", 6379);

            // Insertar documentos en la colección "películas"
            insertMovies();
            queryMovies();

            // Actualizar documentos
            updateMovies();

            // Realizar consultas en la colección "películas"
            queryMovies();
            
         // Eliminar documentos
            deleteMovies("Pee Wee Herman's Big Adventure"); // Eliminar por título
            deleteMoviesByWriter("Quentin Tarantino"); // Eliminar por escritor
            deleteMoviesWithActor("Brad Pitt"); // Eliminar por actor
                        
            // Cerrar la conexión a Redis
            jedis.close();
        } catch (JedisException e) {
            e.printStackTrace();
        }
    }

    private static void insertMovies() {
        insertMovie("Fight Club", "Chuck Palahniuk", "1999", "[Brad Pitt, Edward Norton]", "");
        insertMovie("Pulp Fiction", "Quentin Tarantino", "1994", "[John Travolta, Uma Thurman]", "");
        insertMovie("Inglorious Basterds", "Quentin Tarantino", "2009", "[Brad Pitt, Diane Kruger, Eli Roth]", "");
        insertMovie("The Hobbit: An Unexpected Journey", "J.R.R Tolkein", "2012", "", "The Hobbit");
        insertMovie("The Hobbit: The Desolation of Smaug", "J.R.R Tolkein", "2013", "", "The Hobbit");
        insertMovie("The Hobbit: The Battle of the Five Armies", "J.R.R Tolkein", "2012", "", "The Hobbit");
        insertMovie("Pee Wee Herman's Big Adventure", "", "", "", "");
        insertMovie("Avatar", "", "", "", "");
    }

    private static void insertMovie(String title, String writer, String year, String actors, String synopsis) {
        jedis.hset("peliculas:" + title, "Title", title);
        if (!writer.isEmpty()) jedis.hset("peliculas:" + title, "writer", writer);
        if (!year.isEmpty()) jedis.hset("peliculas:" + title, "year", year);
        if (!actors.isEmpty()) jedis.hset("peliculas:" + title, "actors", actors);
        if (!synopsis.isEmpty()) jedis.hset("peliculas:" + title, "synopsis", synopsis);
    }

    private static void updateMovies() {
        jedis.hset("peliculas:The Hobbit: An Unexpected Journey", "synopsis", "A reluctant hobbit, Bilbo Baggins, sets out to the Lonely Mountain with a spirited group of dwarves to reclaim their mountain home - and the gold within it - from the dragon Smaug.");
        jedis.hset("peliculas:The Hobbit: The Desolation of Smaug", "synopsis", "The dwarves, along with Bilbo Baggins and Gandalf the Grey, continue their quest to reclaim Erebor, their homeland, from Smaug. Bilbo Baggins is in possession of a mysterious and magical ring.");
        jedis.hset("peliculas:Pulp Fiction", "actors", "[John Travolta, Uma Thurman, Samuel L. Jackson]");
        jedis.hset("peliculas:Avatar", "actors", "[Sam Worthington, Zoe Saldaña, Stephen Lang, Sigourney Weaver, Michelle Rodríguez]");
        jedis.hset("peliculas:Pee Wee Herman’s Big Adventure", "writers", "[Tom McCarthy, Alex Ross Perry, Allison Schroeder]");
    }

    private static void queryMovies() {
        getAllMovies();
        getMoviesByWriter("Quentin Tarantino");
        getMoviesByActor("Brad Pitt");
        getMoviesByFranchise("The Hobbit");
        getMoviesByDecade(1990, 1999);
        getMoviesByDecade(2000, 2010);
    }

    private static void getAllMovies() {
        Set<String> keys = jedis.keys("peliculas:*");
        for (String key : keys) {
            Map<String, String> movie = jedis.hgetAll(key);
            System.out.println(movie);
        }
    }

    private static void getMoviesByWriter(String writer) {
        searchByKeyValue("writer", writer);
    }

    private static void getMoviesByActor(String actor) {
        searchByValueContaining("actors", actor);
    }

    private static void getMoviesByFranchise(String franchise) {
        searchByKeyValue("franchise", franchise);
    }

    private static void getMoviesByDecade(int startYear, int endYear) {
        Set<String> keys = jedis.keys("peliculas:*");
        for (String key : keys) {
            String yearValue = jedis.hget(key, "year");
            if (yearValue != null) {
                int year = Integer.parseInt(yearValue);
                if (year >= startYear && year <= endYear) {
                    Map<String, String> movie = jedis.hgetAll(key);
                    System.out.println(movie);
                }
            }
        }
    }

    private static void searchByKeyValue(String key, String value) {
        Set<String> keys = jedis.keys("peliculas:*");
        for (String movieKey : keys) {
            String storedValue = jedis.hget(movieKey, key);
            if (storedValue != null && storedValue.equals(value)) {
                Map<String, String> movie = jedis.hgetAll(movieKey);
                System.out.println(movie);
            }
        }
    }

    private static void searchByValueContaining(String key, String value) {
        Set<String> keys = jedis.keys("peliculas:*");
        for (String movieKey : keys) {
            String storedValue = jedis.hget(movieKey, key);
            if (storedValue != null && storedValue.contains(value)) {
                Map<String, String> movie = jedis.hgetAll(movieKey);
                System.out.println(movie);
            }
        }
    }
    
    private static void deleteMovies(String movieTitle) {
        Set<String> keys = jedis.keys("peliculas:" + movieTitle);
        for (String key : keys) {
            jedis.del(key);
            System.out.println("Película '" + movieTitle + "' eliminada.");
        }
    }

    private static void deleteMoviesByWriter(String writer) {
        Set<String> keys = jedis.keys("peliculas:*");
        for (String key : keys) {
            String writerValue = jedis.hget(key, "writer");
            if (writerValue != null && writerValue.equals(writer)) {
                jedis.del(key);
                System.out.println("Película escrita por '" + writer + "' eliminada: " + key);
            }
        }
    }

    private static void deleteMoviesWithActor(String actor) {
        Set<String> keys = jedis.keys("peliculas:*");
        for (String key : keys) {
            String actorsValue = jedis.hget(key, "actors");
            if (actorsValue != null && actorsValue.contains(actor)) {
                jedis.del(key);
                System.out.println("Película con el actor '" + actor + "' eliminada: " + key);
            }
        }
    }
    
    
}
