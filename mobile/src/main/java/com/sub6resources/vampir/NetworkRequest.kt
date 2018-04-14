package com.sub6resources.vampir

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.util.Log
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException

sealed class BasicNetworkState<T> {
    class Loading<T> : BasicNetworkState<T>()
    class Success<T>(val data: T) : BasicNetworkState<T>()
    class Error<T>(val message: String) : BasicNetworkState<T>()
}

fun <T, U> passThrough(first: T, second: U): MutableLiveData<U> = MutableLiveData<U>().apply { value = second }

infix fun <T, U> LiveData<BasicNetworkState<T>>.chainOnSuccess(next: (param: T) -> LiveData<BasicNetworkState<U>>): LiveData<BasicNetworkState<U>> {
    return Transformations.switchMap(this) {
        when (it) {
            is BasicNetworkState.Loading<T> -> passThrough(it, BasicNetworkState.Loading())
            is BasicNetworkState.Error<T> -> passThrough(it, BasicNetworkState.Error(it.message))
            is BasicNetworkState.Success<T> -> next(it.data)
        }
    }
}

class ExtrasHandler<T> {
    // force the implementation of methods. Don't worry, if you don't need an insert,
    // it won't be called

    var insertFunction: ((d: T) -> Unit)? = { Log.e("NETWORKEXTRAS", "You haven't implemented an insert function!!!") }
        private set

    var errorFunction: ((t: Throwable) -> BasicNetworkState.Error<T>) = {
        if(it is HttpException) {
            BasicNetworkState.Error("Error: ${it.code()}: ${it.message}")
        } else {
            BasicNetworkState.Error("Unknown Error ${it.message}")
        }

    }
        private set

    fun onError(error: (t: Throwable) -> BasicNetworkState.Error<T>) {
        errorFunction = error
    }

    fun insert(insert: (d: T) -> Unit) {
        insertFunction = insert
    }
}

inline fun <T> makeNetworkRequest(call: Single<T>, query: LiveData<T>? = null, handler: ExtrasHandler<T>.() -> Unit = {}): LiveData<BasicNetworkState<T>> {
    val mediator: MediatorLiveData<BasicNetworkState<T>> = MediatorLiveData()
    mediator.value = BasicNetworkState.Loading()
    val extrasHandler = ExtrasHandler<T>().apply { handler() }

    call.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
        if (query == null) {
            mediator.postValue(BasicNetworkState.Success(it))
        } else {
            extrasHandler.insertFunction?.invoke(it)
            mediator.addSource(query) { mediator.postValue(BasicNetworkState.Success(it!!)) }
        }
    }, {
        mediator.postValue(extrasHandler.errorFunction.invoke(it))
    })

    return mediator
}
