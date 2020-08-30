package us.ihmc.graphicsDescription.geometry;

import us.ihmc.euclid.tools.EuclidCoreIOTools;

public class Torus3DDescription implements GeometryDescription
{
   private String name = "torus";
   private double majorRadius;
   private double minorRadius;
   private int resolution = 64;

   public Torus3DDescription()
   {
   }

   public Torus3DDescription(double majorRadius, double minorRadius)
   {
      this.majorRadius = majorRadius;
      this.minorRadius = minorRadius;
   }

   public Torus3DDescription(double majorRadius, double minorRadius, int resolution)
   {
      this.majorRadius = majorRadius;
      this.minorRadius = minorRadius;
      this.resolution = resolution;
   }

   @Override
   public void setName(String name)
   {
      this.name = name;
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
      return "Torus: [name: " + name + EuclidCoreIOTools.getStringOf(", radii: (", ")", ", ", majorRadius, minorRadius) + ", resolution: " + resolution + "]";
   }
}
