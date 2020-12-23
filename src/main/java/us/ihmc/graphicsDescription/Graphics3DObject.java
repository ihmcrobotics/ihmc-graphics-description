package us.ihmc.graphicsDescription;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import us.ihmc.euclid.Axis3D;
import us.ihmc.euclid.axisAngle.AxisAngle;
import us.ihmc.euclid.geometry.interfaces.ConvexPolygon2DReadOnly;
import us.ihmc.euclid.geometry.tools.EuclidGeometryTools;
import us.ihmc.euclid.matrix.interfaces.Matrix3DReadOnly;
import us.ihmc.euclid.orientation.interfaces.Orientation3DReadOnly;
import us.ihmc.euclid.shape.primitives.interfaces.Box3DReadOnly;
import us.ihmc.euclid.shape.primitives.interfaces.Capsule3DReadOnly;
import us.ihmc.euclid.shape.primitives.interfaces.Cylinder3DReadOnly;
import us.ihmc.euclid.shape.primitives.interfaces.Ellipsoid3DReadOnly;
import us.ihmc.euclid.shape.primitives.interfaces.PointShape3DReadOnly;
import us.ihmc.euclid.shape.primitives.interfaces.Ramp3DReadOnly;
import us.ihmc.euclid.shape.primitives.interfaces.Shape3DReadOnly;
import us.ihmc.euclid.shape.primitives.interfaces.Sphere3DReadOnly;
import us.ihmc.euclid.shape.primitives.interfaces.Torus3DReadOnly;
import us.ihmc.euclid.transform.AffineTransform;
import us.ihmc.euclid.transform.RigidBodyTransform;
import us.ihmc.euclid.transform.interfaces.RigidBodyTransformReadOnly;
import us.ihmc.euclid.tuple2D.Point2D;
import us.ihmc.euclid.tuple2D.Point2D32;
import us.ihmc.euclid.tuple2D.interfaces.Point2DReadOnly;
import us.ihmc.euclid.tuple3D.Point3D;
import us.ihmc.euclid.tuple3D.Point3D32;
import us.ihmc.euclid.tuple3D.Vector3D;
import us.ihmc.euclid.tuple3D.Vector3D32;
import us.ihmc.euclid.tuple3D.interfaces.Point3DReadOnly;
import us.ihmc.euclid.tuple3D.interfaces.Tuple3DReadOnly;
import us.ihmc.euclid.tuple3D.interfaces.Vector3DReadOnly;
import us.ihmc.graphicsDescription.appearance.AppearanceDefinition;
import us.ihmc.graphicsDescription.appearance.MaterialDescription;
import us.ihmc.graphicsDescription.appearance.YoAppearance;
import us.ihmc.graphicsDescription.exceptions.ShapeNotSupportedException;
import us.ihmc.graphicsDescription.geometry.ArcTorus3DDescription;
import us.ihmc.graphicsDescription.geometry.Box3DDescription;
import us.ihmc.graphicsDescription.geometry.Capsule3DDescription;
import us.ihmc.graphicsDescription.geometry.Cone3DDescription;
import us.ihmc.graphicsDescription.geometry.Cylinder3DDescription;
import us.ihmc.graphicsDescription.geometry.Ellipsoid3DDescription;
import us.ihmc.graphicsDescription.geometry.ExtrudedPolygon2DDescription;
import us.ihmc.graphicsDescription.geometry.ExtrusionDescription;
import us.ihmc.graphicsDescription.geometry.GeometryDescription;
import us.ihmc.graphicsDescription.geometry.HeightMapDescription;
import us.ihmc.graphicsDescription.geometry.HemiEllipsoid3DDescription;
import us.ihmc.graphicsDescription.geometry.MeshDescription;
import us.ihmc.graphicsDescription.geometry.ModelFileGeometryDescription;
import us.ihmc.graphicsDescription.geometry.ModelFileGeometryDescription.SubMeshDescription;
import us.ihmc.graphicsDescription.geometry.Polygon3DDescription;
import us.ihmc.graphicsDescription.geometry.PyramidBox3DDescription;
import us.ihmc.graphicsDescription.geometry.Sphere3DDescription;
import us.ihmc.graphicsDescription.geometry.TruncatedCone3DDescription;
import us.ihmc.graphicsDescription.geometry.Wedge3DDescription;
import us.ihmc.graphicsDescription.mesh.MeshDataGenerator;
import us.ihmc.graphicsDescription.mesh.MeshDataHolder;
import us.ihmc.log.LogTools;

public class Graphics3DObject
{
   private static final AppearanceDefinition DEFAULT_APPEARANCE = YoAppearance.Black();

   private static final int RESOLUTION = 25;

   private final AffineTransform currentTransform = new AffineTransform();
   private List<VisualDescription> visualDescriptions;

   private boolean changeable = false;

   /**
    * Default no-arg constructor. This creates a new empty Graphics3DObject component.
    */
   public Graphics3DObject()
   {
      this((List<VisualDescription>) null);
   }

   public Graphics3DObject(List<VisualDescription> visualDescriptions)
   {
      if (visualDescriptions != null)
         this.visualDescriptions = visualDescriptions;
      else
         this.visualDescriptions = new ArrayList<>();
   }

   public Graphics3DObject(Graphics3DObject other)
   {
      this();
      combine(other);
   }

   public List<VisualDescription> getGraphics3DInstructions()
   {
      return visualDescriptions;
   }

   /**
    * Merge this with the specified Graphics3DObject.
    *
    * @param other Graphics3DObject to combine with.
    */
   public void combine(Graphics3DObject other)
   {
      identity();
      visualDescriptions.addAll(other.getGraphics3DInstructions());
   }

   public void combine(Graphics3DObject other, Vector3DReadOnly offset)
   {
      List<VisualDescription> otherVisuals = other.getGraphics3DInstructions();

      for (VisualDescription otherVisual : otherVisuals)
      {
         VisualDescription visual = new VisualDescription(otherVisual.getGeometry(), otherVisual.getMaterial());
         AffineTransform pose = new AffineTransform(otherVisual.getPose());
         pose.prependTranslation(offset);
         visual.setPose(pose);
         visualDescriptions.add(otherVisual);
      }

      currentTransform.set(other.currentTransform);
      currentTransform.prependTranslation(offset);
   }

   public void appendTransform(RigidBodyTransformReadOnly transform)
   {
      currentTransform.multiply(transform);
   }

   /**
    * Translates from the current position by the specified distances. Graphic components added after
    * translation will appear in the new location. The coordinate system for these translations is
    * based on those that preceded it. Each new has its coordinates reset to the parent joint's origin.
    * {@link #identity Identity} resets back to the joint origin.
    *
    * @param x distance translated in the x direction
    * @param y distance translated in the y direction
    * @param z distance translated in the z direction
    */
   public void appendTranslation(double x, double y, double z)
   {
      currentTransform.appendTranslation(x, y, z);
   }

