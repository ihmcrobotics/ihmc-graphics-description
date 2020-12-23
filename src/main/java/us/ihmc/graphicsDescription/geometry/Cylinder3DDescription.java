package us.ihmc.graphicsDescription.geometry;

import us.ihmc.euclid.tools.EuclidHashCodeTools;

/**
 * Description for creating a 3D cylinder.
 * <p>
 * It is assumed that the cylinder's axis is aligned with the z-axis and it is centered at the
 * origin.
 * </p>
 */
public class Cylinder3DDescription implements GeometryDescription
{
   private String name = "cylinder";
   private double height;
   private double radius;
   private boolean centered = true;
   private int resolution = 64;

   /**
    * Creates an empty description. The parameters have to be all set before this description can be
    * used.
    */
   public Cylinder3DDescription()
   {
   }

   /**
    * Creates and initializes a description for a 3D cylinder.
    * <p>
    * The cylinder is assumed to be centered at the origin.
    * </p>
    * 
    * @param radius the cylinder's radius.
    * @param height the cylinder's height or length.
    */
   public Cylinder3DDescription(double height, double radius)
   {
      this.height = height;
      this.radius = radius;
   }

   /**
    * Creates and initializes a description for a 3D cylinder.
    * <p>
    * The cylinder is assumed to be centered at the origin.
    * </p>
    * 
    * @param radius     the cylinder's radius.
    * @param height     the cylinder's height or length.
    * @param resolution used for discretizing the geometry.
    */
   public Cylinder3DDescription(double height, double radius, int resolution)
   {
      this.height = height;
      this.radius = radius;
      this.resolution = resolution;
   }

   /**
    * Creates and initializes a description for a 3D cylinder.
    * 
    * @param radius   the cylinder's radius.
    * @param height   the cylinder's height or length.
    * @param centered {@code true} to center the cylinder are the origin, {@code false} to center its
    *                 bottom face at the origin.
    */
   public Cylinder3DDescription(double height, double radius, boolean centered)
   {
      this.height = height;
      this.radius = radius;
      this.centered = centered;
   }

   /**
    * Creates and initializes a description for a 3D cylinder.
    * 
    * @param radius     the cylinder's radius.
    * @param height     the cylinder's height or length.
    * @param centered   {@code true} to center the cylinder are the origin, {@code false} to center its
    *                   bottom face at the origin.
    * @param resolution used for discretizing the geometry.
    */
   public Cylinder3DDescription(double height, double radius, boolean centered, int resolution)
   {
      this.height = height;
      this.radius = radius;
      this.centered = centered;
      this.resolution = resolution;
   }

   public Cylinder3DDescription(Cylinder3DDescription other)
   {
      name = other.name;
      height = other.height;
      radius = other.radius;
      centered = other.centered;
      resolution = other.resolution;
   }

   /** {@inheritDoc} */
   @Override
   public void setName(String name)
   {
      this.name = name;
   }

   /**
    * Sets the height of the cylinder.
    * 
    * @param height the cylinder's height or length.
    */
   public void setHeight(double height)
   {
      this.height = height;
   }

   /**
    * Sets the radius of the cylinder.
    * 
    * @param radius the cylinder's radius.
    */
   public void setRadius(double radius)
   {
      this.radius = radius;
   }

   /**
    * Sets whether the cylinder should be centered at the origin or if its bottom face should be.
    * 
    * @param centered {@code true} for the cylinder to be centered at the origin, {@code false} for the
    *                 bottom face to be centered at the origin.
    */
   public void setCentered(boolean centered)
   {
      this.centered = centered;
   }

   /**
    * Sets the cylinder's resolution used when discretizing it.
    * 
    * @param resolution the cylinder's resolution.
    */
   public void setResolution(int resolution)
   {
      this.resolution = resolution;
   }

   /** {@inheritDoc} */
   @Override
   public String getName()
   {
      return name;
   }

   /**
    * Returns the height of the cylinder.
    * 
    * @return the cylinder's height or length.
    */
   public double getHeight()
   {
      return height;
   }

   /**
    * Returns the radius of the cylinder.
    * 
    * @return the cylinder's radius.
    */
   public double getRadius()
   {
      return radius;
   }

   /**
    * Returns whether the cylinder should be centered at the origin.
    * 
    * @return {@code true} if the cylinder should be centered at the origin, {@code false} if its
    *         bottom face should centered at the origin.
    */
   public boolean isCentered()
   {
      return centered;
   }

   /**
    * Returns the cylinder's resolution which can be used for discretizing it.
    * 
    * @return the cylinder's resolution.
    */
   public int getResolution()
   {
      return resolution;
   }

   @Override
   public Cylinder3DDescription copy()
   {
      return new Cylinder3DDescription(this);
   }

   @Override
   public int hashCode()
   {
      long bits = 1L;
      bits = EuclidHashCodeTools.addToHashCode(bits, height);
      bits = EuclidHashCodeTools.addToHashCode(bits, radius);
      bits = EuclidHashCodeTools.addToHashCode(bits, centered);
      bits = EuclidHashCodeTools.addToHashCode(bits, resolution);
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
      else if (object instanceof Cylinder3DDescription)
      {
         Cylinder3DDescription other = (Cylinder3DDescription) object;
         if (height != other.height)
            return false;
         if (radius != other.radius)
            return false;
         if (centered != other.centered)
            return false;
         if (resolution != other.resolution)
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
      return "Cylinder: [name: " + name + ", height: " + height + ", radius: " + radius + ", resolution: " + resolution + "]";
   }
}
