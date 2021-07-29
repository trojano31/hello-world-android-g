package com.example.myvideocallapp

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import live.hms.video.error.HMSException
import live.hms.video.media.tracks.HMSTrack
import live.hms.video.sdk.HMSSDK
import live.hms.video.sdk.HMSUpdateListener
import live.hms.video.sdk.models.*
import live.hms.video.sdk.models.enums.HMSPeerUpdate
import live.hms.video.sdk.models.enums.HMSRoomUpdate
import live.hms.video.sdk.models.enums.HMSTrackUpdate

sealed class RoomState {
    object Loading : RoomState()
    data class Joined(val peers: List<HMSPeer>) : RoomState()
}

class VideoCallVm(authKey: String?, application: Application) : AndroidViewModel(application),
    HMSUpdateListener {

    private val _roomState = MutableLiveData<RoomState>(RoomState.Loading)
    val roomState: LiveData<RoomState> = _roomState

    private val TAG = "VideoCallVm"

    private val hmsSdk = HMSSDK
        .Builder(application)
        .build()

    init {
        if (authKey != null) {
            Log.d(TAG, "Joining with $authKey")
            hmsSdk.join(HMSConfig("name", authKey), this)
        }
    }

    override fun onError(error: HMSException) {
        Log.d(TAG, "Error $error")
    }

    override fun onJoin(room: HMSRoom) {
        // Room joined.
        _roomState.postValue(RoomState.Joined(room.peerList.asList()))
    }

    override fun onMessageReceived(message: HMSMessage) {
    }

    override fun onPeerUpdate(type: HMSPeerUpdate, peer: HMSPeer) {
//        Log.d(TAG, "Peer ${peer.name} -> $type")
        _roomState.postValue(RoomState.Joined(hmsSdk.getPeers().asList()))
    }

    override fun onRoleChangeRequest(request: HMSRoleChangeRequest) {
    }

    override fun onRoomUpdate(type: HMSRoomUpdate, hmsRoom: HMSRoom) {
    }

    override fun onTrackUpdate(type: HMSTrackUpdate, track: HMSTrack, peer: HMSPeer) {
        _roomState.postValue(
            RoomState.Joined(hmsSdk.getRemotePeers().asList())
        )
    }

}