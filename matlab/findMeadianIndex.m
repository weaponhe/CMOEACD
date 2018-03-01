function [ medianIndex ] = findMeadianIndex( array )
%FINDMEADIANINDEX Summary of this function goes here
%   Detailed explanation goes here
medianValue = median(array);
index = 1;
minDis = abs(array(1)-medianValue);
for i=1:length(array)
    dis = abs(array(i)-medianValue);
    if(dis<minDis)
        index=i;
        minDis=dis;
    end
end
medianIndex = index;
end

