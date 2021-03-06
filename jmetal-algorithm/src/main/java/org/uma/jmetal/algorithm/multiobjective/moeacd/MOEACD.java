package org.uma.jmetal.algorithm.multiobjective.moeacd;

import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import org.jcp.xml.dsig.internal.dom.DOMX509Data;
import org.uma.jmetal.algorithm.multiobjective.moead.AbstractMOEAD;
import org.uma.jmetal.algorithm.multiobjective.moead.MOEAD;
import org.uma.jmetal.algorithm.multiobjective.moead.util.MOEADUtils;
import org.uma.jmetal.measure.Measurable;
import org.uma.jmetal.measure.MeasureManager;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.operator.impl.crossover.SBXCrossover;
import org.uma.jmetal.problem.ConstrainedProblem;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.solution.util.RepairDoubleSolution;
import org.uma.jmetal.solution.util.RepairDoubleSolutionAtBounds;
import org.uma.jmetal.util.Constant;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.KDTree;
import org.uma.jmetal.util.comparator.impl.NormallizedMaximumViolationThresholdComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by X250 on 2016/4/26.
 */
public class MOEACD extends AbstractMOEACD {

    protected int extraEvolvingSize;
    protected List<Integer> evolvingIdxList = null;
    protected double D0Mean = 0;
    protected double D0Min = 0;

    protected double chooseR;
    protected double Rde;
    protected double Rsbx;
    protected int Cde;
    protected int Sde;
    protected int Csbx;
    protected int Ssbx;

    protected double beta_ConeUpdate = 3.0;
    protected double beta_NeighborUpdate = 3.0;

    protected int maxGen;

    protected double LP;
    protected AbstractMOEAD.FunctionType functionType;

    protected enum ComparisonMethod {
        CDP,
        CORE_AREA,
        FEASIBILITY_FIRST,
        FITNESS_FIRST
    }

    ;

    public MOEACD(Problem<DoubleSolution> problem,
                  int[] arrayH,
                  double[] integratedTaus,
                  int populationSize,
                  int maxEvaluations,
                  int maxGen,
                  int neighborhoodSize,
                  double neighborhoodSelectionProbability,
                  SBXCrossover sbxCrossoverOperator,
                  DifferentialEvolutionCrossover deCrossoverOperator,
                  MutationOperator<DoubleSolution> mutation,
                  AbstractMOEAD.FunctionType functionType
    ) {
        super(problem, arrayH, integratedTaus,
                populationSize, maxEvaluations, neighborhoodSize,
                neighborhoodSelectionProbability,
                sbxCrossoverOperator, deCrossoverOperator, mutation);
        evolvingIdxList = new ArrayList<>(2 * populationSize);

        chooseR = 0.5;
        Rde = 0.5;
        Rsbx = 0.5;
        Cde = 0;
        Sde = 0;
        Csbx = 0;
        Ssbx = 0;
        LP = 2;

        this.maxGen = maxGen;
        this.functionType = functionType;
    }

    public MOEACD(Measurable measureManager, Problem<DoubleSolution> problem,
                  int[] arrayH,
                  double[] integratedTaus,
                  int populationSize,
                  int maxEvaluations,
                  int maxGen,
                  int neighborhoodSize,
                  double neighborhoodSelectionProbability,
                  SBXCrossover sbxCrossoverOperator,
                  DifferentialEvolutionCrossover deCrossoverOperator,
                  MutationOperator<DoubleSolution> mutation,
                  AbstractMOEAD.FunctionType functionType
    ) {
        super(measureManager, problem, arrayH, integratedTaus,
                populationSize, maxEvaluations, neighborhoodSize,
                neighborhoodSelectionProbability,
                sbxCrossoverOperator, deCrossoverOperator, mutation);
        evolvingIdxList = new ArrayList<>(2 * populationSize);

        chooseR = 0.5;
        Rde = 0.5;
        Rsbx = 0.5;
        Cde = 0;
        Sde = 0;
        Csbx = 0;
        Ssbx = 0;
        LP = 2;

        this.maxGen = maxGen;
        this.functionType = functionType;
    }

