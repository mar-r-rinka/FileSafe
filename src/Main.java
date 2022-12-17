import java.io.*;
import java.util.Arrays;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {
    public static void main(String[] args) {
        GameProgress[] progress = {new GameProgress(20, 40, 3, 14.7),
                new GameProgress(80, 30, 8, 23.3),
                new GameProgress(56, 74, 6, 43.5)};
        String[] files = new String[progress.length];

        for (int i = 0; i < progress.length; i++) {
            File fileProg = new File("/Users/mar-r-rinka/Games/savegames/fileProg" + i + ".text");
            try {
                fileProg.createNewFile();
                    files[i] = fileProg.getName();

            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
            saveGame(fileProg.getParent() + "/" + fileProg.getName(), progress[i]);

        }
        zipFiles("/Users/mar-r-rinka/Games/savegames/fileProg.zip", files, "/Users/mar-r-rinka/Games/savegames/");
        System.out.println(Arrays.toString(files));


    }

    public static void saveGame(String fileName, GameProgress progress) {
        try (FileOutputStream out = new FileOutputStream(fileName);
             ObjectOutputStream oos = new ObjectOutputStream(out)) {
            oos.writeObject(progress);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void zipFiles(String fileName, String[] files, String parent) {
        try (ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(fileName))) {
            for (String file : files) {
                try (FileInputStream fis = new FileInputStream(parent+file)) {
                    ZipEntry entry = new ZipEntry(file);
                    zout.putNextEntry(entry);
                    byte[] buffer = new byte[fis.available()];
                    fis.read(buffer);
                    zout.write(buffer);
                    zout.closeEntry();
                } catch (FileNotFoundException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}