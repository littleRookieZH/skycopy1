package processresult.resultCompare;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

/**
 * @author xzbz
 * @create 2023-12-17 10:59
 */
public class ResultCompared {

    public static void main(String[] args) throws FileNotFoundException {
        File fileVar = new File("E:\\同步文件\\BaiduSyncdisk\\小论文\\处理结果集\\对比处理");
        Data data = new Data(new ArrayList<String>(), new HashMap<String, List<double[]>>(), new HashMap<String, ProcessResData>());
        ResultCompared resultCompared = new ResultCompared();
        for (File file : fileVar.listFiles()) {
            resultCompared.readData(file, data);
        }
        for (int i = 0; i < data.fileNames.size(); i++) {
            String name = data.fileNames.get(i);
            if (data.dataInfos.containsKey("I" + name)) {
                List<double[]> gapInfos = data.dataInfos.get(name);
                List<double[]> IGapInfos = data.dataInfos.get("I" + name);
                List<Double> doublesAvg = new ArrayList<>();
                List<Double> doublesBest = new ArrayList<>();

                for (int j = 0; j < gapInfos.get(0).length; j++) {
                    if(j % 2 == 0){
                        double gapAvg = 0;
                        double iGapAvg = 0;
                        for (int k = 0; k < gapInfos.size(); k++) {
                            gapAvg += gapInfos.get(k)[j];
                        }
                        gapAvg /= gapInfos.size();
                        for (int k = 0; k < IGapInfos.size(); k++) {
                            iGapAvg += IGapInfos.get(k)[j];
                        }
                        iGapAvg /= IGapInfos.size();
                        doublesAvg.add(gapAvg);
                        doublesAvg.add(iGapAvg);
                    }
                    if(j % 2 == 1){
                        double gapBest = 0;
                        double iGapBest = 0;
                        for (int k = 0; k < gapInfos.size(); k++) {
                            gapBest += gapInfos.get(k)[j];
                        }
                        gapBest /= gapInfos.size();
                        for (int k = 0; k < IGapInfos.size(); k++) {
                            iGapBest += IGapInfos.get(k)[j];
                        }
                        iGapBest /= IGapInfos.size();
                        doublesBest.add(gapBest);
                        doublesBest.add(iGapBest);
                    }
                }
                ProcessResData processResData = new ProcessResData(doublesAvg, doublesBest);
                data.dataResInfos.put(name, processResData);
            }
        }
        System.out.println(313);
        for(Map.Entry<String, ProcessResData> entry : data.dataResInfos.entrySet()){
            String key = entry.getKey();
            ProcessResData value = entry.getValue();
            System.out.println("Avg," + '\t' +key + '\t' + value.doublesAvg);

        }

        for(Map.Entry<String, ProcessResData> entry : data.dataResInfos.entrySet()){
            String key = entry.getKey();
            ProcessResData value = entry.getValue();

            System.out.println("Best," + '\t' +key + '\t' + value.doublesBest);
        }
    }


    public void readData(File fileData, Data data) throws FileNotFoundException {
        String line = null;
        String[] substr = null;
        String fileName = fileData.getName();
        data.fileNames.add(fileName);
        data.dataInfos.put(fileName, new ArrayList<double[]>());
        Scanner cin = new Scanner(new BufferedReader((new FileReader(fileData))));
        while (cin.hasNext()) {
            substr = cin.nextLine().trim().split("\\s+");
            double[] dataInfo = new double[]{Double.parseDouble(substr[6]), Double.parseDouble(substr[7]),
                    Double.parseDouble(substr[9]), Double.parseDouble(substr[10]),
                    Double.parseDouble(substr[12]), Double.parseDouble(substr[13]),
                    Double.parseDouble(substr[15]), Double.parseDouble(substr[16])};
            data.dataInfos.get(fileName).add(dataInfo);
        }
    }
}


class Data {
    List<String> fileNames;
    Map<String, List<double[]>> dataInfos;

    Map<String, ProcessResData> dataResInfos;

    public Data() {
    }

    public Data(List<String> fileNames, Map<String, List<double[]>> dataInfos, Map<String, ProcessResData> dataResInfos) {
        this.fileNames = fileNames;
        this.dataInfos = dataInfos;
        this.dataResInfos = dataResInfos;
    }
}
class ProcessResData{
    List<Double> doublesAvg;
    List<Double> doublesBest;

    public ProcessResData() {
    }

    public ProcessResData(List<Double> doublesAvg, List<Double> doublesBest) {
        this.doublesAvg = doublesAvg;
        this.doublesBest = doublesBest;
    }
}
