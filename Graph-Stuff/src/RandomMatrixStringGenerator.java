import java.util.Random;

public class RandomMatrixStringGenerator {

    public static int kMaxSize = 100;

    //Generates a random adj matrix with a random size and random edges
    public static MatrixBuilderPair generateRandomMatrixString(){
        Random rand = new Random();
        int matrixSize = rand.nextInt(kMaxSize) + 5;
        String[] returnMatrix = new String[matrixSize];

        for(int i = 0; i < matrixSize; i++){
            StringBuilder line = new StringBuilder();
            for(int j = 0; j <= i; j++){
                if(rand.nextInt(5) == 1){
                    line.append("1 ");
                }else{
                    line.append("0 ");
                }
            }

            //Check that our vertex is connected somewhere
            String strLine = line.toString();
            boolean foundConnection = strLine.contains("1");
            if(!foundConnection){
                strLine = strLine.replaceFirst("0", "1");
            }
            returnMatrix[i] = strLine;
        }

        return new MatrixBuilderPair(matrixSize - 1, returnMatrix);
    }

    public static class MatrixBuilderPair{

        public final String[] matrix;
        public final int size;

        public MatrixBuilderPair(int size, String[] matrix){
            this.matrix = matrix;
            this.size = size;
        }
    }
}
