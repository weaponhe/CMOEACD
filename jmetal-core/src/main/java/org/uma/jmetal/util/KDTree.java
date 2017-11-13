package org.uma.jmetal.util;

import org.uma.jmetal.solution.DoubleSolution;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.Vector;

/**
 * Created by X250 on 2016/6/13.
 */
public class KDTree {
    private Node kdtree;

    private class Node {
        //分割的维度
        int partitionDimention;
        //分割的值
        double partitionValue;
        //如果为非叶子节点，该属性为空
        //否则为数据
        double[] value;
        int index;
        //是否为叶子
        boolean isLeaf = false;
        //左树
        Node left;
        //右树
        Node right;
        //每个维度的最小值
        double[] min;
        //每个维度的最大值
        double[] max;

        Node() {
        }
    }

    private static class UtilZ {
        /**
         * 计算给定维度的方差
         *
         * @param dataNodes 数据
         * @param dimention 维度
         * @return 方差
         */
        static double variance(ArrayList<Node> dataNodes, int dimention) {
            double vsum = 0;
            double sum = 0;
            for (Node node : dataNodes) {
                sum += node.value[dimention];
                vsum += node.value[dimention] * node.value[dimention];
            }
            int nPoint = dataNodes.size();
            return vsum / nPoint - Math.pow(sum / nPoint, 2);
        }

        /**
         * 取排序后的中间位置数值
         *
         * @param dataNodes 数据
         * @param dimention 维度
         * @return
         */
        static double median(ArrayList<Node> dataNodes, int dimention) {
            double[] d = new double[dataNodes.size()];
            int i = 0;
            for (Node node : dataNodes) {
                d[i++] = node.value[dimention];
            }
            return findPos(d, 0, d.length - 1, d.length / 2);
        }

        static double[][] maxmin(ArrayList<Node> dataNodes, int dimentions) {
            double[][] mm = new double[2][dimentions];
            //初始化 第一行为min，第二行为max
            for (int i = 0; i < dimentions; i++) {
                mm[0][i] = mm[1][i] = dataNodes.get(0).value[i];
                for (int j = 1; j < dataNodes.size(); j++) {
                    double[] d = dataNodes.get(j).value;
                    if (d[i] < mm[0][i]) {
                        mm[0][i] = d[i];
                    } else if (d[i] > mm[1][i]) {
                        mm[1][i] = d[i];
                    }
                }
            }
            return mm;
        }

        static double distance(double[] a, double[] b) {
            double sum = 0;
            for (int i = 0; i < a.length; i++) {
                sum += Math.pow(a[i] - b[i], 2);
            }
            return sum;
        }

        /**
         * 在max和min表示的超矩形中的点和点a的最小距离
         *
         * @param a   点a
         * @param max 超矩形各个维度的最大值
         * @param min 超矩形各个维度的最小值
         * @return 超矩形中的点和点a的最小距离
         */
        static double mindistance(double[] a, double[] max, double[] min) {
            double sum = 0;
            for (int i = 0; i < a.length; i++) {
                if (a[i] > max[i])
                    sum += Math.pow(a[i] - max[i], 2);
                else if (a[i] < min[i]) {
                    sum += Math.pow(min[i] - a[i], 2);
                }
            }

            return sum;
        }

        /**
         * 使用快速排序，查找排序后位置在point处的值
         * 比Array.sort()后去对应位置值，大约快30%
         *
         * @param data  数据
         * @param low   参加排序的最低点
         * @param high  参加排序的最高点
         * @param point 位置
         * @return
         */
        private static double findPos(double[] data, int low, int high, int point) {
            int lowt = low;
            int hight = high;
            double v = data[low];
            ArrayList<Integer> same = new ArrayList<Integer>((int) ((high - low) * 0.25));
            while (low < high) {
                while (low < high && data[high] >= v) {
                    if (Math.abs(data[high] - v) < Constant.TOLERATION) {
                        same.add(high);
                    }
                    high--;
                }
                data[low] = data[high];
                while (low < high && data[low] < v)
                    low++;
                data[high] = data[low];
            }
            data[low] = v;
            int upper = low + same.size();
            if (low <= point && upper >= point) {
                return v;
            }

            if (low > point) {
                return findPos(data, lowt, low - 1, point);
            }

            int i = low + 1;
            for (int j : same) {
                if (j <= low + same.size())
                    continue;
                while (Math.abs(data[i] - v) < Constant.TOLERATION)
                    i++;
                data[j] = data[i];
                data[i] = v;
                i++;
            }

            return findPos(data, low + same.size() + 1, hight, point);
        }
    }

