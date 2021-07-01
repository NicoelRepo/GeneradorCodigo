package com.repo.utilSRV;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UtilClone
{
    /**
     * Create deep copy of the list l.
     *
     * @param <T>
     * @param l
     * @return
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public static <T extends Cloneable> List<T> cloneList(List<T> l) throws NoSuchMethodException, IllegalAccessException,
        IllegalArgumentException, InvocationTargetException
    {
        var ret = new ArrayList<T>();
        for (T value : l)
        {
            var clonedValue = clone(value);
            ret.add(clonedValue);
        }
        return ret;
    }

    /**
     * Copy all the elements of the list l to list l2
     *
     * @param <T>
     * @param l
     * @param l2
     * @return
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public static <T extends Cloneable> List<T> cloneElements(List<T> l, List<T> l2) throws NoSuchMethodException, IllegalAccessException,
        IllegalArgumentException, InvocationTargetException
    {
        for (T value : l)
        {
            var clonedValue = clone(value);
            l2.add(clonedValue);
        }
        return l2;
    }

    public static <T extends Cloneable, R extends Cloneable> Map<T, R> cloneMap(Map<T, R> map) throws NoSuchMethodException, IllegalAccessException,
        IllegalArgumentException, InvocationTargetException
    {
        var ret = new HashMap<T, R>();
        for (Map.Entry<T, R> entry : map.entrySet())
        {
            ret.put(clone(entry.getKey()), entry.getValue());
        }
        return ret;
    }

    public static <T extends Cloneable, R extends Cloneable> Map<T, R> cloneElementsMap(Map<T, R> map, Map<T, R> map2) throws NoSuchMethodException, IllegalAccessException,
        IllegalArgumentException, InvocationTargetException
    {
        for (Map.Entry<T, R> entry : map.entrySet())
        {
            map2.put(clone(entry.getKey()), entry.getValue());
        }
        return map2;
    }

    public static <T extends Cloneable> T clone(T obj) throws NoSuchMethodException, IllegalAccessException,
        IllegalArgumentException, InvocationTargetException
    {
        return (T) obj.getClass().getMethod("clone").invoke(obj);
    }

    public static <T extends Serializable> T deepCopy(T obj)
    {
        T ret = null;
        try
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);

            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            ret = (T) ois.readObject();
        }
        catch (IOException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        return ret;
    }
}
