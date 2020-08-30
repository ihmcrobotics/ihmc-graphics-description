package us.ihmc.graphicsDescription.instructions.listeners;

import us.ihmc.graphicsDescription.mesh.MeshDataHolder;

public interface MeshChangedListener
{
   public void meshChanged(MeshDataHolder newMesh);
}
