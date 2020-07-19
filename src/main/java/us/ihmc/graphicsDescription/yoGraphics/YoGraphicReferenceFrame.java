package us.ihmc.graphicsDescription.yoGraphics;

import us.ihmc.euclid.referenceFrame.ReferenceFrame;
import us.ihmc.graphicsDescription.appearance.AppearanceDefinition;
import us.ihmc.yoVariables.registry.YoRegistry;
import us.ihmc.yoVariables.variable.YoVariable;

public class YoGraphicReferenceFrame extends YoGraphicCoordinateSystem
{
   private final ReferenceFrame referenceFrame;

   public YoGraphicReferenceFrame(ReferenceFrame referenceFrame, YoRegistry registry, boolean useYawPitchRoll, double scale)
   {
      this(referenceFrame, registry, useYawPitchRoll, scale, YoGraphicCoordinateSystem.DEFAULT_APPEARANCE);
   }

   public YoGraphicReferenceFrame(ReferenceFrame referenceFrame, YoRegistry registry, boolean useYawPitchRoll, double scale,
                                  AppearanceDefinition arrowColor)
   {
      super(referenceFrame.getName(), "", registry, useYawPitchRoll, scale, arrowColor);

      this.referenceFrame = referenceFrame;
   }

   public YoGraphicReferenceFrame(String prefix, ReferenceFrame referenceFrame, YoRegistry registry, boolean useYawPitchRoll, double scale,
                                  AppearanceDefinition arrowColor)
   {
      super(prefix + referenceFrame.getName(), "", registry, useYawPitchRoll, scale, arrowColor);

      this.referenceFrame = referenceFrame;
   }

   static YoGraphicReferenceFrame createAsRemoteYoGraphic(String name, YoVariable[] yoVariables, double[] constants)
   {
      return new YoGraphicReferenceFrame(name, yoVariables, constants);
   }

   private YoGraphicReferenceFrame(String name, YoVariable[] yoVariables, double[] constants)
   {
      super(name, yoVariables, constants);
      referenceFrame = null;
   }

   @Override
   public void update()
   {
      if (referenceFrame != null)
         setToReferenceFrame(referenceFrame);
   }
}
