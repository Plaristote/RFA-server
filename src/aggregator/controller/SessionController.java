package aggregator.controller;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import aggregator.Controller;
import aggregator.db.Model;
import aggregator.model.UserModel;
import aggregator.table.UserTable;
import aggregator.view.UserView;

public class SessionController extends Controller
{
  public boolean use_password_auth = true;
  
  public void index() throws Exception
  {
	require_authentified_user();

	UserTable   table = new UserTable();
	UserModel   user  = (UserModel)table.find(user_id);

	response.addHeader("Content-Type", "application/json");
	response.getWriter().write(UserView.show(user));
  }

  public void get(String id) throws Exception
  {
    response.getWriter().write("Session ID: " + request.getSession().getAttribute("user_id").toString());
  }

  @Override
  public void create() throws Exception
  {
	try
	{
      if (use_password_auth)
    	password_authentication();
      else
    	oauth_authentication();
	}
	catch (SQLException e)
	{
	  response.setStatus(500);
	  response.getWriter().write("Wrong username or password");
	}
  }

  @Override
  public void destroy() throws Exception
  {
    request.getSession().removeAttribute("user_id");
  }

  @SuppressWarnings("serial")
  private void password_authentication() throws Exception
  {
	require_parameters(new ArrayList<String>() {{ add("email"); add("password"); }});

	UserTable   table = new UserTable();
	List<Model> users = table.where(new HashMap<String,String>() {{ 
	  put("email",    getParameter("email", ""));
	  put("password", getParameter("password", ""));
	}}).entries();

	if (users.size() > 0)
	{
	  UserModel user = (UserModel)users.get(0);
	  
	  request.getSession().setAttribute("user_id", user.getId());
	}
	else
	  throw new SQLException();
  }

  private void oauth_authentication()
  {
  }
}
