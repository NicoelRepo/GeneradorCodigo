package com.repo.estrategy;

import com.repo.entity.CodeFile;
import com.repo.entity.Parameter;
import com.repo.utilSRV.UtilClone;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

import java.util.HashMap;
import java.util.Map;

public abstract class EstrategyGenerateText implements Cloneable, Serializable
{
    CodeFile codeFile = null;
    public final Map<String, Parameter> mapParameters = new HashMap<>();

    public abstract void generate(CodeFile codeFile);

    public Map<String, Parameter> getMapParameters()
    {
        return mapParameters;
    }
    
    public void copyElementsTo(EstrategyGenerateText estrategyEmpty) throws NoSuchMethodException, IllegalAccessException,
        IllegalArgumentException, InvocationTargetException
    {
        estrategyEmpty.codeFile = UtilClone.clone(this.codeFile);
        
    }
}
