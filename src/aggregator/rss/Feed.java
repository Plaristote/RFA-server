package aggregator.rss;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class Feed
{
  public String         xml;
  public String         url, link, title, description = "", favicon = "";
  public List<FeedItem> items = new ArrayList<FeedItem>();

  public static Feed download(String url_string) throws IOException, Exception
  {
	URL            url  = new URL(url_string);
	Feed           feed = new Feed();

	feed.url = url.toString();
	feed.loadFromUrl();
	return (feed);
  }

  public void loadFromUrl() throws ParserConfigurationException, SAXException, IOException
  {
    SAXParserFactory            parserFactor = SAXParserFactory.newInstance();
    javax.xml.parsers.SAXParser parser       = parserFactor.newSAXParser();
    SaxRssHandler               handler      = new SaxRssHandler(this);

    parser.parse(url, handler);
  }
}

class SaxRssHandler  extends org.xml.sax.helpers.DefaultHandler {
	public Feed feed;

	public SaxRssHandler(Feed feed)
	{
	  this.feed = feed;
	}
	
	private static final int STATE_ROOT    = 0;
	private static final int STATE_CHANNEL = 1;
	private static final int STATE_IMAGE   = 2;
	private static final int STATE_ITEM    = 3;
	private int              state         = STATE_ROOT;
	private String           content;
	private FeedItem         current_item;

	public void characters(char[] ch, int start, int length)
	{
	  content += String.copyValueOf(ch, start, length).trim();
	}

	/*
	 * startElement
	 */
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
	{
	  content = new String();
	  switch (state)
	  {
	  case STATE_ROOT:
		  startElementAtRoot(uri, localName, qName, attributes);
		  break ;
	  case STATE_CHANNEL:
		  startElementAtChannel(uri, localName, qName, attributes);
		  break ;
	  }
	}
	
	public void startElementAtRoot(String uri, String localName, String qName, Attributes attributes)
	{
	  switch (qName)
	  {
	  case "channel":
		  state = STATE_CHANNEL;
		  break ;
	  }
	}
	
	public void startElementAtChannel(String uri, String localName, String qName, Attributes attributes)
	{
	  switch (qName)
	  {
	  case "image":
		  state = STATE_IMAGE;
		  break ;
	  case "item":
		  state = STATE_ITEM;
		  current_item = new FeedItem();
		  break ;
	  }
	}

	/*
	 * endElement
	 */
	public void endElement(String uri, String localName, String qName) throws SAXException
	{
	  switch (state)
	  {
	  case STATE_CHANNEL:
		  endElementAtChannel(uri, localName, qName);
		  break ;
	  case STATE_IMAGE:
		  endElementAtImage(uri, localName, qName);
		  break ;
	  case STATE_ITEM:
		  endElementAtItem(uri, localName, qName);
		  break ;
	  }
	}
	
	public void endElementAtChannel(String uri, String localName, String qName)
	{
	  switch (qName)
	  {
	  case "channel":
		  state = STATE_ROOT;
		  break ;
	  case "title":
		  feed.title = content;
		  break ;
	  case "description":
		  feed.description = content;
		  break ;
	  case "link":
		  feed.link = content;
		  break ;
	  }
	  if (qName == "channel")
		state = STATE_ROOT;
	  
	}
	
	public void endElementAtImage(String uri, String localName, String qName)
	{
	  switch (qName)
	  {
	  case "image":
		  state = STATE_CHANNEL;
		  break ;
	  case "url":
		  feed.favicon = content;
		  break ;
	  }
	}

	public void endElementAtItem(String uri, String localName, String qName)
	{
	  switch (qName)
	  {
	  case "item":
		  feed.items.add(current_item);
		  state = STATE_CHANNEL;
		  break ;
	  case "title":
		  current_item.title = content;
		  break ;
	  case "description":
		  current_item.description = content;
		  break ;
	  case "link":
		  current_item.link = content;
		  break ;
	  case "category":
		  current_item.category = content;
		  break ;
	  case "comments":
		  current_item.comments = content;
		  break ;
	  case "pubDate":
		  current_item.pubDate = content;
		  break ;
	  }
	}

}
