package service;

/**
 * Created by James on 9/29/2015.
 */
public class FileI {
    private String filename;
    private String fileowner;

    public FileI(String filename) {
        this.filename = filename;
    }

    public String getName() {
        return filename;
    }
}
