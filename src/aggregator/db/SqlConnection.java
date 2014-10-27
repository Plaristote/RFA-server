package aggregator.db;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class SqlConnection
{
  private static HashMap<Long,SqlConnection> singletons = new HashMap<Long,SqlConnection>();

  private String     driver        = "com.mysql.jdbc.Driver";
  private String     username;
  private String     password;
  private String     hostname;
  private short      port;
  private String     database_name;
  public  Connection connection;
  public  Statement  statement;

  protected SqlConnection() throws SQLException, ClassNotFoundException
  {
    initialize();
    {
	  String url = "jdbc:mysql://" + hostname + ':' + port;

	  Class.forName(driver);
	  if (username != null)
	  {
        url += "?user=" + username;
	    if (password != null)
          url += "&password=" + password;
	  }
      connection = DriverManager.getConnection(url);
	  statement  = connection.createStatement();
	  statement.execute("USE " + database_name);
    }
  }

  public static synchronized SqlConnection getSingleton() throws SQLException, ClassNotFoundException
  {
	long          thread_id  = Thread.currentThread().getId();
	SqlConnection connection = singletons.get(thread_id);
	
	if (connection == null)
	{
	  connection = new SqlConnection();
	  singletons.put(thread_id,  connection);
	}
    return (connection);
  }
  
  public static synchronized void cleanUp()
  {
	long thread_id = Thread.currentThread().getId();
	
	if (singletons.get(thread_id) != null)
	  singletons.remove(thread_id);
  }
  
  private void initialize()
  {
    if (System.getenv().get("OPENSHIFT_REPO_DIR") != null)
      initialize_production();
    else
      initialize_development();
  }
  
  private void initialize_production()
  {
	Map<String, String> env = System.getenv();

    hostname      = env.get("OPENSHIFT_MYSQL_DB_HOST");
    port          = Short.parseShort(env.get("OPENSHIFT_MYSQL_DB_PORT"));
    username      = "admin9NX9fuB";
    password      = "BAurMPY-Rugz";
    database_name = "tomcat8";
  }

  private void initialize_development()
  {
	username      = "root";
	password      = null;
	hostname      = "localhost";
	port          = 3306;
	database_name = "rss_aggregator";
  }
}
