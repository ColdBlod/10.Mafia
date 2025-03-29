package com.gamegenius.mafia

import android.os.Bundle
import android.print.PrintAttributes.Margins
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.core.view.marginLeft
import androidx.core.view.marginRight

class MainActivity : ComponentActivity() {
    private var players: MutableList<Int> = mutableListOf<Int>(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14)
    private var players_name: MutableList<String> = mutableListOf("player 1", "player 2", "player 3", "player 4", "player 5", "player 6", "player 7", "player 8", "player 9", "player 10", "player 11", "player 12", "player 13", "player 14")


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
        for (i in 1..this.players.size) {
            val id = resources.getIdentifier("player_${i}_btn", "id", packageName)
            val btn = findViewById<Button>(id)

            btn.setOnClickListener(View.OnClickListener {make_keyboard()})
        }
    }

    fun make_keyboard(){
        val layout: LinearLayout = this.findViewById(R.id.main_layout_lst_players)

        var tx:TextView = TextView(this)
        tx.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4f, this.resources.displayMetrics).toInt(), 0f)
        var params = tx.layoutParams as ViewGroup.MarginLayoutParams
        params.leftMargin = 50
        params.rightMargin = 50
        tx.layoutParams = params
        tx.setBackgroundColor(getColor(R.color.white))
        tx.text = ""

        layout.addView(tx, 5)






        tx = TextView(this)
        tx.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4f, this.resources.displayMetrics).toInt(), 0f)
        params = tx.layoutParams as ViewGroup.MarginLayoutParams
        params.leftMargin = 50
        params.rightMargin = 50
        tx.layoutParams = params
        tx.setBackgroundColor(getColor(R.color.white))
        tx.text = ""

        layout.addView(tx, 5)
    }
}