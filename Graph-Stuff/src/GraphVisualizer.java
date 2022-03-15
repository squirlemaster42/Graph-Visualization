import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Random;

public class GraphVisualizer extends JPanel {

    public static final double cRep = 30000.0;
    public static final double cSpring = 12.0;
    public static final double kL = 100.0;
    public static final int overscaleWidth = 16;
    public static final int overscaleHeight = 39;
    public static final int screenWidth = 1800 - overscaleWidth;
    public static final int screenHeight = 1200 - overscaleHeight;
    public static final int circleDiameter = 20;
    public static final int delay = 10;
    private final FileManager fileManager;
    private UnweightedDirectedGraph graph;
    private int numRuns = 0;
    private GraphVisualizationRunner forceDirectedRunner;
    private RandomMatrixStringGenerator.MatrixBuilderPair matrixPair = null;

    public GraphVisualizer(UnweightedDirectedGraph graph) {
        this.graph = graph;

        try {
            fileManager = new FileManager("nodeDataDump3.txt");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        fileManager.writeMessage(numRuns + " - ");
        fileManager.writeMessage("{");
        int i = 0;
        for (Map.Entry<String, UnweightedDirectedGraph.Node> entry : graph.getVertices().entrySet()) {
            resetRandomPos(entry.getKey());
            i++;
            if(i != graph.getNumNodes() - 1){
                fileManager.writeMessage(",");
            }
        }
        fileManager.writeMessage("}");
        forceDirectedRunner = new ForceDirectedRunner(graph, cRep, cSpring, kL, screenWidth, screenHeight, circleDiameter);
    }

    public void paintComponent(Graphics g) {
        drawGraph(g);
        if (!forceDirectedRunner.isOptimized()) {
            forceDirectedRunner.optimizeGraphPositions(0.005, 10000);
        }else{
            int intersections = Utils.computeNumEdgeIntersections(graph);
            System.out.println(numRuns + " - Done: " + intersections);
            fileManager.writeMessage(" - " + intersections + "\n");

            if(this.matrixPair != null && intersections == 0){
                try {
                    FileManager badMatrixWriter = new FileManager(numRuns + "BadMatrix.txt");
                    badMatrixWriter.writeMessage(this.matrixPair.size + "\n");
                    for(String str : this.matrixPair.matrix){
                        badMatrixWriter.writeMessage(str + "\n");
                    }
                    badMatrixWriter.close();
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }

            numRuns++;
            fileManager.writeMessage(numRuns + " - ");
            fileManager.writeMessage("{");
//            this.matrixPair = RandomMatrixStringGenerator.generateRandomMatrixString();
//            AdjMat adjMat =  AdjMat.makeMatrixFromStringArray(this.matrixPair.matrix, this.matrixPair.size);
//            graph = adjMat.makeGraph();
            int i = 0;
            for (Map.Entry<String, UnweightedDirectedGraph.Node> entry : graph.getVertices().entrySet()){
                resetRandomPos(entry.getKey());
                i++;
                if(i != graph.getNumNodes() - 1){
                    fileManager.writeMessage(",");
                }
            }
            fileManager.writeMessage("}");
            forceDirectedRunner = new ForceDirectedRunner(graph, cRep, cSpring, kL, screenWidth, screenHeight, circleDiameter);
        }
    }

    private void resetRandomPos(String i){
        Random rand = new Random();
        //The random component will be > 0 so we don't need to worry about the < 0 case.
        double xPos = Math.min(rand.nextInt(screenWidth), screenWidth);
        double yPos = Math.min(rand.nextInt(screenHeight), screenHeight);
        graph.getVertices().get(i).setXY(xPos, yPos);
        fileManager.writeMessage(String.format("{%f,%f}", xPos, yPos));
    }

    public void drawGraph(Graphics g) {
        ((Graphics2D) g).setStroke(new BasicStroke(4));
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, screenWidth, screenHeight);

        g.setColor(Color.LIGHT_GRAY);
        for (Map.Entry<String, UnweightedDirectedGraph.Node> entry : graph.getVertices().entrySet()) {
            String i = entry.getKey();
            g.fillOval((int) graph.getVertices().get(i).getX() - 10, (int) graph.getVertices().get(i).getY() - 10, circleDiameter, circleDiameter);
        }

        ((Graphics2D) g).setStroke(new BasicStroke(2));

        for (Map.Entry<String, UnweightedDirectedGraph.Node> entry : graph.getVertices().entrySet()) {
            UnweightedDirectedGraph.Node n1 = entry.getValue();
            for (UnweightedDirectedGraph.Node n2 : n1.getEdges()) {
                g.drawLine((int) n1.getX(), (int) n1.getY(), (int) n2.getX(), (int) n2.getY());
            }
        }
        super.repaint();
        super.revalidate();
    }
}
