import java.net.InetSocketAddress;
import java.util.List;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;



public class CassandraCrud {
	private static final String CONTACT_POINT = "127.0.0.1";
	private static final int PORT = 9042;
	private static final String KEYSPACE = "causasJudiciales";
	private static final String DATACENTER = "datacenter1";

	//Keyspace
	public static void crearKeyspace(CqlSession session) {
		session.execute("CREATE KEYSPACE IF NOT EXISTS " + KEYSPACE + " WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1}");
		session.execute("USE " + KEYSPACE);
	}
	
	//CONEXION
	private static CqlSession connect() {
        return CqlSession.builder()
                .addContactPoint(new InetSocketAddress(CONTACT_POINT, PORT))
                .withLocalDatacenter(DATACENTER)
                .build();

	}
	
	
	public static void crearTablas(CqlSession session) {
		session.execute("CREATE TABLE IF NOT EXISTS causas("
				+"id_expediente INT PRIMARY KEY,"
				+"caratula TEXT,"
				+"fecha_presentacion DATE,"
				+"id_juzgado_denuncia INT,"
				+"id_juzgado_actual INT,"
				+"id_juez_interviniente INT,"
				+"area_derecho TEXT,"
				+"id_causante INT,"
				+"id_abogados LIST<INT>,"
				+"escritos LIST<INT>"
				+")");
		
		session.execute("CREATE TABLE IF NOT EXISTS abogados("
				+"id_abogado INT PRIMARY KEY,"
				+"matricula INT,"
				+"nombre TEXT"
				+")");
		
		session.execute("CREATE TABLE IF NOT EXISTS causantes("
				+"id_causante INT PRIMARY KEY,"
				+"id_expediente INT,"
				+"documento TEXT,"
				+"nombre TEXT,"
				+"direccion TEXT"
				+")");
		
		session.execute("CREATE TABLE IF NOT EXISTS jueces("
				+"id_abogado INT PRIMARY KEY,"
				+"matricula INT,"
				+"nombre TEXT,"
				+"tipo TEXT,"
				+"id_juzgado INT"
				+")");
		
		session.execute("CREATE TABLE IF NOT EXISTS fiscales("
				+"id_abogado INT PRIMARY KEY,"
				+"matricula INT,"
				+"nombre TEXT,"
				+"tipo TEXT,"
				+"id_juzgado INT"
				+")");
		
		session.execute("CREATE TABLE IF NOT EXISTS juzgados("
				+"id_juzgado INT PRIMARY KEY,"
				+"nombre TEXT"
		+")");
		
		session.execute("CREATE TABLE IF NOT EXISTS escritos("
				+"id_escrito INT PRIMARY KEY,"
				+"id_expediente INT,"
				+"fecha_escrito DATE,"
				+"tipo_persona TEXT,"
				+"id_persona INT,"
				+"texto TEXT,"
				+"peritos LIST<INT>,"
				+"dictamenes TEXT"
				+")");
	}
	

	
	
	public static void insertarCausas(CqlSession session) {
		// Insertar datos de muestra en la tabla Causas
		session.execute("INSERT INTO causas (id_expediente, caratula, fecha_presentacion," +
		                "id_juzgado_denuncia, id_juzgado_actual, id_juez_interviniente, area_derecho, id_causante, " +
		                "id_abogados, escritos) VALUES " +
		                "(1, 'Robo Calificado', '2023-10-15',1, 2, 3, 'Derecho Penal', 1, [1,2], [1,2])");
		
		session.execute("INSERT INTO causas (id_expediente, caratula, fecha_presentacion," +
                "id_juzgado_denuncia, id_juzgado_actual, id_juez_interviniente, area_derecho, id_causante, " +
                "id_abogados, escritos) VALUES " +
                "(2, 'Robo Calificado', '2023-11-15',4, 4, 1, 'Derecho Penal', 4, [1,2], [4])");

		session.execute("INSERT INTO causas (id_expediente, caratula, fecha_presentacion," +
                "id_juzgado_denuncia, id_juzgado_actual, id_juez_interviniente, area_derecho, id_causante, " +
                "id_abogados, escritos) VALUES " +
                "(3, 'Hurto', '2023-12-15',1, 1, 1, 'Derecho Penal', 2, [4,5], [3])");

		session.execute("INSERT INTO causas (id_expediente, caratula, fecha_presentacion," +
                "id_juzgado_denuncia, id_juzgado_actual, id_juez_interviniente, area_derecho, id_causante, " +
                "id_abogados, escritos) VALUES " +
                "(4, 'Violencia Domestica', '2023-01-15',3, 5, 5, 'Derecho Familiar', 3, [2,5], [2])");

		session.execute("INSERT INTO causas (id_expediente, caratula, fecha_presentacion," +
                "id_juzgado_denuncia, id_juzgado_actual, id_juez_interviniente, area_derecho, id_causante, " +
                "id_abogados, escritos) VALUES " +
                "(5, 'Bandalismo', '2023-02-15',5, 5, 4, 'Derecho Penal', 5, [1,2], [1,5])");


	}
	
