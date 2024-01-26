# Use the Maven image as the base image
FROM maven:3.6-openjdk-11

# Install Python 3.7 and pip
RUN apt-get update && \
    apt-get install -y python3.7 python3-pip git-all && \
    rm -rf /var/lib/apt/lists/*

# Create a symbolic link for pip3
RUN ln -s /usr/bin/pip3 /usr/bin/pip

# Set the working directory in the image
WORKDIR /usr/src/app

RUN mkdir -p pre-trained/codebert-base-mlm

# Copy everything from the current directory to the working directory
COPY . .

# Display installed Python and pip versions
RUN python3 --version && pip --version

RUN python3 download-codebert.py

RUN mvn clean install -DskipTests=true

RUN pip install --upgrade pip

RUN pip3 install torch
RUN pip3 install transformers

# Define an environment variables for targeted project root and targeted directories to mutate
ENV TARGET_PROJECT_EXE_ROOT /tmp/target_project_test
ENV LOOKFOR_SOURCE_FILES /tmp/target_project_test/src

# Export environment variables
ENV BASEDIR=$(pwd)

ENV ANT_HOME=./envs/ant/
ENV LIB_HOME=./envs/lib/
ENV MVN_REPO=/root/.m2/repository
ENV CODEBERT_MUT_PATH="$MVN_REPO/fr/inria/gforge/spoon/spoon-core/8.2.0/spoon-core-8.2.0.jar:$MVN_REPO/org/eclipse/jdt/org.eclipse.jdt.core/3.20.0/org.eclipse.jdt.core-3.20.0.jar:$MVN_REPO/com/martiansoftware/jsap/2.1/jsap-2.1.jar:$MVN_REPO/org/apache/logging/log4j/log4j-api/2.13.3/log4j-api-2.13.3.jar:$MVN_REPO/org/apache/logging/log4j/log4j-to-slf4j/2.13.3/log4j-to-slf4j-2.13.3.jar:$MVN_REPO/org/slf4j/slf4j-api/1.7.25/slf4j-api-1.7.25.jar:$MVN_REPO/commons-io/commons-io/2.7/commons-io-2.7.jar:$MVN_REPO/org/apache/maven/maven-model/3.6.3/maven-model-3.6.3.jar:$MVN_REPO/org/codehaus/plexus/plexus-utils/3.2.1/plexus-utils-3.2.1.jar:$MVN_REPO/org/apache/commons/commons-lang3/3.11/commons-lang3-3.11.jar:$MVN_REPO/org/tukaani/xz/1.8/xz-1.8.jar:$MVN_REPO/com/fasterxml/jackson/core/jackson-databind/2.11.1/jackson-databind-2.11.1.jar:$MVN_REPO/com/fasterxml/jackson/core/jackson-annotations/2.11.1/jackson-annotations-2.11.1.jar:$MVN_REPO/com/fasterxml/jackson/core/jackson-core/2.11.1/jackson-core-2.11.1.jar:$MVN_REPO/org/apache/commons/commons-compress/1.20/commons-compress-1.20.jar:$MVN_REPO/org/apache/maven/shared/maven-invoker/3.0.1/maven-invoker-3.0.1.jar:$MVN_REPO/org/apache/maven/shared/maven-shared-utils/3.2.1/maven-shared-utils-3.2.1.jar:$MVN_REPO/org/codehaus/plexus/plexus-component-annotations/1.7.1/plexus-component-annotations-1.7.1.jar:$MVN_REPO/org/mdkt/compiler/InMemoryJavaCompiler/1.3.0/InMemoryJavaCompiler-1.3.0.jar:$MVN_REPO/org/slf4j/slf4j-api/1.7.5/slf4j-api-1.7.5.jar:$MVN_REPO/org/eclipse/jdt/org.eclipse.jdt.core/3.15.0/org.eclipse.jdt.core-3.15.0.jar:$MVN_REPO/org/eclipse/platform/org.eclipse.core.resources/3.15.0/org.eclipse.core.resources-3.15.0.jar:$MVN_REPO/org/eclipse/platform/org.eclipse.core.expressions/3.7.100/org.eclipse.core.expressions-3.7.100.jar:$MVN_REPO/org/eclipse/platform/org.eclipse.core.runtime/3.22.0/org.eclipse.core.runtime-3.22.0.jar:$MVN_REPO/org/eclipse/platform/org.eclipse.osgi/3.16.300/org.eclipse.osgi-3.16.300.jar:$MVN_REPO/org/eclipse/platform/org.eclipse.equinox.common/3.15.0/org.eclipse.equinox.common-3.15.0.jar:$MVN_REPO/org/eclipse/platform/org.eclipse.core.jobs/3.11.0/org.eclipse.core.jobs-3.11.0.jar:$MVN_REPO/org/eclipse/platform/org.eclipse.equinox.registry/3.10.200/org.eclipse.equinox.registry-3.10.200.jar:$MVN_REPO/org/eclipse/platform/org.eclipse.equinox.preferences/3.8.200/org.eclipse.equinox.preferences-3.8.200.jar:$MVN_REPO/org/eclipse/platform/org.eclipse.core.contenttype/3.7.1000/org.eclipse.core.contenttype-3.7.1000.jar:$MVN_REPO/org/eclipse/platform/org.eclipse.equinox.app/1.5.100/org.eclipse.equinox.app-1.5.100.jar:$MVN_REPO/org/eclipse/platform/org.eclipse.core.filesystem/1.9.0/org.eclipse.core.filesystem-1.9.0.jar:$MVN_REPO/org/eclipse/platform/org.eclipse.text/3.12.0/org.eclipse.text-3.12.0.jar:$MVN_REPO/org/eclipse/platform/org.eclipse.core.commands/3.10.0/org.eclipse.core.commands-3.10.0.jar:$MVN_REPO/log4j/log4j/1.2.17/log4j-1.2.17.jar:$MVN_REPO/commons-io/commons-io/2.6/commons-io-2.6.jar:$MVN_REPO/org/apache/maven/maven-model/3.5.0/maven-model-3.5.0.jar:$MVN_REPO/org/codehaus/plexus/plexus-utils/3.0.24/plexus-utils-3.0.24.jar:$MVN_REPO/org/apache/commons/commons-lang3/3.8.1/commons-lang3-3.8.1.jar:$MVN_REPO/com/fasterxml/jackson/core/jackson-databind/2.9.8/jackson-databind-2.9.8.jar:$MVN_REPO/com/fasterxml/jackson/core/jackson-annotations/2.9.0/jackson-annotations-2.9.0.jar:$MVN_REPO/com/fasterxml/jackson/core/jackson-core/2.9.8/jackson-core-2.9.8.jar:$MVN_REPO/org/apache/commons/commons-compress/1.18/commons-compress-1.18.jar:$MVN_REPO/junit/junit/4.12/junit-4.12.jar:$MVN_REPO/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar:$MVN_REPO/org/json/json/20200518/json-20200518.jar"
ENV PATH=$JAVA_HOME/bin:$ANT_HOME/bin:$LIB_HOME:$MVN_REPO:$CODEBERT_MUT_PATH:$PATH

# Specify the command to run your application
# CMD ["python3", "run-codebert.py", "int <mask> = 0;"]

# CMD [ "/bin/bash" ]
CMD [ "sh", "docker_run.sh" ]
