package us.ihmc.graphicsDescription.geometry;

import us.ihmc.graphicsDescription.HeightMap;

public class HeightMapDescription implements GeometryDescription
{
   private String name;
   private HeightMap heightMap;
   private int resolutionX;
   private int resolutionY;

   public HeightMapDescription()
   {
   }

   public HeightMapDescription(HeightMap heightMap, int resolutionX, int resolutionY)
   {
      setHeightMap(heightMap);
      setResolutionX(resolutionX);
      setResolutionY(resolutionY);
   }

   public void setName(String name)
   {
      this.name = name;
   }

   public void setHeightMap(HeightMap heightMap)
   {
      this.heightMap = heightMap;
   }

   public void setResolutionX(int resolutionX)
   {
      this.resolutionX = resolutionX;
   }

   public void setResolutionY(int resolutionY)
   {
      this.resolutionY = resolutionY;
   }

   public String getName()
   {
      return name;
   }

   public HeightMap getHeightMap()
   {
      return heightMap;
   }

   public int getResolutionX()
   {
      return resolutionX;
   }

   public int getResolutionY()
   {
      return resolutionY;
   }
}