   /**
    * Translates from the current position by the specified distances. Graphic components added after
    * translation will appear in the new location. The coordinate system for these translations is
    * based on those that preceded it. Each new has its coordinates reset to the parent joint's origin.
    * {@link #identity Identity} resets back to the joint origin.
    *
    * @param translation Tuple3d representing the translation.
    */
   public void appendTranslation(Tuple3DReadOnly translation)
   {
      currentTransform.appendTranslation(translation);
   }

   /**
    * Rotates the coordinate system counter clockwise around the specified axis by the given angle in
    * radians. This does not rotate existing graphics, instead it rotates a "cursor" When another
    * object is added it will be centered on the origin of the current system as described by the
    * translations and rotations applied since its creation at the joint origin.
    *
    * @param rotationAngle the angle to rotate around the specified axis in radians.
    * @param rotationAxis  Vector3d describing the axis of rotation.
    */
   public void appendRotation(double rotationAngle, Vector3DReadOnly rotationAxis)
   {
      appendRotation(new AxisAngle(rotationAxis, rotationAngle));
   }

   /**
    * Rotates the coordinate system as described by the given rotation matrix. This does not rotate
    * existing graphics, instead it rotates a "cursor". When another object is added it will be
    * centered on the origin of the current system as described by the translations and rotations
    * applied since its creation at the joint origin.
    *
    * @param orientation Matrix3d describing the rotation to be applied.
    */
   public void appendRotation(Orientation3DReadOnly orientation)
   {
      currentTransform.appendOrientation(orientation);
   }

   /**
    * Scales the coordinate system by the specified scale factor. This does not scale existing
    * graphics, instead it scales the "current" coordinate system. When another object is added it will
    * be uniformly scaled by the specified factor.
    *
    * @param scaleFactor Factor by which the coordinate system is scaled. For example, 0.5 would reduce
    *                    future objects size by 50% whereas 2 would double it.
    * @return
    */
   public void appendScale(double scaleFactor)
   {
      currentTransform.appendScale(scaleFactor);
   }

   /**
    * Scales the coordinate system by the specified scale factor. This does not scale existing
    * graphics, instead it scales the "current" coordinate system. When another object is added it will
    * be uniformly scaled by the specified factor. The components of the vector indicate scale factors
    * in each dimension.
    *
    * @param scaleFactors Vector3d describing the scaling factors in each dimension.
    * @return
    */
   public void appendScale(Vector3DReadOnly scaleFactors)
   {
      currentTransform.appendScale(scaleFactors);
   }

   public void prependTransform(RigidBodyTransformReadOnly transform)
   {
      for (int i = 0; i < visualDescriptions.size(); i++)
         visualDescriptions.get(i).getPose().preMultiply(transform);
      currentTransform.preMultiply(transform);
   }

   public void prependTranslation(double x, double y, double z)
   {
      for (int i = 0; i < visualDescriptions.size(); i++)
      {
         visualDescriptions.get(i).getPose().prependTranslation(x, y, z);
      }
      currentTransform.prependTranslation(x, y, z);
   }

   public void prependTranslation(Tuple3DReadOnly translation)
   {
      prependTranslation(translation.getX(), translation.getY(), translation.getZ());
   }

   public void prependRotation(double rotationAngle, Vector3DReadOnly rotationAxis)
   {
      prependRotation(new AxisAngle(rotationAxis, rotationAngle));
   }

   public void prependRotation(Orientation3DReadOnly orientation)
   {
      for (int i = 0; i < visualDescriptions.size(); i++)
         visualDescriptions.get(i).getPose().prependOrientation(orientation);
      currentTransform.prependOrientation(orientation);
   }

   /**
    * Scales the origin coordinate system by the specified scale factor. This will scale existing
    * graphics and all graphics added after calling this function till identity() is called.
    *
    * @param scaleFactor Factor by which the graphics object system is scaled. For example, 0.5 would
    *                    reduce future objects size by 50% whereas 2 would double it.
    * @return
    */
   public void prependScale(double scaleFactor)
   {
      prependScale(new Vector3D(scaleFactor, scaleFactor, scaleFactor));
   }

   /**
    * Scales the origin coordinate system by the specified scale factor. This will scale existing
    * graphics and all graphics added after calling this function. The components of the vector
    * indicate scale factors in each dimension.
    *
    * @param scaleFactors Vector3d describing the scaling factors in each dimension
    * @return
    */
   public void prependScale(Vector3D scaleFactors)
   {
      for (int i = 0; i < visualDescriptions.size(); i++)
         visualDescriptions.get(i).getPose().prependScale(scaleFactors);
      currentTransform.prependScale(scaleFactors);
   }

   /**
    * Resets the coordinate system to the joint origin. This clears all rotations, translations, and
    * scale factors.
    */
   public void identity()
   {
      currentTransform.setIdentity();
   }

   public VisualDescription addVisualDescription(VisualDescription instruction)
   {
      visualDescriptions.add(instruction);
      return instruction;
   }

   public VisualDescription addGeometryDescription(GeometryDescription geometryDescription, AppearanceDefinition appearance)
   {
      return addVisualDescription(new VisualDescription(new AffineTransform(currentTransform),
                                                        geometryDescription,
                                                        new MaterialDescription(appearance.toColorDescription())));
   }

   /**
    * Adds the specified 3DS Max file to the center of the current coordinate system with a default
    * appearance. 3DS Max is a 3D modeling program that allows the creation of detailed models and
    * animations. This function only imports the model allowing the use of more complicated and
    * detailed system representations in simulations.
    *
    * @param fileURL URL pointing to the desired 3ds file.
    */
   public VisualDescription addModelFile(URL fileURL)
   {
      return addModelFile(fileURL, null);
   }

   /**
    * Adds the specified 3DS Max file to the center of the current coordinate system with the given
    * appearance. 3DS Max is a 3D modeling program that allows the creation of detailed models and
    * animations. This function only imports the model allowing the use of more complicated and
    * detailed system representations in simulations.
    *
    * @param fileURL                URL pointing to the desired 3ds file.
    * @param yoAppearanceDefinition Appearance to use with the 3ds model once imported.
    */
   public VisualDescription addModelFile(URL fileURL, AppearanceDefinition yoAppearanceDefinition)
   {
      if (fileURL == null)
      {
         LogTools.error("fileURL == null in addModelFile");
         return null;
      }

      String fileName = fileURL.getFile();

      if (fileName == null || fileName.equals(""))
      {
         LogTools.error("Null File Name in add3DSFile");
         return null;
      }

      return addModelFile(fileName, yoAppearanceDefinition);
   }

