package us.ihmc.graphicsDescription.yoGraphics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import us.ihmc.euclid.geometry.interfaces.ConvexPolygon2DReadOnly;
import us.ihmc.euclid.geometry.interfaces.Vertex2DSupplier;
import us.ihmc.euclid.referenceFrame.ReferenceFrame;
import us.ihmc.euclid.referenceFrame.interfaces.FrameConvexPolygon2DReadOnly;
import us.ihmc.euclid.tuple2D.interfaces.Point2DReadOnly;
import us.ihmc.graphicsDescription.Graphics3DObject;
import us.ihmc.graphicsDescription.GraphicsUpdatable;
import us.ihmc.graphicsDescription.MeshDataGenerator;
import us.ihmc.graphicsDescription.MeshDataHolder;
import us.ihmc.graphicsDescription.appearance.AppearanceDefinition;
import us.ihmc.graphicsDescription.instructions.Graphics3DAddMeshDataInstruction;
import us.ihmc.graphicsDescription.plotting.artifact.Artifact;
import us.ihmc.yoVariables.euclid.referenceFrame.*;
import us.ihmc.yoVariables.registry.YoRegistry;
import us.ihmc.yoVariables.variable.YoDouble;
import us.ihmc.yoVariables.variable.YoInteger;
import us.ihmc.yoVariables.variable.YoVariable;

public class YoGraphicPolygon extends YoGraphicAbstractShape implements RemoteYoGraphic, GraphicsUpdatable
{
   private static final ReferenceFrame worldFrame = ReferenceFrame.getWorldFrame();
   private static final double DEFAULT_HEIGHT = 0.01;
   private final double height;

   private YoFrameConvexPolygon2D yoFrameConvexPolygon2d;
   private final Graphics3DObject graphics3dObject;
   private final Graphics3DAddMeshDataInstruction instruction;

   private final AppearanceDefinition appearance;
   private final List<Point2DReadOnly> verticesToDisplay;

   public YoGraphicPolygon(String name, YoFramePose3D framePose, int maxNumberOfVertices, YoRegistry registry, double scale,
                           AppearanceDefinition appearance)
   {
      this(name, new YoFrameConvexPolygon2D(name + "ConvexPolygon2d", worldFrame, maxNumberOfVertices, registry), framePose, scale, appearance);
   }

   public YoGraphicPolygon(String name, YoFrameConvexPolygon2D yoFrameConvexPolygon2d, YoFramePose3D framePose, double scale, AppearanceDefinition appearance)
   {
      this(name, yoFrameConvexPolygon2d, framePose.getPosition(), framePose.getOrientation(), scale, appearance);
   }

   public YoGraphicPolygon(String name, YoFrameConvexPolygon2D yoFrameConvexPolygon2d, YoFramePoint3D framePoint, YoFrameQuaternion orientation, double scale,
                           AppearanceDefinition appearance)
   {
      this(name, yoFrameConvexPolygon2d, framePoint, orientation, scale, DEFAULT_HEIGHT, appearance);
   }

   public YoGraphicPolygon(String name, YoFrameConvexPolygon2D yoFrameConvexPolygon2d, YoFramePoint3D framePoint, YoFrameQuaternion orientation, double scale,
                           double height, AppearanceDefinition appearance)
   {
      this(name, yoFrameConvexPolygon2d, framePoint, null, orientation, scale, height, appearance);
   }

   public YoGraphicPolygon(String name, YoFrameConvexPolygon2D convexPolygon2d, String namePrefix, String nameSuffix, YoRegistry registry,
                           boolean useYawPitchRoll, double scale, AppearanceDefinition appearance)
   {
      this(name, convexPolygon2d, new YoFramePoint3D(namePrefix, nameSuffix, worldFrame, registry),
           useYawPitchRoll ? new YoFrameYawPitchRoll(namePrefix, nameSuffix, worldFrame, registry) : null,
           useYawPitchRoll ? null : new YoFrameQuaternion(namePrefix, nameSuffix, worldFrame, registry), scale, DEFAULT_HEIGHT, appearance);
   }

   public YoGraphicPolygon(String name, YoFramePoseUsingYawPitchRoll framePose, int maxNumberOfVertices, YoRegistry registry, double scale,
                           AppearanceDefinition appearance)
   {
      this(name, new YoFrameConvexPolygon2D(name + "ConvexPolygon2d", worldFrame, maxNumberOfVertices, registry), framePose, scale, appearance);
   }