    @Override
    public void run() {
        measureManager.durationMeasure.start();
        initializeConeSubRegions();
        initializePopulation();
        evaluations = populationSize;
        int gen = 1;

        initializeExtremePoints(population, utopianPoint, idealPoint, nadirPoint, referencePoint);
        initializeIntecepts(population, intercepts, utopianPoint, nadirPoint);
        initializeNormIntecepts(normIntercepts, utopianPoint, intercepts);

        associateSubRegion(population, utopianPoint, normIntercepts);

        do {
            calcEvolvingSubproblemList();

            for (int i = 0; i < populationSize; i++) {

                List<DoubleSolution> children = reproduction(evolvingIdxList.get(i));
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
//
//                if(!isUpdated){
//                    isUpdated |= coneNeighborUpdate(child, subRegion, utopianPoint, normIntercepts);
//                }
                collectForAdaptiveCrossover(isUpdated);
            }

            gen++;

            initializeNadirPoint(population, nadirPoint);
            if (gen % updateInterval == 0)
                updateIntercepts(population, intercepts, utopianPoint, nadirPoint);
            updateNormIntercepts(normIntercepts, utopianPoint, intercepts);
//            associateSubRegion(population,utopianPoint,normIntercepts);

            updateAdaptiveCrossover();

        } while (gen < maxGen);
        measureManager.durationMeasure.stop();

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
        measureManager.updateMeasureProgress(getMeasurePopulation());
        do {

            calcEvolvingSubproblemList();

            for (int i = 0; i < populationSize; i++) {
                List<DoubleSolution> children = reproduction(evolvingIdxList.get(i));
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
//                if(!isUpdated){
//                    isUpdated |= coneNeighborUpdate(child, subRegion, utopianPoint, normIntercepts);
//                }
//                collectForAdaptiveCrossover(isUpdated);
            }
            gen++;

            initializeNadirPoint(population, nadirPoint);
            if (gen % updateInterval == 0)
                updateIntercepts(population, intercepts, utopianPoint, nadirPoint);
            updateNormIntercepts(normIntercepts, utopianPoint, intercepts);
//            associateSubRegion(population,utopianPoint,normIntercepts);

            updateAdaptiveCrossover();
            //calculate measure
            measureManager.updateMeasureProgress(getMeasurePopulation());

        } while (evaluations < maxEvaluations);
        measureManager.durationMeasure.stop();
    }


    /**
     * Initialize subproblems
     */

    protected void initializeConeSubRegions() {
        subRegionManager.generateConeSubRegionList();
        subRegionManager.initializingSubRegionsNeighbors(neighborhoodSize);
        findD0();
    }

    protected int updateExtraEvolvingSize() {
        double maxES = Math.min(subRegionManager.getConeSubRegionsNum() / 5.0, 5.0 * subRegionManager.getMarginalSubRegionIdxList().size());
        return extraEvolvingSize = (int) Math.ceil(MOEACDUtils.sigmoid(1.0 * evaluations / maxEvaluations, 15.0, 0.382) * maxES);

    }

    protected void calcEvolvingSubproblemList() {

        extraEvolvingSize = 0;//updateExtraEvolvingSize();

        int evolvingSize = subRegionManager.getConeSubRegionsNum() + extraEvolvingSize;

        evolvingIdxList = new ArrayList<>(evolvingSize);

        for (int i = 0; i < subRegionManager.getConeSubRegionsNum(); i++) {
            evolvingIdxList.add(i);
        }

        if (extraEvolvingSize > 0) {
            List<Integer> marginalIdx = subRegionManager.getMarginalSubRegionIdxList();
            Collections.shuffle(marginalIdx);
            int idx = 0;
            while (evolvingIdxList.size() < evolvingSize) {
                if (idx == marginalIdx.size())
                    idx = 0;
                evolvingIdxList.add(marginalIdx.get(idx));
                idx++;
            }
        }

        Collections.shuffle(evolvingIdxList);
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

        List<Integer> unboundSubregion = new ArrayList<>(subRegionManager.getConeSubRegionsNum());
        for (int i = 0; i < subRegionManager.getConeSubRegionsNum(); i++) {
            ConeSubRegion subRegion = subRegionManager.getConeSubRegion(i);
            if (subRegion.getIdxSolution() < 0) {
                unboundSubregion.add(i);
            }
        }

        Collections.shuffle(remainingSolutionIdx);

        for (int i = 0; i < remainingSolutionIdx.size() && (!unboundSubregion.isEmpty()); i++) {
            int selectedIdx = nearestUnboundSubRegionIdx(population.get(remainingSolutionIdx.get(i)), unboundSubregion, utopianPoint, normIntercepts);
            ConeSubRegion subRegion = subRegionManager.getConeSubRegion(unboundSubregion.get(selectedIdx));
            subRegion.setIdxSolution(remainingSolutionIdx.get(i));
            unboundSubregion.remove(selectedIdx);
        }
    }

