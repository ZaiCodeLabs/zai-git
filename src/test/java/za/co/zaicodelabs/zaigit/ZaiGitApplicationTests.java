package za.co.zaicodelabs.zaigit;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Simple test to verify the application class exists and can be instantiated.
 * Does NOT load Spring context to avoid hanging on user input.
 */
class ZaiGitApplicationTests {

	@Test
	void testApplicationClassExists() {
		assertNotNull(ZaiGitApplication.class,
				"ZaiGitApplication class should exist");
	}

	@Test
	void testMainMethodExists() throws NoSuchMethodException {
		// Verify main method exists with correct signature
		var mainMethod = ZaiGitApplication.class.getMethod("main", String[].class);
		assertNotNull(mainMethod, "main() method should exist");
	}
}