    private KDTree() {
    }

    /**
     * 构建树
     *
     * @param input 输入
     * @return KDTree树
     */
    public static KDTree build(List<double[]> input) {
        int nPoint = input.size();
        int nObj = input.get(0).length;

        KDTree tree = new KDTree();

        ArrayList<Node> dataNodes = new ArrayList<>(nPoint);
        for (int i = 0; i < nPoint; i++) {
            Node node = tree.new Node();
            node.value = input.get(i);
            node.index = i;
            dataNodes.add(node);
        }
        tree.kdtree = tree.buildDetail(dataNodes, nObj);

        return tree;
    }

    public static KDTree build(double[][] input) {
        int nPoint = input.length;
        int nObj = input[0].length;

        KDTree tree = new KDTree();

        ArrayList<Node> dataNodes = new ArrayList<>(nPoint);
        for (int i = 0; i < nPoint; i++) {
            Node node = tree.new Node();
            node.value = input[i];
            node.index = i;
            dataNodes.add(node);
        }
        tree.kdtree = tree.buildDetail(dataNodes, nObj);

        return tree;
    }

    /**
     * 循环构建树
     *
     * @param dataNodes  数据
     * @param dimentions 数据的维度
     */
    private Node buildDetail(ArrayList<Node> dataNodes, int dimentions) {
        if (dataNodes.size() == 1) {
            Node node = dataNodes.get(0);
            node.isLeaf = true;
            return node;
        }

        Node node = new Node();
        //选择方差最大的维度
        node.partitionDimention = -1;
        double var = -1;
        double tmpvar;
        for (int i = 0; i < dimentions; i++) {
            tmpvar = UtilZ.variance(dataNodes, i);
            if (tmpvar > var) {
                var = tmpvar;
                node.partitionDimention = i;
            }
        }
        //如果方差=0，表示所有数据都相同，判定为叶子节点
        if (Math.abs(var) <= Constant.TOLERATION) {
//                JMetalLogger.logger.info("\nSize : "+dataNodes.size()+"\n");
            Node leaf = dataNodes.get(0);
            leaf.isLeaf = true;
            return leaf;
        }

        //选择分割的值
        node.partitionValue = UtilZ.median(dataNodes, node.partitionDimention);

        double[][] maxmin = UtilZ.maxmin(dataNodes, dimentions);
        node.min = maxmin[0];
        node.max = maxmin[1];

        int size = (int) (dataNodes.size() * 0.55);
        ArrayList<Node> left = new ArrayList<>(size);
        ArrayList<Node> right = new ArrayList<>(size);
        ArrayList<Node> tmpEqual = new ArrayList<>(size);
        double newPartitionValue = Double.MAX_VALUE;
        for (Node n : dataNodes) {
            if (n.value[node.partitionDimention] < node.partitionValue) {
                left.add(n);
            } else if (n.value[node.partitionDimention] > node.partitionValue) {
                right.add(n);
                if(n.value[node.partitionDimention] < newPartitionValue)
                    newPartitionValue = n.value[node.partitionDimention];
            } else {
                tmpEqual.add(n);
            }
        }

        if (left.size() < right.size()) {
            left.addAll(tmpEqual);
            node.partitionValue = newPartitionValue;
        }
        else
            right.addAll(tmpEqual);

//            JMetalLogger.logger.info("\nL["+left.size()+"] <"+var+" , "+node.partitionValue+">  R["+right.size()+"]\n");
        if (!left.isEmpty())
            node.left = buildDetail(left, dimentions);
        if (!right.isEmpty())
            node.right = buildDetail(right, dimentions);

        return node;
    }

    public int queryIndex(double[] input) {
        Node node = kdtree;
        Stack<Node> stack = new Stack<Node>();
        while (!node.isLeaf) {
            if (input[node.partitionDimention] < node.partitionValue) {
                stack.add(node.right);
                node = node.left;
            } else {
                stack.push(node.left);
                node = node.right;
            }
        }
        /**
         * 首先按树一路下来，得到一个相对较近的距离，再找比这个距离更近的点
         */
        double distance = UtilZ.distance(input, node.value);
        Node nearestNode = queryRec(input, distance, stack);

        return nearestNode == null ? node.index : nearestNode.index;
    }

