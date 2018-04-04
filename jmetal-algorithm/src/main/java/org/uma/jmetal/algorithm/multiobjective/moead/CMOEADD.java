package org.uma.jmetal.algorithm.multiobjective.moead;

import org.uma.jmetal.algorithm.multiobjective.moead.util.MOEADUtils;
import org.uma.jmetal.measure.Measurable;
import org.uma.jmetal.measure.MeasureManager;
import org.uma.jmetal.measure.impl.MyAlgorithmMeasures;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.problem.ConstrainedProblem;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.Constant;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.SolutionListUtils;
import org.uma.jmetal.util.comparator.impl.OverallConstraintViolationComparator;
import org.uma.jmetal.util.solutionattribute.impl.NumberOfViolatedConstraints;
import org.uma.jmetal.util.solutionattribute.impl.OverallConstraintViolation;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by X250 on 2016/9/4.
 */
public class CMOEADD extends MOEADD implements Measurable {
    public NumberOfViolatedConstraints<DoubleSolution> numberOfViolatedConstraints;
    protected MyAlgorithmMeasures measure;


    public CMOEADD(Problem<DoubleSolution> problem,
                   int populationSize,
                   int resultPopulationSize,
                   int maxEvaluations,
                   CrossoverOperator<DoubleSolution> crossover,
                   MutationOperator<DoubleSolution> mutation,
                   FunctionType functionType,
                   String dataDirectory,
                   double neighborhoodSelectionProbability,
                   int maximumNumberOfReplacedSolutions,
                   int neighborSize) {
        super(problem, populationSize, resultPopulationSize, maxEvaluations, crossover, mutation, functionType,
                dataDirectory, neighborhoodSelectionProbability, maximumNumberOfReplacedSolutions,
                neighborSize);

        differentialEvolutionCrossover = (DifferentialEvolutionCrossover) crossoverOperator;
        numberOfViolatedConstraints = new NumberOfViolatedConstraints<DoubleSolution>();
        measure = new MyAlgorithmMeasures<>();
        measure.initMeasures();
    }

    public CMOEADD(Problem<DoubleSolution> problem,
                   int populationSize,
                   int resultPopulationSize,
                   int maxEvaluations,
                   CrossoverOperator<DoubleSolution> crossover,
                   MutationOperator<DoubleSolution> mutation,
                   FunctionType functionType,
                   int[] arrayH,
                   double[] integratedTau,
                   double neighborhoodSelectionProbability,
                   int maximumNumberOfReplacedSolutions,
                   int neighborSize) {
        super(problem, populationSize, resultPopulationSize, maxEvaluations, crossover, mutation, functionType,
                arrayH, integratedTau, neighborhoodSelectionProbability, maximumNumberOfReplacedSolutions,
                neighborSize);

        differentialEvolutionCrossover = (DifferentialEvolutionCrossover) crossoverOperator;
        numberOfViolatedConstraints = new NumberOfViolatedConstraints<DoubleSolution>();
        measure = new MyAlgorithmMeasures<>();
        measure.initMeasures();

    }

    public CMOEADD(Problem<DoubleSolution> problem,
                   int populationSize,
                   int resultPopulationSize,
                   int maxEvaluations,
                   int maxGen,
                   CrossoverOperator<DoubleSolution> crossover,
                   MutationOperator<DoubleSolution> mutation,
                   FunctionType functionType,
                   int[] arrayH,
                   double[] integratedTau,
                   double neighborhoodSelectionProbability,
                   int maximumNumberOfReplacedSolutions,
                   int neighborSize) {
        super(problem, populationSize, resultPopulationSize, maxEvaluations, maxGen, crossover, mutation, functionType,
                arrayH, integratedTau, neighborhoodSelectionProbability, maximumNumberOfReplacedSolutions,
                neighborSize);

        differentialEvolutionCrossover = (DifferentialEvolutionCrossover) crossoverOperator;
        numberOfViolatedConstraints = new NumberOfViolatedConstraints<DoubleSolution>();
        measure = new MyAlgorithmMeasures<>();
        measure.initMeasures();

    }

    @Override
    public MeasureManager getMeasureManager() {
        return measure.getMeasureManager();
    }


