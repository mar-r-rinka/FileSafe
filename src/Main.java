import java.io.*;
import java.util.Arrays;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Main {
    static GameProgress[] progress = {new GameProgress(20, 40, 3, 14.7),
            new GameProgress(80, 30, 8, 23.3),
            new GameProgress(56, 74, 6, 43.5)};
    static String[] files = new String[progress.length];

    public static void main(String[] args) {


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
        openZip("/Users/mar-r-rinka/Games/savegames/fileProg.zip", "/Users/mar-r-rinka/Games/savegames/");
        for (String file : files) {
            System.out.println(openProgress("/Users/mar-r-rinka/Games/savegames/" + file));
        }
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
                try (FileInputStream fis = new FileInputStream(parent + file)) {
                    ZipEntry entry = new ZipEntry(file);
                    zout.putNextEntry(entry);
                    byte[] buffer = new byte[fis.available()];
                    fis.read(buffer);
                    zout.write(buffer);
                    zout.closeEntry();
                    File file1 = new File(parent + file);
                    file1.delete();
                } catch (FileNotFoundException ex) {
                    System.out.println(ex.getMessage());
                }

            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void openZip(String fileNameZip, String dir) {
        byte[] buffer = new byte[1024];
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(fileNameZip))) {
            ZipEntry entry;
            String name;
            while ((entry = zis.getNextEntry()) != null) {
                name = entry.getName();
                File newName = new File(dir + "/" + name);
                FileOutputStream fout = new FileOutputStream(newName);
                for (int i = zis.read(); i != -1; i = zis.read()) {
                    fout.write(i);
                }
                fout.flush();
                zis.closeEntry();
                fout.close();
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());

        }
    }

    public static GameProgress openProgress(String fileName) {
        GameProgress gameProgress = null;
        try (FileInputStream fis = new FileInputStream(fileName);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            gameProgress = (GameProgress) ois.readObject();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return gameProgress;
    }

}