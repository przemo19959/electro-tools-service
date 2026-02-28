package pl.dabrowski.electrotools;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import pl.dabrowski.electrotools.security.SecurityAuditorAware;
import pl.dabrowski.electrotools.security.SecurityConverter;

import jakarta.persistence.EntityManager;

import static org.springframework.security.config.Customizer.withDefaults;

@SpringBootApplication
@EnableJpaRepositories
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@RequiredArgsConstructor
public class ElectroToolsApplication {
  private final EntityManager entityManager;

  public static void main(String[] args) {
    SpringApplication.run(ElectroToolsApplication.class, args);
  }

  @Bean
  public JPAQueryFactory jpaQueryFactory() {
    return new JPAQueryFactory(entityManager);
  }

  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry
            .addMapping("/**")
            .allowedOrigins("http://localhost:4200")
            .allowedMethods("GET", "POST", "PUT", "DELETE");
      }
    };
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .cors(withDefaults())
        .csrf(AbstractHttpConfigurer::disable);
//        .authorizeRequests()
//        .anyRequest()
//        .authenticated()
//        .and()
//        .oauth2ResourceServer()
//        .jwt(jwt -> jwt.jwtAuthenticationConverter(tokenConverter()));

    return http.build();
  }

  @Bean
  public JwtAuthenticationConverter tokenConverter() {
    JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
    converter.setJwtGrantedAuthoritiesConverter(new SecurityConverter());

    return converter;
  }

  @Bean
  AuditorAware<String> auditorProvider() {
    return new SecurityAuditorAware();
  }
}
