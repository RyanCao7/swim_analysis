
import java.io.*;
import java.util.*;

public class CoordinateProcess
{
	public static void main(String[] args) throws IOException
	{
		// Each frame takes 0.04 seconds
		PrintStream smoothTwoP = new PrintStream(new File("smoothedPositions.txt"));
		PrintStream smoothTwoV = new PrintStream(new File("smoothedVelocities.txt"));
		PrintStream smoothTwoA = new PrintStream(new File("smoothedAccelerations.txt"));
		PrintStream smoothTwoF = new PrintStream(new File("smoothedDragForces.txt"));
		PrintStream smoothTwoAF = new PrintStream(new File("smoothedAppliedForces.txt"));
		
		Scanner input = new Scanner(System.in);
		System.out.print("Please enter the name of the file you would like to read in: ");
		String inputFile = input.nextLine();
		input.close();
		
		Scanner readPosition = new Scanner(new File(inputFile));
		double convertPixelsToMeters = 299.45;
		
		// Array of lengths (meters). Order goes: head/hand/lower arm/upper arm/torso/upper leg/lower leg/foot
		double[] lengths = {0.2286, 0.193802, 0.269748, 0.340868, 0.6604, 0.508, 0.36449, 0.249936};
		
		// Array of masses (kg). Order goes: head/hand/lower arm/upper arm/torso/upper leg/lower leg/foot
		double[] masses = {5.8911, 0.53262, 1.2912, 2.1789, 40.9956, 7.9732, 3.753, 1.17015};
		
		// Array of drag coefficients. Order goes: head/hand/lower arm/upper arm/torso/upper leg/lower leg/foot
		double[] dragCfs = {0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5};
		
		// 2-D ArrayList of applied forces. Order goes: head/hand/lower arm/upper arm/torso/upper leg/lower leg/foot
		ArrayList<ArrayList<Vector>> appliedForces = new ArrayList<ArrayList<Vector>>();
		for (int i = 0; i < 8; i++)
		{
			appliedForces.add(new ArrayList<Vector>());
		}
		
		// 2-D ArrayList of drag force magnitudes. Order goes: head/hand/lower arm/upper arm/torso/upper leg/lower leg/foot
		ArrayList<ArrayList<Vector>> smoothAppliedForces = new ArrayList<ArrayList<Vector>>();
		for (int i = 0; i < 8; i++)
		{
			smoothAppliedForces.add(new ArrayList<Vector>());
		}
		
		// 2-D ArrayList of force vectors (in n-direction). Order goes: head/hand/lower arm/upper arm/torso/upper leg/lower leg/foot
		ArrayList<ArrayList<Vector>> forceVectors = new ArrayList<ArrayList<Vector>>();
		for (int i = 0; i < 8; i++)
		{
			forceVectors.add(new ArrayList<Vector>());
		}
		
		// 2-D ArrayList of force vectors (in n-direction). Order goes: head/hand/lower arm/upper arm/torso/upper leg/lower leg/foot
		ArrayList<ArrayList<Vector>> smoothedForces = new ArrayList<ArrayList<Vector>>();
		for (int i = 0; i < 8; i++)
		{
			smoothedForces.add(new ArrayList<Vector>());
		}
		
		// 2-D ArrayList of body part average accelerations. Order goes: head/hand/forearm/upper arm/torso/thigh/lower leg/foot
		ArrayList<ArrayList<Vector>> bodyPartAccel = new ArrayList<ArrayList<Vector>>();
		for (int i = 0; i < 8; i++)
		{
			bodyPartAccel.add(new ArrayList<Vector>());
		}
		
		// 2-D ArrayList of positions. Order goes: head/fingertip/wrist/elbow/shoulder/hip/knee/ankle/toe
		ArrayList<ArrayList<Vector>> positions = new ArrayList<ArrayList<Vector>>();
		for (int i = 0; i < 9; i++)
		{
			positions.add(new ArrayList<Vector>());
		}
		
		// 2-D ArrayList of positions. Order goes: head/fingertip/wrist/elbow/shoulder/hip/knee/ankle/toe
		ArrayList<ArrayList<Vector>> smoothPositions = new ArrayList<ArrayList<Vector>>();
		for (int i = 0; i < 9; i++)
		{
			smoothPositions.add(new ArrayList<Vector>());
		}
		
		// 2-D ArrayList of velocities. Order goes: head/fingertip/wrist/elbow/shoulder/hip/knee/ankle/toe
		ArrayList<ArrayList<Vector>> velocities = new ArrayList<ArrayList<Vector>>();
		for (int i = 0; i < 9; i++)
		{
			velocities.add(new ArrayList<Vector>());
		}
		
		// 2-D ArrayList of velocities. Order goes: head/fingertip/wrist/elbow/shoulder/hip/knee/ankle/toe
		ArrayList<ArrayList<Vector>> smoothVelocitiesTwo = new ArrayList<ArrayList<Vector>>();
		for (int i = 0; i < 9; i++)
		{
			smoothVelocitiesTwo.add(new ArrayList<Vector>());
		}
		
		// 2-D ArrayList of accelerations. Order goes: head/fingertip/wrist/elbow/shoulder/hip/knee/ankle/toe
		ArrayList<ArrayList<Vector>> accelerations = new ArrayList<ArrayList<Vector>>();
		for (int i = 0; i < 9; i++)
		{
			accelerations.add(new ArrayList<Vector>());
		}
		
		// 2-D ArrayList of accelerations. Order goes: head/fingertip/wrist/elbow/shoulder/hip/knee/ankle/toe
		ArrayList<ArrayList<Vector>> smoothAccelerationsTwo = new ArrayList<ArrayList<Vector>>();
		for (int i = 0; i < 9; i++)
		{
			smoothAccelerationsTwo.add(new ArrayList<Vector>());
		}
		
		// Reading in the positions
		readInPositions(positions, convertPixelsToMeters, readPosition);
		
		// Smoothing out the positions
		smoothTwice(positions, smoothPositions);
		
		// Calculating velocities
		for (int i = 0; i < smoothPositions.size(); i++)
		{
			ArrayList<Vector> points = smoothPositions.get(i);
			for (int j = 1; j < points.size() - 1; j++)
			{
				velocities.get(i).add(new Vector(points.get(j - 1), points.get(j + 1), true));
			}
		}
		
		// Smoothing out the velocity curve using weighted averages.
		smoothTwice(velocities, smoothVelocitiesTwo);

		// Calculating accelerations. Let's try this using the smoothed velocity data!
		for (int i = 0; i < smoothVelocitiesTwo.size(); i++)
		{
			ArrayList<Vector> velocity = smoothVelocitiesTwo.get(i);
			for (int j = 1; j < velocity.size() - 1; j++)
			{
				accelerations.get(i).add(new Vector(velocity.get(j - 1), velocity.get(j + 1), false));
			}
		}
		
		// Smoothing out acceleration curve.
		smoothTwice(accelerations, smoothAccelerationsTwo);
		
		// Calculating average acceleration for head
		for (int i = 0; i < smoothAccelerationsTwo.get(0).size(); i++)
		{
			Vector endOne = smoothAccelerationsTwo.get(0).get(i);
			Vector endTwo = smoothAccelerationsTwo.get(4).get(i);
			bodyPartAccel.get(0).add(new Vector((endOne.x() + endTwo.x()) / 2, (endOne.y() + endTwo.y()) / 2));
		}
		
		// Calculating average accelerations for body parts
		for (int i = 1; i < smoothAccelerationsTwo.size() - 1; i++)
		{
			ArrayList<Vector> endOneList = smoothAccelerationsTwo.get(i);
			ArrayList<Vector> endTwoList = smoothAccelerationsTwo.get(i + 1);
			
			for (int j = 0; j < endOneList.size(); j++)
			{
				Vector endOne = endOneList.get(j);
				Vector endTwo = endTwoList.get(j);
				bodyPartAccel.get(i).add(new Vector((endOne.x() + endTwo.x()) / 2, (endOne.y() + endTwo.y()) / 2));
			}
		}
		
		// Calculates all forces
		calcAllForces(smoothVelocitiesTwo, smoothPositions, lengths, forceVectors, dragCfs);
		
		// Smooth out forces, too!
		smoothTwice(forceVectors, smoothedForces);
		
		// Calculates applied forces
		for (int i = 0; i < smoothedForces.get(0).size() - 2; i++) // Loops through all the frames in terms of the force/velocity indexes
		{
			Vector externalForce;
			
			for (int j = 0; j < 4; j++)
			{
				if (j == 0 || j == 1)
				{
					externalForce = new Vector(0, 0);
					//System.out.println("here!");
				}
				else
				{
					externalForce = appliedForces.get(j - 1).get(i);
				}
				
				Vector internalForce = calcInternalForce(i, j, smoothedForces, bodyPartAccel, masses, externalForce);
				appliedForces.get(j).add(internalForce);
			}
			
			for (int j = 7; j > 3; j--)
			{
				if (j == 7)
				{
					externalForce = new Vector(0, 0);
				}
				else
				{
					externalForce = appliedForces.get(j + 1).get(i);
				}
				Vector internalForce = calcInternalForce(i, j, smoothedForces, bodyPartAccel, masses, externalForce);
				appliedForces.get(j).add(internalForce);
			}
		}
		
		smoothCurve(appliedForces, smoothAppliedForces);
		
		/* Displaying stuff here */
		
		writeToFile(smoothPositions, smoothTwoP, false, false);
		
		writeToFile(smoothVelocitiesTwo, smoothTwoV, true, false);
		
		writeToFile(smoothAccelerationsTwo, smoothTwoA, true, false);
		
		writeToFile(smoothedForces, smoothTwoF, true, true);
		
		writeToFile(smoothAppliedForces, smoothTwoAF, true, true);
		
		System.out.println("Got here");
	} // End of main

//===========================================================================================================================================//
	
