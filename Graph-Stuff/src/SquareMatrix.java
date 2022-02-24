public abstract class SquareMatrix {

    protected int[][] matrix;

    public SquareMatrix(int size){
        matrix = new int[size][size];
    }

    public abstract void buildMatrix();

    //TODO Add methods for computing things like eigenvalues of the matrix
}
