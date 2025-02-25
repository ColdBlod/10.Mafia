package com.gamegenius.Mafia

import android.widget.Button

data class Help_file(var num:Int, var btn:Button?)

data class Player(var name:String, var role:String, var voices:Int, var extras:Int, var Fols:Int)

// Мирный, мафия, дон, шериф, красотка, маньяк, доктор
sealed class Card_kit{
    val fourteen: List<Int> = listOf(7, 3, 1, 1, 1, 1, 0)

    companion object {
        val fourteen: List<Int> = listOf(7, 3, 1, 1, 1, 1, 0)
        fun get_lst_of_cards(quality:Int):List<Int>{
            when (quality){
                14 -> return fourteen
                else -> return listOf<Int>()
            }
        }
    }
}