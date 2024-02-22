package sellyourunhappiness.api.log.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class LogTestController {
	@GetMapping("/test")
	public void test() {
		log.trace("TRACE!!");
		log.debug("DEBUG!!");
		log.info("INFO!!");
		log.warn("WARN!!");
		log.error("ERROR!!");
	}
}
