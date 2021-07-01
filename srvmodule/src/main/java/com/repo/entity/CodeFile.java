package com.repo.entity;

import java.io.Serializable;

public class CodeFile implements Cloneable, Serializable
{
    private StringBuffer text;
    private String path;
    private String name;

    public String getPath()
    {
        return path;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public StringBuffer getText()
    {
        return text;
    }

    public void setText(StringBuffer text)
    {
        this.text = text;
    }

    @Override
    public Object clone() throws CloneNotSupportedException
    {
        CodeFile ret = new CodeFile();
        ret.setName(name);
        ret.setPath(path);
        ret.setText(text);
        return ret;
    }

}
