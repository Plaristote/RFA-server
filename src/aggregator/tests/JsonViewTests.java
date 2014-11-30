package aggregator.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import aggregator.JsonView;

public class JsonViewTests {

	private JsonView view = new JsonView();
	
	@Test
	public void testPropertyStringString() {
		view.property("property", "value");
		assertEquals("{\"property\":\"value\"}", view.render());
	}

	@Test
	public void testPropertyStringInt() {
		view.property("property", 21);
		assertEquals("{\"property\":21}", view.render());
	}

	@Test
	public void testPropertyStringBoolean() {
		view.property("property_true",  true);
		view.property("property_false", false);
		assertEquals("{\"property_true\":true,\"property_false\":false}", view.render());
	}

	@Test
	public void testObjectString() {
		view.object("my_object");
		view.end_object();
		assertEquals("{\"my_object\":{}}", view.render());
	}

	@Test
	public void testObject() {
		view.object();
		view.end_object();
		assertEquals("{{}}", view.render());
	}

	@Test
	public void testArrayString() {
		view.array("my_array");
		view.end_array();
		assertEquals("{\"my_array\":[]}", view.render());
	}

	@Test
	public void testArray() {
		view.array();
		view.end_array();
		assertEquals("{[]}", view.render());
	}
}
