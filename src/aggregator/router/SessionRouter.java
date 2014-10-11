package aggregator.router;

import aggregator.Router;
import aggregator.controller.SessionController;

@SuppressWarnings("serial")
public class SessionRouter extends Router
{
  @Override
  public void initializeController()
  {
    controller = new SessionController();
  }
}
