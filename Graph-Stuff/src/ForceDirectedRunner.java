import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ForceDirectedRunner extends GraphVisualizationRunner{

    private final double cRep;
    private final double cSpring;
    private final double kL;
    private final int screenWidth;
    private final int screenHeight;
    private final int circleDiameter;

    private final UnweightedDirectedGraph graph;
    private int iters = 1;
    private boolean optimized = false;

    public ForceDirectedRunner(final UnweightedDirectedGraph graph, final double cRep, final double cSpring, final double kL, final int screenWidth, final int screenHeight, final int circleDiameter){
        super(graph);
        this.graph = graph;
        this.cRep = cRep;
        this.cSpring = cSpring;
        this.kL = kL;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.circleDiameter = circleDiameter;
    }

    public double coolingFunction(double t) {
        return Math.min(1.0, 10.0/t);
    }

    private double maxForceChange = Integer.MAX_VALUE;

    @Override
    public void optimizeGraphPositions(final double epsilon, final int maxIter) {
        Map<UnweightedDirectedGraph.Node, Vector> forceMap = new HashMap<>();
        for (Map.Entry<String, UnweightedDirectedGraph.Node> entry : graph.getVertices().entrySet()) {
            forceMap.put(entry.getValue(), new Vector(0, 0));
        }

        if (iters > maxIter || maxForceChange < epsilon) {
            optimized = true;
            return;
        }

        for (Map.Entry<String, UnweightedDirectedGraph.Node> entry : graph.getVertices().entrySet()) {
            UnweightedDirectedGraph.Node n = entry.getValue();
            java.util.List<Vector> repList = new ArrayList<>();
            List<Vector> springList = new ArrayList<>();

            Set<UnweightedDirectedGraph.Node> adj = new HashSet<>();
            for (UnweightedDirectedGraph.Node e : n.getEdges()) {
                springList.add(attractiveForce(n.getX(), n.getY(), e.getX(), e.getY()));
                adj.add(e);
            }

            for(Map.Entry<String, UnweightedDirectedGraph.Node> entry1 : graph.getVertices().entrySet()){
                UnweightedDirectedGraph.Node u = entry1.getValue();
                if(!u.equals(n) && !adj.contains(u)){
                    repList.add(repulsiveForce(n.getX(), n.getY(), u.getX(), u.getY()));
                }
            }

            Vector sumRep = new Vector(0, 0);
            Vector sumSpring = new Vector(0, 0);
            for (Vector v : repList) {
                sumRep = new Vector(sumRep.getxMag() + v.getxMag(), sumRep.getyMag() + v.getyMag());
            }

            for (Vector v : springList) {
                sumSpring = new Vector(sumSpring.getxMag() + v.getxMag(), sumSpring.getyMag() + v.getyMag());
            }

            forceMap.put(n, new Vector(sumRep.getxMag() + sumSpring.getxMag(), sumRep.getyMag() + sumSpring.getyMag()));
        }

        for (Map.Entry<String, UnweightedDirectedGraph.Node> entry : graph.getVertices().entrySet()) {
            UnweightedDirectedGraph.Node n = entry.getValue();
            Vector f = forceMap.get(n);

            Vector cooledF = new Vector(f.getxMag() * coolingFunction(iters), f.getyMag() * coolingFunction(iters));
            Vector constrainedVector = constrainVector(n.getX(), n.getY(), cooledF);
            forceMap.put(n, constrainedVector);
            n.setXY(n.getX() + constrainedVector.getxMag(), n.getY() + constrainedVector.getyMag());
        }

        iters++;
        double max = 0;
        for (Map.Entry<UnweightedDirectedGraph.Node, Vector> entry : forceMap.entrySet()) {
            if (Math.abs(magnitude(entry.getValue())) > max) {
                max = magnitude(entry.getValue());
            }
        }
        maxForceChange = max;
//        System.out.printf("Iters: %d - Cooling Function: %f - MaxChange: %f\n", iters, coolingFunction(iters), maxForceChange);
    }

    private Vector attractiveForce(double x1, double y1, double x2, double y2) {
        double distance = distance(x1, y1, x2, y2);
        double scalar = cSpring * Math.log(distance / kL);
        Vector unitVector = getUnitVector(x1, y1, x2, y2);
        return new Vector(unitVector.getxMag() * scalar, unitVector.getyMag() * scalar);
    }

    private double distance(double x1, double y1, double x2, double y2) {
        double xDist = Math.abs(x2-x1);
        double yDist = Math.abs(y2-y1);
        return Math.sqrt((xDist) * (xDist) + (yDist) * (yDist));
    }

    public Vector getUnitVector(double x1, double y1, double x2, double y2) {
        double x = x2 - x1;
        double y = y2 - y1;
        double mag = magnitude(new Vector(x, y));
        return new Vector(x / mag, y / mag);
    }

    private double magnitude(Vector v) {
        double x = v.getxMag();
        double y = v.getyMag();
        return Math.sqrt(Math.abs(x * x) + Math.abs(y * y));
    }

    public Vector repulsiveForce(double x1, double y1, double x2, double y2) {
        Vector unitVector = getUnitVector(x2, y2, x1, y1);
        double distance = distance(x2, y2, x1, y1);
        double scalar = cRep / (distance * distance);
        return new Vector(unitVector.getxMag() * scalar, unitVector.getyMag() * scalar);
    }

    //Takes a vector and ensures that it will not cause a vertex to go off the screen
    //If it does make us go off the screen we constrain it to the edge
    public Vector constrainVector(final double x, final double y, final Vector v){
        //Check x constraint
        double constrainedX;
        double summedX = x + v.getxMag();
        if(summedX > screenWidth - circleDiameter){ //Right of the screen
            if(y > screenWidth - circleDiameter){
                constrainedX = -(x - (screenWidth - circleDiameter));
            }else{
                constrainedX = (screenWidth - circleDiameter) - x;
            }
        }else if(summedX < 0){ //Left of the screen
            constrainedX = -x;
        }else{
            constrainedX = v.getxMag();
        }

        //Check y constraint
        double constrainedY;
        double summedY = y + v.getyMag();
        if(summedY > screenHeight - circleDiameter){ //Bottom of the screen
            if(y > screenHeight - circleDiameter){
                constrainedY = -(y - (screenHeight - circleDiameter));
            }else{
                constrainedY = (screenHeight - circleDiameter) - y ;
            }
        }else if(summedY < 0){ //Top of the screen
            constrainedY = -y;
        }else{
            constrainedY = v.getyMag();
        }

//        if(out){
//            System.out.printf("X: %f - Y: %f - Constrained x: %f - Constrained y: %f\n", x, y, constrainedX, constrainedY);
//        }
        return new Vector(constrainedX, constrainedY);
    }

    public boolean isOptimized() {
        return optimized;
    }
}
