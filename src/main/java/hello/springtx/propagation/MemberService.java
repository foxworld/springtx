package hello.springtx.propagation;

import org.antlr.v4.runtime.misc.LogManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
	
	private final MemberRespository memberRespository;
	private final LogRepository logRepository;
	
	public void joinV1(String username) {
		Member member = new Member(username);
		Log logMessage = new Log(username);
		
		log.info("== memberRepositoy 호출 시작 ==");
		memberRespository.save(member);
		log.info("== memberRepositoy 호출 종료 ==");
		
		log.info("== logRepositoy 호출 시작 ==");
		logRepository.save(logMessage);
		log.info("== logRepositoy 호출 종료 ==");
	}

	public void joinV2(String username) {
		Member member = new Member(username);
		Log logMessage = new Log(username);
		
		log.info("== memberRepositoy 호출 시작 ==");
		memberRespository.save(member);
		log.info("== memberRepositoy 호출 종료 ==");
		
		log.info("== logRepositoy 호출 시작 ==");
		try {
			logRepository.save(logMessage);
		} catch (RuntimeException e) {
			log.info("log 저장에 실패하였습니다. logMessage={}", logMessage, e);
			log.info("정상 흐름 반환");
		}
		log.info("== logRepositoy 호출 종료 ==");
	}
	
}
