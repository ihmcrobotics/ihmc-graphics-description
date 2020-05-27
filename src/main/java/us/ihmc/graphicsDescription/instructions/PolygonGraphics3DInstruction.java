package us.ihmc.graphicsDescription.instructions;

import java.util.List;

import us.ihmc.euclid.tuple3D.interfaces.Point3DReadOnly;

public class PolygonGraphics3DInstruction extends PrimitiveGraphics3DInstruction
{
   private final List<? extends Point3DReadOnly> polygonPoints;

   public PolygonGraphics3DInstruction(List<? extends Point3DReadOnly> polygonPoints)
   {
      this.polygonPoints = polygonPoints;
   }

   public List<? extends Point3DReadOnly> getPolygonPoints()
   {
      return polygonPoints;
   }
}