   /**
    * Adds the specified model file to the center of the current coordinate system with a default
    * appearance. Supported model files are .stl, .obj and .dae Appearances of model files can only be
    * changed at runtime if an appearance is given while loading the model
    *
    * @param fileName File path of the desired 3ds file.
    * @return void
    */
   public VisualDescription addModelFile(String fileName)
   {
      return addModelFile(fileName, null);
   }

   /**
    * Adds the specified model file to the center of the current coordinate system with the given
    * appearance. Supported model files are .stl, .obj and .dae. Appearances of model files can only be
    * changed at runtime if an appearance is given while loading the model
    *
    * @param fileName File path to the desired 3ds file.
    * @param app      Appearance to use with the model once imported.
    */

   /**
    * @param fileName
    * @param appearance
    * @return
    */
   public VisualDescription addModelFile(String fileName, AppearanceDefinition appearance)
   {
      return addGeometryDescription(new ModelFileGeometryDescription(fileName), appearance);
   }

   public VisualDescription addModelFile(String fileName, String submesh, boolean centerSubmesh, List<String> resourceDirectories,
                                         ClassLoader resourceClassLoader, AppearanceDefinition appearance)
   {
      ModelFileGeometryDescription modelFileGeometryDescription = new ModelFileGeometryDescription(fileName);
      modelFileGeometryDescription.setResourceDirectories(resourceDirectories);
      modelFileGeometryDescription.setSubmeshes(Collections.singletonList(new SubMeshDescription(submesh, centerSubmesh)));
      modelFileGeometryDescription.setResourceClassLoader(resourceClassLoader);
      return addGeometryDescription(modelFileGeometryDescription, appearance);
   }

   public VisualDescription addModelFile(String fileName, List<String> resourceDirectories, ClassLoader resourceClassLoader, AppearanceDefinition app)
   {
      return addModelFile(fileName, null, false, resourceDirectories, resourceClassLoader, app);
   }

   public void addCoordinateSystem(double length)
   {
      addCoordinateSystem(length, YoAppearance.Gray());
   }

   public void addCoordinateSystem(double length, AppearanceDefinition arrowAppearance)
   {
      addCoordinateSystem(length, YoAppearance.Red(), YoAppearance.White(), YoAppearance.Blue(), arrowAppearance);
   }

   /**
    * Creates a graphical representation of the x, y, and z axis of the current coordinate system
    * centered at its origin. In the image below red, white and blue represent the x, y and z axies
    * respectively.<br />
    * <br />
    * <img src="doc-files/LinkGraphics.addCoordinateSystem.jpg">
    *
    * @param length the length in meters of each axis arrow.
    */
   public void addCoordinateSystem(double length, AppearanceDefinition xAxisAppearance, AppearanceDefinition yAxisAppearance,
                                   AppearanceDefinition zAxisAppearance, AppearanceDefinition arrowAppearance)
   {
      appendRotation(Math.PI / 2.0, Axis3D.Y);
      addArrow(length, YoAppearance.Red(), arrowAppearance);
      appendRotation(-Math.PI / 2.0, Axis3D.Y);
      appendRotation(-Math.PI / 2.0, Axis3D.X);
      addArrow(length, YoAppearance.White(), arrowAppearance);
      appendRotation(Math.PI / 2.0, Axis3D.X);
      addArrow(length, YoAppearance.Blue(), arrowAppearance);
   }

   public void add(Shape3DReadOnly shape)
   {
      add(shape, DEFAULT_APPEARANCE);
   }

   public void add(Shape3DReadOnly shape, AppearanceDefinition shapeAppearance)
   {
      if (shape instanceof Box3DReadOnly)
      {
         Box3DReadOnly box = (Box3DReadOnly) shape;
         appendTransform(box.getPose());
         addCube(box.getSizeX(), box.getSizeY(), box.getSizeZ(), true, shapeAppearance);
      }
      else if (shape instanceof Capsule3DReadOnly)
      {
         Capsule3DReadOnly capsule = (Capsule3DReadOnly) shape;
         appendTranslation(capsule.getPosition());
         appendRotation(EuclidGeometryTools.axisAngleFromZUpToVector3D(capsule.getAxis()));
         addCapsule(capsule.getRadius(),
                    capsule.getLength() + 2.0 * capsule.getRadius(), // the 2nd term is removed internally.
                    shapeAppearance);
      }
      else if (shape instanceof Cylinder3DReadOnly)
      {
         Cylinder3DReadOnly cylinder = (Cylinder3DReadOnly) shape;
         appendTranslation(cylinder.getPosition());
         appendRotation(EuclidGeometryTools.axisAngleFromZUpToVector3D(cylinder.getAxis()));
         appendTranslation(0.0, 0.0, -cylinder.getHalfLength());
         addCylinder(cylinder.getLength(), cylinder.getRadius(), shapeAppearance);
      }
      else if (shape instanceof Ellipsoid3DReadOnly)
      {
         Ellipsoid3DReadOnly ellipsoid = (Ellipsoid3DReadOnly) shape;
         appendTransform(ellipsoid.getPose());
         addEllipsoid(ellipsoid.getRadiusX(), ellipsoid.getRadiusY(), ellipsoid.getRadiusZ(), shapeAppearance);
      }
      else if (shape instanceof PointShape3DReadOnly)
      {
         PointShape3DReadOnly pointShape = (PointShape3DReadOnly) shape;
         appendTranslation(pointShape);
         addSphere(0.005, shapeAppearance); // Arbitrary radius
      }
      else if (shape instanceof Ramp3DReadOnly)
      {
         Ramp3DReadOnly ramp = (Ramp3DReadOnly) shape;
         appendTransform(ramp.getPose());
         appendTranslation(-0.5 * ramp.getSizeX(), 0.0, 0.0);
         addWedge(ramp.getSizeX(), ramp.getSizeY(), ramp.getSizeZ(), shapeAppearance);
      }
      else if (shape instanceof Sphere3DReadOnly)
      {
         Sphere3DReadOnly sphere = (Sphere3DReadOnly) shape;
         appendTranslation(sphere.getPosition());
         addSphere(sphere.getRadius(), shapeAppearance);
      }
      else if (shape instanceof Torus3DReadOnly)
      {
         Torus3DReadOnly torus = (Torus3DReadOnly) shape;
         appendTranslation(torus.getPosition());
         appendRotation(EuclidGeometryTools.axisAngleFromZUpToVector3D(torus.getAxis()));
         addArcTorus(0.0, 2.0 * Math.PI, torus.getRadius(), torus.getTubeRadius(), shapeAppearance);
      }
      else
      {
         // TODO Implement for ConvexPolytope3D
         throw new ShapeNotSupportedException(shape);
      }
   }

