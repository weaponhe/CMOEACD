package org.uma.jmetal.experiment;


import org.uma.jmetal.solution.DoubleBinarySolution;
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

/**
 * Created by X250 on 2016/4/9.
 */
public class MyExperimentAnalysis {
    Vector<MyExperimentIndicator> indicators;
    String dataDirectory;
    String instance;
    public MyExperimentAnalysis(String dataDirectory,String instance){
        this.dataDirectory = dataDirectory;
        this.instance = instance;
        indicators = new Vector<MyExperimentIndicator>();
    }
    public void addIndicator(MyExperimentIndicator indicator){indicators.add(indicator);}

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
        return sum/indicator.size();
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
        return indicator.get(getMinRun(indicator));
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
        return indicator.get(getMaxRun(indicator));
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
        statistics.add(median);
    }

    public void analyzeIndicator(Vector<Vector<Double>> indicators,Vector statistics,Vector indicatorTrend){
        getStatistics(indicators,statistics);
        averageIndicators(indicators,indicatorTrend);
    }

    //analyzing all datas
    public void analyzingResults(){
        Vector<Vector<Integer>> Generates = new Vector<Vector<Integer>>();
        Vector<Vector<Vector<Double>>> totalIndicators = new Vector<Vector<Vector<Double>>>();
        for(int j = 0;j<indicators.get(0).getIndicatorNumer();++j) {
            totalIndicators.add(new Vector<Vector<Double>>());
        }
//        Vector<Vector<Double>> Hypervolumes = new Vector<Vector<Double>>();
//        Vector<Vector<Double>> EPSs = new Vector<Vector<Double>>();
//        Vector<Vector<Double>> GDs = new Vector<Vector<Double>>();
//        Vector<Vector<Double>> IGDs = new Vector<Vector<Double>>();
//        Vector<Vector<Double>> IGDPluss = new Vector<Vector<Double>>();
//        Vector<Vector<Double>> spreads = new Vector<Vector<Double>>();
//        Vector<Vector<Double>> errors = new Vector<Vector<Double>>();
        Vector<Double> times = new Vector<Double>();
        int run = indicators.size();
        for(int i =0; i<run ; ++i){
            Generates.add(indicators.get(i).getGeneration());
            for(int j = 0;j<indicators.get(i).getIndicatorNumer();++j) {
                totalIndicators.get(j).add(indicators.get(i).getIndicator(j));
            }
//            Hypervolumes.add(indicators.get(i).getHypervolume());
//            EPSs.add(indicators.get(i).getEPS());
//            GDs.add(indicators.get(i).getGD());
//            IGDs.add(indicators.get(i).getIGD());
//            IGDPluss.add(indicators.get(i).getIGDPlus());
//            spreads.add(indicators.get(i).getSpread());
//            errors.add(indicators.get(i).getErrorRatio());
            times.add(new Double(indicators.get(i).getComputingTimes()));
        }

        //Statistics
        Vector<Vector> statIndicators = new Vector<Vector>();
        Vector<Vector> trendIndicators = new Vector<Vector>();
        for(int i=0;i<totalIndicators.size();++i){
            statIndicators.add(new Vector());
            trendIndicators.add(new Vector());
            analyzeIndicator(totalIndicators.get(i),statIndicators.get(i),trendIndicators.get(i));
        }
//        Vector statHypervolume = new Vector();
//        Vector trendHypervolume = new Vector<Double>();
//        analyzeIndicator(Hypervolumes,statHypervolume,trendHypervolume);
//
//        Vector statEPS = new Vector();
//        Vector trendEPS = new Vector<Double>();
//        analyzeIndicator(EPSs,statEPS,trendEPS);
//
//        Vector statGD = new Vector();
//        Vector trendGD = new Vector<Double>();
//        analyzeIndicator(GDs,statGD,trendGD);
//
//        Vector statIGD = new Vector();
//        Vector trendIGD = new Vector<Double>();
//        analyzeIndicator(IGDs,statIGD,trendIGD);
//
//        Vector statIGDPlus = new Vector();
//        Vector trendIGDPlus = new Vector<Double>();
//        analyzeIndicator(IGDPluss,statIGDPlus,trendIGDPlus);
//
//        Vector statSpread = new Vector();
//        Vector trendSpread = new Vector<Double>();
//        analyzeIndicator(spreads,statSpread,trendSpread);
//
//        Vector statError = new Vector();
//        Vector trendError = new Vector<Double>();
//        analyzeIndicator(errors,statError,trendError);

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

            for(int i=0;i<totalIndicators.size();++i){
                String indicatorName = indicators.get(0).getIndicatorEvaluator(i).getName();
                BufferedWriter writer = new DefaultFileOutputContext(dataDirectory + "/"+indicatorName+"/" + instance + ".csv").getFileWriter();
                saveIndicators(Generates,totalIndicators.get(i),writer);
                writer.close();
                BufferedWriter writerTrend = new DefaultFileOutputContext(dataDirectory+"/"+indicatorName+"/trend_"+instance+".csv").getFileWriter();
                saveIndicator(Generates.get(0),trendIndicators.get(i),writerTrend);
                writerTrend.close();
                BufferedWriter writerStat = new DefaultFileOutputContext(dataDirectory+"/"+indicatorName+"/stat_"+instance+".csv").getFileWriter();
                saveIndicator(statIndicators.get(i),writerStat);
                writerStat.close();
            }
//            //Hypervolume
//            BufferedWriter writerHypervolume = new DefaultFileOutputContext(dataDirectory + "/HV/" + instance + ".csv").getFileWriter();
//            saveIndicators(Generates,Hypervolumes,writerHypervolume);
//            writerHypervolume.close();
//            BufferedWriter writerTrendHypervolume = new DefaultFileOutputContext(dataDirectory+"/HV/trend_"+instance+".csv").getFileWriter();
//            saveIndicator(Generates.get(0),trendHypervolume,writerTrendHypervolume);
//            writerTrendHypervolume.close();
//            BufferedWriter writerStatHypervolume = new DefaultFileOutputContext(dataDirectory+"/HV/stat_"+instance+".csv").getFileWriter();
//            saveIndicator(statHypervolume,writerStatHypervolume);
//            writerStatHypervolume.close();
//
//            //EPS
//            BufferedWriter writerEPS = new DefaultFileOutputContext(dataDirectory + "/EPS/" + instance + ".csv").getFileWriter();
//            saveIndicators(Generates,EPSs,writerEPS);
//            writerEPS.close();
//            BufferedWriter writerTrendEPS = new DefaultFileOutputContext(dataDirectory+"/EPS/trend_"+instance+".csv").getFileWriter();
//            saveIndicator(Generates.get(0),trendEPS,writerTrendEPS);
//            writerTrendEPS.close();
//            BufferedWriter writerStatEPS = new DefaultFileOutputContext(dataDirectory+"/EPS/stat_"+instance+".csv").getFileWriter();
//            saveIndicator(statEPS,writerStatEPS);
//            writerStatEPS.close();
//
//            //GD
//            BufferedWriter writerGD = new DefaultFileOutputContext(dataDirectory + "/GD/" + instance + ".csv").getFileWriter();
//            saveIndicators(Generates,GDs,writerGD);
//            writerGD.close();
//            BufferedWriter writerTrendGD = new DefaultFileOutputContext(dataDirectory+"/GD/trend_"+instance+".csv").getFileWriter();
//            saveIndicator(Generates.get(0),trendGD,writerTrendGD);
//            writerTrendGD.close();
//            BufferedWriter writerStatGD = new DefaultFileOutputContext(dataDirectory+"/GD/stat_"+instance+".csv").getFileWriter();
//            saveIndicator(statGD,writerStatGD);
//            writerStatGD.close();
//
//            //IGD
//            BufferedWriter writerIGD = new DefaultFileOutputContext(dataDirectory + "/IGD/" + instance + ".csv").getFileWriter();
//            saveIndicators(Generates,IGDs,writerIGD);
//            writerIGD.close();
//            BufferedWriter writerTrendIGD = new DefaultFileOutputContext(dataDirectory+"/IGD/trend_"+instance+".csv").getFileWriter();
//            saveIndicator(Generates.get(0),trendIGD,writerTrendIGD);
//            writerTrendIGD.close();
//            BufferedWriter writerStatIGD = new DefaultFileOutputContext(dataDirectory+"/IGD/stat_"+instance+".csv").getFileWriter();
//            saveIndicator(statIGD,writerStatIGD);
//            writerStatIGD.close();
//
//            //IGD Plus
//            BufferedWriter writerIGDPlus = new DefaultFileOutputContext(dataDirectory + "/IGDPlus/" + instance + ".csv").getFileWriter();
//            saveIndicators(Generates,IGDPluss,writerIGDPlus);
//            writerIGDPlus.close();
//            BufferedWriter writerTrendIGDPlus = new DefaultFileOutputContext(dataDirectory+"/IGDPlus/trend_"+instance+".csv").getFileWriter();
//            saveIndicator(Generates.get(0),trendIGDPlus,writerTrendIGDPlus);
//            writerTrendIGDPlus.close();
//            BufferedWriter writerStatIGDPlus = new DefaultFileOutputContext(dataDirectory+"/IGDPlus/stat_"+instance+".csv").getFileWriter();
//            saveIndicator(statIGDPlus,writerStatIGDPlus);
//            writerStatIGDPlus.close();
//
//            //Spread
//            BufferedWriter writerSpread = new DefaultFileOutputContext(dataDirectory + "/Spread/" + instance + ".csv").getFileWriter();
//            saveIndicators(Generates,spreads,writerSpread);
//            writerSpread.close();
//            BufferedWriter writerTrendSpread = new DefaultFileOutputContext(dataDirectory+"/Spread/trend_"+instance+".csv").getFileWriter();
//            saveIndicator(Generates.get(0),trendSpread,writerTrendSpread);
//            writerTrendSpread.close();
//            BufferedWriter writerStatSpread = new DefaultFileOutputContext(dataDirectory+"/Spread/stat_"+instance+".csv").getFileWriter();
//            saveIndicator(statSpread,writerStatSpread);
//            writerStatSpread.close();
//
//            //Error Rate
//            BufferedWriter writerError = new DefaultFileOutputContext(dataDirectory + "/Error/" + instance + ".csv").getFileWriter();
//            saveIndicators(Generates,errors,writerError);
//            writerError.close();
//            BufferedWriter writerTrendError = new DefaultFileOutputContext(dataDirectory+"/Error/trend_"+instance+".csv").getFileWriter();
//            saveIndicator(Generates.get(0),trendError,writerTrendError);
//            writerTrendError.close();
//            BufferedWriter writerStatError = new DefaultFileOutputContext(dataDirectory+"/Error/stat_"+instance+".csv").getFileWriter();
//            saveIndicator(statError,writerStatError);
//            writerStatError.close();

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
            for (int i=0;i<totalIndicators.size();++i){
                String indicatorName = indicators.get(0).getIndicatorEvaluator(i).getName();
                outputString += indicatorName + "\t: [min  "+statIndicators.get(i).get(0)+"]"+statIndicators.get(i).get(1)+"\t[avg]"+statIndicators.get(i).get(4)+"\t[median]"+statIndicators.get(i).get(5)+"\t[max "+statIndicators.get(i).get(2)+"]"+statIndicators.get(i).get(3)+"\n";
            }
//            outputString += "Hypervolume     : [avg]" + statHypervolume.get(0)+"\t[min]"+statHypervolume.get(2)+"\t[max]"+statHypervolume.get(4)+"\n";
//            outputString += "Epsilon         : [avg]" + statEPS.get(0)+"\t[min]"+statEPS.get(2)+"\t[max]"+statEPS.get(4)+"\n";
//            outputString += "GD              : [avg]" + statGD.get(0)+"\t[min]"+statGD.get(2)+"\t[max]"+statGD.get(4)+"\n";
//            outputString += "IGD             : [avg]" + statIGD.get(0)+"\t[min]"+statIGD.get(2)+"\t[max]"+statIGD.get(4)+"\n";
//            outputString += "IGD+            : [avg]" + statIGDPlus.get(0)+"\t[min]"+statIGDPlus.get(2)+"\t[max]"+statIGDPlus.get(4)+"\n";
//            outputString += "Spread          : [avg]" + statSpread.get(0)+"\t[min]"+statSpread.get(2)+"\t[max]"+statSpread.get(4)+"\n";
//            outputString += "Error ratio     : [avg]" + statError.get(0)+"\t[min]"+statError.get(2)+"\t[max]"+statError.get(4)+"\n";
            outputString += "Avg Time        : " + avgTime+" ms\n";
            JMetalLogger.logger.info(outputString);
        }catch (IOException e){ }

        statIndicators.clear();
        trendIndicators.clear();
        times.clear();
        totalIndicators.clear();
        Generates.clear();

    }
}
