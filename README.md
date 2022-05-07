# Fuzzy Intelligent System for the Evaluation of Technological Risks

> This project was part of my undergraduate thesis in 2014, while this repository serves only as a
> historical archive and will never be updated.

## Abstract

SIDCIRT (*Sistema inteligente difuso para la evaluación de riesgos tecnológicos* in Spanish) is a fuzzy intelligent system for the evaluation of technological risks, a desktop application as the final product of my bachelor thesis in 2014.
It is developed in Java and using the [Xfuzzy 3.0](http://www2.imse-cnm.csic.es/Xfuzzy/Xfuzzy_3.5/index.html) library for modeling of six Inference Fuzzy Systems of type Mamdani to determine the parameters that compute the index of technological risk with the corresponding adjustments in the defuzzification process. The need to use a fuzzy model stems from the presence of uncertainty, inaccuracy or subjectivity in the parameters that determine the technological risk index, which are: Impact Aggravation Coefficient, Physical Vulnerability and Frequency. 
The work was carried out jointly and under the supervision of a multidisciplinary team of chemical engineers and environmental engineers with the aim of automating the technological risk management process and decision making in the event of a disaster in a chemical industry.

## Prerequisites

- [JDK](https://jdk.java.net) (Java Development Kit).
- [Apache Ant](https://ant.apache.org/manual/install.html) build system.
- [NetBeans](https://netbeans.apache.org/download/index.html) IDE (*Optional*).

## Build and run

Since the source code is a NetBeans project, it might be easier to build using this IDE. Otherwise we could use the Apache Ant build tool from the command line as follows

```bash
# Compiles the project and builds a JAR (located in dist/)
ant jar
# Executes the JAR
java -jar dist/SIDCIRT.jar
```