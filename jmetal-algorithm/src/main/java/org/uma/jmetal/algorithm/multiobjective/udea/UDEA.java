package org.uma.jmetal.algorithm.multiobjective.udea;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.moeacd.MOEACDUtils;
import org.uma.jmetal.measure.Measurable;
import org.uma.jmetal.measure.MeasureManager;
import org.uma.jmetal.measure.impl.MyAlgorithmMeasures;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.operator.impl.crossover.SBXCrossover;
import org.uma.jmetal.problem.ConstrainedProblem;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.Constant;
import org.uma.jmetal.util.KDTree;
import org.uma.jmetal.util.SolutionListUtils;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;
import org.uma.jmetal.util.solutionattribute.impl.OverallConstraintViolation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by X250 on 2017/1/3.
 */
public class UDEA implements Algorithm<List<DoubleSolution>> {

    protected MyAlgorithmMeasures<DoubleSolution> measureManager;
    protected Problem<DoubleSolution> problem;
    public OverallConstraintViolation<DoubleSolution> overallConstraintViolationDegree;

    //Ideal Point
    protected double[] idealPoint;
    //utopian point
    protected double[] utopianPoint;
    //nadir point
    protected double[] nadirPoint;
    //Reference Point
    protected double[] referencePoint;
    //intercepts of constructed hyper-plane
    protected double[] intercepts;
    //for normalization
    protected double[] normIntercepts;

    protected SubRegionManager subRegionManager;

    protected List<DoubleSolution> population;

    protected int populationSize;


    protected int evaluations;
    protected int maxEvaluations;

    protected JMetalRandom randomGenerator ;

    protected int neighborhoodSize;

    protected double neighborhoodSelectionProbability;
    public enum MatingType{NEIGHBOR,GLOBAL};
    protected MatingType matingType;
    public enum CrossoverType{SBX,DE};
    protected CrossoverType crossoverType;
    protected SBXCrossover sbxCrossoverOperator ;
    protected DifferentialEvolutionCrossover deCrossoverOperator ;
    protected MutationOperator<DoubleSolution> mutationOperator ;

    protected int updateInterval = 10;

    protected int extraEvolvingSize;
    protected List<Integer> evolvingIdxList =  null;
    protected double D0Mean = 0;
    protected double D0Min =0;

    protected double chooseR;
    protected double Rde;
    protected double Rsbx;
    protected int Cde;
    protected int Sde;
    protected int Csbx;
    protected int Ssbx;

    protected double beta_ConeUpdate = 3.0;
    protected double beta_NeighborUpdate = 0.0;


    public UDEA(Problem<DoubleSolution> problem,
                int[] H,
                double[] Tau,
                int populationSize,
                int maxEvaluations,
                int neighborhoodSize,
                double neighborhoodSelectionProbability,
                SBXCrossover sbxCrossoverOperator,
                DifferentialEvolutionCrossover deCrossoverOperator,
                MutationOperator<DoubleSolution> mutation){
        this.problem = problem;
        subRegionManager = new SubRegionManager(problem.getNumberOfObjectives(),H,Tau);
        this.populationSize = populationSize;

        this.maxEvaluations = maxEvaluations;
        this.mutationOperator = mutation;
        this.sbxCrossoverOperator = sbxCrossoverOperator;
        this.deCrossoverOperator = deCrossoverOperator;
        this.neighborhoodSize = neighborhoodSize;
        this.neighborhoodSelectionProbability = neighborhoodSelectionProbability;
        randomGenerator = JMetalRandom.getInstance();

        idealPoint = new double[problem.getNumberOfObjectives()];
        utopianPoint = new double[problem.getNumberOfObjectives()];
        nadirPoint = new double[problem.getNumberOfObjectives()];
        referencePoint = new double[problem.getNumberOfObjectives()];
        intercepts = new double[problem.getNumberOfObjectives()];
        normIntercepts = new double[problem.getNumberOfObjectives()];

        overallConstraintViolationDegree = new OverallConstraintViolation<>();

        evolvingIdxList = new ArrayList<>(2*populationSize);

        chooseR = 0.5;
        Rde = 0.5;
        Rsbx = 0.5;
        Cde = 0;
        Sde =0;
        Csbx = 0;
        Ssbx = 0;
    }

    public UDEA(Measurable measureManager,
                Problem<DoubleSolution> problem,
                int[] arrayH,
                double[] integratedTaus,
                int populationSize,
                int maxEvaluations,
                int neighborhoodSize,
                double neighborhoodSelectionProbability,
                SBXCrossover sbxCrossoverOperator,
                DifferentialEvolutionCrossover deCrossoverOperator,
                MutationOperator<DoubleSolution> mutation) {
        this(problem, arrayH, integratedTaus, populationSize, maxEvaluations, neighborhoodSize,
                neighborhoodSelectionProbability,
                sbxCrossoverOperator, deCrossoverOperator, mutation);
        this.measureManager = (MyAlgorithmMeasures<DoubleSolution>) measureManager;
        this.measureManager.initMeasures();
    }


