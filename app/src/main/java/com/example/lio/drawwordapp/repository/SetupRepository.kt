package com.example.lio.drawwordapp.repository

import com.example.lio.drawwordapp.data.remote.ws.Room
import com.example.lio.drawwordapp.util.Resource

interface SetupRepository {

    suspend fun createRoom(room: Room): Resource<Unit>

    suspend fun getRooms(searchQuery: String): Resource<List<Room>>

    suspend fun joinRoom(username: String, roomName: String): Resource<Unit>
}