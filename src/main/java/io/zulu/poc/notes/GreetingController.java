package io.zulu.poc.notes;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Test probe for Spring Cloud Config: shows a property served by the Config Server.
 * @RefreshScope makes this bean rebuild on POST /actuator/refresh, so the new value shows up.
 */
@RestController
@RefreshScope
public class GreetingController {

    @Value("${notes.greeting:greeting not configured}")
    private String greeting;

    @GetMapping("/greeting")
    public Map<String, String> greeting() {
        return Map.of("greeting", greeting);
    }
}