    public double[] queryCoordinate(double[] input) {
        Node node = kdtree;
        Stack<Node> stack = new Stack<Node>();
        while (!node.isLeaf) {
            if (input[node.partitionDimention] < node.partitionValue) {
                stack.add(node.right);
                node = node.left;
            } else {
                stack.push(node.left);
                node = node.right;
            }
        }
        /**
         * 首先按树一路下来，得到一个相对较近的距离，再找比这个距离更近的点
         */
        double distance = UtilZ.distance(input, node.value);
        Node nearestNode = queryRec(input, distance, stack);

        return nearestNode == null ? node.value : nearestNode.value;
    }

    public Node queryRec(double[] input, double distance, Stack<Node> stack) {
        Node nearest = null;
        Node node = null;
        double tdis;
        while (stack.size() != 0) {
            node = stack.pop();
            if (node.isLeaf) {
                tdis = UtilZ.distance(input, node.value);
                if (tdis < distance) {
                    distance = tdis;
                    nearest = node;
                }
            } else {
                /*
                 * 得到该节点代表的超矩形中点到查找点的最小距离mindistance
                 * 如果mindistance<distance表示有可能在这个节点的子节点上找到更近的点
                 * 否则不可能找到
                 */
                double mindistance = UtilZ.mindistance(input, node.max, node.min);
                if (mindistance < distance) {
                    while (!node.isLeaf) {
                        if (input[node.partitionDimention] < node.partitionValue) {
                            stack.add(node.right);
                            node = node.left;
                        } else {
                            stack.push(node.left);
                            node = node.right;
                        }
                    }
                    tdis = UtilZ.distance(input, node.value);
                    if (tdis < distance) {
                        distance = tdis;
                        nearest = node;
                    }
                }
            }
        }
        return nearest;
    }

    public List<Integer> queryKNearestIndexes(double[] input,int K) {
        List<Node> nodeList = queryKNearestNodes(input,K);
        List<Integer> result = new ArrayList<>(nodeList.size());
        for (int i=0;i<nodeList.size();i++){
            result.add(nodeList.get(i).index);
        }
        return result;
    }

    public List<double[]> queryKNearestCoordinates(double[] input,int K) {
        List<Node> nodeList = queryKNearestNodes(input,K);
        List<double[]> result = new ArrayList<>(nodeList.size());
        for (int i=0;i<nodeList.size();i++){
            result.add(nodeList.get(i).value);
        }
        return result;
    }

    public List<Node> queryKNearestNodes(double[] input,int K) {
        Node node = kdtree;
        Stack<Node> stack = new Stack<Node>();
        while (!node.isLeaf) {
            if (input[node.partitionDimention] < node.partitionValue) {
                stack.add(node.right);
                node = node.left;
            } else {
                stack.push(node.left);
                node = node.right;
            }
        }
        if(node != kdtree)
            stack.add(node);

        List<Node> nearestKNodes = new ArrayList<>(K);
        List<Double> distances = new ArrayList<>(K);

        while (stack.size() != 0) {
            node = stack.pop();
            if(!node.isLeaf) {
                /*
                     * 得到该节点代表的超矩形中点到查找点的最小距离mindistance
                     * 如果mindistance<distance表示有可能在这个节点的子节点上找到更近的点
                     * 否则不可能找到
                     */
                double mindistance = UtilZ.mindistance(input, node.max, node.min);
                double maxDis = Double.POSITIVE_INFINITY;
                if (nearestKNodes.size() == K) {
                    maxDis = distances.get(0);
                }

                if (mindistance < maxDis) {
                    while (!node.isLeaf) {
                        if (input[node.partitionDimention] < node.partitionValue) {
                            stack.add(node.right);
                            node = node.left;
                        } else {
                            stack.push(node.left);
                            node = node.right;
                        }
                    }
                }
            }

            if (node.isLeaf) {
                double dis = UtilZ.distance(input, node.value);
                findKNearestNodes(nearestKNodes,distances,node,dis,K);
            }
        }

        if(nearestKNodes.size()<K)
            buildHeap(nearestKNodes, distances);
        //排序
        for (int i = nearestKNodes.size() - 1; i > 0; --i) {
            //堆顶与堆尾交换
            nodeDataSwap(nearestKNodes,distances,i, 0);
            //调整，维护堆结构
            MaintainHeap(nearestKNodes,distances, 0,i-1);
        }

        return nearestKNodes;
    }


