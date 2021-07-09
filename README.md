# IPL DASHBOARD

This application does:

### 1. Charges an on memory database 
The data consist on all IPL matches in the period 2000-2008. They come from  _"IPL Matches 2008-2020.csv"_ file.
   
This is done by a Spring Batch Job that is executed every time the application stars (we haven't disabled automatic job execution in properties file). 
   