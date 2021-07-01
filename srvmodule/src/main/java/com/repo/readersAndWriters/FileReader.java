package com.repo.readersAndWriters;

import java.io.*;

public class FileReader
{
    public static StringBuffer readFile(String path) throws IOException
    {
        BufferedReader reader = new BufferedReader(new java.io.FileReader(path));
        StringBuffer ret = new StringBuffer();
        String currentLine = null;
        while ((currentLine = reader.readLine()) != null)
        {
            ret.append(currentLine).append("\n");
        }
        reader.close();
        return ret;
    }
}