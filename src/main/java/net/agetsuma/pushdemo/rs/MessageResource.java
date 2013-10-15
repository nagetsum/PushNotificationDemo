package net.agetsuma.pushdemo.rs;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import net.agetsuma.pushdemo.message.Message;
import net.agetsuma.pushdemo.message.MessageService;

/**
 * メッセージに関するリソースのエンドポイント。
 * @author Norito AGETSUMA
 */
@Path("/message")
@RequestScoped
public class MessageResource {
	
    /** メッセージ処理に関するServiceクラス */
    @Inject
    private MessageService messageService;
	
    /**
     * POSTされたメッセージを、全ての登録済みEndpointに通知します。
     * 完了後、200 - OK を返します。
     * @param title メッセージのタイトル
     * @param message メッセージ
     */
    @POST
    @Consumes("application/x-www-form-urlencoded")
    public Response registMessageAndNotifyAll(@FormParam("title") String title,
                                              @FormParam("message") String message) {
        messageService.notifyAllEndpoints(new Message(title, message));
        return Response.ok().build();
    }
    
    /**
     * 引数に指定されたバージョンのメッセージを取得します。
     * @param versionId バージョンID
     * @return メッセージ
     */
    @GET
    public Message findMessage(@QueryParam("versionID") long versionId) {
        return messageService.findMessage(versionId);
    }

}
