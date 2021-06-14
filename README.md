# live_sport_events_challenge

#DESCRIPTION
Example of getting some betting data in hex format, and passing it down to clients
after validating inputs. The MatchEventQueryServiceTest class emulates a potential client
request to query for match events. There are some sample txt files that have the events in
hex format. From there, information about match events are extracted according to the specification
and converted to Events.

Effectively the MatchEventQueryService can be plugged in to any controller/REST api endpoint.

#ASSUMPTIONS
1. We assume that the basket game is 48 mins to validate an event.
2. For validation, a team can only score 1, 2, or 3 points in a basket game.
3. For validation, we take each event and compare it with the previous in terms of points scored 
    from each team. If the difference is more than 3 or less than 0, then the current event is invalid.
    
