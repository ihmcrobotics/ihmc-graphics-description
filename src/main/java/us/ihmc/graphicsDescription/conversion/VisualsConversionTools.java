package us.ihmc.graphicsDescription.conversion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import us.ihmc.euclid.matrix.LinearTransform3D;
import us.ihmc.euclid.transform.AffineTransform;
import us.ihmc.euclid.tuple2D.Point2D32;
import us.ihmc.graphicsDescription.Graphics3DObject;
import us.ihmc.graphicsDescription.MeshDataHolder;
import us.ihmc.graphicsDescription.TexCoord2f;
import us.ihmc.graphicsDescription.appearance.AppearanceDefinition;
import us.ihmc.graphicsDescription.appearance.SDFAppearance;
import us.ihmc.graphicsDescription.appearance.YoAppearanceMaterial;
import us.ihmc.graphicsDescription.appearance.YoAppearanceRGBColor;
import us.ihmc.graphicsDescription.appearance.YoAppearanceTexture;
import us.ihmc.graphicsDescription.color.MutableColor;
import us.ihmc.graphicsDescription.instructions.ArcTorusGraphics3DInstruction;
import us.ihmc.graphicsDescription.instructions.CapsuleGraphics3DInstruction;
import us.ihmc.graphicsDescription.instructions.ConeGraphics3DInstruction;
import us.ihmc.graphicsDescription.instructions.CubeGraphics3DInstruction;
import us.ihmc.graphicsDescription.instructions.CylinderGraphics3DInstruction;
import us.ihmc.graphicsDescription.instructions.EllipsoidGraphics3DInstruction;
import us.ihmc.graphicsDescription.instructions.ExtrudedPolygonGraphics3DInstruction;
import us.ihmc.graphicsDescription.instructions.Graphics3DAddExtrusionInstruction;
import us.ihmc.graphicsDescription.instructions.Graphics3DAddHeightMapInstruction;
import us.ihmc.graphicsDescription.instructions.Graphics3DAddMeshDataInstruction;
import us.ihmc.graphicsDescription.instructions.Graphics3DAddModelFileInstruction;
import us.ihmc.graphicsDescription.instructions.Graphics3DInstruction;
import us.ihmc.graphicsDescription.instructions.Graphics3DPrimitiveInstruction;
import us.ihmc.graphicsDescription.instructions.HemiEllipsoidGraphics3DInstruction;
import us.ihmc.graphicsDescription.instructions.PolygonGraphics3DInstruction;
import us.ihmc.graphicsDescription.instructions.PrimitiveGraphics3DInstruction;
import us.ihmc.graphicsDescription.instructions.PyramidCubeGraphics3DInstruction;
import us.ihmc.graphicsDescription.instructions.SphereGraphics3DInstruction;
import us.ihmc.graphicsDescription.instructions.TruncatedConeGraphics3DInstruction;
import us.ihmc.graphicsDescription.instructions.WedgeGraphics3DInstruction;
import us.ihmc.graphicsDescription.instructions.primitives.Graphics3DIdentityInstruction;
import us.ihmc.graphicsDescription.instructions.primitives.Graphics3DRotateInstruction;
import us.ihmc.graphicsDescription.instructions.primitives.Graphics3DScaleInstruction;
import us.ihmc.graphicsDescription.instructions.primitives.Graphics3DTranslateInstruction;
import us.ihmc.scs2.definition.AffineTransformDefinition;
import us.ihmc.scs2.definition.geometry.ArcTorus3DDefinition;
import us.ihmc.scs2.definition.geometry.Box3DDefinition;
import us.ihmc.scs2.definition.geometry.Capsule3DDefinition;
import us.ihmc.scs2.definition.geometry.Cone3DDefinition;
import us.ihmc.scs2.definition.geometry.ConvexPolytope3DDefinition;
import us.ihmc.scs2.definition.geometry.Cylinder3DDefinition;
import us.ihmc.scs2.definition.geometry.Ellipsoid3DDefinition;
import us.ihmc.scs2.definition.geometry.ExtrudedPolygon2DDefinition;
import us.ihmc.scs2.definition.geometry.ExtrusionDefinition;
import us.ihmc.scs2.definition.geometry.GeometryDefinition;
import us.ihmc.scs2.definition.geometry.HemiEllipsoid3DDefinition;
import us.ihmc.scs2.definition.geometry.ModelFileGeometryDefinition;
import us.ihmc.scs2.definition.geometry.ModelFileGeometryDefinition.SubMeshDefinition;
import us.ihmc.scs2.definition.geometry.Polygon3DDefinition;
import us.ihmc.scs2.definition.geometry.PyramidBox3DDefinition;
import us.ihmc.scs2.definition.geometry.Ramp3DDefinition;
import us.ihmc.scs2.definition.geometry.Sphere3DDefinition;
import us.ihmc.scs2.definition.geometry.Torus3DDefinition;
import us.ihmc.scs2.definition.geometry.TriangleMesh3DDefinition;
import us.ihmc.scs2.definition.geometry.TruncatedCone3DDefinition;
import us.ihmc.scs2.definition.visual.ColorDefinition;
import us.ihmc.scs2.definition.visual.MaterialDefinition;
import us.ihmc.scs2.definition.visual.TextureDefinition;
import us.ihmc.scs2.definition.visual.VisualDefinition;

