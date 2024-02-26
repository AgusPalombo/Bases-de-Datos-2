package mongo;
import java.net.InetSocketAddress;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ColumnDefinitions;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;

public class multasCassandra {
	
	//Conexion
	private static final String IP = "127.0.0.1";
	private static final int PORT = 9042;
	private static final String DATACENTER = "datacenter1";
	
	//Crear Keyspace
	
	private static void createKeyspace(CqlSession session) {
		session.execute("CREATE KEYSPACE IF NOT EXISTS multasBase WITH replication ={'class':'SimpleStrategy','replication_factor':1}");
		
		session.execute("USE multasBase");
	}
	
	//CONEXION
	
	private static CqlSession connect() {
		return CqlSession.builder()
				.addContactPoint(new InetSocketAddress(IP,PORT))
				.withLocalDatacenter(DATACENTER)
				.build();
	}
	
	
	//Crear Tablas
	
	private static void crearTabla(CqlSession session) {
		session.execute("CREATE TABLE IF NOT EXISTS multasBase.vehiculos("
				+ "id_vehiculos INT PRIMARY KEY,"
				+ "marca TEXT,"
				+"modelo TEXT,"
				+"anio INT,"
				+"propietario_id INT,"
				+"conductor_actual_id INT,)");
		
		//Tabla impuestos
		
		session.execute("CREATE TABLE IF NOT EXISTS multasBase.impuestos("
				+"id_impuesto INT PRIMARY KEY,"
				+"vehiculo_id INT,"
				+"fecha_vencimiento DATE,"
				+"fecha_pago DATE,"
				+"monto DECIMAL"
			+")");
		
		//Tablas Multas
		
		session.execute("CREATE TABLE IF NOT EXISTS multasBase.multas("
				+"id_multas INT PRIMARY KEY,"
				+"vehiculo_id INT,"
				+"propietario_id INT,"
				+"conductor_id INT,"
				+"tipo_multa TEXT,"
				+"fecha DATE,"
				+"descuenta_puntos BOOLEAN"
				+")");
		
		//Tablas DUEÑOS
		
		session.execute("CREATE TABLE IF NOT EXISTS multasBase.duenios("
				+"id_duenio INT PRIMARY KEY,"
				+"nombre TEXT,"
				+"apellido TEXT,"
				+"direccion TEXT,"
				+"telefono TEXT"
			+")");
		
		//Tablas CONDUCTOR
		
		session.execute("CREATE TABLE IF NOT EXISTS multasBase.conductor("
				+"id_conductor INT PRIMARY KEY,"
				+"nombre TEXT,"
				+"apellido TEXT,"
				+"direccion TEXT,"
				+"telefono TEXT"
			+")");
		
	}
	
