package us.ihmc.graphicsDescription.geometry;

import us.ihmc.euclid.tools.EuclidCoreIOTools;

public class PyramidBox3DDescription implements GeometryDescription
{
   private String name = "pyramidBox";
   private double boxSizeX;
   private double boxSizeY;
   private double boxSizeZ;
   private double pyramidHeight;

   public PyramidBox3DDescription()
   {
   }

   public PyramidBox3DDescription(double boxSizeX, double boxSizeY, double boxSizeZ, double pyramidHeight)
   {
      this.boxSizeX = boxSizeX;
      this.boxSizeY = boxSizeY;
      this.boxSizeZ = boxSizeZ;
      this.pyramidHeight = pyramidHeight;
   }

   @Override
   public void setName(String name)
   {
      this.name = name;
   }

   public void setBoxSizeX(double boxSizeX)
   {
      this.boxSizeX = boxSizeX;
   }

   public void setBoxSizeY(double boxSizeY)
   {
      this.boxSizeY = boxSizeY;
   }

   public void setBoxSizeZ(double boxSizeZ)
   {
      this.boxSizeZ = boxSizeZ;
   }

   public void setBoxSize(double sizeX, double sizeY, double sizeZ)
   {
      this.boxSizeX = sizeX;
      this.boxSizeY = sizeY;
      this.boxSizeZ = sizeZ;
   }

   public void setPyramidHeight(double pyramidHeight)
   {
      this.pyramidHeight = pyramidHeight;
   }

   @Override
   public String getName()
   {
      return name;
   }

   public double getBoxSizeX()
   {
      return boxSizeX;
   }

   public double getBoxSizeY()
   {
      return boxSizeY;
   }

   public double getBoxSizeZ()
   {
      return boxSizeZ;
   }

   public double getPyramidHeight()
   {
      return pyramidHeight;
   }

   @Override
   public String toString()
   {
      return EuclidCoreIOTools.getStringOf("Wedge: [name: " + name
            + ", box size: (", "), pryamid height: " + pyramidHeight + "]", ", ", boxSizeX, boxSizeY, boxSizeZ);
   }
}
