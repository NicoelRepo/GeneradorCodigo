package com.coto.gescom.abm.#NOMBRE:firstToLowerCase#.models;

import java.text.ParseException;

import com.coto.gescom.base.aplicacionBase.abmBase.models.ModeloAbmBase;
import com.coto.gescom.dto.#NOMBRE#DTO;

public class Modelo#NOMBRE# extends ModeloAbmBase
{
	public Modelo#NOMBRE#(String columns)
	{
		super(columns);
	}

	@Override
	public Object getValueAt(int row, int col)
	{
		Object obj = null;
		if (getData() != null && row < size())
		{
			#NOMBRE#DTO dto = getRow(row);
			switch (col)
			{
				// La primer columna se corresponde con la seleccion de filas
				case 0:
					obj = dto.getSeleccionado();
					break;
					
				/*
					case 1: ...
					obj = ...
					break;
					
					case 2:
					...
					...
				*/

				default:
					obj = "no-def";
					break;
			}
		}
		return obj;
	}
}
