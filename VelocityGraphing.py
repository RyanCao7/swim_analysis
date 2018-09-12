from visual import *
from visual.graph import *

gd = gdisplay(x=0, y=0, width=600, height=400, 
      title='Moving Stroke Path for the Butterfly Stroke Cycle', xtitle='Time (s)', ytitle='velocity magnitude (m/s)', 
      foreground=color.white, background=color.black, 
      xmax=3, xmin=0, ymax=5, ymin=0)

curveOne = gcurve(color = color.cyan)
curveTwo = gcurve(color = color.white)
curveThree = gcurve(color = color.orange)
curveFour = gcurve(color = color.yellow)

graph1 = gdisplay()
label(display=gd.display, pos=(1, 4), text="Key:\nCyan - Fingertips\nWhite - Wrist\nOrange - Elbow\nYellow - Shoulder")
graph1.display.visible = False # make the display invisible

fileOne = open("FingertipVelocities.txt", "r")
fileTwo = open("WristVelocities.txt", "r")
fileThree = open("ElbowVelocities.txt", "r")
fileFour = open("ShoulderVelocities.txt", "r")

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

"""for line in fileOne:
    # sleep(0.05)
    if (True):
        try:
            forceCurveOne.plot(pos = (time, float(line)))
        except ValueError:
            print()

        time = time + dt

forceCurveOne.color = color.orange
time = 0

for line in fileTwo:
    # sleep(0.05)
    if (True):
        try:
            forceCurveOne.plot(pos = (time, float(line)))
        except ValueError:
            print()

        time = time + dt

forceCurveOne.color = color.green
time = 0

for line in fileAccel:
    # sleep(0.05)
    if (True):
        try:
            forceCurveOne.plot(pos = (time, float(line)))
        except ValueError:
            print()

        time = time + dt

forceCurveOne.color = color.white
time = 0

for line in fileSmoothA:
    # sleep(0.05)
    if (True):
        try:
            forceCurveOne.plot(pos = (time, float(line)))
        except ValueError:
            print()

        time = time + dt"""

