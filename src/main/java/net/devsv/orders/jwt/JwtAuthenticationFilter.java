package net.devsv.orders.jwt;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter{

	private final JwtService jwtService;
	private final UserDetailsService userDetailsService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		//obteniendo el token del request
		final String token = getTokenFromRequest(request);
		final String username;
		if(token == null) {
			filterChain.doFilter(request,response);
			return;
		}
		//obtenemos el username del token
		username = jwtService.getUsernameFromToken(token);
		if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			//lo buscamos en la db
			UserDetails userDetails = userDetailsService.loadUserByUsername(username);
			//verificando si el token es valido, para ello creamos el metodo isTokenValid
			if(jwtService.isTokenValid(token,userDetails)) {
				//actualizar el SecurityContextHolder
				UsernamePasswordAuthenticationToken authToken =
						new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
				//seteamos el details
				authToken.setDetails(new 
						WebAuthenticationDetailsSource().buildDetails(request));
				//obtenemos el contexto
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}
		}
		filterChain.doFilter(request, response);		
	}

	private String getTokenFromRequest(HttpServletRequest request) {
		final String authHeader = 
				request.getHeader(HttpHeaders.AUTHORIZATION);
		if(StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
			return authHeader.substring(7);
		}
		return null;
	}

}