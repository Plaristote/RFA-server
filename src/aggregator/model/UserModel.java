package aggregator.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import aggregator.StringUtils;
import aggregator.db.Model;
import aggregator.db.SqlConnection;
import aggregator.db.Table;

public class UserModel extends Model {
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

}
