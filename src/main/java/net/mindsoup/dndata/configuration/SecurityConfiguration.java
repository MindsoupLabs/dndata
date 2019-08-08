package net.mindsoup.dndata.configuration;

import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.stereotype.Component;

/**
 * Created by Valentijn on 8-8-2019
 */
@Component
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
				.authorizeRequests()
				.antMatchers("/ui/**").authenticated().and()
				.formLogin()
				.loginPage("/ui/login.html").permitAll()
				.failureUrl("/ui/login.html?error")
				.usernameParameter("un")
				.passwordParameter("pw")
				.defaultSuccessUrl("/ui/index");
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/static/**","/swagger-ui.html", "/data/**", "/user/**", "/publish/**", "/status/**", "/csrf");
	}
}
