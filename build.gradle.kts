plugins {
   id("us.ihmc.ihmc-build")
   id("us.ihmc.log-tools-plugin") version "0.5.0"
   id("us.ihmc.ihmc-ci") version "7.4"
   id("us.ihmc.ihmc-cd") version "1.17"
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
   api("org.apache.commons:commons-lang3:3.11")

   api("us.ihmc:euclid-frame:0.15.2")
   api("us.ihmc:euclid-shape:0.15.2")
   api("us.ihmc:ihmc-commons:0.30.4")
   api("us.ihmc:ihmc-yovariables:0.9.7")
}

testDependencies {
   api("us.ihmc:ihmc-commons-testing:0.30.4")
}