   public YoGraphicPolygon(String name, YoFrameConvexPolygon2D yoFrameConvexPolygon2d, YoFramePoseUsingYawPitchRoll framePose, double scale,
                           AppearanceDefinition appearance)
   {
      this(name, yoFrameConvexPolygon2d, framePose.getPosition(), framePose.getYawPitchRoll(), scale, appearance);
   }

   public YoGraphicPolygon(String name, int maxNumberOfVertices, YoRegistry registry, boolean useYawPitchRoll, double scale,
                           AppearanceDefinition appearance)
   {
      this(name, new YoFrameConvexPolygon2D(name + "ConvexPolygon2d", worldFrame, maxNumberOfVertices, registry), registry, useYawPitchRoll, scale, appearance);
   }

   public YoGraphicPolygon(String name, YoFrameConvexPolygon2D convexPolygon2d, YoRegistry registry, boolean useYawPitchRoll, double scale,
                           AppearanceDefinition appearance)
   {
      this(name, convexPolygon2d, new YoFramePoint3D(name + "Position", worldFrame, registry),
           useYawPitchRoll ? new YoFrameYawPitchRoll(name + "Orientation", worldFrame, registry) : null,
           useYawPitchRoll ? null : new YoFrameQuaternion(name + "Orientation", worldFrame, registry), scale, DEFAULT_HEIGHT, appearance);
   }

   public YoGraphicPolygon(String name, YoFrameConvexPolygon2D yoFrameConvexPolygon2d, YoFramePoint3D framePoint, YoFrameYawPitchRoll orientation, double scale,
                           AppearanceDefinition appearance)
   {
      this(name, yoFrameConvexPolygon2d, framePoint, orientation, scale, DEFAULT_HEIGHT, appearance);
   }

   public YoGraphicPolygon(String name, YoFrameConvexPolygon2D yoFrameConvexPolygon2d, YoFramePoint3D framePoint, YoFrameYawPitchRoll orientation, double scale,
                           double height, AppearanceDefinition appearance)
   {
      this(name, yoFrameConvexPolygon2d, framePoint, orientation, null, scale, height, appearance);
   }

   private YoGraphicPolygon(String name, YoFrameConvexPolygon2D yoFrameConvexPolygon2d, YoFramePoint3D framePoint, YoFrameYawPitchRoll yawPitchRoll,
                            YoFrameQuaternion quaternion, double scale, double height, AppearanceDefinition appearance)
   {
      super(name, framePoint, yawPitchRoll, quaternion, scale);

      if (yoFrameConvexPolygon2d.getNumberOfVertices() <= 0)
         yoFrameConvexPolygon2d.set(Vertex2DSupplier.emptyVertex2DSupplier());

      this.yoFrameConvexPolygon2d = yoFrameConvexPolygon2d;
      this.appearance = appearance;
      this.height = height;
      verticesToDisplay = new ArrayList<>(yoFrameConvexPolygon2d.getMaxNumberOfVertices());

      graphics3dObject = new Graphics3DObject();
      graphics3dObject.setChangeable(true);

      MeshDataHolder meshDataHolder = MeshDataGenerator.ExtrudedPolygon(yoFrameConvexPolygon2d, height);
      instruction = new Graphics3DAddMeshDataInstruction(meshDataHolder, appearance);
      graphics3dObject.addInstruction(instruction);
   }

   static YoGraphicPolygon createAsRemoteYoGraphic(String name, YoVariable[] yoVariables, double[] constants, AppearanceDefinition appearance)
   {
      return new YoGraphicPolygon(name, yoVariables, constants, appearance);
   }

