package us.ihmc.graphicsDescription.yoGraphics;

import static us.ihmc.robotics.Assert.assertArrayEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;

import us.ihmc.euclid.referenceFrame.ReferenceFrame;
import us.ihmc.graphicsDescription.yoGraphics.YoGraphicPolynomial3D.Polynomial3DVariables;
import us.ihmc.graphicsDescription.yoGraphics.YoGraphicPolynomial3D.PolynomialVariables;
import us.ihmc.yoVariables.euclid.referenceFrame.YoFramePose3D;
import us.ihmc.yoVariables.registry.YoRegistry;
import us.ihmc.yoVariables.variable.YoDouble;
import us.ihmc.yoVariables.variable.YoVariable;

public class YoGraphicPolynomial3DTest
{
   @Test
   public void testRemoteYoGraphicVariableOrdering()
   {
      Random random = new Random(345345L);

      for (int iteration = 0; iteration < 10; iteration++)
      {
         String name = "writer";
         YoRegistry registry = new YoRegistry("writerRegistry");
         int numberOfPolynomials = random.nextInt(20) + 3;

         YoFramePose3D poseToPolynomialFrame = new YoFramePose3D(name + "Pose", ReferenceFrame.getWorldFrame(), registry);

         List<Polynomial3DVariables> yoPolynomial3Ds = new ArrayList<>();
         List<YoDouble> waypointTimes = new ArrayList<>();

         for (int i = 0; i < numberOfPolynomials; i++)
         {
            PolynomialVariables xPolynomial = new PolynomialVariables(name + "XPoly" + i, random.nextInt(20) + 1, registry);
            PolynomialVariables yPolynomial = new PolynomialVariables(name + "YPoly" + i, random.nextInt(20) + 1, registry);
            PolynomialVariables zPolynomial = new PolynomialVariables(name + "ZPoly" + i, random.nextInt(20) + 1, registry);
            yoPolynomial3Ds.add(new Polynomial3DVariables(xPolynomial, yPolynomial, zPolynomial));
            waypointTimes.add(new YoDouble(name + "WaypointTime" + i, registry));
         }

         double radius = random.nextDouble();
         int resolution = random.nextInt(50);
         int radialResolution = random.nextInt(50);
         YoGraphicPolynomial3D yoGraphicWriter = new YoGraphicPolynomial3D(name,
                                                                           poseToPolynomialFrame,
                                                                           yoPolynomial3Ds,
                                                                           waypointTimes,
                                                                           radius,
                                                                           resolution,
                                                                           radialResolution,
                                                                           registry);

         YoVariable[] allWriterYoVariables = yoGraphicWriter.getVariables();
         double[] allWriterConstants = new double[yoGraphicWriter.getConstants().length];
         for (int i = 0; i < yoGraphicWriter.getConstants().length; i++)
            allWriterConstants[i] = yoGraphicWriter.getConstants()[i];

         YoGraphicPolynomial3D yoGraphicReader = YoGraphicPolynomial3D.createAsRemoteYoGraphic("reader", allWriterYoVariables, allWriterConstants);
         YoVariable[] allReaderYoVariables = yoGraphicReader.getVariables();
         double[] allReaderConstants = new double[yoGraphicReader.getConstants().length];
         for (int i = 0; i < yoGraphicReader.getConstants().length; i++)
            allReaderConstants[i] = yoGraphicReader.getConstants()[i];

         assertArrayEquals(allWriterYoVariables, allReaderYoVariables);
         assertArrayEquals(allWriterConstants, allReaderConstants, 1e-7);
      }
   }
}
