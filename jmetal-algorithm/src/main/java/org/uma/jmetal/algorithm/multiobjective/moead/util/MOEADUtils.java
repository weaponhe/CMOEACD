//  Utils.java
//
//  Author:
//       Antonio J. Nebro <antonio@lcc.uma.es>
//       Juan J. Durillo <durillo@lcc.uma.es>
//
//  Copyright (c) 2011 Antonio J. Nebro, Juan J. Durillo
//
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

package org.uma.jmetal.algorithm.multiobjective.moead.util;

import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

/**
 * Utilities methods to used by MOEA/D
 */
public class MOEADUtils {
	
	/**
	 * Quick sort procedure (ascending order)
	 * 
	 * @param array
	 * @param idx
	 * @param from
	 * @param to
	 */
	public static void quickSort(double[] array, int[] idx, int from, int to) {
		if (from < to) {
			double temp = array[to];
			int tempIdx = idx[to];
			int i = from - 1;
			for (int j = from; j < to; j++) {
				if (array[j] <= temp) {
					i++;
					double tempValue = array[j];
					array[j] = array[i];
					array[i] = tempValue;
					int tempIndex = idx[j];
					idx[j] = idx[i];
					idx[i] = tempIndex;
				}
			}
			array[to] = array[i + 1];
			array[i + 1] = temp;
			idx[to] = idx[i + 1];
			idx[i + 1] = tempIdx;
			quickSort(array, idx, from, i);
			quickSort(array, idx, i + 1, to);
		}
	}

  public static double distVector(double[] vector1, double[] vector2) {
    int dim = vector1.length;
    double sum = 0;
    for (int n = 0; n < dim; n++) {
      sum += (vector1[n] - vector2[n]) * (vector1[n] - vector2[n]);
    }
    return Math.sqrt(sum);
  }

  public static void minFastSort(double x[], int idx[], int n, int m) {
    for (int i = 0; i < m; i++) {
      for (int j = i + 1; j < n; j++) {
        if (x[i] > x[j]) {
          double temp = x[i];
          x[i] = x[j];
          x[j] = temp;
          int id = idx[i];
          idx[i] = idx[j];
          idx[j] = id;
        }
      }
    }
  }

  public static void randomPermutation(int[] perm, int size) {
    JMetalRandom randomGenerator = JMetalRandom.getInstance() ;
    int[] index = new int[size];
    boolean[] flag = new boolean[size];

    for (int n = 0; n < size; n++) {
      index[n] = n;
      flag[n] = true;
    }

    int num = 0;
    while (num < size) {
      int start = randomGenerator.nextInt(0, size - 1);
      while (true) {
        if (flag[start]) {
          perm[num] = index[start];
          flag[start] = false;
          num++;
          break;
        }
        if (start == (size - 1)) {
          start = 0;
        } else {
          start++;
        }
      }
    }
  }

    public static double dot(double[] _vec1,double[] _vec2){
        double sum = 0.0;
        for(int i=0;i<_vec1.length;++i)
            sum += (_vec1[i] * _vec2[i]);
        return sum;
    }

    public  static double norm(double[] _vec){
        double sum = 0.0;
        for(int i=0;i<_vec.length;++i)
            sum += (_vec[i] * _vec[i]);
        return Math.sqrt(sum);
    }

    public static double angle(double[] _vec1,double[] _vec2){
        double cosAngle = dot(_vec1,_vec2)/(norm(_vec1)*norm(_vec2));
        cosAngle = cosAngle < -1.0 ? -1.0:cosAngle;
        cosAngle = cosAngle > 1.0 ? 1.0:cosAngle;
        return Math.acos(cosAngle);
    }

    public static double maxAngle2Axis(double[] _vec){
        double maxAngle = Double.MIN_VALUE;
        double angle ;
        double cosAngle;
        for(int i=0;i<_vec.length;++i){
            cosAngle = _vec[i]/norm(_vec);
            cosAngle =  cosAngle < -1.0 ? -1.0 : cosAngle;
            cosAngle = cosAngle > 1.0 ? 1.0 : cosAngle;
            angle = Math.acos(cosAngle);

            if (maxAngle < angle)
                maxAngle = angle;
        }
        return maxAngle;
    }
    /**/
}
