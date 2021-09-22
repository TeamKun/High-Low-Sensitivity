# High-Low-Sensitivity
ハイ・ローセンシMOD

1.16.5 Forge
# 仕様

コンフィグファイルはworldフォルダ内のserverconfig内のhighlowsensitivity-server.toml  
	"high sensitivity"が最大感度  
	"low sensitivity"が最低感度  
	"high sensitivity cont"が最大感度になる人数  
	"low sensitivity cont"が最低感度になる人数  

個人ことに個別に感度を固定したり指定することが可能  
プレイヤーセレクターにはバニラのセレクターを使用（@pで自分、@aで全員、@rでランダム)  
modeはコンフィグファイルで設定した通りにプレイヤーを選び感度を変える  
randomは選択したプレイヤーをHIGHかLOWにランダムで変える  
わからなくなったらとりあえずリセットすればすべて元に戻ります

* sensitivity
    * lock プレイヤーセレクター　プレイヤーの感度を固定する
    * unlock プレイヤーセレクター　プレイヤーの感度を固定を解除する
    * set 感度(200がバニラで最大、0が最低) プレイヤーセレクター　プレイヤーの感度を指定の値で固定する
    * mode  
          * on コンフィグファイルで設定したとおりに選ぶ  
          * off すべて戻す
    * random プレイヤーセレクター 選択したプレイヤーををランダムでHIGHかLOWにする  
    * reset すべてをリセットする
# 例　
コンフィグで設定したとおりに選ぶ  
/sensitivity mode on

全員をランダムにHIGHかLOWにする
/sensitivity random @a

全員の感度を固定する  
/sensitivity lock @a

全員の感度を100%で固定する  
/sensitivity set 100 @a

全員の感度の固定を解除する  
/sensitivity unlock @a

感度の固定をリセットする  
/sensitivity reset
