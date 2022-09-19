package mono.shop_mono.config.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mono.shop_mono.model.Role;
import mono.shop_mono.model.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserRepository userRepository;
    private final JwtTokenFilter jwtTokenFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // disable login page
        http = http.httpBasic().disable().formLogin().disable();

        // Enable CORS and disable CSRF
        http = http.cors().and().csrf().disable();

        // Set session management to stateless
        http = http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and();

        // Set unauthorized requests exception handler
        http = http
                .exceptionHandling()
                .authenticationEntryPoint(
                        (request, response, ex) -> {
                            log.error("User unauthorized", ex);
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User unauthorized");
                        }
                )
                .and();

        // Set permissions on endpoints
        http.authorizeRequests()
                // public endpoints without any auth
                .mvcMatchers("/user/login").permitAll()
                .mvcMatchers("/user/register").permitAll()
                .mvcMatchers(HttpMethod.GET, "/product").permitAll()
                .mvcMatchers(HttpMethod.GET, "/product/{id}").permitAll()
                .mvcMatchers("/swagger").permitAll()

                // private endpoints for every logged user
                .mvcMatchers(HttpMethod.GET, "/user/{username}").authenticated()
                .mvcMatchers(HttpMethod.POST, "/user/{username}").authenticated()
                .mvcMatchers(HttpMethod.PUT, "/user/{username}").authenticated()
                .mvcMatchers(HttpMethod.DELETE, "/user/{username}").authenticated()
                .mvcMatchers(HttpMethod.GET, "/user/{username}/orders").authenticated()
                .mvcMatchers(HttpMethod.GET, "/order/{id}").authenticated()
                .mvcMatchers(HttpMethod.POST, "/order").authenticated()
                .mvcMatchers(HttpMethod.PATCH, "/order").authenticated()

                // private endpoints only for admin
                .mvcMatchers(HttpMethod.GET, "/user").hasAuthority(Role.ADMIN.getAuthority())
                .mvcMatchers(HttpMethod.DELETE, "/user/{username}").hasAuthority(Role.ADMIN.getAuthority())
                .mvcMatchers(HttpMethod.POST, "/product").hasAuthority(Role.ADMIN.getAuthority())
                .mvcMatchers(HttpMethod.POST, "/product/{id}").hasAuthority(Role.ADMIN.getAuthority())
                .mvcMatchers(HttpMethod.PUT, "/product/{id}").hasAuthority(Role.ADMIN.getAuthority())
                .mvcMatchers(HttpMethod.DELETE, "/product/{id}").hasAuthority(Role.ADMIN.getAuthority())
                .mvcMatchers(HttpMethod.GET, "/order").hasAuthority(Role.ADMIN.getAuthority());

        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(username -> userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User %s not found", username))));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOriginPatterns(List.of("*"));
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}