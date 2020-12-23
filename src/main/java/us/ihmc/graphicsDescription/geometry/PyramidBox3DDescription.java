package us.ihmc.graphicsDescription.geometry;

import us.ihmc.euclid.tools.EuclidCoreIOTools;
import us.ihmc.euclid.tools.EuclidHashCodeTools;

/**
 * Description for creating a 3D pyramid-box, i.e. a 3D box which bottom and top faces are each
 * extended with a pyramid.
 */
public class PyramidBox3DDescription implements GeometryDescription
{
   private String name = "pyramidBox";
   private double boxSizeX;
   private double boxSizeY;
   private double boxSizeZ;
   private double pyramidHeight;

   /**
    * Creates an empty description. The parameters have to be all set before this description can be
    * used.
    */
   public PyramidBox3DDescription()
   {
   }

   /**
    * Creates and initializes a description for a 3D pyramid-box.
    *
    * @param boxSizeX      the size of the box along the x-axis.
    * @param boxSizeY      the size of the box along the y-axis.
    * @param boxSizeZ      the size of the box along the z-axis.
    * @param pyramidHeight the height for each pyramid.
    */
   public PyramidBox3DDescription(double boxSizeX, double boxSizeY, double boxSizeZ, double pyramidHeight)
   {
      this.boxSizeX = boxSizeX;
      this.boxSizeY = boxSizeY;
      this.boxSizeZ = boxSizeZ;
      this.pyramidHeight = pyramidHeight;
   }

   public PyramidBox3DDescription(PyramidBox3DDescription other)
   {
      name = other.name;
      boxSizeX = other.boxSizeX;
      boxSizeY = other.boxSizeY;
      boxSizeZ = other.boxSizeZ;
      pyramidHeight = other.pyramidHeight;
   }

   /** {@inheritDoc} */
   @Override
   public void setName(String name)
   {
      this.name = name;
   }

   /**
    * Sets the size along the x-axis for the box.
    *
    * @param boxSizeX the box's size along the x-axis.
    */
   public void setBoxSizeX(double boxSizeX)
   {
      this.boxSizeX = boxSizeX;
   }

   /**
    * Sets the size along the y-axis for the box.
    *
    * @param boxSizeY the box's size along the y-axis.
    */
   public void setBoxSizeY(double boxSizeY)
   {
      this.boxSizeY = boxSizeY;
   }

   /**
    * Sets the size along the z-axis for the box.
    *
    * @param boxSizeZ the box's size along the z-axis.
    */
   public void setBoxSizeZ(double boxSizeZ)
   {
      this.boxSizeZ = boxSizeZ;
   }

   /**
    * Sets the size of the box.
    *
    * @param sizeX the size of the box along the x-axis.
    * @param sizeY the size of the box along the y-axis.
    * @param sizeZ the size of the box along the z-axis.
    */
   public void setBoxSize(double sizeX, double sizeY, double sizeZ)
   {
      boxSizeX = sizeX;
      boxSizeY = sizeY;
      boxSizeZ = sizeZ;
   }

   /**
    * Sets the height for each pyramid.
    *
    * @param pyramidHeight the height for each pyramid.
    */
   public void setPyramidHeight(double pyramidHeight)
   {
      this.pyramidHeight = pyramidHeight;
   }

   /** {@inheritDoc} */
   @Override
   public String getName()
   {
      return name;
   }

   /**
    * Returns the box's size along the x-axis.
    *
    * @return the box's size along the x-axis.
    */
   public double getBoxSizeX()
   {
      return boxSizeX;
   }

   /**
    * Returns the box's size along the y-axis.
    *
    * @return the box's size along the y-axis.
    */
   public double getBoxSizeY()
   {
      return boxSizeY;
   }

   /**
    * Returns the box's size along the z-axis.
    *
    * @return the box's size along the z-axis.
    */
   public double getBoxSizeZ()
   {
      return boxSizeZ;
   }

   /**
    * Returns the height for each pyramid.
    *
    * @return the height for each pyramid.
    */
   public double getPyramidHeight()
   {
      return pyramidHeight;
   }

   @Override
   public PyramidBox3DDescription copy()
   {
      return new PyramidBox3DDescription(this);
   }

   @Override
   public int hashCode()
   {
      long bits = 1L;
      bits = EuclidHashCodeTools.addToHashCode(bits, boxSizeX);
      bits = EuclidHashCodeTools.addToHashCode(bits, boxSizeY);
      bits = EuclidHashCodeTools.addToHashCode(bits, boxSizeZ);
      bits = EuclidHashCodeTools.addToHashCode(bits, pyramidHeight);
      bits = EuclidHashCodeTools.addToHashCode(bits, name);
      return EuclidHashCodeTools.toIntHashCode(bits);
   }

   @Override
   public boolean equals(Object object)
   {
      if (object == this)
      {
         return true;
      }
      else if (object instanceof PyramidBox3DDescription)
      {
         PyramidBox3DDescription other = (PyramidBox3DDescription) object;
         if (boxSizeX != other.boxSizeX)
            return false;
         if (boxSizeY != other.boxSizeY)
            return false;
         if (boxSizeZ != other.boxSizeZ)
            return false;
         if (pyramidHeight != other.pyramidHeight)
            return false;
         if (name == null ? other.name != null : !name.equals(other.name))
            return false;
         return true;
      }
      else
      {
         return false;
      }
   }

   @Override
   public String toString()
   {
      return EuclidCoreIOTools.getStringOf("Wedge: [name: " + name
            + ", box size: (", "), pryamid height: " + pyramidHeight + "]", ", ", boxSizeX, boxSizeY, boxSizeZ);
   }
}
