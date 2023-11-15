package ProveedorService;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.softlond.base.entity.Barrio;
import com.softlond.base.entity.Ciudad;
import com.softlond.base.entity.ClasificacionLegal;
import com.softlond.base.entity.Departamento;
import com.softlond.base.entity.Proveedor;
import com.softlond.base.repository.ProveedorDao;
import com.softlond.base.service.ProveedorService;

public class ProveedorServiceTest {
/*
	
	@Autowired
	private ProveedorDao proveedorDao = mock(ProveedorDao.class);
	
	@Autowired
	private ProveedorService proveedorService = new ProveedorService();
	
	
	
	@Before
	public void setupNull() {
		Proveedor proveedor = null;
		when(proveedorDao.save(proveedor)).thenReturn(null);
	}
	
	@Test
	public void guardarProveedorNull() {
		Proveedor proveedor = null;
		proveedorService.setProveedorDao(proveedorDao);
		ResponseEntity<Object> respuesta = proveedorService.crearProveedor(proveedor);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, respuesta.getStatusCode());
	}
	
	/*@Before
	public void setupProveedorExistente() {
		Proveedor proveedor = new Proveedor();
		proveedor.setActivo(true);
		proveedor.setCiudad(new Ciudad());
		proveedor.setDepto(new Departamento());
		proveedor.setBarrio(new Barrio());
		proveedor.setDireccion("Cra 33 # 78-16");
		proveedor.setClasificacionLegal(new ClasificacionLegal());
		proveedor.setProveedor("TejidosPruebas");
		proveedor.setNit("10101017");
		when(proveedorDao.findByNit(proveedor.getNit())).thenReturn(proveedor);
	}
	
	@Test
	public void guardarProveedorExistente() {
		Proveedor proveedor = new Proveedor();
		proveedor.setActivo(true);
		proveedor.setCiudad(new Ciudad());
		proveedor.setDepto(new Departamento());
		proveedor.setBarrio(new Barrio());
		proveedor.setDireccion("Cra 33 # 78-16");
		proveedor.setClasificacionLegal(new ClasificacionLegal());
		proveedor.setProveedor("TejidosPruebas");
		proveedor.setNit("10101017");
		proveedorService.setProveedorDao(proveedorDao);
		ResponseEntity<Object> respuesta = proveedorService.crearProveedor(proveedor);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, respuesta.getStatusCode());
	}*/
	
	/*@Before
	public void setup() {
		Proveedor proveedor = new Proveedor();
		proveedor.setActivo(true);
		proveedor.setCiudad(new Ciudad());
		proveedor.setDepto(new Departamento());
		proveedor.setBarrio(new Barrio());
		proveedor.setDireccion("Cra 33 # 78-16");
		proveedor.setClasificacionLegal(new ClasificacionLegal());
		proveedor.setProveedor("TejidosPruebas");
		proveedor.setNit("10101018");
		when(proveedorDao.save(proveedor)).thenReturn(proveedor);
	}
	
	@Test
	public void guardarProveedor() {
		Proveedor proveedor = new Proveedor();
		proveedor.setActivo(true);
		proveedor.setCiudad(new Ciudad());
		proveedor.setDepto(new Departamento());
		proveedor.setBarrio(new Barrio());
		proveedor.setDireccion("Cra 33 # 78-16");
		proveedor.setClasificacionLegal(new ClasificacionLegal());
		proveedor.setProveedor("TejidosPruebas");
		proveedor.setNit("10101018");
		proveedorService.setProveedorDao(proveedorDao);
		ResponseEntity<Object> respuesta = proveedorService.crearProveedor(proveedor);
		assertEquals(HttpStatus.OK, respuesta.getStatusCode());
	}*/
	
	
}
