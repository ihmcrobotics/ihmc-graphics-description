package us.ihmc.graphicsDescription.appearance;

import java.awt.image.BufferedImage;
import java.net.URL;

import us.ihmc.graphicsDescription.geometry.ExtrusionDescription;

public class TextureDescription
{
   private URL textureFileURL;
   private String textureFilename;
   private BufferedImage textureImage;

   public TextureDescription()
   {
   }

   public TextureDescription(TextureDescription other)
   {
      textureFileURL = other.textureFileURL;
      textureFilename = other.textureFilename;
      textureImage = ExtrusionDescription.copyImage(other.textureImage);
   }

   public void setTextureFileURL(URL textureFileURL)
   {
      this.textureFileURL = textureFileURL;
   }

   public void setTextureFilename(String textureFilename)
   {
      this.textureFilename = textureFilename;
   }

   public void setTextureImage(BufferedImage textureImage)
   {
      this.textureImage = textureImage;
   }

   public URL getTextureFileURL()
   {
      return textureFileURL;
   }

   public String getTextureFilename()
   {
      return textureFilename;
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
