package org.uma.jmetal.algorithm.singleobjective.evolutionstrategy;

import org.uma.jmetal.algorithm.impl.AbstractEvolutionStrategy;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.problem.ConstrainedProblem;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.comparator.ObjectiveComparator;
import org.uma.jmetal.util.comparator.impl.OverallConstraintViolationComparator;
import org.uma.jmetal.util.comparator.impl.StochasticRankingComparator;
import org.uma.jmetal.util.solutionattribute.impl.OverallConstraintViolation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by X250 on 2017/1/7.
 */
public class SRElitistEvolutionStrategy<S extends Solution<?>> extends AbstractEvolutionStrategy<S, S> {
    private int mu;
    protected int lambda;
    private int maxEvaluations;
    protected int evaluations;
    private MutationOperator<S> mutation;

    private Comparator<S> comparator;
    private double Pf = 0.45;

    private OverallConstraintViolationComparator<S> overallConstraintViolationComparator;
    private OverallConstraintViolation<S> overallConstraintViolationDegree;
    /**
     * Constructor
     */
    public SRElitistEvolutionStrategy(Problem<S> problem, int mu, int lambda, double Pf,int maxEvaluations,
                                    MutationOperator<S> mutation) {
        super(problem) ;
        this.mu = mu;
        this.lambda = lambda;
        this.maxEvaluations = maxEvaluations;
        this.mutation = mutation;
        this.Pf = Pf;

        comparator = new StochasticRankingComparator<S>(Pf,0);
        overallConstraintViolationComparator = new OverallConstraintViolationComparator<>();
        overallConstraintViolationDegree = new OverallConstraintViolation<>();
    }

    @Override protected void initProgress() {
        evaluations = 1;
    }

    @Override protected void updateProgress() {
        evaluations += lambda;
    }

    @Override protected boolean isStoppingConditionReached() {
        return evaluations >= maxEvaluations;
    }

    @Override protected List<S> createInitialPopulation() {
        List<S> population = new ArrayList<>(mu);
        for (int i = 0; i < mu; i++) {
            S newIndividual = getProblem().createSolution();
            population.add(newIndividual);
        }

        return population;
    }

    @Override protected List<S> evaluatePopulation(List<S> population) {
        for (S solution : population) {
            getProblem().evaluate(solution);
            ((ConstrainedProblem<S>) getProblem()).evaluateConstraints(solution);
        }
        findBestSolution(population);
        return population;
    }

    @Override protected List<S> selection(List<S> population) {
        return population;
    }

    @SuppressWarnings("unchecked")
    @Override protected List<S> reproduction(List<S> population) {
        List<S> offspringPopulation = new ArrayList<>(lambda + mu);
        for (int i = 0; i < mu; i++) {
            for (int j = 0; j < lambda / mu; j++) {
                S offspring = (S)population.get(i).copy();
                mutation.execute(offspring);
                offspringPopulation.add(offspring);
            }
        }

        return offspringPopulation;
    }

    private void findBestSolution(List<S> population){
        int idxBest = 0;
        for (int i = 1;i<population.size();i++){
            if(overallConstraintViolationComparator.compare(population.get(idxBest),population.get(i)) ==1 ) {
                idxBest = i;
            }
        }
        if(idxBest != 0){
            S tmp = population.get(0);
            population.set(0,population.get(idxBest));
            population.set(idxBest,tmp);
        }
    }

    private void SRBubbleSort(List<S> offspringPopulation){
        for (int i=1;i<offspringPopulation.size();i++){
            boolean hasSwap = false;
            for (int j = 1;j<offspringPopulation.size() -1;j++){
                if(comparator.compare(offspringPopulation.get(j),offspringPopulation.get(j+1)) == 1){
                    S tmp = offspringPopulation.get(j);
                    offspringPopulation.set(j,offspringPopulation.get(j+1));
                    offspringPopulation.set(j+1,tmp);
                    hasSwap = true;
                }
            }
            if(!hasSwap)
                break;
        }
    }
    @Override protected List<S> replacement(List<S> population,
                                            List<S> offspringPopulation) {
        for (int i = 0; i < mu; i++) {
            offspringPopulation.add(population.get(i));
        }

        SRBubbleSort(offspringPopulation);

        List<S> newPopulation = new ArrayList<>(mu);
        for (int i = 0; i < mu; i++) {
            newPopulation.add(offspringPopulation.get(i));
        }
        return newPopulation;
    }

    protected boolean isFeasible(S indiv){
        return overallConstraintViolationDegree.getAttribute(indiv) >= 0.0;
    }


    public List<S> getMeasurePopulation(){
        List<S> feasiblePopulation = new ArrayList<>(getPopulation().size());
        for (int i=0;i<getPopulation().size();i++){
            if(isFeasible(getPopulation().get(i)))
                feasiblePopulation.add(getPopulation().get(i));
        }
        return feasiblePopulation;
    }
    @Override public S getResult() {
        List<S> feasiblePopulation = getMeasurePopulation();
        if(feasiblePopulation.isEmpty())
            return null;
        findBestSolution(feasiblePopulation);
        return feasiblePopulation.get(0);
    }

    @Override public String getName() {
        return "SRElitistEA" ;
    }

    @Override public String getDescription() {
        return "Stochatic ranking Elitist Evolution Strategy Algorithm, i.e, (mu + lambda) EA" ;
    }
}