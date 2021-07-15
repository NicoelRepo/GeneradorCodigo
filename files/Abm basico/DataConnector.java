package com.coto.gescom.abm.#NOMBRE:firstToLowerCase#;

import java.util.ArrayList;

import com.coto.gescom.common.Answer;
import com.coto.gescom.common.Requirement;
import com.coto.gescom.common.ServletConnection;
import com.coto.gescom.common.ServletConnectionConfig;
import com.coto.gescom.dto.#NOMBRE#DTO;
import com.coto.gescom.dto.ObjetoABM;

public class DataConnector
{
	private ServletConnection connection = null;
	private ServletConnectionConfig config = null;

	public DataConnector(String servletHost, int port, String file)
	{
		this.config = new ServletConnectionConfig("http", servletHost, port, file);
		this.connection = new ServletConnection(config);
	}

	public ArrayList<#NOMBRE#DTO> consultarDatos() throws Exception
	{
		ArrayList<#NOMBRE#DTO> ret = null;
		Answer answer = null;
		Requirement requirement = null;

		requirement = new Requirement();

		requirement.setClassName("com.coto.gescom.servlet.process.#NOMBRE#Process");
		requirement.setMethodName("consultarDatos");

		answer = this.connection.query(requirement);

		if (answer.isCorrect())
		{
			ret = (ArrayList<#NOMBRE#DTO>) answer.getAttachedObject();
		}

		return (ret);
	}

	public Object guardar(ArrayList<? extends ObjetoABM> datos, int ncodmenu, Object... informacionExtra) throws Exception
	{
		ArrayList<#NOMBRE#DTO> ret = null;
		Answer answer = null;
		Requirement requirement = null;

		requirement = new Requirement();

		requirement.setClassName("com.coto.gescom.servlet.process.#NOMBRE#Process");
		requirement.setMethodName("guardar");
		requirement.addParameter(datos);
		requirement.addParameter(ncodmenu);
		requirement.addParameter(informacionExtra);

		answer = this.connection.query(requirement);

		if (answer.isCorrect())
		{
			ret = (ArrayList<#NOMBRE#DTO>) answer.getAttachedObject();
		}

		return (ret);
	}
	
	public Object guardar(ArrayList<? extends ObjetoABM> datos, int ncodmenu) throws Exception
	{
		ArrayList<#NOMBRE#DTO> ret = null;
		Answer answer = null;
		Requirement requirement = null;

		requirement = new Requirement();

		requirement.setClassName("com.coto.gescom.servlet.process.#NOMBRE#Process");
		requirement.setMethodName("guardar");
		requirement.addParameter(datos);
		requirement.addParameter(ncodmenu);
		requirement.addParameter(null);

		answer = this.connection.query(requirement);

		if (answer.isCorrect())
		{
			ret = (ArrayList<#NOMBRE#DTO>) answer.getAttachedObject();
		}

		return (ret);
	}
}