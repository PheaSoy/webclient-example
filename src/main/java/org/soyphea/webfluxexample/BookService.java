package org.soyphea.webfluxexample;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class BookService {

    private final BookClient bookClient;

    public BookService(BookClient bookClient) {
        this.bookClient = bookClient;
    }

    public Book getBookById(String id) {
        Mono<Book> bookMono =bookClient.getWebClient()
                .get()
                .uri("/books")
                .retrieve().bodyToMono(Book.class);
        Book book = bookMono.block();
        log.info("Books are => {}",book);
        return book;
    }


}
@AllArgsConstructor
@NoArgsConstructor
class Book {
    String title;
    double price;

    public String getTitle() {
        return title;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", price=" + price +
                '}';
    }
}

