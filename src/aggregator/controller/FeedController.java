package aggregator.controller;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import aggregator.Controller;
import aggregator.FeedScheduler;
import aggregator.StringUtils;
import aggregator.db.Model;
import aggregator.db.QueryBuilder;
import aggregator.db.SqlConnection;
import aggregator.model.FeedModel;
import aggregator.table.FeedTable;
import aggregator.table.ReadListTable;
import aggregator.table.UserFeedTable;
import aggregator.view.FeedView;

public class FeedController extends Controller
{
  @Override
  public void index() throws Exception
  {
	  System.out.println("index");
	require_authentified_user();

	FeedTable              feed_table = new FeedTable();
	HashMap<String,String> criterias  = new HashMap<String,String>();
	List<Model>            entries;

	criterias.put("user_feeds.user_id", user_id);
	entries = feed_table.where(criterias).join("user_feeds", "feed").entries();
	response.addHeader("Content-Type", "application/json");
	response.getWriter().write(FeedView.index(entries));
  }

  @SuppressWarnings("serial")
  @Override
  public void create() throws Exception
  {
	  System.out.println("create");
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
	  feed = FeedModel.create_from_url(feed_url);
	else
      feed = (FeedModel)items.get(0);
	feed.reloadFromSource();
	UserFeedTable.linkUserToFeed(user_id, Long.toString(feed.getId()));
	response.addHeader("Content-Type", "application/json");
    response.getWriter().write(FeedView.show(feed));
  }
  
  @SuppressWarnings("serial")
  @Override
  public void update(String feed_id) throws Exception
  {
	  System.out.println("update");
	require_authentified_user();
	require_parameters(new ArrayList<String>() {{ add("post[id]"); add("post[has_been_read]"); }});

	final String  post_id            = request.getParameter("post[id]");
	String        post_has_been_read = request.getParameter("post[has_been_read]");
	ReadListTable table              = new ReadListTable();

	if (post_has_been_read != "false")
	{
	  table.create(Long.parseLong(user_id),  Long.parseLong(feed_id),  Long.parseLong(post_id));
	  response.getWriter().write("Feed has been read");
	}
	else
	{
	  table.destroy(Long.parseLong(user_id), Long.parseLong(feed_id), Long.parseLong(post_id));
	  response.getWriter().write("Feed is not read anymore");
	}
  }

  @SuppressWarnings("serial")
  @Override
  public void destroy(String feed_id) throws Exception
  {
	require_authentified_user();

	final String           _feed_id   = feed_id;
	final String           _user_id   = user_id;
	ReadListTable          read_list  = new ReadListTable();
	UserFeedTable          user_feeds = new UserFeedTable();
	HashMap<String,String> criterias  = new HashMap<String,String>() {{
	  put("feed_id", _feed_id);
	  put("user_id", _user_id);
	}};
	ResultSet              users_remaining;

	feed_id = StringUtils.ecmaScriptStringEscape(feed_id);
	user_feeds.where(criterias).destroy();
	users_remaining = SqlConnection.getSingleton().statement.executeQuery("SELECT COUNT(user_id) AS feed_count FROM user_feeds WHERE feed_id='" + feed_id + "'");
	users_remaining.next();
	if (users_remaining.getInt("feed_count") == 0)
	{
	  FeedTable feed_table = new FeedTable();
	  FeedModel feed       = (FeedModel)feed_table.find(feed_id);

	  feed.destroy();
	}
	else
	  read_list.where(criterias).destroy();
  }

  @SuppressWarnings("serial")
  @Override
  public void get(String feed_id) throws Exception
  {
	  System.out.println("get");
    require_authentified_user();

    FeedTable   feed_table = new FeedTable();
    FeedModel   feed       = (FeedModel)feed_table.find(feed_id);
    List<Model> posts;
    int         page, limit;
	final int   highest_id;
    QueryBuilder query;

    highest_id = Integer.parseInt(getParameter("highest_id", "0"));
    page       = Integer.parseInt(getParameter("page",  "0"));
    limit      = Integer.parseInt(getParameter("limit", "50"));
    if (limit < 1 || limit > 250)
      limit = 50;
    query = feed.getPosts().order_by("created_at", "desc").limit(limit).skip(page * limit);
    if (highest_id > 0)
      query = query.where("id", "<=", highest_id);
    posts = query.entries();

	response.addHeader("Content-Type", "application/json");
    response.getWriter().write(FeedView.show(feed, posts, user_id));

    FeedScheduler.scheduleUpdate(feed);
  }
}
