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
	  scheduler.scheduleWithFixedDelay(runnable, 0, 5, TimeUnit.MINUTES);
	  feed_updaters.put(feed_id,  runnable);
	}
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
	
	public FeedUpdater(long feed_id)
	{
	  this.feed_id = feed_id;
	}

	@Override
	public void run() {
	  try {
		System.out.println("Updating feed " + feed_id);
	    FeedTable table = new FeedTable();
        FeedModel feed  = (FeedModel)table.find((int)feed_id);

		feed.reloadFromSource();
	  } catch (Exception e) {
	  }
	}
}
