<template type="text/template" id="modal-generic-modal">
    <div class="modal fade" tabindex="-1" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">{0}</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label=""
                            style="outline: none !important;">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    {{ body }}
                </div>
                <div class="modal-footer">
                    {{ footer }}
                    <button type="button" id="modal-generic-modal-footer-cancel" class="btn btn-secondary">
                        {{ cancel }}
                    </button>
                    <button type="button" id="modal-generic-modal-footer-confirm" class="btn btn-primary">
                        {{ confirm }}
                    </button>
                </div>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import {Component, Vue} from 'vue-property-decorator';

@Component
export default class Modal extends Vue {
}
</script>