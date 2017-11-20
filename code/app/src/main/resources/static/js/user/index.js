(function ($) {
    $(function () {

        // crsf
        // var csrfParameter = $("meta[name='_csrf_parameter']").attr("content");
        var csrfHeader = $("meta[name='_csrf_header']").attr("content");
        var csrfToken = $("meta[name='_csrf']").attr("content");

        var headers = {};
        headers[csrfHeader] = csrfToken;

        var dialog, form, userTable, action; // action create, edit and delete, initialized with empty
        var highlight = 0;

        /*
        * update user when update is true, otherwise create a new one
        * */
        function createUser() {

            // get roles
            var roles = [];
            $('#role :checked').each(function () {
                roles.push({id: $(this).val()});
            });

            var formData = {
                username: $('#username').val(), firstName: $('#firstName').val(),
                lastName: $('#lastName').val(), description: $('#description').val(), roles: roles
            };

            $.ajax({
                type: 'post',
                url: '/rest/user/create',
                headers: headers,
                contentType: 'application/json',
                dataType: 'json',
                data: JSON.stringify(formData),
                success: function (result) {
                    if (result.status == 'successful') {
                        dialog.dialog('close');
                        // set highlight value to be the id of created user
                        highlight = result.data.id;
                        userTable.draw();
                        $('#notification')
                            .append($('<div/>', {id: 'alert', class: 'alert alert-success text-center'})
                                .text(result.data.username + ' Created'));
                        $('#alert').fadeOut(8000, function () {
                            $(this).remove();
                        });
                    } else if (result.status == 'error') {
                        $.each(result.data, function (index, value) {
                            $('#errors').append(
                                $('<li/>').text(value.field + value.defaultMessage)
                            );
                        });

                        setTimeout(function () {
                            $('#errors').empty();
                        }, 10000);
                    }
                },
                error: function (e) {
                    alert("Error: " + e);
                }
            });
        }

        /*
       * update user when update is true, otherwise create a new one
       * */
        function updateUser() {

            // get roles
            var roles = [];
            $('#role :checked').each(function () {
                roles.push({id: $(this).val()});
            });

            var formData = {
                id: $('#id').val(),
                username: $('#username').val(), firstName: $('#firstName').val(),
                lastName: $('#lastName').val(), description: $('#description').val(), roles: roles
            };

            $.ajax({
                type: 'put',
                url: '/rest/user/update',
                headers: headers,
                contentType: 'application/json',
                dataType: 'json',
                data: JSON.stringify(formData),
                success: function (result) {
                    if (result.status == 'successful') {
                        dialog.dialog('close');
                        // set highlight value to be the id of created user
                        highlight = result.data.id;
                        userTable.draw();
                        $('#notification')
                            .append($('<div/>', {id: 'alert', class: 'alert alert-info text-center'})
                                .text(result.data.username + ' Updated'));
                        $('#alert').fadeOut(8000, function () {
                            $(this).remove();
                        });
                    } else if (result.status == 'error') {
                        $.each(result.data, function (index, value) {
                            $('#errors').append(
                                $('<li/>').text(value.field + value.defaultMessage)
                            );
                        });

                        setTimeout(function () {
                            $('#errors').empty();
                        }, 10000);
                    }
                },
                error: function (e) {
                    alert("Error: " + e);
                }
            });
        }


        function deleteUser() {
            var id = $('#id').val();

            $.ajax({
                type: 'delete',
                url: '/rest/user/delete/' + id,
                headers: headers,
                dataType: 'json',
                success: function (result) {
                    dialog.dialog('close');
                    userTable.draw();
                    $('#notification')
                        .append($('<div/>', {id: 'alert', class: 'alert alert-warning text-center'})
                            .text(result.data + ' Deleted'));
                    $('#alert').fadeOut(8000, function () {
                        $(this).remove();
                    });
                },
                error: function (e) {
                    alert("Error: " + e);
                }
            });
        }

        function editTable() {

            switch (action) {
                case 'create':
                    createUser();
                    break;
                case 'edit':
                    updateUser();
                    break;
                case 'delete':
                    deleteUser();
                    break;
                default:
                    break;
            }
        }

        dialog = $("#user-dialog").dialog({
            autoOpen: false,
            height: 'auto',
            width: 'auto',
            modal: true,
            buttons: [{
                id: 'action-button',
                text: 'Create User',
                click: function () {
                    editTable();
                }
            }, {
                text: 'Cancel',
                click: function () {
                    $(this).dialog('close');
                }
            }],
            close: function () {
                form[0].reset();
                $('#errors').empty();
            }
        });

        form = dialog.find("form").on("submit", function (event) {
            event.preventDefault();
        });

        // function to get selected row data and put into the dialog
        function populateDialog() {
            var user = userTable.rows({selected: true}).data()[0];
            $('#id').val(user.id);
            $('#username').val(user.username);
            $('#firstName').val(user.firstName);
            $('#lastName').val(user.lastName);
            $('#description').val(user.description);
            var roleIds = [];
            $.each(user.roles, function (index, value) {
                roleIds.push(value.id);
            });

            $('#role :checkbox').each(function (index, value) {
                if (roleIds.indexOf(parseInt($(this).val())) >= 0) {
                    $(this).prop('checked', true);
                }
            });
        }

        $.fn.dataTable.ext.buttons.create = {
            className: 'btn-create',
            text: 'Create',
            action: function () {
                dialog.dialog('option', 'title', 'Create User');
                dialog.dialog('open');
                action = 'create';
            }
        };
        $.fn.dataTable.ext.buttons.edit = {
            className: 'btn-edit',
            text: 'Edit',
            action: function () {
                dialog.dialog('option', 'title', 'Edit User');
                $('#action-button').button('option', 'label', 'Update User');
                $('#action-button').button('option', 'click', 'updateUser');
                dialog.dialog('open');
                action = 'edit';
                populateDialog();
            }
        };
        $.fn.dataTable.ext.buttons.delete = {
            className: 'btn-delete',
            text: 'Delete',
            action: function () {
                dialog.dialog('option', 'title', 'Delete User');
                $('#action-button').button('option', 'label', 'Delete User');
                dialog.dialog('open');
                populateDialog();
                action = 'delete';
                $('#username').prop('disabled', true);
            }
        };

        userTable = $('#user-table').DataTable({
            drawCallback: function () {
                // drawCallback is called first whenever initComplete is called.
                var api = this.api();
                api.buttons('.btn-edit').disable();
                api.buttons('.btn-delete').disable();
            },

            createdRow: function (row, data, index) {
                if (data.id == highlight) {
                    $(row).addClass('highlight');
                    // reset highlight
                    highlight = 0;
                }
                // reset background color to normal after 5 sec
                setTimeout(function () {
                    $(row).removeClass('highlight');
                }, 10000);
            },

            language: {
                url: tableLangUrl
            },
            dom: 'lBfrtip',
            buttons: [
                'create',
                'edit',
                'delete',
                'excel',
                'print',
                'pdf',
                {
                    extend: 'colvis',
                    columns: ':gt(0)'
                }
            ],
            responsive: true,
            select: true,

            processing: true,
            serverSide: true,
            paging: true,
            paginationType: 'full_numbers',
            order: [[0, "desc"]],
            ajax: {
                'url': listUserUrl,
                'type': "get",
                'dataType': 'json'
            },

            // MUST HAVE DATA ON COLUMNDEFS IF SERVER RESPONSE IS JSON ARRAY!!!
            columnDefs: [
                {name: 'id', targets: 0, visible: false, data: 'id'},
                {name: 'username', targets: 1, data: 'username', orderable: false},
                {name: 'firstName', targets: 2, data: 'firstName', searchable: true, orderable: false},
                {name: 'lastName', targets: 3, data: 'lastName', searchable: true, orderable: false},
                {name: 'description', targets: 4, data: 'description', searchable: true, orderable: false},
                {name: 'dateCreated', targets: 5, searchable: false, data: 'dateCreated'},
                {name: 'lastUpdated', targets: 6, searchable: false, data: 'lastUpdated'}
            ]
        });

        /**
         * because language.url is an ajax call, the table is initialized async, cannot set button options here.
         * have to use initComplete callback.
         * */
        // userTable.buttons([1,2]).disable(); // select multi buttons by index array
        // userTable.button(1).disable(); // select single button by index
        // userTable.buttons('.btn-edit').disable();
        // userTable.buttons('.btn-delete').disable();
        userTable
            .on('select', function (e, dt, type, indexes) {
                var rowData = userTable.rows(indexes).data().toArray();
                console.log('<div><b>' + type + ' selection</b> - ' + JSON.stringify(rowData) + '</div>');
                // userTable.buttons([1,2]).enable();
                userTable.buttons('.btn-edit').enable();
                userTable.buttons('.btn-delete').enable();
            })
            .on('deselect', function (e, dt, type, indexes) {
                var rowData = userTable.rows(indexes).data().toArray();
                console.log('<div><b>' + type + ' <i>de</i>selection</b> - ' + JSON.stringify(rowData) + '</div>');
                // userTable.buttons([1,2]).disable();
                userTable.buttons('.btn-edit').disable();
                userTable.buttons('.btn-delete').disable();
            });
    });
})(jQuery);