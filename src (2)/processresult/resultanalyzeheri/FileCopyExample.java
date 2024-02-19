package processresult.resultanalyzeheri;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.stream.Collectors;

public class FileCopyExample {
    public static void main(String[] args) {
        File file = new File("E:\\同步文件\\BaiduSyncdisk\\小论文\\结果集\\启发式算法集\\改进放置位置后的启发式结果集\\ResultData03\\COX");
        File[] files = file.listFiles();
        int limitNum = 10;
        for (int i = 0; i < files.length; i++) {
            String fileName = files[i].getName();
            String absolutePath = files[i].getAbsolutePath();
            String parentName = files[i].getParentFile().getParentFile().getAbsolutePath();
            // 指定源文件夹和目标文件夹的路径
            Path sourceFolderPath = Paths.get(absolutePath);
            Path targetFolderPath = Paths.get(parentName + "\\COX1\\" + fileName);

            try {
                // 获取源文件夹中的所有子文件夹，并选择前 10 个
                Files.walk(sourceFolderPath, 1)
                        .filter(Files::isDirectory)
                        .skip(1) // 跳过源文件夹本身
                        .filter(subfolder -> {
                            try {
                                // 判断子文件夹中的文件个数是否为10
                                return Files.list(subfolder).count() == 10;
                            } catch (IOException e) {
                                e.printStackTrace();
                                return false;
                            }
                        })
                        .limit(limitNum)
                        .collect(Collectors.toList())
                        .forEach(sourceSubfolder -> {
                            // 构建目标文件夹的路径
                            Path targetSubfolder = targetFolderPath.resolve(sourceFolderPath.relativize(sourceSubfolder));

                            try {
                                // 复制文件夹及其内容
                                copyFolder(sourceSubfolder, targetSubfolder);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });

                System.out.println("文件夹复制完成！");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private static void copyFolder(Path source, Path target) throws IOException {
        Files.createDirectories(target);

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(source)) {
            for (Path entry : stream) {
                Path entryTarget = target.resolve(entry.getFileName());
                if (Files.isDirectory(entry)) {
                    copyFolder(entry, entryTarget);
                } else {
                    Files.copy(entry, entryTarget, StandardCopyOption.COPY_ATTRIBUTES);
                }
            }
        }
    }
}
