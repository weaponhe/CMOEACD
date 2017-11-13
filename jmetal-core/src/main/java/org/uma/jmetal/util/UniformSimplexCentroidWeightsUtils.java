package org.uma.jmetal.util;

import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;

/**
 * Created by X250 on 2016/6/9.
 */
public class UniformSimplexCentroidWeightsUtils {
    enum NeighborType{INNER,OUTER};
    //Node type for coordinate tree, including index node and coordinate node
    private enum NodeType {Index,Coordinate}
    private class Node {
        int value;
        NodeType type;
        Node next;
        Node sibling;
        public Node(int v,NodeType t){
            value = v;
            type = t;
            next = null;
            sibling = null;
        }
    };
    private Node coordinateTree = null;
    private int nObj;
    private int H;
    private int totalNum;
    private int plane ; // the intercept of hyperplane for uniform weights generating tree

    private ArrayList<int[]> observationCoordinate = null;
    private List<int[]> innerNeighborFactor = null;
    private ArrayList<ArrayList<Integer>> innerNeighbors = null;
    private List<int[]> outerNeighborFactor = null;
    private ArrayList<ArrayList<Integer>> outerNeighbors = null;
    private int[] coordinate = null;
    private int[] nextCoordinate = null;
    private KDTree kdtree = null;

    public UniformSimplexCentroidWeightsUtils(int _nObj,int _H){
        nObj = _nObj;
        H = _H;
        plane = H*nObj;

        coordinate = new int[nObj];
        nextCoordinate = new int[nObj];
    }

    public void free(){
        coordinateTree = null;
    }
    // build the uniform coordinate tree for generating uniform weights,
    // locating a certain observation vector to its belonging subregion with certain uniform weight
    // and finding neighbors
    private void buildTree(){
        List<int[]> subtractionFactors = new ArrayList<>(nObj);
        int[] tmp1 = new int[nObj];
        tmp1[0] = -(nObj-1);
        for(int i=1;i<nObj;++i)
            tmp1[i] = 1;
        subtractionFactors.add(tmp1);
        int[] tmp2 = new int[nObj];
        for(int i=0;i<nObj;++i)
            tmp2[i] = -1;
        for(int i=1;i<nObj;++i){
            tmp2[i] = nObj-1;
            int[] tmp3 = new int[nObj];
            System.arraycopy(tmp2,0,tmp3,0,nObj);
            subtractionFactors.add(tmp3);
            tmp2[i] = -1;
        }

        totalNum = 0;
        coordinate[0] = plane;
        for(int i=1;i<nObj;i++)
            coordinate[i] = 0;
        Node dummy = new Node(-1,NodeType.Coordinate);
        checkAndInsert(dummy,coordinate,0);
//        buildTree(dummy,coordinate,subtractionFactors);
//
//        candidate queue to store coordinate for generating next coordinate
        Queue<int[]> candidateQueue = new ArrayDeque<>();
        candidateQueue.add(coordinate);

        while(!candidateQueue.isEmpty()){
            coordinate = candidateQueue.remove();

            for(int i=0;i<subtractionFactors.size();i++){
                int[] factor =  subtractionFactors.get(i);
                boolean isValid = true;
                for(int j=0;j<nObj;++j){
                    nextCoordinate[j] = coordinate[j] + factor[j];
                    if(nextCoordinate[j] < 0){
                        isValid = false;
                        break;
                    }
                }
                if (isValid) {
                    if (checkAndInsert(dummy, nextCoordinate, 0)) {//build the coordinate tree
                        int j;
                        for( j= 0;j<nObj;j++) {
                            if(nextCoordinate[j] == 0)
                                break;
                        }
                        if(j == nObj) {
                            int[] copyCoordinate = new int[nObj];
                            System.arraycopy(nextCoordinate,0,copyCoordinate,0,nObj);
                            candidateQueue.add(copyCoordinate);
                        }
                    }
                }
            }
        }
        coordinateTree = dummy.next;
    }

