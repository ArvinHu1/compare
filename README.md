# compare
function: 
1.compare files within two folders, return the different files, files only in the first folder and files only in the second folder;
2.send mail to someone with attachment.

tips:
1.you can set the two folders path through config/config.properties;
2.if there are files don't need to compare, you can add a row like 'excludeFile=xxx', it could be a file with  
absolute path or a file with relative path or even a suffix(.class for example);
3.set your mail properties, you should set your mail address and password, and also the receiver(s). write down the subject in the end;
4.set the timer properties, the argument of starttime defines the schedule time of the task. And period means the interval of the schedule,
it could be minutes or hours(10m or 2h for example).