/**
 * This class defines conversions from SCS2 visual types ({@link VisualDefinition}) to SCS1 visual
 * types ({@link Graphics3DObject}).
 */
public class VisualsConversionTools
{
   /////////////////////////////////////////////////////////////////////////////////////////////////////////////
   ///////////////////// Conversion from SCS2 types to SCS1 ////////////////////////////////////////////////////
   /////////////////////////////////////////////////////////////////////////////////////////////////////////////

   public static Graphics3DObject toGraphics3DObject(Collection<? extends VisualDefinition> source)
   {
      Graphics3DObject output = new Graphics3DObject();
      for (VisualDefinition visualDefinition : source)
         output.combine(toGraphics3DObject(visualDefinition));
      return output;
   }

   public static Graphics3DObject toGraphics3DObject(VisualDefinition source)
   {
      if (source == null)
         return null;

      Graphics3DObject output = new Graphics3DObject();
      AffineTransformDefinition originPose = source.getOriginPose();

      if (originPose.hasTranslation())
      {
         output.translate(originPose.getTranslation());
      }

      if (originPose.hasLinearTransform())
      {
         LinearTransform3D linearTransform = originPose.getLinearTransform();

         if (linearTransform.isRotationMatrix())
         {
            output.rotate(linearTransform.getAsQuaternion());
         }
         else
         {
            if (!linearTransform.getPreScaleQuaternion().isZeroOrientation())
               output.rotate(linearTransform.getPreScaleQuaternion());
            output.scale(linearTransform.getScaleVector());
            if (!linearTransform.getPostScaleQuaternion().isZeroOrientation())
               output.rotate(linearTransform.getPostScaleQuaternion());
         }
      }

      List<Graphics3DPrimitiveInstruction> instructions = toGraphics3DPrimitiveInstruction(source.getGeometryDefinition());
      if (instructions == null || instructions.isEmpty())
         return null;

      for (Graphics3DPrimitiveInstruction instruction : instructions)
      {
         if (instruction instanceof Graphics3DInstruction)
            ((Graphics3DInstruction) instruction).setAppearance(toAppearanceDefinition(source.getMaterialDefinition()));
         output.addInstruction(instruction);
      }
      return output;
   }

