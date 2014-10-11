package aggregator.router;

import aggregator.Router;
import aggregator.controller.FeedController;

@SuppressWarnings("serial")
public class FeedRouter extends Router
{
  @Override
  public void initializeController()
  {
	controller = new FeedController();
  }
}