   public void addArrow(double length, AppearanceDefinition baseAppearance, AppearanceDefinition headAppearance)
   {
      double coneHeight = 0.1 * length;
      double cylinderHeight = length - coneHeight;
      double radius = 0.02 * length;
      double coneRadius = 2.0 * radius;

      addCylinder(cylinderHeight, radius, baseAppearance);
      appendTranslation(0.0, 0.0, cylinderHeight);
      addCone(coneHeight, coneRadius, headAppearance);
      appendTranslation(0.0, 0.0, -cylinderHeight);
   }

   /**
    * Adds a solid black cube with the specified dimensions centered around the origin of the current
    * coordinate system. All lengths are in meters.</ br></ br> The image below demonstrates a 0.25 x
    * 0.25 x 0.25 cube generated by the following code:<br />
    * <br />
    * {@code linkGraphics.addCoordinateSystem(0.5);}<br />
    * {@code linkGraphics.addCube(0.25, 0.25, 0.25);}<br />
    * <br />
    * As is show by the graphical representation the cube is centered on the coordinate system. Again,
    * x, y and z are red, white and blue. <br />
    * <br />
    * <img src="doc-files/LinkGraphics.addCube1.jpg">
    *
    * @param lengthX length of the cube in the x direction.
    * @param widthY  width of the cube in the y direction.
    * @param heightZ height of the cube in the z direction.
    */
   public VisualDescription addCube(double lengthX, double widthY, double heightZ)
   {
      return addCube(lengthX, widthY, heightZ, DEFAULT_APPEARANCE);
   }

   /**
    * Adds a solid cube with the given dimensions and appearance centered on the origin of the current
    * coordinate system. All lengths are in meters.</ br></ br> The image below demonstrates a yellow
    * 0.1 x 0.35 x 0.2 cube generated by the following code:<br />
    * <br />
    * {@code linkGraphics.addCoordinateSystem(0.5);}<br />
    * {@code linkGraphics.addCube(0.1, 0.35, 0.2, YoAppearance.Yellow());}<br />
    * <br />
    * As is show by the graphical representation the cube is centered on the coordinate system. Again,
    * x, y and z are red, white and blue. <br />
    * <br />
    * <img src="doc-files/LinkGraphics.addCube2.jpg">
    *
    * @param lengthX        length of the cube in the x direction.
    * @param widthY         width of the cube in the y direction.
    * @param heightZ        height of the cube in the z direction.
    * @param cubeAppearance Appearance of the cube. See {@link YoAppearance YoAppearance} for
    *                       implementations.
    * @return
    */
   public VisualDescription addCube(double lengthX, double widthY, double heightZ, AppearanceDefinition cubeAppearance)
   {
      return addCube(lengthX, widthY, heightZ, false, cubeAppearance);
   }

   public VisualDescription addCube(double lengthX, double widthY, double heightZ, boolean centeredInTheCenter, AppearanceDefinition cubeApp)
   {
      return addGeometryDescription(new Box3DDescription(lengthX, widthY, heightZ, centeredInTheCenter), cubeApp);
   }

   /**
    * Adds a solid wedge with the given dimensions centered on the origin of the current coordinate
    * system. The peak of the wedge is directly above the far edge of the cube in the x direction.</
    * br></ br> The image below demonstrates a 0.25 x 0.25 x 0.25 wedge generated by the following
    * code:<br />
    * <br />
    * {@code linkGraphics.addCoordinateSystem(0.5);}<br />
    * {@code linkGraphics.addWedge(0.25, 0.25, 0.25);}<br />
    * <br />
    * As is show by the graphical representation the wedge is centered on the coordinate system. Again,
    * x, y and z are red, white and blue. <br />
    * <br />
    * <img src="doc-files/LinkGraphics.addWedge1.jpg">
    *
    * @param lengthX length of the wedge in the x direction.
    * @param widthY  width of the wedge in the y direction.
    * @param heightZ height of the wedge in the z direction.
    */
   public VisualDescription addWedge(double lengthX, double widthY, double heightZ)
   {
      return addWedge(lengthX, widthY, heightZ, DEFAULT_APPEARANCE);
   }

   /**
    * Adds a solid wedge with the given dimensions and appearance centered on the origin of the current
    * coordinate system. The peak of the wedge is directly above the far edge of the cube in the x
    * direction.</ br></ br> The image below demonstrates a green 0.35 x 0.3 x 0.1 wedge generated by
    * the following code:<br />
    * <br />
    * {@code linkGraphics.addCoordinateSystem(0.5);}<br />
    * {@code linkGraphics.addWedge(0.35, 0.3, 0.1, YoAppearance.GREEN());}<br />
    * <br />
    * As is show by the graphical representation the wedge is centered on the coordinate system. Again,
    * x, y and z are red, white and blue. <br />
    * <br />
    * <img src="doc-files/LinkGraphics.addWedge2.jpg">
    *
    * @param lengthX         length of the wedge in the x direction.
    * @param widthY          width of the wedge in the y direction.
    * @param heightZ         height of the wedge in the z direction.
    * @param wedgeAppearance Appearance of the wedge. See {@link YoAppearance YoAppearance} for
    *                        implementations.
    */
   public VisualDescription addWedge(double lengthX, double widthY, double heightZ, AppearanceDefinition wedgeAppearance)
   {
      return addGeometryDescription(new Wedge3DDescription(lengthX, widthY, heightZ), wedgeAppearance);
   }

   /**
    * Adds a solid sphere with the given radius centered on the origin of the current coordinate
    * system. </ br></ br> The image below demonstrates a sphere with a 0.25 meter radius generated by
    * the following code:<br />
    * <br />
    * {@code linkGraphics.addCoordinateSystem(0.5);}<br />
    * {@code linkGraphics.addSphere(0.25);}<br />
    * <br />
    * As is show by the graphical representation the sphere is centered on the coordinate system.
    * Again, x, y and z are red, white and blue. <br />
    * <br />
    * <img src="doc-files/LinkGraphics.addSphere1.jpg">
    *
    * @param radius radius of the new sphere in meters.
    */
   public VisualDescription addSphere(double radius)
   {
      return addSphere(radius, DEFAULT_APPEARANCE);
   }

   /**
    * Adds a solid sphere with the given radius and appearance centered on the origin of the current
    * coordinate system. </ br></ br> The image below demonstrates a blue sphere with a 0.15 meter
    * radius generated by the following code:<br />
    * <br />
    * {@code linkGraphics.addCoordinateSystem(0.5);}<br />
    * {@code linkGraphics.addSphere(0.15, YoAppearance.Blue());}<br />
    * <br />
    * As is show by the graphical representation the sphere is centered on the coordinate system.
    * Again, x, y and z are red, white and blue. <br />
    * <br />
    * <img src="doc-files/LinkGraphics.addSphere2.jpg">
    *
    * @param radius           radius of the new sphere in meters.
    * @param sphereAppearance Appearance to be used with the new sphere. See {@link YoAppearance
    *                         YoAppearance} for implementations.
    */
   public VisualDescription addSphere(double radius, AppearanceDefinition sphereAppearance)
   {
      return addGeometryDescription(new Sphere3DDescription(radius, RESOLUTION), sphereAppearance);
   }

