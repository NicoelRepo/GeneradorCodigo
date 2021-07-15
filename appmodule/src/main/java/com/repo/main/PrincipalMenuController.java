package com.repo.main;

import com.repo.datos.SingletonDatos;
import com.repo.entity.Plantilla;
import com.repo.utilAPP.Alertas;
import com.repo.utilAPP.Buttons;
import com.repo.utilAPP.Drag;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToolBar;
import javafx.stage.Stage;

public class PrincipalMenuController implements Initializable
{
    SingletonDatos datos = SingletonDatos.getInstance();

    @FXML
    private Button btnVerPlantilla;
    @FXML
    private TableView<Plantilla> tableViewPlantillas;
    @FXML
    private TableColumn<Plantilla, String> columnName;
    @FXML
    private TableColumn<Plantilla, Integer> columnNumberFiles;
    @FXML
    private TableColumn<Plantilla, String> columnEstrategias;

    @FXML
    private ToolBar toolbarPrincipalDER;
    @FXML
    private ToolBar toolbarPrincipalIZQ;
    
    @FXML
    private void switchToPlantilla() throws IOException
    {
        var plantillaSeleccionada = tableViewPlantillas.getSelectionModel().getSelectedItem();
        if (plantillaSeleccionada != null)
        {
            var plantillaController = (PlantillaController) App.setRoot(App.PLANTILLA);
            plantillaController.cargarPlantilla(plantillaSeleccionada);
        }
        else
        {
            Alertas.mostrarAlertWarning("Debe seleccionar una plantilla");
        }
    }

    @FXML
    private void cerrar()
    {
        Buttons.cerrarVentana((Stage) btnVerPlantilla.getScene().getWindow());
    }

    @FXML
    private void minimizar()
    {
        Buttons.minimizarVentana((Stage) btnVerPlantilla.getScene().getWindow());
    }

    @FXML
    private void maximizarMinimizar()
    {
        Stage stage = (Stage) btnVerPlantilla.getScene().getWindow();
        if (stage.isMaximized())
        {
            stage.setMaximized(false);
        }
        else
        {
            stage.setMaximized(true);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        columnName.setCellValueFactory(celldata -> new SimpleStringProperty(celldata.getValue().getName()));
        columnNumberFiles.setCellValueFactory(celldata -> new SimpleIntegerProperty(celldata.getValue().getListCodeFiles().size()).asObject());
        columnEstrategias.setCellValueFactory(celldata -> new SimpleObjectProperty(celldata.getValue().getEstrategysSecuence()));

        tableViewPlantillas.getItems().addAll(datos.listaPlantillas);
    }
    
    public void cargarMenu()
    {
        Drag.makeCanDrag(toolbarPrincipalDER);
        Drag.makeCanDrag(toolbarPrincipalIZQ);
    }
}