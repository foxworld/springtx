package hello.springtx.propagation;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class MemberServiceTest {
	
	@Autowired
	MemberService memberService;
	@Autowired
	MemberRespository memberRespository;
	@Autowired
	LogRepository logRepository;
	
	/*
	 * memberService    @Transaction:off
	 * memberRepository @Transaction:on
	 * logRepository    @Transaction:on
	 */
	
	@Test
	void outerTxOff_success() {
		//given
		String username = "outerTxOff_success";
		//when
		memberService.joinV1(username);
		//then 모든데이터가 저장된다
		assertTrue(memberRespository.find(username).isPresent());
		assertTrue(logRepository.find(username).isPresent());
	}
	
	/*
	 * memberService    @Transaction:off
	 * memberRepository @Transaction:on
	 * logRepository    @Transaction:on
	 */
	
	@Test
	void outerTxOff_fail() {
		//given
		String username = "로그예외_outerTxOff_fail";
		//when
		assertThatThrownBy(() -> memberService.joinV1(username)).isInstanceOf(RuntimeException.class);
		//then 모든데이터가 저장된다
		assertTrue(memberRespository.find(username).isPresent());
		assertTrue(logRepository.find(username).isEmpty());
	}	

}
