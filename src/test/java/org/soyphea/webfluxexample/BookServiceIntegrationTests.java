package org.soyphea.webfluxexample;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.support.TestPropertySourceUtils;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class BookServiceIntegrationTests implements ApplicationContextInitializer<ConfigurableApplicationContext> {

	private MockWebServer mockWebServer;

	@Autowired
	BookService bookService;

	int port = 9090;

	ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
		TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
				configurableApplicationContext, "book.base-url=http://localhost:"+port );
	}

	@BeforeEach
	void setUp() throws IOException {
		this.mockWebServer = new MockWebServer();
		this.mockWebServer.start(port);
		System.getProperty("book.base-url", this.mockWebServer.getHostName()+this.mockWebServer.getPort());
	}

	@Test
	void tetGetBook_Should_Return_Ok() throws Exception{
		String responseBody = objectMapper.writeValueAsString(new Book("Cloud-native Java",100.0));
		prepareResponse(response -> response.setHeader("Content-Type", "application/json").setBody(responseBody));
		Book book = bookService.getBookById("123");
		assertThat(book).isNotNull();

	}

	@Test
	void contextLoads() {
	}

	@AfterEach
	void tearDown() throws Exception {
		this.mockWebServer.shutdown();
	}

	private void prepareResponse(Consumer<MockResponse> consumer) {
		MockResponse response = new MockResponse();
		consumer.accept(response);
		this.mockWebServer.enqueue(response);
	}
}
