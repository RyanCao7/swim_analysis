
import java.util.*;
import java.io.*;

public class SplitFiles
{
	public static void main(String[] args) throws IOException
	{
		Scanner positionReader = new Scanner(new File("smoothedPositions.txt"));
		Scanner velocityReader = new Scanner(new File("smoothedVelocities.txt"));
		Scanner accelerationReader = new Scanner(new File("smoothedAccelerations.txt"));
		Scanner dragForceReader = new Scanner(new File("smoothedDragForces.txt"));
		Scanner appliedForceReader = new Scanner(new File("smoothedAppliedForces.txt"));
		
		splitFiles(positionReader, "Positions");
		splitFiles(velocityReader, "Velocities");
		splitFiles(accelerationReader, "Accelerations");
		splitFiles(dragForceReader, "DragForces");
		splitFiles(appliedForceReader, "AppliedForces");
	}
	
	public static void splitFiles(Scanner reader, String dataSegment) throws IOException
	{
		File directory = new File(dataSegment);
		directory.mkdir();
		
		// We have to declare the lists out here...
		PrintStream[] partWriters = new PrintStream[8];
		PrintStream[] pointWriters = new PrintStream[9];
		
		// But we don't have to open up Pandora's Box!! (Aka all the files. Only ones we need)
		if(dataSegment.equals("DragForces") || dataSegment.equals("AppliedForces"))
		{
			partWriters[0] = new PrintStream(new File(directory.getAbsolutePath() + "/Head" + dataSegment + ".txt"));
			partWriters[1] = new PrintStream(new File(directory.getAbsolutePath() + "/Hand" + dataSegment + ".txt"));
			partWriters[2] = new PrintStream(new File(directory.getAbsolutePath() + "/Lower Arm" + dataSegment + ".txt"));
			partWriters[3] = new PrintStream(new File(directory.getAbsolutePath() + "/Upper Arm" + dataSegment + ".txt"));
			partWriters[4] = new PrintStream(new File(directory.getAbsolutePath() + "/Torso" + dataSegment + ".txt"));
			partWriters[5] = new PrintStream(new File(directory.getAbsolutePath() + "/Upper Leg" + dataSegment + ".txt"));
			partWriters[6] = new PrintStream(new File(directory.getAbsolutePath() + "/Lower Leg" + dataSegment + ".txt"));
			partWriters[7] = new PrintStream(new File(directory.getAbsolutePath() + "/Foot" + dataSegment + ".txt"));
		}
		else
		{
			pointWriters[0] = new PrintStream(new File(directory.getAbsolutePath() + "/Head" + dataSegment + ".txt"));
			pointWriters[1] = new PrintStream(new File(directory.getAbsolutePath() + "/Fingertip" + dataSegment + ".txt"));
			pointWriters[2] = new PrintStream(new File(directory.getAbsolutePath() + "/Wrist" + dataSegment + ".txt"));
			pointWriters[3] = new PrintStream(new File(directory.getAbsolutePath() + "/Elbow" + dataSegment + ".txt"));
			pointWriters[4] = new PrintStream(new File(directory.getAbsolutePath() + "/Shoulder" + dataSegment + ".txt"));
			pointWriters[5] = new PrintStream(new File(directory.getAbsolutePath() + "/Hip" + dataSegment + ".txt"));
			pointWriters[6] = new PrintStream(new File(directory.getAbsolutePath() + "/Knee" + dataSegment + ".txt"));
			pointWriters[7] = new PrintStream(new File(directory.getAbsolutePath() + "/Ankle" + dataSegment + ".txt"));
			pointWriters[8] = new PrintStream(new File(directory.getAbsolutePath() + "/Toe" + dataSegment + ".txt"));
		}
		
		boolean[] whichOutput = new boolean[9];
		
		while(reader.hasNextLine())
		{
			if(dataSegment.equals("DragForces") || dataSegment.equals("AppliedForces")) // Body part references, as opposed to joints
			{
				String input = reader.nextLine();
				if (input.substring(0, 4).equals("Part")) // If it's time to read in a new body part
				{
					clear(whichOutput);
					switch(input.substring(7))
					{
					case "Head":
						whichOutput[0] = true;
					break;
					case "Hand":
						whichOutput[1] = true;
					break;
					case "Lower Arm":
						whichOutput[2] = true;
					break;
					case "Upper Arm":
						whichOutput[3] = true;
					break;
					case "Torso":
						whichOutput[4] = true;
					break;
					case "Upper Leg":
						whichOutput[5] = true;
					break;
					case "Lower Leg":
						whichOutput[6] = true;
					break;
					case "Foot":
						whichOutput[7] = true;
					break;
					}
				}
				else // Otherwise just write it!
				{
					partWriters[which(whichOutput)].println(input);
					// System.out.println("Got here 1");
				}
			}
			else // Joint references, as opposed to body parts
			{	
				String input = reader.nextLine();
				if (input.substring(0, 4).equals("Part")) // If it's time to read in a new body part
				{
					clear(whichOutput);
					switch(input.substring(7))
					{
					case "Head":
						whichOutput[0] = true;
					break;
					case "Fingertips":
						whichOutput[1] = true;
					break;
					case "Wrist":
						whichOutput[2] = true;
					break;
					case "Elbow":
						whichOutput[3] = true;
					break;
					case "Shoulder":
						whichOutput[4] = true;
					break;
					case "Hip":
						whichOutput[5] = true;
					break;
					case "Knees":
						whichOutput[6] = true;
					break;
					case "Ankles":
						whichOutput[7] = true;
					break;
					case "Toes":
						whichOutput[8] = true;
					break;
					}
				}
				else // Otherwise just write it!
				{
					pointWriters[which(whichOutput)].println(input);
					// System.out.println(which(whichOutput) + ", " + input);
				}
			}
		}
		
	}
	
	// Resets a boolean array to all false
	public static void clear(boolean[] arr)
	{
		for (int i = 0; i < arr.length; i++)
		{
			arr[i] = false;
		}
	}
	
	// Gives the index for which output source to use
	public static int which(boolean[] arr)
	{
		for (int i = 0; i < arr.length; i++)
		{
			if (arr[i])
			{
				return i;
			}
		}
		return -1;
	}

}
