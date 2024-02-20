import com.mongodb.client.MongoClients;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.Arrays;
import java.util.Iterator;
import java.util.regex.Pattern;

public class MONGO {
    private static final String DATABASE_NAME = "test";
    private static final String COLLECTION_NAME = "peliculas";
    private final MongoClient mongoClient;
    private final MongoDatabase database;

    public MONGO(String connectionString) {
        mongoClient = MongoClients.create(connectionString);
        database = mongoClient.getDatabase(DATABASE_NAME);
    }

    public void insertarPeliculas() {
        MongoCollection<Document> peliculasCollection = database.getCollection(COLLECTION_NAME);

        Document fightClub = new Document("title", "Fight Club")
                .append("writer", "Chuck Palahniuk")
                .append("year", 1999)
                .append("actors", Arrays.asList("Brad Pitt", "Edward Norton"));

        Document pulpFiction = new Document("title", "Pulp Fiction")
                .append("writer", "Quentin Tarantino")
                .append("year", 1994)
                .append("actors", Arrays.asList("John Travolta", "Uma Thurman"));

        Document inglouriousBasterds = new Document("title", "Inglorious Basterds")
                .append("writer", "Quentin Tarantino")
                .append("year", 2009)
                .append("actors", Arrays.asList("Brad Pitt", "Diane Kruger", "Eli Roth"));

        Document theHobbit1 = new Document("title", "The Hobbit: An Unexpected Journey")
                .append("writer", "J.R.R. Tolkein")
                .append("year", 2012)
                .append("franchise", "The Hobbit");

        Document theHobbit2 = new Document("title", "The Hobbit: The Desolation of Smaug")
                .append("writer", "J.R.R. Tolkein")
                .append("year", 2013)
                .append("franchise", "The Hobbit");

        Document theHobbit3 = new Document("title", "The Hobbit: The Battle of the Five Armies")
                .append("writer", "J.R.R. Tolkein")
                .append("year", 2014)
                .append("franchise", "The Hobbit")
                .append("synopsis", "Bilbo and Company are forced to engage in a war against an array of combatants and keep the Lonely Mountain from falling into the hands of a rising darkness.");

        Document peeWeeHermansBigAdventure = new Document("title", "Pee Wee Herman's Big Adventure");

        Document avatar = new Document("title", "Avatar");

        peliculasCollection.insertMany(Arrays.asList(
                fightClub, pulpFiction, inglouriousBasterds, theHobbit1, theHobbit2, theHobbit3,
                peeWeeHermansBigAdventure, avatar));

        System.out.println("Documentos insertados correctamente en la colección de películas.");
    }
    
    
    //Actualizar documentos
    
    public void actualizarDocumentos() {
        MongoCollection<Document> peliculasCollection = database.getCollection(COLLECTION_NAME);

        // Actualizar sinopsis de "The Hobbit: An Unexpected Journey"
        peliculasCollection.updateOne(Filters.eq("title", "The Hobbit: An Unexpected Journey"),
                Updates.set("synopsis", "A reluctant hobbit, Bilbo Baggins, sets out to the Lonely Mountain with a spirited group of dwarves to reclaim their mountain home - and the gold within it - from the dragon Smaug."));

        // Actualizar sinopsis de "The Hobbit: The Desolation of Smaug"
        peliculasCollection.updateOne(Filters.eq("title", "The Hobbit: The Desolation of Smaug"),
                Updates.set("synopsis", "The dwarves, along with Bilbo Baggins and Gandalf the Grey, continue their quest to reclaim Erebor, their homeland, from Smaug. Bilbo Baggins is in possession of a mysterious and magical ring."));

        // Agregar actor a "Pulp Fiction"
        peliculasCollection.updateOne(Filters.eq("title", "Pulp Fiction"),
                Updates.addToSet("actors", "Samuel L. Jackson"));

        // Agregar actores a "Avatar"
        peliculasCollection.updateOne(Filters.eq("title", "Avatar"),
                Updates.addToSet("actors", Arrays.asList("Sam Worthington", "Zoe Saldaña", "Stephen Lang", "Sigourney Weaver", "Michelle Rodríguez")));

        // Agregar escritores a "Pee Wee Herman’s Big Adventure"
        peliculasCollection.updateOne(Filters.eq("title", "Pee Wee Herman's Big Adventure"),
                Updates.set("writers", Arrays.asList("Tom McCarthy", "Alex Ross Perry", "Allison Schroeder")));

        System.out.println("Documentos actualizados correctamente en la colección de películas.");
    }
       
    
    //BUSCAR DOCUMENTOS
    public void consultarDocumentos() {
        MongoCollection<Document> moviesCollection = database.getCollection(COLLECTION_NAME);

        // 1. Obtener todos los documentos
        System.out.println("Todos los documentos:");
        printDocuments(moviesCollection.find());

        // 2. Obtener documentos con writer igual a "Quentin Tarantino"
        System.out.println("Documentos con writer igual a 'Quentin Tarantino':");
        printDocuments(moviesCollection.find(Filters.eq("writer", "Quentin Tarantino")));

        // 3. Obtener documentos con actors que incluyan a "Brad Pitt"
        System.out.println("Documentos con actors que incluyan a 'Brad Pitt':");
        printDocuments(moviesCollection.find(Filters.in("actors", "Brad Pitt")));

        // 4. Obtener documentos con franchise igual a "The Hobbit"
        System.out.println("Documentos con franchise igual a 'The Hobbit':");
        printDocuments(moviesCollection.find(Filters.eq("franchise", "The Hobbit")));

        // 5. Obtener todas las películas de los 90s
        System.out.println("Películas de los 90s:");
        printDocuments(moviesCollection.find(Filters.and(
                Filters.gte("year", 1990),
                Filters.lt("year", 2000))));

        // 6. Obtener las películas estrenadas entre el año 2000 y 2010
        System.out.println("Películas estrenadas entre el año 2000 y 2010:");
        printDocuments(moviesCollection.find(Filters.and(
                Filters.gte("year", 2000),
                Filters.lt("year", 2011))));
    }