   public VisualDescription addCapsule(double radius, double height)
   {
      return addCapsule(radius, height, DEFAULT_APPEARANCE);
   }

   public VisualDescription addCapsule(double radius, double height, AppearanceDefinition capsuleAppearance)
   {
      return addGeometryDescription(new Capsule3DDescription(height, radius, RESOLUTION), capsuleAppearance);
   }

   public VisualDescription addMeshData(MeshDataHolder meshData, AppearanceDefinition appearance)
   {
      // The subsequent classes do not accept null, just create an empty mesh in that case
      return addGeometryDescription(createMeshDescription(meshData, appearance), appearance);
   }

   public static MeshDescription createMeshDescription(MeshDataHolder meshData, AppearanceDefinition meshAppearance)
   {
      if (meshData == null)
      {
         meshData = new MeshDataHolder(new Point3D32[0], new Point2D32[0], new int[0], new Vector3D32[0]);
         meshData.setName("nullMesh");
      }
      return new MeshDescription(meshData);
   }

   /**
    * Adds a solid ellipsoid with the given radii centered on the origin of the current coordinate
    * system. </ br></ br> The image below demonstrates an ellipsoid with radii of 0.3, 0.2 and 0.1 in
    * the x, y and z directions respectively:<br />
    * <br />
    * {@code linkGraphics.addCoordinateSystem(0.5);}<br />
    * {@code linkGraphics.addEllipsoid(0.3, 0.2, 0.1);}<br />
    * <br />
    * As is show by the graphical representation the ellipsoid is centered on the coordinate system.
    * Again, x, y and z are red, white and blue. <br />
    * <br />
    * <img src="doc-files/LinkGraphics.addEllipsoid1.jpg">
    *
    * @param xRadius x direction radius in meters
    * @param yRadius y direction radius in meters
    * @param zRadius z direction radius in meters
    */
   public VisualDescription addEllipsoid(double xRadius, double yRadius, double zRadius)
   {
      return addEllipsoid(xRadius, yRadius, zRadius, DEFAULT_APPEARANCE);
   }

   /**
    * Adds a solid ellipsoid with the given radii and appearance centered on the origin of the current
    * coordinate system. </ br></ br> The image below demonstrates a red ellipsoid with radii of 0.2,
    * 0.2 and 0.1 in the x, y and z directions respectively:<br />
    * <br />
    * {@code linkGraphics.addCoordinateSystem(0.5);}<br />
    * {@code linkGraphics.addEllipsoid(0.2, 0.2, 0.1, YoAppearance.Red());}<br />
    * <br />
    * As is show by the graphical representation the ellipsoid is centered on the coordinate system.
    * Again, x, y and z are red, white and blue. <br />
    * <br />
    * <img src="doc-files/LinkGraphics.addEllipsoid2.jpg">
    *
    * @param xRadius             x direction radius in meters
    * @param yRadius             y direction radius in meters
    * @param zRadius             z direction radius in meters
    * @param ellipsoidAppearance Appearance to be used with the new ellipsoid. See {@link YoAppearance
    *                            YoAppearance} for implementations.
    */
   public VisualDescription addEllipsoid(double xRadius, double yRadius, double zRadius, AppearanceDefinition ellipsoidAppearance)
   {
      return addGeometryDescription(new Ellipsoid3DDescription(xRadius, yRadius, zRadius, RESOLUTION), ellipsoidAppearance);
   }

   /**
    * Adds a soild cylinder with the given radius and height centered on the origin of the current
    * coordinate system. </ br></ br> The image below demonstrates a cylinder with radius of 0.2 and a
    * height of 0.4 as described by the following code:<br />
    * <br />
    * {@code linkGraphics.addCoordinateSystem(0.5);}<br />
    * {@code linkGraphics.addCylinder(0.4, 0.2);}<br />
    * <br />
    * As is show by the graphical representation the cylinder is centered on the coordinate system.
    * Again, x, y and z are red, white and blue. <br />
    * <br />
    * <img src="doc-files/LinkGraphics.addCylinder1.jpg">
    *
    * @param height cylinder height in meters.
    * @param radius cylinder radius in meters.
    */
   public VisualDescription addCylinder(double height, double radius)
   {
      return addCylinder(height, radius, DEFAULT_APPEARANCE);
   }

   /**
    * Adds a soild cylinder with the given radius, height and appearance centered on the origin of the
    * current coordinate system. </ br></ br> The image below demonstrates a maroon cylinder with
    * radius of 0.3 and a height of 0.1 as described by the following code:<br />
    * <br />
    * {@code linkGraphics.addCoordinateSystem(0.5);}<br />
    * {@code linkGraphics.addCylinder(0.1, 0.3, YoAppearance.Maroon());}<br />
    * <br />
    * As is show by the graphical representation the cylinder is centered on the coordinate system.
    * Again, x, y and z are red, white and blue. <br />
    * <br />
    * <img src="doc-files/LinkGraphics.addCylinder2.jpg">
    *
    * @param height cylinder height in meters.
    * @param radius cylinder radius in meters.
    * @param cylApp Appearance to be used with the new cylinder. See {@link YoAppearance YoAppearance}
    *               for implementations.
    */
   public VisualDescription addCylinder(double height, double radius, AppearanceDefinition cylApp)
   {
      return addGeometryDescription(new Cylinder3DDescription(height, radius, false, RESOLUTION), cylApp);
   }

   /**
    * Adds a cone with the given height and radius centered on the origin of the current coordinate
    * system. </ br></ br> The image below demonstrates a cone with radius of 0.15 and a height of 0.35
    * as described by the following code:<br />
    * <br />
    * {@code linkGraphics.addCoordinateSystem(0.5);}<br />
    * {@code linkGraphics.addCone(0.35, 0.15);}<br />
    * <br />
    * As is show by the graphical representation the cone is centered on the coordinate system. Again,
    * x, y and z are red, white and blue. <br />
    * <br />
    * <img src="doc-files/LinkGraphics.addCone1.jpg">
    *
    * @param height cone height in meters.
    * @param radius cone radius in meters.
    */
   public VisualDescription addCone(double height, double radius)
   {
      return addCone(height, radius, DEFAULT_APPEARANCE);
   }

