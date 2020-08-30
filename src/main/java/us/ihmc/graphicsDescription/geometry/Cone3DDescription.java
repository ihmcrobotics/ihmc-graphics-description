package us.ihmc.graphicsDescription.geometry;

import us.ihmc.euclid.tools.EuclidCoreIOTools;

public class Cone3DDescription implements GeometryDescription
{
   private String name = "cone";
   private double height;
   private double radius;
   private int resolution = 64;

   public Cone3DDescription()
   {
   }

   public Cone3DDescription(double height, double radius)
   {
      this.height = height;
      this.radius = radius;
   }

   public Cone3DDescription(double height, double radius, int resolution)
   {
      this.height = height;
      this.radius = radius;
      this.resolution = resolution;
   }

   @Override
   public void setName(String name)
   {
      this.name = name;
   }

   public void setHeight(double height)
   {
      this.height = height;
   }

   public void setRadius(double radius)
   {
      this.radius = radius;
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

   public double getHeight()
   {
      return height;
   }

   public double getRadius()
   {
      return radius;
   }

   public int getResolution()
   {
      return resolution;
   }

   @Override
   public String toString()
   {
      return "Cone: [name: " + name + ", height: " + String.format(EuclidCoreIOTools.DEFAULT_FORMAT, height) + ", radius: "
            + String.format(EuclidCoreIOTools.DEFAULT_FORMAT, radius) + ", resolution: " + resolution + "]";
   }
}
