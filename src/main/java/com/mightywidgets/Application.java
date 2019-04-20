package com.mightywidgets;

import com.mightywidgets.repository.WidgetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.ThreadLocalRandom;

@SpringBootApplication
public class Application {

    Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    WidgetRepository newRepository() {
        WidgetRepository widgetRepository = new WidgetRepository();

        for (int i = 0; i < 3; i++) {
            widgetRepository.save(new Widget(null, ThreadLocalRandom.current().nextInt(-10, 10),
                    ThreadLocalRandom.current().nextInt(-10, 10),
                    ThreadLocalRandom.current().nextInt(0, 100),
                    ThreadLocalRandom.current().nextInt(0, 100),
                    ThreadLocalRandom.current().nextInt(0, 100)));
        }
        return widgetRepository;
    }

}