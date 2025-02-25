package com.gamegenius.Mafia

import android.os.Bundle
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    private var players: MutableList<Help_file> = mutableListOf<Help_file>(Help_file(-1, null),
        Help_file(-1, null),
        Help_file(-1, null), Help_file(-1, null), Help_file(-1, null), Help_file(-1, null), Help_file(-1, null), Help_file(-1, null), Help_file(-1, null), Help_file(-1, null), Help_file(-1, null), Help_file(-1, null), Help_file(-1, null), Help_file(-1, null))
    private var players_name: MutableList<String> = mutableListOf("player 1", "player 2", "player 3", "player 4", "player 5", "player 6", "player 7", "player 8", "player 9", "player 10", "player 11", "player 12", "player 13", "player 14")
    private var players_data: MutableList<Player> = mutableListOf()
    private var is_clicked_players_lst: Boolean = false
    private var current_player_selected_players_lst: Int = -1
    private var is_paused:Boolean = false
    private var is_muted:Boolean = false

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
                    btn.setImageResource(R.drawable.start_touched_anim)
                    true
                }
                MotionEvent.ACTION_UP -> {
                    btn.setImageResource(R.drawable.start)
                    if ((btn.width > motionEvent.getX() && motionEvent.getX() > 0) && (btn.height > motionEvent.getY() && motionEvent.getY() > 0)) {
                        // Обновление кнопки
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
                    if (this.players[this.current_player_selected_players_lst-1].num == -1) btn.setTextColor(getColor(R.color.lst_players_btn))
                    else btn.setTextColor(getColor(R.color.white))

                    id = resources.getIdentifier("player_${i}_btn", "id", packageName)
                    btn = findViewById<Button>(id)
                    btn.setTextColor(getColor(R.color.purple_700))

                    this.current_player_selected_players_lst = i
                    println(this.current_player_selected_players_lst.toString())
                }
            })
        }
        val btn_start = findViewById<ImageButton>(R.id.start_game_inizial_btn)
        btn_start.setOnTouchListener(View.OnTouchListener {view, motionEvent ->
            when (motionEvent.actionMasked){
                MotionEvent.ACTION_DOWN -> {
                    btn_start.setImageResource(R.drawable.start_touched_anim)
                    true
                }
                MotionEvent.ACTION_UP -> {
                    btn_start.setImageResource(R.drawable.start)
                    if ((btn_start.width > motionEvent.getX() && motionEvent.getX() > 0) && (btn_start.height > motionEvent.getY() && motionEvent.getY() > 0)) {
                        inizializate_role_making()
                    }
                    true
                }
                else -> {true}
            }
        })
    }

    fun make_number_for_player(number:Int, itbtn:Button){
        fun get_player_by_number(num:Int):Help_file{
            for (i in 0..this.players.size-1){
                if (num == this.players[i].num) return this.players[i]
            }
            return Help_file(num, null)
        }

        if (number != -1){
            if (this.players.contains(get_player_by_number(number)) == false) {
                if (this.players[this.current_player_selected_players_lst - 1].num != -1) {
                    val old_btn = this.players[this.current_player_selected_players_lst - 1].btn
                    if (old_btn != null) {
                        old_btn.setTextColor(getColor(R.color.white))
                    }
                }
                var id = resources.getIdentifier(
                    "player_${this.current_player_selected_players_lst}_btn",
                    "id",
                    packageName
                )
                var btn = findViewById<Button>(id)
                btn.setText(number.toString().replace("-1", "X"))

                this.players[this.current_player_selected_players_lst - 1].num = number
                this.players[this.current_player_selected_players_lst - 1].btn = itbtn

                if (this.players.filter{it.num == -1}.isEmpty()) {
                    val layout: LinearLayout = this.findViewById(R.id.main_layout_lst_players)
                    layout.removeViewAt(5)
                    layout.removeViewAt(5)
                    layout.removeViewAt(5)
                    btn.setTextColor(getColor(R.color.white))
                    this.is_clicked_players_lst = false
                }
            }
        }else{
            var old_btn = this.players[this.current_player_selected_players_lst-1].btn
            if (old_btn != null) {
                old_btn.setTextColor(getColor(R.color.white))
            }
            old_btn = findViewById<Button>(resources.getIdentifier(
                "player_${this.current_player_selected_players_lst}_btn",
                "id",
                packageName
            ))
            old_btn.setText("X")
            this.players[this.current_player_selected_players_lst-1] = Help_file(-1, null)
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
                            make_number_for_player(ind, btn)
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
            btn_clear.setOnTouchListener(View.OnTouchListener(){view, motionEvent ->
                when(motionEvent.actionMasked){
                    MotionEvent.ACTION_DOWN ->{
                        btn_clear.setTextColor(getColor(R.color.cancel_keyboard_clicked_lst_players))
                        true
                    }
                    MotionEvent.ACTION_UP -> {
                        btn_clear.setTextColor(getColor(R.color.cancel_keyboard_lst_players))
                        if ((btn_clear.width > motionEvent.getX() && motionEvent.getX() > 0) && (btn_clear.height > motionEvent.getY() && motionEvent.getY() > 0)) {
                            make_number_for_player(-1, btn_clear)
                        }
                        true
                    }
                    else -> {
                        true
                    }
                }
            })
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
            btn_clear.setOnTouchListener(View.OnTouchListener(){view, motionEvent ->
                when(motionEvent.actionMasked){
                    MotionEvent.ACTION_DOWN ->{
                        btn_clear.setTextColor(getColor(R.color.cancel_keyboard_clicked_lst_players))
                        true
                    }
                    MotionEvent.ACTION_UP -> {
                        btn_clear.setTextColor(getColor(R.color.cancel_keyboard_lst_players))
                        if ((btn_clear.width > motionEvent.getX() && motionEvent.getX() > 0) && (btn_clear.height > motionEvent.getY() && motionEvent.getY() > 0)) {
                            make_number_for_player(-1, btn_clear)
                        }
                        true
                    }
                    else -> {
                        true
                    }
                }
            })
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


    fun inizializate_role_making(){
        setContentView(R.layout.role_screen_layout)
        for (i in 0..this.players.size-1){
            if (this.players[i].num != -1) this.players_data.add(Player(this.players_name[i], "Noname", 0, 0, 0))
        }
        println(this.players_data.size)
        val roles: List<Int> = Card_kit.get_lst_of_cards(14) //this.players_data.size)

        for ((i, el) in this.players_data.withIndex()){
            val role:Int = Random.nextInt(roles.size.toInt())
            println(role)
        }

        var btn = findViewById<ImageButton>(R.id.pause_btn_role_screen)
        btn.setOnClickListener(View.OnClickListener { when (this.is_paused) {
            false -> {
                btn.setImageResource(R.drawable.play)
                this.is_paused = true
                true
            }
            true -> {
                btn.setImageResource(R.drawable.pause)
                this.is_paused = false
                true
            }
        } })

        btn = findViewById<ImageButton>(R.id.sound_btn_role_screen)
        btn.setOnClickListener(View.OnClickListener { when (this.is_muted) {
            false -> {
                btn.setImageResource(R.drawable.play)
                this.is_muted = true
                true
            }
            true -> {
                btn.setImageResource(R.drawable.pause)
                this.is_muted = false
                true
            }
        } })

        btn = findViewById<ImageButton>(R.id.next_btn_role_screen)
        btn.setOnTouchListener(View.OnTouchListener {view, event -> when (event.actionMasked){
            MotionEvent.ACTION_DOWN -> {
                btn.setImageResource(R.drawable.start_touched_anim)
                true
            }
            MotionEvent.ACTION_UP -> {
                btn.setImageResource(R.drawable.start)
                true
            }
            else -> {true}
        }})
    }
}