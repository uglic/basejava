package ru.javawebinar.basejava;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class MainFile {
    public static void main(String[] args) {
        MainFile mainFile = new MainFile();
        //mainFile.listDir(new File(System.getProperty("user.dir")));
        mainFile.listDirWithCounters(new File(System.getProperty("user.dir")));
    }

    public void listDir(File fileFrom) {
        try {
            Queue<File> savedDirs = new ArrayDeque<>();
            if (fileFrom.isDirectory()) {
                savedDirs.add(fileFrom);
            }
            File currentFile = null;
            while ((currentFile = savedDirs.poll()) != null) {
                if (currentFile.isDirectory()) {
                    for (File internalFile : currentFile.listFiles()) {
                        if (internalFile.isFile()) {
                            System.out.println(internalFile.getCanonicalPath());
                        } else {
                            savedDirs.add(internalFile);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listDirWithCounters(File fileFrom) {
        try {
            Queue<File> savedDirs = new ArrayDeque<>();
            int fileCounter = 0;
            int dirsCounter = 0;
            int maxDequeSize = 0;
            if (fileFrom.isDirectory()) {
                savedDirs.add(fileFrom);
                maxDequeSize = savedDirs.size();
            }
            File currentFile = null;
            while ((currentFile = savedDirs.poll()) != null) {
                if (currentFile.isDirectory()) {
                    dirsCounter++;
                    for (File internalFile : currentFile.listFiles()) {
                        if (internalFile.isFile()) {
                            System.out.println(internalFile.getCanonicalPath());
                            fileCounter++;
                        } else {
                            savedDirs.add(internalFile);
                            if (savedDirs.size() > maxDequeSize) {
                                maxDequeSize = savedDirs.size();
                            }
                        }
                    }
                }
            }
            System.out.println("Files: " + fileCounter);
            System.out.println("Dirs:  " + dirsCounter);
            System.out.println("Max deque len:  " + maxDequeSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
