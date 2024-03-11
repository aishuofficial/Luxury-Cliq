package com.LUXURYCLIQ.Security;

import com.LUXURYCLIQ.Service.UserProviderService;
import com.LUXURYCLIQ.SuccessHandler.CustomAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Autowired
    private CustomAuthenticationSuccessHandler authenticationSuccessHandler;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf().disable()
                .authorizeHttpRequests()
                .antMatchers("/*.css","*.scss","/asset/**","/signup","/registerUser","/shop/**","/admin/**","/agency/**"
                        ,"/admin1/**","/category/**","/static/**","/**","/product/**","/addresses/**","/address/**","/css/**","/home/**","/js/**").permitAll()
                .and()
                .authorizeHttpRequests()
                .antMatchers("/**")
                .authenticated().and()

                .formLogin()
                .loginPage("/login").permitAll()
                .defaultSuccessUrl("/", true)
                .and()
                .exceptionHandling()
                .accessDeniedPage("/403")
                .and()
                .formLogin()
                .successHandler(authenticationSuccessHandler)
                .and()
                .logout()
                .logoutSuccessUrl("/login")
                .and()
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService(){
        return new UserProviderService();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());

        return authenticationProvider;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() throws Exception{
        return (web) -> web.ignoring().antMatchers("/static/**","/templates/**");
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

