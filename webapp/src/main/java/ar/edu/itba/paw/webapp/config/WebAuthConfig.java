package ar.edu.itba.paw.webapp.config;

import ar.edu.itba.paw.webapp.auth.AuthenticationSuccessHandlerImpl;
import ar.edu.itba.paw.webapp.auth.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
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

    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

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
                .invalidSessionUrl("/")
            .and().authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/verifyEmail").hasAnyRole(UserRole.ANONYMOUS.name(), UserRole.UNVERIFIED.name())
                .antMatchers("/mediclist/**").hasAnyRole(UserRole.ANONYMOUS.name(), UserRole.PATIENT.name())
                .antMatchers("/login").anonymous()
                .antMatchers("/signup/**").anonymous()
                .antMatchers("/patient/**").hasRole(UserRole.PATIENT.name())
                .antMatchers("/staff/**").hasRole(UserRole.STAFF.name())
                .antMatchers("/img/**").permitAll()
                .antMatchers("/profilepics").authenticated()
                .antMatchers("/**").authenticated()
            .and().formLogin()
                .usernameParameter("email")
                .passwordParameter("password")
                .loginPage("/login")
                .permitAll()
                .successHandler(new AuthenticationSuccessHandlerImpl())
            .and().rememberMe()
                .rememberMeParameter("rememberMe")
                .userDetailsService(this.userDetailsService)
                .key(this.getSecretKey())
                .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(30))
            .and().logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .permitAll()
            .and().exceptionHandling()
                .accessDeniedPage("/403")
            .and().csrf()
                .disable();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/js/**", "/css/**", "/img/**", "/403", "/500", "/404");
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