	public static String getBodyJoint(int i)
	{
		switch(i)
		{
		case 0: return "Head";
		case 1: return "Fingertips";
		case 2: return "Wrist";
		case 3: return "Elbow";
		case 4: return "Shoulder";
		case 5: return "Hip";
		case 6: return "Knees";
		case 7: return "Ankles";
		case 8: return "Toes";
		}
		
		return "N/A";
	}
	
	public static String getBodyPart(int i)
	{
		switch(i)
		{
		case 0: return "Head";
		case 1: return "Hand";
		case 2: return "Lower Arm";
		case 3: return "Upper Arm";
		case 4: return "Torso";
		case 5: return "Upper Leg";
		case 6: return "Lower Leg";
		case 7: return "Foot";
		}
		
		return "N/A";
	}
	
	public static void line(PrintStream p)
	{
		System.out.println("==================================================");
		p.println("==================================================");
	}
	
	/* 
	 * Takes the magnitudes of the n direction vectors of each end of a body segment,
	 * and the length of that body segment, and calculates the net drag force magnitude on it.
	 */
	public static double calculateForce(double nV1, double nV2, double length, Vector origin, Vector insertion, double dragC)
	{
		double rho = 1000;
		
		dragC = getDragC(origin, insertion, dragC);
		
		double rotationPoint = (-nV1 * length / (nV2 - nV1));
		if (rotationPoint > length || rotationPoint < -length) // If the rotation point is outside of the body
		{
			return sign(nV1) * (length / 6 * dragC * rho * (Math.pow(nV1, 2) + Math.pow(nV2,  2) + nV1 * nV2));
		}
		else // If the rotation point is within the body
		{
			return sign(nV2 - nV1) * ((length / 3.0) * dragC * rho * 
					   ((2 * Math.pow(nV1, 3)) / (nV1 - nV2) - nV1 * nV1 + nV2 * nV2 + nV1 * nV2));
		}
	}
	
