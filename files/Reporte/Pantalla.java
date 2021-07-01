package com.coto.gescom.report.#NOMBRE:firstToLowerCase#;

public class Pantalla extends JPanel
{
	private LaunchReport aplicacion = null;
	DataConnector dataConnector = null;
	PanelTablaDeDatos panelTablaDatos = null;
	MiModeloTabla modelo = null;
	private PanelFiltros panelFiltros = null;	
	private PanelToolbarMenu toolbar = null;
	
	public Pantalla(LaunchReport aplicacion,DataConnector dataConnector)
	{	
		this.aplicacion=aplicacion;
		dataConnector=dataConnector;
		setLayout(new BorderLayout(0, 0));
		
		panelDeFiltros = new PanelFiltros(this.aplicacion.getUsuario(),this.aplicacion.getParamenters());
		add(panelDeFiltros, BorderLayout.NORTH);
		
		panelDeFiltros.btnBuscar.addActionListener(new ActionListener()
        {
              public void actionPerformed(ActionEvent arg0)
              {
            	  
            	  new Thread( new Runnable()
			        {
			            public void run()
			            {
			            	abrirGlassPanel("Obteniendo datos");
			                FiltrosDTO dto=new FiltrosDTO();
			                dto=panelDeFiltros.colectarDatosFiltros();
			                
					/* Recuperar los datos de FiltrosDTO */
			                
			                cargarGrilla(/* pasar los filtros como parametro */);
			                
			                cerrarGlassPanel();
			                
					/* Cargar los botones de la toolbar con toolbar.cargarBotones() */
			            }
			        }).start();
                  
              }
        });
		
		
	    JPanel panelDatos = new JPanel();
		add(panelDatos, BorderLayout.CENTER);
		panelDatos.setLayout(new BorderLayout(0, 0));

		modelo=new MiModeloTabla(this.aplicacion.getString("report.columns"));
		panelTablaDatos = new PanelTablaDeDatos(modelo);
		panelDatos.add(panelTablaDatos, BorderLayout.CENTER);
		toolbar = new PanelToolbarMenu(aplicacion.getUsuario(), aplicacion.getParamenters());
		panelDatos.add(toolbar, BorderLayout.NORTH);
		toolbar.btnExportar.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
		{
			ExportarXls(panelTablaDatos.tablaDatos);
		}
		});
		
	}
	
	public void cargarGrilla(/* filtros */)
	{
		
		ArrayList<#NOMBRE#DTO> datos = null;
		try
		{	 
			datos = dataConnector.obtenerDatos(/* filtros */);

	        if (datos == null || datos.size() == 0)
	        {
	        	Alerta.mostrarMensajeAdvertencia("No se recuperaron registros para los filtros seleccionados.");
			}
	        	miModelo.setData(datos);
	        	miModelo.refresh();
		}
		catch (Exception e)
		{
	        e.printStackTrace();
		}          
	}
	

	public void ExportarXls (JTable tabla)
	{
		final String extensionXLS = "xls";
		final String extensionXLSX = "xlsx";
		final String extensionODS = "ods";
		try
		{
			if (tabla.getRowCount() > 0)
			{
				JFileChooser chooser = new JFileChooser();
				if (tabla.getRowCount() < ExportTable.MAX_NUMERO_FILAS_CON_XLS)
				{
					FileNameExtensionFilter filterXLS = new FileNameExtensionFilter("Archivos de Excel .xls", extensionXLS);
					chooser.setFileFilter(filterXLS);
					FileNameExtensionFilter filterODS = new FileNameExtensionFilter("Archivos de OpenOffice", extensionODS);
					chooser.setFileFilter(filterODS);
				}
				else
				{
					FileNameExtensionFilter filterSinExtension = new FileNameExtensionFilter("Archivo sin Extensión", "no def");
					chooser.setFileFilter(filterSinExtension);
				}
				FileNameExtensionFilter filterXLSX = new FileNameExtensionFilter("Archivos de Excel .xlsx", extensionXLSX);
				chooser.setFileFilter(filterXLSX);
				chooser.setDialogTitle("Guardar archivo");
				chooser.setAcceptAllFileFilterUsed(false);

				if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION)
				{
					ArrayList<JTable> tb = new ArrayList<JTable>();
					ArrayList<String> nom = new ArrayList<String>();
					tb.add(tabla);
					tb.add(/* titulo a mostrar en el excel */);

					ArrayList<String> param = new ArrayList<String>();
					/* Se agregan los filtros usados */

					String extensionSeleccionada = ((FileNameExtensionFilter) chooser.getFileFilter()).getExtensions()[0];
					String nombreArchivo = chooser.getSelectedFile().toString();
					String nombreArchivoConExtension = nombreArchivo + "." + extensionSeleccionada;

					boolean exportadoOk = false;
					if (extensionSeleccionada.equals(extensionXLS))
					{
						exportadoOk = ExportTable.exportExcelXLS(nombreArchivoConExtension, tb, nom);
					}
					else if (extensionSeleccionada.equals(extensionXLSX))
					{
						exportadoOk = ExportTable.exportExcelXLSX(nombreArchivoConExtension, tb, nom);
					}
					else if (extensionSeleccionada.equals(extensionODS))
					{
						exportadoOk = ExportTable.exportODS(nombreArchivoConExtension, tb, nom);
					}
					else if (extensionSeleccionada.equals("no def"))
					{
						exportadoOk = ExportTable.exportarSinExtension(nombreArchivo, tb, nom);
					}

					if (exportadoOk)
					{
						Alerta.mostrarMensajeInformativo("Los datos fueron exportados en el directorio seleccionado");
					}
					else
					{
						Alerta.mostrarMensaje("No hay datos para exportar o ha ocurrido un error");
					}
				}
			}

		}
		catch (OutOfMemoryError e)
		{
			Alerta.mostrarMensajeError("Ha ocurrido un error: " + e);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			Alerta.mostrarMensaje("Ocurrio un error al intentar exportar el archivo"
					+ e.getMessage());
		}


	}


	public void abrirGlassPanel(String texto)
	{
		this.aplicacion.setActivateWorking();
	
	}
	
	public void cerrarGlassPanel()
	{
		this.aplicacion.setWorking(false);
	}

	
}
