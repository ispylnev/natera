# POST "https://qa-quiz.natera.com/triangle

Description: UTF-8 does not support 

### Test
Body for post 

{"separator": "В", "input": "3В4В5"}


1. [ ]  UTF-8 in the separator does not support 

Actual: 500	

Expected: 200 ok 
expected body 

{
    "firstSide": 3.0,
    "secondSide": 4.0,
    "thirdSide": 5.0
}