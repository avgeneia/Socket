package com.mhms.util;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private DataSource dataSource;
	
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/write", "/write/*").authenticated()
                .antMatchers("/admin", "/admin/*").authenticated()
                .antMatchers("/users", "/users/*").authenticated()
                .anyRequest().permitAll()
            .and()
            	.formLogin()
	                .loginPage("/login")
	                .permitAll()
	                .defaultSuccessUrl("/main")
            .and()
            	.logout()	
            		.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
	            	.logoutSuccessUrl("/login")
	            	.invalidateHttpSession(true)
	                .permitAll();
    }
    @Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
			.jdbcAuthentication()
				.dataSource(dataSource)
				.passwordEncoder(new BCryptPasswordEncoder())
		;
	}
	@Override
	public void configure(WebSecurity web) throws Exception {
		web
			.ignoring()
				.antMatchers("/favicon.ico", "/css/**", "/image/**", "/js/**", "/webjars/**");
	}
}