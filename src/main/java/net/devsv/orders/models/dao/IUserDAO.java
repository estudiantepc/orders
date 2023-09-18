package net.devsv.orders.models.dao;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import net.devsv.orders.models.entities.Usuario;

public interface IUserDAO extends JpaRepository<Usuario,Integer>{
	
	Optional<Usuario>findByUsername(String username);
	

}