    protected int nearestUnboundSubRegionIdx(DoubleSolution solution, List<Integer> unboundSubRegion, double[] utopianPoint, double[] normIntercepts) {
        int minIdx = 0;
        double[] observation = MOEACDUtils.calObservation(MOEACDUtils.normalize(solution, utopianPoint, normIntercepts));
        ConeSubRegion subRegion = subRegionManager.getConeSubRegion(unboundSubRegion.get(0));
        double minDis = MOEACDUtils.distance2(observation, subRegion.getRefDirection());
        for (int i = 1; i < unboundSubRegion.size(); ++i) {
            subRegion = subRegionManager.getConeSubRegion(unboundSubRegion.get(i));
            double tmp = MOEACDUtils.distance2(observation, subRegion.getRefDirection());
            if (tmp < minDis) {
                minDis = tmp;
                minIdx = i;
            }
        }
        return minIdx;
    }

    protected double calcConvergence(DoubleSolution solution, double[] utopianPoint, double[] normIntercepts) {
        double[] normObjectives = MOEACDUtils.normalize(solution, utopianPoint, normIntercepts);
        return MOEACDUtils.norm(normObjectives);
    }

    protected double calcConvergence(double[] normObjectives) {
        return MOEACDUtils.norm(normObjectives);
    }

    protected double calcDirectivity(DoubleSolution solution, double[] direction, double[] utopianPoint, double[] normIntercepts) {
        double[] normObjectives = MOEACDUtils.normalize(solution, utopianPoint, normIntercepts);
        double[] observation = MOEACDUtils.calObservation(normObjectives);
        return directivity(observation, direction);
    }

    protected double calcDirectivity(double[] normObjectives, double[] direction) {
        double[] observation = MOEACDUtils.calObservation(normObjectives);

        return directivity(observation, direction);
    }

    public void findD0() {
        D0Min = Double.POSITIVE_INFINITY;
        D0Mean = 0.0;
//        for (int i = 0; i < subRegionManager.getConeSubRegionsNum(); i++) {
//            double minDist = Double.POSITIVE_INFINITY;
//            double[] w1 = subRegionManager.getConeSubRegion(i).getRefObservation();
//            for (int j = 0; j < subRegionManager.getConeSubRegionsNum(); j++) {
//                if (i == j) continue;
//
//                double[] w2 = subRegionManager.getConeSubRegion(j).getRefObservation();
//                double tmp = MOEACDUtils.distance(w1, w2);
//                minDist = Math.min(tmp, minDist);
//            }
//            D0Min = Math.min(D0Min, minDist);
//            D0Mean += minDist;
//        }

        KDTree kdTree = subRegionManager.getKdTree();
        for (int i = 0; i < subRegionManager.getConeSubRegionsNum(); i++) {
            double[] refD = subRegionManager.getConeSubRegion(i).getRefDirection();
            List<double[]> nearestCoordinates = kdTree.queryKNearestCoordinates(refD, 2);
            double minDist = MOEACDUtils.distance(refD, nearestCoordinates.get(1));
            D0Min = Math.min(D0Min, minDist);
            D0Mean += minDist;
        }
        D0Mean /= subRegionManager.getConeSubRegionsNum();
    }

