package com.shopme.admin.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.userdetails.DaoAuthenticationConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
// cau hinh spring security: co @EnableWebSecurity va extend WebSecurityConfigurer
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(){
        return new ShopmeUserDetailsService();
    }

    public DaoAuthenticationProvider daoAuthenticationProvider(){
//        tell the spring security authentication will be base on database
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationProvider.setUserDetailsService(userDetailsService());
        return authenticationProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        config authentication provider
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // setting việc access application se can authentication (login)
        http.authorizeHttpRequests()
            .antMatchers("/users/**").hasAuthority("Admin")
            .antMatchers("/categories/**","/brands/**").hasAnyAuthority("Admin","Editor")
            .anyRequest().authenticated() // tất cả các request đến đều cần authenticated - cần login
            .and()
                .formLogin()
                .loginPage("/login") // form login tuỳ chỉnh với đường dẫn tới trang login là /login chứ không sử dụng login page mặc định của spring security
                .usernameParameter("email")
                .permitAll() // cho phép tất cả đều được quyền truy cập trang login
            .and()
                .logout()
                .permitAll()
            .and().rememberMe()
                .key("010203040506070809ShopmeAdmin")
                .tokenValiditySeconds(7*24*60*60);
    }


    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/images/**" , "/js/**" , "/webjars/**" , "/css/**");
    }
}
