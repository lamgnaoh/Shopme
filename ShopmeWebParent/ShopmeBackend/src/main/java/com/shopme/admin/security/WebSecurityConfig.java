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
        // setting vi???c access application se can authentication (login)
        http.authorizeHttpRequests()
            .antMatchers("/users/**").hasAuthority("Admin")
            .antMatchers("/categories/**").hasAnyAuthority("Admin","Editor")
            .anyRequest().authenticated() // t???t c??? c??c request ?????n ?????u c???n authenticated - c???n login
            .and()
                .formLogin()
                .loginPage("/login") // form login tu??? ch???nh v???i ???????ng d???n t???i trang login l?? /login ch??? kh??ng s??? d???ng login page m???c ?????nh c???a spring security
                .usernameParameter("email")
                .permitAll() // cho ph??p t???t c??? ?????u ???????c quy???n truy c???p trang login
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
