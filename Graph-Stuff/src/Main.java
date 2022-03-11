import java.io.FileNotFoundException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static final double cRep = 10000.0;
    public static final double cSpring = 15.0;
    public static final double kL = 50.0;
    public static final int overscaleWidth = 16;
    public static final int overscaleHeight = 39;
    public static final int screenWidth = 1000 - overscaleWidth;
    public static final int screenHeight = 800 - overscaleHeight;
    public static final int circleDiameter = 20;

    public static void main(String[] args){
        //Used for random graph generation. Currently, broken
        //RandomMatrixStringGenerator.MatrixBuilderPair randMatrix = RandomMatrixStringGenerator.generateRandomMatrixString();
        //AdjMat adjMat =  AdjMat.makeMatrixFromStringArray(randMatrix.matrix, randMatrix.size);

        AdjMat adjMat = AdjMat.makeMatrixFromFile("adjMat.txt");
        UnweightedDirectedGraph g = adjMat.makeGraph();
        ConcurrentFileWriter concurrentFileWriter;
        try {
            concurrentFileWriter = new ConcurrentFileWriter("largeTest.txt");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        concurrentFileWriter.start();
        ExecutorService pool = Executors.newFixedThreadPool(10);

        ConcurrentGraphProcessor[] jobs = new ConcurrentGraphProcessor[10000];
        for(int i = 0; i < jobs.length; i++){
            RandomMatrixStringGenerator.MatrixBuilderPair randMatrixPair = RandomMatrixStringGenerator.generateRandomMatrixString();
            AdjMat adj = AdjMat.makeMatrixFromStringArray(randMatrixPair.matrix, randMatrixPair.size);
            jobs[i] = new ConcurrentGraphProcessor(adj, concurrentFileWriter, screenWidth, screenHeight);
        }

        for(int i = 0; i < jobs.length; i++){
            pool.execute(jobs[i]);
        }

        pool.shutdown();
        concurrentFileWriter.stop();


//        JFrame frame = new JFrame("Graph Visualizer");
//        frame.add(new GraphVisualizer((g)));
//        frame.setSize(GraphVisualizer.screenWidth + GraphVisualizer.overscaleWidth, GraphVisualizer.screenHeight + GraphVisualizer.overscaleHeight);
//        frame.setVisible(true);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
