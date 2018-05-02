package us.ihmc.graphicsDescription.appearance;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.NotImplementedException;

import us.ihmc.graphicsDescription.color.MutableColor;


public class SDFAppearance extends YoAppearanceTransparency
{
   private final ArrayList<String> urls;
   private final String name;
   private final ArrayList<String> resourceDirectories = new ArrayList<String>();


   public SDFAppearance(ArrayList<String> urls, String name, List<String> resourceDirectories)
   {
      super();
      this.urls = urls;
      this.name = name;
      this.resourceDirectories.addAll(resourceDirectories);
   }

   public ArrayList<String> getUrls()
   {
      return urls;
   }

   public String getName()
   {
      return name;
   }
   
   public ArrayList<String> getResourceDirectories()
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
