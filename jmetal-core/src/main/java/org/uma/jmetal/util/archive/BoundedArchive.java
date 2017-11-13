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
package org.uma.jmetal.util.archive;

import org.uma.jmetal.solution.Solution;

import java.util.Comparator;

/**
 * Interface representing a bounded archive of solutions
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public interface BoundedArchive<S extends Solution<?>> extends Archive<S> {
  public int getMaxSize() ;
  public Comparator<S> getComparator() ;
  public void computeDensityEstimator() ;
}
