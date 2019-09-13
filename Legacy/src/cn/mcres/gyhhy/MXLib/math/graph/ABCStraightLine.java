package cn.mcres.gyhhy.MXLib.math.graph;

/**
 * Ax + By + C = 0
 */
public class ABCStraightLine implements StraightLine {

    /**
     * 直线方程中 <br>
     * A = y2 - y1,<br>
     * B = x1 - x2, <br>
     * C = (x2 * y1) - (x1 * y2)<br><br>
     * Copy from <a href="https://blog.csdn.net/ffgcc/article/details/79941512">
     * https://blog.csdn.net/ffgcc/article/details/79941512</a>
     */
    public static double distance(double A, double B, double C, Point p) {

        double x0 = p.getXAsDouble();
        double y0 = p.getYAsDouble();

        return (Math.abs(A * x0 + B * y0 + C)) / (Math.sqrt(Math.pow(A, 2) + Math.pow(B, 2)));
    }
    private final double a, b, c;

    public ABCStraightLine(double A, double B, double C) {
        this.a = A;
        this.b = B;
        this.c = C;
    }

    public double getA() {
        return a;
    }

    public double getB() {
        return b;
    }

    public double getC() {
        return c;
    }

    @Override
    public Point getPoint(double x) {
        double y;
        if (a == 0) {
            y = -c / b;
        } else {
            y = (-c - (a * x)) / b;
        }
        return new DoublePoint(x, y);
    }

    @Override
    public double distance(Point o) {
        return distance(a, b, c, o);
    }

    @Override
    public double distance(Distancer d) {
        if (d instanceof Point) {
            return distance((Point) d);
        }
        if (d instanceof StraightLine) {
            ABCStraightLine ac = ((StraightLine) d).toABCStraightLine();
            double aax;
            if ((aax = a / ac.getA()) == b / ac.getB()) {
                if (aax == c / ac.getC()) {
                    return Math.abs(c - (ac.getC() * aax)) / Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
                }
                return 0;
            }
        }
        return Double.NaN;
    }

    @Override
    public ABCStraightLine toABCStraightLine() {
        return this;
    }
}
