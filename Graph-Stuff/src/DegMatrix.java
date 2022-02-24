public class DegMatrix extends SquareMatrix{

    private final AdjMat adjMat;

    public DegMatrix(final AdjMat adjMat) {
        super(adjMat.getNumVertices());
        this.adjMat = adjMat;
        buildMatrix();

        for (int[] ints : matrix) {
            for (int j = 0; j < matrix.length; j++) {
                System.out.print(ints[j] + " ");
            }
            System.out.println();
        }
    }

    @Override
    public void buildMatrix() {
        for(int i = 0; i < adjMat.getNumVertices(); i++){
            int degree = 0;
            for(int j = 0; j < adjMat.getNumVertices(); j++){
                if(adjMat.isEdge(i, j)){
                    degree++;
                }
            }
            matrix[i][i] = degree;
        }
    }
}
