package aggregator.view;

import java.util.List;

import aggregator.JsonView;
import aggregator.db.Model;
import aggregator.model.FeedModel;

public class FeedView
{
  public static String index(List<Model> entries)
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

  public static String show(FeedModel feed)
  {
	JsonView view = new JsonView();

	render_feed(feed, view);
	return (view.render());
  }

  private static void render_feed(FeedModel item, JsonView view)
  {
    view.property("title",       item.title);
    view.property("url",         item.url);
    view.property("description", item.description);
  }
}
