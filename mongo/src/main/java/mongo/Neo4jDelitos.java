package mongo;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;


public class Neo4jDelitos {
	private final Driver driver;
	
	public Neo4jDelitos(String uri, String user, String password) {
		this.driver = GraphDatabase.driver(uri,AuthTokens.basic(user, password));
	}

	
	public static void crearDatos() {
		
	}
	
	
	public static void main(String[] args) {
		Neo4jDelitos mainNeo4j = new Neo4jDelitos("bolt://bolt+s://915516e3d64ed19cbbb5ef1161f04d17.neo4jsandbox.com:7687","neo4j","password");
		

	}

}
