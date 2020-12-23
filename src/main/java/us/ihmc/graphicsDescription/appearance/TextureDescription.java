package us.ihmc.graphicsDescription.appearance;

import java.awt.image.BufferedImage;
import java.net.URL;

import us.ihmc.graphicsDescription.geometry.ExtrusionDescription;

public class TextureDescription
{
   private URL textureFileURL;
   private BufferedImage textureImage;

   public TextureDescription()
   {
   }

   public TextureDescription(TextureDescription other)
   {
      textureFileURL = other.textureFileURL;
      textureImage = ExtrusionDescription.copyImage(other.textureImage);
   }

   public void setTextureFileURL(URL textureFileURL)
   {
      this.textureFileURL = textureFileURL;
   }

   public void setTextureImage(BufferedImage textureImage)
   {
      this.textureImage = textureImage;
   }

   public URL getTextureFileURL()
   {
      return textureFileURL;
   }

   public BufferedImage getTextureImage()
   {
      return textureImage;
   }

   public TextureDescription copy()
   {
      return new TextureDescription(this);
   }
}
