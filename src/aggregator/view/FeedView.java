package aggregator.view;

import java.sql.SQLException;
import java.util.List;

import aggregator.JsonView;
import aggregator.db.Model;
import aggregator.model.FeedModel;
import aggregator.model.FeedPostModel;

public class FeedView
{
  public static String index(List<Model> entries) throws SQLException, Exception
  {
	JsonView view = new JsonView();

	view.array("feeds");
	for (int i = 0 ; i < entries.size() ; ++i)
	{
	  FeedModel item = (FeedModel)entries.get(i);

	  view.object();
	  render_feed(item, view);
	  view.end_object();
	}
	view.end_array();
	return (view.render());
  }
  
  public static String show(FeedModel feed) throws SQLException, Exception
  {
	JsonView view = new JsonView();
	
	render_feed(feed, view);
	return (view.render());
  }

  public static String show(FeedModel feed, List<Model> posts) throws SQLException, Exception
  {
	JsonView view = new JsonView();

	render_feed(feed, view);
	render_posts(posts, view);
	return (view.render());
  }

  private static void render_feed(FeedModel item, JsonView view) throws SQLException, Exception
  {
	view.property("id",          Long.toString(item.getId()));
    view.property("title",       item.title);
    view.property("url",         item.url);
    view.property("description", item.description);
    view.property("favicon",     item.favicon);
  }

  private static void render_posts(List<Model> posts, JsonView view) throws SQLException, Exception
  {
    view.array("posts");
    for (Model _post: posts)
    {
      FeedPostModel post = (FeedPostModel)_post;

      view.object();
      view.property("id",               Long.toString(post.getId()));
      view.property("title",            post.title);
      view.property("description",      post.description);
      view.property("category",         post.category);
      view.property("comments",         post.comments);
      view.property("link",             post.link);
      view.property("publication_date", post.publication_date);
      view.property("source",           post.source);
      view.end_object();
    }
    view.end_array();
  }
}
