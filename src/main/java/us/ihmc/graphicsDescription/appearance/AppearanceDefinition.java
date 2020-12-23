package us.ihmc.graphicsDescription.appearance;

import java.awt.Color;

import us.ihmc.graphicsDescription.color.ColorDescription;

public interface AppearanceDefinition
{
   public void setTransparency(double transparancy);

   public double getTransparency();

   public ColorDescription getColor();

   public Color getAwtColor();
}
