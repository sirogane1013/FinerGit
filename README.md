# FinerGit （日本語）
FinerGit は Git のメカニズムを用いて Java メソッドに対して行われた変更を調査するためのツールです．
FinerGit の入力は Java ソースコードを含む Git リポジトリです．
FinerGit の出力は，以下の2つの特徴をもった Git リポジトリです．
- ソースコード内の各 Java メソッドが1つのファイルとして抽出されている．
- 抽出された各 Java メソッドの各行には1つの字句のみからなる．

1つ目の特徴により，Git のファイル追跡機能を使って Java メソッドを追跡することができます．
2つ目の特徴により，Java メソッドの追跡可能性を高めることができます．

## 準備

1. [GitHub の FinerGit のページ](https://github.com/kusumotolab/FinerGit)にアクセスし，ローカルストレージに FinerGit を clone する．
2. git-subcommand 内のファイル（FinerGit.jar，git-fg，git-msv，git-sv）を環境変数 PATH が通ったディレクトリ以下にコピーする．

ターミナルを起動し，`git fg` とタイプし，使い方が表示されればOKです．

## Git リポジトリを FinerGit リポジトリに変換する

変換は以下のコマンドで行います．

> git fg --src repoA --des repoB

ここで，`repoA`は既存の Git リポジトリ，`repoB`は新しく作成する FinerGit リポジトリです．
`repoB` には存在しないファイルパスを指定してください．

なお，

> git fg


と，引数無しでタイプすれば，利用可能なオプションが表示されます．

## FinerGit リポジトリを使って Java メソッドの変更履歴を確認する

FinerGit リポジトリには拡張子が`.cjava`や`.mjava`なファイルが含まれています．

- 拡張子が`.cjava`なファイルは，Java のクラスを表すファイルです．ただし，その中に定義されているメソッドは別ファイルとして抽出されています．
- 拡張子が`.mjava`なファイルは，Java のメソッドを表すクラスです．なお，Java メソッドのファイル名は，`クラス名$メソッドシグネチャ.mjava`となっています．

例えば，
> git log Hoge$fuga().mjava

というコマンドを入力すると，`fuga()`メソッドに変更を加えたコミットの一覧を得ることができます．

> git log --follow Hoge$fuga().mjava

というように，``--follow``オプションを利用すれば，メソッド名やそれを含むクラス名が変わっていた場合でも追跡して，コミット一覧を表示します．

## FinerGit リポジトリを使って Java メソッドのセマンティックバージョンを取得する

ひとことで言うと，[セマンティックバージョニング](https://semver.org/lang/ja/)とは，ソフトウェアのバージョニングを以下のルールに基づいて行うことです．
- ソフトウェアのバージョンは，`a.b.c`の3つの組で表す．
- `a`はソフトウェアに後方互換性が無い変更がされた場合に1つ増やす．なお，このとき，`b`および`c`は0に戻す．
- `b`はソフトウェアに後方互換性の有る変更がされた場合に1つ増やす．なお，このとき，`a`は変更せず，`c`は0に戻す．
- `c`はソフトウェアのバグ修正が行われた場合に1つ増やす．なお，このとき，`a`と`b`は変更しない．

FinerGit は上記の，**ソフトウェアに対するセマンティックバージョニングを Java メソッドに対して応用する機能**を持っています．
なお，FinerGit では，Java メソッドにおける後方互換性の無い変更，後方互換性の有る変更，バグ修正は以下の定義としています．
- Java メソッドの，名前，仮引数，返り値，アクセス修飾子のいずれかが変更された場合，後方互換性の無い変更とする．
- Java メソッドの，名前，仮引数，返り値，アクセス修飾子のいずれもが保たれた変更が行われた場合，後方互換性の有る変更とする．
- 後方互換性のある変更のうち，その変更のコミットメッセージに bug や fix 等のバグ修正を連想させる単語が含まれている場合，バグ修正とする．

Java メソッドに対してセマンティックバージョニングを算出することで，そのメソッドがこれまでに何度シグネチャ変更されているのか，最後に機能追加されてから何度バグフィックスされているのか，といったことがわかります．
FinerGit では，Java メソッドのセマンティックバージョニングの算出には，`git-sv`コマンドを使います．

例えば，
> git sv Hoge$fuga().mjava

と入力すれば，`fuga()`メソッドのセマンティックバージョニングが出力されます．また，git-sv コマンドにはいくつかのオプションがあり，セマンティックバージョニング以外にも，これまでの変更の総数等を表示することができます．`git sv`とファイル名なしでコマンドを実行すると，利用可能なオプション一覧が表示されます．

## 複数のファイルに対してセマンティックバージョンを効率的に算出する

セマンティックバージョンの算出はある程度重い処理であり，場合によっては数秒程度かかることがあります．
また，このコマンドは内部で JavaVM を起動しているため，何度も連続して`git-sv`を実行する場合，そのプロセス起動オーバーヘッドも無視できない時間となります．
そのため，複数ファイルに対してセマンティックバージョンを算出したい場合には，`git-sv`ではなく，`git-msv`を使うとプロセス起動オーバーヘッドを除外することができます．
`git-msv`を実行する場合は，その引数には，各行にJavaファイルへのパスを記入したリストファイルを指定してください．Javaファイルへのパスは，相対パスでも指定できますが，絶対パスで指定することをオススメします．








