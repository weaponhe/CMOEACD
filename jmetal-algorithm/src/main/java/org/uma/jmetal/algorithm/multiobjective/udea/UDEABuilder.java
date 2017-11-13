package org.uma.jmetal.algorithm.multiobjective.udea;

import org.uma.jmetal.algorithm.multiobjective.moeacd.*;
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
 * Created by X250 on 2017/1/3.
 */
public class UDEABuilder implements AlgorithmBuilder<UDEA> {
    public enum Variant {
        UDEA,UDEAMeasure,
        CUDEA,CUDEAMeasure,
        CUDEAII,CUDEAIIMeasure
    };
    protected Problem<DoubleSolution> problem ;

    protected int[] H;
    protected double[] Tau;

    protected SBXCrossover sbxCrossover;
    protected DifferentialEvolutionCrossover deCrossover;
    protected MutationOperator<DoubleSolution> mutation;
    protected double neighborhoodSelectionProbability;
    protected int populationSize;

    protected int neighborhoodSize;

    protected int maxEvaluations;

    protected int numberOfThreads ;

    protected Variant udeaVariant ;

    protected MyAlgorithmMeasures<DoubleSolution> measureManager;

    protected int indicatorEvaluatingTimes;

    protected boolean strictlyFinalPopulationFlitting;

    protected List<double[]> predifineDirections = null;

    /** Constructor */
    public UDEABuilder(Problem<DoubleSolution> problem, Variant variant) {
        this.problem = problem ;
        populationSize = 300 ;
        neighborhoodSize = 20;
        maxEvaluations = 150000 ;
        sbxCrossover = new SBXCrossover(1.0,30.0) ;
        deCrossover = new DifferentialEvolutionCrossover(1.0,0.5,"rand/1/bin");
        mutation = new PolynomialMutation(1.0/problem.getNumberOfVariables(), 20.0);
        numberOfThreads = 1 ;
        udeaVariant = variant ;
        measureManager = new MyAlgorithmMeasures<>();
    }

    /* Getters/Setters */
    public int getMaxEvaluations() {
        return maxEvaluations;
    }

    public int getPopulationSize() {
        return populationSize;
    }

    public int getNeighborhoodSize(){return neighborhoodSize;}

    public int[] getH(){return this.H;}

    public double[] getTau(){return this.Tau;}


    public MutationOperator<DoubleSolution> getMutation() {
        return mutation;
    }


    public SBXCrossover getSBXCrossover() {return  sbxCrossover;}

    public DifferentialEvolutionCrossover getDECrossover() {return deCrossover;}


    public double getNeighborhoodSelectionProbability(){return neighborhoodSelectionProbability;}

    public int getNumberOfThreads() {
        return numberOfThreads ;
    }

    public int getIndicatorEvaluatingTimes(){return this.indicatorEvaluatingTimes;}


    public boolean getStrictlyFinalPopulationFlitting(){return  this.strictlyFinalPopulationFlitting;}

    public UDEABuilder setPopulationSize(int populationSize) {
        this.populationSize = populationSize;
        return this;
    }

    public UDEABuilder setNeighborhoodSize(int neighborhoodSize){
        this.neighborhoodSize = neighborhoodSize;

        return this;
    }

    public  UDEABuilder setNumOfDivision(int[] H){
        this.H = H;
        return this;
    }

    public UDEABuilder setIntegratedTaus(double[] Tau){
        this.Tau = Tau;
        return this;
    }
    public UDEABuilder setMaxEvaluations(int maxEvaluations) {
        this.maxEvaluations = maxEvaluations;

        return this;
    }


    public UDEABuilder setSBXCrossover(SBXCrossover crossover){
        this.sbxCrossover = crossover;

        return this;
    }

    public UDEABuilder setDECrossover(DifferentialEvolutionCrossover crossover){
        this.deCrossover = crossover;
        return this;
    }

    public UDEABuilder setMutation(MutationOperator<DoubleSolution> mutation) {
        this.mutation = mutation ;

        return this ;
    }

    public UDEABuilder setNeighborhoodSelectionProbability(double neighborhoodSelectionProbability){
        this.neighborhoodSelectionProbability = neighborhoodSelectionProbability;

        return this;
    }

    public UDEABuilder setNumberOfThreads(int numberOfThreads) {
        this.numberOfThreads = numberOfThreads ;

        return this ;
    }

    public UDEABuilder setIndicatorEvaluatingTimes(int indicatorEvaluatingTimes){
        this.indicatorEvaluatingTimes = indicatorEvaluatingTimes;
        return this;
    }

    public UDEABuilder setStrictlyFinalPopulationFlitting(boolean strictlyFinalPopulationFlitting){
        this.strictlyFinalPopulationFlitting = strictlyFinalPopulationFlitting;
        return this;
    }

    public List<double[]> getPredifineDirections(){
        return predifineDirections;
    }

    public UDEABuilder setPredifineDirections(List<double[]> predefineDirections){
        this.predifineDirections = predefineDirections;
        return  this;
    }

    public MyAlgorithmMeasures<DoubleSolution> getMeasureManager(){
        return this.measureManager;
    }

    public UDEABuilder setMeasureManager(MyAlgorithmMeasures<DoubleSolution> measureManager)
    {
        this.measureManager = measureManager;
        return this;
    }

    public UDEA build() {
        UDEA algorithm = null;
        if (udeaVariant.equals(Variant.UDEA)){
            algorithm = new UDEA(problem,H, Tau,
                    populationSize,maxEvaluations,neighborhoodSize,
                    neighborhoodSelectionProbability,
                    sbxCrossover,deCrossover, mutation);
        }else if(udeaVariant.equals(Variant.UDEAMeasure)) {
            algorithm = new UDEA(measureManager,problem, H, Tau,
                    populationSize, maxEvaluations,neighborhoodSize,
                    neighborhoodSelectionProbability,
                    sbxCrossover,deCrossover, mutation);
        }else if (udeaVariant.equals(Variant.CUDEA)){
            algorithm = new CUDEA(problem,H, Tau,
                    populationSize,maxEvaluations,neighborhoodSize,
                    neighborhoodSelectionProbability,
                    sbxCrossover,deCrossover, mutation);
        }else if(udeaVariant.equals(Variant.CUDEAMeasure)) {
            algorithm = new CUDEA(measureManager,problem, H, Tau,
                    populationSize, maxEvaluations,neighborhoodSize,
                    neighborhoodSelectionProbability,
                    sbxCrossover,deCrossover, mutation);
        }else if (udeaVariant.equals(Variant.CUDEAII)){
            algorithm = new CUDEAII(problem,H, Tau,
                    populationSize,maxEvaluations,neighborhoodSize,
                    neighborhoodSelectionProbability,
                    sbxCrossover,deCrossover, mutation);
        }else if(udeaVariant.equals(Variant.CUDEAIIMeasure)) {
            algorithm = new CUDEAII(measureManager,problem, H, Tau,
                    populationSize, maxEvaluations,neighborhoodSize,
                    neighborhoodSelectionProbability,
                    sbxCrossover,deCrossover, mutation);
        }
        return algorithm;
    }
}