    @Override
    public void run() {
        measure.durationMeasure.start();

        initializePopulation();
        initializeUniformWeight();
        initializeNeighborhood();
        initializeIdealPoint();
        initializeNadirPoint();


        // initialize the distance
        for (int i = 0; i < populationSize; i++) {
            double distance = calculateDistance2(population.get(i), lambda[i]);
            subregionDist[i][i] = distance;
        }


        // Non-dominated sorting for feasible solutions
        ranking = ranking.computeRanking(population);
        int curRank;
        for (int i = 0; i < populationSize; i++) {
            curRank = ranking.getAttribute(population.get(i));
            rankIdx[curRank][i] = 1;
        }

        evaluations = populationSize;
//        maxEvaluations = populationSize * maxGen;
        int gen = 0;
        do {
            int[] permutation = new int[populationSize];
            MOEADUtils.randomPermutation(permutation, populationSize);

//            for (int i = 0; i < populationSize && evaluations < maxEvaluations; i++) {
            for (int i = 0; i < populationSize; i++) {

                int subProblemId = permutation[i];

                NeighborType neighborType = chooseNeighborType();
                List<DoubleSolution> parents = parentSelection(subProblemId, neighborType);

                differentialEvolutionCrossover.setCurrentSolution(population.get(subProblemId));
                List<DoubleSolution> children = differentialEvolutionCrossover.execute(parents);

                DoubleSolution child1 = children.get(0);

                mutationOperator.execute(child1);

                problem.evaluate(child1);
                ((ConstrainedProblem<DoubleSolution>) problem).evaluateConstraints(child1);


//                evaluations += 1;

                updateIdealPoint(child1);

                updateNadirPoint(child1);

                updateArchive(child1);
            }
            gen++;
        } while (gen < maxGen);
        measure.durationMeasure.stop();
    }

    protected void initializePopulation() {
        for (int i = 0; i < populationSize; i++) {
            DoubleSolution newSolution = (DoubleSolution) problem.createSolution();

            problem.evaluate(newSolution);
            ((ConstrainedProblem<DoubleSolution>) problem).evaluateConstraints(newSolution);
            population.add(newSolution);
            subregionIdx[i][i] = 1;
        }
    }


