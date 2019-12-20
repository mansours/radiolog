/**
 * @Authors: Samer Mansour
 * @Year: 2018
 * This javascript file contains library components used across our platform.
 */

/**
 * ru is a site wide reserved root package for Ryerson University
 * @type {{Object}}
 */
var ru = ru || {};

/**
 * ru.lib is a library package, and contain common application components.
 * @type {{Object}}
 */
ru.lib = {};

/**
 * ru.lib.page is a package for common page functions.
 * @type {{Object}}
 */
ru.lib.page = {};

/**
 * Updates a list page's form to sort by a specific column and submits
 * @param column the name of the column to sort by
 */
ru.lib.page.sort = function (column) {
    let sortBy = $('#sortBy');
    let orderDir = $('#orderDir');

    if (sortBy.val() === column) {
        orderDir.val(orderDir.val() === "DESC" ? "ASC" : "DESC");
    } else {
        sortBy.val(column);
        orderDir.val("ASC");
    }

    //Remove export sub method, does nothing if not there.
    let action = $('#searchForm').attr('action').replace("/export", "");
    $('#searchForm').attr('action', action);

    $('#page').val(1);
    $('#searchForm').submit();
};

/**
 * Updates a list page's form to navigate to a specific page and submits
 * @param page in the results to navigate to
 */
ru.lib.page.navigate = function (page) {
    //Remove export sub method, does nothing if not there.
    let action = $('#searchForm').attr('action').replace("/export", "");
    $('#searchForm').attr('action', action);

    $('#page').val(page); //Set page number
    $('#searchForm').submit();
};