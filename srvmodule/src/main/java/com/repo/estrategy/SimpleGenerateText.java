package com.repo.estrategy;

import com.repo.entity.CodeFile;
import com.repo.entity.Parameter;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class SimpleGenerateText extends EstrategyGenerateText
{
    private static final char CARACTER = '#';
    final static Map<String, Function<String, String>> mapModifiers;
    static
    {
        mapModifiers = new HashMap<>();
        mapModifiers.put("toLowerCase", String::toLowerCase);
        mapModifiers.put("toUpperCase", String::toUpperCase);
        mapModifiers.put("firstToLowerCase", (s) -> s.substring(0, 1).toLowerCase() + s.substring(1));
        mapModifiers.put("firstToUpperCase", (s) -> s.substring(0, 1).toUpperCase() + s.substring(1));
    }

    @Override
    public void generate(CodeFile codeFile)
    {
        StringBuffer sb = codeFile.getText();
        for (Parameter p : mapParameters.values())
        {
            // Se reemplaza el parametro p en el texto
            replaceAllOcurrences(sb, p.nameParameter, p.value);

            // Se reemplaza el parametro p en el nombre del archivo
            StringBuffer sbForName = new StringBuffer(codeFile.getName());
            replaceAllOcurrences(sbForName, p.nameParameter, p.value);
            codeFile.setName(sbForName.toString());
            
            StringBuffer sbForPath = new StringBuffer(codeFile.getPath());
            replaceAllOcurrences(sbForPath, p.nameParameter, p.value);
            codeFile.setPath(sbForPath.toString());
        }
    }

    private void replaceAllOcurrences(StringBuffer sb, String parameter, String value)
    {
        int i = 0;
        int indexBegin;
        int indexEnd;
        while (i < sb.length())
        {
//            if (sb.charAt(i) == CARACTER)
            if (i + parameter.length() + 1 < sb.length() &&
                sb.substring(i, i + parameter.length() + 1).equals(CARACTER + parameter) && 
                (sb.charAt(i + parameter.length() + 1) == CARACTER ||
                sb.charAt(i + parameter.length() + 1) == ':'))
            {
                indexBegin = i;
                indexEnd = indexBegin + sb.substring(indexBegin+1).indexOf(CARACTER) + 1;
                String parameterWithModifiers = sb.substring(indexBegin+1, indexEnd);
                String formattedValue = produceValue(parameterWithModifiers, value);
                sb.replace(indexBegin, indexEnd + 1, formattedValue);
                i++;
            }
            else
            {
                i++;
            }
        }
    }

    private static String produceValue(String parameterWithModifiers, String value)
    {
        String[] modifiers = parameterWithModifiers.split(":");
        for (int i = 1; i < modifiers.length; i++)
        {
            value = mapModifiers.get(modifiers[i]).apply(value);
        }
        return value;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

//    @Override
//    public Object clone() throws CloneNotSupportedException
//    {
//        try
//        {
//            var objClone = new SimpleGenerateText();
//            super.copyElementsTo(objClone);
//        }
//        catch (NoSuchMethodException ex)
//        {
//            ex.printStackTrace();
//        }
//        catch (IllegalAccessException ex)
//        {
//            ex.printStackTrace();
//        }
//        catch (IllegalArgumentException ex)
//        {
//            ex.printStackTrace();
//        }
//        catch (InvocationTargetException ex)
//        {
//            ex.printStackTrace();
//        }
//        
//    }
}