    public void buildTree(Node head,int[] coordinate, List<int[]> genFactors){

        int[] nextCoordinate = new int[nObj];
        for(int i=0;i<genFactors.size();i++){
            int[] factor =  genFactors.get(i);
            boolean isValid = true;
            for(int j=0;j<nObj;++j){
                nextCoordinate[j] = coordinate[j] + factor[j];
                if(nextCoordinate[j] < 0){
                    isValid = false;
                    break;
                }
            }
            if (isValid && checkAndInsert(head,nextCoordinate,0)) {
                int j;
                for(j = 0;j<nObj;j++) {
                    if(nextCoordinate[j] == 0)
                        break;
                }
                if(j == nObj)
                    buildTree(head, nextCoordinate, genFactors);
            }
        }
    }


    public boolean checkAndInsert(Node root,int[] coordinate,int dim){
        if(dim == nObj){
            return false;
        }
        if(root.next == null){
            Node node1 = new Node(coordinate[nObj - 1], NodeType.Coordinate);
            for (int i = nObj - 2; i >= dim; i--) {
                Node node2 = new Node(coordinate[i], NodeType.Coordinate);
                node2.next = node1;
                node1 = node2;
            }
            root.next = node1;
            return true;
        }

        Node p = root.next;
        Node q = p;
        while(p != null && p.value < coordinate[dim]){
            q = p;
            p= p.sibling;
        }
        if(p == null || p.value > coordinate[dim]){

            Node node1 = new Node(coordinate[nObj - 1], NodeType.Coordinate);
            for (int i = nObj - 2; i >= dim; i--) {
                Node node2 = new Node(coordinate[i], NodeType.Coordinate);
                node2.next = node1;
                node1 = node2;
            }
            node1.sibling = p;
            if(q.value < coordinate[dim]){
                q.sibling = node1;
            }else{
                root.next = node1;
            }
            return true;
        }

        return checkAndInsert(p,coordinate,dim+1);
    }

    private void insertIndex(Node node , int dim){
        if(dim == nObj-1){
            //leaf
            Node indexNode = new Node(totalNum,NodeType.Index);
            node.next = indexNode;
            totalNum++;
            return;
        }

        Node p = node;
        while(p != null){
            insertIndex(p.next,dim+1);
            p = p.sibling;
        }
    }

    public void initUniformWeights(){
        buildTree();
        totalNum = 0;
        insertIndex(coordinateTree,0);
        observationCoordinate = null;
        innerNeighborFactor = null;
        innerNeighbors = null;
        outerNeighborFactor = null;
        outerNeighbors = null;
    }


    public void generateIndexingTree(){
        ArrayList<double[]> uniformWeights = getUniformWeights();
        kdtree =  KDTree.build(uniformWeights);
    }

    //use the uniform coordinate tree to generate uniform weights
    public Vector<Vector<Double>> getUniformWeightsVector(){
        ArrayList<double[]> uniformWeights = getUniformWeights();
        Vector<Vector<Double>> uniformWeightsVector = new Vector<>(uniformWeights.size());
        for(int i=0;i<uniformWeights.size();++i){
            Vector<Double> weights = new Vector<>(nObj);
            for(int j=0;j<nObj;++j)
                weights.add(uniformWeights.get(i)[j]);
            uniformWeightsVector.add(weights);
        }
        return uniformWeightsVector;
    }


    public ArrayList<double[]> getUniformWeights(){
        if(coordinateTree == null){
            initUniformWeights();
        }
        if(observationCoordinate == null){
            observationCoordinate = calcObservationCoordinate();
        }
        ArrayList<double[]> uniformWeights = new ArrayList<>(totalNum);

        for(int i=0;i<observationCoordinate.size();i++){
            double[] weights = new double[nObj];
            for(int j=0;j<nObj;j++)
                weights[j] = (1.0 * observationCoordinate.get(i)[j])/plane;
            uniformWeights.add(weights);
        }
        return uniformWeights;
    }


