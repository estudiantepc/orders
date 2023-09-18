package net.devsv.orders.models.dao;

import java.util.Date;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import net.devsv.orders.models.entities.Orden;

public interface IOrdenDAO extends CrudRepository<Orden, Integer>{
	
	@Query("FROM Orden o WHERE o.estado = 'R' ORDER BY o.fechaOrden DESC")
	List<Orden> findAllRecibidas();

	@Query("FROM Orden o WHERE o.estado = 'D' ORDER BY o.fechaOrden DESC")
	List<Orden> findAllDespachadas();
	
	@Query("FROM Orden o WHERE o.estado = 'A' ORDER BY o.fechaOrden DESC")
	List<Orden> findAllAnuladas();
	
	@Query("FROM Orden o WHERE o.estado = 'R' AND o.fechaOrden BETWEEN :fechaInicio AND :fechaFinal ORDER BY o.fechaOrden DESC")
	List<Orden> findAllRecibidasWithRangoFechas(@Param("fechaInicio") Date fechaIncio, @Param("fechaFinal") Date fechaFinal);
	
	@Query("FROM Orden o WHERE o.estado = 'D' AND o.fechaOrden BETWEEN :fechaInicio AND :fechaFinal ORDER BY o.fechaOrden DESC")
	List<Orden> findAllDespachadasWithRangoFechas(@Param("fechaInicio") Date fechaIncio, @Param("fechaFinal") Date fechaFinal);
	
	@Query("FROM Orden o WHERE o.estado = 'A' AND o.fechaOrden BETWEEN :fechaInicio AND :fechaFinal ORDER BY o.fechaOrden DESC")
	List<Orden> findAllAnuladasWithRangoFechas(@Param("fechaInicio") Date fechaIncio, @Param("fechaFinal") Date fechaFinal);
	
	@Query("SELECT MAX(CAST  (SUBSTRING(o.correlativo,7) as LONG )) FROM Orden o WHERE SUBSTRING  (o.correlativo,1,6) = :yearMonth")
	
	Long findMaxCorrelativoByYearMonth(@Param("yearMonth") String yearMonth);
}


