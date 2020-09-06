package us.ihmc.graphicsDescription.instructions;

import us.ihmc.graphicsDescription.geometry.GeometryDescription;
import us.ihmc.graphicsDescription.mesh.MeshDataGenerator;
import us.ihmc.graphicsDescription.mesh.MeshDataHolder;

public class GeometryGraphics3DInstruction extends Graphics3DInstruction
{
   private GeometryDescription geometry;

   public GeometryGraphics3DInstruction()
   {
   }

   public GeometryGraphics3DInstruction(GeometryDescription geometry)
   {
      this.geometry = geometry;
   }

   public void setGeometry(GeometryDescription geometry)
   {
      this.geometry = geometry;
   }

   public GeometryDescription getGeometry()
   {
      return geometry;
   }

   public Graphics3DAddMeshDataInstruction toGraphics3DAddMeshDataInstruction()
   {
      MeshDataHolder mesh = MeshDataGenerator.Mesh(geometry);
      if (mesh == null)
         return null;
      else
         return new Graphics3DAddMeshDataInstruction(mesh, getAppearance());
   }
}
