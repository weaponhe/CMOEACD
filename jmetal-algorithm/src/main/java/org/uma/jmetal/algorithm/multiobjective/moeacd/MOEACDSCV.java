package org.uma.jmetal.algorithm.multiobjective.moeacd;

import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import org.uma.jmetal.measure.Measurable;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.operator.impl.crossover.SBXCrossover;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.Constant;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.Jama.Matrix;
import org.uma.jmetal.util.KDTree;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by X250 on 2016/12/21.
 */
public class MOEACDSCV extends MOEACD {
    protected  double factorial = 0.0;
    public MOEACDSCV(Problem<DoubleSolution> problem,
                     int[] arrayH,
                     double[] integratedTaus,
                     int populationSize,
                     int maxEvaluations,
                     int neighborhoodSize,
                     double neighborhoodSelectionProbability,
                     SBXCrossover sbxCrossoverOperator,
                     DifferentialEvolutionCrossover deCrossoverOperator,
                     MutationOperator<DoubleSolution> mutation
    ) {
        super(problem,arrayH, integratedTaus,
                populationSize,maxEvaluations,neighborhoodSize,
                neighborhoodSelectionProbability,
                sbxCrossoverOperator,deCrossoverOperator, mutation);
        factorial = 1.0;
        for (int i=0;i<problem.getNumberOfObjectives();i++){
            factorial *= (i+1);
        }
    }

    public MOEACDSCV(Measurable measureManager, Problem<DoubleSolution> problem,
                     int[] arrayH,
                     double[] integratedTaus,
                     int populationSize,
                     int maxEvaluations,
                     int neighborhoodSize,
                     double neighborhoodSelectionProbability,
                     SBXCrossover sbxCrossoverOperator,
                     DifferentialEvolutionCrossover deCrossoverOperator,
                     MutationOperator<DoubleSolution> mutation
    ) {
        super(measureManager,problem,arrayH, integratedTaus,
                populationSize,maxEvaluations,neighborhoodSize,
                neighborhoodSelectionProbability,
                sbxCrossoverOperator,deCrossoverOperator, mutation);
        factorial = 1.0;
        for (int i=0;i<problem.getNumberOfObjectives();i++){
            factorial *= (i+1);
        }
    }

    protected DoubleSolution getBetterSolutionByIndicator(DoubleSolution newSolution, DoubleSolution storedSolution, ConeSubRegion coneSubRegion,double[] utopianPoint,double[] normIntercepts) {
        int domination = MOEACDUtils.dominateCompare(newSolution,storedSolution);
        if(domination == -1 ){
            return newSolution;
        }
        else if(domination == 1|| domination == 2){
            return storedSolution;
        }
        else {
            boolean isMarginalSop = coneSubRegion.isMarginalConeSubRegion();
            double[] refDirection = coneSubRegion.getRefDirection();

            double[] normNewSolution = MOEACDUtils.normalize(newSolution,utopianPoint,normIntercepts);
            double[] normStoredSolution = MOEACDUtils.normalize(storedSolution,utopianPoint,normIntercepts);
            double[] observationNewSolution = MOEACDUtils.calObservation(normNewSolution);
            double[] obsercationStoredSolution = MOEACDUtils.calObservation(normStoredSolution);
            double[] limit = new double[problem.getNumberOfObjectives()];
            for (int i=0;i<problem.getNumberOfObjectives();i++) {
                limit[i] = refDirection[i] + D0Mean * Math.sqrt(1.0 * problem.getNumberOfObjectives() - 1.0) / Math.sqrt(1.0 * problem.getNumberOfObjectives());
                limit[i] = Math.max(limit[i],observationNewSolution[i]);
                limit[i] = Math.max(limit[i],obsercationStoredSolution[i]);
                if(isMarginalSop && refDirection[i] > 0.0){
                    limit[i] = 1.1;
                }
            }
            double LSVNewSolution = LocalSimplexVolume(normNewSolution, limit);
            double LSVStoredSolution = LocalSimplexVolume(normStoredSolution, limit);
//            double LSVNewSolution = LocalSimplexVolume(newSolution, refDirection);
//            double LSVStoredSolution = LocalSimplexVolume(storedSolution, refDirection);

            if (LSVNewSolution < LSVStoredSolution)
                return newSolution;
            else
                return storedSolution;
        }
    }

