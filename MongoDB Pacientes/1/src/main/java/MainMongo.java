import java.util.Arrays;
import java.util.Date;

import org.bson.Document;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

public class MainMongo {
	
	//Creo variables necesarias
	private static final String DATABASE = "clinicadb";
	private static final String PACIENTES = "patients";
	private static final String MEDICOS = "doctors";
	private static final String CONSULTAS = "consultation";
	
	//coneccion con la base de datos
	private MongoClient mongoClient;
	private MongoDatabase database;
	
	public MainMongo(String connectionString) {
		mongoClient = MongoClients.create(connectionString);
		database = mongoClient.getDatabase(DATABASE);
	}
	
	public void initialData() {
		//Pacientes
		Document paciente1 = new Document("id",1)
				.append("nombre", "John Doe")
				.append("edad",35)
				.append("seguro medico","OSDE BASICO");
	
	
		Document paciente2 = new Document("id",2)
				.append("nombre", "Maria Perez")
				.append("edad",24)
				.append("seguro medico","MEDICUS AZUL");
	
		
		Document paciente3 = new Document("id",3)
				.append("nombre", "Javier Miley")
				.append("edad",60)
				.append("seguro medico","SWISS MEDICAL");
		
		database.getCollection(PACIENTES).insertMany(Arrays.asList(paciente1,paciente2,paciente3));
		
		
		//CARGA INICIAL DOCTORES
		
		Document doctor1 = new Document("licencia",123)
				.append("nombre", "Juan Perez")
				.append("edad",53)
				.append("especialidad","cardiologia");
	
	
		Document doctor2 = new Document("licencia",456)
				.append("nombre", "Maria Juarez")
				.append("edad",29)
				.append("especialidad","pediatria");
	
		
		Document doctor3 = new Document("licencia",789)
				.append("nombre", "Cristina Fernandez")
				.append("edad",60)
				.append("especialidad","nutriologa");
		
		database.getCollection(MEDICOS).insertMany(Arrays.asList(doctor1,doctor2,doctor3));
		
		//CARGA INICIAL CONSULTAS
		
		Document consulta1 = new Document("paciente_id", 1)
                .append("doctor_id", 123)
                .append("date", new Date())
                .append("diagnosis", "Hipertension")
                .append("treatment", "Tensionil 20mg cada 12hs");

        Document consulta2 = new Document("paciente_id", 3)
                .append("doctor_id", 789)
                .append("date", new Date())
                .append("diagnosis", "Fiebre")
                .append("treatment", "Ibuprofeno 600mg cada 8hs");
        
        database.getCollection(CONSULTAS).insertMany(Arrays.asList(consulta1,consulta2));
        
        
        
        //FIND DOCUMENTO ENTERO
        
    	}
        
		public void consultarDocumento(String collectionName) {
	        MongoCollection<Document> collection = database.getCollection(collectionName);
	        FindIterable<Document> colecciones = collection.find();
	        for(Document coleccion:colecciones) {
	        	System.out.println(coleccion.toJson());
	        }
	        
	    }
	
	
		//FIND DOCUMENTO SEGUN FILTRO
		public void pacientesEdad(int edad) {
	        MongoCollection<Document> patientsCollection = database.getCollection(PACIENTES);
	        FindIterable<Document> pacientes = patientsCollection.find(Filters.eq("edad", edad));
	        for(Document paciente:pacientes) {
	        	System.out.println("Paciente encontrado:");
		        System.out.println(paciente.toJson());
	        }
	    }
		
		
		public void consultasID(int id) {
	        MongoCollection<Document> consultasCollection = database.getCollection(CONSULTAS);
	        FindIterable<Document> consultas = consultasCollection.find(Filters.eq("doctor_id", id));
	        for(Document consulta:consultas) {
	        	System.out.println("Consulta del doctor " + id + "encontrada:");
		        System.out.println(consulta.toJson());
	        }
	    }
		
		//UPTDATE DOCUMENTO
        
		public void actualizarPaciente(int id, int newAge) {
			MongoCollection<Document> pacientesCollection = database.getCollection(PACIENTES);
			UpdateResult result =  pacientesCollection.updateOne(Filters.eq("id", id), 
					new Document("$set", new Document("edad",newAge)));
			 System.out.println(result.getModifiedCount() + " documento actualizado.");
		}
		
		//ELIMINAR DOCUMENTO
		
		public void eliminarPacienteID(int id) {
			MongoCollection<Document> pacientesCollection = database.getCollection(PACIENTES);
			DeleteResult result = pacientesCollection.deleteOne(Filters.eq("id",2));
			System.out.println(result.getDeletedCount() + " documento eliminado.");
		}
		
		//INSERTAR NUEVO DOCUMENTO
		
		public void insertDocument(Document document, String collectionName) {
	        MongoCollection<Document> collection = database.getCollection(collectionName);
	        collection.insertOne(document);

	        System.out.println("Documento insertado correctamente en la colección " + collectionName);
	    }
		
        //-----------------------------------------------------------------------------------------------------
	  
        
        public static void main(String args[]) {
        	
            MainMongo mainMongo = new MainMongo("mongodb://localhost:27017");
            
            //CARGA INICIAL DE DATOS
           //mainMongo.initialData();
           //System.out.println("Carga inicial realizada");
            System.out.println("PACIENTES: ");
            mainMongo.consultarDocumento("patients");
            
            System.out.println("DOCTORES");
            mainMongo.consultarDocumento("doctors");
            
            System.out.println("CONSULTAS");
            mainMongo.consultarDocumento("consultation");
            
            System.out.println("PACIENTE EDAD = 60:");
            mainMongo.pacientesEdad(60);
            
            System.out.println("CONSULTA DEL DOCTOR = 789:");
            mainMongo.consultasID(789);
            
            System.out.println("ACTUALIZAR EDAD DEL ID 2: ");
            mainMongo.actualizarPaciente(2, 27);
            
            System.out.println("PACIENTES: ");
            mainMongo.consultarDocumento("patients");
            
            
            System.out.println("ELIMINAR PACIENTE DE ID = 2: ");
            mainMongo.eliminarPacienteID(2);
            
            System.out.println("PACIENTES: ");
            mainMongo.consultarDocumento("patients");
            
           
            
            //INGRESO UN NUEVO DOCUMENTO
            Document newDocument = new Document("id",2)
    				.append("nombre", "Juana Viale")
    				.append("edad",44)
    				.append("seguro medico","MEDICUS BLANCO");
            
            mainMongo.insertDocument(newDocument, "patients");
            
            
            System.out.println("PACIENTES: ");
            mainMongo.consultarDocumento("patients");
            
            
         // Cerrar la conexión 
            mainMongo.mongoClient.close();
	}
	
}