    private void nodeDataSwap(List<Node> nodeList , List<Double> distances, int pos1, int pos2){
        double tmpD = distances.get(pos1);
        distances.set(pos1,distances.get(pos2));
        distances.set(pos2,tmpD);
        Node tmpN = nodeList.get(pos1);
        nodeList.set(pos1,nodeList.get(pos2));
        nodeList.set(pos2,tmpN);
    }


    private void buildHeap(List<Node> nodeList,List<Double> distances) {
        //建堆
        for (int i = nodeList.size() / 2; i >= 0; --i)
            MaintainHeap(nodeList,distances,i,nodeList.size()-1);
    }

    private void MaintainHeap(List<Node> nodeList,List<Double> distances, int pos, int end) {
        //递归写法
        int idxNext = 2 * pos + 1;
        //左孩子 2 * pos + 1
        if (idxNext > end) return;
        //右孩子 2 * pos + 2
        if (idxNext + 1 <= end && distances.get(idxNext) < distances.get(idxNext + 1))
            idxNext += 1;
        //如果孩子节点优于 当前节点时交换并向下调整
        if (distances.get(pos) < distances.get(idxNext)) {
            nodeDataSwap(nodeList,distances,pos,idxNext);
            MaintainHeap(nodeList,distances,idxNext, end);
        }
    }

    private void insert2Heap(List<Node> nodeList, List<Double> distances, Node nodeInsert,double distanceInsert) {
        if (distanceInsert < distances.get(0)) {
            nodeList.set(0,nodeInsert);
            distances.set(0,distanceInsert);
            MaintainHeap(nodeList,distances,0,nodeList.size()-1);
        }
    }

    private void findKNearestNodes(List<Node> nodeList,List<Double> distances,Node newNode,double newDistance, int K){
        if(nodeList.size() < K){
            nodeList.add(newNode);
            distances.add(newDistance);
            if(nodeList.size() == K) {
                buildHeap(nodeList, distances);
            }
        }else{
            insert2Heap(nodeList,distances,newNode,newDistance);
        }
    }

    public static int nearest(double[] input, double[][] data) {
        int nearest = -1;
        double dis = Double.MAX_VALUE;
        double tdis;
        for (int i = 0; i < data.length; i++) {
            tdis = UtilZ.distance(input, data[i]);
            if (tdis < dis) {
                dis = tdis;
                nearest = i;
            }
        }
        return nearest;
    }

    public static void correct() {
        int count = 100000;
        while (count-- > 0) {
            int num = 100;
            double[][] input = new double[num][2];
            for (int i = 0; i < num; i++) {
                input[i][0] = Math.random() * 10;
                input[i][1] = Math.random() * 10;
            }
            double[] query = new double[]{Math.random() * 50, Math.random() * 50};

            KDTree tree = KDTree.build(input);
            int result = tree.queryIndex(query);
            int result1 = nearest(query, input);
            if (result == -1 || result1 == -1 || result != result1) {
                JMetalLogger.logger.info("["+result+"] -- ["+result1+"]");
                System.out.println("wrong");
                break;
            }
        }
    }

    public static void main(String[] args) {
//            int num = 10;
//            int iteration = 10;
//            int nObj = 10;
////            ArrayList<double[]> input = new ArrayList<>(num);
////            for(int i=0;i<num;i++){
////                double[] d = new double[nObj];
////                for (int j=0;j<nObj;j++)
////                    d[j]= Math.random();
////                input.add(d);
////            }
//
//            UniformSimplexCentroidWeightsUtils uniformWeights = new UniformSimplexCentroidWeightsUtils(nObj,3);
//            ArrayList<double[]> weights = uniformWeights.getUniformWeightsList();
//
//            KDTree tree=KDTree.build(weights);
//
//            double[][] query = new double[iteration][nObj];
//            for(int i=0;i<iteration;i++){
//                for(int j=0;j<nObj;j++)
//                    query[i][j]= Math.random();
//            }
//
//            for(int i=0;i<iteration;i++){
//                int result = tree.query(query[i]);
//            }
//
//        }
//        correct();
    }
}