package aggregator.tests;
import static org.junit.Assert.*;

import org.junit.Test;

import aggregator.StringUtils;


public class StringUtilsTests {

	@Test
	public void testEcmaScriptStringEscapeStringChar() {
		String escaped1 = StringUtils.ecmaScriptStringEscape("Il a dit 'coucou'", '\'');
		String escaped2 = StringUtils.ecmaScriptStringEscape("Il a dit \"coucou\"", '"');

		assertEquals(escaped1, "Il a dit \\'coucou\\'");
		assertEquals(escaped2, "Il a dit \\\"coucou\\\"");
	}

	@Test
	public void testSqlField() {
	  String escaped = StringUtils.sqlField("some'value");
	  
	  assertEquals(escaped, "'some\\'value'");
	}

}