    protected boolean coneUpdate(DoubleSolution _solution, ConeSubRegion targetSubRegion, double[] utopianPoint, double[] normIntercepts) {
        int idxStoreInPop = targetSubRegion.getIdxSolution();
        DoubleSolution storedSolution = population.get(idxStoreInPop);
        ConeSubRegion storedSubRegion = locateConeSubRegion(storedSolution, utopianPoint, normIntercepts);
        boolean isUpdated = false;
        if (targetSubRegion == storedSubRegion) {
            DoubleSolution worseS = _solution;
            DoubleSolution betterS = getBetterSolutionByIndicator(_solution, storedSolution, targetSubRegion, utopianPoint, normIntercepts);
//            DoubleSolution betterS = getBetterSolutionByIndicator(_solution, storedSolution, targetSubRegion);
            if (betterS == _solution) {
                //has updated
                isUpdated = true;
                population.set(idxStoreInPop, _solution);
                worseS = storedSolution;
            }

            isUpdated |= coneNeighborUpdate(worseS, storedSubRegion, utopianPoint, normIntercepts);
        } else {
            isUpdated = true;
            population.set(idxStoreInPop, _solution);
            //cone update recursively
            coneUpdate(storedSolution, storedSubRegion, utopianPoint, normIntercepts);
        }

        return isUpdated;
    }

    protected boolean coneNeighborUpdate(DoubleSolution _solution, ConeSubRegion targetSubRegion, double[] utopianPoint, double[] normIntercepts) {
        List<Integer> neighbors = targetSubRegion.getNeighbors();
        int idxNeighborSubRegion = neighbors.get(randomGenerator.nextInt(1, neighbors.size() - 1));
//        int idxNeighborSubRegion = randomGenerator.nextInt(0,subRegionManager.getConeSubRegionsNum() - 1);

        ConeSubRegion neighborSubRegion = subRegionManager.getConeSubRegion(idxNeighborSubRegion);

        int idxNeighbor = neighborSubRegion.getIdxSolution();
        DoubleSolution storedSolution = population.get(idxNeighbor);
        DoubleSolution betterOne = getBetterSolutionForNeighborUpdate(_solution, storedSolution, neighborSubRegion, utopianPoint, normIntercepts);

        if (betterOne == _solution) {
            population.set(idxNeighbor, _solution);

            return true;
        }
        return false;
    }

    protected DoubleSolution getBetterSolutionForNeighborUpdate(DoubleSolution newSolution, DoubleSolution storedSolution, ConeSubRegion neighborSubRegion, double[] utopianPoint, double[] normIntercepts) {
        return getBetterSolutionForNeighborUpdateUnConstraint(newSolution, storedSolution, neighborSubRegion, utopianPoint, normIntercepts);
    }

    protected DoubleSolution getBetterSolutionByIndicator(DoubleSolution newSolution, DoubleSolution storedSolution, ConeSubRegion coneSubRegion, double[] utopianPoint, double[] normIntercepts) {
        return this.getBetterSolutionByIndicatorUnConstraint(newSolution, storedSolution, coneSubRegion.getRefDirection(), utopianPoint, normIntercepts, beta_ConeUpdate);
    }

    protected DoubleSolution getBetterSolutionByIndicator(DoubleSolution newSolution, DoubleSolution storedSolution, ConeSubRegion coneSubRegion) {
        boolean newFessible = isFessible(newSolution);
        boolean storeFessible = isFessible(storedSolution);
        if (newFessible && storeFessible) {
            double newFun = fitnessFunction(newSolution, coneSubRegion.getRefDirection());
            double storeFun = fitnessFunction(newSolution, coneSubRegion.getRefDirection());
            return newFun < storeFun ? newSolution : storedSolution;
        } else if (newFessible) {
            return newSolution;
        } else if (storeFessible) {
            return storedSolution;
        } else {
            double newCV = getOverallConstraintViolationDegree(newSolution);
            double storedCV = getOverallConstraintViolationDegree(storedSolution);
            return newCV < storedCV ? newSolution : storedSolution;
        }
    }

    protected DoubleSolution getBetterSolutionByIndicator(DoubleSolution newSolution, DoubleSolution storedSolution, double[] refDirection, double[] utopianPoint, double[] normIntercepts, double beta) {
        return getBetterSolutionByIndicatorUnConstraint(newSolution, storedSolution, refDirection, utopianPoint, normIntercepts, beta);
    }

