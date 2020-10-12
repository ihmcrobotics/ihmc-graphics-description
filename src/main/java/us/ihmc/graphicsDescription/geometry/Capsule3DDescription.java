package us.ihmc.graphicsDescription.geometry;

import us.ihmc.euclid.tools.EuclidCoreIOTools;
import us.ihmc.euclid.tools.EuclidHashCodeTools;

/**
 * Description for creating a 3D capsule.
 * <p>
 * It is assumed that the capsule's axis is aligned with the z-axis and it is centered at the
 * origin.
 * </p>
 */
public class Capsule3DDescription implements GeometryDescription
{
   private String name = "capsule";
   private double height;
   private double radiusX;
   private double radiusY;
   private double radiusZ;
   private int resolution = 64;

   /**
    * Creates an empty description. The parameters have to be all set before this description can be
    * used.
    */
   public Capsule3DDescription()
   {
   }

   /**
    * Creates and initializes a description for a regular 3D capsule.
    * 
    * @param height the capsule's height or length. Distance separating the center of the two half
    *               spheres.
    * @param radius the capsule's radius.
    */
   public Capsule3DDescription(double height, double radius)
   {
      this(height, radius, radius, radius);
   }

   /**
    * Creates and initializes a description for a 3D capsule.
    * 
    * @param height  the capsule's height or length. Distance separating the center of the two half
    *                ellipsoids.
    * @param radiusX radius of along the x-axis.
    * @param radiusY radius of along the y-axis.
    * @param radiusZ radius of along the z-axis.
    */
   public Capsule3DDescription(double height, double radiusX, double radiusY, double radiusZ)
   {
      this.height = height;
      this.radiusX = radiusX;
      this.radiusY = radiusY;
      this.radiusZ = radiusZ;
   }

   /**
    * Creates and initializes a description for a regular 3D capsule.
    * 
    * @param height     the capsule's height or length. Distance separating the center of the two half
    *                   spheres.
    * @param radius     the capsule's radius.
    * @param resolution used for discretizing the geometry.
    */
   public Capsule3DDescription(double height, double radius, int resolution)
   {
      this(height, radius, radius, radius, resolution);
   }

   /**
    * Creates and initializes a description for a 3D capsule.
    * 
    * @param height     the capsule's height or length. Distance separating the center of the two half
    *                   ellipsoids.
    * @param radiusX    radius of the capsule along the x-axis.
    * @param radiusY    radius of the capsule along the y-axis.
    * @param radiusZ    radius of the capsule along the z-axis.
    * @param resolution used for discretizing the geometry.
    */
   public Capsule3DDescription(double height, double radiusX, double radiusY, double radiusZ, int resolution)
   {
      this.height = height;
      this.radiusX = radiusX;
      this.radiusY = radiusY;
      this.radiusZ = radiusZ;
      this.resolution = resolution;
   }

   /** {@inheritDoc} */
   @Override
   public void setName(String name)
   {
      this.name = name;
   }

   /**
    * Sets the height of the capsule.
    * 
    * @param height the capsule's height or length. Distance separating the center of the two half
    *               ellipsoids.
    */
   public void setHeight(double height)
   {
      this.height = height;
   }

   /**
    * Tests whether the capsule is a regular capsule, i.e. all radii are equal.
    * 
    * @return {@code true} if the capsule is regular, {@code false} otherwise.
    */
   public boolean isRegular()
   {
      return radiusX == radiusY && radiusX == radiusZ;
   }

   /**
    * Sets the radius along the x-axis of the capsule.
    * 
    * @param radiusX radius of the capsule along the x-axis.
    */
   public void setRadiusX(double radiusX)
   {
      this.radiusX = radiusX;
   }

   /**
    * Sets the radius along the y-axis of the capsule.
    * 
    * @param radiusY radius of the capsule along the y-axis.
    */
   public void setRadiusY(double radiusY)
   {
      this.radiusY = radiusY;
   }

   /**
    * Sets the radius along the z-axis of the capsule.
    * 
    * @param radiusZ radius of the capsule along the z-axis.
    */
   public void setRadiusZ(double radiusZ)
   {
      this.radiusZ = radiusZ;
   }

   /**
    * Sets the same radius along the three axes making the capsule regular.
    * 
    * @param radius the capsule's radius.
    */
   public void setRadius(double radius)
   {
      setRadii(radius, radius, radius);
   }

   /**
    * Sets the radii for the capsule along each axis individually.
    * 
    * @param radiusX radius of the capsule along the x-axis.
    * @param radiusY radius of the capsule along the y-axis.
    * @param radiusZ radius of the capsule along the z-axis.
    */
   public void setRadii(double radiusX, double radiusY, double radiusZ)
   {
      this.radiusX = radiusX;
      this.radiusY = radiusY;
      this.radiusZ = radiusZ;
   }

   /**
    * Sets the capsule's resolution used when discretizing it.
    * 
    * @param resolution the capsule's resolution.
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
    * Returns the height of the capsule.
    * 
    * @return the capsule's height or length. Distance separating the center of the two half
    *         ellipsoids.
    */
   public double getHeight()
   {
      return height;
   }

   /**
    * Returns the capsule's radius along the x-axis.
    * 
    * @return the radius along the x-axis.
    */
   public double getRadiusX()
   {
      return radiusX;
   }

   /**
    * Returns the capsule's radius along the y-axis.
    * 
    * @return the radius along the y-axis.
    */
   public double getRadiusY()
   {
      return radiusY;
   }

   /**
    * Returns the capsule's radius along the z-axis.
    * 
    * @return the radius along the z-axis.
    */
   public double getRadiusZ()
   {
      return radiusZ;
   }

   /**
    * Returns the capsule's resolution which can be used for discretizing it.
    * 
    * @return the capsule's resolution.
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
      else if (object instanceof Capsule3DDescription)
      {
         Capsule3DDescription other = (Capsule3DDescription) object;
         if (height != other.height)
            return false;
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
      return "Capsule: [name: " + name + ", height: " + String.format(EuclidCoreIOTools.DEFAULT_FORMAT, height)
            + EuclidCoreIOTools.getStringOf(", radii: (", ")", ", ", radiusX, radiusY, radiusZ) + ", resolution: " + resolution + "]";
   }
}