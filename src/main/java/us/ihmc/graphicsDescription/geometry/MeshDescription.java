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
      notifyListeners(true, false);
   }

   public void setMesh(MeshDataHolder mesh)
   {
      this.mesh = mesh;
      notifyListeners(false, true);
   }

   private void notifyListeners(boolean wasNameChanged, boolean wasMeshDataChanged)
   {
      if (listeners == null || listeners.isEmpty())
         return;
      Change change = new ChangeImpl(wasNameChanged, wasMeshDataChanged, this);
      listeners.forEach(listener -> listener.onChange(change));
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
      void onChange(Change change);
   }

   public static interface Change
   {
      boolean wasNameChanged();

      boolean wasMeshDataChanged();

      MeshDescription getMeshDescription();
   }

   private static class ChangeImpl implements Change
   {
      private final boolean wasNameChanged;
      private final boolean wasMeshDataChanged;
      private final MeshDescription meshDescription;

      private ChangeImpl(boolean wasNameChanged, boolean wasMeshDataChanged, MeshDescription meshDescription)
      {
         this.wasNameChanged = wasNameChanged;
         this.wasMeshDataChanged = wasMeshDataChanged;
         this.meshDescription = meshDescription;
      }

      public boolean wasNameChanged()
      {
         return wasNameChanged;
      }

      public boolean wasMeshDataChanged()
      {
         return wasMeshDataChanged;
      }

      public MeshDescription getMeshDescription()
      {
         return meshDescription;
      }
   }

   @Override
   public MeshDescription copy()
   {
      return new MeshDescription(this);
   }
}
