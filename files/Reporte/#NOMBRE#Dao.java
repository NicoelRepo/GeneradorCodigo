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
	
	public ArrayList<#NOMBRE#DTO> obtenerDatos(#NOMBRE#DTO filtros) throws Exception
	{

		StringBuffer sql = null;
		Connection connection = null;
		Statement stmt = null;
		ResultSet rset = null;

		ArrayList<#NOMBRE#DTO> ret = new ArrayList<#NOMBRE#DTO>();
		
		String filtroDepto = filtros.getDeptoFiltro();
		String filtroClase = filtros.getClaseFiltro();
		Integer filtroPlu = filtros.getPluFiltro();
		Integer filtroStock = filtros.getStockFiltro();
		Integer filtroActivos = filtros.getActivosFiltro();
		
		
		try
		{
			connection = this.getConnection();
			
			sql = new StringBuffer();
			
			
				
				ret.add(dto);
				
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
			
			throw(e);
		}

		return(ret);
	}

}