package com.coto.gescom.dto;

import java.io.Serializable;

public class #NOMBRE#DTO extends ObjetoABM
{
	
	@Override
	public Boolean estaActivo()
	{
		return fechbaja.equals("00000000");
	}

	@Override
	public void darDeBaja()
	{
		try
		{
			fechbaja = UtilidadesDeFechas.FechaActualSinBarras();
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
	}
}

