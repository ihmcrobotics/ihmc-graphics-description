package us.ihmc.graphicsDescription.yoGraphics.plotting;

import java.util.ArrayList;
import java.util.List;

import us.ihmc.graphicsDescription.plotting.artifact.Artifact;

public class ArtifactList
{
   private String label;
   private List<Artifact> artifacts;

   public ArtifactList(String label, List<Artifact> artifacts)
   {
      // TODO: should a defensive copy be made of artifacts?
      this.label = label;
      updateLabels(artifacts);
      this.artifacts = artifacts;
   }

   public ArtifactList(String label, Artifact[] artifactArray)
   {
      this.label = label;

      List<Artifact> artifacts = new ArrayList<>(artifactArray.length);

      for (Artifact artifact : artifactArray)
      {
         artifacts.add(artifact);
      }

      this.artifacts = artifacts;
      updateLabels(artifacts);
   }

   public ArtifactList(String label, Artifact artifact)
   {
      this.label = label;
      updateLabel(artifact);

      ArrayList<Artifact> artifacts = new ArrayList<>(1);

      artifacts.add(artifact);

      this.artifacts = artifacts;
   }

   public ArtifactList(String label)
   {
      this.label = label;
      artifacts = new ArrayList<>();
   }

   private void updateLabels(List<Artifact> artifacts)
   {
      for (Artifact artifact : artifacts)
      {
         updateLabel(artifact);
      }
   }

   private void updateLabel(Artifact artifact)
   {
      artifact.setLabel(label);
   }

   public String getLabel()
   {
      return label;
   }

   public List<Artifact> getArtifacts()
   {
      return artifacts;
   }

   public void add(Artifact artifact)
   {
      updateLabel(artifact);
      artifacts.add(artifact);
   }

   public void addAll(List<Artifact> artifacts)
   {
      updateLabels(artifacts);
      this.artifacts.addAll(artifacts);
   }

   public void addArtifactsToPlotter(PlotterInterface plotter)
   {
      for (Artifact artifact : artifacts)
      {
         plotter.addArtifact(artifact);
      }
   }

   public void setVisible(boolean visible)
   {
      if (artifacts != null)
      {
         int numberOfElements = artifacts.size();

         for (int i = 0; i < numberOfElements; i++)
         {
            Artifact artifact = artifacts.get(i);
            artifact.setVisible(visible);
         }
      }
   }

   public void hideArtifacts()
   {
      if (artifacts != null)
      {
         int numberOfElements = artifacts.size();

         for (int i = 0; i < numberOfElements; i++)
         {
            Artifact artifact = artifacts.get(i);
            artifact.setVisible(false);
         }
      }
   }
}
