package com.softlond.base.controller;

import com.softlond.base.constant.ApiConstant;
import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.entity.InformacionUsuario;
import com.softlond.base.entity.Prefijo;
import com.softlond.base.entity.PrefijoEgreso;
import com.softlond.base.entity.Usuario;
import com.softlond.base.repository.PrefijoDao;
import com.softlond.base.repository.UsuarioInformacionDao;
import com.softlond.base.service.PrefijoEgresoService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiConstant.PREFIJO_EGRESO_CONTROLADOR_API)
public class PrefijoEgresoController {

    private final Logger logger = Logger.getLogger(getClass());

    @Autowired
    private PrefijoEgresoService prefijoEgresoService;

    @Autowired
    private PrefijoDao prefijoDao;

    @Autowired
    UsuarioInformacionDao usuarioInformacionDao;

    @GetMapping(value = ApiConstant.PREFIJO_EGRESO_CONTROLADOR_API_LISTAR, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity listarPrefijosEgreso() {
        RespuestaDto respuestaDto;
        try {
            List<PrefijoEgreso> prefijos = prefijoEgresoService.obtenerPrefijos();
            respuestaDto = new RespuestaDto(HttpStatus.OK, "Ok");
            respuestaDto.setObjetoRespuesta(prefijos);
            return new ResponseEntity(respuestaDto, HttpStatus.OK);
        } catch (Exception ex) {
            String msj = "Error buscando prefijos de egreso... " + ex.getMessage();
            logger.error(msj);
            respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, msj);
            return new ResponseEntity(respuestaDto, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = ApiConstant.PREFIJO_EGRESO_CONTROLADOR_API_CREAR, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity crearPrefijo(
            @RequestBody PrefijoEgreso body) {
        RespuestaDto respuestaDto;
        try {
            PrefijoEgreso prefijo = prefijoEgresoService.crearPrefijo(body);
            respuestaDto = new RespuestaDto(HttpStatus.OK, "Ok");
            respuestaDto.setObjetoRespuesta(prefijo);
            return new ResponseEntity(respuestaDto, HttpStatus.OK);
        } catch (Exception ex) {
            String msj = "Error creando prefijos de egreso... " + ex.getMessage();
            logger.error(msj);
            respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, msj);
            return new ResponseEntity(respuestaDto, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = ApiConstant.PREFIJO_EGRESO_CONTROLADOR_API_ACTUALIZAR, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity actualizarPrefijo(
            @RequestBody PrefijoEgreso body) {
        RespuestaDto respuestaDto;
        try {
            prefijoEgresoService.actualizarPrefijo(body);
            respuestaDto = new RespuestaDto(HttpStatus.OK, "Ok");
            return new ResponseEntity(respuestaDto, HttpStatus.OK);
        } catch (Exception ex) {
            String msj = "Error actualizando prefijos de egreso... " + ex.getMessage();
            logger.error(msj);
            respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, msj);
            return new ResponseEntity(respuestaDto, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = ApiConstant.PREFIJO_EGRESO_CONTROLADOR_API_LISTAR_POR_TIPO, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity listarPrefijosEgresoPorTipo(
            @RequestParam String tipo) {
        RespuestaDto respuestaDto;
        try {
            List<PrefijoEgreso> prefijos = prefijoEgresoService.obtenerPrefijosPorTipo(tipo);
            respuestaDto = new RespuestaDto(HttpStatus.OK, "Ok");
            respuestaDto.setObjetoRespuesta(prefijos);
            return new ResponseEntity(respuestaDto, HttpStatus.OK);
        } catch (Exception ex) {
            String msj = "Error buscando prefijos de egreso por tipo... " + ex.getMessage();
            logger.error(msj);
            respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, msj);
            return new ResponseEntity(respuestaDto, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = ApiConstant.PREFIJO_EGRESO_CONTROLADOR_API_VALIDAR_VALOR_INICIO, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity obtenerValorPrefijo(Integer idPrefijo) {
        RespuestaDto respuestaDto;
        try {
            boolean estado = prefijoEgresoService.validarInicializacionPrefijo(idPrefijo);
            respuestaDto = new RespuestaDto(HttpStatus.OK, "Ok");
            respuestaDto.setObjetoRespuesta(estado);
            return new ResponseEntity(respuestaDto, HttpStatus.OK);
        } catch (Exception ex) {
            String msj = "Error buscando prefijos de egreso por tipo... " + ex.getMessage();
            logger.error(msj);
            respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, msj);
            return new ResponseEntity(respuestaDto, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = ApiConstant.PREFIJO_EGRESO_CONTROLADOR_API_LISTAR1, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity listarPrefijosEgreso1() {
        RespuestaDto respuestaDto;
        try {
            Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
            Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
            InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
                    .buscarPorIdUsuario(usuarioAutenticado.getId());
            Integer sede = usuarioInformacion.getIdOrganizacion().getId();
            List<Prefijo> prefijos = prefijoDao.obtenerPrefijosEgreso1(sede);
            respuestaDto = new RespuestaDto(HttpStatus.OK, "Ok");
            respuestaDto.setObjetoRespuesta(prefijos);
            return new ResponseEntity(respuestaDto, HttpStatus.OK);
        } catch (Exception ex) {
            String msj = "Error buscando prefijos de egreso... " + ex.getMessage();
            logger.error(msj);
            respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, msj);
            return new ResponseEntity(respuestaDto, HttpStatus.BAD_REQUEST);
        }
    }
}
