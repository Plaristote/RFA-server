package aggregator.db;

import java.sql.*;

public class SqlConnection
{
  private static SqlConnection singleton = null;

  private String     driver        = "com.mysql.jdbc.Driver";
  private String     username      = "root";
  private String     password      = null;
  private String     hostname      = "localhost";
  private short      port          = 3306;
  private String     database_name = "rss_aggregator";
  private Connection connection;
  public  Statement  statement;

  protected SqlConnection() throws SQLException, ClassNotFoundException
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

  public static SqlConnection getSingleton() throws SQLException, ClassNotFoundException
  {
	if (singleton == null)
      singleton = new SqlConnection();
    return (singleton);
  }
}
