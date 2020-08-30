package us.ihmc.graphicsDescription.geometry;

import us.ihmc.euclid.tools.EuclidCoreIOTools;

public class ArcTorus3DDescription implements GeometryDescription
{
   private String name = "arcTorus";
   private double startAngle;
   private double endAngle;
   private double majorRadius;
   private double minorRadius;
   private int resolution = 64;

   public ArcTorus3DDescription()
   {
   }

   public ArcTorus3DDescription(double startAngle, double endAngle, double majorRadius, double minorRadius)
   {
      this.startAngle = startAngle;
      this.endAngle = endAngle;
      this.majorRadius = majorRadius;
      this.minorRadius = minorRadius;
   }

   public ArcTorus3DDescription(double startAngle, double endAngle, double majorRadius, double minorRadius, int resolution)
   {
      this.startAngle = startAngle;
      this.endAngle = endAngle;
      this.majorRadius = majorRadius;
      this.minorRadius = minorRadius;
      this.resolution = resolution;
   }

   @Override
   public void setName(String name)
   {
      this.name = name;
   }

   public void setStartAngle(double startAngle)
   {
      this.startAngle = startAngle;
   }

   public void setEndAngle(double endAngle)
   {
      this.endAngle = endAngle;
   }

   public void setMajorRadius(double majorRadius)
   {
      this.majorRadius = majorRadius;
   }

   public void setMinorRadius(double minorRadius)
   {
      this.minorRadius = minorRadius;
   }

   public void setResolution(int resolution)
   {
      this.resolution = resolution;
   }

   @Override
   public String getName()
   {
      return name;
   }

   public double getStartAngle()
   {
      return startAngle;
   }

   public double getEndAngle()
   {
      return endAngle;
   }

   public double getMajorRadius()
   {
      return majorRadius;
   }

   public double getMinorRadius()
   {
      return minorRadius;
   }

   public int getResolution()
   {
      return resolution;
   }

   @Override
   public String toString()
   {
      return "Arc-Torus: [name: " + name + EuclidCoreIOTools.getStringOf(", radii: (", ")", ", ", majorRadius, minorRadius) + ", start angle: "
            + String.format(EuclidCoreIOTools.DEFAULT_FORMAT, startAngle) + ", end angle: " + String.format(EuclidCoreIOTools.DEFAULT_FORMAT, endAngle)
            + ", resolution: " + resolution + "]";
   }
}