   /**
    * Adds a cone with the given height, radius and appearance centered on the origin of the current
    * coordinate system. </ br></ br> The image below demonstrates a dark green cone with radius of 0.4
    * and a height of 0.2 as described by the following code:<br />
    * <br />
    * {@code linkGraphics.addCoordinateSystem(0.5);}<br />
    * {@code linkGraphics.addCone(0.2, 0.4, YoAppearance.DarkGreen());}<br />
    * <br />
    * As is show by the graphical representation the cone is centered on the coordinate system. Again,
    * x, y and z are red, white and blue. <br />
    * <br />
    * <img src="doc-files/LinkGraphics.addCone2.jpg">
    *
    * @param height  cone height in meters.
    * @param radius  cone radius in meters.
    * @param coneApp Appearance to be used with the new cone. See {@link YoAppearance YoAppearance} for
    *                implementations.
    */
   public VisualDescription addCone(double height, double radius, AppearanceDefinition coneApp)
   {
      return addGeometryDescription(new Cone3DDescription(height, radius, RESOLUTION), coneApp);
   }

   /**
    * Adds a truncated cone with the given height, base width x, base width y, top width x, and top
    * width y centered on the origin of the current coordinate system. </ br></ br> The image below
    * demonstrates a truncated cone with a height of 0.3, a x base width of 0.25, a y base width of
    * 0.2, a x top width of 0.15, and a y top width of 0.1:<br />
    * <br />
    * {@code linkGraphics.addCoordinateSystem(0.5);}<br />
    * {@code linkGraphics.addGenTruncatedCone(0.3, 0.25, 0.2, 0.15, 0.1);}<br />
    * <br />
    * As is show by the graphical representation the truncated cone is centered on the coordinate
    * system. Again, x, y and z are red, white and blue. <br />
    * <br />
    * <img src="doc-files/LinkGraphics.addGenTruncatedCone1.jpg">
    *
    * @param height in meters
    * @param bx     x direction width of the base in meters
    * @param by     y direction width of the base in meters
    * @param tx     x direction width of the top in meters
    * @param ty     y direction width of the top in meters
    */
   public VisualDescription addGenTruncatedCone(double height, double bx, double by, double tx, double ty)
   {
      return addGenTruncatedCone(height, bx, by, tx, ty, DEFAULT_APPEARANCE);
   }

   /**
    * Adds a truncated cone with the given height, base width x, base width y, top width x, top width
    * y, and appearance centered on the origin of the current coordinate system. </ br></ br> The image
    * below demonstrates a navy blue truncated cone with a height of 0.25, a x base width of 0.15, a y
    * base width of 0.15, a x top width of 0.05, and a y top width of 0.1:<br />
    * <br />
    * {@code linkGraphics.addCoordinateSystem(0.5);}<br />
    * {@code linkGraphics.addGenTruncatedCone(0.25, 0.15, 0.15, 0.05, 0.1, YoAppearance.Navy());}<br />
    * <br />
    * As is show by the graphical representation the truncated cone is centered on the coordinate
    * system. Again, x, y and z are red, white and blue. <br />
    * <br />
    * <img src="doc-files/LinkGraphics.addGenTruncatedCone2.jpg">
    *
    * @param height  in meters
    * @param bx      x direction width of the base in meters
    * @param by      y direction width of the base in meters
    * @param tx      x direction width of the top in meters
    * @param ty      y direction width of the top in meters
    * @param coneApp Appearance to be used with the new truncated cone. See {@link YoAppearance
    *                YoAppearance} for implementations.
    */
   public VisualDescription addGenTruncatedCone(double height, double bx, double by, double tx, double ty, AppearanceDefinition coneApp)
   {
      return addGeometryDescription(new TruncatedCone3DDescription(height, tx, ty, bx, by, RESOLUTION), coneApp);
   }

   /**
    * Adds a hemi ellipsoid with the given x, y and z radii centered on the current coordinate system.
    * Hemi ellipsoids are essentially cut in half, in this case the missing half is below the xy plane.
    * </ br></ br> The image below demonstrates a hemi ellipsoid with x, y and z radii of 0.25, 0.15
    * and 0.35 respectively:<br />
    * <br />
    * {@code linkGraphics.addCoordinateSystem(0.5);}<br />
    * {@code linkGraphics.addHemiEllipsoid(0.25, 0.15, 0.35);}<br />
    * <br />
    * As is show by the graphical representation the hemi ellipsoid is centered on the coordinate
    * system. Again, x, y and z are red, white and blue. <br />
    * <br />
    * <img src="doc-files/LinkGraphics.addHemiEllipsoid1.jpg">
    *
    * @param xRad radius of the ellipsoid in the x direction.
    * @param yRad radius of the ellipsoid in the y direction.
    * @param zRad radius of the ellipsoid in the z direction.
    */
   public VisualDescription addHemiEllipsoid(double xRad, double yRad, double zRad)
   {
      return addHemiEllipsoid(xRad, yRad, zRad, DEFAULT_APPEARANCE);
   }

   /**
    * Adds a hemi ellipsoid with the given appearance and x, y and z radii centered on the current
    * coordinate system. Hemi ellipsoids are essentially cut in half, in this case the missing half is
    * below the xy plane. </ br></ br> The image below demonstrates a dark red hemi ellipsoid with x, y
    * and z radii of 0.15, 0.2 and 0.4 respectively:<br />
    * <br />
    * {@code linkGraphics.addCoordinateSystem(0.5);}<br />
    * {@code linkGraphics.addHemiEllipsoid(0.15, 0.2, 0.4, YoAppearance.DarkRed());}<br />
    * <br />
    * As is show by the graphical representation the hemi ellipsoid is centered on the coordinate
    * system. Again, x, y and z are red, white and blue. <br />
    * <br />
    * <img src="doc-files/LinkGraphics.addHemiEllipsoid2.jpg">
    *
    * @param xRad  radius of the ellipsoid in the x direction.
    * @param yRad  radius of the ellipsoid in the y direction.
    * @param zRad  radius of the ellipsoid in the z direction.
    * @param hEApp Appearance to be used with the new hemi ellipsoid. See {@link YoAppearance
    *              YoAppearance} for implementations.
    */
   public VisualDescription addHemiEllipsoid(double xRad, double yRad, double zRad, AppearanceDefinition hEApp)
   {
      return addGeometryDescription(new HemiEllipsoid3DDescription(xRad, yRad, zRad, RESOLUTION), hEApp);
   }

