package aggregator.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import aggregator.StringUtils;
import aggregator.db.Model;
import aggregator.db.SqlConnection;
import aggregator.db.Table;
import aggregator.table.ReadListTable;
import aggregator.table.UserFeedTable;

public class UserModel extends Model {
	public static final int MIN_CHARACTER_FOR_PASSWORD = 5;
	public String email, password;

	public UserModel(Table table, ResultSet row) throws SQLException {
		super(table, row);
		email    = row.getString("email");
		password = row.getString("password");
	}
	
	public UserModel(Table table) throws SQLException {
		super(table);
	}

	@Override
	public boolean update() throws Exception, SQLException {
		String query = "UPDATE " + table.getTableName();
		
		query += " SET email="    + StringUtils.sqlField(email);
		query += " AND password=" + StringUtils.sqlField(password);
		query += " WHERE id="     + Long.toString(getId());
		return (SqlConnection.getSingleton().statement.execute(query));
	}

	@Override
	public boolean create() throws Exception, SQLException {
		String query = "INSERT INTO " + table.getTableName();

		query += " VALUES(0,";
		query += StringUtils.sqlField(email)       + ',';
		query += StringUtils.sqlField(password);
		query += ')';
		return (insertQuery(query));
	}

	@SuppressWarnings("serial")
	@Override
	public void destroy() throws Exception, SQLException
	{
	  UserFeedTable user_feeds = new UserFeedTable();
	  ReadListTable read_list  = new ReadListTable();

	  user_feeds.where(new HashMap<String,String>() {{ put("user_id", Long.toString(getId())); }}).destroy();
	  read_list.where (new HashMap<String,String>() {{ put("user_id", Long.toString(getId())); }}).destroy();
	  super.destroy();
	}
}
