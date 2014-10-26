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
          if (qName == "channel")
	  {
		  state = STATE_CHANNEL;
	  }
	}
	
	public void startElementAtChannel(String uri, String localName, String qName, Attributes attributes)
	{
          if (qName == "image")
		  state = STATE_IMAGE;
          else if (qName == "item")
          {
		  state = STATE_ITEM;
		  current_item = new FeedItem();
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
          if (qName == "channel")
		  state = STATE_ROOT;
          else if (qName == "title")
		  feed.title = content;
          else if (qName == "description")
		  feed.description = content;
          else if (qName == "link")
		  feed.link = content;
	}
	
	public void endElementAtImage(String uri, String localName, String qName)
	{
          if (qName == "image")
		  state = STATE_CHANNEL;
	  else if (qName == "url")
		  feed.favicon = content;
	}

	public void endElementAtItem(String uri, String localName, String qName)
	{
	  if (qName == "item")
          {
		  feed.items.add(current_item);
		  state = STATE_CHANNEL;
	  }
	  else if (qName == "title")
		  current_item.title = content;
	  else if (qName == "description")
		  current_item.description = content;
	  else if (qName == "link")
		  current_item.link = content;
	  else if (qName == "category")
		  current_item.category = content;
	  else if (qName == "comments")
		  current_item.comments = content;
	  else if (qName == "pubDate")
		  current_item.pubDate = content;
	}

}
