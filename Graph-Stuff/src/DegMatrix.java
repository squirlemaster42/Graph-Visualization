import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

public class DegMatrix{

    private final RealMatrix rMatrix;

    public DegMatrix(final AdjMat adjMat) {
        rMatrix = new Array2DRowRealMatrix(adjMat.getNumVertices(), adjMat.getNumVertices());
        buildMatrix(adjMat);

        for (int i = 0; i < adjMat.getNumVertices(); i++) {
            for (int j = 0; j < adjMat.getNumVertices(); j++) {
                System.out.print(rMatrix.getEntry(i, j) + " ");
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
            rMatrix.setEntry(i, i, degree);
        }
    }

    public RealMatrix getrMatrix(){
        return rMatrix;
    }
}
