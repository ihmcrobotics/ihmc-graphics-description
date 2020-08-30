package us.ihmc.graphicsDescription.geometry;

import java.util.List;

import us.ihmc.euclid.tools.EuclidCoreIOTools;
import us.ihmc.euclid.tuple3D.Point3D;

public class Polygon3DDescription implements GeometryDescription
{
   private String name = "polygon3D";
   private List<Point3D> polygonVertices;
   private boolean counterClockwiseOrdered;

   public Polygon3DDescription(List<Point3D> polygonVertices, boolean counterClockwiseOrdered)
   {
      this.polygonVertices = polygonVertices;
      this.counterClockwiseOrdered = counterClockwiseOrdered;
   }

   @Override
   public void setName(String name)
   {
      this.name = name;
   }

   public void setPolygonVertices(List<Point3D> polygonVertices)
   {
      this.polygonVertices = polygonVertices;
   }

   public void setCounterClockwiseOrdered(boolean counterClockwiseOrdered)
   {
      this.counterClockwiseOrdered = counterClockwiseOrdered;
   }

   @Override
   public String getName()
   {
      return name;
   }

   public List<Point3D> getPolygonVertices()
   {
      return polygonVertices;
   }

   public boolean isCounterClockwiseOrdered()
   {
      return counterClockwiseOrdered;
   }

   @Override
   public String toString()
   {
      String ordering = counterClockwiseOrdered ? "counter-clockwise" : "clockwise";
      return "Polygon 3D: [name: " + name + ", " + ordering + ", vertices: "
            + EuclidCoreIOTools.getCollectionString("[", "]", ", ", polygonVertices, Object::toString);
   }
}
