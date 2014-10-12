package aggregator.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public abstract class Table
{
  public    abstract String getTableName();
  protected abstract Model  createModel(ResultSet row) throws SQLException;

  public Model find(int id) throws Exception, SQLException
  {
	String    query   = "SELECT * FROM " + getTableName() + " WHERE id='" + id + '\'';
	ResultSet results = SqlConnection.getSingleton().statement.executeQuery(query);
	
	if (!(results.next()))
	  throw new Exception("No item with id " + id);
	return (createModel(results));
  }
  
  public Model find(String id) throws Exception, SQLException
  {
	return (find(Integer.parseInt(id)));
  }

  public QueryBuilder where(Map<String,String> criterias)
  {
	QueryBuilder query_builder = new QueryBuilder(this);
	
	return (query_builder.where(criterias));
  }
  
  public QueryBuilder all()
  {
	return (new QueryBuilder(this));
  }
}
