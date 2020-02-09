Phasma FOOD mobile application for Android
==========================================

The main objective of the PhasmaFOOD is to design and implement a multi-target food sensitive mini-portable system for on-the-spot food quality sensing and shelf-life prediction.

License: Apache Software License 2.0


About
--------


This repository stores source code of the Android mobile application for the PhasmaFOOD project solution.
The PhasmaFOOD system performs spectroscopy and photonics based sensing of parameters indicating food quality across three use cases: mycotoxin detection, food spoilage and shelf life, and food authenticity and adulteration. The system comprises smart sensing devices (NIR, UV/VIS spectrometers and CMOS camera with processing board), a dedicated mobile application and a cloud platform. It enables the collection of reference data sets or enhances existing ones. Furthermore, the system supports the training and validating of data analysis, decision making pipelines and performing food analysis. In addition, the platform is flexible and allows realization of further, aligned use cases and usage scenarios.
The PhasmaFOOD system can be used across food supply chains from farm to fork. Stakeholders may utilize the system to perform on the spot food safety and quality analysis based on the PhasmaFOOD use cases or create their own use cases based on the PhasmaFOOD technology.
The PhasmaFOOD mobile application provides has two main functions:
1. Acting as communication bridge between PhasmaFOOD embedded system (PhasmaFOOD smart sensing device) and the PhasmaFOOD cloud platform hosting the refernece datasets, cloud DB and data analysis procedures. Communication with the sensing device is achieved through Bluetooth interface while communicaiton with the cloud platform is achieved through REST API.
2. Providing the main interface for end users through which to configure the measurements process, inspect obtained measurement data, intiate data analysis (performed on the cloud side) and receive analysis results in line with one of the project scenarios. This interface also provides means for managing user's PhasmaFOOD account and pairing with PhasmaFOOD device through Bluetooth interface.

PhasmaFOOD Android app is published:

https://play.google.com/store/apps/details?id=com.vizlore.phasmafood

Source code for the PhasmaFOOD cloud platform can be found here:

https://github.com/VizLoreLabs/phasmaFoodPlatform

Interactive cloud - app REST API documentation can be found here:

https://phasma-v2.xyz/documentation/

For more information please visit:
http://www.phasmafood.eu/


This project has received funding from the European Union’s Horizon 2020 research and innovation programme under grant agreement No 732541.
