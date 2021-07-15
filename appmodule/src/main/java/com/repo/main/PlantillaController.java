package com.repo.main;

import com.repo.entity.CodeFile;
import com.repo.entity.Parameter;
import com.repo.entity.Plantilla;
import com.repo.estrategy.EstrategyGenerateText;
import com.repo.exceptions.DirectorioNoEncontradoException;
import com.repo.logica.GeneradorPrincipal;
import com.repo.main.FileItem.Type;
import com.repo.readersAndWriters.FileReader;
import com.repo.utilAPP.Alertas;
import com.repo.utilAPP.Buttons;
import com.repo.utilAPP.Drag;
import com.repo.utilAPP.UtilTrees;
import com.repo.utilSRV.UtilClone;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class PlantillaController implements Initializable
{
    private Plantilla plantillaOriginal;
    private Plantilla plantillaAModificar;
    private Stage stage;
    private CodeFile archivoActualSeleccionado;

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
    TextFlow textFlowVista;

    @FXML
    TabPane tabPane;
    @FXML
    Tab tabVista;
    
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
        
        treeViewFiles.addEventFilter(KeyEvent.KEY_PRESSED, (keyEvent) ->
        {
            var combinationCollapse = new KeyCodeCombination(KeyCode.DIVIDE, KeyCombination.CONTROL_DOWN);
            var combinationExpand = new KeyCodeCombination(KeyCode.MULTIPLY, KeyCombination.CONTROL_DOWN);
            
            var itemSeleccionado = treeViewFiles.getSelectionModel().getSelectedItem();
            if (combinationExpand.match(keyEvent))
            {
                UtilTrees.expandItemDeep(itemSeleccionado);
                keyEvent.consume();
            }
            else if (combinationCollapse.match(keyEvent))
            {
                UtilTrees.collapseItemDeep(itemSeleccionado);
                treeViewFiles.getSelectionModel().select(itemSeleccionado);
                keyEvent.consume();
            }
        });
    }

    public void cargarPlantilla(Plantilla plantilla)
    {
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
        vincularArbolAVisorDeArchivos();

        textFieldDirectory.setText(plantilla.getDefaultPath());
        
        Drag.makeCanDrag(toolbarPrincipalDER);
        Drag.makeCanDrag(toolbarPrincipalIZQ);
        
        new ThreadVerificadorDeArchivos().start();
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

            if (faltanParametros()) return "Faltan parámetros por colocar";
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return e;
        }

        return true;
    }

    private boolean faltanParametros()
    {
        boolean faltaParametro = false;
        for (EstrategyGenerateText estrategia : listaEstrategias)
        {
            for (Parameter p : estrategia.mapParameters.values())
            {
                if (p.value == null) faltaParametro = true;
            }
        }
        if (faltaParametro)
        {
            return true;
        }
        return false;
    }

    private void completarArbolArchivos()
    {
        var rootItem = FileItem.createRoot().createTreeItem();
        rootItem.setExpanded(false);
        for (CodeFile codeFile : plantillaOriginal.getListCodeFiles())
        {
            UtilTrees.insertarArchivoEnArbol(rootItem, codeFile, (Queue) new LinkedList<>(Arrays.asList(codeFile.getPath().split("/"))));
        }

        UtilTrees.ordenarArbol(rootItem, (elem1, elem2) ->
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

    private  void vincularArbolAVisorDeArchivos()
    {
        treeViewFiles.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
        {
            if (newValue == null) return;
            var seleccionado = newValue.getValue();
            if (seleccionado.type == Type.FILE && seleccionado.codeFile.getText() != null)
            {
                System.out.println("Se actualiza el archivo seleccionado");
                archivoActualSeleccionado = newValue.getValue().codeFile;
            }
        });
        
        treeViewFiles.addEventHandler(MouseEvent.MOUSE_CLICKED, (event) ->
        {
            if (event.getClickCount() == 2 && treeViewFiles.getSelectionModel().getSelectedItem().getValue().type == Type.FILE)
            {
                tabPane.getSelectionModel().select(tabVista);
            }
        });
    }

    class ThreadVerificadorDeArchivos extends Thread
    {
        private final int tiempoDeEspera = 700;

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
                if (archivoActualSeleccionado != null) actualizarFormatoDeVista();
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

        public synchronized boolean verificarArchivos(TreeItem<FileItem> nodo)
        {
            if (nodo.isLeaf())
            {
                String nombreArchivo = nodo.getValue().name;
                var file = new File("../files/" + plantillaOriginal.getName() + "/" + nombreArchivo);
                
                actualizarVista(nodo, file);
                
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

        private void actualizarFormatoDeVista()
        {
            System.out.println("Se actualiza el formato de vista");
            var texto = archivoActualSeleccionado.getText().toString();
            
            List<Text> listaTextos = new ArrayList<>();
            boolean textoEnParametro = texto.charAt(0) == '#';
            Color colorActual = Color.BLACK;
            for (int i = 0; i < texto.length(); i++)
            {
                var charAt = texto.charAt(i);
                if (charAt == '#')
                {
                    if (textoEnParametro)
                    {
                        Text caracter = new Text(charAt + "");
                        caracter.setFill(colorActual);
                        listaTextos.add(caracter);
                        
                        colorActual = Color.BLACK;
                        textoEnParametro = false;
                    }
                    else
                    {
                        colorActual = Color.MEDIUMVIOLETRED;
                        textoEnParametro = true;
                        
                        Text caracter = new Text(charAt + "");
                        caracter.setFill(colorActual);
                        listaTextos.add(caracter);
                    }
                }
                else
                {
                    Text caracter = new Text(charAt + "");
                    caracter.setFill(colorActual);
                    listaTextos.add(caracter);
                }
            }
            var listaTextFlow = textFlowVista.getChildren();
            Platform.runLater(() -> 
            {
                listaTextFlow.clear();
                listaTextFlow.addAll(listaTextos);
            });
        }

        private void actualizarVista(TreeItem<FileItem> nodo, File file)
        {
            var codeFile = nodo.getValue().codeFile;
            
            if (!file.exists())
            {
                codeFile.setText(new StringBuffer("Archivo no encontrado"));
                return;
            }
            
            // Se obtiene el archivo
            StringBuffer texto = null;
            try
            {
                texto = FileReader.readFile(file.getAbsolutePath());
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            
            if (texto != null && codeFile.getText() != null)
            {
                // Si hay cambios
                if (!codeFile.getText().equals(texto))
                {
                    codeFile.setText(texto);
                }
            }
            else if (texto != null && codeFile.getText() == null)
            {
                codeFile.setText(texto);
            }
        }
    }
}