package net.devsv.orders.auth;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.devsv.orders.jwt.JwtService;
import net.devsv.orders.models.dao.IUserDAO;
import net.devsv.orders.models.entities.Role;
import net.devsv.orders.models.entities.Usuario;
import org.springframework.security.core.userdetails.UserDetails;

@Service
@RequiredArgsConstructor
public class AuthService {
	
	private final IUserDAO userDAO;
	private final JwtService jwtService;
	private final PasswordEncoder passwordEnconder;
	private final AuthenticationManager authManager;
	
	
	public AuthResponse login(LoginRequest request){
		authManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(),request.getPassword()));
		UserDetails user = userDAO.findByUsername(request.getUsername()).orElseThrow();
		String token = jwtService.getToken(user);
		return AuthResponse.builder()
				.token(token)
				.build();
	}		
	

	public AuthResponse register(RegisterRequest request) {
		Usuario user = Usuario.builder()
				.nombre(request.getNombre())
				.username(request.getUsername())
				.correo(request.getCorreo())
				.direccion(request.getDireccion())
				.telefono(request.getTelefono())
				.password(passwordEnconder.encode(request.getPassword()))
				.createAt(request.getCreateAt())
				.enabled(request.getEnabled())
				.role(Role.USER)
				.build();
		userDAO.save(user);
		return AuthResponse.builder()
				.token(jwtService.getToken(user))
				.build();
	}
}