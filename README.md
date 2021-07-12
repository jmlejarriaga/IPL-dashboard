# IPL DASHBOARD

This application does:

### 1. Charges an on memory database 
The data consist on all IPL matches in the period 2000-2008. They come from  _"IPL Matches 2008-2020.csv"_ file.
   
This is done by a Spring Batch Job that is executed every time the application stars (we haven't disabled automatic job execution in properties file). 

The JobCompletionNotificationListener is executed when job finishes. It inserts in _"Team"_ table all the teams specifying all matches played and number of victories.

### 2. The API for the app is
####Team dashboard
/teams/*teamName* - Will show team data (name, number of matches, win/lost)

####Match page
/teams/*teamName*/matches?year=*number* - Will show data per year. Any match can be selected

### 3. The React app
Is located in **/src/frontend**

It was created following: https://es.reactjs.org/docs/create-a-new-react-app.html.

The name chosen for React app is *"frontend"* (that's why path is /src/frontend). 