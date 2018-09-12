from visual import *
from visual.graph import *

gd = gdisplay(x=0, y=0, width=600, height=400, 
      title='Various Joint Accelerations During Butterfly Stroke Cycle', xtitle='Time (s)', ytitle='acceleration magnitude (m/s^2)', 
      foreground=color.white, background=color.black, 
      xmax=3, xmin=0, ymax=25, ymin=0)

curveOne = gcurve(color = color.cyan)
curveTwo = gcurve(color = color.white)
curveThree = gcurve(color = color.orange)
curveFour = gcurve(color = color.yellow)

graph1 = gdisplay()          #(x, y)
label(display=gd.display, pos=(0.75, 16), text="Key:\nCyan - Fingertips\nWhite - Wrist\nOrange - Elbow\nYellow - Shoulder")
graph1.display.visible = False # make the display invisible

fileOne = open("FingertipAccelerations.txt", "r")
fileTwo = open("WristAccelerations.txt", "r")
fileThree = open("ElbowAccelerations.txt", "r")
fileFour = open("ShoulderAccelerations.txt", "r")

time = 0
dt = 0.04

def plotData(fileName, plot, resetTime, timeStart):
    time = timeStart
    for line in fileName:
        # sleep(0.05)
        if (True):
            try:
                plot.plot(pos = (time, float(line)))
            except ValueError:
                print()
            time = time + dt
            
    if (resetTime):
        time = 0

plotData(fileOne, curveOne, True, 0)
plotData(fileTwo, curveTwo, True, 0)
plotData(fileThree, curveThree, True, 0)
plotData(fileFour, curveFour, True, 0)
