package processresult.resultanalyzeheri;


import com.twodimension.GetFile;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Scanner;

public class ModifyAddress {
        public static void main(String[] args) {

            GetFile getFile = new GetFile();
            File file = new File("E:\\同步文件\\BaiduSyncdisk\\小论文\\结果集\\启发式算法集\\启发式结果集\\ResultData04\\res1_newDef\\COX");
            for (File listFile : file.listFiles()) {
                String path = listFile.getAbsolutePath();
                String rePath = listFile.getName();
                String parentName = listFile.getParentFile().getName();
                String sourceFolder = path;
                String absolutePath = listFile.getParentFile().getAbsolutePath();
                String destinationFolder = absolutePath + "\\C" + rePath;
                // 调用文件夹复制方法
                copyFolder(sourceFolder, destinationFolder);
            }
            System.out.println("文件夹复制完成！");
        }

        // 文件夹复制方法
        private static void copyFolder(String sourceFolder, String destinationFolder) {
            try {
                // 创建目标文件夹
                Path destPath = Paths.get(destinationFolder);
                Files.createDirectories(destPath);

                // 使用 Files.walkFileTree 遍历源文件夹
                Files.walkFileTree(Paths.get(sourceFolder), new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        // 构建目标文件路径
                        Path destFile = destPath.resolve(Paths.get(sourceFolder).relativize(file));

                        // 创建目标文件所在的目录
                        Files.createDirectories(destFile.getParent());

                        // 复制文件
                        Files.copy(file, destFile, StandardCopyOption.REPLACE_EXISTING);

                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                        // 构建目标文件夹路径
                        Path destDir = destPath.resolve(Paths.get(sourceFolder).relativize(dir));

                        // 创建目标文件夹
                        Files.createDirectories(destDir);

                        return FileVisitResult.CONTINUE;
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("文件夹复制失败：" + e.getMessage());
            }
        }

    public int readNumt(File fileData) throws FileNotFoundException {
        String line = null;
        String[] substr = null;
        Scanner cin = new Scanner(new BufferedReader((new FileReader(fileData))));
        // 先读一行
        line = cin.nextLine(); // 空行
        line = cin.nextLine(); // 时间
        substr = cin.nextLine().trim().split("\\s+"); // 物品数量
        return Integer.parseInt(substr[0]);
    }

}
