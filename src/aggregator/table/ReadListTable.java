package aggregator.table;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import aggregator.db.Model;
import aggregator.db.SqlConnection;
import aggregator.db.Table;

public class ReadListTable extends Table {

	@Override
	public String getTableName() {
	  return ("read_list");
	}

	@Override
	protected Model createModel(ResultSet row) throws SQLException {
	  return null;
	}

	public void create(long user_id, long feed_id, long post_id) throws ClassNotFoundException, SQLException
	{
	  String query = "INSERT INTO read_list VALUES(" + Long.toString(user_id) + ',' + Long.toString(feed_id) + ',' + Long.toString(post_id) + ')';

	  SqlConnection.getSingleton().statement.execute(query);
	}

	@SuppressWarnings("serial")
	public void destroy(final long user_id, final long feed_id, final long post_id) throws ClassNotFoundException, SQLException
	{
	  where(new HashMap<String,String>() {{
		put("user_id", Long.toString(user_id));
		put("feed_id", Long.toString(feed_id));
		put("post_id", Long.toString(post_id));
	  }}).destroy();
	}
}
