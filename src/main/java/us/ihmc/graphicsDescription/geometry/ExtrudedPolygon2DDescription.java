package us.ihmc.graphicsDescription.geometry;

import java.util.List;
import java.util.stream.Collectors;

import us.ihmc.euclid.tools.EuclidCoreIOTools;
import us.ihmc.euclid.tools.EuclidHashCodeTools;
import us.ihmc.euclid.tuple2D.Point2D;

/**
 * Description for creating an extruded 2D polygon.
 * <p>
 * It is assumed that the polygon is convex and is described in the xy-plane and extruded along the
 * z-axis.
 * </p>
 */
public class ExtrudedPolygon2DDescription implements GeometryDescription
{
   private String name = "extrudedPolygon2D";
   private List<Point2D> polygonVertices;
   private boolean counterClockwiseOrdered;
   private double topZ, bottomZ;

   /**
    * Creates an empty description. The parameters have to be all set before this description can be
    * used.
    */
   public ExtrudedPolygon2DDescription()
   {
   }

   /**
    * Creates and initializes a description for an extruded 2D polygon.
    * 
    * @param polygonVertices         the 2D polygon's vertices.
    * @param counterClockwiseOrdered indicate the winding of the polygon: {@code true} if the polygon
    *                                is counter clockwise, {@code false} if clockwise.
    * @param extrusionHeight         the thickness of the extrusion along the z-axis.
    */
   public ExtrudedPolygon2DDescription(List<Point2D> polygonVertices, boolean counterClockwiseOrdered, double extrusionHeight)
   {
      this.polygonVertices = polygonVertices;
      this.counterClockwiseOrdered = counterClockwiseOrdered;
      this.topZ = extrusionHeight;
      this.bottomZ = 0.0;
   }

   /**
    * Creates and initializes a description for an extruded 2D polygon.
    * 
    * @param polygonVertices         the 2D polygon's vertices.
    * @param counterClockwiseOrdered indicate the winding of the polygon: {@code true} if the polygon
    *                                is counter clockwise, {@code false} if clockwise.
    * @param topZ                    the z-coordinate of the top face of the extrusion.
    * @param bottomZ                 the z-coordinate of the bottom face of the extrusion.
    */
   public ExtrudedPolygon2DDescription(List<Point2D> polygonVertices, boolean counterClockwiseOrdered, double topZ, double bottomZ)
   {
      this.polygonVertices = polygonVertices;
      this.counterClockwiseOrdered = counterClockwiseOrdered;
      this.topZ = topZ;
      this.bottomZ = bottomZ;
   }

   public ExtrudedPolygon2DDescription(ExtrudedPolygon2DDescription other)
   {
      name = other.name;
      if (other.polygonVertices != null)
         polygonVertices = other.polygonVertices.stream().map(Point2D::new).collect(Collectors.toList());
      counterClockwiseOrdered = other.counterClockwiseOrdered;
      topZ = other.topZ;
      bottomZ = other.bottomZ;
   }

   /** {@inheritDoc} */
   @Override
   public void setName(String name)
   {
      this.name = name;
   }

   /**
    * Sets the polygon's vertices.
    * 
    * @param polygonVertices the polygon's vertices.
    */
   public void setPolygonVertices(List<Point2D> polygonVertices)
   {
      this.polygonVertices = polygonVertices;
   }

   /**
    * Sets the flag for indicating the winding of the polygon.
    * 
    * @param counterClockwiseOrdered indicate the winding of the polygon: {@code true} if the polygon
    *                                is counter clockwise, {@code false} if clockwise.
    */
   public void setCounterClockwiseOrdered(boolean counterClockwiseOrdered)
   {
      this.counterClockwiseOrdered = counterClockwiseOrdered;
   }

   /**
    * Sets the z-coordinate for the top face of the extrusion.
    * 
    * @param topZ the z-coordinate of the top face of the extrusion.
    */
   public void setTopZ(double topZ)
   {
      this.topZ = topZ;
   }

   /**
    * Sets the z-coordinate for the bottom face of the extrusion.
    * 
    * @param bottomZ the z-coordinate of the bottom face of the extrusion.
    */
   public void setBottomZ(double bottomZ)
   {
      this.bottomZ = bottomZ;
   }

   /**
    * Sets the thickness of the extrusion and sets the z-coordinate of the bottom face of the extrusion
    * to zero.
    * 
    * @param extrusionHeight the thickness of the extrusion along the z-axis.
    */
   public void setExtrusionHeight(double extrusionHeight)
   {
      topZ = extrusionHeight;
      bottomZ = 0.0;
   }

   /** {@inheritDoc} */
   @Override
   public String getName()
   {
      return name;
   }

   /**
    * Returns the polygon's vertices.
    * 
    * @return the polygon's vertices.
    */
   public List<Point2D> getPolygonVertices()
   {
      return polygonVertices;
   }

   /**
    * Returns the winding of the polygon.
    * 
    * @return {@code true} if the polygon is counter clockwise, {@code false} if clockwise.
    */
   public boolean isCounterClockwiseOrdered()
   {
      return counterClockwiseOrdered;
   }

   /**
    * Returns the z-coordinate of the top face of the extrusion.
    * 
    * @return the z-coordinate of the top face of the extrusion.
    */
   public double getTopZ()
   {
      return topZ;
   }

   /**
    * Returns the z-coordinate of the bottom face of the extrusion.
    * 
    * @return the z-coordinate of the bottom face of the extrusion.
    */
   public double getBottomZ()
   {
      return bottomZ;
   }

   @Override
   public ExtrudedPolygon2DDescription copy()
   {
      return new ExtrudedPolygon2DDescription(this);
   }

   @Override
   public int hashCode()
   {
      long bits = 1L;
      bits = EuclidHashCodeTools.addToHashCode(bits, polygonVertices);
      bits = EuclidHashCodeTools.addToHashCode(bits, counterClockwiseOrdered);
      bits = EuclidHashCodeTools.addToHashCode(bits, topZ);
      bits = EuclidHashCodeTools.addToHashCode(bits, bottomZ);
      bits = EuclidHashCodeTools.addToHashCode(bits, name);
      return EuclidHashCodeTools.toIntHashCode(bits);
   }

   @Override
   public boolean equals(Object object)
   {
      if (object == this)
      {
         return true;
      }
      else if (object instanceof ExtrudedPolygon2DDescription)
      {
         ExtrudedPolygon2DDescription other = (ExtrudedPolygon2DDescription) object;
         if (polygonVertices == null ? other.polygonVertices != null : !polygonVertices.equals(other.polygonVertices))
            return false;
         if (counterClockwiseOrdered != other.counterClockwiseOrdered)
            return false;
         if (topZ != other.topZ)
            return false;
         if (bottomZ != other.bottomZ)
            return false;
         if (name == null ? other.name != null : !name.equals(other.name))
            return false;
         return true;
      }
      else
      {
         return false;
      }
   }

   @Override
   public String toString()
   {
      String ordering = counterClockwiseOrdered ? "counter-clockwise" : "clockwise";
      return "Extruded Polygon 2D: [name: " + name + ", " + ordering + ", vertices: "
            + EuclidCoreIOTools.getCollectionString("[", "]", ", ", polygonVertices, Object::toString);
   }
}
