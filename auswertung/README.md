## MongoDB des Servers
### Verbinden mit der Datenbank
```bash
mongo
```
Auf dem Server ausführen.
### In die richtige Datenbank gehen
```mongo
use vindinium
```
### Herausfinden von Spielen in denen kein Bot gecrasht ist:
```mongo
db.replay.find({"moves": {$not: {$in: [99]}}})
```

Moves:
1 - Oben (North)
2 - Unten (South)
3 - Rechts (Osten)
4 - Links (Westen)
99 - Crashed
