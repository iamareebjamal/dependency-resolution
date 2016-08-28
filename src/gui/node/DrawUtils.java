package gui.node;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DrawUtils {
    private Graphics2D g;
    private int radius = 30;

    public DrawUtils(Graphics2D graphics2D) {
        g = graphics2D;
    }

    public static Color parseColor(String colorStr) {
        return new Color(
                Integer.valueOf(colorStr.substring(1, 3), 16),
                Integer.valueOf(colorStr.substring(3, 5), 16),
                Integer.valueOf(colorStr.substring(5, 7), 16));
    }

    public static java.util.List<Point> getPolygonPoints(Point centre, int radius, int n) {
        List<Point> points = new ArrayList<>();

        for (int i = 1; i <= n; i++) {
            double x = radius * Math.cos(2 * Math.PI * i / n) + centre.getX();
            double y = radius * Math.sin(2 * Math.PI * i / n) + centre.getY();

            points.add(new Point((int) x, (int) y));
        }

        return points;
    }

    public static List<Point> getCircleLineIntersectionPoint(Point pointA,
                                                             Point pointB, Point center, int radius) {
        double baX = pointB.x - pointA.x;
        double baY = pointB.y - pointA.y;
        double caX = center.x - pointA.x;
        double caY = center.y - pointA.y;

        double a = baX * baX + baY * baY;
        double bBy2 = baX * caX + baY * caY;
        double c = caX * caX + caY * caY - radius * radius;

        double pBy2 = bBy2 / a;
        double q = c / a;

        double disc = pBy2 * pBy2 - q;
        if (disc < 0) {
            return Collections.emptyList();
        }
        // if disc == 0 ... dealt with later
        double tmpSqrt = Math.sqrt(disc);
        double abScalingFactor1 = -pBy2 + tmpSqrt;
        double abScalingFactor2 = -pBy2 - tmpSqrt;

        Point p1 = new Point((int) (pointA.x - baX * abScalingFactor1), (int) (pointA.y
                - baY * abScalingFactor1));
        if (disc == 0) { // abScalingFactor1 == abScalingFactor2
            return Collections.singletonList(p1);
        }
        Point p2 = new Point((int) (pointA.x - baX * abScalingFactor2), (int) (pointA.y
                - baY * abScalingFactor2));
        return Arrays.asList(p1, p2);
    }

    public void drawHalo(Point point) {
        int radius = 300;
        g.setColor(parseColor("#ef9a9a"));
        g.fillOval(point.x - radius, point.y - radius, 2 * radius, 2 * radius);
        radius -= 5;
        g.setColor(parseColor("#ffebee"));
        g.fillOval(point.x - radius, point.y - radius, 2 * radius, 2 * radius);
    }

    public void drawSmallHalo(Point point) {
        int radius = 40;
        g.setColor(parseColor("#ef9a9a"));
        g.fillOval(point.x - radius, point.y - radius, 2 * radius, 2 * radius);
        radius -= 20;
        g.setColor(parseColor("#e57373"));
        g.fillOval(point.x - radius, point.y - radius, 2 * radius, 2 * radius);

    }

    public void drawHighlightedNode(Point point){
        g.setColor(parseColor("#AA00FF"));
        radius+=7;
        g.fillOval(point.x - radius, point.y - radius, 2 * radius, 2 * radius);
        radius-=7;
    }

    public void drawHighlightedEdge(Point from, Point to) {
        g.setColor(parseColor("#8E24AA"));
        drawBaseEdge(from, to);
    }

    public void drawEdge(Point from, Point to) {
        g.setColor(parseColor("#555555"));
        drawBaseEdge(from, to);
    }

    private void drawBaseEdge(Point from, Point to) {
        g.setStroke(new BasicStroke(3));
        drawArrowLine(from, to, 20, 8);
    }

    public void drawNode(Point point, String text) {
        drawNode(point, text, parseColor("#f44336"), parseColor("#ffcdd2"));
    }

    public void drawNode(Point point, String text, Color dark, Color light) {
        g.setColor(dark);
        g.fillOval(point.x - radius, point.y - radius, 2 * radius, 2 * radius);

        radius -= 5;
        g.setColor(light);
        g.fillOval(point.x - radius, point.y - radius, 2 * radius, 2 * radius);

        radius += 5;
        g.setColor(dark);
        drawCentreText(text, point.x, point.y);
    }

    public void drawCentreText(String text, int x, int y) {
        int size = (int) (radius / 1.5);

        size -= text.length() + 1;

        Font myFont = new Font("Courier New", Font.PLAIN, size);
        g.setFont(myFont);
        FontMetrics fm = g.getFontMetrics();
        double t_width = fm.getStringBounds(text, g).getWidth();
        g.drawString(text, (int) (x - t_width / 2), (y + fm.getMaxAscent() / 2));
    }

    private void drawArrowLine(Point a, Point bOld, int d, int h) {
        Point b = getCircleLineIntersectionPoint(a, bOld, bOld, radius).get(0);

        int dx = b.x - a.x, dy = b.y - a.y;
        double D = Math.sqrt(dx * dx + dy * dy);
        double xm = D - d, xn = xm, ym = h, yn = -h, x;
        double sin = dy / D, cos = dx / D;

        x = xm * cos - ym * sin + a.x;
        ym = xm * sin + ym * cos + a.y;
        xm = x;

        x = xn * cos - yn * sin + a.x;
        yn = xn * sin + yn * cos + a.y;
        xn = x;

        int[] xpoints = {b.x, (int) xm, (int) xn};
        int[] ypoints = {b.y, (int) ym, (int) yn};

        g.fillPolygon(xpoints, ypoints, 3);
        b = getCircleLineIntersectionPoint(a, bOld, bOld, radius + h).get(0);
        g.drawLine(a.x, a.y, b.x, b.y);
    }

}
