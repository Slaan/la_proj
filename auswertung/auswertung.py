from flask import Flask
from flask import jsonify
from flask import render_template
from flask import request
import mysql.connector
import jprops

# Load our mainConfig.properties file for public use.
properties = jprops.load_properties(open('res/mainConfig.properties'))

app = Flask(__name__)


@app.route('/wins')
def wins():
    # Retrieve query parameters.
    db = int(request.args.get('bender'))
    start = int(request.args.get('start', 0))
    stack_size = int(request.args.get('stack_size', 10))
    end = int(request.args.get('end', start + (50)))

    # Split the DB property entry.
    dbs = str(properties.get('DB')).split(',')
    # Check that the 'bender' query param references a valid DB.
    if db >= len(dbs):
        raise ValueError('DB does not exist.')
    # Generate result set.
    result = condense_wins(dbs[db], stack_size, start, end)
    return jsonify(result=result,
                   count=len(result),
                   axe={
                           'plays': {
                               'label': 'Gespielte Spiele',
                               'backgroundColor': '#FF0000'
                           }, 'wins': {
                               'label': 'Gewonnene Spiele',
                               'backgroundColor': '#00FF00'
                           }
                   })

@app.route('/rewards')
def rewards():
    # Retrieve query parameters.
    db = int(request.args.get('bender'))
    start = int(request.args.get('start', 0))
    stack_size = int(request.args.get('stack_size', 10))
    end = int(request.args.get('end', start + (50)))

    # Split the DB property entry.
    dbs = str(properties.get('DB')).split(',')
    # Check that the 'bender' query param references a valid DB.
    if db >= len(dbs):
        raise ValueError('DB does not exist.')
    # Generate result set.
    result = condense_rewards(dbs[db], stack_size, start, end)
    return jsonify(result=result,
                   count=len(result),
                   axe={
                           'rewards': {
                               'label': 'Mittelwert der gesammten Spiel Rewards',
                               'borderColor': '#FF0000',
                               'fill': False
                           }
                   })


def condense_rewards(db, stack_size, start, end):
    # Get connection to database.
    cnx = mysql.connector.connect(user=properties.get('dbuser'), password=properties.get('dbpassword'), database=db)
    cursor = cnx.cursor()
    cursor.execute('SELECT '
                   'COUNT(*) AS count, '
                   '(gameID DIV %(stack)d) * %(stack)d AS ids, '
                   'SUM(reward) AS rewards '
                   'FROM GameLog '
                   'GROUP BY '
                   '(gameID DIV %(stack)d) '
                   'LIMIT %(start)d, %(end)d' % {
                        'stack': stack_size,
                        'start': start,
                        'end': end}
                   )
    reward = list()
    # Read our results an add them to the "win_loss" list.
    for (count, ids, rewards) in cursor:
        reward.append({
            'label': int(ids),
            'data': {
                'rewards': (int(rewards) * stack_size) / count
            }
        })
    # Close the DB-Connection.
    cnx.close()
    return reward


@app.route('/kills')
def kills():
    # Retrieve query parameters.
    db = int(request.args.get('bender'))
    start = int(request.args.get('start', 0))
    stack_size = int(request.args.get('stack_size', 10))
    end = int(request.args.get('end', start + (50)))

    # Split the DB property entry.
    dbs = str(properties.get('DB')).split(',')
    # Check that the 'bender' query param references a valid DB.
    if db >= len(dbs):
        raise ValueError('DB does not exist.')
    # Generate result set.
    result = condense_kills(dbs[db], stack_size, start, end)
    return jsonify(result=result,
                   count=len(result),
                   axe={
                           'kills': {
                               'label': 'Gekillte gegner',
                               'borderColor': '#00FF00',
                               'fill': False
                           }, 'deaths': {
                               'label': '# Gestorben',
                               'borderColor': '#FF0000',
                               'fill': False
                           }, 'deathMine': {
                               'label': '# Gestorben durch Miene.',
                               'borderColor': '#880000',
                               'fill': False
                           }, 'deathEnemy': {
                               'label': '# Gestorben durch Feind.',
                               'borderColor': '#CC0000',
                               'fill': False
                           }
                   })


@app.route('/')
def index():
    # Split the DB property entry.
    dbs = str(properties.get('DB')).split(',')
    # Generate param info for use in templating.
    param = {
        'start': int(request.args.get('start', 0))
    }
    # Generate list of benders for use in templating.
    benders = list()
    for db in dbs:
        benders.append({
            'id': db
        })
    return render_template('index.xhtml', param=param, benders=benders)


# This function will return a list of wins, kills, deaths and losses
# per stack_size games.
# @param db: Database with "GameLog"-Table where we will select the results from.
# @param stack_size: Specifies how many entries shall be merged to one list-entry.
# @param start: ID of the GameLog entry where we will start selecting data.
# @param end: ID of the GameLog entry that is the first excluded data after "start".
#             Has to be bigger than "start".
def condense_wins(db, stack_size, start, end):
    # Get connection to database.
    cnx = mysql.connector.connect(user=properties.get('dbuser'), password=properties.get('dbpassword'), database=db)
    cursor = cnx.cursor()
    cursor.execute('SELECT '
                   'COUNT(*) AS count, '
                   '(gameID DIV %(stack)d) * %(stack)d AS ids, '
                   'SUM(win) AS wins '
                   'FROM GameLog '
                   'GROUP BY '
                   '(gameID DIV %(stack)d) '
                   'LIMIT %(start)d, %(end)d' % {
                        'stack': stack_size,
                        'start': start,
                        'end': end}
                   )
    win_loss = list()
    # Read our results an add them to the "win_loss" list.
    for (count, ids, wins) in cursor:
        win_loss.append({
            'label': int(ids),
            'data': {
                'wins': int(wins),
                'plays': int(count)
            }
        })
    # Close the DB-Connection.
    cnx.close()
    return win_loss


# This function will return a list of kills
# per stack_size games.
# @param db: Database with "GameLog"-Table where we will select the results from.
# @param stack_size: Specifies how many entries shall be merged to one list-entry.
# @param start: ID of the GameLog entry where we will start selecting data.
# @param end: ID of the GameLog entry that is the first excluded data after "start".
#             Has to be bigger than "start".
def condense_kills(db, stack_size, start, end):
    # Get connection to database.
    cnx = mysql.connector.connect(user=properties.get('dbuser'), password=properties.get('dbpassword'), database=db)
    cursor = cnx.cursor()
    cursor.execute('SELECT '
                   'COUNT(*) AS count, '
                   '(gameID DIV %(stack)d) * %(stack)d AS ids, '
                   'SUM(kills) AS kills, '
                   'SUM(deatbByMine) AS deathByMine, '
                   'SUM(deathByEnemy) AS deathByEnemy '
                   'FROM GameLog '
                   'GROUP BY '
                   '(gameID DIV %(stack)d) '
                   'LIMIT %(start)d, %(end)d' % {
                        'stack': stack_size,
                        'start': start,
                        'end': end}
                   )
    win_loss = list()
    # Read our results an add them to the "win_loss" list.
    for (count, ids, kills, deathByMine, deathByEnemy) in cursor:
        win_loss.append({
            'label': int(ids),
            'data': {
                'kills': int(kills),
                'deaths': int(int(deathByMine) + int(deathByEnemy)),
                'deathMine': int(deathByMine),
                'deathEnemy': int(deathByEnemy)
            }
        })
    # Close the DB-Connection.
    cnx.close()
    return win_loss


# Start the programm in debug mode.
if __name__ == '__main__':
    app.run(debug=True)

