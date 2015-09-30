package service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by James on 9/29/2015.
 */
public class FileList {
    String files;
    ArrayList<FileI> list = new ArrayList<FileI>();

    public FileList()
    {
        this.files="Files";
        try {
            File file = new File("src/service/Files.txt");
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String array[] = line.split("\\,");
                this.list.add(new FileI(array[0], array[1]));
            }
            fileReader.close();;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getFiles() {
        return this.files;
    }

    public ArrayList<FileI> getList() {
        return this.list;
    }
}
