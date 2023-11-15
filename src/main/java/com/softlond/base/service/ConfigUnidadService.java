package com.softlond.base.service;

import com.softlond.base.entity.ConfigUnidades;
import com.softlond.base.repository.ConfigUnidadesDao;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConfigUnidadService {

    private final Logger logger = Logger.getLogger(getClass());

    @Autowired
    private ConfigUnidadesDao unidadDao;

    public List<ConfigUnidades> listarTodos() throws Exception {
        List<ConfigUnidades> unidades = (List<ConfigUnidades>) unidadDao.findAll();
        return unidades;
    }

    public ConfigUnidades crearUnidad(ConfigUnidades body) throws Exception {
        // TODO: validar body
        ConfigUnidades nuevaUnidad = unidadDao.save(body);
        return nuevaUnidad;
    }

    public void editarUnidad(ConfigUnidades body) throws Exception {
        // TODO: validar body
        unidadDao.save(body);
    }

    public void eliminarUnidad(Integer idUnidad) throws Exception {
        ConfigUnidades unidad = unidadDao.findById(idUnidad).get();
        if (unidad == null) {
            throw new Exception("No existe unidad con este ID");
        }
        unidadDao.delete(unidad);
    }
}
