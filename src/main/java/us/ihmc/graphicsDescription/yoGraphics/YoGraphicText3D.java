package us.ihmc.graphicsDescription.yoGraphics;

import us.ihmc.euclid.referenceFrame.ReferenceFrame;
import us.ihmc.graphicsDescription.Graphics3DObject;
import us.ihmc.graphicsDescription.VisualDescription;
import us.ihmc.graphicsDescription.appearance.AppearanceDefinition;
import us.ihmc.graphicsDescription.appearance.MaterialDescription;
import us.ihmc.graphicsDescription.geometry.ExtrusionDescription;
import us.ihmc.yoVariables.euclid.referenceFrame.YoFramePoint3D;
import us.ihmc.yoVariables.euclid.referenceFrame.YoFrameYawPitchRoll;
import us.ihmc.yoVariables.registry.YoRegistry;

public class YoGraphicText3D extends YoGraphicAbstractShape
{
   private final Graphics3DObject graphics3dObject;
   private final ExtrusionDescription extrusionDescription;
   private final MaterialDescription materialDescription;
   private final String text;
   private final AppearanceDefinition appearance;

   public YoGraphicText3D(String name, String text, YoFramePoint3D framePoint, YoFrameYawPitchRoll orientation, double scale, AppearanceDefinition appearance)
   {
      super(name, framePoint, orientation, scale);
      this.text = text;
      this.appearance = appearance;
      graphics3dObject = new Graphics3DObject();
      graphics3dObject.setChangeable(true);
      extrusionDescription = new ExtrusionDescription(text, 20.0);
      materialDescription = new MaterialDescription(appearance.getColor());
      graphics3dObject.addVisualDescription(new VisualDescription(extrusionDescription, materialDescription));
   }

   public YoGraphicText3D(String name, String text, String namePrefix, String nameSuffix, YoRegistry registry, double scale, AppearanceDefinition appearance)
   {
      this(name, text, new YoFramePoint3D(namePrefix, nameSuffix, ReferenceFrame.getWorldFrame(), registry),
           new YoFrameYawPitchRoll(namePrefix, nameSuffix, ReferenceFrame.getWorldFrame(), registry), scale, appearance);
   }

   public void setAppearance(AppearanceDefinition appearance)
   {
      materialDescription.setDiffuseColor(appearance.getColor());
   }

   public void setText(String text)
   {
      extrusionDescription.setImage(ExtrusionDescription.textToImage(text));
   }

   @Override
   public Graphics3DObject getLinkGraphics()
   {
      return graphics3dObject;
   }

   @Override
   public YoGraphic duplicate(YoRegistry newRegistry)
   {
      return new YoGraphicText3D(getName(), text, yoFramePoint.duplicate(newRegistry), yoFrameYawPitchRoll.duplicate(newRegistry), scale, appearance);
   }
}
