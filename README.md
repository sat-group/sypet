# SyPet 2.0

SyPet is a program synthesis tool for Java libraries that automatically constructs programs by composing APIs. SyPet 2.0 is a clean implementation of the synthesis algorithm described in the POPL paper: 

```
Component-Based Synthesis for Complex APIs. POPL 2017.
Yu Feng, Ruben Martins, Yuepeng Wang, Isil Dillig, Thomas W. Reps. 
```

### Features

Version 2.0 of SyPet has the following additional features (some under development):
* Atom plug-in for increased usability;
* Use of program analysis to prune equivalent infeasible programs;
* Use of machine learning to guide SyPet's search;
* Webserver version that can be deployed in any remote server;
* Improved performance by changing the underlying graph representation;
* Improved performance by caching previous Soot executions;
* Improved performance by incremental construction of the SAT encoding;
* Improved performance by running multiple instances of SyPet in parallel;
* User can give hints to the synthesizer using keywords or APIs;
* Limited support for conditionals and loops.

If you want a new feature implemented into SyPet, please create an issue with your request.

More details about SyPet can be found at:
* Webpage: https://utopia-group.github.io/sypet/
* GitHub SyPet 1.0: https://github.com/utopia-group/sypet

## Building SyPet from Source

SyPet has been tested on Linux and Mac OS environments.

Prerequisites for building SyPet:

* Java 8 (More recent versions are not yet supported)
* Ant (We are currently using version 1.10)

```
git clone https://github.com/sat-group/sypet.git
cd sypet
ant clean jar
```

### Docker

You can use docker to set up a development environment with the prerequisites
pre-installed. The provided Makefile lets you build the image, and to create,
start and stop the container:

```
make build-dev  # Build docker image
make create-dev # Create docker container
make start-dev  # Start docker container
make stop-dev   # Stop docker container
```

## Running SyPet

Having built SyPet, you can the run it with Ant:

```
ant sypet -Dargs=path/to/input/file
```

For example:

### Docker

Alternatively, you can run SyPet as a docker app:

```
# Build docker image for the SyPet app
make build-app

# Run as a docker app. <filename> is the argument to SyPet
make run-app FILE=<filename>
```

## Acknowledgments

SyPet 2.0 is mainly developed and maintained by Ruben Martins at CMU. We would like to thank the UToPiA research group lead by Isil Dillig at UT Austin and Yu Feng at UCSB (former PhD student at UT Austin) for their collaboration in this project. We would also like to thank all contributors to this project, namely, Yuepeng Wang at UT Austin, Tom Reps at UW-Madison and Kaige Liu, Anlun Xu, Tianlei Pan and Mayank Jain at CMU.

