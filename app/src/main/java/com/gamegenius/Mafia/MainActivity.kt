package com.gamegenius.Mafia

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import org.w3c.dom.Text
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    private var players: MutableList<Help_file> = mutableListOf<Help_file>(Help_file(-1, null),
        Help_file(-1, null),
        Help_file(-1, null), Help_file(-1, null), Help_file(-1, null), Help_file(-1, null), Help_file(-1, null), Help_file(-1, null), Help_file(-1, null), Help_file(-1, null), Help_file(-1, null), Help_file(-1, null), Help_file(-1, null), Help_file(-1, null))
    private var players_name: MutableList<String> = mutableListOf("Вагю", "Железная леди", "Тур де шале", "Мистер У", "Керамогранит", "Святой Отец", "Осмыслитель", "Иван Иваныч", "Тимбер", "Кассиопея", "Зануда", "Котофей", "Торт", "Рея")
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
                    println(this.current_player_selected_players_lst.toString())
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
        var ts = MutableList(this.players.size) {Player(this.players_name[it], "Noname", 0, 0, 0)}
        for (i in 0..this.players.size-1){
            if (this.players[i].num != -1) {
                ts[this.players[i].num - 1] = Player(this.players_name[i], "Noname", 0, 0, 0)
                ts[i] = Player(this.players_name[this.players[i].num-1], "Noname", 0, 0, 0)
            }
        }

        for (dt in ts){
            if (dt.name != "") this.players_data.add(dt)
        }

        this.players_name = this.players_data.map { it.name }.toMutableList()


        val roles: MutableList<Int> =
            Card_kit.get_lst_of_cards(14) //this.players_data.size)

        for (i in 0..this.players_data.size-1){
            var role:Int = Random.nextInt(roles.size.toInt())
            while (roles[role] == 0) {role = Random.nextInt(roles.size.toInt())}

            this.players_data[i].role = listOf("Мирный", "Мафия", "Дон", "Шериф", "Красотка", "Маньяк", "Доктор")[role]

            roles[role] -= 1
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
        btn_show_players.setOnClickListener(View.OnClickListener { inizializate_show_lst_players() })

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
            btn_show_players.setOnClickListener(View.OnClickListener { inizializate_show_lst_players() })

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
        println(this.current_player_selected_players_lst)
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

                    }
                }
                true
            }
            else -> {true}
        } })
    }
    fun inizializate_show_lst_players(){
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
                2f
            )
            playerNumberTextView.gravity = Gravity.CENTER
            playerNumberTextView.textAlignment = View.TEXT_ALIGNMENT_CENTER
            playerNumberTextView.setBackgroundResource(R.drawable.btn_element) //Replace with your drawable
            playerNumberTextView.setTextColor(getColor(R.color.white))
            playerNumberTextView.text = playerNumber
            linearLayout.addView(playerNumberTextView)


            // Player Name TextView
            val playerNameTextView = TextView(context)
            playerNameTextView.setTextSize(20f)
            playerNameTextView.layoutParams = LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                15f
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

        val btn_back = findViewById<Button>(R.id.check_roles_btn_players_lst)
        btn_back.setOnClickListener(View.OnClickListener {
            preshow_player()
        })
    }

    fun create
}