	/*
	 * Takes the ratio of body part sticking out of the water, and
	 * multiplies that by the current drag coefficient. Density of air
	 * is 1/1000 times density of water.
	 */
	public static double getDragC(Vector origin, Vector insertion, double oldDrag)
	{
		double originY = origin.y();
		double insertionY = insertion.y();
		
		if (originY < 0 && insertionY < 0)
		{
			return oldDrag;
		}
		else if (originY > 0 && insertionY < 0)
		{
			return ((0 - insertionY) / (originY - insertionY)) * oldDrag;
		}
		else if (insertionY > 0 && originY < 0)
		{
			return ((0 - originY) / (insertionY - originY)) * oldDrag;
		}
		else
		{
			return 0.001 * oldDrag;
		}
	}
	
	/*
	 * Returns the sign of a number. 1 for positive; -1 for negative.
	 * 
	 */
	public static int sign(double num)
	{
		if (num < 0)
		{
			return -1;
		}
		return 1;
	}
	
	/*
	 * Simple write command to print out all the elements in a list to a file.
	 * 
	 */
	public static void write(ArrayList list, PrintStream p)
	{
		for (int j = 0; j < list.size(); j++)
		{
			p.println(list.get(j));
		}
	}
	
	public static void writeMagnitudes(ArrayList<Vector> list, PrintStream p)
	{
		for (int i = 0; i < list.size(); i++)
		{
			p.println(list.get(i).magnitude());
			/* if (list.get(i).x() < 0)
			{
				p.println(list.get(i).magnitude() * -1);
			}
			else
			{
				p.println(list.get(i).magnitude());
			} */
		}
	}
	