	public static void insertarDatos(CqlSession session) {
		//Datos AUTOS
		session.execute("INSERT INTO vehiculos(id_vehiculos, marca, modelo, anio, propietario_id, conductor_actual_id)"+"VALUES(?,?,?,?,?,?);", 1,"toyota","corola",2018,1,1);
		session.execute("INSERT INTO vehiculos(id_vehiculos, marca, modelo, anio, propietario_id, conductor_actual_id)"+"VALUES(?,?,?,?,?,?);", 2,"chevrolete","corsa",2011,2,2);
		session.execute("INSERT INTO vehiculos(id_vehiculos, marca, modelo, anio, propietario_id, conductor_actual_id)"+"VALUES(?,?,?,?,?,?);", 3,"peugeot","206",2008,3,3);
		session.execute("INSERT INTO vehiculos(id_vehiculos, marca, modelo, anio, propietario_id, conductor_actual_id)"+"VALUES(?,?,?,?,?,?);", 4,"fiat","palio",2021,4,4);
		
		//Datos IMPUESTOS Auto 1
		LocalDate fechaVto1 = LocalDate.of(2024, 2, 28);
		LocalDate fechaPago1 = LocalDate.of(2024, 1, 5);
		session.execute("INSERT INTO impuestos(id_impuesto, vehiculo_id, fecha_vencimiento, fecha_pago, monto)"+"VALUES(?,?,?,?,?);", 1,1,fechaVto1,fechaPago1,20210.78);
		
		LocalDate fechaVto2 = LocalDate.of(2024, 3, 28);
		LocalDate fechaPago2 = LocalDate.of(2024, 2, 5);
		session.execute("INSERT INTO impuestos(id_impuesto, vehiculo_id, fecha_vencimiento, fecha_pago, monto)"+"VALUES(?,?,?,?,?);", 2,2,fechaVto2,fechaPago2,7800.00);
		
		LocalDate fechaVto3 = LocalDate.of(2024, 4, 28);
		LocalDate fechaPago3 = LocalDate.of(2024, 3, 5);
		session.execute("INSERT INTO impuestos(id_impuesto, vehiculo_id, fecha_vencimiento, fecha_pago, monto)"+"VALUES(?,?,?,?,?);", 3,3,fechaVto3,fechaPago3,3000.00);
		
		LocalDate fechaVto4 = LocalDate.of(2024, 5, 28);
		LocalDate fechaPago4 = LocalDate.of(2024, 4, 5);
		session.execute("INSERT INTO impuestos(id_impuesto, vehiculo_id, fecha_vencimiento, fecha_pago, monto)"+"VALUES(?,?,?,?,?);", 4,4,fechaVto4,fechaPago4,50000.00);
		
		
		//Datos MULTAS
		LocalDate fecha1 = LocalDate.of(2023, 5, 20);
		session.execute("INSERT INTO multas(id_multas, vehiculo_id, propietario_id, conductor_id, tipo_multa, fecha, descuenta_puntos)"+"VALUES(?,?,?,?,?,?,?);",1,1,1,2,"Exceso de velocidad",fecha1,true);
	
		LocalDate fecha2 = LocalDate.of(2022, 8, 30);
		session.execute("INSERT INTO multas(id_multas, vehiculo_id, propietario_id, conductor_id, tipo_multa, fecha, descuenta_puntos)"+"VALUES(?,?,?,?,?,?,?);",2,2,2,2,"Mal estacionado",fecha2,false);
		
		LocalDate fecha3 = LocalDate.of(2023, 11, 16);
		session.execute("INSERT INTO multas(id_multas, vehiculo_id, propietario_id, conductor_id, tipo_multa, fecha, descuenta_puntos)"+"VALUES(?,?,?,?,?,?,?);",3,2,3,3,"Semaforo en rojo",fecha3,true);
		
		LocalDate fecha4 = LocalDate.of(2022, 11, 26);
		session.execute("INSERT INTO multas(id_multas, vehiculo_id, propietario_id, conductor_id, tipo_multa, fecha, descuenta_puntos)"+"VALUES(?,?,?,?,?,?,?);",4,4,4,3,"Exceso de velocidad",fecha4,true);
		
		//Datos DUEÑOS
		session.execute("INSERT INTO duenios(id_duenio, nombre, apellido, direccion, telefono)"+"VALUES(?,?,?,?,?);", 1,"Marvio","Gomez","Gaona 2100","1158211303");
		session.execute("INSERT INTO duenios(id_duenio, nombre, apellido, direccion, telefono)"+"VALUES(?,?,?,?,?);", 2,"Jose","Lopez","Gaona 100","1158211304");
		session.execute("INSERT INTO duenios(id_duenio, nombre, apellido, direccion, telefono)"+"VALUES(?,?,?,?,?);", 3,"Mario","Perez","Gaona 1000","1158211305");
		session.execute("INSERT INTO duenios(id_duenio, nombre, apellido, direccion, telefono)"+"VALUES(?,?,?,?,?);", 4,"Pedro","Diaz","Gaona 200","1158211307");
		
		
		//Datos CONDUCTORES
		session.execute("INSERT INTO conductor(id_conductor, nombre, apellido, direccion, telefono)"+"VALUES(?,?,?,?,?);", 1,"Gonzalo","Acosta","Gaona 5200","1151211307");
		session.execute("INSERT INTO conductor(id_conductor, nombre, apellido, direccion, telefono)"+"VALUES(?,?,?,?,?);", 2,"Jose","Lopez","Gaona 4200","1152211307");
		session.execute("INSERT INTO conductor(id_conductor, nombre, apellido, direccion, telefono)"+"VALUES(?,?,?,?,?);", 3,"Mario","Perez","Gaona 1200","1153211307");
		session.execute("INSERT INTO conductor(id_conductor, nombre, apellido, direccion, telefono)"+"VALUES(?,?,?,?,?);", 4,"Jose","Papa","Gaona 2010","1154211307");
		
	}
	
	
	public static void mostrarTablas(CqlSession session, String tabla) {
		String query = String.format("SELECT * FROM %s", tabla);
        ResultSet result = session.execute(query);
        System.out.println("Consulta general a la tabla " + tabla + ":");
        for (Row r : result) {
            System.out.println("Fila:");
            // Obtener el número de columnas en la fila
            int numColumnas = r.size();
            // Iterar sobre cada columna y mostrar el nombre y el valor
            for (int i = 0; i < numColumnas; i++) {
                // Obtener el nombre de la columna
                String nombreColumna = result.getColumnDefinitions().get(i).getName().asInternal();
                // Obtener el valor de la columna en la posición i
                Object valorColumna = r.getObject(i);
                // Imprimir el nombre y el valor de la columna
                System.out.println("\t" + nombreColumna + ": " + valorColumna);
            }
            System.out.println(); // Agregar una línea en blanco entre las filas
        }
		        System.out.println();		    
	}
	
