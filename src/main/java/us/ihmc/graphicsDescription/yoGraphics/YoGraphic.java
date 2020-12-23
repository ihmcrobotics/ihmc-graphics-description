package us.ihmc.graphicsDescription.yoGraphics;

import us.ihmc.euclid.transform.AffineTransform;
import us.ihmc.graphicsDescription.Graphics3DObject;
import us.ihmc.graphicsDescription.plotting.artifact.Artifact;
import us.ihmc.yoVariables.providers.DoubleProvider;
import us.ihmc.yoVariables.registry.YoRegistry;

public abstract class YoGraphic
{
   private final String name;

   private boolean showGraphicObject = true;
   private final AffineTransform transform = new AffineTransform();
   protected DoubleProvider globalScaleProvider;

   protected abstract void computeRotationTranslation(AffineTransform transform3D);

   protected abstract boolean containsNaN();

   public abstract Graphics3DObject getLinkGraphics();

   public abstract Artifact createArtifact();

   public YoGraphic(String name)
   {
      this.name = name;
   }

   public void setGlobalScaleProvider(DoubleProvider globalScaleProvider)
   {
      this.globalScaleProvider = globalScaleProvider;
   }

   public void showGraphicObject()
   {
      showGraphicObject = true;
   }

   public void hideGraphicObject()
   {
      showGraphicObject = false;
   }

   public void setVisible(boolean visible)
   {
      showGraphicObject = visible;
   }

   public boolean isGraphicObjectShowing()
   {
      return showGraphicObject;
   }

   public String getName()
   {
      return name;
   }

   public final AffineTransform getTransform()
   {
      if (showGraphicObject && !containsNaN())
      {
         computeRotationTranslation(transform);
      }
      else
      {
         transform.setToZero();
      }

      return transform;
   }

   /**
    * Overwrite the update method if an object needs special updating. But it is up to each user to
    * update their objects. The internals only update the position and orientation transform... This is
    * generally use to update YoGraphics based on non-yovariablized data (referenceframes etc). Not
    * doing this will break rewind-playback.
    */
   public void update()
   {
   }

   public abstract YoGraphic duplicate(YoRegistry newRegistry);

   @Override
   public String toString()
   {
      return name;
   }
}
