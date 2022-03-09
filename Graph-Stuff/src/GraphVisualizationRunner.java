public abstract class GraphVisualizationRunner {

    private final UnweightedDirectedGraph graph;

    public GraphVisualizationRunner(final UnweightedDirectedGraph graph){
        this.graph = graph;
    }

    public abstract void optimizeGraphPositions(final double epsilon, final int maxIter);
    public abstract boolean isOptimized();

    public UnweightedDirectedGraph getGraph(){
        return graph;
    }
}
