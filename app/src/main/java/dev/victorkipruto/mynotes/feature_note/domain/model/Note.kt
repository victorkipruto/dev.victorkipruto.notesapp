package dev.victorkipruto.mynotes.feature_note.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.victorkipruto.mynotes.ui.theme.Blue
import dev.victorkipruto.mynotes.ui.theme.BlueSmooth
import dev.victorkipruto.mynotes.ui.theme.BrightOrange
import dev.victorkipruto.mynotes.ui.theme.DeepPurple
import dev.victorkipruto.mynotes.ui.theme.GreenSmooth
import dev.victorkipruto.mynotes.ui.theme.LimeGreen
import dev.victorkipruto.mynotes.ui.theme.Orange
import dev.victorkipruto.mynotes.ui.theme.Purple40
import dev.victorkipruto.mynotes.ui.theme.SkyBlue
import dev.victorkipruto.mynotes.ui.theme.Yellow

@Entity
data class Note(val title:String,
                val content:String,
                val timestamp:Long,
                val color:Int,
                @PrimaryKey val id:Int?=null)
{
    companion object{
        val noteColors = listOf(Purple40, LimeGreen, Yellow, DeepPurple, Orange, Blue, BlueSmooth,
            SkyBlue, BrightOrange, GreenSmooth)
    }
}


class InvalidNoteException(message:String):Exception(message)