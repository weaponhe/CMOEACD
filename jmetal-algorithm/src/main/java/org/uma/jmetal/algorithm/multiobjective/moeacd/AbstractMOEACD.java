package org.uma.jmetal.algorithm.multiobjective.moeacd;

import org.apache.commons.lang3.builder.Diff;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.moead.AbstractMOEAD;
import org.uma.jmetal.algorithm.multiobjective.moead.MOEAD;
import org.uma.jmetal.algorithm.multiobjective.paes.PAES;
import org.uma.jmetal.algorithm.multiobjective.udea.SubRegion;
import org.uma.jmetal.algorithm.singleobjective.differentialevolution.DifferentialEvolution;
import org.uma.jmetal.measure.Measurable;
import org.uma.jmetal.measure.MeasureManager;
import org.uma.jmetal.measure.impl.MyAlgorithmMeasures;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.operator.impl.crossover.SBXCrossover;
import org.uma.jmetal.problem.ConstrainedProblem;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.Constant;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.SolutionListUtils;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;
import org.uma.jmetal.util.solutionattribute.impl.OverallConstraintViolation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Created by X250 on 2016/4/22.
 */
public abstract class AbstractMOEACD implements Algorithm<List<DoubleSolution>> {

    protected MyAlgorithmMeasures<DoubleSolution> measureManager;
    protected Problem<DoubleSolution> problem;
    public OverallConstraintViolation<DoubleSolution> overallConstraintViolationDegree;
    //Ideal Point
    protected double[] idealPoint;
    protected double[] utopianPoint;
    protected List<List<Double>> subPlaneUtopianPointList;

    //maximum value of each objective in the seached objective space
    protected double[] nadirPoint;
    //maximum value of each objective in the objective space of current population
    //Reference Point
    protected double[] referencePoint;
    //intercepts of hyper-plane
    protected double[] intercepts;
    protected double[] normIntercepts;
    protected double[] delta;

    protected ConeSubRegionManager subRegionManager;


    protected List<DoubleSolution> population;

    protected int populationSize;
    protected int constraintLayerSize;

    protected int evaluations;
    protected int maxEvaluations;
    protected int maxGen;

    protected JMetalRandom randomGenerator;

    protected int neighborhoodSize;

    protected double neighborhoodSelectionProbability;

    public enum MatingType {NEIGHBOR, GLOBAL}

    ;
    protected MatingType matingType;

    public enum CrossoverType {SBX, DE}

    ;
    protected CrossoverType crossoverType;
    protected SBXCrossover sbxCrossoverOperator;
    protected DifferentialEvolutionCrossover deCrossoverOperator;
    protected MutationOperator<DoubleSolution> mutationOperator;

    protected int[] dominatedCount;

    protected int updateInterval = 10;

    protected AbstractMOEAD.FunctionType functionType;

    public AbstractMOEACD() {
    }

    public AbstractMOEACD(Problem<DoubleSolution> problem,
                          int[] arrayH,
                          double[] integratedTaus,
                          int populationSize,
                          int constraintLayerSize,
                          int maxEvaluations,
                          int maxGen,
                          int neighborhoodSize,
                          double neighborhoodSelectionProbability,
                          AbstractMOEAD.FunctionType functionType,
                          SBXCrossover sbxCrossoverOperator,
                          DifferentialEvolutionCrossover deCrossoverOperator,
                          MutationOperator<DoubleSolution> mutation,
                          double[] delta) {
        this.problem = problem;
        subRegionManager = new ConeSubRegionManager(problem.getNumberOfObjectives(), arrayH, integratedTaus);
        this.populationSize = populationSize;
        this.constraintLayerSize = constraintLayerSize;
        this.maxEvaluations = maxEvaluations;
        this.maxGen = maxGen;
        this.mutationOperator = mutation;
        this.sbxCrossoverOperator = sbxCrossoverOperator;
        this.deCrossoverOperator = deCrossoverOperator;
        this.neighborhoodSize = neighborhoodSize;
        this.neighborhoodSelectionProbability = neighborhoodSelectionProbability;
        this.functionType = functionType;
        this.delta = delta;
        randomGenerator = JMetalRandom.getInstance();

        idealPoint = new double[problem.getNumberOfObjectives()];
        utopianPoint = new double[problem.getNumberOfObjectives()];
        nadirPoint = new double[problem.getNumberOfObjectives()];
        referencePoint = new double[problem.getNumberOfObjectives()];
        intercepts = new double[problem.getNumberOfObjectives()];
        normIntercepts = new double[problem.getNumberOfObjectives()];

        dominatedCount = new int[populationSize];
        overallConstraintViolationDegree = new OverallConstraintViolation<>();

        //modified by heweipeng
        subPlaneUtopianPointList = new ArrayList<>(populationSize);
        for (int i = 0; i < populationSize; i++) {
            subPlaneUtopianPointList.add(new ArrayList<Double>(constraintLayerSize));
        }
    }

