package net.agetsuma.pushdemo.rs;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import net.agetsuma.pushdemo.notify.Endpoint;
import net.agetsuma.pushdemo.notify.EndpointManager;

/**
 * SimplePuthエンドポイントの登録を行うクラス。
 * @author Norito AGETSUMA
 */
@Path("/endpoint")
@RequestScoped
public class EndpointResource {
	
	@Inject
	private EndpointManager endpointManager;
	
        /**
         * 引数に設定されたSimplePushサーバのエンドポイントを
         * 通知対象として登録します。
         * @param endpointURL エンドポイントURL
         * @return 空ボディのステータスコード200(OK)
         */
	@PUT
	@Consumes("text/plain;charset=utf-8")
	public Response register(String endpointURL) {
		endpointManager.register(new Endpoint(endpointURL));
		
                // debug
		System.out.println("URL : " + endpointURL);
		
		// 200 - OKを返す
		return Response.ok().build();
	}

}
