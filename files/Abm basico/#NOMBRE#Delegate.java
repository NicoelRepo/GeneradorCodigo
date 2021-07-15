package com.coto.gescom.delegate;

import java.util.ArrayList;

import com.coto.gescom.dto.#NOMBRE#DTO;
import com.coto.gescom.dto.ObjetoABM;
import com.coto.gescom.session.interfaces.#NOMBRE#Local;

public class #NOMBRE#Delegate
{
	private #NOMBRE#Local local = null;
	
	public #NOMBRE#Delegate()
	{
		local = (#NOMBRE#Local) HelperBusinessDelegate.getLocalInteface("#NOMBRE#EJB");
	}
	
	public ArrayList<#NOMBRE#DTO> consultarDatos() throws Exception
	{
		return local.consultarDatos();
	}

	public Object guardar(ArrayList<? extends ObjetoABM> datos, Integer ncodmenu, Object[] informacionExtra) throws Exception
	{
		return local.guardar(datos, ncodmenu, informacionExtra);
	}
}

