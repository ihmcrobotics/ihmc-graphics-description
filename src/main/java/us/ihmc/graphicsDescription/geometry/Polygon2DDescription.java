package us.ihmc.graphicsDescription.geometry;

import java.util.List;

import us.ihmc.euclid.tools.EuclidCoreIOTools;
import us.ihmc.euclid.tools.EuclidHashCodeTools;
import us.ihmc.euclid.tuple2D.Point2D;

/**
 * Description for creating a 2D polygon.
 * <p>
 * It is assumed that the polygon is convex.
 * </p>
 */
public class Polygon2DDescription implements GeometryDescription
{
   private String name = "polygon2D";
   private List<Point2D> polygonVertices;
   private boolean counterClockwiseOrdered;

   /**
    * Creates an empty description. The parameters have to be all set before this description can be
    * used.
    */
   public Polygon2DDescription()
   {
   }

   /**
    * Creates and initializes a description for a 2D polygon.
    * 
    * @param polygonVertices         the 2D polygon's vertices.
    * @param counterClockwiseOrdered indicate the winding of the polygon: {@code true} if the polygon
    *                                is counter clockwise, {@code false} if clockwise.
    */
   public Polygon2DDescription(List<Point2D> polygonVertices, boolean counterClockwiseOrdered)
   {
      this.polygonVertices = polygonVertices;
      this.counterClockwiseOrdered = counterClockwiseOrdered;
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

   @Override
   public int hashCode()
   {
      long bits = 1L;
      bits = EuclidHashCodeTools.addToHashCode(bits, polygonVertices);
      bits = EuclidHashCodeTools.addToHashCode(bits, counterClockwiseOrdered);
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
      else if (object instanceof Polygon2DDescription)
      {
         Polygon2DDescription other = (Polygon2DDescription) object;
         if (polygonVertices == null ? other.polygonVertices != null : !polygonVertices.equals(other.polygonVertices))
            return false;
         if (counterClockwiseOrdered != other.counterClockwiseOrdered)
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
      return "Polygon 2D: [name: " + name + ", " + ordering + ", vertices: "
            + EuclidCoreIOTools.getCollectionString("[", "]", ", ", polygonVertices, Object::toString);
   }
}
