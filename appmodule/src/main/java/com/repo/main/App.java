package com.repo.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.stage.StageStyle;

public class App extends Application
{
    private static Scene scene;

    static String PRINCIPAL_MENU = "principalMenu";
    static String PLANTILLA = "plantilla";
        
    @Override
    public void start(Stage stage) throws IOException
    {
        scene = new Scene(loadFXML(PRINCIPAL_MENU).load(), 640, 480);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
    }

    static Initializable setRoot(String fxml) throws IOException
    {
        FXMLLoader fxmlLoader = loadFXML(fxml);
        scene.setRoot(fxmlLoader.load());
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
