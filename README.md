# COP4520-HW3-Problem2-KieranJimenez
## Problem 2: Atmospheric Temperature Reading Module (50 points) 
You are tasked with the design of the module responsible for measuring the atmospheric
temperature of the next generation Mars Rover, equipped with a multicore CPU and 8
temperature sensors. The sensors are responsible for collecting temperature readings at
regular intervals and storing them in shared memory space. The atmospheric
temperature module has to compile a report at the end of every hour, comprising the top
5 highest temperatures recorded for that hour, the top 5 lowest temperatures recorded
for that hour, and the 10-minute interval of time when the largest temperature
difference was observed. The data storage and retrieval of the shared memory region
must be carefully handled, as we do not want to delay a sensor and miss the interval of
time when it is supposed to conduct temperature reading. Design and implement a
solution using 8 threads that will offer a solution for this task. Assume that the
temperature readings are taken every 1 minute. In your solution, simulate the operation
of the temperature reading sensor by generating a random number from -100F to 70F at
every reading. In your report, discuss the efficiency, correctness, and progress guarantee
of your program. 

## Solution:
Unfortunately I found this problem a little unclear, but I believe I have a proper solution. 
I started by having each of the 8 cores given an ID and making each in charge of 1 of the 8 
sensors. Then I just had them "record" a temperature every minute. (Attempting a short wait 
for each thread to closer simulate the 1 minute wait was stopped by an IllegalMonitorStateException 
that I couldn't figure out. As far as the shared memory space, I just used a large int array.
I made sure there was no overlap by controlling which elements a thread could access with its
given ID. This was especially nice as there was no waiting on threads.
Compiling the report was a little slower since I dropped down to 3 working threads, but 
splitting up the sorting and calculation work the little I did should still help it speed up.
