package aggregator.table;

import java.sql.ResultSet;
import java.sql.SQLException;

import aggregator.db.Model;
import aggregator.db.Table;
import aggregator.model.FeedPostModel;

public class FeedPostTable extends Table
{
	@Override
	public String getTableName()
	{
	  return ("feed_posts");
	}

	@Override
	protected Model createModel(ResultSet row) throws SQLException
	{
	  return (new FeedPostModel(this, row));
	}
}