	public static void mostrarMultasNoPropietario(CqlSession session) {
		ResultSet resultSet = session.execute("SELECT * FROM MULTAS;");
		for (Row row : resultSet) {
			int propietarioId = row.getInt("propietario_id");
			int conductorId = row.getInt("conductor_id");
			
			 if (conductorId != propietarioId) {
				 System.out.println("Fila con IDs distintos:");
				 ColumnDefinitions columnDefinitions = row.getColumnDefinitions();
			        for (int i = 0; i < columnDefinitions.size(); i++) {
			            String nombreColumna = columnDefinitions.get(i).getName().asInternal();
			            Object valorColumna = row.getObject(i);
			            System.out.println(nombreColumna + ": " + valorColumna);
			       }
			        System.out.println();
			    }
			 }
		}
		
	public static void rangoFechasMultas(CqlSession session, LocalDate fechaIni,LocalDate fechaFin) {
		 String fechaInicioStr = fechaIni.format(DateTimeFormatter.ISO_LOCAL_DATE);
         String fechaFinStr = fechaFin.format(DateTimeFormatter.ISO_LOCAL_DATE);
         
         String query = "SELECT tipo_multa, propietario_id " +
                 "FROM multas " +
                 "WHERE fecha >= '" + fechaInicioStr + "' AND fecha <= '" + fechaFinStr + "' " +
                 "ALLOW FILTERING;";
         
         ResultSet result = session.execute(query);
         
         for(Row row : result) {
        	 System.out.println("Tipo de multa: " + row.getString("tipo_multa"));
             System.out.println("ID del propietario: " + row.getInt("propietario_id"));
             System.out.println();
         }
         
	}
	
	public static void multasImpagas(CqlSession session, LocalDate fechaPago) {
		String fechaPagoStr = fechaPago.format(DateTimeFormatter.ISO_LOCAL_DATE);
		
		String query = "SELECT * FROM multas WHERE fecha >='"+ fechaPagoStr +"' "+"ALLOW FILTERING;";
		
		ResultSet result = session.execute(query);
		for(Row row : result) {
			 System.out.println("Tipo de multa: " + row.getString("tipo_multa"));
             System.out.println("ID del vehiculo: " + row.getInt("vehiculo_id"));
             System.out.println();
		}
		
	}
	
	public static void multasDescuentan(CqlSession session) {
		String query = "SELECT * FROM multas WHERE descuenta_puntos = TRUE ALLOW FILTERING";
		
		ResultSet result = session.execute(query);
		for (Row r : result) {
			System.out.println("Tipo de multa: " + r.getString("tipo_multa"));
			System.out.println("Descuenta: " + r.getBoolean("descuenta_puntos"));
		}
			
	}
		
	
	
	public static void main(String[] args) {
		CqlSession session = connect();
		createKeyspace(session);
		crearTabla(session);
		//insertarDatos(session);
		mostrarTablas(session, "vehiculos");
		mostrarMultasNoPropietario(session);
		
		LocalDate fechaIni = LocalDate.of(2023, 5, 20);
		LocalDate fechaFin = LocalDate.of(2023, 11, 18);
		
		rangoFechasMultas(session, fechaIni,fechaFin);
		
		
		LocalDate fechaPago = LocalDate.of(2024, 1, 2);
		multasImpagas(session, fechaPago);
		
		multasDescuentan(session);
	}

}
