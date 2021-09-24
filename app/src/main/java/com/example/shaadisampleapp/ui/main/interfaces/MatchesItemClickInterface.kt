package com.example.shaadisampleapp.ui.main.interfaces

import com.example.shaadisampleapp.network.model.Results

interface MatchesItemClickInterface {
    fun onMatchStatusChange(result: Results?, status: String?, pos: Int)
}