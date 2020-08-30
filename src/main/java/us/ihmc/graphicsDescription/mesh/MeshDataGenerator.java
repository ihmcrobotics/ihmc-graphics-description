package us.ihmc.graphicsDescription.mesh;

import java.util.List;

import us.ihmc.commons.MathTools;
import us.ihmc.euclid.Axis3D;
import us.ihmc.euclid.geometry.interfaces.ConvexPolygon2DReadOnly;
import us.ihmc.euclid.geometry.interfaces.LineSegment3DReadOnly;
import us.ihmc.euclid.geometry.tools.EuclidGeometryTools;
import us.ihmc.euclid.matrix.RotationMatrix;
import us.ihmc.euclid.transform.interfaces.RigidBodyTransformReadOnly;
import us.ihmc.euclid.tuple2D.Point2D32;
import us.ihmc.euclid.tuple2D.interfaces.Point2DReadOnly;
import us.ihmc.euclid.tuple3D.Point3D32;
import us.ihmc.euclid.tuple3D.Vector3D;
import us.ihmc.euclid.tuple3D.Vector3D32;
import us.ihmc.euclid.tuple3D.interfaces.Point3DReadOnly;

/**
 * This class provides factories to create generic meshes, i.e. {@code MeshDataHolder}, to represent
 * a 3D shape.
 * <p>
 * The generic mesh can be used to construct a triangle mesh in the format of the graphics engine in
 * which it will be rendered.
 * </p>
 * <p>
 * The construction methods assumes the following coordinate system convention: x is pointing
 * forward, y pointing left, z pointing upward.
 * </p>
 *
 * @author Sylvain Bertrand
 */
public class MeshDataGenerator
{
   private static final float TwoPi = 2.0f * (float) Math.PI;
   private static final float HalfPi = (float) Math.PI / 2.0f;

   private static final float SQRT3 = (float) Math.sqrt(3.0);
   private static final float SQRT6 = (float) Math.sqrt(6.0);
   private static final float HALF_SQRT3 = SQRT3 / 2.0f;
   private static final float THIRD_SQRT3 = SQRT3 / 3.0f;
   private static final float SIXTH_SQRT3 = SQRT3 / 6.0f;
   private static final float THIRD_SQRT6 = SQRT6 / 3.0f;
   private static final float FOURTH_SQRT6 = SQRT6 / 4.0f;

   private static final float ONE_THIRD = 1.0f / 3.0f;

   private MeshDataGenerator()
   {
      // Prevent an object being generated.
   }

   /**
    * Creates a triangle mesh for a 3D sphere.
    * <p>
    * The sphere is centered at the origin and is a UV sphere, see
    * <a href="https://en.wikipedia.org/wiki/UV_mapping">UV mapping</a>.
    * </p>
    *
    * @param radius              the radius of the sphere. Each vertex is positioned at that distance
    *                            from the origin.
    * @param latitudeResolution  the resolution along the vertical axis, i.e. z-axis.
    * @param longitudeResolution the resolution around the vertical axis, i.e. the number of vertices
    *                            per latitude.
    * @return the generic triangle mesh.
    */
   public static MeshDataHolder Sphere(double radius, int latitudeResolution, int longitudeResolution)
   {
      return Sphere((float) radius, latitudeResolution, longitudeResolution);
   }

   /**
    * Creates a triangle mesh for a 3D sphere.
    * <p>
    * The sphere is centered at the origin and is a UV sphere, see
    * <a href="https://en.wikipedia.org/wiki/UV_mapping">UV mapping</a>.
    * </p>
    *
    * @param radius              the radius of the sphere. Each vertex is positioned at that distance
    *                            from the origin.
    * @param latitudeResolution  the resolution along the vertical axis, i.e. z-axis.
    * @param longitudeResolution the resolution around the vertical axis, i.e. the number of vertices
    *                            per latitude.
    * @return the generic triangle mesh.
    */
   public static MeshDataHolder Sphere(float radius, int latitudeResolution, int longitudeResolution)
   {
      return Ellipsoid(radius, radius, radius, latitudeResolution, longitudeResolution);
   }

   /**
    * Creates a triangle mesh for a 3D ellipsoid.
    * <p>
    * The ellipsoid is centered at the origin and the algorithm is similar to a UV sphere, see
    * <a href="https://en.wikipedia.org/wiki/UV_mapping">UV mapping</a>.
    * </p>
    *
    * @param xRadius             radius of the ellipsoid along the x-axis.
    * @param yRadius             radius of the ellipsoid along the y-axis.
    * @param zRadius             radius of the ellipsoid along the z-axis.
    * @param latitudeResolution  the resolution along the vertical axis, i.e. z-axis.
    * @param longitudeResolution the resolution around the vertical axis, i.e. the number of vertices
    *                            per latitude.
    * @return the generic triangle mesh.
    */
   public static MeshDataHolder Ellipsoid(double xRadius, double yRadius, double zRadius, int latitudeResolution, int longitudeResolution)
   {
      return Ellipsoid((float) xRadius, (float) yRadius, (float) zRadius, latitudeResolution, longitudeResolution);
   }

   /**
    * Creates a triangle mesh for a 3D ellipsoid.
    * <p>
    * The ellipsoid is centered at the origin and the algorithm is similar to a UV sphere, see
    * <a href="https://en.wikipedia.org/wiki/UV_mapping">UV mapping</a>.
    * </p>
    *
    * @param xRadius             radius of the ellipsoid along the x-axis.
    * @param yRadius             radius of the ellipsoid along the y-axis.
    * @param zRadius             radius of the ellipsoid along the z-axis.
    * @param latitudeResolution  the resolution along the vertical axis, i.e. z-axis.
    * @param longitudeResolution the resolution around the vertical axis, i.e. the number of vertices
    *                            per latitude.
    * @return the generic triangle mesh.
    */
   public static MeshDataHolder Ellipsoid(float xRadius, float yRadius, float zRadius, int latitudeResolution, int longitudeResolution)
   {
      if (longitudeResolution % 2 == 1)
         longitudeResolution += 1;
      int nPointsLongitude = longitudeResolution + 1;
      int nPointsLatitude = latitudeResolution + 1;

      // Reminder of longitude and latitude: http://www.geographyalltheway.com/ks3_geography/maps_atlases/longitude_latitude.htm
      Point3D32 points[] = new Point3D32[nPointsLatitude * nPointsLongitude];
      Vector3D32[] normals = new Vector3D32[nPointsLatitude * nPointsLongitude];
      Point2D32 textPoints[] = new Point2D32[nPointsLatitude * nPointsLongitude];

      for (int longitudeIndex = 0; longitudeIndex < nPointsLongitude; longitudeIndex++)
      {
         float longitudeAngle = TwoPi * ((float) longitudeIndex / (float) (nPointsLongitude - 1));
         float cosLongitude = (float) Math.cos(longitudeAngle);
         float sinLongitude = (float) Math.sin(longitudeAngle);
         float textureX = (float) longitudeIndex / (float) (nPointsLongitude - 1);

         for (int latitudeIndex = 1; latitudeIndex < nPointsLatitude - 1; latitudeIndex++)
         {
            float latitudeAngle = (float) (-HalfPi + Math.PI * ((float) latitudeIndex / (nPointsLatitude - 1.0f)));
            float cosLatitude = (float) Math.cos(latitudeAngle);
            float sinLatitude = (float) Math.sin(latitudeAngle);

            int currentIndex = latitudeIndex * nPointsLongitude + longitudeIndex;
            float normalX = cosLongitude * cosLatitude;
            float normalY = sinLongitude * cosLatitude;
            float normalZ = sinLatitude;
            float vertexX = xRadius * normalX;
            float vertexY = yRadius * normalY;
            float vertexZ = zRadius * normalZ;
            points[currentIndex] = new Point3D32(vertexX, vertexY, vertexZ);
            normals[currentIndex] = new Vector3D32(normalX, normalY, normalZ);

            float textureY = 0.5f * (1.0f - sinLatitude);
            textPoints[currentIndex] = new Point2D32(textureX, textureY);
         }

         textureX += 0.5f / (nPointsLongitude - 1.0f);
         // South pole
         int southPoleIndex = longitudeIndex;
         points[southPoleIndex] = new Point3D32(0.0f, 0.0f, -zRadius);
         normals[southPoleIndex] = new Vector3D32(0.0f, 0.0f, -1.0f);
         textPoints[southPoleIndex] = new Point2D32(textureX, 1.0f - 1.0f / 256.0f);

         // North pole
         int northPoleIndex = (nPointsLatitude - 1) * nPointsLongitude + longitudeIndex;
         points[northPoleIndex] = new Point3D32(0.0f, 0.0f, zRadius);
         normals[northPoleIndex] = new Vector3D32(0.0f, 0.0f, 1.0f);
         textPoints[northPoleIndex] = new Point2D32(textureX, 1.0f / 256.0f);
      }

      int numberOfTriangles = 2 * ((nPointsLatitude - 2) * nPointsLongitude - 1);
      int[] triangleIndices = new int[3 * numberOfTriangles];

      int index = 0;

      // Mid-latitude faces
      for (int latitudeIndex = 1; latitudeIndex < nPointsLatitude - 2; latitudeIndex++)
      {
         for (int longitudeIndex = 0; longitudeIndex < nPointsLongitude - 1; longitudeIndex++)
         {
            int nextLongitudeIndex = (longitudeIndex + 1) % nPointsLongitude;
            int nextLatitudeIndex = latitudeIndex + 1;

            // Lower triangles
            triangleIndices[index++] = latitudeIndex * nPointsLongitude + longitudeIndex;
            triangleIndices[index++] = latitudeIndex * nPointsLongitude + nextLongitudeIndex;
            triangleIndices[index++] = nextLatitudeIndex * nPointsLongitude + longitudeIndex;
            // Upper triangles
            triangleIndices[index++] = latitudeIndex * nPointsLongitude + nextLongitudeIndex;
            triangleIndices[index++] = nextLatitudeIndex * nPointsLongitude + nextLongitudeIndex;
            triangleIndices[index++] = nextLatitudeIndex * nPointsLongitude + longitudeIndex;
         }
      }

      // South pole faces
      for (int longitudeIndex = 0; longitudeIndex < nPointsLongitude - 1; longitudeIndex++)
      {
         int nextLongitudeIndex = (longitudeIndex + 1) % nPointsLongitude;
         triangleIndices[index++] = longitudeIndex;
         triangleIndices[index++] = nPointsLongitude + nextLongitudeIndex;
         triangleIndices[index++] = nPointsLongitude + longitudeIndex;
      }

      // North pole faces
      for (int longitudeIndex = 0; longitudeIndex < nPointsLongitude - 1; longitudeIndex++)
      {
         int nextLongitudeIndex = (longitudeIndex + 1) % nPointsLongitude;
         triangleIndices[index++] = (nPointsLatitude - 1) * nPointsLongitude + longitudeIndex;
         triangleIndices[index++] = (nPointsLatitude - 2) * nPointsLongitude + longitudeIndex;
         triangleIndices[index++] = (nPointsLatitude - 2) * nPointsLongitude + nextLongitudeIndex;
      }

      return new MeshDataHolder(points, textPoints, triangleIndices, normals);
   }

   /**
    * Create a triangle mesh for the given polygon.
    *
    * @param convexPolygon the polygon to create a mesh from.
    * @return the generic triangle mesh.
    */
   public static MeshDataHolder Polygon(ConvexPolygon2DReadOnly convexPolygon)
   {
      return Polygon(null, convexPolygon);
   }

   /**
    * Create a triangle mesh for the given polygon.
    *
    * @param polygonPose   the 3D pose of the polygon. Can be {@code null}, in which case no transform
    *                      is applied.
    * @param convexPolygon the polygon to create a mesh from.
    * @return the generic triangle mesh or {@code null} if {@code convexPolygon} is {@code null}.
    */
   public static MeshDataHolder Polygon(RigidBodyTransformReadOnly polygonPose, ConvexPolygon2DReadOnly convexPolygon)
   {
      if (convexPolygon == null)
         return null;
      else
         return Polygon(polygonPose, convexPolygon.getPolygonVerticesView(), !convexPolygon.isClockwiseOrdered());
   }

   /**
    * Create a triangle mesh for the given polygon.
    * <p>
    * <b> It is assumed that the polygon is convex and clockwise ordered. </b>
    * </p>
    *
    * @param polygonPose              the 3D pose of the polygon. Can be {@code null}, in which case no
    *                                 transform is applied.
    * @param cwOrderedPolygonVertices the clockwise-ordered vertices of the polygon.
    * @return the generic triangle mesh or {@code null} if {@code cwOrderedPolygonVertices} is
    *         {@code null}.
    */
   public static MeshDataHolder PolygonClockwise(RigidBodyTransformReadOnly polygonPose, List<? extends Point2DReadOnly> cwOrderedPolygonVertices)
   {
      return Polygon(polygonPose, cwOrderedPolygonVertices, false);
   }

   /**
    * Create a triangle mesh for the given polygon.
    * <p>
    * <b> It is assumed that the polygon is convex and counter-clockwise ordered. </b>
    * </p>
    *
    * @param polygonPose               the 3D pose of the polygon. Can be {@code null}, in which case
    *                                  no transform is applied.
    * @param ccwOrderedPolygonVertices the counter-clockwise-ordered vertices of the polygon.
    * @return the generic triangle mesh or {@code null} if {@code ccwOrderedPolygonVertices} is
    *         {@code null}.
    */
   public static MeshDataHolder PolygonCounterClockwise(RigidBodyTransformReadOnly polygonPose, List<? extends Point2DReadOnly> ccwOrderedPolygonVertices)
   {
      return Polygon(polygonPose, ccwOrderedPolygonVertices, true);
   }