    public AbstractMOEACD(Measurable measureManager,
                          Problem<DoubleSolution> problem,
                          int[] arrayH,
                          double[] integratedTaus,
                          int populationSize,
                          int constraintLayerSize,
                          int maxEvaluations,
                          int maxGen,
                          int neighborhoodSize,
                          double neighborhoodSelectionProbability,
                          AbstractMOEAD.FunctionType functionType,
                          SBXCrossover sbxCrossoverOperator,
                          DifferentialEvolutionCrossover deCrossoverOperator,
                          MutationOperator<DoubleSolution> mutation,
                          double[] delta) {
        this(problem, arrayH, integratedTaus, populationSize, constraintLayerSize, maxEvaluations, maxGen, neighborhoodSize,
                neighborhoodSelectionProbability, functionType,
                sbxCrossoverOperator, deCrossoverOperator, mutation, delta);
        this.measureManager = (MyAlgorithmMeasures<DoubleSolution>) measureManager;
        this.measureManager.initMeasures();
        overallConstraintViolationDegree = new OverallConstraintViolation<>();
    }


    @Override
    public void run() {

        initializeConeSubRegions();
        initializePopulation();
        evaluations = populationSize;
        int gen = 1;

        initializeExtremePoints(population, utopianPoint, idealPoint, nadirPoint, referencePoint);
        initializeIntecepts(population, intercepts, utopianPoint, nadirPoint);
        initializeNormIntecepts(normIntercepts, utopianPoint, intercepts);

        associateSubRegion(population, utopianPoint, normIntercepts);


        do {
            for (int i = 0; i < populationSize; i++) {

                List<DoubleSolution> children = reproduction(i);

                DoubleSolution child = children.get(0);
                problem.evaluate(child);
                if (problem instanceof ConstrainedProblem) {
                    ((ConstrainedProblem<DoubleSolution>) problem).evaluateConstraints(child);
                }

                evaluations += 1;

                if (updateExtremePoints(child, utopianPoint, idealPoint, nadirPoint, referencePoint)) {
                    updateNormIntercepts(normIntercepts, utopianPoint, intercepts);
                }

                ConeSubRegion subRegion = locateConeSubRegion(child, utopianPoint, normIntercepts);

                boolean isUpdated = coneUpdate(child, subRegion, utopianPoint, normIntercepts);

            }


            gen++;

            initializeNadirPoint(population, nadirPoint);
            updateIntercepts(population, intercepts, utopianPoint, nadirPoint);
            updateNormIntercepts(normIntercepts, utopianPoint, intercepts);


        } while (evaluations < maxEvaluations);
    }

    double fitnessFunction(Solution individual, double[] lambda) throws JMetalException {
        double fitness;


        if (MOEAD.FunctionType.TCH.equals(functionType)) {
            double maxFun = -1.0e+30;

            for (int n = 0; n < problem.getNumberOfObjectives(); n++) {
                double diff = Math.abs(individual.getObjective(n) - idealPoint[n]);

                double feval;
                if (Math.abs(lambda[n]) < Constant.TOLERATION) {
                    feval = 0.0001 * diff;
                } else {
                    feval = diff * lambda[n];
                }

                if (feval > maxFun) {
                    maxFun = feval;
                }
            }

            fitness = maxFun;
        } else if (MOEAD.FunctionType.AGG.equals(functionType)) {
            double sum = 0.0;
            for (int n = 0; n < problem.getNumberOfObjectives(); n++) {
                sum += (lambda[n]) * (individual.getObjective(n) - idealPoint[n]);
            }

            fitness = sum;

        } else if (MOEAD.FunctionType.PBI.equals(functionType)) {
            double d1, d2, nl;
            double theta = 5.0;

            d1 = d2 = nl = 0.0;

            for (int i = 0; i < problem.getNumberOfObjectives(); i++) {
                d1 += (individual.getObjective(i) - idealPoint[i]) * lambda[i];
                nl += Math.pow(lambda[i], 2.0);
            }
            nl = Math.sqrt(nl);
            if (nl < 1e-10)
                nl = 1e-10;
            d1 = Math.abs(d1) / nl;

            for (int i = 0; i < problem.getNumberOfObjectives(); i++) {
                d2 += Math.pow((individual.getObjective(i) - idealPoint[i]) - d1 * (lambda[i] / nl), 2.0);
            }
            d2 = Math.sqrt(d2);

            fitness = (d1 + theta * d2);
        } else {
            throw new JMetalException(" MOEAD.fitnessFunction: unknown type " + functionType);
        }
        return fitness;
    }

    public void measureRun() {
        //Start
        measureManager.durationMeasure.start();
        initializeConeSubRegions();
        initializePopulation();
        evaluations = populationSize;
        int gen = 1;
        initializeExtremePoints(population, utopianPoint, idealPoint, nadirPoint, referencePoint);
        initializeIntecepts(population, intercepts, utopianPoint, nadirPoint);
        initializeNormIntecepts(normIntercepts, utopianPoint, intercepts);

        associateSubRegion(population, utopianPoint, normIntercepts);


        //calculate measure
        measureManager.updateMeasureProgress(getPopulation());
        do {
            for (int i = 0; i < populationSize; i++) {

                List<DoubleSolution> children = reproduction(i);

                DoubleSolution child = children.get(0);

                problem.evaluate(child);
                if (problem instanceof ConstrainedProblem) {
                    ((ConstrainedProblem<DoubleSolution>) problem).evaluateConstraints(child);
                }

                evaluations += 1;

                if (updateExtremePoints(child, utopianPoint, idealPoint, nadirPoint, referencePoint)) {
                    updateNormIntercepts(normIntercepts, utopianPoint, intercepts);
                }

                ConeSubRegion subRegion = locateConeSubRegion(child, utopianPoint, normIntercepts);
                boolean isUpdated = coneUpdate(child, subRegion, utopianPoint, normIntercepts);

            }
            gen++;

            initializeNadirPoint(population, nadirPoint);
            updateIntercepts(population, intercepts, utopianPoint, nadirPoint);
            updateNormIntercepts(normIntercepts, utopianPoint, intercepts);

            //calculate measure
            measureManager.updateMeasureProgress(getPopulation());

        } while (evaluations < maxEvaluations);
        measureManager.durationMeasure.stop();
    }

    public MeasureManager getMeasureManager() {
        return measureManager.getMeasureManager();
    }


    protected void initializePopulation() {
        int populationSize = this.populationSize * this.constraintLayerSize;
        population = new ArrayList<>(populationSize);
        for (int i = 0; i < populationSize; i++) {
            DoubleSolution newSolution = (DoubleSolution) problem.createSolution();
            problem.evaluate(newSolution);
            if (problem instanceof ConstrainedProblem) {
                ((ConstrainedProblem<DoubleSolution>) problem).evaluateConstraints(newSolution);
            }
            population.add(newSolution);
        }
    }


    public int getCurrentEvalution() {
        return evaluations;
    }

    ;

    /**
     * Initialize subproblems
     */

    protected void initializeConeSubRegions() {
        subRegionManager.generateConeSubRegionList();
        subRegionManager.initializingSubRegionsNeighbors(neighborhoodSize);
    }


    //initialize ideal point
    protected void initializeIdealPoint(List<DoubleSolution> population, double[] idealPoint) {
        for (int i = 0; i < problem.getNumberOfObjectives(); i++) {
            idealPoint[i] = Double.POSITIVE_INFINITY;
        }
        for (int i = 0; i < population.size(); ++i) {
            updateIdealPoint(population.get(i), idealPoint);
        }
    }

    //initialized utopian point
    protected void initializedUtopianPoint(double[] utopianPoint, double[] idealPoint) {
        updateUtopianPoint(utopianPoint, idealPoint);
    }

    //initialize nadir points
    protected void initializeNadirPoint(List<DoubleSolution> population, double[] nadirPoint) {
        for (int i = 0; i < problem.getNumberOfObjectives(); i++) {
            nadirPoint[i] = Double.NEGATIVE_INFINITY;
        }
        for (int i = 0; i < population.size(); ++i) {
            updateNadirPoint(population.get(i), this.nadirPoint);
        }
    }

    protected void initializeReferencePoint(double[] referencePoint, double[] idealPoint, double[] nadirPoint) {
        updateReferencePoint(referencePoint, idealPoint, nadirPoint);
    }

