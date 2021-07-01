package com.coto.gescom.delegate;

import java.util.ArrayList;

import com.coto.gescom.dto.#NOMBRE#DTO;
import com.coto.gescom.session.interfaces.#NOMBRE#Local;

public class #NOMBRE#Delegate
{
	private #NOMBRE#Local local = null;
	
	public #NOMBRE#Delegate()
	{
		local = (#NOMBRE#Local) HelperBusinessDelegate.getLocalInteface("#NOMBRE#EJB");
	}
	
	public ArrayList<#NOMBRE#DTO> obtenerDatos(#NOMBRE#DTO filtros) throws Exception
	{
		return(local.obtenerDatos(filtros));
	}
	

}

