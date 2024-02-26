import java.lang.reflect.Array;
import java.util.Arrays;

import org.bson.Document;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.*;
import org.bson.Document;
import org.bson.conversions.Bson;

public class MongoCRUD {
	
	private static final String DATABASE = "crud";
	private static final String COLLECTION = "peliculas";
	private final MongoClient mongoClient;
	private final MongoDatabase database;
	
	public MongoCRUD(String connectionString) {
		mongoClient = MongoClients.create(connectionString);
		database = mongoClient.getDatabase(DATABASE);
	}
	
	public void insertarDatos() {
		MongoCollection <Document> collection = database.getCollection(COLLECTION);
		
		
		Document fight_club = new Document("title", "Fight Club")
				.append("writer", "Chuck Palahniuk")
				.append("year",1999)
				.append("actors", Arrays.asList("Brad Pitt", "Edward Norton"))
			;
		
		Document pulp_fiction = new Document ("title", "Pulp Fiction")
				.append("writer", "Quentin Tarantino")
				.append("year", 1994)
				.append("actors", Arrays.asList("John Travolta","Uma Thurman"))
			;
		
		
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
	        
	    collection.insertMany(Arrays.asList(fight_club,pulp_fiction,inglouriousBasterds,theHobbit1,theHobbit2,theHobbit3));   
	}
	
	
	public void findAll() {
		MongoCollection <Document> collection = database.getCollection(COLLECTION);
		
		FindIterable<Document> documents = collection.find();
		
		for(Document doc : documents) {
			System.out.println(doc.toJson());
		}
	}
	
	
	//UPDATE DOCUMENTS
	
