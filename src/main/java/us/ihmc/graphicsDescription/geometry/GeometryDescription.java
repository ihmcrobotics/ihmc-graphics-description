package us.ihmc.graphicsDescription.geometry;

/**
 * Base interfaces for classes that describe a geometry.
 */
public interface GeometryDescription
{
   /**
    * Sets the name to associate with this description. The name can be passed to objects or graphics
    * created using this description.
    * 
    * @param name the geometry's name.
    */
   void setName(String name);

   /**
    * Returns this geometry's name.
    * 
    * @return the name associated with this description.
    */
   String getName();

   GeometryDescription copy();
}
