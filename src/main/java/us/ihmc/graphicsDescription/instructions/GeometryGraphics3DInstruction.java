package us.ihmc.graphicsDescription.instructions;

import us.ihmc.graphicsDescription.geometry.GeometryDescription;

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
}
