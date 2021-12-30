var list_item;

var json_test;

function element(id) {
    return document.getElementById(id);
}

function clearTable() {
    element('tbody').innerHTML = '';
    $('#table > tbody:last-child')
        .append(list_item);
}

function clearListItem() {
    list_item = element('list_item').outerHTML;
    element('list_item').outerHTML = '';
}

function getRequestAllTasks() {
    $.ajax({
        type: 'GET',
        url: 'http://localhost:8080/job4j_todo/task_list',
        dataType: 'json'
    }).done(function (data) {
        addRow(data);
        json_test = data;
        console.log('Response OK 200');
        console.log('JSON data -> ' + json_test);
        setStatus(json_test);
    }).fail(function (err) {
        console.log(err);
    });
}

function getRequestUnfinishedTasks() {
    $.ajax({
        type: 'GET',
        url: 'http://localhost:8080/job4j_todo/task_list',
        dataType: 'json'
    }).done(function (data) {
        addRowUnfinished(data);
        json_test = data;
        console.log('Response OK 200');
        console.log('JSON data -> ' + json_test);
        setStatus(json_test);
    }).fail(function (err) {
        console.log(err);
    });
}

$(document).ready(function () {
    clearListItem();
    getRequestAllTasks();
} );

function addRow(data) {
    $.each(data, function (i, item) {
        addTaskFromDb(item);
    });
}

function addRowUnfinished(data) {
    $.each(data, function (i, item) {
        if (item.done === false) {
            console.log('Выводиться должны только false -> ' + item.done);
            addTaskFromDb(item);
        }
    });
}

function addTaskFromDb(item) {
    console.log('ID ---> ' + item.id);
    $('#table > tbody:last-child')
        .append(list_item
            .replace('$task_id', item.id)
            .replace('$task_name', item.name)
            .replace('$task_description', item.description)
            .replace('$check_id', item.id)
            .replace('$check_id', item.id)
        );
}

function getTaskList() {
    var task_id = 0;

    console.log($('#taskName').val());
    console.log($('#description').val());
    console.log(new Date().toLocaleString());

    $.ajax({
        type: 'POST',
        crossdomain: true,
        url: 'http://localhost:8080/job4j_todo/task_list',
        data: JSON.stringify({
            id: task_id ,
            name: $('#taskName').val(),
            description: $('#description').val(),
            done: false
        }),
        dataType: 'json'
    }).done(function (data) {
        addTaskFromDb(data);
    }).fail(function (err) {
        console.log('Response with error');
        console.log(err);
    });
}

function setStatus(data) {
    $.each(data, function (i, item) {
        document.getElementById(item.id).checked = item.done;
    });
}

function checkAllTasks() {
    clearTable();
    clearListItem();

    if ($(this).is(':checked')) {
        getRequestAllTasks();
        console.log('getRequestAllTasks() ' + json_test);
    } else {
        getRequestUnfinishedTasks();
        console.log('getRequestUnfinishedTasks() ' + json_test);
    }
}

function testJSON(data) {
    clearTable();
    clearListItem();
    $.each(data, function (i, item) {
        if (item.id === 3) {
            console.log('Выводиться должны элементы id = 3 -> ' + item.name);
            console.log('JSON с id = ' + i + ' -> ' + item[i]);
            addTaskFromDb(item);
        }
    });
}