package us.ihmc.graphicsDescription.instructions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import us.ihmc.graphicsDescription.appearance.AppearanceDefinition;

public class Graphics3DAddModelFileInstruction extends Graphics3DInstruction
{
   private final String fileName;
   private final String submesh;
   private final boolean centerSubmesh;
   private final ArrayList<String> resourceDirectories = new ArrayList<>();
   private final ClassLoader resourceClassLoader;

   public Graphics3DAddModelFileInstruction(String fileName)
   {
      this(fileName, null);
   }

   public Graphics3DAddModelFileInstruction(String fileName, AppearanceDefinition appearance, List<String> resourceDirectories, ClassLoader resourceClassLoader)
   {
      this(fileName, null, false, appearance, resourceDirectories, resourceClassLoader);
   }

   public Graphics3DAddModelFileInstruction(String fileName, String submesh, boolean centerSubmesh, AppearanceDefinition appearance,
                                            List<String> resourceDirectories, ClassLoader resourceClassLoader)
   {
      this.fileName = fileName;
      this.submesh = submesh;
      this.centerSubmesh = centerSubmesh;
      setAppearance(appearance);
      this.resourceDirectories.addAll(resourceDirectories);
      this.resourceClassLoader = resourceClassLoader;
   }

   public Graphics3DAddModelFileInstruction(String fileName, AppearanceDefinition appearance)
   {
      this(fileName, null, false, appearance, Collections.emptyList(), null);
   }

   public String getFileName()
   {
      return fileName;
   }

   public ArrayList<String> getResourceDirectories()
   {
      return resourceDirectories;
   }

   public ClassLoader getResourceClassLoader()
   {
      return resourceClassLoader;
   }

   public String getSubmesh()
   {
      return submesh;
   }

   public boolean centerSubmesh()
   {
      return centerSubmesh;
   }

   @Override
   public String toString()
   {
      String ret = "\t\t\t<Add3DSFile>\n\t\t\t\t<Name>" + fileName + "</Name>\n";
      if (getAppearance() != null)
         ret += getAppearance().toString();
      ret += "\t\t\t</Add3DSFile>\n";
      return ret;
   }
}
