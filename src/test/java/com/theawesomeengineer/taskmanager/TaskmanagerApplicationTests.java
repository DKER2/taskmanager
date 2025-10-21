package com.theawesomeengineer.taskmanager;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@ComponentScan("com.theawesomeengineer.taskmanager.mappers")
@ExtendWith(MockitoExtension.class)
class TaskmanagerApplicationTests {
	@Test
	void contextLoads() {
	}
}
