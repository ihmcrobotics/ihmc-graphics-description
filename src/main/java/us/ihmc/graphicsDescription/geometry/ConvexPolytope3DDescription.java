package us.ihmc.graphicsDescription.geometry;

import us.ihmc.euclid.shape.convexPolytope.ConvexPolytope3D;
import us.ihmc.euclid.shape.convexPolytope.interfaces.ConvexPolytope3DReadOnly;
import us.ihmc.euclid.tools.EuclidHashCodeTools;

/**
 * Description for creating a 3D convex polytope.
 */
public class ConvexPolytope3DDescription implements GeometryDescription
{
   private String name;
   private ConvexPolytope3DReadOnly convexPolytope;

   /**
    * Creates an empty description. The parameters have to be all set before this description can be
    * used.
    */
   public ConvexPolytope3DDescription()
   {
   }

   /**
    * Creates and initializes a description for a 3D convex polytope.
    * 
    * @param convexPolytope the convex polytope for this description.
    */
   public ConvexPolytope3DDescription(ConvexPolytope3DReadOnly convexPolytope)
   {
      this.convexPolytope = convexPolytope;
   }

   public ConvexPolytope3DDescription(ConvexPolytope3DDescription other)
   {
      name = other.name;
      if (other.convexPolytope != null)
         convexPolytope = new ConvexPolytope3D(other.convexPolytope);
   }

   /** {@inheritDoc} */
   @Override
   public void setName(String name)
   {
      this.name = name;
   }

   /**
    * Sets the convex polytope to associate with this description.
    * 
    * @param convexPolytope the convex polytope for this description.
    */
   public void setConvexPolytope(ConvexPolytope3DReadOnly convexPolytope)
   {
      this.convexPolytope = convexPolytope;
   }

   /** {@inheritDoc} */
   @Override
   public String getName()
   {
      return name;
   }

   /**
    * Returns the convex polytope associated with this description.
    * 
    * @return the convex polytope associated with this description.
    */
   public ConvexPolytope3DReadOnly getConvexPolytope()
   {
      return convexPolytope;
   }

   @Override
   public ConvexPolytope3DDescription copy()
   {
      return new ConvexPolytope3DDescription(this);
   }

   @Override
   public int hashCode()
   {
      long bits = 1L;
      bits = EuclidHashCodeTools.addToHashCode(bits, convexPolytope);
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
      else if (object instanceof ConvexPolytope3DDescription)
      {
         ConvexPolytope3DDescription other = (ConvexPolytope3DDescription) object;
         if (convexPolytope == null ? other.convexPolytope != null : !convexPolytope.equals(other.convexPolytope))
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
      return "Convex Polytope: [name: " + name + ", " + convexPolytope + "]";
   }
}
