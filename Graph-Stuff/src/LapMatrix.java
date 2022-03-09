import org.apache.commons.math3.linear.RealMatrix;

public class LapMatrix {

    private final RealMatrix laplacianMatrix;

    public LapMatrix(final AdjMat adjMat, final DegMatrix degMatrix) {
        laplacianMatrix = degMatrix.getMatrix().subtract(adjMat.getMatrix());
    }

    public RealMatrix getLaplacianMatrix() {
        return laplacianMatrix;
    }
}
