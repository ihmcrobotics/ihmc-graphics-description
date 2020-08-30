package us.ihmc.graphicsDescription.geometry;

import us.ihmc.euclid.tools.EuclidCoreIOTools;

public class TruncatedCone3DDescription implements GeometryDescription
{
   private String name = "truncatedCone";
   private double height;
   private double topRadiusX;
   private double topRadiusY;
   private double baseRadiusX;
   private double baseRadiusY;
   private int resolution = 64;

   public TruncatedCone3DDescription()
   {
   }

   public TruncatedCone3DDescription(double height, double topRadius, double baseRadius)
   {
      this.height = height;
      this.topRadiusX = topRadius;
      this.topRadiusY = topRadius;
      this.baseRadiusX = baseRadius;
      this.baseRadiusY = baseRadius;
   }

   public TruncatedCone3DDescription(double height, double topRadius, double baseRadius, int resolution)
   {
      this.height = height;
      this.topRadiusX = topRadius;
      this.topRadiusY = topRadius;
      this.baseRadiusX = baseRadius;
      this.baseRadiusY = baseRadius;
      this.resolution = resolution;
   }

   public TruncatedCone3DDescription(double height, double topRadiusX, double topRadiusY, double baseRadiusX, double baseRadiusY)
   {
      this.height = height;
      this.topRadiusX = topRadiusX;
      this.topRadiusY = topRadiusY;
      this.baseRadiusX = baseRadiusX;
      this.baseRadiusY = baseRadiusY;
   }

   public TruncatedCone3DDescription(double height, double topRadiusX, double topRadiusY, double baseRadiusX, double baseRadiusY, int resolution)
   {
      this.height = height;
      this.topRadiusX = topRadiusX;
      this.topRadiusY = topRadiusY;
      this.baseRadiusX = baseRadiusX;
      this.baseRadiusY = baseRadiusY;
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

   public void setTopRadiusX(double topRadiusX)
   {
      this.topRadiusX = topRadiusX;
   }

   public void setTopRadiusY(double topRadiusY)
   {
      this.topRadiusY = topRadiusY;
   }

   public void setTopRadii(double topRadius)
   {
      setTopRadii(topRadius, topRadius);
   }

   public void setTopRadii(double topRadiusX, double topRadiusY)
   {
      this.topRadiusX = topRadiusX;
      this.topRadiusY = topRadiusY;
   }

   public void setBaseRadiusX(double baseRadiusX)
   {
      this.baseRadiusX = baseRadiusX;
   }

   public void setBaseRadiusY(double baseRadiusY)
   {
      this.baseRadiusY = baseRadiusY;
   }

   public void setBaseRadii(double baseRadius)
   {
      setBaseRadii(baseRadius, baseRadius);
   }

   public void setBaseRadii(double baseRadiusX, double baseRadiusY)
   {
      this.baseRadiusX = baseRadiusX;
      this.baseRadiusY = baseRadiusY;
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

   public boolean isRegular()
   {
      return topRadiusX == topRadiusY && baseRadiusX == baseRadiusY;
   }

   public double getTopRadiusX()
   {
      return topRadiusX;
   }

   public double getTopRadiusY()
   {
      return topRadiusY;
   }

   public double getBaseRadiusX()
   {
      return baseRadiusX;
   }

   public double getBaseRadiusY()
   {
      return baseRadiusY;
   }

   public int getResolution()
   {
      return resolution;
   }

   @Override
   public String toString()
   {
      return "Truncated Cone: [name: " + name + ", height: " + String.format(EuclidCoreIOTools.DEFAULT_FORMAT, height)
            + EuclidCoreIOTools.getStringOf(", top radii: (", ")", ", ", topRadiusX, topRadiusY)
            + EuclidCoreIOTools.getStringOf(", base radii: (", ")", ", ", baseRadiusX, baseRadiusY) + ", resolution: " + resolution + "]";
   }
}
