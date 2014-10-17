package aggregator.view;

import aggregator.JsonView;
import aggregator.model.UserModel;

public class UserView {
  public static String show(UserModel user)
  {
    JsonView view = new JsonView();

    view.property("email", user.email);
    return (view.render());
  }
}
