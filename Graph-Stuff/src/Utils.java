import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Utils {

    public static int generateConstrainedRandomInt(final Random rand, final int max){
        return Math.min(rand.nextInt(max), max);
    }

    public static boolean inBounds(final Rectangle bounds, final Point point){
        return false;
    }
    public static int computeNumEdgeIntersections(UnweightedDirectedGraph g) {
        final List<LineSegment> equations = new ArrayList<>();
        for (String s : g.getEdges()) {
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
        for (int i = 0; i < equations.size(); i++) {
            for (int j = i + 1; j < equations.size(); j++) {
                if (equations.get(i).intersects(equations.get(j))) {
                    //System.out.printf("Line between x: %f y: %f and x: %f y: %f intersections line between x: %f y: %f and x: %f y: %f\n",
                    //        equations.get(i).x1, equations.get(i).y1,  equations.get(i).x2, equations.get(i).y2,
                    //        equations.get(j).x1, equations.get(j).y1,  equations.get(j).x2, equations.get(j).y2);
                    intersections++;
                }
            }
        }

        return intersections;
    }

    //(y1-y2)x+(x2-x1)y=x2y1-x1y2
    public static class LineSegment {
        private final double x1, y1, x2, y2;

        public LineSegment(double x1, double y1, double x2, double y2) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        }

        public boolean onSegment(Point p, Point q, Point r) {
            return q.x <= Math.max(p.x, r.x) && q.x >= Math.min(p.x, r.x) && q.y <= Math.max(p.y, r.y) && q.y >= Math.min(p.y, r.y);
        }

        public int orientation(Point p, Point q, Point r) {
            // See https://www.geeksforgeeks.org/orientation-3-ordered-points/
            // for details of below formula.
            double val = (q.y - p.y) * (r.x - q.x) - (q.x - p.x) * (r.y - q.y);

            if (val == 0){
                return 0; // collinear
            }else{
                return (val > 0)? 1: 2; // clock or counterclock wise
            }
        }

        public boolean isVertexIntersection(Point p1, Point q1, Point p2, Point q2){
            return p1.x == p2.x || p1.x == q2.x || q1.x == p2.x || q1.x == q2.x ||
                    p1.y == p2.y || p1.y == q2.y || q1.y == p2.y || q1.y == q2.y;
        }

        public boolean intersects(LineSegment l) {
            Point p1 = new Point(x1, y1);
            Point q1 = new Point(x2, y2);
            Point p2 = new Point(l.x1, l.y1);
            Point q2 = new Point(l.x2, l.y2);

            //Exclude Intersections at Vertices
            if(isVertexIntersection(p1, q1, p2, q2)){
                return false;
            }

            int o1 = orientation(p1, q1, p2);
            int o2 = orientation(p1, q1, q2);
            int o3 = orientation(p2, q2, p1);
            int o4 = orientation(p2, q2, q1);

            // General case
            if (o1 != o2 && o3 != o4){
                return true;
            }

            // Special Cases
            // p1, q1 and p2 are collinear and p2 lies on segment p1q1
            if (o1 == 0 && onSegment(p1, p2, q1)){
                return true;
            }

            // p1, q1 and q2 are collinear and q2 lies on segment p1q1
            if (o2 == 0 && onSegment(p1, q2, q1)){
                return true;
            }

            // p2, q2 and p1 are collinear and p1 lies on segment p2q2
            if (o3 == 0 && onSegment(p2, p1, q2)){
                return true;
            }

            // p2, q2 and q1 are collinear and q1 lies on segment p2q2
            return o4 == 0 && onSegment(p2, q1, q2);// Doesn't fall in any of the above cases
        }
    }

    public static class Point{
        private final double x, y;

        public Point(final double x, final double y){
            this.x = x;
            this.y = y;
        }
    }
}
