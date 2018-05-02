package us.ihmc.graphicsDescription.yoGraphics;

import us.ihmc.euclid.referenceFrame.ReferenceFrame;
import us.ihmc.graphicsDescription.Graphics3DObject;
import us.ihmc.graphicsDescription.appearance.AppearanceDefinition;
import us.ihmc.graphicsDescription.instructions.Graphics3DAddExtrusionInstruction;
import us.ihmc.yoVariables.registry.YoVariableRegistry;
import us.ihmc.yoVariables.variable.YoFramePoint3D;
import us.ihmc.yoVariables.variable.YoFrameYawPitchRoll;

public class YoGraphicText3D extends YoGraphicAbstractShape
{
   private final Graphics3DObject graphics3dObject;
   private final Graphics3DAddExtrusionInstruction instruction;
   private final String text;
   private final AppearanceDefinition appearance;

   public YoGraphicText3D(String name, String text, YoFramePoint3D framePoint, YoFrameYawPitchRoll orientation, double scale, AppearanceDefinition appearance)
   {
      super(name, framePoint, orientation, scale);
      this.text = text;
      this.appearance = appearance;
      graphics3dObject = new Graphics3DObject();
      graphics3dObject.setChangeable(true);
      instruction = graphics3dObject.addText(text, 20.0, appearance);
   }

   public YoGraphicText3D(String name, String text, String namePrefix, String nameSuffix, YoVariableRegistry registry, double scale,
                          AppearanceDefinition appearance)
   {
      this(name, text, new YoFramePoint3D(namePrefix, nameSuffix, ReferenceFrame.getWorldFrame(), registry),
           new YoFrameYawPitchRoll(namePrefix, nameSuffix, ReferenceFrame.getWorldFrame(), registry), scale, appearance);
   }

   public void setAppearance(AppearanceDefinition appearance)
   {
      instruction.setAppearance(appearance);
   }

   public void setText(String text)
   {
      instruction.setText(text);
   }

   @Override
   public Graphics3DObject getLinkGraphics()
   {
      return graphics3dObject;
   }

   @Override
   public YoGraphic duplicate(YoVariableRegistry newRegistry)
   {
      return new YoGraphicText3D(getName(), text, yoFramePoint.duplicate(newRegistry), yoFrameOrientation.duplicate(newRegistry), scale, appearance);
   }
}
