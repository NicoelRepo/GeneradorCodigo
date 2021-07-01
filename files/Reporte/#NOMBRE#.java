package com.coto.gescom.report.#NOMBRE:firstToLowerCase#;

import java.awt.Component;
import com.coto.gescom.base.LaunchReport;
import com.coto.gescom.resource.ResourceLoader;

public class #NOMBRE# extends LaunchReport
{
	
	protected Component initializeContent(String[] parameters)
	{
		DataConnector dataConnector = new DataConnector(parameters[0],Integer.valueOf(parameters[1]).intValue(),parameters[2]);
		this.setParamenters(parameters); 
		Pantalla pantalla = new Pantalla(this, dataConnector);
		return pantalla;
	}

	public void close()
	{}
}



