package com.example.RussiaSquareGame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import com.example.RussiaSquareGame.ELoS;
import org.w3c.dom.Text;

/**
 * Created by peng on 2017/1/21.
 */
public class GameOverActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.game_over);
        Button restart=(Button)findViewById(R.id.restart);
        Button exit=(Button)findViewById(R.id.exit);
        TextView grade=(TextView)findViewById(R.id.grade);
        TextView stage=(TextView)findViewById(R.id.stage);
        grade.setText("Game grade:"+ELoS.gameGrade);
        stage.setText("Game stage:"+ELoS.gameStage);
        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ELoS.gameOverActivityRunningFlag=false;
                finish();
                Intent intent = new Intent(GameOverActivity.this, ELoS.class);
                startActivity(intent);
            }
        });
        exit.setOnClickListener(new View.OnClickListener(){
            @Override
        public void onClick(View v)
            {
               finish();
            }
        });
    }
}
