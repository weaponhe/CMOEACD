filepath = '../jmetal-data/MOEACDStudy/data/MOEACDa/C3_DTLZ4/FUN1.tsv';
[f1,f2,f3] = textread(filepath,'%f %f %f');
scatter3(f1,f2,f3,'ro');