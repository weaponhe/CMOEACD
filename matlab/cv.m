MOEACD_filepath = '../log/CV C1_DTLZ1_10D/MOEACD.txt';
[cd] = textread(MOEACD_filepath,'%f');

MOEAD_filepath = '../log/CV C1_DTLZ1_10D/MOEAD.txt';
[d] = textread(MOEAD_filepath,'%f');

cd = abs(cd);
d = abs(d);

h=figure;
plot(cd,'b-');
hold on;
plot(d,'r-');

xlabel('Generation');
ylabel('Mean CV');
le = legend('C-MOEA/CD', 'C-MOEA/D');
title('C1DTLZ1 10D Mean CV');

% set(gca,'YLim',[0 1.5]);