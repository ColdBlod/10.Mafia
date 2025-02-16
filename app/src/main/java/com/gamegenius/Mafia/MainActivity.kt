package com.gamegenius.Mafia

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.View.OnKeyListener
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.gamegenius.Mafia.ui.theme._10MafiaTheme

class MainActivity : ComponentActivity() {
    private var players: MutableList<Int> = mutableListOf<Int>(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        actionBar?.hide();
        setContentView(R.layout.lst_of_players_start)
        this.inzializate_start()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }
    }

    fun inzializate_start(){
        setContentView(R.layout.main)

        var lb: TextView = this.findViewById(R.id.info_inizial_lb)
        lb.setText("Городская мафия; Царский стол\n8 февраля 19:00 - 00:30")

        var btn: ImageButton = this.findViewById(R.id.start_game_inizial_btn)

        btn.setOnTouchListener(View.OnTouchListener { view, motionEvent ->
            when (motionEvent.actionMasked){
                MotionEvent.ACTION_DOWN -> {
                    // Здесь должна быть твоя анимация (начало)
                    true
                }
                MotionEvent.ACTION_UP -> {
                    // Здесь она должна закончиться
                    if ((btn.width > motionEvent.getX() && motionEvent.getX() > 0) && (btn.height > motionEvent.getY() && motionEvent.getY() > 0)) {
                        inizializate_lst_players()
                    }
                    true
                }
                else -> {
                    true
                }
            }
        })
    }

    fun inizializate_lst_players(){
        setContentView(R.layout.lst_of_players_start)
    }
}