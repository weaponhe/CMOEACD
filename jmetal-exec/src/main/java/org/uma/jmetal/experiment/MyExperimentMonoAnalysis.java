package org.uma.jmetal.experiment;

import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.SolutionListUtils;
import org.uma.jmetal.util.fileoutput.SolutionListOutput;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;
import org.uma.jmetal.util.point.Point;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.zip.DeflaterOutputStream;

/**
 * Created by X250 on 2017/1/1.
 */
public class MyExperimentMonoAnalysis {
    Vector<MyExperimentMonoIndicator> indicators;
    String dataDirectory;
    String instance;
    public MyExperimentMonoAnalysis(String dataDirectory,String instance){
        this.dataDirectory = dataDirectory;
        this.instance = instance;
        indicators = new Vector<MyExperimentMonoIndicator>();
    }
    public void addIndicator(MyExperimentMonoIndicator indicator){indicators.add(indicator);}

    public  void printFinalSolutionSet(int run, List<? extends Solution<?>> population) {

        try{
            new SolutionListOutput(population)
                    .setSeparator(" ")
                    .printObjectivesToFile(dataDirectory+"/POP/"+instance+"R"+run+".pop");
        } catch (IOException e) {
            throw new JMetalException("Error writing data ", e) ;
        }

        new SolutionListOutput(SolutionListUtils.getNondominatedSolutions(population))
                .setSeparator(" ")
                .setVarFileOutputContext(new DefaultFileOutputContext(dataDirectory+"/POS/"+instance+"R"+run+".pos"))
                .setFunFileOutputContext(new DefaultFileOutputContext(dataDirectory+"/POF/"+instance+"R"+run+".pof"))
                .print();
    }

    public void saveIndicator(Vector<Integer> generation, Vector<Double> indicator, BufferedWriter writer){
        try {
            int num = generation.size();
            for (int i = 0; i < num; ++i) {
                writer.write(generation.get(i) + "," + indicator.get(i));
                writer.newLine();
            }
        }catch (IOException e){
        }
    }
    public void saveIndicators(Vector<Vector<Integer>> generations,Vector<Vector<Double>> indicators,BufferedWriter writer){
        try {
            int num = generations.size();
            for (int i = 0; i < num; ++i) {
                saveIndicator(generations.get(i),indicators.get(i),writer);
                writer.newLine();
            }
        }catch (IOException e){
        }
    }
    public void saveIndicator(Vector<Double> indicator,BufferedWriter writer){
        try {
            int num = indicator.size();
            for (int i = 0; i < num; ++i) {
                writer.write(""+indicator.get(i));
                writer.newLine();
            }
        }catch (IOException e){
        }
    }
    public void saveIndicators(Vector<Vector<Double>> indicators,BufferedWriter writer){
        try {
            int num = indicators.size();
            for (int i = 0; i < num; ++i) {
                saveIndicator(indicators.get(i),writer);
                writer.newLine();
            }
        }catch (IOException e){
        }
    }

    public void saveFront(Vector<Point> front, BufferedWriter writer){
        int num = front.size();
        if(num==0)
            return;
        int nObj = front.get(0).getNumberOfDimensions();
        try{
            for (int i = 0; i < num; ++i) {
                for (int j=0;j<nObj;++j){
                    writer.write(""+front.get(i).getDimensionValue(j)+" ");
                }
                writer.newLine();
            }
        }catch (IOException e){}
    }

    public double getAverage(Vector<Double> indicator){
        double sum = 0;
        for(int i=0 ; i<indicator.size();++i)
            sum += indicator.get(i).doubleValue();
        double average = sum/indicator.size();
        if(average > Double.MAX_VALUE)
            average = Double.MAX_VALUE;
        return average;
    }

    public void averageIndicators(Vector<Vector<Double>> indicators , Vector<Double> indicatorTrend){
        int run = indicators.size();
        int lines = indicators.get(0).size();
        for(int i=0;i<lines;++i){
            Vector<Double> tmp = new Vector<Double>();
            for(int j =0;j<run;++j)
                tmp.add(indicators.get(j).get(i));
            indicatorTrend.add(getAverage(tmp));
        }
    }

    public int getMinRun(Vector<Double> indicator){
        double minV = indicator.get(0);
        int minIdx = 0;
        for(int i=1;i<indicator.size();++i) {
            if (minV > indicator.get(i)) {
                minV = indicator.get(i);
                minIdx = i;
            }
        }
        return minIdx;
    }
    public double getMin(Vector<Double> indicator){
        double result = indicator.get(getMinRun(indicator));
        if(result > Double.MAX_VALUE)
            result = Double.MAX_VALUE;
        return result;
    }
    public double getMin(Vector<Double> indicator,int minIdx){
        return indicator.get(minIdx);
    }

    public int getMaxRun(Vector<Double> indicator){
        double maxV = indicator.get(0);
        int maxIdx = 0;
        for(int i=1;i<indicator.size();++i){
            if(maxV < indicator.get(i)){
                maxV = indicator.get(i);
                maxIdx = i;
            }
        }
        return maxIdx;
    }
    public double getMax(Vector<Double> indicator){
        double result = indicator.get(getMaxRun(indicator));
        if(result > Double.MAX_VALUE)
            result = Double.MAX_VALUE;
        return result;
    }
    public double getMax(Vector<Double> indicator,int maxIdx){
        return indicator.get(maxIdx);
    }

