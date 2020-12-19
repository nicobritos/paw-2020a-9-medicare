package ar.edu.itba.paw.webapp.config;

import ar.edu.itba.paw.webapp.auth.AccessDeniedHandlerImpl;
import ar.edu.itba.paw.webapp.auth.JWTAuthenticationEntryPoint;
import ar.edu.itba.paw.webapp.auth.JWTAuthenticationFilter;
import ar.edu.itba.paw.webapp.auth.JWTAuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@Configuration
@ComponentScan(basePackages = {"ar.edu.itba.paw.webapp.auth"})
@EnableGlobalMethodSecurity(prePostEnabled = true, proxyTargetClass = true)
public class WebAuthConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsService userDetailsService;

    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JWTAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        JWTAuthenticationFilter authenticationFilter = new JWTAuthenticationFilter();
        authenticationFilter.setAuthenticationManager(this.authenticationManagerBean());
        return authenticationFilter;
    }

    @Bean
    public JWTAuthorizationFilter jwtAuthorizationFilter() throws Exception {
        return new JWTAuthorizationFilter(this.authenticationManager());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(this.userDetailsService)
                .passwordEncoder(this.passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .antMatcher("/api/**")
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().csrf().disable()
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers(HttpMethod.POST, "/login").anonymous()
                .antMatchers(HttpMethod.POST, "/auth/logout").authenticated()
                .antMatchers(HttpMethod.GET, "/verify/**").permitAll() // Verifies a user
                .antMatchers(HttpMethod.POST, "/auth/refresh").permitAll() // Refreshes the access token
                .antMatchers(HttpMethod.POST, "/users").anonymous() // Creates a user
                .antMatchers(HttpMethod.GET, "/specialties").permitAll()
                .antMatchers(HttpMethod.GET, "/countries").permitAll()
                .antMatchers(HttpMethod.GET, "/provinces").permitAll()
                .antMatchers(HttpMethod.GET, "/localities").permitAll()
                .antMatchers(HttpMethod.GET, "/doctors/**").permitAll()
                .anyRequest().fullyAuthenticated().and()
                .addFilter(this.jwtAuthorizationFilter())  // Verifies JWT if provided
                .addFilterAfter(this.jwtAuthenticationFilter(), JWTAuthorizationFilter.class) // Authenticates a user and sends JWT and Refresh token
                .exceptionHandling()
                .authenticationEntryPoint(new JWTAuthenticationEntryPoint()) // Handles forbidden/unauthorized page access exceptions
                .accessDeniedHandler(new AccessDeniedHandlerImpl()); // Handles role exceptions
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/js/**", "/css/**", "/img/**");
    }
}
