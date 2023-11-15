package com.softlond.base.service;

import com.softlond.base.entity.Organizacion;
import com.softlond.base.entity.Usuario;
import com.softlond.base.repository.OrganizacionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class DatosSesionService {

    @Autowired
    private OrganizacionDao organizacionDao;

    public Usuario obtenerUsuarioSesion() {
        Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
        return usuarioAutenticado;
    }

    public Organizacion sedeUsuarioSesion() {
        Usuario user = obtenerUsuarioSesion();
        Organizacion sede = organizacionDao.obtenerSede(user.getId());
        return sede;
    }
}
