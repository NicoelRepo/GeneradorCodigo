package com.coto.gescom.abm.#NOMBRE:firstToLowerCase#;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import com.coto.gescom.abm.#NOMBRE:firstToLowerCase#.models.ModeloTabla;
import com.coto.gescom.base.LaunchReport;
import com.coto.gescom.base.aplicacionBase.abmBase.PanelAbm.TipoTabla;
import com.coto.gescom.base.aplicacionBase.abmBase.builders.AbmSimpleBuilder;
import com.coto.gescom.base.aplicacionBase.estructuraEditores.EditorFactory;
import com.coto.gescom.base.aplicacionBase.estructuraEditores.EstructuraEditores;
import com.coto.gescom.dto.#NOMBRE#DTO;

public class Pantalla extends JPanel
{
	LaunchReport aplicacion = null;
	DataConnector dataConnector = null;
	
	Abm#NOMBRE# abm = null;
	
	public Pantalla(LaunchReport aplicacion, DataConnector dataconnector)
	{
		this.setLayout(new BorderLayout());
		this.aplicacion = aplicacion;
		this.dataConnector = dataconnector;
		

		ModeloTabla modelo = new ModeloTabla(this.aplicacion.getString("abm.columns"));
		

		EstructuraEditores estructura = new EstructuraEditores(#NOMBRE#DTO.class);
		int col = 1;
		
		

		abm = AbmSimpleBuilder.crearBuilder(new Abm#NOMBRE#(dataconnector))
						.aplicacion(aplicacion)
						.claseDTO(#NOMBRE#DTO.class)
						.modeloAbmBase(modelo)
						.camposParaMuestraDeErrores(" /* Colocar columnas a mostrar en el dialogo de errores */ ")
						.estructuraEditores(estructura)
						.tipoTabla(TipoTabla.JXTable)
						.habilitarCrear()
						.habilitarDarDeBaja()
						.habilitarBuscar()
						.habilitarGuardar()
						.build();
		

		this.add(abm.getPanelAbm(), BorderLayout.CENTER);
		abm.getPanelAbm().getToolBar().cargarBotones("0", "0");
		estructura.mostrarEstructura();
	}
}