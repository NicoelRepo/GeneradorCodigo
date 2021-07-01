package com.repo.utilAPP;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.ToolBar;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class Drag
{
    public static void makeCanDrag(ToolBar layout)
    {
        final Stage stage = (Stage) layout.getScene().getWindow();
        final Scene scene = layout.getScene();

        final Tupla dragDelta = new Tupla();

        layout.setOnMousePressed(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent mouseEvent)
            {
                // record a delta distance for the drag and drop operation.
                dragDelta.x = stage.getX() - mouseEvent.getScreenX();
                dragDelta.y = stage.getY() - mouseEvent.getScreenY();
                scene.setCursor(Cursor.MOVE);
            }
        });
        layout.setOnMouseReleased(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent mouseEvent)
            {
                scene.setCursor(Cursor.HAND);
            }
        });
        layout.setOnMouseDragged(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent mouseEvent)
            {
                stage.setX(mouseEvent.getScreenX() + dragDelta.x);
                stage.setY(mouseEvent.getScreenY() + dragDelta.y);
            }
        });
        layout.setOnMouseEntered(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent mouseEvent)
            {
                if (!mouseEvent.isPrimaryButtonDown())
                {
                    scene.setCursor(Cursor.HAND);
                }
            }
        });
        layout.setOnMouseExited(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent mouseEvent)
            {
                if (!mouseEvent.isPrimaryButtonDown())
                {
                    scene.setCursor(Cursor.DEFAULT);
                }
            }
        });
    }

    public static class Tupla
    {
        double x, y;
    }
}
