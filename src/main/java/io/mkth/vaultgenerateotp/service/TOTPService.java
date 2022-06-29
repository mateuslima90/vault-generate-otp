package io.mkth.vaultgenerateotp.service;

import com.bettercloud.vault.Vault;
import com.bettercloud.vault.VaultConfig;
import com.bettercloud.vault.VaultException;
import com.bettercloud.vault.response.LogicalResponse;
import io.mkth.vaultgenerateotp.model.TOTPResponse;
import io.mkth.vaultgenerateotp.model.TOTPValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class TOTPService {

    private final String pathWrite = "totp/keys/user-";

    private final String pathRead = "totp/code/user-";

    private final String CODE = "code";

    private final String VALID = "valid";

    private final VaultConfig vaultConfig;

    private final Vault vault;

    public TOTPService(VaultConfig vaultConfig) {
        this.vaultConfig = vaultConfig;
        this.vault = new Vault(vaultConfig);
    }

    Logger logger = LoggerFactory.getLogger(TOTPService.class);

    public Mono<TOTPValidation> generateOTP(String username) {
        return Mono.just(writeTOTP(pathWrite.concat(username), userInfo(username)))
                .doOnSuccess(response -> logger.info("Generate a totp with success to user {}", username))
                .flatMap(path -> readOTP(username));
    }

    public Mono<TOTPValidation> readOTP(String username) {
        return Mono.just(pathRead.concat(username))
                .map(path -> readTOTP(path))
                .doOnSuccess(user -> logger.info("Read a totp with success to user {}", user))
                .map(code -> new TOTPValidation(code.toString()));
    }

    public Mono<TOTPResponse>  validateOTP(String username, String code) {
        return Mono.just(pathRead.concat(username))
                .map(path -> writeTOTP(path, codeValidation(code)))
                .filter(lr -> !lr.getData().isEmpty())
                .map(otp -> new TOTPResponse(Boolean.valueOf(otp.getData().get(VALID))))
                .doOnSuccess(user -> logger.info("Validate a totp with success to user {}", username))
                .onErrorResume(response -> Mono.error(response))
                .defaultIfEmpty(new TOTPResponse(false));
    }

    private Object readTOTP(String path) {
        try {
            return this.vault.logical().read(path).getData().get(CODE);
        } catch (VaultException e) {
            throw new RuntimeException(e);
        }
    }

    private LogicalResponse writeTOTP(String path, Map<String, Object> objectMap) {
        try {
            return this.vault.logical().write(path, objectMap);
        } catch (VaultException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, Object> codeValidation(String code) {
        return Map.of(CODE, code);
    }

    private Map<String, Object> userInfo(String username) {
        return Map.of("generate", "true",
                      "exported", "true",
                      "issuer", "Vault",
                      "account_name", username);
    }
}
