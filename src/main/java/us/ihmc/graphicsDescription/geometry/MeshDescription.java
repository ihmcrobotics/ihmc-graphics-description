package us.ihmc.graphicsDescription.geometry;

import java.util.ArrayList;
import java.util.List;

import us.ihmc.graphicsDescription.mesh.MeshDataHolder;

public class MeshDescription implements GeometryDescription
{
   private String name;
   private MeshDataHolder mesh;
   private List<MeshChangedListener> listeners;

   public MeshDescription()
   {
   }

   public MeshDescription(MeshDataHolder mesh)
   {
      setMesh(mesh);
   }

   public MeshDescription(MeshDescription other)
   {
      setName(other.name);
      setMesh(other.mesh);
   }

   @Override
   public void setName(String name)
   {
      this.name = name;
   }

   public void setMesh(MeshDataHolder mesh)
   {

      if (listeners != null && !listeners.isEmpty())
      {
         MeshDescription oldValue = new MeshDescription(this);
         this.mesh = mesh;
         listeners.forEach(listener -> listener.onChange(oldValue, this));
      }
      else
      {
         this.mesh = mesh;
      }
   }

   public void addListener(MeshChangedListener listener)
   {
      if (listeners == null)
         listeners = new ArrayList<>();
      listeners.add(listener);
   }

   public boolean removeListener(MeshChangedListener listener)
   {
      if (listeners == null)
         return false;
      return listeners.remove(listener);
   }

   public void removeListeners()
   {
      listeners = null;
   }

   @Override
   public String getName()
   {
      return name;
   }

   public MeshDataHolder getMesh()
   {
      return mesh;
   }

   public static interface MeshChangedListener
   {
      void onChange(MeshDescription oldValue, MeshDescription newValue);
   }
}
