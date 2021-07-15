package com.repo.main;

import java.util.function.Function;

import com.repo.entity.CodeFile;

import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;

public class FileItem extends Label
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
    public final Type type;
    public final String name;
    private Node icon;

    private static final String ROOT_NAME = "Root";
    
    public FileItem(Type type, String name)
    {
        this.type = type;
        this.name = name;
        setText(name);
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
    
    public void subrayarEnRojo()
    {
        //this.setStyle("-fx-border-color: red;" +
        //"-fx-border-width: 0 0 0 0");
        this.setStyle("-fx-text-fill: red;");
    }

    public void quitarSubrayado()
    {
        this.setStyle(null);
    }

    public FileItem icon(Node icon)
    {
        this.icon = icon;
        return this;
    }
}
