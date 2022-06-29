package io.mkth.vaultgenerateotp.configuration;

import com.bettercloud.vault.VaultConfig;
import com.bettercloud.vault.VaultException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VaultConfiguration {

    Logger logger = LoggerFactory.getLogger(VaultConfiguration.class);

    @Value("${vault.root.address}")
    private String address;

    @Value("${vault.root.token}")
    private String token;

    @Bean
    public VaultConfig createVaultConfiguration() throws VaultException {
        return new VaultConfig()
                .address(address)
                .token(token)
                .engineVersion(1)
                .build();
    }
}
