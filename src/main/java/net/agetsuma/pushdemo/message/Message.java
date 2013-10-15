package net.agetsuma.pushdemo.message;

/**
 * 新着メッセージ。
 * @author Norito AGETSUMA
 */
public class Message {
    
    /** メッセージのタイトル */
    private String title;
    
    /** メッセージ */
    private String message;
    
    /**
     * コンストラクタ。
     * @param title メッセージのタイトル
     * @param message メッセージ
     */
    public Message(String title, String message) {
        this.title = title;
        this.message = message;
    }
	
    /**
     * タイトルを取得
     * @return タイトル
     */
    public String getTitle() {
        return title;
    }
    
    /**
     * メッセージを取得
     * @return メッセージ
     */
    public String getMessage() {
        return message;
    }

}
