package org.soyphea.webfluxexample;

import io.netty.channel.ChannelOption;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;


@Component
@Slf4j
public class BookClient {

    @Value("${book.base-url}")
    String baseUrl;


    public WebClient getWebClient() {
        HttpClient client = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000)
                .responseTimeout(Duration.ofSeconds(1));

        log.info("Sending to book with base url:{}",baseUrl);

        WebClient webClient = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(client))
                .baseUrl(baseUrl)
                .build();
        return webClient;
    }
}
