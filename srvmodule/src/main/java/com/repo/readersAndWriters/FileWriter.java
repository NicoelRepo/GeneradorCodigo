package com.repo.readersAndWriters;

import java.io.*;

public class FileWriter
{
    public static void createFile(String path, StringBuffer code) throws IOException
    {
        File file = new File(path);

        if (!file.exists())
        {
           file.createNewFile();
        }

        BufferedWriter writer = new BufferedWriter(new java.io.FileWriter(file));
        writer.write(code.toString());
        writer.close();
    }
}