    /**
     * If all solutions are feasible, go back to the original selection in MOEA/DD
     *
     * @param indiv
     * @param location
     */
    public void originalUpdate(DoubleSolution indiv, int location) {

        numRanks = nondominated_sorting_add(indiv);

        if (numRanks == 1) {
            deleteRankOne(indiv, location);
        } else {
            List<DoubleSolution> lastFront = new ArrayList<DoubleSolution>(populationSize);
            int frontSize = countRankOnes(numRanks - 1);
            if (frontSize == 0) {    // the last non-domination level only contains 'indiv'
                frontSize++;
                lastFront.add(indiv);
            } else {
                for (int i = 0; i < populationSize; i++) {
                    if (rankIdx[numRanks - 1][i] == 1)
                        lastFront.add(population.get(i));
                }
                if (ranking.getAttribute(indiv) == (numRanks - 1)) {
                    frontSize++;
                    lastFront.add(indiv);
                }
            }

            if (frontSize == 1 && lastFront.get(0).equals(indiv)) {    // the last non-domination level only has 'indiv'
                int curNC = countOnes(location);
                if (curNC > 0) {    // if the subregion of 'indiv' has other solution, drop 'indiv'
                    nondominated_sorting_delete(indiv);
                    return;
                } else {    // if the subregion of 'indiv' has no solution, keep 'indiv'
                    deleteCrowdRegion1(indiv, location);
                }
            } else if (frontSize == 1 && !lastFront.get(0).equals(indiv)) { // the last non-domination level only has one solution, but not 'indiv'
                int targetIdx = findPosition(lastFront.get(0));
                int parentLocation = findRegion(targetIdx);
                int curNC = countOnes(parentLocation);
                if (parentLocation == location)
                    curNC++;

                if (curNC == 1) {    // the subregion only has the solution 'targetIdx', keep solution 'targetIdx'
                    deleteCrowdRegion2(indiv, location);
                } else {    // the subregion contains some other solutions, drop solution 'targetIdx'
                    int indivRank = ranking.getAttribute(indiv);
                    int targetRank = ranking.getAttribute(population.get(targetIdx));
                    rankIdx[targetRank][targetIdx] = 0;
                    rankIdx[indivRank][targetIdx] = 1;

                    DoubleSolution targetSol = (DoubleSolution) population.get(targetIdx).copy();

//                    population.set(targetIdx, indiv);
                    replace(targetIdx, indiv);
                    subregionIdx[parentLocation][targetIdx] = 0;
                    subregionIdx[location][targetIdx] = 1;

                    // update the non-domination level structure
                    nondominated_sorting_delete(targetSol);
                }
            } else {
                double indivFitness = fitnessFunction(indiv, lambda[location]);

                // find the index of the solution in the last non-domination level, and its corresponding subregion
                int[] idxArray = new int[frontSize];
                int[] regionArray = new int[frontSize];

                for (int i = 0; i < frontSize; i++) {
                    idxArray[i] = findPosition(lastFront.get(i));
                    if (idxArray[i] == -1)
                        regionArray[i] = location;
                    else
                        regionArray[i] = findRegion(idxArray[i]);
                }

                // find the most crowded subregion, if more than one exist, keep them in 'crowdList'
                Vector<Integer> crowdList = new Vector<Integer>();
                int crowdIdx;
                int nicheCount = countOnes(regionArray[0]);
                if (regionArray[0] == location)
                    nicheCount++;
                crowdList.addElement(regionArray[0]);
                for (int i = 1; i < frontSize; i++) {
                    int curSize = countOnes(regionArray[i]);
                    if (regionArray[i] == location)
                        curSize++;
                    if (curSize > nicheCount) {
                        crowdList.clear();
                        nicheCount = curSize;
                        crowdList.addElement(regionArray[i]);
                    } else if (curSize == nicheCount) {
                        crowdList.addElement(regionArray[i]);
                    } else {
                        continue;
                    }
                }
                // find the index of the most crowded subregion
                if (crowdList.size() == 1) {
                    crowdIdx = crowdList.get(0);
                } else {
                    int listLength = crowdList.size();
                    crowdIdx = crowdList.get(0);
                    double sumFitness = sumFitness(crowdIdx);
                    if (crowdIdx == location)
                        sumFitness = sumFitness + indivFitness;
                    for (int i = 1; i < listLength; i++) {
                        int curIdx = crowdList.get(i);
                        double curFitness = sumFitness(curIdx);
                        if (curIdx == location)
                            curFitness = curFitness + indivFitness;
                        if (curFitness > sumFitness) {
                            crowdIdx = curIdx;
                            sumFitness = curFitness;
                        }
                    }
                }

                if (nicheCount == 0)
                    System.out.println("Impossible empty subregion!!!");
                else if (nicheCount == 1) { // if the subregion of each solution in the last non-domination level only has one solution, keep them all
                    deleteCrowdRegion2(indiv, location);
                } else { // delete the worst solution from the most crowded subregion in the last non-domination level
                    Vector<Integer> list = new Vector<Integer>();
                    for (int i = 0; i < frontSize; i++) {
                        if (regionArray[i] == crowdIdx)
                            list.addElement(i);
                    }
                    if (list.size() == 0) {
                        System.out.println("Cannot happen!!!");
                    } else {
                        double maxFitness, curFitness;
                        int targetIdx = list.get(0);
                        if (idxArray[targetIdx] == -1)
                            maxFitness = indivFitness;
                        else
                            maxFitness = fitnessFunction(population.get(idxArray[targetIdx]), lambda[crowdIdx]);
                        for (int i = 1; i < list.size(); i++) {
                            int curIdx = list.get(i);
                            if (idxArray[curIdx] == -1)
                                curFitness = indivFitness;
                            else
                                curFitness = fitnessFunction(population.get(idxArray[curIdx]), lambda[crowdIdx]);
                            if (curFitness > maxFitness) {
                                targetIdx = curIdx;
                                maxFitness = curFitness;
                            }
                        }
                        if (idxArray[targetIdx] == -1) {
                            nondominated_sorting_delete(indiv);
                            return;
                        } else {
                            int indivRank = ranking.getAttribute(indiv);
                            int targetRank = ranking.getAttribute(population.get(idxArray[targetIdx]));
                            rankIdx[targetRank][idxArray[targetIdx]] = 0;
                            rankIdx[indivRank][idxArray[targetIdx]] = 1;

                            DoubleSolution targetSol = (DoubleSolution) population.get(idxArray[targetIdx]).copy();

//                            population.set(idxArray[targetIdx], indiv);
                            replace(idxArray[targetIdx], indiv);
                            subregionIdx[crowdIdx][idxArray[targetIdx]] = 0;
                            subregionIdx[location][idxArray[targetIdx]] = 1;

                            // update the non-domination level structure
                            nondominated_sorting_delete(targetSol);
                        }
                    }
                }
            }
        }

        return;

    }

