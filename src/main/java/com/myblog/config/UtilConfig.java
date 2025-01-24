package com.myblog.config;

import com.myblog.utility.TokenUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UtilConfig {

        @Bean
        public TokenUtil tokenUtil() {
            return new TokenUtil();
        }

}
