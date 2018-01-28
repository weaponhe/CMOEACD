instances = {'C1_DTLZ1';'C1_DTLZ3';'C2_DTLZ2';'ConvexC2_DTLZ2';'C3_DTLZ1';'C3_DTLZ4'};
objCounts = [3,5,8,10,15];
algrithms = {'C-NSGAIII';'C-MOEADD';'C-MOEACD(PBI)'};
for  k=1:length(instances)
    ins = instances{k};
    z=[];
    for  i=1:length(algrithms)
        alg = algrithms{i};
        temp = [];
        for obj=objCounts
            filepath = sprintf('../jmetal-data/MOEACDStudy/data/%s/%s_%dD/Time',alg,ins,obj);
            [v] = textread(filepath,'%f');
            t = min(v);
            temp=[temp,t];
        end
        z = [z;temp];
    end
    
    figure;
%     saveFile='../Figure/Time/DTLZ1.eps';
    bar3(z);
    set(gca,'FontSize',12,'FontName','Arial','FontWeight','Bold');
    title(sprintf('The running time for %s',ins));
    set(gca,'xticklabel',{ '3'  '5'  '8'  '10'  '15'});
    set(gca,'yticklabel',algrithms);
    zlabel('Time(ms)');
    % print('-depsc','-painters',saveFile);
end