	public static void insertarAbogados(CqlSession session) {
		session.execute("INSERT INTO abogados(id_abogado, matricula, nombre) VALUES(1,01,'Carlos Perez')");
		session.execute("INSERT INTO abogados(id_abogado, matricula, nombre) VALUES(2,02,'Maria Juarez')");
		session.execute("INSERT INTO abogados(id_abogado, matricula, nombre) VALUES(3,12,'Agustin Palombo')");
		session.execute("INSERT INTO abogados(id_abogado, matricula, nombre) VALUES(4,05, 'Victoria Lopez')");
		session.execute("INSERT INTO abogados(id_abogado, matricula, nombre) VALUES(5,09,'Sol LaRosa')");
		
	}
	
	public static void insertarCausantes(CqlSession session) {
		session.execute("INSERT INTO causantes(id_causante, id_expediente, documento, nombre, direccion) VALUES (1, 1, '12', 'Juan Perez', 'Calle Falsa 123')");
		session.execute("INSERT INTO causantes(id_causante, id_expediente, documento, nombre, direccion) VALUES (2, 2, '34', 'Juan Gomez', 'Calle Falsa 345')");
		session.execute("INSERT INTO causantes(id_causante, id_expediente, documento, nombre, direccion) VALUES (3, 3, '56', 'Juan Diaz', 'Calle Falsa 678')");
		session.execute("INSERT INTO causantes(id_causante, id_expediente, documento, nombre, direccion) VALUES (4, 4, '78', 'Juan Lopez', 'Calle Falsa 012')");
		session.execute("INSERT INTO causantes(id_causante, id_expediente, documento, nombre, direccion) VALUES (5, 5, '91', 'Juan Perez', 'Calle Falsa 321')");
	} 
	
	public static void insertarJueces(CqlSession session) {
		session.execute("INSERT INTO jueces (id_abogado, matricula, nombre, tipo, id_juzgado)VALUES (1, 12, 'María Gonzalez', 'Federal', 1)");
		session.execute("INSERT INTO jueces (id_abogado, matricula, nombre, tipo, id_juzgado)VALUES (2, 34, 'María zalez', 'Casacion', 2)");
		session.execute("INSERT INTO jueces (id_abogado, matricula, nombre, tipo, id_juzgado)VALUES (3, 56, 'María Gon', 'Federal', 3)");
		session.execute("INSERT INTO jueces (id_abogado, matricula, nombre, tipo, id_juzgado)VALUES (4, 78, 'María onzalez', 'Casacion', 4)");
		session.execute("INSERT INTO jueces (id_abogado, matricula, nombre, tipo, id_juzgado)VALUES (5, 91, 'María Gonlez', 'Federal', 5)");

	}
	
	public static void insertarFiscales(CqlSession session) {
		session.execute("INSERT INTO fiscales(id_abogado, matricula, nombre, tipo,id_juzgado)VALUES(1,65,'Mario Martinez','Federal',1)");
		session.execute("INSERT INTO fiscales(id_abogado, matricula, nombre, tipo,id_juzgado)VALUES(2,67,'Mario Tinez','Federal',2)");
		session.execute("INSERT INTO fiscales(id_abogado, matricula, nombre, tipo,id_juzgado)VALUES(3,54,'Mario Mar','Casacion',3)");
		session.execute("INSERT INTO fiscales(id_abogado, matricula, nombre, tipo,id_juzgado)VALUES(4,52,'Mario Marti','Federal',4)");
		session.execute("INSERT INTO fiscales(id_abogado, matricula, nombre, tipo,id_juzgado)VALUES(5,43,'Mario Marnez','Casacion',5)");
	}
	
