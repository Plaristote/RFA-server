package aggregator.table;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import aggregator.db.Model;
import aggregator.db.SqlConnection;
import aggregator.db.Table;

public class UserFeedTable extends Table
{
	@Override
	public String getTableName()
	{
	  return ("user_feeds");
	}

	@Override
	protected Model createModel(ResultSet row) throws SQLException
	{
	  return null;
	}
	
	public static void linkUserToFeed(String user_id, String feed_id) throws ClassNotFoundException, SQLException
	{
	  UserFeedTable          table     = new UserFeedTable();
	  HashMap<String,String> criterias = new HashMap<String,String>();

	  criterias.put("user_id", user_id);
	  criterias.put("feed_id", feed_id);
	  if (table.where(criterias).entries().size() == 0)
		SqlConnection.getSingleton().statement.execute("INSERT INTO user_feeds VALUES(" + user_id + ", " + feed_id + ')');
	}
}