    @Override public void run() {

        initializeSubRegions();
        initializePopulation();
        evaluations = populationSize;
        int gen = 1;

        initializeExtremePoints(population,utopianPoint,idealPoint,nadirPoint,referencePoint);
        initializeIntecepts(population,intercepts,utopianPoint,nadirPoint);
        initializeNormIntecepts(normIntercepts,utopianPoint,intercepts);

        associateSubRegion(population,utopianPoint,normIntercepts);

        do {
            calcEvolvingSubproblemList();
            for (int i = 0; i < populationSize; i++) {

                List<DoubleSolution> children = reproduction(evolvingIdxList.get(i));
                DoubleSolution child = children.get(0);

                problem.evaluate(child);
                if(problem instanceof ConstrainedProblem){
                    ((ConstrainedProblem<DoubleSolution>)problem).evaluateConstraints(child);
                }

                evaluations += 1;

                if(updateExtremePoints(child,utopianPoint,idealPoint,nadirPoint,referencePoint)){
                    if(problem.getNumberOfObjectives() == 1)
                        updateNormIntercepts(normIntercepts,utopianPoint,nadirPoint);
                    else
                        updateNormIntercepts(normIntercepts,utopianPoint,intercepts);
                }

                SubRegion subRegion = locateSubRegion(child,utopianPoint,normIntercepts);

                boolean isUpdated = coneUpdate(child, subRegion,utopianPoint,normIntercepts);

                collectForAdaptiveCrossover(isUpdated);
            }

            gen++;
            updateAdaptiveCrossover();
            population = collectPopulation();
            initializeNadirPoint(population,nadirPoint);
            updateIntercepts(population,intercepts,utopianPoint,nadirPoint);
            updateNormIntercepts(normIntercepts,utopianPoint,intercepts);

        } while (evaluations < maxEvaluations);
    }

    public void measureRun() {
        //Start
        measureManager.durationMeasure.start();
        initializeSubRegions();
        initializePopulation();
        evaluations = populationSize;
        int gen = 1;

        initializeExtremePoints(population,utopianPoint,idealPoint,nadirPoint,referencePoint);
        initializeIntecepts(population,intercepts,utopianPoint,nadirPoint);
        initializeNormIntecepts(normIntercepts,utopianPoint,intercepts);

        associateSubRegion(population,utopianPoint,normIntercepts);


        //calculate measure
        measureManager.updateMeasureProgress(getMeasurePopulation());
        do {
            calcEvolvingSubproblemList();
            for (int i = 0; i < populationSize; i++) {

                List<DoubleSolution> children = reproduction(evolvingIdxList.get(i));
                DoubleSolution child = children.get(0);

                problem.evaluate(child);
                if(problem instanceof ConstrainedProblem){
                    ((ConstrainedProblem<DoubleSolution>)problem).evaluateConstraints(child);
                }

                evaluations += 1;

                if(updateExtremePoints(child,utopianPoint,idealPoint,nadirPoint,referencePoint)){
                    if(problem.getNumberOfObjectives() == 1)
                        updateNormIntercepts(normIntercepts,utopianPoint,nadirPoint);
                    else
                        updateNormIntercepts(normIntercepts,utopianPoint,intercepts);
                }

                SubRegion subRegion = locateSubRegion(child,utopianPoint,normIntercepts);
                boolean isUpdated = coneUpdate(child, subRegion,utopianPoint,normIntercepts);

                collectForAdaptiveCrossover(isUpdated);
            }
            gen++;

            updateAdaptiveCrossover();
            population = collectPopulation();
            initializeNadirPoint(population,nadirPoint);
            updateIntercepts(population,intercepts,utopianPoint,nadirPoint);
            updateNormIntercepts(normIntercepts,utopianPoint,intercepts);

            //calculate measure
            measureManager.updateMeasureProgress(getMeasurePopulation());

        } while (evaluations < maxEvaluations);
        measureManager.durationMeasure.stop();
    }


    public MeasureManager getMeasureManager() {
        return measureManager.getMeasureManager();
    }


    /**
     * Initialize subproblems
     */

    protected void initializeSubRegions() {
        subRegionManager.generateSubRegionList();
        subRegionManager.initializeSubRegionsNeighbors(neighborhoodSize);
        findD0();
    }


    protected void initializePopulation() {
        population = new ArrayList<>(populationSize);
        for (int i = 0; i < populationSize; i++) {
            DoubleSolution newSolution = (DoubleSolution)problem.createSolution();
            problem.evaluate(newSolution);
            if(problem instanceof ConstrainedProblem){
                ((ConstrainedProblem<DoubleSolution>)problem).evaluateConstraints(newSolution);
            }
            population.add(newSolution);
        }
    }


    public int getCurrentEvalution(){return evaluations;};



