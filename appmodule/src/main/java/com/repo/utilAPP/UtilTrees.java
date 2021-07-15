package com.repo.utilAPP;

import com.repo.entity.CodeFile;
import com.repo.main.FileItem;
import java.util.Comparator;
import java.util.Queue;
import javafx.scene.control.TreeItem;

public class UtilTrees
{
    public static void expandItemDeep(TreeItem<?> item)
    {
        if (item != null && !item.isLeaf())
        {
            item.setExpanded(true);
            for (var child : item.getChildren())
            {
                expandItemDeep(child);
            }
        }
    }

    public static void collapseItemDeep(TreeItem<?> item)
    {
        if (item != null && !item.isLeaf())
        {
            item.setExpanded(false);
            for (var child : item.getChildren())
            {
                collapseItemDeep(child);
            }
        }
    }

    public static void ordenarArbol(TreeItem<FileItem> nodoPadre, final Comparator<FileItem> comparator)
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
    
    public static void insertarArchivoEnArbol(TreeItem<FileItem> nodoPadre, CodeFile codeFile, Queue<String> colaDirectorios)
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
}
