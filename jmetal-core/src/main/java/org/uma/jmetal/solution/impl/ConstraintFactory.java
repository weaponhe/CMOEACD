package org.uma.jmetal.solution.impl;

/**
 * Created by X250 on 2016/12/26.
 */
import org.uma.jmetal.solution.SolutionEvaluator.Constraint;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

/**
 * This factory provides facilities to generate {@link Constraint}s from usual
 * situations.
 *
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 *
 */
public class ConstraintFactory {

    /**
     * This method retrieves all the values accessible through a getter (
     * <code>getX()</code> method) in order to build the corresponding set of
     * {@link Constraint}s. Notice that {@link Constraint}s are supposed to
     * represent evaluations of a {@link Solution}, so if the {@link Solution}
     * has other kinds of information accessible through getters, they will also
     * be retrieved as {@link Constraint}s. In such a case, you should filter the
     * returned {@link Constraint}s, rely on more advanced methods, or generate
     * the {@link Constraint}s manually.
     *
     * @param solutionClass
     *            the {@link Solution} class to analyze
     * @return the set of {@link Constraint}s retrieved from this class
     */
    public <Solution> Collection<Constraint<Solution, ?>> createFromGetters(
            Class<Solution> solutionClass) {
        Collection<Constraint<Solution, ?>> constraints = new LinkedList<>();
        for (Method method : solutionClass.getMethods()) {
            if (method.getParameterTypes().length == 0
                    && method.getReturnType() != null
                    && !method.getName().equals("getClass")
                    && method.getName().matches("get[^a-z].*")) {
                String name = method.getName().substring(3);
                constraints.add(createConstraintOn(solutionClass, method, name,
                        method.getReturnType()));
            } else {
                // not a getter, ignore it
            }
        }
        return constraints;
    }

    /**
     * This method retrieves all the values accessible through a getter (
     * <code>getX()</code> method) in order to build the corresponding set of
     * {@link Constraint}s. At the opposite of {@link #createFromGetters(Class)},
     * an additional filter is used: we build an {@link Constraint} for each
     * getter which does not correspond to a setter (<code>setX()</code> method
     * with the same <code>X</code> than the getter). This method is adapted for
     * {@link Solution} implementations which provide setters only for their
     * fundamental values (e.g. the path of a TSP {@link Solution}) and use
     * getters only for the computed values (e.g. the length of such a path).<br/>
     * <br/>
     * Notice that, if all the relevant getters are not present, the
     * corresponding {@link Constraint}s will not be retrieved. On the opposite,
     * any additional getter which does not correspond to a relevant
     * {@link Constraint} will be mistakenly retrieved. So be sure that the
     * relevant elements (and only these ones) have their getter (and no
     * setter). Otherwise, you should use a different method or generate the
     * {@link Constraint}s manually.
     *
     * @param solutionClass
     *            the {@link Solution} class to analyze
     * @return the set of {@link Constraint}s retrieved from this class
     */
    public <Solution> Collection<Constraint<Solution, ?>> createFromGettersWithoutSetters(
            Class<Solution> solutionClass) {
        Map<String, Method> getters = new HashMap<>();
        Map<String, Method> setters = new HashMap<>();
        for (Method method : solutionClass.getMethods()) {
            if (isGetter(method)) {
                String name = method.getName().substring(3);
                getters.put(name, method);
            } else if (isSetter(method)) {
                String name = method.getName().substring(3);
                setters.put(name, method);
            } else {
                // not a getter/setter, ignore it
            }
        }

        getters.keySet().removeAll(setters.keySet());

        Collection<Constraint<Solution, ?>> constraints = new LinkedList<>();
        for (Entry<String, Method> entry : getters.entrySet()) {
            String name = entry.getKey();
            Method getter = entry.getValue();
            constraints.add(createConstraintOn(solutionClass, getter, name,
                    getter.getReturnType()));
        }
        return constraints;
    }

    private boolean isSetter(Method method) {
        return method.getParameterTypes().length == 1
                && method.getReturnType() == void.class
                && method.getName().matches("set[^a-z].*");
    }

    private boolean isGetter(Method method) {
        return method.getParameterTypes().length == 0
                && method.getReturnType() != void.class
                && !method.getName().equals("getClass")
                && method.getName().matches("get[^a-z].*");
    }

    private <Solution, Value> Constraint<Solution, Value> createConstraintOn(
            final Class<Solution> solutionClass, final Method getter,
            final String name, final Class<Value> type) {
        return new Constraint<Solution, Value>() {

            @SuppressWarnings("unchecked")
            @Override
            public Value get(Solution solution) {
                try {
                    return (Value) getter.invoke(solution);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public String getName() {
                return name;
            }

            @Override
            public String getDescription() {
                return type.getSimpleName() + " value for the "
                        + solutionClass.getSimpleName() + " solutions.";
            }
        };
    }
}
