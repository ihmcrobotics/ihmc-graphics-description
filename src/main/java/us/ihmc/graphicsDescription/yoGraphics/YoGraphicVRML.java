package us.ihmc.graphicsDescription.yoGraphics;

import us.ihmc.euclid.matrix.RotationMatrix;
import us.ihmc.euclid.referenceFrame.ReferenceFrame;
import us.ihmc.euclid.tuple3D.Vector3D;
import us.ihmc.graphicsDescription.Graphics3DObject;
import us.ihmc.graphicsDescription.appearance.AppearanceDefinition;
import us.ihmc.yoVariables.euclid.referenceFrame.YoFramePoint3D;
import us.ihmc.yoVariables.euclid.referenceFrame.YoFramePoseUsingYawPitchRoll;
import us.ihmc.yoVariables.euclid.referenceFrame.YoFrameYawPitchRoll;
import us.ihmc.yoVariables.registry.YoRegistry;

public class YoGraphicVRML extends YoGraphicCoordinateSystem
{
   private final ReferenceFrame referenceFrame;
   private final String modelFilePath;
   private final Vector3D graphicOffset;
   private final RotationMatrix graphicRotation;
   private final boolean showCoordinateSystem;
   private final AppearanceDefinition appearance;

   public YoGraphicVRML(String name, ReferenceFrame referenceFrame, YoRegistry registry, String modelFilePath, boolean showCoordinateSystem)
   {
      this(name, referenceFrame, registry, modelFilePath, new Vector3D(), new RotationMatrix(1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0),
           showCoordinateSystem);
   }

   public YoGraphicVRML(String name, ReferenceFrame referenceFrame, YoRegistry registry, String modelFilePath, Vector3D graphicOffset,
                        RotationMatrix graphicRotation, boolean showCoordinateSystem)
   {
      this(name, referenceFrame, registry, modelFilePath, graphicOffset, graphicRotation, null, showCoordinateSystem, 0.1);
   }

   public YoGraphicVRML(String name, ReferenceFrame referenceFrame, YoRegistry registry, String modelFilePath, Vector3D graphicOffset,
                        RotationMatrix graphicRotation, AppearanceDefinition appearance, boolean showCoordinateSystem, double coordinateAxisLength)
   {
      super(name, "", registry, true, coordinateAxisLength);
      this.referenceFrame = referenceFrame;
      this.modelFilePath = modelFilePath;
      this.graphicOffset = graphicOffset;
      this.graphicRotation = graphicRotation;
      this.appearance = appearance;
      this.showCoordinateSystem = showCoordinateSystem;
   }

   public YoGraphicVRML(String name, ReferenceFrame referenceFrame, YoFramePoint3D position, YoFrameYawPitchRoll orientation, String modelFilePath,
                        Vector3D graphicOffset, RotationMatrix graphicRotation, AppearanceDefinition appearance, boolean showCoordinateSystem,
                        double coordinateAxisLength)
   {
      this(name, referenceFrame, new YoFramePoseUsingYawPitchRoll(position, orientation), modelFilePath, graphicOffset, graphicRotation, appearance,
           showCoordinateSystem, coordinateAxisLength);
   }

   public YoGraphicVRML(String name, ReferenceFrame referenceFrame, YoFramePoseUsingYawPitchRoll yoFramePose, String modelFilePath, Vector3D graphicOffset,
                        RotationMatrix graphicRotation, AppearanceDefinition appearance, boolean showCoordinateSystem, double coordinateAxisLength)
   {
      super(name, yoFramePose, coordinateAxisLength);
      this.referenceFrame = referenceFrame;
      this.modelFilePath = modelFilePath;
      this.graphicOffset = graphicOffset;
      this.graphicRotation = graphicRotation;
      this.appearance = appearance;
      this.showCoordinateSystem = showCoordinateSystem;
   }

   @Override
   public void update()
   {
      setToReferenceFrame(referenceFrame);
   }

   @Override
   public Graphics3DObject getLinkGraphics()
   {
      Graphics3DObject linkGraphics = new Graphics3DObject();

      if (showCoordinateSystem)
      {
         linkGraphics.addCoordinateSystem(scale, arrowColor);
      }

      linkGraphics.rotate(graphicRotation);
      linkGraphics.translate(graphicOffset);

      linkGraphics.addModelFile(modelFilePath, appearance);

      return linkGraphics;
   }

   @Override
   public YoGraphicVRML duplicate(YoRegistry newRegistry)
   {
      return new YoGraphicVRML(getName(),
                               referenceFrame,
                               position.duplicate(newRegistry),
                               yawPitchRoll.duplicate(newRegistry),
                               modelFilePath,
                               graphicOffset,
                               graphicRotation,
                               appearance,
                               showCoordinateSystem,
                               scale);
   }
}