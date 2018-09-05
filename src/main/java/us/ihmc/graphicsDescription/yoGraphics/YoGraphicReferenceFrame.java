package us.ihmc.graphicsDescription.yoGraphics;

import us.ihmc.euclid.referenceFrame.ReferenceFrame;
import us.ihmc.graphicsDescription.appearance.AppearanceDefinition;
import us.ihmc.graphicsDescription.appearance.YoAppearance;
import us.ihmc.yoVariables.registry.YoVariableRegistry;
import us.ihmc.yoVariables.variable.YoVariable;

public class YoGraphicReferenceFrame extends YoGraphicCoordinateSystem
{
   private final ReferenceFrame referenceFrame;

   public YoGraphicReferenceFrame(ReferenceFrame referenceFrame, YoVariableRegistry registry, double scale)
   {
      this(referenceFrame, registry, scale, YoAppearance.Gray());
   }

   public YoGraphicReferenceFrame(ReferenceFrame referenceFrame, YoVariableRegistry registry, double scale, AppearanceDefinition arrowColor)
   {
      super(referenceFrame.getName(), "", registry, scale, arrowColor);

      this.referenceFrame = referenceFrame;
   }

   public YoGraphicReferenceFrame(String prefix, ReferenceFrame referenceFrame, YoVariableRegistry registry, double scale, AppearanceDefinition arrowColor)
   {
      super(prefix + referenceFrame.getName(), "", registry, scale, arrowColor);

      this.referenceFrame = referenceFrame;
   }

   static YoGraphicReferenceFrame createAsRemoteYoGraphic(String name, YoVariable<?>[] yoVariables, double[] constants)
   {
      return new YoGraphicReferenceFrame(name, yoVariables, constants);
   }

   private YoGraphicReferenceFrame(String name, YoVariable<?>[] yoVariables, double[] constants)
   {
      super(name, yoVariables, constants);
      referenceFrame = null;
   }

   @Override
   public void update()
   {
      if (referenceFrame != null)
         this.setToReferenceFrame(referenceFrame);
   }
}
