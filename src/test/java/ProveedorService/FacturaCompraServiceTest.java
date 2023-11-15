package ProveedorService;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.softlond.base.entity.FacturaCompra;
import com.softlond.base.entity.FacturaRemision;
import com.softlond.base.entity.InformacionUsuario;
import com.softlond.base.entity.Usuario;
import com.softlond.base.repository.FacturaCompraDao;
import com.softlond.base.repository.UsuarioInformacionDao;
import com.softlond.base.request.FacturaCompraRemisionRequest;
import com.softlond.base.service.FacturaCompraService;

public class FacturaCompraServiceTest {
/*
	
	@Autowired
	FacturaCompraDao facturaCompraDao = mock(FacturaCompraDao.class);
	
	@Autowired
	Authentication autenticacion = mock(Authentication.class);
	
	@Autowired
	SecurityContext security = mock(SecurityContext.class);
	
	@Autowired
	UsuarioInformacionDao usuarioInformacionDao = mock(UsuarioInformacionDao.class);
	
	
	private FacturaCompraService facturaCompraService = new FacturaCompraService();
	
	/*@Before
	public void setupNull() {
		FacturaCompra facturaCompra = null;
		when(security.getAuthentication()).thenReturn(autenticacion);
		SecurityContextHolder.setContext(security);
		Usuario usuario = new Usuario();
		usuario.setId(1);
		InformacionUsuario informacionUsuario = new InformacionUsuario();
		informacionUsuario.setId(1);
		when(usuarioInformacionDao.buscarPorIdUsuario(usuario.getId())).thenReturn(informacionUsuario);
		when(facturaCompraDao.save(facturaCompra)).thenReturn(null);
	}
	
	@Test
	public void guardarProveedorNull() {
		facturaCompraService.setUsuarioInformacionDao(usuarioInformacionDao);
		facturaCompraService.setFacturaCompraDao(facturaCompraDao);
		FacturaCompra facturaCompra = null;
		List<FacturaRemision> facturaRemision = null;
		FacturaCompraRemisionRequest request = new FacturaCompraRemisionRequest(facturaCompra, facturaRemision);
		ResponseEntity<Object> respuesta = facturaCompraService.crearFactura(request);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, respuesta.getStatusCode());
	}*/
	
/*	@Before
	public void setupListar() {
		List<FacturaCompra> facturas = new ArrayList<FacturaCompra>();
		for (int i = 0; i < 3; i++) {
			FacturaCompra factura = new FacturaCompra();
			factura.setId(i);
			facturas.add(factura);
		}
		//when(security.getContext().getAuthentication()).thenCallRealMethod();
		when(facturaCompraDao.findByIdSede(1)).thenReturn(facturas);
	}
	
	@Test
	public void listarFacturas() {
		List<FacturaCompra> facturas = new ArrayList<FacturaCompra>();
		for (int i = 0; i < 3; i++) {
			FacturaCompra factura = new FacturaCompra();
			factura.setId(i);
			facturas.add(factura);
		}
		facturaCompraService.setFacturaCompraDao(facturaCompraDao);
		ResponseEntity<Object> respuesta = facturaCompraService.obtenerNumero(1);	
		assertEquals(HttpStatus.OK, respuesta.getStatusCode());
	}*/
	
	
}