    public ArrayList<int[]> getObservationCoordinate() {
        if (coordinateTree == null) {
            initUniformWeights();
        }
        if(observationCoordinate == null)
            observationCoordinate = calcObservationCoordinate();

        return observationCoordinate;
    }

    private ArrayList<int[]> calcObservationCoordinate(){
        if(coordinateTree == null){
            initUniformWeights();
        }
        ArrayList<int[]> observationInt = new ArrayList<>();
        calcObservationCoordinate(coordinateTree,coordinate,0,observationInt);
        return observationInt;
    }

    private void calcObservationCoordinate(Node node, int[] coordinate , int dim, ArrayList<int[]> observationInt){
        if(dim == nObj){
            int[] copyCoordinate = new int[nObj];
            System.arraycopy(coordinate,0,copyCoordinate,0,nObj);
            observationInt.add(copyCoordinate);
            return;
        }

        Node p = node;
        while(p != null){
            coordinate[dim] = p.value;
            calcObservationCoordinate(p.next,coordinate,dim+1,observationInt);
            p = p.sibling;
        }
    }

    public ArrayList<Integer> getExtremeIdxes(){
        if(coordinateTree == null){
            initUniformWeights();
        }
        ArrayList<Integer> idxExtreme = new ArrayList<>(nObj);
        for(int i=0;i<nObj;++i)
            coordinate[i] =0;
        for(int i=0;i<nObj;++i){
            coordinate[i] = plane;
            idxExtreme.add(indexing(coordinate));
            coordinate[i] = 0;
        }
        return idxExtreme;
    }

    public ArrayList<Integer> getMarginalIdxes(){
        if(coordinateTree == null){
            initUniformWeights();
        }
        if(observationCoordinate == null)
            observationCoordinate = calcObservationCoordinate();

        ArrayList<Integer> idxMarginal = new ArrayList<>();

        for(int i=0;i<observationCoordinate.size();i++){
            int j;
            for(j=0;j<nObj;j++){
                if(observationCoordinate.get(i)[j] == 0){
                    break;
                }
            }
            if(j < nObj)
                idxMarginal.add(i);
        }
        return idxMarginal;
    }

    private void calcMarginalIdxes(Node node,int[] coordinate,int dim,List<Integer> idxMarginal){
        if(dim == nObj){
            int i;
            for(i=0;i<nObj;i++){
                if(coordinate[i]==0){
                    break;
                }
            }
            if(i < nObj){
                idxMarginal.add(node.value);
            }
            return;
        }

        Node p = node;
        while(p != null){
            coordinate[dim] = p.value;
            calcMarginalIdxes(p.next,coordinate,dim+1,idxMarginal);
            p = p.sibling;
        }
    }

    public double dist2(int[] observation, double[] objective){
        double d = 0;
        for(int i=0;i<nObj;i++){
            d += Math.pow(objective[i] - observation[i],2.0);
        }
        return d;
    }

    public double dist2(int[] observation1, int[] observation2){
        double d = 0;
        for(int i=0;i<nObj;i++){
            d += Math.pow(observation1[i] - observation2[i],2.0);
        }
        return d;
    }

    public int indexing(double[] v) {
        if (coordinateTree == null) {
            initUniformWeights();
        }
        return kdtree.queryIndex(v);
    }

    public int indexing(int[] coordinate){
        if(coordinateTree == null){
            initUniformWeights();
        }

        int index = searchIndex(coordinateTree,coordinate,0);
        return index;
    }

    public int searchIndex(Node node, int[] coordinate, int dim) {
        if (dim == nObj)
            return node.value;

        Node p = node;
        while (p != null && p.value < coordinate[dim]) {
            p = p.sibling;
        }
        if (p != null && p.value == coordinate[dim])
            return searchIndex(p.next, coordinate, dim + 1);
        else
            return -1;
    }

