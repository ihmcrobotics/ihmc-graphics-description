package us.ihmc.graphicsDescription.instructions;

import java.util.List;

import us.ihmc.graphicsDescription.instructions.primitives.Graphics3DIdentityInstruction;
import us.ihmc.graphicsDescription.instructions.primitives.Graphics3DRotateInstruction;
import us.ihmc.graphicsDescription.instructions.primitives.Graphics3DScaleInstruction;
import us.ihmc.graphicsDescription.instructions.primitives.Graphics3DTranslateInstruction;

public abstract class Graphics3DInstructionExecutor
{
   public Graphics3DInstructionExecutor()
   {
      super();
   }

   protected void setUpGraphicsFromDefinition(List<Graphics3DPrimitiveInstruction> instructions)
   {
      for (Graphics3DPrimitiveInstruction instruction : instructions)
      {
         if (instruction instanceof GeometryGraphics3DInstruction)
         {
            GeometryGraphics3DInstruction primitiveInstruction = (GeometryGraphics3DInstruction) instruction;

            doAddGeometryInstruction(primitiveInstruction);
         }
         else if (instruction instanceof Graphics3DIdentityInstruction)
         {
            doIdentityInstruction();
         }
         else if (instruction instanceof Graphics3DRotateInstruction)
         {
            Graphics3DRotateInstruction graphics3DRotateMatrix = (Graphics3DRotateInstruction) instruction;
            doRotateInstruction(graphics3DRotateMatrix);
         }
         else if (instruction instanceof Graphics3DScaleInstruction)
         {
            Graphics3DScaleInstruction graphics3DScale = (Graphics3DScaleInstruction) instruction;
            doScaleInstruction(graphics3DScale);
         }
         else if (instruction instanceof Graphics3DTranslateInstruction)
         {
            Graphics3DTranslateInstruction graphics3DTranslate = (Graphics3DTranslateInstruction) instruction;
            doTranslateInstruction(graphics3DTranslate);
         }
         else
         {
            System.err.println("Unknown graphics3DDefinition: " + instruction.getClass().getSimpleName());
         }
      }
   }

   protected abstract void doAddGeometryInstruction(GeometryGraphics3DInstruction primitiveInstruction);

   protected abstract void doIdentityInstruction();

   protected abstract void doRotateInstruction(Graphics3DRotateInstruction graphics3DRotateMatrix);

   protected abstract void doScaleInstruction(Graphics3DScaleInstruction graphics3DScale);

   protected abstract void doTranslateInstruction(Graphics3DTranslateInstruction graphics3DTranslate);
}
