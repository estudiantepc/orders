package net.devsv.orders.auth;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
	String nombre;
	String username;
	String correo;
	String direccion;
	String telefono;
	String password;
	Date createAt;
	Boolean enabled;
	

}
