package rs.ac.uns.ftn.informatika.rest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class SecurityConfig {

    @Bean
    public HttpFirewall httpFirewall() {
        // Koristi default firewall koji je manje striktan
        return new DefaultHttpFirewall();
    }

    @Bean
    public WebSecurityConfigurerAdapter webSecurityConfigurerAdapter() {
        return new WebSecurityConfigurerAdapter() {
            @Override
            protected void configure(HttpSecurity http) throws Exception {
                http
                        .csrf().disable()
                        .authorizeRequests()
                        .anyRequest().permitAll();
            }

            @Override
            public void configure(WebSecurity web) throws Exception {
                web.httpFirewall(httpFirewall());
            }
        };
    }

}
