package aggregator.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import aggregator.StringUtils;

public class QueryBuilder
{
  private Table                   table;
  private String                  limit;
  private String                  offset;
  private String                  order_by;
  private String                  order_sort;
  private String                  join_with, join_foreign_key;
  private List<String>            criterias   = new ArrayList<String>();
  private List<String>            only_fields = new ArrayList<String>();

  public QueryBuilder(Table table)
  {
	this.table = table;
  }

  public QueryBuilder where(Map<String, String> criterias)
  {
	for (Map.Entry<String,String> entry: criterias.entrySet())
	{
	  this.criterias.add(entry.getKey() + "=\"" + StringUtils.ecmaScriptStringEscape(entry.getValue()) + '"');
	}
	return (this);
  }
  
  public QueryBuilder where(String key, String operator, int value)
  {
	this.criterias.add(key + operator + Integer.toString(value));
	return (this);
  }

  public QueryBuilder limit(int value)
  {
	limit = Integer.toString(value);
	return (this);
  }

  public QueryBuilder skip(int value)
  {
	offset = Integer.toString(value);
	return (this);
  }

  public QueryBuilder only_ask_for(List<String> fields)
  {
	only_fields.addAll(fields);
	return (this);
  }
  
  public QueryBuilder order_by(String order_by, String order_sort)
  {
    this.order_by   = order_by;
    this.order_sort = order_sort;
    return (this);
  }
  
  public QueryBuilder join(String table_name, String foreign_key)
  {
	join_with        = table_name;
	join_foreign_key = foreign_key;
	return (this);
  }
  
  public List<Model> entries() throws ClassNotFoundException, SQLException
  {
	List<Model> _entries = new ArrayList<Model>();
	ResultSet   results  = SqlConnection.getSingleton().statement.executeQuery(toString());

	while (results.next())
	  _entries.add(table.createModel(results));
	return (_entries);
  }
  
  public void destroy() throws ClassNotFoundException, SQLException
  {
	String query = "DELETE FROM " + getTableReference() + getCriterias();
	
	SqlConnection.getSingleton().statement.execute(query);
  }

  public String toString()
  {
	String query = "SELECT " + getFieldSelector() + " FROM " + getTableReference();

	query += getCriterias();
	query += getOrdering();
	query += getLimits();
	return (query);
  }
  
  private String getTableReference()
  {
	String query = table.getTableName();
	
	if (join_with != null)
	{
	  query += " INNER JOIN " + join_with + " ON ";
	  query += join_with + '.' + join_foreign_key + "_id = " + table.getTableName() + ".id";
	}
	return (query);
  }
  
  private String getOrdering()
  {
	String query = "";
	
	if (order_by != null)
	{
	  query += " ORDER BY " + order_by;
	  if (order_sort != null)
		query += ' ' + order_sort;
	}
	return (query);
  }
  
  private String getLimits()
  {
	String query = "";
	
	if (limit != null)
	{
	  query += " LIMIT " + limit;
	  if (offset != null)
		query += " OFFSET " + offset;
	}
	return (query);
  }

  private String getFieldSelector()
  {
	if (only_fields.size() > 0)
	{
	  String result = "";

	  for (String field_name: only_fields)
	  {
		if (result != "")
		  result += ", ";
		result += field_name;
	  }
	  return (result);
	}
	return ("*");
  }
  
  private String getCriterias()
  {
    String query = "";

	if (criterias.size() > 0)
	{
      boolean prepend_and = false;
		
	  query += " WHERE ";
	  for (String entry: criterias)
	  {
		if (prepend_and)
		  query += " AND ";
		query   += entry;
		prepend_and = true;
	  }
	}
    return (query);
  }
}

class Pair<A,B> {
	public A first;
	public B second;
	
	public Pair(A first, B second) {
      this.first  = first;
      this.second = second;
	}
}
