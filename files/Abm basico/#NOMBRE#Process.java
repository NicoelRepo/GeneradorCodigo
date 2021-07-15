package com.coto.gescom.servlet.process;

import java.util.ArrayList;

import com.coto.gescom.delegate.#NOMBRE#Delegate;
import com.coto.gescom.dto.#NOMBRE#DTO;
import com.coto.gescom.dto.ObjetoABM;

public class #NOMBRE#Process extends ProcessBase
{

	public ArrayList<#NOMBRE#DTO> consultarDatos() throws Exception
	{
		#NOMBRE#Delegate delegate = new #NOMBRE#Delegate();
		return delegate.consultarDatos();
	}
	
	public Object guardar(ArrayList<? extends ObjetoABM> datos, Integer ncodmenu, Object[] informacionExtra) throws Exception
	{
		#NOMBRE#Delegate delegate = new #NOMBRE#Delegate();
		return delegate.guardar(datos, ncodmenu, informacionExtra);
	}
	
}
