package com.repo.main;

import com.repo.entity.Parameter;
import com.repo.entity.Plantilla;
import com.repo.estrategy.EstrategyGenerateText;
import com.repo.exceptions.DirectorioNoEncontradoException;
import com.repo.logica.GeneradorPrincipal;
import com.repo.utilAPP.Alertas;
import com.repo.utilAPP.Buttons;
import com.repo.utilSRV.UtilClone;
import java.io.File;
import java.io.IOException;
import java.net.URL;
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
        if (!listaEstrategias.isEmpty()) listViewEstrategias.getSelectionModel().select(0);
    }

    private void cargarParametros(EstrategyGenerateText estrategia)
    {
        listaParametros.clear();
        tableColumnName.setCellValueFactory(celldata -> new SimpleStringProperty(celldata.getValue().nameParameter));

        tableColumnValue.setCellValueFactory(celldata -> new SimpleStringProperty(celldata.getValue().value));
        tableColumnValue.setCellFactory(TextFieldTableCell.forTableColumn());
        tableColumnValue.setOnEditCommit(e -> ((Parameter) e.getRowValue()).setValue(e.getNewValue()));

        if (estrategia != null) listaParametros.addAll(estrategia.getMapParameters().values());
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
}
