
function StackChart(uri, canvas, title, benderIndex, min_stack_size, display_data_count) {
    this.uri = uri;
    this.chart = new Chart.Line(canvas, {data: {labels: [], datasets: []}});
    this.title = title;
    this.benderIndex = benderIndex;
    this.min_stack_size = min_stack_size;
    this.display_data_count = display_data_count;


}

StackChart.prototype.reload = function(start, stack_size) {
    var myThis = this;
    $.ajax({
        url: this.uri +
        '?bender=' + this.benderIndex +
        '&start=' + start +
        '&end=' + (start + this.display_data_count) +
        '&stack_size=' + stack_size
    }).done(function (items) {
        myThis.reload_callback(start, stack_size, items);
    });
};

StackChart.prototype.reload_callback = function (start, stack_size, items) {
    var stackChart = this;
    // Add controls to the right of the title.
    if (items.count >= this.display_data_count) {
        this.title.append(
            $('<a href="./?start=' + (start + this.display_data_count) + '" class="navigation"> &gt;</a>').
            click(function() {
                $(stackChart.title).children(".navigation").remove();
                stackChart.reload(start + stackChart.display_data_count, stack_size);
                return false;
            })
        ).append(
            $('<a href="./?start=' + (start) + '&stack_size=' + (stack_size + this.min_stack_size) + '" class="navigation"> -</a>').
            click(function() {
                $(stackChart.title).children(".navigation").remove();
                stackChart.reload(start, stack_size + stackChart.min_stack_size);
                return false;
            })
        );
    }

    // Add controls to the left of the title.
    if (start > 0) {
        this.title.prepend(
            $('<a href="./?start=' + (start - this.display_data_count) + '" class="navigation">&lt; </a>').
            click(function() {
                $(stackChart.title).children(".navigation").remove();
                stackChart.reload(start - stackChart.display_data_count, stack_size);
                return false;
            })
        ).prepend(
             $('<a href="./?start=0" class="navigation">&lt;&lt; </a>').
            click(function() {
                $(stackChart.title).children(".navigation").remove();
                stackChart.reload(0, stackChart.min_stack_size);
                return false;
            })
        );
    }

    // Generate property lists from the returned results.
    var labels = [];
    var datasets = [];

    // Initialize datasets.
    Object.keys(items.axe).forEach(function (key) {
        var axe = items.axe[key];
        axe.data = [];
        datasets.push(axe);
    });

    // Fill datasets with data.
    items.result.forEach(function (item) {
        labels.push(item.label);
        for (var i = 0; i < Object.keys(item.data).length; i++) {
            var data = item.data[Object.keys(item.data)[i]];
            datasets[i].data.push(data);
        }
    });
    reloadChart(this.chart, labels, datasets);
};

function reloadChart(chart, labels, datasets) {
    chart.data.labels = labels;
    chart.data.datasets = datasets;
    chart.initialize();
}

// Add "eventlistener" for triggering after the document ist loaded.
$(window).load(function () {
    Chart.defaults.scale.ticks.beginAtZero = true;
    benders.forEach(function (benderId, index) {
        canvas.forEach(function (can) {
            console.log('create bender StackChar: #' + can + 'Canvas' + benderId);

            var chart = new StackChart(
                '/' + can,
                $('#' + can + 'Canvas' + benderId),
                $('#' + can + '' + benderId),
                index,
                10,
                20
            );
            chart.reload(start, 10);

        });
    });
});