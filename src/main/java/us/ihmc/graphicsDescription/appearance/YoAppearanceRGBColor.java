package us.ihmc.graphicsDescription.appearance;

import java.awt.Color;

import us.ihmc.graphicsDescription.color.ColorDescription;

public class YoAppearanceRGBColor extends YoAppearanceTransparency
{
   private final ColorDescription color;
   private final Color awtColor;

   public YoAppearanceRGBColor(ColorDescription color, double transparency)
   {
      this.color = new ColorDescription(color);
      awtColor = new Color((float) this.color.getRed(), (float) this.color.getGreen(), (float) this.color.getBlue(), (float) (1.0 - transparency));
      setTransparency(transparency);
   }

   public YoAppearanceRGBColor(Color color, double transparency)
   {
      this.color = ColorDescription.rgb(color.getRGB());
      this.color.setAlpha(1.0 - transparency);
      awtColor = new Color(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, (float) (1.0 - transparency));
      setTransparency(transparency);
   }

   public YoAppearanceRGBColor(float red, float green, float blue, double transparency)
   {
      color = new ColorDescription(red, green, blue);
      awtColor = new Color(red, green, blue, (float) (1.0 - transparency));
      setTransparency(transparency);
   }

   public YoAppearanceRGBColor(double red, double green, double blue, double transparency)
   {
      color = new ColorDescription((float) red, (float) green, (float) blue);
      awtColor = new Color((float) red, (float) green, (float) blue, (float) (1.0 - transparency));
      setTransparency(transparency);
   }

   public double getRed()
   {
      return color.getRed();
   }

   public double getGreen()
   {
      return color.getGreen();
   }

   public double getBlue()
   {
      return color.getBlue();
   }

   @Override
   public ColorDescription getColor()
   {
      return color;
   }

   @Override
   public Color getAwtColor()
   {
      return awtColor;
   }

   @Override
   public String toString()
   {
      return "YoAppearanceRGBColor{" + "color=" + color + '}';
   }
}
