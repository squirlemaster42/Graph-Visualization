import javax.swing.JFrame;

public class Main {

    public static void main(String[] args){
        //Used for random graph generation. Currently, broken
        //RandomMatrixStringGenerator.MatrixBuilderPair randMatrix = RandomMatrixStringGenerator.generateRandomMatrixString();
        //AdjMat adjMat =  AdjMat.makeMatrixFromStringArray(randMatrix.matrix, randMatrix.size);

        AdjMat adjMat = AdjMat.makeMatrixFromFile("adjMat.txt");
        UnweightedDirectedGraph g = adjMat.makeGraph();

        JFrame frame = new JFrame("Graph Visualizer");
        frame.add(new GraphVisualizer((g)));
        frame.setSize(GraphVisualizer.screenWidth, GraphVisualizer.screenHeight);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