    public void initializeIntecepts(List<DoubleSolution> population, double[] intercepts, double[] utopianPoint, double[] nadirPoint) {
        updateIntercepts(population, intercepts, utopianPoint, nadirPoint);
    }

    public void initializeNormIntecepts(double[] normIntercepts, double[] utopianPoint, double[] intercepts) {
        updateNormIntercepts(normIntercepts, utopianPoint, intercepts);
    }

    //initialize extreme points
    protected void initializeExtremePoints(List<DoubleSolution> population, double[] utopianPoint, double[] idealPoint, double[] nadirPoint, double[] referencePoint) {
        initializeIdealPoint(population, idealPoint);
        initializedUtopianPoint(utopianPoint, idealPoint);
        initializeNadirPoint(population, nadirPoint);
        initializeReferencePoint(referencePoint, idealPoint, nadirPoint);
    }

    protected boolean updateIdealPoint(DoubleSolution solution, double[] idealPoint) {
        boolean isIdealPointUpdated = false;
        for (int n = 0; n < problem.getNumberOfObjectives(); n++) {
            if (solution.getObjective(n) < idealPoint[n]) {
                idealPoint[n] = solution.getObjective(n);
                isIdealPointUpdated = true;
            }
        }
        return isIdealPointUpdated;
    }

    protected void updateUtopianPoint(double[] utopianPoint, double[] idealPoint) {
        for (int i = 0; i < problem.getNumberOfObjectives(); i++)
            utopianPoint[i] = idealPoint[i] - Constant.TOLERATION;
    }

    protected boolean updateNadirPoint(DoubleSolution solution, double[] nadirPoint) {
        boolean isNadirPointUpdated = false;
        for (int i = 0; i < problem.getNumberOfObjectives(); ++i) {
            if (solution.getObjective(i) > nadirPoint[i]) {
                nadirPoint[i] = solution.getObjective(i);
                isNadirPointUpdated = true;
            }
        }
        return isNadirPointUpdated;
    }

    protected void updateReferencePoint(double[] referencePoint, double[] idealPoint, double[] nadirPoint) {
        for (int i = 0; i < problem.getNumberOfObjectives(); ++i) {
            referencePoint[i] = nadirPoint[i] + 1.0e3 * (nadirPoint[i] - idealPoint[i]);
        }
    }

    protected boolean updateExtremePoints(DoubleSolution solution, double[] utopianPoint, double[] idealPoint, double[] nadirPoint, double[] referencePoint) {
        boolean isIdealPointUpdated = updateIdealPoint(solution, idealPoint);
        if (isIdealPointUpdated) {
            updateUtopianPoint(utopianPoint, idealPoint);
        }
        boolean isNadirPointUpdated = updateNadirPoint(solution, nadirPoint);
        if (isNadirPointUpdated || isIdealPointUpdated) {
            updateReferencePoint(referencePoint, idealPoint, nadirPoint);
        }
        return isIdealPointUpdated || isNadirPointUpdated;
    }

    public void updateNormIntercepts(double[] normIntercepts, double[] utopianPoint, double[] intercepts) {
        for (int i = 0; i < problem.getNumberOfObjectives(); ++i) {
            normIntercepts[i] = 1.0;
        }
//        //normalization procedure in multi-, many-objective optimization
//        for (int i = 0; i < problem.getNumberOfObjectives(); ++i) {
//            normIntercepts[i] = intercepts[i] - utopianPoint[i];
//            if (normIntercepts[i] < Constant.TOLERATION)
//                normIntercepts[i] = Constant.TOLERATION;
//        }
    }

    //use for normalization procedure for multi-, many-objective optimization
    protected void updateIntercepts(List<DoubleSolution> population, double[] intercepts, double[] utopianPoint, double[] nadirPoint) {
////        normalization procedure in multi-, many-objective optimization
//        List<List<Double>> extremePoints = findExtremePoints(population, utopianPoint);
//        constructHyperplaneAndUpdateIntercepts(intercepts, extremePoints, utopianPoint, nadirPoint);
    }

    protected ConeSubRegion locateConeSubRegion(DoubleSolution solution, double[] utopianPoint, double[] normIntercepts) {
        double[] normObjectives = MOEACDUtils.normalize(solution, utopianPoint, normIntercepts);
        return subRegionManager.locateSubRegion(normObjectives);
    }

    protected ConeSubRegion locateConeSubRegion(double[] normObjectives) {
        return subRegionManager.locateSubRegion(normObjectives);
    }

