CD_COUNT = 275*5;
D_COUNT =275;

MOEACD_filepath = '../log/FR C1_DTLZ1_10D/MOEACD_FR.txt';
[cdfc] = textread(MOEACD_filepath,'%d');

MOEAD_filepath = '../log/FR C1_DTLZ1_10D/MOEAD_FR.txt';
[dfc] = textread(MOEAD_filepath,'%d');

cdfc = cdfc/CD_COUNT;
dfc = dfc/D_COUNT;

h=figure;
plot(cdfc,'b-');
hold on;
plot(dfc,'r-');

xlabel('Generation');
ylabel('Fessible Ratio');
le = legend('C-MOEA/CD', 'C-MOEA/D');
title('C1DTLZ1 10D Fessible Ratio');

set(gca,'YLim',[0 1.5]);