   /**
    * Create a triangle mesh for the given polygon.
    * <p>
    * <b> It is assumed that the polygon is convex. </b>
    * </p>
    *
    * @param polygonPose             the 3D pose of the polygon. Can be {@code null}, in which case no
    *                                transform is applied.
    * @param polygonVertices         the counter-clockwise-ordered vertices of the polygon.
    * @param counterClockwiseOrdered {@code true} to indicate that the vertices are counter-clockwise
    *                                ordered, {@code false} for clockwise ordered.
    * @return the generic triangle mesh or {@code null} if {@code convexPolygonVertices} is
    *         {@code null}.
    */
   public static MeshDataHolder Polygon(RigidBodyTransformReadOnly polygonPose, List<? extends Point2DReadOnly> polygonVertices,
                                        boolean counterClockwiseOrdered)
   {
      if (polygonVertices == null)
         return null;

      int numberOfVertices = polygonVertices.size();

      if (numberOfVertices < 3)
         return null;

      Point3D32[] vertices = new Point3D32[numberOfVertices];
      Vector3D32[] normals = new Vector3D32[numberOfVertices];
      Point2D32[] texturePoints = new Point2D32[numberOfVertices];

      normals[0] = new Vector3D32(Axis3D.Z);
      if (polygonPose != null)
         polygonPose.transform(normals[0]);

      for (int i = 1; i < numberOfVertices; i++)
         normals[i] = new Vector3D32(normals[0]);

      float minX = polygonVertices.get(0).getX32();
      float minY = polygonVertices.get(0).getY32();
      float maxX = polygonVertices.get(0).getX32();
      float maxY = polygonVertices.get(0).getY32();

      for (int i = 1; i < numberOfVertices; i++)
      {
         Point2DReadOnly vertex2D;
         if (counterClockwiseOrdered)
            vertex2D = polygonVertices.get(i);
         else
            vertex2D = polygonVertices.get(numberOfVertices - 1 - i);

         if (vertex2D.getX32() > maxX)
            maxX = vertex2D.getX32();
         else if (vertex2D.getX32() < minX)
            minX = vertex2D.getX32();

         if (vertex2D.getY32() > maxY)
            maxY = vertex2D.getY32();
         else if (vertex2D.getY32() < minY)
            minY = vertex2D.getY32();
      }

      for (int i = 0; i < numberOfVertices; i++)
      {
         Point2DReadOnly vertex2D = polygonVertices.get(i);
         Point3D32 vertex3D = new Point3D32();
         vertex3D.set(vertex2D);
         if (polygonPose != null)
            polygonPose.transform(vertex3D);
         vertices[i] = vertex3D;

         float textureX = 1.0f - (vertex2D.getY32() - minY) / (maxY - minY);
         float textureY = 1.0f - (vertex2D.getX32() - minX) / (maxX - minX);
         texturePoints[i] = new Point2D32(textureX, textureY);
      }

      if (!counterClockwiseOrdered)
      {
         reverse(vertices);
         reverse(texturePoints);
      }

      int numberOfTriangles = numberOfVertices - 2;
      // Do a naive way of splitting a polygon into triangles. Assumes convexity and ccw ordering.
      int[] triangleIndices = new int[3 * numberOfTriangles];
      int index = 0;
      for (int j = 2; j < vertices.length; j++)
      {
         triangleIndices[index++] = 0;
         triangleIndices[index++] = j - 1;
         triangleIndices[index++] = j;
      }

      return new MeshDataHolder(vertices, texturePoints, triangleIndices, normals);
   }

   /**
    * Create a triangle mesh for the given polygon.
    * <p>
    * <b> It is assumed that the polygon is convex and clockwise ordered. </b>
    * </p>
    *
    * @param cwOrderedPolygonVertices the clockwise-ordered vertices of the polygon.
    * @return the generic triangle mesh or {@code null} if {@code cwOrderedPolygonVertices} is
    *         {@code null}.
    */
   public static MeshDataHolder PolygonClockwise(List<? extends Point3DReadOnly> cwOrderedPolygonVertices)
   {
      return Polygon(cwOrderedPolygonVertices, false);
   }

   /**
    * Create a triangle mesh for the given polygon.
    * <p>
    * <b> It is assumed that the polygon is convex and counter-clockwise ordered. </b>
    * </p>
    *
    * @param ccwOrderedPolygonVertices the counter-clockwise-ordered vertices of the polygon.
    * @return the generic triangle mesh or {@code null} if {@code ccwOrderedPolygonVertices} is
    *         {@code null}.
    */
   public static MeshDataHolder PolygonCounterClockwise(List<? extends Point3DReadOnly> ccwOrderedPolygonVertices)
   {
      return Polygon(ccwOrderedPolygonVertices, true);
   }

   /**
    * Create a triangle mesh for the given polygon.
    * <p>
    * <b> It is assumed that the polygon is convex. </b>
    * </p>
    *
    * @param convexPolygonVertices   the vertices of the polygon.
    * @param counterClockwiseOrdered {@code true} to indicate that the vertices are counter-clockwise
    *                                ordered, {@code false} for clockwise ordered.
    * @return the generic triangle mesh or {@code null} if {@code convexPolygonVertices} is
    *         {@code null}.
    */
   public static MeshDataHolder Polygon(List<? extends Point3DReadOnly> convexPolygonVertices, boolean counterClockwiseOrdered)
   {
      if (convexPolygonVertices == null)
         return null;

      int numberOfVertices = convexPolygonVertices.size();

      if (numberOfVertices < 3)
         return null;

      int numberOfTriangles = numberOfVertices - 2;
      Vector3D32[] triangleNormals = new Vector3D32[numberOfTriangles];

      for (int i = 0; i < numberOfTriangles; i++)
      {
         Vector3D32 triangleNormal = new Vector3D32();
         EuclidGeometryTools.normal3DFromThreePoint3Ds(convexPolygonVertices.get(0),
                                                       convexPolygonVertices.get(i + 1),
                                                       convexPolygonVertices.get(i + 2),
                                                       triangleNormal);
         triangleNormals[i] = triangleNormal;
      }

      boolean areAllNormalsEqual = true;
      Vector3D32 polygonNormal = new Vector3D32();

      for (int i = 1; i < numberOfTriangles; i++)
      {
         if (areAllNormalsEqual && !triangleNormals[i - 1].epsilonEquals(triangleNormals[i], 1.0e-7))
         {
            areAllNormalsEqual = false;
         }

         polygonNormal.add(triangleNormals[i]);
      }

      polygonNormal.normalize();
      RotationMatrix polygonOrientation = new RotationMatrix();
      EuclidGeometryTools.orientation3DFromZUpToVector3D(polygonNormal, polygonOrientation);
      Point2D32[] texturePoints = new Point2D32[numberOfVertices];

      float minX = Float.POSITIVE_INFINITY;
      float minY = Float.POSITIVE_INFINITY;
      float maxX = Float.NEGATIVE_INFINITY;
      float maxY = Float.NEGATIVE_INFINITY;
      Point3D32 point = new Point3D32();

      for (int i = 0; i < numberOfVertices; i++)
      {
         polygonOrientation.inverseTransform(convexPolygonVertices.get(i), point);
         Point2D32 texturePoint = new Point2D32();
         texturePoint.set(-point.getY(), -point.getX());
         texturePoints[i] = texturePoint;
         minX = Math.min(minX, texturePoint.getX32());
         minY = Math.min(minY, texturePoint.getY32());
         maxX = Math.max(maxX, texturePoint.getX32());
         maxY = Math.max(maxY, texturePoint.getY32());
      }

      for (int i = 0; i < numberOfVertices; i++)
      {
         texturePoints[i].sub(minX, minY);
         texturePoints[i].scale(1.0 / (maxX - minX), 1.0 / (maxY - minY));
      }

      Point3D32[] vertices = convexPolygonVertices.stream().map(Point3D32::new).toArray(Point3D32[]::new);
      Vector3D32[] normals = new Vector3D32[numberOfVertices];
      int[] triangleIndices = new int[3 * numberOfTriangles];

      if (areAllNormalsEqual)
      {
         for (int i = 0; i < numberOfVertices; i++)
         {
            if (i < numberOfTriangles)
               normals[i] = triangleNormals[i];
            else
               normals[i] = new Vector3D32(triangleNormals[0]);
         }
      }
      else
      {
         normals[0] = polygonNormal;
         normals[1] = triangleNormals[0];

         for (int i = 1; i < numberOfVertices - 2; i++)
         {
            Vector3D32 normal = new Vector3D32();
            normal.interpolate(triangleNormals[i - 1], triangleNormals[i], 0.5);
            normal.normalize();
            normals[i] = normal;
         }
         Vector3D32 normal = new Vector3D32();
         normal.interpolate(triangleNormals[numberOfTriangles - 2], triangleNormals[numberOfTriangles - 1], 0.5);
         normal.normalize();
         normals[numberOfVertices - 2] = triangleNormals[numberOfTriangles - 1];
         normals[numberOfVertices - 1] = triangleNormals[numberOfTriangles - 1];
      }

      // Do a naive way of splitting a polygon into triangles. Assumes convexity and ccw ordering.
      int index = 0;
      for (int j = 2; j < numberOfVertices; j++)
      {
         triangleIndices[index++] = 0;
         triangleIndices[index++] = j - 1;
         triangleIndices[index++] = j;
      }

      return new MeshDataHolder(vertices, texturePoints, triangleIndices, normals);
   }

   /**
    * Create a triangle mesh for the given polygon 2d and extrude it along the z-axis.
    *
    * @param convexPolygon   the polygon to create a mesh from.
    * @param extrusionHeight thickness of the extrusion. If {@code extrusionHeight < 0}, the polygon is
    *                        extruded toward z negative.
    * @return the generic triangle mesh or {@code null} if {@code convexPolygon} is {@code null}.
    */
   public static MeshDataHolder ExtrudedPolygon(ConvexPolygon2DReadOnly convexPolygon, double extrusionHeight)
   {
      if (convexPolygon == null)
         return null;
      return ExtrudedPolygon(convexPolygon.getPolygonVerticesView(), false, extrusionHeight, 0.0);
   }

   /**
    * Create a triangle mesh for the given polygon 2d and extrude it along the z-axis.
    * <p>
    * <b> It is assumed that the polygon is convex and counter-clockwise ordered. </b>
    * </p>
    *
    * @param ccwOrderedPolygonVertices the counter-clockwise-ordered vertices of the polygon.
    * @param extrusionHeight           thickness of the extrusion. The polygon is extruded upward along
    *                                  the z-axis.
    * @return the generic triangle mesh or {@code null} if {@code ccwOrderedPolygonVertices} is
    *         {@code null}.
    */
   public static MeshDataHolder ExtrudedPolygon(List<? extends Point2DReadOnly> ccwOrderedPolygonVertices, double extrusionHeight)
   {
      return ExtrudedPolygonCounterClockwise(ccwOrderedPolygonVertices, extrusionHeight, 0.0);
   }

   /**
    * Create a triangle mesh for the given polygon 2d and extrude it along the z-axis.
    * <p>
    * <b> It is assumed that the polygon is convex and counter-clockwise ordered. </b>
    * </p>
    *
    * @param ccwOrderedPolygonVertices the counter-clockwise-ordered vertices of the polygon.
    * @param zTop                      thickness of the extrusion. If {@code extrusionHeight < 0}, the
    *                                  polygon is extruded toward z negative.
    * @param zBottom                   offset along the z-axis that is applied on all the vertices of
    *                                  the resulting mesh.
    * @return the generic triangle mesh or {@code null} if {@code ccwOrderedPolygonVertices} is
    *         {@code null}.
    */
   public static MeshDataHolder ExtrudedPolygonCounterClockwise(List<? extends Point2DReadOnly> ccwOrderedPolygonVertices, double zTop, double zBottom)
   {
      return ExtrudedPolygon(ccwOrderedPolygonVertices, true, zTop, zBottom);
   }

   /**
    * Create a triangle mesh for the given polygon and extrude it along the z-axis.
    * <p>
    * <b> It is assumed that the polygon is convex and counter-clockwise ordered. </b>
    * </p>
    *
    * @param polygonVertices         the counter-clockwise-ordered vertices of the polygon.
    * @param counterClockwiseOrdered {@code true} to indicate that the vertices are counter-clockwise
    *                                ordered, {@code false} for clockwise ordered.
    * @param zTop                    z-coordinate of the bottom face.
    * @param zBottom                 z-coordinate of the top face.
    * @return the generic triangle mesh or {@code null} if {@code polygonVertices} is {@code null}.
    */
   public static MeshDataHolder ExtrudedPolygon(List<? extends Point2DReadOnly> polygonVertices, boolean counterClockwiseOrdered, double zTop, double zBottom)
   {
      if (polygonVertices == null || polygonVertices.size() < 3)
         return null;

      int numberOfVertices = polygonVertices.size();

      Point3D32 vertices[] = new Point3D32[6 * numberOfVertices + 4];
      Vector3D32 normals[] = new Vector3D32[6 * numberOfVertices + 4];
      Point2D32[] texturePoints = new Point2D32[6 * numberOfVertices + 4];

      float minX = polygonVertices.get(0).getX32();
      float minY = polygonVertices.get(0).getY32();
      float maxX = polygonVertices.get(0).getX32();
      float maxY = polygonVertices.get(0).getY32();

      for (int i = 1; i < numberOfVertices; i++)
      {
         Point2DReadOnly vertex = polygonVertices.get(i);

         if (vertex.getX32() > maxX)
            maxX = vertex.getX32();
         else if (vertex.getX32() < minX)
            minX = vertex.getX32();

         if (vertex.getY32() > maxY)
            maxY = vertex.getY32();
         else if (vertex.getY32() < minY)
            minY = vertex.getY32();
      }

      for (int i = 0; i < numberOfVertices; i++)
      {
         Point2DReadOnly vertex;

         if (counterClockwiseOrdered)
            vertex = polygonVertices.get(i);
         else
            vertex = polygonVertices.get(numberOfVertices - 1 - i);

         float vertexX = vertex.getX32();
         float vertexY = vertex.getY32();

         // Vertices for bottom face
         vertices[i] = new Point3D32(vertexX, vertexY, (float) zBottom);
         normals[i] = new Vector3D32(0.0f, 0.0f, -1.0f);
         texturePoints[i] = new Point2D32(0.5f - 0.5f * (vertexY - minY) / (maxY - minY) + 0.5f, 0.5f - 0.5f * (vertexX - minX) / (maxX - minX));

         // Vertices for top face
         vertices[i + numberOfVertices] = new Point3D32(vertexX, vertexY, (float) (zTop));
         normals[i + numberOfVertices] = new Vector3D32(0.0f, 0.0f, 1.0f);
         texturePoints[i + numberOfVertices] = new Point2D32(0.5f - 0.5f * (vertexY - minY) / (maxY - minY), 0.5f - 0.5f * (vertexX - minX) / (maxX - minX));
      }

      double perimeter = 0.0;

      for (int i = 0; i < polygonVertices.size(); i++)
      {
         Point2DReadOnly vertex = polygonVertices.get(i);
         Point2DReadOnly nextVertex = polygonVertices.get((i + 1) % numberOfVertices);
         perimeter += vertex.distance(nextVertex);
      }

      double distanceAlongPerimeter = 0.0;
      double nextDistanceAlongPerimeter = 0.0;
      int nextIndex = counterClockwiseOrdered ? 0 : numberOfVertices - 1;
      Point2DReadOnly vertex;
      Point2DReadOnly nextVertex = polygonVertices.get(nextIndex);

      for (int i = 0; i < numberOfVertices + 1; i++)
      {
         vertex = nextVertex;

         if (counterClockwiseOrdered)
         {
            nextIndex++;
            nextIndex %= numberOfVertices;
         }
         else
         {
            nextIndex--;
            if (nextIndex < 0)
               nextIndex = numberOfVertices - 1;
         }
         nextVertex = polygonVertices.get(nextIndex);
         nextDistanceAlongPerimeter += nextVertex.distance(vertex);

         float vertexX = vertex.getX32();
         float vertexY = vertex.getY32();

         Point3D32 vertexBottom = new Point3D32(vertexX, vertexY, (float) zBottom);
         Point3D32 vertexTop = new Point3D32(vertexX, vertexY, (float) (zTop));

         Point3D32 nextVertexBottom = new Point3D32(nextVertex.getX32(), nextVertex.getY32(), (float) zBottom);
         Point3D32 nextVertexTop = new Point3D32(nextVertex.getX32(), nextVertex.getY32(), (float) (zTop));

         Vector3D normal = new Vector3D();
         normal.sub(nextVertexTop, vertexTop);
         normal.cross(Axis3D.Z, normal);
         normal.negate();
         normal.normalize();

         int vertexBottomIndex = 2 * i + 2 * numberOfVertices;
         int nextVertexBottomIndex = vertexBottomIndex + 1;
         int vertexTopIndex = vertexBottomIndex + 2 * (numberOfVertices + 1);
         int nextVertexTopIndex = vertexTopIndex + 1;

         // Vertices for side faces
         // Bottom
         vertices[vertexBottomIndex] = vertexBottom;
         normals[vertexBottomIndex] = new Vector3D32(normal);
         texturePoints[vertexBottomIndex] = new Point2D32((float) (distanceAlongPerimeter / perimeter), 1.0f);

         vertices[nextVertexBottomIndex] = nextVertexBottom;
         normals[nextVertexBottomIndex] = new Vector3D32(normal);
         texturePoints[nextVertexBottomIndex] = new Point2D32((float) (nextDistanceAlongPerimeter / perimeter), 1.0f);

         // Top
         vertices[vertexTopIndex] = vertexTop;
         normals[vertexTopIndex] = new Vector3D32(normal);
         texturePoints[vertexTopIndex] = new Point2D32((float) (distanceAlongPerimeter / perimeter), 0.5f);

         vertices[nextVertexTopIndex] = nextVertexTop;
         normals[nextVertexTopIndex] = new Vector3D32(normal);
         texturePoints[nextVertexTopIndex] = new Point2D32((float) (nextDistanceAlongPerimeter / perimeter), 0.5f);

         distanceAlongPerimeter = nextDistanceAlongPerimeter;
      }

      int numberOfTriangles = 4 * numberOfVertices;
      int[] triangleIndices = new int[3 * numberOfTriangles];

      int index = 0;

      for (int i = 1; i < numberOfVertices; i++)
      {
         // Bottom face
         triangleIndices[index++] = (i + 1) % numberOfVertices;
         triangleIndices[index++] = i;
         triangleIndices[index++] = 0;

         // Top face
         triangleIndices[index++] = numberOfVertices;
         triangleIndices[index++] = i + numberOfVertices;
         triangleIndices[index++] = (i + 1) % numberOfVertices + numberOfVertices;
      }

      // Side faces
      for (int i = 0; i < numberOfVertices + 1; i++)
      {
         // Lower triangle
         triangleIndices[index++] = 2 * i + 2 * numberOfVertices;
         triangleIndices[index++] = (2 * i + 1) % (2 * numberOfVertices + 2) + 2 * numberOfVertices;
         triangleIndices[index++] = 2 * i + 4 * numberOfVertices + 2;
         // Upper triangle
         triangleIndices[index++] = (2 * i + 1) % (2 * numberOfVertices + 2) + 2 * numberOfVertices;
         triangleIndices[index++] = (2 * i + 1) % (2 * numberOfVertices + 2) + 4 * numberOfVertices + 2;
         triangleIndices[index++] = 2 * i + 4 * numberOfVertices + 2;
      }

      return new MeshDataHolder(vertices, texturePoints, triangleIndices, normals);
   }

   /**
    * Creates a triangle mesh for a 3D hemi-ellipsoid, i.e. top half of an ellipsoid with the bottom
    * closed by a flat cap.
    *
    * @param xRadius             radius of the ellipsoid along the x-axis.
    * @param yRadius             radius of the ellipsoid along the y-axis.
    * @param zRadius             radius of the ellipsoid along the z-axis.
    * @param latitudeResolution  the resolution along the vertical axis, i.e. z-axis.
    * @param longitudeResolution the resolution around the vertical axis, i.e. the number of vertices
    *                            per latitude.
    * @return the generic triangle mesh.
    */
   public static MeshDataHolder HemiEllipsoid(double xRadius, double yRadius, double zRadius, int latitudeResolution, int longitudeResolution)
   {
      return HemiEllipsoid((float) xRadius, (float) yRadius, (float) zRadius, latitudeResolution, longitudeResolution);
   }

   /**
    * Creates a triangle mesh for a 3D hemi-ellipsoid, i.e. top half of an ellipsoid with the bottom
    * closed by a flat cap.
    *
    * @param xRadius             radius of the ellipsoid along the x-axis.
    * @param yRadius             radius of the ellipsoid along the y-axis.
    * @param zRadius             radius of the ellipsoid along the z-axis.
    * @param latitudeResolution  the resolution along the vertical axis, i.e. z-axis.
    * @param longitudeResolution the resolution around the vertical axis, i.e. the number of vertices
    *                            per latitude.
    * @return the generic triangle mesh.
    */
   public static MeshDataHolder HemiEllipsoid(float xRadius, float yRadius, float zRadius, int latitudeResolution, int longitudeResolution)
   {
      if (longitudeResolution % 2 == 1)
         longitudeResolution += 1;
      int nPointsLongitude = longitudeResolution + 1;
      int nPointsLatitude = latitudeResolution + 1;

      // Reminder of longitude and latitude: http://www.geographyalltheway.com/ks3_geography/maps_atlases/longitude_latitude.htm
      Point3D32 points[] = new Point3D32[(nPointsLatitude + 1) * nPointsLongitude];
      Vector3D32[] normals = new Vector3D32[(nPointsLatitude + 1) * nPointsLongitude];
      Point2D32 textPoints[] = new Point2D32[(nPointsLatitude + 1) * nPointsLongitude];

      for (int longitudeIndex = 0; longitudeIndex < nPointsLongitude; longitudeIndex++)
      {
         float longitudeAngle = TwoPi * ((float) longitudeIndex / (float) (nPointsLongitude - 1));
         float cosLongitude = (float) Math.cos(longitudeAngle);
         float sinLongitude = (float) Math.sin(longitudeAngle);
         float textureX = (float) longitudeIndex / (float) (nPointsLongitude - 1);

         for (int latitudeIndex = 1; latitudeIndex < nPointsLatitude - 1; latitudeIndex++)
         {
            float latitudeAngle = HalfPi * ((float) (latitudeIndex - 1) / (float) (nPointsLatitude - 2));
            float cosLatitude = (float) Math.cos(latitudeAngle);
            float sinLatitude = (float) Math.sin(latitudeAngle);

            int currentIndex = (latitudeIndex + 1) * nPointsLongitude + longitudeIndex;
            float normalX = cosLongitude * cosLatitude;
            float normalY = sinLongitude * cosLatitude;
            float normalZ = sinLatitude;
            float vertexX = xRadius * normalX;
            float vertexY = yRadius * normalY;
            float vertexZ = zRadius * normalZ;
            points[currentIndex] = new Point3D32(vertexX, vertexY, vertexZ);
            normals[currentIndex] = new Vector3D32(normalX, normalY, normalZ);

            float textureY = 0.5f * (1.0f - sinLatitude);
            textPoints[currentIndex] = new Point2D32(textureX, textureY);
         }

         // Bottom side
         int currentIndex = nPointsLongitude + longitudeIndex;
         float vertexX = (float) (xRadius * Math.cos(longitudeAngle));
         float vertexY = (float) (yRadius * Math.sin(longitudeAngle));
         float vertexZ = 0.0f;
         points[currentIndex] = new Point3D32(vertexX, vertexY, vertexZ);
         normals[currentIndex] = new Vector3D32(0.0f, 0.0f, -1.0f);
         textPoints[currentIndex] = new Point2D32(textureX, 0.5f);

         textureX += 0.5f / (nPointsLongitude - 1);

         // Bottom center
         int southPoleIndex = longitudeIndex;
         points[southPoleIndex] = new Point3D32(0.0f, 0.0f, 0.0f);
         normals[southPoleIndex] = new Vector3D32(0.0f, 0.0f, -1.0f);
         textPoints[southPoleIndex] = new Point2D32(textureX, 1.0f - 1.0f / 256.0f);

         // North pole
         int northPoleIndex = nPointsLatitude * nPointsLongitude + longitudeIndex;
         points[northPoleIndex] = new Point3D32(0.0f, 0.0f, zRadius);
         normals[northPoleIndex] = new Vector3D32(0.0f, 0.0f, 1.0f);
         textPoints[northPoleIndex] = new Point2D32(textureX, 1.0f / 256.0f);
      }

      int numberOfTriangles = 2 * (nPointsLatitude - 1) * (nPointsLongitude - 1);
      int[] triangleIndices = new int[3 * numberOfTriangles];

      int index = 0;

      // Mid-latitude faces
      for (int latitudeIndex = 1; latitudeIndex < nPointsLatitude - 1; latitudeIndex++)
      {
         for (int longitudeIndex = 0; longitudeIndex < nPointsLongitude - 1; longitudeIndex++)
         {
            int nextLongitudeIndex = (longitudeIndex + 1) % nPointsLongitude;
            int nextLatitudeIndex = latitudeIndex + 1;

            // Lower triangles
            triangleIndices[index++] = latitudeIndex * nPointsLongitude + longitudeIndex;
            triangleIndices[index++] = latitudeIndex * nPointsLongitude + nextLongitudeIndex;
            triangleIndices[index++] = nextLatitudeIndex * nPointsLongitude + longitudeIndex;
            // Upper triangles
            triangleIndices[index++] = latitudeIndex * nPointsLongitude + nextLongitudeIndex;
            triangleIndices[index++] = nextLatitudeIndex * nPointsLongitude + nextLongitudeIndex;
            triangleIndices[index++] = nextLatitudeIndex * nPointsLongitude + longitudeIndex;
         }
      }

      // Bottom face
      for (int longitudeIndex = 0; longitudeIndex < nPointsLongitude - 1; longitudeIndex++)
      {
         int nextLongitudeIndex = (longitudeIndex + 1) % nPointsLongitude;
         triangleIndices[index++] = longitudeIndex;
         triangleIndices[index++] = nPointsLongitude + nextLongitudeIndex;
         triangleIndices[index++] = nPointsLongitude + longitudeIndex;
      }

      // North pole faces
      for (int longitudeIndex = 0; longitudeIndex < nPointsLongitude - 1; longitudeIndex++)
      {
         int nextLongitudeIndex = (longitudeIndex + 1) % nPointsLongitude;
         triangleIndices[index++] = (nPointsLatitude - 0) * nPointsLongitude + longitudeIndex;
         triangleIndices[index++] = (nPointsLatitude - 1) * nPointsLongitude + longitudeIndex;
         triangleIndices[index++] = (nPointsLatitude - 1) * nPointsLongitude + nextLongitudeIndex;
      }

      return new MeshDataHolder(points, textPoints, triangleIndices, normals);
   }

   /**
    * Creates a triangle mesh for a 3D cylinder.
    * <p>
    * The cylinder's axis is aligned with the z-axis. When {@code centered} is true, the cylinder is
    * centered at the origin, when false its bottom face is centered at the origin.
    * </p>
    *
    * @param radius     the cylinder's radius.
    * @param height     the cylinder's height or length.
    * @param resolution the resolution for the cylindrical part.
    * @param centered   {@code true} to center the cylinder are the origin, {@code false} to center its
    *                   bottom face at the origin.
    * @return the generic triangle mesh.
    */
   public static MeshDataHolder Cylinder(double radius, double height, int resolution, boolean centered)
   {
      return Cylinder((float) radius, (float) height, resolution, centered);
   }

   /**
    * Creates a triangle mesh for a 3D cylinder.
    * <p>
    * The cylinder's axis is aligned with the z-axis. When {@code centered} is true, the cylinder is
    * centered at the origin, when false its bottom face is centered at the origin.
    * </p>
    * 
    * @param radius     the cylinder's radius.
    * @param height     the cylinder's height or length.
    * @param resolution the resolution for the cylindrical part.
    * @param centered   {@code true} to center the cylinder are the origin, {@code false} to center its
    *                   bottom face at the origin.
    * @return the generic triangle mesh.
    */
   public static MeshDataHolder Cylinder(float radius, float height, int resolution, boolean centered)
   {
      Point3D32 points[] = new Point3D32[4 * resolution + 2];
      Vector3D32 normals[] = new Vector3D32[4 * resolution + 2];
      Point2D32 texturePoints[] = new Point2D32[4 * resolution + 2];

      float zTop = centered ? 0.5f * height : height;
      float zBottom = centered ? -0.5f * height : 0.0f;

      for (int i = 0; i < resolution; i++)
      {
         double angle = i * TwoPi / (resolution - 1.0);
         float cosAngle = (float) Math.cos(angle);
         float sinAngle = (float) Math.sin(angle);

         float vertexX = radius * cosAngle;
         float vertexY = radius * sinAngle;

         // Bottom vertices
         points[i] = new Point3D32(vertexX, vertexY, zBottom);
         normals[i] = new Vector3D32(0.0f, 0.0f, -1.0f);
         texturePoints[i] = new Point2D32(0.25f * (1f + sinAngle) + 0.5f, 0.25f * (1f - cosAngle));

         // Top vertices
         points[i + resolution] = new Point3D32(vertexX, vertexY, zTop);
         normals[i + resolution] = new Vector3D32(0.0f, 0.0f, 1.0f);
         texturePoints[i + resolution] = new Point2D32(0.25f * (1f - sinAngle), 0.25f * (1f - cosAngle));

         // Outer vertices
         // Bottom
         points[i + 2 * resolution] = new Point3D32(vertexX, vertexY, zBottom);
         normals[i + 2 * resolution] = new Vector3D32(cosAngle, sinAngle, 0.0f);
         texturePoints[i + 2 * resolution] = new Point2D32(i / (resolution - 1.0f), 1.0f);

         // Top
         points[i + 3 * resolution] = new Point3D32(vertexX, vertexY, zTop);
         normals[i + 3 * resolution] = new Vector3D32(cosAngle, sinAngle, 0.0f);
         texturePoints[i + 3 * resolution] = new Point2D32(i / (resolution - 1.0f), 0.5f);
      }

      // Center of bottom cap
      points[4 * resolution] = new Point3D32(0.0f, 0.0f, zBottom);
      normals[4 * resolution] = new Vector3D32(0.0f, 0.0f, -1.0f);
      texturePoints[4 * resolution] = new Point2D32(0.75f, 0.25f);
      // Center of top cap
      points[4 * resolution + 1] = new Point3D32(0.0f, 0.0f, zTop);
      normals[4 * resolution + 1] = new Vector3D32(0.0f, 0.0f, 1.0f);
      texturePoints[4 * resolution + 1] = new Point2D32(0.25f, 0.25f);

      int numberOfTriangles = 4 * resolution;
      int[] triangleIndices = new int[6 * numberOfTriangles];

      int index = 0;

      for (int i = 0; i < resolution; i++)
      { // The bottom cap
         triangleIndices[index++] = (i + 1) % resolution;
         triangleIndices[index++] = i;
         triangleIndices[index++] = 4 * resolution;
      }

      for (int i = 0; i < resolution; i++)
      { // The top cap
         triangleIndices[index++] = 4 * resolution + 1;
         triangleIndices[index++] = i + resolution;
         triangleIndices[index++] = (i + 1) % resolution + resolution;
      }

      for (int i = 0; i < resolution; i++)
      { // The cylinder part
        // Lower triangle
         triangleIndices[index++] = i + 2 * resolution;
         triangleIndices[index++] = (i + 1) % resolution + 2 * resolution;
         triangleIndices[index++] = i + 3 * resolution;
         // Upper triangle
         triangleIndices[index++] = (i + 1) % resolution + 2 * resolution;
         triangleIndices[index++] = (i + 1) % resolution + 3 * resolution;
         triangleIndices[index++] = i + 3 * resolution;
      }

      return new MeshDataHolder(points, texturePoints, triangleIndices, normals);
   }

