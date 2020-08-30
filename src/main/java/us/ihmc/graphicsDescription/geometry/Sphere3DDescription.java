package us.ihmc.graphicsDescription.geometry;

import us.ihmc.euclid.tools.EuclidCoreIOTools;

public class Sphere3DDescription implements GeometryDescription
{
   private String name = "sphere";
   private double radius;
   private int resolution = 64;

   public Sphere3DDescription()
   {
   }

   public Sphere3DDescription(double radius)
   {
      this.radius = radius;
   }

   public Sphere3DDescription(double radius, int resolution)
   {
      this.radius = radius;
      this.resolution = resolution;
   }

   @Override
   public void setName(String name)
   {
      this.name = name;
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
      return "Sphere: [name: " + name + ", radius: " + String.format(EuclidCoreIOTools.DEFAULT_FORMAT, radius) + ", resolution: " + resolution + "]";
   }
}
