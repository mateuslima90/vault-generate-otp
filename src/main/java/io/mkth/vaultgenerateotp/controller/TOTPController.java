package io.mkth.vaultgenerateotp.controller;

import com.bettercloud.vault.VaultException;
import io.mkth.vaultgenerateotp.model.TOTPResponse;
import io.mkth.vaultgenerateotp.model.TOTPValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import io.mkth.vaultgenerateotp.service.TOTPService;

@RestController
@RequestMapping("/totp")
public class TOTPController {

    Logger logger = LoggerFactory.getLogger(TOTPController.class);

    private TOTPService totpService;

    public TOTPController(TOTPService totpService) {
        this.totpService = totpService;
    }

    @PostMapping("/generate/{username}")
    public Mono<TOTPValidation> writeOTP(@PathVariable("username") String username) {
        logger.info("Generating a totp to user {}", username);
        return this.totpService.generateOTP(username);
    }

    @PostMapping("/validate/{username}")
    public Mono<TOTPResponse> validateOTP(@PathVariable("username") String user,
                                          @RequestBody TOTPValidation totp)  {
        logger.info("Validating a totp to user {}", user);
        logger.info("Validating a totp {}", totp);
        //logger.info("Init sleep")
        //Thread.sleep(4900)
        //logger.info("Terminate sleep")
        return this.totpService.validateOTP(user, totp.getCode())
                .map(result -> result)
                .doOnError(response -> new Exception(response));
    }

    @ExceptionHandler(VaultException.class)
    private void handleException(String message) {
        logger.info("Failed proccess from vault with error {}", message);
        ResponseEntity.badRequest().body("Unauthorized");
    }
}
