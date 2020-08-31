package us.ihmc.graphicsDescription.geometry;

import java.util.List;

import us.ihmc.euclid.tools.EuclidCoreIOTools;
import us.ihmc.euclid.tuple2D.Point2D;

public class ExtrudedPolygon2DDescription implements GeometryDescription
{
   private String name = "extrudedPolygon2D";
   private List<Point2D> polygonVertices;
   private boolean counterClockwiseOrdered;
   private double topZ, bottomZ;

   public ExtrudedPolygon2DDescription()
   {
   }

   public ExtrudedPolygon2DDescription(List<Point2D> polygonVertices, boolean counterClockwiseOrdered, double extrusionHeight)
   {
      this.polygonVertices = polygonVertices;
      this.counterClockwiseOrdered = counterClockwiseOrdered;
      this.topZ = extrusionHeight;
      this.bottomZ = 0.0;
   }

   public ExtrudedPolygon2DDescription(List<Point2D> polygonVertices, boolean counterClockwiseOrdered, double topZ, double bottomZ)
   {
      this.polygonVertices = polygonVertices;
      this.counterClockwiseOrdered = counterClockwiseOrdered;
      this.topZ = topZ;
      this.bottomZ = bottomZ;
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

   public void setTopZ(double topZ)
   {
      this.topZ = topZ;
   }

   public void setBottomZ(double bottomZ)
   {
      this.bottomZ = bottomZ;
   }

   public void setExtrusionHeight(double extrusionHeight)
   {
      topZ = extrusionHeight;
      bottomZ = 0.0;
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

   public double getTopZ()
   {
      return topZ;
   }

   public double getBottomZ()
   {
      return bottomZ;
   }

   @Override
   public String toString()
   {
      String ordering = counterClockwiseOrdered ? "counter-clockwise" : "clockwise";
      return "Extruded Polygon 2D: [name: " + name + ", " + ordering + ", vertices: "
            + EuclidCoreIOTools.getCollectionString("[", "]", ", ", polygonVertices, Object::toString);
   }
}
