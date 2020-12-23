package us.ihmc.graphicsDescription.geometry;

import us.ihmc.euclid.tools.EuclidCoreIOTools;
import us.ihmc.euclid.tools.EuclidHashCodeTools;

/**
 * Description for creating a 3D sphere.
 */
public class Sphere3DDescription implements GeometryDescription
{
   private String name = "sphere";
   private double radius;
   private int resolution = 64;

   /**
    * Creates an empty description. The parameters have to be all set before this description can be
    * used.
    */
   public Sphere3DDescription()
   {
   }

   /**
    * Creates and initializes a description for a 3D sphere.
    * 
    * @param radius the radius of the sphere.
    */
   public Sphere3DDescription(double radius)
   {
      this.radius = radius;
   }

   /**
    * Creates and initializes a description for a 3D sphere.
    * 
    * @param radius     the radius of the sphere.
    * @param resolution used for discretizing the geometry.
    */
   public Sphere3DDescription(double radius, int resolution)
   {
      this.radius = radius;
      this.resolution = resolution;
   }

   public Sphere3DDescription(Sphere3DDescription other)
   {
      name = other.name;
      radius = other.radius;
      resolution = other.resolution;
   }

   /** {@inheritDoc} */
   @Override
   public void setName(String name)
   {
      this.name = name;
   }

   /**
    * Sets the radius of the sphere.
    * 
    * @param radius the radius of the sphere.
    */
   public void setRadius(double radius)
   {
      this.radius = radius;
   }

   /**
    * Sets the sphere's resolution used when discretizing it.
    * 
    * @param resolution the sphere's resolution.
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
    * Returns the radius of the sphere.
    * 
    * @return the radius of the sphere.
    */
   public double getRadius()
   {
      return radius;
   }

   /**
    * Returns the sphere's resolution which can be used for discretizing it.
    * 
    * @return the sphere's resolution.
    */
   public int getResolution()
   {
      return resolution;
   }

   @Override
   public Sphere3DDescription copy()
   {
      return new Sphere3DDescription(this);
   }

   @Override
   public int hashCode()
   {
      long bits = 1L;
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
      else if (object instanceof Sphere3DDescription)
      {
         Sphere3DDescription other = (Sphere3DDescription) object;
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
      return "Sphere: [name: " + name + ", radius: " + String.format(EuclidCoreIOTools.DEFAULT_FORMAT, radius) + ", resolution: " + resolution + "]";
   }
}
