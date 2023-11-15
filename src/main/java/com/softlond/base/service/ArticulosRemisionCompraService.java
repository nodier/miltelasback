package com.softlond.base.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.entity.ArticulosRemisionCompra;
import com.softlond.base.entity.FacturaCompra;
import com.softlond.base.repository.ArticulosRemisionCompraDao;

@Service
public class ArticulosRemisionCompraService {

	private static final Logger logger = Logger.getLogger(ArticulosRemisionCompra.class);

	@Autowired
	public ArticulosRemisionCompraDao articulosRemisionCompraDao;

	// Crear
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER')")
	public ResponseEntity<Object> crearArticulosRemision(
			@RequestBody List<ArticulosRemisionCompra> remisionesArticulos) {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		try {
			this.articulosRemisionCompraDao.saveAll(remisionesArticulos);

			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito creando articulos de remision");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);

		} catch (Exception e) {
			logger.error("Error en la creación de articulos remision" + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error en la creación de articulos remisión");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return respuesta;
	}

	@PreAuthorize("hasAuthority('USER') or hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerArticulosRemision(Integer idRemisionCompra) {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		try {

			ArrayList<ArticulosRemisionCompra> articulos = this.articulosRemisionCompraDao
					.findByIdRemisionCommpra(idRemisionCompra);

			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de articulo exitosa");
			respuestaDto.setObjetoRespuesta(articulos);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo articulo " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error obteniendo articulo");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	@PreAuthorize("hasAuthority('USER') or hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerArticulosRemisionPaginado(Integer idRemisionCompra) {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		try {
			List<ArticulosRemisionCompra> articulos = this.articulosRemisionCompraDao
					.findByIdRemisionCompraPageable(idRemisionCompra);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de articulo exitosa");
			respuestaDto.setObjetoRespuesta(articulos);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo articulo " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error obteniendo articulo");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	@PreAuthorize("hasAuthority('USER') or hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerArticulosRemisionPaginadoUpdate(Integer idfacturaCompra) {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		try {
			List<ArticulosRemisionCompra> articulos = this.articulosRemisionCompraDao
					.findByIdRemisionCompraUpdate(idfacturaCompra);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de articulo exitosa");
			respuestaDto.setObjetoRespuesta(articulos);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo articulo " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error obteniendo articulo");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	@PreAuthorize("hasAuthority('USER') or hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerArticulosRemisionProductos(String idProducto, Integer page) {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Pageable pageConfig = PageRequest.of(page, 10);
		try {
			Page<ArticulosRemisionCompra> articulos = this.articulosRemisionCompraDao
					.listarArticulosParaListadoProductoPorProveedor(idProducto, pageConfig);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de articulo exitosa");
			respuestaDto.setObjetoRespuesta(articulos);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo articulo " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error obteniendo articulo");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	public List<ArticulosRemisionCompra> buscarPorIdArticulo(Integer idSede, Integer idArticulo) {
		List<ArticulosRemisionCompra> lista = articulosRemisionCompraDao.buscarPorIdArticulo(idSede, idArticulo);
		return lista;
	}

}
