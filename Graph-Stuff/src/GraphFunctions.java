import java.util.ArrayList;
import java.util.List;

public class GraphFunctions {

    public static int computeNumEdgeIntersections(UnweightedDirectedGraph g){
        final List<LineSegment> equations = new ArrayList<>();
        for(String s : g.getEdges()){
           String[] splitS = s.split(",");
           UnweightedDirectedGraph.Node n1 = g.getVertex(splitS[0]);
           UnweightedDirectedGraph.Node n2 = g.getVertex(splitS[1]);
           double x1 = n1.getX();
           double y1 = n1.getY();
           double x2 = n2.getX();
           double y2 = n2.getY();
           equations.add(new LineSegment(x1, y1, x2, y2));
        }

        int intersections = 0;
        for(int i = 0; i < equations.size(); i++){
            for(int j = i + 1; j < equations.size(); j++){
                if(equations.get(i).intersects(equations.get(j))){
                    intersections++;
                }
            }
        }

        return intersections;
    }

    private static double distance(double x1, double y1, double x2, double y2) {
        double xDist = Math.abs(x2-x1);
        double yDist = Math.abs(y2-y1);
        return Math.sqrt((xDist) * (xDist) + (yDist) * (yDist));
    }

    //(y1-y2)x+(x2-x1)y=x2y1-x1y2
    public static class LineSegment{
        private final double x1, y1, x2, y2;
        private final double A, B, C;

        public LineSegment(double x1, double y1, double x2, double y2) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;

            //Find Standard form of line
            double m = (y2 - y1) / (x2 - x1);
            double b = (-m * x1) + y1;
            A = m;
            B = -1;
            C = -b;
        }

        public boolean intersects(LineSegment l){
            double xIntersect = (C - l.C) / (A - l.A);
            double yIntersect = -(C/(A * xIntersect));
            boolean firstEndpoints = distance(xIntersect, yIntersect, x1, y1) + distance(xIntersect, yIntersect, x2, y2) <= distance(x1, y1, x2, y2);
            boolean secondEndpoints = distance(xIntersect, yIntersect, l.x1, l.y1) + distance(xIntersect, yIntersect, l.x2, l.y2) <= distance(l.x1, l.y1, l.x2, l.y2);
            return firstEndpoints && secondEndpoints;
        }

        public double getX1() {
            return x1;
        }

        public double getY1() {
            return y1;
        }

        public double getX2() {
            return x2;
        }

        public double getY2() {
            return y2;
        }
    }
}
