const App = function() {
    let showOk = function (messages) {
        if (messages == null) {
            return;
        }

        for (let message of messages) {
            $.notify({
                title: '<b>Exito: </b>',
                message: message,
                icon: 'fa fa-tick'
            }, {
                type: 'success',
                newest_on_top: true,
                timer: 300,
                icon_type: 'fa'
            });
        }
    };

    let showError = function (messages) {
        if (messages == null) {
            messages = ['Un error ha ocurrido'];
        }

        for (let message of messages) {
            $.notify({
                title: '<b>Error: </b>',
                message: message,
                icon: 'fa fa-warning'
            }, {
                type: 'danger',
                newest_on_top: true,
                timer: 300
            });
        }
    };

    let ajax = function(url, parameters, method = 'GET') {
        return new Promise((resolve, reject) => {
            let errorCallback = function (data) {
                showError(data.status.messages);
                reject(data);
            };

            let options = {
                url: url,
                type: method,
                success: function (data) {
                    if (data.status.error) {
                        errorCallback(data);
                    } else {
                        resolve(data);
                    }
                },
                error: function () {
                    showError();
                    reject();
                }
            };
            if ($.isPlainObject(parameters)) {
                options.data = JSON.stringify(parameters);
                options.dataType = 'json';
                options.contentType = 'application/json';
            } else {
                options.data = parameters;
            }

            $.ajax(url, options);
        });
    };

    return {
        get: function(url, parameters = {}) {
            return ajax(url, parameters);
        },
        post: function (url, parameters = {}) {
            return ajax(url, parameters, 'POST');
        }
    };
}();
