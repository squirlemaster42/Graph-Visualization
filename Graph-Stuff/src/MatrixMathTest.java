import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.jupiter.api.Test;

public class MatrixMathTest {

    @Test
    public void testCreateLaplacian(){
        AdjMat adjMat = AdjMat.makeMatrixFromFile("adjMat3.txt");
        DegMatrix degMat = new DegMatrix(adjMat);
        LapMatrix lapacianMatrix = new LapMatrix(adjMat, degMat);
        RealMatrix m = lapacianMatrix.getLaplacianMatrix();
        for(int i = 0; i < m.getColumnDimension(); i++) {
            for (int j = 0; j < m.getColumnDimension(); j++) {
                System.out.printf("%3.0f", m.getEntry(i, j));
            }
            System.out.println();
        }
        EigenDecomposition eigenDecomposition = new EigenDecomposition(m);
        double[] eigenValues = eigenDecomposition.getRealEigenvalues();
        for(int i = 0; i < m.getColumnDimension(); i++){
            System.out.println(eigenValues[i]);
            System.out.println(eigenDecomposition.getEigenvector(i));
        }
    }

    @Test
    public void makeRandomAdjMatrixTest(){
        RandomMatrixStringGenerator.MatrixBuilderPair randMatrixPair = RandomMatrixStringGenerator.generateRandomMatrixString();
        for(int i = 0; i < randMatrixPair.size; i++){
            System.out.println(randMatrixPair.matrix[i]);
        }
        AdjMat adjMat = AdjMat.makeMatrixFromStringArray(randMatrixPair.matrix, randMatrixPair.size);
        System.out.println(adjMat);
        UnweightedDirectedGraph graph = adjMat.makeGraph();
    }
}
