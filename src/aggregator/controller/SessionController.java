package aggregator.controller;

import java.sql.*;
import java.util.ArrayList;

import aggregator.Controller;
import aggregator.StringUtils;
import aggregator.db.SqlConnection;

public class SessionController extends Controller
{
  public boolean use_password_auth = true;
  
  public void index() throws Exception
  {
    create();
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
	ResultSet results;
	String    email    = StringUtils.ecmaScriptStringEscape(request.getParameter("email"));
	String    password = StringUtils.ecmaScriptStringEscape(request.getParameter("password"));
	String    query    = "SELECT id FROM users WHERE email='" + email + "' AND password='" + password + "'";

	results = SqlConnection.getSingleton().statement.executeQuery(query);
	while (results.next())
	{
	  request.getSession().setAttribute("user_id", results.getInt("id"));
	  return ;
	}
	throw new SQLException();
  }

  private void oauth_authentication()
  {
  }
}