   /**
    * Creates an ArcTorus centered on the current coordinate system. An ArcTorus is a toroid shape
    * beginning at the start angle and wrapping around to the end angle. Ensure that the angles don't
    * overlap as the shape will appear inverted; the start angle must be smaller than the end angle.
    * All angles are measured in radians. The shape is also defined by its major and minor radii. </
    * br></ br> The image below demonstrates a arctorus beginning at 3/2 pi and ending at 5/2 pi with
    * major and minor radii of 0.25 and 0.15 respectively:<br />
    * <br />
    * {@code linkGraphics.addCoordinateSystem(0.5);}<br />
    * {@code linkGraphics.addArcTorus(3*Math.PI/2, 5*Math.PI/2, 0.25, 0.15);}<br />
    * <br />
    * As is show by the graphical representation the arctorus is centered on the coordinate system.
    * Note that the ends of the torus have no texture or surface, they are transparent. Again, x, y and
    * z are red, white and blue. <br />
    * <br />
    * <img src="doc-files/LinkGraphics.addArcTorus1.jpg">
    *
    * @param startAngle  Angle in radians at which the torus begins.
    * @param endAngle    Angle in radians at which the torus ends
    * @param majorRadius Distance from the origin to the center of the torus
    * @param minorRadius Distance from the center of the torus to the walls on either side.
    */
   public VisualDescription addArcTorus(double startAngle, double endAngle, double majorRadius, double minorRadius)
   {
      return addArcTorus(startAngle, endAngle, majorRadius, minorRadius, DEFAULT_APPEARANCE);
   }

   /**
    * Creates an ArcTorus centered on the current coordinate system with the specified appearance. An
    * ArcTorus is a toroid shape beginning at the start angle and wrapping around to the end angle.
    * Ensure that the angles don't overlap as the shape will appear inverted; the start angle must be
    * smaller than the end angle. All angles are measured in radians. The shape is also defined by its
    * major and minor radii. The minor radius is from the origin to the inner wall of the torus while
    * the major is from the origin to the outer wall. </ br></ br> The image below demonstrates an aqua
    * arctorus beginning at 0 and ending at 3/2 pi with major and minor radii of 0.30 and 0.05
    * respectively:<br />
    * <br />
    * {@code linkGraphics.addCoordinateSystem(0.5);}<br />
    * {@code linkGraphics.addArcTorus(0, 3*Math.PI/2, 0.30, 0.05);}<br />
    * <br />
    * As is show by the graphical representation the arctorus is centered on the coordinate system.
    * Note that the ends of the torus have no texture or surface, they are transparent. Again, x, y and
    * z are red, white and blue. <br />
    * <br />
    * <img src="doc-files/LinkGraphics.addArcTorus2.jpg">
    *
    * @param startAngle  Angle in radians at which the torus begins.
    * @param endAngle    Angle in radians at which the torus ends.
    * @param majorRadius Distance from the origin to the center of the torus.
    * @param minorRadius Distance from the center of the torus to the walls on either side.
    * @param arcTorusApp Appearance to be used with the new arctorus. See {@link YoAppearance
    *                    YoAppearance} for implementations.
    */
   public VisualDescription addArcTorus(double startAngle, double endAngle, double majorRadius, double minorRadius, AppearanceDefinition arcTorusApp)
   {
      return addGeometryDescription(new ArcTorus3DDescription(startAngle, endAngle, majorRadius, minorRadius, RESOLUTION), arcTorusApp);
   }

   /**
    * Creates a pyramid cube centered on the origin of the current coordinate system. A pyramid cube is
    * nothing more than a standard cube of the given length, width and height with a square base
    * pyramid of the specified height on the top and bottom. </ br></ br> The image below demonstrates
    * a pyramid cube beginning with dimensions of 0.2 by 0.2 by 0.2 and a pyramid height of 0.2:<br />
    * <br />
    * {@code linkGraphics.addCoordinateSystem(0.5);}<br />
    * {@code linkGraphics.addPyramidCube(0.2, 0.2, 0.2, 0.2);}<br />
    * <br />
    * As is show by the graphical representation the pyramid cube is centered on the coordinate system.
    * Again, x, y and z are red, white and blue. <br />
    * <br />
    * <img src="doc-files/LinkGraphics.addPyramidCube1.jpg">
    *
    * @param lx Length in meters of the cube. (x direction)
    * @param ly Width in meters of the cube. (y direction)
    * @param lz Height of the cube in meters. (z direction)
    * @param lh Height of the pyramids in meters.
    */
   public VisualDescription addPyramidCube(double lx, double ly, double lz, double lh)
   {
      return addPyramidCube(lx, ly, lz, lh, DEFAULT_APPEARANCE);
   }

   /**
    * Creates a pyramid cube with the specified appearance centered on the origin of the current
    * coordinate system. A pyramid cube is nothing more than a standard cube of the given length, width
    * and height with a square base pyramid of the specified height on the top and bottom. </ br></ br>
    * The image below demonstrates a alluminum pyramid cube beginning with dimensions of 0.4 by 0.2 by
    * 0.1 and a pyramid height of 0.3:<br />
    * <br />
    * {@code linkGraphics.addCoordinateSystem(0.5);}<br />
    * {@code linkGraphics.addPyramidCube(0.2, 0.2, 0.2, 0.2);}<br />
    * <br />
    * As is show by the graphical representation the pyramid cube is centered on the coordinate system.
    * Again, x, y and z are red, white and blue. <br />
    * <br />
    * <img src="doc-files/LinkGraphics.addPyramidCube2.jpg">
    *
    * @param lx      Length in meters of the cube. (x direction)
    * @param ly      Width in meters of the cube. (y direction)O
    * @param lz      Height of the cube in meters. (z direction)
    * @param lh      Height of the pyramids in meters.
    * @param cubeApp Appearance to be used with the new pyramid cube. See {@link YoAppearance
    *                YoAppearance} for implementations.
    */
   public VisualDescription addPyramidCube(double lx, double ly, double lz, double lh, AppearanceDefinition cubeApp)
   {
      return addGeometryDescription(new PyramidBox3DDescription(lx, ly, lz, lh), cubeApp);
   }

   public VisualDescription addPolygon(List<? extends Point3DReadOnly> polygonPoints)
   {
      return addPolygon(polygonPoints, DEFAULT_APPEARANCE);
   }

   /**
    * Creates a polygon centered at the current coordinate system with the given vertices. The points
    * this shape is composed of must be coplanar and the order matters. Randomly inserting points will
    * produce unpredictable results, clockwise direction determines the side that is drawn.
    *
    * @param polygonPoints ArrayList containing the points.
    * @param yoAppearance  Appearance to be used with the new polygon. See {@link YoAppearance
    *                      YoAppearance} for implementations.
    */
   public VisualDescription addPolygon(List<? extends Point3DReadOnly> polygonPoints, AppearanceDefinition yoAppearance)
   {
      return addGeometryDescription(new Polygon3DDescription(polygonPoints.stream().map(Point3D::new).collect(Collectors.toList()), true), yoAppearance);
   }

