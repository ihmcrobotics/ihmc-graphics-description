package us.ihmc.graphicsDescription.instructions;

import java.util.List;

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
         else
         {
            System.err.println("Unknown graphics3DDefinition: " + instruction.getClass().getSimpleName());
         }
      }
   }

   protected abstract void doAddGeometryInstruction(GeometryGraphics3DInstruction primitiveInstruction);
}
