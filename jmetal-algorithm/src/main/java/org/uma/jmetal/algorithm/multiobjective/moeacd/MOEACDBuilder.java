package org.uma.jmetal.algorithm.multiobjective.moeacd;

import org.uma.jmetal.algorithm.multiobjective.moead.AbstractMOEAD;
import org.uma.jmetal.measure.impl.MyAlgorithmMeasures;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.operator.impl.crossover.SBXCrossover;
import org.uma.jmetal.operator.impl.mutation.PolynomialMutation;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.AlgorithmBuilder;

import java.util.List;


/**
 * Created by X250 on 2016/4/22.
 */
public class MOEACDBuilder implements AlgorithmBuilder<AbstractMOEACD> {
    public enum Variant {
        MOEACD, MOEACDMeasure,
        MOEACDP, MOEACDPMeasure,
        MOEACDN, MOEACDNMeasure,
        CMOEACD, CMOEACDMeasure,
        CMOEACDN, CMOEACDNMeasure,
        MOEACDD, MOEACDDMeasure,
        MOEACDND, MOEACDNDMeasure,
        CMOEACDD, CMOEACDDMeasure,
        CMOEACDND, CMOEACDNDMeasure,
        CMOEACDA, CMOEACDAMeasure,
        CMOEACDNA, CMOEACDNAMeasure,
        CMOEACDAD, CMOEACDADMeasure,
        CMOEACDNAD, CMOEACDNADMeasure,
        MOEACDDE, MOEACDDEMeasure,
        MOEACDF, MOEACDFMeasure,
        MOEACDSBX, MOEACDSBXMeasure,
        MOEACDSCV, MOEACDSCVMeasure,
        MOEACDPEH, MOEACDPEHMeasure,
        MOEACDNSBX, MOEACDNSBXMeasure,
        MOEACDNDE, MOEACDNDEMeasure,
        MOEACDNF, MOEACDNFMeasure,
        MOEACDACV, MOEACDACVMeasure,
        MOEACDAECV, MOEACDAECVMeasure,
        CMOEACDO, CMOEACDOMeasure,
        CMOEACDAO, CMOEACDAOMeasure,
        CMOEACDAOII, CMOEACDAOIIMeasure,
        CMOEACDAOD, CMOEACDAODMeasure,

        UCDEA, UCDEAMeasure,
        CUCDEA, CUCDEAMeasure,
        CUCDEAII, CUCDEAIIMeasure,

        CMOEACDII, CMOEACDIIMeasure,
        CMOEACDIII, CMOEACDIIIMeasure,
        CMOEACDIV, CMOEACDIVMeasure,
        CMOEACDV, CMOEACDVMeasure,
        CMOEACDVI, CMOEACDVIMeasure,
        CMOEACDVII, CMOEACDVIIMeasure,
        CMOEACDVIII, CMOEACDVIIIMeasure,

        CMOEACDAII, CMOEACDAIIMeasure,
        CMOEACDAIII, CMOEACDAIIIMeasure,
        CMOEACDAIV, CMOEACDAIVMeasure,
        CMOEACDAV, CMOEACDAVMeasure
    }

    ;
    protected Problem<DoubleSolution> problem;

    protected int[] numOfDivision;
    protected double[] integratedTaus;

    protected double pbi_theta;

    protected SBXCrossover sbxCrossover;
    protected DifferentialEvolutionCrossover deCrossover;
    protected MutationOperator<DoubleSolution> mutation;
    protected double neighborhoodSelectionProbability;
    protected AbstractMOEAD.FunctionType functionType;

    protected int populationSize;
    protected int constraintLayerSize;

    protected int neighborhoodSize;

    protected int maxEvaluations;

    protected int numberOfThreads;

    protected Variant moeacdVariant;

    protected MyAlgorithmMeasures<DoubleSolution> measureManager;

    protected int indicatorEvaluatingTimes;

    protected boolean strictlyFinalPopulationFlitting;

    protected List<double[]> predifineDirections = null;

    protected double c_uneven;

