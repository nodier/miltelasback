package com.softlond.base.service;

import com.softlond.base.entity.ConsecutivoEgresoSede;
import com.softlond.base.entity.Organizacion;
import com.softlond.base.repository.ConsecutivoEgresoSedeDao;
import com.softlond.base.repository.PrefijoEgresoDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConsecutivoEgresoSedeService {

    @Autowired
    private ConsecutivoEgresoSedeDao consecutivoDao;
    
    @Autowired
    private PrefijoEgresoDao prefijoEgresoDao;

    public int siguienteNumero(Organizacion sede, Integer idPrefijo) {
        ConsecutivoEgresoSede consec = consecutivoDao.buscarConsecutivoPorSede(sede.getId());
        Integer numero = prefijoEgresoDao.obtenerPrefijoConValorSede(sede.getId()).orElse(null).getInicio();
        if (consec == null) {
            consec = new ConsecutivoEgresoSede();
            consec.setValorActual(numero);
            consec.setSede(sede);
            consec.setPrefijo(prefijoEgresoDao.findById(idPrefijo).orElse(null));
            consec = consecutivoDao.save(consec);
            consec.setValorActual(consec.getValorActual()+1);
        }
        else if(consec.getValorActual() + 1 < numero) {
        	consec.setValorActual(numero);
        }
        else {
        	consec.setValorActual(consec.getValorActual()+1);
        }
        return consec.getValorActual();
    }
}
