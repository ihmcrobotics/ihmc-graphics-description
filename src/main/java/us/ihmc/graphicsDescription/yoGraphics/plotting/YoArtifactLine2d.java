package us.ihmc.graphicsDescription.yoGraphics.plotting;

import java.awt.BasicStroke;
import java.awt.Color;

import us.ihmc.euclid.geometry.Line2D;
import us.ihmc.euclid.tuple2D.Point2D;
import us.ihmc.graphicsDescription.plotting.Graphics2DAdapter;
import us.ihmc.graphicsDescription.plotting.Plotter2DAdapter;
import us.ihmc.yoVariables.registry.YoVariableRegistry;
import us.ihmc.yoVariables.variable.YoFrameLine2D;

public class YoArtifactLine2d extends YoArtifact
{
   private static final BasicStroke STROKE = new BasicStroke(2);

   private final YoFrameLine2D yoFrameLine2d;

   private final Line2D tempLine = new Line2D();

   public YoArtifactLine2d(String name, YoFrameLine2D yoFrameLine2d, Color color)
   {
      super(name, new double[0], color, yoFrameLine2d.getYoPointX(), yoFrameLine2d.getYoPointY(), yoFrameLine2d.getYoDirectionX(),
            yoFrameLine2d.getYoDirectionY());
      this.yoFrameLine2d = yoFrameLine2d;
   }

   @Override
   public void draw(Graphics2DAdapter graphics)
   {
      graphics.setColor(color);
      graphics.setStroke(STROKE);

      tempLine.set(yoFrameLine2d);
      graphics.drawLine(tempLine);
   }

   @Override
   public void drawLegend(Plotter2DAdapter graphics, Point2D origin)
   {
      graphics.setColor(color);
      graphics.setStroke(STROKE);

      graphics.drawLineSegment(graphics.getScreenFrame(), -20.0 + origin.getX(), -5.0 + origin.getY(), 20.0 + origin.getX(), 5.0 + origin.getY());
   }

   @Override
   public void drawHistoryEntry(Graphics2DAdapter graphics, double[] entry)
   {
      graphics.setColor(color);
      graphics.setStroke(STROKE);

      tempLine.set(entry[0], entry[1], entry[2], entry[3]);
      graphics.drawLine(tempLine);
   }

   @Override
   public YoArtifactLine2d duplicate(YoVariableRegistry newRegistry)
   {
      return new YoArtifactLine2d(getName(), yoFrameLine2d.duplicate(newRegistry), color);
   }
}
