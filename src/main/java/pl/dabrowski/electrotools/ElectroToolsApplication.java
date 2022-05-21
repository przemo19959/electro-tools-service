package pl.dabrowski.electrotools;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
@EnableJpaAuditing
public class ElectroToolsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ElectroToolsApplication.class, args);
    }

}
