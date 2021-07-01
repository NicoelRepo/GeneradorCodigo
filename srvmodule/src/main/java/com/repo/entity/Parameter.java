package com.repo.entity;

import com.repo.estrategy.EstrategyGenerateText;
import java.io.Serializable;

public class Parameter implements Cloneable, Serializable
{
    public final String nameParameter;
    public String value;
    public final EstrategyGenerateText estrategy;

    public Parameter(String nameParameter, String value, EstrategyGenerateText estrategy)
    {
        this.nameParameter = nameParameter;
        this.value = value;
        this.estrategy = estrategy;
    }
    
    public String getValue()
    {
        return this.value;
    }
    
    public void setValue(String value)
    {
        this.value = value;
    }

}
