package us.ihmc.graphicsDescription.instructions;

import java.util.List;

import us.ihmc.euclid.tuple3D.Point3D;

public class PolygonGraphics3DInstruction extends PrimitiveGraphics3DInstruction
{
   private final List<Point3D> polygonPoints;

   public PolygonGraphics3DInstruction(List<Point3D> polygonPoints)
   {
      this.polygonPoints = polygonPoints;
   }

   public List<Point3D> getPolygonPoints()
   {
      return polygonPoints;
   }
}