    protected DoubleSolution getBetterSolutionForNeighborUpdateUnConstraint(DoubleSolution newSolution, DoubleSolution storedSolution, ConeSubRegion neighborSubRegion, double[] utopianPoint, double[] normIntercepts) {
        ConeSubRegion idealSubRegion = locateConeSubRegion(storedSolution, utopianPoint, normIntercepts);
        if (idealSubRegion != neighborSubRegion) {
            return getBetterSolutionByIndicatorUnConstraint(newSolution, storedSolution, neighborSubRegion.getRefDirection(), utopianPoint, normIntercepts, beta_NeighborUpdate);
        }
        return storedSolution;
    }

    protected DoubleSolution getBetterSolutionByIndicatorUnConstraint(DoubleSolution newSolution, DoubleSolution storedSolution, ConeSubRegion coneSubRegion, double[] utopianPoint, double[] normIntercepts) {
        return this.getBetterSolutionByIndicatorUnConstraint(newSolution, storedSolution, coneSubRegion.getRefDirection(), utopianPoint, normIntercepts, beta_ConeUpdate);
    }

    protected DoubleSolution getBetterSolutionByIndicatorUnConstraint(DoubleSolution newSolution, DoubleSolution storedSolution, double[] refDirection, double[] utopianPoint, double[] normIntercepts, double beta) {
        int domination = MOEACDUtils.dominateCompare(newSolution, storedSolution);
        if (domination == -1) {
            return newSolution;
        } else if (domination == 1 || domination == 2) {
            return storedSolution;
        } else {
//            double PDDNewSolution = PDD(newSolution, refDirection, utopianPoint, normIntercepts, beta);
//            double PDDStoreSolution = PDD(storedSolution, refDirection, utopianPoint, normIntercepts, beta);
            double PDDNewSolution = fitnessFunction(newSolution, refDirection);
            double PDDStoreSolution = fitnessFunction(storedSolution, refDirection);

            if (PDDNewSolution < PDDStoreSolution)
                return newSolution;
            else
                return storedSolution;
        }
    }

    protected double PDD(DoubleSolution solution, double[] direction, double[] utopianPoint, double[] normIntercepts, double beta) {
        double p = 1.0 * evaluations / maxEvaluations;
        double[] normObjectives = MOEACDUtils.normalize(solution, utopianPoint, normIntercepts);
        double Dc = calcConvergence(normObjectives);
        double[] observation = MOEACDUtils.calObservation(normObjectives);

        //
        double directivityCrowdness = directivity(observation, direction);

        return Dc * (1.0 + Math.pow(p, beta) * /*problem.getNumberOfObjectives() * */directivityCrowdness);
    }

