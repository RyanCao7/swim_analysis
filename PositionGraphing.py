from visual import *
from visual.graph import *


gd = gdisplay(x=0, y=0, width=600, height=400, 
      title='Moving Stroke Path for the Butterfly Stroke Cycle', xtitle='x-displacement (m)', ytitle='y-displacement (m)', 
      foreground=color.white, background=color.black, 
      xmax=0, xmin=-3, ymax=0.5, ymin=-0.6)

waterMovementPerFrame = 0.02237 # 0.02237 for with motion, 0 for without motion
forceCurveOne = gcurve(color = color.cyan)
forceCurveTwo = gcurve(color = color.white)
forceCurveThree = gcurve(color = color.orange)
forceCurveFour = gcurve(color = color.yellow)

graph1 = gdisplay()
label(display=gd.display, pos=(-1,0.4), text="Key:\nCyan - Fingertips\nWhite - Wrist\nOrange - Elbow\nYellow - Shoulder")
graph1.display.visible = False # make the display invisible

coordinateFile = open("FingertipPositions.txt", "r")
coordinateFileTwo = open("WristPositions.txt", "r")
coordinateFileThree = open("ElbowPositions.txt", "r")
coordinateFileFour = open("ShoulderPositions.txt", "r")
time = 0
dt = 0.04

def plotData(fileName, graph, bodyMotion):
    frameCounter = 0
        
    for line in fileName:
        # sleep(0.05)
        if (True):
            coords = line.split()
            try:
                coords[0] = coords[0][:-1]
                x = float(coords[0])
                y = float(coords[1])
                graph.plot(pos = (x - frameCounter, y))
            except ValueError:
                print()

            frameCounter = frameCounter + waterMovementPerFrame

plotData(coordinateFile, forceCurveOne, True)
plotData(coordinateFileTwo, forceCurveTwo, True)
plotData(coordinateFileThree, forceCurveThree, True)
plotData(coordinateFileFour, forceCurveFour, True)
