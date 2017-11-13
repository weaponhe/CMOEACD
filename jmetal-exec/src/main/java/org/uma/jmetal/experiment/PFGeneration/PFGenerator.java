package org.uma.jmetal.experiment.PFGeneration;

import org.apache.commons.math3.optim.nonlinear.scalar.LineSearch;
import org.uma.jmetal.util.UniformSimplexCentroidWeightsUtils;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by X250 on 2016/7/24.
 */
public abstract class PFGenerator {
    public enum UNIFORMTYPE{SIMPLEX,CENTROID};
    protected UNIFORMTYPE type = UNIFORMTYPE.CENTROID;
    protected int nObj;
    protected int popSize;
    protected int H;
    protected int[] arrayH;
    protected double[] arrayTao;

    protected String instanceName;

    protected UniformSimplexCentroidWeightsUtils uniformWeightsManager;

    protected List<double[]> weights = null;
    protected List<double[]> pf = null;

    public PFGenerator(int _popSize, int _nObj, int _H) {
        popSize = _popSize;
        nObj = _nObj;
        H = _H;
        uniformWeightsManager = new UniformSimplexCentroidWeightsUtils(nObj, H);
        type = UNIFORMTYPE.CENTROID;
    }

    public PFGenerator(int _popSize, int _nObj, int[] _arrayH,double[] _arrayTao) {
        popSize = _popSize;
        nObj = _nObj;
        arrayH = _arrayH;
        arrayTao = _arrayTao;
        type = UNIFORMTYPE.SIMPLEX;
    }

    public void printPF(String _dir) {
        String fileName =  _dir+"/" + instanceName +"." + nObj + "D.pf["+pf.size()+"]";
        BufferedWriter writer = new DefaultFileOutputContext(fileName).getFileWriter();
       try {
            for (int i = 0; i < pf.size(); ++i) {
                for(int j=0;j<nObj;j++){
                    writer.write(pf.get(i)[j]+" ");
                }
                writer.newLine();
            }
           writer.close();
        }catch (IOException e){
        }

    }

    public abstract void generatePF();

    public void setInstanceName(String _instanceName) {
        instanceName = _instanceName;
    }

    public String getInstanceName() {
        return instanceName;
    }
}
