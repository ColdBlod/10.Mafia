package com.gamegenius.Mafia

import android.widget.Button

data class Help_file(var num:Int, var btn:Button?)

data class Player(var name:String, var role:String, var voices:Int, var extras:Float, var Fols:Int, var is_alive:Boolean)

// Мирный, мафия, дон, шериф, красотка, маньяк, доктор
sealed class Card_kit{
    val fourteen: List<Int> = listOf(6, 3, 1, 1, 1, 1, 1)

    companion object {
        val fourteen: MutableList<Int> = mutableListOf(6, 3, 1, 1, 1, 1, 1)
        fun get_lst_of_cards(quality:Int):MutableList<Int>{
            when (quality){
                14 -> return fourteen
                else -> return mutableListOf()
            }
        }
    }
}

