package com.gamegenius.Mafia

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.print.PrintAttributes.Margins
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.ALPHA
import android.view.ViewGroup.MarginLayoutParams
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.core.content.ContextCompat
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.setPadding
import kotlin.properties.Delegates

class MainActivity : ComponentActivity() {
    private var players: MutableList<Help_file> = mutableListOf<Help_file>(Help_file(-1, null),
        Help_file(-1, null),
        Help_file(-1, null), Help_file(-1, null), Help_file(-1, null), Help_file(-1, null), Help_file(-1, null), Help_file(-1, null), Help_file(-1, null), Help_file(-1, null), Help_file(-1, null), Help_file(-1, null), Help_file(-1, null), Help_file(-1, null))
    private var players_name: MutableList<String> = mutableListOf("player 1", "player 2", "player 3", "player 4", "player 5", "player 6", "player 7", "player 8", "player 9", "player 10", "player 11", "player 12", "player 13", "player 14")
    private var is_clicked_players_lst: Boolean = false
    private var  current_player_selected_players_lst: Int = -1

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

            btn.setOnClickListener(View.OnClickListener {
                if (this.is_clicked_players_lst == false) {make_keyboard()
                    btn.setTextColor(getColor(R.color.purple_700))
                    this.current_player_selected_players_lst = i
                    this.is_clicked_players_lst = true
                }else if (i != this.current_player_selected_players_lst){
                    var id = resources.getIdentifier("player_${this.current_player_selected_players_lst}_btn", "id", packageName)
                    var btn = findViewById<Button>(id)
                    btn.setTextColor(getColor(R.color.lst_players_btn))

                    id = resources.getIdentifier("player_${i}_btn", "id", packageName)
                    btn = findViewById<Button>(id)
                    btn.setTextColor(getColor(R.color.purple_700))

                    this.current_player_selected_players_lst = i
                    println(this.current_player_selected_players_lst.toString())
                }
            })
        }
    }


    fun make_number_for_player(number:Int){
        fun get_player_by_number(num:Int):Help_file{
            for (i in 0..this.players.size){
                if (num == this.players[i].num) return this.players[i]
            }
            return Help_file(num, Button(this))
        }
        if (this.players.contains(get_player_by_number(number)) == false) {
            if (this.players[this.current_player_selected_players_lst].num != -1){
                val old_btn = this.players[this.current_player_selected_players_lst].btn
                if (old_btn != null) {
                    old_btn.setTextColor(getColor(R.color.white))
                }
            }
            var id = resources.getIdentifier(
                "player_${this.current_player_selected_players_lst}_btn",
                "id",
                packageName
            )
            val btn = findViewById<Button>(id)
            btn.setText(number.toString())
            this.players[this.current_player_selected_players_lst-1].num = number
        }
    }

    fun make_keyboard(){
        val layout: LinearLayout = this.findViewById(R.id.main_layout_lst_players)

        fun make_btn_keyboard(ind:Int):Button {
            val btn = Button(this)
            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
            params.rightMargin = 10
            params.leftMargin = 10
            params.topMargin = 10
            params.bottomMargin = 10
            btn.layoutParams = params
            btn.setBackgroundResource(R.drawable.keyboard_btn_lst_players)
            btn.setText(ind.toString())
            btn.setTextSize(30f)
            btn.setTextColor(getColor(R.color.white))
            btn.setOnTouchListener(View.OnTouchListener(){view, motionEvent ->
                when(motionEvent.actionMasked){
                    MotionEvent.ACTION_DOWN -> {
                        btn.setTextColor(getColor(R.color.btn_is_selecting_lst_players))
                        true
                    }
                    MotionEvent.ACTION_UP -> {
                        if ((btn.width > motionEvent.getX() && motionEvent.getX() > 0) && (btn.height > motionEvent.getY() && motionEvent.getY() > 0)) {
                            make_number_for_player(ind)
                            btn.setTextColor(getColor(R.color.btn_selected_lst_players))
                        }
                        true
                    }
                    else -> {
                        true
                    }
                }
            })
            return btn
        }
        fun make_invisible_btn():TextView{
            val tx = TextView(this)
            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
            tx.layoutParams = params
            tx.setText("")
            tx.setBackgroundColor(getColor(R.color.background_keyboard_lst_players))
            tx.setTextSize(30f)
            tx.setTextColor(getColor(R.color.white))
            return tx
        }


        var tx:TextView = TextView(this)
        tx.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4f, this.resources.displayMetrics).toInt(), 0f)
        var params = tx.layoutParams as ViewGroup.MarginLayoutParams
        tx.layoutParams = params
        tx.setBackgroundColor(getColor(R.color.white))
        tx.text = ""

        layout.addView(tx, 5)

        val main_main_layout = this.findViewById<LinearLayout>(R.id.main_main_layout_lst_players)
        params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 10f)
        main_main_layout.layoutParams = params

        var res:LinearLayout = LinearLayout(this)
        res.orientation = LinearLayout.VERTICAL
        res.setBackgroundResource(R.drawable.bg_keyboard_lst_players)
        params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.rightMargin = 100
        params.leftMargin = 100
        params.topMargin = 50
        params.bottomMargin = 50
        res.layoutParams = params



        for (i in 0..this.players.size/4-1){
            var l = LinearLayout(this)
            params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
            l.layoutParams = params
            l.orientation = LinearLayout.HORIZONTAL
            l.addView(make_btn_keyboard(i*4+1))
            l.addView(make_btn_keyboard(i*4+2))
            l.addView(make_btn_keyboard(i*4+3))
            l.addView(make_btn_keyboard(i*4+4))
            res.addView(l)
        }


        val l = LinearLayout(this)
        params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT,
            1f
        )
        l.layoutParams = params
        l.orientation = LinearLayout.HORIZONTAL
        if (players.size%4 != 0){

            for (i in 1..this.players.size % 4) {
                l.addView(make_btn_keyboard(i + (this.players.size / 4) * 4))
            }

            val btn_clear = Button(this)
            params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
            params.rightMargin = 10
            params.leftMargin = 10
            params.topMargin = 10
            params.bottomMargin = 10
            btn_clear.layoutParams = params
            btn_clear.setBackgroundResource(R.drawable.keyboard_btn_lst_players)
            btn_clear.setText("X")
            btn_clear.setTextSize(27f)
            btn_clear.setTextColor(getColor(R.color.cancel_keyboard_lst_players))
            l.addView(btn_clear)

            for (i in this.players.size + 2..(this.players.size / 4 + 1) * 4) {
                l.addView(make_invisible_btn())
            }
        } else {
            val btn_clear = Button(this)
            params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
            params.rightMargin = 10
            params.leftMargin = 10
            params.topMargin = 10
            params.bottomMargin = 10
            btn_clear.layoutParams = params
            btn_clear.setBackgroundResource(R.drawable.keyboard_btn_lst_players)
            btn_clear.setText("X")
            btn_clear.setTextSize(27f)
            btn_clear.setTextColor(getColor(R.color.cancel_keyboard_lst_players))
            l.addView(btn_clear)
            for (i in 1..3){
                l.addView(make_invisible_btn())
            }
        }
        res.addView(l)

        layout.addView(res, 5)

        tx = TextView(this)
        tx.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4f, this.resources.displayMetrics).toInt(), 0f)
        params = tx.layoutParams as ViewGroup.MarginLayoutParams
        tx.layoutParams = params
        tx.setBackgroundColor(getColor(R.color.white))
        tx.text = ""

        layout.addView(tx, 5)
    }
}