package com.softlond.base.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.softlond.base.constant.ApiConstant;
import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.entity.Clientes;
import com.softlond.base.entity.FacturaCompra;
import com.softlond.base.entity.NotaDebito;
import com.softlond.base.entity.PrdDescuentos;
import com.softlond.base.entity.Producto;
import com.softlond.base.repository.DescuentoClienteDao;

import com.softlond.base.repository.UsuarioInformacionDao;

@Service
public class DescuentoClienteService {

	private static final Logger logger = Logger.getLogger(DescuentoClienteService.class);

	@Autowired
	UsuarioInformacionDao usuarioInformacionDao;

	@Autowired
	private DescuentoClienteDao descuentoClienteDao;

	@PersistenceContext
	private EntityManager entityManager;

	@PreAuthorize("hasAuthority('USER') or hasAuthority('SUPER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerDescuentoClientes() {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		try {
			ArrayList<Clientes> descuento = this.descuentoClienteDao.obtenerDescuentoClientes();
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de descuentos exitosa");
			respuestaDto.setObjetoRespuesta(descuento);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo descuento " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error obteniendo descuento");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	// Listar todos los descuentos por paginado
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerTodosDescuentosClientes(Integer page) {
		ResponseEntity<Object> respuesta;
		try {
			Pageable pageConfig = PageRequest.of(page, 10);
			Page<Clientes> descuentos = descuentoClienteDao.obtenerTodosDescuentosClientes(pageConfig);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de descuentos exitosa");
			respuestaDto.setObjetoRespuesta(descuentos);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			logger.error("Error al listar los descuentos" + e.getMessage());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"No se pudo obtener los descuentos");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

}
