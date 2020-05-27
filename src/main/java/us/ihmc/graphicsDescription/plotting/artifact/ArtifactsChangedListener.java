package us.ihmc.graphicsDescription.plotting.artifact;

import java.util.List;

public interface ArtifactsChangedListener
{
   public void artifactsChanged(List<Artifact> newArtifacts);
}