    /**
     * Constructor
     */
    public MOEACDBuilder(Problem<DoubleSolution> problem, Variant variant) {
        this.problem = problem;
        populationSize = 300;
        constraintLayerSize = 5;
        neighborhoodSize = 20;
        maxEvaluations = 150000;
        pbi_theta = 5.0;
        sbxCrossover = new SBXCrossover(1.0, 30.0);
        deCrossover = new DifferentialEvolutionCrossover(1.0, 0.5, "rand/1/bin");
        mutation = new PolynomialMutation(1.0 / problem.getNumberOfVariables(), 20.0);
        numberOfThreads = 1;
        moeacdVariant = variant;
        c_uneven = 1.04;
        measureManager = new MyAlgorithmMeasures<>();
        this.functionType = AbstractMOEAD.FunctionType.TCH;
    }

    public AbstractMOEAD.FunctionType getFunctionType() {
        return functionType;
    }

    public MOEACDBuilder setFunctionType(AbstractMOEAD.FunctionType functionType) {
        this.functionType = functionType;
        return this;
    }

    public int getMaxEvaluations() {
        return maxEvaluations;
    }

    public int getPopulationSize() {
        return populationSize;
    }

    public int getConstraintLayerSize() {
        return constraintLayerSize;
    }

    public int getNeighborhoodSize() {
        return neighborhoodSize;
    }

    public int[] getNumofDivision() {
        return this.numOfDivision;
    }

    public double[] getIntegratedTaus() {
        return this.integratedTaus;
    }

    public double getPBITheta() {
        return pbi_theta;
    }

    public MutationOperator<DoubleSolution> getMutation() {
        return mutation;
    }


    public SBXCrossover getSBXCrossover() {
        return sbxCrossover;
    }

    public DifferentialEvolutionCrossover getDECrossover() {
        return deCrossover;
    }


    public double getNeighborhoodSelectionProbability() {
        return neighborhoodSelectionProbability;
    }

    public int getNumberOfThreads() {
        return numberOfThreads;
    }

    public int getIndicatorEvaluatingTimes() {
        return this.indicatorEvaluatingTimes;
    }


    public boolean getStrictlyFinalPopulationFlitting() {
        return this.strictlyFinalPopulationFlitting;
    }

    public MOEACDBuilder setPopulationSize(int populationSize) {
        this.populationSize = populationSize;
        return this;
    }

    public MOEACDBuilder setConstraintLayerSize(int constraintLayerSize) {
        this.constraintLayerSize = constraintLayerSize;
        return this;
    }

    public MOEACDBuilder setNeighborhoodSize(int neighborhoodSize) {
        this.neighborhoodSize = neighborhoodSize;

        return this;
    }

    public MOEACDBuilder setNumOfDivision(int[] numOfDivision) {
        this.numOfDivision = numOfDivision;
        return this;
    }

    public MOEACDBuilder setIntegratedTaus(double[] integratedTaus) {
        this.integratedTaus = integratedTaus;
        return this;
    }

    public MOEACDBuilder setMaxEvaluations(int maxEvaluations) {
        this.maxEvaluations = maxEvaluations;

        return this;
    }

    public MOEACDBuilder setPBITheta(double theta) {
        this.pbi_theta = theta;

        return this;
    }


    public MOEACDBuilder setSBXCrossover(SBXCrossover crossover) {
        this.sbxCrossover = crossover;

        return this;
    }

    public MOEACDBuilder setDECrossover(DifferentialEvolutionCrossover crossover) {
        this.deCrossover = crossover;
        return this;
    }

    public MOEACDBuilder setMutation(MutationOperator<DoubleSolution> mutation) {
        this.mutation = mutation;

        return this;
    }

    public MOEACDBuilder setNeighborhoodSelectionProbability(double neighborhoodSelectionProbability) {
        this.neighborhoodSelectionProbability = neighborhoodSelectionProbability;

        return this;
    }

    public MOEACDBuilder setNumberOfThreads(int numberOfThreads) {
        this.numberOfThreads = numberOfThreads;

        return this;
    }

    public MOEACDBuilder setIndicatorEvaluatingTimes(int indicatorEvaluatingTimes) {
        this.indicatorEvaluatingTimes = indicatorEvaluatingTimes;
        return this;
    }

    public MOEACDBuilder setStrictlyFinalPopulationFlitting(boolean strictlyFinalPopulationFlitting) {
        this.strictlyFinalPopulationFlitting = strictlyFinalPopulationFlitting;
        return this;
    }

    public List<double[]> getPredifineDirections() {
        return predifineDirections;
    }

    public MOEACDBuilder setPredifineDirections(List<double[]> predefineDirections) {
        this.predifineDirections = predefineDirections;
        return this;
    }

