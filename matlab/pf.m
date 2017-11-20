figure(1)
filepath = '../jmetal-data/MOEACDStudy/data/MOEACD-3331/C2_DTLZ2/BEST_HV_FUN.tsv';
[f1,f2,f3] = textread(filepath,'%f %f %f');
scatter3(f1,f2,f3,'ro');

figure(2)
filepath = '../jmetal-data/MOEACDStudy/data/MOEACD-5311/C2_DTLZ2/BEST_HV_FUN.tsv';
[f1,f2,f3] = textread(filepath,'%f %f %f');
scatter3(f1,f2,f3,'bo');






