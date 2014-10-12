package aggregator.controller;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import aggregator.Controller;
import aggregator.StringUtils;
import aggregator.db.Model;
import aggregator.db.SqlConnection;
import aggregator.model.FeedModel;
import aggregator.table.FeedTable;
import aggregator.view.FeedView;

public class FeedController extends Controller
{
  @Override
  public void index() throws Exception
  {
	require_authentified_user();

	FeedTable              feed_table = new FeedTable();
	HashMap<String,String> criterias  = new HashMap<String,String>();
	List<Model>            entries;

	criterias.put("user_feeds.user_id", user_id);
	entries = feed_table.where(criterias).join("user_feeds", "feed").entries();
	response.getWriter().write(FeedView.index(entries));
  }

  @SuppressWarnings("serial")
  @Override
  public void create() throws Exception
  {
	require_authentified_user();
	require_parameters(new ArrayList<String>() {{ add("feed[url]"); }});

	String    feed_url   = request.getParameter("feed[url]");
	FeedTable feed_table = new FeedTable();
	FeedModel feed;
	HashMap<String,String> criterias = new HashMap<String,String>();
	List<Model>            items;

	criterias.put("url", feed_url);
	items = feed_table.where(criterias).entries();
	if (items.size() == 0)
	{
	  System.out.println("create");
	  feed = FeedModel.create_from_url(feed_url);
	}
	else
	{
      System.out.println("get");
      feed = (FeedModel)items.get(0);
	}
	System.out.println("Feed id = " + feed.getId());
	SqlConnection.getSingleton().statement.execute("INSERT INTO user_feeds VALUES(" + user_id + ", " + feed.getId() + ')');
	response.getWriter().write(FeedView.show(feed));
  }

  @Override
  public void destroy(String feed_id) throws Exception
  {
	ResultSet users_remaining;
	  
	require_authentified_user();
	feed_id = StringUtils.ecmaScriptStringEscape(feed_id);
	SqlConnection.getSingleton().statement.execute("DELETE FROM user_feeds WHERE user_id='" + user_id + "' feed_id='" + feed_id + "'");

	users_remaining = SqlConnection.getSingleton().statement.executeQuery("SELECT COUNT(user_id) AS feed_count WHERE feed_id='" + feed_id + "'");
	users_remaining.next();
	if (users_remaining.getInt("feed_count") == 0)
	  SqlConnection.getSingleton().statement.executeQuery("DELETE FROM feeds WHERE feed_id='" + feed_id + "'");
  }

  @Override
  public void get(String feed_id) throws Exception
  {
    require_authentified_user();

    FeedTable feed_table = new FeedTable();
    FeedModel feed       = (FeedModel)feed_table.find(feed_id);

    if (feed != null)
    {
      response.getWriter().write("Coucou tu veux voir mon sexe (" + feed_id + ") ?");
    }
  }
}
