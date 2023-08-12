package hello.springtx.apply;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import jakarta.annotation.PostConstruct;

@SpringBootTest
public class InitTxTest {
	@Autowired
	Hello hello;

	@Test
	void go() {
		// 초기화 코드는 스프링이 초기화 시점에 호출한다.
	}

	@TestConfiguration
	static class InitTxTestConfig {
		@Bean
		Hello hello() {
			return new Hello();
		}
	}

	@Slf4j
	static class Hello {
		// @PostConstruct spring이 처음실행할때 실행한다(호출하지않아도 자동으로 실행)
		// @PostConstruct은 @Transactional 이 실행되지 않는다
		@PostConstruct
		@Transactional
		public void initV1() {
			boolean isActive = TransactionSynchronizationManager.isActualTransactionActive();
			// Hello init @PostConstruct tx active=false
			log.info("Hello init @PostConstruct tx active={}", isActive);
		}

		// @EventListener 는 이밴트가 발생하면 실행된다
		// @EventListener(value = ApplicationReadyEvent.class) 는 spring이 완전 다 떳을때 실행한다
		// @@Transactional spring이 다 떳깄때문에 transaction이 실행될수 있다
		@EventListener(value = ApplicationReadyEvent.class)
		@Transactional
		public void init2() {
			boolean isActive = TransactionSynchronizationManager.isActualTransactionActive();
			// Hello init ApplicationReadyEvent tx active=true
			log.info("Hello init ApplicationReadyEvent tx active={}", isActive);
		}
	}
}