    //initialize ideal point
    protected void initializeIdealPoint(List<DoubleSolution>population,double[] idealPoint){
        for (int i = 0; i < problem.getNumberOfObjectives(); i++) {
            idealPoint[i] = Double.POSITIVE_INFINITY;
        }
        for(int i=0;i<population.size();++i){
            updateIdealPoint(population.get(i),idealPoint);
        }
    }

    //initialized utopian point
    protected void initializedUtopianPoint(double[] utopianPoint,double[] idealPoint){
        updateUtopianPoint(utopianPoint,idealPoint);
    }

    //initialize nadir points
    protected void initializeNadirPoint(List<DoubleSolution>population,double[] nadirPoint){
        for (int i = 0; i < problem.getNumberOfObjectives(); i++) {
            nadirPoint[i] = Double.NEGATIVE_INFINITY;
        }
        for(int i=0;i<population.size();++i){
            updateNadirPoint(population.get(i),this.nadirPoint);
        }
    }

    protected void initializeReferencePoint(double[] referencePoint,double[] idealPoint,double[]nadirPoint){
        updateReferencePoint(referencePoint,idealPoint,nadirPoint);
    }

    public void initializeIntecepts(List<DoubleSolution>population,double[] intercepts, double[] utopianPoint, double[] nadirPoint){
        updateIntercepts(population,intercepts,utopianPoint,nadirPoint);
    }

    public void initializeNormIntecepts(double[] normIntercepts,double[] utopianPoint,double[] intercepts){
        updateNormIntercepts(normIntercepts,utopianPoint,intercepts);
    }

    //initialize extreme points
    protected void initializeExtremePoints(List<DoubleSolution> population, double[] utopianPoint,double[] idealPoint,double[] nadirPoint,double[] referencePoint){
        initializeIdealPoint(population,idealPoint);
        initializedUtopianPoint(utopianPoint,idealPoint);
        initializeNadirPoint(population,nadirPoint);
        initializeReferencePoint(referencePoint,idealPoint,nadirPoint);
    }

    protected boolean updateIdealPoint(DoubleSolution solution,double[] idealPoint) {
        boolean isIdealPointUpdated = false;
        for (int n = 0; n < problem.getNumberOfObjectives(); n++) {
            if (solution.getObjective(n) < idealPoint[n]) {
                idealPoint[n] = solution.getObjective(n);
                isIdealPointUpdated = true;
            }
        }
        return isIdealPointUpdated;
    }

    protected void updateUtopianPoint(double[] utopianPoint, double[] idealPoint){
        for (int i=0;i<problem.getNumberOfObjectives();i++)
            utopianPoint[i] = idealPoint[i] - Constant.TOLERATION;
    }

    protected boolean updateNadirPoint(DoubleSolution solution,double[] nadirPoint){
        boolean isNadirPointUpdated = false;
        for(int i=0;i<problem.getNumberOfObjectives();++i){
            if(solution.getObjective(i) > nadirPoint[i]) {
                nadirPoint[i] = solution.getObjective(i);
                isNadirPointUpdated = true;
            }
        }
        return isNadirPointUpdated;
    }

    protected void updateReferencePoint(double[] referencePoint,double[] idealPoint,double[] nadirPoint){
        for(int i=0;i<problem.getNumberOfObjectives();++i){
            referencePoint[i] = nadirPoint[i] + 1.0e3*(nadirPoint[i] - idealPoint[i]);
        }
    }

    protected boolean updateExtremePoints(DoubleSolution solution,double[] utopianPoint,double[] idealPoint,double[] nadirPoint,double[] referencePoint) {
        boolean isIdealPointUpdated = updateIdealPoint(solution,idealPoint);
        if(isIdealPointUpdated) {
            updateUtopianPoint(utopianPoint, idealPoint);
        }
        boolean isNadirPointUpdated = updateNadirPoint(solution,nadirPoint);
        if(isNadirPointUpdated || isIdealPointUpdated){
            updateReferencePoint(referencePoint,idealPoint,nadirPoint);
        }
        return isIdealPointUpdated||isNadirPointUpdated;
    }

    public void updateNormIntercepts(double[] normIntercepts , double[] utopianPoint, double[] intercepts){
        if(problem.getNumberOfObjectives() == 1){
            for(int i=0;i<problem.getNumberOfObjectives();++i){
                normIntercepts[i] = intercepts[i] - utopianPoint[i];
                if(normIntercepts[i] < Constant.TOLERATION)
                    normIntercepts[i] = Constant.TOLERATION;
            }
        }else {
            for(int i=0;i<problem.getNumberOfObjectives();++i) {
                normIntercepts[i] = 1.0;
            }
        }
        //normalization procedure in multi-, many-objective optimization
//        else {
//            for(int i=0;i<problem.getNumberOfObjectives();++i){
//                normIntercepts[i] = intercepts[i] - utopianPoint[i];
//                if(normIntercepts[i] < Constant.TOLERATION)
//                    normIntercepts[i] = Constant.TOLERATION;
//            }
//        }
    }

