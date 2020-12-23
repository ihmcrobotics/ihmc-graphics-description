package us.ihmc.graphicsDescription;

import us.ihmc.euclid.interfaces.Transformable;
import us.ihmc.euclid.transform.AffineTransform;
import us.ihmc.euclid.transform.interfaces.Transform;
import us.ihmc.graphicsDescription.appearance.MaterialDescription;
import us.ihmc.graphicsDescription.geometry.GeometryDescription;

/**
 * Description for creating a 3D graphic object with a pose, geometry, and material.
 */
public class VisualDescription implements Transformable
{
   private String name;
   private AffineTransform pose;
   private GeometryDescription geometry;
   private MaterialDescription material;

   /**
    * Creates an empty description. The parameters have to be all set before this description can be
    * used.
    */
   public VisualDescription()
   {
   }

   /**
    * Creates and initializes a description for creating a 3D graphic.
    * 
    * @param geometry the geometry of the visual.
    * @param material the material of the visual.
    */
   public VisualDescription(GeometryDescription geometry, MaterialDescription material)
   {
      this.geometry = geometry;
      this.material = material;
   }

   /**
    * Creates and initializes a description for creating a 3D graphic.
    *
    * @param pose     the pose of the visual.
    * @param geometry the geometry of the visual.
    * @param material the material of the visual.
    */
   public VisualDescription(AffineTransform pose, GeometryDescription geometry, MaterialDescription material)
   {
      this.pose = pose;
      this.geometry = geometry;
      this.material = material;
   }

   public VisualDescription(VisualDescription other)
   {
      pose = other.pose == null ? null : new AffineTransform(other.pose);
      geometry = other.geometry == null ? null : other.geometry.copy();
      material = other.material == null ? null : other.material.copy();
   }

   /**
    * Sets the name to associate to this visual. The name can be passed to objects or graphics created
    * using this description.
    * 
    * @param name the visual's name.
    */
   public void setName(String name)
   {
      this.name = name;
   }

   /**
    * Sets the pose for this visual.
    * 
    * @param pose the pose for this visual.
    */
   public void setPose(AffineTransform pose)
   {
      this.pose = pose;
   }

   /**
    * Sets the geometry for this visual.
    * 
    * @param geometry the geometry for this visual.
    */
   public void setGeometry(GeometryDescription geometry)
   {
      this.geometry = geometry;
   }

   /**
    * Sets the material for this visual.
    * 
    * @param material the material for this visual.
    */
   public void setMaterial(MaterialDescription material)
   {
      this.material = material;
   }

   /**
    * Returns this visual's name.
    * 
    * @return the name associated with this visual.
    */
   public String getName()
   {
      return name;
   }

   /**
    * Returns the pose of this visual.
    * 
    * @return the pose of this visual.
    */
   public AffineTransform getPose()
   {
      return pose;
   }

   /**
    * Returns the geometry of this visual.
    * 
    * @return the geometry of this visual.
    */
   public GeometryDescription getGeometry()
   {
      return geometry;
   }

   /**
    * Returns the material of this visual.
    * 
    * @return the material of this visual.
    */
   public MaterialDescription getMaterial()
   {
      return material;
   }

   @Override
   public void applyTransform(Transform transform)
   {
      transform.transform(pose);
   }

   @Override
   public void applyInverseTransform(Transform transform)
   {
      transform.inverseTransform(pose);
   }

   public VisualDescription copy()
   {
      return new VisualDescription(this);
   }
}
