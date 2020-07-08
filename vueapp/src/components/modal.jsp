<%@ page contentType="text/html;charset=UTF-8" %>

<script type="text/template" id="modal-generic-modal">
    <div class="modal fade" tabindex="-1" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">{0}</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="" style="outline: none !important;">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    {1}
                </div>
                <div class="modal-footer">
                    {2}
                </div>
            </div>
        </div>
    </div>
</script>

<script type="text/template" id="modal-generic-modal-footer">
    <button type="button" id="modal-generic-modal-footer-cancel" class="btn btn-secondary">{1}</button>
    <button type="button" id="modal-generic-modal-footer-confirm" class="btn btn-primary">{0}</button>
</script>
