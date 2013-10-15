package net.agetsuma.pushdemo.message;

import javax.inject.Inject;
import javax.inject.Singleton;

import net.agetsuma.pushdemo.notify.EndpointManager;

/**
 * メッセージ管理クラス。
 * メッセージの追加、新着メッセージの取得などのメッセージに関する操作は
 * このクラスを経由して行います。
 * @author Norito AGETSUMA
 */
@Singleton
public class MessageService {
    
    /** メッセージへのデータアクセスを抽象化したインスタンス */
    @Inject
    private MessageRepository messageRepository;
    
    /** エンドポイントURL管理インスタンス */
    @Inject
    private EndpointManager endpointManager;
	
    /**
     * 引数の新着メッセージを保存した後、
     * 全てのエンドポイントに更新通知します。
     * @param message 新着メッセージ
     */
    public void notifyAllEndpoints(Message message) {
        long versionID = messageRepository.saveMessage(message);
        endpointManager.notifyAllEndpoints(versionID);
    }
    
    /**
     * 指定されたバージョンのメッセージを取得する。
     * @return 引数に指定されたバージョンのメッセージ
     */
    public Message findMessage(long versionId) {
        return messageRepository.findMessage(versionId);
    }
}
