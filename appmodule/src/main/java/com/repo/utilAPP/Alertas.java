package com.repo.utilAPP;


import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class Alertas
{
   public static void mostrarAlertError(String message) {
       Alert alert = new Alert(Alert.AlertType.ERROR);
       alert.setHeaderText(null);
       alert.setTitle("Error");
       alert.setContentText(message);
       alert.showAndWait();
   }

   public static void mostrarAlertInfo(String message) {
       Alert alert = new Alert(Alert.AlertType.INFORMATION);
       alert.setHeaderText(null);
       alert.setTitle("Informativo");
       alert.setContentText(message);
       alert.showAndWait();
   }

   public static void mostrarAlertWarning(String message) {
       Alert alert = new Alert(Alert.AlertType.WARNING);
       alert.setHeaderText(null);
       alert.setTitle("Advertencia");
       alert.setContentText(message);
       alert.showAndWait();
   }

   public static boolean mostrarAlertConfirmation(String message) {
       Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
       alert.setHeaderText(null);
       alert.setTitle("Confirmacion");
       alert.setContentText(message);
       alert.showAndWait();
       
       return alert.getResult() == ButtonType.YES;
   }

   public static void mostrarAlertCabecera(String message, String cabecera) {
       Alert alert = new Alert(Alert.AlertType.INFORMATION);
       alert.setHeaderText(cabecera);
       alert.setTitle("Informativo");
       alert.setContentText(message);
       alert.showAndWait();
   }   
}
