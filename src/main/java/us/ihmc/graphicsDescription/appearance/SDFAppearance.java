package us.ihmc.graphicsDescription.appearance;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.NotImplementedException;

import us.ihmc.graphicsDescription.color.MutableColor;

public class SDFAppearance extends YoAppearanceTransparency
{
   private final List<String> urls;
   private final String name;
   private final List<String> resourceDirectories = new ArrayList<>();

   public SDFAppearance(List<String> urls, String name, List<String> resourceDirectories)
   {
      super();
      this.urls = urls;
      this.name = name;
      this.resourceDirectories.addAll(resourceDirectories);
   }

   public List<String> getUrls()
   {
      return urls;
   }

   public String getName()
   {
      return name;
   }

   public List<String> getResourceDirectories()
   {
      return resourceDirectories;
   }

   @Override
   public MutableColor getColor()
   {
      throw new NotImplementedException("getColor() is not implemented");
   }

   @Override
   public Color getAwtColor()
   {
      throw new NotImplementedException("getAwtColor() is not implemented");
   }
}
