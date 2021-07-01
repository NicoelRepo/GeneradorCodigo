package com.repo.entity;

import com.repo.estrategy.EstrategyGenerateText;
import com.repo.utilSRV.UtilClone;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Plantilla implements Cloneable, Serializable
{
    final private List<CodeFile> listCodeFiles;
    final String name;
    private final List<EstrategyGenerateText> estrategysSecuence = new LinkedList<>();

    public Plantilla(List<CodeFile> listCodeFiles, String name)
    {
        this.listCodeFiles = listCodeFiles;
        this.name = name;
    }

    public List<CodeFile> getListCodeFiles()
    {
        return listCodeFiles;
    }

    public String getName()
    {
        return name;
    }

    public List<EstrategyGenerateText> getEstrategysSecuence()
    {
        return estrategysSecuence;
    }

    @Override
    public String toString()
    {
        return "Plantilla{" + "nro de archivos=" + listCodeFiles.size()
            + ", name='" + name + '\'' + ", estrategysSecuence="
            + estrategysSecuence.stream().map((e) -> e.getClass().getSimpleName()).collect(Collectors.toList())
            + ", hashcode=" + this.hashCode()
            + '}';
    }

    @Override
    public Object clone() throws CloneNotSupportedException
    {
        Object ret;
        try
        {
            List<CodeFile> listCodeFilesCopy = null;
            try
            {
                listCodeFilesCopy = (List<CodeFile>) UtilClone.cloneList(this.listCodeFiles);
            }
            catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex)
            {
                ex.printStackTrace();
            }

            var plantillaClone = new Plantilla(listCodeFilesCopy, this.name);
            UtilClone.cloneElements(this.getEstrategysSecuence(), plantillaClone.getEstrategysSecuence());

            ret = plantillaClone;
            return ret;
        }
        catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex)
        {
            ex.printStackTrace();
            ret = ex;
        }

        return ret;
    }

}
