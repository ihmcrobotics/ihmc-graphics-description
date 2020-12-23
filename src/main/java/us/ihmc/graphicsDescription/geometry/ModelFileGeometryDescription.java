package us.ihmc.graphicsDescription.geometry;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import us.ihmc.euclid.tuple3D.Vector3D;

/**
 * Description for creating a geometry from model file.
 */
public class ModelFileGeometryDescription implements GeometryDescription
{
   private String name;
   private String fileName;
   private List<SubMeshDescription> submeshes;
   private List<String> resourceDirectories;
   private ClassLoader resourceClassLoader;
   private Vector3D scale;

   /**
    * Creates an empty description. The parameters have to be all set before this description can be
    * used.
    */
   public ModelFileGeometryDescription()
   {
   }

   /**
    * Creates and initializes a description for loading a model file.
    * 
    * @param fileName the path to the model file.
    */
   public ModelFileGeometryDescription(String fileName)
   {
      setFileName(fileName);
   }

   public ModelFileGeometryDescription(ModelFileGeometryDescription other)
   {
      name = other.name;
      fileName = other.fileName;
      if (other.submeshes != null)
         submeshes = other.submeshes.stream().map(SubMeshDescription::new).collect(Collectors.toList());
      if (other.resourceDirectories != null)
         resourceDirectories = new ArrayList<>(other.resourceDirectories);
      resourceClassLoader = other.resourceClassLoader;
      if (other.scale != null)
         scale = new Vector3D(other.scale);
   }

   /** {@inheritDoc} */
   @Override
   public void setName(String name)
   {
      this.name = name;
   }

   /**
    * Sets the path to the model file.
    * 
    * @param fileName the path to the model file.
    */
   public void setFileName(String fileName)
   {
      this.fileName = fileName;
   }

   /**
    * Sets the list of sub-meshes to be loaded together with the main model file.
    * 
    * @param submeshes the list of the sub-meshes.
    */
   public void setSubmeshes(List<SubMeshDescription> submeshes)
   {
      this.submeshes = submeshes;
   }

   /**
    * Sets the list of directories containing resources that may be needed to load the model file.
    * 
    * @param resourceDirectories the list of the directories containing resources needed for the
    *                            loading the model file.
    */
   public void setResourceDirectories(List<String> resourceDirectories)
   {
      this.resourceDirectories = resourceDirectories;
   }

   /**
    * Sets the class loader to use for loading the resource(s).
    * 
    * @param resourceClassLoader the class loader to use with this model file.
    */
   public void setResourceClassLoader(ClassLoader resourceClassLoader)
   {
      this.resourceClassLoader = resourceClassLoader;
   }

   /**
    * Sets the scale to apply to the loaded mesh.
    * 
    * @param scale scale to apply to the loaded mesh.
    */
   public void setScale(Vector3D scale)
   {
      this.scale = scale;
   }

   /** {@inheritDoc} */
   @Override
   public String getName()
   {
      return name;
   }

   /**
    * Returns the path to the model file.
    * 
    * @return the path to the model file.
    */
   public String getFileName()
   {
      return fileName;
   }

   /**
    * Returns the list of sub-meshes to be loaded together with the main model file.
    * 
    * @return the list of sub-meshes to be loaded together with the main model file.
    */
   public List<SubMeshDescription> getSubmeshes()
   {
      return submeshes;
   }

   /**
    * Returns the list of directories containing resources that may be needed to load the model file.
    * 
    * @return the list of directories containing resources that may be needed to load the model file.
    */
   public List<String> getResourceDirectories()
   {
      return resourceDirectories;
   }

   /**
    * Returns the class loader to use for loading the resource(s).
    * 
    * @return the class loader to use for loading the resource(s).
    */
   public ClassLoader getResourceClassLoader()
   {
      return resourceClassLoader;
   }

   /**
    * Returns the scale to apply to the loaded mesh.
    * 
    * @return the scale to apply to the loaded mesh.
    */
   public Vector3D getScale()
   {
      return scale;
   }

   @Override
   public ModelFileGeometryDescription copy()
   {
      return new ModelFileGeometryDescription(this);
   }

   /**
    * Description of a sub-mesh to be loaded together with a main mesh.
    */
   public static class SubMeshDescription
   {
      private String name;
      private boolean center;

      /**
       * Creates an empty description. The parameters have to be all set before this description can be
       * used.
       */
      public SubMeshDescription()
      {
      }

      /**
       * Creates and initializes a description for loading a sub-mesh.
       * 
       * @param name   the name of the sub-mesh.
       * @param center whether to center this sub-mesh vertices at the origin or load the sub-mesh as is.
       */
      public SubMeshDescription(String name, boolean center)
      {
         this.name = name;
         this.center = center;
      }

      public SubMeshDescription(SubMeshDescription other)
      {
         name = other.name;
         center = other.center;
      }

      /**
       * Sets the name of the sub-mesh.
       * 
       * @param name the name of the sub-mesh.
       */
      public void setName(String name)
      {
         this.name = name;
      }

      /**
       * Sets whether the vertices of this sub-mesh should be centered at (0, 0, 0) which would
       * effectively remove any transformations specific to the sub-mesh.
       * 
       * @param center whether to center this sub-mesh vertices at the origin or load the sub-mesh as is.
       */
      public void setCenter(boolean center)
      {
         this.center = center;
      }

      /**
       * Returns the name of this sub-mesh.
       * 
       * @return the name of this sub-mesh.
       */
      public String getName()
      {
         return name;
      }

      /**
       * Returns whether the vertices of this sub-mesh should be centered at the origin or if the sub-mesh
       * should be loaded as is.
       * 
       * @return whether the vertices of this sub-mesh should be centered at the origin or if the sub-mesh
       *         should be loaded as is.
       */
      public boolean getCenter()
      {
         return center;
      }

      public SubMeshDescription copy()
      {
         return new SubMeshDescription(this);
      }
   }
}
