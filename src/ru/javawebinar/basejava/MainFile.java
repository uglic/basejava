package ru.javawebinar.basejava;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class MainFile {
    public static void main(String[] args) {
        MainFile mainFile = new MainFile();
        String rootDirName = System.getProperty("user.dir");
        File rootFileObject = new File(rootDirName);
        Path rootPathObject = Paths.get(rootDirName);

        mainFile.listDirRecursiveWalk(rootFileObject);
        mainFile.listDirRecursiveWithPath(rootPathObject);
        //mainFile.listDirRecursiveFilterSorted(rootFileObject);
        //mainFile.listDirRecursiveFilter(rootFileObject);
        //mainFile.listDir(rootFileObject);
        //mainFile.listDirWithCounters(rootFileObject);
    }

    public void listDirRecursiveWalk(File fileFrom) {
        try (Stream<Path> stream = Files.walk(fileFrom.toPath())) {
            stream.filter(p -> !p.toFile().isDirectory()).map(Path::toString).forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listDirRecursiveWithPath(Path pathFrom) {
        try {
            Files.find(pathFrom, 1, (p, a) -> !a.isDirectory())
                    .map(Path::toString)
                    .sorted()
                    .forEach(System.out::println);
            Files.find(pathFrom, 1, (p, a) -> a.isDirectory() && p != pathFrom)
                    .sorted(Comparator.comparing(Path::toString))
                    .forEach(this::listDirRecursiveWithPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listDirRecursiveFilterSorted(File fileFrom) {
        List<String> files = new ArrayList<>();
        try {
            for (File file : fileFrom.listFiles(File::isFile)) {
                files.add(file.getCanonicalPath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Collections.sort(files);
        for (String file : files) {
            System.out.println(file);
        }

        files.clear();
        try {
            for (File file : fileFrom.listFiles(File::isDirectory)) {
                files.add(file.getCanonicalPath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Collections.sort(files);
        for (String file : files) {
            listDirRecursiveFilterSorted(new File(file));
        }
    }

    public void listDirRecursiveFilter(File fileFrom) {
        try {
            for (File file : fileFrom.listFiles(File::isFile)) {
                System.out.println(file.getCanonicalPath());
            }
            for (File folder : fileFrom.listFiles(File::isDirectory)) {
                listDirRecursiveFilter(folder);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listDir(File fileFrom) {
        try {
            Queue<File> savedDirs = new ArrayDeque<>();
            if (fileFrom.isDirectory()) {
                savedDirs.add(fileFrom);
            }
            File currentFile;
            while ((currentFile = savedDirs.poll()) != null) {
                for (File internalFile : currentFile.listFiles()) {
                    if (internalFile.isFile()) {
                        System.out.println(internalFile.getCanonicalPath());
                    } else {
                        savedDirs.offer(internalFile);
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
            File currentFile;
            while ((currentFile = savedDirs.poll()) != null) {
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
            System.out.println("Files: " + fileCounter);
            System.out.println("Dirs:  " + dirsCounter);
            System.out.println("Max deque len:  " + maxDequeSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
