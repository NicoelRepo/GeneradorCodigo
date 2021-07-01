package com.repo.logica;

import com.repo.entity.Plantilla;
import com.repo.estrategy.EstrategyGenerateText;
import com.repo.readersAndWriters.FileReader;
import com.repo.readersAndWriters.FileWriter;
import com.repo.entity.CodeFile;

import java.io.File;
import java.io.IOException;

public class GeneradorPrincipal
{
    public static void generarUsandoPlantilla(Plantilla plantilla, String path)
    {
        for (CodeFile codeFile : plantilla.getListCodeFiles())
        {
            // Se busca el archivo de la plantilla
            StringBuffer texto = null;
            try
            {
                texto = FileReader.readFile("../files/" + plantilla.getName() + "/" + codeFile.getName());
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            codeFile.setText(texto);

            // Se aplican las estrategias al codefile
            for (EstrategyGenerateText estrategy : plantilla.getEstrategysSecuence())
            {
                estrategy.generate(codeFile);
            }

            // Se obtiene la ruta donde se guardará el archivo
            File directorioDondeGuardar = new File(path + "\\" + codeFile.getPath());
            directorioDondeGuardar.mkdirs();

            // Se genera el archivo en la ruta especificada. En este punto codeFile.getText() ya esta listo
            try
            {
                FileWriter.createFile(path + "\\" + codeFile.getPath() + "\\" + codeFile.getName(), codeFile.getText());
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

        }
    }

    public static String validarGeneracion(Plantilla plantilla, String path)
    {
        try
        {
            for (CodeFile codeFile : plantilla.getListCodeFiles())
            {
                // Se busca el archivo de la plantilla
                StringBuffer texto = null;
                try
                {
                    texto = FileReader.readFile("files\\" + plantilla.getName() + "\\" + codeFile.getName());
                } catch (IOException e)
                {
                    e.printStackTrace();
                    return e.getMessage() + ": El archivo " + codeFile.getName() + " de la plantilla " + plantilla.getName() + " no fue encontrado.";
                }
                codeFile.setText(texto);

                // Se aplican las estrategias al codefile
                for (EstrategyGenerateText estrategy : plantilla.getEstrategysSecuence())
                {
                    estrategy.generate(codeFile);
                }

                // Se obtiene la ruta donde se guardará el archivo
                File directorioDondeGuardar = new File(path);
                System.out.println(directorioDondeGuardar);
            }
        }
        catch (Exception e)
        {
            return e.getMessage();
        }
        return null;
    }
}
