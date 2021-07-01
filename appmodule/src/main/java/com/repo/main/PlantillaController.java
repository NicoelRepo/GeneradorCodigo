package com.repo.main;

import com.repo.entity.CodeFile;
import com.repo.entity.Parameter;
import com.repo.entity.Plantilla;
import com.repo.estrategy.EstrategyGenerateText;
import com.repo.exceptions.DirectorioNoEncontradoException;
import com.repo.logica.GeneradorPrincipal;
import com.repo.utilAPP.Alertas;
import com.repo.utilAPP.Buttons;
import com.repo.utilAPP.Drag;
import com.repo.utilSRV.UtilClone;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.ResourceBundle;
import java.util.function.Function;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
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
        listViewEstrategias.setItems(listaEstrategias);
        listViewEstrategias.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) ->
        {
            cargarParametros(newValue);
        });
        tableViewParametros.setItems(listaParametros);
    }

    public void cargarPlantilla(Plantilla plantilla)
    {
        this.plantillaOriginal = plantilla;
        this.plantillaAModificar = UtilClone.deepCopy(plantilla);
        listaEstrategias.clear();
        listaEstrategias.addAll(plantillaAModificar.getEstrategysSecuence());
        stage = (Stage) tableViewParametros.getScene().getWindow();
        if (!listaEstrategias.isEmpty())
        {
            listViewEstrategias.getSelectionModel().select(0);
        }
        
        completarArbolArchivos();
        
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
        
        treeViewFiles.setRoot(rootItem);
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

    public static class FileItem
    {   
        private static final Function<Image, Node> createIcono = (img -> new ImageView(img));
        
        private static final Image ROOT_ICON = new Image(FileItem.class.getClassLoader().getResourceAsStream("com/repo/images/root.png"));
        private static final Image FOLDER_ICON = new Image(FileItem.class.getClassLoader().getResourceAsStream("com/repo/images/folder.png"));
        private static final Image FILE_ICON = new Image(FileItem.class.getClassLoader().getResourceAsStream("com/repo/images/file.png"));
        
        public enum Type
        {   
            ROOT, FOLDER, FILE;
        }

        CodeFile codeFile;
        final Type type;
        final String name;
        private Node icon;

        private static final String ROOT_NAME = "Root";
        
        public FileItem(Type type, String name)
        {
            this.type = type;
            this.name = name;
        }

        public static FileItem createFolder(String name)
        {
            return new FileItem(Type.FOLDER, name)
                .icon(createIcono.apply(FOLDER_ICON));
        }
        
        public static FileItem createFile(String name, CodeFile codeFile)
        {
            return new FileItem(Type.FILE, name)
                .icon(createIcono.apply(FILE_ICON))
                .codeFile(codeFile);
                
        }
        
        public static FileItem createRoot()
        {
            return new FileItem(Type.ROOT, ROOT_NAME)
                .icon(createIcono.apply(ROOT_ICON));
        }
        
        public Node getIcon()
        {
            return this.icon;
        }
        
        public TreeItem<FileItem> createTreeItem()
        {
            return new TreeItem<>(this, this.getIcon());
        }
        
        public FileItem codeFile(CodeFile codeFile)
        {
            this.codeFile = codeFile;
            return this;
        }
        
        public FileItem icon(Node icon)
        {
            this.icon = icon;
            return this;
        }

        @Override
        public String toString()
        {
            return name;
        }
    }
    
}
