package com.softlond.base.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.softlond.base.constant.ApiConstant;
import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.entity.InvSector;
import com.softlond.base.service.SectorService;

@Controller
@RequestMapping(ApiConstant.SECTORES_CONTROL_API)
public class SectorController {
    private final Logger logger = Logger.getLogger(getClass());

    @Autowired
    private SectorService sectorService;
    
    @GetMapping(value = ApiConstant.SECTORES_CONTROL_API_LISTAR_TODOS, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<Object> obtenerSectorPorLocal(Integer idLocal, Integer page) {
        RespuestaDto respuestaDto;
        return sectorService.obtenerSectorPorLocal(idLocal, page);
    }
    
    @PostMapping(value = ApiConstant.SECTORES_CONTROL_API_CREAR_SECTOR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> crearSector(@RequestBody InvSector sector) {
		return sectorService.crearSector(sector);
	}
    
    @PutMapping(value = ApiConstant.SECTORES_CONTROL_API_EDITAR_SECTOR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> actualizarSector(@RequestBody InvSector sector) {
		return sectorService.actualizarSector(sector);
	}
    
	//Eliminar Local
	@DeleteMapping(value = ApiConstant.SECTORES_CONTROL_API_ELIMINAR_SECTOR, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> eliminarSector(Integer idSector){
		return this.sectorService.eliminarSector(idSector);
	}
	
	@GetMapping(value = ApiConstant.SECTORES_CONTROL_API_LISTAR_TODOS_CONSULTAS, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> obtenerSectorConsulta(@PathVariable Integer page, Integer local){
		return this.sectorService.obtenerSectorConsulta(page, local);
	}
}
