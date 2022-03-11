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
        AdjMat returnMatrix = new AdjMat(numRows + 1);
        for(int j = 1; j < numRows + 1; j++){
            String[] splitData = matrix[j - 1].split(" ");
            for(int i = 0; i < splitData.length; i++){
                if(splitData[i].equals("1")){
                    returnMatrix.setEdge(j, i);
                    returnMatrix.setEdge(i, j);
                }
            }
        }
        return returnMatrix;
    }

    private final RealMatrix matrix;

    public AdjMat(int n){
        matrix = new Array2DRowRealMatrix(n, n);
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                matrix.setEntry(i, j, 0);
            }
        }
    }

    private void setEdge(int i, int j){
        matrix.setEntry(i, j, 1);
    }

    public int getNumVertices(){
        //Our matrix is square so matrix.getColumnDimension() = matrix.getRowDimension()
        return matrix.getColumnDimension();
    }

    public boolean isEdge(int i, int j){
        return matrix.getEntry(i, j) == 1;
    }

    public int get(int i, int j){
        return (int) matrix.getEntry(i, j);
    }

    public UnweightedDirectedGraph makeGraph(){
        UnweightedDirectedGraph graph = new UnweightedDirectedGraph();
        for(int i = 0; i < getNumVertices(); i++){
            graph.addVertex(new UnweightedDirectedGraph.Node(i + ""));
        }
        for(int i = 1; i < getNumVertices(); i++){
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

        for (int i = 0; i < getNumVertices(); i++) {
            for (int j = 0; j < getNumVertices(); j++) {
                builder.append(" ").append(matrix.getEntry(i, j)).append(" ");
            }
            builder.append("\n");
        }

        return builder.toString();
    }

    public RealMatrix getMatrix() {
        return matrix;
    }
}
