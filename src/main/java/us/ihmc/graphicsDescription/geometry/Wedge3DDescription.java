package us.ihmc.graphicsDescription.geometry;

import us.ihmc.euclid.tools.EuclidCoreIOTools;

public class Wedge3DDescription implements GeometryDescription
{
   private String name = "wedge";
   private double sizeX;
   private double sizeY;
   private double sizeZ;

   public Wedge3DDescription()
   {
   }

   public Wedge3DDescription(double sizeX, double sizeY, double sizeZ)
   {
      this.sizeX = sizeX;
      this.sizeY = sizeY;
      this.sizeZ = sizeZ;
   }

   @Override
   public void setName(String name)
   {
      this.name = name;
   }

   public void setSizeX(double sizeX)
   {
      this.sizeX = sizeX;
   }

   public void setSizeY(double sizeY)
   {
      this.sizeY = sizeY;
   }

   public void setSizeZ(double sizeZ)
   {
      this.sizeZ = sizeZ;
   }

   public void setSize(double sizeX, double sizeY, double sizeZ)
   {
      this.sizeX = sizeX;
      this.sizeY = sizeY;
      this.sizeZ = sizeZ;
   }

   @Override
   public String getName()
   {
      return name;
   }

   public double getSizeX()
   {
      return sizeX;
   }

   public double getSizeY()
   {
      return sizeY;
   }

   public double getSizeZ()
   {
      return sizeZ;
   }

   @Override
   public String toString()
   {
      return EuclidCoreIOTools.getStringOf("Wedge: [name: " + name + ", size: (", ")]", ", ", sizeX, sizeY, sizeZ);
   }
}
