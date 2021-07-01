package com.coto.gescom.session;

import java.util.ArrayList;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import com.coto.gescom.dao.#NOMBRE#Dao;
import com.coto.gescom.dto.#NOMBRE#DTO;
import com.coto.gescom.session.interfaces.#NOMBRE#Local;

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
@Local(#NOMBRE#Local.class)

public class #NOMBRE#EJB implements #NOMBRE#Local
{
	public #NOMBRE#EJB()
	{
	}


	@Override
	public ArrayList<#NOMBRE#DTO> obtenerDatos(#NOMBRE#DTO filtros) throws Exception
	{
		#NOMBRE#Dao dao = null;
		dao = new #NOMBRE#Dao();
		return(dao.obtenerDatos(filtros));
	}

}