package us.ihmc.graphicsDescription.geometry;

import us.ihmc.euclid.shape.convexPolytope.interfaces.ConvexPolytope3DReadOnly;

public class ConvexPolytope3DDescription implements GeometryDescription
{
   private String name;
   private ConvexPolytope3DReadOnly convexPolytope;

   public ConvexPolytope3DDescription()
   {
   }

   public ConvexPolytope3DDescription(ConvexPolytope3DReadOnly convexPolytope)
   {
      this.convexPolytope = convexPolytope;
   }

   public void setConvexPolytope(ConvexPolytope3DReadOnly convexPolytope)
   {
      this.convexPolytope = convexPolytope;
   }

   public ConvexPolytope3DReadOnly getConvexPolytope()
   {
      return convexPolytope;
   }

   @Override
   public void setName(String name)
   {
      this.name = name;
   }

   @Override
   public String getName()
   {
      return name;
   }

   @Override
   public String toString()
   {
      return "Convex Polytope: [name: " + name + ", " + convexPolytope + "]";
   }
}
