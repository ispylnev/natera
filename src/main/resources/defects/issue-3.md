# POST "https://qa-quiz.natera.com/triangle

Description: We can't send more than 10 models. 

### Test
Body for post 
        String body1 = "{\"separator\": \";\", \"input\": \"3;4;5\"}";
        String body2 = "{\"separator\": \";\", \"input\": \"3;4;5\"}";
        String body3 = "{\"separator\": \";\", \"input\": \"3;4;5\"}";
        String body4 = "{\"separator\": \";\", \"input\": \"3;4;5\"}";
        String body5 = "{\"separator\": \";\", \"input\": \"3;4;5\"}";
        String body6 = "{\"separator\": \";\", \"input\": \"3;4;5\"}";
        String body7 = "{\"separator\": \";\", \"input\": \"3;4;5\"}";
        String body8 = "{\"separator\": \";\", \"input\": \"3;4;5\"}";
        String body9 = "{\"separator\": \";\", \"input\": \"3;4;5\"}";
        String body10 = "{\"separator\": \";\", \"input\": \"3;4;5\"}";
        String body11 = "{\"separator\": \";\", \"input\": \"3;4;5\"}";


1. [ ]  Service have to return status 422 when we send 11-th model  

Actual: 200	

Expected: 422  


