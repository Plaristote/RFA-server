package aggregator.oauth;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import aggregator.db.Model;
import aggregator.model.UserModel;
import aggregator.table.UserTable;

@SuppressWarnings("serial")
public class Google extends HttpServlet {
	public final static String client_id          = "774589530542-4apb37j5ntt0hm06do5cudc9jksbg6gs.apps.googleusercontent.com";
	public final static String email_address      = "774589530542-4apb37j5ntt0hm06do5cudc9jksbg6gs@developer.gserviceaccount.com";
	public final static String client_secret      = "mxFD4u0XKoa8QPurtFqMEd7H";
    public final static String redirect_uri       = "http://tomcat8-wokesmeed.rhcloud.com/oauth/2/google";
    public final static String javascript_oirings = "http://tomcat8-wokesmeed.rhcloud.com";
    
    private HttpServletResponse response;
    private HttpServletRequest  request;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
      this.response = response;
      this.request  = request;
      {
    	AccessToken        token = new AccessToken(request.getParameter("code"));
    	final GooglePeople people = new GooglePeople(token.getAccessToken());

    	people.query();

		try {
    	  UserTable   table = new UserTable();
    	  List<Model> users;
    	  UserModel   user;

		  users = table.where(new HashMap<String,String>() {{
		    put("email", people.email);
		  }}).entries();
		  if (users.size() > 0)
			user = (UserModel)users.get(0);
		  else
			user = table.create(people.email);
    	  request.getSession().setAttribute("user_id", user.getId());
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      }
      redirectToHome();
    }

    private void redirectToHome()
    {
      response.setStatus(302);
      response.setHeader("location", request.getContextPath());
    }
}

class AccessToken {
	private URL    url;
	private String code;
	private String access_token, id;
	private long   expires_in, delivered_at;

	public AccessToken(String code)
	{
		this.code = code;
		try {
			url = new URL("https://accounts.google.com/o/oauth2/token");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized String getAccessToken() throws IOException
	{
	  if (access_token == null || hasTokenExpired())
		fetchAccessToken();
	  return (access_token);
	}

	private boolean hasTokenExpired()
	{
	  return (delivered_at + expires_in <= getTimestamp());
	}
	
	private void fetchAccessToken() throws IOException
	{
	  HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();

	  connection.setRequestMethod("POST");
	  connection.setDoOutput(true);
	  connection.setDoInput(true);

	  DataOutputStream output = new DataOutputStream(connection.getOutputStream());
	  String           params;

	  params  = "code="          + URLEncoder.encode(code, "UTF-8")                 + '&';
	  params += "client_id="     + URLEncoder.encode(Google.client_id, "UTF-8")     + '&';
	  params += "client_secret=" + URLEncoder.encode(Google.client_secret, "UTF-8") + '&';
	  params += "redirect_uri="  + URLEncoder.encode(Google.redirect_uri, "UTF-8")  + '&';
	  params += "grant_type=authorization_code";
	  output.writeBytes(params);

	  DataInputStream input = new DataInputStream(connection.getInputStream());
	  String json = new String();

	  for (int c = input.read() ; c != -1 ; c = input.read())
		json += Character.toString((char)c);
	  input.close();

	  Pattern pattern_token   = Pattern.compile("\"access_token\"\\s+:\\s+\"([^\"]+)\"", Pattern.MULTILINE);
	  Pattern pattern_expires = Pattern.compile("\"expires_in\"\\s*:\\s*([0-9]+)",       Pattern.MULTILINE);
	  Pattern pattern_id      = Pattern.compile("\"id_token\"\\s*:\\s*\"([^\"]+)\"",     Pattern.MULTILINE);

	  Matcher token_matches   = pattern_token.matcher(json);
	  Matcher expires_matches = pattern_expires.matcher(json);
	  Matcher id_matches      = pattern_id.matcher(json);

	  if (token_matches.find() && expires_matches.find() && id_matches.find())
	  {
	    id           = json.substring(id_matches.start(1), id_matches.end(1));
		access_token = json.substring(token_matches.start(1), token_matches.end(1));
		expires_in   = Long.parseLong(json.substring(expires_matches.start(1), expires_matches.end(1)));
	    delivered_at = getTimestamp();
	  }
	}
	  
	private long getTimestamp()
	{
		return ((new java.util.Date()).getTime() / 1000);
	}
}

class GooglePeople {
	public URL    url = new URL("https://www.googleapis.com/plus/v1/people/me");
	public String access_token;
	public String email;

	public GooglePeople(String access_token) throws MalformedURLException
	{
	  this.access_token = access_token;
	}

	public void query() throws IOException
	{
	  HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();

	  System.out.println("Trying to oauth with code: " + access_token);
	  connection.setRequestProperty("Authorization", "Bearer " + access_token);
	  readAnswer(connection);
	}

	private void readAnswer(HttpsURLConnection connection) throws IOException
	{
	  DataInputStream input = new DataInputStream(connection.getInputStream());	  
	  String          json  = new String();

	  for (int c = input.read() ; c != -1 ; c = input.read())
		json += Character.toString((char)c);
      input.close();
      
      Pattern pattern_email = Pattern.compile("\\{\\s*\"value\"\\s*:\\s*\"([^\"]+)\"\\s*,\\s*\"type\"\\s*:\\s*\"account\"");
      
      Matcher email_matcher = pattern_email.matcher(json);
      
      if (email_matcher.find())
    	email = json.substring(email_matcher.start(1), email_matcher.end(1));
      System.out.println(email);
      
      System.out.println("\nDone.");
	}
}