   public static List<Graphics3DPrimitiveInstruction> toGraphics3DPrimitiveInstruction(GeometryDefinition source)
   {
      if (source == null)
         return null;

      if (source instanceof ArcTorus3DDefinition)
      {
         ArcTorus3DDefinition arcTorus = (ArcTorus3DDefinition) source;
         return Collections.singletonList(new ArcTorusGraphics3DInstruction(arcTorus.getStartAngle(),
                                                                            arcTorus.getEndAngle(),
                                                                            arcTorus.getMajorRadius(),
                                                                            arcTorus.getMinorRadius(),
                                                                            arcTorus.getResolution()));
      }
      else if (source instanceof Box3DDefinition)
      {
         Box3DDefinition box = (Box3DDefinition) source;
         return Collections.singletonList(new CubeGraphics3DInstruction(box.getSizeX(), box.getSizeY(), box.getSizeZ(), box.isCentered()));
      }
      else if (source instanceof Capsule3DDefinition)
      {
         Capsule3DDefinition capsule = (Capsule3DDefinition) source;
         return Collections.singletonList(new CapsuleGraphics3DInstruction(capsule.getLength(),
                                                                           capsule.getRadiusX(),
                                                                           capsule.getRadiusY(),
                                                                           capsule.getRadiusZ(),
                                                                           capsule.getResolution()));
      }
      else if (source instanceof Cone3DDefinition)
      {
         Cone3DDefinition cone = (Cone3DDefinition) source;
         return Collections.singletonList(new ConeGraphics3DInstruction(cone.getHeight(), cone.getRadius(), cone.getResolution()));
      }
      else if (source instanceof ConvexPolytope3DDefinition)
      {
         return null; // TODO Not sure here
      }
      else if (source instanceof Cylinder3DDefinition)
      {
         Cylinder3DDefinition cylinder = (Cylinder3DDefinition) source; // FIXME Handle the offset along the cylinder's axis.
         return Collections.singletonList(new CylinderGraphics3DInstruction(cylinder.getRadius(), cylinder.getLength(), cylinder.getResolution()));
      }
      else if (source instanceof Ellipsoid3DDefinition)
      {
         Ellipsoid3DDefinition ellipsoid = (Ellipsoid3DDefinition) source;
         return Collections.singletonList(new EllipsoidGraphics3DInstruction(ellipsoid.getRadiusX(),
                                                                             ellipsoid.getRadiusY(),
                                                                             ellipsoid.getRadiusZ(),
                                                                             ellipsoid.getResolution()));
      }
      else if (source instanceof ExtrudedPolygon2DDefinition)
      {
         ExtrudedPolygon2DDefinition polygon = (ExtrudedPolygon2DDefinition) source; // FIXME handle the case that bottom-z is not 0
         return Collections.singletonList(new ExtrudedPolygonGraphics3DInstruction(polygon.getPolygonVertices(), polygon.getTopZ() - polygon.getBottomZ()));
      }
      else if (source instanceof ExtrusionDefinition)
      {
         return null; // FIXME implement me
      }
      else if (source instanceof HemiEllipsoid3DDefinition)
      {
         HemiEllipsoid3DDefinition hemiEllipsoid = (HemiEllipsoid3DDefinition) source;
         return Collections.singletonList(new HemiEllipsoidGraphics3DInstruction(hemiEllipsoid.getRadiusX(),
                                                                                 hemiEllipsoid.getRadiusY(),
                                                                                 hemiEllipsoid.getRadiusZ(),
                                                                                 hemiEllipsoid.getResolution()));
      }
      else if (source instanceof ModelFileGeometryDefinition)
      {
         ModelFileGeometryDefinition model = (ModelFileGeometryDefinition) source;
         List<Graphics3DPrimitiveInstruction> output = new ArrayList<>();
         if (model.getScale() != null)
            output.add(new Graphics3DScaleInstruction(model.getScale()));
         if (model.getSubmeshes() == null || model.getSubmeshes().isEmpty())
            output.add(new Graphics3DAddModelFileInstruction(model.getFileName(), null, model.getResourceDirectories(), model.getResourceClassLoader()));
         else
            output.add(new Graphics3DAddModelFileInstruction(model.getFileName(),
                                                             model.getSubmeshes().get(0).getName(),
                                                             model.getSubmeshes().get(0).getCenter(),
                                                             null,
                                                             model.getResourceDirectories(),
                                                             model.getResourceClassLoader()));
         return output;
      }
      else if (source instanceof PyramidBox3DDefinition)
      {
         PyramidBox3DDefinition pyramidBox = (PyramidBox3DDefinition) source;
         return Collections.singletonList(new PyramidCubeGraphics3DInstruction(pyramidBox.getBoxSizeX(),
                                                                               pyramidBox.getBoxSizeY(),
                                                                               pyramidBox.getBoxSizeZ(),
                                                                               pyramidBox.getPyramidHeight()));
      }
      else if (source instanceof Ramp3DDefinition)
      {
         Ramp3DDefinition ramp = (Ramp3DDefinition) source; // FIXME The origin might not be the same.
         return Collections.singletonList(new WedgeGraphics3DInstruction(ramp.getSizeX(), ramp.getSizeY(), ramp.getSizeZ()));
      }
      else if (source instanceof Sphere3DDefinition)
      {
         Sphere3DDefinition sphere = (Sphere3DDefinition) source;
         return Collections.singletonList(new SphereGraphics3DInstruction(sphere.getRadius(), sphere.getResolution()));
      }
      else if (source instanceof Torus3DDefinition)
      {
         Torus3DDefinition torus = (Torus3DDefinition) source;
         return Collections.singletonList(new ArcTorusGraphics3DInstruction(0,
                                                                            2.0 * Math.PI,
                                                                            torus.getMajorRadius(),
                                                                            torus.getMinorRadius(),
                                                                            torus.getResolution()));
      }
      else if (source instanceof TriangleMesh3DDefinition)
      {
         TriangleMesh3DDefinition mesh = (TriangleMesh3DDefinition) source;
         return Collections.singletonList(new Graphics3DAddMeshDataInstruction(new MeshDataHolder(mesh.getVertices(),
                                                                                                  mesh.getTextures() == null ? null
                                                                                                        : Arrays.stream(mesh.getTextures())
                                                                                                                .map(t -> new TexCoord2f(t.getX32(),
                                                                                                                                         t.getY32()))
                                                                                                                .toArray(TexCoord2f[]::new),
                                                                                                  mesh.getTriangleIndices(),
                                                                                                  mesh.getNormals()),
                                                                               null));
      }
      else if (source instanceof TruncatedCone3DDefinition)
      {
         TruncatedCone3DDefinition cone = (TruncatedCone3DDefinition) source;
         return Collections.singletonList(new TruncatedConeGraphics3DInstruction(cone.getHeight(),
                                                                                 cone.getBaseRadiusX(),
                                                                                 cone.getBaseRadiusY(),
                                                                                 cone.getTopRadiusX(),
                                                                                 cone.getTopRadiusY(),
                                                                                 cone.getResolution()));
      }
      else
      {
         throw new IllegalArgumentException("Unsupported geometry type: " + source.getClass().getName());
      }
   }

