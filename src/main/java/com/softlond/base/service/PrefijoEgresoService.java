package com.softlond.base.service;

import com.softlond.base.entity.ConsecutivoEgresoSede;
import com.softlond.base.entity.InformacionUsuario;
import com.softlond.base.entity.PrefijoEgreso;
import com.softlond.base.entity.Usuario;
import com.softlond.base.repository.ConsecutivoEgresoSedeDao;
import com.softlond.base.repository.PrefijoEgresoDao;
import com.softlond.base.repository.UsuarioInformacionDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrefijoEgresoService {

    @Autowired
    private PrefijoEgresoDao prefijoDao;
    @Autowired
    private ConsecutivoEgresoSedeDao consecutivoDao;
    @Autowired
    private DatosSesionService sesionService;
    @Autowired
	UsuarioInformacionDao usuarioInformacionDao;

    public List<PrefijoEgreso> obtenerPrefijos() {
    	Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
        List<PrefijoEgreso> prefijos = prefijoDao.buscarPrefijosEgreso(usuarioInformacion.getIdOrganizacion().getId());
        return prefijos;
    }

    public PrefijoEgreso crearPrefijo(PrefijoEgreso body) throws Exception{
    	Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		body.setIdSede(usuarioInformacion.getIdOrganizacion());
        validarDatos(body);
        body.setCreador(sesionService.obtenerUsuarioSesion());
        PrefijoEgreso prefijoEgreso = prefijoDao.save(body);
        return prefijoEgreso;
    }

    public void actualizarPrefijo(PrefijoEgreso body) throws Exception {
        validarDatos(body);
        prefijoDao.save(body);
    }

    private void validarDatos(PrefijoEgreso body) throws Exception {
        if (body.getPrefijo() == null || body.getPrefijo().length() <= 0) {
            throw new Exception("Debe ingresar prefijo");
        }
    }

    public List<PrefijoEgreso> obtenerPrefijosPorTipo(String tipo) {
    	Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
        boolean tipoProv = tipo.compareTo("Proveedores") == 0;
        boolean tipoGastos = tipo.compareTo("Gastos") == 0;
        boolean tipoNomina = tipo.compareTo("Servicios") == 0;
        List<PrefijoEgreso> prefijos = prefijoDao.buscarPrefijosEgresoPorTipos(
                tipoProv,
                tipoGastos,
                tipoNomina,
                usuarioInformacion.getIdOrganizacion().getId()
        );
        return prefijos;
    }
    
    public boolean validarInicializacionPrefijo(Integer idPrefijo) {
    	boolean existenciaValorPrefijo = false;
    	Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
    	PrefijoEgreso prefijo = prefijoDao.validarExistenciaValorInicialPrefijo(idPrefijo,usuarioInformacion.getIdOrganizacion().getId()).orElse(null);
    	Integer cantidad = prefijoDao.validarExistenciaValorInicialPrefijo(usuarioInformacion.getIdOrganizacion().getId());
    	if(cantidad > 0 && prefijo == null) {
    		existenciaValorPrefijo = true;
    	}
    	return existenciaValorPrefijo;
    }
}
