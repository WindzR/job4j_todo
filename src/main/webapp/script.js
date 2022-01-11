var list_item;

var json_test;

var user;

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
    console.log('list_item in clearListItem --> ' + list_item);
    element('list_item').outerHTML = '';
}

function getRequestAllTasks() {
    $.ajax({
        type: 'GET',
        url: 'http://localhost:8080/job4j_todo/task_list.do',
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

function getUser() {
    $.ajax({
        type: 'GET',
        url: 'http://localhost:8080/job4j_todo/user.do',
        dataType: 'json'
    }).done(function (data) {
        user = data;
        console.log('USER Response OK 200');
        console.log('User JSON -> ' + user.name);
        setUser();
    }).fail(function (err) {
        console.log(err);
    });
}

function getRequestUnfinishedTasks() {
    $.ajax({
        type: 'GET',
        url: 'http://localhost:8080/job4j_todo/task_list.do',
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
    getUser();
    getRequestAllTasks();
} );

function addRow(data) {
    $.each(data, function (i, item) {
        addTaskFromDb(item);
        textStatus(item.id, item.done);
    });
}

function addRowUnfinished(data) {
    $.each(data, function (i, item) {
        if (item.done === false) {
            console.log('Выводиться должны только false -> ' + item.done);
            addTaskFromDb(item);
            textStatus(item.id, item.done);
        }
    });
}

function setUser() {
    let userHTML = element('log_user').innerHTML;
    let name = user.name;
    console.log('userHTML --> ' + userHTML);
    element('log_user').innerHTML = name;
    console.log('setUser user.name --> ' + name);
}

function addTaskFromDb(item) {
    console.log('ID ---> ' + item.id);
    $('#table > tbody:last-child')
        .append(list_item
            .replace('$task_id', item.id)
            .replace('$task_name', item.name)
            .replace('$task_description', item.description)
            .replace('$task_author', item.author.name)
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
        url: 'http://localhost:8080/job4j_todo/task_list.do',
        data: JSON.stringify({
            id: task_id ,
            name: $('#taskName').val(),
            description: $('#description').val(),
            done: false,
            author : user
        }),
        dataType: 'json'
    }).done(function (data) {
        addTaskFromDb(data);
        textStatus(id, checkStatus);
    }).fail(function (err) {
        console.log('Response with error');
        console.log(err);
    });
}

$(document).on('change', 'input[class="status-check-input"]', function () {
    let clickId = $(this).prop('id');
    var labelText = $('label[for='+  clickId  +']');
    if ($(this).is(':checked')){
        postStatus(clickId, true);
        labelText.text('Выполнено');
    } else {
        postStatus(clickId, false);
        labelText.text('В процессе...');
    }
});

function postStatus(idTask, statusTask) {
    $.ajax({
        type: 'POST',
        crossdomain: true,
        url: 'http://localhost:8080/job4j_todo/task_list/update',
        data: JSON.stringify({
            id: idTask ,
            done: statusTask
        }),
        dataType: 'json'
    }).done(function (data) {
        console.log('JSON --> ' + data);
    }).fail(function (err) {
        console.log('Response with error');
        console.log(err);
    });
}

function setStatus(data) {
    $.each(data, function (i, item) {
        document.getElementById(item.id).checked = item.done;
        let checkStatus = item.done;
        textStatus(item.id, checkStatus);
    });
}

function textStatus(id, checkStatus) {
    var labelText = $('label[for='+  id  +']');
    console.log('labelText ' + labelText.text() );
    if (checkStatus) {
        labelText.text('Выполнено');
    } else {
        labelText.text('В процессе...');
    }
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

function testJSON() {
}