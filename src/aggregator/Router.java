package aggregator;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import aggregator.db.SqlConnection;

public abstract class Router extends HttpServlet
{
  private static final long serialVersionUID = 1L;
  protected Controller controller;
  
  public abstract void initializeController();

  public void loadController(HttpServletRequest request, HttpServletResponse response)
  {
	initializeController();
	controller.setHttpResources(request, response);
  }
  
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
	loadController(request, response);
	String feed_id = getIdFromUri(request.getRequestURI());
	try
	{
	  if (feed_id == null)
	    controller.index();
	  else
	    controller.get(feed_id);
	}
	catch (Exception e)
	{
      handleException(e);
	}
	cleanUp();
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
	loadController(request, response);
	String feed_id = getIdFromUri(request.getRequestURI());
	try
	{
	  if (feed_id == null)
        controller.create();
	  else
		controller.update(feed_id);
	}
	catch (Exception e)
	{
      handleException(e);
	}
	cleanUp();
  }
    
  @Override
  protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
	loadController(request, response);
	String feed_id = getIdFromUri(request.getRequestURI());
	try
	{
      if (feed_id != null)
        controller.update(feed_id);
      else
        controller.update();
	}
	catch (Exception e)
	{
      handleException(e);
	}
	cleanUp();
  }

  @Override
  protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
	loadController(request, response);
	String feed_id = getIdFromUri(request.getRequestURI());
	try
	{
      if (feed_id != null)
        controller.destroy(feed_id);
      else
        controller.destroy();
	}
	catch (Exception e)
	{
      handleException(e);
	}
	cleanUp();
  }

  private void handleException(Exception exception) throws IOException
  {
    StringWriter sw = new StringWriter();
    PrintWriter  pw = new PrintWriter(sw, true);

    exception.printStackTrace(pw);
    if (controller.response.getStatus() == 200)
      controller.response.setStatus(500);
    controller.response.getWriter().write("Exception Received: " + exception.toString() + "\n" + sw.getBuffer().toString());
  }

  public String getIdFromUri(String uri)
  {
	java.util.regex.Pattern pattern = Pattern.compile(getContextPath() + "/[^/]+/([^/]+)$");
	java.util.regex.Matcher matcher = pattern.matcher(uri);

	if (matcher.find())
	  return (uri.substring(matcher.start(1), matcher.end(1)));
	return (null);
  }
  
  public String getContextPath()
  {
	try {
	  return controller.request.getContextPath();
	} catch (NullPointerException e) {
	}
	return "";
  }
  
  private void cleanUp()
  {
	SqlConnection.cleanUp();
  }
}
