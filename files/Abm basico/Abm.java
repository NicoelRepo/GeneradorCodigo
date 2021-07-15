package com.coto.gescom.abm.#NOMBRE:toLo#;

import java.util.ArrayList;

import com.coto.gescom.base.aplicacionBase.abmBase.AbmSimple;
import com.coto.gescom.dto.#NOMBRE#DTO;
import com.coto.gescom.dto.ObjetoABM;

public class Abm extends AbmSimple
{
	DataConnector dataConnector = null;
	
	public Abm(DataConnector dataConnector)
	{
		this.dataConnector = dataConnector;
	}
	
	@Override
	protected ArrayList<? extends ObjetoABM> consultar() throws Exception
	{
		return dataConnector.consultarDatos();
	}

	@Override
	protected Object guardar(ArrayList<? extends ObjetoABM> datos) throws Exception
	{
		return dataConnector.guardar(datos, aplicacion.getUsuario().getIdAplicacion());
	}
	
	@Override
	protected ObjetoABM crearObjetoDefault()
	{
		#NOMBRE#DTO impuestoDTO = new #NOMBRE#DTO();
		/* Aca se insertan todos los valores que se colocan por default al momento del alta */
		
		return impuestoDTO;
	}

}