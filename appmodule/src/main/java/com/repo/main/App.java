package com.repo.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import javafx.fxml.Initializable;
import javafx.stage.StageStyle;

public class App extends Application
{
    private static Scene scene;
    private static Stage stage;

    static String PRINCIPAL_MENU = "principalMenu";
    static String PLANTILLA = "plantilla";
        
    @Override
    public void start(Stage stage) throws IOException
    {
        App.stage = stage;
        ((PrincipalMenuController) App.setRoot(App.PRINCIPAL_MENU)).cargarMenu();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }

    static Initializable setRoot(String fxml) throws IOException
    {
        FXMLLoader fxmlLoader = loadFXML(fxml);
        if (scene == null)
        {
            scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
        }
        else
        {
            scene.setRoot(fxmlLoader.load());
        }
        return fxmlLoader.getController();
    }

    public static FXMLLoader loadFXML(String fxml) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader;
    }

    public static void main(String[] args)
    {
        launch();
    }
    
}
