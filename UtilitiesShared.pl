%Robocode predicates

ahead(Ref, Amount) :- 
	jpl_call(Ref, ahead, [Amount], _). 

turnLeft(Ref, Amount) :- 
	jpl_call(Ref, turnLeft, [Amount], _). 

turnRight(Ref, Amount) :- 
	jpl_call(Ref, turnRight, [Amount], _). 

turnGunLeft(Ref, Amount) :- 
	jpl_call(Ref, turnGunLeft, [Amount], _). 

getDistance(Ref, Return) :- 
	jpl_call(Ref, getDistance, [], Return).

getEnergy(Ref, Return) :- 
	jpl_call(Ref, getEnergy, [], Return).

getHeading(Ref, Return) :- 
	jpl_call(Ref, getHeading, [], Return). 
	
getOthers(Ref, Return) :- 
	jpl_call(Ref, getOthers, [], Return).

fire(Ref, Amount) :- 
	jpl_call(Ref, fire, [Amount], _).

scan(Ref) :- 
	jpl_call(Ref, scan, [], _).

stop(Ref) :- 
	jpl_call(Ref, stop, [], _).

resume(Ref) :- 
	jpl_call(Ref, resume, [], _).

normalRelativeAngle(Amount, Return) :- 
	jpl_call('robocode.util.Utils', normalRelativeAngle, [Amount], Return).









