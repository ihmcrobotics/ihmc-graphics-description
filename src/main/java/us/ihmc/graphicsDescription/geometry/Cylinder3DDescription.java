package us.ihmc.graphicsDescription.geometry;

public class Cylinder3DDescription implements GeometryDescription
{
   private String name = "cylinder";
   private double height;
   private double radius;
   private boolean centered;
   private int resolution = 64;

   public Cylinder3DDescription()
   {
   }

   public Cylinder3DDescription(double height, double radius)
   {
      this.height = height;
      this.radius = radius;
   }

   public Cylinder3DDescription(double height, double radius, int resolution)
   {
      this.height = height;
      this.radius = radius;
      this.resolution = resolution;
   }

   public Cylinder3DDescription(double height, double radius, boolean centered)
   {
      this.height = height;
      this.radius = radius;
      this.centered = centered;
   }

   public Cylinder3DDescription(double height, double radius, boolean centered, int resolution)
   {
      this.height = height;
      this.radius = radius;
      this.centered = centered;
      this.resolution = resolution;
   }

   @Override
   public void setName(String name)
   {
      this.name = name;
   }

   public void setHeight(double height)
   {
      this.height = height;
   }

   public void setRadius(double radius)
   {
      this.radius = radius;
   }

   public void setCentered(boolean centered)
   {
      this.centered = centered;
   }

   public void setResolution(int resolution)
   {
      this.resolution = resolution;
   }

   @Override
   public String getName()
   {
      return name;
   }

   public double getHeight()
   {
      return height;
   }

   public double getRadius()
   {
      return radius;
   }

   public boolean isCentered()
   {
      return centered;
   }

   public int getResolution()
   {
      return resolution;
   }

   @Override
   public String toString()
   {
      return "Cylinder: [name: " + name + ", height: " + height + ", radius: " + radius + ", resolution: " + resolution + "]";
   }
}
