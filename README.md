## 人狼ゲーム(werewolf game)の説明
このゲームは市民陣営と人狼陣営の2チームに分かれて、話し合いによって競い合うゲームです。  
各プレイヤーに役割が割り振られ、それぞれ以下の特殊能力が存在します。  
- 市民陣営
  - 市民&emsp;(Citizen)&emsp;&emsp;&emsp;: 特になし  
  - 占い師(Fortuneteller)&nbsp;: 生存者の陣営を把握  
  - 霊媒師(Psychic)&emsp;&emsp;&nbsp;&nbsp;&thinsp;: 死亡者の陣営を把握  
  - 騎士&emsp;(Knight)&emsp;&emsp;&emsp;: 生存者を人狼より保護  
- 人狼陣営
  + 人狼&emsp;(Werewolf)&emsp;&emsp;: 市民陣営の生存者を殺害  

また、このゲームには以下の3場面が存在します。昼以外は話し合いません。  
- 昼&emsp;&nbsp;: 話し合いをする場面です。夕方の投票を行う際のヒントとなる情報の収集を行います。
- 夕方&nbsp;: 処刑する人を投票する場面です。この投票による多数決で処刑する人を決めます。
- 夜&emsp;&nbsp;: 役割を持つ市民または人狼がそれぞれの特殊能力を発揮できる時間帯です。  

このプログラムでは対戦人数は以下の6人に固定しました。
- 市民陣営4人(市民、占い師、霊媒師、騎士、各1名ずつ)
- 人狼陣営2人(人狼2名)

前提条件は以下の通りです。
- 人狼陣営が誰であるかは人狼陣営にしか分からない。
- 上記以外でお互いの役割が何かは分からない。(能力により分かる場合もある。)

終了条件は以下の2通りです。
- 全ての人狼を処刑した場合、市民陣営の勝利で終了
- 人狼陣営と市民陣営の人数が同数以下になった場合、人狼陣営の勝利で終了

## 起動方法
実行する際は、Server_Main.java,Client_Main.javaをコンパイルして、
コマンドプロンプトを７つ起動して、一つのコマンドプロンプトにおいて、java Server_Main
６つそれぞれについてはjava Client_Main <username>を実行してください
  
  usernameは６つそれぞれで異なるようにしてください
  異なってないと、誤動作を起こします。
  
起動した後、接続ボタンを押すとゲームがスタートします

昼 : チャットが利用できます。  
夜、夕方 : チャットは送れません。  

ローカルで実行なら、変更しなくていいですが、２台以上でつなぐ場合は、Client_Mainのstartconnection()メソッドの"localhost"を自分のホスト名に変更してください。

## 実装されている役割
- 市民(能力を全く持たない市民)
- 人狼
- 霊媒師
- 占い師
- 騎士

## 起動時の役割分担・人数比
起動時にランダムで実装されている役割の中から役割が割り当てられます。  
人数比としては以下のようになります。  
市民 : 人狼 : 占い師 : 霊媒師 : 騎士 = 1 : 2 : 1 : 1 : 1  
また、現在の実装としては参加者の人数は6人で固定となっています。その為、この比率はそのまま人数の実数値と同値になります。

## 動作時のスクリーンショット例
![動作時のスクリーンショット](https://github.com/tama14142356/werewolf/blob/master/img/werewolf.jpg)
