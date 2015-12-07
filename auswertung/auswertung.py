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
    return jsonify(result=result, count=len(result))


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
                   'SUM(win) AS wins, '
                   'SUM(kills) AS kills, '
                   'SUM(deaths) AS deaths '
                   'FROM GameLog '
                   'GROUP BY '
                   'gameID DIV %(stack)d '
                   'LIMIT %(start)d, %(end)d' % {
                        'stack': stack_size,
                        'start': start,
                        'end': end}
                   )
    win_loss = list()
    # Read our results an add them to the "win_loss" list.
    for (count, ids, wins, kills, deaths) in cursor:
        win_loss.append({
            'label': int(ids),
            'wins': int(wins),
            'kills': int(kills),
            'deaths': int(deaths),
            'losses': int(count - wins)
        })
    # Close the DB-Connection.
    cnx.close()
    return win_loss


# Start the programm in debug mode.
if __name__ == '__main__':
    app.run(debug=True)
