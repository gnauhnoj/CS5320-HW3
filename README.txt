Team Members:
Jonathan Huang (jhh283)
Alap Parikh (akp76)

Directions:
The java source files can be found in the /src directory. Compiled and build files can be found in the /out directory. To run our code, navigate to /out/production/CS5320-HW3/ and call "java TrajDB;". This implementation follows the syntax as suggested in the assignment.

How Data is Stored:
Data is stored across 3 separate files in the /data director. The creation of a trajectory set creates 3 files - <set name>-data (data file), <set name>-map (map file), and <set name>-freespace (space file). Upon initialization, the map and space file are initialized with a header count of 0 (this initial integer value represents the number of indexes used (map file) and the number of "free spaces" (space file) available.

For all 3 files, all data is stored as binary values. The map file stores the following information: number of indices used (header integer), trajectory index, trajectory starting position (in the data file), and trajectory end position (in the data file). The data file stores trajectories as binary representations (broken down for each of the entry fields). The space file stores the count of "free space" available, free space map position, free space data start position, and free space data end position.

Insert:
When trajectories are inserted, the space file is retrieved to check if there is a continuous "free space" available to be overwritten. Regardless of whether space is available, the map file header (which counts the number of indices used) is updated and incremented by one.

  If there is no space available:
    The map file is updated by appending a new entry with the index, the data starting location, and the data end location. The trajectory values are inserted at the end of the data file.

  If there is space available:
    We examine if the corresponding map file space is available (check if the map index for the free space is not set to -1).
      If the space is not available:
        The map file has the new entry appended to the bottom of the file (as in the case of no space).
      If space is available:
        The map file is updated, overwriting the previously deleted map entry with the new information (index, start location, end location). The free space file is updated to record the free space's map index as -1.

      The data file is updated by writing the provided trajectory values into the free space as indicated in the space file.

      If the space is completely used:
        We move the last "row" of the space file into the "free space" entry we just used and truncate the whole file by one row. In affect, this deletes the "free space" from the space file.
      If the space is not completely used:
        We update the space file with the new starting position of the "free space" which we just used (thereby making the space smaller).

Deletion:
