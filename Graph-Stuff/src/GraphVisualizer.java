import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class GraphVisualizer extends JPanel {

    public static final double cRep = 10000.0;
    public static final double cSpring = 15.0;
    public static final double kL = 50.0;
    public static final int overscaleWidth = 16;
    public static final int overscaleHeight = 39;
    public static final int screenWidth = 1000 - overscaleWidth;
    public static final int screenHeight = 800 - overscaleHeight;
    public static final int offset = 200;
    public static final int circleDiameter = 20;
    public static final int delay = 10;

    private final UnweightedDirectedGraph graph;

    private boolean optimized = false;

    private int iters = 1;

    public GraphVisualizer(UnweightedDirectedGraph graph) {
        this.graph = graph;
        for (int i = 0; i < graph.getNumNodes(); i++) {
            resetRandomPos(i);
        }
    }

    public void paintComponent(Graphics g) {
        drawGraph(g);
        if (!optimized) {
            optimizeGraphPositions(0.005, 1000000, g);
        }else{
            System.out.println("Done");
            for (int i = 0; i < graph.getNumNodes(); i++) {
                resetRandomPos(i);
            }
            iters = 1;
            optimized = false;
            maxForceChange = Integer.MAX_VALUE;
        }
    }

    private void resetRandomPos(int i){
        Random rand = new Random();
        //The random component will be > 0 so we don't need to worry about the < 0 case.
        graph.getVertices().get(i).setXY(Math.min(i * (50 - rand.nextInt(40)) + offset, screenWidth), Math.min(i * (50 - rand.nextInt(40)) + offset, screenHeight));
    }

    public void drawGraph(Graphics g) {
        ((Graphics2D) g).setStroke(new BasicStroke(4));
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, screenWidth, screenHeight);

        g.setColor(Color.LIGHT_GRAY);
        for (int i = 0; i < graph.getNumNodes(); i++) {
            g.fillOval((int) graph.getVertices().get(i).getX() - 10, (int) graph.getVertices().get(i).getY() - 10, circleDiameter, circleDiameter);
        }

        ((Graphics2D) g).setStroke(new BasicStroke(2));

        for (UnweightedDirectedGraph.Node n1 : graph.getVertices()) {
            for (UnweightedDirectedGraph.Node n2 : n1.getEdges()) {
                g.drawLine((int) n1.getX(), (int) n1.getY(), (int) n2.getX(), (int) n2.getY());
            }
        }
        super.repaint();
        super.revalidate();
    }

    public double coolingFunction(double t) {
        return 1.0;
    }

    private double maxForceChange = Integer.MAX_VALUE;
    public void optimizeGraphPositions(final double epsilon, final int maxIter, final Graphics g) {
        Map<UnweightedDirectedGraph.Node, Vector> forceMap = new HashMap<>();
        for (UnweightedDirectedGraph.Node n : graph.getVertices()) {
            forceMap.put(n, new Vector(0, 0));
        }

        if (iters > maxIter || maxForceChange < epsilon) {
            optimized = true;
            return;
        }

        for (UnweightedDirectedGraph.Node n : graph.getVertices()) {
            List<Vector> repList = new ArrayList<>();
            List<Vector> springList = new ArrayList<>();

            Set<UnweightedDirectedGraph.Node> adj = new HashSet<>();
            for (UnweightedDirectedGraph.Node e : n.getEdges()) {
                springList.add(attractiveForce(n.getX(), n.getY(), e.getX(), e.getY(), kL));
                adj.add(e);
            }

            for(UnweightedDirectedGraph.Node u : graph.getVertices()){
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

        for (UnweightedDirectedGraph.Node n : graph.getVertices()) {
            Vector f = forceMap.get(n);

            Vector cooledF = new Vector(f.getxMag() * coolingFunction(iters), f.getyMag() * coolingFunction(iters));
            Vector constrainedVector = constrainVector(n.getX(), n.getY(), cooledF);
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
        drawGraph(g);
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private Vector attractiveForce(double x1, double y1, double x2, double y2, final double l) {
        double distance = distance(x1, y1, x2, y2);
        double scalar = cSpring * Math.log(distance / l);
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

    //TODO FIX THIS - probably doesn't work because we have nothing pushing us back after we go off the screen
    //Takes a vector and ensures that it will not cause a vertex to go off the screen
    //If it does make us go off the screen we constrain it to the edge
    public Vector constrainVector(final double x, final double y, final Vector v){
        boolean out = false;
        //Check x constraint
        double constrainedX;
        double summedX = x + v.getxMag();
        if(summedX > screenWidth - circleDiameter){ //Right of the screen
            out = true;
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
            out = true;
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

    public static class Vector {
        private final double xMag, yMag;

        public Vector(double xMag, double yMag) {
            this.xMag = xMag;
            this.yMag = yMag;
        }

        public double getxMag() {
            return xMag;
        }

        @Override
        public String toString() {
            return "Vector{" +
                    "xMag=" + xMag +
                    ", yMag=" + yMag +
                    '}';
        }

        public double getyMag() {
            return yMag;
        }
    }

}