	public static void write(double[] list, PrintStream p)
	{
		for (int j = 0; j < list.length; j++)
		{
			p.println(list[j]);
		}
	}
	
	/*
	 * Returns a vector as the sum of the two vectors input.
	 * 
	 */
	public static Vector addVector(Vector v1, Vector v2)
	{
		return new Vector(v1.x() + v2.x(), v1.y() + v2.y());
	}
	
	/*
	 * Returns a vector as the sum of the three vectors input.
	 * 
	 */
	public static Vector addVector(Vector v1, Vector v2, Vector v3)
	{
		return new Vector(v1.x() + v2.x() + v3.x(), v1.y() + v2.y() + v3.y());
	}
	
	/*
	 * Returns a vector as the product of the constant and the given vector.
	 * 
	 */
	public static Vector multiplyVector(Vector v1, double k)
	{
		return new Vector(v1.x() * k, v1.y() * k);
	}
	
	public static Vector calcInternalForce(int frame, int bodyPart, ArrayList<ArrayList<Vector>> forceVectors, ArrayList<ArrayList<Vector>> bodyPartAccel, double[] masses, Vector externalForce)
	{
		// Head internal force calculation. Formula is F(internal) = -(ma + F(drag) - F(external))
		externalForce = multiplyVector(externalForce, -1);
		Vector dragVector = forceVectors.get(bodyPart).get(frame + 1);
		Vector totalAccel = bodyPartAccel.get(bodyPart).get(frame);
		double mass = masses[bodyPart];
		
		Vector ma = multiplyVector(totalAccel, mass);
		
		Vector internalForce = multiplyVector(addVector(ma, dragVector, externalForce), -1);
		return internalForce;
	}
	
