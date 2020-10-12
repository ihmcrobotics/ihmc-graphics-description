package us.ihmc.graphicsDescription.appearance;

import java.awt.image.BufferedImage;
import java.net.URL;

public class TextureDescription
{
   private URL textureFileURL;
   private BufferedImage textureImage;

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
}
