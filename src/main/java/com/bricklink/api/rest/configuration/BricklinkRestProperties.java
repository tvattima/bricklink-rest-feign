package com.bricklink.api.rest.configuration;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "bricklink.rest")
public class BricklinkRestProperties {
    private Path clientConfigDir;
    private Path clientConfigFile;
    private URI uri;
    private Bricklink bricklink;

    public void setClientConfigDir(Path clientConfigDir) {
        this.clientConfigDir = clientConfigDir;
        System.out.println(getClientConfigDir());
        loadPropertiesFromJson();
    }

    public void setClientConfigFile(Path clientConfigFile) {
        this.clientConfigFile = clientConfigFile;
        System.out.println(getClientConfigFile());
        loadPropertiesFromJson();
    }

    private void loadPropertiesFromJson() {
        Optional<Path> optionalDir = Optional.ofNullable(getClientConfigDir());
        Optional<Path> optionalFile = Optional.ofNullable(getClientConfigFile());
        if ((optionalDir.isPresent()) && (optionalFile.isPresent())) {
            Path jsonConfigFile = Paths.get(clientConfigDir.toString(), clientConfigFile.toString());
            if (Files.exists(jsonConfigFile)) {
                ObjectMapper mapper = new ObjectMapper();
                mapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
                mapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
                try {
                    bricklink = mapper.readValue(jsonConfigFile.toFile(), Bricklink.class);
                } catch (IOException e) {
                    throw new IllegalStateException(e);
                }
            } else {
                throw new IllegalStateException("[" + jsonConfigFile.toAbsolutePath() + "] does not exist");
            }
        }
    }

    @Data
    @JsonRootName(value = "bricklink")
    public static class Bricklink {
        private Consumer consumer = new Consumer();
        private Token token = new Token();
    }

    @Data
    public static class Consumer {
        private String key;
        private String secret;
    }

    @Data
    public static class Token {
        private String value;
        private String secret;
    }
}

