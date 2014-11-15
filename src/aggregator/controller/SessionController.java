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

  @SuppressWarnings("serial")
  @Override
  public void create() throws Exception
  {
	require_parameters(new ArrayList<String>() {{ add("email"); add("password"); }});
	if (request.getParameter("email") != "" && request.getParameter("password") != "")
	{
	  UserTable   users   = new UserTable();
	  List<Model> results = users.where(new HashMap<String,String>() {{ put("email", request.getParameter("email")); }}).entries();

	  if (results.size() == 0)
	  {
	    UserModel user  = new UserModel(users);
	
	    user.email    = request.getParameter("email");
	    user.password = request.getParameter("password");
	    user.save();
	    request.getSession().setAttribute("user_id", user.getId());
	  }
	  else
	    response.setStatus(422);
	}
	else
      response.setStatus(422);
  }

  @Override
  public void update() throws Exception
  {
	try
	{
      password_authentication();
	}
	catch (SQLException e)
	{
	  response.setStatus(401);
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
	final String email    = getParameter("email", "");
	final String password = getParameter("password", "");

	if (email != "" && password != "")
	{
	  UserTable   table = new UserTable();
	  List<Model> users = table.where(new HashMap<String,String>() {{ 
	    put("email",    email);
	    put("password", password);
	  }}).entries();

	  if (users.size() > 0)
	  {
	    UserModel user = (UserModel)users.get(0);
	  
	    request.getSession().setAttribute("user_id", user.getId());
	  }
	  else
	    throw new SQLException();
	}
	else
	  throw new Exception();
  }
}
