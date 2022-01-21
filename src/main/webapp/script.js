var list_item;

var json_tasks;

var unfinished_tasks;

var user;

var categories;

var post_category = [];

var dropList;

$(document).ready(function () {
    clearListItem();
    getUser();
    getCategory();
    getRequestAllTasks();
} );

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
    dropList = document.querySelector('.option_item').outerHTML;
    document.querySelector('.option_item').outerHTML = '';
    console.log('dropList in clearListItem --> ' + dropList);
}

/*
Основная функция для запроса заданий из БД и парсинга их на страницу
 */
function getRequestAllTasks() {
    $.ajax({
        type: 'GET',
        url: 'http://localhost:8080/job4j_todo/task_list.do',
        dataType: 'json'
    }).done(function (data) {
        addRow(data);
        json_tasks = data;
        console.log('Response OK 200');
        console.log('JSON data -> ' + json_tasks);
        setStatus(json_tasks);
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

/*
Использовалась для запроса категорий и вывода категорий в список
 */
function getCategory() {
    $.ajax({
        type: 'GET',
        url: 'http://localhost:8080/job4j_todo/task_list/category.do',
        dataType: 'json'
    }).done(function (data) {
        categories += data;
        console.log('CATEGORIES Response OK 200');
        $.each(data, function (i, item) {
            console.log('CATEGORIES ID -> ' + item.id + ' NAME --> ' + item.name);
            setCategories(item.id, item.name);
        });
    }).fail(function (err) {
        console.log(err);
    });
}

/*
Функция для вывода категорий в список
 */
function parseCategory(data) {
    $.each(data, function (i, item) {
        console.log('CATEGORIES ID -> ' + item.id + ' NAME --> ' + item.name);
        setCategories(item.id, item.name);
    });
}

function getRequestUnfinishedTasks() {
    $.ajax({
        type: 'GET',
        url: 'http://localhost:8080/job4j_todo/task_list.do',
        dataType: 'json'
    }).done(function (data) {
        addRowUnfinished(data);
        unfinished_tasks = data;
        console.log('Response OK 200');
        console.log('JSON data -> ' + unfinished_tasks);
        setStatus(unfinished_tasks);
    }).fail(function (err) {
        console.log(err);
    });
}

function addRow(data) {
    $.each(data, function (i, item) {
        addTaskFromDb(item);
        textStatus(item.id, item.done);
    });
}

function addRowUnfinished(data) {
    $.each(data, function (i, item) {
        if (item.done === false) {
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

function setCategories(id, name) {
    $('#drop_down_list:last-child')
        .append(dropList
            .replace('$option_id', id)
            .replace('$option_id', id)
            .replace('$option_value', name));
}

function addTaskFromDb(item) {
    let categories = item.categories;
    let list_categories = '';
    for(let key in categories) {
        console.log('CATEGORY.NAME ---> ' + key + '----' + categories[key].name);
        list_categories += categories[key].name;
        list_categories += '  ';
    }
    console.log('LIST CATEGORIES ---> ' + list_categories);

    console.log('ID ---> ' + item.id);
    $('#table > tbody:last-child')
        .append(list_item
            .replace('$task_id', item.id)
            .replace('$task_name', item.name)
            .replace('$task_description', item.description)
            .replace('$task_category', list_categories)
            .replace('$task_author', item.author.name)
            .replace('$check_id', 'checkbox_' + item.id)
            .replace('$check_id', 'checkbox_' + item.id)
        );
}

function saveTask() {
    validationTask();
    $.ajax({
        type: 'POST',
        crossdomain: true,
        url: 'http://localhost:8080/job4j_todo/task_list.do',
        data: JSON.stringify({
            id: 0 ,
            name: $('#taskName').val(),
            description: $('#description').val(),
            done: false,
            author : user,
            categories : post_category
        }),
        dataType: 'json'
    }).done(function (data) {
        addTaskFromDb(data);
        setStatus(data);
    }).fail(function (err) {
        console.log('Response with error');
        console.log(err);
    });
}

/*
Валидация параметров отправления задачи
 */
function validationTask() {
    let name = $('#taskName').val();
    let descr = $('#description').val();
    let empty_category = $('#drop_down_list option:selected').text().startsWith('Выберите');
    let categoryIds = $('#drop_down_list').val();
    console.log('MASSIV id ********** ' + '[' + categoryIds.toString() + ']');
    if (!empty_category && name !== '' && descr !== '') {
        for(let key in categoryIds) {
            post_category.push({id : categoryIds[key]});
        }
    } else {
        alert('Заполните все поля или выберите категорию задачи!');
    }
    console.log(name);
    console.log(descr);
    console.log('СРАВНЕНИЕ СТРОК ---' + empty_category);
    console.log('POST CATEGORY ---- ' + post_category[0]);
}

/*
Функция для изменения статуса checkbox влияет на БД и label
 */
$(document).on('change', 'input[class="status-check-input"]', function () {
    let clickId = $(this).prop('id');
    var labelText = $('label[for=checkbox_'+  clickId  +']');
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

/*
Функция проставляет галочки в checkbox в зависимости от item.done
 */
function setStatus(data) {
    $.each(data, function (i, item) {
        let checkbox_id = item.id;
        console.log('setStatus CHECKBOX ---- ' + item.done);
        $('#checkbox_' + checkbox_id).prop('checked', item.done);
        let checkStatus = item.done;
        textStatus(item.id, checkStatus);
    });
}

/*
Функция меняет label у checkbox в зависимости от item.done
 */
function textStatus(id, checkStatus) {
    var labelText = $('label[for=checkbox_'+  id  +']');
    console.log('labelText ' + labelText.text() );
    if (checkStatus) {
        labelText.text('Выполнено');
    } else {
        labelText.text('В процессе...');
    }
}

/*
Checkbox для скрытия/показа выполненных заданий
 */
function checkAllTasks() {
    clearTable();
    clearListItem();

    if ($(this).is(':checked')) {
        getRequestAllTasks();
        console.log('getRequestAllTasks() ' + json_tasks);
    } else {
        getRequestUnfinishedTasks();
        console.log('getRequestUnfinishedTasks() ' + json_tasks);
    }
}

function testJSON() {
}