(function() {
    
    function showSuccess() {
        reset();
        $("#resultAlert").addClass("alert-success");
        $(".alert").alert();
    }
    
    function showFailed() {
        reset();
        $("resultAlert").addClass("alert-danger");
        $(".alert").alert();
    }
    
    function reset() {
        $("#resultAlert").removeClass("alert-success").removeClass("alert-danger");
    }
    
    // ブラウザオープン時に警告を削除
    $(".alert").alert('close');

    $("#send-button").on("click", function(event) {
        // サーバにメッセージを送信する
        var str = '{"title":"' + $("#title").val() + '", "message":"' + $("#message").val() + '"}';
        var message = JSON.parse(str);
        
        $.ajax({
            url: "http://localhost:8080/PushNotificationDemo/rest/message",
            type: "POST",
            data: message,
            dataType: "json"
        }).done(function() {
            showSuccess();
        }).fail(function() {
            showFailed();
        });
    });
        

})();