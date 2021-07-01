package com.coto.gescom.report.#NOMBRE:firstToLowerCase#;

import java.util.ArrayList;

import com.coto.gescom.common.Answer;
import com.coto.gescom.common.Requirement;
import com.coto.gescom.common.ServletConnection;
import com.coto.gescom.common.ServletConnectionConfig;
import com.coto.gescom.dto.#NOMBRE#DTO;


public class DataConnector
{
	private ServletConnection connection = null;
	private ServletConnectionConfig config = null;
	
	public DataConnector(String servletHost, int port, String file)
	{
		this.config = new ServletConnectionConfig("http", servletHost, port, file);
		this.connection = new ServletConnection(config);
	}
	
	
	public ArrayList<#NOMBRE#DTO> obtenerDatos(#NOMBRE#DTO filtros) throws Exception
	{
		ArrayList<#NOMBRE#DTO> ret = null;
		Answer answer = null;
		Requirement requirement = null;
		
		requirement = new Requirement();
		requirement.setClassName("com.coto.gescom.servlet.process.#NOMBRE#Process");
		requirement.setMethodName("obtenerDatos");
		requirement.addParameter(filtros);
		
		answer = this.connection.query(requirement);
		if(answer.isCorrect())
		{	
			ret = (ArrayList<#NOMBRE#DTO>) answer.getAttachedObject();
		}/*end if*/
		return(ret);
	}
}

