package us.ihmc.graphicsDescription.geometry;

import us.ihmc.euclid.tools.EuclidCoreIOTools;

public class HemiEllipsoid3DDescription implements GeometryDescription
{
   private String name = "hemiEllipsoid";
   private double radiusX;
   private double radiusY;
   private double radiusZ;
   private int resolution = 64;

   public HemiEllipsoid3DDescription()
   {
   }

   public HemiEllipsoid3DDescription(double radiusX, double radiusY, double radiusZ)
   {
      this.radiusX = radiusX;
      this.radiusY = radiusY;
      this.radiusZ = radiusZ;
   }

   public HemiEllipsoid3DDescription(double radiusX, double radiusY, double radiusZ, int resolution)
   {
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
      return EuclidCoreIOTools.getStringOf("Hemi-ellipsoid: [name: " + name
            + ", radii: (", "), resolution: " + resolution + "]", ", ", radiusX, radiusY, radiusZ);
   }
}