    double fitnessFunction(Solution individual, double[] lambda) throws JMetalException {
        double fitness;


        if (AbstractMOEAD.FunctionType.TCH.equals(functionType)) {
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
        } else if (AbstractMOEAD.FunctionType.LP.equals(functionType)) {
            double sum = 0;
            for (int n = 0; n < problem.getNumberOfObjectives(); n++) {
                double diff = Math.pow(Math.abs(individual.getObjective(n) - idealPoint[n]), LP);
                double feval;
                if (Math.abs(lambda[n]) < Constant.TOLERATION) {
                    feval = 0;
                } else {
                    feval = diff * lambda[n];
                }
                sum += feval;
            }
            return Math.pow(sum, 1.0 / LP);
        } else if (AbstractMOEAD.FunctionType.AGG.equals(functionType)) {
            double sum = 0.0;
            for (int n = 0; n < problem.getNumberOfObjectives(); n++) {
                sum += (lambda[n]) * (individual.getObjective(n) - idealPoint[n]);
            }

            fitness = sum;

        } else if (AbstractMOEAD.FunctionType.PDD.equals(functionType)) {
            return PDD((DoubleSolution) individual, lambda, utopianPoint, normIntercepts, beta_ConeUpdate);
        } else if (AbstractMOEAD.FunctionType.PBI.equals(functionType)) {
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

    protected double getOverallConstraintViolationDegree(DoubleSolution solution) {
        double cv = (double) solution.getAttribute("overallConstraintViolationDegree");
        return cv >= 0 ? 0 : Math.abs(cv);
    }

    protected boolean isFessible(DoubleSolution solution) {
        return getOverallConstraintViolationDegree(solution) == 0;
    }

    protected double directivity(double[] observation, double[] direction) {
        int nObj = problem.getNumberOfObjectives();
        double lenLambda = 0.0;
        double crowdness = 0.0;
        double weightedCrowdness = 0.0;
        for (int i = 0; i < nObj; i++) {
            double w = direction[i];
            if (w < 0.0001)
                w = 0.0001;
            double tmp = Math.abs(observation[i] - direction[i]);
            crowdness += tmp;
//            crowdness += Math.pow(tmp,2.0);
            weightedCrowdness += (Math.pow(tmp, 2.0) / w);
//            z
            weightedCrowdness += (1 / w);
//            weightedCrowdness += (tmp/w );
            lenLambda += Math.pow(direction[i], 2.0);
        }
        lenLambda = Math.sqrt(lenLambda);
//        double preK = D0Mean/2.0*Math.sqrt(1.0*(nObj-1)/nObj);
//        return Math.sqrt(lenLambda*weightedCrowdness/Math.pow(preK,2.0));
        return Math.sqrt(lenLambda * weightedCrowdness) / D0Mean;
//        return lenLambda  * weightedCrowdness/D0Mean;
//        return lenLambda  * weightedCrowdness/nObj/D0Mean;
//        return crowdness/D0Mean * lenLambda * weightedCrowdness ;
    }

    protected List<DoubleSolution> reproduction(int idxSubRegion) {
        chooseMatingType();

        chooseCrossoverType();

        int parentPoolSize = 2;
        if (crossoverType == CrossoverType.DE)
            parentPoolSize = 3;

        List<DoubleSolution> parents = parentSelection(idxSubRegion, parentPoolSize);

        if (CrossoverType.DE == crossoverType) {
            DoubleSolution tmp = parents.get(0);
            parents.set(0, parents.get(parents.size() - 1));
            parents.set(parents.size() - 1, tmp);
        }

        List<DoubleSolution> children = null;
        if (CrossoverType.SBX == crossoverType)
            children = sbxCrossoverOperator.execute(parents);
        else if (CrossoverType.DE == crossoverType) {
            deCrossoverOperator.setCurrentSolution(parents.get(2));
            children = deCrossoverOperator.execute(parents);
        }

        mutationOperator.execute(children.get(0));

        return children;
    }

    protected List<DoubleSolution> parentSelection(int idxSubRegion, int parentPoolSize) {
        List<DoubleSolution> parents = new ArrayList<>(parentPoolSize);

        ConeSubRegion coneSubRegion = subRegionManager.getConeSubRegion(idxSubRegion);
        DoubleSolution solution = population.get(coneSubRegion.getIdxSolution());
        ConeSubRegion targetSubRegion = locateConeSubRegion(solution, utopianPoint, normIntercepts);
        if (targetSubRegion == coneSubRegion)
            parents.add(solution);

        List<Integer> neighbors = coneSubRegion.getNeighbors();
        if (neighbors.size() < parentPoolSize + 1)
            matingType = MatingType.GLOBAL;

        while (parents.size() < parentPoolSize) {

            int idxSubRegion1;
            int idxSubRegion2;
            if (matingType == MatingType.NEIGHBOR) {
                int idx1 = randomGenerator.nextInt(0, neighbors.size() - 1);
                idxSubRegion1 = neighbors.get(idx1);

                int idx2 = randomGenerator.nextInt(0, neighbors.size() - 1);
                idxSubRegion2 = neighbors.get(idx2);

                while (idxSubRegion1 == idxSubRegion2) {
                    idx2 = randomGenerator.nextInt(0, neighbors.size() - 1);
                    idxSubRegion2 = neighbors.get(idx2);
                }
            } else {
                idxSubRegion1 = randomGenerator.nextInt(0, subRegionManager.getConeSubRegionsNum() - 1);
                idxSubRegion2 = randomGenerator.nextInt(0, subRegionManager.getConeSubRegionsNum() - 1);

                while (idxSubRegion1 == idxSubRegion2) {
                    idxSubRegion2 = randomGenerator.nextInt(0, subRegionManager.getConeSubRegionsNum() - 1);
                }
            }

            DoubleSolution selectedSolution = tourmentSelection(idxSubRegion1, idxSubRegion2);

            boolean flag = true;
            for (DoubleSolution s : parents) {
                if (s == selectedSolution) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                parents.add(selectedSolution);
            }
        }
        return parents;
    }

    protected DoubleSolution tourmentSelection(int idx1, int idx2) {
        ConeSubRegion subRegion1 = subRegionManager.getConeSubRegion(idx1);
        ConeSubRegion subRegion2 = subRegionManager.getConeSubRegion(idx2);
        int idxSolution1 = subRegion1.getIdxSolution();
        int idxSolution2 = subRegion2.getIdxSolution();

        DoubleSolution solution1 = population.get(idxSolution1);
        DoubleSolution solution2 = population.get(idxSolution2);
        return tourmentSelection(subRegion1, solution1, subRegion2, solution2, utopianPoint, normIntercepts);
    }

    ;

    protected DoubleSolution tourmentSelection(ConeSubRegion subRegion1, DoubleSolution solution1, ConeSubRegion subRegion2, DoubleSolution solution2, double[] utopianPoint, double[] normIntercepts) {
        return tourmentSelectionUnConstraint(subRegion1, solution1, subRegion2, solution2, utopianPoint, normIntercepts);
    }

    ;

    protected DoubleSolution tourmentSelectionUnConstraint(ConeSubRegion subRegion1, DoubleSolution solution1, ConeSubRegion subRegion2, DoubleSolution solution2, double[] utopianPoint, double[] normIntercepts) {
        int domination = MOEACDUtils.dominateCompare(solution1, solution2);
        if (domination == -1 || (domination == 2 && randomGenerator.nextDouble(0.0, 1.0) < 0.5))
            return solution1;
        else if (domination == 1)
            return solution2;
        else {
            ConeSubRegion idealSubregion1 = locateConeSubRegion(solution1, utopianPoint, normIntercepts);
            boolean isInPlace1 = (idealSubregion1 == subRegion1);
            ConeSubRegion idealSubregion2 = locateConeSubRegion(solution2, utopianPoint, normIntercepts);
            boolean isInPlace2 = (idealSubregion2 == subRegion2);
            if (isInPlace1 && !isInPlace2)
                return solution1;
            else if (!isInPlace1 && isInPlace2)
                return solution2;
        }

        if (randomGenerator.nextDouble(0.0, 1.0) < 0.5)
            return solution1;
        else
            return solution2;
    }

    ;

    protected void chooseMatingType() {
        if (randomGenerator.nextDouble() < neighborhoodSelectionProbability)
            matingType = MatingType.NEIGHBOR;
        else
            matingType = MatingType.GLOBAL;
    }

    protected void chooseCrossoverType() {
//        if (randomGenerator.nextDouble() < chooseR)
        crossoverType = CrossoverType.SBX;
//        else {
//        crossoverType = CrossoverType.DE;
//        }
    }

    protected void collectForAdaptiveCrossover(boolean isUpdated) {
        if (crossoverType == CrossoverType.SBX) {
            Csbx++;
            if (isUpdated)
                Ssbx++;
        } else {
            Cde++;
            if (isUpdated)
                Sde++;
        }
    }

    protected void updateAdaptiveCrossover() {
        double rde = 1.0 * Sde / Cde;
        double rsbx = 1.0 * Ssbx / Csbx;
        if (!(Double.isNaN(rde) || Double.isNaN(rsbx) || rde + rsbx < Constant.TOLERATION)) {
            Rde = 0.5 * Rde + 0.5 * rde / (rde + rsbx);
            double upperBound = 0.9;
            double lowerBound = 0.1;
            Rde = Math.max(Rde, lowerBound);
            Rde = Math.min(Rde, upperBound);

            Rsbx = 0.5 * Rsbx + 0.5 * rsbx / (rde + rsbx);
            Rsbx = Math.max(Rsbx, lowerBound);
            Rsbx = Math.min(Rsbx, upperBound);

            chooseR = Rsbx;
        }

        Cde = 0;
        Sde = 0;
        Csbx = 0;
        Ssbx = 0;
    }

    @Override
    public String getName() {
        return "MOEA/CD";
    }

    @Override
    public String getDescription() {
        return "Cone Decomposition based Evolutionary Algorithm (Dynamic SBX and DE)";
    }
}
