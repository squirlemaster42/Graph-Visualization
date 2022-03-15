import java.util.Map;
import java.util.Random;

public class ConcurrentGraphProcessor implements Runnable{

    private final UnweightedDirectedGraph graph;
    private final ConcurrentFileWriter writer;
    private final int screenWidth, screenHeight;
    private final int jobNumber;
    private String outputString;

    public ConcurrentGraphProcessor(final int jobNumber, final AdjMat adjMat, final ConcurrentFileWriter writer, final int screenWidth, final int screenHeight){
        this.graph = adjMat.makeGraph();
        this.writer = writer;
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
        this.jobNumber = jobNumber;
    }

    @Override
    public void run() {
        outputString = "{";
        int i = 0;
        for (Map.Entry<String, UnweightedDirectedGraph.Node> entry : graph.getVertices().entrySet()){
            try {
                resetRandomPos(entry.getKey());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            i++;
            if(i != graph.getNumNodes() - 1){
                outputString += ",";
            }
        }
        outputString += "}";
        ForceDirectedRunner forceDirectedRunner = new ForceDirectedRunner(graph, GraphVisualizer.cRep, GraphVisualizer.cSpring, GraphVisualizer.kL, screenWidth, screenHeight, GraphVisualizer.circleDiameter);
        while (!forceDirectedRunner.isOptimized()){
            forceDirectedRunner.optimizeGraphPositions(0.005, 100000);
        }
        int intersections = Utils.computeNumEdgeIntersections(graph);
        outputString += " - " + intersections + "\n";
        System.out.print(jobNumber + " - ");
        System.out.println(outputString);
        try {
            writer.queueMessage(outputString);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void resetRandomPos(String i) throws InterruptedException {
        Random rand = new Random();
        //The random component will be > 0 so we don't need to worry about the < 0 case.
        double xPos = Math.min(rand.nextInt(screenWidth), screenWidth);
        double yPos = Math.min(rand.nextInt(screenHeight), screenHeight);
        graph.getVertices().get(i).setXY(xPos, yPos);
        outputString += String.format("{%f,%f}", xPos, yPos);
    }
}
