package com.ffanxxy.phe.phe.Save;

import com.ffanxxy.phe.phe.Applicant.Logger;
import com.ffanxxy.phe.phe.PclComponents.CardWith;
import com.ffanxxy.phe.phe.PclComponents.PCLC;
import com.ffanxxy.phe.phe.PclComponents.components.Card;
import com.ffanxxy.phe.phe.PclComponents.components.TextBlock;
import javafx.scene.control.ComboBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class SLphe {

    private ArrayList<Card> assemblies = new ArrayList<>();
    private ArrayList<String> work = new ArrayList<>();

    private ArrayList<Card> Rassemblies = new ArrayList<>();
    private ArrayList<String> Rwork = new ArrayList<>();

    /**
     * 是否在保存时刷新保存地址
     */
    private boolean doReloadSavePath = false;

    public void ReloadSavePath(boolean bool) {
        this.doReloadSavePath = bool;
    }

    public SLphe(ArrayList<CardWith> assemblies,ArrayList<String> work) {
        for(CardWith cw:assemblies) {
             this.assemblies.add(cw.getCard());
        }
        this.work = new ArrayList<>(work);
    }

    // 添加文件路径到filePaths列表中
    public void addFilePath(String path) {
        work.add(path);
    }

    public ArrayList<CardWith> getAssemblies() {
        ArrayList<CardWith> a = new ArrayList<>();
        for(Card c  : Rassemblies) {
            ComboBox<String> comboBox = new ComboBox<>();
            comboBox.setPromptText(c.getName());
            a.add(new CardWith(c,comboBox));
        }
        return a;
    }

    public File saveAsZip() {
        File zipFile = chooseDirectory();   // 自定义后缀的压缩文件

        if(zipFile == null) {
            return null;
        }

        if(isDoReloadSavePath()) {
            work.set(0,zipFile.getPath());
        }

        File afile = new File("assemblies.ser");
        File bfile = new File("work.ser");

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(afile))) {
            // 序列化ArrayList到文件
            oos.writeObject(assemblies);
            System.out.println("ArrayList has been serialized to " + afile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(bfile))) {
            // 序列化ArrayList到文件
            oos.writeObject(work);
            System.out.println("ArrayList has been serialized to " + bfile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        File[] filesToCompress = new File[]{
                afile,
                bfile
        };

        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile))) {
            compressFiles(filesToCompress, zos);
            System.out.println("Files have been compressed to " + zipFile.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Exception e = CheckSave(zipFile);

        if(e == null) {
            Logger.addLog("SLphe.saveasZip","已经保存文件为" + zipFile.getPath());
        } else {
            Logger.errorLog("SLphe.saveasZip",e);
        }

        return zipFile;

    }

    public static void save(String path, ArrayList<CardWith> assemblies, ArrayList<String> work) {
        File zipFile = new File(path);   // 自定义后缀的压缩文件

        ArrayList<Card> Cards = new ArrayList<Card>();
        for(CardWith cardWith : assemblies) {
            Cards.add(cardWith.getCard());
        }

        File afile = new File("assemblies.ser");
        File bfile = new File("work.ser");

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(afile))) {
            // 序列化ArrayList到文件
            oos.writeObject(Cards);
            System.out.println("ArrayList has been serialized to " + afile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(bfile))) {
            // 序列化ArrayList到文件
            oos.writeObject(work);
            System.out.println("ArrayList has been serialized to " + bfile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        File[] filesToCompress = new File[]{
                afile,
                bfile
        };

        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile))) {
            compressFiles(filesToCompress, zos);
            System.out.println("Files have been compressed to " + zipFile.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Exception e = CheckSave(zipFile);

        if(e == null) {
            Logger.addLog("SLphe.saveasZip","已经保存文件为" + zipFile.getPath());
        } else {
            Logger.errorLog("SLphe.saveasZip",e);
        }
    }

    private static void compressFiles(File[] files, ZipOutputStream zos) throws IOException {
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    // 如果是文件，则添加到压缩文件中
                    try (InputStream is = new FileInputStream(file)) {
                        ZipEntry zipEntry = new ZipEntry(file.getName());
                        zos.putNextEntry(zipEntry);

                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = is.read(buffer)) > 0) {
                            zos.write(buffer, 0, len);
                        }
                        zos.closeEntry();
                    }
                } else if (file.isDirectory()) {
                    // 如果是目录，则递归处理目录下的所有文件
                    File[] dirFiles = file.listFiles();
                    if (dirFiles != null) {
                        compressFiles(dirFiles, zos);
                    }
                }
            }
        }
    }

    private static Exception CheckSave(File file) {
        // 指定读取文件的位置和名称
        if(file == null) {
            System.out.println("已取消");
            return new Exception("不存在文件");
        }

        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(file))) {
            ZipEntry zipEntry;
            boolean afileExtracted = false, bfileExtracted = false;

            while ((zipEntry = zis.getNextEntry()) != null) {
                String entryName = zipEntry.getName();

                if ("assemblies.ser".equals(entryName) && !afileExtracted) {
                    Logger.addLog("SLphe.CheckSave","已经检查到assemblies.ser : " + readObjectFromZip(zis));
                    afileExtracted = true;
                } else if ("work.ser".equals(entryName) && !bfileExtracted) {
                    Logger.addLog("SLphe.CheckSave","已经检查到work.ser" + readObjectFromZip(zis));
                    bfileExtracted = true;
                }

                zis.closeEntry();

                // 如果两个文件都已处理，则可以提前退出循环
                if (afileExtracted && bfileExtracted) {
                    break;
                }
            }

            if (afileExtracted && bfileExtracted) {
                return null;
            } else {
                return new Exception("检测失败:没找到文件");
            }
        } catch (IOException | ClassNotFoundException e) {
            return e;
        }
    }

    public File loadFromZip() {
        // 指定读取文件的位置和名称
        File zipFile = chooseFile();
        if(zipFile == null) {
            System.out.println("已取消");
            return null;
        }

        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile))) {
            ZipEntry zipEntry;
            boolean afileExtracted = false, bfileExtracted = false;

            while ((zipEntry = zis.getNextEntry()) != null) {
                String entryName = zipEntry.getName();

                if ("assemblies.ser".equals(entryName) && !afileExtracted) {
                    Rassemblies = readObjectFromZip(zis);
                    afileExtracted = true;
                } else if ("work.ser".equals(entryName) && !bfileExtracted) {
                    Rwork = readObjectFromZip(zis);
                    bfileExtracted = true;
                }

                zis.closeEntry();

                // 如果两个文件都已处理，则可以提前退出循环
                if (afileExtracted && bfileExtracted) {
                    break;
                }
            }

            if (afileExtracted && bfileExtracted) {
                System.out.println("Both 'assemblies.ser' and 'work.ser' have been loaded from the archive.");
            } else {
                System.out.println("Not all specified files were found in the archive.");
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return zipFile;
    }

    private static <T> T readObjectFromZip(ZipInputStream zis) throws IOException, ClassNotFoundException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(readAllBytes(zis)))) {

            @SuppressWarnings("unchecked")
            T object = (T) ois.readObject();
            return object;
        }
    }

    private static byte[] readAllBytes(InputStream is) throws IOException {
        byte[] buffer = new byte[1024];
        int bytesRead;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        while ((bytesRead = is.read(buffer)) != -1) {
            bos.write(buffer, 0, bytesRead);
        }

        return bos.toByteArray();
    }


    public static File chooseFile() {
        FileChooser fc = new FileChooser();
        fc.setTitle("选择项目文件");

        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("PHE files", "*.phe");
        fc.getExtensionFilters().add(extensionFilter);

        fc.setInitialDirectory(new File(System.getProperty("user.home")));

        return fc.showOpenDialog(new Stage());
    }

    public static File chooseDirectory() {
        FileChooser fc = new FileChooser();
        fc.setTitle("保存为PHE文件");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("PHE files", "*.phe"));
        return fc.showSaveDialog(new Stage());
    }

    public ArrayList<String> getWork() {
        return this.Rwork;
    }

    public boolean isDoReloadSavePath() {
        return doReloadSavePath;
    }

    //构建为xmal

    public ArrayList<String> build() {
        ArrayList<String> lines = new ArrayList<>();
        for(Card card : assemblies) {
            lines.add("<local:MyCard Title=\" "+ card.getName() +"\" Margin=\"" + card.buildMargins() +"\" CanSwap=\"True\" IsSwaped=\"True\">");
            if(card.isHasStackPanle()) {
                lines.add("    <StackPanel Margin=\"" + card.buildStackPanelMargin() +"\">");
                for (PCLC pclc : card.getComponents()) {
                    if (pclc instanceof TextBlock textBlock) {
                        lines.add("<!--" + textBlock.getName() + "-->");
                        lines.add("        <TextBlock TextWrapping=\"Wrap\" Margin=\"" + textBlock.buildMargins() +"\"\n" +
                                "                    Text=\"" + textBlock.getText() + "\" />");
                    }
                }
                lines.add("    </StackPanel>");
            }else{
                lines.add("<!--" + "并没有堆叠文本" + "-->");
            }
            lines.add("</local:MyCard>");
        }
        return lines;

    }

    public void spawn(ArrayList<String> x) {
        File resultFile = chooseDirectoryX();

        // 定义文件路径和名称
        Path filePath = Paths.get(resultFile.getPath());

        // 确保目录存在
        try {
            Files.createDirectories(filePath.getParent());
        } catch (IOException e1) {
            System.err.println("创建目录时出错.");
            e1.fillInStackTrace();
        }

        // 构建要写入文件的内容
        StringBuilder contentBuilder = new StringBuilder();
        for (String str : x) {
            contentBuilder.append(str).append("\n");
        }
        String content = contentBuilder.toString();

        try {
            // 使用 write() 方法直接写入字符串，并指定编码
            Files.writeString(filePath, content, StandardCharsets.UTF_8);
            System.out.println("内容已成功写入文件: " + filePath.getFileName());
        } catch (IOException e) {
            System.err.println("写入文件时出错.");
            e.fillInStackTrace();
        }
    }

    public static File chooseDirectoryX() {
        FileChooser fc = new FileChooser();
        fc.setTitle("保存为xaml文件");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("XAML file", "*.xaml"));
        return fc.showSaveDialog(new Stage());
    }
}