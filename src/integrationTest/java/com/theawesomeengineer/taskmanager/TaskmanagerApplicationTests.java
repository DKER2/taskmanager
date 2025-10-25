package com.theawesomeengineer.taskmanager;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
class TaskmanagerApplicationTests extends IntegrationTestBase {
	@Test
	void contextLoads() {
	}
}
