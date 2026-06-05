# GravityBlocks プラグイン

設置したブロックが自動で落下して地面に積み上がる Spigot/Paper プラグインです。

---

## 機能

- `/gravity` コマンドで**重力モードのON/OFF**を切り替え
- 重力モードON中は、設置したブロックがすべて落下エンティティ（FallingBlock）として生成され、地面まで落ちてそのままブロックとして積み上がります
- サバイバルモードではインベントリのブロックが正しく消費されます
- クリエイティブモードでも動作します
- エンティティへのダメージなし（安全）

## コマンド

| コマンド | 説明 |
|---|---|
| `/gravity` | 重力モードのON/OFFをトグル |
| `/gravity on` | 重力モードをONにする |
| `/gravity off` | 重力モードをOFFにする |

## 権限

| 権限ノード | 説明 | デフォルト |
|---|---|---|
| `gravityblocks.use` | /gravityコマンドの使用 | 全員 |

---

## ビルド手順

### 必要なもの
- Java 17 以上（JDK）
- Maven 3.x
- インターネット接続（Spigot API の自動ダウンロードのため）

### 手順

```bash
# 1. このフォルダに移動
cd GravityBlocks

# 2. Maven でビルド
mvn package

# 3. output/ フォルダに GravityBlocks-1.0.0.jar が生成されます
```

### サーバーへのインストール

```
GravityBlocks-1.0.0.jar を Minecraft サーバーの plugins/ フォルダにコピーし、サーバーを再起動
```

### 対応バージョン
- Spigot / Paper 1.20.x 以上推奨
- Java 17 以上が必要

---

## ファイル構成

```
GravityBlocks/
├── pom.xml
└── src/main/
    ├── java/com/example/gravityblocks/
    │   ├── GravityBlocks.java        ← メインクラス・コマンド処理
    │   └── BlockPlaceListener.java   ← ブロック設置イベント処理
    └── resources/
        └── plugin.yml
```
