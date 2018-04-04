% 
% % filepath = sprintf('../jmetal-data/MOEACDStudy/data/CMOEACD2Measure/%s/FUN0.tsv', ins);
% % [f1,f2,f3] = textread(filepath,'%f %f %f');
% % scatter3(f1,f2,f3,'yd');
% % filepath = sprintf('../jmetal-data/MOEACDStudy/data/CMOEACD2Measure/%s/FUN1.tsv', ins);
% % [f1,f2,f3] = textread(filepath,'%f %f %f');
% % scatter3(f1,f2,f3,'g*');
% % hold on;
% % filepath = sprintf('../jmetal-data/MOEACDStudy/data/CMOEACD2Measure/%s/FUN2.tsv', ins);
% % [f1,f2,f3] = textread(filepath,'%f %f %f');
% % scatter3(f1,f2,f3,'k.');
% % hold on;
% % filepath = sprintf('../jmetal-data/MOEACDStudy/data/CMOEACD2Measure/%s/FUN3.tsv', ins);
% % [f1,f2,f3] = textread(filepath,'%f %f %f');
% % scatter3(f1,f2,f3,'c+');
% % hold on;
% ins = 'C1_DTLZ1_3D';
% algorithmList = {
%      'C-MOEACD';
%      'C-NSGAIII';
%      'C-MOEADD';
%      'C-MOEAD';
%     'C-MOEACD-SR';
%     'C-MOEACD-CDP';
%     };
% instanceList = {
%     'C1_DTLZ1_15D';
%     'C1_DTLZ3_15D';
%     'C2_DTLZ2_15D';
%     'ConvexC2_DTLZ2_15D';
%     'C3_DTLZ1_15D';
%     'C3_DTLZ4_15D';
%     };
% for i = 0:0
%     figure;
%     filepath = sprintf('../../jmetal-data/MOEACDStudy/data/C-MOEADD/%s/FUN%d.tsv', ins,i);
%     %filepath = sprintf('../../jmetal-data/MOEACDStudy/data/C-NSGAIII/%s/FUN%d.tsv', ins,i);
%     [f1,f2,f3] = textread(filepath,'%f %f %f');
%     scatter3(f1,f2,f3,'r^');
% %      hold on;
% %     filepath = '../jmetal-core/src/main/resources/pareto_fronts/C2_Convex_DTLZ2.3D.pf[91]';
% %     [f1,f2,f3] = textread(filepath,'%f %f %f');
% %     scatter3(f1,f2,f3,'bo');
% % filepath = sprintf('../../jmetal-data/MOEACDStudy/data/CNSGAIII/%s/FUN%d.tsv', ins,i);
% %     %filepath = sprintf('../../jmetal-data/MOEACDStudy/data/C-NSGAIII/%s/FUN%d.tsv', ins,i);
% %     [f1,f2,f3] = textread(filepath,'%f %f %f');
% %     scatter3(f1,f2,f3,'bo');
% end



algorithmList = {
     'C-MOEACD';
%      'C-NSGAIII';
%      'C-MOEADD';
%      'C-MOEAD';
%     'C-MOEACD-SR';
%     'C-MOEACD-CDP';
    };
instanceList = {
%     'C1_DTLZ1';
    'C1_DTLZ3';
   % 'C2_DTLZ2';
%     'ConvexC2_DTLZ2';
    %'C3_DTLZ1';
    %'C3_DTLZ4';
    };
for j=1:length(algorithmList)
    algorithm = algorithmList{j};
    for i=1:length(instanceList)
        instance = instanceList{i};
        path = sprintf('../../jmetal-data/MOEACDStudy/data/%s/%s_3D/IGD', algorithm, instance);
        [IGD] = load(path);
        index = findMeadianIndex(IGD);
        figure;
        
        
        filepath = '../jmetal-core/src/main/resources/pareto_fronts/C2_Convex_DTLZ2.3D.pf[91]';
        filepath = sprintf('../jmetal-core/src/main/resources/pareto_fronts/%s.3D.pf[91]', instance);
        [f1,f2,f3] = textread(filepath,'%f %f %f');
        scatter3(f1,f2,f3,'bo');
        hold on;
        
        pfFile = sprintf('../../jmetal-data/MOEACDStudy/data/%s/%s_3D/FUN%d.tsv', algorithm, instance,index-1);
        saveFile = sprintf('./Figures/pf_%s_%s.eps',algorithm, instance);
        [f1,f2,f3] = textread(pfFile,'%f %f %f');
        scatter3(f1,f2,f3,'r^');
        set(gca, 'Fontname', 'Arial', 'Fontsize', 28,'FontWeight','bold');
        print('-depsc','-painters',saveFile);
        %clf
    end
end