   public static AppearanceDefinition toAppearanceDefinition(MaterialDefinition source)
   {
      if (source == null)
         return null;

      ColorDefinition diffuseColor = source.getDiffuseColor();
      ColorDefinition specularColor = source.getSpecularColor();
      ColorDefinition ambientColor = source.getAmbientColor();

      if (diffuseColor != null)
      {
         if (specularColor != null && ambientColor != null)
         {
            YoAppearanceMaterial output = new YoAppearanceMaterial();
            output.setDiffuseColor((float) diffuseColor.getRed(), (float) diffuseColor.getGreen(), (float) diffuseColor.getBlue());
            output.setSpecularColor((float) specularColor.getRed(), (float) specularColor.getGreen(), (float) specularColor.getBlue());
            output.setAmbientColor((float) ambientColor.getRed(), (float) ambientColor.getGreen(), (float) ambientColor.getBlue());
            output.setShininess((float) source.getShininess());
            return output;
         }
         else
         {
            return new YoAppearanceRGBColor(diffuseColor.getRed(), diffuseColor.getGreen(), diffuseColor.getBlue(), 1.0 - diffuseColor.getAlpha());
         }
      }
      else
      {
         TextureDefinition diffuseMap = source.getDiffuseMap();

         if (diffuseMap == null)
            return null;

         if (diffuseMap.getFilename() != null)
            return new YoAppearanceTexture(diffuseMap.getFilename());
         if (diffuseMap.getFileURL() != null)
            return new YoAppearanceTexture(diffuseMap.getFileURL().toExternalForm());
         if (diffuseMap.getImage() != null)
            return new YoAppearanceTexture(diffuseMap.getImage());
         return null;
      }
   }

