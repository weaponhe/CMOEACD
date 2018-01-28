
% filepath = sprintf('../jmetal-data/MOEACDStudy/data/CMOEACD2Measure/%s/FUN0.tsv', ins);
% [f1,f2,f3] = textread(filepath,'%f %f %f');
% scatter3(f1,f2,f3,'yd');
% filepath = sprintf('../jmetal-data/MOEACDStudy/data/CMOEACD2Measure/%s/FUN1.tsv', ins);
% [f1,f2,f3] = textread(filepath,'%f %f %f');
% scatter3(f1,f2,f3,'g*');
% hold on;
% filepath = sprintf('../jmetal-data/MOEACDStudy/data/CMOEACD2Measure/%s/FUN2.tsv', ins);
% [f1,f2,f3] = textread(filepath,'%f %f %f');
% scatter3(f1,f2,f3,'k.');
% hold on;
% filepath = sprintf('../jmetal-data/MOEACDStudy/data/CMOEACD2Measure/%s/FUN3.tsv', ins);
% [f1,f2,f3] = textread(filepath,'%f %f %f');
% scatter3(f1,f2,f3,'c+');
% hold on;
ins = 'C1_DTLZ1_3D';
for i = 0:4
    figure;
    filepath = sprintf('../jmetal-data/MOEACDStudy/data/C-MOEACD(PBI)/%s/FUN%d.tsv', ins,i);
    [f1,f2,f3] = textread(filepath,'%f %f %f');
    scatter3(f1,f2,f3,'r^');
    hold on;
    filepath = '../jmetal-core/src/main/resources/pareto_fronts/DTLZ1.3D.pf[91]';
    [f1,f2,f3] = textread(filepath,'%f %f %f');
    scatter3(f1,f2,f3,'bo');
end




