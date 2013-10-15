package net.agetsuma.pushdemo.notify;

/**
 * SimplePushサーバのエンドポイントを示すクラス。
 * エンドポイントはSimplePushサーバにプッシュ通知依頼をした返り値として
 * 渡される一意なURLである。このURLに対してAPサーバ->SimplePushサーバに
 * HTTP PUTを行うことで、SimplePushサーバは対象のユーザエージェントに通知を行う。
 * 用語の定義については、MozillaWikiに詳しく解説されている。
 * 
 * @see https://wiki.mozilla.org/WebAPI/SimplePush
 * @author Norito AGETSUMA
 */
public class Endpoint {

    /**
     * SimplePushサーバのエンドポイントURL
     */
    private String endpointURL;
    
    /**
     * コンストラクタ。
     * @param endpointURL エンドポイントURL 
     */
    public Endpoint(String endpointURL) {
        this.endpointURL = endpointURL;
    }

    /**
     * エンドポイントURLを取得する
     * @return エンドポイントURL
     */
    public String getEndpointURL() {
        return endpointURL;
    }
	
}