   /**
    * Creates a polygon centered at the current coordinate system with the given vertices. The points
    * this shape is composed of must be coplanar and the order matters. Randomly inserting points will
    * produce unpredictable results, clockwise direction determines the side that is drawn.
    * 
    * @param convexPolygon2d ConvexPolygon2d containing the points.
    * @param yoAppearance    Appearance to be used with the new polygon. See {@link YoAppearance
    *                        YoAppearance} for implementations.
    */
   public VisualDescription addPolygon(ConvexPolygon2DReadOnly convexPolygon2d, AppearanceDefinition yoAppearance)
   {
      List<Point3D> polygonPoints = new ArrayList<>();
      int numPoints = convexPolygon2d.getNumberOfVertices();

      for (int i = 0; i < numPoints; i++)
      {
         Point2DReadOnly planarPoint = convexPolygon2d.getVertex(i);
         polygonPoints.add(new Point3D(planarPoint.getX(), planarPoint.getY(), 0.0));
      }

      return addPolygon(polygonPoints, yoAppearance);
   }

   public VisualDescription addPolygon(ConvexPolygon2DReadOnly convexPolygon2d)
   {
      return addPolygon(convexPolygon2d, DEFAULT_APPEARANCE);
   }

   public void addPolygons(RigidBodyTransformReadOnly transform, List<? extends ConvexPolygon2DReadOnly> convexPolygon2D)
   {
      addPolygons(transform, convexPolygon2D, YoAppearance.Black());
   }

   public void addPolygons(RigidBodyTransformReadOnly transform, List<? extends ConvexPolygon2DReadOnly> convexPolygon2D, AppearanceDefinition appearance)
   {
      appendTransform(transform);

      for (int i = 0; i < convexPolygon2D.size(); i++)
      {
         ConvexPolygon2DReadOnly convexPolygon = convexPolygon2D.get(i);
         MeshDataHolder meshDataHolder = MeshDataGenerator.ExtrudedPolygon(convexPolygon, -0.0001);
         addMeshData(meshDataHolder, appearance);
      }

      RigidBodyTransform transformLocal = new RigidBodyTransform(transform);
      transformLocal.invert();
      appendTransform(transformLocal);
   }

   /**
    * Creates a polygon centered at the current coordinate system with the given vertices. The points
    * this shape is composed of must be coplanar and in a logical order. Randomly inserting points will
    * produce unpredictable results, clockwise direction determines the side that is drawn.
    *
    * @param polygonPoint Array containing Point3D's to be used when generating the shape.
    */
   public VisualDescription addPolygon(Point3DReadOnly[] polygonPoint)
   {
      return addPolygon(polygonPoint, DEFAULT_APPEARANCE);
   }

   /**
    * Creates a polygon centered at the current coordinate system with the given vertices. The points
    * this shape is composed of must be coplanar and the order matters. Randomly inserting points will
    * produce unpredictable results, clockwise direction determines the side that is drawn.
    *
    * @param polygonPoints Array containing the points
    * @param yoAppearance  Appearance to be used with the new polygon. See {@link AppearanceDefinition}
    *                      for implementations.
    */
   public VisualDescription addPolygon(Point3DReadOnly[] polygonPoints, AppearanceDefinition yoAppearance)
   {
      MeshDataHolder meshData = MeshDataGenerator.PolygonCounterClockwise(Arrays.asList(polygonPoints));
      return addMeshData(meshData, yoAppearance);
   }

   public VisualDescription addPolygon(AppearanceDefinition yoAppearance, Point3DReadOnly... polygonPoints)
   {
      return addPolygon(polygonPoints, yoAppearance);
   }

   public VisualDescription addExtrudedPolygon(ConvexPolygon2DReadOnly convexPolygon2d, double height)
   {
      return addExtrudedPolygon(convexPolygon2d, height, DEFAULT_APPEARANCE);
   }

   public VisualDescription addExtrudedPolygon(ConvexPolygon2DReadOnly convexPolygon2d, double height, AppearanceDefinition appearance)
   {
      List<Point2D> vertices = convexPolygon2d.getPolygonVerticesView().stream().map(Point2D::new).collect(Collectors.toList());
      return addGeometryDescription(new ExtrudedPolygon2DDescription(vertices, true, height), appearance);
   }

   public VisualDescription addExtrudedPolygon(List<? extends Point2DReadOnly> polygonPoints, double height)
   {
      return addExtrudedPolygon(polygonPoints, height, DEFAULT_APPEARANCE);
   }

   public VisualDescription addExtrudedPolygon(List<? extends Point2DReadOnly> polygonPoints, double height, AppearanceDefinition appearance)
   {
      List<Point2D> vertices = polygonPoints.stream().map(Point2D::new).collect(Collectors.toList());
      return addGeometryDescription(new ExtrudedPolygon2DDescription(vertices, true, height), appearance);
   }

   /**
    * Create an extrusion of a BufferedImage. Black pixels of the image are extruded. A pixel is
    * considered black when (red+green+blue)/3 < 60
    *
    * @param bufferedImageToExtrude BufferedImage to extrude
    * @param thickness              Thinkness of extrusion
    * @param appearance             Appearance
    */
   public VisualDescription addExtrusion(BufferedImage bufferedImageToExtrude, double thickness, AppearanceDefinition appearance)
   {
      return addGeometryDescription(new ExtrusionDescription(bufferedImageToExtrude, thickness), appearance);
   }

   public VisualDescription addText(String text, double thickness, AppearanceDefinition appearance)
   {
      return addGeometryDescription(new ExtrusionDescription(text, thickness), appearance);
   }

   public void createInertiaEllipsoid(Matrix3DReadOnly momentOfInertia, Vector3DReadOnly comOffset, double mass, AppearanceDefinition appearance)
   {
      double Ixx = momentOfInertia.getM00();
      double Iyy = momentOfInertia.getM11();
      double Izz = momentOfInertia.getM22();

      //    http://en.wikipedia.org/wiki/Ellipsoid#Mass_properties
      Vector3D ellipsoidRadii = new Vector3D();
      ellipsoidRadii.setX(Math.sqrt(5.0 / 2.0 * (Iyy + Izz - Ixx) / mass));
      ellipsoidRadii.setY(Math.sqrt(5.0 / 2.0 * (Izz + Ixx - Iyy) / mass));
      ellipsoidRadii.setZ(Math.sqrt(5.0 / 2.0 * (Ixx + Iyy - Izz) / mass));

      this.appendTranslation(comOffset);
      this.addEllipsoid(ellipsoidRadii.getX(), ellipsoidRadii.getY(), ellipsoidRadii.getZ(), appearance);
      identity();
   }

   public VisualDescription addHeightMap(HeightMap heightMap, int xPointsPerSide, int yPointsPerSide, AppearanceDefinition appearance)
   {
      return addGeometryDescription(new HeightMapDescription(heightMap, xPointsPerSide, yPointsPerSide), appearance);
   }

   public boolean isChangeable()
   {
      return changeable;
   }

   public void setChangeable(boolean changeable)
   {
      this.changeable = changeable;
   }
}
