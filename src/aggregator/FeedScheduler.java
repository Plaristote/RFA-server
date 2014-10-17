package aggregator;

import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import aggregator.model.FeedModel;
import aggregator.table.FeedTable;

public class FeedScheduler {
  private final static  ScheduledExecutorService  scheduler = Executors.newScheduledThreadPool(1);
  private static HashMap<Long,FeedUpdater> feed_updaters    = new HashMap<Long,FeedUpdater>();

  public static void scheduleUpdate(FeedModel feed)
  {
	long        feed_id  = feed.getId();
	FeedUpdater runnable = feed_updaters.get(new Long(feed_id));

	if (runnable == null && feed_id != 0)
	{
	  runnable = new FeedUpdater(feed.getId());
	  scheduler.scheduleWithFixedDelay(runnable, 0, 60, TimeUnit.MINUTES);
	  feed_updaters.put(feed_id,  runnable);
	}
	else if (runnable.shouldUpdate())
	  scheduler.schedule(runnable, 0, TimeUnit.SECONDS);
  }
  
  public static void stopUpdatingFeed(long _feed_id)
  {
	Long        feed_id  = new Long(_feed_id);
    FeedUpdater runnable =feed_updaters.get(feed_id);

    if (runnable != null)
      feed_updaters.remove(feed_id);
  }
}

class FeedUpdater implements Runnable {
	public long feed_id;
	public long last_run = 0;
	
	public FeedUpdater(long feed_id)
	{
	  this.feed_id = feed_id;
	}

	@Override
	public void run() {
	  if (shouldUpdate()) {
	    try {
		  System.out.println("Updating feed " + feed_id);
	      FeedTable table = new FeedTable();
          FeedModel feed  = (FeedModel)table.find((int)feed_id);

		  feed.reloadFromSource();
		  hasBeenUpdated();
	    }
	    catch (Exception e) {
	      System.out.println(e.getMessage());
	      e.printStackTrace();
	    }
	  }
	}

	public synchronized boolean shouldUpdate() {
	  if (last_run != 0) {
	    long timestamp = (new java.util.Date()).getTime();

	    return (timestamp - last_run > 60);
	  }
	  return (true);
	}

	private synchronized void hasBeenUpdated() {
		last_run = (new java.util.Date()).getTime();
	}
}
