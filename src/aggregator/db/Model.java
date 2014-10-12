package aggregator.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class Model
{
  protected Table table;
  protected long  id = 0;

  protected Model(Table table, ResultSet row) throws SQLException
  {
	this.table = table;
	id         = row.getInt("id");
  }

  protected Model(Table table) throws SQLException
  {
	this.table = table;
  }

  public long getId()
  {
    return (id);
  }

  public void destroy() throws Exception, SQLException
  {
	if (id != 0)
	{
	  String table_name = table.getTableName();
	  
	  SqlConnection.getSingleton().statement.execute("DELETE FROM " + table_name + " WHERE id=" + id);
	}
  }

  public boolean save() throws Exception, SQLException
  {
	if (id == 0)
      return (create());
    return (update());
  }

  public abstract boolean update() throws Exception, SQLException;
  public abstract boolean create() throws Exception, SQLException;
  
  protected boolean insertQuery(String query) throws Exception, SQLException
  {
    PreparedStatement statement     = SqlConnection.getSingleton().connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
    int               affectedRows  = statement.executeUpdate();
    ResultSet         generatedKeys = statement.getGeneratedKeys();

    if (generatedKeys.next())
      id = generatedKeys.getLong(1);
    return (affectedRows > 0);
  }
}
