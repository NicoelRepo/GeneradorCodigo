package com.coto.gescom.report.#NOMBRE:firstToLowerCase#;

import javax.swing.JFrame;

import com.coto.gescom.dto.UsuarioDTO;

public class Launcher
{
	public static void main(String[] args)
	{
		String[] parameters = null;
		parameters = new String[4]; 
		
		parameters[0] = "localhost";
		parameters[1] = "8080";
		parameters[2] = "/gescom/CrudServlet";

		UsuarioDTO usuario = null;
		usuario = new UsuarioDTO();
		usuario.setLegajo(new Integer(85599));
		usuario.setNombre("Diego");
		usuario.setNombre("Lesertesseur");
		usuario.setUsuario("dlesertesseur");
		usuario.setSucursal(0);
		usuario.setIdAplicacion(1186);
		
		#NOMBRE# report = null;
		report = new #NOMBRE#();
		report.setUsuario(usuario);
		report.initialize("report/#NOMBRE:firstToLowerCase#", parameters);
		report.setFrameParent(new JFrame());
		report.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		report.setVisible(true);
	}
}
