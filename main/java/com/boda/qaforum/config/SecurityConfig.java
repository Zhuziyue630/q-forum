package com.boda.qaforum.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers(
                        "/", "/home",
                        "/login", "/register",
                        "/discussions", "/discussions/**",
                        "/css/**", "/js/**", "/images/**",
                        "/h2-console/**"
                ).permitAll()
                .antMatchers("/discussions/new", "/discussions/*/replies").authenticated()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/discussions")
                .failureUrl("/login?error=true")
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .permitAll()
                .and()
                .csrf()
                .ignoringAntMatchers("/h2-console/**")
                .and()
                .headers()
                .frameOptions().sameOrigin();

        // 为了方便开发，暂时禁用CSRF
        http.csrf().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 使用内存用户，明文密码（最简单的方式）
        auth.inMemoryAuthentication()
                .withUser("admin").password("{noop}admin123").roles("USER")
                .and()
                .withUser("user").password("{noop}123456").roles("USER")
                .and()
                .withUser("test").password("{noop}test123").roles("USER");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // 为了简单，使用无操作的密码编码器（不加密）
        return NoOpPasswordEncoder.getInstance();
        // 或者使用BCrypt：
        // return new BCryptPasswordEncoder();
    }
}