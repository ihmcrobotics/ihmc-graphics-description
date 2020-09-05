package us.ihmc.graphicsDescription.geometry;

import us.ihmc.euclid.tools.EuclidCoreIOTools;
import us.ihmc.euclid.tools.EuclidHashCodeTools;

/**
 * Description for creating a 3D box.
 */
public class Box3DDescription implements GeometryDescription
{
   private String name = "box";
   private double sizeX;
   private double sizeY;
   private double sizeZ;
   private boolean centered = true;

   /**
    * Creates an empty description. The parameters have to be all set before this description can be
    * used.
    */
   public Box3DDescription()
   {
   }

   /**
    * Creates and initializes a description for a 3D box.
    * <p>
    * The box is centered at the origin.
    * </p>
    * 
    * @param sizeX box size along the x-axis.
    * @param sizeY box size along the y-axis.
    * @param sizeZ box size along the z-axis.
    */
   public Box3DDescription(double sizeX, double sizeY, double sizeZ)
   {
      this(sizeX, sizeY, sizeZ, true);
   }

   /**
    * Creates and initializes a description for a 3D box.
    * <p>
    * The box is centered at the origin.
    * </p>
    * 
    * @param sizeX    box size along the x-axis.
    * @param sizeY    box size along the y-axis.
    * @param sizeZ    box size along the z-axis.
    * @param centered when {@code true} the box centered at the origin, when {@code false} the bottom
    *                 face (the face at min z-coordinate) is centered at the origin.
    */
   public Box3DDescription(double sizeX, double sizeY, double sizeZ, boolean centered)
   {
      this.sizeX = sizeX;
      this.sizeY = sizeY;
      this.sizeZ = sizeZ;
      this.centered = centered;
   }

   /** {@inheritDoc} */
   @Override
   public void setName(String name)
   {
      this.name = name;
   }

   /**
    * Sets the size of the box along the x-axis.
    * 
    * @param sizeX the box size along the x-axis.
    */
   public void setSizeX(double sizeX)
   {
      this.sizeX = sizeX;
   }

   /**
    * Sets the size of the box along the y-axis.
    * 
    * @param sizeY the box size along the y-axis.
    */
   public void setSizeY(double sizeY)
   {
      this.sizeY = sizeY;
   }

   /**
    * Sets the size of the box along the z-axis.
    * 
    * @param sizeZ the box size along the z-axis.
    */
   public void setSizeZ(double sizeZ)
   {
      this.sizeZ = sizeZ;
   }

   /**
    * Sets whether the box should be centered at the origin or if its bottom face should be.
    * 
    * @param centered {@code true} for the box to be centered at the origin, {@code false} for the
    *                 bottom face to be centered at the origin.
    */
   public void setCentered(boolean centered)
   {
      this.centered = centered;
   }

   /** {@inheritDoc} */
   @Override
   public String getName()
   {
      return name;
   }

   /**
    * Returns the size of the box along the x-axis.
    * 
    * @return the box size along the x-axis.
    */
   public double getSizeX()
   {
      return sizeX;
   }

   /**
    * Returns the size of the box along the y-axis.
    * 
    * @return the box size along the y-axis.
    */
   public double getSizeY()
   {
      return sizeY;
   }

   /**
    * Returns the size of the box along the z-axis.
    * 
    * @return the box size along the z-axis.
    */
   public double getSizeZ()
   {
      return sizeZ;
   }

   /**
    * Returns whether the box should be centered at the origin.
    * 
    * @return {@code true} if the box should be centered at the origin, {@code false} if its bottom
    *         face should centered at the origin.
    */
   public boolean isCentered()
   {
      return centered;
   }

   @Override
   public int hashCode()
   {
      long bits = 1L;
      bits = EuclidHashCodeTools.addToHashCode(bits, sizeX);
      bits = EuclidHashCodeTools.addToHashCode(bits, sizeY);
      bits = EuclidHashCodeTools.addToHashCode(bits, sizeZ);
      bits = EuclidHashCodeTools.addToHashCode(bits, centered);
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
      else if (object instanceof Box3DDescription)
      {
         Box3DDescription other = (Box3DDescription) object;
         if (sizeX != other.sizeX)
            return false;
         if (sizeY != other.sizeY)
            return false;
         if (sizeZ != other.sizeZ)
            return false;
         if (centered != other.centered)
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
      return EuclidCoreIOTools.getStringOf("Box: [name: " + name + ", size: (", ")]", ", ", sizeX, sizeY, sizeZ);
   }
}
