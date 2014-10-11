package aggregator.model;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import aggregator.StringUtils;
import aggregator.db.Model;
import aggregator.db.SqlConnection;
import aggregator.table.FeedTable;

public class FeedModel extends Model
{
  public String title;
  public String url;
  public String description;
  public Date   updated_at;

  public FeedModel(FeedTable table, ResultSet row) throws SQLException
  {
	super(table, row);
	title       = row.getString("title");
	url         = row.getString("url");
	description = row.getString("description");
	updated_at  = row.getDate("updated_at");
  }

  public FeedModel(FeedTable table) throws SQLException
  {
	super(table);
  }

  public static FeedModel create(Map<String,String> parameters) throws SQLException, Exception
  {
	FeedModel item       = new FeedModel(new FeedTable());

	item.title       = parameters.get("title");
	item.url         = parameters.get("url");
	item.description = parameters.get("description");
	item.save();
	return (item);
  }

  public static FeedModel create_from_url(String url) throws SQLException, Exception
  {
	HashMap<String,String> parameters = new HashMap<String,String>();
	aggregator.rss.Feed    rss_feed   = aggregator.rss.Feed.download(url);

	parameters.put("title",       rss_feed.title);
	parameters.put("url",         rss_feed.url);
	parameters.put("description", rss_feed.description);
	return (create(parameters));
  }

  @Override
  public boolean save() throws SQLException, Exception
  {
	updated_at = new Date((new java.util.Date()).getTime());
	return (super.save());
  }

  public boolean create() throws SQLException, Exception
  {
	String query = "INSERT INTO " + table.getTableName();

	query += " VALUES(";
	query += '\'' + StringUtils.ecmaScriptStringEscape(title)       + "',";
	query += '\'' + StringUtils.ecmaScriptStringEscape(url)         + "',";
	query += '\'' + StringUtils.ecmaScriptStringEscape(description) + "',";
	query += "Date(" + updated_at.toString() + ')';
	query += ')';
	return (SqlConnection.getSingleton().statement.execute(query));
  }

  public boolean update() throws SQLException, Exception
  {
	String query = "UPDATE " + table.getTableName();
	
	query += " SET title='"       + StringUtils.ecmaScriptStringEscape(title)       + '\'';
	query += " AND url='"         + StringUtils.ecmaScriptStringEscape(url)         + '\'';
	query += " AND description='" + StringUtils.ecmaScriptStringEscape(description) + '\'';
	query += " AND updated_at='"  + updated_at.toString()                           + '\'';
	query += " WHERE id=" + id;
	return (SqlConnection.getSingleton().statement.execute(query));
  }
}
