package us.ihmc.graphicsDescription.appearance;

import org.apache.commons.lang3.NotImplementedException;

import us.ihmc.graphicsDescription.color.ColorDescription;

public abstract class YoAppearanceTransparency implements AppearanceDefinition
{
   private double transparency = 0.0;

   @Override
   public final double getTransparency()
   {
      return transparency;
   }

   @Override
   public void setTransparency(double transparency)
   {
      this.transparency = transparency;
   }

   @Override
   public ColorDescription getColor()
   {
      throw new NotImplementedException("getColor() is not implemented");
   }
}
