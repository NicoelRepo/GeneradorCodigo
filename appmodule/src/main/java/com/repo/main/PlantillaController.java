package com.repo.main;

import com.repo.entity.CodeFile;
import com.repo.entity.Parameter;
import com.repo.entity.Plantilla;
import com.repo.estrategy.EstrategyGenerateText;
import com.repo.exceptions.DirectorioNoEncontradoException;
import com.repo.logica.GeneradorPrincipal;
import com.repo.main.FileItem.Type;
import com.repo.utilAPP.Alertas;
import com.repo.utilAPP.Buttons;
import com.repo.utilAPP.Drag;
import com.repo.utilSRV.UtilClone;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.attribute.FileTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class PlantillaController implements Initializable
{
    private Plantilla plantillaOriginal;
    private Plantilla plantillaAModificar;
    private Stage stage;

    @FXML
    ListView<EstrategyGenerateText> listViewEstrategias;

    @FXML
    TableView<Parameter> tableViewParametros;
    @FXML
    TableColumn<Parameter, String> tableColumnName;
    @FXML
    TableColumn<Parameter, String> tableColumnValue;

    @FXML
    TextField textFieldDirectory;

    @FXML
    TreeView<FileItem> treeViewFiles;

    @FXML
    private ToolBar toolbarPrincipalDER;
    @FXML
    private ToolBar toolbarPrincipalIZQ;
    
    final ObservableList<EstrategyGenerateText> listaEstrategias = FXCollections.observableArrayList();
    final ObservableList<Parameter> listaParametros = FXCollections.observableArrayList();

    @FXML
    private void switchToPrincipalMenu() throws IOException
    {
        plantillaOriginal = null;
        plantillaAModificar = null;
        listaEstrategias.clear();
        listaParametros.clear();
        textFieldDirectory.setText(null);
        App.setRoot(App.PRINCIPAL_MENU);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        System.out.println("PlantillaController.initialize()");
        listViewEstrategias.setItems(listaEstrategias);
        listViewEstrategias.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) ->
        {
            cargarParametros(newValue);
        });
        tableViewParametros.setItems(listaParametros);
    }

    public void cargarPlantilla(Plantilla plantilla)
    {
        System.out.println("PlantillaController.cargarPlantilla()");
        this.plantillaOriginal = plantilla;
        this.plantillaAModificar = UtilClone.deepCopy(plantilla);
        listaEstrategias.clear();
        listaEstrategias.addAll(plantillaAModificar.getEstrategysSecuence());
        var scene = toolbarPrincipalDER.getScene();
        System.out.println(scene);
        stage = (Stage) scene.getWindow();
        if (!listaEstrategias.isEmpty())
        {
            listViewEstrategias.getSelectionModel().select(0);
        }
        
        completarArbolArchivos();
        new ThreadVerificadorDeArchivos().start();

        Drag.makeCanDrag(toolbarPrincipalDER);
        Drag.makeCanDrag(toolbarPrincipalIZQ);
    }

    private void cargarParametros(EstrategyGenerateText estrategia)
    {
        listaParametros.clear();
        tableColumnName.setCellValueFactory(celldata -> new SimpleStringProperty(celldata.getValue().nameParameter));

        tableColumnValue.setCellValueFactory(celldata -> new SimpleStringProperty(celldata.getValue().value));
        tableColumnValue.setCellFactory(TextFieldTableCell.forTableColumn());
        tableColumnValue.setOnEditCommit(e -> ((Parameter) e.getRowValue()).setValue(e.getNewValue()));

        if (estrategia != null)
        {
            listaParametros.addAll(estrategia.getMapParameters().values());
        }
    }

    @FXML
    private void cerrar()
    {
        Buttons.cerrarVentana(stage);
    }

    @FXML
    private void minimizar()
    {
        Buttons.minimizarVentana(stage);
    }

    @FXML
    private void maximizarMinimizar()
    {
        Stage stage = (Stage) toolbarPrincipalDER.getScene().getWindow();
        if (stage.isMaximized())
        {
            stage.setMaximized(false);
        }
        else
        {
            stage.setMaximized(true);
        }
    }

    @FXML
    private void ejecutarPlantilla()
    {
        try
        {
            Object retValidaciones = validarDatosDePlantilla();
            if (retValidaciones instanceof Exception)
            {
                Alertas.mostrarAlertError(((Exception) retValidaciones).getMessage());
                return;
            }
            if (retValidaciones instanceof String)
            {
                Alertas.mostrarAlertError((String) retValidaciones);
                return;
            }

            GeneradorPrincipal.generarUsandoPlantilla(plantillaAModificar, textFieldDirectory.getText());

            cargarPlantilla(plantillaOriginal);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Alertas.mostrarAlertError("Ha ocurrido un error: " + e.getMessage());
        }
    }

    @FXML
    private void elegirDirectorio(ActionEvent event)
    {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File dir = directoryChooser.showDialog(stage);
        if (dir != null)
        {
            textFieldDirectory.setText(dir.getAbsolutePath());
        }
    }

    private Object validarDatosDePlantilla()
    {
        try
        {
            // Valida que el directorio destino exista
            File dir = new File(textFieldDirectory.getText());
            if (!dir.exists())
            {
                return new DirectorioNoEncontradoException(dir.getAbsolutePath());
            }

            boolean faltaParametro = false;
            for (int i = 0; i < listaParametros.size(); i++)
            {
                if (listaParametros.get(i).value == null) faltaParametro = true;
            }
            if (faltaParametro)
            {
                return "Faltan parámetros por colocar";
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return e;
        }

        return true;
    }

    private void completarArbolArchivos()
    {
        var rootItem = FileItem.createRoot().createTreeItem();
        rootItem.setExpanded(false);
        for (CodeFile codeFile : plantillaOriginal.getListCodeFiles())
        {
            insertarArchivoEnArbol(rootItem, codeFile, (Queue) new LinkedList<>(Arrays.asList(codeFile.getPath().split("/"))));
        }

        ordenarArbol(rootItem, (elem1, elem2) ->
        {
            if (elem1.type == elem2.type)
            {
                return elem1.name.compareTo(elem2.name);
            }
            else
            {
                return elem1.type == Type.FOLDER ? -1 : 1;
            }
        });
        treeViewFiles.setRoot(rootItem);
    }
    
    private static void ordenarArbol(TreeItem<FileItem> nodoPadre, final Comparator<FileItem> comparator)
    {
        if (!nodoPadre.isLeaf())
        {
            nodoPadre.getChildren().sort((titem1, titem2) -> comparator.compare(titem1.getValue(), titem2.getValue()));
            for (TreeItem<FileItem> titem : nodoPadre.getChildren())
            {
                ordenarArbol(titem, comparator);
            }
        }
    }

    private void insertarArchivoEnArbol(TreeItem<FileItem> nodoPadre, CodeFile codeFile, Queue<String> colaDirectorios)
    {
        if (colaDirectorios.isEmpty())
            // Si no quedan carpetas padres por recorrer, se crea el archivo en nodoPadre
        {
            nodoPadre.getChildren().add(FileItem.createFile(codeFile.getName(), codeFile).createTreeItem());
        }
        else
        {
            // Si aun quedan carpetas por recorrer se extrae la siguiente carpeta.
            String dir = colaDirectorios.poll();
            
            // Se busca, por nombre, la siguiente carpeta en nodoPadre
            var folderOptional = nodoPadre.getChildren().stream()
                .filter((titem -> titem.getValue().type == FileItem.Type.FOLDER || titem.getValue().type == FileItem.Type.ROOT))
                .filter(titem -> titem.getValue().name.equals(dir))
                .findAny();
            
            if (folderOptional.isPresent())
                // Si la carpeta ya existe se llama recursivamente sobre esta
            {
                insertarArchivoEnArbol(folderOptional.get(), codeFile, colaDirectorios);
            }
            else
            {
                // Si la carpeta aún no existe, se crea y se llama recursivamente sobre esta
                var newFolder = FileItem.createFolder(dir).createTreeItem();
                nodoPadre.getChildren().add(newFolder);
                insertarArchivoEnArbol(newFolder, codeFile, colaDirectorios);
            }
        }
    }
 
    class ThreadVerificadorDeArchivos extends Thread
    {
        private final int tiempoDeEspera = 1500;

        public ThreadVerificadorDeArchivos()
        {
            this.setDaemon(true);
        }

        @Override
        public void run()
        {
            while (plantillaOriginal != null)
            {
                var root = treeViewFiles.getRoot();
                verificarArchivos(root);
                try
                {
                    Thread.sleep(tiempoDeEspera);
                }
                catch (Exception e)
                {
                    e.printStackTrace();    
                }
            }
        }

        public boolean verificarArchivos(TreeItem<FileItem> nodo)
        {
            if (nodo.isLeaf())
            {
                String nombreArchivo = nodo.getValue().name;
                var file = new File("../files/" + plantillaOriginal.getName() + "/" + nombreArchivo);
                
                if (file.exists())
                {
                    nodo.getValue().quitarSubrayado();
                    return false;
                }
                else
                {
                    nodo.getValue().subrayarEnRojo();
                    return true;
                }
            }
            else
            {
                boolean hayArchivoInexistente = false;
                for (TreeItem<FileItem> hijo : nodo.getChildren())
                {
                    if (verificarArchivos(hijo))
                    {
                        hayArchivoInexistente = true;
                    }
                }
                if (hayArchivoInexistente)
                {
                    nodo.getValue().subrayarEnRojo();
                }
                else
                {
                    nodo.getValue().quitarSubrayado();
                }
                return hayArchivoInexistente;
            }
        }
    }
}