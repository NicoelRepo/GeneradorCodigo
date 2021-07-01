package com.repo.exceptions;

public class DirectorioNoEncontradoException extends Exception
{
    String dir;

    public DirectorioNoEncontradoException(String dir)
    {
        super("El directorio " + dir + " no existe");
    }
}
