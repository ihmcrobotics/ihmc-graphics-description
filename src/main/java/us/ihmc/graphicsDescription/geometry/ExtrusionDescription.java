package us.ihmc.graphicsDescription.geometry;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class ExtrusionDescription implements GeometryDescription
{
   private String name;
   private BufferedImage image;
   private double thickness;
   private List<ExtrusionChangedListener> listeners;

   public ExtrusionDescription(String text, double thickness)
   {
      this(textToImage(text), thickness);
   }

   public ExtrusionDescription(BufferedImage image, double thickness)
   {
      this.image = image;
      this.thickness = thickness;
   }

   public ExtrusionDescription(ExtrusionDescription other)
   {
      name = other.name;
      image = other.image;
      thickness = other.thickness;
   }

   public static BufferedImage textToImage(String text)
   {
      return textToImage(text, new Font("Lucida Sans", Font.PLAIN, 40));
   }

   public static BufferedImage textToImage(String text, Font font)
   {
      BufferedImage measurementImage = new BufferedImage(1, 1, BufferedImage.TYPE_3BYTE_BGR);
      Graphics2D measurementGraphics = measurementImage.createGraphics();
      FontRenderContext fontRenderContext = measurementGraphics.getFontRenderContext();

      Rectangle2D bounds = font.getStringBounds(text, fontRenderContext);

      int width = (int) bounds.getWidth();
      int height = (int) bounds.getHeight();

      BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
      Graphics graphics = bufferedImage.getGraphics();

      graphics.setColor(Color.white);
      graphics.fillRect(0, 0, width, height);
      graphics.setColor(Color.black);
      graphics.setFont(font);
      graphics.drawString(text, 0, (int) (height + bounds.getCenterY()));
      graphics.dispose();

      return bufferedImage;
   }

   @Override
   public void setName(String name)
   {
      this.name = name;
   }

   public void setImage(BufferedImage image)
   {
      if (listeners != null && !listeners.isEmpty())
      {
         ExtrusionDescription oldValue = new ExtrusionDescription(this);
         this.image = image;
         listeners.forEach(listener -> listener.onChange(oldValue, this));
      }
      else
      {
         this.image = image;
      }
   }

   public void setThickness(double thickness)
   {
      if (listeners != null && !listeners.isEmpty())
      {
         ExtrusionDescription oldValue = new ExtrusionDescription(this);
         this.thickness = thickness;
         listeners.forEach(listener -> listener.onChange(oldValue, this));
      }
      else
      {
         this.thickness = thickness;
      }
   }

   public void addListener(ExtrusionChangedListener listener)
   {
      if (listeners == null)
         listeners = new ArrayList<>();
      listeners.add(listener);
   }

   public boolean removeListener(ExtrusionChangedListener listener)
   {
      if (listeners == null)
         return false;
      return listeners.remove(listener);
   }

   public void removeListeners()
   {
      listeners = null;
   }

   @Override
   public String getName()
   {
      return name;
   }

   public BufferedImage getImage()
   {
      return image;
   }

   public double getThickness()
   {
      return thickness;
   }

   public static interface ExtrusionChangedListener
   {
      void onChange(ExtrusionDescription oldValue, ExtrusionDescription newValue);
   }
}
