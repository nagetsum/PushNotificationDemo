package net.agetsuma.pushdemo.notify;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import javax.inject.Singleton;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.InvocationCallback;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.*;

/**
 * SimplePushのエンドポイント管理クラス。
 * エンドポイントの追加、通知などの操作はこのクラスを通して行う。
 * @author Norito AGETSUMA
 */
@Singleton
public class EndpointManager {
	
    /** 
     * 登録済みエンドポイント。デモなのでシンプルにヒープ上に保持させる。
     * MapのkeyはエンドポイントURL、valueはEndpointオブジェクト。
     */
    private ConcurrentHashMap<String, Endpoint> endpoints = new ConcurrentHashMap<>();
	
    /**
     * 新規エンドポイントの追加。
     * @param newEndpoint 追加対象のエンドポイント
     */
    public void register(Endpoint newEndpoint) {
        endpoints.put(newEndpoint.getEndpointURL(), newEndpoint);
    }
	
    /**
     * 非同期処理で全ての登録済みエンドポイントに対して通知を行う。
     * @param 通知するバージョンID
     */
    public void notifyAllEndpoints(long versionId) {
        // 非同期で全てのエンドポイントに通知する
        for (String key : endpoints.keySet()) {
            Endpoint endpoint = endpoints.get(key);
            notifyToAeroGearSimplePush(versionId, endpoint);
        }
    }
	
    /**
     * SimplePushサービスに非同期でプッシュ通知を依頼する。
     * 対象のendpointURLに対して version=? をPUTすることで通知が行える。
     * 成功した場合、SimplePushサーバより200-OK(空のボディ)が返される。
     * @param versionID 通知するバージョン
     * @param targetEndpoint 通知対象エンドポイント
     * @return レスポンスのFutureオブジェクト
     */
    private Future<Response> notifyToAeroGearSimplePush(long versionID, final Endpoint targetEndpoint) {
        
        Client client = ClientBuilder.newClient();	
        WebTarget target = client.target(targetEndpoint.getEndpointURL());

        Entity<String> versionId = Entity.entity("version=" + versionID, MediaType.APPLICATION_FORM_URLENCODED);
		
        // 非同期でEndpointURLに対してPUTリクエスト
	Future<Response> res = target.request().async().put(versionId, new InvocationCallback<Response>() {
            /** 非同期リクエスト完了時のコールバックメソッド。 */
            @Override
            public void completed(Response res) {
                // debug
                if (res.getStatusInfo() == OK) {
                    System.out.println("notify success EndpointURL: " + targetEndpoint.getEndpointURL());
                } else {
                    System.out.println("notify failed EndpointURL: " + targetEndpoint.getEndpointURL());
                }
            }
            /** 非同期リクエスト失敗時のコールバックメソッド。 */
            @Override
            public void failed(Throwable th) {
                // debug
                System.out.println("notify failed EndpointURL: " + targetEndpoint.getEndpointURL());
                System.out.println(th);
            }
        });

        return res;
    }
}
