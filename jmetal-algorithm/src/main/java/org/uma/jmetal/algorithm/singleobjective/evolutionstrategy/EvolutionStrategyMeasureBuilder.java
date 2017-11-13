package org.uma.jmetal.algorithm.singleobjective.evolutionstrategy;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.impl.mutation.PolynomialMutation;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.AlgorithmBuilder;
import org.uma.jmetal.util.JMetalException;

/**
 * Created by X250 on 2017/1/1.
 */
public class EvolutionStrategyMeasureBuilder implements AlgorithmBuilder<Algorithm<DoubleSolution>> {
    public enum EvolutionStrategyVariant {ELITIST, NON_ELITIST
        , CMAES
        ,
        SRELITIST
    }

    private DoubleProblem problem;
    private int mu;
    private int lambda;
    private int maxEvaluations;
    private int populationSize;
    private MutationOperator<DoubleSolution> mutation;
    private EvolutionStrategyVariant variant ;
    private double[] typicalX;
    private double sigma;
    private double Pf;


    public EvolutionStrategyMeasureBuilder(DoubleProblem problem,
                                    EvolutionStrategyVariant variant) {
        this.problem = problem;
        this.mu = 1;
        this.lambda = 10;
        this.maxEvaluations = 250000;
        this.mutation = new PolynomialMutation(1.0/problem.getNumberOfVariables(), 20.0);;
        this.variant = variant;
        this.sigma = 0.3;
        this.Pf = 0.45;
        typicalX = null;
    }

    public EvolutionStrategyMeasureBuilder setMu(int mu) {
        this.mu = mu;

        return this;
    }

    public EvolutionStrategyMeasureBuilder setLambda(int lambda) {
        this.lambda = lambda;

        return this;
    }

    public EvolutionStrategyMeasureBuilder setPf(double Pf){
        this.Pf = Pf;

        return this;
    }

    public EvolutionStrategyMeasureBuilder setMutation(MutationOperator mutation) {
        this.mutation = mutation;

        return this;
    }


    public EvolutionStrategyMeasureBuilder setMaxEvaluations(int maxEvaluations) {
        this.maxEvaluations = maxEvaluations;

        return this;
    }


    public EvolutionStrategyMeasureBuilder setMaxPopulationSize(int maxPopulationSize){
        this.populationSize = maxPopulationSize;

        return this;
    }


    public EvolutionStrategyMeasureBuilder setTypicalX(double[] typicalX){
        this.typicalX = typicalX;

        return this;
    }

    public EvolutionStrategyMeasureBuilder setSigma(double sigma){
        this.sigma = sigma;

        return this;
    }

    @Override public Algorithm<DoubleSolution> build() {
        if (variant == EvolutionStrategyVariant.ELITIST) {
            return new ElitistEvolutionStrategyMeasure(problem, mu, lambda, maxEvaluations,populationSize, mutation);
        } else if (variant == EvolutionStrategyVariant.NON_ELITIST) {
            return new NonElitistEvolutionStrategyMeasure(problem, mu, lambda, maxEvaluations,populationSize, mutation);
        }
        else if (variant == EvolutionStrategyVariant.CMAES) {
            return new CovarianceMatrixAdaptationEvolutionStrategyMeasure(problem, lambda, maxEvaluations,populationSize, typicalX, sigma);
        }
        if (variant == EvolutionStrategyVariant.SRELITIST) {
            return new SRElitistEvolutionStrategyMeasure(problem, mu, lambda,Pf, maxEvaluations,populationSize, mutation);
        }
        else {
            throw new JMetalException("Unknown variant: " + variant) ;
        }
    }

    /* Getters */
    public int getMu() {
        return mu;
    }

    public int getLambda() {
        return lambda;
    }

    public double getPf(){return Pf;}

    public int getMaxEvaluations() {
        return maxEvaluations;
    }

    public int getMaxPopulationSize(){return populationSize;}
    public MutationOperator<DoubleSolution> getMutation() {
        return mutation;
    }

    public double[] getTypicalX(){return typicalX;}

    public double getSigma(){return sigma;}
}