   private YoGraphicPolygon(String name, YoVariable[] yoVariables, double[] constants, AppearanceDefinition appearance)
   {
      /*
       * 2 * (int) constants[1] + 1 => constants[1] is the size of the 2D vertex buffer so we have 2 *
       * YoVariables, the "+ 1" is for the YoInteger that keeps track of the actual polygon size.
       */
      super(name, Arrays.copyOfRange(yoVariables, 2 * (int) constants[1] + 1, yoVariables.length), constants[0]);

      int constantIndex = 1; // 0 corresponds to the scale factor
      int vertexBufferSize = (int) constants[constantIndex++];
      // Ensuring backward compatibility
      height = constants.length == 3 ? constants[constantIndex++] : DEFAULT_HEIGHT;

      int yoIndex = 0;
      YoInteger numberOfVertices = (YoInteger) yoVariables[yoIndex++];

      List<YoFramePoint2D> yoFrameVertices = new ArrayList<>();

      while (yoFrameVertices.size() < vertexBufferSize)
      {
         YoDouble xVariable = (YoDouble) yoVariables[yoIndex++];
         YoDouble yVariable = (YoDouble) yoVariables[yoIndex++];
         yoFrameVertices.add(new YoFramePoint2D(xVariable, yVariable, worldFrame));
      }

      yoFrameConvexPolygon2d = new YoFrameConvexPolygon2D(yoFrameVertices, numberOfVertices, worldFrame);
      verticesToDisplay = new ArrayList<>(yoFrameConvexPolygon2d.getMaxNumberOfVertices());

      this.appearance = appearance;

      graphics3dObject = new Graphics3DObject();
      graphics3dObject.setChangeable(true);

      MeshDataHolder meshDataHolder = MeshDataGenerator.ExtrudedPolygon(yoFrameConvexPolygon2d, height);
      instruction = new Graphics3DAddMeshDataInstruction(meshDataHolder, appearance);
      graphics3dObject.addInstruction(instruction);
   }

   @Override
   public Artifact createArtifact()
   {
      throw new RuntimeException("Implement Me!");
   }

   @Override
   public void update()
   {
      verticesToDisplay.clear();

      for (int i = yoFrameConvexPolygon2d.getNumberOfVertices() - 1; i >= 0; i--)
      {
         /*
          * Using the unsafe getter as this instance of the polygon might not be recognized as up-to-date.
          * This can occur when displaying a remote YoGraphic. In such scenario, the coordinates of the
          * vertices are updated under the hood and this polygon only serves as a data holder.
          */
         verticesToDisplay.add(yoFrameConvexPolygon2d.getVertexUnsafe(i));
      }

      instruction.setMesh(MeshDataGenerator.ExtrudedPolygon(verticesToDisplay, height));
   }

   public void updateAppearance(AppearanceDefinition appearance)
   {
      instruction.setAppearance(appearance);
   }

   public void updateConvexPolygon2d(FrameConvexPolygon2DReadOnly frameConvexPolygon2d)
   {
      yoFrameConvexPolygon2d.set(frameConvexPolygon2d);
      update();
   }

   public void updateConvexPolygon2d(ConvexPolygon2DReadOnly convexPolygon2d)
   {
      yoFrameConvexPolygon2d.set(convexPolygon2d);
      update();
   }

   @Override
   public Graphics3DObject getLinkGraphics()
   {
      return graphics3dObject;
   }

   @Override
   public YoVariable[] getVariables()
   {
      //poly + framePoint + frameOrientation
      YoVariable[] superYoVariables = super.getVariables();
      YoVariable[] yoVariables = new YoVariable[1 + 2 * yoFrameConvexPolygon2d.getMaxNumberOfVertices() + superYoVariables.length];

      int i = 0;
      yoVariables[i++] = yoFrameConvexPolygon2d.getYoNumberOfVertices();

      for (YoFramePoint2D p : yoFrameConvexPolygon2d.getVertexBuffer())
      {
         yoVariables[i++] = p.getYoX();
         yoVariables[i++] = p.getYoY();
      }

      for (YoVariable superYoVariable : superYoVariables)
      {
         yoVariables[i++] = superYoVariable;
      }

      return yoVariables;
   }

   @Override
   public double[] getConstants()
   {
      return new double[] {scale, yoFrameConvexPolygon2d.getVertexBuffer().size(), height};
   }

   @Override
   public YoGraphicPolygon duplicate(YoRegistry newRegistry)
   {
      if (isUsingYawPitchRoll())
         return new YoGraphicPolygon(getName(),
                                     yoFrameConvexPolygon2d.duplicate(newRegistry),
                                     yoFramePoint.duplicate(newRegistry),
                                     yoFrameYawPitchRoll.duplicate(newRegistry),
                                     scale,
                                     height,
                                     appearance);
      else
         return new YoGraphicPolygon(getName(),
                                     yoFrameConvexPolygon2d.duplicate(newRegistry),
                                     yoFramePoint.duplicate(newRegistry),
                                     yoFrameQuaternion.duplicate(newRegistry),
                                     scale,
                                     height,
                                     appearance);
   }

   @Override
   public AppearanceDefinition getAppearance()
   {
      return appearance;
   }
}