    //
    public void updateArchive(DoubleSolution indiv) {

        // find the location of 'indiv'
        //setLocation(indiv);
        int location = findLocation(indiv);//region.getAttribute(indiv);

        // find the infeasible solutions in the population
        int num_infeasible = 0;
        Vector<Integer> infeasibleList = new Vector<Integer>();
        for (int i = 0; i < populationSize; i++) {
            if (Math.abs(overallConstraintViolationDegree.getAttribute(population.get(i))) > 0.0) {
                infeasibleList.addElement(i);
                num_infeasible++;
            }
        }

        if (Math.abs(overallConstraintViolationDegree.getAttribute(indiv)) <= 0.0) {    // indiv is feasible
            if (num_infeasible == 0) { // all solutions are feasible
                originalUpdate(indiv, location);
            } else {

                // get indiv's non-domination level
                nondominated_sorting_add(indiv);

                int singleTargetIdx = infeasibleList.get(0);
                int multipleTargetIdx = singleTargetIdx;
                int targetRegion = findRegion(singleTargetIdx);

                int flag = 0;
                if (countOnes(targetRegion) > 1)
                    flag = 1;

                double multipleMax = Math.abs(overallConstraintViolationDegree.getAttribute(population.get(multipleTargetIdx)));
                double singleMax = multipleMax;
                for (int i = 1; i < num_infeasible; i++) {
                    int curIdx = infeasibleList.get(i);
                    int curRegion = findRegion(curIdx);
                    double curCV;
                    if (countOnes(curRegion) > 1) {
                        flag = 1;
                        curCV = Math.abs(overallConstraintViolationDegree.getAttribute(population.get(curIdx)));
                        if (curCV > multipleMax) {
                            multipleMax = curCV;
                            multipleTargetIdx = curIdx;
                        }
                    } else {
                        curCV = Math.abs(overallConstraintViolationDegree.getAttribute(population.get(curIdx)));
                        if (curCV > singleMax) {
                            singleMax = curCV;
                            singleTargetIdx = curIdx;
                        }
                    }
                }
                if (flag == 1) {
                    targetRegion = findRegion(multipleTargetIdx);
//                    population.set(multipleTargetIdx, indiv);
                    replace(multipleTargetIdx, indiv);
                    subregionIdx[targetRegion][multipleTargetIdx] = 0;
                    subregionIdx[location][multipleTargetIdx] = 1;
//

                } else {
                    targetRegion = findRegion(singleTargetIdx);

//                    population.set(singleTargetIdx, indiv);
                    replace(singleTargetIdx, indiv);
                    subregionIdx[targetRegion][singleTargetIdx] = 0;
                    subregionIdx[location][singleTargetIdx] = 1;

                }
            }
        } else {    // indiv is infeasible
            if (num_infeasible == 0)
                return;
            else {


                double singleMax, multipleMax;
                int singleTargetIdx = infeasibleList.get(0);
                int multipleTargetIdx = singleTargetIdx;
                int targetRegion = findRegion(singleTargetIdx);

                int curNC = countOnes(targetRegion);
                if (targetRegion == location)
                    curNC++;

                int flag = 0;
                if (curNC > 1)
                    flag = 1;

                multipleMax = Math.abs(overallConstraintViolationDegree.getAttribute(population.get(multipleTargetIdx)));
                singleMax = multipleMax;
                for (int i = 1; i < num_infeasible; i++) {
                    int curIdx = infeasibleList.get(i);
                    int curRegion = findRegion(curIdx);
                    curNC = countOnes(curRegion);
                    if (curRegion == location)
                        curNC++;
                    double curCV;
                    if (curNC > 1) {
                        flag = 1;
                        curCV = Math.abs(overallConstraintViolationDegree.getAttribute(population.get(curIdx)));
                        if (curCV > multipleMax) {
                            multipleMax = curCV;
                            multipleTargetIdx = curIdx;
                        }
                    } else {
                        curCV = Math.abs(overallConstraintViolationDegree.getAttribute(population.get(curIdx)));
                        if (curCV > singleMax) {
                            singleMax = curCV;
                            singleTargetIdx = curIdx;
                        }
                    }
                }
                if (flag == 1) {
                    if (Math.abs(overallConstraintViolationDegree.getAttribute(indiv)) < multipleMax) {

                        targetRegion = findRegion(multipleTargetIdx);
//                        population.set(multipleTargetIdx, indiv);
                        replace(multipleTargetIdx, indiv);
                        subregionIdx[targetRegion][multipleTargetIdx] = 0;
                        subregionIdx[location][multipleTargetIdx] = 1;
//

                    }

                } else {
                    if (Math.abs(overallConstraintViolationDegree.getAttribute(indiv)) < singleMax) {

                        targetRegion = findRegion(singleTargetIdx);
//                        population.set(singleTargetIdx, indiv);
                        replace(singleTargetIdx, indiv);
                        subregionIdx[targetRegion][singleTargetIdx] = 0;
                        subregionIdx[location][singleTargetIdx] = 1;

                    }
                }
            }
        }
        return;
    }
//
//    public void updateArchive(DoubleSolution indiv) {
//
//        // find the location of 'indiv'
//        //setLocation(indiv);
//        int location = findLocation(indiv);//region.getAttribute(indiv);
//
//        // find the infeasible solutions in the population
//        List<DoubleSolution> infeasibleSet = new ArrayList<>(populationSize+1);
//
//        for (int i = 0; i < populationSize; i++) {
//            if (Math.abs(overallConstraintViolationDegree.getAttribute(population.get(i))) > 0.0) {
//                infeasibleSet.add(population.get(i));
//            }
//        }
//
//        if (Math.abs(overallConstraintViolationDegree.getAttribute(indiv)) > 0.0) {
//            infeasibleSet.add(indiv);
//        }
//
//        if (infeasibleSet.size() == 0) { // all solutions are feasible
//            originalUpdate(indiv, location);
//        } else {
//            for (int i=0;i<infeasibleSet.size();i++){
//                int idx = i;
//                for (int j= i + 1;j<infeasibleSet.size();j++){
//                    if(Math.abs(overallConstraintViolationDegree.getAttribute(infeasibleSet.get(j)))  >  Math.abs(overallConstraintViolationDegree.getAttribute(infeasibleSet.get(idx)))){
//                        idx = j;
//                    }
//                }
//                if (idx != i){
//                    DoubleSolution tmp = infeasibleSet.get(idx);
//                    infeasibleSet.set(idx,infeasibleSet.get(i));
//                    infeasibleSet.set(i,tmp);
//                }
//            }
//
//            int flag = 0;
//            DoubleSolution selectedInd = null;
//            int selectedIdx = 0;
//            int targetRegion  = 0;
//            for (int i=0;i<infeasibleSet.size();i++){
//
//                if(infeasibleSet.get(i).equals(indiv))
//                    targetRegion = location;
//                else {
//                    selectedIdx = findPosition(infeasibleSet.get(i));
//                    targetRegion = findRegion(selectedIdx);
//                }
//
//                int c = countOnes(targetRegion);
//                if(targetRegion == location)
//                    c++;
//                if (c > 1){
//                    flag = 1;
//                    selectedInd = infeasibleSet.get(i);
//                    break;
//                }
//            }
//
//            if (flag == 0) {
//                selectedInd = infeasibleSet.get(0);
//                if(!selectedInd.equals(indiv)) {
//                    selectedIdx = findPosition(selectedInd);
//                    targetRegion = findRegion(selectedIdx);
//                }
//            }
//
//            if(selectedInd.equals(indiv)) {
////                // update the non-domination level structure
////                nondominated_sorting_delete(indiv);
//                return;
//            }
//
//            // get indiv's non-domination level
//            int indivRank  = nondominated_sorting_add(indiv);
//            int targetRank = ranking.getAttribute(selectedInd);
//            rankIdx[targetRank][selectedIdx] = 0;
//            rankIdx[indivRank][selectedIdx]  = 1;
//
//            DoubleSolution targetSol = (DoubleSolution) selectedInd.copy();
//
////          population.set(targetIdx, indiv);
//            replace(selectedIdx,indiv);
//            subregionIdx[targetRegion][selectedIdx] = 0;
//            subregionIdx[location][selectedIdx]     = 1;
//
//            // update the non-domination level structure
//            nondominated_sorting_delete(targetSol);
//        }
//
//        return;
//    }