	/*
	 * Smoothes out a 2-D ArrayList of Vectors, by taking a weighted average
	 * of the x- and y-components of the vectors.
	 */
	public static void smoothCurve(ArrayList<ArrayList<Vector>> original, ArrayList<ArrayList<Vector>> smoothed)
	{
		for (int i = 0; i < original.size(); i++)
		{
			ArrayList<Vector> bodyPartVelocity = original.get(i);
			ArrayList<Vector> smoothedPartVelocity = smoothed.get(i);
			smoothedPartVelocity.add(multiplyVector(addVector(bodyPartVelocity.get(0), bodyPartVelocity.get(0), bodyPartVelocity.get(1)), 1.0 / 3.0));
			int j = 1;
			for (; j < bodyPartVelocity.size() - 1; j++)
			{
				Vector adjacents = addVector(bodyPartVelocity.get(j - 1), bodyPartVelocity.get(j + 1));
				Vector theOne = multiplyVector(bodyPartVelocity.get(j), 2);
				smoothedPartVelocity.add(multiplyVector(addVector(adjacents, theOne), 0.25));
			}
			smoothedPartVelocity.add(multiplyVector(addVector(bodyPartVelocity.get(j), bodyPartVelocity.get(j), bodyPartVelocity.get(j - 1)), 1.0 / 3.0));
		}
	}
	
	public static void smoothTwice(ArrayList<ArrayList<Vector>> original, ArrayList<ArrayList<Vector>> twiceSmoothed)
	{
		ArrayList<ArrayList<Vector>> onceSmoothed = new ArrayList<ArrayList<Vector>>();
		for (int i = 0; i < original.size(); i++)
		{
			onceSmoothed.add(new ArrayList<Vector>());
		}
		
		smoothCurve(original, onceSmoothed);
		smoothCurve(onceSmoothed, twiceSmoothed);
	}
	
	public static void writeToFile(ArrayList<ArrayList<Vector>> list, PrintStream p, boolean magnitude, boolean bodyPart)
	{
		if (magnitude)
		{
			for (int i = 0; i < list.size(); i++)
			{
				if (bodyPart)
				{
					p.println("Part - " + getBodyPart(i));
				}
				else
				{
					p.println("Part - " + getBodyJoint(i));
				}
				writeMagnitudes(list.get(i), p);
			}
		}
		else
		{
			for (int i = 0; i < list.size(); i++)
			{
				if (bodyPart)
				{
					p.println("Part - " + getBodyPart(i));
				}
				else
				{
					p.println("Part - " + getBodyJoint(i));
				}
				write(list.get(i), p);
			}
		}

	}
	
	public static void calcAllForces(ArrayList<ArrayList<Vector>> velocityList, ArrayList<ArrayList<Vector>> positionList, double[] lengths, ArrayList<ArrayList<Vector>> forceList, double[] dragCfs)
	{
		// Calculating n-component forces: head = top of head -> shoulder
		for (int i = 0; i < velocityList.get(0).size(); i++)
		{
			// Read in the coords of the endpoints of the body part
			Vector origin = positionList.get(0).get(i + 1);
			Vector insertion = positionList.get(4).get(i + 1);
			
			// Read in the velocities of the endpoints of the body part
			Vector originVelocity = velocityList.get(0).get(i);
			Vector insertionVelocity = velocityList.get(4).get(i);
			
			Vector axis = new Vector(insertion.x() - origin.x(), insertion.y() - origin.y()); // axis = vector down the axis of the body part
			Vector n = axis.getN().norm(); // Unit vector in the perpendicular direction
			
			// N-direction velocity vectors
			Vector originNV = originVelocity.project(n);
			Vector insertionNV = insertionVelocity.project(n);
			
			// Calculates magnitude of n-force
			double nForceMagnitude = calculateForce(originNV.magnitude(), insertionNV.magnitude(), lengths[0], origin, insertion, dragCfs[0]);
			
			// Multiplies magnitude of n-force to n-directional vector copy
			Vector nForce = new Vector(n.x() * nForceMagnitude, n.y() * nForceMagnitude);
			forceList.get(0).add(nForce);
		}
		
		// Calculating n-component forces: everything else
		for (int i = 1; i < velocityList.size() - 1; i++)
		{
			// Endpoint positions
			ArrayList<Vector> origins = positionList.get(i);
			ArrayList<Vector> insertions = positionList.get(i + 1);
			
			// Endpoint velocities
			ArrayList<Vector> originV = velocityList.get(i);
			ArrayList<Vector> insertionV = velocityList.get(i + 1);
			
			for (int j = 0; j < originV.size(); j++)
			{
				// Read in the coords of the endpoints of the body part
				Vector origin = origins.get(j + 1);
				Vector insertion = insertions.get(j + 1);
				
				// Read in the velocities of the endpoints of the body part
				Vector originVelocity = originV.get(j);
				Vector insertionVelocity = insertionV.get(j);
				
				Vector axis = new Vector(insertion.x() - origin.x(), insertion.y() - origin.y()); // axis = vector down the axis of the body part
				Vector n = axis.getN().norm(); // Unit vector in the perpendicular direction
				
				// N-direction velocity vectors
				Vector insertionNV = insertionVelocity.project(n);
				Vector originNV = originVelocity.project(n);
				
				// Calculates magnitude of n-force
				double nForceMagnitude = calculateForce(originNV.magnitude(), insertionNV.magnitude(), lengths[i], origin, insertion, dragCfs[i]);
				
				// Multiplies magnitude of n-force to n-directional vector copy
				Vector nForce = new Vector(n.x() * nForceMagnitude, n.y() * nForceMagnitude);
				forceList.get(i).add(nForce);
			}
		}
	}
	
