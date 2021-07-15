package com.coto.gescom.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import com.coto.gescom.dto.CodigoDescripcionDTO;
import com.coto.gescom.dto.#NOMBRE#DTO;
import com.coto.gescom.dto.ObjetoABM;

public class #NOMBRE#Dao extends AbmBaseDao
{
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
			sql.append("	SELECT CIMPUEST,	");
			sql.append("	         DIMPUEST,	");
			sql.append("	         IIMPUEST,	");
			sql.append("	         XPORIMPO,	");
			sql.append("	         CTIPIMPU,	");
			sql.append("	         CTIPAPLI,	");
			sql.append("	         IUMBAPLI,	");
			sql.append("	         CIMPEXCL,	");
			sql.append("	         XCOSTEAR,	");
			sql.append("	         FECHALTA,	");
			sql.append("	         FECHBAJA	");
			sql.append("	    FROM t9690640	");
			sql.append("	ORDER BY CIMPUEST	");

			stmt = connection.createStatement();
			rset = stmt.executeQuery(sql.toString());

			list = new ArrayList<#NOMBRE#DTO>();
			while (rset.next())
			{
				#NOMBRE#DTO dto = new #NOMBRE#DTO();
				dto.setCimpuest(rset.getInt("cimpuest"));
				dto.setDimpuest(rset.getString("dimpuest"));
				dto.setIimpuest(rset.getDouble("iimpuest"));
				dto.setXporimpo(rset.getString("xporimpo"));
				dto.setCtipimpu(rset.getString("ctipimpu"));
				dto.setCtipapli(rset.getString("ctipapli"));
				dto.setIumbapli(rset.getInt("iumbapli"));
				dto.setCimpexcl(rset.getInt("cimpexcl") == 1);
				dto.setXcostear(rset.getInt("xcostear") == 1 ? true : false);
				dto.setFechalta(rset.getString("fechalta"));
				dto.setFechbaja(rset.getString("fechbaja"));

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

	@Override
	public CodigoDescripcionDTO guardarObjeto(Connection connection, ObjetoABM o, Integer ncodmenu, Object[] informacionExtra) throws Exception
	{
		CodigoDescripcionDTO retorno = new CodigoDescripcionDTO();

		PreparedStatement stmtInsert = null;
		StringBuffer sql = new StringBuffer();
		sql.append("	INSERT INTO T9690640 (CIMPUEST,	");
		sql.append("	         DIMPUEST,	");
		sql.append("	         IIMPUEST,	");
		sql.append("	         XPORIMPO,	");
		sql.append("	         CTIPIMPU,	");
		sql.append("	         CTIPAPLI,	");
		sql.append("	         IUMBAPLI,	");
		sql.append("	         CIMPEXCL,	");
		sql.append("	         XCOSTEAR,	");
		sql.append("	         FECHALTA,	");
		sql.append("	         FECHAMOD,	");
		sql.append("	         NHORAMOD,	");
		sql.append("	         FECHBAJA,	");
		sql.append("	         NUSUARIO)	");
		sql.append("	         VALUES ((SELECT MAX(CIMPUEST) + 1 FROM T9690640),	");
		sql.append("	         ?,	"); // DIMPUEST
		sql.append("	         ?,	"); // IIMPUEST
		sql.append("	         ?,	"); // XPORIMPO
		sql.append("	         ?,	"); // CTIPIMPU
		sql.append("	         ?,	"); // CTIPAPLI
		sql.append("	         ?,	"); // IUMBAPLI
		sql.append("	         ?,	"); // CIMPEXCL
		sql.append("	         ?,	"); // XCOSTEAR
		sql.append("	         TO_CHAR(SYSDATE, 'yyyymmdd'),	");
		sql.append("	         '00000000',	");
		sql.append("	         to_char (sysdate, 'SSSSS'),	");
		sql.append("	         '00000000',	");
		sql.append("	         ?)	");

		stmtInsert = connection.prepareStatement(sql.toString());

		PreparedStatement stmtUpdate = null;
		sql = new StringBuffer();
		sql.append("	UPDATE T9690640	");
		sql.append("	   SET DIMPUEST = ?,	");
		sql.append("	       IIMPUEST = ?,	");
		sql.append("	       XPORIMPO = ?,	");
		sql.append("	       CTIPIMPU = ?,	");
		sql.append("	       CTIPAPLI = ?,	");
		sql.append("	       IUMBAPLI = ?,	");
		sql.append("	       CIMPEXCL = ?,	");
		sql.append("	       XCOSTEAR = ?, 	");
		sql.append("		   FECHBAJA = ?    ");
		sql.append("	 WHERE CIMPUEST = ?	");

		stmtUpdate = connection.prepareStatement(sql.toString());

		try
		{
			#NOMBRE#DTO dto = (#NOMBRE#DTO) o;

			if (dto.isAlta())
			{
				int col = 1;
				stmtInsert.setString(col++, dto.getDimpuest());
				stmtInsert.setDouble(col++, dto.getIimpuest());
				stmtInsert.setString(col++, dto.getXporimpo());
				stmtInsert.setString(col++, dto.getCtipimpu());
				stmtInsert.setString(col++, dto.getCtipapli());
				stmtInsert.setInt(col++, dto.getIumbapli());
				stmtInsert.setInt(col++, dto.getCimpexcl() ? 1 : 0);
				stmtInsert.setInt(col++, dto.getXcostear() ? 1 : 0);
				stmtInsert.setString(col++, dto.getNusuario());

				stmtInsert.executeUpdate();
			}
			else if (dto.isModificacion() || dto.isBaja())
			{
				int col = 1;
				stmtUpdate.setString(col++, dto.getDimpuest());
				stmtUpdate.setDouble(col++, dto.getIimpuest());
				stmtUpdate.setString(col++, dto.getXporimpo());
				stmtUpdate.setString(col++, dto.getCtipimpu());
				stmtUpdate.setString(col++, dto.getCtipapli());
				stmtUpdate.setInt(col++, dto.getIumbapli());
				stmtUpdate.setInt(col++, dto.getCimpexcl() ? 1 : 0);
				stmtUpdate.setInt(col++, dto.getXcostear() ? 1 : 0);
				stmtUpdate.setString(col++, dto.getFechbaja());
				stmtUpdate.setInt(col++, dto.getCimpuest());
				
				stmtUpdate.executeUpdate();
			}

			stmtInsert.close();
			stmtUpdate.close();

			retorno.setCodigo(0);
			retorno.setDescripcion("Los datos fueron guardados correctamente");
		}
		catch (Exception e)
		{
			retorno.setCodigo(1);
			retorno.setDescripcion(ErroresDao.obtenerMensajeError(ncodmenu, e));
			e.printStackTrace();
			if (stmtInsert != null)
			{
				stmtInsert.close();
			}/* end if */

			if (stmtUpdate != null)
			{
				stmtUpdate.close();
			}/* end if */
		}
		return (retorno);
	}
}