   /////////////////////////////////////////////////////////////////////////////////////////////////////////////
   ///////////////////// Conversion from SCS1 types to SCS2 ////////////////////////////////////////////////////
   /////////////////////////////////////////////////////////////////////////////////////////////////////////////

   public static ColorDefinition toColorDefinition(MutableColor mutableColor, double transparency)
   {
      return new ColorDefinition(mutableColor.getX(), mutableColor.getY(), mutableColor.getZ(), 1.0 - transparency);
   }

   public static MaterialDefinition toMaterialDefinition(AppearanceDefinition appearanceDefinition)
   {
      if (appearanceDefinition == null)
         return null;

      MaterialDefinition output = new MaterialDefinition();

      if (appearanceDefinition instanceof SDFAppearance)
      {

      }
      else if (appearanceDefinition instanceof YoAppearanceMaterial)
      {
         YoAppearanceMaterial yoAppearanceMaterial = (YoAppearanceMaterial) appearanceDefinition;
         output.setDiffuseColor(toColorDefinition(yoAppearanceMaterial.getDiffuseColor(), yoAppearanceMaterial.getTransparency()));
         output.setSpecularColor(toColorDefinition(yoAppearanceMaterial.getSpecularColor(), yoAppearanceMaterial.getTransparency()));
         output.setShininess(yoAppearanceMaterial.getShininess());
         output.setAmbientColor(toColorDefinition(yoAppearanceMaterial.getAmbientColor(), yoAppearanceMaterial.getTransparency()));
      }
      else if (appearanceDefinition instanceof YoAppearanceRGBColor)
      {
         YoAppearanceRGBColor yoAppearanceRGBColor = (YoAppearanceRGBColor) appearanceDefinition;
         output.setDiffuseColor(toColorDefinition(yoAppearanceRGBColor.getColor(), yoAppearanceRGBColor.getTransparency()));
      }
      else if (appearanceDefinition instanceof YoAppearanceTexture)
      {
         YoAppearanceTexture yoAppearanceTexture = (YoAppearanceTexture) appearanceDefinition;
         TextureDefinition textureDefinition = new TextureDefinition();
         textureDefinition.setFilename(yoAppearanceTexture.getPath());
         textureDefinition.setImage(yoAppearanceTexture.getBufferedImage());
         output.setDiffuseMap(textureDefinition);
      }
      else
      {
         throw new UnsupportedOperationException("Unsupported appearance definition type: " + appearanceDefinition);
      }

      return output;
   }

