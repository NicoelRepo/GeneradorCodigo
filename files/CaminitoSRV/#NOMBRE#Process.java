package com.coto.gescom.servlet.process;

import java.util.ArrayList;

import com.coto.gescom.delegate.#NOMBRE:firstToLowerCase#Delegate;
import com.coto.gescom.dto.#NOMBRE#DTO;

public class #NOMBRE#Process extends ProcessBase
{
	public #NOMBRE#Process()
	{
	}
	
	public ArrayList<ImpuestoDTO> consultarDatos() throws Exception
	{
		ImpuestosDelegate delegate = new ImpuestosDelegate();
		return delegate.consultarDatos();
	}
}