    protected boolean coneUpdate(DoubleSolution _individual, ConeSubRegion targetSubRegion, double[] utopianPoint, double[] normIntercepts) {
        int idxStoreInPop = targetSubRegion.getIdxSolution();
        DoubleSolution storeInd = population.get(idxStoreInPop);
        ConeSubRegion storeSubRegion = locateConeSubRegion(storeInd, utopianPoint, normIntercepts);
        DoubleSolution betterS = null;
        boolean isUpdated = false;
        if (targetSubRegion == storeSubRegion) {
            DoubleSolution worseS = _individual;
            betterS = getBetterSolutionByIndicator(_individual, population.get(idxStoreInPop), targetSubRegion, utopianPoint, normIntercepts);
            if (betterS == _individual) {
                //has updated
                population.set(idxStoreInPop, _individual);
                worseS = storeInd;
                isUpdated = true;
            }
        } else {
            isUpdated = true;
            population.set(idxStoreInPop, _individual);
            //cone update recursively
            coneUpdate(storeInd, storeSubRegion, utopianPoint, normIntercepts);
        }
        return isUpdated;
    }


    //update the association between cone subregion and solution
    protected void associateSubRegion(List<DoubleSolution> population, double[] utopianPoint, double[] normIntercepts) {

        //clearing the associate information
        for (int i = 0; i < subRegionManager.getConeSubRegionsNum(); ++i) {
            subRegionManager.getConeSubRegion(i).setIdxSolution(-1);
        }

        List<Integer> remainingSolutionIdx = new ArrayList<>(population.size());
        for (int i = 0; i < population.size(); ++i) {
            //find the cone subregion which the individual belongs to
            ConeSubRegion subRegion = locateConeSubRegion(population.get(i), utopianPoint, normIntercepts);
            if (subRegion.getIdxSolution() < 0) {//No individual has been bound to this subregion
                //bind it
                subRegion.setIdxSolution(i);
            } else {
                int idxBoundInd = subRegion.getIdxSolution();
                int idxWorst = i;
                //choose the better one for subregion by comparing their indicators using in the algorithm
                DoubleSolution betterS = getBetterSolutionByIndicator(population.get(i), population.get(idxBoundInd), subRegion, utopianPoint, normIntercepts);
                if (betterS == population.get(i)) {
                    //replace the bound one
                    subRegion.setIdxSolution(i);
                    idxWorst = idxBoundInd;
                }
                //record the worst one
                remainingSolutionIdx.add(idxWorst);
            }
        }


        for (int i = 0; i < subRegionManager.getConeSubRegionsNum(); i++) {
            ConeSubRegion subRegion = subRegionManager.getConeSubRegion(i);
            if (subRegion.getIdxSolution() < 0) {
                int selectedIdx = nearestRemainingSolutionIdx(remainingSolutionIdx, subRegion, utopianPoint, normIntercepts);
                subRegion.setIdxSolution(remainingSolutionIdx.get(selectedIdx));
                remainingSolutionIdx.remove(selectedIdx);
            }
        }
    }


    protected int nearestRemainingSolutionIdx(List<Integer> remainingSolutionIdx, ConeSubRegion targetSubRegion, double[] utopianPoint, double[] normIntercepts) {
        int minIdx = 0;
        ConeSubRegion subRegion = locateConeSubRegion(population.get(remainingSolutionIdx.get(0)), utopianPoint, normIntercepts);
        double minDis = MOEACDUtils.distance2(subRegion.getRefDirection(), targetSubRegion.getRefDirection());
        for (int i = 1; i < remainingSolutionIdx.size(); ++i) {
            subRegion = locateConeSubRegion(population.get(remainingSolutionIdx.get(i)), utopianPoint, normIntercepts);
            double tmp = MOEACDUtils.distance2(subRegion.getRefDirection(), targetSubRegion.getRefDirection());
            if (tmp < minDis) {
                minDis = tmp;
                minIdx = i;
            }
        }
        return minIdx;
    }


    protected abstract List<DoubleSolution> reproduction(int idxSubRegion);

    protected abstract DoubleSolution getBetterSolutionByIndicator(DoubleSolution solution1, DoubleSolution solution2, ConeSubRegion coneSubRegion, double[] utopianPoint, double[] normIntercepts);


