package us.ihmc.graphicsDescription.yoGraphics;

import us.ihmc.euclid.referenceFrame.ReferenceFrame;
import us.ihmc.graphicsDescription.Graphics3DObject;
import us.ihmc.graphicsDescription.appearance.AppearanceDefinition;
import us.ihmc.yoVariables.euclid.referenceFrame.YoFramePoint3D;
import us.ihmc.yoVariables.euclid.referenceFrame.YoFramePoseUsingYawPitchRoll;
import us.ihmc.yoVariables.euclid.referenceFrame.YoFrameYawPitchRoll;
import us.ihmc.yoVariables.registry.YoRegistry;

public class YoGraphicShape extends YoGraphicAbstractShape
{
   private final Graphics3DObject linkGraphics;

   public YoGraphicShape(String name, Graphics3DObject linkGraphics, YoFramePoseUsingYawPitchRoll framePose, double scale)
   {
      this(name, linkGraphics, framePose.getPosition(), framePose.getYawPitchRoll(), scale);
   }

   public YoGraphicShape(String name, Graphics3DObject linkGraphics, YoFramePoint3D framePoint, YoFrameYawPitchRoll frameOrientation, double scale)
   {
      super(name, framePoint, frameOrientation, scale);

      this.linkGraphics = linkGraphics;
   }

   public YoGraphicShape(String name, Graphics3DObject linkGraphics, String namePrefix, String nameSuffix, YoRegistry registry, double scale,
                         AppearanceDefinition appearance)
   {
      this(name, linkGraphics, new YoFramePoint3D(namePrefix, nameSuffix, ReferenceFrame.getWorldFrame(), registry),
           new YoFrameYawPitchRoll(namePrefix, nameSuffix, ReferenceFrame.getWorldFrame(), registry), scale);
   }

   @Override
   public Graphics3DObject getLinkGraphics()
   {
      return linkGraphics;
   }

   @Override
   public YoGraphic duplicate(YoRegistry newRegistry)
   {
      return new YoGraphicShape(getName(), getLinkGraphics(), yoFramePoint.duplicate(newRegistry), yoFrameYawPitchRoll.duplicate(newRegistry), scale);
   }
}
