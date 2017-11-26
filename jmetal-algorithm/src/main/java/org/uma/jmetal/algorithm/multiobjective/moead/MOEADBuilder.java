//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU Lesser General Public License for more details.
//
//  You should have received a copy of the GNU Lesser General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.

package org.uma.jmetal.algorithm.multiobjective.moead;

import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.operator.impl.mutation.PolynomialMutation;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.AlgorithmBuilder;

/**
 * Builder class for algorithm MOEA/D and variants
 *
 * @author Antonio J. Nebro
 * @version 1.0
 */
public class MOEADBuilder implements AlgorithmBuilder<AbstractMOEAD<DoubleSolution>> {
    public enum Variant {
        MOEAD, MOEADMeasure, MOEADDE, MOEADDEMeasure, ConstraintMOEAD, MOEADDRA, MOEADDRAMeasure, MOEADSTM, MOEADSTMMeasure, MOEADCD, MOEADCDMeasure, MOEADACD, MOEADACDMeasure, MOEADD, MOEADDMeasure, MOEADACDM, MOEADACDMMeasure, MOEADN, MOEADNMeasure, MOEADACDN, MOEADACDNMeasure, MOEADDEN, MOEADDENMeasure, MOEADDN, MOEADDNMeasure,
        MOEADGR, MOEADGRMeasure, MOEADGRN, MOEADGRNMeasure,
        MOEADAGR, MOEADAGRMeasure, MOEADAGRN, MOEADAGRNMeasure,
        CMOEADD, CMOEADDMeasure, CMOEADDN, CMOEADDNMeasure,
        MOEADACDSBX, MOEADACDSBXMeasure,
        MOEADGRSBX, MOEADGRSBXMeasure,
        MOEADAGRSBX, MOEADAGRSBXMeasure,
        CMOEAD, CMOEADMeasure,
        CMOEADN, CMOEADNMeasure
    }

    ;

    protected Problem<DoubleSolution> problem;

    /**
     * T in Zhang & Li paper
     */
    protected int neighborSize;
    /**
     * Delta in Zhang & Li paper
     */
    protected double neighborhoodSelectionProbability;
    /**
     * nr in Zhang & Li paper
     */
    protected int maximumNumberOfReplacedSolutions;

    /*modified by Yuehong Xie 2016/03/22*/
    protected int kIndex;
    /**/
    protected boolean isFromFile;
    protected int[] numOfDivision;
    protected double[] integratedTau;

    protected MOEAD.FunctionType functionType;

    protected CrossoverOperator<DoubleSolution> crossover;
    protected MutationOperator<DoubleSolution> mutation;
    protected String dataDirectory;

    protected int populationSize;
    protected int resultPopulationSize;

    protected int maxEvaluations;
    protected int maxGen;

    protected int numberOfThreads;

    protected Variant moeadVariant;

    /**
     * Constructor
     */
    public MOEADBuilder(Problem<DoubleSolution> problem, Variant variant) {
        this.problem = problem;
        populationSize = 300;
        resultPopulationSize = 300;
        maxEvaluations = 150000;
        maxGen = 500;
        crossover = new DifferentialEvolutionCrossover();
        mutation = new PolynomialMutation(1.0 / problem.getNumberOfVariables(), 20.0);
        functionType = MOEAD.FunctionType.TCH;
        neighborhoodSelectionProbability = 0.1;
        maximumNumberOfReplacedSolutions = 2;
    /*modified by Yuehong Xie 2016/03/22 */
        kIndex = 9;
        isFromFile = false;
    /**/
        dataDirectory = "";
        neighborSize = 20;
        numberOfThreads = 1;
        moeadVariant = variant;
    }

  /* Getters/Setters */

    public int getMaxGen() {
        return maxGen;
    }

    public MOEADBuilder setMaxGen(int maxGen) {
        this.maxGen = maxGen;
        return this;
    }

    public int[] getNumOfDivision() {
        return numOfDivision;
    }

    public double[] getIntegratedTau() {
        return integratedTau;
    }

    public int getNeighborSize() {
        return neighborSize;
    }

    public int getMaxEvaluations() {
        return maxEvaluations;
    }