	public static void insertarJuzgados(CqlSession session) {
		session.execute("INSERT INTO juzgados(id_juzgado, nombre) VALUES(1, 'KKC')");
		session.execute("INSERT INTO juzgados(id_juzgado, nombre) VALUES(2, 'MVP')");
		session.execute("INSERT INTO juzgados(id_juzgado, nombre) VALUES(3, 'KFC')");
		session.execute("INSERT INTO juzgados(id_juzgado, nombre) VALUES(4, 'CKGO')");
		session.execute("INSERT INTO juzgados(id_juzgado, nombre) VALUES(5, 'KBC')");
	}
	
	public static void insertarEscritos(CqlSession session) {
		session.execute("INSERT INTO escritos(id_escrito, id_expediente,fecha_escrito,tipo_persona,id_persona, texto, peritos, dictamenes) VALUES(1, 1, '2024-02-27', 'abogado', 1, 'Texto del escrito', [1, 2], 'blabla')");
		session.execute("INSERT INTO escritos(id_escrito, id_expediente,fecha_escrito,tipo_persona,id_persona, texto, peritos, dictamenes) VALUES(2, 2, '2024-02-27', 'abogado', 2, 'Texto del escrito', [3, 5], 'blabla')");
		session.execute("INSERT INTO escritos(id_escrito, id_expediente,fecha_escrito,tipo_persona,id_persona, texto, peritos, dictamenes) VALUES(3, 3, '2024-02-27', 'abogado', 3, 'Texto del escrito', [1, 2], 'blabla')");
		session.execute("INSERT INTO escritos(id_escrito, id_expediente,fecha_escrito,tipo_persona,id_persona, texto, peritos, dictamenes) VALUES(4, 4, '2024-02-27', 'abogado', 4, 'Texto del escrito', [6, 2], 'blabla')");
		session.execute("INSERT INTO escritos(id_escrito, id_expediente, fecha_escrito, tipo_persona, id_persona, texto, peritos, dictamenes) VALUES(5, 5, '2024-02-27', 'abogado', 5, 'Texto del escrito', [4, 3], 'blabla')");
	}
	
	public static void buscarAcusado(CqlSession session, int acusado) {
		ResultSet result = session.execute("SELECT * FROM causas WHERE id_causante = ? ALLOW FILTERING", acusado);
		
		System.out.println("Causas asociadas al acusado con ID " + acusado + ":");
		for(Row row : result) {
			int expediente = row.getInt("id_expediente");
			String caratula = row.getString("caratula");
			List<Integer> escritos = row.getList("escritos", Integer.class);
			System.out.println("Id del expediente: " + expediente + ", Caratula del expediente: " + caratula);
			System.out.println("Id de los escritos del expediente: " + escritos);
		}
	}
	
	
	private static void buscarAbogado(CqlSession session, int abogado) {
		ResultSet result = session.execute("SELECT * FROM causas WHERE id_abogados CONTAINS ? ALLOW FILTERING", abogado);
	    System.out.println("Causas asociadas al ABOGADO con ID " + abogado + ":");
	    for(Row row : result) {
	        int expediente = row.getInt("id_expediente");
	        System.out.println("Id del expediente: " + expediente);
	    }
	}


	
	
	
	public static void main(String[] args) {
		CqlSession session = connect();
		crearKeyspace(session);
		crearTablas(session);
		//insertarCausas(session);
		//insertarAbogados(session);
		//insertarCausantes(session);
		//insertarJueces(session);
		//insertarFiscales(session);
		//insertarJuzgados(session);
		//insertarEscritos(session);
		
		int id_acusado = 1;
		buscarAcusado(session, id_acusado);
		
		int id_abogado = 1;
		buscarAbogado(session, id_abogado);
		
		
		
	}

	
}
