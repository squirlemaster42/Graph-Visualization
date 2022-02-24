public class LapMatrix {

    private final AdjMat adjMat;
    private final DegMatrix degMatrix;
    private final int[][] lapMatrix;

    public LapMatrix(final AdjMat adjMat, final DegMatrix degMatrix){
        this.adjMat = adjMat;
        this.degMatrix = degMatrix;

        lapMatrix = new int[adjMat.getNumVertices()][adjMat.getNumVertices()];
    }

    //Computes the Laplacian: laplacian = degMatrix - adjMatrix
    private void computeLaplaceMatrix(){
        for(int i = 0; i < adjMat.getNumVertices(); i++){
            for(int j = 0; j < adjMat.getNumVertices(); j++){
                lapMatrix[i][j] = degMatrix.get(i, j) - adjMat.get(i, j);
            }
        }
    }
}
