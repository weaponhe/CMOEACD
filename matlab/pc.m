
algorithmList = {
     'C-MOEACD';
     'C-NSGAIII';
     'C-MOEADD';
     'C-MOEAD-ACV';
    'C-MOEAD-SR';
    'C-MOEAD-CDP';
    };
instanceList = {
    'C1_DTLZ1_15D';
    'C1_DTLZ3_15D';
    'C2_DTLZ2_15D';
    'ConvexC2_DTLZ2_15D';
    'C3_DTLZ1_15D';
    'C3_DTLZ4_15D';
    };
for j=1:length(algorithmList)
    algorithm = algorithmList{j};
    for i=1:length(instanceList)
        instance = instanceList{i};
        path = sprintf('../../jmetal-data/MOEACDStudy/data/%s/%s/IGD', algorithm, instance);
        [IGD] = load(path);
        index = findMeadianIndex(IGD);
        figure;
        pfFile = sprintf('../../jmetal-data/MOEACDStudy/data/%s/%s/FUN%d.tsv', algorithm, instance,index-1);
        saveFile = sprintf('./Figures/pc_%s_%s.eps',algorithm, instance);
        [F] = load(pfFile);
        [m,n] = size(F);
        for k=1:m
            V = F(k,:);
            X = 1:1:n;
            plot(X,V);hold on;
        end
        set(gca, 'Fontname', 'Arial', 'Fontsize', 28,'FontWeight','bold');
        set(findobj(get(gca,'Children'),'LineWidth',0.5),'LineWidth',1.5);
        %title(instance)
        xlim([1 n]);
        xlabel('Objective No.')
        ylabel('Objective Value')
        print('-depsc','-painters',saveFile);
%         saveas(gca, saveFile);
        %clf
    end
end
close all;
