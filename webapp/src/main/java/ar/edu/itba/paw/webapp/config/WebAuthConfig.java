package ar.edu.itba.paw.webapp.config;

import ar.edu.itba.paw.webapp.auth.UserRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

@EnableWebSecurity
@Configuration
@ComponentScan(basePackages = { "ar.edu.itba.paw.webapp.auth" })
public class WebAuthConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsService userDetailsService;
    @Value("classpath:rememberMe.key")
    private Resource secret;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(this.userDetailsService)
                .passwordEncoder(this.passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement()
                .invalidSessionUrl("/login")
                .invalidSessionUrl("/signup")
            .and().authorizeRequests()
                .anyRequest().fullyAuthenticated()
                .antMatchers("/").anonymous()
                .antMatchers("/login").anonymous()
                .antMatchers("/signup").anonymous()
                .antMatchers("/patient/**").hasRole(UserRoles.PATIENT.name())
                .antMatchers("/staff/**").hasRole(UserRoles.STAFF.name())
            .and().formLogin()
                .usernameParameter("email")
                .passwordParameter("password")
//                .loginProcessingUrl("/login")
                .loginPage("/login")
                .permitAll()
                .defaultSuccessUrl("/", false)  // todo: change to /patient or /staff
            .and().rememberMe()
                .rememberMeParameter("remember_me")
                .userDetailsService(this.userDetailsService)
                .key(this.getSecretKey())
                .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(30))
            .and().logout()
                .logoutUrl("/logout") // todo
                .logoutSuccessUrl("/")
            .and().exceptionHandling()
                .accessDeniedPage("/403")
            .and().csrf()
                .disable();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**", "/img/**", "/403", "/500", "/404");
    }

    private String getSecretKey() throws IOException {
        InputStreamReader streamReader = new InputStreamReader(this.secret.getInputStream(), StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(streamReader);

        StringBuilder stringBuilder = new StringBuilder();
        for (String line; (line = reader.readLine()) != null;) {
            stringBuilder.append(line);
        }
        return stringBuilder.toString();
    }

    public static class SecurityWebInitializer extends AbstractSecurityWebApplicationInitializer {

    }
}