    public void getStatistics(Vector<Vector<Double>> indicators,Vector statistics){
        int run = indicators.size();
        int lines = indicators.get(0).size();
        Vector tmp = new Vector();
        for(int i=0;i<run;++i)
            tmp.add(indicators.get(i).get(lines-1));

        int minIdx = getMinRun(tmp);
        statistics.add(minIdx);
        statistics.add(getMin(tmp,minIdx));
        int maxIdx = getMaxRun(tmp);
        statistics.add(maxIdx);
        statistics.add(getMax(tmp,maxIdx));

        statistics.add(getAverage(tmp));
        Collections.sort(tmp);
        double median = ((Double)tmp.get((tmp.size()-1)/2)).doubleValue();
        if(tmp.size()%2 == 0) {
            median += ((Double) tmp.get(tmp.size()/2));
            median *= 0.5;
        }
        if(median > Double.MAX_VALUE)
            median = Double.MAX_VALUE;
        statistics.add(median);
    }

    public void analyzeIndicator(Vector<Vector<Double>> indicators,Vector statistics,Vector indicatorTrend){
        getStatistics(indicators,statistics);
        averageIndicators(indicators,indicatorTrend);
    }

    //analyzing all datas
    public void analyzingResults(){
        Vector<Vector<Integer>> Generates = new Vector<Vector<Integer>>();
        Vector<Vector<Double>> Optimums = new Vector<>();
        Vector<Vector<Double>> ErrorValues = new Vector<>();

        Vector<Double> times = new Vector<Double>();
        int run = indicators.size();
        for(int i =0; i<run ; ++i){
            Generates.add(indicators.get(i).getGeneration());
            Optimums.add(indicators.get(i).getOptimumList());
            ErrorValues.add(indicators.get(i).getErrorValueList());
            times.add(new Double(indicators.get(i).getComputingTimes()));
        }

        //Statistics
        Vector statOptimum = new Vector();
        Vector trendOptimum = new Vector();
        analyzeIndicator(Optimums,statOptimum,trendOptimum);
        Vector statError = new Vector();
        Vector trendError = new Vector();
        analyzeIndicator(ErrorValues,statError,trendError);

        //average computing time
        double avgTime = getAverage(times);

        //save to files
        try {
            Vector<Integer> tmp = indicators.get(0).getGeneration();
            Vector<Double> gens = new Vector<>();
            for(int i=0;i<tmp.size();++i)
                gens.add(tmp.get(i).doubleValue());
            //save generation
            BufferedWriter writerGen = new DefaultFileOutputContext(dataDirectory+"/GenData/gen_"+instance + ".csv").getFileWriter();
            saveIndicator(gens,writerGen);
            writerGen.close();
            //save fronts
            for(int i=0;i<run;++i){
                Vector<Vector<Point>> fronts = indicators.get(i).getFronts();
                for(int j=0;j<gens.size();++j){
                    BufferedWriter writerFront = new DefaultFileOutputContext(dataDirectory+"/GenData/"+instance + "R"+i+"_G_"+gens.get(j).intValue()+".pof").getFileWriter();
                    saveFront(fronts.get(j),writerFront);
                    writerFront.close();
                }
            }


                String indicatorName = "Optimum";
                BufferedWriter writer = new DefaultFileOutputContext(dataDirectory + "/"+indicatorName+"/" + instance + ".csv").getFileWriter();
                saveIndicators(Generates,Optimums,writer);
                writer.close();
                BufferedWriter writerTrend = new DefaultFileOutputContext(dataDirectory+"/"+indicatorName+"/trend_"+instance+".csv").getFileWriter();
                saveIndicator(Generates.get(0),trendOptimum,writerTrend);
                writerTrend.close();
                BufferedWriter writerStat = new DefaultFileOutputContext(dataDirectory+"/"+indicatorName+"/stat_"+instance+".csv").getFileWriter();
                saveIndicator(statOptimum,writerStat);
                writerStat.close();

            indicatorName = "Error";
            writer = new DefaultFileOutputContext(dataDirectory + "/"+indicatorName+"/" + instance + ".csv").getFileWriter();
            saveIndicators(Generates,ErrorValues,writer);
            writer.close();
            writerTrend = new DefaultFileOutputContext(dataDirectory+"/"+indicatorName+"/trend_"+instance+".csv").getFileWriter();
            saveIndicator(Generates.get(0),trendError,writerTrend);
            writerTrend.close();
            writerStat = new DefaultFileOutputContext(dataDirectory+"/"+indicatorName+"/stat_"+instance+".csv").getFileWriter();
            saveIndicator(statError,writerStat);
            writerStat.close();

            //Computing Time
            BufferedWriter writerTime = new DefaultFileOutputContext(dataDirectory+"/Time/"+instance + ".csv").getFileWriter();
            saveIndicator(times,writerTime);
            writerTime.close();
            BufferedWriter writerAvgTime = new DefaultFileOutputContext(dataDirectory+"/Time/avg_"+instance+".csv").getFileWriter();
            Vector<Double> avgVTime = new Vector();
            avgVTime.add(avgTime);
            saveIndicator(avgVTime,writerAvgTime);
            writerAvgTime.close();

            //print

            int finalGen = Generates.get(0).size()-1;
            String outputString ="\n" ;
            outputString += "Avg Time        : " + avgTime+" ms\n";
            JMetalLogger.logger.info(outputString);
        }catch (IOException e){ }

        statOptimum.clear();
        trendOptimum.clear();
        statError.clear();
        trendError.clear();
        times.clear();
        Optimums.clear();
        ErrorValues.clear();
        Generates.clear();

    }
}
