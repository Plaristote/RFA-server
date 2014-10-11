package aggregator.table;

import java.sql.ResultSet;
import java.sql.SQLException;

import aggregator.db.Model;
import aggregator.db.Table;
import aggregator.model.FeedModel;

public class FeedTable extends Table
{
  @Override
  public String getTableName()
  {
    return ("feeds");
  }

  @Override
  protected Model createModel(ResultSet row) throws SQLException
  {
    return (new FeedModel(this, row));
  }
}
