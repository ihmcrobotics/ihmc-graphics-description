package us.ihmc.graphicsDescription.geometry;

import us.ihmc.euclid.tools.EuclidCoreIOTools;

public class Box3DDescription implements GeometryDescription
{
   private String name = "box";
   private double sizeX;
   private double sizeY;
   private double sizeZ;
   private boolean centered = true;

   public Box3DDescription()
   {
   }

   public Box3DDescription(double sizeX, double sizeY, double sizeZ)
   {
      this(sizeX, sizeY, sizeZ, true);
   }

   public Box3DDescription(double sizeX, double sizeY, double sizeZ, boolean centered)
   {
      this.sizeX = sizeX;
      this.sizeY = sizeY;
      this.sizeZ = sizeZ;
      this.centered = centered;
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

   public void setCentered(boolean centered)
   {
      this.centered = centered;
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

   public boolean isCentered()
   {
      return centered;
   }

   @Override
   public String toString()
   {
      return EuclidCoreIOTools.getStringOf("Box: [name: " + name + ", size: (", ")]", ", ", sizeX, sizeY, sizeZ);
   }
}