   public static List<VisualDefinition> toVisualDefinitions(Graphics3DObject graphics3DObject)
   {
      if (graphics3DObject == null)
         return Collections.emptyList();

      List<VisualDefinition> visualDefinitions = new ArrayList<>();

      AffineTransform currentTransform = new AffineTransform();

      for (Graphics3DPrimitiveInstruction instruction : graphics3DObject.getGraphics3DInstructions())
      {
         if (instruction instanceof Graphics3DIdentityInstruction)
         {
            currentTransform.setIdentity();
         }
         else if (instruction instanceof Graphics3DRotateInstruction)
         {
            currentTransform.appendOrientation(((Graphics3DRotateInstruction) instruction).getRotationMatrix());
         }
         else if (instruction instanceof Graphics3DScaleInstruction)
         {
            currentTransform.appendScale(((Graphics3DScaleInstruction) instruction).getScaleFactor());
         }
         else if (instruction instanceof Graphics3DTranslateInstruction)
         {
            currentTransform.appendTranslation(((Graphics3DTranslateInstruction) instruction).getTranslation());
         }
         else if (instruction instanceof Graphics3DInstruction)
         {
            VisualDefinition visualDefinition = new VisualDefinition();
            visualDefinition.setOriginPose(new AffineTransform(currentTransform));
            visualDefinition.setMaterialDefinition(toMaterialDefinition(((Graphics3DInstruction) instruction).getAppearance()));
            visualDefinitions.add(visualDefinition);

            if (instruction instanceof PrimitiveGraphics3DInstruction)
            {
               if (instruction instanceof ArcTorusGraphics3DInstruction)
               {
                  ArcTorusGraphics3DInstruction arcTorus = (ArcTorusGraphics3DInstruction) instruction;
                  visualDefinition.setGeometryDefinition(new ArcTorus3DDefinition(arcTorus.getStartAngle(),
                                                                                  arcTorus.getEndAngle(),
                                                                                  arcTorus.getMajorRadius(),
                                                                                  arcTorus.getMinorRadius(),
                                                                                  arcTorus.getResolution()));
               }
               else if (instruction instanceof CapsuleGraphics3DInstruction)
               {
                  CapsuleGraphics3DInstruction capsule = (CapsuleGraphics3DInstruction) instruction;
                  visualDefinition.setGeometryDefinition(new Capsule3DDefinition(capsule.getHeight(),
                                                                                 capsule.getXRadius(),
                                                                                 capsule.getYRadius(),
                                                                                 capsule.getZRadius(),
                                                                                 capsule.getResolution()));
               }
               else if (instruction instanceof ConeGraphics3DInstruction)
               {
                  ConeGraphics3DInstruction cone = (ConeGraphics3DInstruction) instruction;
                  visualDefinition.setGeometryDefinition(new Cone3DDefinition(cone.getHeight(), cone.getRadius(), cone.getResolution()));
               }
               else if (instruction instanceof CubeGraphics3DInstruction)
               {
                  CubeGraphics3DInstruction cube = (CubeGraphics3DInstruction) instruction;
                  visualDefinition.setGeometryDefinition(new Box3DDefinition(cube.getLength(),
                                                                             cube.getWidth(),
                                                                             cube.getHeight(),
                                                                             cube.getCenteredInTheCenter()));
               }
               else if (instruction instanceof CylinderGraphics3DInstruction)
               {
                  CylinderGraphics3DInstruction cylinder = (CylinderGraphics3DInstruction) instruction;
                  visualDefinition.setGeometryDefinition(new Cylinder3DDefinition(cylinder.getHeight(), cylinder.getRadius(), false));
               }
               else if (instruction instanceof EllipsoidGraphics3DInstruction)
               {
                  EllipsoidGraphics3DInstruction ellipsoid = (EllipsoidGraphics3DInstruction) instruction;
                  visualDefinition.setGeometryDefinition(new Ellipsoid3DDefinition(ellipsoid.getXRadius(),
                                                                                   ellipsoid.getYRadius(),
                                                                                   ellipsoid.getZRadius(),
                                                                                   ellipsoid.getResolution()));
               }
               else if (instruction instanceof ExtrudedPolygonGraphics3DInstruction)
               {
                  ExtrudedPolygonGraphics3DInstruction extrusion = (ExtrudedPolygonGraphics3DInstruction) instruction;
                  visualDefinition.setGeometryDefinition(new ExtrudedPolygon2DDefinition(ExtrudedPolygon2DDefinition.toPoint2DDefinitionList(extrusion.getPolygonPoints()),
                                                                                         true,
                                                                                         extrusion.getExtrusionHeight()));
               }
               else if (instruction instanceof HemiEllipsoidGraphics3DInstruction)
               {
                  HemiEllipsoidGraphics3DInstruction hemiEllipsoid = (HemiEllipsoidGraphics3DInstruction) instruction;
                  visualDefinition.setGeometryDefinition(new HemiEllipsoid3DDefinition(hemiEllipsoid.getXRadius(),
                                                                                       hemiEllipsoid.getYRadius(),
                                                                                       hemiEllipsoid.getZRadius(),
                                                                                       hemiEllipsoid.getResolution()));
               }
               else if (instruction instanceof PolygonGraphics3DInstruction)
               {
                  PolygonGraphics3DInstruction polygon = (PolygonGraphics3DInstruction) instruction;
                  visualDefinition.setGeometryDefinition(new Polygon3DDefinition(Polygon3DDefinition.toPoint3DDefinitionList(polygon.getPolygonPoints()),
                                                                                 true));
               }
               else if (instruction instanceof PyramidCubeGraphics3DInstruction)
               {
                  PyramidCubeGraphics3DInstruction pyramid = (PyramidCubeGraphics3DInstruction) instruction;
                  visualDefinition.setGeometryDefinition(new PyramidBox3DDefinition(pyramid.getLengthX(),
                                                                                    pyramid.getWidthY(),
                                                                                    pyramid.getHeightZ(),
                                                                                    pyramid.getPyramidHeight()));
               }
               else if (instruction instanceof SphereGraphics3DInstruction)
               {
                  SphereGraphics3DInstruction sphere = (SphereGraphics3DInstruction) instruction;
                  visualDefinition.setGeometryDefinition(new Sphere3DDefinition(sphere.getRadius(), sphere.getResolution()));
               }
               else if (instruction instanceof TruncatedConeGraphics3DInstruction)
               {
                  TruncatedConeGraphics3DInstruction cone = (TruncatedConeGraphics3DInstruction) instruction;
                  visualDefinition.setGeometryDefinition(new TruncatedCone3DDefinition(cone.getHeight(),
                                                                                       cone.getXTopRadius(),
                                                                                       cone.getYTopRadius(),
                                                                                       cone.getXBaseRadius(),
                                                                                       cone.getYBaseRadius(),
                                                                                       false,
                                                                                       cone.getResolution()));
               }
               else if (instruction instanceof WedgeGraphics3DInstruction)
               {
                  WedgeGraphics3DInstruction wedge = (WedgeGraphics3DInstruction) instruction;
                  visualDefinition.setGeometryDefinition(new Ramp3DDefinition(wedge.getLengthX(), wedge.getWidthY(), wedge.getHeightZ()));
               }
            }
            else if (instruction instanceof Graphics3DAddExtrusionInstruction)
            {
               Graphics3DAddExtrusionInstruction extrusion = (Graphics3DAddExtrusionInstruction) instruction;
               visualDefinition.setGeometryDefinition(new ExtrusionDefinition(extrusion.getBufferedImage(), extrusion.getHeight()));
            }
            else if (instruction instanceof Graphics3DAddHeightMapInstruction)
            {
               throw new UnsupportedOperationException("Unsupported instruction");
            }
            else if (instruction instanceof Graphics3DAddMeshDataInstruction)
            {
               Graphics3DAddMeshDataInstruction mesh = (Graphics3DAddMeshDataInstruction) instruction;
               visualDefinition.setGeometryDefinition(new TriangleMesh3DDefinition(mesh.getMeshData().getName(),
                                                                                   mesh.getMeshData().getVertices(),
                                                                                   Stream.of(mesh.getMeshData().getTexturePoints()).map(Point2D32::new)
                                                                                         .toArray(Point2D32[]::new),
                                                                                   mesh.getMeshData().getVertexNormals(),
                                                                                   mesh.getMeshData().getTriangleIndices()));
            }
            else if (instruction instanceof Graphics3DAddModelFileInstruction)
            {
               Graphics3DAddModelFileInstruction model = (Graphics3DAddModelFileInstruction) instruction;
               ModelFileGeometryDefinition definition = new ModelFileGeometryDefinition();
               definition.setFileName(model.getFileName());
               definition.setResourceClassLoader(model.getResourceClassLoader());
               definition.setResourceDirectories(model.getResourceDirectories());
               definition.setSubmeshes(Collections.singletonList(new SubMeshDefinition(model.getSubmesh(), model.centerSubmesh())));
               visualDefinition.setGeometryDefinition(definition);
            }
            else
            {
               throw new UnsupportedOperationException("Unsupported instruction type: " + instruction);
            }
         }
         else
         {
            throw new UnsupportedOperationException("Unsupported instruction type: " + instruction);
         }
      }

      return visualDefinitions;
   }
}