   /**
    * Creates a triangle mesh for a 3D cone.
    * <p>
    * The cone's axis is aligned with the z-axis and is positioned such that the center of its bottom
    * face is at the origin.
    * </p>
    * 
    * @param height     the height of the cone.
    * @param radius     the radius of the base.
    * @param resolution the resolution for the cylindrical part of the cone.
    * @return the generic triangle mesh.
    */
   public static MeshDataHolder Cone(double height, double radius, int resolution)
   {
      return Cone((float) height, (float) radius, resolution);
   }

   /**
    * Creates a triangle mesh for a 3D cone.
    * <p>
    * The cone's axis is aligned with the z-axis and is positioned such that the center of its bottom
    * face is at the origin.
    * </p>
    * 
    * @param height     the height of the cone.
    * @param radius     the radius of the base.
    * @param resolution the resolution for the cylindrical part of the cone.
    * @return the generic triangle mesh.
    */
   public static MeshDataHolder Cone(float height, float radius, int resolution)
   {
      Point3D32[] vertices = new Point3D32[3 * resolution + 1];
      Vector3D32[] normals = new Vector3D32[3 * resolution + 1];
      Point2D32[] texturePoints = new Point2D32[3 * resolution + 1];

      // This is equal to half of the opening angle of the cone at its top. Used to compute the normals.
      float slopeAngle = (float) Math.atan2(radius, height);
      float cosSlopeAngle = (float) Math.cos(slopeAngle);
      float sinSlopeAngle = (float) Math.sin(slopeAngle);

      for (int i = 0; i < resolution; i++)
      {
         double angle = i * TwoPi / (resolution - 1);
         float cosAngle = (float) Math.cos(angle);
         float sinAngle = (float) Math.sin(angle);

         float vertexX = radius * cosAngle;
         float vertexY = radius * sinAngle;

         // Vertices for the bottom part.
         vertices[i] = new Point3D32(vertexX, vertexY, 0.0f);
         normals[i] = new Vector3D32(0.0f, 0.0f, -1.0f);
         texturePoints[i] = new Point2D32(0.25f * (1f + sinAngle), 0.25f * (1f - cosAngle));

         // Vertices for the side part.
         vertices[i + resolution] = new Point3D32(vertexX, vertexY, 0.0f);
         normals[i + resolution] = new Vector3D32(cosSlopeAngle * cosAngle, cosSlopeAngle * sinAngle, sinSlopeAngle);
         texturePoints[i + resolution] = new Point2D32(i / (resolution - 1.0f), 1.0f);
         vertices[i + 2 * resolution] = new Point3D32(0.0f, 0.0f, height);
         normals[i + 2 * resolution] = new Vector3D32(cosSlopeAngle * cosAngle, cosSlopeAngle * sinAngle, sinSlopeAngle);
         texturePoints[i + 2 * resolution] = new Point2D32(i / (resolution - 1.0f), 0.5f);
      }

      // The center of the bottom
      int bottomCenterIndex = 3 * resolution;
      vertices[bottomCenterIndex] = new Point3D32(0.0f, 0.0f, 0.0f);
      normals[bottomCenterIndex] = new Vector3D32(0.0f, 0.0f, -1.0f);
      texturePoints[bottomCenterIndex] = new Point2D32(0.25f, 0.25f);

      int numberOfTriangles = 2 * resolution;
      int[] triangleIndices = new int[3 * numberOfTriangles];

      int index = 0;

      for (int i = 0; i < resolution; i++)
      {
         // The bottom face
         triangleIndices[index++] = bottomCenterIndex;
         triangleIndices[index++] = (i + 1) % resolution;
         triangleIndices[index++] = i;

         // The side faces
         triangleIndices[index++] = i + resolution;
         triangleIndices[index++] = (i + 1) % resolution + resolution;
         triangleIndices[index++] = i + 2 * resolution;
      }

      return new MeshDataHolder(vertices, texturePoints, triangleIndices, normals);
   }

   /**
    * Creates a triangle mesh for a 3D truncated cone which base and top are ellipses.
    * <p>
    * The cone's axis is aligned with the z-axis and is positioned such that the center of its bottom
    * face is at the origin.
    * </p>
    * 
    * @param height      the cone's height.
    * @param xBaseRadius radius around the x-axis of the base ellipse.
    * @param yBaseRadius radius around the y-axis of the base ellipse.
    * @param xTopRadius  radius around the x-axis of the top ellipse.
    * @param yTopRadius  radius around the x-axis of the top ellipse.
    * @param resolution  the resolution for the cylindrical part of the cone.
    * @return the generic triangle mesh.
    */
   public static MeshDataHolder GenTruncatedCone(double height, double xBaseRadius, double yBaseRadius, double xTopRadius, double yTopRadius, int resolution)
   {
      return GenTruncatedCone((float) height, (float) xBaseRadius, (float) yBaseRadius, (float) xTopRadius, (float) yTopRadius, resolution);
   }

   /**
    * Creates a triangle mesh for a 3D truncated cone which base and top are ellipses.
    * <p>
    * The cone's axis is aligned with the z-axis and is positioned such that the center of its bottom
    * face is at the origin.
    * </p>
    * 
    * @param height      the cone's height.
    * @param xBaseRadius radius around the x-axis of the base ellipse.
    * @param yBaseRadius radius around the y-axis of the base ellipse.
    * @param xTopRadius  radius around the x-axis of the top ellipse.
    * @param yTopRadius  radius around the x-axis of the top ellipse.
    * @param resolution  the resolution for the cylindrical part of the cone.
    * @return the generic triangle mesh.
    */
   public static MeshDataHolder GenTruncatedCone(float height, float xBaseRadius, float yBaseRadius, float xTopRadius, float yTopRadius, int resolution)
   {
      Point3D32 points[] = new Point3D32[4 * resolution + 2];
      Vector3D32[] normals = new Vector3D32[4 * resolution + 2];
      Point2D32[] textPoints = new Point2D32[4 * resolution + 2];

      for (int i = 0; i < resolution; i++)
      {
         double angle = i * TwoPi / (resolution - 1);
         float cosAngle = (float) Math.cos(angle);
         float sinAngle = (float) Math.sin(angle);

         float baseX = xBaseRadius * cosAngle;
         float baseY = yBaseRadius * sinAngle;
         float topX = xTopRadius * cosAngle;
         float topY = yTopRadius * sinAngle;

         // Bottom face vertices
         points[i] = new Point3D32(baseX, baseY, 0.0f);
         normals[i] = new Vector3D32(0.0f, 0.0f, -1.0f);
         textPoints[i] = new Point2D32(0.25f * (1f + sinAngle) + 0.5f, 0.25f * (1f - cosAngle));

         // Top face vertices
         points[i + resolution] = new Point3D32(topX, topY, height);
         normals[i + resolution] = new Vector3D32(0.0f, 0.0f, 1.0f);
         textPoints[i + resolution] = new Point2D32(0.25f * (1f - sinAngle), 0.25f * (1f - cosAngle));

         // Cone face
         float currentBaseRadius = (float) Math.sqrt(baseX * baseX + baseY * baseY);
         float currentTopRadius = (float) Math.sqrt(topX * topX + topY * topY);
         float openingAngle = (float) Math.atan((currentBaseRadius - currentTopRadius) / height);
         float baseAngle = (float) Math.atan2(baseY, baseX);
         float topAngle = (float) Math.atan2(topY, topX);
         points[i + 2 * resolution] = new Point3D32(baseX, baseY, 0.0f);
         normals[i + 2 * resolution] = new Vector3D32((float) (Math.cos(baseAngle) * Math.cos(openingAngle)),
                                                      (float) (Math.sin(baseAngle) * Math.cos(openingAngle)),
                                                      (float) Math.sin(openingAngle));
         textPoints[i + 2 * resolution] = new Point2D32(i / (resolution - 1.0f), 1.0f);
         points[i + 3 * resolution] = new Point3D32(topX, topY, height);
         normals[i + 3 * resolution] = new Vector3D32((float) (Math.cos(topAngle) * Math.cos(openingAngle)),
                                                      (float) (Math.sin(topAngle) * Math.cos(openingAngle)),
                                                      (float) Math.sin(openingAngle));
         textPoints[i + 3 * resolution] = new Point2D32(i / (resolution - 1.0f), 0.5f);
      }

      // Bottom center
      points[4 * resolution] = new Point3D32(0.0f, 0.0f, 0.0f);
      normals[4 * resolution] = new Vector3D32(0.0f, 0.0f, -1.0f);
      textPoints[4 * resolution] = new Point2D32(0.75f, 0.25f);
      // Top center
      points[4 * resolution + 1] = new Point3D32(0.0f, 0.0f, height);
      normals[4 * resolution + 1] = new Vector3D32(0.0f, 0.0f, 1.0f);
      textPoints[4 * resolution + 1] = new Point2D32(0.25f, 0.25f);

      int numberOfTriangles = 4 * resolution;
      int[] triangleIndices = new int[3 * numberOfTriangles];
      int index = 0;

      for (int i = 0; i < resolution; i++)
      {
         // Bottom face
         triangleIndices[index++] = 4 * resolution;
         triangleIndices[index++] = (i + 1) % resolution;
         triangleIndices[index++] = i;
         // Top face
         triangleIndices[index++] = 4 * resolution + 1;
         triangleIndices[index++] = i + resolution;
         triangleIndices[index++] = (i + 1) % resolution + resolution;
         //Cone face: lower triangle
         triangleIndices[index++] = i + 2 * resolution;
         triangleIndices[index++] = (i + 1) % resolution + 2 * resolution;
         triangleIndices[index++] = i + 3 * resolution;
         //Cone face: upper triangle
         triangleIndices[index++] = (i + 1) % resolution + 2 * resolution;
         triangleIndices[index++] = (i + 1) % resolution + 3 * resolution;
         triangleIndices[index++] = i + 3 * resolution;
      }

      return new MeshDataHolder(points, textPoints, triangleIndices, normals);
   }

   /**
    * Creates a triangle mesh for a 3D torus.
    * <p>
    * The torus' axis is aligned with the z-axis and its centroid at the origin.
    * </p>
    * 
    * @param majorRadius the radius from the torus centroid to the tube center.
    * @param minorRadius the radius of the tube.
    * @param resolution  used to define the longitudinal and radial resolutions.
    * @return the generic triangle mesh.
    */
   public static MeshDataHolder Torus(double majorRadius, double minorRadius, int resolution)
   {
      return Torus((float) majorRadius, (float) minorRadius, resolution);
   }

   /**
    * Creates a triangle mesh for a 3D torus.
    * <p>
    * The torus' axis is aligned with the z-axis and its centroid at the origin.
    * </p>
    * 
    * @param majorRadius the radius from the torus centroid to the tube center.
    * @param minorRadius the radius of the tube.
    * @param resolution  used to define the longitudinal and radial resolutions.
    * @return the generic triangle mesh.
    */
   public static MeshDataHolder Torus(float majorRadius, float minorRadius, int resolution)
   {
      return ArcTorus(0.0f, (float) (2.0 * Math.PI), majorRadius, minorRadius, resolution);
   }

   /**
    * Creates a triangle mesh for an open partial 3D torus.
    * <p>
    * The torus' axis is aligned with the z-axis and its centroid at the origin.
    * </p>
    * 
    * @param startAngle  the angle at which the torus starts. The angle is in radians, it is expressed
    *                    with respect to the x-axis, and a positive angle corresponds to a
    *                    counter-clockwise rotation.
    * @param endAngle    the angle at which the torus ends. If {@code startAngle == endAngle} the torus
    *                    will be closed. The angle is in radians, it is expressed with respect to the
    *                    x-axis, and a positive angle corresponds to a counter-clockwise rotation.
    * @param majorRadius the radius from the torus centroid to the tube center.
    * @param minorRadius the radius of the tube.
    * @param resolution  used to define the longitudinal and radial resolutions.
    * @return the generic triangle mesh.
    */
   public static MeshDataHolder ArcTorus(double startAngle, double endAngle, double majorRadius, double minorRadius, int resolution)
   {
      return ArcTorus((float) startAngle, (float) endAngle, (float) majorRadius, (float) minorRadius, resolution);
   }

