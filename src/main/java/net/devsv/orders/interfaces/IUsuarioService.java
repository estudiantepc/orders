package net.devsv.orders.interfaces;

import net.devsv.orders.models.entities.Usuario;

public interface IUsuarioService {

	public Usuario findByNombre(String userName);
}
