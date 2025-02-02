package com.randre.task_tracker.infrastructure.filters;

import com.randre.task_tracker.utils.SecurityConstants;
import com.randre.task_tracker.repositories.IUserRepository;
import com.randre.task_tracker.services.impl.JwtServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FilterTaskAuth extends OncePerRequestFilter {

    private final JwtServiceImpl jwtService;

    private final IUserRepository userRepository;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        String jwt = this.recoverToken(request);

        if (jwt == null) {
            filterChain.doFilter(request, response);
            return;
        }

        String username = this.jwtService.validateToken(jwt);

        Optional<UserDetails> userO = this.userRepository.findByUsername(username);

        if (userO.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        UserDetails user = userO.get();

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        final String authHeader = request.getHeader(SecurityConstants.AUTH_HEADER);

        if (authHeader == null || !authHeader.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            return null;
        }

        return authHeader.substring(SecurityConstants.TOKEN_PREFIX.length());
    }
}
