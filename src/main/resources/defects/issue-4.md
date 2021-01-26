# POST "https://qa-quiz.natera.com/triangle

Description: We can't send one of which side a negative value. 

### Test
Body for post 
{"separator": ";", "input": "3;3;-3"}


1. [ ]  Service have to return status 422 

Actual: 200	

Expected: 422  


