(function() {
    var messageEndpoint, mailRequest, SPClient;

    // onConnect からコールバックされる関数:
    function spConnect() {
    	initDisplay();
    	
    	$("#messages").append("<h1>プッシュサーバに<br/>登録しました</h1>");

    	// PushServerの新規メッセージ通知エンドポイントURLを要求するために 'PushManager' を使う:
        messageRequest = navigator.push.register();

        // DOMRequestが成功した場合:
        messageRequest.onsuccess = function( event ) {
            // extract the endpoint object from the event: 
            messageEndpoint = event.target.result;

            // 最初の登録時には、プッシュエンドポイントURLを表示し、
            // アプリケーションサーバにエンドポイントを登録する。
            // 既に登録済みの場合はその旨を表示する。
            if ( messageEndpoint.pushEndpoint ) {
            	showEndpointURLForm(messageEndpoint.pushEndpoint);
            	$.ajax({
            		url: "http://localhost:8080/PushNotificationDemo/rest/endpoint",
            		type: "PUT",
                        contentType: "text/plain;charset=utf-8",
            		data: messageEndpoint.pushEndpoint
            	});
            	
            	// for debug
            	console.log(messageEndpoint.pushEndpoint);
            } else {
            	$("#messages").append("<p>既に登録済みです。</p>");
            }

            // channelIDを表示する...
            $("#messages").append("<p>ChannelID : " + messageEndpoint.channelID + "</p>");
            
            // 再接続ボタンの表示を消す
            $("#reconnect").hide();
        };

        // 通知ハンドラを設定する:
        navigator.setMessageHandler( "push", function( message ) {
            // 受け取った通知が 'messageEndopoint' のものか
            if ( message.channelID === messageEndpoint.channelID ) {
                // プッシュ通知に対して最新のメッセージを取得
            	// RESTでメッセージ取得し、デスクトップ通知にメッセージを表示
                console.log("version : " + message.version);
            	var url = "http://localhost:8080/PushNotificationDemo/rest/message?versionID=" + message.version;
            	$.get(url, function(message){
            	    showDesktopNotification(message);
            	});
            }
        });
    }
    
    function initDisplay() {
    	// メッセージを削除する
    	$("#messages").empty();
    }
    
    function showEndpointURLForm(endpointURL) {
    	$("#messages").append(
    			'<div class="form-group">' +
    			'<label for="endpoint">EndpointURL</label>' + 
    			'<input type="text" class="form-control" value="' + endpointURL + '">' +
    			'</div>');
    }
    
    function showDesktopNotification(message) {
    	if (window.webkitNotifications) {
    	    if (window.webkitNotifications.checkPermission() === 0) {
    	        var popup = window.webkitNotifications.createNotification(
                                'http://localhost:8080/PushNotificationDemo/img/duke.gif',
                                message.title,
                                message.message);
    		popup.show();
    	    }
    	} else {
    		console.log("WebNotifications not supported.");
    	}
    }

    // custom.....
    $("#reconnect").on("click", function(event) {
    	// WebSocket/SockJSコネクションが切れた場合、AeroGearには再接続する機能がある
        navigator.push.reconnect();
    });
        
    // onClose時のコールバック関数:
    function spClose() {
        $("#message").append("<p>コネクションが切断されました</p>");
        $("#reconnect").show();
    }
    

    SPClient = AeroGear.SimplePushClient({
        simplePushServerURL: "http://localhost:7777/simplepush",
        onConnect: spConnect,
        onClose: spClose
    });
})();
