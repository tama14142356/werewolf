## werewolf

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
