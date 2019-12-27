package com.example.alohaandroid.ui.a_base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BaseViewModel : ViewModel() {

    private val disposables = CompositeDisposable()
    val loadingStatus: MutableLiveData<Boolean> = MutableLiveData()
    val errorMsg: MutableLiveData<Int> = MutableLiveData()
    val taskCompleted: MutableLiveData<Boolean> = MutableLiveData()

    fun addDisposable(disposable: Disposable) {
        disposables.add(disposable)
    }

    override fun onCleared() {
        disposables.clear()
    }
}