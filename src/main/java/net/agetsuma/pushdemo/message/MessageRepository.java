package net.agetsuma.pushdemo.message;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import javax.inject.Singleton;

/**
 * メッセージに関するデータアクセスを抽象化するクラス。
 * デモアプリのため、データはJPA等で永続化せずに
 * フィールド上のConcurrentHashMapで管理しています。
 * @author Norito AGETSUMA
 */
@Singleton
public class MessageRepository {
	
    /**
     * メッセージ。
     * デモなので永続化せずにシンプルにヒープに持たせる。
     * keyはメッセージのバージョン、valueはメッセージオブジェクト。
     */
    private AtomicLong version = new AtomicLong();
    private ConcurrentHashMap<Long, Message> messages = new ConcurrentHashMap<>();
    
    /**
     * 指定されたバージョンIDを持つメッセージを返します。
     * @return 引数に指定されたバージョンのメッセージ
     */ 
    public Message findMessage(long versionId) {
        return messages.get(versionId);
    }
	
    /**
     * 新着メッセージを保存します。
     * @param message 新着メッセージ
     * @return 保存されたメッセージのバージョンID
     */
    public long saveMessage(Message message) {
        // バージョンを1インクリメントした後、メッセージを保存
        long v = version.incrementAndGet();
        messages.put(v, message);
        return v;
    }
    
    /**
     * 現在の最新メッセージのバージョンを取得する。
     * @return 最新メッセージのバージョン
     */
    public long getCurrentVersion() {
        return version.get();
    }
}
