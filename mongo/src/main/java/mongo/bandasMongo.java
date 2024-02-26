package mongo;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bson.Document;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import static com.mongodb.client.model.Sorts.descending;
import static com.mongodb.client.model.Sorts.ascending;

public class bandasMongo {
	private static final String DATABASE = "database";
	private static final String COLLECTION = "bandas";
	private  final MongoClient mongoClient;
	private  final MongoDatabase database;
	
		
	public bandasMongo(String connectionString){
		mongoClient = MongoClients.create(connectionString);
		database = mongoClient.getDatabase(DATABASE);
	}
		
		
	public void insertarDatos() {
		MongoCollection <Document> collection = database.getCollection(COLLECTION);
		
		List<Document> discosVer = new ArrayList<>();
        discosVer.add(new Document("nombre", "Dios Agujero Negro").append("año", 2008));
        discosVer.add(new Document("nombre", "Apichonados").append("año", 2007));
        discosVer.add(new Document("nombre", "Meridiano").append("año", 2006));
		
		Document verKBitch = new Document ("nombre","VER K BITCH")
			.append("genero","instrumental")
			.append("estilo","")
			.append("inscripcion","20/01/2018")
			.append("discos", discosVer)
			.append("barrio","versalles")
			.append("integrantes", 1);
			
			
		List<Document> discosTro = new ArrayList<>();
        discosTro.add(new Document("nombre", "Hecho Bolita").append("año", 2014));
	        
		Document trota = new Document ("nombre","TROTAMUNDOS")
			.append("genero","indie")
			.append("estilo","")
			.append("inscripcion","30/11/2017")
			.append("discos", discosTro)
			.append("barrio","villa luro")
			.append("integrantes", 4);
	
		List<Document> discosEfecto = new ArrayList<>();
        discosEfecto.add(new Document("nombre", "Efecto Alfons").append("año", 2000));
			
		Document efectoAlfons = new Document ("nombre","EFECTO ALFONS")
			.append("genero","rock")
			.append("estilo","power trio")
			.append("inscripcion","27/11/2017")
			.append("discos", discosEfecto)
			.append("barrio","barracas")
			.append("integrantes", 3);
		
		
		Document marce  = new Document ("nombre","MARCELO GIULLITTI")
			.append("genero","solista")
			.append("estilo","")
			.append("inscripcion","26/11/2017")
			.append("discos", "")
			.append("barrio","agronomia")
			.append("integrantes", 1);
	
					
			
					
		Document afterlife = new Document ("nombre","AFTERLIFE")
			.append("genero","rock")
			.append("estilo","rock alternativo")
			.append("inscripcion","14/11/2017")
			.append("discos", "")
			.append("barrio","balvanera")
			.append("integrantes", 5);
	
		collection.insertMany(Arrays.asList(verKBitch,trota,efectoAlfons,marce,afterlife));
			
		}
		
	//LECTURA DE BANDAS
		
	public void findAll() {
		MongoCollection <Document> collection = database.getCollection(COLLECTION);
		FindIterable<Document> documents = collection.find();
		
		//Mostrar los documentos
		System.out.println("Todas las bandas: ");
		for (Document doc:documents) {
			System.out.println(doc.toJson());
		}
			
	}
	
	public void findIntegrantes() {
		MongoCollection <Document> collection = database.getCollection(COLLECTION);
		FindIterable<Document> documents = collection.find().sort(descending("integrantes"));
		
		System.out.println("Todas las bandas por INTEGRANTES CRESC: ");
		for(Document doc:documents) {
			System.out.println(doc.toJson());
		}
	}
	
	
	public void findMayorIntegrantes() {
		MongoCollection <Document> collection = database.getCollection(COLLECTION);
		FindIterable<Document> documents = collection.find().sort(descending("integrantes")).limit(2);
		
		System.out.println("Las 2 bandas con mas integrantes: ");
		for(Document doc:documents) {
			System.out.println(doc.toJson());
		}
	}
	
	public void sumarUno() {
		MongoCollection <Document> collection = database.getCollection(COLLECTION);
		collection.updateMany(new Document(), new Document("$inc", new Document("integrantes", 1)));
	}
	
	public void dosMilSeis() {
		MongoCollection <Document> collection = database.getCollection(COLLECTION);
		FindIterable<Document> documents = collection.find(Filters.eq("discos.año",2006));
		
		System.out.println("Las bandas con discos en el 2006: ");
		for(Document doc:documents) {
			System.out.println(doc.toJson());
		}
		
	}
	
	public void barracas() {
		MongoCollection <Document> collection = database.getCollection(COLLECTION);
		FindIterable<Document> documents = collection.find(Filters.eq("barrio","barracas"));
		
		System.out.println("Las bandas del barrio BARRACAS: ");
		for(Document doc:documents) {
			System.out.println(doc.toJson());
		}
		
	}
	
	
	public static void main(String[] args) {
		bandasMongo mainMongoDB = new bandasMongo("mongodb://localhost:27017");
		//mainMongoDB.insertarDatos();
		mainMongoDB.findAll();
		mainMongoDB.findIntegrantes();
		mainMongoDB.findMayorIntegrantes();
		mainMongoDB.sumarUno();
		mainMongoDB.findAll();
		mainMongoDB.dosMilSeis();
		mainMongoDB.barracas();
	}

}
