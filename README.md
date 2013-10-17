PushNotificationDemo
====================

AeroGear SimplePushサーバと連携するデモアプリケーション。  
SimplePushサーバからのPush通知を契機にメッセージをAPサーバから取得し、デスクトップ通知を表示します。

構成
---------

![overview](https://raw.github.com/n-agetsu/PushNotificationDemo/master/site/overview.png)

### APサーバ

* [GlassFish4](https://glassfish.java.net)を使用。
* SimplePushサーバに通知を依頼するのが役割。

### SimplePushサーバ

* [AeroGear SimplePush](http://aerogear.org/docs/guides/aerogear-push-js/simplepush-server/)を使用。デフォルトでポート7777でサービスを提供する独立したサーバ。
* 事前に通知対象のクライアントを登録し、APサーバからの依頼に応じて通知を行うのが役割。

### クライアント

* デスクトップ通知を表示するためにwebkitNotificationsを使用しているため、Google Chromeを使用してください。


セットアップ方法
------------------------------

### 1. GlassFish4のインストール

GlassFish4は[ここから](https://glassfish.java.net/download.html)ダウンロードできます。インストールは非常にシンプルで、任意のディレクトリにunzipして展開するだけです。

    unzip glassfish-4.0.zip

### 2. アプリケーションのビルド

Apache Mavenを使ってビルドし、WARを生成します。

    git clone https://github.com/n-agetsu/PushNotificationDemo.git PushNotificationDemo
    cd PushNotificationDemo
    mvn package

argetディレクトリ配下にPushNotificationDemo.warが生成されたらビルドは完了です。

### 3. AeroGear SimplePushのインストールを起動

AeroGear SimplePushサーバは[GitHub](https://github.com/aerogear/aerogear-simplepush-server)でコードが公開されています。以下の手順で取得してビルドしてください。ビルドには[Apache Maven](http://maven.apache.org)が必要なので、事前にインストールしてください。

    git cllone https://github.com/aerogear/aerogear-simplepush-server.git aerogear-simplepush-server
    cd aerogear-simplepush-server
    mvn install -DskipTests=true

AeroGear SimplePushサーバはノンブロッキングIOを実現するサーバを元に動作しており、[Netty](http://netty.io)ベースと[VERT.x](http://vertx.io)ベースの2つの実装があります。デモではNettyベースのものを使用します。以下のコマンドで起動します。

    cd server-netty
    mvn exec:java -Dexec.args="-host=localhost -port=7777 -tls=false -ack_interval=10000 -useragent_reaper_timeout=60000 -token_key=yourRandomToken"

上記の設定ではバインド先のアドレスをlocalhostとしているため、同じマシンからしかSimplePushサーバにアクセスできません。複数の端末からアクセスさせたい場合は、-hostの設定を自端末のIPアドレスに変更してください。なんとこれだけで、SimplePushサーバのセットアップは終わりです。


### 4. GlassFishの起動とアプリケーションのインストール

GlassFishの管理コンソール http://localhost:4848 にアクセスしてアプリケーションをデプロイします。その前に、先ほどインストールしたGlassFishを起動してください。

    cd glassfish4/bin
    ./asadmin start-domain

起動後に [http://localhost:4848](http://localhost:4848) にアクセスすると、しばらくして以下の画面が表示されます。

![console](https://raw.github.com/n-agetsu/PushNotificationDemo/master/site/GlassFishManagementConsole.png)

画面中央付近の『Deploy an Application』を選択して、先ほどビルドしたPushNotificationDemo.warをデプロイしてください。

### 5. アプリケーションへのアクセス

Google Chromeから[http://localhost:8080/PushNotificationDemo/](http://localhost:8080/PushNotificationDemo/)にアクセスして以下のような画面が表示されたら、SimplePushサーバは正常に起動し、アプリケーションも正常にデプロイされています。

![index](https://raw.github.com/n-agetsu/PushNotificationDemo/master/site/index.png)

次に[http://localhost:8080/PushNotificationDemo/message.html](http://localhost:8080/PushNotificationDemo/message.html)アクセスして、プッシュ通知を契機に取得させたメッセージを入力します。

![message](https://raw.github.com/n-agetsu/PushNotificationDemo/master/site/message.png)

以下のように、Dukeからメッセージが表示されたらデモは成功です。

![notification](https://raw.github.com/n-agetsu/PushNotificationDemo/master/site/notification.png)

前述したように、SimplePushサーバのバインドIPアドレス(-host)を設定することで、複数人に対してメッセージ通知することもできます。是非遊んでみてください。

