import java.net.InetSocketAddress;
import java.util.Arrays;

import org.bson.Document;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;




public class TestMongoCassandra {
	
	//Mongo
	private static final String DATABASE = "test";
	private static final String COLLECTION = "testCollection";
	private final MongoClient mongoClient;
	private final MongoDatabase database;
	
	//Cassandra
	private static final String CONTACT_POINT = "127.0.0.1";
	private static final int PORT = 9042;
	private static final String KEYSPACE = "testCassandra";
	private static final String DATACENTER = "datacenter1";

	
	public TestMongoCassandra(String connectionString) {
		mongoClient = MongoClients.create(connectionString);
		database = mongoClient.getDatabase(DATABASE);
	}
	
	//CONEXION CASSANDRA
	public static CqlSession connect() {
		return CqlSession.builder()
				.addContactPoint(new InetSocketAddress(CONTACT_POINT, PORT))
				.withLocalDatacenter(DATACENTER)
				.build();
	}
	
	
	//KEYSPACE
	public static void crearKeyspace(CqlSession session) {
		session.execute("CREATE KEYSPACE IF NOT EXISTS " + KEYSPACE + " WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1}");
		session.execute("USE " + KEYSPACE);
	}
	
	
	public static void crearTablas(CqlSession session) {
		session.execute("CREATE TABLE IF NOT EXISTS prueba("
				+"id INT PRIMARY KEY,"
				+"nombre TEXT"
				+ ")");
	}
	
	
	public static void insertTablas(CqlSession session) {
		session.execute("INSERT INTO prueba(id, nombre) VALUES(1,'Carlos Perez')");
		session.execute("INSERT INTO prueba(id,nombre)VALUES(2, 'Carlos Perez')");
	}
	
	public static void readTablas(CqlSession session) {
		ResultSet result = session.execute("SELECT * FROM prueba");
		
		for(Row row : result) {
			int ID = row.getInt("id");
			String NAME = row.getString("nombre");
			
			System.out.println("Prueba completada: "+ ID + ", " + NAME);
		}
	}
	
	public static void updateTablas(CqlSession session) {
		session.execute("UPDATE prueba SET nombre = 'Maria Garcias' WHERE id = 2");
	}
	
	
	public static void deleteTablas(CqlSession session) {
		session.execute("DELETE FROM prueba WHERE id = 1");		
	}
	
	
	
	public void crearDocumentos() {
		MongoCollection<Document> collection = database.getCollection(COLLECTION);
		
		Document prueba = new Document("id",1)
				.append("nombre","Carlos Perez")			
		;
		
		Document prueba2 = new Document("id",2)
				.append("nombre", "Carlos Perez");
		
		System.out.println("CREACION EXITOSA");
		collection.insertMany(Arrays.asList(prueba,prueba2));
		
	}
	
	public void readDocumentos() {
		MongoCollection<Document> collection = database.getCollection(COLLECTION);
		
		FindIterable<Document> document = collection.find();
		
		for(Document doc:document) {
			System.out.println(doc.toJson());
		}
	}
	
	public void updateDocumentos() {
		MongoCollection<Document> collection = database.getCollection(COLLECTION);
		
		collection.updateOne(Filters.eq("id",2),Updates.set("nombre","Maria Garcia"));
		readDocumentos();
	}
	
	public void deleteDocumentos() {
		MongoCollection<Document> collection = database.getCollection(COLLECTION);
		
		collection.deleteMany(Filters.eq("id",1));
		collection.deleteMany(Filters.eq("nombre","Carlos Perez"));
		readDocumentos();
		
	}
	
	public static void main(String[] args) {
		TestMongoCassandra mongoClient = new TestMongoCassandra("mongodb://localhost:27017");
		CqlSession session = connect();
		
		crearKeyspace(session);
		crearTablas(session);
		insertTablas(session);
		updateTablas(session);
		deleteTablas(session);
		readTablas(session);
		
		
		
		//MONGO
		//mongoClient.crearDocumentos();
		//mongoClient.updateDocumentos();
		mongoClient.deleteDocumentos();

	}

	

	

}
