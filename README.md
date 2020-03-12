[![stability-experimental](https://img.shields.io/badge/stability-experimental-orange.svg)](https://github.com/emersion/stability-badges#experimental)

# SyPet 2.0

SyPet is a program synthesis tool for Java libraries that automatically constructs programs by 
composing APIs. SyPet 2.0 is a clean implementation of the synthesis algorithm described in the 
POPL paper: 

```
Component-Based Synthesis for Complex APIs. POPL 2017.
Yu Feng, Ruben Martins, Yuepeng Wang, Isil Dillig, Thomas W. Reps. 
```

More details about SyPet can be found at:

* Webpage: https://utopia-group.github.io/sypet/
* GitHub SyPet 1.0: https://github.com/utopia-group/sypet

**_Note_**: This project is currently being rewritten from scratch. Currently, the following 
components are implemented:

* _sypet-petrinet-lib_: Construct Petri nets from types and method signatures.

## Building SyPet from Source

Prerequisites:

* Java 8 (More recent versions are not yet supported)
* Kotlin 1.3

Build SyPet with:

```
git clone https://github.com/sat-group/sypet.git
cd sypet
./gradlew build
```

This will install Gradle if you don't have it already installed.

You can get a jar of the application with:

```
./gradlew jar
```

Tests can be run with:

```
./gradlew test
```

## Acknowledgments

SyPet 2.0 is mainly developed and maintained by Ruben Martins at CMU. We would like to thank the 
UToPiA research group lead by Isil Dillig at UT Austin and Yu Feng at UCSB (former PhD student at 
UT Austin) for their collaboration in this project. We would also like to thank all contributors 
to this project, namely, Yuepeng Wang at UT Austin, Tom Reps at UW-Madison and Kaige Liu, Anlun Xu, 
Tianlei Pan and Mayank Jain at CMU.

