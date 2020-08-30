package us.ihmc.graphicsDescription.geometry;

import java.util.List;

import us.ihmc.euclid.tools.EuclidCoreIOTools;
import us.ihmc.euclid.tuple2D.Point2D;

public class Polygon2DDescription implements GeometryDescription
{
   private String name = "polygon2D";
   private List<Point2D> polygonVertices;
   private boolean counterClockwiseOrdered;

   public Polygon2DDescription(List<Point2D> polygonVertices, boolean counterClockwiseOrdered)
   {
      this.polygonVertices = polygonVertices;
      this.counterClockwiseOrdered = counterClockwiseOrdered;
   }

   @Override
   public void setName(String name)
   {
      this.name = name;
   }

   public void setPolygonVertices(List<Point2D> polygonVertices)
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

   public List<Point2D> getPolygonVertices()
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
      return "Polygon 2D: [name: " + name + ", " + ordering + ", vertices: "
            + EuclidCoreIOTools.getCollectionString("[", "]", ", ", polygonVertices, Object::toString);
   }
}
