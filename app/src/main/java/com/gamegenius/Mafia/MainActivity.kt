package com.gamegenius.mafia

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.View.OnClickListener
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge


import androidx.core.view.setPadding
import com.gamegenius.mafia.R
import kotlin.math.roundToInt
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    private var current_player: Int = 0
    private var players: MutableList<Help_file> = mutableListOf<Help_file>(Help_file(-1, null),
        Help_file(-1, null),
        Help_file(-1, null), Help_file(-1, null), Help_file(-1, null), Help_file(-1, null), Help_file(-1, null), Help_file(-1, null), Help_file(-1, null), Help_file(-1, null), Help_file(-1, null), Help_file(-1, null), Help_file(-1, null), Help_file(-1, null))
    private var players_name: MutableList<String> = mutableListOf("Вагю", "Железная леди", "Тур де шале", "Мистер У", "Керамогранит", "Святой Отец", "Осмыслитель", "Иван Иваныч", "Тимбер", "Кассиопея", "Зануда", "Котофей", "Торт", "Рея")
    private var players_data: MutableList<Player> = mutableListOf()
    private var is_clicked_players_lst: Boolean = false
    private var current_player_selected_players_lst: Int = -1
    private var is_paused:Boolean = false
    private var is_muted:Boolean = false
    private var night:Int = -1
    private var killing_lst = mutableListOf<Int>()
    private var don_checked_players: MutableMap<Int, Boolean> = mutableMapOf()
    private var sheriff_checked_players: MutableMap<Int, Boolean> = mutableMapOf()
    private var timer_to_say: Int = 15
    private var step = 1
    private var player_choice = 0
    private val mafia_timer = 15
    private val maniac_timer = 15
    private val sheriff_timer = 15
    private val don_timer = 15
    private val doctor_timer = 15
    private val gorgeous_timer = 15
    private var players_id = 0
    private lateinit var choizen_btn_img: ImageButton
    private var voting_candidates = mutableListOf<Int>()
    private var voting_result: MutableMap<Int, Int> = mutableMapOf()
    private val queue_of_nigth: List<String> = listOf("Мафия", "Дон", "Шериф", "Маньяк", "Доктор", "Красотка")
    private var night_result: MutableMap<String, Int> = mutableMapOf("Мафия" to -1, "Дон" to -1, "Шериф" to -1, "Маньяк" to -1, "Доктор" to -1, "Красотка" to -1)
    private var is_doctor_health_himself:Boolean = false
    private var is_checked_at_action: Boolean = false
    private var red_team = mutableListOf("Мафия", "Дон")
    private lateinit var choizen_btn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        actionBar?.hide()
        if (Build.VERSION.SDK_INT < 16) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
        setContentView(R.layout.main)
        inzializate_start()
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

        val lb: TextView = this.findViewById(R.id.info_inizial_lb)
        lb.text = "Городская мафия; Царский стол\n8 февраля 19:00 - 00:30"

        val btn: ImageButton = this.findViewById(R.id.start_game_inizial_btn)

        btn.setOnTouchListener(View.OnTouchListener { _, motionEvent ->
            when (motionEvent.actionMasked){
                MotionEvent.ACTION_DOWN -> {
                    btn.setImageResource(R.drawable.start_touched_anim)
                    true
                }
                MotionEvent.ACTION_UP -> {
                    runOnUiThread{ btn.setImageResource(R.drawable.start) }
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
            var id = resources.getIdentifier("player_${i}_btn", "id", packageName)
            val btn = findViewById<Button>(id)
            id = resources.getIdentifier("player_${i}_layout", "id", packageName)
            val layout: LinearLayout = findViewById(id)
            id = resources.getIdentifier("player_${i}_lb", "id", packageName)
            val textview: TextView = findViewById(id)
            textview.setText(players_name[i-1])

            layout.setOnClickListener(View.OnClickListener {
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
                }
            })
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
        var ts = MutableList(this.players.size) {Player(this.players_name[it], "Noname", 0, 0f, 0, true)}
        for (i in 0..this.players.size-1){
            if (this.players[i].num != -1) {
                ts[this.players[i].num - 1] = Player(this.players_name[i], "Noname", 0, 0f, 0, true)
                ts[i] = Player(this.players_name[this.players[i].num-1], "Noname", 0, 0f, 0, true)
            }
        }

        for (dt in ts){
            if (dt.name != "") this.players_data.add(dt)
        }

        this.players_name = this.players_data.map { it.name }.toMutableList()


        val roles: MutableList<Int> =
            Card_kit.get_lst_of_cards(14) //this.players_data.size)

        var sum = -1
        roles.forEach { sum += it }
        for (i in 0..this.players_data.size-1){
            var role = 0

            while (roles[role] == 0) role += 1

            var ind = (Random.nextInt((this.players_data.size-1)*100).toDouble()/100).roundToInt()

            while (this.players_data[ind].role != "Noname") ind = (Random.nextInt((this.players_data.size-1)*100).toDouble()/100).roundToInt()

            this.players_data[ind].role = listOf("Мирный", "Мафия", "Дон", "Шериф", "Красотка", "Маньяк", "Доктор")[role]

            roles[role] -= 1
            sum -= 1
        }

        val btn_pause = findViewById<ImageButton>(R.id.pause_btn_role_screen)
        btn_pause.setOnClickListener(View.OnClickListener { when (this.is_paused) {
            false -> {
                btn_pause.setImageResource(R.drawable.play)
                this.is_paused = true
                true
            }
            true -> {
                btn_pause.setImageResource(R.drawable.pause)
                this.is_paused = false
                true
            }
        } })

        val btn_sound = findViewById<ImageButton>(R.id.sound_btn_role_screen)
        btn_sound.setOnClickListener(View.OnClickListener { when (this.is_muted) {
            false -> {
                btn_sound.setImageResource(R.drawable.mute)
                this.is_muted = true
                true
            }
            true -> {
                btn_sound.setImageResource(R.drawable.sound)
                this.is_muted = false
                true
            }
        } })

        val btn_show_players = findViewById<Button>(R.id.check_roles_btn_role_screen)
        btn_show_players.setOnClickListener(View.OnClickListener { inizializate_show_lst_players()
            val btn_back = findViewById<Button>(R.id.check_roles_btn_players_lst)
            btn_back.setOnClickListener(View.OnClickListener {
                preshow_player()
            })})

        this.current_player_selected_players_lst = 0
        var btn = findViewById<ImageButton>(R.id.next_btn_role_screen)
        btn.setOnTouchListener(View.OnTouchListener {view, event -> when (event.actionMasked){
            MotionEvent.ACTION_DOWN -> {
                btn.setImageResource(R.drawable.start_touched_anim)
                true
            }
            MotionEvent.ACTION_UP -> {
                btn.setImageResource(R.drawable.start)
                if ((btn.width > event.getX() && event.getX() > 0) && (btn.height > event.getY() && event.getY() > 0)) {
                    setContentView(R.layout.role_screen_shown)
                    show_player()
                }
                true
            }
            else -> {true}
        }})
    }
    fun preshow_player(){
        setContentView(R.layout.role_screen_layout)
        if (this.current_player_selected_players_lst != this.players_data.size) {
            val btn_pause = findViewById<ImageButton>(R.id.pause_btn_role_screen)
            btn_pause.setOnClickListener(View.OnClickListener { when (this.is_paused) {
                false -> {
                    btn_pause.setImageResource(R.drawable.play)
                    this.is_paused = true
                    true
                }
                true -> {
                    btn_pause.setImageResource(R.drawable.pause)
                    this.is_paused = false
                    true
                }
            } })

            val btn_sound = findViewById<ImageButton>(R.id.sound_btn_role_screen)
            btn_sound.setOnClickListener(View.OnClickListener { when (this.is_muted) {
                false -> {
                    btn_sound.setImageResource(R.drawable.mute)
                    this.is_muted = true
                    true
                }
                true -> {
                    btn_sound.setImageResource(R.drawable.sound)
                    this.is_muted = false
                    true
                }
            } })

            val btn_show_players = findViewById<Button>(R.id.check_roles_btn_role_screen)
            btn_show_players.setOnClickListener(View.OnClickListener { inizializate_show_lst_players()
                val btn_back = findViewById<Button>(R.id.check_roles_btn_players_lst)
                btn_back.setOnClickListener(View.OnClickListener {
                    preshow_player()
                })})

            var btn = findViewById<ImageButton>(R.id.next_btn_role_screen)
            var img_view = findViewById<ImageView>(R.id.main_img_role_screen)
            var img_id:Int
            when (this.current_player_selected_players_lst){
                1 -> img_id = R.drawable.two
                2 -> img_id = R.drawable.three
                3 -> img_id = R.drawable.four
                4 -> img_id = R.drawable.five
                5 -> img_id = R.drawable.six
                6 -> img_id = R.drawable.seven
                7 -> img_id = R.drawable.eight
                8 -> img_id = R.drawable.nine
                9 -> img_id = R.drawable.ten
                10 -> img_id = R.drawable.eleven
                11 -> img_id = R.drawable.twelve
                12 -> img_id = R.drawable.thirteen
                13 -> img_id = R.drawable.fourteen
                else -> img_id = R.drawable.one
            }
            img_view.setImageResource(img_id)

            btn.setOnTouchListener(View.OnTouchListener { v, event -> when(event.actionMasked){
                MotionEvent.ACTION_DOWN -> {
                    btn.setImageResource(R.drawable.start_touched_anim)
                    true
                }
                MotionEvent.ACTION_UP -> {
                    btn.setImageResource(R.drawable.start)
                    if ((btn.width > event.getX() && event.getX() > 0) && (btn.height > event.getY() && event.getY() > 0)) {
                        setContentView(R.layout.role_screen_shown)
                        show_player()
                    }
                    true
                }
                else -> {true}
            } })
        }else{

        }
    }
    fun show_player(){
        val textview = findViewById<TextView>(R.id.saying_textview_role_screen_shown)
        when (this.current_player_selected_players_lst){
            this.players_data.size-1 -> textview.setText("Перейти к ознакомительной ночи")
            else -> textview.setText("К игроку №${this.current_player_selected_players_lst+2}")
        }


        var btn = findViewById<ImageView>(R.id.main_img_role_screen_shown)
        var img_id:Int

        when (this.players_data[this.current_player_selected_players_lst].role){
            "Мафия" -> img_id = R.drawable.mafia
            "Маньяк" -> img_id = R.drawable.maniac
            "Мирный" -> img_id = R.drawable.peaceful
            "Шериф" -> img_id = R.drawable.sheriff
            "Доктор" -> img_id = R.drawable.doctor
            "Красотка" -> img_id = R.drawable.gorgeous
            "Дон" -> img_id = R.drawable.don
            else -> img_id = R.drawable.peaceful
        }

        btn.setImageResource(img_id)



        var next_btn = findViewById<ImageButton>(R.id.next_btn_role_screen_shown)
        next_btn.setOnTouchListener(View.OnTouchListener { v, event -> when(event.actionMasked){
            MotionEvent.ACTION_DOWN -> {
                next_btn.setImageResource(R.drawable.start_touched_anim)
                true
            }
            MotionEvent.ACTION_UP -> {
                next_btn.setImageResource(R.drawable.start)
                if ((next_btn.width > event.getX() && event.getX() > 0) && (next_btn.height > event.getY() && event.getY() > 0)) {
                    if (this.current_player_selected_players_lst != this.players_data.size-1) {
                        this.current_player_selected_players_lst += 1
                        preshow_player()
                    } else {
                        next_night()
                    }
                }
                true
            }
            else -> {true}
        } })
    }
    fun inizializate_show_lst_players() {
        setContentView(R.layout.players_lst_screen)
        fun createPlayerListItem(context: Context, playerNumber: String, playerName: String, playerPosition: String, parentView: ViewGroup) {
            val linearLayout = LinearLayout(context)
            val params = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(0, 0, 0, 10)
            linearLayout.layoutParams = params
            linearLayout.orientation = LinearLayout.HORIZONTAL
            linearLayout.setBackgroundResource(R.drawable.lst_element) // Replace with your drawable
            linearLayout.setPadding(0, 0, 0, 0) //Added padding bottom



            // Player Number TextView
            val playerNumberTextView = TextView(context)
            playerNumberTextView.setTextSize(15f)
            playerNumberTextView.layoutParams = LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.MATCH_PARENT,
                3f
            )
            playerNumberTextView.gravity = Gravity.CENTER
            playerNumberTextView.textAlignment = View.TEXT_ALIGNMENT_CENTER
            playerNumberTextView.setBackgroundResource(R.drawable.btn_element) //Replace with your drawable
            playerNumberTextView.setTextColor(getColor(R.color.white))
            playerNumberTextView.text = playerNumber
            linearLayout.addView(playerNumberTextView)
            val player_icon = ImageView(context)
            player_icon.layoutParams = LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.MATCH_PARENT,
                2f
            )
            when (playerPosition){
                "Мр" -> player_icon.setImageResource(R.drawable.peacful_icon)
                "Мн" -> player_icon.setImageResource(R.drawable.maniac_icon)
                "Шф" -> player_icon.setImageResource(R.drawable.sheriff_icon)
                "Кр" -> player_icon.setImageResource(R.drawable.gorgeous_icon)
                "Дк" -> player_icon.setImageResource(R.drawable.doctor_icon)
                "Мф" -> player_icon.setImageResource(R.drawable.mafia_icon)
                "Дн" -> player_icon.setImageResource(R.drawable.don_icon)
                else -> player_icon.setImageResource(R.drawable.peacful_icon)
            }
            player_icon.foregroundGravity = Gravity.CENTER
            player_icon.scaleType = ImageView.ScaleType.FIT_CENTER
            player_icon.setBackgroundColor(getColor(R.color.nothing))
            linearLayout.addView(player_icon)


            // Player Name TextView
            val playerNameTextView = TextView(context)
            playerNameTextView.setTextSize(20f)
            playerNameTextView.layoutParams = LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                14f
            )
            playerNameTextView.setPadding(70, 10, 0, 10)
            playerNameTextView.text = playerName
            playerNameTextView.setTextColor(getColor(R.color.white))
            linearLayout.addView(playerNameTextView)


            // Player Position TextView
            val playerPositionTextView = TextView(context)
            playerPositionTextView.setTextSize(15f)
            playerPositionTextView.layoutParams = LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                2f
            )
            playerPositionTextView.text = playerPosition
            playerPositionTextView.setTextColor(getColor(R.color.white))
            linearLayout.addView(playerPositionTextView)

            parentView.addView(linearLayout)
        }

        var layout = findViewById<LinearLayout>(R.id.show_layout_players_lst_screen)
        for (i in 0..this.players_data.size-1) {
            when (this.players_data[i].role){
                "Мирный" -> createPlayerListItem(this, (i+1).toString(), this.players_name[i], "Мр", layout)
                "Маньяк" -> createPlayerListItem(this, (i+1).toString(), this.players_name[i], "Мн", layout)
                "Шериф" -> createPlayerListItem(this, (i+1).toString(), this.players_name[i], "Шф", layout)
                "Красотка" -> createPlayerListItem(this, (i+1).toString(), this.players_name[i], "Кр", layout)
                "Доктор" -> createPlayerListItem(this, (i+1).toString(), this.players_name[i], "Дк", layout)
                "Мафия" -> createPlayerListItem(this, (i+1).toString(), this.players_name[i], "Мф", layout)
                "Дон" -> createPlayerListItem(this, (i+1).toString(), this.players_name[i], "Дн", layout)
            }
        }
    }

    fun next_night(){
        setContentView(R.layout.show_night_screen)
        val next_btn = findViewById<ImageButton>(R.id.start_night_btn)
        if (this.night == -1){
            val handler = Handler(Looper.getMainLooper())
            class runable : Runnable{
                var time = 0
                override fun run(){
                    val textView = findViewById<TextView>(R.id.lb_timer_acquaintance_screen)
                    this.time += 1
                    try { runOnUiThread { textView.text = this.time.toString() } } catch (e: Exception) {null}
                    handler.postDelayed(this, 1000) // Запуск каждые 1000 мс
                }
            }
            fun create_zero_screen(next_btn:ImageButton, handler:Handler, runnable: runable){
                setContentView(R.layout.night_acquaintance_screen)

                val textview = findViewById<TextView>(R.id.lb_timer_acquaintance_screen)
                textview.setText(runnable.time.toString())

                val queue = findViewById<Button>(R.id.check_roles_btn_acquaintance_screen)
                queue.setOnClickListener(View.OnClickListener {
                    inizializate_show_lst_players()
                    handler.removeCallbacks(runnable)
                    val btn_back = findViewById<Button>(R.id.check_roles_btn_players_lst)
                    btn_back.setOnClickListener(View.OnClickListener {
                        if (this.is_paused == false) handler.postDelayed(runnable, 1000)
                        create_zero_screen(next_btn, handler, runnable)
                    })})

                val btn_pause = findViewById<ImageButton>(R.id.pause_btn_acquaintance_screen)
                if (this.is_paused == true){
                    btn_pause.setImageResource(R.drawable.play)
                }
                btn_pause.setOnClickListener(View.OnClickListener { when (this.is_paused) {
                    false -> {
                        btn_pause.setImageResource(R.drawable.play)
                        this.is_paused = true
                        handler.removeCallbacks(runnable)
                        true
                    }
                    true -> {
                        btn_pause.setImageResource(R.drawable.pause)
                        this.is_paused = false
                        handler.postDelayed(runnable, 1000)
                        true
                    }
                } })

                val btn_sound = findViewById<ImageButton>(R.id.sound_btn_acquaintance_screen)
                btn_sound.setOnClickListener(View.OnClickListener { when (this.is_muted) {
                    false -> {
                        btn_sound.setImageResource(R.drawable.mute)
                        this.is_muted = true
                        true
                    }
                    true -> {
                        btn_sound.setImageResource(R.drawable.sound)
                        this.is_muted = false
                        true
                    }
                } })

                val next_btn = findViewById<ImageButton>(R.id.next_btn_acquaintance_screen)
                next_btn.setOnTouchListener(View.OnTouchListener {view, event -> when (event.actionMasked){
                    MotionEvent.ACTION_DOWN -> {
                        next_btn.setImageResource(R.drawable.start_touched_anim)
                        true
                    }
                    MotionEvent.ACTION_UP -> {
                        next_btn.setImageResource(R.drawable.start)
                        if ((next_btn.width > event.getX() && event.getX() > 0) && (next_btn.height > event.getY() && event.getY() > 0)) {
                            this.night += 1
                            handler.removeCallbacks(runnable)
                            next_night()
                        }
                        true
                    }
                    else -> {true}
                } })

            }
            next_btn.setOnTouchListener(View.OnTouchListener {view, event -> when(event.actionMasked){
                MotionEvent.ACTION_DOWN -> {
                    next_btn.setImageResource(R.drawable.start_touched_anim)
                    true
                }
                MotionEvent.ACTION_UP -> {
                    next_btn.setImageResource(R.drawable.start)
                    if ((next_btn.width > event.getX() && event.getX() > 0) && (next_btn.height > event.getY() && event.getY() > 0)) {
                        val runnable = runable()
                        handler.postDelayed(runnable, 1000)
                        create_zero_screen(next_btn, handler, runnable)
                    }
                    true
                }
                else -> {true}
            } })

        }
        else if (this.night == 0){
            this.current_player = 0
            make_vote_screen()
            inizializate_voting()
        }
        else {
            val what_day_lb = findViewById<TextView>(R.id.what_day_lb)
            val tx_lb_night = findViewById<TextView>(R.id.text_lb_night_screen)
            val start_btn = findViewById<ImageButton>(R.id.start_night_btn)

            for (i in 0..this.queue_of_nigth.size-1) this.night_result[this.queue_of_nigth[i]] = -1

            what_day_lb.setText("Ночь " + this.night.toString())
            tx_lb_night.setText("Начать")
            this.current_player = 0

            start_btn.setOnTouchListener(View.OnTouchListener { v, event -> when(event.actionMasked){
                MotionEvent.ACTION_DOWN -> {
                    start_btn.setImageResource(R.drawable.start_touched_anim)
                    true
                }
                MotionEvent.ACTION_UP -> {
                    start_btn.setImageResource(R.drawable.start)
                    if ((start_btn.width > event.getX() && event.getX() > 0) && (start_btn.height > event.getY() && event.getY() > 0)) {
                        setContentView(R.layout.night_work_screen)
                        player_choice = 0
                        who_next_working()
                    }
                    true
                }
                else -> {true}
            } })
        }
    }
    fun make_keyboard_for_night(){
        val layout: LinearLayout = this.findViewById(R.id.keyboard_layout_night_work)

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
            if (this.queue_of_nigth[this.current_player] == "Дон" && ind in this.don_checked_players.keys) {
                when (this.don_checked_players[ind]){
                    true -> btn.setTextColor(getColor(R.color.green))
                    false -> btn.setTextColor(getColor(R.color.red))
                    else -> {btn.setTextColor(getColor(R.color.white))} }
            }
            if (this.queue_of_nigth[this.current_player] == "Шериф" && ind in this.sheriff_checked_players.keys){
                when (this.sheriff_checked_players[ind]) {
                    true -> btn.setTextColor(getColor(R.color.green))
                    false -> btn.setTextColor(getColor(R.color.red))
                    else -> btn.setTextColor(getColor(R.color.white))
                }
            }
            if (this.queue_of_nigth[this.current_player] == "Доктор" && this.is_doctor_health_himself == true && this.players_id == ind-1) btn.setTextColor(getColor(R.color.btn_selected_lst_players))

            if (this.queue_of_nigth[this.current_player] == this.players_data[ind-1].role && this.queue_of_nigth[this.current_player] != "Доктор") btn.setTextColor(getColor(R.color.btn_selected_lst_players))
            btn.setOnTouchListener(View.OnTouchListener(){view, motionEvent ->
                when(motionEvent.actionMasked){
                    MotionEvent.ACTION_DOWN -> {
                        btn.setTextColor(getColor(R.color.btn_is_selecting_lst_players))
                        true
                    }
                    MotionEvent.ACTION_UP -> {
                        if ((this.queue_of_nigth[this.current_player] != this.players_data[ind-1].role || this.queue_of_nigth[this.current_player] == "Доктор" ) && ((btn.width > motionEvent.getX() && motionEvent.getX() > 0) && (btn.height > motionEvent.getY() && motionEvent.getY() > 0))) {
                            if (this.queue_of_nigth[this.current_player] == "Дон") {
                                don_check(ind)
                                when (this.don_checked_players[ind]) {
                                    true -> btn.setTextColor(getColor(R.color.green))
                                    false -> btn.setTextColor(getColor(R.color.red))
                                    else -> {}
                                }
                            }
                            else if (this.queue_of_nigth[this.current_player] == "Шериф") {
                                sherif_check(ind)
                                when (this.sheriff_checked_players[ind]) {
                                    true -> btn.setTextColor(getColor(R.color.green))
                                    false -> btn.setTextColor(getColor(R.color.red))
                                    else -> {}
                                }
                            }
                            else if (this.queue_of_nigth[this.current_player] == "Доктор"){
                                println((ind-1 == this.players_id).toString() + " | " + (this.is_checked_at_action == false).toString() + " | " + (this.is_doctor_health_himself == false).toString())
                                if (ind-1 == this.players_id){
                                    if (this.is_doctor_health_himself == false) {
                                        if (this.is_checked_at_action == true) this.choizen_btn.setTextColor(getColor(R.color.white))
                                        this.is_checked_at_action = true
                                        this.choizen_btn = btn
                                        this.player_choice = ind
                                    }else btn.setTextColor(getColor(R.color.btn_selected_lst_players))
                                } else if (this.is_checked_at_action == false){   //   (this.queue_of_nigth[this.current_player] != "Мафия" || (this.queue_of_nigth[this.current_player] == "Мафия" && this.players_data[ind-1].role != "Мафия"))) {
                                    this.choizen_btn = btn
                                    this.is_checked_at_action = true
                                    this.player_choice = ind
                                }
                                else {
                                    this.choizen_btn.setTextColor(getColor(R.color.white))
                                    this.choizen_btn = btn
                                    this.player_choice = ind
                                }
                            }
                            else if (this.is_checked_at_action == false && this.queue_of_nigth[this.current_player] != this.players_data[ind-1].role){   //   (this.queue_of_nigth[this.current_player] != "Мафия" || (this.queue_of_nigth[this.current_player] == "Мафия" && this.players_data[ind-1].role != "Мафия"))) {
                                this.choizen_btn = btn
                                this.is_checked_at_action = true
                                this.player_choice = ind
                            }
                            else {
                                this.choizen_btn.setTextColor(getColor(R.color.white))
                                this.choizen_btn = btn
                                this.player_choice = ind
                            }
                        }
                        else if (this.queue_of_nigth[this.current_player] == this.players_data[ind-1].role) btn.setTextColor(getColor(R.color.btn_selected_lst_players))
                        else btn.setTextColor(getColor(R.color.white))
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

        layout.addView(tx)

        var res:LinearLayout = LinearLayout(this)
        res.orientation = LinearLayout.VERTICAL
        res.setBackgroundResource(R.drawable.bg_keyboard_lst_players)
        params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT, 1f
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
                            if (this.is_checked_at_action == true) this.choizen_btn.setTextColor(getColor(R.color.white))
                            this.player_choice = 0
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
                            if (this.is_checked_at_action == true) this.choizen_btn.setTextColor(getColor(R.color.white))
                            this.player_choice = 0
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

        layout.addView(res)

        tx = TextView(this)
        tx.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4f, this.resources.displayMetrics).toInt(), 0f)
        params = tx.layoutParams as ViewGroup.MarginLayoutParams
        tx.layoutParams = params
        tx.setBackgroundColor(getColor(R.color.white))
        tx.text = ""

        layout.addView(tx)
    }
    fun mafia_work(){
        this.is_checked_at_action = false
        setContentView(R.layout.night_work_screen)
        make_keyboard_for_night()
        val sound_btn = findViewById<ImageButton>(R.id.sound_btn_night_work)
        if (is_muted == true) sound_btn.setImageResource(R.drawable.mute)
        sound_btn.setOnClickListener { if (is_muted == true){
            is_muted = false
            sound_btn.setImageResource(R.drawable.sound)
        } else {
            is_muted = true
            sound_btn.setImageResource(R.drawable.mute)} }

        val who_playint = findViewById<TextView>(R.id.who_playing_night_work)
        who_playint.text = "Мафия\nстреляет"

        val timer_lb = findViewById<TextView>(R.id.lb_timer_night_work)
        timer_lb.text = this.mafia_timer.toString()

        findViewById<TextView>(R.id.saying_textview_night_work).text = predict_next_role(this.current_player+1)

        var time = this.mafia_timer
        val handler = Handler()
        val runnable = object : Runnable {
            override fun run(){
                time -= 1
                if (time == -1) {
                    findViewById<TextView>(R.id.lb_timer_night_work).text = "0"
                    if (player_choice != 0) night_result["Мафия"] = player_choice
                    current_player += 1
                    if (player_choice != 0) players_data[player_choice-1].is_alive = false
                    player_choice = 0
                    is_checked_at_action = false
                    who_next_working()
                }
                else if (time >= 0) {
                    try{ findViewById<TextView>(R.id.lb_timer_night_work).text = time.toString()
                        handler.postDelayed(this, 1000)} catch (e: Exception){}

                }
            }
        }
        if (this.is_paused == false) handler.postDelayed(runnable, 1000)

        val pause_btn = findViewById<ImageButton>(R.id.pause_btn_night_work)
        if (this.is_paused == true) pause_btn.setImageResource(R.drawable.play)
        pause_btn.setOnClickListener { if (this.is_paused == true){
            this.is_paused = false
            handler.postDelayed(runnable, 1000)
            pause_btn.setImageResource(R.drawable.pause)
        } else {
            this.is_paused = true
            handler.removeCallbacks(runnable)
            pause_btn.setImageResource(R.drawable.play)
        } }

        val start_btn = findViewById<ImageButton>(R.id.next_btn_night_work)
        start_btn.setOnTouchListener(View.OnTouchListener { v, event -> when(event.actionMasked){
            MotionEvent.ACTION_DOWN -> {
                start_btn.setImageResource(R.drawable.start_touched_anim)
                true
            }
            MotionEvent.ACTION_UP -> {
                start_btn.setImageResource(R.drawable.start)
                if ((start_btn.width > event.getX() && event.getX() > 0) && (start_btn.height > event.getY() && event.getY() > 0)){
                    handler.removeCallbacks(runnable)
                    this.current_player += 1
                    if (this.player_choice != 0) this.night_result["Мафия"] = this.player_choice
                    if (this.player_choice != 0) this.players_data[this.player_choice-1].is_alive = false
                    this.player_choice = 0
                    this.is_checked_at_action = false
                    who_next_working()
                }
                true
            }
            else -> {true}
        } })
    }
    fun don_work(){
        this.is_checked_at_action = false
        setContentView(R.layout.night_work_screen)
        make_keyboard_for_night()
        val sound_btn = findViewById<ImageButton>(R.id.sound_btn_night_work)
        if (is_muted == true) sound_btn.setImageResource(R.drawable.mute)
        sound_btn.setOnClickListener { if (is_muted == true){
            is_muted = false
            sound_btn.setImageResource(R.drawable.sound)
        } else {
            is_muted = true
            sound_btn.setImageResource(R.drawable.mute)} }

        val who_playint = findViewById<TextView>(R.id.who_playing_night_work)
        who_playint.text = "Дон ищет\nШерифа"

        val timer_lb = findViewById<TextView>(R.id.lb_timer_night_work)
        timer_lb.text = this.don_timer.toString()

        findViewById<TextView>(R.id.saying_textview_night_work).text = predict_next_role(this.current_player+1)

        var time = this.don_timer
        val handler = Handler()
        val runnable = object : Runnable {
            override fun run(){
                time -= 1
                if (time == -1) {
                    try {
                        current_player += 1
                        player_choice = 0
                        is_checked_at_action = false
                        who_next_working()
                    } catch (e: Exception) {}
                }
                else if (time >= 0) {
                    try{
                        findViewById<TextView>(R.id.lb_timer_night_work).text = time.toString()
                        handler.postDelayed(this, 1000)
                    } catch (e: Exception) {}
                }
            }
        }
        if (this.is_paused == false) handler.postDelayed(runnable, 1000)

        val pause_btn = findViewById<ImageButton>(R.id.pause_btn_night_work)
        if (this.is_paused == true) pause_btn.setImageResource(R.drawable.play)
        pause_btn.setOnClickListener { if (this.is_paused == true){
            this.is_paused = false
            handler.postDelayed(runnable, 1000)
            pause_btn.setImageResource(R.drawable.pause)
        } else {
            this.is_paused = true
            handler.removeCallbacks(runnable)
            pause_btn.setImageResource(R.drawable.play)
        } }

        val start_btn = findViewById<ImageButton>(R.id.next_btn_night_work)
        start_btn.setOnTouchListener(View.OnTouchListener { v, event -> when(event.actionMasked){
            MotionEvent.ACTION_DOWN -> {
                start_btn.setImageResource(R.drawable.start_touched_anim)
                true
            }
            MotionEvent.ACTION_UP -> {
                start_btn.setImageResource(R.drawable.start)
                if ((start_btn.width > event.getX() && event.getX() > 0) && (start_btn.height > event.getY() && event.getY() > 0)){
                    handler.removeCallbacks(runnable)
                    this.current_player += 1
                    this.player_choice = 0
                    this.is_checked_at_action = false
                    who_next_working()
                }
                true
            }
            else -> {true}
        } })
    }
    fun don_check(player: Int){
        if (this.is_checked_at_action == false && player in this.don_checked_players.keys == false){
            this.is_checked_at_action = true
            val layout = findViewById<LinearLayout>(R.id.work_layout_night_work_screen)
            layout.removeView(findViewById<TextView>(R.id.lb_timer_night_work))

            val img_view = ImageView(this)
            img_view.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT,
                3f
            )
            val textview = TextView(this)
            textview.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT,
                4f
            )
            textview.gravity = Gravity.CENTER
            textview.setTextSize(35f)

            if (this.players_data[player - 1].role != "Шериф") {
                this.don_checked_players[player] = false
                img_view.setImageResource(R.drawable.dislike)
                textview.text = "Не Шериф"
                textview.setTextColor(getColor(R.color.red))
            } else {
                this.don_checked_players[player] = true
                img_view.setImageResource(R.drawable.like)
                textview.text = "Шериф"
                textview.setTextColor(getColor(R.color.green))
            }
            layout.addView(textview, 1)
            layout.addView(img_view, 1)
        }
    }
    fun sherif_work(){
        this.is_checked_at_action = false
        setContentView(R.layout.night_work_screen)
        make_keyboard_for_night()
        val sound_btn = findViewById<ImageButton>(R.id.sound_btn_night_work)
        if (is_muted == true) sound_btn.setImageResource(R.drawable.mute)
        sound_btn.setOnClickListener { if (is_muted == true){
            is_muted = false
            sound_btn.setImageResource(R.drawable.sound)
        } else {
            is_muted = true
            sound_btn.setImageResource(R.drawable.mute)} }

        val who_playint = findViewById<TextView>(R.id.who_playing_night_work)
        who_playint.text = "Шериф ищет\nМафию"

        val timer_lb = findViewById<TextView>(R.id.lb_timer_night_work)
        timer_lb.text = this.sheriff_timer.toString()

        findViewById<TextView>(R.id.saying_textview_night_work).text = predict_next_role(this.current_player+1)

        var time = this.sheriff_timer
        val handler = Handler()
        val runnable = object : Runnable {
            override fun run(){
                time -= 1
                if (time == -1) {
                    try {
                        current_player += 1
                        player_choice = 0
                        who_next_working()
                    } catch (e: Exception) {}
                }
                else if (time >= 0) {
                    try{
                        findViewById<TextView>(R.id.lb_timer_night_work).text = time.toString()
                        handler.postDelayed(this, 1000)
                    } catch (e: Exception) {}
                }
            }
        }
        if (this.is_paused == false) handler.postDelayed(runnable, 1000)

        val pause_btn = findViewById<ImageButton>(R.id.pause_btn_night_work)
        if (this.is_paused == true) pause_btn.setImageResource(R.drawable.play)
        pause_btn.setOnClickListener { if (this.is_paused == true){
            this.is_paused = false
            handler.postDelayed(runnable, 1000)
            pause_btn.setImageResource(R.drawable.pause)
        } else {
            this.is_paused = true
            handler.removeCallbacks(runnable)
            pause_btn.setImageResource(R.drawable.play)
        } }

        val start_btn = findViewById<ImageButton>(R.id.next_btn_night_work)
        start_btn.setOnTouchListener(View.OnTouchListener { v, event -> when(event.actionMasked){
            MotionEvent.ACTION_DOWN -> {
                start_btn.setImageResource(R.drawable.start_touched_anim)
                true
            }
            MotionEvent.ACTION_UP -> {
                start_btn.setImageResource(R.drawable.start)
                if ((start_btn.width > event.getX() && event.getX() > 0) && (start_btn.height > event.getY() && event.getY() > 0)){
                    handler.removeCallbacks(runnable)
                    this.current_player += 1
                    who_next_working()
                }
                true
            }
            else -> {true}
        } })
    }
    fun sherif_check(player: Int){
        if (this.is_checked_at_action == false && player in this.sheriff_checked_players.keys == false){
            this.is_checked_at_action = true
            val layout = findViewById<LinearLayout>(R.id.work_layout_night_work_screen)
            layout.removeView(findViewById<TextView>(R.id.lb_timer_night_work))

            val img_view = ImageView(this)
            img_view.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT,
                3f
            )
            val textview = TextView(this)
            textview.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT,
                4f
            )
            textview.gravity = Gravity.CENTER
            textview.setTextSize(35f)

            if (this.players_data[player - 1].role in this.red_team == false) {
                this.sheriff_checked_players[player] = false
                img_view.setImageResource(R.drawable.like)
                textview.text = "Не Мафия"
                textview.setTextColor(getColor(R.color.green))
            } else {
                this.sheriff_checked_players[player] = true
                img_view.setImageResource(R.drawable.dislike)
                textview.text = "Мафия"
                textview.setTextColor(getColor(R.color.red))
            }
            layout.addView(textview, 1)
            layout.addView(img_view, 1)
        }
    }
    fun maniac_work(){
        this.is_checked_at_action = false
        setContentView(R.layout.night_work_screen)
        make_keyboard_for_night()
        val sound_btn = findViewById<ImageButton>(R.id.sound_btn_night_work)
        if (is_muted == true) sound_btn.setImageResource(R.drawable.mute)
        sound_btn.setOnClickListener { if (is_muted == true){
            is_muted = false
            sound_btn.setImageResource(R.drawable.sound)
        } else {
            is_muted = true
            sound_btn.setImageResource(R.drawable.mute)} }

        val who_playint = findViewById<TextView>(R.id.who_playing_night_work)
        who_playint.text = "Стреляет\nМаньяк"

        val timer_lb = findViewById<TextView>(R.id.lb_timer_night_work)
        timer_lb.text = this.maniac_timer.toString()

        findViewById<TextView>(R.id.saying_textview_night_work).text = predict_next_role(this.current_player+1)

        var time = this.maniac_timer
        val handler = Handler()
        val runnable = object : Runnable {
            override fun run(){
                time -= 1
                if (time == -1) {
                    findViewById<TextView>(R.id.lb_timer_night_work).text = "0"
                    if (player_choice != 0) night_result["Маньяк"] = player_choice
                    current_player += 1
                    if (player_choice != 0) players_data[player_choice-1].is_alive = false
                    player_choice = 0
                    is_checked_at_action = false
                    who_next_working()
                }
                else if (time >= 0) {
                    try { findViewById<TextView>(R.id.lb_timer_night_work).text = time.toString() } catch(e: Exception) {}
                    handler.postDelayed(this, 1000)
                }
            }
        }
        if (this.is_paused == false) handler.postDelayed(runnable, 1000)

        val pause_btn = findViewById<ImageButton>(R.id.pause_btn_night_work)
        if (this.is_paused == true) pause_btn.setImageResource(R.drawable.play)
        pause_btn.setOnClickListener { if (this.is_paused == true){
            this.is_paused = false
            handler.postDelayed(runnable, 1000)
            pause_btn.setImageResource(R.drawable.pause)
        } else {
            this.is_paused = true
            handler.removeCallbacks(runnable)
            pause_btn.setImageResource(R.drawable.play)
        } }

        val start_btn = findViewById<ImageButton>(R.id.next_btn_night_work)
        start_btn.setOnTouchListener(View.OnTouchListener { v, event -> when(event.actionMasked){
            MotionEvent.ACTION_DOWN -> {
                start_btn.setImageResource(R.drawable.start_touched_anim)
                true
            }
            MotionEvent.ACTION_UP -> {
                start_btn.setImageResource(R.drawable.start)
                if ((start_btn.width > event.getX() && event.getX() > 0) && (start_btn.height > event.getY() && event.getY() > 0)){
                    handler.removeCallbacks(runnable)
                    this.current_player += 1
                    if (this.player_choice != 0) night_result["Маньяк"] = this.player_choice
                    if (this.player_choice != 0) this.players_data[this.player_choice-1].is_alive = false
                    this.player_choice = 0
                    this.is_checked_at_action = false
                    who_next_working()
                }
                true
            }
            else -> {true}
        } })
    }
    fun doctor_work(){
        this.is_checked_at_action = false
        for (i in 0..this.players_data.size-1) if (this.players_data[i].role == "Доктор") this.players_id = i
        setContentView(R.layout.night_work_screen)
        make_keyboard_for_night()
        val sound_btn = findViewById<ImageButton>(R.id.sound_btn_night_work)
        if (is_muted == true) sound_btn.setImageResource(R.drawable.mute)
        sound_btn.setOnClickListener { if (is_muted == true){
            is_muted = false
            sound_btn.setImageResource(R.drawable.sound)
        } else {
            is_muted = true
            sound_btn.setImageResource(R.drawable.mute)} }

        val who_playint = findViewById<TextView>(R.id.who_playing_night_work)
        who_playint.text = "Лечит\nДоктор"

        val timer_lb = findViewById<TextView>(R.id.lb_timer_night_work)
        timer_lb.text = this.doctor_timer.toString()

        findViewById<TextView>(R.id.saying_textview_night_work).text = predict_next_role(this.current_player+1)

        var time = this.doctor_timer
        val handler = Handler()
        val runnable = object : Runnable {
            override fun run(){
                time -= 1
                if (time == -1) {
                    findViewById<TextView>(R.id.lb_timer_night_work).text = "0"
                    if (player_choice != 0) night_result["Доктор"] = player_choice
                    if (player_choice-1 == players_id) is_doctor_health_himself = true
                    current_player += 1
                    if (player_choice != 0) players_data[player_choice-1].is_alive = true
                    player_choice = 0
                    is_checked_at_action = false
                    who_next_working()
                }
                else if (time >= 0) {
                    findViewById<TextView>(R.id.lb_timer_night_work).text = time.toString()
                    handler.postDelayed(this, 1000)
                }
            }
        }
        if (this.is_paused == false) handler.postDelayed(runnable, 1000)

        val pause_btn = findViewById<ImageButton>(R.id.pause_btn_night_work)
        if (this.is_paused == true) pause_btn.setImageResource(R.drawable.play)
        pause_btn.setOnClickListener { if (this.is_paused == true){
            this.is_paused = false
            handler.postDelayed(runnable, 1000)
            pause_btn.setImageResource(R.drawable.pause)
        } else {
            this.is_paused = true
            handler.removeCallbacks(runnable)
            pause_btn.setImageResource(R.drawable.play)
        } }

        val start_btn = findViewById<ImageButton>(R.id.next_btn_night_work)
        start_btn.setOnTouchListener(View.OnTouchListener { v, event -> when(event.actionMasked){
            MotionEvent.ACTION_DOWN -> {
                start_btn.setImageResource(R.drawable.start_touched_anim)
                true
            }
            MotionEvent.ACTION_UP -> {
                start_btn.setImageResource(R.drawable.start)
                if ((start_btn.width > event.getX() && event.getX() > 0) && (start_btn.height > event.getY() && event.getY() > 0)){
                    handler.removeCallbacks(runnable)
                    this.current_player += 1
                    if (this.player_choice != 0) night_result["Доктор"] = this.player_choice
                    if (player_choice-1 == players_id) is_doctor_health_himself = true
                    if (this.player_choice != 0) this.players_data[this.player_choice-1].is_alive = true
                    this.player_choice = 0
                    this.is_checked_at_action = false
                    who_next_working()
                }
                true
            }
            else -> {true}
        } })
    }
    fun gorgeous_work(){
        this.is_checked_at_action = false
        setContentView(R.layout.night_work_screen)
        make_keyboard_for_night()
        val sound_btn = findViewById<ImageButton>(R.id.sound_btn_night_work)
        if (is_muted == true) sound_btn.setImageResource(R.drawable.mute)
        sound_btn.setOnClickListener { if (is_muted == true){
            is_muted = false
            sound_btn.setImageResource(R.drawable.sound)
        } else {
            is_muted = true
            sound_btn.setImageResource(R.drawable.mute)} }

        val who_playint = findViewById<TextView>(R.id.who_playing_night_work)
        who_playint.text = "Ходит\nКрасотка"

        val timer_lb = findViewById<TextView>(R.id.lb_timer_night_work)
        timer_lb.text = this.gorgeous_timer.toString()

        findViewById<TextView>(R.id.saying_textview_night_work).text = predict_next_role(this.current_player+1)

        var time = this.gorgeous_timer
        val handler = Handler()
        val runnable = object : Runnable {
            override fun run(){
                time -= 1
                if (time == -1) {
                    findViewById<TextView>(R.id.lb_timer_night_work).text = "0"
                    if (player_choice != 0) night_result["Красотка"] = player_choice
                    current_player += 1
                    for (el in players_data) if (el.role == "Красотка" && player_choice != 0) players_data[player_choice-1].is_alive = el.is_alive
                    player_choice = 0
                    is_checked_at_action = false
                    who_next_working()
                }
                else if (time >= 0) {
                    findViewById<TextView>(R.id.lb_timer_night_work).text = time.toString()
                    handler.postDelayed(this, 1000)
                }
            }
        }
        if (this.is_paused == false) handler.postDelayed(runnable, 1000)

        val pause_btn = findViewById<ImageButton>(R.id.pause_btn_night_work)
        if (this.is_paused == true) pause_btn.setImageResource(R.drawable.play)
        pause_btn.setOnClickListener { if (this.is_paused == true){
            this.is_paused = false
            handler.postDelayed(runnable, 1000)
            pause_btn.setImageResource(R.drawable.pause)
        } else {
            this.is_paused = true
            handler.removeCallbacks(runnable)
            pause_btn.setImageResource(R.drawable.play)
        } }

        val start_btn = findViewById<ImageButton>(R.id.next_btn_night_work)
        start_btn.setOnTouchListener(View.OnTouchListener { v, event -> when(event.actionMasked){
            MotionEvent.ACTION_DOWN -> {
                start_btn.setImageResource(R.drawable.start_touched_anim)
                true
            }
            MotionEvent.ACTION_UP -> {
                start_btn.setImageResource(R.drawable.start)
                if ((start_btn.width > event.getX() && event.getX() > 0) && (start_btn.height > event.getY() && event.getY() > 0)){
                    handler.removeCallbacks(runnable)
                    this.current_player += 1
                    if (this.player_choice != 0) night_result["Красотка"] = this.player_choice
                    for (el in this.players_data) if (el.role == "Красотка" && this.player_choice != 0) this.players_data[this.player_choice-1].is_alive = el.is_alive
                    this.player_choice = 0
                    this.is_checked_at_action = false
                    who_next_working()
                }
                true
            }
            else -> {true}
        } })
    }
    fun who_next_working(){
        var ans = ""
        while (this.current_player != this.queue_of_nigth.size && ans == "") {
            var res = this.queue_of_nigth[this.current_player]
            for (el in this.players_data){
                if (el.role == res && el.is_alive == true) ans = res
            }

            if (ans == "") this.current_player += 1
        }

        when (ans) {
            "" -> when (who_will_win()){
                "red" -> win("red")
                "black" -> win("black")
                "maniac" -> win("maniac")
                else -> next_day()
            }
            "Мафия" -> mafia_work()
            "Дон" -> don_work()
            "Доктор" -> doctor_work()
            "Маньяк" -> maniac_work()
            "Шериф" -> sherif_work()
            "Красотка" -> gorgeous_work()
        }
        if (ans != ""){
            val check_roles = findViewById<Button>(R.id.check_roles_btn_night_work)
            check_roles.setOnClickListener {
                setContentView(R.layout.players_lst_screen)
                fun createPlayerListItem(context: Context, playerNumber: String, playerName: String, playerPosition: String, parentView: ViewGroup) {
                    val linearLayout = LinearLayout(context)
                    val params = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    params.setMargins(0, 0, 0, 10)
                    linearLayout.layoutParams = params
                    linearLayout.orientation = LinearLayout.HORIZONTAL
                    linearLayout.setBackgroundResource(R.drawable.lst_element) // Replace with your drawable
                    linearLayout.setPadding(0, 0, 0, 0) //Added padding bottom



                    // Player Number TextView
                    val playerNumberTextView = TextView(context)
                    playerNumberTextView.setTextSize(15f)
                    playerNumberTextView.layoutParams = LinearLayout.LayoutParams(
                        0,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        3f
                    )
                    playerNumberTextView.gravity = Gravity.CENTER
                    playerNumberTextView.textAlignment = View.TEXT_ALIGNMENT_CENTER
                    playerNumberTextView.setBackgroundResource(R.drawable.btn_element) //Replace with your drawable
                    playerNumberTextView.setTextColor(getColor(R.color.white))
                    playerNumberTextView.text = playerNumber
                    linearLayout.addView(playerNumberTextView)
                    val player_icon = ImageView(context)
                    player_icon.layoutParams = LinearLayout.LayoutParams(
                        0,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        2f
                    )
                    when (playerPosition){
                        "Мр" -> player_icon.setImageResource(R.drawable.peacful_icon)
                        "Мн" -> player_icon.setImageResource(R.drawable.maniac_icon)
                        "Шф" -> player_icon.setImageResource(R.drawable.sheriff_icon)
                        "Кр" -> player_icon.setImageResource(R.drawable.gorgeous_icon)
                        "Дк" -> player_icon.setImageResource(R.drawable.doctor_icon)
                        "Мф" -> player_icon.setImageResource(R.drawable.mafia_icon)
                        "Дн" -> player_icon.setImageResource(R.drawable.don_icon)
                        else -> player_icon.setImageResource(R.drawable.peacful_icon)
                    }
                    player_icon.foregroundGravity = Gravity.CENTER
                    player_icon.scaleType = ImageView.ScaleType.FIT_CENTER
                    player_icon.setBackgroundColor(getColor(R.color.nothing))
                    linearLayout.addView(player_icon)


                    // Player Name TextView
                    val playerNameTextView = TextView(context)
                    playerNameTextView.setTextSize(20f)
                    playerNameTextView.layoutParams = LinearLayout.LayoutParams(
                        0,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        14f
                    )
                    playerNameTextView.setPadding(70, 10, 0, 10)
                    playerNameTextView.text = playerName
                    linearLayout.addView(playerNameTextView)


                    // Player Position TextView
                    val playerPositionTextView = TextView(context)
                    playerPositionTextView.setTextSize(15f)
                    playerPositionTextView.layoutParams = LinearLayout.LayoutParams(
                        0,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        2f
                    )
                    playerPositionTextView.text = playerPosition
                    linearLayout.addView(playerPositionTextView)

                    parentView.addView(linearLayout)
                }

                var layout = findViewById<LinearLayout>(R.id.show_layout_players_lst_screen)
                for (i in 0..this.queue_of_nigth.size-1) {
                    when (this.queue_of_nigth[i]){
                        "Маньяк" -> createPlayerListItem(this, (i+1).toString(), this.queue_of_nigth[i], "Мн", layout)
                        "Шериф" -> createPlayerListItem(this, (i+1).toString(), this.queue_of_nigth[i], "Шф", layout)
                        "Красотка" -> createPlayerListItem(this, (i+1).toString(), this.queue_of_nigth[i], "Кр", layout)
                        "Доктор" -> createPlayerListItem(this, (i+1).toString(), this.queue_of_nigth[i], "Дк", layout)
                        "Мафия" -> createPlayerListItem(this, (i+1).toString(), this.queue_of_nigth[i], "Мф", layout)
                        "Дон" -> createPlayerListItem(this, (i+1).toString(), this.queue_of_nigth[i], "Дн", layout)
                    }
                }

                val return_btn = findViewById<Button>(R.id.check_roles_btn_players_lst)
                return_btn.setOnClickListener {
                    who_next_working()
                }
            }
        }
    }
    fun predict_next_role(current_player: Int):String{
        var ans = ""
        var player = current_player
        while (player != this.queue_of_nigth.size && ans == "") {
            var res = this.queue_of_nigth[player]
            for (el in this.players_data){
                if (el.role == res && el.is_alive == true) ans = res
            }

            if (ans == "") player += 1
        }
        when (ans){
            "" -> ans = "Итоги ночи"
            "Мафия" -> ans = "К Мафиии"
            "Дон" -> ans = "К Дону"
            "Шериф" -> ans = "К Шерифу"
            "Доктор" -> ans = "К Доктору"
            "Маньяк" -> ans = "К Маньяку"
            "Красотка" -> ans = "К Красотке"
        }
        return ans
    }
    fun inizalizate_end_game_in_start(){
        setContentView(R.layout.end_the_game_in_the_start)
    }


    fun next_day(){
        for (i in 0..this.players_data.size-1) this.players_data[i].voices = 0
        this.current_player = this.players_data.size
        make_vote_screen()
        val exit_btn = findViewById<Button>(R.id.end_the_game_in_the_start_btn)
        exit_btn.setOnClickListener(OnClickListener { pre_end_the_game_in_start()
            val back_btn = findViewById<Button>(R.id.back_to_the_game_end_the_game_start)
            back_btn.setOnClickListener({next_day()}) })

        val saying_lb = findViewById<TextView>(R.id.saying_lb_vote_screen)
        saying_lb.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 15f)
        saying_lb.setBackgroundColor(getColor(R.color.red))

        val undexbtn_textview = findViewById<TextView>(R.id.text_lb_next_vote_screen)
        undexbtn_textview.text = "Начать речь"

        this.killing_lst = mutableListOf()
        for (el in this.queue_of_nigth){
            if (this.night_result[el] != -1){
                if (el == "Мафия" || el == "Маньяк" && this.night_result[el]!! in this.killing_lst == false) this.killing_lst.add(this.night_result[el]!!)
                if (el == "Доктор" && this.night_result[el]!! in this.killing_lst) this.killing_lst.remove(this.night_result[el])
                if (el == "Красотка"){
                    for (el_player in this.players_data) if (el_player.role == "Красотка"){
                        when (el_player.is_alive){
                            true -> if (this.night_result[el]!! in this.killing_lst) this.killing_lst.remove(this.night_result[el]!!)
                            false -> if (this.night_result[el]!! in this.killing_lst == false) this.killing_lst.add(this.night_result[el]!!)
                        }
                    }
                }
            }
        }
        if (this.killing_lst.isNotEmpty()){
            this.killing_lst.sort()
            var ans = ""
            this.killing_lst.forEach { ans += "№" + it.toString() + " " }
            saying_lb.text = ans + "\nВне игры"
        } else saying_lb.text = "Этой ночью никто не выбывает из игры"


        val next_btn = findViewById<ImageButton>(R.id.start_next_btn_vote_screen)
        next_btn.setOnTouchListener(View.OnTouchListener { _, event -> when(event.actionMasked){
            MotionEvent.ACTION_DOWN -> {
                next_btn.setImageResource(R.drawable.start_touched_anim)
                true
            }
            MotionEvent.ACTION_UP -> {
                next_btn.setImageResource(R.drawable.start)
                if ((next_btn.width > event.getX() && event.getX() > 0) && (next_btn.height > event.getY() && event.getY() > 0)){
                    this.current_player = 0
                    while (this.current_player < this.players_data.size && this.players_data[this.current_player].is_alive == false) this.current_player += 1
                    if (this.killing_lst.isNotEmpty()) {this.current_player = this.killing_lst[0]-1
                        make_vote_screen()
                        inizializate_voting_for_dead()
                    }
                    else {
                        make_vote_screen()
                        inizializate_voting()
                    }
                }
                true
            }else -> {true}
        } })
    }
    fun end_the_day(){
        this.night += 1
        findViewById<TextView>(R.id.saying_lb_vote_screen).setText("Итоги дня")
        findViewById<TextView>(R.id.text_lb_next_vote_screen).setText("Закончить день")
        findViewById<ImageButton>(R.id.start_next_btn_vote_screen).setOnTouchListener(View.OnTouchListener { v, event -> when(event.actionMasked){
            MotionEvent.ACTION_DOWN -> {
                findViewById<ImageButton>(R.id.start_next_btn_vote_screen).setImageResource(R.drawable.start_touched_anim)
                true
            }
            MotionEvent.ACTION_UP -> {
                findViewById<ImageButton>(R.id.start_next_btn_vote_screen).setImageResource(R.drawable.start)
                if ((findViewById<ImageButton>(R.id.start_next_btn_vote_screen).width > event.getX() && event.getX() > 0) && (findViewById<ImageButton>(R.id.start_next_btn_vote_screen).height > event.getY() && event.getY() > 0)) {
                    when (who_will_win()){
                        "red" -> win("red")
                        "black" -> win("black")
                        "maniac" -> win("maniac")
                        else -> next_night()
                    }
                }
                true
            }
            else -> {true}
        } })
    }
    fun inizializate_voting(){
        val textview = findViewById<TextView>(R.id.saying_lb_vote_screen)
        val btn_next = findViewById<ImageButton>(R.id.start_next_btn_vote_screen)
        val handler = Handler()
        var timer_was_started = false
        val stop = this.timer_to_say
        fun next_player(){this.current_player += 1
            while (this.current_player < this.players_data.size && this.players_data[this.current_player].is_alive == false) this.current_player += 1
            make_vote_screen()
            if (this.current_player != this.players_data.size) inizializate_voting()
            else if (this.night == 0) end_the_day()
            else {
                before_voting()
            }}

        fun make_next_player_voice(runnable: Runnable){
            findViewById<TextView>(R.id.text_lb_next_vote_screen).setText("Закончить речь")
            runOnUiThread {textview.setTextSize(100f)
                textview.setBackgroundResource(R.drawable.timer_background)
                textview.text = this.timer_to_say.toString()}
            handler.postDelayed(runnable, 1000)
            timer_was_started = true
            btn_next.setOnTouchListener(View.OnTouchListener { v, event -> when(event.actionMasked){
                MotionEvent.ACTION_DOWN -> {
                    runOnUiThread{ btn_next.setImageResource(R.drawable.start_touched_anim) }
                    true
                }
                MotionEvent.ACTION_UP -> {
                    runOnUiThread { btn_next.setImageResource(R.drawable.start) }
                    if ((btn_next.width > event.getX() && event.getX() > 0) && (btn_next.height > event.getY() && event.getY() > 0)) {
                        handler.removeCallbacks(runnable)
                        timer_was_started = false
                        runOnUiThread {
                            textview.setTextSize(30f)
                            textview.setBackgroundColor(getColor(R.color.nothing))
                        }
                        this.current_player += 1
                        while (this.current_player < this.players_data.size && this.players_data[this.current_player].is_alive == false) this.current_player += 1
                        runOnUiThread { make_vote_screen() }
                        if (this.current_player != this.players_data.size) inizializate_voting()
                        else if (this.night == 0) end_the_day()
                        else {before_voting()}
                    }
                    true
                }
                else -> {true}
            }})
        }
        val runnable = object: Runnable {
            var i=0
            override fun run() {
                this.i += 1
                if (stop - i < 0){
                    textview.setTextSize(30f)
                    textview.setBackgroundColor(getColor(R.color.nothing))
                    next_player()
                    this.i = 0
                    val btn_end = findViewById<Button>(R.id.end_the_game_in_the_start_btn)
                    btn_end.setOnClickListener(View.OnClickListener { inizalizate_end_game_in_start()
                        findViewById<Button>(R.id.back_to_the_game_end_the_game_start).setOnClickListener(
                            View.OnClickListener { this.i = 0
                                pre_end_the_game_in_start()
                                val back_btn = findViewById<Button>(R.id.back_to_the_game_end_the_game_start)
                                back_btn.setOnClickListener({make_vote_screen()
                                    inizializate_voting()})
                                if (is_paused == false){
                                    handler.removeCallbacks(this)
                                }})})
                } else {
                    textview.text = (stop-i).toString()
                    handler.postDelayed(this, 1000)
                }
            }
        }

        val exit_btn = findViewById<Button>(R.id.end_the_game_in_the_start_btn)
        exit_btn.setOnClickListener(OnClickListener {
            handler.removeCallbacks(runnable)
            this.is_paused = false
            pre_end_the_game_in_start()
            val back_btn = findViewById<Button>(R.id.back_to_the_game_end_the_game_start)
            back_btn.setOnClickListener({make_vote_screen()
                inizializate_voting()}) })

        btn_next.setOnTouchListener(View.OnTouchListener { v, event -> when(event.actionMasked){
            MotionEvent.ACTION_DOWN -> {
                btn_next.setImageResource(R.drawable.start_touched_anim)
                true
            }
            MotionEvent.ACTION_UP -> {
                btn_next.setImageResource(R.drawable.start)
                if ((btn_next.width > event.getX() && event.getX() > 0) && (btn_next.height > event.getY() && event.getY() > 0)) {
                    make_next_player_voice(runnable)
                }
                true
            }
            else -> {true}
        } })
        val btn_pause = findViewById<ImageButton>(R.id.pause_btn_vote_screen)
        btn_pause.setOnClickListener(View.OnClickListener { when (this.is_paused) {
            false -> {
                btn_pause.setImageResource(R.drawable.play)
                this.is_paused = true
                if (timer_was_started == true) handler.removeCallbacks(runnable)
                true
            }
            true -> {
                btn_pause.setImageResource(R.drawable.pause)
                if (timer_was_started == true) handler.postDelayed(runnable, 1000)
                this.is_paused = false
                true
            }
        } })
    }
    fun inizializate_voting_for_dead(){
        val textview = findViewById<TextView>(R.id.saying_lb_vote_screen)
        val btn_next = findViewById<ImageButton>(R.id.start_next_btn_vote_screen)
        val handler = Handler()
        var timer_was_started = false
        val stop = this.timer_to_say
        fun next_player(){val ind = this.killing_lst.indexOf(this.current_player+1)
            if (ind == this.killing_lst.size-1) {
                this.current_player = 0
                while (this.current_player < this.players_data.size && this.players_data[this.current_player].is_alive == false) this.current_player += 1
                make_vote_screen()
                inizializate_voting()
            }
            else{
                this.current_player = this.killing_lst[ind+1]-1
                make_vote_screen()
                inizializate_voting_for_dead()}
        }

        fun make_next_player_voice(runnable: Runnable){
            findViewById<TextView>(R.id.text_lb_next_vote_screen).setText("Закончить речь")
            runOnUiThread {textview.setTextSize(100f)
                textview.setBackgroundResource(R.drawable.timer_background)
                textview.text = this.timer_to_say.toString()}
            handler.postDelayed(runnable, 1000)
            timer_was_started = true
            btn_next.setOnTouchListener(View.OnTouchListener { v, event -> when(event.actionMasked){
                MotionEvent.ACTION_DOWN -> {
                    runOnUiThread{ btn_next.setImageResource(R.drawable.start_touched_anim) }
                    true
                }
                MotionEvent.ACTION_UP -> {
                    runOnUiThread { btn_next.setImageResource(R.drawable.start) }
                    if ((btn_next.width > event.getX() && event.getX() > 0) && (btn_next.height > event.getY() && event.getY() > 0)) {
                        handler.removeCallbacks(runnable)
                        timer_was_started = false
                        runOnUiThread {
                            textview.setTextSize(30f)
                            textview.setBackgroundColor(getColor(R.color.nothing))
                        }
                        val ind = this.killing_lst.indexOf(this.current_player+1)
                        if (ind == this.killing_lst.size-1) {
                            this.current_player = 0
                            while (this.current_player < this.players_data.size && this.players_data[this.current_player].is_alive == false) this.current_player += 1
                            make_vote_screen()
                            inizializate_voting()
                        }
                        else{
                            this.current_player = this.killing_lst[ind+1]-1
                            make_vote_screen()
                            inizializate_voting_for_dead()}
                    }
                    true
                }
                else -> {true}
            }})
        }
        val runnable = object: Runnable {
            var i=0
            override fun run() {
                this.i += 1
                if (stop - i < 0){
                    textview.setTextSize(30f)
                    textview.setBackgroundColor(getColor(R.color.nothing))
                    next_player()
                    this.i = 0
                    val btn_end = findViewById<Button>(R.id.end_the_game_in_the_start_btn)
                    btn_end.setOnClickListener(View.OnClickListener { inizalizate_end_game_in_start()
                        findViewById<Button>(R.id.back_to_the_game_end_the_game_start).setOnClickListener(
                            View.OnClickListener { this.i = 0
                                pre_end_the_game_in_start()
                                val back_btn = findViewById<Button>(R.id.back_to_the_game_end_the_game_start)
                                back_btn.setOnClickListener({make_vote_screen()
                                    inizializate_voting()})
                                if (is_paused == false){
                                    make_next_player_voice(this)
                                    handler.removeCallbacks(this)
                                }})})
                } else {
                    textview.text = (stop-i).toString()
                    handler.postDelayed(this, 1000)
                }
            }
        }

        val exit_btn = findViewById<Button>(R.id.end_the_game_in_the_start_btn)
        exit_btn.setOnClickListener(OnClickListener {
            handler.removeCallbacks(runnable)
            this.is_paused = false
            pre_end_the_game_in_start()
            val back_btn = findViewById<Button>(R.id.back_to_the_game_end_the_game_start)
            back_btn.setOnClickListener({make_vote_screen()
                inizializate_voting_for_dead()}) })

        btn_next.setOnTouchListener(View.OnTouchListener { v, event -> when(event.actionMasked){
            MotionEvent.ACTION_DOWN -> {
                btn_next.setImageResource(R.drawable.start_touched_anim)
                true
            }
            MotionEvent.ACTION_UP -> {
                btn_next.setImageResource(R.drawable.start)
                if ((btn_next.width > event.getX() && event.getX() > 0) && (btn_next.height > event.getY() && event.getY() > 0)) {
                    make_next_player_voice(runnable)
                }
                true
            }
            else -> {true}
        } })
        val btn_pause = findViewById<ImageButton>(R.id.pause_btn_vote_screen)
        btn_pause.setOnClickListener(View.OnClickListener { when (this.is_paused) {
            false -> {
                btn_pause.setImageResource(R.drawable.play)
                this.is_paused = true
                if (timer_was_started == true) handler.removeCallbacks(runnable)
                true
            }
            true -> {
                btn_pause.setImageResource(R.drawable.pause)
                if (timer_was_started == true) handler.postDelayed(runnable, 1000)
                this.is_paused = false
                true
            }
        } })
    }
    fun make_vote_screen(){
        setContentView(R.layout.vote_screen)
        findViewById<TextView>(R.id.saying_lb_vote_screen).setText("Игрок #${this.current_player+1}\nследующий")

        val btn_end_the_game = findViewById<Button>(R.id.end_the_game_in_the_start_btn)
        btn_end_the_game.setOnClickListener(OnClickListener { pre_end_the_game_in_start() })

        val btn_pause = findViewById<ImageButton>(R.id.pause_btn_vote_screen)
        btn_pause.setOnClickListener(View.OnClickListener { when (this.is_paused) {
            false -> {
                btn_pause.setImageResource(R.drawable.play)
                this.is_paused = true
                true
            }
            true -> {
                btn_pause.setImageResource(R.drawable.pause)
                this.is_paused = false
                true
            }
        } })

        fun create_player_settings(context: Context, number: Int, name: String, result: TextView, save_res: OnClickListener){
            val notif_lt = LinearLayout(context)
            notif_lt.orientation = LinearLayout.VERTICAL
            notif_lt.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
            notif_lt.setBackgroundColor(getColor(R.color.background))

            val main_lt = LinearLayout(context)
            main_lt.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
            main_lt.orientation = LinearLayout.VERTICAL


            val txt1 = Button(context)
            txt1.setBackgroundColor(getColor(R.color.nothing))
            txt1.setOnClickListener(save_res)
            txt1.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f)

            val txt2 = Button(context)
            txt2.setBackgroundColor(getColor(R.color.nothing))
            txt2.setOnClickListener(save_res)
            txt2.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f)



            var tx = TextView(context)
            tx.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4f, this.resources.displayMetrics).toInt())
            var params = tx.layoutParams as ViewGroup.MarginLayoutParams
            tx.layoutParams = params
            tx.setBackgroundColor(getColor(R.color.white))
            tx.text = ""
            notif_lt.addView(tx)


            val name_tx = TextView(context)
            name_tx.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 3f)
            name_tx.text = "№${number} ${name}"
            name_tx.setTextColor(getColor(R.color.white))
            name_tx.gravity = Gravity.CENTER
            name_tx.setTextSize(30f)
            notif_lt.addView(name_tx)

            tx = TextView(this)
            tx.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4f, this.resources.displayMetrics).toInt())
            params = tx.layoutParams as ViewGroup.MarginLayoutParams
            params.leftMargin = 40
            params.rightMargin = 40
            tx.layoutParams = params
            tx.setBackgroundColor(getColor(R.color.white))
            tx.text = ""
            notif_lt.addView(tx)


            val settings_lt = LinearLayout(context)
            settings_lt.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 2f)
            params = settings_lt.layoutParams as ViewGroup.MarginLayoutParams
            params.leftMargin = 40
            params.rightMargin = 40
            settings_lt.layoutParams = params


            val lt1 = LinearLayout(context)
            lt1.orientation = LinearLayout.VERTICAL
            lt1.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 3f)

            val tx1 = TextView(context)
            tx1.gravity = Gravity.CENTER
            tx1.setText("Голос")
            tx1.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 3f)

            val btn1 = ImageButton(context)
            btn1.setBackgroundColor(getColor(R.color.nothing))
            btn1.setImageResource(R.drawable.like)
            btn1.scaleType = ImageView.ScaleType.FIT_CENTER
            btn1.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f)

            btn1.setOnClickListener(OnClickListener {
                var res:LinearLayout = LinearLayout(this)
                res.orientation = LinearLayout.VERTICAL
                res.setBackgroundResource(R.drawable.bg_keyboard_lst_players)
                params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT, 1f
                )
                params.rightMargin = 100
                params.leftMargin = 100
                params.topMargin = 50
                params.bottomMargin = 50
                res.layoutParams = params
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
                    if (this.players_data[ind-1].is_alive == false) btn.setTextColor(getColor(R.color.black))
                    else btn.setTextColor(getColor(R.color.white))
                    for (el in this.players_data) if (el.voices == ind) btn.setTextColor(getColor(R.color.btn_selected_lst_players))
                    btn.setOnTouchListener(View.OnTouchListener(){view, motionEvent ->
                        when(motionEvent.actionMasked){
                            MotionEvent.ACTION_DOWN -> {
                                btn.setTextColor(getColor(R.color.btn_is_selecting_lst_players))
                                true
                            }
                            MotionEvent.ACTION_UP -> {
                                if ((btn.width > motionEvent.getX() && motionEvent.getX() > 0) && (btn.height > motionEvent.getY() && motionEvent.getY() > 0)) {
                                    var is_was_in_other_player = false
                                    for (el in this.players_data) if (el.voices == ind) is_was_in_other_player = true
                                    if (is_was_in_other_player == false){
                                        this.players_data[number - 1].voices = ind
                                        notif_lt.removeView(res)
                                        txt2.layoutParams = LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                            1f
                                        )
                                        txt1.layoutParams = LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                            1f
                                        )
                                    } else btn.setTextColor(getColor(R.color.btn_selected_lst_players))
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
                    btn_clear.setOnTouchListener(View.OnTouchListener(){_, motionEvent ->
                        when(motionEvent.actionMasked){
                            MotionEvent.ACTION_DOWN ->{
                                btn_clear.setTextColor(getColor(R.color.cancel_keyboard_clicked_lst_players))
                                true
                            }
                            MotionEvent.ACTION_UP -> {
                                btn_clear.setTextColor(getColor(R.color.cancel_keyboard_lst_players))
                                if ((btn_clear.width > motionEvent.getX() && motionEvent.getX() > 0) && (btn_clear.height > motionEvent.getY() && motionEvent.getY() > 0)) {
                                    notif_lt.removeView(res)
                                    this.players_data[number-1].voices = 0
                                    txt2.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
                                    txt1.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
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
                txt2.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 2f)
                txt1.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 2f)
                notif_lt.addView(res)
            })

            lt1.addView(tx1)
            lt1.addView(btn1)

            settings_lt.addView(lt1)

            val lt2 = LinearLayout(context)
            lt2.orientation = LinearLayout.VERTICAL
            lt2.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 2f)

            val tx2 = TextView(context)
            tx2.gravity = Gravity.CENTER
            tx2.setText("Допы")
            tx2.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 3f)

            val btn2 = LinearLayout(context)
            btn2.orientation = LinearLayout.HORIZONTAL
            btn2.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f)



            val btn_left = Button(context)
            btn_left.setTextColor(getColor(R.color.white))
            btn_left.setText("<")
            btn_left.gravity = Gravity.CENTER
            btn_left.setTextSize(20f)
            btn_left.setBackgroundColor(getColor(R.color.nothing))
            btn_left.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
            btn_left.setOnClickListener(View.OnClickListener {
                this.players_data[number-1].extras -= this.step
                if (this.players_data[number-1].extras > 0) result.text = "+"
                else result.text = ""
                if (this.players_data[number-1].extras == this.players_data[number-1].extras.toInt().toFloat()) result.text = result.text.toString() + this.players_data[number-1].extras.toInt().toString()
                else result.text = result.text.toString() + this.players_data[number-1].extras.toString()
            })



            val btn_rigth = Button(context)
            btn_rigth.setTextColor(getColor(R.color.white))
            btn_rigth.setText(">")
            btn_rigth.gravity = Gravity.CENTER
            btn_rigth.setTextSize(20f)
            btn_rigth.setBackgroundColor(getColor(R.color.nothing))
            btn_rigth.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
            btn_rigth.setOnClickListener(View.OnClickListener {
                this.players_data[number-1].extras += this.step
                if (this.players_data[number-1].extras > 0) result.text = "+"
                else result.text = ""
                if (this.players_data[number-1].extras == this.players_data[number-1].extras.toInt().toFloat()) result.text = result.text.toString() + this.players_data[number-1].extras.toInt().toString()
                else result.text = result.text.toString() + this.players_data[number-1].extras.toString()
            })

            btn2.addView(btn_left)
            btn2.addView(result)
            btn2.addView(btn_rigth)


            lt2.addView(tx2)
            lt2.addView(btn2)

            settings_lt.addView(lt2)


            val lt3 = LinearLayout(context)
            lt3.orientation = LinearLayout.VERTICAL
            lt3.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 3f)

            val tx3 = TextView(context)
            tx3.gravity = Gravity.CENTER
            tx3.setText("Фолы")
            tx3.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 3f)

            val btn3 = ImageButton(context)
            btn3.setBackgroundColor(getColor(R.color.nothing))
            when (this.players_data[number-1].Fols) {
                0 -> btn3.setImageResource(R.drawable.zero_fols)
                1 -> btn3.setImageResource(R.drawable.one_fol)
                2 -> btn3.setImageResource(R.drawable.two_fols)
                3 -> btn3.setImageResource(R.drawable.three_fols)
                4 -> btn3.setImageResource(R.drawable.four_fols)
            }
            btn3.scaleType = ImageView.ScaleType.FIT_CENTER
            btn3.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
            btn3.setOnClickListener(OnClickListener {
                this.players_data[number-1].Fols = this.players_data[number-1].Fols%4+1-(this.players_data[number-1].Fols/4).toInt()
                when (this.players_data[number-1].Fols) {
                    0 -> {
                        btn3.setImageResource(R.drawable.zero_fols)
                        this.players_data[number-1].is_alive = true
                    }
                    1 -> {
                        btn3.setImageResource(R.drawable.one_fol)
                        this.players_data[number-1].is_alive = true
                    }
                    2 -> {
                        btn3.setImageResource(R.drawable.two_fols)
                        this.players_data[number-1].is_alive = true
                    }
                    3 -> {
                        btn3.setImageResource(R.drawable.three_fols)
                        this.players_data[number-1].is_alive = true
                    }
                    4 -> {btn3.setImageResource(R.drawable.four_fols)
                        this.players_data[number-1].is_alive = false}
                }
            })
            btn3.setOnLongClickListener {
                this.players_data[number-1].is_alive = false
                this.players_data[number-1].Fols = 4
                btn3.setImageResource(R.drawable.four_fols)
                true }

            lt3.addView(tx3)
            lt3.addView(btn3)

            settings_lt.addView(lt3)




            notif_lt.addView(settings_lt)

            tx = TextView(context)
            tx.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4f, this.resources.displayMetrics).toInt())
            params = tx.layoutParams as ViewGroup.MarginLayoutParams
            tx.layoutParams = params
            tx.setBackgroundColor(getColor(R.color.white))
            tx.text = ""
            notif_lt.addView(tx)




            main_lt.addView(txt1)
            main_lt.addView(notif_lt)
            main_lt.addView(txt2)

            findViewById<FrameLayout>(R.id.main_vote_layout_screen).addView(main_lt, 1)
        }
        fun createPlayerListItem(context: Context, playerNumber: Int, playerName: String, playerPosition: String, parentView: ViewGroup, is_alive:Boolean, is_selected:Boolean, dop: Float, fol:Int) {
            val linearLayout = LinearLayout(context)
            val params = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            params.bottomMargin = 5
            linearLayout.layoutParams = params
            linearLayout.orientation = LinearLayout.HORIZONTAL
            linearLayout.setBackgroundResource(R.drawable.lst_element) // Replace with your drawable
            linearLayout.setPadding(0, 0, 0, 0) //Added padding bottom



            // Player Number TextView
            val playerNumberTextView = TextView(context)
            playerNumberTextView.setTextSize(15f)
            playerNumberTextView.layoutParams = LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.MATCH_PARENT,
                4f
            )
            playerNumberTextView.gravity = Gravity.CENTER
            playerNumberTextView.textAlignment = View.TEXT_ALIGNMENT_CENTER
            playerNumberTextView.setBackgroundResource(R.drawable.btn_element) //Replace with your drawable
            playerNumberTextView.setTextColor(getColor(R.color.white))
            playerNumberTextView.text = playerNumber.toString()

            if (is_selected == true) {
                playerNumberTextView.setBackgroundResource(R.drawable.lst_element_white)
                playerNumberTextView.setTextColor(getColor(R.color.black))
            }
            else if (is_alive == false){
                playerNumberTextView.setTextColor(getColor(R.color.black))
            }

            linearLayout.addView(playerNumberTextView)
            val player_icon = ImageView(context)
            player_icon.layoutParams = LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.MATCH_PARENT,
                2f
            )
            when (playerPosition){
                "Мирный" -> player_icon.setImageResource(R.drawable.peacful_icon)
                "Маньяк" -> player_icon.setImageResource(R.drawable.maniac_icon)
                "Шериф" -> player_icon.setImageResource(R.drawable.sheriff_icon)
                "Красотка" -> player_icon.setImageResource(R.drawable.gorgeous_icon)
                "Доктор" -> player_icon.setImageResource(R.drawable.doctor_icon)
                "Мафия" -> player_icon.setImageResource(R.drawable.mafia_icon)
                "Дон" -> player_icon.setImageResource(R.drawable.don_icon)
                else -> player_icon.setImageResource(R.drawable.peacful_icon)
            }
            player_icon.foregroundGravity = Gravity.CENTER
            player_icon.scaleType = ImageView.ScaleType.FIT_CENTER
            player_icon.setBackgroundColor(getColor(R.color.nothing))
            linearLayout.addView(player_icon)



            // Player Name TextView
            val playerNameTextView = TextView(context)
            playerNameTextView.setTextSize(13f)
            playerNameTextView.layoutParams = LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                14f
            )
            playerNameTextView.setPadding(30, 10, 0, 10)
            playerNameTextView.text = playerName

            if (is_alive == false) playerNameTextView.setTextColor(getColor(R.color.black))

            linearLayout.addView(playerNameTextView)


            // Player Position TextView
            val playerPositionTextView = TextView(context)
            playerPositionTextView.setTextSize(15f)
            playerPositionTextView.layoutParams = LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                4f
            )
            if (this.players_data[playerNumber-1].voices != 0) playerPositionTextView.text = this.players_data[playerNumber-1].voices.toString()
            else playerPositionTextView.text = ""
            linearLayout.addView(playerPositionTextView)

            val playerPositionTextView2 = TextView(context)
            playerPositionTextView2.setTextSize(15f)
            playerPositionTextView2.layoutParams = LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                4f
            )
            if (dop > 0) playerPositionTextView2.setTextColor(getColor(R.color.green))
            else if (dop < 0) playerPositionTextView2.setTextColor(getColor(R.color.red))
            else playerPositionTextView2.setTextColor(getColor(R.color.btn_is_selecting_lst_players))
            if (dop == dop.toInt().toFloat()) playerPositionTextView2.text = dop.toInt().toString()
            else playerPositionTextView2.text = dop.toString()
            linearLayout.addView(playerPositionTextView2)

            val playerPositionTextView3 = TextView(context)
            playerPositionTextView3.setTextSize(15f)
            playerPositionTextView3.layoutParams = LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                4f
            )
            when (fol) {
                0 -> playerPositionTextView3.text = ""
                1 -> playerPositionTextView3.text = "|"
                2 -> {playerPositionTextView3.text = "||"
                    playerPositionTextView3.setTextColor(getColor(R.color.two_fols))}
                3 -> {playerPositionTextView3.text = "|||"
                    playerPositionTextView3.setTextColor(getColor(R.color.red))}
                4 -> {playerPositionTextView3.text = "||||"
                    playerPositionTextView3.setTextColor(getColor(R.color.red))}
            }
            linearLayout.addView(playerPositionTextView3)

            parentView.addView(linearLayout)
        }


        val layout = findViewById<LinearLayout>(R.id.lst_players_vote_screen)
        for ((i, el) in this.players_data.withIndex()){
            val result = TextView(this)
            result.setTextColor(getColor(R.color.white))
            result.setTextSize(20f)
            result.gravity = Gravity.CENTER
            if (this.players_data[i].extras > 0) result.text = "+"
            else result.text = ""
            if (this.players_data[i].extras == this.players_data[i].extras.toInt().toFloat()) result.text = result.text.toString() + this.players_data[i].extras.toInt().toString()
            else result.text = result.text.toString() + this.players_data[i].extras.toString()
            result.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f)

            fun save_res(result: TextView, number: Int ){
                findViewById<FrameLayout>(R.id.main_vote_layout_screen).removeViewAt(1)
                var ans = ""

                for (ii in result.text){
                    if (listOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.').contains(ii)) ans = ans + ii.toString()
                }
                when(result.text[0]){
                    '+' -> this.players_data[number].extras = ans.toFloat()
                    else -> this.players_data[number].extras = -ans.toFloat()
                }

                val layout = findViewById<LinearLayout>(R.id.lst_players_vote_screen)
                layout.removeAllViews()
                for ((ii, el) in this.players_data.withIndex()){
                    val result = TextView(this)
                    result.setTextColor(getColor(R.color.white))
                    result.setTextSize(20f)
                    result.gravity = Gravity.CENTER
                    if (this.players_data[ii].extras > 0) result.text = "+"
                    else result.text = ""
                    if (this.players_data[ii].extras == this.players_data[ii].extras.toInt().toFloat()) result.text = result.text.toString() + this.players_data[ii].extras.toInt().toString()
                    else result.text = result.text.toString() + this.players_data[ii].extras.toString()
                    result.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
                    createPlayerListItem(this, ii+1, el.name, el.role, layout, el.is_alive, ii == this.current_player, el.extras, el.Fols)
                    layout.getChildAt(layout.childCount-1).setOnClickListener(View.OnClickListener { create_player_settings(this, ii+1, el.name, result, OnClickListener {save_res(result, number)}) })
                }
            }

            createPlayerListItem(this, i+1, el.name, el.role, layout, el.is_alive, i == this.current_player, el.extras, el.Fols)
            layout.getChildAt(layout.childCount-1).setOnClickListener(View.OnClickListener { create_player_settings(this, i+1, el.name, result, OnClickListener {save_res(result, i)}) })
        }
    }
    fun before_voting(){
        this.voting_candidates = mutableListOf()
        for (el in this.players_data) if (el.voices != 0 && el.voices in this.voting_candidates == false) this.voting_candidates.add(el.voices)

        if (this.voting_candidates.isNotEmpty()) {
            setContentView(R.layout.vote_screen)

            val btn_end_the_game = findViewById<Button>(R.id.end_the_game_in_the_start_btn)
            btn_end_the_game.setOnClickListener(OnClickListener { pre_end_the_game_in_start()
                val back_btn = findViewById<Button>(R.id.back_to_the_game_end_the_game_start)
                back_btn.setOnClickListener({make_vote_screen()
                    inizializate_voting()})})

            findViewById<TextView>(R.id.text_lb_next_vote_screen).text = "К голосованию"

            this.current_player = this.voting_candidates[0]

            val next_btn = findViewById<ImageButton>(R.id.start_next_btn_vote_screen)
            next_btn.setOnTouchListener(View.OnTouchListener { _, event ->
                when (event.actionMasked) {
                    MotionEvent.ACTION_DOWN -> {
                        next_btn.setImageResource(R.drawable.start_touched_anim)
                        true
                    }

                    MotionEvent.ACTION_UP -> {
                        next_btn.setImageResource(R.drawable.start)
                        if ((next_btn.width > event.getX() && event.getX() > 0) && (next_btn.height > event.getY() && event.getY() > 0)) {
                            setContentView(R.layout.show_night_screen)
                            findViewById<TextView>(R.id.what_day_lb).text = "Голосование"
                            findViewById<TextView>(R.id.text_lb_night_screen).text = "Начать"
                            val next_next_btn = findViewById<ImageButton>(R.id.start_night_btn)
                            next_next_btn.setOnTouchListener(View.OnTouchListener { _, event ->
                                when (event.actionMasked) {
                                    MotionEvent.ACTION_DOWN -> {
                                        next_next_btn.setImageResource(R.drawable.start_touched_anim)
                                        true
                                    }

                                    MotionEvent.ACTION_UP -> {
                                        if ((next_next_btn.width > event.getX() && event.getX() > 0) && (next_next_btn.height > event.getY() && event.getY() > 0)) {
                                            vote()
                                        }
                                        true
                                    }

                                    else -> {
                                        true
                                    }
                                }
                            })
                        }
                        true
                    }

                    else -> {
                        true
                    }
                }
            })
            val tx = findViewById<TextView>(R.id.saying_lb_vote_screen)
            tx.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT,
                15f
            )

            var voices = mutableListOf<Int>()
            for (el in this.players_data) if (el.voices != 0 && el.voices in voices == false) voices.add(el.voices)

            if (voices.isNotEmpty()) {
                var ans = ""
                voices.forEach { ans += it.toString() + "   " }
                tx.text = "Выставленны игроки\n" + ans
            } else tx.text = "В этот день никто не был выставлен на голосование"

            val btn_pause = findViewById<ImageButton>(R.id.pause_btn_vote_screen)
            btn_pause.setOnClickListener(View.OnClickListener {
                when (this.is_paused) {
                    false -> {
                        btn_pause.setImageResource(R.drawable.play)
                        this.is_paused = true
                        true
                    }

                    true -> {
                        btn_pause.setImageResource(R.drawable.pause)
                        this.is_paused = false
                        true
                    }
                }
            })

            fun create_player_settings(
                context: Context,
                number: Int,
                name: String,
                result: TextView,
                save_res: OnClickListener
            ) {
                val notif_lt = LinearLayout(context)
                notif_lt.orientation = LinearLayout.VERTICAL
                notif_lt.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1f
                )
                notif_lt.setBackgroundColor(getColor(R.color.background))

                val main_lt = LinearLayout(context)
                main_lt.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
                )
                main_lt.orientation = LinearLayout.VERTICAL


                val txt1 = Button(context)
                txt1.setBackgroundColor(getColor(R.color.nothing))
                txt1.setOnClickListener(save_res)
                txt1.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1f
                )

                val txt2 = Button(context)
                txt2.setBackgroundColor(getColor(R.color.nothing))
                txt2.setOnClickListener(save_res)
                txt2.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1f
                )


                var tx = TextView(context)
                tx.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        4f,
                        this.resources.displayMetrics
                    ).toInt()
                )
                var params = tx.layoutParams as ViewGroup.MarginLayoutParams
                tx.layoutParams = params
                tx.setBackgroundColor(getColor(R.color.white))
                tx.text = ""
                notif_lt.addView(tx)


                val name_tx = TextView(context)
                name_tx.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    3f
                )
                name_tx.text = "№${number} ${name}"
                name_tx.setTextColor(getColor(R.color.white))
                name_tx.gravity = Gravity.CENTER
                name_tx.setTextSize(30f)
                notif_lt.addView(name_tx)

                tx = TextView(this)
                tx.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        4f,
                        this.resources.displayMetrics
                    ).toInt()
                )
                params = tx.layoutParams as ViewGroup.MarginLayoutParams
                params.leftMargin = 40
                params.rightMargin = 40
                tx.layoutParams = params
                tx.setBackgroundColor(getColor(R.color.white))
                tx.text = ""
                notif_lt.addView(tx)


                val settings_lt = LinearLayout(context)
                settings_lt.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    2f
                )
                params = settings_lt.layoutParams as ViewGroup.MarginLayoutParams
                params.leftMargin = 40
                params.rightMargin = 40
                settings_lt.layoutParams = params


                val lt1 = LinearLayout(context)
                lt1.orientation = LinearLayout.VERTICAL
                lt1.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    3f
                )

                val tx1 = TextView(context)
                tx1.gravity = Gravity.CENTER
                tx1.setText("Голос")
                tx1.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    3f
                )

                val btn1 = ImageButton(context)
                btn1.setBackgroundColor(getColor(R.color.nothing))
                btn1.setImageResource(R.drawable.like)
                btn1.scaleType = ImageView.ScaleType.FIT_CENTER
                btn1.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1f
                )

                btn1.setOnClickListener(OnClickListener {
                    var res: LinearLayout = LinearLayout(this)
                    res.orientation = LinearLayout.VERTICAL
                    res.setBackgroundResource(R.drawable.bg_keyboard_lst_players)
                    params = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT, 1f
                    )
                    params.rightMargin = 100
                    params.leftMargin = 100
                    params.topMargin = 50
                    params.bottomMargin = 50
                    res.layoutParams = params
                    fun make_btn_keyboard(ind: Int): Button {
                        val btn = Button(this)
                        val params = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            1f
                        )
                        params.rightMargin = 10
                        params.leftMargin = 10
                        params.topMargin = 10
                        params.bottomMargin = 10
                        btn.layoutParams = params
                        btn.setBackgroundResource(R.drawable.keyboard_btn_lst_players)
                        btn.setText(ind.toString())
                        btn.setTextSize(30f)
                        btn.setTextColor(getColor(R.color.white))
                        btn.setOnTouchListener(View.OnTouchListener() { view, motionEvent ->
                            when (motionEvent.actionMasked) {
                                MotionEvent.ACTION_DOWN -> {
                                    btn.setTextColor(getColor(R.color.btn_is_selecting_lst_players))
                                    true
                                }

                                MotionEvent.ACTION_UP -> {
                                    if ((btn.width > motionEvent.getX() && motionEvent.getX() > 0) && (btn.height > motionEvent.getY() && motionEvent.getY() > 0)) {
                                        notif_lt.removeView(res)
                                        this.players_data[number - 1].voices = ind
                                        txt2.layoutParams = LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                            1f
                                        )
                                        txt1.layoutParams = LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                            1f
                                        )
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

                    fun make_invisible_btn(): TextView {
                        val tx = TextView(this)
                        val params = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            1f
                        )
                        tx.layoutParams = params
                        tx.setText("")
                        tx.setBackgroundColor(getColor(R.color.background_keyboard_lst_players))
                        tx.setTextSize(30f)
                        tx.setTextColor(getColor(R.color.white))
                        return tx
                    }






                    for (i in 0..this.players.size / 4 - 1) {
                        var l = LinearLayout(this)
                        params = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            1f
                        )
                        l.layoutParams = params
                        l.orientation = LinearLayout.HORIZONTAL
                        l.addView(make_btn_keyboard(i * 4 + 1))
                        l.addView(make_btn_keyboard(i * 4 + 2))
                        l.addView(make_btn_keyboard(i * 4 + 3))
                        l.addView(make_btn_keyboard(i * 4 + 4))
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
                    if (players.size % 4 != 0) {

                        for (i in 1..this.players.size % 4) {
                            l.addView(make_btn_keyboard(i + (this.players.size / 4) * 4))
                        }

                        val btn_clear = Button(this)
                        params = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            1f
                        )
                        params.rightMargin = 10
                        params.leftMargin = 10
                        params.topMargin = 10
                        params.bottomMargin = 10
                        btn_clear.layoutParams = params
                        btn_clear.setBackgroundResource(R.drawable.keyboard_btn_lst_players)
                        btn_clear.setText("X")
                        btn_clear.setTextSize(27f)
                        btn_clear.setTextColor(getColor(R.color.cancel_keyboard_lst_players))
                        btn_clear.setOnTouchListener(View.OnTouchListener() { _, motionEvent ->
                            when (motionEvent.actionMasked) {
                                MotionEvent.ACTION_DOWN -> {
                                    btn_clear.setTextColor(getColor(R.color.cancel_keyboard_clicked_lst_players))
                                    true
                                }

                                MotionEvent.ACTION_UP -> {
                                    btn_clear.setTextColor(getColor(R.color.cancel_keyboard_lst_players))
                                    if ((btn_clear.width > motionEvent.getX() && motionEvent.getX() > 0) && (btn_clear.height > motionEvent.getY() && motionEvent.getY() > 0)) {
                                        notif_lt.removeView(res)
                                        this.players_data[number - 1].voices = 0
                                        txt2.layoutParams = LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                            1f
                                        )
                                        txt1.layoutParams = LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                            1f
                                        )
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
                        params = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            1f
                        )
                        params.rightMargin = 10
                        params.leftMargin = 10
                        params.topMargin = 10
                        params.bottomMargin = 10
                        btn_clear.layoutParams = params
                        btn_clear.setBackgroundResource(R.drawable.keyboard_btn_lst_players)
                        btn_clear.setText("X")
                        btn_clear.setTextSize(27f)
                        btn_clear.setTextColor(getColor(R.color.cancel_keyboard_lst_players))
                        btn_clear.setOnTouchListener(View.OnTouchListener() { view, motionEvent ->
                            when (motionEvent.actionMasked) {
                                MotionEvent.ACTION_DOWN -> {
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
                        for (i in 1..3) {
                            l.addView(make_invisible_btn())
                        }
                    }
                    res.addView(l)
                    txt2.layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        2f
                    )
                    txt1.layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        2f
                    )
                    notif_lt.addView(res)
                })

                lt1.addView(tx1)
                lt1.addView(btn1)

                settings_lt.addView(lt1)

                val lt2 = LinearLayout(context)
                lt2.orientation = LinearLayout.VERTICAL
                lt2.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    2f
                )

                val tx2 = TextView(context)
                tx2.gravity = Gravity.CENTER
                tx2.setText("Допы")
                tx2.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    3f
                )

                val btn2 = LinearLayout(context)
                btn2.orientation = LinearLayout.HORIZONTAL
                btn2.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1f
                )


                val btn_left = Button(context)
                btn_left.setTextColor(getColor(R.color.white))
                btn_left.setText("<")
                btn_left.gravity = Gravity.CENTER
                btn_left.setTextSize(20f)
                btn_left.setBackgroundColor(getColor(R.color.nothing))
                btn_left.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1f
                )
                btn_left.setOnClickListener(View.OnClickListener {
                    this.players_data[number - 1].extras -= this.step
                    if (this.players_data[number - 1].extras > 0) result.text = "+"
                    else result.text = ""
                    if (this.players_data[number - 1].extras == this.players_data[number - 1].extras.toInt()
                            .toFloat()
                    ) result.text =
                        result.text.toString() + this.players_data[number - 1].extras.toInt()
                            .toString()
                    else result.text =
                        result.text.toString() + this.players_data[number - 1].extras.toString()
                })


                val btn_rigth = Button(context)
                btn_rigth.setTextColor(getColor(R.color.white))
                btn_rigth.setText(">")
                btn_rigth.gravity = Gravity.CENTER
                btn_rigth.setTextSize(20f)
                btn_rigth.setBackgroundColor(getColor(R.color.nothing))
                btn_rigth.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1f
                )
                btn_rigth.setOnClickListener(View.OnClickListener {
                    this.players_data[number - 1].extras += this.step
                    if (this.players_data[number - 1].extras > 0) result.text = "+"
                    else result.text = ""
                    if (this.players_data[number - 1].extras == this.players_data[number - 1].extras.toInt()
                            .toFloat()
                    ) result.text =
                        result.text.toString() + this.players_data[number - 1].extras.toInt()
                            .toString()
                    else result.text =
                        result.text.toString() + this.players_data[number - 1].extras.toString()
                })

                btn2.addView(btn_left)
                btn2.addView(result)
                btn2.addView(btn_rigth)


                lt2.addView(tx2)
                lt2.addView(btn2)

                settings_lt.addView(lt2)


                val lt3 = LinearLayout(context)
                lt3.orientation = LinearLayout.VERTICAL
                lt3.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    3f
                )

                val tx3 = TextView(context)
                tx3.gravity = Gravity.CENTER
                tx3.setText("Фолы")
                tx3.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    3f
                )

                val btn3 = ImageButton(context)
                btn3.setBackgroundColor(getColor(R.color.nothing))
                when (this.players_data[number - 1].Fols) {
                    0 -> btn3.setImageResource(R.drawable.zero_fols)
                    1 -> btn3.setImageResource(R.drawable.one_fol)
                    2 -> btn3.setImageResource(R.drawable.two_fols)
                    3 -> btn3.setImageResource(R.drawable.three_fols)
                    4 -> btn3.setImageResource(R.drawable.four_fols)
                }
                btn3.scaleType = ImageView.ScaleType.FIT_CENTER
                btn3.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1f
                )
                btn3.setOnClickListener(OnClickListener {
                    this.players_data[number - 1].Fols =
                        this.players_data[number - 1].Fols % 4 + 1 - (this.players_data[number - 1].Fols / 4).toInt()
                    when (this.players_data[number - 1].Fols) {
                        0 -> {
                            btn3.setImageResource(R.drawable.zero_fols)
                            this.players_data[number - 1].is_alive = true
                        }

                        1 -> {
                            btn3.setImageResource(R.drawable.one_fol)
                            this.players_data[number - 1].is_alive = true
                        }

                        2 -> {
                            btn3.setImageResource(R.drawable.two_fols)
                            this.players_data[number - 1].is_alive = true
                        }

                        3 -> {
                            btn3.setImageResource(R.drawable.three_fols)
                            this.players_data[number - 1].is_alive = true
                        }

                        4 -> {
                            btn3.setImageResource(R.drawable.four_fols)
                            this.players_data[number - 1].is_alive = false
                        }
                    }
                })

                lt3.addView(tx3)
                lt3.addView(btn3)

                settings_lt.addView(lt3)




                notif_lt.addView(settings_lt)

                tx = TextView(context)
                tx.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        4f,
                        this.resources.displayMetrics
                    ).toInt()
                )
                params = tx.layoutParams as ViewGroup.MarginLayoutParams
                tx.layoutParams = params
                tx.setBackgroundColor(getColor(R.color.white))
                tx.text = ""
                notif_lt.addView(tx)




                main_lt.addView(txt1)
                main_lt.addView(notif_lt)
                main_lt.addView(txt2)

                findViewById<FrameLayout>(R.id.main_vote_layout_screen).addView(main_lt, 1)
            }

            fun createPlayerListItem(
                context: Context,
                playerNumber: Int,
                playerName: String,
                playerPosition: String,
                parentView: ViewGroup,
                is_alive: Boolean,
                is_selected: Boolean,
                dop: Float,
                fol: Int
            ) {
                val linearLayout = LinearLayout(context)
                val params = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                params.bottomMargin = 5
                linearLayout.layoutParams = params
                linearLayout.orientation = LinearLayout.HORIZONTAL
                linearLayout.setBackgroundResource(R.drawable.lst_element) // Replace with your drawable
                linearLayout.setPadding(0, 0, 0, 0) //Added padding bottom


                // Player Number TextView
                val playerNumberTextView = TextView(context)
                playerNumberTextView.setTextSize(15f)
                playerNumberTextView.layoutParams = LinearLayout.LayoutParams(
                    0,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    4f
                )
                playerNumberTextView.gravity = Gravity.CENTER
                playerNumberTextView.textAlignment = View.TEXT_ALIGNMENT_CENTER
                playerNumberTextView.setBackgroundResource(R.drawable.btn_element) //Replace with your drawable
                playerNumberTextView.setTextColor(getColor(R.color.white))
                playerNumberTextView.text = playerNumber.toString()

                if (is_selected == true) {
                    playerNumberTextView.setBackgroundResource(R.drawable.lst_element_white)
                    playerNumberTextView.setTextColor(getColor(R.color.black))
                } else if (is_alive == false) {
                    playerNumberTextView.setTextColor(getColor(R.color.black))
                }

                linearLayout.addView(playerNumberTextView)
                val player_icon = ImageView(context)
                player_icon.layoutParams = LinearLayout.LayoutParams(
                    0,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    2f
                )
                when (playerPosition) {
                    "Мирный" -> player_icon.setImageResource(R.drawable.peacful_icon)
                    "Маньяк" -> player_icon.setImageResource(R.drawable.maniac_icon)
                    "Шериф" -> player_icon.setImageResource(R.drawable.sheriff_icon)
                    "Красотка" -> player_icon.setImageResource(R.drawable.gorgeous_icon)
                    "Доктор" -> player_icon.setImageResource(R.drawable.doctor_icon)
                    "Мафия" -> player_icon.setImageResource(R.drawable.mafia_icon)
                    "Дон" -> player_icon.setImageResource(R.drawable.don_icon)
                    else -> player_icon.setImageResource(R.drawable.peacful_icon)
                }
                player_icon.foregroundGravity = Gravity.CENTER
                player_icon.scaleType = ImageView.ScaleType.FIT_CENTER
                player_icon.setBackgroundColor(getColor(R.color.nothing))
                linearLayout.addView(player_icon)


                // Player Name TextView
                val playerNameTextView = TextView(context)
                playerNameTextView.setTextSize(13f)
                playerNameTextView.layoutParams = LinearLayout.LayoutParams(
                    0,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    14f
                )
                playerNameTextView.setPadding(30, 10, 0, 10)
                playerNameTextView.text = playerName

                if (is_alive == false) playerNameTextView.setTextColor(getColor(R.color.black))

                linearLayout.addView(playerNameTextView)


                // Player Position TextView
                val playerPositionTextView = TextView(context)
                playerPositionTextView.setTextSize(15f)
                playerPositionTextView.layoutParams = LinearLayout.LayoutParams(
                    0,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    4f
                )
                if (this.players_data[playerNumber - 1].voices != 0) playerPositionTextView.text =
                    this.players_data[playerNumber - 1].voices.toString()
                else playerPositionTextView.text = ""
                linearLayout.addView(playerPositionTextView)

                val playerPositionTextView2 = TextView(context)
                playerPositionTextView2.setTextSize(15f)
                playerPositionTextView2.layoutParams = LinearLayout.LayoutParams(
                    0,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    4f
                )
                if (dop > 0) playerPositionTextView2.setTextColor(getColor(R.color.green))
                else if (dop < 0) playerPositionTextView2.setTextColor(getColor(R.color.red))
                else playerPositionTextView2.setTextColor(getColor(R.color.btn_is_selecting_lst_players))
                if (dop == dop.toInt().toFloat()) playerPositionTextView2.text =
                    dop.toInt().toString()
                else playerPositionTextView2.text = dop.toString()
                linearLayout.addView(playerPositionTextView2)

                val playerPositionTextView3 = TextView(context)
                playerPositionTextView3.setTextSize(15f)
                playerPositionTextView3.layoutParams = LinearLayout.LayoutParams(
                    0,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    4f
                )
                when (fol) {
                    0 -> playerPositionTextView3.text = ""
                    1 -> playerPositionTextView3.text = "|"
                    2 -> {
                        playerPositionTextView3.text = "||"
                        playerPositionTextView3.setTextColor(getColor(R.color.two_fols))
                    }

                    3 -> {
                        playerPositionTextView3.text = "|||"
                        playerPositionTextView3.setTextColor(getColor(R.color.red))
                    }

                    4 -> {
                        playerPositionTextView3.text = "||||"
                        playerPositionTextView3.setTextColor(getColor(R.color.red))
                    }
                }
                linearLayout.addView(playerPositionTextView3)

                parentView.addView(linearLayout)
            }

            val layout = findViewById<LinearLayout>(R.id.lst_players_vote_screen)
            for ((i, el) in this.players_data.withIndex()) {
                val result = TextView(this)
                result.setTextColor(getColor(R.color.white))
                result.setTextSize(20f)
                result.gravity = Gravity.CENTER
                if (this.players_data[i].extras > 0) result.text = "+"
                else result.text = ""
                if (this.players_data[i].extras == this.players_data[i].extras.toInt()
                        .toFloat()
                ) result.text =
                    result.text.toString() + this.players_data[i].extras.toInt().toString()
                else result.text = result.text.toString() + this.players_data[i].extras.toString()
                result.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1f
                )

                fun save_res(result: TextView, number: Int) {
                    findViewById<FrameLayout>(R.id.main_vote_layout_screen).removeViewAt(1)
                    var ans = ""

                    for (ii in result.text) {
                        if (listOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.').contains(
                                ii
                            )
                        ) ans = ans + ii.toString()
                    }
                    when (result.text[0]) {
                        '+' -> this.players_data[number].extras = ans.toFloat()
                        else -> this.players_data[number].extras = -ans.toFloat()
                    }

                    val layout = findViewById<LinearLayout>(R.id.lst_players_vote_screen)
                    layout.removeAllViews()
                    for ((ii, el) in this.players_data.withIndex()) {
                        val result = TextView(this)
                        result.setTextColor(getColor(R.color.white))
                        result.setTextSize(20f)
                        result.gravity = Gravity.CENTER
                        if (this.players_data[ii].extras > 0) result.text = "+"
                        else result.text = ""
                        if (this.players_data[ii].extras == this.players_data[ii].extras.toInt()
                                .toFloat()
                        ) result.text =
                            result.text.toString() + this.players_data[ii].extras.toInt().toString()
                        else result.text =
                            result.text.toString() + this.players_data[ii].extras.toString()
                        result.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            1f
                        )
                        createPlayerListItem(
                            this,
                            ii + 1,
                            el.name,
                            el.role,
                            layout,
                            el.is_alive,
                            false,
                            el.extras,
                            el.Fols
                        )
                        layout.getChildAt(layout.childCount - 1)
                            .setOnClickListener(View.OnClickListener {
                                create_player_settings(
                                    this,
                                    ii + 1,
                                    el.name,
                                    result,
                                    OnClickListener { save_res(result, number) })
                            })
                    }
                }

                createPlayerListItem(
                    this,
                    i + 1,
                    el.name,
                    el.role,
                    layout,
                    el.is_alive,
                    false,
                    el.extras,
                    el.Fols
                )
                layout.getChildAt(layout.childCount - 1).setOnClickListener(View.OnClickListener {
                    create_player_settings(
                        this,
                        i + 1,
                        el.name,
                        result,
                        OnClickListener { save_res(result, i) })
                })
            }
        } else end_the_day()
    }
    fun vote(){
        this.current_player -= 1
        make_vote_screen()
        this.current_player += 1
        var alive_size = 0
        for (el in this.players_data) if (el.is_alive == true) alive_size += 1
        this.voting_result[this.current_player] = 0



        val lt = findViewById<LinearLayout>(R.id.main_vote_layout_vote_screen)
        lt.removeView(findViewById<TextView>(R.id.saying_lb_vote_screen))

        val tt_layout = LinearLayout(this)
        tt_layout.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 15f)
        tt_layout.orientation = LinearLayout.HORIZONTAL

        val textview_tx = TextView(this)
        textview_tx.text = this.current_player.toString()
        textview_tx.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
        textview_tx.gravity = Gravity.CENTER
        textview_tx.setTextColor(getColor(R.color.white))
        textview_tx.setTextSize(100f)

        tt_layout.addView(textview_tx)

        val imgview_img = ImageView(this)
        imgview_img.setImageResource(R.drawable.dislike)
        imgview_img.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
        imgview_img.scaleType = ImageView.ScaleType.FIT_CENTER

        tt_layout.addView(imgview_img)

        val textview2_tx = TextView(this)
        textview2_tx.text = this.player_choice.toString()
        textview2_tx.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
        textview2_tx.gravity = Gravity.CENTER
        textview2_tx.setTextColor(getColor(R.color.white))
        textview2_tx.setTextSize(100f)

        tt_layout.addView(textview2_tx)

        lt.addView(tt_layout, 1)


        findViewById<TextView>(R.id.text_lb_next_vote_screen).text = "Следующее голосование"


        val layout = findViewById<LinearLayout>(R.id.lst_players_layout_vote_scren)
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
            if (ind <= alive_size) btn.setTextColor(getColor(R.color.white))
            else btn.setTextColor(getColor(R.color.black))
            btn.setOnTouchListener(View.OnTouchListener(){_, motionEvent ->
                when(motionEvent.actionMasked){
                    MotionEvent.ACTION_DOWN -> {
                        btn.setTextColor(getColor(R.color.btn_is_selecting_lst_players))
                        true
                    }
                    MotionEvent.ACTION_UP -> {
                        if ((btn.width > motionEvent.getX() && motionEvent.getX() > 0) && (btn.height > motionEvent.getY() && motionEvent.getY() > 0)) {
                            if (this.players_data[ind-1].is_alive == true) {
                                if (this.voting_result[this.current_player] != 0) this.choizen_btn.setTextColor(getColor(R.color.white))
                                this.voting_result[this.current_player] = ind
                                textview2_tx.text = ind.toString()
                                this.choizen_btn = btn
                                btn.setTextColor(getColor(R.color.btn_selected_lst_players))
                            }else btn.setTextColor(getColor(R.color.black))
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

        var tx = TextView(this)
        tx.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4f, this.resources.displayMetrics).toInt())
        var params = tx.layoutParams as ViewGroup.MarginLayoutParams
        tx.layoutParams = params
        tx.setBackgroundColor(getColor(R.color.white))
        tx.text = ""

        layout.addView(tx)


        var res:LinearLayout = LinearLayout(this)
        res.orientation = LinearLayout.VERTICAL
        res.setBackgroundResource(R.drawable.bg_keyboard_lst_players)
        params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            1f
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
                            if (this.voting_result[this.current_player] != 0) this.choizen_btn.setTextColor(getColor(R.color.white))
                            this.voting_result[this.current_player] = 0
                            textview2_tx.text = "0"
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
                            if (this.voting_result[this.current_player] != 0) this.choizen_btn.setTextColor(getColor(R.color.white))
                            this.voting_result[this.current_player] = 0
                            textview2_tx.text = "0"
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

        layout.addView(res)

        val tx1 = TextView(this)
        tx1.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4f, this.resources.displayMetrics).toInt())
        tx1.setBackgroundColor(getColor(R.color.white))
        tx1.text = ""

        layout.addView(tx1)

        val next_btn = findViewById<ImageButton>(R.id.start_next_btn_vote_screen)
        next_btn.setOnTouchListener(View.OnTouchListener { _, event -> when(event.actionMasked){
            MotionEvent.ACTION_DOWN -> {
                next_btn.setImageResource(R.drawable.start_touched_anim)
                true
            }
            MotionEvent.ACTION_UP -> {
                next_btn.setImageResource(R.drawable.start)
                if ((next_btn.width > event.getX() && event.getX() > 0) && (next_btn.height > event.getY() && event.getY() > 0)){
                    var ind = this.voting_candidates.indexOf(this.current_player)

                    if (ind+1 < this.voting_candidates.size){
                        this.current_player = this.voting_candidates[ind+1]
                        vote()
                    } else {
                        var max=0
                        var who = mutableListOf<Int>()

                        for (el in this.voting_result.keys){
                            if (this.voting_result[el]!! > max){
                                max = this.voting_result[el]!!
                                who = mutableListOf(el)
                            } else if (this.voting_result[el]!! == max) who.add(el)
                        }

                        if (who.size > 1){
                            this.voting_candidates = who
                            this.current_player = who[0]
                            before_popil()
                        } else {
                            lt.removeViewAt(1)
                            val textview = TextView(this)
                            textview.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 15f)
                            textview.text = "№${who[0]}\nвне игры"
                            textview.setTextColor(getColor(R.color.white))
                            textview.setBackgroundColor(getColor(R.color.red))
                            textview.gravity = Gravity.CENTER
                            textview.setTextSize(50f)
                            lt.addView(textview, 1)
                            findViewById<LinearLayout>(R.id.lst_players_layout_vote_scren).removeView(tx)
                            findViewById<LinearLayout>(R.id.lst_players_layout_vote_scren).removeView(tx1)
                            findViewById<LinearLayout>(R.id.lst_players_layout_vote_scren).removeView(res)
                            findViewById<TextView>(R.id.text_lb_next_vote_screen).text = "Закончить день"
                            this.players_data[who[0]-1].is_alive = false
                            this.killing_lst = who

                            next_btn.setOnTouchListener { _, event -> when(event.actionMasked){
                                MotionEvent.ACTION_DOWN -> {
                                    next_btn.setImageResource(R.drawable.start_touched_anim)
                                    true
                                }
                                MotionEvent.ACTION_UP -> {
                                    next_btn.setImageResource(R.drawable.start)
                                    if ((next_btn.width > event.getX() && event.getX() > 0) && (next_btn.height > event.getY() && event.getY() > 0)){
                                        this.current_player = 15
                                        setContentView(R.layout.vote_screen)
                                        this.voting_candidates.forEach { it-1 }
                                        this.killing_lst = this.voting_candidates
                                        this.current_player = this.voting_candidates[0]
                                        make_vote_screen()
                                        inizializate_voting_for_dead_voting_and_popil()
                                    }
                                    true
                                }
                                else -> {true}
                            } }
                        }
                    }
                }
                true
            }
            else -> {true}
        } })
    }
    fun who_will_win():String{
        var red_team = 0
        var black_team = 0
        var maniac_team = 0

        for (el in this.players_data) {
            if (el.role in listOf("Мирный", "Шериф", "Красотка", "Доктор")) red_team += 1
            else if (el.role in listOf("Мафия", "Дон")) black_team += 1
            else if (el.role == "Маньяк") maniac_team += 1
        }

        if (red_team != 0 && black_team == 0 && maniac_team == 0) return "red"
        else if (black_team != 0 && red_team <= black_team && maniac_team == 0) return "black"
        else if (maniac_team != 0 && red_team < maniac_team && black_team == 0) return "maniac"
        else if (maniac_team != 0 && black_team < maniac_team && red_team == 0) return "maniac"
        else return "none"
    }

    fun before_popil(){
        setContentView(R.layout.show_night_screen)
        findViewById<TextView>(R.id.what_day_lb).text = "Попил"
        findViewById<TextView>(R.id.text_lb_night_screen).text = "Начать"
        val next_next_btn = findViewById<ImageButton>(R.id.start_night_btn)
        next_next_btn.setOnTouchListener(View.OnTouchListener { _, event ->
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    next_next_btn.setImageResource(R.drawable.start_touched_anim)
                    true
                }

                MotionEvent.ACTION_UP -> {
                    if ((next_next_btn.width > event.getX() && event.getX() > 0) && (next_next_btn.height > event.getY() && event.getY() > 0)) {
                        popil()
                    }
                    true
                }

                else -> {
                    true
                }
            }
        })
    }
    fun popil(){
        this.current_player -= 1
        make_vote_screen()
        this.current_player += 1
        var alive_size = 0
        for (el in this.players_data) if (el.is_alive == true) alive_size += 1
        this.voting_result[this.current_player] = 0



        var lt = findViewById<LinearLayout>(R.id.main_vote_layout_vote_screen)
        lt.removeView(findViewById<TextView>(R.id.saying_lb_vote_screen))

        val tt_layout = LinearLayout(this)
        tt_layout.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 15f)
        tt_layout.orientation = LinearLayout.HORIZONTAL

        val textview_tx = TextView(this)
        textview_tx.text = this.current_player.toString()
        textview_tx.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
        textview_tx.gravity = Gravity.CENTER
        textview_tx.setTextColor(getColor(R.color.white))
        textview_tx.setTextSize(100f)

        tt_layout.addView(textview_tx)

        val imgview_img = ImageView(this)
        imgview_img.setImageResource(R.drawable.dislike)
        imgview_img.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
        imgview_img.scaleType = ImageView.ScaleType.FIT_CENTER

        tt_layout.addView(imgview_img)

        val textview2_tx = TextView(this)
        textview2_tx.text = this.player_choice.toString()
        textview2_tx.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
        textview2_tx.gravity = Gravity.CENTER
        textview2_tx.setTextColor(getColor(R.color.white))
        textview2_tx.setTextSize(100f)

        tt_layout.addView(textview2_tx)

        lt.addView(tt_layout, 1)


        findViewById<TextView>(R.id.text_lb_next_vote_screen).text = "Следующее голосование"


        val layout = findViewById<LinearLayout>(R.id.lst_players_layout_vote_scren)
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
            if (ind <= alive_size) btn.setTextColor(getColor(R.color.white))
            else btn.setTextColor(getColor(R.color.black))
            btn.setOnTouchListener(View.OnTouchListener(){_, motionEvent ->
                when(motionEvent.actionMasked){
                    MotionEvent.ACTION_DOWN -> {
                        btn.setTextColor(getColor(R.color.btn_is_selecting_lst_players))
                        true
                    }
                    MotionEvent.ACTION_UP -> {
                        if ((btn.width > motionEvent.getX() && motionEvent.getX() > 0) && (btn.height > motionEvent.getY() && motionEvent.getY() > 0)) {
                            if (this.players_data[ind-1].is_alive == true) {
                                if (this.voting_result[this.current_player] != 0) this.choizen_btn.setTextColor(getColor(R.color.white))
                                this.voting_result[this.current_player] = ind
                                textview2_tx.text = ind.toString()
                                this.choizen_btn = btn
                                btn.setTextColor(getColor(R.color.btn_selected_lst_players))
                            }else btn.setTextColor(getColor(R.color.black))
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

        var tx = TextView(this)
        tx.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4f, this.resources.displayMetrics).toInt())
        var params = tx.layoutParams as ViewGroup.MarginLayoutParams
        tx.layoutParams = params
        tx.setBackgroundColor(getColor(R.color.white))
        tx.text = ""

        layout.addView(tx)


        var res:LinearLayout = LinearLayout(this)
        res.orientation = LinearLayout.VERTICAL
        res.setBackgroundResource(R.drawable.bg_keyboard_lst_players)
        params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            1f
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
                            if (this.voting_result[this.current_player] != 0) this.choizen_btn.setTextColor(getColor(R.color.white))
                            this.voting_result[this.current_player] = 0
                            textview2_tx.text = "0"
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
                            if (this.voting_result[this.current_player] != 0) this.choizen_btn.setTextColor(getColor(R.color.white))
                            this.voting_result[this.current_player] = 0
                            textview2_tx.text = "0"
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

        layout.addView(res)

        val tx1 = TextView(this)
        tx1.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4f, this.resources.displayMetrics).toInt())
        tx1.setBackgroundColor(getColor(R.color.white))
        tx1.text = ""

        layout.addView(tx1)

        val next_btn = findViewById<ImageButton>(R.id.start_next_btn_vote_screen)
        next_btn.setOnTouchListener(View.OnTouchListener { _, event -> when(event.actionMasked){
            MotionEvent.ACTION_DOWN -> {
                next_btn.setImageResource(R.drawable.start_touched_anim)
                true
            }
            MotionEvent.ACTION_UP -> {
                next_btn.setImageResource(R.drawable.start)
                if ((next_btn.width > event.getX() && event.getX() > 0) && (next_btn.height > event.getY() && event.getY() > 0)){
                    var ind = this.voting_candidates.indexOf(this.current_player)

                    if (ind+1 < this.voting_candidates.size){
                        this.current_player = this.voting_candidates[ind+1]
                        popil()
                    } else {
                        var max=0
                        var who = mutableListOf<Int>()

                        for (el in this.voting_result.keys){
                            if (this.voting_result[el]!! > max){
                                max = this.voting_result[el]!!
                                who = mutableListOf(el)
                            } else if (this.voting_result[el]!! == max) who.add(el)
                        }

                        if (who.size > 1){
                            findViewById<LinearLayout>(R.id.lst_players_layout_vote_scren).removeViewAt(1)
                            findViewById<LinearLayout>(R.id.lst_players_layout_vote_scren).removeViewAt(1)
                            findViewById<LinearLayout>(R.id.lst_players_layout_vote_scren).removeViewAt(1)
                            this.voting_candidates = who
                            this.current_player = who[0]
                            val tt_layout = LinearLayout(this)
                            tt_layout.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 15f)
                            tt_layout.orientation = LinearLayout.VERTICAL

                            val textview = TextView(this)
                            textview.text = "Удалить всех?"
                            textview.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 3f)
                            textview.gravity = Gravity.CENTER
                            textview.setTextSize(25f)

                            tt_layout.addView(textview)

                            val ans_lt = LinearLayout(this)
                            ans_lt.orientation = LinearLayout.HORIZONTAL
                            ans_lt.gravity = Gravity.CENTER
                            ans_lt.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f)


                            val true_ans = ImageButton(this)
                            true_ans.setImageResource(R.drawable.like)
                            true_ans.setBackgroundColor(getColor(R.color.nothing))
                            true_ans.scaleType = ImageView.ScaleType.FIT_END
                            true_ans.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
                            true_ans.setPadding(0, 0, 20, 0)
                            true_ans.setOnClickListener {
                                this.current_player = -1
                                var ans_str = ""
                                this.voting_candidates.forEach { ans_str = ans_str + "№" + it.toString() +" "
                                    this.players_data[it-1].is_alive = false}
                                ans_str = ans_str + "\nвне игры"

                                make_vote_screen()

                                findViewById<TextView>(R.id.text_lb_next_vote_screen).text = "Начать завещание"

                                lt = findViewById(R.id.main_vote_layout_vote_screen)
                                lt.removeViewAt(1)
                                val tx = TextView(this)
                                tx.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 15f)
                                tx.setTextSize(45f)
                                tx.gravity = Gravity.CENTER
                                tx.text = ans_str
                                tx.setBackgroundColor(getColor(R.color.low_red))
                                tx.setTextColor(getColor(R.color.white))
                                lt.addView(tx, 1)

                                findViewById<ImageButton>(R.id.start_next_btn_vote_screen).setOnTouchListener(
                                    OnTouchListener { _, event -> when(event.actionMasked){
                                        MotionEvent.ACTION_DOWN -> {
                                            findViewById<ImageButton>(R.id.start_next_btn_vote_screen).setImageResource(R.drawable.start_touched_anim)
                                            true
                                        }
                                        MotionEvent.ACTION_UP -> {
                                            findViewById<ImageButton>(R.id.start_next_btn_vote_screen).setImageResource(R.drawable.start)
                                            if ((findViewById<ImageButton>(R.id.start_next_btn_vote_screen).width > event.getX() && event.getX() > 0) && (findViewById<ImageButton>(R.id.start_next_btn_vote_screen).height > event.getY() && event.getY() > 0)){
                                                this.voting_candidates.forEach { it-1 }
                                                this.killing_lst = this.voting_candidates
                                                this.current_player = this.voting_candidates[0]
                                                make_vote_screen()
                                                inizializate_voting_for_dead_voting_and_popil()
                                            }
                                            true
                                        }
                                        else -> {true}
                                    } })
                            }

                            ans_lt.addView(true_ans)

                            val false_ans = ImageButton(this)
                            false_ans.setImageResource(R.drawable.dislike)
                            false_ans.setBackgroundColor(getColor(R.color.nothing))
                            false_ans.scaleType = ImageView.ScaleType.FIT_START
                            false_ans.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
                            false_ans.setPadding(20, 0, 0, 0)
                            false_ans.setOnClickListener {
                                lt.removeViewAt(1)
                                val tx = TextView(this)
                                tx.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 15f)
                                tx.setTextSize(45f)
                                tx.gravity = Gravity.CENTER
                                tx.text = "Все остаются"
                                tx.setBackgroundColor(getColor(R.color.green))
                                tx.setTextColor(getColor(R.color.white))
                                lt.addView(tx, 1)

                                findViewById<TextView>(R.id.text_lb_next_vote_screen).setText("Завершить день")
                                findViewById<ImageButton>(R.id.start_next_btn_vote_screen).setOnTouchListener(View.OnTouchListener { v, event -> when(event.actionMasked){
                                    MotionEvent.ACTION_DOWN -> {
                                        findViewById<ImageButton>(R.id.start_next_btn_vote_screen).setImageResource(R.drawable.start_touched_anim)
                                        true
                                    }
                                    MotionEvent.ACTION_UP -> {
                                        findViewById<ImageButton>(R.id.start_next_btn_vote_screen).setImageResource(R.drawable.start)
                                        if ((findViewById<ImageButton>(R.id.start_next_btn_vote_screen).width > event.getX() && event.getX() > 0) && (findViewById<ImageButton>(R.id.start_next_btn_vote_screen).height > event.getY() && event.getY() > 0)) {
                                            when (who_will_win()){
                                                "red" -> win("red")
                                                "black" -> win("black")
                                                "maniac" -> win("maniac")
                                                else -> next_night()
                                            }
                                        }
                                        true
                                    }
                                    else -> {true}
                                } })
                            }

                            ans_lt.addView(false_ans)

                            tt_layout.addView(ans_lt)

                            lt.removeViewAt(1)

                            lt.addView(tt_layout, 1)
                            next_btn.setOnTouchListener { _, event -> when(event.actionMasked){
                                MotionEvent.ACTION_DOWN ->{
                                    next_btn.setImageResource(R.drawable.start_touched_anim)
                                    true
                                }
                                MotionEvent.ACTION_UP -> {
                                    next_btn.setImageResource(R.drawable.start)
                                    true
                                }
                                else -> {true}
                            } }
                        } else {
                            lt.removeViewAt(1)
                            val textview = TextView(this)
                            textview.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 15f)
                            textview.text = "№${who[0]}\nвне игры"
                            textview.setTextColor(getColor(R.color.white))
                            textview.setBackgroundColor(getColor(R.color.red))
                            textview.gravity = Gravity.CENTER
                            textview.setTextSize(50f)
                            lt.addView(textview, 1)
                            findViewById<LinearLayout>(R.id.lst_players_layout_vote_scren).removeView(tx)
                            findViewById<LinearLayout>(R.id.lst_players_layout_vote_scren).removeView(tx1)
                            findViewById<LinearLayout>(R.id.lst_players_layout_vote_scren).removeView(res)
                            findViewById<TextView>(R.id.text_lb_next_vote_screen).text = "Закончить день"
                            this.players_data[who[0]-1].is_alive = false
                            this.killing_lst = who

                            next_btn.setOnTouchListener { _, event -> when(event.actionMasked){
                                MotionEvent.ACTION_DOWN -> {
                                    next_btn.setImageResource(R.drawable.start_touched_anim)
                                    true
                                }
                                MotionEvent.ACTION_UP -> {
                                    next_btn.setImageResource(R.drawable.start)
                                    if ((next_btn.width > event.getX() && event.getX() > 0) && (next_btn.height > event.getY() && event.getY() > 0)){
                                        this.current_player = 15
                                        setContentView(R.layout.vote_screen)
                                        make_vote_screen()
                                        end_the_day()
                                    }
                                    true
                                }
                                else -> {true}
                            } }
                        }
                    }
                }
                true
            }
            else -> {true}
        } })
    }
    fun inizializate_voting_for_dead_voting_and_popil(){
        val textview = findViewById<TextView>(R.id.saying_lb_vote_screen)
        val btn_next = findViewById<ImageButton>(R.id.start_next_btn_vote_screen)
        val handler = Handler()
        var timer_was_started = false
        val stop = this.timer_to_say
        fun next_player(){val ind = this.killing_lst.indexOf(this.current_player+1)
            if (ind == this.killing_lst.size-1) {
                this.current_player = 0
                while (this.current_player < this.players_data.size && this.players_data[this.current_player].is_alive == false) this.current_player += 1
                end_the_day()
            }
            else{
                this.current_player = this.killing_lst[ind+1]-1
                make_vote_screen()
                inizializate_voting_for_dead_voting_and_popil()}
        }

        fun make_next_player_voice(runnable: Runnable){
            findViewById<TextView>(R.id.text_lb_next_vote_screen).setText("Закончить речь")
            runOnUiThread {textview.setTextSize(100f)
                textview.setBackgroundResource(R.drawable.timer_background)
                textview.text = this.timer_to_say.toString()}
            handler.postDelayed(runnable, 1000)
            timer_was_started = true
            btn_next.setOnTouchListener(View.OnTouchListener { v, event -> when(event.actionMasked){
                MotionEvent.ACTION_DOWN -> {
                    runOnUiThread{ btn_next.setImageResource(R.drawable.start_touched_anim) }
                    true
                }
                MotionEvent.ACTION_UP -> {
                    runOnUiThread { btn_next.setImageResource(R.drawable.start) }
                    if ((btn_next.width > event.getX() && event.getX() > 0) && (btn_next.height > event.getY() && event.getY() > 0)) {
                        handler.removeCallbacks(runnable)
                        timer_was_started = false
                        runOnUiThread {
                            textview.setTextSize(30f)
                            textview.setBackgroundColor(getColor(R.color.nothing))
                        }
                        val ind = this.killing_lst.indexOf(this.current_player+1)
                        if (ind == this.killing_lst.size-1) {
                            this.current_player = 0
                            while (this.current_player < this.players_data.size && this.players_data[this.current_player].is_alive == false) this.current_player += 1
                            end_the_day()
                        }
                        else{
                            this.current_player = this.killing_lst[ind+1]-1
                            make_vote_screen()
                            inizializate_voting_for_dead_voting_and_popil()}
                    }
                    true
                }
                else -> {true}
            }})
        }
        val runnable = object: Runnable {
            var i=0
            override fun run() {
                this.i += 1
                if (stop - i < 0){
                    textview.setTextSize(30f)
                    textview.setBackgroundColor(getColor(R.color.nothing))
                    next_player()
                    this.i = 0
                    val btn_end = findViewById<Button>(R.id.end_the_game_in_the_start_btn)
                    btn_end.setOnClickListener(View.OnClickListener { inizalizate_end_game_in_start()
                        findViewById<Button>(R.id.back_to_the_game_end_the_game_start).setOnClickListener(
                            View.OnClickListener { this.i = 0
                                pre_end_the_game_in_start()
                                val back_btn = findViewById<Button>(R.id.back_to_the_game_end_the_game_start)
                                back_btn.setOnClickListener({end_the_day()})
                                if (is_paused == false){
                                    make_next_player_voice(this)
                                    handler.removeCallbacks(this)
                                }})})
                } else {
                    textview.text = (stop-i).toString()
                    handler.postDelayed(this, 1000)
                }
            }
        }

        val exit_btn = findViewById<Button>(R.id.end_the_game_in_the_start_btn)
        exit_btn.setOnClickListener(OnClickListener {
            handler.removeCallbacks(runnable)
            this.is_paused = false
            pre_end_the_game_in_start()
            val back_btn = findViewById<Button>(R.id.back_to_the_game_end_the_game_start)
            back_btn.setOnClickListener({make_vote_screen()
                inizializate_voting_for_dead_voting_and_popil()}) })

        btn_next.setOnTouchListener(View.OnTouchListener { v, event -> when(event.actionMasked){
            MotionEvent.ACTION_DOWN -> {
                btn_next.setImageResource(R.drawable.start_touched_anim)
                true
            }
            MotionEvent.ACTION_UP -> {
                btn_next.setImageResource(R.drawable.start)
                if ((btn_next.width > event.getX() && event.getX() > 0) && (btn_next.height > event.getY() && event.getY() > 0)) {
                    make_next_player_voice(runnable)
                }
                true
            }
            else -> {true}
        } })
        val btn_pause = findViewById<ImageButton>(R.id.pause_btn_vote_screen)
        btn_pause.setOnClickListener(View.OnClickListener { when (this.is_paused) {
            false -> {
                btn_pause.setImageResource(R.drawable.play)
                this.is_paused = true
                if (timer_was_started == true) handler.removeCallbacks(runnable)
                true
            }
            true -> {
                btn_pause.setImageResource(R.drawable.pause)
                if (timer_was_started == true) handler.postDelayed(runnable, 1000)
                this.is_paused = false
                true
            }
        } })
    }
    fun win(who:String){
        setContentView(R.layout.win_screen)
        when (who){
            "red" -> {
                findViewById<ImageView>(R.id.win_img_win_screen).setImageResource(R.drawable.peacful_win)
                findViewById<TextView>(R.id.win_lb_win_screen).setText(R.string.red_win)
            }
            "black" -> {
                findViewById<ImageView>(R.id.win_img_win_screen).setImageResource(R.drawable.mafia_win)
                findViewById<TextView>(R.id.win_lb_win_screen).setText(R.string.black_win)
            }
            "maniac" -> {
                findViewById<ImageView>(R.id.win_img_win_screen).setImageResource(R.drawable.maniac_win)
                findViewById<TextView>(R.id.win_lb_win_screen).setText(R.string.maniac_win)
            }
        }
        val to_results_btn = findViewById<ImageButton>(R.id.to_results_win_screen)
        to_results_btn.setOnClickListener { show_results(who) }
    }
    fun show_results(who_win:String){
        setContentView(R.layout.vote_screen)
        when (who_win) {
            "black" -> findViewById<TextView>(R.id.saying_lb_vote_screen).setText("Команда Мафии\nпобедитель")
            "red" -> findViewById<TextView>(R.id.saying_lb_vote_screen).setText("Команда Мирных\nпобедитель")
            "maniac" -> findViewById<TextView>(R.id.saying_lb_vote_screen).setText("Команда Маньяка\nпобедитель")
        }

        val btn_pause = findViewById<ImageButton>(R.id.pause_btn_vote_screen)
        btn_pause.setOnClickListener(View.OnClickListener { when (this.is_paused) {
            false -> {
                btn_pause.setImageResource(R.drawable.play)
                this.is_paused = true
                true
            }
            true -> {
                btn_pause.setImageResource(R.drawable.pause)
                this.is_paused = false
                true
            }
        } })

        fun create_player_settings(context: Context, number: Int, name: String, result: TextView, save_res: OnClickListener){
            val notif_lt = LinearLayout(context)
            notif_lt.orientation = LinearLayout.VERTICAL
            notif_lt.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
            notif_lt.setBackgroundColor(getColor(R.color.background))

            val main_lt = LinearLayout(context)
            main_lt.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
            main_lt.orientation = LinearLayout.VERTICAL


            val txt1 = Button(context)
            txt1.setBackgroundColor(getColor(R.color.nothing))
            txt1.setOnClickListener(save_res)
            txt1.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f)

            val txt2 = Button(context)
            txt2.setBackgroundColor(getColor(R.color.nothing))
            txt2.setOnClickListener(save_res)
            txt2.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f)



            var tx = TextView(context)
            tx.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4f, this.resources.displayMetrics).toInt())
            var params = tx.layoutParams as ViewGroup.MarginLayoutParams
            tx.layoutParams = params
            tx.setBackgroundColor(getColor(R.color.white))
            tx.text = ""
            notif_lt.addView(tx)


            val name_tx = TextView(context)
            name_tx.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 3f)
            name_tx.text = "№${number} ${name}"
            name_tx.setTextColor(getColor(R.color.white))
            name_tx.gravity = Gravity.CENTER
            name_tx.setTextSize(30f)
            notif_lt.addView(name_tx)

            tx = TextView(this)
            tx.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4f, this.resources.displayMetrics).toInt())
            params = tx.layoutParams as ViewGroup.MarginLayoutParams
            params.leftMargin = 40
            params.rightMargin = 40
            tx.layoutParams = params
            tx.setBackgroundColor(getColor(R.color.white))
            tx.text = ""
            notif_lt.addView(tx)


            val settings_lt = LinearLayout(context)
            settings_lt.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 2f)
            params = settings_lt.layoutParams as ViewGroup.MarginLayoutParams
            params.leftMargin = 40
            params.rightMargin = 40
            settings_lt.layoutParams = params


            val lt1 = LinearLayout(context)
            lt1.orientation = LinearLayout.VERTICAL
            lt1.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 3f)

            val tx1 = TextView(context)
            tx1.gravity = Gravity.CENTER
            tx1.setText("Голос")
            tx1.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 3f)

            val btn1 = ImageButton(context)
            btn1.setBackgroundColor(getColor(R.color.nothing))
            btn1.setImageResource(R.drawable.like)
            btn1.scaleType = ImageView.ScaleType.FIT_CENTER
            btn1.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f)

            btn1.setOnClickListener(OnClickListener {
                var res:LinearLayout = LinearLayout(this)
                res.orientation = LinearLayout.VERTICAL
                res.setBackgroundResource(R.drawable.bg_keyboard_lst_players)
                params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT, 1f
                )
                params.rightMargin = 100
                params.leftMargin = 100
                params.topMargin = 50
                params.bottomMargin = 50
                res.layoutParams = params
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
                    if (this.players_data[ind-1].is_alive == false) btn.setTextColor(getColor(R.color.black))
                    else btn.setTextColor(getColor(R.color.white))
                    for (el in this.players_data) if (el.voices == ind) btn.setTextColor(getColor(R.color.btn_selected_lst_players))
                    btn.setOnTouchListener(View.OnTouchListener(){view, motionEvent ->
                        when(motionEvent.actionMasked){
                            MotionEvent.ACTION_DOWN -> {
                                btn.setTextColor(getColor(R.color.btn_is_selecting_lst_players))
                                true
                            }
                            MotionEvent.ACTION_UP -> {
                                if ((btn.width > motionEvent.getX() && motionEvent.getX() > 0) && (btn.height > motionEvent.getY() && motionEvent.getY() > 0)) {
                                    var is_was_in_other_player = false
                                    for (el in this.players_data) if (el.voices == ind) is_was_in_other_player = true
                                    if (is_was_in_other_player == false){
                                        this.players_data[number - 1].voices = ind
                                        notif_lt.removeView(res)
                                        txt2.layoutParams = LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                            1f
                                        )
                                        txt1.layoutParams = LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                            1f
                                        )
                                    } else btn.setTextColor(getColor(R.color.btn_selected_lst_players))
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
                    btn_clear.setOnTouchListener(View.OnTouchListener(){_, motionEvent ->
                        when(motionEvent.actionMasked){
                            MotionEvent.ACTION_DOWN ->{
                                btn_clear.setTextColor(getColor(R.color.cancel_keyboard_clicked_lst_players))
                                true
                            }
                            MotionEvent.ACTION_UP -> {
                                btn_clear.setTextColor(getColor(R.color.cancel_keyboard_lst_players))
                                if ((btn_clear.width > motionEvent.getX() && motionEvent.getX() > 0) && (btn_clear.height > motionEvent.getY() && motionEvent.getY() > 0)) {
                                    notif_lt.removeView(res)
                                    this.players_data[number-1].voices = 0
                                    txt2.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
                                    txt1.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
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
                txt2.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 2f)
                txt1.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 2f)
                notif_lt.addView(res)
            })

            lt1.addView(tx1)
            lt1.addView(btn1)

            settings_lt.addView(lt1)

            val lt2 = LinearLayout(context)
            lt2.orientation = LinearLayout.VERTICAL
            lt2.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 2f)

            val tx2 = TextView(context)
            tx2.gravity = Gravity.CENTER
            tx2.setText("Допы")
            tx2.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 3f)

            val btn2 = LinearLayout(context)
            btn2.orientation = LinearLayout.HORIZONTAL
            btn2.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f)



            val btn_left = Button(context)
            btn_left.setTextColor(getColor(R.color.white))
            btn_left.setText("<")
            btn_left.gravity = Gravity.CENTER
            btn_left.setTextSize(20f)
            btn_left.setBackgroundColor(getColor(R.color.nothing))
            btn_left.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
            btn_left.setOnClickListener(View.OnClickListener {
                this.players_data[number-1].extras -= this.step
                if (this.players_data[number-1].extras > 0) result.text = "+"
                else result.text = ""
                if (this.players_data[number-1].extras == this.players_data[number-1].extras.toInt().toFloat()) result.text = result.text.toString() + this.players_data[number-1].extras.toInt().toString()
                else result.text = result.text.toString() + this.players_data[number-1].extras.toString()
            })



            val btn_rigth = Button(context)
            btn_rigth.setTextColor(getColor(R.color.white))
            btn_rigth.setText(">")
            btn_rigth.gravity = Gravity.CENTER
            btn_rigth.setTextSize(20f)
            btn_rigth.setBackgroundColor(getColor(R.color.nothing))
            btn_rigth.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
            btn_rigth.setOnClickListener(View.OnClickListener {
                this.players_data[number-1].extras += this.step
                if (this.players_data[number-1].extras > 0) result.text = "+"
                else result.text = ""
                if (this.players_data[number-1].extras == this.players_data[number-1].extras.toInt().toFloat()) result.text = result.text.toString() + this.players_data[number-1].extras.toInt().toString()
                else result.text = result.text.toString() + this.players_data[number-1].extras.toString()
            })

            btn2.addView(btn_left)
            btn2.addView(result)
            btn2.addView(btn_rigth)


            lt2.addView(tx2)
            lt2.addView(btn2)

            settings_lt.addView(lt2)


            val lt3 = LinearLayout(context)
            lt3.orientation = LinearLayout.VERTICAL
            lt3.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 3f)

            val tx3 = TextView(context)
            tx3.gravity = Gravity.CENTER
            tx3.setText("Фолы")
            tx3.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 3f)

            val btn3 = ImageButton(context)
            btn3.setBackgroundColor(getColor(R.color.nothing))
            when (this.players_data[number-1].Fols) {
                0 -> btn3.setImageResource(R.drawable.zero_fols)
                1 -> btn3.setImageResource(R.drawable.one_fol)
                2 -> btn3.setImageResource(R.drawable.two_fols)
                3 -> btn3.setImageResource(R.drawable.three_fols)
                4 -> btn3.setImageResource(R.drawable.four_fols)
            }
            btn3.scaleType = ImageView.ScaleType.FIT_CENTER
            btn3.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
            btn3.setOnClickListener(OnClickListener {
                this.players_data[number-1].Fols = this.players_data[number-1].Fols%4+1-(this.players_data[number-1].Fols/4).toInt()
                when (this.players_data[number-1].Fols) {
                    0 -> {
                        btn3.setImageResource(R.drawable.zero_fols)
                        this.players_data[number-1].is_alive = true
                    }
                    1 -> {
                        btn3.setImageResource(R.drawable.one_fol)
                        this.players_data[number-1].is_alive = true
                    }
                    2 -> {
                        btn3.setImageResource(R.drawable.two_fols)
                        this.players_data[number-1].is_alive = true
                    }
                    3 -> {
                        btn3.setImageResource(R.drawable.three_fols)
                        this.players_data[number-1].is_alive = true
                    }
                    4 -> {btn3.setImageResource(R.drawable.four_fols)
                        this.players_data[number-1].is_alive = false}
                }
            })
            btn3.setOnLongClickListener {
                this.players_data[number-1].is_alive = false
                this.players_data[number-1].Fols = 4
                btn3.setImageResource(R.drawable.four_fols)
                true }

            lt3.addView(tx3)
            lt3.addView(btn3)

            settings_lt.addView(lt3)




            notif_lt.addView(settings_lt)

            tx = TextView(context)
            tx.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4f, this.resources.displayMetrics).toInt())
            params = tx.layoutParams as ViewGroup.MarginLayoutParams
            tx.layoutParams = params
            tx.setBackgroundColor(getColor(R.color.white))
            tx.text = ""
            notif_lt.addView(tx)




            main_lt.addView(txt1)
            main_lt.addView(notif_lt)
            main_lt.addView(txt2)

            findViewById<FrameLayout>(R.id.main_vote_layout_screen).addView(main_lt, 1)
        }
        fun createPlayerListItem(context: Context, playerNumber: Int, playerName: String, playerPosition: String, parentView: ViewGroup, is_alive:Boolean, is_selected:Boolean, dop: Float, fol:Int) {
            val linearLayout = LinearLayout(context)
            val params = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            params.bottomMargin = 5
            linearLayout.layoutParams = params
            linearLayout.orientation = LinearLayout.HORIZONTAL
            linearLayout.setBackgroundResource(R.drawable.lst_element) // Replace with your drawable
            linearLayout.setPadding(0, 0, 0, 0) //Added padding bottom



            // Player Number TextView
            val playerNumberTextView = TextView(context)
            playerNumberTextView.setTextSize(15f)
            playerNumberTextView.layoutParams = LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.MATCH_PARENT,
                4f
            )
            playerNumberTextView.gravity = Gravity.CENTER
            playerNumberTextView.textAlignment = View.TEXT_ALIGNMENT_CENTER
            playerNumberTextView.setBackgroundResource(R.drawable.btn_element) //Replace with your drawable
            if (who_win == "red" && this.players_data[playerNumber-1].role in listOf("Мирный", "Доктор", "Красотка", "Шериф")) playerNumberTextView.setTextColor(getColor(R.color.gold))
            else if (who_win == "black" && this.players_data[playerNumber-1].role in listOf("Дон", "Мафия")) playerNumberTextView.setTextColor(getColor(R.color.gold))
            else if (who_win == "maniac" && this.players_data[playerNumber-1].role == "Маньяк") playerNumberTextView.setTextColor(getColor(R.color.gold))
            else playerNumberTextView.setTextColor(getColor(R.color.white))
            playerNumberTextView.text = playerNumber.toString()

            linearLayout.addView(playerNumberTextView)
            val player_icon = ImageView(context)
            player_icon.layoutParams = LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.MATCH_PARENT,
                2f
            )
            when (playerPosition){
                "Мирный" -> player_icon.setImageResource(R.drawable.peacful_icon)
                "Маньяк" -> player_icon.setImageResource(R.drawable.maniac_icon)
                "Шериф" -> player_icon.setImageResource(R.drawable.sheriff_icon)
                "Красотка" -> player_icon.setImageResource(R.drawable.gorgeous_icon)
                "Доктор" -> player_icon.setImageResource(R.drawable.doctor_icon)
                "Мафия" -> player_icon.setImageResource(R.drawable.mafia_icon)
                "Дон" -> player_icon.setImageResource(R.drawable.don_icon)
                else -> player_icon.setImageResource(R.drawable.peacful_icon)
            }
            player_icon.foregroundGravity = Gravity.CENTER
            player_icon.scaleType = ImageView.ScaleType.FIT_CENTER
            player_icon.setBackgroundColor(getColor(R.color.nothing))
            linearLayout.addView(player_icon)



            // Player Name TextView
            val playerNameTextView = TextView(context)
            playerNameTextView.setTextSize(13f)
            playerNameTextView.layoutParams = LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                14f
            )
            playerNameTextView.setPadding(30, 10, 0, 10)
            playerNameTextView.text = playerName

            if (is_alive == false) playerNameTextView.setTextColor(getColor(R.color.black))

            linearLayout.addView(playerNameTextView)


            // Player Position TextView
            val playerPositionTextView = TextView(context)
            playerPositionTextView.setTextSize(15f)
            playerPositionTextView.layoutParams = LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                4f
            )
            if (this.players_data[playerNumber-1].voices != 0) playerPositionTextView.text = this.players_data[playerNumber-1].voices.toString()
            else playerPositionTextView.text = ""
            linearLayout.addView(playerPositionTextView)

            val playerPositionTextView2 = TextView(context)
            playerPositionTextView2.setTextSize(15f)
            playerPositionTextView2.layoutParams = LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                4f
            )
            if (dop > 0) playerPositionTextView2.setTextColor(getColor(R.color.green))
            else if (dop < 0) playerPositionTextView2.setTextColor(getColor(R.color.red))
            else playerPositionTextView2.setTextColor(getColor(R.color.btn_is_selecting_lst_players))
            if (dop == dop.toInt().toFloat()) playerPositionTextView2.text = dop.toInt().toString()
            else playerPositionTextView2.text = dop.toString()
            linearLayout.addView(playerPositionTextView2)

            val playerPositionTextView3 = TextView(context)
            playerPositionTextView3.setTextSize(15f)
            playerPositionTextView3.layoutParams = LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                4f
            )
            when (fol) {
                0 -> playerPositionTextView3.text = ""
                1 -> playerPositionTextView3.text = "|"
                2 -> {playerPositionTextView3.text = "||"
                    playerPositionTextView3.setTextColor(getColor(R.color.two_fols))}
                3 -> {playerPositionTextView3.text = "|||"
                    playerPositionTextView3.setTextColor(getColor(R.color.red))}
                4 -> {playerPositionTextView3.text = "||||"
                    playerPositionTextView3.setTextColor(getColor(R.color.red))}
            }
            linearLayout.addView(playerPositionTextView3)

            parentView.addView(linearLayout)
        }


        val layout = findViewById<LinearLayout>(R.id.lst_players_vote_screen)
        for ((i, el) in this.players_data.withIndex()){
            val result = TextView(this)
            result.setTextColor(getColor(R.color.white))
            result.setTextSize(20f)
            result.gravity = Gravity.CENTER
            if (this.players_data[i].extras > 0) result.text = "+"
            else result.text = ""
            if (this.players_data[i].extras == this.players_data[i].extras.toInt().toFloat()) result.text = result.text.toString() + this.players_data[i].extras.toInt().toString()
            else result.text = result.text.toString() + this.players_data[i].extras.toString()
            result.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f)

            fun save_res(result: TextView, number: Int ){
                findViewById<FrameLayout>(R.id.main_vote_layout_screen).removeViewAt(1)
                var ans = ""

                for (ii in result.text){
                    if (listOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.').contains(ii)) ans = ans + ii.toString()
                }
                when(result.text[0]){
                    '+' -> this.players_data[number].extras = ans.toFloat()
                    else -> this.players_data[number].extras = -ans.toFloat()
                }

                val layout = findViewById<LinearLayout>(R.id.lst_players_vote_screen)
                layout.removeAllViews()
                for ((ii, el) in this.players_data.withIndex()){
                    val result = TextView(this)
                    result.setTextColor(getColor(R.color.white))
                    result.setTextSize(20f)
                    result.gravity = Gravity.CENTER
                    if (this.players_data[ii].extras > 0) result.text = "+"
                    else result.text = ""
                    if (this.players_data[ii].extras == this.players_data[ii].extras.toInt().toFloat()) result.text = result.text.toString() + this.players_data[ii].extras.toInt().toString()
                    else result.text = result.text.toString() + this.players_data[ii].extras.toString()
                    result.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
                    createPlayerListItem(this, ii+1, el.name, el.role, layout, el.is_alive, ii == this.current_player, el.extras, el.Fols)
                    layout.getChildAt(layout.childCount-1).setOnClickListener(View.OnClickListener { create_player_settings(this, ii+1, el.name, result, OnClickListener {save_res(result, number)}) })
                }
            }

            createPlayerListItem(this, i+1, el.name, el.role, layout, el.is_alive, i == this.current_player, el.extras, el.Fols)
            layout.getChildAt(layout.childCount-1).setOnClickListener(View.OnClickListener { create_player_settings(this, i+1, el.name, result, OnClickListener {save_res(result, i)}) })
        }
    }
    fun pre_end_the_game_in_start(){
        setContentView(R.layout.end_the_game_in_the_start)

        this.current_player = 0

        val peaceful_win_btn = findViewById<ImageButton>(R.id.peacful_win_end_the_game_screen)
        peaceful_win_btn.setOnClickListener {
            if (this.current_player != 0) this.choizen_btn_img.setBackgroundColor(getColor(R.color.nothing))
            peaceful_win_btn.setBackgroundColor(Color.argb(50, 255, 255, 255))
            this.choizen_btn_img = peaceful_win_btn
            this.current_player = 1
        }

        val mafia_win_btn = findViewById<ImageButton>(R.id.mafia_win_end_the_game_screen)
        mafia_win_btn.setOnClickListener {
            if (this.current_player != 0) this.choizen_btn_img.setBackgroundColor(getColor(R.color.nothing))
            mafia_win_btn.setBackgroundColor(Color.argb(50, 255, 255, 255))
            this.choizen_btn_img = mafia_win_btn
            this.current_player = 2
        }

        val maniac_win_btn = findViewById<ImageButton>(R.id.maniac_win_end_the_game_screen)
        maniac_win_btn.setOnClickListener {
            if (this.current_player != 0) this.choizen_btn_img.setBackgroundColor(getColor(R.color.nothing))
            maniac_win_btn.setBackgroundColor(Color.argb(50, 255, 255, 255))
            this.choizen_btn_img = maniac_win_btn
            this.current_player = 3
        }

        val end_the_game_btn = findViewById<ImageButton>(R.id.end_the_game_btn)
        end_the_game_btn.setOnClickListener {
            if (this.current_player != 0){
                win(listOf("red", "black", "maniac")[this.current_player-1])
            }
        }
    }
}