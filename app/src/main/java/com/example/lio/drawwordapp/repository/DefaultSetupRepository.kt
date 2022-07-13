package com.example.lio.drawwordapp.repository

import android.content.Context
import com.example.lio.drawwordapp.R
import com.example.lio.drawwordapp.data.remote.api.SetupApi
import com.example.lio.drawwordapp.data.remote.ws.Room
import com.example.lio.drawwordapp.util.Resource
import com.example.lio.drawwordapp.util.checkForInternetConnection
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class DefaultSetupRepository @Inject constructor(
    private val setupApi: SetupApi,
    private val context: Context
) : SetupRepository {

    override suspend fun createRoom(room: Room): Resource<Unit> {
        if (!context.checkForInternetConnection()) {
            return Resource.Error(context.getString(R.string.error_internet_turned_off))
        }
        val response = try {
            setupApi.createRoom(room)
        } catch (e: HttpException) {
            return Resource.Error(context.getString(R.string.error_http))
        } catch (e: IOException) {
            return Resource.Error(context.getString(R.string.check_internet_connection))
        }

        return if (response.isSuccessful && response.body()?.successful == true) {
            Resource.Success(Unit)
        } else if (response.body()?.successful == false) {
            Resource.Error(response.body()!!.message!!)
        } else {
            Resource.Error(context.getString(R.string.error_unknown))
        }
    }

    override suspend fun getRooms(searchQuery: String): Resource<List<Room>> {
        if (!context.checkForInternetConnection()) {
            return Resource.Error(context.getString(R.string.error_internet_turned_off))
        }
        val response = try {
            setupApi.getRooms(searchQuery)
        } catch (e: HttpException) {
            return Resource.Error(context.getString(R.string.error_http))
        } catch (e: IOException) {
            return Resource.Error(context.getString(R.string.check_internet_connection))
        }

        return if (response.isSuccessful && response.body() != null) {
            Resource.Success(response.body()!!)
        } else {
            Resource.Error(context.getString(R.string.error_unknown))
        }
    }

    override suspend fun joinRoom(username: String, roomName: String): Resource<Unit> {
        if (!context.checkForInternetConnection()) {
            return Resource.Error(context.getString(R.string.error_internet_turned_off))
        }
        val response = try {
            setupApi.joinRoom(username, roomName)
        } catch (e: HttpException) {
            return Resource.Error(context.getString(R.string.error_http))
        } catch (e: IOException) {
            return Resource.Error(context.getString(R.string.check_internet_connection))
        }

        return if (response.isSuccessful && response.body()?.successful == true) {
            Resource.Success(Unit)
        } else if (response.body()?.successful == false) {
            Resource.Error(response.body()!!.message!!)
        } else {
            Resource.Error(context.getString(R.string.error_unknown))
        }
    }
}