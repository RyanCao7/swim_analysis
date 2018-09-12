from visual import *
from visual.graph import *

gd = gdisplay(x=0, y=0, width=600, height=400, 
      title='Applied Force Magnitudes for Butterfly Stroke Cycle', xtitle='Time (s)', ytitle='Applied force magnitude (N)', 
      foreground=color.white, background=color.black, 
      xmax=3, xmin=0, ymax=90, ymin=0)

curveOne = gcurve(color = color.cyan)
curveTwo = gcurve(color = color.white)
curveThree = gcurve(color = color.orange)

label(display=gd.display, pos=(2.3, 80), text="Key:\nCyan - Hand\nWhite - Upper Arm\nOrange - Lower Arm")

fileOne = open("HandAppliedForces.txt", "r")
fileTwo = open("Lower ArmAppliedForces.txt", "r")
fileThree = open("Upper ArmAppliedForces.txt", "r")

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