    //use for normalization procedure for multi-, many-objective optimization
    protected void updateIntercepts(List<DoubleSolution>population,double[] intercepts, double[] utopianPoint,double[] nadirPoint) {
        if(problem.getNumberOfObjectives() == 1) {
            for (int i=0;i<problem.getNumberOfObjectives();i++)
                intercepts[i] = nadirPoint[i];
        }
////        normalization procedure in multi-, many-objective optimization
//        else{
//            List<List<Double>> extremePoints = findExtremePoints(population,utopianPoint);
//            constructHyperplaneAndUpdateIntercepts(intercepts,extremePoints,utopianPoint,nadirPoint);
//        }
    }

    protected List<List<Double>> findExtremePoints(List<DoubleSolution>population,double[] utopianPoint) {

        List<List<Double>> extremePoints = new ArrayList<>(problem.getNumberOfObjectives());
        for (int i = 0; i < problem.getNumberOfObjectives(); ++i) {
            int idxExtremeInd = -1;
            double min_Fitness = Double.POSITIVE_INFINITY;
            for (int j = 0; j < population.size(); ++j) {
                double fitness = ASF(population.get(j), i,utopianPoint);
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

    protected double ASF(DoubleSolution s, int index, double[] utopianPoint) {
        double max_ratio = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < s.getNumberOfObjectives(); i++) {
            double weight = (index == i) ? 1.0 : 0.000001;
            max_ratio = Math.max(max_ratio, (s.getObjective(i) - utopianPoint[i])/weight);
        }
        return max_ratio;
    }

    protected void constructHyperplaneAndUpdateIntercepts(double[] intercepts,List<List<Double>> extremePoints,double[] utopianPoint, double[] nadirPoint){
        // Check whether there are duplicate extreme points.
        int numberOfObjectives = problem.getNumberOfObjectives();

        boolean duplicate = false;
        boolean isQualified = true;
        for (int i=0; !duplicate && i< extremePoints.size(); i+=1)
        {
            for (int j=i+1; !duplicate && j<extremePoints.size(); j+=1)
            {
                int k=0;
                int c =0;
                for(;k<numberOfObjectives;++k){
                    if(Math.abs(extremePoints.get(i).get(k) -  extremePoints.get(j).get(k)) < Constant.TOLERATION){
                        c++;
                    }
                }
                if(c == numberOfObjectives)
                    duplicate = true;
            }
        }
        if (!duplicate)
        {
            for (int i=0;i<extremePoints.size();i++) {
                for (int j = 0; j < problem.getNumberOfObjectives(); ++j)
                    extremePoints.get(i).set(j,extremePoints.get(i).get(j) - utopianPoint[j]);
            }

            // Find the equation of the hyperplane
            List<Double> b = new ArrayList<>(); //(pop[0].objs().size(), 1.0);
            for (int i =0; i < numberOfObjectives;i++)
                b.add(1.0);

            List<Double> x = Utils.guassianElimination(extremePoints, b);

            // Find intercepts
            for (int f=0; f<numberOfObjectives; f+=1)
            {
                if(Double.isNaN(x.get(f)) || x.get(f) <= Constant.TOLERATION){
                    isQualified = false;
                    break;
                }
                intercepts[f] = (1.0/x.get(f));
                if(Double.isNaN(intercepts[f]) || intercepts[f] <= 0.0){
                    isQualified = false;
                    break;
                }
            }
        }
        if(duplicate || !isQualified)
        {
            for (int f=0; f<numberOfObjectives; f+=1)
                intercepts[f] = nadirPoint[f];
        }
        else{
            for (int f=0; f<numberOfObjectives; f+=1)
                intercepts[f] += utopianPoint[f];
        }
    }

    protected SubRegion locateSubRegion(DoubleSolution solution,double[] utopianPoint,double[] normIntercepts) {
        double[] normObjectives = Utils.normalize(solution,utopianPoint, normIntercepts);
        return  subRegionManager.locateSubRegion(normObjectives);
    }

    protected SubRegion locateSubRegion(double[]  normObjectives) {
        return subRegionManager.locateSubRegion(normObjectives);
    }

    protected int updateExtraEvolvingSize(){
        double maxES = Math.min(subRegionManager.getSubRegionsNum()/5.0,5.0*subRegionManager.getMarginalSubRegionIdxList().size());
        return  extraEvolvingSize = (int) Math.ceil(Utils.sigmoid(1.0*evaluations/maxEvaluations,15.0,0.382)*maxES);
    }

    protected void calcEvolvingSubproblemList() {

        if (problem.getNumberOfObjectives() == 1) {
            evolvingIdxList = new ArrayList<>(subRegionManager.getSubRegionsNum());
            double[] observation = new double[]{0.0};
            int index = subRegionManager.indexing(observation);
            for (int i = 0; i < subRegionManager.getSubRegionsNum(); i++) {
                evolvingIdxList.add(index);
            }
        } else {
            extraEvolvingSize = updateExtraEvolvingSize();

            int evolvingSize = subRegionManager.getSubRegionsNum() + extraEvolvingSize;

            evolvingIdxList = new ArrayList<>(evolvingSize);

            for (int i = 0; i < subRegionManager.getSubRegionsNum(); i++) {
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
    }


    //update the association between cone subregion and solution
    protected void associateSubRegion(List<DoubleSolution> population,double[] utopianPoint,double[] normIntercepts) {

        //clearing the associate information
        for (int i = 0; i < subRegionManager.getSubRegionsNum(); ++i) {
            subRegionManager.getSubRegion(i).setSolution(null);
        }

        List<DoubleSolution> remainingSolutions = new ArrayList<>(population.size());
        for (int i = 0; i < population.size(); ++i) {
            //find the subregion which the solution belongs to
            SubRegion subRegion = locateSubRegion(population.get(i),utopianPoint,normIntercepts);
            if (subRegion.getSolution() == null) {//No solution has been bound to this subregion
                //bind it
                subRegion.setSolution(population.get(i));
            } else {
                DoubleSolution boundSolution = subRegion.getSolution();
                DoubleSolution worserOne = population.get(i);
                //choose the better one for subregion by comparing their indicators using in the algorithm
                DoubleSolution betterOne = getBetterSolutionByIndicator(population.get(i), boundSolution, subRegion,utopianPoint,normIntercepts);
                if (betterOne ==(population.get(i))) {
                    //replace the bound one
                    subRegion.setSolution(population.get(i));
                    worserOne = boundSolution;
                }
                //record the worst one
                remainingSolutions.add(worserOne);
            }
        }

        List<SubRegion> unboundSubregion = new ArrayList<>(subRegionManager.getSubRegionsNum());
        for (int i=0;i<subRegionManager.getSubRegionsNum();i++){
            SubRegion subRegion = subRegionManager.getSubRegion(i);
            if(subRegion.getSolution() == null){
                unboundSubregion.add(subRegion);
            }
        }

        Collections.shuffle(remainingSolutions);

        for (int i=0;i<remainingSolutions.size()&&(!unboundSubregion.isEmpty());i++){
            int selectedIdx = nearestUnboundSubRegionIndex(remainingSolutions.get(i), unboundSubregion,utopianPoint,normIntercepts);
            SubRegion subRegion = unboundSubregion.get(selectedIdx);
            subRegion.setSolution(remainingSolutions.get(i));
            unboundSubregion.remove(selectedIdx);
        }
    }

    protected int nearestUnboundSubRegionIndex(DoubleSolution ind,List<SubRegion> unboundSubRegion,double[] utopianPoint,double[] normIntercepts){
        int minIdx = 0;
        double[] observation = Utils.calObservation(Utils.normalize(ind,utopianPoint,normIntercepts));
        SubRegion subRegion = unboundSubRegion.get(0);
        double minDis = Utils.distance2(observation,subRegion.getDirection());
        for(int i=1;i<unboundSubRegion.size();++i){
            subRegion = unboundSubRegion.get(i);
            double tmp = Utils.distance2(observation,subRegion.getDirection());
            if(tmp < minDis){
                minDis = tmp;
                minIdx = i;
            }
        }
        return minIdx;
    }


    protected double calcConvergence(DoubleSolution solution,double[] direction,double[] utopianPoint,double[] normIntercepts){
        double[] normObjectives = Utils.normalize(solution, utopianPoint, normIntercepts);
        return Utils.norm(normObjectives);
    }

    protected double calcConvergence(double[] normObjectives,double[] direction){
        return Utils.norm(normObjectives);
    }

    protected double calcDiversity(DoubleSolution solution,double[] direction,double[] utopianPoint,double[] normIntercepts){
        double[] normObjectives = Utils.normalize(solution, utopianPoint, normIntercepts);
        double[] observation = Utils.calObservation(normObjectives);
        return weightedCrowd(observation,direction);
    }

    protected double calcDiversity(double[] normObjectives,double[] weight){
        double[] observation = Utils.calObservation(normObjectives);

        return weightedCrowd(observation,weight);
    }

    public void findD0() {
        D0Min = Double.POSITIVE_INFINITY;
        D0Mean = 0.0;
//        for (int i = 0; i < subRegionManager.getSubRegionsNum(); i++) {
//            double minDist = Double.POSITIVE_INFINITY;
//            double[] w1 = subRegionManager.getSubRegion(i).getDirection();
//            for (int j = 0; j < subRegionManager.getSubRegionsNum(); j++) {
//                if (i == j) continue;
//
//                double[] w2 = subRegionManager.getSubRegion(j).getDirection();
//                double tmp = MOEACDUtils.distance(w1, w2);
//                minDist = Math.min(tmp, minDist);
//            }
//            D0Min = Math.min(D0Min, minDist);
//            D0Mean += minDist;
//        }
        KDTree kdTree = subRegionManager.getKdTree();
        for (int i = 0; i < subRegionManager.getSubRegionsNum(); i++) {
            double[] refV = subRegionManager.getSubRegion(i).getDirection();
            List<double[]> nearestCoordinates = kdTree.queryKNearestCoordinates(refV,2);
            double minDist = Utils.distance(refV,nearestCoordinates.get(1));
            D0Min = Math.min(D0Min, minDist);
            D0Mean += minDist;
        }
        D0Mean /= subRegionManager.getSubRegionsNum();
    }

    protected  boolean coneUpdate(DoubleSolution solution,SubRegion targetSubRegion,double[] utopianPoint,double[] normIntercepts) {

        DoubleSolution storeSolution = targetSubRegion.getSolution();
        SubRegion storeSubRegion = locateSubRegion(storeSolution,utopianPoint,normIntercepts);
        DoubleSolution betterOne = null;
        boolean isUpdated = false;
        if(targetSubRegion == (storeSubRegion)){
            DoubleSolution worserOne = solution;
            betterOne = getBetterSolutionByIndicator(solution,storeSolution,targetSubRegion,utopianPoint,normIntercepts);
            if(betterOne == (solution)){
                //has updated
                isUpdated = true;
                targetSubRegion.setSolution(solution);
                worserOne = storeSolution;
            }
            isUpdated |= coneNeighborUpdate(worserOne,targetSubRegion,utopianPoint,normIntercepts);
        }
        else{
            isUpdated = true;
            //cone update recursively
            targetSubRegion.setSolution(solution);
            coneUpdate(storeSolution,storeSubRegion,utopianPoint,normIntercepts);
        }

        return isUpdated;
    }


    protected  boolean coneNeighborUpdate(DoubleSolution solution,SubRegion targetSubRegion,double[] utopianPoint,double[] normIntercepts) {
        List<Integer> neighbors = targetSubRegion.getNeighbors();
        int idxNeighborSubRegion = neighbors.get(randomGenerator.nextInt(0,neighbors.size()-1));
        SubRegion neighborSubRegion = subRegionManager.getSubRegion(idxNeighborSubRegion);

        DoubleSolution storeNeighbor = neighborSubRegion.getSolution();

        DoubleSolution betterOne = getBetterSolutionForNeighborUpdate(solution,storeNeighbor,neighborSubRegion,utopianPoint,normIntercepts);
        if(betterOne == (solution)){
            neighborSubRegion.setSolution(solution);
            return true;
        }

        return false;
    }

    protected DoubleSolution getBetterSolutionForNeighborUpdate(DoubleSolution newSolution,DoubleSolution storeSolution,SubRegion neighborSubRegion,double[] utopianPoint,double[] normIntercepts){
        return getBetterSolutionForNeighborUpdateUnConstraint(newSolution,storeSolution,neighborSubRegion,utopianPoint,normIntercepts);
    }


    protected  DoubleSolution getBetterSolutionByIndicator(DoubleSolution newSolution, DoubleSolution storeSolution, SubRegion subRegion, double[] utopianPoint,double[] normIntercepts) {
        return this.getBetterSolutionByIndicatorUnConstraint(newSolution,storeSolution,subRegion.getDirection(),utopianPoint,normIntercepts,beta_ConeUpdate);
    }

    protected  DoubleSolution getBetterSolutionByIndicator(DoubleSolution newSolution, DoubleSolution storeSolution, double[] refDirection,double[] utopianPoint,double[] normIntercepts, double beta) {
        return getBetterSolutionByIndicatorUnConstraint(newSolution,storeSolution,refDirection,utopianPoint,normIntercepts,beta);
    }

    protected DoubleSolution getBetterSolutionForNeighborUpdateUnConstraint(DoubleSolution newSolution,DoubleSolution storeSolution,SubRegion neighborSubRegion,double[] utopianPoint,double[] normIntercepts){
        SubRegion idealSubRegion = locateSubRegion(storeSolution,utopianPoint,normIntercepts);
        if(!(idealSubRegion == neighborSubRegion)) {
//            int domination = Utils.dominateCompare(newSolution, storeSolution);
//            if (domination == -1) {
//                return newSolution;
//            }else if(domination == 1 || domination == 2){
//                return storeSolution;
//            }else {
            return getBetterSolutionByIndicator(newSolution, storeSolution, neighborSubRegion.getDirection(), utopianPoint,normIntercepts,beta_NeighborUpdate);
//            }
        }
        return storeSolution;
    }


    protected  DoubleSolution getBetterSolutionByIndicatorUnConstraint(DoubleSolution newSolution, DoubleSolution storeSolution, SubRegion subRegion,double[] utopianPoint,double[] normIntercepts) {
        return this.getBetterSolutionByIndicator(newSolution,storeSolution,subRegion.getDirection(),utopianPoint,normIntercepts,beta_ConeUpdate);
    }

    protected  DoubleSolution getBetterSolutionByIndicatorUnConstraint(DoubleSolution newSolution, DoubleSolution storeSolution, double[] refDirection,double[] utopianPoint,double[] normIntercepts, double beta) {
//        int domination = Utils.dominateCompare(newSolution,storeSolution);
//        if(domination == -1 ){
//            return newSolution;
//        }
//        else if(domination == 1|| domination == 2){
//            return storeSolution;
//        }
//        else {
        double PDDNewSolution = PDD(newSolution, refDirection,utopianPoint,normIntercepts, beta);
        double PDDStoreSolution = PDD(storeSolution, refDirection,utopianPoint,normIntercepts, beta);

        if (PDDNewSolution < PDDStoreSolution)
            return newSolution;
        else
            return storeSolution;
//        }
    }



    protected  double PDD(DoubleSolution solution,double[] direction, double[] utopianPoint,double[] normIntercepts,double beta) {

        double p = 1.0*evaluations/maxEvaluations;
        double[] normObjectives = Utils.normalize(solution,utopianPoint,normIntercepts);
        double Dc = calcConvergence(normObjectives,direction);
        double[] observation = Utils.calObservation(normObjectives);

        double weightedCrowd = weightedCrowd(observation,direction);

        return Dc*(1.0 + Math.pow(p,beta)* problem.getNumberOfObjectives() * weightedCrowd);
    }

    protected double weightedCrowd(double[] observation,double[] direction) {

        double crowdedSum = 0.0;
        double closeness = 0.0;
        for (int i = 0; i < problem.getNumberOfObjectives(); i++) {
            double w = direction[i];
            if (w < 0.0001)
                w = 0.0001;
            double tmp = Math.abs(observation[i] - direction[i]);
            crowdedSum += ( tmp / w);
            closeness += tmp*tmp;
        }
        closeness = Math.sqrt(closeness);
        return crowdedSum * closeness/D0Mean;
    }


    protected List<DoubleSolution> reproduction(int idxSubRegion){
        chooseMatingType();

        chooseCrossoverType();

        int parentPoolSize = 2;
        if(crossoverType == CrossoverType.DE)
            parentPoolSize = 3;

        List<DoubleSolution> parents = parentSelection(idxSubRegion,parentPoolSize);

        if(CrossoverType.DE == crossoverType){
            DoubleSolution tmp = parents.get(0);
            parents.set(0,parents.get(parents.size()-1));
            parents.set(parents.size()-1,tmp);
        }

        List<DoubleSolution> children = null;
        if (CrossoverType.SBX == crossoverType)
            children = sbxCrossoverOperator.execute(parents);
        else if(CrossoverType.DE == crossoverType){
            deCrossoverOperator.setCurrentSolution(parents.get(2));
            children = deCrossoverOperator.execute(parents);
        }

        mutationOperator.execute(children.get(0));


        return children;
    }


    protected List<DoubleSolution> parentSelection(int idxSubRegion,int parentPoolSize) {
        List<DoubleSolution> parents = new ArrayList<>(parentPoolSize);

        DoubleSolution solution = subRegionManager.getSubRegion(idxSubRegion).getSolution();
        SubRegion targetSubRegion = locateSubRegion(solution,utopianPoint,normIntercepts);
        if (targetSubRegion == (subRegionManager.getSubRegion(idxSubRegion)))
            parents.add(solution);

        List<Integer> neighbors = subRegionManager.getSubRegion(idxSubRegion).getNeighbors();
        if(neighbors.size() < parentPoolSize + 1)
            matingType = MatingType.GLOBAL;

        while (parents.size() < parentPoolSize) {

            int idxSubRegion1;
            int idxSubRegion2;
            if(matingType == MatingType.NEIGHBOR) {
                int idx1 = randomGenerator.nextInt(0, neighbors.size()  - 1);
                idxSubRegion1 = neighbors.get(idx1);

                int idx2 = randomGenerator.nextInt(0, neighbors.size()  - 1);
                idxSubRegion2 = neighbors.get(idx2);

                while (idxSubRegion1 == idxSubRegion2) {
                    idx2 = randomGenerator.nextInt(0, neighbors.size() - 1);
                    idxSubRegion2 = neighbors.get(idx2);
                }
            }else{
                idxSubRegion1 = randomGenerator.nextInt(0,subRegionManager.getSubRegionsNum()-1);
                idxSubRegion2 = randomGenerator.nextInt(0,subRegionManager.getSubRegionsNum()-1);

                while (idxSubRegion1 == idxSubRegion2) {
                    idxSubRegion2 = randomGenerator.nextInt(0,subRegionManager.getSubRegionsNum()-1);
                }
            }

            DoubleSolution selectedSolution = tourmentSelection(idxSubRegion1,idxSubRegion2);

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
        return parents ;
    }

    protected DoubleSolution tourmentSelection(int idx1,int idx2){
        SubRegion subRegion1 = subRegionManager.getSubRegion(idx1);
        SubRegion subRegion2 = subRegionManager.getSubRegion(idx2);
        DoubleSolution solution1 = subRegion1.getSolution();
        DoubleSolution solution2 = subRegion2.getSolution();
        return tourmentSelection(subRegion1,solution1,subRegion2,solution2,utopianPoint,normIntercepts);
    };

    protected DoubleSolution tourmentSelection(SubRegion subRegion1, DoubleSolution solution1, SubRegion subRegion2, DoubleSolution solution2,double[] utopianPoint,double[] normIntercepts){
        return tourmentSelectionUnConstraint(subRegion1,solution1,subRegion2,solution2,utopianPoint,normIntercepts);
    };

    protected DoubleSolution tourmentSelectionUnConstraint(SubRegion subRegion1, DoubleSolution solution1, SubRegion subRegion2, DoubleSolution solution2,double[]utopianPoint,double[] normIntercepts){
        int domination = MOEACDUtils.dominateCompare(solution1,solution2);
        if(domination == -1 || (domination == 2 && randomGenerator.nextDouble(0.0,1.0) < 0.5))
            return solution1;
        else if(domination == 1)
            return solution2;
        else {
            SubRegion idealSubregion1 = locateSubRegion(solution1,utopianPoint,normIntercepts);
            boolean isInPlace1 = idealSubregion1 ==(subRegion1);
            SubRegion idealSubregion2 = locateSubRegion(solution2,utopianPoint,normIntercepts);
            boolean isInPlace2 = idealSubregion2 == (subRegion2);
            if(isInPlace1 && !isInPlace2)
                return solution1;
            else if(!isInPlace1 && isInPlace2)
                return solution2;
        }

        if (randomGenerator.nextDouble(0.0,1.0) < 0.5)
            return solution1;
        else
            return solution2;
    };

    protected void chooseMatingType(){
        if(randomGenerator.nextDouble() < neighborhoodSelectionProbability)
            matingType = MatingType.NEIGHBOR;
        else
            matingType = MatingType.GLOBAL;
    }

    protected void chooseCrossoverType(){
        if (randomGenerator.nextDouble() < chooseR)
            crossoverType = CrossoverType.SBX;
        else {
            crossoverType = CrossoverType.DE;
        }
    }

    protected void collectForAdaptiveCrossover(boolean isUpdated){
        if(crossoverType == CrossoverType.SBX){
            Csbx++;
            if(isUpdated)
                Ssbx++;
        }else {
            Cde++;
            if(isUpdated)
                Sde++;
        }
    }

    protected void updateAdaptiveCrossover(){
        double rde = 1.0*Sde/Cde;
        double rsbx = 1.0*Ssbx/Csbx;
        if(!(Double.isNaN(rde) || Double.isNaN(rsbx) || rde + rsbx < Constant.TOLERATION)) {
            Rde = 0.5 * Rde + 0.5 * rde/(rde + rsbx);
            double upperBound = 0.9;
            double lowerBound = 0.1;
            Rde = Math.max(Rde,lowerBound);
            Rde = Math.min(Rde, upperBound);

            Rsbx = 0.5 * Rsbx + 0.5 * rsbx/(rde + rsbx);
            Rsbx = Math.max(Rsbx,lowerBound);
            Rsbx = Math.min(Rsbx,upperBound);

            chooseR = Rsbx;
        }

        Cde = 0;
        Sde =0;
        Csbx = 0;
        Ssbx = 0;
    }



    public List<DoubleSolution> collectPopulation() {
        population = new ArrayList<>(subRegionManager.getSubRegionsNum());
        List<SubRegion> subRegionsList = subRegionManager.getSubRegionsList();
        for (int i=0;i<subRegionsList.size();i++){
            DoubleSolution solution = subRegionsList.get(i).getSolution();
            if(solution!= null)
                population.add(solution);
        }
        return population;
    }

    public List<DoubleSolution> getPopulation() {
        return collectPopulation();
    }

    public List<DoubleSolution> getMeasurePopulation(){return collectPopulation();}

    protected boolean isFeasible(DoubleSolution solution){
        return overallConstraintViolationDegree.getAttribute(solution) >= 0.0;
    }

    @Override public List<DoubleSolution> getResult() {
        List<DoubleSolution> pop = getMeasurePopulation();
        if(pop.isEmpty())
            return pop;
        else
            return SolutionListUtils.getNondominatedSolutions(pop);
    }

    @Override public String getName() {
        return "U-DEA" ;
    }

    @Override public String getDescription() {
        return "Unified Decompositon-based Evolutionary Algorithm for Mono-, Multi- and Many-objective Optimization" ;
    }
}
