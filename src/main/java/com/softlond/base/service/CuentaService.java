package com.softlond.base.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.entity.Cuenta;
import com.softlond.base.entity.InformacionUsuario;
import com.softlond.base.entity.Usuario;
import com.softlond.base.repository.CuentaDao;
import com.softlond.base.repository.UsuarioInformacionDao;

@Service
public class CuentaService {

	private static final Logger logger = Logger.getLogger(CuentaService.class);
	
	@Autowired
	CuentaDao cuentaDao;
	
	@Autowired
	UsuarioInformacionDao usuarioInformacionDao;
	
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN')")
	public ResponseEntity<Object> crearCuenta(Cuenta cuenta) {
		
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		
		try {
			if(cuenta.getId()!= null) {
				this.cuentaDao.findById(cuenta.getId());
			}
			this.cuentaDao.save(cuenta);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		}
		catch (Exception e) {
			logger.error("Error se presentan problemas al guardar cuenta " + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error se presentan problemas al guardar la cuenta "+ e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return respuesta;
	}
	
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN')")
	public ResponseEntity<Object> listarTodasCuentas() {
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		
		try {
			List<Cuenta> cuentas = (List<Cuenta>) this.cuentaDao.findAll();
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito");
			respuestaDto.setObjetoRespuesta(cuentas);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		}
		catch (Exception e) {
			logger.error("Error se presentan problemas al guardar cuenta " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error se presentan problemas al guardar la cuenta "+ e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return respuesta;
	}
	
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> listarCuentasNotaDebito() {
		
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		int idSede = usuarioInformacion.getIdOrganizacion().getId();
		try {
			List<Cuenta> cuentas = (List<Cuenta>) this.cuentaDao.obtenerCuentasDebito(idSede);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito");
			respuestaDto.setObjetoRespuesta(cuentas);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		}
		catch (Exception e) {
			logger.error("Error se presentan problemas al guardar cuenta " + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error se presentan problemas al guardar la cuenta "+ e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return respuesta;
	}
	
	
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerCuentasDebitoCliente() {
		
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		int idSede = usuarioInformacion.getIdOrganizacion().getId();
		try {
			List<Cuenta> cuentas = (List<Cuenta>) this.cuentaDao.obtenerCuentasDebitoCliente(idSede);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito");
			respuestaDto.setObjetoRespuesta(cuentas);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		}
		catch (Exception e) {
			logger.error("Error se presentan problemas al guardar cuenta " + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error se presentan problemas al guardar la cuenta "+ e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return respuesta;
	}
	
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> listarCuentasNotaCredito() {
		
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		int idSede = usuarioInformacion.getIdOrganizacion().getId();
		try {
			List<Cuenta> cuentas = (List<Cuenta>) this.cuentaDao.obtenerCuentasCredito(idSede);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito");
			respuestaDto.setObjetoRespuesta(cuentas);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		}
		catch (Exception e) {
			logger.error("Error se presentan problemas al guardar cuenta " + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error se presentan problemas al guardar la cuenta "+ e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return respuesta;
	}
	
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> listarCuentasNotaCreditoClientes() {
		
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		int idSede = usuarioInformacion.getIdOrganizacion().getId();
		try {
			List<Cuenta> cuentas = (List<Cuenta>) this.cuentaDao.obtenerCuentasCreditoClientes(idSede);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito");
			respuestaDto.setObjetoRespuesta(cuentas);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		}
		catch (Exception e) {
			logger.error("Error se presentan problemas al guardar cuenta " + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error se presentan problemas al guardar la cuenta "+ e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return respuesta;
	}
	
	
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> listarCuentasPrecargadasDevolucion() {
		
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		int idSede = usuarioInformacion.getIdOrganizacion().getId();
		try {
			List<Cuenta> cuentas = (List<Cuenta>) this.cuentaDao.obtenerCuentasPrecargadasDevolucion(idSede);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito");
			respuestaDto.setObjetoRespuesta(cuentas);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		}
		catch (Exception e) {
			logger.error("Error se presentan problemas al guardar cuenta " + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error se presentan problemas al guardar la cuenta "+ e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return respuesta;
	}
	
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> listarCuentasPrecargadasDevolucionClientes() {
		
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		int idSede = usuarioInformacion.getIdOrganizacion().getId();
		try {
			List<Cuenta> cuentas = (List<Cuenta>) this.cuentaDao.obtenerCuentasPrecargadasDevolucionClientes(idSede);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito");
			respuestaDto.setObjetoRespuesta(cuentas);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		}
		catch (Exception e) {
			logger.error("Error se presentan problemas al guardar cuenta " + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error se presentan problemas al guardar la cuenta "+ e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return respuesta;
	}
	
	
	public ResponseEntity<Object> listarCuentasBancariasCredito() {
		
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		int idSede = usuarioInformacion.getIdOrganizacion().getId();
		try {
			List<Cuenta> cuentas = (List<Cuenta>) this.cuentaDao.obtenerCuentasBancariasCredito(idSede);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito");
			respuestaDto.setObjetoRespuesta(cuentas);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		}
		catch (Exception e) {
			logger.error("Error al listar cuenta " + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error se presentan problemas al listar la cuenta "+ e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return respuesta;
	}
	
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> listarCuentasNotaCreditoCliente() {
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		int idSede = usuarioInformacion.getIdOrganizacion().getId();
		try {
			List<Cuenta> cuentas = (List<Cuenta>) this.cuentaDao.obtenerCuentasCreditoCliente(idSede);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito");
			respuestaDto.setObjetoRespuesta(cuentas);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		}
		catch (Exception e) {
			logger.error("Error al listar cuenta " + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error se presentan problemas al listar la cuenta "+ e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return respuesta;
	}
	
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> listarCuentasBancariasDebito() {
		
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		int idSede = usuarioInformacion.getIdOrganizacion().getId();
		try {
			List<Cuenta> cuentas = (List<Cuenta>) this.cuentaDao.obtenerCuentasBancariasDebito(idSede);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito");
			respuestaDto.setObjetoRespuesta(cuentas);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		}
		catch (Exception e) {
			logger.error("Error al listar cuenta " + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error se presentan problemas al listar la cuenta "+ e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return respuesta;
	}
	
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> listarCuentasComprobanteGastos() {
		
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		int idSede = usuarioInformacion.getIdOrganizacion().getId();
		try {
			List<Cuenta> cuentas = (List<Cuenta>) this.cuentaDao.obtenerCuentasComprobanteGastos(idSede);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito");
			respuestaDto.setObjetoRespuesta(cuentas);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		}
		catch (Exception e) {
			logger.error("Error al listar cuenta " + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error se presentan problemas al listar la cuenta "+ e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return respuesta;
	}
	
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> listarCuentasComprobanteServicios() {
		
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		int idSede = usuarioInformacion.getIdOrganizacion().getId();
		try {
			List<Cuenta> cuentas = (List<Cuenta>) this.cuentaDao.obtenerCuentasComprobanteServicios(idSede);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito");
			respuestaDto.setObjetoRespuesta(cuentas);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		}
		catch (Exception e) {
			logger.error("Error al listar cuenta " + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error se presentan problemas al listar la cuenta "+ e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return respuesta;
	}
	
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> listarCuentasBancariasEfectivo() {
		
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		int idSede = usuarioInformacion.getIdOrganizacion().getId();
		try {
			List<Cuenta> cuentas = (List<Cuenta>) this.cuentaDao.obtenerCuentasBancariasEfectivo(idSede);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito");
			respuestaDto.setObjetoRespuesta(cuentas);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		}
		catch (Exception e) {
			logger.error("Error al listar cuenta " + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error se presentan problemas al listar la cuenta "+ e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return respuesta;
	}
	
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> listarCuentasBancarias() {
		
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		int idSede = usuarioInformacion.getIdOrganizacion().getId();
		try {
			List<Cuenta> cuentas = (List<Cuenta>) this.cuentaDao.obtenerCuentasBancarias(idSede);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito");
			respuestaDto.setObjetoRespuesta(cuentas);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		}
		catch (Exception e) {
			logger.error("Error se presentan problemas al guardar cuenta " + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error se presentan problemas al guardar la cuenta "+ e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return respuesta;
	}
	
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> listarCuentasProveedores() {
		
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		int idSede = usuarioInformacion.getIdOrganizacion().getId();
		try {
			List<Cuenta> cuentas = (List<Cuenta>) this.cuentaDao.obtenerCuentasProveedores(idSede);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito");
			respuestaDto.setObjetoRespuesta(cuentas);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		}
		catch (Exception e) {
			logger.error("Error se presentan problemas al guardar cuenta " + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error se presentan problemas al guardar la cuenta "+ e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return respuesta;
	}
}


