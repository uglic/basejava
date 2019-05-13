package ru.javawebinar.basejava;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class MainFile {
    public static void main(String[] args) {
        String rootDirName = System.getProperty("user.dir");
        File rootFileObject = new File(rootDirName);
        Path rootPathObject = Paths.get(rootDirName);

        listPathRecursiveBeautiful(rootPathObject, 0);
//        listDirRecursiveWalk(rootFileObject);
//        listDirRecursiveWithPath(rootPathObject);
//        listDirRecursiveFilter(rootFileObject);
//        listDir(rootFileObject);
//        listDirWithCounters(rootFileObject);
    }

    public static void listPathRecursiveBeautiful(Path pathFrom, int startIndent) {
        if (startIndent < 0) return;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < startIndent; i++) {
            builder.append("| ");
        }
        String prefix = builder.toString();
        try {
            Files.list(pathFrom)
                    .filter(Files::isDirectory)
                    //.sorted()
                    .forEach(path -> {
                        System.out.println(prefix + "+"  + path.getFileName());
                        listPathRecursiveBeautiful(path, startIndent + 1);
                    });
            Files.list(pathFrom)
                    .filter(((Predicate<Path>) Files::isDirectory).negate())
                    //.sorted()
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .map(prefix::concat)
                    .forEach(System.out::println);
        } catch (IOException e) {
            System.out.println(prefix + "<READING ERROR>");
        }
    }

    public static void listDirRecursiveWalk(File fileFrom) {
        try (Stream<Path> stream = Files.walk(fileFrom.toPath())) {
            stream.filter(Files::isRegularFile).forEach(p -> System.out.println(p.getFileName()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void listDirRecursiveWithPath(Path pathFrom) {
        try {
            Files.find(pathFrom, 1, (p, a) -> Files.isRegularFile(p))
                    .map(Path::getFileName)
                    .forEach(System.out::println);
            Files.find(pathFrom, 1, (p, a) -> Files.isDirectory(p) && p != pathFrom)
                    .forEach(MainFile::listDirRecursiveWithPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void listDirRecursiveFilter(File fileFrom) {
        File[] files = fileFrom.listFiles(File::isFile);
        if (files != null) {
            for (File file : files) {
                System.out.println(file.getName());
            }
        }
        File[] folders = fileFrom.listFiles(File::isDirectory);
        if (folders != null) {
            for (File folder : folders) {
                listDirRecursiveFilter(folder);
            }
        }
    }

    public static void listDir(File fileFrom) {
        Queue<File> savedDirs = new ArrayDeque<>();
        if (fileFrom.isDirectory()) {
            savedDirs.add(fileFrom);
        }
        File currentFile;
        while ((currentFile = savedDirs.poll()) != null) {
            File[] files = currentFile.listFiles();
            if (files != null) {
                for (File internalFile : files) {
                    if (internalFile.isFile()) {
                        System.out.println(internalFile.getName());
                    } else {
                        savedDirs.offer(internalFile);
                    }
                }
            }
        }
    }

    public static void listDirWithCounters(File fileFrom) {
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
            File[] files = currentFile.listFiles();
            if (files != null) {
                for (File internalFile : files) {
                    if (internalFile.isFile()) {
                        System.out.println(internalFile.getName());
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
    }
}
