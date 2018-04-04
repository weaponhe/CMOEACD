algorithmList = {
     'C-MOEACD';
      'C-NSGAIII';
%     'C-MOEADD';
%     'C-MOEAD';
    };
instance = 'CarSideImpact';
for run=0:0
    for j=1:length(algorithmList)
        algorithm = algorithmList{j};
        figure;
        pfFile = sprintf('../../jmetal-data/MOEACDStudy/data/%s/%s/FUN%d.tsv', algorithm, instance,run);
        % [f1,f2] = textread(pfFile,'%f %f');
        [f1,f2,f3] = textread(pfFile,'%f %f %f');
        scatter3(f1,f2,f3,'r^');
        title(algorithm);
        %plot(f1,f2,'r^');
    end
end