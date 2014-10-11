package aggregator.rss;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class Feed
{
  public String url, title, description;

  public static Feed download(String url_string) throws IOException
  {
	URL            url  = new URL(url_string);
	BufferedReader in   = new BufferedReader(new InputStreamReader(url.openStream()));
	String         input;
	String         xml  = new String();
	Feed           feed = new Feed();

	while ((input = in.readLine()) != null)
	  xml += input;
	feed.url = url.toString();
	feed.loadFromXml(xml);
	return (feed);
  }

  public void loadFromXml(String xml)
  {
	System.out.println("FUCK YEAH");
	System.out.println(xml);
  }
}
