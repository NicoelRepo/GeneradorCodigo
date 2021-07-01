package com.repo.datos;

import com.repo.entity.CodeFile;
import com.repo.entity.Parameter;
import com.repo.entity.Plantilla;
import com.repo.estrategy.EstrategyGenerateText;
import com.repo.estrategy.SimpleGenerateText;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import com.repo.readersAndWriters.XMLReader;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class SingletonDatos
{
    private static SingletonDatos instance;
    public static final List<Plantilla> listaPlantillas = new ArrayList<>();

    public static synchronized SingletonDatos getInstance()
    {
        if (instance == null)
        {
            instance = new SingletonDatos();
        }
        return instance;
    }

    private SingletonDatos()
    {
        cargarDatosDataBase();
    }

    public static void cargarDatosDataBase()
    {
        listaPlantillas.clear();
        Document document = null;
        try
        {
            document = XMLReader.readXML(new File("../files/database.xml"));
        }
        catch (ParserConfigurationException | IOException | SAXException e)
        {
            e.printStackTrace();
        }

        NodeList listaDePlantillas = document.getElementsByTagName("plantilla");
        for (int i = 0; i < listaDePlantillas.getLength(); i++)
        {
            Node nodoPlantilla = listaDePlantillas.item(i);

            String namePlantilla = null;
            Optional<Node> optionalNode = hijosConCiertoNombre(nodoPlantilla, "name")
                    .findAny();
            if (optionalNode.isPresent())
            {
                namePlantilla = optionalNode.get().getTextContent();
            }
            else
            {
                System.out.println("No hay nombre definido");
            }

            List<EstrategyGenerateText> listaDeExtrategias = new ArrayList<>();
            hijosConCiertoNombre(nodoPlantilla, "estrategy")
                    .forEach(nodeEstrategia -> {
                        EstrategyGenerateText estrategia = null;
                        try
                        {
                            estrategia = (EstrategyGenerateText) Class.forName(SingletonDatos.class.getModule(),
                                    nodeEstrategia.getAttributes().getNamedItem("estrategia").getTextContent()
                            ).getConstructor().newInstance();
                        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e)
                        {
                            e.printStackTrace();
                        }
                        final EstrategyGenerateText estrategiaFinal = estrategia;
                        listaDeExtrategias.add(estrategia);
                        hijosConCiertoNombre(nodeEstrategia, "param")
                                .forEach(nodeParam ->
                                {
                                    String nomberParametro = nodeParam.getTextContent();
                                    estrategiaFinal.getMapParameters().put(nomberParametro, new Parameter(nomberParametro, null, estrategiaFinal));
                                });
                    });

            // Obtener los archivos
            List<CodeFile> listaArchivos = hijosConCiertoNombre(nodoPlantilla, "file")
                    .map(SingletonDatos::crearArchivoUsandoNodo)
                    .collect(Collectors.toList());

            Plantilla plantilla = new Plantilla(listaArchivos, namePlantilla);
            plantilla.getEstrategysSecuence().addAll(listaDeExtrategias);
            listaPlantillas.add(plantilla);
        }
    }

    private static CodeFile crearArchivoUsandoNodo(Node nodoArchivo)
    {
        CodeFile codeFile = new CodeFile();
        hijosConCiertoNombre(nodoArchivo, null)
                .forEach(node -> {
                    String nodeName = node.getNodeName();
                    switch (nodeName)
                    {
                        case "name":
                            codeFile.setName(node.getTextContent());
                            break;

                        case "path":
                            codeFile.setPath(node.getTextContent());
                            break;

                        default:
                            break;
                    }
                });

        return codeFile;
    }

    private static Stream<Node> hijosConCiertoNombre(Node nodo, String nombre)
    {
        NodeList listaNodos = nodo.getChildNodes();
        return IntStream.range(0, listaNodos.getLength())
                .mapToObj(index -> listaNodos.item(index))
                .filter(node -> node.getNodeType() == Node.ELEMENT_NODE && (node.getNodeName().equals(nombre)) || nombre == null);
    }
}