    public double getC_UnEven() {
        return this.c_uneven;
    }

    public MOEACDBuilder setCUnEven(double c_uneven) {
        this.c_uneven = c_uneven;
        return this;
    }

    public MyAlgorithmMeasures<DoubleSolution> getMeasureManager() {
        return this.measureManager;
    }

    public MOEACDBuilder setMeasureManager(MyAlgorithmMeasures<DoubleSolution> measureManager) {
        this.measureManager = measureManager;
        return this;
    }

    public AbstractMOEACD build() {
        AbstractMOEACD algorithm = null;
        if (moeacdVariant.equals(Variant.MOEACD)) {
            algorithm = new MOEACD(problem, numOfDivision, integratedTaus,
                    populationSize, constraintLayerSize, maxEvaluations, neighborhoodSize,
                    neighborhoodSelectionProbability,functionType,
                    sbxCrossover, deCrossover, mutation);
        }
//        else if(moeacdVariant.equals(Variant.MOEACDMeasure)) {
//            algorithm = new MOEACD(measureManager,problem, numOfDivision, integratedTaus,
//                    populationSize, maxEvaluations,neighborhoodSize,
//                    neighborhoodSelectionProbability,
//                    sbxCrossover,deCrossover, mutation);
//        }else if (moeacdVariant.equals(Variant.MOEACDP)){
//            algorithm = new MOEACDP(problem,predifineDirections,
//                    populationSize,maxEvaluations,neighborhoodSize,
//                    neighborhoodSelectionProbability,
//                    sbxCrossover,deCrossover, mutation);
//        }else if(moeacdVariant.equals(Variant.MOEACDPMeasure)) {
//            algorithm = new MOEACDP(measureManager,problem, predifineDirections,
//                    populationSize, maxEvaluations,neighborhoodSize,
//                    neighborhoodSelectionProbability,
//                    sbxCrossover,deCrossover, mutation);
//        }
//        else if (moeacdVariant.equals(Variant.MOEACDN)){
//            algorithm = new MOEACDN(problem,numOfDivision, integratedTaus,
//                    populationSize,maxEvaluations,neighborhoodSize,
//                    neighborhoodSelectionProbability,
//                    sbxCrossover,deCrossover, mutation);
//        }else if(moeacdVariant.equals(Variant.MOEACDNMeasure)) {
//            algorithm = new MOEACDN(measureManager,problem, numOfDivision, integratedTaus,
//                    populationSize, maxEvaluations,neighborhoodSize,
//                    neighborhoodSelectionProbability,
//                    sbxCrossover,deCrossover, mutation);
//        }
//      else if (moeacdVariant.equals(Variant.CMOEACD)){
//            algorithm = new CMOEACD(problem,numOfDivision, integratedTaus,
//                    populationSize,maxEvaluations,neighborhoodSize,
//                    neighborhoodSelectionProbability,
//                    sbxCrossover,deCrossover, mutation);
//        }else if(moeacdVariant.equals(Variant.CMOEACDMeasure)) {
//            algorithm = new CMOEACD(measureManager,problem, numOfDivision, integratedTaus,
//                    populationSize, maxEvaluations,neighborhoodSize,
//                    neighborhoodSelectionProbability,
//                    sbxCrossover,deCrossover, mutation);
//        }
//        else if (moeacdVariant.equals(Variant.CMOEACDN)){
//            algorithm = new CMOEACDN(problem,numOfDivision, integratedTaus,
//                    populationSize,maxEvaluations,neighborhoodSize,
//                    neighborhoodSelectionProbability,
//                    sbxCrossover,deCrossover, mutation);
//        }else if(moeacdVariant.equals(Variant.CMOEACDNMeasure)) {
//            algorithm = new CMOEACDN(measureManager,problem, numOfDivision, integratedTaus,
//                    populationSize, maxEvaluations,neighborhoodSize,
//                    neighborhoodSelectionProbability,
//                    sbxCrossover,deCrossover, mutation);
//        } else if (moeacdVariant.equals(Variant.MOEACDD)){
//            algorithm = new MOEACDD(problem,numOfDivision, integratedTaus,
//                    populationSize,maxEvaluations,neighborhoodSize,
//                    neighborhoodSelectionProbability,
//                    sbxCrossover,deCrossover, mutation);
//        }else if(moeacdVariant.equals(Variant.MOEACDDMeasure)) {
//            algorithm = new MOEACDD(measureManager,problem, numOfDivision, integratedTaus,
//                    populationSize, maxEvaluations,neighborhoodSize,
//                    neighborhoodSelectionProbability,
//                    sbxCrossover,deCrossover, mutation);
//        }
//        else if (moeacdVariant.equals(Variant.MOEACDND)){
//            algorithm = new MOEACDND(problem,numOfDivision, integratedTaus,
//                    populationSize,maxEvaluations,neighborhoodSize,
//                    neighborhoodSelectionProbability,
//                    sbxCrossover,deCrossover, mutation);
//        }else if(moeacdVariant.equals(Variant.MOEACDNDMeasure)) {
//            algorithm = new MOEACDND(measureManager,problem, numOfDivision, integratedTaus,
//                    populationSize, maxEvaluations,neighborhoodSize,
//                    neighborhoodSelectionProbability,
//                    sbxCrossover,deCrossover, mutation);
//        }else if (moeacdVariant.equals(Variant.CMOEACDD)){
//            algorithm = new CMOEACDD(problem,numOfDivision, integratedTaus,
//                    populationSize,maxEvaluations,neighborhoodSize,
//                    neighborhoodSelectionProbability,
//                    sbxCrossover,deCrossover, mutation);
//        }else if(moeacdVariant.equals(Variant.CMOEACDDMeasure)) {
//            algorithm = new CMOEACDD(measureManager,problem, numOfDivision, integratedTaus,
//                    populationSize, maxEvaluations,neighborhoodSize,
//                    neighborhoodSelectionProbability,
//                    sbxCrossover,deCrossover, mutation);
//        }else  if (moeacdVariant.equals(Variant.CMOEACDND)){
//            algorithm = new CMOEACDND(problem,numOfDivision, integratedTaus,
//                    populationSize,maxEvaluations,neighborhoodSize,
//                    neighborhoodSelectionProbability,
//                    sbxCrossover,deCrossover, mutation);
//        }else if(moeacdVariant.equals(Variant.CMOEACDNDMeasure)) {
//            algorithm = new CMOEACDND(measureManager,problem, numOfDivision, integratedTaus,
//                    populationSize, maxEvaluations,neighborhoodSize,
//                    neighborhoodSelectionProbability,
//                    sbxCrossover,deCrossover, mutation);
//        }else if (moeacdVariant.equals(Variant.CMOEACDA)){
//            algorithm = new CMOEACDA(problem,numOfDivision, integratedTaus,
//                    populationSize,maxEvaluations,neighborhoodSize,
//                    neighborhoodSelectionProbability,
//                    sbxCrossover,deCrossover, mutation);
//        }else if(moeacdVariant.equals(Variant.CMOEACDAMeasure)) {
//            algorithm = new CMOEACDA(measureManager,problem, numOfDivision, integratedTaus,
//                    populationSize, maxEvaluations,neighborhoodSize,
//                    neighborhoodSelectionProbability,
//                    sbxCrossover,deCrossover, mutation);
//        }else if (moeacdVariant.equals(Variant.CMOEACDNA)){
//            algorithm = new CMOEACDNA(problem,numOfDivision, integratedTaus,
//                    populationSize,maxEvaluations,neighborhoodSize,
//                    neighborhoodSelectionProbability,
//                    sbxCrossover,deCrossover, mutation);
//        }else if(moeacdVariant.equals(Variant.CMOEACDNAMeasure)) {
//            algorithm = new CMOEACDNA(measureManager,problem, numOfDivision, integratedTaus,
//                    populationSize, maxEvaluations,neighborhoodSize,
//                    neighborhoodSelectionProbability,
//                    sbxCrossover,deCrossover, mutation);
//        }
//        else if (moeacdVariant.equals(Variant.CMOEACDAD)){
//            algorithm = new CMOEACDAD(problem,numOfDivision, integratedTaus,
//                    populationSize,maxEvaluations,neighborhoodSize,
//                    neighborhoodSelectionProbability,
//                    sbxCrossover,deCrossover, mutation);
//        }else if(moeacdVariant.equals(Variant.CMOEACDADMeasure)) {
//            algorithm = new CMOEACDAD(measureManager,problem, numOfDivision, integratedTaus,
//                    populationSize, maxEvaluations,neighborhoodSize,
//                    neighborhoodSelectionProbability,
//                    sbxCrossover,deCrossover, mutation);
//        }
//        else if (moeacdVariant.equals(Variant.CMOEACDNAD)){
//            algorithm = new CMOEACDNAD(problem,numOfDivision, integratedTaus,
//                    populationSize,maxEvaluations,neighborhoodSize,
//                    neighborhoodSelectionProbability,
//                    sbxCrossover,deCrossover, mutation);
//        }else if(moeacdVariant.equals(Variant.CMOEACDNADMeasure)) {
//            algorithm = new CMOEACDNAD(measureManager,problem, numOfDivision, integratedTaus,
//                    populationSize, maxEvaluations,neighborhoodSize,
//                    neighborhoodSelectionProbability,
//                    sbxCrossover,deCrossover, mutation);
//        }else if (moeacdVariant.equals(Variant.MOEACDDE)){
//            algorithm = new MOEACDDE(problem,numOfDivision, integratedTaus,
//                    populationSize,maxEvaluations,neighborhoodSize,
//                    neighborhoodSelectionProbability,
//                    deCrossover, mutation);
//        }else if(moeacdVariant.equals(Variant.MOEACDDEMeasure)) {
//            algorithm = new MOEACDDE(measureManager,problem, numOfDivision, integratedTaus,
//                    populationSize, maxEvaluations,neighborhoodSize,
//                    neighborhoodSelectionProbability,deCrossover, mutation);
//        }else if (moeacdVariant.equals(Variant.MOEACDF)){
//            algorithm = new MOEACDF(problem,numOfDivision, integratedTaus,
//                    populationSize,maxEvaluations,neighborhoodSize,
//                    neighborhoodSelectionProbability,
//                    sbxCrossover,deCrossover, mutation);
//        }else if(moeacdVariant.equals(Variant.MOEACDFMeasure)) {
//            algorithm = new MOEACDF(measureManager,problem, numOfDivision, integratedTaus,
//                    populationSize, maxEvaluations,neighborhoodSize,
//                    neighborhoodSelectionProbability,
//                    sbxCrossover,deCrossover, mutation);
//        }else if (moeacdVariant.equals(Variant.MOEACDSBX)){
//            algorithm = new MOEACDSBX(problem,numOfDivision, integratedTaus,
//                    populationSize,maxEvaluations,neighborhoodSize,
//                    neighborhoodSelectionProbability,
//                    sbxCrossover, mutation);
//        }else if(moeacdVariant.equals(Variant.MOEACDSBXMeasure)) {
//            algorithm = new MOEACDSBX(measureManager,problem, numOfDivision, integratedTaus,
//                    populationSize, maxEvaluations,neighborhoodSize,
//                    neighborhoodSelectionProbability,
//                    sbxCrossover, mutation);
//        }
//        else if (moeacdVariant.equals(Variant.MOEACDSCV)){
//            algorithm = new MOEACDSCV(problem,numOfDivision, integratedTaus,
//                    populationSize,maxEvaluations,neighborhoodSize,
//                    neighborhoodSelectionProbability,
//                    sbxCrossover,deCrossover, mutation);
//        }else if(moeacdVariant.equals(Variant.MOEACDSCVMeasure)) {
//            algorithm = new MOEACDSCV(measureManager,problem, numOfDivision, integratedTaus,
//                    populationSize, maxEvaluations,neighborhoodSize,
//                    neighborhoodSelectionProbability,
//                    sbxCrossover,deCrossover, mutation);
//        }
//
//        else if (moeacdVariant.equals(Variant.MOEACDPEH)){
//            algorithm = new MOEACDPEH(problem,numOfDivision, integratedTaus,
//                    populationSize,maxEvaluations,neighborhoodSize,
//                    neighborhoodSelectionProbability,
//                    sbxCrossover,deCrossover, mutation);
//        }else if(moeacdVariant.equals(Variant.MOEACDPEHMeasure)) {
//            algorithm = new MOEACDPEH(measureManager,problem, numOfDivision, integratedTaus,
//                    populationSize, maxEvaluations,neighborhoodSize,
//                    neighborhoodSelectionProbability,
//                    sbxCrossover,deCrossover, mutation);
//        }else if (moeacdVariant.equals(Variant.MOEACDNDE)){
//            algorithm = new MOEACDNDE(problem,numOfDivision, integratedTaus,
//                    populationSize,maxEvaluations,neighborhoodSize,
//                    neighborhoodSelectionProbability,
//                    deCrossover, mutation);
//        }else if(moeacdVariant.equals(Variant.MOEACDNDEMeasure)) {
//            algorithm = new MOEACDNDE(measureManager,problem, numOfDivision, integratedTaus,
//                    populationSize, maxEvaluations,neighborhoodSize,
//                    neighborhoodSelectionProbability,deCrossover, mutation);
//        }else if (moeacdVariant.equals(Variant.MOEACDNF)){
//            algorithm = new MOEACDNF(problem,numOfDivision, integratedTaus,
//                    populationSize,maxEvaluations,neighborhoodSize,
//                    neighborhoodSelectionProbability,
//                    sbxCrossover,deCrossover, mutation);
//        }else if(moeacdVariant.equals(Variant.MOEACDNFMeasure)) {
//            algorithm = new MOEACDNF(measureManager,problem, numOfDivision, integratedTaus,
//                    populationSize, maxEvaluations,neighborhoodSize,
//                    neighborhoodSelectionProbability,
//                    sbxCrossover,deCrossover, mutation);
//        }else if (moeacdVariant.equals(Variant.MOEACDNSBX)){
//            algorithm = new MOEACDNSBX(problem,numOfDivision, integratedTaus,
//                    populationSize,maxEvaluations,neighborhoodSize,
//                    neighborhoodSelectionProbability,
//                    sbxCrossover, mutation);
//        }else if(moeacdVariant.equals(Variant.MOEACDNSBXMeasure)) {
//            algorithm = new MOEACDNSBX(measureManager,problem, numOfDivision, integratedTaus,
//                    populationSize, maxEvaluations,neighborhoodSize,
//                    neighborhoodSelectionProbability,
//                    sbxCrossover, mutation);
//        }
//
//        else if (moeacdVariant.equals(Variant.MOEACDACV)){
//            algorithm = new MOEACDACV(problem,numOfDivision, integratedTaus,
//                    populationSize,maxEvaluations,neighborhoodSize,
//                    neighborhoodSelectionProbability,
//                    sbxCrossover,deCrossover, mutation);
//        }else if(moeacdVariant.equals(Variant.MOEACDACVMeasure)) {
//            algorithm = new MOEACDACV(measureManager,problem, numOfDivision, integratedTaus,
//                    populationSize, maxEvaluations,neighborhoodSize,
//                    neighborhoodSelectionProbability,
//                    sbxCrossover,deCrossover, mutation);
//        }
//        else if (moeacdVariant.equals(Variant.MOEACDAECV)){
//            algorithm = new MOEACDAECV(problem,numOfDivision, integratedTaus,
//                    populationSize,maxEvaluations,neighborhoodSize,
//                    neighborhoodSelectionProbability,
//                    sbxCrossover,deCrossover, mutation);
//        }else if(moeacdVariant.equals(Variant.MOEACDAECVMeasure)) {
//            algorithm = new MOEACDAECV(measureManager,problem, numOfDivision, integratedTaus,
//                    populationSize, maxEvaluations,neighborhoodSize,
//                    neighborhoodSelectionProbability,
//                    sbxCrossover,deCrossover, mutation);
//        }else if (moeacdVariant.equals(Variant.CMOEACDO)){
//            algorithm = new CMOEACDO(problem,numOfDivision, integratedTaus,
//                    populationSize,maxEvaluations,neighborhoodSize,
//                    neighborhoodSelectionProbability,
//                    sbxCrossover,deCrossover, mutation);
//        }else if(moeacdVariant.equals(Variant.CMOEACDOMeasure)) {
//            algorithm = new CMOEACDO(measureManager,problem, numOfDivision, integratedTaus,
//                    populationSize, maxEvaluations,neighborhoodSize,
//                    neighborhoodSelectionProbability,
//                    sbxCrossover,deCrossover, mutation);
//        }
//        else if (moeacdVariant.equals(Variant.CMOEACDAO)){
//            algorithm = new CMOEACDAO(problem,numOfDivision, integratedTaus,
//                    populationSize,maxEvaluations,neighborhoodSize,
//                    neighborhoodSelectionProbability,
//                    sbxCrossover,deCrossover, mutation);
//        }else if(moeacdVariant.equals(Variant.CMOEACDAOMeasure)) {
//            algorithm = new CMOEACDAO(measureManager,problem, numOfDivision, integratedTaus,
//                    populationSize, maxEvaluations,neighborhoodSize,
//                    neighborhoodSelectionProbability,
//                    sbxCrossover,deCrossover, mutation);
//        }else if (moeacdVariant.equals(Variant.CMOEACDAOII)){
//            algorithm = new CMOEACDAOII(problem,numOfDivision, integratedTaus,
//                    populationSize,maxEvaluations,neighborhoodSize,
//                    neighborhoodSelectionProbability,
//                    sbxCrossover,deCrossover, mutation);
//        }else if(moeacdVariant.equals(Variant.CMOEACDAOIIMeasure)) {
//            algorithm = new CMOEACDAOII(measureManager,problem, numOfDivision, integratedTaus,
//                    populationSize, maxEvaluations,neighborhoodSize,
//                    neighborhoodSelectionProbability,
//                    sbxCrossover,deCrossover, mutation);
//        }else if (moeacdVariant.equals(Variant.CMOEACDAOD)){
//            algorithm = new CMOEACDAOD(problem,numOfDivision, integratedTaus,
//                    populationSize,maxEvaluations,neighborhoodSize,
//                    neighborhoodSelectionProbability,
//                    sbxCrossover,deCrossover, mutation);
//        }else if(moeacdVariant.equals(Variant.CMOEACDAODMeasure)) {
//            algorithm = new CMOEACDAOD(measureManager,problem, numOfDivision, integratedTaus,
//                    populationSize, maxEvaluations,neighborhoodSize,
//                    neighborhoodSelectionProbability,
//                    sbxCrossover,deCrossover, mutation);
//        }
//
//
//        else if (moeacdVariant.equals(Variant.UCDEA)){
//            algorithm = new UCDEA(problem,numOfDivision, integratedTaus,
//                    populationSize,maxEvaluations,neighborhoodSize,
//                    neighborhoodSelectionProbability,
//                    sbxCrossover,deCrossover, mutation,c_uneven);
//        }else if(moeacdVariant.equals(Variant.UCDEAMeasure)) {
//            algorithm = new UCDEA(measureManager,problem, numOfDivision, integratedTaus,
//                    populationSize, maxEvaluations,neighborhoodSize,
//                    neighborhoodSelectionProbability,
//                    sbxCrossover,deCrossover, mutation,c_uneven);
//        }
//        else if (moeacdVariant.equals(Variant.CUCDEA)){
//            algorithm = new CUCDEA(problem,numOfDivision, integratedTaus,
//                    populationSize,maxEvaluations,neighborhoodSize,
//                    neighborhoodSelectionProbability,
//                    sbxCrossover,deCrossover, mutation,c_uneven);
//        }else if(moeacdVariant.equals(Variant.CUCDEAMeasure)) {
//            algorithm = new CUCDEA(measureManager,problem, numOfDivision, integratedTaus,
//                    populationSize, maxEvaluations,neighborhoodSize,
//                    neighborhoodSelectionProbability,
//                    sbxCrossover,deCrossover, mutation,c_uneven);
//        }
//        else if (moeacdVariant.equals(Variant.CUCDEAII)){
//            algorithm = new CUCDEAII(problem,numOfDivision, integratedTaus,
//                    populationSize,maxEvaluations,neighborhoodSize,
//                    neighborhoodSelectionProbability,
//                    sbxCrossover,deCrossover, mutation,c_uneven);
//        }else if(moeacdVariant.equals(Variant.CUCDEAIIMeasure)) {
//            algorithm = new CUCDEAII(measureManager,problem, numOfDivision, integratedTaus,
//                    populationSize, maxEvaluations,neighborhoodSize,
//                    neighborhoodSelectionProbability,
//                    sbxCrossover,deCrossover, mutation,c_uneven);
//        }
//
//        else if (moeacdVariant.equals(Variant.CMOEACDII)){
//            algorithm = new CMOEACDII(problem,numOfDivision, integratedTaus,
//                    populationSize,maxEvaluations,neighborhoodSize,
//                    neighborhoodSelectionProbability,
//                    sbxCrossover,deCrossover, mutation);
//        }else if(moeacdVariant.equals(Variant.CMOEACDIIMeasure)) {
//            algorithm = new CMOEACDII(measureManager,problem, numOfDivision, integratedTaus,
//                    populationSize, maxEvaluations,neighborhoodSize,
//                    neighborhoodSelectionProbability,
//                    sbxCrossover,deCrossover, mutation);
//        }
//        else if (moeacdVariant.equals(Variant.CMOEACDIII)){
//            algorithm = new CMOEACDIII(problem,numOfDivision, integratedTaus,
//                    populationSize,maxEvaluations,neighborhoodSize,
//                    neighborhoodSelectionProbability,
//                    sbxCrossover,deCrossover, mutation);
//        }else if(moeacdVariant.equals(Variant.CMOEACDIIIMeasure)) {
//            algorithm = new CMOEACDIII(measureManager,problem, numOfDivision, integratedTaus,
//                    populationSize, maxEvaluations,neighborhoodSize,
//                    neighborhoodSelectionProbability,
//                    sbxCrossover,deCrossover, mutation);
//        }
//        else if (moeacdVariant.equals(Variant.CMOEACDIV)){
//            algorithm = new CMOEACDIV(problem,numOfDivision, integratedTaus,
//                    populationSize,maxEvaluations,neighborhoodSize,
//                    neighborhoodSelectionProbability,
//                    sbxCrossover,deCrossover, mutation);
//        }else if(moeacdVariant.equals(Variant.CMOEACDIVMeasure)) {
//            algorithm = new CMOEACDIV(measureManager,problem, numOfDivision, integratedTaus,
//                    populationSize, maxEvaluations,neighborhoodSize,
//                    neighborhoodSelectionProbability,
//                    sbxCrossover,deCrossover, mutation);
//        }
//        else if (moeacdVariant.equals(Variant.CMOEACDV)){
//            algorithm = new CMOEACDV(problem,numOfDivision, integratedTaus,
//                    populationSize,maxEvaluations,neighborhoodSize,
//                    neighborhoodSelectionProbability,
//                    sbxCrossover,deCrossover, mutation);
//        }else if(moeacdVariant.equals(Variant.CMOEACDVMeasure)) {
//            algorithm = new CMOEACDV(measureManager,problem, numOfDivision, integratedTaus,
//                    populationSize, maxEvaluations,neighborhoodSize,
//                    neighborhoodSelectionProbability,
//                    sbxCrossover,deCrossover, mutation);
//        }else if (moeacdVariant.equals(Variant.CMOEACDVI)){
//            algorithm = new CMOEACDVI(problem,numOfDivision, integratedTaus,
//                    populationSize,maxEvaluations,neighborhoodSize,
//                    neighborhoodSelectionProbability,
//                    sbxCrossover,deCrossover, mutation);
//        }else if(moeacdVariant.equals(Variant.CMOEACDVIMeasure)) {
//            algorithm = new CMOEACDVI(measureManager,problem, numOfDivision, integratedTaus,
//                    populationSize, maxEvaluations,neighborhoodSize,
//                    neighborhoodSelectionProbability,
//                    sbxCrossover,deCrossover, mutation);
//        }else if (moeacdVariant.equals(Variant.CMOEACDVII)){
//            algorithm = new CMOEACDVII(problem,numOfDivision, integratedTaus,
//                    populationSize,maxEvaluations,neighborhoodSize,
//                    neighborhoodSelectionProbability,
//                    sbxCrossover,deCrossover, mutation);
//        }else if(moeacdVariant.equals(Variant.CMOEACDVIIMeasure)) {
//            algorithm = new CMOEACDVII(measureManager,problem, numOfDivision, integratedTaus,
//                    populationSize, maxEvaluations,neighborhoodSize,
//                    neighborhoodSelectionProbability,
//                    sbxCrossover,deCrossover, mutation);
//        }else if (moeacdVariant.equals(Variant.CMOEACDVIII)){
//            algorithm = new CMOEACDVIII(problem,numOfDivision, integratedTaus,
//                    populationSize,maxEvaluations,neighborhoodSize,
//                    neighborhoodSelectionProbability,
//                    sbxCrossover,deCrossover, mutation);
//        }else if(moeacdVariant.equals(Variant.CMOEACDVIIIMeasure)) {
//            algorithm = new CMOEACDVIII(measureManager,problem, numOfDivision, integratedTaus,
//                    populationSize, maxEvaluations,neighborhoodSize,
//                    neighborhoodSelectionProbability,
//                    sbxCrossover,deCrossover, mutation);
//        }
        return algorithm;
    }
}