   /**
    * Creates a triangle mesh for an open partial 3D torus.
    * <p>
    * The torus' axis is aligned with the z-axis and its centroid at the origin.
    * </p>
    * 
    * @param startAngle  the angle at which the torus starts. The angle is in radians, it is expressed
    *                    with respect to the x-axis, and a positive angle corresponds to a
    *                    counter-clockwise rotation.
    * @param endAngle    the angle at which the torus ends. If {@code startAngle == endAngle} the torus
    *                    will be closed. The angle is in radians, it is expressed with respect to the
    *                    x-axis, and a positive angle corresponds to a counter-clockwise rotation.
    * @param majorRadius the radius from the torus centroid to the tube center.
    * @param minorRadius the radius of the tube.
    * @param resolution  used to define the longitudinal and radial resolutions.
    * @return the generic triangle mesh.
    */
   public static MeshDataHolder ArcTorus(float startAngle, float endAngle, float majorRadius, float minorRadius, int resolution)
   {
      float torusSpanAngle = endAngle - startAngle;
      boolean isClosed = MathTools.epsilonEquals(torusSpanAngle, TwoPi, 1.0e-3);

      // Make things a bit clearer.
      int majorN = resolution;
      // Make things a bit clearer.
      int minorN = resolution;

      float stepAngle = (endAngle - startAngle) / (majorN - 1);

      int numberOfVertices = isClosed ? majorN * minorN : majorN * minorN + 2 * (resolution + 1);
      Point3D32 points[] = new Point3D32[numberOfVertices];
      Vector3D32[] normals = new Vector3D32[numberOfVertices];
      Point2D32[] texturePoints = new Point2D32[numberOfVertices];

      float centerX, centerY;
      float pX, pY, pZ;
      float textureY, textureX;

      // Core part of the torus
      for (int majorIndex = 0; majorIndex < majorN; majorIndex++)
      {
         float majorAngle = startAngle + majorIndex * stepAngle;
         float cosMajorAngle = (float) Math.cos(majorAngle);
         float sinMajorAngle = (float) Math.sin(majorAngle);
         centerX = majorRadius * cosMajorAngle;
         centerY = majorRadius * sinMajorAngle;

         textureY = (float) majorIndex / (float) (majorN - 1);

         for (int minorIndex = 0; minorIndex < minorN; minorIndex++)
         {
            int currentIndex = majorIndex * minorN + minorIndex;
            float minorAngle = minorIndex * 2.0f * (float) Math.PI / (minorN - 1.0f);
            float cosMinorAngle = (float) Math.cos(minorAngle);
            float sinMinorAngle = (float) Math.sin(minorAngle);
            pX = centerX + minorRadius * cosMajorAngle * cosMinorAngle;
            pY = centerY + minorRadius * sinMajorAngle * cosMinorAngle;
            pZ = minorRadius * sinMinorAngle;
            points[currentIndex] = new Point3D32(pX, pY, pZ);
            normals[currentIndex] = new Vector3D32(cosMajorAngle * cosMinorAngle, sinMajorAngle * cosMinorAngle, sinMinorAngle);
            textureX = (float) minorIndex / (float) (minorN - 1);
            if (!isClosed)
               textureX *= 0.5f;
            texturePoints[currentIndex] = new Point2D32(textureX, textureY);
         }
      }

      int lastMajorIndex = isClosed ? majorN : majorN - 1;
      int numberOfTriangles = 2 * lastMajorIndex * minorN;
      if (!isClosed)
         numberOfTriangles += 2 * minorN;
      int[] triangleIndices = new int[3 * numberOfTriangles];

      int index = 0;

      // Torus core
      for (int majorIndex = 0; majorIndex < lastMajorIndex; majorIndex++)
      {
         for (int minorIndex = 0; minorIndex < minorN; minorIndex++)
         {
            int nextMajorIndex = (majorIndex + 1) % majorN;
            int nextMinorIndex = (minorIndex + 1) % minorN;

            triangleIndices[index++] = nextMajorIndex * minorN + minorIndex;
            triangleIndices[index++] = nextMajorIndex * minorN + nextMinorIndex;
            triangleIndices[index++] = majorIndex * minorN + nextMinorIndex;

            triangleIndices[index++] = nextMajorIndex * minorN + minorIndex;
            triangleIndices[index++] = majorIndex * minorN + nextMinorIndex;
            triangleIndices[index++] = majorIndex * minorN + minorIndex;
         }
      }

      // Close both ends when the torus is open
      if (!isClosed)
      {
         // First end
         float cosStartAngle = (float) Math.cos(startAngle);
         float sinStartAngle = (float) Math.sin(startAngle);
         centerX = majorRadius * cosStartAngle;
         centerY = majorRadius * sinStartAngle;

         for (int minorIndex = 0; minorIndex < minorN; minorIndex++)
         {
            int currentIndex = majorN * minorN + minorIndex;
            float minorAngle = minorIndex * 2.0f * (float) Math.PI / minorN;
            float cosMinorAngle = (float) Math.cos(minorAngle);
            float sinMinorAngle = (float) Math.sin(minorAngle);
            pX = centerX + minorRadius * cosStartAngle * cosMinorAngle;
            pY = centerY + minorRadius * sinStartAngle * cosMinorAngle;
            pZ = minorRadius * sinMinorAngle;
            points[currentIndex] = new Point3D32(pX, pY, pZ);
            normals[currentIndex] = new Vector3D32(sinStartAngle, -cosStartAngle, 0.0f);
            textureX = 0.75f + 0.25f * cosMinorAngle;
            textureY = 0.25f - 0.25f * sinMinorAngle;
            texturePoints[currentIndex] = new Point2D32(textureX, textureY);
         }

         // First end center
         int firstEndCenterIndex = numberOfVertices - 2;
         points[firstEndCenterIndex] = new Point3D32(centerX, centerY, 0.0f);
         normals[firstEndCenterIndex] = new Vector3D32(sinStartAngle, -cosStartAngle, 0.0f);
         texturePoints[firstEndCenterIndex] = new Point2D32(0.75f, 0.25f);

         // Second end
         float cosEndAngle = (float) Math.cos(endAngle);
         float sinEndAngle = (float) Math.sin(endAngle);
         centerX = majorRadius * cosEndAngle;
         centerY = majorRadius * sinEndAngle;

         for (int minorIndex = 0; minorIndex < minorN; minorIndex++)
         {
            int currentIndex = (majorN + 1) * minorN + minorIndex;
            float minorAngle = minorIndex * 2.0f * (float) Math.PI / minorN;
            float cosMinorAngle = (float) Math.cos(minorAngle);
            float sinMinorAngle = (float) Math.sin(minorAngle);
            pX = centerX + minorRadius * cosEndAngle * cosMinorAngle;
            pY = centerY + minorRadius * sinEndAngle * cosMinorAngle;
            pZ = minorRadius * sinMinorAngle;
            points[currentIndex] = new Point3D32(pX, pY, pZ);
            normals[currentIndex] = new Vector3D32(-sinEndAngle, cosEndAngle, 0.0f);
            textureX = 0.75f - 0.25f * cosMinorAngle;
            textureY = 0.75f - 0.25f * sinMinorAngle;
            texturePoints[currentIndex] = new Point2D32(textureX, textureY);
         }

         // Second end center
         int secondEndCenterIndex = numberOfVertices - 1;
         points[secondEndCenterIndex] = new Point3D32(centerX, centerY, 0.0f);
         normals[secondEndCenterIndex] = new Vector3D32(-sinEndAngle, cosEndAngle, 0.0f);
         texturePoints[secondEndCenterIndex] = new Point2D32(0.75f, 0.75f);

         // Setting up indices
         for (int minorIndex = 0; minorIndex < minorN; minorIndex++)
         {
            int nextMinorIndex = (minorIndex + 1) % minorN;

            triangleIndices[index++] = firstEndCenterIndex;
            triangleIndices[index++] = majorN * minorN + minorIndex;
            triangleIndices[index++] = majorN * minorN + nextMinorIndex;

            triangleIndices[index++] = secondEndCenterIndex;
            triangleIndices[index++] = (majorN + 1) * minorN + nextMinorIndex;
            triangleIndices[index++] = (majorN + 1) * minorN + minorIndex;
         }
      }

      return new MeshDataHolder(points, texturePoints, triangleIndices, normals);
   }

   /**
    * Creates a triangle mesh for a 3D box of size (lx, ly, lz).
    *
    * @param lx       box size along the x-axis.
    * @param ly       box size along the y-axis.
    * @param lz       box size along the z-axis.
    * @param centered when {@code true} the generated mesh is centered at the origin, when
    *                 {@code false} the bottom face of the box is centered at the origin.
    * @return the generic triangle mesh.
    */
   public static MeshDataHolder Box(double lx, double ly, double lz, boolean centered)
   {
      return Box((float) lx, (float) ly, (float) lz, centered);
   }

   /**
    * Creates a triangle mesh for a 3D box of size (lx, ly, lz).
    *
    * @param lx       box size along the x-axis.
    * @param ly       box size along the y-axis.
    * @param lz       box size along the z-axis.
    * @param centered when {@code true} the generated mesh is centered at the origin, when
    *                 {@code false} the bottom face of the box is centered at the origin.
    * @return the generic triangle mesh.
    */
   public static MeshDataHolder Box(float lx, float ly, float lz, boolean centered)
   {
      Point3D32 points[] = new Point3D32[24];
      Vector3D32[] normals = new Vector3D32[24];
      Point2D32 textPoints[] = new Point2D32[24];

      float zBottom = centered ? -lz / 2f : 0;
      float zTop = centered ? lz / 2f : lz;

      // Bottom vertices for bottom face
      points[0] = new Point3D32(-lx / 2.0f, -ly / 2.0f, zBottom);
      normals[0] = new Vector3D32(0.0f, 0.0f, -1.0f);
      textPoints[0] = new Point2D32(0.5f, 0.5f);
      points[1] = new Point3D32(lx / 2.0f, -ly / 2.0f, zBottom);
      normals[1] = new Vector3D32(0.0f, 0.0f, -1.0f);
      textPoints[1] = new Point2D32(0.25f, 0.5f);
      points[2] = new Point3D32(lx / 2.0f, ly / 2.0f, zBottom);
      normals[2] = new Vector3D32(0.0f, 0.0f, -1.0f);
      textPoints[2] = new Point2D32(0.25f, 0.25f);
      points[3] = new Point3D32(-lx / 2.0f, ly / 2.0f, zBottom);
      normals[3] = new Vector3D32(0.0f, 0.0f, -1.0f);
      textPoints[3] = new Point2D32(0.5f, 0.25f);

      // Top vertices for top face
      points[4] = new Point3D32(-lx / 2.0f, -ly / 2.0f, zTop);
      normals[4] = new Vector3D32(0.0f, 0.0f, 1.0f);
      textPoints[4] = new Point2D32(0.75f, 0.5f);
      points[5] = new Point3D32(lx / 2.0f, -ly / 2.0f, zTop);
      normals[5] = new Vector3D32(0.0f, 0.0f, 1.0f);
      textPoints[5] = new Point2D32(1.0f, 0.5f);
      points[6] = new Point3D32(lx / 2.0f, ly / 2.0f, zTop);
      normals[6] = new Vector3D32(0.0f, 0.0f, 1.0f);
      textPoints[6] = new Point2D32(1.0f, 0.25f);
      points[7] = new Point3D32(-lx / 2.0f, ly / 2.0f, zTop);
      normals[7] = new Vector3D32(0.0f, 0.0f, 1.0f);
      textPoints[7] = new Point2D32(0.75f, 0.25f);

      // Left face vertices
      points[8] = new Point3D32(points[2]);
      normals[8] = new Vector3D32(0.0f, 1.0f, 0.0f);
      textPoints[8] = new Point2D32(0.25f, 0.25f);
      points[9] = new Point3D32(points[3]);
      normals[9] = new Vector3D32(0.0f, 1.0f, 0.0f);
      textPoints[9] = new Point2D32(0.5f, 0.25f);
      points[10] = new Point3D32(points[6]);
      normals[10] = new Vector3D32(0.0f, 1.0f, 0.0f);
      textPoints[10] = new Point2D32(0.25f, 0.0f);
      points[11] = new Point3D32(points[7]);
      normals[11] = new Vector3D32(0.0f, 1.0f, 0.0f);
      textPoints[11] = new Point2D32(0.5f, 0.0f);

      // Right face vertices
      points[12] = new Point3D32(points[0]);
      normals[12] = new Vector3D32(0.0f, -1.0f, 0.0f);
      textPoints[12] = new Point2D32(0.5f, 0.5f);
      points[13] = new Point3D32(points[1]);
      normals[13] = new Vector3D32(0.0f, -1.0f, 0.0f);
      textPoints[13] = new Point2D32(0.25f, 0.5f);
      points[14] = new Point3D32(points[4]);
      normals[14] = new Vector3D32(0.0f, -1.0f, 0.0f);
      textPoints[14] = new Point2D32(0.5f, 0.75f);
      points[15] = new Point3D32(points[5]);
      normals[15] = new Vector3D32(0.0f, -1.0f, 0.0f);
      textPoints[15] = new Point2D32(0.25f, 0.75f);

      // Front face vertices
      points[16] = new Point3D32(points[0]);
      normals[16] = new Vector3D32(-1.0f, 0.0f, 0.0f);
      textPoints[16] = new Point2D32(0.5f, 0.5f);
      points[17] = new Point3D32(points[3]);
      normals[17] = new Vector3D32(-1.0f, 0.0f, 0.0f);
      textPoints[17] = new Point2D32(0.5f, 0.25f);
      points[18] = new Point3D32(points[4]);
      normals[18] = new Vector3D32(-1.0f, 0.0f, 0.0f);
      textPoints[18] = new Point2D32(0.75f, 0.5f);
      points[19] = new Point3D32(points[7]);
      normals[19] = new Vector3D32(-1.0f, 0.0f, 0.0f);
      textPoints[19] = new Point2D32(0.75f, 0.25f);

      // Back face vertices
      points[20] = new Point3D32(points[1]);
      normals[20] = new Vector3D32(1.0f, 0.0f, 0.0f);
      textPoints[20] = new Point2D32(0.25f, 0.5f);
      points[21] = new Point3D32(points[2]);
      normals[21] = new Vector3D32(1.0f, 0.0f, 0.0f);
      textPoints[21] = new Point2D32(0.25f, 0.25f);
      points[22] = new Point3D32(points[5]);
      normals[22] = new Vector3D32(1.0f, 0.0f, 0.0f);
      textPoints[22] = new Point2D32(0.0f, 0.5f);
      points[23] = new Point3D32(points[6]);
      normals[23] = new Vector3D32(1.0f, 0.0f, 0.0f);
      textPoints[23] = new Point2D32(0.0f, 0.25f);

      int numberOfTriangles = 2 * 6;

      int[] triangleIndices = new int[3 * numberOfTriangles];

      int index = 0;

      // Bottom face (face vertices 0, 1, 2, 3)
      triangleIndices[index++] = 2;
      triangleIndices[index++] = 1;
      triangleIndices[index++] = 0;

      triangleIndices[index++] = 3;
      triangleIndices[index++] = 2;
      triangleIndices[index++] = 0;

      // Top face (face vertices 4, 5, 6, 7)
      triangleIndices[index++] = 4;
      triangleIndices[index++] = 5;
      triangleIndices[index++] = 6;

      triangleIndices[index++] = 4;
      triangleIndices[index++] = 6;
      triangleIndices[index++] = 7;

      // Left face (face vertices 8, 9, 10, 11)
      triangleIndices[index++] = 8;
      triangleIndices[index++] = 11;
      triangleIndices[index++] = 10;

      triangleIndices[index++] = 8;
      triangleIndices[index++] = 9;
      triangleIndices[index++] = 11;

      // Right face (face vertices 12, 13, 14, 15)
      triangleIndices[index++] = 15;
      triangleIndices[index++] = 14;
      triangleIndices[index++] = 13;

      triangleIndices[index++] = 14;
      triangleIndices[index++] = 12;
      triangleIndices[index++] = 13;

      // Front face (face vertices 16, 17, 18, 19)
      triangleIndices[index++] = 16;
      triangleIndices[index++] = 19;
      triangleIndices[index++] = 17;

      triangleIndices[index++] = 16;
      triangleIndices[index++] = 18;
      triangleIndices[index++] = 19;

      // Back face (face vertices 20, 21, 22, 23)
      triangleIndices[index++] = 20;
      triangleIndices[index++] = 23;
      triangleIndices[index++] = 22;

      triangleIndices[index++] = 20;
      triangleIndices[index++] = 21;
      triangleIndices[index++] = 23;

      return new MeshDataHolder(points, textPoints, triangleIndices, normals);
   }

   /**
    * Creates a triangle mesh for a flat horizontal rectangle.
    * 
    * @param xSize the rectangle size along the x-axis.
    * @param ySize the rectangle size along the y-axis.
    * @param z     the height of the rectangle.
    * @return the generic triangle mesh.
    */
   public static MeshDataHolder FlatRectangle(double xSize, double ySize, double z)
   {
      return FlatRectangle((float) xSize, (float) ySize, (float) z);
   }