    //whether the coordinate is valid in the hyperplane with 'plane' as its intercept
    public boolean checkCoordinate(int[] coordinate){
        boolean isValid = true;
        int sum = 0;
        for(int i=0;i<nObj;++i){
            if(coordinate[i] < 0 || coordinate[i] > plane){
                isValid = false;
                break;
            }
            sum += coordinate[i];
        }
        if(isValid){
            if(sum != plane)
                isValid = false;
        }
        return  isValid;
    }

    public ArrayList<ArrayList<Integer>> getInnerNeighbors(){
        if(coordinateTree == null){
            initUniformWeights();
        }

        if(innerNeighbors == null)
            innerNeighbors = calcNeighbors(NeighborType.INNER);

        return innerNeighbors;
    }
    public ArrayList<ArrayList<Integer>> getOuterNeighbors(){
        if(coordinateTree == null){
            initUniformWeights();
        }

        if(outerNeighbors == null)
            outerNeighbors = calcNeighbors(NeighborType.OUTER);
        return outerNeighbors;
    }

    private List<int[]> calcNeighborFactor(NeighborType neighborType){
        List<int[]> neighborFactor = null;
        if(neighborType == neighborType.INNER){
            neighborFactor = new ArrayList<>(2*nObj);
            int[] tmp = new int[nObj];
            for(int i=0;i<nObj;i++){
                tmp[i] = -1;
            }
            for(int i=0;i<nObj;i++){
                tmp[i] = nObj-1;
                int[] tmp2 = new int[nObj];
                System.arraycopy(tmp,0,tmp2,0,nObj);
                neighborFactor.add(tmp2);
                tmp[i] = -1;
            }
            if(nObj > 2) {
                for (int i = 0; i < nObj; i++) {
                    tmp[i] = 1;
                }
                for (int i = 0; i < nObj; i++) {
                    tmp[i] = -(nObj - 1);
                    int[] tmp2 = new int[nObj];
                    System.arraycopy(tmp, 0, tmp2, 0, nObj);
                    neighborFactor.add(tmp2);
                    tmp[i] = 1;
                }
            }
        }
        else if(neighborType == NeighborType.OUTER) {
            neighborFactor = new ArrayList<>(nObj * (nObj - 1));
            int[] tmp = new int[nObj];
            for (int i = 0; i < nObj; i++) {
                tmp[i] = 0;
            }
            for (int i = 0; i < nObj; i++) {
                tmp[i] = nObj;
                for (int j = 0; j < nObj; j++) {
                    if (i == j) continue;
                    tmp[j] = -nObj;
                    int[] tmp2 = new int[nObj];
                    System.arraycopy(tmp, 0, tmp2, 0, nObj);
                    neighborFactor.add(tmp2);
                    tmp[j] = 0;
                }
                tmp[i] = 0;
            }
        }
        return neighborFactor;
    }

    private ArrayList<ArrayList<Integer>> calcNeighbors(NeighborType neighborType) {
        if (coordinateTree == null) {
            initUniformWeights();
        }

        if(observationCoordinate == null)
            observationCoordinate = calcObservationCoordinate();

        List<int[]> neighborFactor = null;
        if (neighborType == NeighborType.INNER) {
            if (innerNeighborFactor == null)
                innerNeighborFactor = calcNeighborFactor(NeighborType.INNER);

            neighborFactor = innerNeighborFactor;
        }
        else if(neighborType == NeighborType.OUTER) {
            if (outerNeighborFactor == null)
                outerNeighborFactor = calcNeighborFactor(NeighborType.OUTER);
            neighborFactor = outerNeighborFactor;
        }

        ArrayList<ArrayList<Integer>> neighborsResult = new ArrayList<>(totalNum);

        for (int i=0;i<observationCoordinate.size();i++){
            neighborsResult.add(calcNeighbors(observationCoordinate.get(i),neighborFactor));
        }
        return neighborsResult;
    }

