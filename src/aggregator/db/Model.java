package aggregator.db;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class Model
{
  protected Table table;
  protected int   id = 0;

  protected Model(Table table, ResultSet row) throws SQLException
  {
	this.table = table;
	id         = row.getInt("id");
  }

  protected Model(Table table) throws SQLException
  {
	this.table = table;
  }

  public int getId()
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
}
