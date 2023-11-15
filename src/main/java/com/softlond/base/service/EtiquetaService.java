package com.softlond.base.service;

import com.softlond.base.entity.Articulo;
import com.softlond.base.entity.InformacionUsuario;
import com.softlond.base.entity.RemisionCompra;
import com.softlond.base.entity.Usuario;
import com.softlond.base.repository.ArticuloDao;
import com.softlond.base.repository.RemisionCompraDao;
import com.softlond.base.repository.UsuarioInformacionDao;
import com.softlond.base.response.Etiqueta;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
public class EtiquetaService {

    private final Logger logger = Logger.getLogger(getClass());

    @Autowired
    private ArticuloDao articuloDao;
    @Autowired
    private RemisionCompraDao remisionDao;

    @Autowired
    UsuarioInformacionDao usuarioInformacionDao;

    public List<Etiqueta> articulosPorRemision(String numeroRemision) {
        // Authentication autenticacion =
        // SecurityContextHolder.getContext().getAuthentication();
        // Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
        // InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
        // .buscarPorIdUsuario(usuarioAutenticado.getId());
        // Integer idSede = usuarioInformacion.getIdOrganizacion().getId();
        if (numeroRemision == null) {
            return null;
        }
        RemisionCompra remision = remisionDao.findByNumeroRemision(numeroRemision);
        // logger.info(remision);
        if (remision == null) {
            logger.info("retorna nulo en la busqueda de la remision");
            return null;
        }
        // List<Articulo> articulos = articuloDao.buscarPorRemision(remision.getId(),
        // idSede);
        List<Articulo> articulos = articuloDao.buscarPorRemision(remision.getId());
        logger.info("encuentra articulos en la remision de compraaa");
        List<Etiqueta> etiquetas = new ArrayList<>();
        for (Articulo a : articulos) {
            etiquetas.add(Etiqueta.articuloRemision(a, remision));
        }
        logger.info("retorna etiquetassss");
        return etiquetas;
    }

    public List<Etiqueta> articulosPorCodigoProducto(String codigo) {
        Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
        InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
                .buscarPorIdUsuario(usuarioAutenticado.getId());
        Integer idSede = usuarioInformacion.getIdOrganizacion().getId();
        if (codigo == null) {
            return null;
        }
        List<Articulo> articulos = articuloDao.buscarPorCodigoProducto(codigo, idSede);
        List<Etiqueta> etiquetas = new ArrayList<>();

        if (articulos.size() == 1) {
            RemisionCompra remision = remisionDao.buscarPorArticulo(articulos.get(0).getId());

            etiquetas.add(Etiqueta.articuloRemision(articulos.get(0), remision));//// --------->

        } else {
            for (Articulo a : articulos) {
                RemisionCompra remision = remisionDao.buscarPorArticulo(a.getId());
                etiquetas.add(Etiqueta.articuloRemision(a, remision));
            }
        }

        return etiquetas;
    }

    public List<Etiqueta> articulosPorSector(Integer sector) {
        Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
        InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
                .buscarPorIdUsuario(usuarioAutenticado.getId());
        Integer idSede = usuarioInformacion.getIdOrganizacion().getId();
        if (sector == null) {
            return null;
        }
        List<Articulo> articulos = articuloDao.buscarPorSector(sector, idSede);
        List<Etiqueta> etiquetas = new ArrayList<>();
        for (Articulo a : articulos) {
            RemisionCompra remision = remisionDao.buscarPorArticulo(a.getId());
            etiquetas.add(Etiqueta.articuloRemision(a, remision));
        }
        return etiquetas;
    }

    public List<Etiqueta> articulosPorCodigoArticulo(String codigo) {
        Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
        InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
                .buscarPorIdUsuario(usuarioAutenticado.getId());
        Integer idSede = usuarioInformacion.getIdOrganizacion().getId();
        List<Etiqueta> etiquetas = new ArrayList<>();
        if (codigo == null) {
            // return etiquetas;
            return null;
        }
        List<Articulo> articulos = articuloDao.buscarPorCodigo(codigo, idSede);

        for (Articulo a : articulos) {
            RemisionCompra remision = remisionDao.buscarPorArticulo(a.getId());
            etiquetas.add(Etiqueta.articuloRemision(a, remision));
        }

        return etiquetas;
    }
}
