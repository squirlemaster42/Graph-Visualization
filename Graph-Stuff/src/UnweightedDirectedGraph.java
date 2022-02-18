import java.util.*;

public class UnweightedDirectedGraph {

    private final Map<String, Node> vertex;
    private final List<String> edges;
    private int numNodes = 0;

    public UnweightedDirectedGraph() {
        this.vertex = new HashMap<>();
        this.edges = new ArrayList<>();
    }

    public void addVertex(final Node n) {
        vertex.put(n.name, n);
        numNodes++;
    }

    public int getNumNodes(){
        return numNodes;
    }

    public Node getVertex(String name){
        return vertex.get(name);
    }

    public Map<String, Node> getVertices(){
        return vertex;
    }

    public void addEdge(final Node start, final Node end){
        start.addEdge(end);
        end.addEdge(start);
        edges.add(start.name + "," + end.name);
    }

    public List<String> getEdges(){
        return edges;
    }

    public static class Node{

        private final String name;
        private final List<Node> edges;
        private double x, y;

        public Node(final String name){
            this.name = name;
            this.edges = new ArrayList<>();
        }

        public void addEdge(Node e){
            edges.add(e);
        }

        public List<Node> getEdges(){
            return edges;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "name='" + name + '\'' +
                    ", x=" + x +
                    ", y=" + y +
                    '}';
        }

        public void setXY(final double x, final double y){
            this.x = x;
            this.y = y;
        }

        public String getName() {
            return name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return name.equals(node.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }
    }
}
