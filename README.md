# TheVoice
GitHub for project devApp class.

Group members : CHABOT Yohan : uf4vortex@gmail.com DE SOUSA Julia : julia-desousa@sfr.fr SAMSON Ewenn : samson.ewenn@gmail.com

Description of the app : 
Our app is a game in which you have to scream the louder you can. You first have to fill a formulary with your name and surname. Then, when you start the game, you will have to click the mic and register your five second scream. After that, you will be able to see your score in decibels clicking the Score Page button.

Activities : 
We have three activities :
- MainActivity : where you fill your informations, have access to the game and the score page
- SecondActivity : the game page where you click the button to record the audio
- ScoreActivity : where you can see all the previous scores.

Storage :
- We use the SQLlite local database to store the players' scores.

Intents :
- We start the app with the mainActivity  and the user has two options : go on scoreboard activity or play to beat their best score

Background service :
- we use the Notification Service with the alarmService and a timer to display a notification on cell each day at the same hour

Sensor used :
- We use the microphone as sensor. We record an audio and we get the amplitude and thank to physics calcul, we find its maximal decibels to classify it in the score page.
