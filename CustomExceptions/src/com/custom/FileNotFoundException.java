package com.custom;

import java.io.File;

public class FileNotFoundException extends RuntimeException {

    public FileNotFoundException(File f){
        super("File not found: " + f.toString());
    }

}
