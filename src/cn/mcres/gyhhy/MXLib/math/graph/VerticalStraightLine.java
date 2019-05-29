package cn.mcres.gyhhy.MXLib.math.graph;

/**
 * Vertical Straight Line 竖直的直线<br>
 * This straight line NO has Y value 这条直线不存在y值<br>
 * (This straight ingore Y-axis 这条直线忽略了Y值)<br>
 * The point obtained by this line is only the Y value is different, X/Z is the
 * same<br>
 * 通过这条直线获取的点只有 Y值 是不一样的, X/Z 是相同的
 */
public class VerticalStraightLine implements StraightLine {

    private final double x, z;

    public VerticalStraightLine(double x, double z) {
        this.x = x;
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public double getZ() {
        return z;
    }

    @Override
    public double distance(Point p) {
        double x = p.getXAsDouble() - this.x;
        double z = p.getZAsDouble() - this.z;
        if (x == 0) {
            return Math.abs(z);
        }
        if (z == 0) {
            return Math.abs(x);
        }
        return Math.sqrt(x * x + z * z);
    }

    @Override
    public Point getRandPoint() {
        return new DoublePoint(x, (Math.random() - 0.5) * 327986484L, z);
    }

    @Override
    public Point getPoint(int x) {
        return getRandPoint();
    }

    @Override
    public Point getPoint(double x) {
        return getRandPoint();
    }

}