    /**
     * Select two parents for reproduction
     *
     * @param subproblemId
     * @param neighbourType
     * @return
     */
    public List<Integer> matingSelection(int subproblemId, NeighborType neighbourType) {
        List<Integer> listOfSolutions = new ArrayList<>(2);

        int nLength = neighborhood[subproblemId].length;

        if (neighbourType == NeighborType.NEIGHBOR) {
            int idx1 = neighborhood[subproblemId][randomGenerator.nextInt(0, nLength - 1)];
            int idx2 = neighborhood[subproblemId][randomGenerator.nextInt(0, nLength - 1)];
            while (idx1 == idx2) {
                idx2 = neighborhood[subproblemId][randomGenerator.nextInt(0, nLength - 1)];
            }
            int p1 = tournamentSelection(idx1, idx2);

            int p2 = 0;
            do {
                idx1 = neighborhood[subproblemId][randomGenerator.nextInt(0, nLength - 1)];
                idx2 = neighborhood[subproblemId][randomGenerator.nextInt(0, nLength - 1)];
                while (idx1 == idx2) {
                    idx2 = neighborhood[subproblemId][randomGenerator.nextInt(0, nLength - 1)];
                }
                p2 = tournamentSelection(idx1, idx2);
            } while (p1 == p2);

            listOfSolutions.add(p1);
            listOfSolutions.add(p2);
        } else {
            int idx1 = randomGenerator.nextInt(0, populationSize - 1);
            int idx2 = randomGenerator.nextInt(0, populationSize - 1);
            while (idx1 == idx2) {
                idx2 = randomGenerator.nextInt(0, populationSize - 1);
            }
            int p1 = tournamentSelection(idx1, idx2);

            int p2 = 0;
            do {
                idx1 = randomGenerator.nextInt(0, populationSize - 1);
                idx2 = randomGenerator.nextInt(0, populationSize - 1);
                while (idx1 == idx2) {
                    idx2 = randomGenerator.nextInt(0, populationSize - 1);
                }
                p2 = tournamentSelection(idx1, idx2);
            } while (p1 == p2);

            listOfSolutions.add(p1);
            listOfSolutions.add(p2);
        }

//        int rnd1, rnd2, rnd3, rnd4;
//
//        rnd1 = randomGenerator.nextInt(0, populationSize - 1);
//        do {
//            rnd2 = randomGenerator.nextInt(0, populationSize - 1);
//            rnd3 = randomGenerator.nextInt(0, populationSize - 1);
//            rnd4 = randomGenerator.nextInt(0, populationSize - 1);
//        } while (rnd1 == rnd2 && rnd1 == rnd3 && rnd1 == rnd4 && rnd2 == rnd3 && rnd2 == rnd4 && rnd3 == rnd4);
//
//       listOfSolutions.add(tournamentSelection(rnd1,rnd2));
//        listOfSolutions.add(tournamentSelection(rnd3,rnd4));

        return listOfSolutions;
    } // matingSelection

    public int tournamentSelection(int idx1, int idx2) {
        DoubleSolution ind1 = population.get(idx1);
        DoubleSolution ind2 = population.get(idx2);
        double cv1 = Math.abs(overallConstraintViolationDegree.getAttribute(ind1));
        double cv2 = Math.abs(overallConstraintViolationDegree.getAttribute(ind2));
        if (cv1 <= 0.0 && cv2 <= 0.0) {
            switch (checkDominance(ind1, ind2)) {
                case 1:
                    return idx1;
                case -1:
                    return idx2;
                case 0: {
                    if (randomGenerator.nextDouble() < 0.5)
                        return idx1;
                    else
                        return idx2;
                }
            }
        } else if (cv1 < cv2)
            return idx1;
        else if (cv1 > cv2)
            return idx2;

        if (randomGenerator.nextDouble() < 0.5)
            return idx1;
        else return idx2;
    }


    @Override
    public String getName() {
        return "CMOEADD";
    }

    @Override
    public String getDescription() {
        return "Multi-Objective Evolutionary Algorithm based on Decomposition,  Version with Dominance Concept";
    }
}

