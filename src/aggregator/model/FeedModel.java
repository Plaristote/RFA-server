package aggregator.model;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aggregator.StringUtils;
import aggregator.db.Model;
import aggregator.db.SqlConnection;
import aggregator.table.FeedPostTable;
import aggregator.table.FeedTable;

public class FeedModel extends Model
{
  public String title;
  public String url;
  public String link;
  public String description;
  public String favicon;
  public Date   updated_at;
  private aggregator.rss.Feed rss_feed;

  public FeedModel(FeedTable table, ResultSet row) throws SQLException
  {
	super(table, row);
	title       = row.getString("title");
	url         = row.getString("url");
	description = row.getString("description");
	favicon     = row.getString("favicon");
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
	item.link        = parameters.get("link");
	item.description = parameters.get("description");
	item.favicon     = parameters.get("favicon");
	item.save();
	return (item);
  }

  public static FeedModel create_from_url(String url) throws SQLException, Exception
  {
	HashMap<String,String> parameters = new HashMap<String,String>();
	aggregator.rss.Feed    rss_feed   = aggregator.rss.Feed.download(url);
	FeedModel              model;

	parameters.put("title",       rss_feed.title);
	parameters.put("url",         rss_feed.url);
	parameters.put("link",        rss_feed.link);
	parameters.put("description", rss_feed.description);
	parameters.put("favicon",     rss_feed.favicon);
	model = create(parameters);
	model.rss_feed = rss_feed;
	return (model);
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

	query += " VALUES(0,";
	query += '\'' + StringUtils.ecmaScriptStringEscape(title)       + "',";
	query += '\'' + StringUtils.ecmaScriptStringEscape(url)         + "',";
	query += '\'' + StringUtils.ecmaScriptStringEscape(link)        + "',";
	query += '\'' + StringUtils.ecmaScriptStringEscape(description) + "',";
	query += '\'' + StringUtils.ecmaScriptStringEscape(favicon)     + "',";
	query += '\'' + updated_at.toString() + "'";
	query += ')';
	return (insertQuery(query));
  }

  public boolean update() throws SQLException, Exception
  {
	String query = "UPDATE " + table.getTableName();
	
	query += " SET title='"       + StringUtils.ecmaScriptStringEscape(title)       + '\'';
	query += " AND url='"         + StringUtils.ecmaScriptStringEscape(url)         + '\'';
	query += " AND link='"        + StringUtils.ecmaScriptStringEscape(link)        + '\'';
	query += " AND description='" + StringUtils.ecmaScriptStringEscape(description) + '\'';
	query += " AND favicon='"     + StringUtils.ecmaScriptStringEscape(favicon)     + '\'';
	query += " AND updated_at='"  + updated_at.toString()                           + '\'';
	query += " WHERE id=" + id;
	return (SqlConnection.getSingleton().statement.execute(query));
  }
  
  public void reloadFromSource() throws Exception
  {
	FeedPostTable       table      = new FeedPostTable();

	if (rss_feed == null)
      rss_feed  = aggregator.rss.Feed.download(url);
	title       = rss_feed.title;
	link        = rss_feed.link;
	description = rss_feed.description;
	favicon     = rss_feed.favicon;
	for (aggregator.rss.FeedItem item: rss_feed.items)
	{
	  FeedPostModel feed_post = getPostFromLink(item.link);

	  if (feed_post == null)
	  {
		feed_post                = new FeedPostModel(table);
		feed_post.feed_id        = getId();
		feed_post.link           = item.link;
	  }
	  feed_post.title            = item.title;
	  feed_post.category         = item.category;
	  feed_post.comments         = item.comments;
	  feed_post.description      = item.description;
	  feed_post.publication_date = item.pubDate;
	  feed_post.source           = item.source;
	  feed_post.save();
	}
  }
  
  public List<Model>   getPosts() throws Exception, SQLException
  {
    FeedPostTable          table     = new FeedPostTable();
    HashMap<String,String> criterias = new HashMap<String,String>();

    criterias.put("feed_id", Long.toString(getId()));
    return (table.where(criterias).entries());
  }

  public FeedPostModel getPostFromLink(String link) throws Exception, SQLException
  {
    FeedPostTable          table     = new FeedPostTable();
    HashMap<String,String> criterias = new HashMap<String,String>();
    List<Model>            items;

    criterias.put("feed_id", Long.toString(id));
    criterias.put("link",    link);
    items = table.where(criterias).entries();
    if (items.size() > 0)
      return ((FeedPostModel)items.get(0));
    return (null);
  }
}