    protected double LocalSimplexVolume(DoubleSolution solution, double[] direction){
        double[] normObjective = MOEACDUtils.normalize(solution,utopianPoint,normIntercepts);

        double localSimplexVolume = 0.0;
        double[][] points = new double[problem.getNumberOfObjectives()][problem.getNumberOfObjectives()];
        double[][] points2 = new double[problem.getNumberOfObjectives()][problem.getNumberOfObjectives()];
        for (int i=0;i<problem.getNumberOfObjectives();i++){
            double p1 = direction[i] + D0Mean*Math.sqrt(1.0*problem.getNumberOfObjectives() - 1.0)/Math.sqrt(1.0*problem.getNumberOfObjectives());
            if(p1 - 1.0 >= 0.0){//marginal
                for (int k=0;k<problem.getNumberOfObjectives();k++){
                    if(k == i) {
                        points[i][k] = (referencePoint[k] - utopianPoint[k])/normIntercepts[k];
                        points2[i][k] = (referencePoint[k] - utopianPoint[k])/normIntercepts[k] - normObjective[k];
                    }
                    else {
                        points[i][k] = 0.0;
                        points2[i][k] = 0.0;
                    }
                }
            }else {
                double p2 = 0.0;
                for (int j=0;j<problem.getNumberOfObjectives();j++){
                    if(j!=i)
                        p2 += normObjective[j];
                }
                p2 = p2/(1.0/p1 - 1.0);
                if(p2 < normObjective[i])
                    p2 = normObjective[i];

                for (int k = 0; k < problem.getNumberOfObjectives(); k++) {
                    if(k==i) {
                        points[i][k] = p2;
                        points2[i][k] = points[i][k] - normObjective[k];
                    }
                    else {
                        points[i][k] = normObjective[k];
                        points2[i][k] = 0.0;
                    }
                }
            }
        }

        double volumeSimplex1 = Math.abs(Determinant.determinant(points,problem.getNumberOfObjectives()))/factorial;
//        double volumeSimplex2 = Math.abs(Determinant.determinant(points2,problem.getNumberOfObjectives()))/factorial;
        double volumeSimplex2 = 1.0;
        for (int i=0;i<problem.getNumberOfObjectives();i++){
            volumeSimplex2 *= points2[i][i];
        }
        volumeSimplex2/=factorial;

        localSimplexVolume = volumeSimplex1 - volumeSimplex2;
        return localSimplexVolume;
    }

    protected double LocalSimplexVolume(double[] normObjective, double[] limit){

        double localSimplexVolume = 0.0;
        double[][] points = new double[problem.getNumberOfObjectives()][problem.getNumberOfObjectives()];
        double[][] points2 = new double[problem.getNumberOfObjectives()][problem.getNumberOfObjectives()];
        for (int i=0;i<problem.getNumberOfObjectives();i++){
            if(limit[i] - 1.0 >= 0.0){//axel
                for (int k=0;k<problem.getNumberOfObjectives();k++){
                    if(k == i) {
                        points[i][k] = (referencePoint[k] - utopianPoint[k])/normIntercepts[k];
                        points2[i][k] = points[i][k] - normObjective[k];
                    }
                    else {
                        points[i][k] = 0.0;
                        points2[i][k] = 0.0;
                    }
                }
            }else {
                double p2 = 0.0;
                for (int j=0;j<problem.getNumberOfObjectives();j++){
                    if(j!=i)
                        p2 += normObjective[j];
                }
                p2 = p2/(1.0/limit[i] - 1.0);

                for (int k = 0; k < problem.getNumberOfObjectives(); k++) {
                    if(k==i) {
                        points[i][k] = p2;
                        points2[i][k] = points[i][k] - normObjective[k];
                    }
                    else {
                        points[i][k] = normObjective[k];
                        points2[i][k] = 0.0;
                    }
                }
            }
        }

        double volumeSimplex1 = 0.0;//
        Matrix matrix = new Matrix(points);
        volumeSimplex1 = Math.abs(matrix.det())/factorial;
        // Math.abs(Determinant.determinant(points,problem.getNumberOfObjectives()))/factorial;
//        double volumeSimplex2 = Math.abs(Determinant.determinant(points2,problem.getNumberOfObjectives()))/factorial;
        double volumeSimplex2 = 1.0;
        for (int i=0;i<problem.getNumberOfObjectives();i++){
            volumeSimplex2 *= points2[i][i];
        }
        volumeSimplex2/=factorial;

        localSimplexVolume = volumeSimplex1 - volumeSimplex2;
        return localSimplexVolume;
    }
}
