# MetaToScoreboard Plugin
オンラインプレイヤーのメタデータを取得し、スコアボードのスコアとして設定するspigotプラグイン
```yaml
/metatoscoreboard <objective> <metadatakey>
```

- objective : スコアボードのobjective名（存在しない場合は作成）
- metadatakey : 取得するメタデータのキー
  - metadataKey のみを指定した場合、すべてのプラグインのメタデータが対象
  - pluginname:metadataKey の形式の場合、指定プラグインのデータのみ対象

## スコア変換
プレイヤーのメタデータは、以下のルールに従ってスコアに変換されます

| メタデータの型	                           | 設定されるスコアの値          |
|------------------------------------|---------------------|
| Boolean (true/false)               | true → 1, false → 0 |
| Number (int, double, float, etc.)	 | int に変換             |
| Stringなどのその他のデータ                   | 	1                  |
| メタデータなし                            | -1                  |