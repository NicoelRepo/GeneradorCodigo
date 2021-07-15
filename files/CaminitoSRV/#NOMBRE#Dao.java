package com.coto.gescom.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import com.coto.gescom.dto.#NOMBRE#DTO;

public class #NOMBRE#Dao extends BaseDao
{	
	public #NOMBRE#Dao()
	{
	}
	
	public ArrayList<#NOMBRE#DTO> consultarDatos() throws Exception
	{
		StringBuffer sql = null;
		ArrayList<#NOMBRE#DTO> list = null;
		Connection connection = null;
		Statement stmt = null;
		ResultSet rset = null;

		try
		{
			connection = this.getConnection();

			sql = new StringBuffer();
			/* Colocar consulta */

			stmt = connection.createStatement();
			rset = stmt.executeQuery(sql.toString());

			list = new ArrayList<#NOMBRE#DTO>();
			while (rset.next())
			{
				#NOMBRE#DTO dto = new #NOMBRE#DTO();
				/* Mapear a dto */

				list.add(dto);
			}/* end while() */

			rset.close();
			stmt.close();
			connection.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			if (rset != null)
			{
				rset.close();
			}/* end if */

			if (stmt != null)
			{
				stmt.close();
			}/* end if */

			if (connection != null)
			{
				connection.close();
			}/* end if */

			throw (e);
		}

		return (list);
	}

	
}