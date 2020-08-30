package us.ihmc.graphicsDescription.geometry;

import us.ihmc.euclid.tools.EuclidCoreIOTools;

public class Capsule3DDescription implements GeometryDescription
{
   private String name = "capsule";
   private double height;
   private double radiusX;
   private double radiusY;
   private double radiusZ;
   private int resolution = 64;

   public Capsule3DDescription()
   {
   }

   public Capsule3DDescription(double height, double radius)
   {
      this(height, radius, radius, radius);
   }

   public Capsule3DDescription(double height, double radiusX, double radiusY, double radiusZ)
   {
      this.height = height;
      this.radiusX = radiusX;
      this.radiusY = radiusY;
      this.radiusZ = radiusZ;
   }

   public Capsule3DDescription(double height, double radius, int resolution)
   {
      this(height, radius, radius, radius, resolution);
   }

   public Capsule3DDescription(double height, double radiusX, double radiusY, double radiusZ, int resolution)
   {
      this.height = height;
      this.radiusX = radiusX;
      this.radiusY = radiusY;
      this.radiusZ = radiusZ;
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

   public boolean isRegular()
   {
      return radiusX == radiusY && radiusX == radiusZ;
   }

   public void setRadiusX(double radiusX)
   {
      this.radiusX = radiusX;
   }

   public void setRadiusY(double radiusY)
   {
      this.radiusY = radiusY;
   }

   public void setRadiusZ(double radiusZ)
   {
      this.radiusZ = radiusZ;
   }

   public void setRadii(double radius)
   {
      setRadii(radius, radius, radius);
   }

   public void setRadii(double radiusX, double radiusY, double radiusZ)
   {
      this.radiusX = radiusX;
      this.radiusY = radiusY;
      this.radiusZ = radiusZ;
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

   public double getRadiusX()
   {
      return radiusX;
   }

   public double getRadiusY()
   {
      return radiusY;
   }

   public double getRadiusZ()
   {
      return radiusZ;
   }

   public int getResolution()
   {
      return resolution;
   }

   @Override
   public String toString()
   {
      return "Capsule: [name: " + name + ", height: " + String.format(EuclidCoreIOTools.DEFAULT_FORMAT, height)
            + EuclidCoreIOTools.getStringOf(", radii: (", ")", ", ", radiusX, radiusY, radiusZ) + ", resolution: " + resolution + "]";
   }
}
