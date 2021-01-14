package com.luv2code.diary.configuration;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Configuration
@ConfigurationProperties(prefix = "com.luv2code.mail")
public class UserStatusMessageConfiguration {

    private Map<String, String> userStatusMessage;

}