    public ArrayList<Integer> calcInnerNeighbors(int idx){
        if(coordinateTree == null){
            initUniformWeights();
        }

        if(observationCoordinate == null)
            observationCoordinate = calcObservationCoordinate();
        if(innerNeighborFactor == null)
            innerNeighborFactor = calcNeighborFactor(NeighborType.INNER);

        return calcNeighbors(observationCoordinate.get(idx),innerNeighborFactor);
    }

    public ArrayList<Integer> calcOuterNeighbors(int idx){
        if(coordinateTree == null){
            initUniformWeights();
        }

        if(observationCoordinate == null)
            observationCoordinate = calcObservationCoordinate();
        if(outerNeighborFactor == null)
            outerNeighborFactor = calcNeighborFactor(NeighborType.OUTER);

        return calcNeighbors(observationCoordinate.get(idx),outerNeighborFactor);
    }

    public ArrayList<Integer> calcOuterBoundNeighbors(int idx){
        if(coordinateTree == null){
            initUniformWeights();
        }
        if(observationCoordinate == null)
            observationCoordinate = calcObservationCoordinate();
        if(outerNeighborFactor == null)
            outerNeighborFactor = calcNeighborFactor(NeighborType.OUTER);

        int[] coordinate = observationCoordinate.get(idx);
        List<int[]> neighborFactor = outerNeighborFactor;

        ArrayList<Integer> neighborsResult = new ArrayList<>();
        for(int i=0;i<neighborFactor.size();i++){
            boolean isValid = true;
            for(int j=0;j < nObj;j++){
                if(neighborFactor.get(i)[j] !=0 && coordinate[j] == 0){
                    isValid = false;
                    break;
                }
                nextCoordinate[j] = coordinate[j] + neighborFactor.get(i)[j];
            }
            if(isValid && checkCoordinate(nextCoordinate)){
                int[] copyCoordinate = new int[nObj];
                System.arraycopy(nextCoordinate,0,copyCoordinate,0,nObj);
                neighborsResult.add(indexing(copyCoordinate));
            }
        }
        return neighborsResult;
    }

    public ArrayList<Integer> calcNeighbors(int[] coordinate, List<int[]> neighborFactor){
        ArrayList<Integer> neighborsResult = new ArrayList<>();
        for(int i=0;i<neighborFactor.size();i++){
            for(int j=0;j < nObj;j++){
                nextCoordinate[j] = coordinate[j] + neighborFactor.get(i)[j];
            }
            if(checkCoordinate(nextCoordinate)){
                int[] copyCoordinate = new int[nObj];
                System.arraycopy(nextCoordinate,0,copyCoordinate,0,nObj);
                neighborsResult.add(indexing(copyCoordinate));
            }
        }
        return neighborsResult;
    }

    public double[] observation(double[] objective){
        double[] observationV = new double[nObj];
        double sum = 0.0;
        for(int i=0;i<nObj;i++)
            sum += objective[i];
        if(sum <= Constant.TOLERATION){
            for(int i=0;i<nObj;i++)
                observationV[i] = 0;
            observationV[0] = 1;
        }else{
            for(int i=0;i<nObj;i++)
                observationV[i] = objective[i]/sum;
        }
        return observationV;
    }

