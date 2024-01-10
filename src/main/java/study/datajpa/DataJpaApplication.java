package study.datajpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Optional;
import java.util.UUID;

@EnableJpaAuditing // (modifyOnCreate = false) // 얘는 false 로 하면 updated 는 null 값이 들어감
@SpringBootApplication
//@EnableJpaRepositories(basePackages = "study.datajpa.repository") // 스프링 부트를 사용하면 생략 가능
public class DataJpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataJpaApplication.class, args);
	}

	@Bean
	public AuditorAware<String> auditorProvider() {
		return () -> Optional.of(UUID.randomUUID().toString()); // 지금은 예시로 uuid 썼지만, 실제로는 session값을 쓰거나 하면됨
	}

}
