package com.yvanscoop.gestmedecins.security;

import com.yvanscoop.gestmedecins.utilities.SecurityUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String[] PUBLIC_MATCHERS = {
            "/css/**",
            "/js/**",
            "/fonts/**",
            "/imgs/**",
            "/images/**",
            "/login"
    };
    @Autowired
    private DataSource dataSource;

    private BCryptPasswordEncoder passwordEncoder() {
        return SecurityUtility.passwordEncoder();
    }

  /*  @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }
*/
    @Autowired
    public void globalConfiguration(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("select username as principal, password as credentials,active from users where username=?").
                authoritiesByUsernameQuery("select username as principal,role as role " +
                        "from users u,roles r,users_roles ur " +
                        "where u.id = ur.user_id and r.id = ur.roles_id and username=?")
                .rolePrefix("ROLE_")
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().cors().disable();
        http.authorizeRequests().antMatchers(PUBLIC_MATCHERS).permitAll().anyRequest().authenticated();
        http.formLogin().loginPage("/login").defaultSuccessUrl("/chargement").failureUrl("/login?error").permitAll()
                .and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login?logout").deleteCookies("remember-me").permitAll()
                .and().rememberMe();
        /*http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);*/
        http.exceptionHandling().accessDeniedPage("/403");
    }
}
