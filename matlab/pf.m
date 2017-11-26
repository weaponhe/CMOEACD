figure(1)
filepath = '../jmetal-core/src/main/resources/pareto_fronts/C2_Convex_DTLZ2.3D.pf';
[f1,f2,f3] = textread(filepath,'%f %f %f');
scatter3(f1,f2,f3,'ro');





