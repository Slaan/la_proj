from flask import Flask
import mysql.connector

app = Flask(__name__)


@app.route('/')
def hello_world():
    cnx = mysql.connector.connect(user='bender0', password='Een5jQcn6D98bt3y', database='bender2')
    cursor = cnx.cursor()
    cursor.execute("SELECT deaths, kills, mine, rounds, tavern, win FROM GameLog ORDER BY startingTime DESC")

    str = ""
    for (deaths, kills, mine, rounds, tavern, win) in cursor:
        str += ("MurderDeathKill: {} - {}, MineTavern: {} - {}, Rounds: {}, Won: {}\n".format(deaths, kills, mine, tavern, rounds, win))
    return str


if __name__ == '__main__':
    app.run()
