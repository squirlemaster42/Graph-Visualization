import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

public class DegMatrix{

    private final RealMatrix matrix;

    public DegMatrix(final AdjMat adjMat) {
        matrix = new Array2DRowRealMatrix(adjMat.getNumVertices(), adjMat.getNumVertices());
        buildMatrix(adjMat);

        for (int i = 0; i < adjMat.getNumVertices(); i++) {
            for (int j = 0; j < adjMat.getNumVertices(); j++) {
                System.out.print(matrix.getEntry(i, j) + " ");
            }
            System.out.println();
        }
    }

    private void buildMatrix(AdjMat adjMat) {
        for(int i = 0; i < adjMat.getNumVertices(); i++){
            int degree = 0;
            for(int j = 0; j < adjMat.getNumVertices(); j++){
                if(adjMat.isEdge(i, j)){
                    degree++;
                }
            }
            matrix.setEntry(i, i, degree);
        }
    }

    public RealMatrix getMatrix(){
        return matrix;
    }
}
