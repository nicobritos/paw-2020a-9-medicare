const App = function () {
    let baseUrl = $("base")[0].href;
    baseUrl = baseUrl.substring(0, baseUrl.length - 1);

    let showOk = function (messages, title = app_strings['ok']) {
        if (messages == null) {
            return;
        }
        if (!$.isArray(messages))
            messages = [messages];

        for (let message of messages) {
            $.notify({
                title: '<b>' + title + ': </b>',
                message: message,
                icon: 'fa fa-tick'
            }, {
                type: 'success',
                newest_on_top: true,
                timer: 300,
            });
        }
    };

    let showError = function (messages, title = 'Error') {
        if (messages == null) {
            messages = [app_strings['error']];
        }

        if (!$.isArray(messages))
            messages = [messages];
        for (let message of messages) {
            $.notify({
                title: '<b>' + title + ': </b>',
                message: message,
                icon: 'fa fa-warning'
            }, {
                type: 'danger',
                newest_on_top: true,
                timer: 300
            });
        }
    };

    let ajax = function (url, parameters, method = 'GET') {
        return new Promise((resolve, reject) => {
            let errorCallback = function (data) {
                showError(data.status.messages);
                reject(data.status.messages);
            };

            let options = {
                url: url,
                type: method,
                success: function (data) {
                    if (data.status.error) {
                        errorCallback(data);
                    } else {
                        resolve(data.data);
                    }
                },
                error: function () {
                    showError();
                    reject();
                }
            };
            if (Object.keys(parameters).length > 0) {
                if ($.isPlainObject(parameters)) {
                    if (method.toLowerCase() == 'get') {
                        options.data = parameters;
                    } else {
                        options.data = JSON.stringify(parameters);
                    }
                    options.dataType = 'json';
                    options.contentType = 'application/json';
                } else {
                    options.data = parameters;
                }
            }

            $.ajax(url, options);
        });
    };

    let goto = function (url, preserveParameters) {
        if (url.startsWith('/') || url.startsWith('http://') || url.startsWith('https://')) {
            if (preserveParameters)
                location.href = url + location.search;
            else
                location.href = url;
        } else if (url.startsWith('..')) {
            let pathname = location.pathname;
            if (pathname.endsWith('/') || pathname.endsWith('#')) pathname = pathname.substring(0, pathname.length - 1);

            let oldPaths = pathname.split('/');
            let newPaths = url.split('/');

            while (newPaths.length) {
                if (newPaths[0] === '') {
                    newPaths.shift();
                } else {
                    if (newPaths[0] !== '..') break;
                    oldPaths.pop();
                    newPaths.shift();
                }
            }

            location.pathname = oldPaths.concat(newPaths).join('/');
        } else {
            let pathname = location.pathname;
            if (pathname.endsWith('/') || pathname.endsWith('#')) pathname = pathname.substring(0, pathname.length - 1);
            location.pathname = [pathname, url].join('/');
        }
    };

    return {
        init: function () {
            // First, checks if it isn't implemented yet.
            if (!String.prototype.format) {
                String.prototype.format = function () {
                    var args = arguments;
                    return this.replace(/{(\d+)}/g, function (match, number) {
                        return typeof args[number] != 'undefined'
                            ? args[number]
                            : match
                            ;
                    });
                };
            }

            $(document).ready(function () {
                $('.btn-loading').on('click', function () {
                    $(this).append(' <div class="spinner-border loading-spinner-button" role="status"></div>');
                });
            });
        },
        get: function (url, parameters = {}) {
            if (url[0] === "/") {
                return ajax(baseUrl + url, parameters);
            } else {
                return ajax(url, parameters);
            }
        },
        post: function (url, parameters = {}) {
            if (url[0] === "/") {
                return ajax(baseUrl + url, parameters, 'POST');
            } else {
                return ajax(url, parameters, 'POST');
            }
        },
        showError: showError,
        showOk: showOk,
        goBack: function () {
            history.back();
        },
        goto: function (url, preserveParameters = false) {
            if (url[0] === "/") {
                return goto(baseUrl + url, preserveParameters)
            } else {
                return goto(url, preserveParameters);
            }
        }
    };
}();