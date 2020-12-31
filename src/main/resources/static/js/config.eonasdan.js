var config = config || {};

config.eonasdan = {};

config.eonasdan.defaultOption = {
    format: "DD/MM/YYYY H:mm", //Temp 24hr format change because date format is printing AM as a.m. and its bugging out.
    sideBySide: true,
    icons: {
        time: "fa fa-clock-o",
        date: "fa fa-calendar",
        up: "fa fa-arrow-up",
        down: "fa fa-arrow-down",
        previous: 'fa fa-chevron-left',
        next: 'fa fa-chevron-right',
        today: 'fa fa-dot-circle-o',
        clear: 'fa fa-trash',
        close: 'fa fa-times'
    }
};

config.eonasdan.defaultEndOption = $.extend(true, {}, config.eonasdan.defaultOption, {useCurrent: false});

config.eonasdan.defaultDateOnlyOption = $.extend(true, {}, config.eonasdan.defaultOption, {format: "DD/MM/YYYY"});
config.eonasdan.defaultDateOnlyEndOption = $.extend(true, {}, config.eonasdan.defaultDateOnlyOption, {useCurrent: false});

config.eonasdan.defaultMonthOnlyOption = $.extend(true, {}, config.eonasdan.defaultOption, {format: "MM/YYYY", viewMode: "months"});

config.eonasdan.defaultYearOnlyOption = $.extend(true, {}, config.eonasdan.defaultOption, {format: "YYYY", viewMode: "years"});
