package org.uma.jmetal.algorithm.multiobjective.moead;

import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.problem.ConstrainedProblem;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.Constant;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.comparator.impl.ViolationThresholdComparator;

import java.util.List;

/**
 * Created by X250 on 2016/11/25.
 */
public class CMOEADN extends CMOEAD {
    protected double[] intercepts;
    protected double[] normIntercepts;

    public CMOEADN(Problem<DoubleSolution> problem,
                  int populationSize,
                  int resultPopulationSize,
                  int maxEvaluations,
                  CrossoverOperator<DoubleSolution> crossover,
                  MutationOperator<DoubleSolution> mutation,
                  FunctionType functionType,
                  String dataDirectory,
                  int neighborSize) {
        super(problem, populationSize, resultPopulationSize, maxEvaluations, crossover, mutation, functionType,
                dataDirectory,neighborSize);
        intercepts = new double[problem.getNumberOfObjectives()];
        normIntercepts = new double[problem.getNumberOfObjectives()];
    }

    public CMOEADN(Problem<DoubleSolution> problem,
                  int populationSize,
                  int resultPopulationSize,
                  int maxEvaluations,
                  CrossoverOperator<DoubleSolution> crossover,
                  MutationOperator<DoubleSolution> mutation,
                  FunctionType functionType,
                  int[] arrayH,
                  double[] integratedTau,
                  int neighborSize) {
        super(problem, populationSize, resultPopulationSize, maxEvaluations, crossover, mutation, functionType,
                arrayH,integratedTau,neighborSize);
        intercepts = new double[problem.getNumberOfObjectives()];
        normIntercepts = new double[problem.getNumberOfObjectives()];
    }

    @Override public void run() {
        initializePopulation() ;
        initializeUniformWeight();
        initializeNeighborhood();
        initializeIdealPoint() ;
        initializeNadirPoint();

        initializeIntercepts();

        violationThresholdComparator.updateThreshold(population);

        evaluations = populationSize ;
        do {
            for (int i = 0; i < populationSize; i++) {
                int subProblemId = i;

                List<DoubleSolution> parents = parentSelection(subProblemId) ;

                List<DoubleSolution> children = crossoverOperator.execute(parents);

                DoubleSolution child = children.get(0) ;
                mutationOperator.execute(child);
                problem.evaluate(child);
                if (problem instanceof ConstrainedProblem) {
                    ((ConstrainedProblem<DoubleSolution>) problem).evaluateConstraints(child);
                }
                evaluations++;

                updateIdealPoint(child);
                updateNeighborhood(child, subProblemId);
            }

            violationThresholdComparator.updateThreshold(population);
            updateIntercepts();
        } while (evaluations < maxEvaluations);

//        int numFeasible = 0;
//        for (int i=0;i<population.size();i++){
//            if(isFeasible(population.get(i))){
//                numFeasible++;
//            }
//        }
//        JMetalLogger.logger.info("\nNumber of feasible solutions :  "+numFeasible + "\t\n");
    }

    protected void initializeIntercepts(){
        for(int i=0;i<problem.getNumberOfObjectives();i++){
            intercepts[i] = nadirPoint[i];
        }
        updateNormIntercepts();
    }


    protected void updateIdealPoint(DoubleSolution individual) {
        boolean isUpdated = false;
        for (int n = 0; n < problem.getNumberOfObjectives(); n++) {
            if (individual.getObjective(n) < idealPoint[n]) {
                idealPoint[n] = individual.getObjective(n);
                isUpdated = true;
            }
        }
        if(isUpdated)
            updateNormIntercepts();
    }


    protected void updateIntercepts(){
        for(int i=0;i<problem.getNumberOfObjectives();++i){
            intercepts[i] = Double.NEGATIVE_INFINITY;
        }
        for(int i=0;i<populationSize;++i){
            for (int j = 0; j < problem.getNumberOfObjectives(); j++) {
                if (population.get(i).getObjective(j) > intercepts[j]) {
                    intercepts[j] = population.get(i).getObjective(j);
                }
            }
        }
        updateNormIntercepts();
    }


    public void updateNormIntercepts(){
        for(int i=0;i<problem.getNumberOfObjectives();++i){
            normIntercepts[i] = intercepts[i] - idealPoint[i];
            if(normIntercepts[i] < Constant.TOLERATION)
                normIntercepts[i] = Constant.TOLERATION;
        }
    }

    double fitnessFunction(DoubleSolution individual, double[] lambda) throws JMetalException {
        double fitness;
        if (MOEAD.FunctionType.TCH.equals(functionType)) {
            double maxFun = -1.0e+30;

            for (int n = 0; n < problem.getNumberOfObjectives(); n++) {

                double diff = Math.abs((individual.getObjective(n) - idealPoint[n])/normIntercepts[n]);

                double feval;
                if (Math.abs(lambda[n])< Constant.TOLERATION) {
                    feval = 0.0001 * diff;
                } else {
                    feval = diff * lambda[n];
                }
                if (feval > maxFun) {
                    maxFun = feval;
                }
            }

            fitness = maxFun;
        }else if (MOEAD.FunctionType.AGG.equals(functionType)) {
            double sum = 0.0;
            for (int n = 0; n < problem.getNumberOfObjectives(); n++) {
                sum += (lambda[n]) * (individual.getObjective(n) - idealPoint[n])/normIntercepts[n];
            }

            fitness = sum;

        }else if (MOEAD.FunctionType.PBI.equals(functionType)) {
            double d1, d2, nl;
            double theta = 5.0;

            d1 = d2 = nl = 0.0;

            for (int i = 0; i < problem.getNumberOfObjectives(); i++) {
                d1 += (individual.getObjective(i) - idealPoint[i])/normIntercepts[i] * lambda[i];
                nl += Math.pow(lambda[i], 2.0);
            }
            nl = Math.sqrt(nl);
            if(nl < 1e-10)
                nl = 1e-10;
            d1 = Math.abs(d1) / nl;

            for (int i = 0; i < problem.getNumberOfObjectives(); i++) {
                d2 += Math.pow((individual.getObjective(i) - idealPoint[i])/normIntercepts[i] - d1 * (lambda[i] / nl), 2.0);
            }
            d2 = Math.sqrt(d2);

            fitness = (d1 + theta * d2);
        } else {
            throw new JMetalException(" MOEAD.fitnessFunction: unknown type " + functionType);
        }
        return fitness;
    }


    @Override public String getName() {
        return "CMOEAD" ;
    }

    @Override public String getDescription() {
        return "Multi-Objective Evolutionary Algorithm based on Decomposition with constraints support" ;
    }
}