    public int getPopulationSize() {
        return populationSize;
    }

    public int getResultPopulationSize() {
        return resultPopulationSize;
    }

    public String getDataDirectory() {
        return dataDirectory;
    }

    public MutationOperator<DoubleSolution> getMutation() {
        return mutation;
    }

    public CrossoverOperator<DoubleSolution> getCrossover() {
        return crossover;
    }

    public MOEAD.FunctionType getFunctionType() {
        return functionType;
    }

    public int getMaximumNumberOfReplacedSolutions() {
        return maximumNumberOfReplacedSolutions;
    }

    /*modified by Yuehong Xie 2016/03/22*/
    public int getKIndex() {
        return kIndex;
    }
  /**/

    public double getNeighborhoodSelectionProbability() {
        return neighborhoodSelectionProbability;
    }

    public int getNumberOfThreads() {
        return numberOfThreads;
    }


    public MOEADBuilder setNumofDivision(int[] numofDivision) {
        this.numOfDivision = numofDivision;
        isFromFile = false;

        return this;
    }

    public MOEADBuilder setIntegratedTau(double[] integratedTau) {
        this.integratedTau = integratedTau;

        return this;
    }

    public MOEADBuilder setPopulationSize(int populationSize) {
        this.populationSize = populationSize;

        return this;
    }

    public MOEADBuilder setResultPopulationSize(int resultPopulationSize) {
        this.resultPopulationSize = resultPopulationSize;

        return this;
    }

    public MOEADBuilder setMaxEvaluations(int maxEvaluations) {
        this.maxEvaluations = maxEvaluations;

        return this;
    }

    public MOEADBuilder setNeighborSize(int neighborSize) {
        this.neighborSize = neighborSize;

        return this;
    }

    public MOEADBuilder setNeighborhoodSelectionProbability(double neighborhoodSelectionProbability) {
        this.neighborhoodSelectionProbability = neighborhoodSelectionProbability;

        return this;
    }

    public MOEADBuilder setFunctionType(MOEAD.FunctionType functionType) {
        this.functionType = functionType;

        return this;
    }

    public MOEADBuilder setMaximumNumberOfReplacedSolutions(int maximumNumberOfReplacedSolutions) {
        this.maximumNumberOfReplacedSolutions = maximumNumberOfReplacedSolutions;

        return this;
    }

    /*modified by Yuehong Xie 2016/03/22*/
    public MOEADBuilder setKIndex(int kIndex) {
        this.kIndex = kIndex;

        return this;
    }
  /**/

    public MOEADBuilder setCrossover(CrossoverOperator<DoubleSolution> crossover) {
        this.crossover = crossover;

        return this;
    }

    public MOEADBuilder setMutation(MutationOperator<DoubleSolution> mutation) {
        this.mutation = mutation;

        return this;
    }

    public MOEADBuilder setDataDirectory(String dataDirectory) {
        this.dataDirectory = dataDirectory;
        isFromFile = true;
        return this;
    }

    public MOEADBuilder setNumberOfThreads(int numberOfThreads) {
        this.numberOfThreads = numberOfThreads;

        return this;
    }

