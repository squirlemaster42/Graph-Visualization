public class DegMatrix{

    private final AdjMat adjMat;
    private final int[][] matrix;

    public DegMatrix(final AdjMat adjMat) {
        this.adjMat = adjMat;
        matrix = new int[adjMat.getNumVertices()][adjMat.getNumVertices()];
        buildMatrix();

        for (int[] ints : matrix) {
            for (int j = 0; j < matrix.length; j++) {
                System.out.print(ints[j] + " ");
            }
            System.out.println();
        }
    }

    private void buildMatrix() {
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

    public int get(int i, int j){
        return matrix[i][j];
    }
}
