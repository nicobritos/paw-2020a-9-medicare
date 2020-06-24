const Modal = function () {
    /**
     * @param options: {
     *     title: {String|HTML},
     *     body: {String|HTML},
     *     footer: {String|HTML|null},
     *     accept: {String},
     *     cancel: {String},
     *     callbacks: {null|{
     *         confirm: {Function|null},
     *         cancel: {Function|null},
     *         preventDestroyOnCancel: {Boolean|null}
     *     }}
     * }
     * @returns {*|jQuery.fn.init|jQuery|HTMLElement}
     */
    let create = function (options) {
        let title = options.title || '';
        let body = options.body || '';
        let footer = options.footer || '';
        let accept = options.accept || '';
        let cancel = options.cancel || '';
        let confirmCallback, cancelCallback;
        let preventDestroyOnCancel;
        let confirmed = false;

        if (options.callbacks) {
            confirmCallback = options.callbacks.confirm || null;
            cancelCallback = options.callbacks.cancel || null;
            preventDestroyOnCancel = options.callbacks.preventDestroyOnCancel || false;
        } else {
            confirmCallback = null;
            cancelCallback = null;
            preventDestroyOnCancel = false;
        }

        let isDefaultFooter = footer === '';

        if (typeof body !== 'string')
            body = $(body).html();
        if (isDefaultFooter) {
            footer = $('#modal-generic-modal-footer');
        } else {
            footer = $(footer);
        }

        let modal = $($('#modal-generic-modal').html().format(title, body, footer.html().format(accept, cancel)));
        if (isDefaultFooter) {
            if (confirmCallback) {
                modal.find('#modal-generic-modal-footer-confirm').click(function () {
                    confirmed = true;
                    confirmCallback();
                });
            }
            modal.find('#modal-generic-modal-footer-cancel').click(function () {
                if (cancelCallback) cancelCallback();
                else if (!preventDestroyOnCancel) destroy(modal);
            });
        }
        if (cancelCallback) {
            modal.on('hidden.bs.modal', function () {
                if (!confirmed)
                    cancelCallback();
            });
        }
        modal = $(modal);
        modal.modal('show');
        return modal;
    };

    /**
     * @param options: {
     *     title: {String|HTML},
     *     body: {String|HTML},
     *     footer: {String|HTML|null},
     *     accept: {String},
     *     cancel: {String},
     *     callbacks: {null|{
     *         confirm: {Function|null},
     *         cancel: {Function|null}
     *     }}
     * }
     * @returns {*|jQuery.fn.init|jQuery|HTMLElement}
     */
    let confirm = function (options) {
        return create(options);
    };

    let destroy = function (modal) {
        modal = $(modal);
        modal.on(
            'hidden.bs.modal',
            function () {
                modal.remove();
            }
        );
        modal.modal('hide');
    };

    return {
        create: create,
        confirm: confirm,
        destroy: destroy
    };
}();
