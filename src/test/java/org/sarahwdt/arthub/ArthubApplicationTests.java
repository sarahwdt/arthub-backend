package org.sarahwdt.arthub;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class ArthubApplicationTests {

	@Test
	void contextLoads() {
	}

}