   /**
    * Creates a triangle mesh for a flat horizontal rectangle.
    * 
    * @param xSize the rectangle size along the x-axis.
    * @param ySize the rectangle size along the y-axis.
    * @param z     the height of the rectangle.
    * @return the generic triangle mesh.
    */
   public static MeshDataHolder FlatRectangle(float xSize, float ySize, float z)
   {
      return FlatRectangle(-0.5f * xSize, -0.5f * ySize, 0.5f * xSize, 0.5f * ySize, z);
   }

   /**
    * Creates a triangle mesh for a flat horizontal rectangle.
    * 
    * @param xMin the rectangle's lower-bound along the x-axis.
    * @param xMax the rectangle's upper-bound along the x-axis.
    * @param yMin the rectangle's lower-bound along the y-axis.
    * @param yMax the rectangle's upper-bound along the x-axis.
    * @param z    the height of the rectangle.
    * @return the generic triangle mesh.
    */
   public static MeshDataHolder FlatRectangle(double xMin, double yMin, double xMax, double yMax, double z)
   {
      return FlatRectangle((float) xMin, (float) yMin, (float) xMax, (float) yMax, (float) z);
   }

   /**
    * Creates a triangle mesh for a flat horizontal rectangle.
    * 
    * @param xMin the rectangle's lower-bound along the x-axis.
    * @param xMax the rectangle's upper-bound along the x-axis.
    * @param yMin the rectangle's lower-bound along the y-axis.
    * @param yMax the rectangle's upper-bound along the x-axis.
    * @param z    the height of the rectangle.
    * @return the generic triangle mesh.
    */
   public static MeshDataHolder FlatRectangle(float xMin, float yMin, float xMax, float yMax, float z)
   {
      Point3D32[] points = new Point3D32[4];
      Vector3D32[] normals = new Vector3D32[4];
      Point2D32[] textPoints = new Point2D32[4];

      points[0] = new Point3D32(xMin, yMin, z);
      points[1] = new Point3D32(xMax, yMin, z);
      points[2] = new Point3D32(xMax, yMax, z);
      points[3] = new Point3D32(xMin, yMax, z);

      textPoints[0] = new Point2D32(1.0f, 1.0f);
      textPoints[1] = new Point2D32(1.0f, 0.0f);
      textPoints[2] = new Point2D32(0.0f, 0.0f);
      textPoints[3] = new Point2D32(0.0f, 1.0f);

      normals[0] = new Vector3D32(0.0f, 0.0f, 1.0f);
      normals[1] = new Vector3D32(0.0f, 0.0f, 1.0f);
      normals[2] = new Vector3D32(0.0f, 0.0f, 1.0f);
      normals[3] = new Vector3D32(0.0f, 0.0f, 1.0f);

      int[] triangleIndices = new int[3 * 2];
      int index = 0;
      triangleIndices[index++] = 0;
      triangleIndices[index++] = 1;
      triangleIndices[index++] = 3;

      triangleIndices[index++] = 1;
      triangleIndices[index++] = 2;
      triangleIndices[index++] = 3;

      return new MeshDataHolder(points, textPoints, triangleIndices, normals);
   }

   /**
    * Creates a triangle mesh for a 3D wedge.
    * <p>
    * The wedge is positioned such that its bottom face is centered at the origin.
    * </p>
    * 
    * @param lx wedge size along the x-axis.
    * @param ly wedge size along the y-axis.
    * @param lz wedge size along the z-axis.
    * @return the generic triangle mesh.
    */
   public static MeshDataHolder Wedge(double lx, double ly, double lz)
   {
      return Wedge((float) lx, (float) ly, (float) lz);
   }

   /**
    * Creates a triangle mesh for a 3D wedge.
    * <p>
    * The wedge is positioned such that its bottom face is centered at the origin.
    * </p>
    * 
    * @param lx wedge size along the x-axis.
    * @param ly wedge size along the y-axis.
    * @param lz wedge size along the z-axis.
    * @return the generic triangle mesh.
    */
   public static MeshDataHolder Wedge(float lx, float ly, float lz)
   {
      Point3D32[] points = new Point3D32[18];
      Vector3D32[] normals = new Vector3D32[18];
      Point2D32[] textPoints = new Point2D32[18];

      float tex0 = 0.0f;
      float tex1 = 1.0f / 3.0f;
      float tex2 = 2.0f / 3.0f;
      float tex3 = 1.0f;

      // Bottom face vertices
      points[0] = new Point3D32(-lx / 2.0f, -ly / 2.0f, 0.0f);
      normals[0] = new Vector3D32(0.0f, 0.0f, -1.0f);
      textPoints[0] = new Point2D32(tex2, tex2);
      points[1] = new Point3D32(lx / 2.0f, -ly / 2.0f, 0.0f);
      normals[1] = new Vector3D32(0.0f, 0.0f, -1.0f);
      textPoints[1] = new Point2D32(tex1, tex2);
      points[2] = new Point3D32(lx / 2.0f, ly / 2.0f, 0.0f);
      normals[2] = new Vector3D32(0.0f, 0.0f, -1.0f);
      textPoints[2] = new Point2D32(tex1, tex1);
      points[3] = new Point3D32(-lx / 2.0f, ly / 2.0f, 0.0f);
      normals[3] = new Vector3D32(0.0f, 0.0f, -1.0f);
      textPoints[3] = new Point2D32(tex2, tex1);

      // Back face vertices
      points[4] = new Point3D32(lx / 2.0f, -ly / 2.0f, lz);
      normals[4] = new Vector3D32(1.0f, 0.0f, 0.0f);
      textPoints[4] = new Point2D32(tex0, tex2);
      points[5] = new Point3D32(lx / 2.0f, ly / 2.0f, lz);
      normals[5] = new Vector3D32(1.0f, 0.0f, 0.0f);
      textPoints[5] = new Point2D32(tex0, tex1);
      points[6] = new Point3D32(points[2]);
      normals[6] = new Vector3D32(1.0f, 0.0f, 0.0f);
      textPoints[6] = new Point2D32(tex1, tex1);
      points[7] = new Point3D32(points[1]);
      normals[7] = new Vector3D32(1.0f, 0.0f, 0.0f);
      textPoints[7] = new Point2D32(tex1, tex2);

      // Top face vertices
      float wedgeAngle = (float) Math.atan2(lz, lx);
      points[8] = new Point3D32(points[0]);
      normals[8] = new Vector3D32(-(float) Math.sin(wedgeAngle), 0.0f, (float) Math.cos(wedgeAngle));
      textPoints[8] = new Point2D32(tex2, tex2);
      points[9] = new Point3D32(points[4]);
      normals[9] = new Vector3D32(-(float) Math.sin(wedgeAngle), 0.0f, (float) Math.cos(wedgeAngle));
      textPoints[9] = new Point2D32(tex3, tex2);
      points[10] = new Point3D32(points[5]);
      normals[10] = new Vector3D32(-(float) Math.sin(wedgeAngle), 0.0f, (float) Math.cos(wedgeAngle));
      textPoints[10] = new Point2D32(tex3, tex1);
      points[11] = new Point3D32(points[3]);
      normals[11] = new Vector3D32(-(float) Math.sin(wedgeAngle), 0.0f, (float) Math.cos(wedgeAngle));
      textPoints[11] = new Point2D32(tex2, tex1);

      // Right face vertices
      points[12] = new Point3D32(points[0]);
      normals[12] = new Vector3D32(0.0f, -1.0f, 0.0f);
      textPoints[12] = new Point2D32(tex2, tex2);
      points[13] = new Point3D32(points[1]);
      normals[13] = new Vector3D32(0.0f, -1.0f, 0.0f);
      textPoints[13] = new Point2D32(tex1, tex2);
      points[14] = new Point3D32(points[4]);
      normals[14] = new Vector3D32(0.0f, -1.0f, 0.0f);
      textPoints[14] = new Point2D32(tex1, tex3);

      // Left face vertices
      points[15] = new Point3D32(points[2]);
      normals[15] = new Vector3D32(0.0f, 1.0f, 0.0f);
      textPoints[15] = new Point2D32(tex1, tex1);
      points[16] = new Point3D32(points[3]);
      normals[16] = new Vector3D32(0.0f, 1.0f, 0.0f);
      textPoints[16] = new Point2D32(tex2, tex1);
      points[17] = new Point3D32(points[5]);
      normals[17] = new Vector3D32(0.0f, 1.0f, 0.0f);
      textPoints[17] = new Point2D32(tex1, tex0);

      int numberOfTriangles = 2 * 3 + 2;
      int[] triangleIndices = new int[3 * numberOfTriangles];

      int index = 0;

      // Bottom face
      triangleIndices[index++] = 0;
      triangleIndices[index++] = 2;
      triangleIndices[index++] = 1;

      triangleIndices[index++] = 0;
      triangleIndices[index++] = 3;
      triangleIndices[index++] = 2;

      // Back face
      triangleIndices[index++] = 7;
      triangleIndices[index++] = 5;
      triangleIndices[index++] = 4;

      triangleIndices[index++] = 5;
      triangleIndices[index++] = 7;
      triangleIndices[index++] = 6;

      // Top face
      triangleIndices[index++] = 8;
      triangleIndices[index++] = 9;
      triangleIndices[index++] = 10;

      triangleIndices[index++] = 8;
      triangleIndices[index++] = 10;
      triangleIndices[index++] = 11;

      // Right face
      triangleIndices[index++] = 12;
      triangleIndices[index++] = 13;
      triangleIndices[index++] = 14;

      // Left face
      triangleIndices[index++] = 15;
      triangleIndices[index++] = 16;
      triangleIndices[index++] = 17;

      return new MeshDataHolder(points, textPoints, triangleIndices, normals);
   }

   /**
    * Creates a triangle mesh for a 3D cube which bottom and top faces are extended with a pyramid.
    * 
    * @param lx cube size along the x-axis.
    * @param ly cube size along the y-axis.
    * @param lz cube size along the z-axis.
    * @param lh the height for each pyramid.
    * @return the generic triangle mesh.
    */
   public static MeshDataHolder PyramidCube(double lx, double ly, double lz, double lh)
   {
      return PyramidCube((float) lx, (float) ly, (float) lz, (float) lh);
   }

