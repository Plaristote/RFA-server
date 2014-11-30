package aggregator.tests;

import static org.junit.Assert.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.mockito.*;

import aggregator.Controller;
import aggregator.Router;

public class RouterTests {
	private class ControllerImplementation extends Controller {
		public boolean setHttpResourcesHasBeenCalled = false;
		@Override
		public void setHttpResources(HttpServletRequest request, HttpServletResponse response)
		{
			setHttpResourcesHasBeenCalled = true;
		}
	}

	@SuppressWarnings("serial")
	private class RouterImplementation extends Router {
		@Override
		public void initializeController() {
			controller = new ControllerImplementation();
		}
		
		ControllerImplementation getController() { return (ControllerImplementation)controller; }
	}

	@Spy RouterImplementation router = new RouterImplementation();

	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void loadControllerMustCallInitializeController() {
		router.loadController(null, null);
		assertNotEquals(router.getController(), null);
	}

	@Test
	public void loadControllerMustSetHttpRessourcesForController() {
		router.loadController(null, null);
		assertEquals(router.getController().setHttpResourcesHasBeenCalled, true);
	}

	@Test
	public void testGetIdFromUri() {
		assertEquals(router.getIdFromUri("/request/uri/12"), "12");
		assertEquals(router.getIdFromUri("/some_uri/424"), "424");
	}
}
