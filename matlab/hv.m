filepath = sprintf('../FUN.tsv');
[f1,f2,f3] = textread(filepath,'%f %f %f');
scatter3(f1,f2,f3,'ro');