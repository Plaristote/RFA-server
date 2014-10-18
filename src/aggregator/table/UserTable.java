package aggregator.table;

import java.sql.ResultSet;
import java.sql.SQLException;

import aggregator.db.Model;
import aggregator.db.Table;
import aggregator.model.UserModel;

public class UserTable extends Table {

	@Override
	public String getTableName() {
		return ("users");
	}

	@Override
	protected Model createModel(ResultSet row) throws SQLException {
		return (new UserModel(this, row));
	}

	public UserModel create(String email) throws Exception {
		UserModel user = new UserModel(this);
		
		user.email    = email;
		user.password = "";
		user.save();
		return (user);
	}

}
