package us.ihmc.graphicsDescription.geometry;

import us.ihmc.euclid.tools.EuclidHashCodeTools;

/**
 * Description for creating a regular 3D tetrahedron.
 * <p>
 * It is assumed that the tetrahedron is centered at the origin.
 * </p>
 */
public class Tetrahedron3DDescription implements GeometryDescription
{
   private String name = "tetrahedron";
   private double edgeLength;

   /**
    * Creates an empty description. The parameters have to be all set before this description can be
    * used.
    */
   public Tetrahedron3DDescription()
   {
   }

   /**
    * Creates and initializes a description for a 3D tetrahedron.
    *
    * @param edgeLength the length for all edges.
    */
   public Tetrahedron3DDescription(double edgeLength)
   {
      this.edgeLength = edgeLength;
   }

   public Tetrahedron3DDescription(Tetrahedron3DDescription other)
   {
      name = other.name;
      edgeLength = other.edgeLength;
   }

   /** {@inheritDoc} */
   @Override
   public void setName(String name)
   {
      this.name = name;
   }

   /**
    * Sets the length for all edges.
    *
    * @param edgeLength the length for all edges.
    */
   public void setEdgeLength(double edgeLength)
   {
      this.edgeLength = edgeLength;
   }

   /** {@inheritDoc} */
   @Override
   public String getName()
   {
      return name;
   }

   /**
    * Returns the length for all edges.
    * 
    * @return the length for all edges.
    */
   public double getEdgeLength()
   {
      return edgeLength;
   }

   @Override
   public Tetrahedron3DDescription copy()
   {
      return new Tetrahedron3DDescription(this);
   }

   @Override
   public int hashCode()
   {
      long bits = 1L;
      bits = EuclidHashCodeTools.addToHashCode(bits, edgeLength);
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
      else if (object instanceof Tetrahedron3DDescription)
      {
         Tetrahedron3DDescription other = (Tetrahedron3DDescription) object;
         if (edgeLength != other.edgeLength)
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
      return "Tetrahedron: [name: " + name + ", edge length: " + edgeLength + "]";
   }
}