   /**
    * Creates a triangle mesh for a 3D cube which bottom and top faces are extended with a pyramid.
    * 
    * @param lx cube size along the x-axis.
    * @param ly cube size along the y-axis.
    * @param lz cube size along the z-axis.
    * @param lh the height for each pyramid.
    * @return the generic triangle mesh.
    */
   public static MeshDataHolder PyramidCube(float lx, float ly, float lz, float lh)
   {
      Point3D32 points[] = new Point3D32[40];
      Vector3D32[] normals = new Vector3D32[40];
      Point2D32 textPoints[] = new Point2D32[40];
      float totalHeight = 2.0f * lh + lz;

      // Box front face
      points[0] = new Point3D32(-lx / 2.0f, -ly / 2.0f, -0.5f * lz);
      normals[0] = new Vector3D32(-1.0f, 0.0f, 0.0f);
      textPoints[0] = new Point2D32(0.75f, 1.0f - lh / totalHeight);
      points[1] = new Point3D32(-lx / 2.0f, -ly / 2.0f, 0.5f * lz);
      normals[1] = new Vector3D32(-1.0f, 0.0f, 0.0f);
      textPoints[1] = new Point2D32(0.75f, lh / totalHeight);
      points[2] = new Point3D32(-lx / 2.0f, ly / 2.0f, 0.5f * lz);
      normals[2] = new Vector3D32(-1.0f, 0.0f, 0.0f);
      textPoints[2] = new Point2D32(0.5f, lh / totalHeight);
      points[3] = new Point3D32(-lx / 2.0f, ly / 2.0f, -0.5f * lz);
      normals[3] = new Vector3D32(-1.0f, 0.0f, 0.0f);
      textPoints[3] = new Point2D32(0.5f, 1.0f - lh / totalHeight);

      // Box back face
      points[4] = new Point3D32(lx / 2.0f, -ly / 2.0f, -0.5f * lz);
      normals[4] = new Vector3D32(1.0f, 0.0f, 0.0f);
      textPoints[4] = new Point2D32(0.0f, 1.0f - lh / totalHeight);
      points[5] = new Point3D32(lx / 2.0f, -ly / 2.0f, 0.5f * lz);
      normals[5] = new Vector3D32(1.0f, 0.0f, 0.0f);
      textPoints[5] = new Point2D32(0.0f, lh / totalHeight);
      points[6] = new Point3D32(lx / 2.0f, ly / 2.0f, 0.5f * lz);
      normals[6] = new Vector3D32(1.0f, 0.0f, 0.0f);
      textPoints[6] = new Point2D32(0.25f, lh / totalHeight);
      points[7] = new Point3D32(lx / 2.0f, ly / 2.0f, -0.5f * lz);
      normals[7] = new Vector3D32(1.0f, 0.0f, 0.0f);
      textPoints[7] = new Point2D32(0.25f, 1.0f - lh / totalHeight);

      // Box left face
      points[8] = new Point3D32(-lx / 2.0f, ly / 2.0f, -0.5f * lz);
      normals[8] = new Vector3D32(0.0f, 1.0f, 0.0f);
      textPoints[8] = new Point2D32(0.5f, 1.0f - lh / totalHeight);
      points[9] = new Point3D32(-lx / 2.0f, ly / 2.0f, 0.5f * lz);
      normals[9] = new Vector3D32(0.0f, 1.0f, 0.0f);
      textPoints[9] = new Point2D32(0.5f, lh / totalHeight);
      points[10] = new Point3D32(lx / 2.0f, ly / 2.0f, 0.5f * lz);
      normals[10] = new Vector3D32(0.0f, 1.0f, 0.0f);
      textPoints[10] = new Point2D32(0.25f, lh / totalHeight);
      points[11] = new Point3D32(lx / 2.0f, ly / 2.0f, -0.5f * lz);
      normals[11] = new Vector3D32(0.0f, 1.0f, 0.0f);
      textPoints[11] = new Point2D32(0.25f, 1.0f - lh / totalHeight);

      // Box right face
      points[12] = new Point3D32(-lx / 2.0f, -ly / 2.0f, -0.5f * lz);
      normals[12] = new Vector3D32(0.0f, -1.0f, 0.0f);
      textPoints[12] = new Point2D32(0.75f, 1.0f - lh / totalHeight);
      points[13] = new Point3D32(-lx / 2.0f, -ly / 2.0f, 0.5f * lz);
      normals[13] = new Vector3D32(0.0f, -1.0f, 0.0f);
      textPoints[13] = new Point2D32(0.75f, lh / totalHeight);
      points[14] = new Point3D32(lx / 2.0f, -ly / 2.0f, 0.5f * lz);
      normals[14] = new Vector3D32(0.0f, -1.0f, 0.0f);
      textPoints[14] = new Point2D32(1.0f, lh / totalHeight);
      points[15] = new Point3D32(lx / 2.0f, -ly / 2.0f, -0.5f * lz);
      normals[15] = new Vector3D32(0.0f, -1.0f, 0.0f);
      textPoints[15] = new Point2D32(1.0f, 1.0f - lh / totalHeight);

      float frontBackAngle = (float) Math.atan2(lx / 2.0, lh);
      float leftRightAngle = (float) Math.atan2(ly / 2.0, lh);

      // Top pyramid
      // Front face
      points[16] = new Point3D32(0.0f, 0.0f, 0.5f * lz + lh);
      normals[16] = new Vector3D32(-(float) Math.cos(frontBackAngle), 0.0f, (float) Math.sin(frontBackAngle));
      textPoints[16] = new Point2D32(0.625f, 0.0f);
      points[17] = new Point3D32(-lx / 2.0f, -ly / 2.0f, 0.5f * lz);
      normals[17] = new Vector3D32(-(float) Math.cos(frontBackAngle), 0.0f, (float) Math.sin(frontBackAngle));
      textPoints[17] = new Point2D32(0.75f, lh / totalHeight);
      points[18] = new Point3D32(-lx / 2.0f, ly / 2.0f, 0.5f * lz);
      normals[18] = new Vector3D32(-(float) Math.cos(frontBackAngle), 0.0f, (float) Math.sin(frontBackAngle));
      textPoints[18] = new Point2D32(0.5f, lh / totalHeight);

      // Back face
      points[19] = new Point3D32(0.0f, 0.0f, 0.5f * lz + lh);
      normals[19] = new Vector3D32((float) Math.cos(frontBackAngle), 0.0f, (float) Math.sin(frontBackAngle));
      textPoints[19] = new Point2D32(0.125f, 0.0f);
      points[20] = new Point3D32(lx / 2.0f, -ly / 2.0f, 0.5f * lz);
      normals[20] = new Vector3D32((float) Math.cos(frontBackAngle), 0.0f, (float) Math.sin(frontBackAngle));
      textPoints[20] = new Point2D32(0.0f, lh / totalHeight);
      points[21] = new Point3D32(lx / 2.0f, ly / 2.0f, 0.5f * lz);
      normals[21] = new Vector3D32((float) Math.cos(frontBackAngle), 0.0f, (float) Math.sin(frontBackAngle));
      textPoints[21] = new Point2D32(0.25f, lh / totalHeight);

      // Left face
      points[22] = new Point3D32(0.0f, 0.0f, 0.5f * lz + lh);
      normals[22] = new Vector3D32(0.0f, (float) Math.cos(leftRightAngle), (float) Math.sin(leftRightAngle));
      textPoints[22] = new Point2D32(0.375f, 0.0f);
      points[23] = new Point3D32(-lx / 2.0f, ly / 2.0f, 0.5f * lz);
      normals[23] = new Vector3D32(0.0f, (float) Math.cos(leftRightAngle), (float) Math.sin(leftRightAngle));
      textPoints[23] = new Point2D32(0.5f, lh / totalHeight);
      points[24] = new Point3D32(lx / 2.0f, ly / 2.0f, 0.5f * lz);
      normals[24] = new Vector3D32(0.0f, (float) Math.cos(leftRightAngle), (float) Math.sin(leftRightAngle));
      textPoints[24] = new Point2D32(0.25f, lh / totalHeight);

      // Right face
      points[25] = new Point3D32(0.0f, 0.0f, 0.5f * lz + lh);
      normals[25] = new Vector3D32(0.0f, -(float) Math.cos(leftRightAngle), (float) Math.sin(leftRightAngle));
      textPoints[25] = new Point2D32(0.875f, 0.0f);
      points[26] = new Point3D32(-lx / 2.0f, -ly / 2.0f, 0.5f * lz);
      normals[26] = new Vector3D32(0.0f, -(float) Math.cos(leftRightAngle), (float) Math.sin(leftRightAngle));
      textPoints[26] = new Point2D32(0.75f, lh / totalHeight);
      points[27] = new Point3D32(lx / 2.0f, -ly / 2.0f, 0.5f * lz);
      normals[27] = new Vector3D32(0.0f, -(float) Math.cos(leftRightAngle), (float) Math.sin(leftRightAngle));
      textPoints[27] = new Point2D32(1.0f, lh / totalHeight);

      // Bottom pyramid
      // Front face
      points[28] = new Point3D32(0.0f, 0.0f, -lh - 0.5f * lz);
      normals[28] = new Vector3D32(-(float) Math.cos(frontBackAngle), 0.0f, -(float) Math.sin(frontBackAngle));
      textPoints[28] = new Point2D32(0.625f, 1.0f);
      points[29] = new Point3D32(-lx / 2.0f, -ly / 2.0f, -0.5f * lz);
      normals[29] = new Vector3D32(-(float) Math.cos(frontBackAngle), 0.0f, -(float) Math.sin(frontBackAngle));
      textPoints[29] = new Point2D32(0.75f, 1.0f - lh / totalHeight);
      points[30] = new Point3D32(-lx / 2.0f, ly / 2.0f, -0.5f * lz);
      normals[30] = new Vector3D32(-(float) Math.cos(frontBackAngle), 0.0f, -(float) Math.sin(frontBackAngle));
      textPoints[30] = new Point2D32(0.5f, 1.0f - lh / totalHeight);

      // Back face
      points[31] = new Point3D32(0.0f, 0.0f, -lh - 0.5f * lz);
      normals[31] = new Vector3D32((float) Math.cos(frontBackAngle), 0.0f, -(float) Math.sin(frontBackAngle));
      textPoints[31] = new Point2D32(0.125f, 1.0f);
      points[32] = new Point3D32(lx / 2.0f, -ly / 2.0f, -0.5f * lz);
      normals[32] = new Vector3D32((float) Math.cos(frontBackAngle), 0.0f, -(float) Math.sin(frontBackAngle));
      textPoints[32] = new Point2D32(0.0f, 1.0f - lh / totalHeight);
      points[33] = new Point3D32(lx / 2.0f, ly / 2.0f, -0.5f * lz);
      normals[33] = new Vector3D32((float) Math.cos(frontBackAngle), 0.0f, -(float) Math.sin(frontBackAngle));
      textPoints[33] = new Point2D32(0.25f, 1.0f - lh / totalHeight);

      // Left face
      points[34] = new Point3D32(0.0f, 0.0f, -lh - 0.5f * lz);
      normals[34] = new Vector3D32(0.0f, (float) Math.cos(leftRightAngle), -(float) Math.sin(leftRightAngle));
      textPoints[34] = new Point2D32(0.375f, 1.0f);
      points[35] = new Point3D32(-lx / 2.0f, ly / 2.0f, -0.5f * lz);
      normals[35] = new Vector3D32(0.0f, (float) Math.cos(leftRightAngle), -(float) Math.sin(leftRightAngle));
      textPoints[35] = new Point2D32(0.5f, 1.0f - lh / totalHeight);
      points[36] = new Point3D32(lx / 2.0f, ly / 2.0f, -0.5f * lz);
      normals[36] = new Vector3D32(0.0f, (float) Math.cos(leftRightAngle), -(float) Math.sin(leftRightAngle));
      textPoints[36] = new Point2D32(0.25f, 1.0f - lh / totalHeight);

      // Right face
      points[37] = new Point3D32(0.0f, 0.0f, -lh - 0.5f * lz);
      normals[37] = new Vector3D32(0.0f, -(float) Math.cos(leftRightAngle), -(float) Math.sin(leftRightAngle));
      textPoints[37] = new Point2D32(0.875f, 1.0f);
      points[38] = new Point3D32(-lx / 2.0f, -ly / 2.0f, -0.5f * lz);
      normals[38] = new Vector3D32(0.0f, -(float) Math.cos(leftRightAngle), -(float) Math.sin(leftRightAngle));
      textPoints[38] = new Point2D32(0.75f, 1.0f - lh / totalHeight);
      points[39] = new Point3D32(lx / 2.0f, -ly / 2.0f, -0.5f * lz);
      normals[39] = new Vector3D32(0.0f, -(float) Math.cos(leftRightAngle), -(float) Math.sin(leftRightAngle));
      textPoints[39] = new Point2D32(1.0f, 1.0f - lh / totalHeight);

      int numberOfTriangles = 2 * 4 + 2 * 4;
      int[] polygonIndices = new int[3 * numberOfTriangles];
      int index = 0;

      // Box front face
      polygonIndices[index++] = 0;
      polygonIndices[index++] = 1;
      polygonIndices[index++] = 2;

      polygonIndices[index++] = 0;
      polygonIndices[index++] = 2;
      polygonIndices[index++] = 3;

      // Box back face
      polygonIndices[index++] = 4;
      polygonIndices[index++] = 6;
      polygonIndices[index++] = 5;

      polygonIndices[index++] = 4;
      polygonIndices[index++] = 7;
      polygonIndices[index++] = 6;

      // Box left face
      polygonIndices[index++] = 8;
      polygonIndices[index++] = 9;
      polygonIndices[index++] = 10;

      polygonIndices[index++] = 8;
      polygonIndices[index++] = 10;
      polygonIndices[index++] = 11;

      // Box right face
      polygonIndices[index++] = 12;
      polygonIndices[index++] = 14;
      polygonIndices[index++] = 13;

      polygonIndices[index++] = 12;
      polygonIndices[index++] = 15;
      polygonIndices[index++] = 14;

      // Top pyramid front face
      polygonIndices[index++] = 16;
      polygonIndices[index++] = 18;
      polygonIndices[index++] = 17;

      // Top pyramid back face
      polygonIndices[index++] = 19;
      polygonIndices[index++] = 20;
      polygonIndices[index++] = 21;

      // Top pyramid left face
      polygonIndices[index++] = 22;
      polygonIndices[index++] = 24;
      polygonIndices[index++] = 23;

      // Top pyramid right face
      polygonIndices[index++] = 25;
      polygonIndices[index++] = 26;
      polygonIndices[index++] = 27;

      // Bottom pyramid front face
      polygonIndices[index++] = 28;
      polygonIndices[index++] = 29;
      polygonIndices[index++] = 30;

      // Bottom pyramid back face
      polygonIndices[index++] = 31;
      polygonIndices[index++] = 33;
      polygonIndices[index++] = 32;

      // Bottom pyramid left face
      polygonIndices[index++] = 36;
      polygonIndices[index++] = 34;
      polygonIndices[index++] = 35;

      // Bottom pyramid right face
      polygonIndices[index++] = 37;
      polygonIndices[index++] = 39;
      polygonIndices[index++] = 38;

      return new MeshDataHolder(points, textPoints, polygonIndices, normals);
   }

   /**
    * Create a triangle mesh for a 3D line segment.
    * <p>
    * The line segment is implemented as an elongated 3D box.
    * </p>
    * 
    * @param lineSegment used to define the mesh end points.
    * @param width       the thickness of the line.
    * @return the generic triangle mesh.
    */
   public static MeshDataHolder Line(LineSegment3DReadOnly lineSegment, double width)
   {
      return Line(lineSegment.getFirstEndpoint(), lineSegment.getSecondEndpoint(), width);
   }

   /**
    * Create a triangle mesh for a 3D line segment.
    * <p>
    * The line segment is implemented as an elongated 3D box.
    * </p>
    *
    * @param point0 the first endpoint of the line segment.
    * @param point1 the second endpoint of the line segment.
    * @param width  the thickness of the line.
    * @return the generic triangle mesh.
    */
   public static MeshDataHolder Line(Point3DReadOnly point0, Point3DReadOnly point1, double width)
   {
      return Line(point0.getX(), point0.getY(), point0.getZ(), point1.getX(), point1.getY(), point1.getZ(), width);
   }

   /**
    * Create a triangle mesh for a 3D line segment.
    * <p>
    * The line segment is implemented as an elongated 3D box.
    * </p>
    * 
    * @param x0    the x-coordinate of the first endpoint for the line segment.
    * @param y0    the y-coordinate of the first endpoint for the line segment.
    * @param z0    the z-coordinate of the first endpoint for the line segment.
    * @param x1    the x-coordinate of the second endpoint for the line segment.
    * @param y1    the y-coordinate of the second endpoint for the line segment.
    * @param z1    the z-coordinate of the second endpoint for the line segment.
    * @param width the thickness of the line.
    * @return the generic triangle mesh.
    */
   public static MeshDataHolder Line(double x0, double y0, double z0, double x1, double y1, double z1, double width)
   {
      return Line((float) x0, (float) y0, (float) z0, (float) x1, (float) y1, (float) z1, (float) width);
   }

   /**
    * Create a triangle mesh for a 3D line segment.
    * <p>
    * The line segment is implemented as an elongated 3D box.
    * </p>
    * 
    * @param x0    the x-coordinate of the first endpoint for the line segment.
    * @param y0    the y-coordinate of the first endpoint for the line segment.
    * @param z0    the z-coordinate of the first endpoint for the line segment.
    * @param x1    the x-coordinate of the second endpoint for the line segment.
    * @param y1    the y-coordinate of the second endpoint for the line segment.
    * @param z1    the z-coordinate of the second endpoint for the line segment.
    * @param width the thickness of the line.
    * @return the generic triangle mesh.
    */
   public static MeshDataHolder Line(float x0, float y0, float z0, float x1, float y1, float z1, float width)
   {
      Vector3D32 lineDirection = new Vector3D32(x1 - x0, y1 - y0, z1 - z0);
      float lineLength = (float) lineDirection.length();
      lineDirection.scale(1.0f / lineLength);
      MeshDataHolder line = Box(width, width, lineLength, false);
      Point3D32[] vertices = line.getVertices();
      Vector3D32[] normals = line.getVertexNormals();

      float yaw;
      float pitch;
      if (Math.abs(lineDirection.getZ()) < 1.0 - 1.0e-7)
      {
         yaw = (float) Math.atan2(lineDirection.getY(), lineDirection.getX());
         double xyLength = Math.sqrt(lineDirection.getX() * lineDirection.getX() + lineDirection.getY() * lineDirection.getY());
         pitch = (float) Math.atan2(xyLength, lineDirection.getZ());
      }
      else
      {
         yaw = 0.0f;
         pitch = lineDirection.getZ() >= 0.0 ? 0.0f : (float) Math.PI;
      }

      float cYaw = (float) Math.cos(yaw);
      float sYaw = (float) Math.sin(yaw);

      float cPitch = (float) Math.cos(pitch);
      float sPitch = (float) Math.sin(pitch);

      float rxx = cYaw * cPitch;
      float rxy = -sYaw;
      float rxz = cYaw * sPitch;
      float ryx = sYaw * cPitch;
      float ryy = cYaw;
      float ryz = sYaw * sPitch;
      float rzx = -sPitch;
      float rzz = cPitch;

      for (int i = 0; i < vertices.length; i++)
      {
         Point3D32 vertex = vertices[i];
         float vx = vertex.getX32();
         float vy = vertex.getY32();
         float vz = vertex.getZ32();
         vertex.setX(x0 + rxx * vx + rxy * vy + rxz * vz);
         vertex.setY(y0 + ryx * vx + ryy * vy + ryz * vz);
         vertex.setZ(z0 + rzx * vx + rzz * vz);
      }

      for (int i = 0; i < normals.length; i++)
      {
         Vector3D32 normal = normals[i];
         float vx = normal.getX32();
         float vy = normal.getY32();
         float vz = normal.getZ32();
         normal.setX(rxx * vx + rxy * vy + rxz * vz);
         normal.setY(ryx * vx + ryy * vy + ryz * vz);
         normal.setZ(rzx * vx + rzz * vz);
      }
      return line;
   }

