plugins {
   id("us.ihmc.ihmc-build") version "0.22.0"
   id("us.ihmc.log-tools-plugin") version "0.5.0"
   id("us.ihmc.ihmc-ci") version "6.3"
   id("us.ihmc.ihmc-cd") version "1.8"
}

ihmc {
   group = "us.ihmc"
   version = "0.19.1"
   vcsUrl = "https://github.com/ihmcrobotics/ihmc-graphics-description"
   openSource = true

   configureDependencyResolution()
   configurePublications()
}

mainDependencies {
   api("net.sf.trove4j:trove4j:3.0.3")
   api("org.apache.commons:commons-lang3:3.9")

   api("us.ihmc:euclid-frame:0.15.1")
   api("us.ihmc:euclid-shape:0.15.1")
   api("us.ihmc:ihmc-commons:0.30.2")
   api("us.ihmc:ihmc-yovariables:0.9.4")
}

testDependencies {
   api("us.ihmc:ihmc-commons-testing:0.30.2")
}
