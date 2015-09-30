package service;

/**
 * Created by James on 9/29/2015.
 */
public class FileI {
    private String filename;
    private String fileowner;

    public FileI(String filename, String fileowner) {
        this.filename = filename;
        this.fileowner = fileowner;
    }

    public String getName() {
        return filename;
    }

    public String getOwner() {
        return fileowner;
    }
}
