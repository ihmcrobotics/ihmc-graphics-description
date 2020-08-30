package us.ihmc.graphicsDescription.geometry;

public class Tetrahedron3DDescription implements GeometryDescription
{
   private String name = "tetrahedron";
   private double edgeLength;

   public Tetrahedron3DDescription()
   {
   }

   public Tetrahedron3DDescription(double edgeLength)
   {
      this.edgeLength = edgeLength;
   }

   @Override
   public void setName(String name)
   {
      this.name = name;
   }

   public void setEdgeLength(double edgeLength)
   {
      this.edgeLength = edgeLength;
   }

   @Override
   public String getName()
   {
      return name;
   }

   public double getEdgeLength()
   {
      return edgeLength;
   }

   @Override
   public String toString()
   {
      return "Tetrahedron: [name: " + name + ", edge length: " + edgeLength + "]";
   }
}
