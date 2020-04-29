const Modal = function() {
    let create = function (title, body, footer = '', confirmCallback = null, cancelCallback = null) {
        let isDefaultFooter = footer == '';

        body = $(body);
        if (isDefaultFooter) {
            footer = $('#modal-generic-modal-footer');
        } else {
            footer = $(footer);
        }

        let modal = $($('#modal-generic-modal').text().format(title, body.text(), footer.text()));
        if (isDefaultFooter) {
            if (confirmCallback) {
                modal.find('#modal-generic-modal-footer-confirm').click(function() {
                    confirmCallback();
                });
            }
            modal.find('#modal-generic-modal-footer-cancel').click(function() {
                if (cancelCallback) cancelCallback();
                else destroy(modal);
            });
        }
        return modal;
    };

    let destroy = function(modal) {
        modal.on(
            'hidden.bs.modal',
            function() {
                modal.remove();
            }
        );
        modal.modal('hide');
    };

    return {
        create: create,
        destroy: destroy
    };
}();
