package org.uma.jmetal.operator.impl.crossover;

import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.solution.util.RepairDoubleSolution;
import org.uma.jmetal.solution.util.RepairDoubleSolutionAtBounds;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by X250 on 2016/9/12.
 */
public class HXCrossover implements CrossoverOperator<DoubleSolution> {
    /** EPS defines the minimum difference allowed between real values */
    private static final double EPS = 1.0e-14;

    private double distributionIndex ;
    private double crossoverProbability  ;
    private double f;
    private RepairDoubleSolution solutionRepair ;

    private JMetalRandom randomGenerator ;

    /** Constructor */
    public HXCrossover(double crossoverProbability, double distributionIndex,double f) {
        this (crossoverProbability, distributionIndex, f, new RepairDoubleSolutionAtBounds()) ;
    }

    /** Constructor */
    public HXCrossover(double crossoverProbability, double distributionIndex, double f,RepairDoubleSolution solutionRepair) {
        if (crossoverProbability < 0) {
            throw new JMetalException("Crossover probability is negative: " + crossoverProbability) ;
        } else if (distributionIndex < 0) {
            throw new JMetalException("Distribution index is negative: " + distributionIndex);
        }

        this.crossoverProbability = crossoverProbability ;
        this.distributionIndex = distributionIndex ;
        this.f = f;
        this.solutionRepair = solutionRepair ;

        randomGenerator = JMetalRandom.getInstance() ;
    }

    /* Getters */
    public double getCrossoverProbability() {
        return crossoverProbability;
    }

    public double getDistributionIndex() {
        return distributionIndex;
    }

    public double getF(){return f;}

    /* Setters */
    public void setCrossoverProbability(double probability) {
        this.crossoverProbability = probability ;
    }

    public void setDistributionIndex(double distributionIndex) {
        this.distributionIndex = distributionIndex ;
    }

    public void setF(double f){this.f = f;}
    /** Execute() method */
    @Override
    public List<DoubleSolution> execute(List<DoubleSolution> solutions) {
        if (null == solutions) {
            throw new JMetalException("Null parameter") ;
        } else if (solutions.size() != 3) {
            throw new JMetalException("There must be two parents instead of " + solutions.size()) ;
        }

        return doCrossover(crossoverProbability, solutions) ;
    }

    /** doCrossover method */
    public List<DoubleSolution> doCrossover(
            double probability, List<DoubleSolution> parentSolutions) {

        DoubleSolution offspring = (DoubleSolution)parentSolutions.get(2).copy();

        DoubleSolution currentParent = parentSolutions.get(2);

        int i;
        double rand;
        double y1, y2, lowerBound, upperBound;
        double c1, c2, v;
        double alpha, beta, betaq;
        double valueX1;
        double valueX2;

        if (randomGenerator.nextDouble() <= probability) {
            for (i = 0; i < currentParent.getNumberOfVariables(); i++) {
                if (randomGenerator.nextDouble() < 0.5 ) {

                    valueX1 = currentParent.getVariableValue(i);
                    valueX2 = parentSolutions.get(randomGenerator.nextInt(0,1)).getVariableValue(i);
                    lowerBound = currentParent.getLowerBound(i);
                    upperBound = currentParent.getUpperBound(i);
                    if (Math.abs(valueX1 - valueX2) > EPS) {

                        if (valueX1 < valueX2) {
                            y1 = valueX1;
                            y2 = valueX2;
                        } else {
                            y1 = valueX2;
                            y2 = valueX1;
                        }

                        rand = randomGenerator.nextDouble();
                        beta = 1.0 + (2.0 * (y1 - lowerBound) / (y2 - y1));
                        alpha = 2.0 - Math.pow(beta, -(distributionIndex + 1.0));

                        if (rand <= (1.0 / alpha)) {
                            betaq = Math.pow(rand * alpha, (1.0 / (distributionIndex + 1.0)));
                        } else {
                            betaq = Math
                                    .pow(1.0 / (2.0 - rand * alpha), 1.0 / (distributionIndex + 1.0));
                        }
                        c1 = 0.5 * (y1 + y2 - betaq * (y2 - y1));

                        beta = 1.0 + (2.0 * (upperBound - y2) / (y2 - y1));
                        alpha = 2.0 - Math.pow(beta, -(distributionIndex + 1.0));

                        if (rand <= (1.0 / alpha)) {
                            betaq = Math.pow((rand * alpha), (1.0 / (distributionIndex + 1.0)));
                        } else {
                            betaq = Math
                                    .pow(1.0 / (2.0 - rand * alpha), 1.0 / (distributionIndex + 1.0));
                        }
                        c2 = 0.5 * (y1 + y2 + betaq * (y2 - y1));

                        c1 = solutionRepair.repairSolutionVariableValue(c1, lowerBound, upperBound);
                        c2 = solutionRepair.repairSolutionVariableValue(c2, lowerBound, upperBound);
                    } else {
                        c1 = valueX1;
                        c2 = valueX2;
                    }

                    v = parentSolutions.get(2).getVariableValue(i) + f * (parentSolutions.get(0).getVariableValue(
                            i) -
                            parentSolutions.get(1).getVariableValue(1));
                    v = solutionRepair.repairSolutionVariableValue(v, lowerBound, upperBound);

                    if(randomGenerator.nextDouble() < 0.5){
                        if(randomGenerator.nextDouble()<0.5)
                            offspring.setVariableValue(i,c1);
                        else
                            offspring.setVariableValue(i,c2);
                    }else{
                        offspring.setVariableValue(i,v);
                    }
                }else{
                    offspring.setVariableValue(i,parentSolutions.get(randomGenerator.nextInt(0,1)).getVariableValue(i));
                }
            }

        }

        List<DoubleSolution> result = new ArrayList<>(1);
        result.add(offspring);
        return result;
    }

    /**
     * Two parents are required to apply this operator.
     * @return
     */
    public int getNumberOfParents() {
        return 3 ;
    }
}