	public void update() {
		MongoCollection <Document> collection = database.getCollection(COLLECTION);
		
		//Actualizar The Hobbit 1
		collection.updateOne(Filters.eq("title", "The Hobbit: An Unexpected Journey"),Updates.set("synopsis", "A reluctant hobbit, Bilbo Baggins, sets out to the Lonely Mountain with a spirited group of dwarves to reclaim their mountain home - and the gold within it - from the dragon Smaug."));
		System.out.println();
		System.out.println("Peliculas actualizadas: ");
		findAll();
		
		
		//Actualizar The Hobbit 2
		collection.updateOne(Filters.eq("title","The Hobbit: The Desolation of Smaug"), Updates.set("synopsis", "The dwarves, along with Bilbo Baggins and Gandalf the Grey, continue their \r\n"
				+ "quest to reclaim Erebor, their homeland, from Smaug. Bilbo Baggins is in \r\n"
				+ "possession of a mysterious and magical ring."));
		
		System.out.println();
		System.out.println("Peliculas actualizadas: ");
		findAll();
		
		//Actualizar PULP FICTION
		collection.updateOne(Filters.eq("title","Pulp Fiction"),Updates.addToSet("actors","Samuel L Jackson"));
		System.out.println();
		System.out.println("Peliculas actualizadas: ");
		findAll();
		
		
	}
	
	
	public void buscar() {
		MongoCollection<Document> collection = database.getCollection(COLLECTION);
		
		//Dirigidas por Tarantino
		FindIterable<Document> tarantino = collection.find(Filters.eq("writer", "Quentin Tarantino"));
		System.out.println();
		System.out.println("Peliculas dirigidas por Quentin Tarantino: ");
		for(Document doc:tarantino){
			System.out.println(doc.toJson());
		}
		
		//Actor Brad pitt
		FindIterable<Document> bradPitt = collection.find(Filters.in("actors","Brad Pitt"));
		System.out.println();
		System.out.println("Peliculas donde actua Brad Pitt: ");
		for(Document doc:bradPitt){
			System.out.println(doc.toJson());
		}

		//THE HOBBIT
		FindIterable<Document> theHobbit = collection.find(Filters.eq("franchise","The Hobbit"));
		System.out.println();
		System.out.println("Peliculas de la franquicia THE HOBBIT: ");
		for(Document doc:theHobbit){
			System.out.println(doc.toJson());
		}
		
		//1990
		FindIterable<Document> noventas = collection.find(Filters.and(Filters.gte("year", 1990),Filters.lte("year", 2000)));
		System.out.println();
		System.out.println("Peliculas de los noventas: ");
		for(Document doc:noventas){
			System.out.println(doc.toJson());
		}		
		
		//1990
		FindIterable<Document> entre = collection.find(Filters.and(Filters.gte("year", 2000),Filters.lte("year", 2010)));
		System.out.println();
		System.out.println("Peliculas entre 2000 y 2010: ");
		for(Document doc:entre){
			System.out.println(doc.toJson());				
		}	
	}
	
	
	public void porTexto() {
		MongoCollection<Document> collection = database.getCollection(COLLECTION);
		
		//1. Encontrar las películas que en la sinopsis contengan la palabra "Bilbo"
		FindIterable<Document> bilbo = collection.find(Filters.regex("synopsis", ".*Bilbo.*"));
		System.out.println();
		System.out.println("Peliculas cuya sinopsis contiene BILBO: ");
		for(Document doc:bilbo){
			System.out.println(doc.toJson());				
		}
		
		//2. Encontrar las películas que en la sinopsis contengan la palabra "Gandalf"
		FindIterable<Document> gandalf = collection.find(Filters.regex("synopsis", ".*Gandalf.*"));
		System.out.println();
		System.out.println("Peliculas cuya sinopsis contiene GANDALF: ");
		for(Document doc:gandalf){
			System.out.println(doc.toJson());			
		}
				
		//3. Encontrar las películas que en la sinopsis contengan la palabra "Bilbo" y no la palabra "Gandalf"
		// Filtrar películas que contienen 'Bilbo' pero no 'Gandalf' en la sinopsis
        FindIterable<Document> bilboNotGandalf = collection.find(
                Filters.and(
                        Filters.regex("synopsis", ".*Bilbo.*"),
                        Filters.not(Filters.regex("synopsis", ".*Gandalf.*"))
                )
        );
        System.out.println();
        System.out.println("Películas que contienen 'Bilbo' pero no 'Gandalf' en la sinopsis:");
        for (Document document : bilboNotGandalf) {
            System.out.println(document.toJson());
        }
		
		//4. Encontrar las películas que en la sinopsis contengan la palabra "dwarves" ó "hobbit"
        FindIterable<Document> hobbitOrDwarves = collection.find(
                Filters.or(
                        Filters.regex("synopsis", ".*hobbit.*"),
                        Filters.regex("synopsis", ".*dwarves.*")
                )
        );
        System.out.println();
        System.out.println("Películas que sinopsis contengan la palabra dwarves ó hobbit:");
        for (Document document : hobbitOrDwarves) {
            System.out.println(document.toJson());
        }
        
		//5. Encontrar las películas que en la sinopsis contengan la palabra "gold" y "dragon"
        FindIterable<Document> goldDragon = collection.find(
                Filters.and(
                        Filters.regex("synopsis", ".*hobbit.*"),
                        Filters.regex("synopsis", ".*dwarves.*")
                )
        );
        System.out.println();
        System.out.println("Películas que sinopsis contengan la palabra gold ó dragon:");
        for (Document document : goldDragon) {
            System.out.println(document.toJson());
        }
        
	}
	
	public void eliminar() {
		MongoCollection<Document> collection = database.getCollection(COLLECTION);
		
		DeleteResult result = collection.deleteMany(Filters.in("actors","Brad Pitt"));
		System.out.println("\nSe eliminaron las películas con Brad Pitt como actor: " + result.getDeletedCount());
		findAll();
	}
	
	
	public static void main(String[] args) {
		MongoCRUD mainMongoDB = new MongoCRUD ("mongodb://localhost:27017");
		
		//mainMongoDB.insertarDatos();
		mainMongoDB.findAll();
		//mainMongoDB.update();
		mainMongoDB.buscar();
		mainMongoDB.porTexto();
		mainMongoDB.eliminar();
		

	}

}
