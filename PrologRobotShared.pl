:- dynamic others/1.
:- dynamic corner/1.
:- dynamic stopWhenSeeRobot/1.

corner(0).
stopWhenSeeRobot(false).

startStrategy(R) :- 
	getOthers(R, Num),
	retractall(others),
	assert(others(Num)),
	goCorner(R), 
	spinGunLoop(R, 3).

spinGunLoop(R, X) :- 
	spinGun(R, X, 30), 
	NewX is -X, 
	spinGunLoop(R, NewX).  

spinGun(_, _, 0) :-
	!.

spinGun(R, X, Num) :- 
	turnGunLeft(R, X), 
	NewNum is Num - 1, 
	spinGun(R, X, NewNum).
					  

goCorner(R) :- 
	retractall(stopWhenSeeRobot),
	assert(setStopWhenSeeRobot(false)),
	corner(Angle), 
	getHeading(R, Heading),
	Difference is Angle - Heading, 
	normalRelativeAngle(Difference, Amount),
	turnRight(R, Amount), 
	retractall(stopWhenSeeRobot),
	assert(setStopWhenSeeRobot(true)),
	ahead(R, 5000), 
	turnLeft(R, 90), 
	ahead(R, 5000), 
	turnLeft(R, 90).
			   
			   
onScannedRobot(R, E) :- 
	stopWhenSeeRobot(Result), 
	onScannedRobot(R, E, Result).

                        
onScannedRobot(R, E, true) :- 
	stop(R), 
	getDistance(E, Distance),
	smartFire(R, Distance),
	scan(R), 
	resume(R).	

onScannedRobot(R, E, false) :- 
	getDistance(E, Distance),
	smartFire(R, Distance).
		
		
smartFire(R, Distance) :- 
	Distance > 200, 
	fire(R, 1), 
	!.

smartFire(R, _) :- 
	getEnergy(R, Energy), 
	Energy < 15, 
	fire(R, 1), 
	!.

smartFire(R, Distance) :- 
	Distance > 50, 
	fire(R, 2), 
	!.

smartFire(R, _) :- 
	fire(R, 3).

onDeath(_) :- 
	others(0), !.
	
onDeath(R) :- 
	others(OldOthers), 
	getOthers(R, NewOthers),
	Value is OldOthers - NewOthers / float(OldOthers),
	Value < 0.75, 
	changeCorner.
			  

changeCorner :- 
	corner(Angle), 
	NewCorner is Angle + 90,
	increaseCorner(NewCorner).
				   
increaseCorner(NewCorner) :- 
	NewCorner is 270, 
	FinalCorner is -90,
	retractall(corner),
	assert(corner(FinalCorner)), !.

increaseCorner(NewCorner) :- 
	retractall(corner),
	assert(corner(NewCorner)).
								
