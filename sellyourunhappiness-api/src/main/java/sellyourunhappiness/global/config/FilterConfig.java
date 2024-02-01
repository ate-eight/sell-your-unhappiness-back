package sellyourunhappiness.global.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

	@Bean
	public FilterRegistrationBean<JwtTokenFilter> jwtTokenFilterRegistrationBean(JwtTokenFilter jwtTokenFilter) {
		FilterRegistrationBean<JwtTokenFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(jwtTokenFilter);
		registrationBean.addUrlPatterns("/api/*");
		return registrationBean;
	}
}