    public static void main(String []argv){
        int nObj = 8;
        int H = 3;
        UniformSimplexCentroidWeightsUtils uniformWeights = new UniformSimplexCentroidWeightsUtils(nObj,H);
        ArrayList<double[]> weights = uniformWeights.getUniformWeights();
//        ArrayList<int[]> observationInt = uniformWeights.getObservationCoordinate();
//        ArrayList<ArrayList<Integer>> innerNeighbors  = uniformWeights.getInnerNeighbors();
//        ArrayList<ArrayList<Integer>> outerNeighbors = uniformWeights.getOuterNeighbors();

//        String outStr = "\n";
//        for(int i=0;i<weights.size();i++){
////            outStr += "["+i+" -- "+uniformWeights.indexing(observationInt.get(i))+"]<";
////            for(int j=0;j<nObj;j++){
////                outStr += observationInt.get(i)[j]+" , ";
////            }
////            outStr += ">";
////            outStr += "(";
////            for(int j=0;j<nObj;j++){
////                outStr += weights.get(i)[j]+" , ";
////            }
////            outStr += ")";
////            outStr +="\n inner neighbors : ";
////            for(int j=0;j<innerNeighbors.get(i).size();++j){
////                outStr += ("\t" + innerNeighbors.get(i).get(j));
////            }
////            outStr +="\n outer neighbors : ";
////            for(int j=0;j<outerNeighbors.get(i).size();++j){
////                outStr += ("\t" + outerNeighbors.get(i).get(j));
////            }
//            outStr+="\n";
//        }
//        JMetalLogger.logger.info(outStr);
////
//        JMetalLogger.logger.info(""+weights.size());
//        double[] point = {
//                0.0036658164194476764,
//                0.0029258021023468813 ,
//                0.2246462896191239 ,
//                4.562310707483365E-4 ,
//                0.2870055091505698 ,
//                0.0019701946670394517 ,
//                3.431764546157917E-4 ,
//                2.3784388306830295E-4 ,
//                0.2579482805856846 ,
//                0.22080085604735528
//                };

//        double[] point = {
//                0.023626233728580782 ,
//                6.249007837939533E-5 ,
//                0.16683117289235722 ,
//                0.10060931292775147 ,
//                0.7088707903729311};

//        JMetalRandom randomGenerator  = JMetalRandom.getInstance();
//        for(int r=0;r<100;r++) {
//            double[] point = new double[nObj];
//            for (int i=0;i<nObj;i++){
//                point[i] = randomGenerator.nextDouble(0.0,1.0);
//            }
//
////            double[] point = {8.639662171328129E-7 , 1.2288778266150832E-6 , 1.0370340393107833E-6 , 6.885885089427594E-6 , 1.2887781941477496E-6 , 2.1844121932369743E-6 , 0.5256457114604238 , 0.25092962696794857 , 0.2234106557350203 , 5.168830475589208E-7 };
//            double[] o = uniformWeights.observation(point);
//            double[] norm = new double[nObj];
//            for (int i = 0; i < nObj; i++) {
//                norm[i] = o[i] * H * nObj;
//            }
//            int idx = 0;
//            double minDist = uniformWeights.dist2(observationInt.get(0), norm);
//            for (int i = 1; i < observationInt.size(); i++) {
//                double tmp = uniformWeights.dist2(observationInt.get(i), norm);
//                if (tmp < minDist) {
//                    idx = i;
//                    minDist = tmp;
//                }
//            }
//
//            String str = "\n(";
//            for(int i=0;i<nObj;i++){
//                str += norm[i] + " , ";
//            }
//
//            str += ")\nbelong : [" + idx + "] < ";
//            for (int i = 0; i < nObj; i++) {
//                str += observationInt.get(idx)[i] + " , ";
//            }
//            str += ">\n";
//
//            int index = -1;//uniformWeights.indexing(uniformWeights.observation(point));
//
//             str += "[" + index + "]<";
//            if(index != -1){
//            for (int i = 0; i < nObj; i++) {
//                str += observationInt.get(index)[i] + " , ";
//            }
//            }
//            str += ">\n";
//            if(idx != index)
//                str += "Error !\n\n\n";
//            JMetalLogger.logger.info( str);
//        }

        BufferedWriter writer = new DefaultFileOutputContext("E:\\uniform.dat").getFileWriter();
        try {
            for (int i = 0; i < weights.size(); ++i) {
                for (int j = 0; j < nObj; j++) {
                    writer.write("" + weights.get(i)[j] + " ");
                }
                writer.newLine();
            }
            writer.close();
        }catch (IOException e){}
    }
}
