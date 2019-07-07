# iRemote

Soha Glal and Ashraf Khalil

suha.glal@gmail.com,ashraf.khalil@adu.ac.ae

Abu Dhabi University

This project was under the supervison of Dr. Ashraf Khalil


##   Introduction
Gesture-based interaction is a highly useful technique for computer interaction. Gesture-based interaction falls under the category of natural user interface (NUI).The use of gesture-based interaction has been heavily used in large screen display manipulation as well as gaming market [1]. We predict that Gesture-based interaction is the future of computer interaction, but a lot needs to be done to design robust interfaces adapted to this new medium and specify the kind of applications that benefit the most from this type of interaction.
Our project employs mobile phones with accelerometers to capture human gestures. The achievement of this project is twofold: firstly, we have built a mobile recognition engine to interpret the captured gestures, secondly; we have built a mobile application on the top of the gesture recognition engine to demonstrate the gesture interaction. The mobile application allows the user to control a power point presentation by just moving his hand that carries the mobile phone. A unique feature of the gesture engine is that it is lightweight enough to run robustly on mobile phones. The principle behind the gesture recognition engine is that human gestures can be characterized by the time series of acceleration. Therefore, our recognition engine bases recognition on the matching of two time series of acceleration, measured by a single three-axis accelerometer. Nowadays most smart phones come equipped with accelerometer sensors.

## System Design and Implementation 

In this section, we present the key technical components of the project: acceleration daemon, recognition engine, iRemote application. Figure 1 illustrates the major components of our system.

![Alt text](/img/picture1.png?raw=true "Figure 1 System architecture")

### 2.1 The Acceleration Daemon
To make the application portable, whenever possible we used J2ME (Java 2 Platform, Micro Edition). The acceleration daemon is the first component of our project. Due to the fact that Nokai N95 8gb does not support java Mobile Sensor API, we had to build a Symbian C++ daemon to get the acceleration data from N95 8gb internal accelerometer sensor. The daemon is build using the mobile native language and using sensor API. The daemon runs all the time in the background and it wait until the midlet send a signal to starts reading the acceleration data. The daemon keeps sending the acceleration data until it receives a stop signal from the midlet.

### 2.2 The Recognition Engine
For recognition, we employ the template library. Template library is used for training the classification algorithm. The input to the recognition engine is a time series of acceleration provided by a three-axis accelerometer. Each time sample is a vector of three elements, corresponding to the acceleration along the three axes. The same template processing (filtering plus quantization) will be applied to the input gesture too. After that, we employed dynamic time warping (DTW) classification algorithm. DTW is an algorithm for measuring similarity between two sequences which may vary in time or speed [2]. Special characteristic of DTW is that it is a light weight algorithm and more convenient for mobiles. The recognition engine recognizes the gesture based on the template that provides the best matching. 

### 2.3 iRemote
We build an application on top of the recognition engine to demonstrate the use of gesture-based interaction. iRemote allows the mobile phone to control a power point presentation by gesture-based interaction. The application consists of two parts server and client. The server is a desktop application that listens for incoming Bluetooth connection from the clients. It then allows multiple users to manipulate the same power point slide show. The client side is a mobile application that enables the user to create a gesture library (template) to be used to control the power point presentation. After filling in the library, the user can manipulate the power point presentation by using his/her own gestures.

## 3.    Conclusion
The main contribution of this project is the realization of gesture-recognition engine that is efficient enough to run on mobile devices. Testing the engine showed that the average recognition accuracy is around 90%. The use of the gesture recognition engine was demonstrated by building a mobile application that controls the power point presentation by using gestures. 

## References
[1] T. Vajk, W. Bamford, P. Coulton, and R. Edwards, “Using a mobile phone as a ‘Wii like’ controller,” in Proceedings of the 3rd International Conference on Games Research and Development, Manchester, UK, September 2007.
[2] Wikipedia.http://en.wikipedia.org/wiki/Dynamic_time_warping

