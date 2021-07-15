package com.coto.gescom.report.#NOMBRE:firstToLowerCase#.models;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import com.coto.gescom.dto.#NOMBRE#DTO;

public class MiModeloTabla#NOMBRE# extends AbstractTableModel
{
	private String[] columns = null;
	private ArrayList<#NOMBRE#DTO> data = null;
	
    public MiModeloTabla(String columns)
    {
    	String[] arrColums = null;
    	arrColums = columns.split(",");
		this.setColumns(arrColums);
		
		this.data = new ArrayList<#NOMBRE#DTO>();
         
     }
    
    public int getRowCount()
	{
		return(this.data.size());
	}

	public int getColumnCount()
	{
		return(this.getColumns().length);
	}

	public Object getValueAt(int row, int col)
	{
		Object object = null;
		try 
		{
			if(this.data != null && row < this.data.size())
			{
				#NOMBRE#DTO dto = this.data.get(row);
				switch(col)
				{
					case 0:
						object = dto.get/* atributos */();
						break;

					/* ... */

					default:
						object = "no def";
						break;
				}/*end switch()*/
			}/*end if*/
			
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return object;
	}
	
	public Class<?> getColumnClass(int c)
	{
		Class<?> ss = null;
		Object o = null;
		o = getValueAt(0, c);
		if(o != null)
		{
			ss = o.getClass();
		}
		else
		{
			ss = String.class;
		}
		return(ss);
	}

	public String getColumnName(int col)
	{
		String columnName = null;
		columnName = this.getColumns()[col];
		return(columnName);
	}

	public void setColumns(String[] columns)
	{
		this.columns = columns;
	}

	public String[] getColumns()
	{
		return columns;
	}

	public int size()
	{
		return(this.data.size());
	}

	public void refresh()
	{
		this.fireTableDataChanged();
	}

	public void setData(ArrayList<#NOMBRE#DTO> datos)
	{
		this.data = datos;
	}
}