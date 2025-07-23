package uz.urinboydev.domproduct.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import uz.urinboydev.domproduct.app.api.ApiHelper
import uz.urinboydev.domproduct.app.models.User
import uz.urinboydev.domproduct.app.utils.Resource
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val apiHelper: ApiHelper
) : ViewModel() {

    private val _userProfile = MutableLiveData<Resource<User>>()
    val userProfile: LiveData<Resource<User>> = _userProfile

    private val _updateProfileStatus = MutableLiveData<Resource<User>>()
    val updateProfileStatus: LiveData<Resource<User>> = _updateProfileStatus

    fun getUserProfile(token: String) {
        viewModelScope.launch {
            _userProfile.postValue(Resource.Loading)
            try {
                val response = apiHelper.getApi().getProfile(token)
                if (response.isSuccessful) {
                    response.body()?.let { _userProfile.postValue(Resource.Success(it)) }
                } else {
                    _userProfile.postValue(Resource.Error(apiHelper.getErrorMessageFromRawBody(response.errorBody()?.string())))
                }
            } catch (e: Exception) {
                _userProfile.postValue(Resource.Error(e.message ?: "Tarmoq xatosi"))
            }
        }
    }

    fun updateUserProfile(token: String, user: User) {
        viewModelScope.launch {
            _updateProfileStatus.postValue(Resource.Loading)
            try {
                val response = apiHelper.getApi().updateProfile(token, user)
                if (response.isSuccessful) {
                    response.body()?.let { _updateProfileStatus.postValue(Resource.Success(it)) }
                } else {
                    _updateProfileStatus.postValue(Resource.Error(apiHelper.getErrorMessageFromRawBody(response.errorBody()?.string())))
                }
            } catch (e: Exception) {
                _updateProfileStatus.postValue(Resource.Error(e.message ?: "Tarmoq xatosi"))
            }
        }
    }
}