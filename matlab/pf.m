figure;
filepath = '../jmetal-core/src/main/resources/pareto_fronts/C3_DTLZ1.2D.pf';
[f1,f2] = textread(filepath,'%f %f');
scatter(f1,f2,'ro');
figure;
filepath = '../jmetal-core/src/main/resources/pareto_fronts/C3_DTLZ1.3D.pf';
[f1,f2,f3] = textread(filepath,'%f %f %f');
scatter3(f1,f2,f3,'ro');



