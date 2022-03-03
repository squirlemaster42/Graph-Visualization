import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

//TODO make extend square matrix
public class AdjMat {

    public static AdjMat makeMatrixFromFile(final String filePath){
        AdjMat returnMatrix = null;
        try {
            File file = new File(filePath);
            Scanner reader = new Scanner(file);
            int numRows = Integer.parseInt(reader.nextLine());
            returnMatrix = new AdjMat(numRows);
            int row = 1;
            while (reader.hasNextLine()){
                String data = reader.nextLine();
                String[] splitData = data.split(" ");
                for(int i = 0; i < splitData.length; i++){
                    if(splitData[i].equals("1")){
                        returnMatrix.setEdge(row, i);
                        returnMatrix.setEdge(i, row);
                    }
                }
                row++;
            }
            reader.close();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }

        return returnMatrix;
    }

    public static AdjMat makeMatrixFromStringArray(String[] matrix, int numRows){
        for(String str : matrix){
            System.out.println(str);
        }

        AdjMat returnMatrix = new AdjMat(numRows);
        int row = 1;
        for(int j = 0; j < numRows; j++){
            String[] splitData = matrix[j].split(" ");
            for(int i = 0; i < splitData.length; i++){
                if(splitData[i].equals("1")){
                    returnMatrix.setEdge(row, i);
                    returnMatrix.setEdge(i, row);
                }
            }
        }
        return returnMatrix;
    }

    private final int[][] adjMat;
    private final RealMatrix rMatrix;

    public AdjMat(int n){
        adjMat = new int[n][n];
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                adjMat[i][j] = 0;
            }
        }

        double[][] doubles = new double[n][n];
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                doubles[i][j] = adjMat[i][j];
            }
        }
        rMatrix = new Array2DRowRealMatrix(doubles);
    }

    private void setEdge(int i, int j){
        adjMat[i][j] = 1;
        rMatrix.setEntry(i, j, 1);
    }

    public int getNumVertices(){
        return adjMat.length;
    }

    public boolean isEdge(int i, int j){
        return adjMat[i][j] == 1;
    }

    public int get(int i, int j){
        return adjMat[i][j];
    }

    public UnweightedDirectedGraph makeGraph(){
        UnweightedDirectedGraph graph = new UnweightedDirectedGraph();
        for(int i = 0; i < adjMat.length; i++){
            graph.addVertex(new UnweightedDirectedGraph.Node(i + ""));
        }
        for(int i = 1; i < adjMat.length; i++){
            for(int j = 0; j < i; j++){
                if(isEdge(i, j)){
                    graph.addEdge(graph.getVertex(i + ""), graph.getVertex(j + ""));
                }
            }
        }

        return graph;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (int[] ints : adjMat) {
            for (int j = 0; j < adjMat.length; j++) {
                builder.append(" ").append(ints[j]).append(" ");
            }
            builder.append("\n");
        }

        return builder.toString();
    }

    public RealMatrix getrMatrix() {
        return rMatrix;
    }
}
