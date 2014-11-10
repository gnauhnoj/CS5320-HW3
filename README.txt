Team Members:
Jonathan Huang (jhh283)
Alap Parikh (akp76)

Directions:
The java source files can be found in the /src directory. Compiled and build files can be found in the /out directory. To run our code, navigate to /out/production/CS5320-HW3/ and call "java TrajDB;". This implementation follows the syntax as suggested in the assignment.

How Data is Stored:
Data is stored across 3 separate files in the /data director. The creation of a trajectory set creates 3 files - <set name>-data (data file), <set name>-map (map file), and <set name>-freespace (space file). Upon initialization, the map and space file are initialized with a header count of 0 (this initial integer value represents the number of indexes used (map file) and the number of "free spaces" (space file) available.

For all 3 files, all data is stored as binary values. The map file stores the following information: number of indices used (header integer), trajectory index, trajectory starting position (in the data file), and trajectory end position (in the data file). The data file stores trajectories as binary representations (broken down for each of the entry fields). The space file stores the count of "free space" available, free space map position, free space data start position, and free space data end position.

Insert:
When trajectories are inserted, the space file is retrieve to check if there is a continuous "free space" available to be overwritten.

If there is no space available:
The map file is update to increment the header -- which counts the number of indices used -- by one and the file is updated to add a new entry with the index, the starting location, and the end location (in the data file). The trajectory values are inserted at the end of the data file.

If there is space available:


map file is updated to increment the header (count of indices) by one and is updated to add an individual entry


 trajectory (in binary representation) is added to the data file with it's

Deletion:
