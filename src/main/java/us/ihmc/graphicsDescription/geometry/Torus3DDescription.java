package us.ihmc.graphicsDescription.geometry;

import us.ihmc.euclid.tools.EuclidCoreIOTools;
import us.ihmc.euclid.tools.EuclidHashCodeTools;

/**
 * Description for creating a 3D torus.
 * <p>
 * It is assumed that the torus' axis is aligned with the z-axis and its centroid at the origin.
 * </p>
 */
public class Torus3DDescription implements GeometryDescription
{
   private String name = "torus";
   private double majorRadius;
   private double minorRadius;
   private int resolution = 64;

   /**
    * Creates an empty description. The parameters have to be all set before this description can be
    * used.
    */
   public Torus3DDescription()
   {
   }

   /**
    * Creates and initializes a description for a 3D torus.
    * 
    * @param majorRadius the radius from the torus centroid to the tube center.
    * @param minorRadius the radius of the tube.
    */
   public Torus3DDescription(double majorRadius, double minorRadius)
   {
      this.majorRadius = majorRadius;
      this.minorRadius = minorRadius;
   }

   /**
    * Creates and initializes a description for a partial 3D torus.
    * 
    * @param majorRadius the radius from the torus centroid to the tube center.
    * @param minorRadius the radius of the tube.
    * @param resolution  used for discretizing the geometry.
    */
   public Torus3DDescription(double majorRadius, double minorRadius, int resolution)
   {
      this.majorRadius = majorRadius;
      this.minorRadius = minorRadius;
      this.resolution = resolution;
   }

   /** {@inheritDoc} */
   @Override
   public void setName(String name)
   {
      this.name = name;
   }

   /**
    * Sets the torus' major radius.
    * 
    * @param majorRadius the radius from the torus centroid to the tube center.
    */
   public void setMajorRadius(double majorRadius)
   {
      this.majorRadius = majorRadius;
   }

   /**
    * Sets the torus' minor radius.
    * 
    * @param minorRadius the radius of the tube.
    */
   public void setMinorRadius(double minorRadius)
   {
      this.minorRadius = minorRadius;
   }

   /**
    * Sets the torus' resolution used when discretizing it.
    * 
    * @param resolution the torus' resolution.
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
    * Returns the torus' major radius.
    * 
    * @return the radius from the torus centroid to the tube center.
    */
   public double getMajorRadius()
   {
      return majorRadius;
   }

   /**
    * Returns the torus' minor radius.
    * 
    * @return the radius of the tube.
    */
   public double getMinorRadius()
   {
      return minorRadius;
   }

   /**
    * Returns the torus' resolution which can be used for discretizing it.
    * 
    * @return the torus' resolution.
    */
   public int getResolution()
   {
      return resolution;
   }

   @Override
   public int hashCode()
   {
      long bits = 1L;
      bits = EuclidHashCodeTools.addToHashCode(bits, majorRadius);
      bits = EuclidHashCodeTools.addToHashCode(bits, minorRadius);
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
      else if (object instanceof Torus3DDescription)
      {
         Torus3DDescription other = (Torus3DDescription) object;
         if (majorRadius != other.majorRadius)
            return false;
         if (minorRadius != other.minorRadius)
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
      return "Torus: [name: " + name + EuclidCoreIOTools.getStringOf(", radii: (", ")", ", ", majorRadius, minorRadius) + ", resolution: " + resolution + "]";
   }
}
