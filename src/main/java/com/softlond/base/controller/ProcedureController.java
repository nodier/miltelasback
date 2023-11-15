package com.softlond.base.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.softlond.base.repository.PeriodosContablesDao;

@Configuration
@EnableScheduling
public class ProcedureController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	PeriodosContablesDao periodoContableDao;

	/*
	 * Ejecución del procedimiento almacenado de la base de datos
	 * que permite crear los períodos contables del nuevo mes
	 * 
	 * hecho por AlejaB - Softlond
	 */

	/*
	 * EXPLICACION
	 * segundos(0-59) - minutos(0-59) - horas(0-23) - diaMes(1-31) - mes(1-12) -
	 * diaSemana(0-6)
	 * 
	 * - ? -> no definido
	 * - * -> todos
	 * - / -> incremento: Ejm: en min poner 0/15 significa que se ejecuta cada 15
	 * min
	 * - , -> conjunto: Ejm: en dia semana poner 6,7 significa que se ejecuta
	 * sabados y domingos
	 */

	// Se ejecuta a las 12:30 AM de los 1 de cada mes
	@Scheduled(cron = "0 30 00 1 * *")
	public void callProcedures() {
		logger.info("Realizando llamado a procedimiento almacenado para crear los siguientes períodos contables");
		this.callProcedure();
	}

	private void callProcedure() {
		try {
			this.periodoContableDao.procedurePeriodoContable();
			logger.info("Termina la ejecución del procedimiento para crear los períodos contables del nuevo mes");
		} catch (Exception e) {
			logger.error("Error ejecutando procedimiento almacenado para crear los períodos contables: " + e);
		}
	}

}