    protected List<List<Double>> findExtremePoints(List<DoubleSolution> population, double[] utopianPoint) {

        List<List<Double>> extremePoints = new ArrayList<>(problem.getNumberOfObjectives());
        for (int i = 0; i < problem.getNumberOfObjectives(); ++i) {
            int idxExtremeInd = -1;
            double min_Fitness = Double.POSITIVE_INFINITY;
            for (int j = 0; j < population.size(); ++j) {
                double fitness = ASF(population.get(j), i, utopianPoint);
                if (fitness < min_Fitness) {
                    min_Fitness = fitness;
                    idxExtremeInd = j;
                }
            }

            List<Double> selectedIndivPoints = new ArrayList<>();
            for (int j = 0; j < problem.getNumberOfObjectives(); ++j)
                selectedIndivPoints.add(population.get(idxExtremeInd).getObjective(j));
            extremePoints.add(selectedIndivPoints);
        }
        return extremePoints;
    }

    protected double ASF(DoubleSolution solution, int idx, double[] utopianPoint) {
        double max_ratio = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < solution.getNumberOfObjectives(); i++) {
            double weight = (idx == i) ? 1.0 : 0.000001;
            max_ratio = Math.max(max_ratio, (solution.getObjective(i) - utopianPoint[i]) / weight);
        }
        return max_ratio;
    }

    protected void constructHyperplaneAndUpdateIntercepts(double[] intercepts, List<List<Double>> extremePoints, double[] utopianPoint, double[] nadirPoint) {
        // Check whether there are duplicate extreme points.
        int numberOfObjectives = problem.getNumberOfObjectives();

        boolean duplicate = false;
        boolean isQualified = true;
        for (int i = 0; !duplicate && i < extremePoints.size(); i += 1) {
            for (int j = i + 1; !duplicate && j < extremePoints.size(); j += 1) {
                int k = 0;
                int c = 0;
                for (; k < numberOfObjectives; ++k) {
                    if (Math.abs(extremePoints.get(i).get(k) - extremePoints.get(j).get(k)) < Constant.TOLERATION) {
                        c++;
                    }
                }
                if (c == numberOfObjectives)
                    duplicate = true;
            }
        }
        if (!duplicate) {
            for (int i = 0; i < extremePoints.size(); i++) {
                for (int j = 0; j < problem.getNumberOfObjectives(); ++j)
                    extremePoints.get(i).set(j, extremePoints.get(i).get(j) - utopianPoint[j]);
            }

            // Find the equation of the hyperplane
            List<Double> b = new ArrayList<>(); //(pop[0].objs().size(), 1.0);
            for (int i = 0; i < numberOfObjectives; i++)
                b.add(1.0);

            List<Double> x = MOEACDUtils.guassianElimination(extremePoints, b);

            // Find intercepts
            for (int f = 0; f < numberOfObjectives; f += 1) {
                if (Double.isNaN(x.get(f)) || x.get(f) <= Constant.TOLERATION) {
                    isQualified = false;
                    break;
                }
                intercepts[f] = (1.0 / x.get(f));
                if (Double.isNaN(intercepts[f]) || intercepts[f] <= 0.0) {
                    isQualified = false;
                    break;
                }
            }
        }
        if (duplicate || !isQualified) {

            for (int f = 0; f < numberOfObjectives; f += 1)
                intercepts[f] = nadirPoint[f];
        } else {
            for (int f = 0; f < numberOfObjectives; f += 1)
                intercepts[f] += utopianPoint[f];
        }
    }

    public List<DoubleSolution> getPopulation() {
        return population;
    }

    public List<DoubleSolution> getMeasurePopulation() {
        if (problem instanceof ConstrainedProblem) {
            List<DoubleSolution> feasibleSet = new ArrayList<>(population.size());
            for (int i = 0; i < population.size(); i++) {
                if (!isFeasible(population.get(i)))
                    continue;
                feasibleSet.add(population.get(i));
            }
            return feasibleSet;
        } else {
            return population;
        }
    }

    protected boolean isFeasible(DoubleSolution solution) {
        return overallConstraintViolationDegree.getAttribute(solution) >= 0.0;
    }

    @Override
    public List<DoubleSolution> getResult() {
        List<DoubleSolution> pop = getMeasurePopulation();
        if (pop.isEmpty())
            return pop;
        else
            return SolutionListUtils.getNondominatedSolutions(pop);
    }

    @Override
    public String getName() {
        return "MOEACD";
    }

    @Override
    public String getDescription() {
        return "An Evolutionary Many-Objective Optimization Algorithm Based on Cone Decomposition";
    }
}