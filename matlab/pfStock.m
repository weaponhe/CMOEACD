instance='StockInvestment';
algorithm='C-MOEAD-CDP';
run=0;
colors=['g','m'];
set(0,'units','centimeters');
h=figure;
set(h,'PaperType','A4');
set(h,'PaperUnits','centimeters');
set(h,'paperpositionmode','auto');
set(h,'units','centimeters');
hold off;




filepath = sprintf('../../jmetal-data/MOEACDStudy/data/%s/%s/FUN%d.tsv', 'C-MOEAD-CDP', instance,run);
[f3,f4] = textread(filepath,'%f  %f');
plot(abs(f3),abs(f4),'ro');
%plot(f3,f4,'ro');

hold on;
filepath = sprintf('../../jmetal-data/MOEACDStudy/data/%s/%s/FUN%d.tsv', 'C-MOEAD-SR', instance,run);
[f1,f2] = textread(filepath,'%f  %f');
plot(abs(f1),abs(f2),'*');
%plot(f1,f2,'*');
legend('C-MOEAD-SR','C-MOEAD-CDP');

%title(algorithm);
xlabel('f1');
ylabel('f2');
%     saveas(gcf,picName);

% close all

