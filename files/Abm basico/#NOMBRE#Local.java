package com.coto.gescom.session.interfaces;

import java.util.ArrayList;

import com.coto.gescom.dto.ImpuestoDTO;
import com.coto.gescom.dto.ObjetoABM;

public interface ImpuestosLocal 
{
	public ArrayList<ImpuestoDTO> consultarDatos() throws Exception;

	public Object guardar(ArrayList<? extends ObjetoABM> datos, Integer ncodmenu, Object[] infomacionExtra) throws Exception;
}
