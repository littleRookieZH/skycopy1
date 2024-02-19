package com.twodimension;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
/**
 * @author zh15178381496
 * @create 2022-10 16:28
 * @说明：
 * @总结：
 */
public class GetFile {
    public List<File> getAllFile(File dirFile) {
        //如果文件夹不存在或者不是文件夹，则返回null
        if (Objects.isNull(dirFile) || !dirFile.exists() || dirFile.isFile()) {
            return null;
        }
        File[] childrenFiles = dirFile.listFiles();
        List<File> files = new ArrayList<>();
        for (File childFile : childrenFiles) {
            // 如果时文件，直接添加到结果集合
            if (childFile.isFile()) {
                files.add(childFile);
            } else {
                // 如果是文件夹，则将其内部文件添加进结果集合
                List<File> cFiles = getAllFile(childFile);
                if (Objects.isNull(cFiles) || cFiles.isEmpty()) {
                    continue;
                }
                files.addAll(cFiles);
            }
        }
        return files;
    }
}
