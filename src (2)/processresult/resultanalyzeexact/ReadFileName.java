package processresult.resultanalyzeexact;


import com.twodimension.GetFile;
import com.twodimension.TargetData;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

public class ReadFileName {


//    public ListNode findFirstCommonNode(ListNode pHead1,ListNode pHead2){
//        if(pHead1 == null || pHead2 == null){
//            return null;
//        }
//        ListNode p1 = phead1;
//
//        ListNode p2 = phead2;
//
//        while(p1 != p2){
//            p1= p1.next;
//            p2= p2.next;
//            if(p1 != p2){
//                if(p1 == null){
//                    p1 = phead2;
//                }
//                if(p2 == null){
//                    p2 = phead1;
//                }
//            }
//        }
//        return p1;
//    }

    public static void main(String[] args) throws FileNotFoundException {

        GetFile getFile = new GetFile();
        File file = new File("C:\\BaiduNetdiskDownload\\Files\\数据处理\\读取标准测试集的名称");
        List<File> allFile = getFile.getAllFile(file);
        for (File listFile : allFile) {
            String path = listFile.getAbsolutePath();
            String rePath = listFile.getName();
            String parentName = listFile.getParentFile().getName();
            String sourceFolder = path;
            String absolutePath = listFile.getParentFile().getAbsolutePath();
            String destinationFolder = absolutePath + "\\C" + rePath;

            TargetData rLayout = new TargetData();
            // 初始化模型
            rLayout.initData(path);
            if (rLayout.targetNum > 30) {
                continue;
            }
            System.out.println(rePath);
        }
    }
}
