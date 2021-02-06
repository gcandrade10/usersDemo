package com.example.usersdemo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class UserListViewModel(private val endpoints: Endpoints) : ViewModel() {

    private var page = 1

    private val disposable = CompositeDisposable()

    private val usersLiveData by lazy {
        val liveData = MutableLiveData<List<User>>()
        getUsers(liveData, page)
        return@lazy liveData
    }

    fun users(): LiveData<List<User>> = usersLiveData

    private fun getUsers(liveData: MutableLiveData<List<User>>, page: Int) {
        endpoints.getUsers(page).observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({ liveData.value = it.results }, {
                errorLiveData.value = Unit
            })
            .addTo(disposable)
    }

    val moreLiveData = MutableLiveData<List<User>>()

    fun loadMore() {
        page += 1
        getUsers(moreLiveData, page)
    }

    val errorLiveData = MutableLiveData<Unit>()

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }

    private fun Disposable.addTo(compositeDisposable: CompositeDisposable) = Unit.also { compositeDisposable.add(this) }


}