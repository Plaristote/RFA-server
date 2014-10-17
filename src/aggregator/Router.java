package aggregator;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
	String feed_id = request.getParameter("id");

	loadController(request, response);
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
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
	String feed_id = request.getParameter("id");

	loadController(request, response);
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
  }
    
  @Override
  protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    String feed_id = request.getParameter("id");

	loadController(request, response);
	try
	{
      if (feed_id == null)
        controller.update(feed_id);
      else
        controller.update();
	}
	catch (Exception e)
	{
      handleException(e);
	}

  }

  @Override
  protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    String feed_id = request.getParameter("id");

	loadController(request, response);
	try
	{
      if (feed_id == null)
        controller.destroy(feed_id);
      else
        controller.destroy();
	}
	catch (Exception e)
	{
      handleException(e);
	}
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
}
