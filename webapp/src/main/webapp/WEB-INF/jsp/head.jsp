<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%--TODO--%>
<%--<link rel="stylesheet" href='<c:url value="/css/styles.css"/> '>--%>
<script src='<c:url value="/js/plugins/jquery-3.5.0.min.js"/>'></script>
<base href='<c:url value="/"/>' id="baseUrl">
<script src='<c:url value="/js/plugins/bootstrap-notify.min.js"/>'></script>
<script src='<c:url value="/js/components/App.js"/>'></script>
<script src='<c:url value="/js/components/Modal.js"/>'></script>
<script>
    App.init();
</script>
<meta name="support-button-msg" content="<spring:message code="HereToHelp"/>">
<script type="text/javascript">
    function button() {
        var options = {
            facebook: "1112883855467682", // Facebook page ID
            whatsapp: "+54 9 11 6939-7444", // WhatsApp number
            call_to_action: getMetaContent('support-button-msg'), // Call to action
            button_color: "#333", // Color of button
            position: "right", // Position may be 'right' or 'left'
            order: "whatsapp", // Order of buttons
        };
        var proto = document.location.protocol, host = "whatshelp.io", url = proto + "//static." + host;
        var s = document.createElement('script'); s.type = 'text/javascript'; s.async = true; s.src = url + '/widget-send-button/js/init.js';
        s.onload = function () { WhWidgetSendButton.init(host, proto, options); };
        var x = document.getElementsByTagName('script')[0]; x.parentNode.insertBefore(s, x);
    }

    function getMetaContent(metaName) {
        let metas = document.getElementsByTagName('meta');
        for (let i = 0; i < metas.length; i++){
            if(metas[i].getAttribute('name') === metaName){
                return metas[i].getAttribute('content');
            }
        }
        return '';
    }

    button();
</script>

<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.13.0/css/all.min.css" crossorigin="anonymous">
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T"  crossorigin="anonymous">
<link rel="stylesheet" href='<c:url value="/css/plugins/animate.css"/> '>

<meta charset="utf-8">
<meta name="description" content="MediCare.">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
<link rel='icon' href='<c:url value="/img/logo.svg"/>' type='image/x-icon'>
<title>MediCare</title>
