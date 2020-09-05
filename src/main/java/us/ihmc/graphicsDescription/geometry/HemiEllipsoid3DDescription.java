package us.ihmc.graphicsDescription.geometry;

import us.ihmc.euclid.tools.EuclidCoreIOTools;
import us.ihmc.euclid.tools.EuclidHashCodeTools;

/**
 * Description for creating a 3D hemi-ellipsoid.
 * <p>
 * It is assumed that the hemi-ellipsoid represents the top half of a full ellipsoid, i.e. the half
 * in the z positive space.
 * </p>
 */
public class HemiEllipsoid3DDescription implements GeometryDescription
{
   private String name = "hemiEllipsoid";
   private double radiusX;
   private double radiusY;
   private double radiusZ;
   private int resolution = 64;

   /**
    * Creates an empty description. The parameters have to be all set before this description can be
    * used.
    */
   public HemiEllipsoid3DDescription()
   {
   }

   /**
    * Creates and initializes a description for a 3D hemi-ellipsoid.
    * 
    * @param radiusX radius of the hemi-ellipsoid along the x-axis.
    * @param radiusY radius of the hemi-ellipsoid along the y-axis.
    * @param radiusZ radius of the hemi-ellipsoid along the z-axis.
    */
   public HemiEllipsoid3DDescription(double radiusX, double radiusY, double radiusZ)
   {
      this.radiusX = radiusX;
      this.radiusY = radiusY;
      this.radiusZ = radiusZ;
   }

   /**
    * Creates and initializes a description for a 3D hemi-ellipsoid.
    * 
    * @param radiusX    radius of the hemi-ellipsoid along the x-axis.
    * @param radiusY    radius of the hemi-ellipsoid along the y-axis.
    * @param radiusZ    radius of the hemi-ellipsoid along the z-axis.
    * @param resolution used for discretizing the geometry.
    */
   public HemiEllipsoid3DDescription(double radiusX, double radiusY, double radiusZ, int resolution)
   {
      this.radiusX = radiusX;
      this.radiusY = radiusY;
      this.radiusZ = radiusZ;
      this.resolution = resolution;
   }

   @Override
   public void setName(String name)
   {
      this.name = name;
   }

   /**
    * Sets the radius along the x-axis for the hemi-ellipsoid.
    * 
    * @param radiusX the hemi-ellipsoid's radius along the x-axis.
    */
   public void setRadiusX(double radiusX)
   {
      this.radiusX = radiusX;
   }

   /**
    * Sets the radius along the y-axis for the hemi-ellipsoid.
    * 
    * @param radiusY the hemi=ellipsoid's radius along the y-axis.
    */
   public void setRadiusY(double radiusY)
   {
      this.radiusY = radiusY;
   }

   /**
    * Sets the radius along the z-axis for the hemi-ellipsoid.
    * 
    * @param radiusZ the hemi-ellipsoid's radius along the z-axis.
    */
   public void setRadiusZ(double radiusZ)
   {
      this.radiusZ = radiusZ;
   }

   /**
    * Sets the radii of the hemi-ellipsoid.
    * 
    * @param radiusX the hemi-ellipsoid's radius along the x-axis.
    * @param radiusY the hemi-ellipsoid's radius along the y-axis.
    * @param radiusZ the hemi-ellipsoid's radius along the z-axis.
    */
   public void setRadii(double radiusX, double radiusY, double radiusZ)
   {
      this.radiusX = radiusX;
      this.radiusY = radiusY;
      this.radiusZ = radiusZ;
   }

   /**
    * Sets the hemi-ellipsoid's resolution used when discretizing it.
    * 
    * @param resolution the hemi-ellipsoid's resolution.
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
    * Returns the radius along the x-axis of the hemi-ellipsoid.
    * 
    * @return the hemi-ellipsoid's radius along the x-axis.
    */
   public double getRadiusX()
   {
      return radiusX;
   }

   /**
    * Returns the radius along the y-axis of the hemi-ellipsoid.
    * 
    * @return the hemi-ellipsoid's radius along the y-axis.
    */
   public double getRadiusY()
   {
      return radiusY;
   }

   /**
    * Returns the radius along the z-axis of the hemi-ellipsoid.
    * 
    * @return the hemi-ellipsoid's radius along the z-axis.
    */
   public double getRadiusZ()
   {
      return radiusZ;
   }

   /**
    * Returns the hemi-ellipsoid's resolution which can be used for discretizing it.
    * 
    * @return the hemi-ellipsoid's resolution.
    */
   public int getResolution()
   {
      return resolution;
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
      else if (object instanceof HemiEllipsoid3DDescription)
      {
         HemiEllipsoid3DDescription other = (HemiEllipsoid3DDescription) object;
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
      return EuclidCoreIOTools.getStringOf("Hemi-ellipsoid: [name: " + name
            + ", radii: (", "), resolution: " + resolution + "]", ", ", radiusX, radiusY, radiusZ);
   }
}