    public AbstractMOEAD<DoubleSolution> build() {
        AbstractMOEAD<DoubleSolution> algorithm = null;
        if (isFromFile) {
            if (moeadVariant.equals(Variant.MOEAD)) {
                algorithm = new MOEAD(problem, populationSize, resultPopulationSize, maxEvaluations, crossover, mutation,
                        functionType, dataDirectory, neighborSize);
            } else if (moeadVariant.equals(Variant.MOEADMeasure)) {
                algorithm = new MOEADMeasure(problem, populationSize, resultPopulationSize, maxEvaluations, crossover, mutation,
                        functionType, dataDirectory, neighborSize);
            } else if (moeadVariant.equals(Variant.MOEADDE)) {
                algorithm = new MOEADDE(problem, populationSize, resultPopulationSize, maxEvaluations, crossover, mutation,
                        functionType, dataDirectory, neighborhoodSelectionProbability,
                        maximumNumberOfReplacedSolutions, neighborSize);
            } else if (moeadVariant.equals(Variant.MOEADDEMeasure)) {
                algorithm = new MOEADDEMeasure(problem, populationSize, resultPopulationSize, maxEvaluations, crossover, mutation,
                        functionType, dataDirectory, neighborhoodSelectionProbability,
                        maximumNumberOfReplacedSolutions, neighborSize);
            } else if (moeadVariant.equals(Variant.ConstraintMOEAD)) {
                algorithm = new ConstraintMOEAD(problem, populationSize, resultPopulationSize, maxEvaluations, maxGen, mutation,
                        crossover, functionType, dataDirectory, neighborhoodSelectionProbability,
                        maximumNumberOfReplacedSolutions, neighborSize);
            } else if (moeadVariant.equals(Variant.MOEADDRA)) {
                algorithm = new MOEADDRA(problem, populationSize, resultPopulationSize, maxEvaluations, mutation,
                        crossover, functionType, dataDirectory, neighborhoodSelectionProbability,
                        maximumNumberOfReplacedSolutions, neighborSize);
            } else if (moeadVariant.equals(Variant.MOEADDRAMeasure)) {
                algorithm = new MOEADDRAMeasure(problem, populationSize, resultPopulationSize, maxEvaluations, mutation,
                        crossover, functionType, dataDirectory, neighborhoodSelectionProbability,
                        maximumNumberOfReplacedSolutions, neighborSize);
            } else if (moeadVariant.equals(Variant.MOEADSTM)) {
                algorithm = new MOEADSTM(problem, populationSize, resultPopulationSize, maxEvaluations, mutation,
                        crossover, functionType, dataDirectory, neighborhoodSelectionProbability,
                        maximumNumberOfReplacedSolutions, neighborSize);
            } else if (moeadVariant.equals(Variant.MOEADSTMMeasure)) {
                algorithm = new MOEADSTMMeasure(problem, populationSize, resultPopulationSize, maxEvaluations, mutation,
                        crossover, functionType, dataDirectory, neighborhoodSelectionProbability,
                        maximumNumberOfReplacedSolutions, neighborSize);
            } else if (moeadVariant.equals(Variant.MOEADCD)) {
                algorithm = new MOEADCD(problem, populationSize, resultPopulationSize, maxEvaluations, crossover, mutation,
                        functionType, dataDirectory, neighborhoodSelectionProbability,
                        maximumNumberOfReplacedSolutions, neighborSize, kIndex);
            } else if (moeadVariant.equals(Variant.MOEADCDMeasure)) {
                algorithm = new MOEADCDMeasure(problem, populationSize, resultPopulationSize, maxEvaluations, crossover, mutation,
                        functionType, dataDirectory, neighborhoodSelectionProbability,
                        maximumNumberOfReplacedSolutions, neighborSize, kIndex);
            } else if (moeadVariant.equals(Variant.MOEADACD)) {
                algorithm = new MOEADACD(problem, populationSize, resultPopulationSize, maxEvaluations, crossover, mutation,
                        functionType, dataDirectory, neighborhoodSelectionProbability,
                        maximumNumberOfReplacedSolutions, neighborSize);
            } else if (moeadVariant.equals(Variant.MOEADACDMeasure)) {
                algorithm = new MOEADACDMeasure(problem, populationSize, resultPopulationSize, maxEvaluations, crossover, mutation,
                        functionType, dataDirectory, neighborhoodSelectionProbability,
                        maximumNumberOfReplacedSolutions, neighborSize);
            } else if (moeadVariant.equals(Variant.MOEADD)) {
                algorithm = new MOEADD(problem, populationSize, resultPopulationSize, maxEvaluations, crossover, mutation,
                        functionType, dataDirectory, neighborhoodSelectionProbability,
                        maximumNumberOfReplacedSolutions, neighborSize);
            } else if (moeadVariant.equals(Variant.MOEADDMeasure)) {
                algorithm = new MOEADDMeasure(problem, populationSize, resultPopulationSize, maxEvaluations, crossover, mutation,
                        functionType, dataDirectory, neighborhoodSelectionProbability,
                        maximumNumberOfReplacedSolutions, neighborSize);
            } else if (moeadVariant.equals(Variant.CMOEAD)) {
                algorithm = new CMOEAD(problem, populationSize, resultPopulationSize, maxEvaluations, crossover, mutation,
                        functionType, dataDirectory, neighborSize);
            } else if (moeadVariant.equals(Variant.CMOEADMeasure)) {
                algorithm = new CMOEADMeasure(problem, populationSize, resultPopulationSize, maxEvaluations, crossover, mutation,
                        functionType, dataDirectory, neighborSize);
            } else if (moeadVariant.equals(Variant.CMOEADN)) {
                algorithm = new CMOEADN(problem, populationSize, resultPopulationSize, maxEvaluations, crossover, mutation,
                        functionType, dataDirectory, neighborSize);
            } else if (moeadVariant.equals(Variant.CMOEADNMeasure)) {
                algorithm = new CMOEADNMeasure(problem, populationSize, resultPopulationSize, maxEvaluations, crossover, mutation,
                        functionType, dataDirectory, neighborSize);
            }
        } else {

            if (moeadVariant.equals(Variant.MOEAD)) {
                algorithm = new MOEAD(problem, populationSize, resultPopulationSize, maxEvaluations, crossover, mutation,
                        functionType, numOfDivision, integratedTau, neighborSize);
            } else if (moeadVariant.equals(Variant.MOEADMeasure)) {
                algorithm = new MOEADMeasure(problem, populationSize, resultPopulationSize, maxEvaluations, crossover, mutation,
                        functionType, numOfDivision, integratedTau, neighborSize);
            } else if (moeadVariant.equals(Variant.MOEADDE)) {
                algorithm = new MOEADDE(problem, populationSize, resultPopulationSize, maxEvaluations, crossover, mutation,
                        functionType, numOfDivision, integratedTau, neighborhoodSelectionProbability,
                        maximumNumberOfReplacedSolutions, neighborSize);
            } else if (moeadVariant.equals(Variant.MOEADDEMeasure)) {
                algorithm = new MOEADDEMeasure(problem, populationSize, resultPopulationSize, maxEvaluations, crossover, mutation,
                        functionType, numOfDivision, integratedTau, neighborhoodSelectionProbability,
                        maximumNumberOfReplacedSolutions, neighborSize);
            } else if (moeadVariant.equals(Variant.ConstraintMOEAD)) {
                algorithm = new ConstraintMOEAD(problem, populationSize, resultPopulationSize, maxEvaluations, maxGen, mutation,
                        crossover, functionType, numOfDivision, integratedTau, neighborhoodSelectionProbability,
                        maximumNumberOfReplacedSolutions, neighborSize);
            } else if (moeadVariant.equals(Variant.MOEADDRA)) {
                algorithm = new MOEADDRA(problem, populationSize, resultPopulationSize, maxEvaluations, mutation,
                        crossover, functionType, numOfDivision, integratedTau, neighborhoodSelectionProbability,
                        maximumNumberOfReplacedSolutions, neighborSize);
            } else if (moeadVariant.equals(Variant.MOEADDRAMeasure)) {
                algorithm = new MOEADDRAMeasure(problem, populationSize, resultPopulationSize, maxEvaluations, mutation,
                        crossover, functionType, numOfDivision, integratedTau, neighborhoodSelectionProbability,
                        maximumNumberOfReplacedSolutions, neighborSize);
            } else if (moeadVariant.equals(Variant.MOEADSTM)) {
                algorithm = new MOEADSTM(problem, populationSize, resultPopulationSize, maxEvaluations, mutation,
                        crossover, functionType, numOfDivision, integratedTau, neighborhoodSelectionProbability,
                        maximumNumberOfReplacedSolutions, neighborSize);
            } else if (moeadVariant.equals(Variant.MOEADSTMMeasure)) {
                algorithm = new MOEADSTMMeasure(problem, populationSize, resultPopulationSize, maxEvaluations, mutation,
                        crossover, functionType, numOfDivision, integratedTau, neighborhoodSelectionProbability,
                        maximumNumberOfReplacedSolutions, neighborSize);
            } else if (moeadVariant.equals(Variant.MOEADCD)) {
                algorithm = new MOEADCD(problem, populationSize, resultPopulationSize, maxEvaluations, crossover, mutation,
                        functionType, numOfDivision, integratedTau, neighborhoodSelectionProbability,
                        maximumNumberOfReplacedSolutions, neighborSize, kIndex);
            } else if (moeadVariant.equals(Variant.MOEADCDMeasure)) {
                algorithm = new MOEADCDMeasure(problem, populationSize, resultPopulationSize, maxEvaluations, crossover, mutation,
                        functionType, numOfDivision, integratedTau, neighborhoodSelectionProbability,
                        maximumNumberOfReplacedSolutions, neighborSize, kIndex);
            } else if (moeadVariant.equals(Variant.MOEADACD)) {
                algorithm = new MOEADACD(problem, populationSize, resultPopulationSize, maxEvaluations, crossover, mutation,
                        functionType, numOfDivision, integratedTau, neighborhoodSelectionProbability,
                        maximumNumberOfReplacedSolutions, neighborSize);
            } else if (moeadVariant.equals(Variant.MOEADACDMeasure)) {
                algorithm = new MOEADACDMeasure(problem, populationSize, resultPopulationSize, maxEvaluations, crossover, mutation,
                        functionType, numOfDivision, integratedTau, neighborhoodSelectionProbability,
                        maximumNumberOfReplacedSolutions, neighborSize);
            } else if (moeadVariant.equals(Variant.MOEADD)) {
                algorithm = new MOEADD(problem, populationSize, resultPopulationSize, maxEvaluations, crossover, mutation,
                        functionType, numOfDivision, integratedTau, neighborhoodSelectionProbability,
                        maximumNumberOfReplacedSolutions, neighborSize);
            } else if (moeadVariant.equals(Variant.MOEADDMeasure)) {
                algorithm = new MOEADDMeasure(problem, populationSize, resultPopulationSize, maxEvaluations, crossover, mutation,
                        functionType, numOfDivision, integratedTau, neighborhoodSelectionProbability,
                        maximumNumberOfReplacedSolutions, neighborSize);
            } else if (moeadVariant.equals(Variant.MOEADN)) {
                algorithm = new MOEADN(problem, populationSize, resultPopulationSize, maxEvaluations, crossover, mutation,
                        functionType, numOfDivision, integratedTau, neighborSize);
            } else if (moeadVariant.equals(Variant.MOEADNMeasure)) {
                algorithm = new MOEADNMeasure(problem, populationSize, resultPopulationSize, maxEvaluations, crossover, mutation,
                        functionType, numOfDivision, integratedTau, neighborSize);
            } else if (moeadVariant.equals(Variant.MOEADACDN)) {
                algorithm = new MOEADACDN(problem, populationSize, resultPopulationSize, maxEvaluations, crossover, mutation,
                        functionType, numOfDivision, integratedTau, neighborhoodSelectionProbability,
                        maximumNumberOfReplacedSolutions, neighborSize);
            } else if (moeadVariant.equals(Variant.MOEADACDNMeasure)) {
                algorithm = new MOEADACDNMeasure(problem, populationSize, resultPopulationSize, maxEvaluations, crossover, mutation,
                        functionType, numOfDivision, integratedTau, neighborhoodSelectionProbability,
                        maximumNumberOfReplacedSolutions, neighborSize);
            } else if (moeadVariant.equals(Variant.MOEADDEN)) {
                algorithm = new MOEADDEN(problem, populationSize, resultPopulationSize, maxEvaluations, crossover, mutation,
                        functionType, numOfDivision, integratedTau, neighborhoodSelectionProbability,
                        maximumNumberOfReplacedSolutions, neighborSize);
            } else if (moeadVariant.equals(Variant.MOEADDENMeasure)) {
                algorithm = new MOEADDENMeasure(problem, populationSize, resultPopulationSize, maxEvaluations, crossover, mutation,
                        functionType, numOfDivision, integratedTau, neighborhoodSelectionProbability,
                        maximumNumberOfReplacedSolutions, neighborSize);
            } else if (moeadVariant.equals(Variant.MOEADDN)) {
                algorithm = new MOEADDN(problem, populationSize, resultPopulationSize, maxEvaluations, crossover, mutation,
                        functionType, numOfDivision, integratedTau, neighborhoodSelectionProbability,
                        maximumNumberOfReplacedSolutions, neighborSize);
            } else if (moeadVariant.equals(Variant.MOEADDNMeasure)) {
                algorithm = new MOEADDNMeasure(problem, populationSize, resultPopulationSize, maxEvaluations, crossover, mutation,
                        functionType, numOfDivision, integratedTau, neighborhoodSelectionProbability,
                        maximumNumberOfReplacedSolutions, neighborSize);
            } else if (moeadVariant.equals(Variant.MOEADGR)) {
                algorithm = new MOEADGR(problem, populationSize, resultPopulationSize, maxEvaluations, crossover, mutation,
                        functionType, numOfDivision, integratedTau, neighborhoodSelectionProbability,
                        maximumNumberOfReplacedSolutions, neighborSize);
            } else if (moeadVariant.equals(Variant.MOEADGRMeasure)) {
                algorithm = new MOEADGRMeasure(problem, populationSize, resultPopulationSize, maxEvaluations, crossover, mutation,
                        functionType, numOfDivision, integratedTau, neighborhoodSelectionProbability,
                        maximumNumberOfReplacedSolutions, neighborSize);
            } else if (moeadVariant.equals(Variant.MOEADGRN)) {
                algorithm = new MOEADGRN(problem, populationSize, resultPopulationSize, maxEvaluations, crossover, mutation,
                        functionType, numOfDivision, integratedTau, neighborhoodSelectionProbability,
                        maximumNumberOfReplacedSolutions, neighborSize);
            } else if (moeadVariant.equals(Variant.MOEADGRNMeasure)) {
                algorithm = new MOEADGRNMeasure(problem, populationSize, resultPopulationSize, maxEvaluations, crossover, mutation,
                        functionType, numOfDivision, integratedTau, neighborhoodSelectionProbability,
                        maximumNumberOfReplacedSolutions, neighborSize);
            } else if (moeadVariant.equals(Variant.MOEADAGR)) {
                algorithm = new MOEADAGR(problem, populationSize, resultPopulationSize, maxEvaluations, crossover, mutation,
                        functionType, numOfDivision, integratedTau, neighborhoodSelectionProbability,
                        maximumNumberOfReplacedSolutions, neighborSize);
            } else if (moeadVariant.equals(Variant.MOEADAGRMeasure)) {
                algorithm = new MOEADAGRMeasure(problem, populationSize, resultPopulationSize, maxEvaluations, crossover, mutation,
                        functionType, numOfDivision, integratedTau, neighborhoodSelectionProbability,
                        maximumNumberOfReplacedSolutions, neighborSize);
            } else if (moeadVariant.equals(Variant.MOEADAGRN)) {
                algorithm = new MOEADAGRN(problem, populationSize, resultPopulationSize, maxEvaluations, crossover, mutation,
                        functionType, numOfDivision, integratedTau, neighborhoodSelectionProbability,
                        maximumNumberOfReplacedSolutions, neighborSize);
            } else if (moeadVariant.equals(Variant.MOEADAGRNMeasure)) {
                algorithm = new MOEADAGRNMeasure(problem, populationSize, resultPopulationSize, maxEvaluations, crossover, mutation,
                        functionType, numOfDivision, integratedTau, neighborhoodSelectionProbability,
                        maximumNumberOfReplacedSolutions, neighborSize);
            } else if (moeadVariant.equals(Variant.CMOEADD)) {
                algorithm = new CMOEADD(problem, populationSize, resultPopulationSize, maxEvaluations, crossover, mutation,
                        functionType, numOfDivision, integratedTau, neighborhoodSelectionProbability,
                        maximumNumberOfReplacedSolutions, neighborSize);
            } else if (moeadVariant.equals(Variant.CMOEADDMeasure)) {
                algorithm = new CMOEADDMeasure(problem, populationSize, resultPopulationSize, maxEvaluations, crossover, mutation,
                        functionType, numOfDivision, integratedTau, neighborhoodSelectionProbability,
                        maximumNumberOfReplacedSolutions, neighborSize);
            } else if (moeadVariant.equals(Variant.CMOEADDN)) {
                algorithm = new CMOEADDN(problem, populationSize, resultPopulationSize, maxEvaluations, crossover, mutation,
                        functionType, numOfDivision, integratedTau, neighborhoodSelectionProbability,
                        maximumNumberOfReplacedSolutions, neighborSize);
            } else if (moeadVariant.equals(Variant.CMOEADDNMeasure)) {
                algorithm = new CMOEADDNMeasure(problem, populationSize, resultPopulationSize, maxEvaluations, crossover, mutation,
                        functionType, numOfDivision, integratedTau, neighborhoodSelectionProbability,
                        maximumNumberOfReplacedSolutions, neighborSize);
            } else if (moeadVariant.equals(Variant.MOEADACDSBX)) {
                algorithm = new MOEADACDSBX(problem, populationSize, resultPopulationSize, maxEvaluations, crossover, mutation,
                        functionType, numOfDivision, integratedTau, neighborhoodSelectionProbability,
                        maximumNumberOfReplacedSolutions, neighborSize);
            } else if (moeadVariant.equals(Variant.MOEADACDSBXMeasure)) {
                algorithm = new MOEADACDSBXMeasure(problem, populationSize, resultPopulationSize, maxEvaluations, crossover, mutation,
                        functionType, numOfDivision, integratedTau, neighborhoodSelectionProbability,
                        maximumNumberOfReplacedSolutions, neighborSize);
            } else if (moeadVariant.equals(Variant.MOEADGRSBX)) {
                algorithm = new MOEADGRSBX(problem, populationSize, resultPopulationSize, maxEvaluations, crossover, mutation,
                        functionType, numOfDivision, integratedTau, neighborhoodSelectionProbability,
                        maximumNumberOfReplacedSolutions, neighborSize);
            } else if (moeadVariant.equals(Variant.MOEADGRSBXMeasure)) {
                algorithm = new MOEADGRSBXMeasure(problem, populationSize, resultPopulationSize, maxEvaluations, crossover, mutation,
                        functionType, numOfDivision, integratedTau, neighborhoodSelectionProbability,
                        maximumNumberOfReplacedSolutions, neighborSize);
            } else if (moeadVariant.equals(Variant.MOEADAGRSBX)) {
                algorithm = new MOEADAGRSBX(problem, populationSize, resultPopulationSize, maxEvaluations, crossover, mutation,
                        functionType, numOfDivision, integratedTau, neighborhoodSelectionProbability,
                        maximumNumberOfReplacedSolutions, neighborSize);
            } else if (moeadVariant.equals(Variant.MOEADAGRSBXMeasure)) {
                algorithm = new MOEADAGRSBXMeasure(problem, populationSize, resultPopulationSize, maxEvaluations, crossover, mutation,
                        functionType, numOfDivision, integratedTau, neighborhoodSelectionProbability,
                        maximumNumberOfReplacedSolutions, neighborSize);
            } else if (moeadVariant.equals(Variant.CMOEAD)) {
                algorithm = new CMOEAD(problem, populationSize, resultPopulationSize, maxEvaluations, crossover, mutation,
                        functionType, numOfDivision, integratedTau, neighborSize);
            } else if (moeadVariant.equals(Variant.CMOEADMeasure)) {
                algorithm = new CMOEADMeasure(problem, populationSize, resultPopulationSize, maxEvaluations, crossover, mutation,
                        functionType, numOfDivision, integratedTau, neighborSize);
            } else if (moeadVariant.equals(Variant.CMOEADN)) {
                algorithm = new CMOEADN(problem, populationSize, resultPopulationSize, maxEvaluations, crossover, mutation,
                        functionType, numOfDivision, integratedTau, neighborSize);
            } else if (moeadVariant.equals(Variant.CMOEADNMeasure)) {
                algorithm = new CMOEADNMeasure(problem, populationSize, resultPopulationSize, maxEvaluations, crossover, mutation,
                        functionType, numOfDivision, integratedTau, neighborSize);
            }
        }

        return algorithm;
    }
}
