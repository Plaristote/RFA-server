package aggregator.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import aggregator.StringUtils;
import aggregator.db.Model;
import aggregator.db.SqlConnection;
import aggregator.db.Table;
import aggregator.table.FeedPostTable;

public class FeedPostModel extends Model
{
	public long   feed_id;
	public String title, description, link, category, comments, publication_date, source;
	public java.sql.Timestamp created_at;

	public FeedPostModel(FeedPostTable table, ResultSet row) throws SQLException
	{
	  super(table, row);
	  feed_id          = row.getLong("feed_id");
	  title            = row.getString("title");
	  description      = row.getString("description");
	  link             = row.getString("link");
	  category         = row.getString("category");
	  comments         = row.getString("comments");
	  source           = row.getString("source");
	  publication_date = row.getString("publication_date");
	  created_at       = row.getTimestamp("created_at");
	}

	public FeedPostModel(Table table) throws SQLException
	{
	  super(table);
	}

  @Override
  public boolean create() throws Exception, SQLException
  {
	String query = "INSERT INTO " + table.getTableName();

	created_at = new java.sql.Timestamp((new java.util.Date()).getTime());	
	query += " VALUES(0,";
	query += Long.toString(feed_id) + ',';
	query += StringUtils.sqlField(title)       + ',';
	query += StringUtils.sqlField(category)         + ',';
	query += StringUtils.sqlField(comments)        + ',';
	query += StringUtils.sqlField(link) + ',';
	query += StringUtils.sqlField(description)     + ',';
	query += StringUtils.sqlField(publication_date) + ',';
	query += StringUtils.sqlField(source) + ',';
	query += '\'' + created_at.toString() + '\'';
	query += ')';
	return (insertQuery(query));
  }

  public boolean update() throws SQLException, Exception
  {
	String query = "UPDATE " + table.getTableName();
	
	query += " SET source="        + StringUtils.sqlField(source);
	query += " AND title="       + StringUtils.sqlField(title);
	query += " AND link="        + StringUtils.sqlField(link);
	query += " AND description=" + StringUtils.sqlField(description);
	query += " AND publication_date="  + StringUtils.sqlField(publication_date);
	query += " AND category="        + StringUtils.sqlField(category);
	query += " AND comments="        + StringUtils.sqlField(comments);
	query += " AND feed_id=" + Long.toString(feed_id);
	query += " WHERE id=" + Long.toString(getId());
	return (SqlConnection.getSingleton().statement.execute(query));
  }
}
