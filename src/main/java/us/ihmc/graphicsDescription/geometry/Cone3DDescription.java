package us.ihmc.graphicsDescription.geometry;

import us.ihmc.euclid.tools.EuclidCoreIOTools;
import us.ihmc.euclid.tools.EuclidHashCodeTools;

/**
 * Description for creating a 3D cone.
 * <p>
 * It is assumed that the cone's axis is aligned with the z-axis and is positioned such that the
 * center of its bottom face is at the origin.
 * </p>
 */
public class Cone3DDescription implements GeometryDescription
{
   private String name = "cone";
   private double height;
   private double radius;
   private int resolution = 64;

   /**
    * Creates an empty description. The parameters have to be all set before this description can be
    * used.
    */
   public Cone3DDescription()
   {
   }

   /**
    * Creates and initializes a description for a regular 3D cone.
    * 
    * @param height the height of the cone.
    * @param radius the radius of the base.
    */
   public Cone3DDescription(double height, double radius)
   {
      this.height = height;
      this.radius = radius;
   }

   /**
    * Creates and initializes a description for a regular 3D cone.
    * 
    * @param height     the height of the cone.
    * @param radius     the radius of the base.
    * @param resolution used for discretizing the geometry.
    */
   public Cone3DDescription(double height, double radius, int resolution)
   {
      this.height = height;
      this.radius = radius;
      this.resolution = resolution;
   }

   /** {@inheritDoc} */
   @Override
   public void setName(String name)
   {
      this.name = name;
   }

   /**
    * Sets the height of the cone.
    * 
    * @param height the height of the cone.
    */
   public void setHeight(double height)
   {
      this.height = height;
   }

   /**
    * Sets the radius of the cone.
    * 
    * @param radius the radius of the base.
    */
   public void setRadius(double radius)
   {
      this.radius = radius;
   }

   /**
    * Sets the cone's resolution used when discretizing it.
    * 
    * @param resolution the cone's resolution.
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
    * Returns the height of the cone.
    * 
    * @return the cone's height.
    */
   public double getHeight()
   {
      return height;
   }

   /**
    * Returns the radius of the cone.
    * 
    * @return the radius of the base.
    */
   public double getRadius()
   {
      return radius;
   }

   /**
    * Returns the cone's resolution which can be used for discretizing it.
    * 
    * @return the cone's resolution.
    */
   public int getResolution()
   {
      return resolution;
   }

   @Override
   public int hashCode()
   {
      long bits = 1L;
      bits = EuclidHashCodeTools.addToHashCode(bits, height);
      bits = EuclidHashCodeTools.addToHashCode(bits, radius);
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
      else if (object instanceof Cone3DDescription)
      {
         Cone3DDescription other = (Cone3DDescription) object;
         if (height != other.height)
            return false;
         if (radius != other.radius)
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
      return "Cone: [name: " + name + ", height: " + String.format(EuclidCoreIOTools.DEFAULT_FORMAT, height) + ", radius: "
            + String.format(EuclidCoreIOTools.DEFAULT_FORMAT, radius) + ", resolution: " + resolution + "]";
   }
}
