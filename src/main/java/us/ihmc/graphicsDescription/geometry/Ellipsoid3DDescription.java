package us.ihmc.graphicsDescription.geometry;

import us.ihmc.euclid.tools.EuclidCoreIOTools;
import us.ihmc.euclid.tools.EuclidHashCodeTools;

/**
 * Description for creating a 3D ellipsoid.
 */
public class Ellipsoid3DDescription implements GeometryDescription
{
   private String name = "ellipsoid";
   private double radiusX;
   private double radiusY;
   private double radiusZ;
   private int resolution = 64;

   /**
    * Creates an empty description. The parameters have to be all set before this description can be
    * used.
    */
   public Ellipsoid3DDescription()
   {
   }

   /**
    * Creates and initializes a description for a 3D ellipsoid.
    * 
    * @param radiusX radius of the ellipsoid along the x-axis.
    * @param radiusY radius of the ellipsoid along the y-axis.
    * @param radiusZ radius of the ellipsoid along the z-axis.
    */
   public Ellipsoid3DDescription(double radiusX, double radiusY, double radiusZ)
   {
      this.radiusX = radiusX;
      this.radiusY = radiusY;
      this.radiusZ = radiusZ;
   }

   /**
    * Creates and initializes a description for a 3D ellipsoid.
    * 
    * @param radiusX    radius of the ellipsoid along the x-axis.
    * @param radiusY    radius of the ellipsoid along the y-axis.
    * @param radiusZ    radius of the ellipsoid along the z-axis.
    * @param resolution used for discretizing the geometry.
    */
   public Ellipsoid3DDescription(double radiusX, double radiusY, double radiusZ, int resolution)
   {
      this.radiusX = radiusX;
      this.radiusY = radiusY;
      this.radiusZ = radiusZ;
      this.resolution = resolution;
   }

   public Ellipsoid3DDescription(Ellipsoid3DDescription other)
   {
      name = other.name;
      radiusX = other.radiusX;
      radiusY = other.radiusY;
      radiusZ = other.radiusZ;
      resolution = other.resolution;
   }

   /** {@inheritDoc} */
   @Override
   public void setName(String name)
   {
      this.name = name;
   }

   /**
    * Tests if this ellipsoid is a sphere, i.e. all three radii are equal.
    * 
    * @return {@code true} if this ellipsoid is a sphere, {@code false} if it is an ellipsoid.
    */
   public boolean isSphere()
   {
      return radiusX == radiusY && radiusX == radiusZ;
   }

   /**
    * Sets the radius along the x-axis for the ellipsoid.
    * 
    * @param radiusX the ellipsoid's radius along the x-axis.
    */
   public void setRadiusX(double radiusX)
   {
      this.radiusX = radiusX;
   }

   /**
    * Sets the radius along the y-axis for the ellipsoid.
    * 
    * @param radiusY the ellipsoid's radius along the y-axis.
    */
   public void setRadiusY(double radiusY)
   {
      this.radiusY = radiusY;
   }

   /**
    * Sets the radius along the z-axis for the ellipsoid.
    * 
    * @param radiusZ the ellipsoid's radius along the z-axis.
    */
   public void setRadiusZ(double radiusZ)
   {
      this.radiusZ = radiusZ;
   }

   /**
    * Sets the radii of the ellipsoid.
    * 
    * @param radiusX the ellipsoid's radius along the x-axis.
    * @param radiusY the ellipsoid's radius along the y-axis.
    * @param radiusZ the ellipsoid's radius along the z-axis.
    */
   public void setRadii(double radiusX, double radiusY, double radiusZ)
   {
      this.radiusX = radiusX;
      this.radiusY = radiusY;
      this.radiusZ = radiusZ;
   }

   /**
    * Sets the ellipsoid's resolution used when discretizing it.
    * 
    * @param resolution the ellipsoid's resolution.
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
    * Returns the radius along the x-axis of the ellipsoid.
    * 
    * @return the ellipsoid's radius along the x-axis.
    */
   public double getRadiusX()
   {
      return radiusX;
   }

   /**
    * Returns the radius along the y-axis of the ellipsoid.
    * 
    * @return the ellipsoid's radius along the y-axis.
    */
   public double getRadiusY()
   {
      return radiusY;
   }

   /**
    * Returns the radius along the z-axis of the ellipsoid.
    * 
    * @return the ellipsoid's radius along the z-axis.
    */
   public double getRadiusZ()
   {
      return radiusZ;
   }

   /**
    * Returns the ellipsoid's resolution which can be used for discretizing it.
    * 
    * @return the ellipsoid's resolution.
    */
   public int getResolution()
   {
      return resolution;
   }

   @Override
   public Ellipsoid3DDescription copy()
   {
      return new Ellipsoid3DDescription(this);
   }

   @Override
   public int hashCode()
   {
      long bits = 1L;
      bits = EuclidHashCodeTools.addToHashCode(bits, radiusX);
      bits = EuclidHashCodeTools.addToHashCode(bits, radiusY);
      bits = EuclidHashCodeTools.addToHashCode(bits, radiusZ);
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
      else if (object instanceof Ellipsoid3DDescription)
      {
         Ellipsoid3DDescription other = (Ellipsoid3DDescription) object;
         if (radiusX != other.radiusX)
            return false;
         if (radiusY != other.radiusY)
            return false;
         if (radiusZ != other.radiusZ)
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
      return EuclidCoreIOTools.getStringOf("Ellipsoid: [name: " + name + ", radii: (", "), resolution: " + resolution + "]", ", ", radiusX, radiusY, radiusZ);
   }
}
