package us.ihmc.graphicsDescription.geometry;

import us.ihmc.euclid.tools.EuclidCoreIOTools;
import us.ihmc.euclid.tools.EuclidHashCodeTools;

/**
 * Description for creating a 3D wedge.
 * <p>
 * It is assumed that the wedge's bottom face is centered at the origin.
 * </p>
 */
public class Wedge3DDescription implements GeometryDescription
{
   private String name = "wedge";
   private double sizeX;
   private double sizeY;
   private double sizeZ;

   /**
    * Creates an empty description. The parameters have to be all set before this description can be
    * used.
    */
   public Wedge3DDescription()
   {
   }

   /**
    * Creates and initializes a description for a 3D wedge.
    * 
    * @param sizeX the length of the wedge along the x-axis.
    * @param sizeY the width of the wedge along the y-axis.
    * @param sizeZ the height of the wedge along the z-axis.
    */
   public Wedge3DDescription(double sizeX, double sizeY, double sizeZ)
   {
      this.sizeX = sizeX;
      this.sizeY = sizeY;
      this.sizeZ = sizeZ;
   }

   /** {@inheritDoc} */
   @Override
   public void setName(String name)
   {
      this.name = name;
   }

   /**
    * Sets the length of the wedge along the x-axis.
    * 
    * @param sizeX the length of the wedge along the x-axis.
    */
   public void setSizeX(double sizeX)
   {
      this.sizeX = sizeX;
   }

   /**
    * Sets the width of the wedge along the y-axis.
    * 
    * @param sizeY the width of the wedge along the y-axis.
    */
   public void setSizeY(double sizeY)
   {
      this.sizeY = sizeY;
   }

   /**
    * Sets the height of the wedge along the z-axis.
    * 
    * @param sizeZ the height of the wedge along the z-axis.
    */
   public void setSizeZ(double sizeZ)
   {
      this.sizeZ = sizeZ;
   }

   /**
    * Sets the size of the wedge.
    * 
    * @param sizeX the length of the wedge along the x-axis.
    * @param sizeY the width of the wedge along the y-axis.
    * @param sizeZ the height of the wedge along the z-axis.
    */
   public void setSize(double sizeX, double sizeY, double sizeZ)
   {
      this.sizeX = sizeX;
      this.sizeY = sizeY;
      this.sizeZ = sizeZ;
   }

   /** {@inheritDoc} */
   @Override
   public String getName()
   {
      return name;
   }

   /**
    * Returns the length of the wedge along the x-axis.
    * 
    * @return the length of the wedge along the x-axis.
    */
   public double getSizeX()
   {
      return sizeX;
   }

   /**
    * Returns the width of the wedge along the y-axis.
    * 
    * @return the width of the wedge along the y-axis.
    */
   public double getSizeY()
   {
      return sizeY;
   }

   /**
    * Returns the height of the wedge along the z-axis.
    * 
    * @return the height of the wedge along the z-axis.
    */
   public double getSizeZ()
   {
      return sizeZ;
   }

   @Override
   public int hashCode()
   {
      long bits = 1L;
      bits = EuclidHashCodeTools.addToHashCode(bits, sizeX);
      bits = EuclidHashCodeTools.addToHashCode(bits, sizeY);
      bits = EuclidHashCodeTools.addToHashCode(bits, sizeZ);
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
      else if (object instanceof Wedge3DDescription)
      {
         Wedge3DDescription other = (Wedge3DDescription) object;
         if (sizeX != other.sizeX)
            return false;
         if (sizeY != other.sizeY)
            return false;
         if (sizeZ != other.sizeZ)
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
      return EuclidCoreIOTools.getStringOf("Wedge: [name: " + name + ", size: (", ")]", ", ", sizeX, sizeY, sizeZ);
   }
}
