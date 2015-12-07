
function StackChart(uri, canvas, title, benderIndex, min_stack_size, display_data_count) {
    this.uri = uri;
    this.chart = new Chart.Line(canvas, {data: {labels: [], datasets: []}});
    this.title = title;
    this.benderIndex = benderIndex;
    this.min_stack_size = min_stack_size;
    this.display_data_count = display_data_count;


}

StackChart.prototype.reload = function(start, stack_size) {
    this.
    $.ajax({
        url: this.uri +
        '?bender=' + this.benderIndex +
        '&start=' + start +
        '&end=' + (start + this.display_data_count) +
        '&stack_size=' + stack_size
    }).done(function (items) {

    });
};

StackChart.prototype.reload_callback = function (start, stack_size) {
        // Add controls to the right of the title.
        if (items.count >= this.display_data_count) {
            this.title.append(
                $('<a href="./?start=' + (start + this.display_data_count) + '" class="navigation"> &gt;</a>').
                click(function() {
                    $(this.title).children(".navigation").remove();
                    reload(start + this.display_data_count, stack_size);
                    return false;
                })
            ).append(
                $('<a href="./?start=' + (start) + '&stack_size=' + stack_size + '" class="navigation"> -</a>').
                click(function() {
                    $(title).children(".navigation").remove();
                    this.reload(start, stack_size + this.min_stack_size);
                    return false;
                })
            );
        }

        // Add controls to the left of the title.
        if (start > 0) {
            this.title.prepend(
                $('<a href="./?start=' + (start - display_data_count) + '" class="navigation">&lt; </a>').
                click(function() {
                    $(this.title).children(".navigation").remove();
                    reloadWinChart(start - this.display_data_count, stack_size);
                    return false;
                })
            ).prepend(
                 $('<a href="./?start=0" class="navigation">&lt;&lt; </a>').
                click(function() {
                    $(this.title).children(".navigation").remove();
                    reloadWinChart(0, this.min_stack_size);
                    return false;
                })
            );
        }

        // Generate property lists from the returned results.
        var labels = [];
        var wins = [];
        var plays = [];
        items.result.forEach(function (item) {
            labels.push(item.label);
            wins.push(item.wins);
            plays.push(item.losses + item.wins);
        });
        var datasets = [
            {
                label: 'Gesammte Spiele',
                data: plays,
                backgroundColor: '#FF0000'
            }, {
                label: 'Gewonnene Spiele',
                data: wins,
                backgroundColor: '#00FF00'
            }
        ];
        reloadChart(this.chart, labels, datasets);
    });
};

function reloadChart(chart, labels, datasets) {
    chart.data.labels = labels;
    chart.data.datasets = datasets;
    chart.initialize();
}

// Add "eventlistener" for triggering after the document ist loaded.
$(window).load(function () {
    benders.forEach(function (benderId, index) {
        console.log('create bender StackChar: ' + benderId);
        var chart = new StackChart(
            'wins',
            $('#wonTime' + benderId),
            $('#wonTitle' + benderId),
            index,
            start,
            10,
            10,
            50
        );
        chart.reload(0, 10);
    });
});