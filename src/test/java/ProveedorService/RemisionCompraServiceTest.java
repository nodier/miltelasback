package ProveedorService;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.softlond.base.entity.RemisionCompra;
import com.softlond.base.repository.RemisionCompraDao;
import com.softlond.base.service.RemisionCompraService;

public class RemisionCompraServiceTest {
/*
	
	@Autowired
	private RemisionCompraDao remisionCompraDao = mock(RemisionCompraDao.class);
	
	
	private RemisionCompraService remisionCompraService = new RemisionCompraService();
	
	@Before
	public void setupListar() {
		List<RemisionCompra> remisiones = new ArrayList<RemisionCompra>();
		for (int i = 0; i < 3; i++) {
			/*RemisionCompra remision = new RemisionCompra();
			remision.setId(i);
			remision.setEstadoDocumento("Asignado");
			remisiones.add(remision);*/
		/*}
		/*RemisionCompra remision = new RemisionCompra();
		remision.setId(i);
		remision.setEstadoDocumento("Asignado");
		remisiones.add(e)*/
		//when(remisionCompraDao.obtenerRemisionesPendientes(98,15)).thenReturn(remisiones);
	//}
	
/*	@Test
	public void listarRemisiones() {
		List<RemisionCompra> remisiones = new ArrayList<RemisionCompra>();
		for (int i = 0; i < 3; i++) {
			RemisionCompra remision = new RemisionCompra();
			remision.setId(i);
			remision.setEstadoDocumento("Pendiente");
			remisiones.add(remision);
		}
		remisionCompraService.setRemisionCompraDao(remisionCompraDao);
		ResponseEntity<Object> respuesta = remisionCompraService.obtenerRemisiones(98);
		assertEquals(remisiones,remisionCompraDao.obtenerRemisionesPendientes(1,15));
		assertEquals(HttpStatus.OK, respuesta.getStatusCode());
	}*/
	
}
