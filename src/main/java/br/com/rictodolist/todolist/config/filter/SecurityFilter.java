package br.com.rictodolist.todolist.config.filter;

import br.com.rictodolist.todolist.config.security.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityFilter {

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Autowired
    private FilterTaskAuth filterTaskAuth;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionMangConfig -> sessionMangConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(this.authenticationProvider)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()

                        .requestMatchers(HttpMethod.GET, "/tasks/").hasAuthority(Permission.READ_ALL_TASKS.name())
                        .requestMatchers(HttpMethod.GET, "/tasks/{id}").hasAuthority(Permission.READ_ONE_TASK.name())
                        .requestMatchers(HttpMethod.POST, "/tasks/").hasAuthority(Permission.CREATE_TASK.name())
                        .requestMatchers(HttpMethod.PUT, "/tasks/").hasAuthority(Permission.UPDATE_TASK.name())
                        .requestMatchers(HttpMethod.DELETE, "/tasks/").hasAuthority(Permission.DELETE_TASK.name())

                        .anyRequest().permitAll()
                )
                .addFilterBefore(this.filterTaskAuth, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
