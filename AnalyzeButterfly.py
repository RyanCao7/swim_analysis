#!/usr/bin/env python2
#encoding: UTF-8

# Ryan Cao, Northside Health Careers HS SRD 2016-2017
import sys
import time
import cv2
import numpy as np

def click(event, x, y, flags, param):
	# if the left mouse button was clicked, record the starting
	# (x, y) coordinates
	if event == cv2.EVENT_LBUTTONDOWN:
            # Translates the coordinates received so that the x and y-axes
            # cross at the center of the screen
            clickedPoints = [(x - (videoWidth / 2), -1 * (y - (videoHeight / 2)))]
            print(str(frameNumber) + str(clickedPoints))
            data.write(str(clickedPoints) + "\n")

bodyPart = raw_input("Enter the body joint/point you are entering data for: ")
if (bodyPart.lower() != "head" and bodyPart.lower() != "fingertip" and 
    bodyPart.lower() != "wrist" and bodyPart.lower() != "elbow" and 
    bodyPart.lower() != "shoulder" and bodyPart.lower() != "hip" and 
    bodyPart.lower() != "knee" and bodyPart.lower() != "ankle" and 
    bodyPart.lower() != "toes"):
        print("Error. Please enter one of: [head, fingertip, wrist, elbow, shoulder, hip, knee, ankle, toes]")
        sys.exit()
        
data = open("ModelCoordinatesThree.txt", "a")
data.write(str("POINT - " + bodyPart + "\n"))
frameNumber = 0 # This gives you an easy way to see if you double clicked in a frame or skipped one. 

videoFile = raw_input("Please enter the name of the video to be analyzed: ")

butterflyVideo = cv2.VideoCapture(videoFile)
print((int)(butterflyVideo.get(cv2.CAP_PROP_FRAME_COUNT)))
clicked = False

videoWidth = (int)(butterflyVideo.get(cv2.CAP_PROP_FRAME_WIDTH))
videoHeight = (int)(butterflyVideo.get(cv2.CAP_PROP_FRAME_HEIGHT))

#ret, sampleImg = cap.read()
#print(len(sampleImg[0]))
while (True):
    frameNumber = frameNumber + 1
    #ret = true/false, whether some img/frame is returned
    #frame = object pointer to the frame
    ret, frame = butterflyVideo.read()
    
    if (frame == None):
        break
    
    clicked = False
    
    # Attempted to use OpenCV corner detect function for detection of joint
    # locations. Joint detection not sensitive enough; detection failed.
    """gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
    gray = np.float32(gray)
    # Img, max, min quality, min distance
    cornerList = cv2.goodFeaturesToTrack(gray, 17, 0.01, 10)
    cornerList = np.int0(cornerList)

    for corner in cornerList:
        x, y = corner.ravel()
        cv2.circle(frame, (x, y), 1, 255, -1)
        # print(x, y)"""
    
    
    cv2.imshow("Current frame", frame)
    cv2.setMouseCallback("Current frame", click)
    rows, cols, channels = frame.shape
    # cv2.imshow("Gray frame", gray)
    
    # Press 'c' to continue, and 'q' to quit.
    k = 0
    while (k != ord('c') and k != ord('q') and not clicked):
        k = cv2.waitKey(0) & 0xFF
    
    if (k == ord('q')):
        break

    
butterflyVideo.release() # make sure you always do this!!!
cv2.destroyAllWindows()