	public static void readInPositions(ArrayList<ArrayList<Vector>> positionList, double conversion, Scanner input)
	{
		// Tells what we are reading into
		boolean head = false;
		boolean fingertip = false;
		boolean elbow = false;
		boolean wrist = false;
		boolean shoulder = false;
		boolean hip = false;
		boolean knee = false;
		boolean ankle = false;
		boolean toe = false;
		
		// boolean everyOther = false;
		
		while(input.hasNextLine())
		{
			double tempX = 0;
			double tempY = 0;
			String data = input.nextLine();
			//System.out.println(positions.get(8));
			if (data.substring(0, 5).equals("POINT"))
			{
				head = false;
				fingertip = false;
				elbow = false;
				wrist = false;
				shoulder = false;
				hip = false;
				knee = false;
				ankle = false;
				toe = false;
				
				switch(data.substring(8))
				{
				case "Head":
					head = true;
				break;
				case "Fingertip":
					fingertip = true;
				break;
				case "Wrist":
					wrist = true;
				break;
				case "Elbow":
					elbow = true;
				break;
				case "Shoulder":
					shoulder = true;
				break;
				case "Hip":
					hip = true;
				break;
				case "Knee":
					knee = true;
				break;
				case "Ankle":
					ankle = true;
				break;
				case "Toes":
					toe = true;
				break;
				}
				continue;
			}
			else
			{
				tempX = Integer.parseInt(data.substring(2, data.indexOf(','))) / conversion;
				tempY = Integer.parseInt(data.substring(data.indexOf(',') + 2, data.length() - 2)) / conversion;
			}
			
			/* everyOther = !everyOther;
			if (everyOther)
			{
				continue;
			}*/
			
			if (head)
			{
				positionList.get(0).add(new Vector(tempX, tempY));
			}
			else if (fingertip)
			{
				positionList.get(1).add(new Vector(tempX, tempY));
			}
			else if (wrist)
			{
				positionList.get(2).add(new Vector(tempX, tempY));
			}
			else if (elbow)
			{
				positionList.get(3).add(new Vector(tempX, tempY));
			}
			else if (shoulder)
			{
				positionList.get(4).add(new Vector(tempX, tempY));
			}
			else if (hip)
			{
				positionList.get(5).add(new Vector(tempX, tempY));
			}
			else if (knee)
			{
				positionList.get(6).add(new Vector(tempX, tempY));
			}
			else if (ankle)
			{
				positionList.get(7).add(new Vector(tempX, tempY));
			}
			else if (toe)
			{
				positionList.get(8).add(new Vector(tempX, tempY));
			}	
		}
	}
}

