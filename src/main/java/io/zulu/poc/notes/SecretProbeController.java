package io.zulu.poc.notes;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Test probe for Vault: shows that a secret was read from Vault, without leaking it.
 * Returns only the length and first character, never the full value.
 * @RefreshScope lets POST /actuator/refresh rebuild this bean after the secret changes in Vault.
 */
@RestController
@RefreshScope
public class SecretProbeController {

    @Value("${notes.api-key:}")
    private String apiKey;

    @GetMapping("/secret-probe")
    public Map<String, Object> probe() {
        if (apiKey.isEmpty()) {
            return Map.of("loaded", false);
        }
        return Map.of(
                "loaded", true,
                "length", apiKey.length(),
                "startsWith", apiKey.substring(0, 1));
    }
}
