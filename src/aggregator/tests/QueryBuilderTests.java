package aggregator.tests;

import static org.junit.Assert.*;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;

import aggregator.db.Model;
import aggregator.db.QueryBuilder;

import java.util.HashMap;

public class QueryBuilderTests {
	private class MockTable extends aggregator.db.Table {
		@Override
		public String getTableName() { return "laTable"; }
		@Override
		protected Model createModel(ResultSet row) throws SQLException { return null; }
	}
	
	public QueryBuilder criteria = new QueryBuilder(new MockTable());

	@Test
	public void testWhereStringStringInt() {
		criteria.where("somekey", "<=", 12);
		assertEquals("SELECT * FROM laTable WHERE somekey<=12", criteria.toString());
	}
	
	@SuppressWarnings("serial")
	@Test
	public void testWhereMap() {
		criteria.where(new HashMap<String,String>() {{
			  put("feed_id", "test");
			  put("user_id", "poil");
			}});
		assertEquals("SELECT * FROM laTable WHERE user_id=\"poil\" AND feed_id=\"test\"", criteria.toString());
	}

	@Test
	public void testLimit() {
		criteria.limit(50);
		assertEquals("SELECT * FROM laTable LIMIT 50", criteria.toString());
	}

	@Test
	public void testSkip() {
		criteria.skip(42);
		assertEquals("SELECT * FROM laTable OFFSET 42", criteria.toString());
	}

	@Test
	public void testOrder_by() {
		criteria.order_by("myfield", "DESC");
		assertEquals("SELECT * FROM laTable ORDER BY myfield DESC", criteria.toString());
	}

	@Test
	public void testJoin() {
		criteria.join("otherTable", "foreign_key");
		assertEquals("SELECT * FROM laTable INNER JOIN otherTable ON otherTable.foreign_key_id = laTable.id", criteria.toString());
	}

}