    //BUSQUEDA POR TEXTO EN LOS DOCUMENTOS
    
    //IMPRIMIR LOS DOCUMENTOS ENCONTRADOS
    private void printDocuments(FindIterable<Document> documents) {
        Iterator<Document> iterator = documents.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next().toJson());
        }
    }
    
    //BUSQUEDA POR TEXTO EN LOS DOCUMENTOS
	
	public void buscarPorTexto() {
	    MongoCollection<Document> moviesCollection = database.getCollection(COLLECTION_NAME);
	
	    // 1. Encontrar las películas que en la sinopsis contengan la palabra "Bilbo"
	    System.out.println("Películas que contienen la palabra 'Bilbo' en la sinopsis:");
	    printDocuments(moviesCollection.find(Filters.text("Bilbo")));
	
	    // 2. Encontrar las películas que en la sinopsis contengan la palabra "Gandalf"
	    
	    // CAMBIAR EL PRINTDOCUMENTS POR ALGO SIMILAR AL PUNTO 4. 5.
	    
	    System.out.println("Películas que contienen la palabra 'Gandalf' en la sinopsis:");
	    printDocuments(moviesCollection.find(Filters.text("Gandalf")));
	
	    // 3. Encontrar las películas que en la sinopsis contengan la palabra "Bilbo" y no la palabra "Gandalf"
	    System.out.println("Películas que contienen 'Bilbo' pero no 'Gandalf' en la sinopsis:");
	    Bson filterBilbo = Filters.and(
	    	    Filters.regex("synopsis", ".*Bilbo.*"),
	    	    Filters.not(Filters.regex("synopsis", ".*Gandalf.*"))
	    	);
	    FindIterable<Document> bilboNotGandalf = moviesCollection.find(filterBilbo);
	    for (Document document:bilboNotGandalf) {
	    	System.out.println(document.toJson());
	    }
	
	 // 4. Encontrar las películas que en la sinopsis contengan la palabra "dwarves" o "hobbit"
	    System.out.println("Películas que contienen 'dwarves' o 'hobbit' en la sinopsis:");
	    Bson filterHobbit = Filters.or(
	    		Filters.regex("synopsis", "dwarves"), 
	    		Filters.regex("synopsis", "hobbit")
	    );
	    
	    FindIterable<Document> dwarvesOrHobbit = moviesCollection.find(filterHobbit);
	    for (Document document : dwarvesOrHobbit) {
	        System.out.println(document.toJson());
	    }
	
	    // 5. Encontrar las películas que en la sinopsis contengan la palabra "gold" y "dragon"
	    System.out.println("Películas que contienen 'gold' y 'dragon' en la sinopsis:");
	    Bson filterGold = Filters.and(Filters.regex("synopsis", "gold"), Filters.regex("synopsis", "dragon"));
	    FindIterable<Document> goldAndDragon = moviesCollection.find(filterGold);
	    for (Document document : goldAndDragon) {
	        System.out.println(document.toJson());
	    }
	
	
	   //INDEXO A SINOPSIS COMO TEXTO
	   //moviesCollection.createIndex(Indexes.text("sinopsis"));
	
	}

	//ELIMINAR DOCUMENTOS
    public void eliminarDocumentos() {
        MongoCollection<Document> moviesCollection = database.getCollection(COLLECTION_NAME);

        // 1. Eliminar la película "Pee Wee Herman's Big Adventure"
        DeleteResult result1 = moviesCollection.deleteOne(Filters.eq("title", "Pee Wee Herman's Big Adventure"));
        System.out.println("Se eliminó la película 'Pee Wee Herman's Big Adventure': " + result1.getDeletedCount());

        // 2. Eliminar las películas que tengan como actor a Brad Pitt
        DeleteResult result2 = moviesCollection.deleteMany(Filters.eq("actors", "Brad Pitt"));
        System.out.println("Se eliminaron las películas con Brad Pitt como actor: " + result2.getDeletedCount());
    }
    
    

    public static void main(String[] args) {
        MONGO mainMongoDB = new MONGO("mongodb://localhost:27017");
        //mainMongoDB.insertarPeliculas();
        //mainMongoDB.actualizarDocumentos();
        mainMongoDB.consultarDocumentos();
        mainMongoDB.buscarPorTexto();
        //mainMongoDB.eliminarDocumentos();
        mainMongoDB.mongoClient.close();
    }
}

