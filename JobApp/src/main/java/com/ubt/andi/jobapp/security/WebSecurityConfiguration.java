package com.ubt.andi.jobapp.security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@Configuration
public class WebSecurityConfiguration {
    private final UserDetailsService userDetailsService;
    public WebSecurityConfiguration(UserDetailsService userDetailsService){
        this.userDetailsService=userDetailsService;
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .requestMatchers(new AntPathRequestMatcher("/register")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/login")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/jobs/**")).hasAnyRole("Employer")
                .requestMatchers(new AntPathRequestMatcher("/job/{jobId}/applicants")).hasAnyRole("Employer")
                .requestMatchers(new AntPathRequestMatcher("/*/**")).authenticated()
                .and().formLogin().loginPage("/login").loginProcessingUrl("/login").defaultSuccessUrl("/")
                .and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/login?loggedout")
                .invalidateHttpSession(true).deleteCookies("JSESSIONID");

        ;
        return http.build();
    }
    @Bean
    protected static PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }

    public void configureAuthenticationManager(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception{
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }
}