   /**
    * Creates a triangle mesh for a 3D capsule with its ends being half ellipsoids.
    * <p>
    * The capsule's axis is aligned with the z-axis and it is centered at the origin.
    * </p>
    *
    * @param height              the capsule's height or length. Distance separating the center of the
    *                            two half ellipsoids.
    * @param xRadius             radius of along the x-axis.
    * @param yRadius             radius of along the y-axis.
    * @param zRadius             radius of along the z-axis.
    * @param latitudeResolution  the resolution along the vertical axis, i.e. z-axis.
    * @param longitudeResolution the resolution around the vertical axis, i.e. the number of vertices
    *                            per latitude.
    * @return the generic triangle mesh.
    */
   public static MeshDataHolder Capsule(double height, double xRadius, double yRadius, double zRadius, int latitudeResolution, int longitudeResolution)
   {
      return Capsule((float) height, (float) xRadius, (float) yRadius, (float) zRadius, latitudeResolution, longitudeResolution);
   }

   /**
    * Creates a triangle mesh for a 3D capsule with its ends being half ellipsoids.
    * <p>
    * The capsule's axis is aligned with the z-axis and it is centered at the origin.
    * </p>
    *
    * @param height              the capsule's height or length. Distance separating the center of the
    *                            two half ellipsoids.
    * @param xRadius             radius of along the x-axis.
    * @param yRadius             radius of along the y-axis.
    * @param zRadius             radius of along the z-axis.
    * @param latitudeResolution  the resolution along the vertical axis, i.e. z-axis.
    * @param longitudeResolution the resolution around the vertical axis, i.e. the number of vertices
    *                            per latitude.
    * @return the generic triangle mesh.
    */
   public static MeshDataHolder Capsule(float height, float xRadius, float yRadius, float zRadius, int latitudeResolution, int longitudeResolution)
   {
      if (latitudeResolution % 2 != 0)
         latitudeResolution++;
      if (longitudeResolution % 2 != 1)
         longitudeResolution++;

      // Reminder of longitude and latitude: http://www.geographyalltheway.com/ks3_geography/maps_atlases/longitude_latitude.htm
      int numberOfVertices = latitudeResolution * longitudeResolution;
      Point3D32 points[] = new Point3D32[numberOfVertices];
      Vector3D32[] normals = new Vector3D32[numberOfVertices];
      Point2D32 textPoints[] = new Point2D32[numberOfVertices];

      float texRatio = zRadius / (2.0f * zRadius + height);

      float halfHeight = 0.5f * height;

      for (int longitudeIndex = 0; longitudeIndex < longitudeResolution; longitudeIndex++)
      {
         float longitudeAngle = TwoPi * ((float) longitudeIndex / (float) (longitudeResolution - 1.0f));
         float textureX = (float) longitudeIndex / (float) (longitudeResolution - 1);

         // Bottom hemi-ellipsoid
         for (int latitudeIndex = 1; latitudeIndex < latitudeResolution / 2; latitudeIndex++)
         {
            float latitudeAngle = (float) (-HalfPi + Math.PI * ((float) latitudeIndex / (latitudeResolution - 1.0f)));

            float cosLongitude = (float) Math.cos(longitudeAngle);
            float sinLongitude = (float) Math.sin(longitudeAngle);
            float cosLatitude = (float) Math.cos(latitudeAngle);
            float sinLatitude = (float) Math.sin(latitudeAngle);

            int currentIndex = latitudeIndex * longitudeResolution + longitudeIndex;
            float normalX = cosLongitude * cosLatitude;
            float normalY = sinLongitude * cosLatitude;
            float normalZ = sinLatitude;
            float vertexX = xRadius * normalX;
            float vertexY = yRadius * normalY;
            float vertexZ = zRadius * normalZ - halfHeight;
            points[currentIndex] = new Point3D32(vertexX, vertexY, vertexZ);
            normals[currentIndex] = new Vector3D32(normalX, normalY, normalZ);

            float textureY = 1.0f - (1.0f + sinLatitude) * texRatio;
            textPoints[currentIndex] = new Point2D32(textureX, textureY);
         }

         // Top hemi-ellipsoid
         for (int latitudeIndex = 0; latitudeIndex < latitudeResolution / 2 - 1; latitudeIndex++)
         {
            float latitudeAngle = (float) (Math.PI * ((float) latitudeIndex / (latitudeResolution - 1.0f)));

            float cosLongitude = (float) Math.cos(longitudeAngle);
            float sinLongitude = (float) Math.sin(longitudeAngle);
            float cosLatitude = (float) Math.cos(latitudeAngle);
            float sinLatitude = (float) Math.sin(latitudeAngle);

            int currentIndex = (latitudeResolution / 2 + latitudeIndex) * longitudeResolution + longitudeIndex;
            float normalX = cosLongitude * cosLatitude;
            float normalY = sinLongitude * cosLatitude;
            float normalZ = sinLatitude;
            float vertexX = xRadius * normalX;
            float vertexY = yRadius * normalY;
            float vertexZ = zRadius * normalZ + halfHeight;
            points[currentIndex] = new Point3D32(vertexX, vertexY, vertexZ);
            normals[currentIndex] = new Vector3D32(normalX, normalY, normalZ);

            float textureY = (1.0f - sinLatitude) * texRatio;
            textPoints[currentIndex] = new Point2D32(textureX, textureY);
         }

         textureX += 0.5f / (longitudeResolution - 1.0f);
         // South pole
         int southPoleIndex = longitudeIndex;
         points[southPoleIndex] = new Point3D32(0.0f, 0.0f, -zRadius - halfHeight);
         normals[southPoleIndex] = new Vector3D32(0.0f, 0.0f, -1.0f);
         textPoints[southPoleIndex] = new Point2D32(textureX, 1.0f - 1.0f / 256.0f);

         // North pole
         int northPoleIndex = (latitudeResolution - 1) * longitudeResolution + longitudeIndex;
         points[northPoleIndex] = new Point3D32(0.0f, 0.0f, zRadius + halfHeight);
         normals[northPoleIndex] = new Vector3D32(0.0f, 0.0f, 1.0f);
         textPoints[northPoleIndex] = new Point2D32(textureX, 1.0f / 256.0f);
      }

      int numberOfTriangles = 2 * latitudeResolution * longitudeResolution + 1 * longitudeResolution;
      int[] triangleIndices = new int[3 * numberOfTriangles];

      int index = 0;

      // Top hemi-ellipsoid Mid-latitude faces
      for (int latitudeIndex = 1; latitudeIndex < latitudeResolution - 1; latitudeIndex++)
      {
         for (int longitudeIndex = 0; longitudeIndex < longitudeResolution; longitudeIndex++)
         {
            int nextLongitudeIndex = (longitudeIndex + 1) % longitudeResolution;
            int nextLatitudeIndex = latitudeIndex + 1;

            // Lower triangles
            triangleIndices[index++] = latitudeIndex * longitudeResolution + longitudeIndex;
            triangleIndices[index++] = latitudeIndex * longitudeResolution + nextLongitudeIndex;
            triangleIndices[index++] = nextLatitudeIndex * longitudeResolution + longitudeIndex;
            // Upper triangles
            triangleIndices[index++] = latitudeIndex * longitudeResolution + nextLongitudeIndex;
            triangleIndices[index++] = nextLatitudeIndex * longitudeResolution + nextLongitudeIndex;
            triangleIndices[index++] = nextLatitudeIndex * longitudeResolution + longitudeIndex;
         }
      }

      // South pole faces
      for (int longitudeIndex = 0; longitudeIndex < longitudeResolution - 1; longitudeIndex++)
      {
         int nextLongitudeIndex = (longitudeIndex + 1) % longitudeResolution;
         triangleIndices[index++] = longitudeIndex;
         triangleIndices[index++] = longitudeResolution + nextLongitudeIndex;
         triangleIndices[index++] = longitudeResolution + longitudeIndex;
      }

      // North pole faces
      for (int longitudeIndex = 0; longitudeIndex < longitudeResolution - 1; longitudeIndex++)
      {
         int nextLongitudeIndex = (longitudeIndex + 1) % longitudeResolution;
         triangleIndices[index++] = (latitudeResolution - 1) * longitudeResolution + longitudeIndex;
         triangleIndices[index++] = (latitudeResolution - 2) * longitudeResolution + longitudeIndex;
         triangleIndices[index++] = (latitudeResolution - 2) * longitudeResolution + nextLongitudeIndex;
      }

      return new MeshDataHolder(points, textPoints, triangleIndices, normals);
   }

   /**
    * Creates a triangle mesh for a 3D regular tetrahedron.
    * <p>
    * Its base is centered at the origin.
    * </p>
    * 
    * @param edgeLength length of the tetrahedron's edges.
    * @return the generic triangle mesh.
    */
   public static MeshDataHolder Tetrahedron(double edgeLength)
   {
      return Tetrahedron((float) edgeLength);
   }

   private static final float TETRAHEDRON_FACE_EDGE_FACE_ANGLE = (float) Math.acos(ONE_THIRD);
   private static final float TETRAHEDRON_SINE_FACE_EDGE_FACE_ANGLE = (float) Math.sin(TETRAHEDRON_FACE_EDGE_FACE_ANGLE);

   /**
    * Creates a triangle mesh for a 3D regular tetrahedron.
    * <p>
    * Its base is centered at the origin.
    * </p>
    * 
    * @param edgeLength length of the tetrahedron's edges.
    * @return the generic triangle mesh.
    */
   public static MeshDataHolder Tetrahedron(float edgeLength)
   {
      /*
       * @formatter:off
       * Base vertices ordering
       *     0
       *    / \
       *   /   \
       *  /     \
       * 2 ----- 1
       * @formatter:on
       */
      float height = THIRD_SQRT6 * edgeLength;
      float topHeight = FOURTH_SQRT6 * edgeLength;
      float baseHeight = topHeight - height;

      float halfEdgeLength = 0.5f * edgeLength;

      float cosFaceEdgeFace = ONE_THIRD;
      float sinFaceEdgeFace = TETRAHEDRON_SINE_FACE_EDGE_FACE_ANGLE;

      float cosEdgeVertexEdge = 0.5f;
      float sinEdgeVertexEdge = HALF_SQRT3;

      Point3D32 topVertex = new Point3D32(0.0f, 0.0f, topHeight);
      Point3D32 baseVertex0 = new Point3D32(edgeLength * THIRD_SQRT3, 0.0f, baseHeight);
      Point3D32 baseVertex1 = new Point3D32(-edgeLength * SIXTH_SQRT3, halfEdgeLength, baseHeight);
      Point3D32 baseVertex2 = new Point3D32(-edgeLength * SIXTH_SQRT3, -halfEdgeLength, baseHeight);

      Vector3D32 frontNormal = new Vector3D32(-sinFaceEdgeFace, 0.0f, cosFaceEdgeFace);
      Vector3D32 rightNormal = new Vector3D32(sinFaceEdgeFace * sinEdgeVertexEdge, sinFaceEdgeFace * cosEdgeVertexEdge, cosFaceEdgeFace);
      Vector3D32 leftNormal = new Vector3D32(sinFaceEdgeFace * sinEdgeVertexEdge, -sinFaceEdgeFace * cosEdgeVertexEdge, cosFaceEdgeFace);
      Vector3D32 baseNormal = new Vector3D32(0.0f, 0.0f, -1.0f);

      int numberOfVertices = 12;
      Point3D32[] vertices = new Point3D32[numberOfVertices];
      Vector3D32[] normals = new Vector3D32[numberOfVertices];
      Point2D32[] texturePoints = new Point2D32[numberOfVertices];

      // Front face
      vertices[0] = new Point3D32(baseVertex2);
      normals[0] = new Vector3D32(frontNormal);
      texturePoints[0] = new Point2D32(0.25f, 0.5f);
      vertices[1] = new Point3D32(baseVertex1);
      normals[1] = new Vector3D32(frontNormal);
      texturePoints[1] = new Point2D32(0.75f, 0.5f);
      vertices[2] = new Point3D32(topVertex);
      normals[2] = new Vector3D32(frontNormal);
      texturePoints[2] = new Point2D32(0.5f, 1.0f);

      // Right face
      vertices[3] = new Point3D32(baseVertex1);
      normals[3] = new Vector3D32(rightNormal);
      texturePoints[3] = new Point2D32(0.75f, 0.5f);
      vertices[4] = new Point3D32(baseVertex0);
      normals[4] = new Vector3D32(rightNormal);
      texturePoints[4] = new Point2D32(0.5f, 0.0f);
      vertices[5] = new Point3D32(topVertex);
      normals[5] = new Vector3D32(rightNormal);
      texturePoints[5] = new Point2D32(1.0f, 0.0f);

      // Left face
      vertices[6] = new Point3D32(baseVertex0);
      normals[6] = new Vector3D32(leftNormal);
      texturePoints[6] = new Point2D32(0.5f, 0.0f);
      vertices[7] = new Point3D32(baseVertex2);
      normals[7] = new Vector3D32(leftNormal);
      texturePoints[7] = new Point2D32(0.25f, 0.5f);
      vertices[8] = new Point3D32(topVertex);
      normals[8] = new Vector3D32(leftNormal);
      texturePoints[8] = new Point2D32(0.0f, 0.0f);

      // Bottom face
      vertices[9] = new Point3D32(baseVertex0);
      normals[9] = new Vector3D32(baseNormal);
      texturePoints[9] = new Point2D32(0.5f, 0.0f);
      vertices[10] = new Point3D32(baseVertex1);
      normals[10] = new Vector3D32(baseNormal);
      texturePoints[10] = new Point2D32(0.75f, 0.5f);
      vertices[11] = new Point3D32(baseVertex2);
      normals[11] = new Vector3D32(baseNormal);
      texturePoints[11] = new Point2D32(0.25f, 0.5f);

      int numberOfTriangles = 4;

      int[] triangleIndices = new int[3 * numberOfTriangles];
      int index = 0;
      // Front face
      triangleIndices[index++] = 0;
      triangleIndices[index++] = 2;
      triangleIndices[index++] = 1;
      // Right face
      triangleIndices[index++] = 3;
      triangleIndices[index++] = 5;
      triangleIndices[index++] = 4;
      // Left face
      triangleIndices[index++] = 6;
      triangleIndices[index++] = 8;
      triangleIndices[index++] = 7;
      // Bottom face
      triangleIndices[index++] = 9;
      triangleIndices[index++] = 11;
      triangleIndices[index++] = 10;

      return new MeshDataHolder(vertices, texturePoints, triangleIndices, normals);
   }

   /**
    * Reverses the order of the elements in the specified array.
    * <p>
    * This method runs in linear time.
    * </p>
    *
    * @param array the array whose elements are to be reversed.
    */
   public static void reverse(Object[] array)
   {
      for (int i = 0, mid = array.length >> 1, j = array.length - 1; i < mid; i++, j--)
      {
         Object oldCoefficient_i = array[i];
         array[i] = array[j];
         array[j] = oldCoefficient_i;
      }
   }
}
