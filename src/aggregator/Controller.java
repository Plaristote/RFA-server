package aggregator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Controller
{
  protected HttpServletRequest  request;
  protected HttpServletResponse response;
  
  public void setHttpResources(HttpServletRequest request, HttpServletResponse response)
  {
	this.request  = request;
	this.response = response;
  }

  public void index() throws Exception
  {
  }

  public void get(String id) throws Exception
  {
  }

  public void create() throws Exception
  {
  }

  public void update(String id) throws Exception
  {
	update();
  }
  
  public void update() throws Exception
  {
  }

  public void destroy(String id) throws Exception
  {
	destroy();
  }

  public void destroy() throws Exception
  {
  }

  protected void require_parameters(List<String> parameters) throws Exception
  {
	List<String> missing_parameters = new ArrayList<String>();

	for(String parameter: parameters)
	{
	  if (request.getParameter(parameter) == null)
		missing_parameters.add(parameter);
	}
	if (missing_parameters.size() > 0)
	{
      String error_message = "Missing parameters ";
      
      for (String parameter: missing_parameters)
      {
    	if (parameter != missing_parameters.get(0))
    	  error_message += ", ";
    	error_message += parameter;
      }
      throw new IOException(error_message); // TODO make an exception for missing parameters
	}
  }
  
  protected Map<String,String> getParameters(List<String> parameters_keys) throws Exception
  {
	Map<String,String> parameters = new HashMap<String,String>();

	for(String parameter: parameters_keys)
	{
	  if (request.getParameter(parameter) == null)
		parameters.put(parameter, request.getParameter(parameter));
	}
	if (parameters_keys.size() != parameters.size())
	{
      throw new Exception("missing parameters");
	}
	return (parameters);
  }
  
  protected String user_id;

  protected void require_authentified_user() throws Exception
  {
	Object session_attr = request.getSession().getAttribute("user_id");

	if (session_attr == null)
	{
	  response.setStatus(403);
	  throw new Exception("Authentication required");
	}
	else
	  user_id = session_attr.toString();
  }
}
